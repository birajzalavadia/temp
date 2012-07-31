package de.bundestag.android.sections.news;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Display;
import android.view.KeyEvent;
import de.bundestag.android.BaseActivity;
import de.bundestag.android.R;
import de.bundestag.android.constant.AppConstant;
import de.bundestag.android.constant.DataHolder;
import de.bundestag.android.storage.NewsDatabaseAdapter;
import de.bundestag.android.synchronization.SynchronizeCheckNewsTask;
import de.bundestag.android.synchronization.SynchronizeNewsTask;

public class NewsActivity extends BaseActivity {
	public static final String SHOW_NEWS_KEY = "SHOW_NEWS";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState, this);

		DataHolder.currentActiviry = this;
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

		if (DataHolder.isOnline(this)) {
			boolean showNews = false;
			Intent launchingIntent = getIntent();
			Bundle extras = launchingIntent.getExtras();
			if (extras != null) {
				showNews = extras.containsKey(SHOW_NEWS_KEY);
			}

			if (showNews) {
				setContentView(R.layout.news);
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
			} else {
				checkNewsNeedsUpdating();
			}

		} else {
			setContentView(R.layout.news_offline);
			// Intent intent = new Intent();
			// intent.setClass(this, MembersListActivity.class);
			// startActivity(intent);
		}

		setProgressBarIndeterminateVisibility(false);

	}

	/**
	 * Hack to handle the back button for news list.
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (DataHolder.isSublistLoaded && AppConstant.isFragmentSupported) {
				Intent intent = new Intent();
				intent.setClass(this, NewsActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent);
				return true;
			} else {
				moveTaskToBack(true);
				DataHolder._id = -1;
				return true;
			}
		}

		return super.onKeyDown(keyCode, event);
	}

	/**
	 * Check if the news needs updating.
	 */
	private void checkNewsNeedsUpdating() {
		NewsDatabaseAdapter newsDatabaseAdapter = new NewsDatabaseAdapter(this);
		newsDatabaseAdapter.open();
		boolean existNews = newsDatabaseAdapter.existNews();
		newsDatabaseAdapter.close();

		if (existNews) {
			SynchronizeCheckNewsTask synchronizeCheckNewsTask = new SynchronizeCheckNewsTask();
			synchronizeCheckNewsTask.execute(this);
		} else {
			SynchronizeNewsTask newsTask = new SynchronizeNewsTask();
			newsTask.execute(this);
		}
	}

}
