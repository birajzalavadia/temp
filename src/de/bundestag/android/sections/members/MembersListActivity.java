package de.bundestag.android.sections.members;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Display;
import android.view.KeyEvent;
import android.view.View;
import de.bundestag.android.BaseActivity;
import de.bundestag.android.R;
import de.bundestag.android.constant.AppConstant;
import de.bundestag.android.constant.DataHolder;
import de.bundestag.android.sections.news.NewsActivity;
import de.bundestag.android.sections.news.NewsActivityTablet;

public class MembersListActivity extends BaseActivity {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState, this);
		if(AppConstant.isFragmentSupported){
			DataHolder.dismissProgress();
			}
		setContentView(R.layout.members_list);
//		DataHolder.mLockScreenRotation(this);
		SharedPreferences settings = getSharedPreferences("Identify", 0);
		SharedPreferences.Editor editor = settings.edit();
		if (settings.getInt("LeftFragmentSize", -1) != -1) {
			AppConstant.isFragmentSupported = settings.getBoolean("FragmentSupport", false);
			DataHolder.calculatedScreenResolution = settings.getInt("LeftFragmentSize", -1);
		} else {
			editor.putBoolean("FragmentSupport", AppConstant.isFragmentSupported);
			editor.putInt("LeftFragmentSize", DataHolder.calculatedScreenResolution);
			editor.commit();
		}

		Display display = getWindowManager().getDefaultDisplay();
		if (display.getWidth() < display.getHeight()) {
			AppConstant.LEFT_FRAGMENT_LAND = display.getHeight() * 40 / 100;
			AppConstant.LEFT_FRAGMENT_PORT = display.getWidth() * 40 / 100;
			AppConstant.RIGHT_FRAGMENT_LAND = display.getHeight() * 60 / 100;
			AppConstant.RIGHT_FRAGMENT_PORT = display.getWidth() * 60 / 100;
		} else {
			AppConstant.LEFT_FRAGMENT_PORT = display.getHeight() * 40 / 100;
			AppConstant.LEFT_FRAGMENT_LAND = display.getWidth() * 40 / 100;
			AppConstant.RIGHT_FRAGMENT_PORT = display.getHeight() * 60 / 100;
			AppConstant.RIGHT_FRAGMENT_LAND = display.getWidth() * 60 / 100;
		}
		if (this.getResources().getConfiguration().orientation == android.content.res.Configuration.ORIENTATION_LANDSCAPE) {
			DataHolder.listFragmentWidth = AppConstant.LEFT_FRAGMENT_LAND;
			DataHolder.detailFragmentWidth = AppConstant.RIGHT_FRAGMENT_LAND;
			DataHolder.isLandscape = true;
		} else {
			DataHolder.listFragmentWidth = AppConstant.LEFT_FRAGMENT_PORT;
			DataHolder.detailFragmentWidth = AppConstant.RIGHT_FRAGMENT_PORT;
			DataHolder.isLandscape = false;
		}
		if (AppConstant.isFragmentSupported) {
			try {
				View lineId = (View) findViewById(R.id.lineId);
				lineId.setVisibility(View.GONE);
			} catch (Exception e) {

			}
		}
		// Fragment f = getSupportFragmentManager().findFragmentById(
		// R.id.detailsFragment);
		// if (f != null) {
		// AppConstant.isFragmentSupported = true;
		// }
		
	}

	@Override
	protected void onStart() {
		super.onStart();

		checkShowOptionsMenu(this);
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}

	/**
	 * Hack to handle the back button.
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			Intent intent = new Intent();
			if(AppConstant.isFragmentSupported)
				intent.setClass(this, NewsActivityTablet.class);
			else
				intent.setClass(this, NewsActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			this.startActivity(intent);
			overridePendingTransition(0, 0);
			return true;
		}

		return super.onKeyDown(keyCode, event);
	}
}
