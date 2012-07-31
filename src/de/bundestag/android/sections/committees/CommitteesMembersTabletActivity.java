package de.bundestag.android.sections.committees;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import de.bundestag.android.BaseActivity;
import de.bundestag.android.R;
import de.bundestag.android.constant.AppConstant;
import de.bundestag.android.constant.DataHolder;
import de.bundestag.android.parser.objects.CommitteesObject;
import de.bundestag.android.parser.objects.MembersDetailsObject;
import de.bundestag.android.sections.members.MembersFractionCommitteeAdapter;
import de.bundestag.android.sections.members.MembersFractionListAdapter;

public class CommitteesMembersTabletActivity extends BaseActivity {
	int y, x;
	private static Rect rect;
	private LinearLayout linearLayout = null;
	private int selectedSubmenu;
	public int committeesId = 0;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState, this);

		if (android.content.res.Configuration.ORIENTATION_LANDSCAPE == getResources().getSystem().getConfiguration().orientation) {
			DataHolder.isLandscape = true;
		} else {
			DataHolder.isLandscape = false;
		}
		if (DataHolder.isLandscape) {
			setContentView(R.layout.popup_landscape);
		} else {
			setContentView(R.layout.popup_portrait_committee);
		}
		// orientationCheck();
		Intent intent = getIntent();
		if (intent.getExtras() != null) {
			y = intent.getExtras().getInt("y");
			x = intent.getExtras().getInt("x");

			rect = (Rect) intent.getExtras().getParcelable("rect");
			committeesId = intent.getExtras().getInt("index");
			selectedSubmenu = intent.getExtras().getInt("selectedSubmenu");
		}

		((LinearLayout) findViewById(R.id.main_layout)).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = getIntent();
				intent.putExtra("selectedSubmenu", selectedSubmenu);
				setResult(RESULT_OK, intent);
				finish();
			}
		});
		linearLayout = (LinearLayout) findViewById(R.id.popUp_layout);
		linearLayout.setVisibility(View.VISIBLE);
		linearLayout.setBackgroundColor(Color.TRANSPARENT);
		LinearLayout.LayoutParams params = null;
		int heightPopup = 0;
		if (DataHolder.isLandscape) {
			heightPopup = AppConstant.LEFT_FRAGMENT_PORT + AppConstant.RIGHT_FRAGMENT_PORT;
			params = new LinearLayout.LayoutParams(DataHolder.calculatedScreenResolution, heightPopup - (heightPopup * 53 / 100) - 3);
		} else {
			heightPopup = (AppConstant.LEFT_FRAGMENT_LAND + AppConstant.RIGHT_FRAGMENT_LAND) - ((y + rect.bottom) * 3);
			params = new LinearLayout.LayoutParams(DataHolder.calculatedScreenResolution, heightPopup - (heightPopup * 51 / 100) - 2);
		}
		if (DataHolder.isLandscape) {
			params.setMargins(0, rect.right, rect.right, 0);
			ImageView ivArrow = (ImageView) findViewById(R.id.arrow);
			LinearLayout.LayoutParams paramsArr = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

			paramsArr.setMargins(0, y - rect.right, 0, 0);

			ivArrow.setLayoutParams(paramsArr);
		} else {
			params.setMargins(x + (rect.right >> 1) - (DataHolder.calculatedScreenResolution >> 1), y + (rect.bottom + 5), 0, 0);
		}
		linearLayout.setLayoutParams(params);

		TextView heading = (TextView) findViewById(R.id.popupHeading);
		heading.setText("Fraktionen der Mitglieder");
		// Matrix matrix = ivArrow.getImageMatrix();
		// matrix.postRotate(90);
		// ///
		final ListView listView = (ListView) findViewById(R.id.listView);
		final CommitteesObject committeesObject = CommitteesDetailsMembersViewHelper.getCommittee(this, committeesId);

		// get members belonging to this committee

		int committeeMembersCount = CommitteesDetailsMembersViewHelper.getCommitteeMembersCount(this, committeesObject.getId());
		// Log.e("COMITEE COUNT","" + committeeMembersCount);

		// get the president
		List<MembersDetailsObject> presidents = CommitteesDetailsMembersViewHelper.getPresident(this, committeesObject.getId());

		// get the sub president
		List<MembersDetailsObject> subPresidents = CommitteesDetailsMembersViewHelper.getSubPresidents(this, committeesObject.getId());

		// create the fractions with members for each in this committee
		ArrayList<HashMap<String, Object>> fractions = CommitteesDetailsMembersViewHelper.createMembersFractions(this, committeesObject.getId());
		listView.setDivider(null);
		listView.setAdapter(new MembersFractionCommitteeAdapter(this, fractions, presidents, subPresidents, committeesObject, committeeMembersCount));
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				// TODO Auto-generated method stub
				HashMap<String, Object> hashMap = (HashMap<String, Object>) listView.getItemAtPosition(arg2);
				String fractionName = (String) hashMap.get(MembersFractionListAdapter.KEY_FRACTION_NAME);
				DataHolder.rowDBSelectedIndex = 0;
				Intent intent = new Intent();
				intent.putExtra("index", arg2);
				intent.putExtra("committeesId", committeesId);
				intent.putExtra("fractionName", fractionName);
				intent.putExtra("selectedSubmenu", selectedSubmenu);
				DataHolder.committeeIdOnBack=committeesId;
				DataHolder.fractionNameOnBack=fractionName;
				DataHolder.selectedSubMenuOnBack=selectedSubmenu;
				intent.setClass(CommitteesMembersTabletActivity.this, CommitteesDetailsMembersByFractionActivity.class);
				// intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent);
				// setResult(RESULT_CANCELED, intent);
				finish();
			}

		});

	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		// DataHolder.dismissProgress();
		DataHolder.releaseScreenLock(this);
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		// linearLayout.setVisibility(View.GONE);
		setResult(RESULT_CANCELED);
		finish();
	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			Intent intent = getIntent();
			intent.putExtra("selectedSubmenu", selectedSubmenu);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			setResult(RESULT_OK, intent);
			finish();
			return true;
		}

		return super.onKeyDown(keyCode, event);
	}

	@Override
	protected void finalize() throws Throwable {
		super.finalize();
		rect = null;
		linearLayout = null;
	}

}
