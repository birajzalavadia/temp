package de.bundestag.android.synchronization;

import android.content.Context;
import android.graphics.Bitmap;
import de.bundestag.android.helpers.ImageHelper;
import de.bundestag.android.parser.CommitteesXMLParser;
import de.bundestag.android.parser.objects.CommitteesDetailsNewsDetailsObject;
import de.bundestag.android.sections.committees.CommitteesDetailsFragment;

public class SynchronizeCommitteeNewsFirstDetailsTask extends BaseSynchronizeTask {

	private CommitteesDetailsFragment newsDetailsFragment = null;

	public SynchronizeCommitteeNewsFirstDetailsTask(CommitteesDetailsFragment committeesDetailsFragment) {
		newsDetailsFragment = committeesDetailsFragment;
	}

	@Override
	protected Void doInBackground(Context... context) {
		this.activity = context[0];

		// CommitteesDetailsNewsDetailsActivity
		// committeesDetailsNewsDetailsActivity =
		// (CommitteesDetailsNewsDetailsActivity) this.activity;

		CommitteesSynchronization committeesSynchronization = new CommitteesSynchronization();
		committeesSynchronization.setup(context[0]);

		committeesSynchronization.openDatabase();

		// String detailsXMLURL =
		// committeesDetailsNewsDetailsActivity.detailsXMLURL;

		

		CommitteesXMLParser committeesXMLParser = new CommitteesXMLParser();
		try {

			CommitteesDetailsNewsDetailsObject committeesDetailsNewsDetailsObject = committeesXMLParser.parseDetailsNewsFirstItem();
			if (committeesDetailsNewsDetailsObject != null && committeesDetailsNewsDetailsObject.getImageURL() != null &&committeesDetailsNewsDetailsObject.getImageURL().trim().length() > 0) {
				try {
					Bitmap bitmap = ImageHelper.loadBitmapFromUrl(committeesDetailsNewsDetailsObject.getImageURL());
					if (bitmap != null) {
						committeesDetailsNewsDetailsObject.setImageString(ImageHelper.convertBitmapToString(bitmap));
					}
				} catch (Exception e) {

				}
			}
			committeesSynchronization.insertFirstItemNews(committeesDetailsNewsDetailsObject);
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
			newsDetailsFragment.createCommitteesDetailsObject();
//			progress.dismiss();
		} catch (Exception e) {
			System.out.println("Exception e: SynchronizeCommitteeNewsDetailsTask " + e.getMessage());
		}
//		progress.dismiss();
	}

}
