package de.bundestag.android.synchronization;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import de.bundestag.android.constant.DataHolder;
import de.bundestag.android.parser.CommitteesXMLParser;
import de.bundestag.android.parser.objects.CommitteesDetailsNewsDetailsObject;
import de.bundestag.android.sections.committees.CommitteesDetailsNewsDetailsActivity;
import de.bundestag.android.sections.committees.CommitteesDetailsNewsDetailsFragment;

public class SynchronizeCommitteeNewsDetailsTask extends BaseSynchronizeTask {
	private int newsId = 0;
	private String detailsXMLURL = "";
	private boolean isFragmentUI = false;
	private CommitteesDetailsNewsDetailsFragment newsDetailsFragment = null;
	private View view = null;

	public SynchronizeCommitteeNewsDetailsTask() {

	}

	public SynchronizeCommitteeNewsDetailsTask(int newsId, String detailsXMLURL, CommitteesDetailsNewsDetailsFragment newsDetailsFragment, View view) {
		this.newsId = newsId;
		this.detailsXMLURL = detailsXMLURL;
		isFragmentUI = true;
		this.newsDetailsFragment = newsDetailsFragment;
		this.view = view;
	}

	@Override
	protected Void doInBackground(Context... context) {
		this.activity = context[0];
		publishProgress("progress");
		// CommitteesDetailsNewsDetailsActivity
		// committeesDetailsNewsDetailsActivity =
		// (CommitteesDetailsNewsDetailsActivity) this.activity;

		CommitteesSynchronization committeesSynchronization = new CommitteesSynchronization();
		committeesSynchronization.setup(context[0]);

		if (!isFragmentUI) {
			CommitteesDetailsNewsDetailsActivity newsDetailsActivity = (CommitteesDetailsNewsDetailsActivity) this.activity;
			newsId = newsDetailsActivity.newsId;
			detailsXMLURL = newsDetailsActivity.detailsXMLURL;
		}
		committeesSynchronization.openDatabase();

		// String detailsXMLURL =
		// committeesDetailsNewsDetailsActivity.detailsXMLURL;

		

		CommitteesXMLParser committeesXMLParser = new CommitteesXMLParser();
		try {
			CommitteesDetailsNewsDetailsObject committeesDetailsNewsDetailsObject = committeesXMLParser.parseDetailsNews(detailsXMLURL);

			committeesSynchronization.updateCommitteeNews(newsId, committeesDetailsNewsDetailsObject);
		} catch (Exception e) {
			e.printStackTrace();

			committeesSynchronization.closeDatabase();

			return null;
		}

		committeesSynchronization.closeDatabase();

		// dismissProgress();
		// progress.dismiss();

		return null;
	}

	/**
	 * Once the committee news synchronize task has been executed, launch the
	 * committee news activity.
	 */
	@Override
	protected void onPostExecute(Void result) {
		try {
			if (!isFragmentUI) {
				Intent intent = new Intent();
				CommitteesDetailsNewsDetailsActivity committeesDetailsNewsDetailsActivity = (CommitteesDetailsNewsDetailsActivity) this.activity;
				intent.setClass(activity, CommitteesDetailsNewsDetailsActivity.class);
				intent.putExtra(CommitteesDetailsNewsDetailsActivity.KEY_COMMITTEE_ID, committeesDetailsNewsDetailsActivity.committeeId);
				intent.putExtra("index", committeesDetailsNewsDetailsActivity.newsId);

				activity.startActivity(intent);

				committeesDetailsNewsDetailsActivity.overridePendingTransition(0, 0);
			} else {
				DataHolder._id = newsId;
				newsDetailsFragment.createNewsDetailsObject();
//				progress.dismiss();
			}
		} catch (Exception e) {
			System.out.println("Exception e: SynchronizeCommitteeNewsDetailsTask " + e.getMessage());
		}
	}
}
