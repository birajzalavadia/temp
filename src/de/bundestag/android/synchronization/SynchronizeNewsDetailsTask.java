package de.bundestag.android.synchronization;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import de.bundestag.android.constant.DataHolder;
import de.bundestag.android.parser.NewsXMLParser;
import de.bundestag.android.parser.objects.NewsDetailsObject;
import de.bundestag.android.sections.news.NewsDetailsActivity;
import de.bundestag.android.sections.news.NewsDetailsFragment;
import de.bundestag.android.sections.news.NewsListActivity;

public class SynchronizeNewsDetailsTask extends BaseSynchronizeTask {
	private int newsId = 0;
	private String detailsXMLURL = "";
	private boolean isFragmentUI = false;
	private NewsDetailsFragment newsDetailsFragment = null;
	private View view = null;

	public SynchronizeNewsDetailsTask() {

	}

	public SynchronizeNewsDetailsTask(int newsId, String detailsXMLURL, NewsDetailsFragment newsDetailsFragment, View view) {
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
		NewsSynchronization newsSynchronization = new NewsSynchronization();
		newsSynchronization.setup(context[0]);

		if (!isFragmentUI) {
			NewsDetailsActivity newsDetailsActivity = (NewsDetailsActivity) this.activity;
			newsId = newsDetailsActivity.newsId;
			detailsXMLURL = newsDetailsActivity.detailsXMLURL;
		}
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
			if (!isFragmentUI) {
				Intent intent = new Intent();
				intent.setClass(activity, NewsDetailsActivity.class);
				NewsDetailsActivity newsDetailsActivity = (NewsDetailsActivity) this.activity;
				intent.putExtra("index", newsDetailsActivity.newsId);
				if (newsDetailsActivity.listId != null) {
					intent.putExtra(NewsListActivity.KEY_NEWS_LISTID, newsDetailsActivity.listId);
				}
				activity.startActivity(intent);
				newsDetailsActivity.overridePendingTransition(0, 0);
			} else {
				DataHolder._id = newsId;
				newsDetailsFragment.createNewsDetailsObject();
//				progress.dismiss();
			}
		} catch (Exception e) {
			System.out.println("Exception e SynchronizeNewsDetailsTask" + e.getMessage());
		}
	}
}
