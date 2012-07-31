package de.bundestag.android.synchronization;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import de.bundestag.android.BaseActivity;
import de.bundestag.android.constant.DataHolder;
import de.bundestag.android.parser.CommitteesXMLParser;
import de.bundestag.android.parser.objects.CommitteesDetailsNewsObject;
import de.bundestag.android.parser.objects.CommitteesDetailsObject;
import de.bundestag.android.sections.committees.CommitteesDetailsMembersByFractionActivity;
import de.bundestag.android.sections.committees.CommitteesDetailsNewsActivity;
import de.bundestag.android.sections.committees.CommitteesDetailsNewsDetailsFragmentActivity;
import de.bundestag.android.sections.members.MembersCommitteesDetailsNewsActivity;
import de.bundestag.android.sections.members.MembersFractionActivityTab;
import de.bundestag.android.sections.members.MembersListActivity;
import de.bundestag.android.sections.members.MembersSubpageMembersActivity;
import de.bundestag.android.storage.CommitteesDatabaseAdapter;

public class SynchronizeCommitteeNewsTask extends BaseSynchronizeTask {
	public String committeeId = null;

	@Override
	protected Void doInBackground(Context... context) {
		this.activity = context[0];

		publishProgress("progress");
		String committeesDetailsURL = getCommitteesNewsXML();

		CommitteesSynchronization committeesSynchronization = new CommitteesSynchronization();
		committeesSynchronization.setup(context[0]);

		committeesSynchronization.openDatabase();

		CommitteesXMLParser committeesXMLParser = new CommitteesXMLParser();
		List<CommitteesDetailsNewsObject> committeesNews = null;
		try {
			CommitteesDetailsObject committeesObject = committeesXMLParser.parseNewsDetails(committeesDetailsURL, committeeId);
			committeesNews = committeesObject.getDetailNews();
		} catch (Exception e) {
			e.printStackTrace();

			committeesSynchronization.closeDatabase();

			return null;
		}

		int insertsSize = BaseActivity.isDebugOn() ? ((BaseActivity.DEBUG_SYNCHRONIZE_NUMBER > committeesNews.size()) ? committeesNews.size()
				: BaseActivity.DEBUG_SYNCHRONIZE_NUMBER) : committeesNews.size();
		for (int i = 0; i < insertsSize; i++) {
			CommitteesDetailsNewsObject committeesNewsObject = (CommitteesDetailsNewsObject) committeesNews.get(i);
			committeesNewsObject.setCommitteeId(committeeId);

			committeesSynchronization.insertCommitteeNews(committeesNewsObject);
		}

		committeesSynchronization.closeDatabase();

		// dismissProgress();
		// progress.dismiss();

		return null;
	}

	private String getCommitteesNewsXML() {
		CommitteesDatabaseAdapter committeesDatabaseAdapter = new CommitteesDatabaseAdapter(this.activity);
		committeesDatabaseAdapter.open();
		Cursor committeesCursor = committeesDatabaseAdapter.fetchCommitteesById(committeeId);
		String committeesDetailsURL = committeesCursor.getString(committeesCursor.getColumnIndex(CommitteesDatabaseAdapter.KEY_DETAILSXML));
		committeesCursor.close();
		committeesDatabaseAdapter.close();

		return committeesDetailsURL;
	}

	/**
	 * Once the committee news synchronize task has been executed, launch the
	 * committee news activity.
	 */
	@Override
	protected void onPostExecute(Void result) {
		try {
			Intent intent = new Intent();
			if (activity instanceof MembersCommitteesDetailsNewsActivity) {
				// MembersCommitteesDetailsNewsActivity
				// membersCommitteesDetailsNewsActivity =
				// (MembersCommitteesDetailsNewsActivity) activity;
				// intent.putExtra("index",
				// membersCommitteesDetailsNewsActivity.memberId);
				intent.setClass(activity, MembersCommitteesDetailsNewsActivity.class);
			} else if (activity instanceof MembersListActivity || activity instanceof MembersSubpageMembersActivity || activity instanceof MembersFractionActivityTab) {
				intent.setClass(activity, CommitteesDetailsNewsDetailsFragmentActivity.class);
				committeeId = DataHolder.committesStringId;
				// DataHolder.committesStringId = DataHolder.committesStringId;
				DataHolder.dismissProgress();
				DataHolder.appState = "MembersListActivity";
				// intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
			} else if (activity instanceof CommitteesDetailsMembersByFractionActivity) {
				intent.setClass(activity, CommitteesDetailsNewsDetailsFragmentActivity.class);
				committeeId = DataHolder.committesStringId;
				// DataHolder.committesStringId = DataHolder.committesStringId;
				DataHolder.dismissProgress();
				DataHolder.appState = "CommitteesDetailsMembersByFractionActivity";
			} else {
				intent.setClass(activity, CommitteesDetailsNewsActivity.class);
			}
			intent.putExtra(CommitteesDetailsNewsActivity.KEY_COMMITTEE_NEWS_UPDATED, true);
			intent.putExtra(CommitteesDetailsNewsActivity.KEY_COMMITTEE_ID, committeeId);
			activity.startActivity(intent);

			if (activity instanceof CommitteesDetailsNewsActivity) {
				CommitteesDetailsNewsActivity committeesDetailsNewsActivity = (CommitteesDetailsNewsActivity) activity;
				committeesDetailsNewsActivity.overridePendingTransition(0, 0);
			} else if (activity instanceof MembersCommitteesDetailsNewsActivity) {
				MembersCommitteesDetailsNewsActivity membersCommitteesDetailsNewsActivity = (MembersCommitteesDetailsNewsActivity) activity;
				membersCommitteesDetailsNewsActivity.overridePendingTransition(0, 0);
			}

		} catch (Exception e) {
			// System.out.println("Exception e: SynchronizeCommitteeNewsTask "+
			// e.getMessage());
		}
	}
}
