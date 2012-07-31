package de.bundestag.android.sections.visitors;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.KeyEvent;
import de.bundestag.android.BaseActivity;
import de.bundestag.android.R;
import de.bundestag.android.fragments.GeneralDetailsFragment;
import de.bundestag.android.parser.objects.NewsDetailsObject;
import de.bundestag.android.storage.NewsDatabaseAdapter;

public class VisitorsDetailsActivity extends BaseActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState, this);

		setContentView(R.layout.news_detail);

		Intent launchingIntent = getIntent();
		Bundle extras = launchingIntent.getExtras();
		int newsId = (Integer) extras.get("index");

		NewsDetailsObject newsDetailsObject = createNewsDetailsObject(newsId);

		GeneralDetailsFragment detailsFragment = (GeneralDetailsFragment) getSupportFragmentManager().findFragmentById(R.id.detailsFragment);

		detailsFragment.showNewsDetails(newsDetailsObject, true);
	}

	/**
	 * Hack to handle the back button for visitor news details.
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			Intent intent = new Intent();
			intent.putExtra(VisitorsNewsActivity.SHOW_VISITOR_NEWS_KEY, true);
			intent.setClass(this, VisitorsNewsActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			this.startActivity(intent);
			overridePendingTransition(0, 0);

			return true;
		}

		return super.onKeyDown(keyCode, event);
	}

	private NewsDetailsObject createNewsDetailsObject(int newsId) {
		NewsDatabaseAdapter newsDatabaseAdapter = new NewsDatabaseAdapter(this);
		newsDatabaseAdapter.open();

		Cursor newsCursor = newsDatabaseAdapter.fetchNewsDetails(newsId);

		NewsDetailsObject newsDetailsObject = new NewsDetailsObject();

		newsDetailsObject.setTitle(newsCursor.getString(newsCursor.getColumnIndex(NewsDatabaseAdapter.KEY_DETAILS_TITLE)));
		newsDetailsObject.setImageURL(newsCursor.getString(newsCursor.getColumnIndex(NewsDatabaseAdapter.KEY_DETAILS_IMAGEURL)));
		newsDetailsObject.setImageString(newsCursor.getString(newsCursor.getColumnIndex(NewsDatabaseAdapter.KEY_DETAILS_IMAGE_STRING)));
		newsDetailsObject.setImageCopyright(newsCursor.getString(newsCursor.getColumnIndex(NewsDatabaseAdapter.KEY_DETAILS_IMAGECOPYRIGHT)));
		newsDetailsObject.setText(newsCursor.getString(newsCursor.getColumnIndex(NewsDatabaseAdapter.KEY_DETAILS_TEXT)));

		newsCursor.close();
		newsDatabaseAdapter.close();

		return newsDetailsObject;
	}
}