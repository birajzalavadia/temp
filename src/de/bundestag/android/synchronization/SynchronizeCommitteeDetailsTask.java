package de.bundestag.android.synchronization;

import android.content.Context;
import android.view.View;
import de.bundestag.android.parser.CommitteesXMLParser;
import de.bundestag.android.parser.objects.CommitteesDetailsObject;
import de.bundestag.android.sections.committees.CommitteesDetailsFragment;

public class SynchronizeCommitteeDetailsTask extends BaseSynchronizeTask {
	private String committeeId = "";
	private String detailsXMLURL = "";
	private CommitteesDetailsFragment committeesDetailsFragment = null;

	public SynchronizeCommitteeDetailsTask() {

	}

	public SynchronizeCommitteeDetailsTask(String committeeId, String detailsXMLURL, CommitteesDetailsFragment newsDetailsFragment, View view) {
		this.committeeId = committeeId;
		this.detailsXMLURL = detailsXMLURL;
		this.committeesDetailsFragment = newsDetailsFragment;
	}

	@Override
	protected Void doInBackground(Context... context) {
		this.activity = context[0];
		publishProgress("progress");
		CommitteesSynchronization committeesSynhronization = new CommitteesSynchronization();
		committeesSynhronization.setup(context[0]);

		committeesSynhronization.openDatabase();

		

		CommitteesXMLParser committeesXMLParser = new CommitteesXMLParser();
		try {
			CommitteesDetailsObject committeesDetailsObject = committeesXMLParser.parseDetails(detailsXMLURL, committeeId);
		} catch (Exception e) {
			e.printStackTrace();
		}

		committeesSynhronization.closeDatabase();

		// progress.dismiss();

		return null;
	}

	/**
	 * Once the news synchronize task has been executed, launch the news
	 * activity.
	 */
	@Override
	protected void onPostExecute(Void result) {
//		DataHolder._id = committeeId;
		try{
			committeesDetailsFragment.createCommitteesDetailsObject();
//			progress.dismiss();
		}catch(Exception e){
//    		System.out.println("Exception e  SynchronizeCommitteeDetailsTask "+ e.getMessage());
    	}

	}
}
