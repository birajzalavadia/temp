package de.bundestag.android.sections.committees;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.FrameLayout;
import android.widget.TextView;
import de.bundestag.android.BaseActivity;
import de.bundestag.android.R;
import de.bundestag.android.constant.AppConstant;
import de.bundestag.android.constant.DataHolder;
import de.bundestag.android.storage.CommitteesDatabaseAdapter;
import de.bundestag.android.synchronization.BaseSynchronizeTask;
import de.bundestag.android.synchronization.SynchronizeCheckCommitteeNewsTask;
import de.bundestag.android.synchronization.SynchronizeCommitteeNewsTask;

public class CommitteesDetailsNewsActivity extends BaseActivity {
	public static final String KEY_COMMITTEE_ID = "COMMITTEE_ID";

	public static final String KEY_COMMITTEE_NEWS_UPDATED = "COMMITTEE_NEWS_UPDATED";

	public int committeesId = -1;

	public String committeesIdString = null;

	public String committeesName = "";

	private BaseSynchronizeTask task = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Log.d("orientation test", "On create CommiteeDetailsNewsActivity");
		super.onCreate(savedInstanceState, this);

		Intent launchingIntent = getIntent();
		Bundle extras = launchingIntent.getExtras();

		boolean committeeById = extras.containsKey(KEY_COMMITTEE_ID);
		if (committeeById) {
			String committeesId = (String) extras.get(KEY_COMMITTEE_ID);
			this.committeesIdString = committeesId;

			this.committeesId = getCommitteeId(committeesId);
			this.committeesName = getCommitteeIdName(committeesId);
			DataHolder._id = this.committeesId;
			// Hack - if a committee doesn't exist (from the members groups),
			// then return to the committees list
			if (this.committeesId == -1) {
				Intent intent = new Intent();
				intent.putExtra("index", this.committeesId);
				intent.putExtra("committeeName", this.committeesName);
				if (!AppConstant.isFragmentSupported) {
					intent.setClass(this, CommitteesActivity.class);
				} else {
					intent.setClass(this, CommitteesActivityTablet.class);
				}
				this.startActivity(intent);
				overridePendingTransition(0, 0);
				finish();
			}
		} else {
			this.committeesIdString = null;
			boolean existsId = extras.containsKey("index");
			if (existsId) {
				int committeesId = (Integer) extras.get("index");
				this.committeesId = committeesId;
				this.committeesName = getCommitteeIdNameString(committeesId);
				extras.putString("committeeName", this.committeesName);
			}

			this.committeesIdString = getCommitteeIdString(committeesId);
		}

		if (DataHolder.isOnline(this)) {
			boolean committeeNewsUpdated = false;
			if ((extras != null) && (extras.containsKey(KEY_COMMITTEE_NEWS_UPDATED))) {
				committeeNewsUpdated = (Boolean) extras.get(KEY_COMMITTEE_NEWS_UPDATED);
			}
			if (!committeeNewsUpdated) {
				checkCommitteeNewsNeedsUpdating();
				return;
			}

			setContentView(R.layout.committees_detail_list);
			if (AppConstant.isFragmentSupported) {
				TextView title = (TextView) findViewById(R.id.mainTitle);
				title.setLayoutParams(new FrameLayout.LayoutParams(DataHolder.calculatedScreenResolution, FrameLayout.LayoutParams.WRAP_CONTENT));
				title.setText(committeesName);
			}
		}
	}

	private int getCommitteeId(String committeesIdString) {
		CommitteesDatabaseAdapter committeesDatabaseAdapter = new CommitteesDatabaseAdapter(this);
		committeesDatabaseAdapter.open();

		int committeeId = -1;
		Cursor memberCursor = committeesDatabaseAdapter.fetchCommitteesById(committeesIdString);
		if (memberCursor.getCount() > 0) {
			committeeId = memberCursor.getInt(memberCursor.getColumnIndex(CommitteesDatabaseAdapter.KEY_ROWID));
		}
		memberCursor.close();
		committeesDatabaseAdapter.close();

		return committeeId;
	}

	/**
	 * Get the committees id from the database, using the row id.
	 */
	private String getCommitteeIdString(int rowId) {
		CommitteesDatabaseAdapter committeesDatabaseAdapter = new CommitteesDatabaseAdapter(this);
		committeesDatabaseAdapter.open();

		Cursor memberCursor = committeesDatabaseAdapter.fetchCommittees(rowId);
		String committeeIdString = memberCursor.getString(memberCursor.getColumnIndex(CommitteesDatabaseAdapter.KEY_ID));
		memberCursor.close();

		committeesDatabaseAdapter.close();

		return committeeIdString;
	}

	/**
	 * Get the committees id from the database, using the row id.
	 */
	private String getCommitteeIdName(String rowId) {
		CommitteesDatabaseAdapter committeesDatabaseAdapter = new CommitteesDatabaseAdapter(this);
		committeesDatabaseAdapter.open();

		String committeeName = "";
		Cursor memberCursor = committeesDatabaseAdapter.fetchCommitteesById(committeesIdString);
		if (memberCursor.getCount() > 0) {
			committeeName = memberCursor.getString(memberCursor.getColumnIndex(CommitteesDatabaseAdapter.KEY_NAME));
		}
		memberCursor.close();
		committeesDatabaseAdapter.close();

		return committeeName;
	}

	/**
	 * Get the committees id from the database, using the row id.
	 */
	private String getCommitteeIdNameString(int rowId) {
		CommitteesDatabaseAdapter committeesDatabaseAdapter = new CommitteesDatabaseAdapter(this);
		committeesDatabaseAdapter.open();

		Cursor memberCursor = committeesDatabaseAdapter.fetchCommittees(rowId);
		String committeeIdString = memberCursor.getString(memberCursor.getColumnIndex(CommitteesDatabaseAdapter.KEY_NAME));
		memberCursor.close();

		committeesDatabaseAdapter.close();

		return committeeIdString;
	}

	/**
	 * Hack to handle the back button for committees news details.
	 */
	public boolean onKeyDownSuper(int keyCode, KeyEvent event) {
		return super.onKeyDown(keyCode, event);
	}

	/**
	 * Hack to handle the back button for committees news details.
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			Intent intent = new Intent();
			intent.putExtra("index", this.committeesId);
			if (!AppConstant.isFragmentSupported) {
				intent.setClass(this, CommitteesActivity.class);
			} else {
				intent.setClass(this, CommitteesActivityTablet.class);
			}
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			this.startActivity(intent);
			overridePendingTransition(0, 0);

			return true;
		}

		return super.onKeyDown(keyCode, event);
	}

	/**
	 * Call the committee news synchronize.
	 */
	private void checkCommitteeNewsNeedsUpdating() {
		CommitteesDatabaseAdapter committeesDatabaseAdapter = new CommitteesDatabaseAdapter(this);
		committeesDatabaseAdapter.open();
		boolean existNews = committeesDatabaseAdapter.existNews(committeesIdString);
		committeesDatabaseAdapter.close();

		if (existNews) {
			SynchronizeCheckCommitteeNewsTask synchronizeCheckCommitteeNewsTask = new SynchronizeCheckCommitteeNewsTask();
			task = synchronizeCheckCommitteeNewsTask;
			synchronizeCheckCommitteeNewsTask.execute(this);
		} else {
			SynchronizeCommitteeNewsTask committeeNewsUpdateTask = new SynchronizeCommitteeNewsTask();
			task = committeeNewsUpdateTask;
			committeeNewsUpdateTask.committeeId = this.committeesIdString;
			committeeNewsUpdateTask.execute(this);
		}
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();

		// LinearLayout menuHolder1= (LinearLayout)
		// findViewById(R.id.subMenuHolder1);
		// menuHolder1.setBackgroundResource(R.drawable.top_navigation_gradient_active);
		// LinearLayout menuHolder2= (LinearLayout)
		// findViewById(R.id.subMenuHolder2);
		// menuHolder2.setBackgroundResource(R.drawable.top_navigation_gradient);
		// LinearLayout menuHolder3= (LinearLayout)
		// findViewById(R.id.subMenuHolder3);
		// menuHolder3.setBackgroundResource(R.drawable.top_navigation_gradient);

	}

	@Override
	protected void finalize() throws Throwable {
		super.finalize();
		committeesIdString = null;

		committeesName = "";

		task = null;
	}
}