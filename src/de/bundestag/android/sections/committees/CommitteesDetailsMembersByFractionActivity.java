package de.bundestag.android.sections.committees;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.KeyEvent;
import de.bundestag.android.BaseActivity;
import de.bundestag.android.R;
import de.bundestag.android.constant.AppConstant;
import de.bundestag.android.constant.DataHolder;
import de.bundestag.android.storage.CommitteesDatabaseAdapter;

public class CommitteesDetailsMembersByFractionActivity extends BaseActivity {
	public int committeesId;
	public String committeesIdString;
	public String fractionName;
	private int selectedSubmenu = 1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState, this);

		Intent launchingIntent = getIntent();
		Bundle extras = launchingIntent.getExtras();
		int committeesId = (Integer) extras.get("committeesId");
		this.committeesId = committeesId;

		this.committeesIdString = getCommitteeIdString(committeesId);
		String fractionName = (String) extras.get("fractionName");
		this.fractionName = fractionName;
		if (AppConstant.isFragmentSupported) {

			if (extras != null) {
				selectedSubmenu = launchingIntent.getExtras().getInt("selectedSubmenu");
			}
		}
		setContentView(R.layout.committees_detail_members_list);
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

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {

			if (AppConstant.isFragmentSupported) {
				Intent intent = new Intent();
				intent.putExtra("index", committeesId);

				if (selectedSubmenu == 1) {
					DataHolder.lastRowDBId=-1;
					DataHolder.rowDBSelectedIndex =0;
					intent.setClass(this, CommitteesDetailsNewsDetailsFragmentActivity.class);
					intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);			
					intent.putExtra(CommitteesDetailsNewsDetailsFragmentActivity.KEY_COMMITTEE_ID, DataHolder.committesStringId );
					this.startActivity(intent);
				} else if (selectedSubmenu == 3) {
					intent.setClass(this, CommitteesDetailsTasksActivity.class);
					intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					this.startActivity(intent);
				}else{
					intent.setClass(this, CommitteesDetailsNewsDetailsFragmentActivity.class);
					intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);			
					intent.putExtra(CommitteesDetailsNewsDetailsFragmentActivity.KEY_COMMITTEE_ID, DataHolder.committesStringId );
					this.startActivity(intent);
				}
				return true;
				// return super.onKeyDown(keyCode, event);
			}
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	protected void finalize() throws Throwable {
		super.finalize();
		committeesIdString = null;
		fractionName = null;
	}
}