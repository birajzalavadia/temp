package de.bundestag.android.sections.committees;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.KeyEvent;
import de.bundestag.android.BaseActivity;
import de.bundestag.android.R;
import de.bundestag.android.constant.AppConstant;
import de.bundestag.android.constant.DataHolder;
import de.bundestag.android.fragments.GeneralDetailsFragment;
import de.bundestag.android.parser.objects.CommitteesDetailsObject;
import de.bundestag.android.storage.CommitteesDatabaseAdapter;

public class CommitteesDetailsTasksActivity extends BaseActivity {
	public int committeesId;
	public String committeesIdString;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState, this);

		Intent launchingIntent = getIntent();
		Bundle extras = launchingIntent.getExtras();
		boolean committeeById = extras.containsKey(CommitteesDetailsNewsActivity.KEY_COMMITTEE_ID);

		if (committeeById) {
			committeesIdString = (String) extras.get(CommitteesDetailsNewsActivity.KEY_COMMITTEE_ID);
			this.committeesId = getCommitteeId(committeesIdString);
		} else {
			int committeesId = (Integer) extras.get("index");
			this.committeesId = committeesId;
		}

		/*
		 * boolean committeeById = extras.containsKey(KEY_COMMITTEE_ID); Cursor
		 * committeesCursor = null; if (committeeById) { String committeesId =
		 * (String) extras.get(KEY_COMMITTEE_ID);
		 * 
		 * // TODO - check that this is not neccessary //this.committeesId =
		 * committeesId; committeesCursor = getCommitteeById(committeesId); }
		 * else { int committeesId = (Integer) extras.get("index");
		 * this.committeesId = committeesId; committeesCursor =
		 * getCommitteeByRow(committeesId); }
		 */

		Cursor committeesCursor = getCommitteeByRow(committeesId);

		setContentView(R.layout.committees_detail);

		CommitteesDetailsObject committeesDetailsObject = createCommitteesDetailsObject(committeesCursor);
		committeesCursor.close();

		GeneralDetailsFragment detailsFragment;
		detailsFragment = (GeneralDetailsFragment) getSupportFragmentManager().findFragmentById(R.id.detailsFragment);
		detailsFragment.showCommitteesDetailsTasks(committeesDetailsObject);
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
			if (DataHolder.isPrevCommittee) {
				DataHolder.isPrevCommittee = false;
				Intent intent = new Intent();
				if (DataHolder.isOnline(this) || AppConstant.isFragmentSupported) {
					intent.setClass(this, CommitteesActivityTablet.class);
				} else {
					intent.setClass(this, CommitteesDetailsTasksActivity.class);
				}
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent);
				this.overridePendingTransition(0, 0);
				return true;
			}else{
				return super.onKeyDown(keyCode, event);
			}
			
		}

		return super.onKeyDown(keyCode, event);
	}

	private Cursor getCommitteeByRow(int committeeId) {
		CommitteesDatabaseAdapter committeesDatabaseAdapter = new CommitteesDatabaseAdapter(this);
		committeesDatabaseAdapter.open();

		Cursor committeesCursor = committeesDatabaseAdapter.fetchCommittees(committeesId);

		return committeesCursor;
	}

	// private Cursor getCommitteeById(String committeesId)
	// {
	// CommitteesDatabaseAdapter committeesDatabaseAdapter = new
	// CommitteesDatabaseAdapter(this);
	// committeesDatabaseAdapter.open();
	//
	// Cursor committeesCursor =
	// committeesDatabaseAdapter.fetchCommitteesById(committeesId);
	//
	// return committeesCursor;
	// }

	private CommitteesDetailsObject createCommitteesDetailsObject(Cursor committeesCursor) {
		CommitteesDetailsObject committeesDetailsObject = new CommitteesDetailsObject();

		committeesDetailsObject.setName(committeesCursor.getString(committeesCursor.getColumnIndex(CommitteesDatabaseAdapter.KEY_DETAILS_NAME)));
		committeesDetailsObject.setPhotoString(committeesCursor.getString(committeesCursor.getColumnIndex(CommitteesDatabaseAdapter.KEY_DETAILS_PHOTOSTRING)));
		committeesDetailsObject.setPhotoCopyright(committeesCursor.getString(committeesCursor.getColumnIndex(CommitteesDatabaseAdapter.KEY_DETAILS_PHOTOCOPYRIGHT)));
		committeesDetailsObject.setDescription(committeesCursor.getString(committeesCursor.getColumnIndex(CommitteesDatabaseAdapter.KEY_DETAILS_DESCRIPTION)));
		DataHolder.releaseScreenLock(this);
		return committeesDetailsObject;
	}
}