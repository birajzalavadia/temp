package de.bundestag.android.synchronization;

import java.util.Date;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import de.bundestag.android.helpers.DateHelper;
import de.bundestag.android.parser.objects.NewsListObject;
import de.bundestag.android.parser.objects.NewsObject;
import de.bundestag.android.sections.plenum.DebateNewsActivity;
import de.bundestag.android.storage.NewsDatabaseAdapter;

public class SynchronizeCheckDebateNewsTask extends BaseSynchronizeTask {
	@Override
	protected Void doInBackground(Context... context) {
		this.activity = context[0];
		publishProgress("progress");
		NewsSynchronization newsSynchronization = new NewsSynchronization();
		newsSynchronization.setup(context[0]);

		

		newsSynchronization.openDatabase();

		List<NewsObject> news = null;
		try {
			news = newsSynchronization.parseDebateNewsDates();
		} catch (Exception e) {
			e.printStackTrace();

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

				Date newsDatabaseLastChanged = null;
				Date newsLastChanged = null;
				if ((newsCursor != null) && (newsCursor.getCount() > 0)) {
					String newsDatabaseLastChangedString = newsCursor.getString(newsCursor.getColumnIndex(NewsDatabaseAdapter.KEY_CHANGEDDATETIME));
					newsDatabaseLastChanged = DateHelper.parseDate(newsDatabaseLastChangedString);
					newsLastChanged = newsObject.getChangedDateTimeDate();
				}

				if ((newsCursor == null) || (newsCursor.getCount() == 0) || (newsDatabaseLastChanged.before(newsLastChanged))) {
					System.out.println("HAS CHANGED");
					hasChanged = true;
				}

				newsCursor.close();
			}
		}

		if (hasChanged) {
			// something has changed, so throw out all the news
			newsSynchronization.deleteDebateNews();

			try {
				news = newsSynchronization.parseDebateNews();
			} catch (Exception e) {
				e.printStackTrace();

				newsSynchronization.closeDatabase();

				return null;
			}

			for (int i = 0; i < news.size(); i++) {
				NewsObject newsObject = (NewsObject) news.get(i);

				NewsListObject newsList = newsObject.getNewsList();
				// Hack to fix problem with wrong type for news articles.
				if (newsList == null) {
					newsObject.setType(NewsSynchronization.NEWS_TYPE_DEBATE);
				}

				newsSynchronization.insertANews(newsObject);
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
		}
		// progress.dismiss();

		newsSynchronization.closeDatabase();

		return null;
	}

	/**
	 * Once the debate news synchronize task has been executed, launch the
	 * debate news activity.
	 */
	@Override
	protected void onPostExecute(Void result) {
		try {
			Intent intent = new Intent();
			intent.setClass(activity, DebateNewsActivity.class);
			intent.putExtra(DebateNewsActivity.SHOW_DEBATE_NEWS_KEY, true);
			activity.startActivity(intent);

			if (activity instanceof DebateNewsActivity) {
				DebateNewsActivity debateNewsActivity = (DebateNewsActivity) activity;
				debateNewsActivity.overridePendingTransition(0, 0);
			}
		} catch (Exception e) {
			System.out.println("Exception in SynchronizeCheckDebateNewsTask");
		}
	}
}
