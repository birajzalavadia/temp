package de.bundestag.android.sections.visitors;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.KeyEvent;
import de.bundestag.android.BaseActivity;
import de.bundestag.android.R;
import de.bundestag.android.fragments.GeneralDetailsFragment;
import de.bundestag.android.parser.objects.VisitorsArticleObject;
import de.bundestag.android.storage.VisitorsDatabaseAdapter;

public class VisitorsLocationsDetailsActivity extends BaseActivity {
	public Integer listId;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState, this);

		setContentView(R.layout.news_detail);

		Intent launchingIntent = getIntent();
		Bundle extras = launchingIntent.getExtras();
		int newsId = (Integer) extras.get("index");

		if (extras.containsKey(VisitorsOffersListActivity.KEY_LIST_ID)) {
			this.listId = (Integer) extras.get(VisitorsOffersListActivity.KEY_LIST_ID);
		} else {
			this.listId = null;
		}

		VisitorsArticleObject visitorsDetailsObject = createNewsDetailsObject(newsId);

		GeneralDetailsFragment detailsFragment = (GeneralDetailsFragment) getSupportFragmentManager().findFragmentById(R.id.detailsFragment);

		detailsFragment.showVisitorsLocationsDetails(visitorsDetailsObject);
	}

	/**
	 * Hack to handle the back button for news details.
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			Intent intent = new Intent();
			if (this.listId != null) {
				intent.putExtra(VisitorsLocationsListActivity.KEY_LIST_ID, this.listId);
				intent.setClass(this, VisitorsLocationsListActivity.class);
			} else {
				intent.setClass(this, VisitorsLocationsActivityTablet.class);
			}
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			this.startActivity(intent);
			overridePendingTransition(0, 0);

			return true;
		}

		return super.onKeyDown(keyCode, event);
	}

	private VisitorsArticleObject createNewsDetailsObject(int rowId) {
		VisitorsDatabaseAdapter visitorsDatabaseAdapter = new VisitorsDatabaseAdapter(this);
		visitorsDatabaseAdapter.open();

		Cursor newsCursor = visitorsDatabaseAdapter.articleDetails(rowId);
		String newsId = newsCursor.getString(newsCursor.getColumnIndex(VisitorsDatabaseAdapter.KEY_ID));
		newsCursor.close();

		newsCursor = visitorsDatabaseAdapter.articleDetailsFromNewsId(newsId);

		VisitorsArticleObject newsDetailsObject = new VisitorsArticleObject();

		newsDetailsObject.setTitle(newsCursor.getString(newsCursor.getColumnIndex(VisitorsDatabaseAdapter.KEY_ARTICLE_TITLE)));
		// newsDetailsObject.setImageURL(newsCursor.getString(newsCursor.getColumnIndex(VisitorsDatabaseAdapter.KEY_ARTICLE_IMAGEURL)));
		newsDetailsObject.setImageString(newsCursor.getString(newsCursor.getColumnIndex(VisitorsDatabaseAdapter.KEY_ARTICLE_IMAGESTRING)));
		newsDetailsObject.setImageCopyright(newsCursor.getString(newsCursor.getColumnIndex(VisitorsDatabaseAdapter.KEY_ARTICLE_IMAGECOPYRIGHT)));
		newsDetailsObject.setText(newsCursor.getString(newsCursor.getColumnIndex(VisitorsDatabaseAdapter.KEY_ARTICLE_TEXT)));

		newsCursor.close();
		visitorsDatabaseAdapter.close();

		return newsDetailsObject;
	}
}