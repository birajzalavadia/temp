package de.bundestag.android.synchronization;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import de.bundestag.android.constant.DataHolder;
import de.bundestag.android.parser.NewsXMLParser;
import de.bundestag.android.parser.objects.NewsDetailsObject;
import de.bundestag.android.sections.visitors.VisitorsNewsDetailsActivity;
import de.bundestag.android.sections.visitors.VisitorsNewsDetailsFragment;

public class SynchronizeVisitorsNewsDetailsTask extends BaseSynchronizeTask {
	private int newsId = 0;
	private String detailsXMLURL = "";
	private boolean isFragmentUI = false;
	private VisitorsNewsDetailsFragment visitorsNewsDetailsFragment = null;
	private View view = null;

	public SynchronizeVisitorsNewsDetailsTask(int newsId) {
		this.newsId = newsId;
	}

	public SynchronizeVisitorsNewsDetailsTask(int newsId, String detailsXMLURL, VisitorsNewsDetailsFragment visitorsNewsDetailsFragment) {
		this.newsId = newsId;
		this.detailsXMLURL = detailsXMLURL;
		isFragmentUI = true;
		this.visitorsNewsDetailsFragment = visitorsNewsDetailsFragment;
	}

	@Override
	protected Void doInBackground(Context... context) {
		this.activity = context[0];
		publishProgress("progress");
		if (!isFragmentUI) {
			VisitorsNewsDetailsActivity visitorsNewsDetailsActivity = (VisitorsNewsDetailsActivity) this.activity;
			int newsId = visitorsNewsDetailsActivity.newsId;
			detailsXMLURL = visitorsNewsDetailsActivity.detailsXMLURL;
		}
		NewsSynchronization newsSynchronization = new NewsSynchronization();
		newsSynchronization.setup(context[0]);

		newsSynchronization.openDatabase();

//		publishProgress("progress");

		NewsXMLParser newsXMLParser = new NewsXMLParser();
		try {
			NewsDetailsObject newsDetailsObject = newsXMLParser.parseVisitorDetails(detailsXMLURL);
			newsSynchronization.updateNews(newsId, newsDetailsObject);
		} catch (Exception e) {
			e.printStackTrace();
		}

		newsSynchronization.closeDatabase();

		// progress.dismiss();

		return null;
	}

	/**
	 * Once the visitors news synchronize task has been executed, launch the
	 * visitors news activity.
	 */
	@Override
	protected void onPostExecute(Void result) {
		try {
			if (!isFragmentUI) {
				Intent intent = new Intent();
				intent.setClass(activity, VisitorsNewsDetailsActivity.class);
				VisitorsNewsDetailsActivity visitorsNewsDetailsActivity = (VisitorsNewsDetailsActivity) this.activity;
				intent.putExtra("index", visitorsNewsDetailsActivity.newsId);

				activity.startActivity(intent);

				visitorsNewsDetailsActivity.overridePendingTransition(0, 0);
			} else {
				DataHolder._id = newsId;
				visitorsNewsDetailsFragment.createNewsDetailsObject();
//				progress.dismiss();
			}
		} catch (Exception e) {
			// System.out.println("Exception e SynchronizeVisitorsNewsDetailsTask "+
			// e.getMessage());
		}
	}
}
