package de.bundestag.android.sections.impressum;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import de.bundestag.android.BaseActivity;
import de.bundestag.android.R;
import de.bundestag.android.constant.DataHolder;
import de.bundestag.android.fragments.GeneralDetailsFragment;
import de.bundestag.android.sections.visitors.VisitorsOffersActivityTablet;

public class ImpressumActivity extends BaseActivity {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState, this);
		setContentView(R.layout.impressum);

		GeneralDetailsFragment detailsFragment = (GeneralDetailsFragment) getSupportFragmentManager().findFragmentById(R.id.detailsFragment);

		detailsFragment.showImpressum();
		DataHolder.releaseScreenLock(this);
	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (DataHolder.isVisitors) {
				DataHolder.isVisitors = false;
				Intent intent = new Intent();
				intent.setClass(this, VisitorsOffersActivityTablet.class);
				this.startActivity(intent);
				overridePendingTransition(0, 0);
				return true;
			} else {
				return super.onKeyDown(keyCode, event);
			}
		}

		return super.onKeyDown(keyCode, event);
	}

}
