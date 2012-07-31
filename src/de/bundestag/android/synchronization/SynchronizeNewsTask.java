package de.bundestag.android.synchronization;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import de.bundestag.android.BaseActivity;
import de.bundestag.android.constant.AppConstant;
import de.bundestag.android.constant.DataHolder;
import de.bundestag.android.parser.objects.NewsListObject;
import de.bundestag.android.parser.objects.NewsObject;
import de.bundestag.android.sections.news.NewsActivity;
import de.bundestag.android.sections.news.NewsActivityTablet;
import de.bundestag.android.sections.news.NewsStartActivity;

public class SynchronizeNewsTask extends BaseSynchronizeTask {
	@Override
	protected Void doInBackground(Context... context) {
		this.activity = context[0];
		if (activity instanceof NewsStartActivity) {
			// ((NewsStartActivity)
			// activity).setContentView(R.layout.splash_screen);
		} else {
			publishProgress("progress");
		}
		NewsSynchronization newsSynchronization = new NewsSynchronization();
		newsSynchronization.setup(context[0]);

		newsSynchronization.openDatabase();

		

		List<NewsObject> news = null;
		try {
			news = newsSynchronization.parseNews();
		} catch (Exception e) {
			e.printStackTrace();

			newsSynchronization.closeDatabase();

			return null;
		}

		if (news != null) {
			try {
				int insertsSize = BaseActivity.isDebugOn() ? ((BaseActivity.DEBUG_SYNCHRONIZE_NUMBER > news.size()) ? news.size() : BaseActivity.DEBUG_SYNCHRONIZE_NUMBER) : news
						.size();
				for (int i = 0; i < insertsSize; i++) {
					NewsObject newsObject = (NewsObject) news.get(i);

					NewsListObject newsList = newsObject.getNewsList();
					// Hack to fix problem with wrong type for news articles.
					if (newsList == null) {
						newsObject.setType(NewsSynchronization.NEWS_TYPE_NORMAL);
					}

					long newsId = newsSynchronization.insertANews(newsObject);

					if (newsList != null) {
						newsList.setParentNewsId(String.valueOf(newsId));

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

		if (activity instanceof NewsStartActivity) {
			// REMOVE SPLASH SCREEN??

		} else {
			// progress.dismiss();
		}
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
			if(AppConstant.isFragmentSupported)
				intent.setClass(activity, NewsActivityTablet.class);
			else
				intent.setClass(activity, NewsActivity.class);
			
			intent.putExtra(NewsActivity.SHOW_NEWS_KEY, true);
			activity.startActivity(intent);

			if (activity instanceof NewsActivity) {
				NewsActivity newsActivity = (NewsActivity) activity;
				newsActivity.overridePendingTransition(0, 0);
			} else if (activity instanceof NewsStartActivity) {
				NewsStartActivity newsStartActivity = (NewsStartActivity) activity;
				newsStartActivity.overridePendingTransition(0, 0);
				DataHolder.releaseScreenLock(DataHolder.currentActiviry);
			}
		} catch (Exception e) {
			System.out.println("Exception e SynchronizeNewsTask "+ e.getMessage());
		}
	}
}
