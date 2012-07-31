package de.bundestag.android.synchronization;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import de.bundestag.android.constant.AppConstant;
import de.bundestag.android.parser.objects.NewsListObject;
import de.bundestag.android.parser.objects.NewsObject;
import de.bundestag.android.sections.visitors.VisitorsNewsActivity;
import de.bundestag.android.sections.visitors.VisitorsNewsActivityTablet;

public class SynchronizeCheckVisitorsNewsTask extends BaseSynchronizeTask {
	@Override
	protected Void doInBackground(Context... context) {
		this.activity = context[0];
		publishProgress("progress");
		NewsSynchronization newsSynchronization = new NewsSynchronization();
		newsSynchronization.setup(context[0]);

		newsSynchronization.openDatabase();

		List<NewsObject> news = null;
		try {
			news = newsSynchronization.parseVisitorNewsDates();
		} catch (Exception e) {
			newsSynchronization.closeDatabase();

			return null;
		}

		boolean hasChanged = false;
		for (int i = 0; i < news.size(); i++) {
			NewsObject newsObject = (NewsObject) news.get(i);

			// we don't have a news id, so we use the xml to check if there are
			// changes
			if ((newsObject.getDetailsXMLURL() != null) && (!newsObject.getDetailsXMLURL().equals(""))) {
				String newsDetailsURL = newsObject.getDetailsXMLURL();
				Cursor newsCursor = newsSynchronization.getNewsFromDetailsURL(newsDetailsURL);

				// Date newsDatabaseLastChanged = null;
				// Date newsLastChanged = null;
				// if ((newsCursor != null) && (newsCursor.getCount() > 0))
				// {
				// String newsDatabaseLastChangedString =
				// newsCursor.getString(newsCursor.getColumnIndex(NewsDatabaseAdapter.KEY_CHANGEDDATETIME));
				// newsDatabaseLastChanged =
				// DateHelper.parseDate(newsDatabaseLastChangedString);
				// newsLastChanged = newsObject.getChangedDateTimeDate();
				// }

				if ((newsCursor == null) || (newsCursor.getCount() == 0)) // ||
																			// (newsDatabaseLastChanged.before(newsLastChanged)))
				{
					System.out.println("HAS CHANGED");
					hasChanged = true;
				}

				newsCursor.close();
			}
		}

		if (hasChanged) {
			// something has changed, so throw out all the news
			newsSynchronization.deleteVisitorNews();

			try {
				news = newsSynchronization.parseVisitorNews();
			} catch (Exception e) {
				newsSynchronization.closeDatabase();

				return null;
			}

			try {
				for (int i = 0; i < news.size(); i++) {
					NewsObject newsObject = (NewsObject) news.get(i);

					newsSynchronization.insertANews(newsObject);

					NewsListObject newsList = newsObject.getNewsList();
					if (newsList != null) {
						long newsListId = newsSynchronization.insertANewsList(newsList);

						List<NewsObject> newsSubItems = newsList.getItems();
						for (int j = 0; j < newsSubItems.size(); j++) {
							NewsObject newsObjectSub = (NewsObject) newsSubItems.get(j);
							newsObjectSub.setListId(newsListId);
							newsSynchronization.insertANews(newsObjectSub);
						}
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		newsSynchronization.closeDatabase();

		return null;
	}

	/**
	 * Once the visitors news synchronize task has been executed, launch the
	 * visitors news activity.
	 */
	@Override
	protected void onPostExecute(Void result) {
		try {
			Intent intent = new Intent();
			if (AppConstant.isFragmentSupported) {
				intent.setClass(activity, VisitorsNewsActivityTablet.class);
			} else {
				intent.setClass(activity, VisitorsNewsActivity.class);
			} 
			intent.putExtra(VisitorsNewsActivity.SHOW_VISITOR_NEWS_KEY, true);
			activity.startActivity(intent);

			if (activity instanceof VisitorsNewsActivity) {
				VisitorsNewsActivity visitorsNewsActivity = (VisitorsNewsActivity) activity;
				visitorsNewsActivity.overridePendingTransition(0, 0);
			} else if (activity instanceof VisitorsNewsActivityTablet) {
				VisitorsNewsActivityTablet visitorsNewsActivity = (VisitorsNewsActivityTablet) activity;
				visitorsNewsActivity.overridePendingTransition(0, 0);
			}
		} catch (Exception e) {
			// System.out.println("Exception e  SynchronizeCheckVisitorsNewsTask "+
			// e.getMessage());
		}
	}
}
