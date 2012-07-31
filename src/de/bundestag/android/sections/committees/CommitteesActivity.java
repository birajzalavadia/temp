package de.bundestag.android.sections.committees;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.KeyEvent;
import de.bundestag.android.BaseActivity;
import de.bundestag.android.R;
import de.bundestag.android.constant.AppConstant;
import de.bundestag.android.constant.DataHolder;
import de.bundestag.android.sections.news.NewsActivity;
import de.bundestag.android.sections.news.NewsActivityTablet;

public class CommitteesActivity extends BaseActivity {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState, this);
		if(AppConstant.isFragmentSupported){
			DataHolder.dismissProgress();
			}
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
		setContentView(R.layout.committees);
	}

	@Override
	protected void onStart() {
		super.onStart();

		checkShowOptionsMenu(this);
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
