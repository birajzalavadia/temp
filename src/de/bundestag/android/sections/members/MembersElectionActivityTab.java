package de.bundestag.android.sections.members;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.Gravity;
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

public class MembersElectionActivityTab extends BaseActivity {
	int y, x;
	Rect rect;
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
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
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
			params = new LinearLayout.LayoutParams(DataHolder.calculatedScreenResolution, heightPopup - (heightPopup / 5) - 11);
		} else {
			heightPopup = (AppConstant.LEFT_FRAGMENT_LAND + AppConstant.RIGHT_FRAGMENT_LAND) - (((y + rect.bottom) * (27 / 10)));

			params = new LinearLayout.LayoutParams(DataHolder.calculatedScreenResolution, heightPopup - 19);
		}
		if (DataHolder.isLandscape) {
			params.setMargins(0, rect.right / 2, rect.right, 0);
			ImageView ivArrow = (ImageView) findViewById(R.id.arrow);
			LinearLayout.LayoutParams paramsArr = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

			paramsArr.setMargins(0, y - rect.right / 2, 0, 0);
			ivArrow.setLayoutParams(paramsArr);
		} else {
			// int marginX =0;
			// int
			// screenW=getWindow().getWindowManager().getDefaultDisplay().getWidth();
			// if(x+DataHolder.calculatedScreenResolution>screenW){
			// marginX = x+DataHolder.calculatedScreenResolution
			// -screenW-ivArrow.getBackground().getIntrinsicWidth()-20;
			// }
			// params.setMargins(x-marginX, y + rect.bottom+5, 0, 0);
			((LinearLayout) findViewById(R.id.main_layout)).setGravity(Gravity.RIGHT);
			params.setMargins(0, y + rect.bottom + 5, 0, 0);

		}
		linearLayout.setLayoutParams(params);

		TextView heading = (TextView) findViewById(R.id.popupHeading);
		heading.setText("Wahlkreis");
		ListView listView = (ListView) findViewById(R.id.listView);
		ArrayList<HashMap<String, Object>> elections = MembersSubpageViewHelperFragment.createMembersElections(this);
		listView.setDivider(null);
		listView.setAdapter(new MembersElectionListAdapter(this, elections));
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();

				intent.setClass(MembersElectionActivityTab.this, MembersSubpageMembersActivity.class);
				DataHolder.rowDBSelectedIndex = 0;
				intent.putExtra(MembersSubpageMembersActivity.KEY_SUB_PAGE, MembersSubpageMembersActivity.SUB_PAGE_ELECTION);
				intent.putExtra("index", arg2);
				intent.putExtra("selectedSubmenu", selectedSubmenu);
				DataHolder.strHeading = "Wahlkreis";
				// intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
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

	@Override
	protected void finalize() throws Throwable {
		super.finalize();
		rect = null;
		linearLayout = null;
	}
}
