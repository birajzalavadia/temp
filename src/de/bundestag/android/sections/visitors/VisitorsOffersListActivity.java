package de.bundestag.android.sections.visitors;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import de.bundestag.android.BaseActivity;
import de.bundestag.android.R;

public class VisitorsOffersListActivity extends BaseActivity {
	public static final String KEY_LIST_ID = "LIST_ID";

	public int listId;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState, this);

		Intent launchingIntent = getIntent();
		Bundle extras = launchingIntent.getExtras();
		this.listId = (Integer) extras.get(KEY_LIST_ID);
		setContentView(R.layout.visitors_offers);
	}

	/**
	 * Hack to handle the back button for offers sub list.
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
}
