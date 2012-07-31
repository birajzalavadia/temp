package de.bundestag.android.sections.visitors;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.KeyEvent;
import de.bundestag.android.BaseActivity;
import de.bundestag.android.R;
import de.bundestag.android.constant.AppConstant;
import de.bundestag.android.constant.DataHolder;
import de.bundestag.android.fragments.GeneralDetailsFragment;
import de.bundestag.android.parser.objects.VisitorsArticleObject;
import de.bundestag.android.storage.VisitorsDatabaseAdapter;

public class VisitorsContactActivity extends BaseActivity {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState, this);
		if (AppConstant.isFragmentSupported) {
			DataHolder.dismissProgress();
		}

		setContentView(R.layout.visitors_contact);

		VisitorsArticleObject contactDetailsObject = createContactDetailsObject();

		GeneralDetailsFragment detailsFragment = (GeneralDetailsFragment) getSupportFragmentManager().findFragmentById(R.id.detailsFragment);
		detailsFragment.showVisitorsContactDetails(contactDetailsObject);
	}

	private VisitorsArticleObject createContactDetailsObject() {
		VisitorsDatabaseAdapter visitorsDatabaseAdapter = new VisitorsDatabaseAdapter(this);
		visitorsDatabaseAdapter.open();

		Cursor visitorContactCursor = visitorsDatabaseAdapter.fetchVisitorsContactDetails();

		VisitorsArticleObject visitorsArticleObject = new VisitorsArticleObject();
		if ((visitorContactCursor != null) && (visitorContactCursor.getCount() > 0)) {
			visitorsArticleObject.setTitle(visitorContactCursor.getString(visitorContactCursor.getColumnIndex(VisitorsDatabaseAdapter.KEY_ARTICLE_TITLE)));
			visitorsArticleObject.setImageString(visitorContactCursor.getString(visitorContactCursor.getColumnIndex(VisitorsDatabaseAdapter.KEY_ARTICLE_IMAGESTRING)));
			visitorsArticleObject.setImageCopyright(visitorContactCursor.getString(visitorContactCursor.getColumnIndex(VisitorsDatabaseAdapter.KEY_ARTICLE_IMAGECOPYRIGHT)));
			visitorsArticleObject.setText(visitorContactCursor.getString(visitorContactCursor.getColumnIndex(VisitorsDatabaseAdapter.KEY_ARTICLE_TEXT)));

			// WebView wv = (WebView) this.findViewById(R.id.text);
			// wv.setBackgroundColor(0);

		}
		DataHolder.releaseScreenLock(this);
		visitorContactCursor.close();
		visitorsDatabaseAdapter.close();

		return visitorsArticleObject;
	}

	/**
	 * Hack to handle the back button.
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {

			Intent intent = new Intent();
			if (!AppConstant.isFragmentSupported) {
				intent.setClass(this, VisitorsOffersActivity.class);
			} else {
				intent.setClass(this, VisitorsOffersActivityTablet.class);
			}
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
