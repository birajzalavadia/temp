package de.bundestag.android.sections.committees;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.KeyEvent;
import de.bundestag.android.BaseActivity;
import de.bundestag.android.R;
import de.bundestag.android.fragments.GeneralDetailsFragment;
import de.bundestag.android.parser.objects.CommitteesDetailsNewsDetailsObject;
import de.bundestag.android.storage.CommitteesDatabaseAdapter;
import de.bundestag.android.synchronization.BaseSynchronizeTask;
import de.bundestag.android.synchronization.SynchronizeCommitteeNewsDetailsTask;

public class CommitteesDetailsNewsDetailsActivity extends BaseActivity {
	public static final String KEY_COMMITTEE_ID = "COMMITTEE_ID";

	public int committeeId = 0;

	public int newsId = 0;

	public String detailsXMLURL;

	private BaseSynchronizeTask task = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState, this);
		setContentView(R.layout.news_detail);

		Intent launchingIntent = getIntent();
		Bundle extras = launchingIntent.getExtras();
		int newsId = (Integer) extras.get("index");
		int committeeId = (Integer) extras.get(KEY_COMMITTEE_ID);
		this.committeeId = committeeId;
		this.newsId = newsId;
		CommitteesDetailsNewsDetailsObject committeesDetailsNewsDetailsObject = createNewsDetailsObject(newsId);

		if (committeesDetailsNewsDetailsObject != null) {
			GeneralDetailsFragment detailsFragment = (GeneralDetailsFragment) getSupportFragmentManager().findFragmentById(R.id.detailsFragment);
			detailsFragment.showCommitteesDetailsNewsDetails(committeesDetailsNewsDetailsObject);
		}
	}

	/**
	 * Hack to handle the back button for committees news details.
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			Intent intent = new Intent();
			intent.putExtra(CommitteesDetailsNewsActivity.KEY_COMMITTEE_NEWS_UPDATED, true);
			intent.putExtra("index", this.committeeId);
			intent.setClass(this, CommitteesDetailsNewsActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			this.startActivity(intent);
			overridePendingTransition(0, 0);

			return true;
		}

		return super.onKeyDown(keyCode, event);
	}

	private CommitteesDetailsNewsDetailsObject createNewsDetailsObject(int newsId) {
		CommitteesDatabaseAdapter committeesNewsDetailsDatabaseAdapter = new CommitteesDatabaseAdapter(this);
		committeesNewsDetailsDatabaseAdapter.open();

		Cursor newsCursor = committeesNewsDetailsDatabaseAdapter.fetchCommitteesDetailsNewsDetails(newsId);

		if (newsCursor.getString(newsCursor.getColumnIndex(CommitteesDatabaseAdapter.KEY_NEWS_DETAILS_TITLE)) == null) {
			this.detailsXMLURL = newsCursor.getString(newsCursor.getColumnIndex(CommitteesDatabaseAdapter.KEY_NEWS_DETAILSXML));
			SynchronizeCommitteeNewsDetailsTask synchronizeCommitteeNewsDetailsTask = new SynchronizeCommitteeNewsDetailsTask();
			task = synchronizeCommitteeNewsDetailsTask;
			synchronizeCommitteeNewsDetailsTask.execute(this);
			newsCursor.close();
			committeesNewsDetailsDatabaseAdapter.close();
		} else {
			CommitteesDetailsNewsDetailsObject committeesDetailsNewsDetailsObject = new CommitteesDetailsNewsDetailsObject();

			committeesDetailsNewsDetailsObject.setTitle(newsCursor.getString(newsCursor.getColumnIndex(CommitteesDatabaseAdapter.KEY_NEWS_DETAILS_TITLE)));
			committeesDetailsNewsDetailsObject.setImageURL(newsCursor.getString(newsCursor.getColumnIndex(CommitteesDatabaseAdapter.KEY_NEWS_DETAILS_IMAGEURL)));
			committeesDetailsNewsDetailsObject.setImageCopyright(newsCursor.getString(newsCursor.getColumnIndex(CommitteesDatabaseAdapter.KEY_NEWS_DETAILS_IMAGECOPYRIGHT)));
			committeesDetailsNewsDetailsObject.setText(newsCursor.getString(newsCursor.getColumnIndex(CommitteesDatabaseAdapter.KEY_NEWS_DETAILS_TEXT)));

			newsCursor.close();
			committeesNewsDetailsDatabaseAdapter.close();

			return committeesDetailsNewsDetailsObject;
		}

		return null;
	}

	@Override
	protected void finalize() throws Throwable {
		super.finalize();

		detailsXMLURL = null;

		task = null;
	}

}