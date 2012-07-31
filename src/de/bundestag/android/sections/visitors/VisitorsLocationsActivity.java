package de.bundestag.android.sections.visitors;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import de.bundestag.android.BaseActivity;
import de.bundestag.android.R;
import de.bundestag.android.constant.AppConstant;
import de.bundestag.android.constant.DataHolder;

public class VisitorsLocationsActivity extends BaseActivity {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState, this);
		setContentView(R.layout.visitors_locations);
		if (AppConstant.isFragmentSupported) {
			DataHolder.dismissProgress();
		}
	}

	/**
	 * Hack to handle the back button.
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			Intent intent = new Intent();
			intent.setClass(this, VisitorsOffersActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			this.startActivity(intent);
			overridePendingTransition(0, 0);
			return true;
		}

		return super.onKeyDown(keyCode, event);
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		// DataHolder.dismissProgress();
	}

}
