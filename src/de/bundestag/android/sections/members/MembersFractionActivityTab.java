package de.bundestag.android.sections.members;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Intent;
import android.content.res.Configuration;
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

public class MembersFractionActivityTab extends BaseActivity {
	int y, x;
	private static Rect rect;
	private LinearLayout linearLayout = null;
	private int selectedSubmenu;

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
			setContentView(R.layout.popup_portrait);
		}
		// orientationCheck();
		Intent intent = getIntent();
		if (intent.getExtras() != null) {
			y = intent.getExtras().getInt("y");
			x = intent.getExtras().getInt("x");

			rect = (Rect) intent.getExtras().getParcelable("rect");
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
		LinearLayout.LayoutParams params = null;
		int heightPopup = 0;
		if (DataHolder.isLandscape) {
			heightPopup = AppConstant.LEFT_FRAGMENT_PORT + AppConstant.RIGHT_FRAGMENT_PORT;
			params = new LinearLayout.LayoutParams(DataHolder.calculatedScreenResolution, heightPopup - (heightPopup * 55 / 100) - 7);
		} else {
			heightPopup = (AppConstant.LEFT_FRAGMENT_LAND + AppConstant.RIGHT_FRAGMENT_LAND) - ((y + rect.bottom) * 3);

			params = new LinearLayout.LayoutParams(DataHolder.calculatedScreenResolution, heightPopup - (heightPopup * 55 / 100) - 5);
		}
		if (DataHolder.isLandscape) {
			params.setMargins(0, rect.right / 2, rect.right, 0);
			ImageView ivArrow = (ImageView) findViewById(R.id.arrow);
			LinearLayout.LayoutParams paramsArr = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

			paramsArr.setMargins(0, y - rect.right / 2, 0, 0);

			ivArrow.setLayoutParams(paramsArr);
		} else {
			params.setMargins(x, y + rect.bottom + 5, 0, 0);
		}
		linearLayout.setLayoutParams(params);

		TextView heading = (TextView) findViewById(R.id.popupHeading);
		heading.setText("Fraktion");
		// Matrix matrix = ivArrow.getImageMatrix();
		// matrix.postRotate(90);
		// ///
		ListView listView = (ListView) findViewById(R.id.listView);
		ArrayList<HashMap<String, Object>> fractions = MembersSubpageViewHelper.createMembersFractions(this);
		listView.setDivider(null);
		listView.setAdapter(new MembersFractionListAdapter(this, fractions));
		// ArrayList<HashMap<String, Object>> cities =
		// MembersSubpageViewHelperFragment.createMembersCities(this);
		//
		// listView.setAdapter(new MembersCityListAdapter(this, cities));
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();

				intent.setClass(MembersFractionActivityTab.this, MembersSubpageMembersActivity.class);
				DataHolder.rowDBSelectedIndex = 0;
				intent.putExtra("index", arg2);
				DataHolder.strHeading = "Land";
				intent.putExtra(MembersSubpageMembersActivity.KEY_SUB_PAGE, MembersSubpageMembersActivity.SUB_PAGE_FRACTION);
				intent.putExtra("selectedSubmenu", selectedSubmenu);
				startActivity(intent);
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
			setResult(RESULT_OK, intent);
			finish();
			return true;
		}

		return super.onKeyDown(keyCode, event);
	}

}
