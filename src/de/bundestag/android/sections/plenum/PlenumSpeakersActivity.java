package de.bundestag.android.sections.plenum;

import java.util.Timer;
import java.util.TimerTask;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import de.bundestag.android.BaseActivity;
import de.bundestag.android.R;
import de.bundestag.android.constant.AppConstant;
import de.bundestag.android.constant.DataHolder;

public class PlenumSpeakersActivity extends BaseActivity {

	private boolean isInFront;
	private Timer waitTimer;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState, this);
		setContentView(R.layout.plenum_speaker);

		waitTimer = new Timer();

		waitTimer.scheduleAtFixedRate(new TimerTask() {
			@Override
			public void run() {
				TimerMethod();
			}

		}, 10000, 10000);
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
		Log.i("timer onCreate", "################## onCreate ");

	}

	protected void onResume() {
		super.onResume();
		if (!AppConstant.isFragmentSupported) {

			isInFront = true;
		} else {
			try {
				DataHolder.generalListFragmentTab.startTimer();
			} catch (Exception e) {
			}
		}

	}

	protected void onPause() {
		super.onPause();
		try {
			waitTimer.cancel();
		} catch (Exception e) {
		}
		if (!AppConstant.isFragmentSupported) {
			isInFront = false;
		} else {
			try {
				DataHolder.generalListFragmentTab.stopTimer();
				PlenumSittingDetailFragment tmp = (PlenumSittingDetailFragment) getSupportFragmentManager().findFragmentById(R.id.detailsFragment);
				tmp.agendaTimer.cancel();
				tmp.agendaTimer = null;
			} catch (Exception e) {
			}
		}
	}

	public void TimerMethod() {

		if (isInFront) {
			Intent intent = new Intent();
			intent.setClass(this, PlenumSpeakersActivity.class);
			startActivity(intent);
			this.overridePendingTransition(0, 0);
			Log.i("timer", "################## PlenumSpeakersActivity refresh ");
		}
	}

	/**
	 * Hack to handle the back button.
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (!AppConstant.isFragmentSupported) {
				Intent intent = new Intent();
				intent.setClass(this, PlenumNewsActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				this.startActivity(intent);
				overridePendingTransition(0, 0);
				return true;
			}
		}

		return super.onKeyDown(keyCode, event);
	}
}
