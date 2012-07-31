package de.bundestag.android.synchronization;

import java.util.Date;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import de.bundestag.android.constant.AppConstant;
import de.bundestag.android.constant.DataHolder;
import de.bundestag.android.helpers.DateHelper;
import de.bundestag.android.parser.CommitteesXMLParser;
import de.bundestag.android.parser.objects.CommitteesDetailsNewsObject;
import de.bundestag.android.parser.objects.CommitteesDetailsObject;
import de.bundestag.android.sections.committees.CommitteesActivity;
import de.bundestag.android.sections.committees.CommitteesActivityTablet;
import de.bundestag.android.sections.committees.CommitteesDetailsMembersByFractionActivity;
import de.bundestag.android.sections.committees.CommitteesDetailsNewsActivity;
import de.bundestag.android.sections.committees.CommitteesDetailsNewsDetailsFragmentActivity;
import de.bundestag.android.sections.members.MembersCommitteesDetailsNewsActivity;
import de.bundestag.android.sections.members.MembersFractionActivityTab;
import de.bundestag.android.sections.members.MembersListActivity;
import de.bundestag.android.sections.members.MembersSubpageMembersActivity;
import de.bundestag.android.storage.CommitteesDatabaseAdapter;

public class SynchronizeCheckCommitteeNewsTask extends BaseSynchronizeTask {
	private String committeeIdString;
	private boolean isFragmentSupport = false;
	private String committeeName = "";

	public SynchronizeCheckCommitteeNewsTask() {

	}

	public SynchronizeCheckCommitteeNewsTask(boolean isFragmnetSupport, String committeeIdString) {
		this.isFragmentSupport = isFragmnetSupport;
		this.committeeIdString = committeeIdString;
		this.committeeName = committeeName;
	}

	@Override
	protected Void doInBackground(Context... context) {
		this.activity = context[0];
		publishProgress("progress");
		if (!AppConstant.isFragmentSupported) {
			CommitteesDetailsNewsActivity committeesDetailsNewsActivity = (CommitteesDetailsNewsActivity) this.activity;
			committeeIdString = committeesDetailsNewsActivity.committeesIdString;
		}

		String committeesDetailsURL = getCommitteesNewsXML(committeeIdString);

		CommitteesSynchronization committeesSynchronization = new CommitteesSynchronization();
		committeesSynchronization.setup(context[0]);

		committeesSynchronization.openDatabase();

		CommitteesXMLParser committeesXMLParser = new CommitteesXMLParser();
		List<CommitteesDetailsNewsObject> committeesNews = null;
		try {
			// should be dates
			CommitteesDetailsObject committeesObject = committeesXMLParser.parseNewsDetailsDates(committeesDetailsURL);
			committeesNews = committeesObject.getDetailNews();
		} catch (Exception e) {
			e.printStackTrace();

			committeesSynchronization.closeDatabase();

			return null;
		}

		boolean hasChanged = false;
		for (int i = 0; i < committeesNews.size(); i++) {
			CommitteesDetailsNewsObject committeesNewsObject = (CommitteesDetailsNewsObject) committeesNews.get(i);

			Cursor committeeNewsCursor = committeesSynchronization.getCommitteeNewsFromIdString(committeeIdString);

			Date newsDatabaseLastChanged = null;
			Date newsLastChanged = null;
			if ((committeeNewsCursor != null) && (committeeNewsCursor.getCount() > 0)) {
				String newsDatabaseLastChangedString = committeeNewsCursor.getString(committeeNewsCursor.getColumnIndex(CommitteesDatabaseAdapter.KEY_NEWS_LASTCHANGED));
				newsDatabaseLastChanged = DateHelper.parseDate(newsDatabaseLastChangedString);
				newsLastChanged = committeesNewsObject.getLastChangedDate();
			}

			if ((committeeNewsCursor == null) || (committeeNewsCursor.getCount() == 0) || (newsDatabaseLastChanged.before(newsLastChanged))) {
				System.out.println("HAS CHANGED");
				hasChanged = true;
			}

			committeeNewsCursor.close();
		}

		if (hasChanged) {
			// something has changed, so throw out all the news
			committeesSynchronization.deleteCommitteeNews(committeeIdString);

			try {
				CommitteesDetailsObject committeesObject = committeesXMLParser.parseNewsDetails(committeesDetailsURL, committeeIdString);
				committeesNews = committeesObject.getDetailNews();
			} catch (Exception e) {
				e.printStackTrace();

				committeesSynchronization.closeDatabase();

				return null;
			}

			for (int i = 0; i < committeesNews.size(); i++) {
				CommitteesDetailsNewsObject committeesNewsObject = (CommitteesDetailsNewsObject) committeesNews.get(i);
				committeesNewsObject.setCommitteeId(committeeIdString);

				committeesSynchronization.insertCommitteeNews(committeesNewsObject);
			}
		}

		// dismissProgress();
		// progress.dismiss();

		committeesSynchronization.closeDatabase();

		return null;
	}

	private String getCommitteesNewsXML(String committeeIdString) {
		CommitteesDatabaseAdapter committeesDatabaseAdapter = new CommitteesDatabaseAdapter(this.activity);
		committeesDatabaseAdapter.open();
		Cursor committeesCursor = committeesDatabaseAdapter.fetchCommitteesById(committeeIdString);
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
			if (activity instanceof CommitteesActivity||activity instanceof CommitteesActivityTablet) {
				intent.setClass(activity, CommitteesDetailsNewsDetailsFragmentActivity.class);
				intent.putExtra(CommitteesDetailsNewsDetailsFragmentActivity.KEY_COMMITTEE_ID, committeeIdString);
				DataHolder.committesStringId = committeeIdString;
				activity.startActivity(intent);
//				progress.dismiss();
				intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
			} else if (activity instanceof MembersListActivity || activity instanceof MembersSubpageMembersActivity || activity instanceof MembersFractionActivityTab) {
				intent.setClass(activity, CommitteesDetailsNewsDetailsFragmentActivity.class);
				intent.putExtra(CommitteesDetailsNewsDetailsFragmentActivity.KEY_COMMITTEE_ID, committeeIdString);
				DataHolder.committesStringId = committeeIdString;
				activity.startActivity(intent);
				DataHolder.appState = "MembersListActivity";
				 progress.dismiss();
				intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
			}else if (activity instanceof CommitteesDetailsMembersByFractionActivity) {
				intent.setClass(activity, CommitteesDetailsNewsDetailsFragmentActivity.class);
				intent.putExtra(CommitteesDetailsNewsDetailsFragmentActivity.KEY_COMMITTEE_ID, committeeIdString);
				DataHolder.committesStringId = committeeIdString;
				activity.startActivity(intent);
				DataHolder.appState = "CommitteesDetailsMembersByFractionActivity";
				 progress.dismiss();
				intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
			} 
			else {
				if (activity instanceof MembersCommitteesDetailsNewsActivity) {
					intent.setClass(activity, MembersCommitteesDetailsNewsActivity.class);
				} else {
					intent.setClass(activity, CommitteesDetailsNewsActivity.class);
				}
				intent.putExtra(CommitteesDetailsNewsActivity.KEY_COMMITTEE_NEWS_UPDATED, true);
				intent.putExtra(CommitteesDetailsNewsActivity.KEY_COMMITTEE_ID, committeeIdString);
				activity.startActivity(intent);

				if (activity instanceof CommitteesDetailsNewsActivity) {
					CommitteesDetailsNewsActivity committeesDetailsNewsActivity = (CommitteesDetailsNewsActivity) activity;
					committeesDetailsNewsActivity.overridePendingTransition(0, 0);
				} else if (activity instanceof MembersCommitteesDetailsNewsActivity) {
					MembersCommitteesDetailsNewsActivity membersCommitteesDetailsNewsActivity = (MembersCommitteesDetailsNewsActivity) activity;
					membersCommitteesDetailsNewsActivity.overridePendingTransition(0, 0);
				}
			}
		} catch (Exception e) {
			System.out.println("Exception e" + e.getMessage());

		}
	}
}
