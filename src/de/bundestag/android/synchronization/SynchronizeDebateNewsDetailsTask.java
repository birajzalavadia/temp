package de.bundestag.android.synchronization;

import android.content.Context;
import android.content.Intent;
import de.bundestag.android.parser.NewsXMLParser;
import de.bundestag.android.parser.objects.NewsDetailsObject;
import de.bundestag.android.sections.plenum.DebateNewsDetailsActivity;

public class SynchronizeDebateNewsDetailsTask extends BaseSynchronizeTask {
	@Override
	protected Void doInBackground(Context... context) {
		this.activity = context[0];
		publishProgress("progress");
		NewsSynchronization newsSynchronization = new NewsSynchronization();
		newsSynchronization.setup(context[0]);

		DebateNewsDetailsActivity debateNewsDetailsActivity = (DebateNewsDetailsActivity) this.activity;
		int newsId = debateNewsDetailsActivity.newsId;
		String detailsXMLURL = debateNewsDetailsActivity.detailsXMLURL;

		newsSynchronization.openDatabase();

		

		NewsXMLParser newsXMLParser = new NewsXMLParser();
		try {
			NewsDetailsObject newsDetailsObject = newsXMLParser.parseDetails(detailsXMLURL);

			newsSynchronization.updateNews(newsId, newsDetailsObject);
		} catch (Exception e) {
			e.printStackTrace();
		}

		newsSynchronization.closeDatabase();

		// progress.dismiss();

		return null;
	}

	/**
	 * Once the news synchronize task has been executed, launch the news
	 * activity.
	 */
	@Override
	protected void onPostExecute(Void result) {
		try {
			Intent intent = new Intent();
			intent.setClass(activity, DebateNewsDetailsActivity.class);
			DebateNewsDetailsActivity debateNewsDetailsActivity = (DebateNewsDetailsActivity) this.activity;
			intent.putExtra("index", debateNewsDetailsActivity.newsId);

			activity.startActivity(intent);

			debateNewsDetailsActivity.overridePendingTransition(0, 0);
		} catch (Exception e) {
			System.out.println("Exception e SynchronizeDebateNewsDetailsTask : " + e.getMessage());
		}
	}
}
