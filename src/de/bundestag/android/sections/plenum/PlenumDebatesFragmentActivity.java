package de.bundestag.android.sections.plenum;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import de.bundestag.android.BaseFragment;
import de.bundestag.android.R;

public class PlenumDebatesFragmentActivity extends BaseFragment {
	public static ArrayList<HashMap<String, Object>> debates;
	private boolean isInFront;
	private Timer waitTimer;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		waitTimer = new Timer();
		waitTimer.scheduleAtFixedRate(new TimerTask() {
			@Override
			public void run() {
				TimerMethod();
			}

		}, 10000, 10000);

		return inflater.inflate(R.layout.plenum_debates, container, false);
	}

	@Override
	public void onDetach() {
		waitTimer.cancel();
		isInFront = false;
		super.onDetach();
	}

	@Override
	public void onAttach(Activity activity) {
		isInFront = true;
		super.onAttach(activity);
	}

	public void TimerMethod() {
		// called every 300 milliseconds, which could be used to
		// send messages or some other action
		if (isInFront) {
			// Intent intent = new Intent();
			// intent.setClass(this, PlenumDebatesFragmentActivity.class);
			// startActivity(intent);
			// this.overridePendingTransition(0, 0);
			// Log.i("timer",
			// "################## PlenumDebatesActivity refresh ");
		}
	}

	@Override
	protected void finalize() throws Throwable {
		super.finalize();
		debates = null;
		waitTimer = null;
	}
}
