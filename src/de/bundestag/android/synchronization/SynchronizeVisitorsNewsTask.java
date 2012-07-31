package de.bundestag.android.synchronization;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import de.bundestag.android.BaseActivity;
import de.bundestag.android.constant.AppConstant;
import de.bundestag.android.parser.objects.NewsListObject;
import de.bundestag.android.parser.objects.NewsObject;
import de.bundestag.android.sections.visitors.VisitorsNewsActivity;
import de.bundestag.android.sections.visitors.VisitorsNewsActivityTablet;

public class SynchronizeVisitorsNewsTask extends BaseSynchronizeTask {
	@Override
	protected Void doInBackground(Context... context) {
		this.activity = context[0];

		NewsSynchronization newsSynchronization = new NewsSynchronization();
		newsSynchronization.setup(context[0]);

		newsSynchronization.openDatabase();

		publishProgress("progress");

		List<NewsObject> news = null;
		try {
			news = newsSynchronization.parseVisitorNews();
		} catch (Exception e) {
			e.printStackTrace();

			newsSynchronization.closeDatabase();

			return null;
		}

		// NewsXMLParser newsXMLParser = new NewsXMLParser();
		if (news != null) {
			try {
				int insertsSize = BaseActivity.isDebugOn() ? ((BaseActivity.DEBUG_SYNCHRONIZE_NUMBER > news.size()) ? news.size() : BaseActivity.DEBUG_SYNCHRONIZE_NUMBER) : news
						.size();
				for (int i = 0; i < insertsSize; i++) {
					NewsObject newsObject = (NewsObject) news.get(i);

					// NewsDetailsObject newsDetailsObject =
					// newsXMLParser.parseVisitorDetails(newsObject.getDetailsXMLURL());

					// VisitorsDatabaseAdapter visitorsDatabaseAdapter = new
					// VisitorsDatabaseAdapter(context[0]);
					// visitorsDatabaseAdapter.open();
					// Cursor pictureCursor =
					// visitorsDatabaseAdapter.getPicture(newsDetailsObject.getImageURL());
					// if (pictureCursor.getCount() > 0)
					// {
					// String imageString =
					// pictureCursor.getString(pictureCursor.getColumnIndex(VisitorsDatabaseAdapter.KEY_ARTICLE_IMAGESTRING));
					// newsDetailsObject.setImageString(imageString);
					// }
					// pictureCursor.close();
					// visitorsDatabaseAdapter.close();

					// newsObject.setNewsDetails(newsDetailsObject);

					// publishProgress("Nachrichten werden gespeichert " + (i +
					// 1) + "/" + insertsSize);
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
			// System.out.println("Exception e SynchronizeVisitorsNewsTask "+
			// e.getMessage());
		}
	}
}
