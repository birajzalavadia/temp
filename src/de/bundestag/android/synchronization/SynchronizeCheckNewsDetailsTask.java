package de.bundestag.android.synchronization;

import java.util.Date;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import de.bundestag.android.constant.AppConstant;
import de.bundestag.android.helpers.DateHelper;
import de.bundestag.android.parser.NewsXMLParser;
import de.bundestag.android.parser.objects.NewsDetailsObject;
import de.bundestag.android.sections.news.NewsActivity;
import de.bundestag.android.sections.news.NewsActivityTablet;
import de.bundestag.android.sections.news.NewsDetailsActivity;
import de.bundestag.android.storage.NewsDatabaseAdapter;

public class SynchronizeCheckNewsDetailsTask extends BaseSynchronizeTask {
	@Override
	protected Void doInBackground(Context... context) {
		this.activity = context[0];
		publishProgress("progress");
		NewsSynchronization newsSynchronization = new NewsSynchronization();
		newsSynchronization.setup(context[0]);

		NewsDetailsActivity newsDetailsActivity = (NewsDetailsActivity) this.activity;
		int newsId = newsDetailsActivity.newsId;
		String detailsXMLURL = newsDetailsActivity.detailsXMLURL;

		newsSynchronization.openDatabase();

		NewsDetailsObject newsDetailsObject = null;
		NewsXMLParser newsXMLParser = new NewsXMLParser();
		try {
			newsDetailsObject = newsXMLParser.parseDetailsDates(detailsXMLURL);
		} catch (Exception e) {
			e.printStackTrace();
		}

		boolean hasChanged = false;
		Cursor newsCursor = newsSynchronization.getNewsFromDetailsURL(detailsXMLURL);
		if (newsDetailsObject != null) {
			Date newsDatabaseLastChanged = null;
			Date newsLastChanged = null;
			if ((newsCursor != null) && (newsCursor.getCount() > 0)) {
				String newsDatabaseLastChangedString = newsCursor.getString(newsCursor.getColumnIndex(NewsDatabaseAdapter.KEY_DETAILS_IMAGECHANGEDDATETIME));
				newsDatabaseLastChanged = DateHelper.parseDate(newsDatabaseLastChangedString);
				newsLastChanged = newsDetailsObject.getImageChangedDateTimeDate();
			}

			if ((newsCursor == null) || (newsCursor.getCount() == 0) || (newsDatabaseLastChanged.before(newsLastChanged))) {
				System.out.println("HAS CHANGED");
				hasChanged = true;
			}
		}
		newsCursor.close();

		if (hasChanged) {
			// something has changed, so throw that particular news
			newsSynchronization.deleteNews(detailsXMLURL);

			try {
				newsDetailsObject = newsXMLParser.parseDetails(detailsXMLURL);
			} catch (Exception e) {
				e.printStackTrace();
			}

			if (newsDetailsObject != null) {
				newsSynchronization.updateNews(newsId, newsDetailsObject);
			}
		}

		// progress.dismiss();

		newsSynchronization.closeDatabase();

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
			if (AppConstant.isFragmentSupported)
				intent.setClass(activity, NewsActivityTablet.class);
			else
				intent.setClass(activity, NewsActivity.class);
			intent.putExtra(NewsActivity.SHOW_NEWS_KEY, true);
			activity.startActivity(intent);
			if (AppConstant.isFragmentSupported) {
				NewsActivityTablet newsActivity = (NewsActivityTablet) activity;
				newsActivity.overridePendingTransition(0, 0);

			} else {
				NewsActivity newsActivity = (NewsActivity) activity;
				newsActivity.overridePendingTransition(0, 0);
			}
		} catch (Exception e) {
			System.out.println("Exception e SynchronizeCheckNewsDetailsTask");
		}
	}
}
