package de.bundestag.android.synchronization;

import java.util.Date;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import de.bundestag.android.BaseActivity;
import de.bundestag.android.constant.AppConstant;
import de.bundestag.android.helpers.DateHelper;
import de.bundestag.android.parser.NewsXMLParser;
import de.bundestag.android.parser.objects.NewsDetailsObject;
import de.bundestag.android.parser.objects.NewsListObject;
import de.bundestag.android.parser.objects.NewsObject;
import de.bundestag.android.sections.news.NewsActivity;
import de.bundestag.android.sections.news.NewsActivityTablet;
import de.bundestag.android.storage.NewsDatabaseAdapter;

public class SynchronizeUpdateNewsTask extends BaseSynchronizeTask {
	@Override
	protected Void doInBackground(Context... context) {
		this.activity = context[0];
		publishProgress("progress");
		NewsSynchronization newsSynchronization = new NewsSynchronization();
		newsSynchronization.setup(context[0]);

		newsSynchronization.openDatabase();

		

		// Parse main news list.
		List<NewsObject> news = null;
		try {
			news = newsSynchronization.parseMainNews();
		} catch (Exception e) {
			e.printStackTrace();

			newsSynchronization.closeDatabase();

			return null;
		}

		// NOTE - we don't have a news id!!! So we use the URL
		int insertsSize = BaseActivity.isDebugOn() ? ((BaseActivity.DEBUG_SYNCHRONIZE_NUMBER > news.size()) ? news.size() : BaseActivity.DEBUG_SYNCHRONIZE_NUMBER) : news.size();
		for (int i = 0; i < insertsSize; i++) {
			// publishProgress("Nachrichten werden auf vorhandene Aktualisierungen überprüft "
			// + (i + 1) + "/" + insertsSize + ".");
			NewsObject newsObject = (NewsObject) news.get(i);

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

				// If news doesn't exist or it has been changed, insert it in
				// the database.
				if ((newsCursor == null) || (newsCursor.getCount() == 0) || (newsDatabaseLastChanged.before(newsLastChanged))) {
					// publishProgress("Nachrichten werden aktualisiert " + (i +
					// 1) + "/" + insertsSize + ".");

					try {
						// Parse the new or updated news.
						NewsXMLParser newsXMLParser = new NewsXMLParser();

						if (!newsObject.getIsList()) {
							NewsDetailsObject newsDetailsObject = newsXMLParser.parseDetails(newsObject.getDetailsXMLURL());
							newsObject.setNewsDetails(newsDetailsObject);

							// Check the news image date, to see if it has been
							// updated.
							if ((newsCursor != null) && (newsCursor.getCount() > 0)) {
								Date imageLastChanged = newsDetailsObject.getImageChangedDateTimeDate();
								String imageDatabaseLastChangedString = newsCursor.getString(newsCursor.getColumnIndex(NewsDatabaseAdapter.KEY_DETAILS_IMAGECHANGEDDATETIME));
								Date imageDatabaseLastChanged = DateHelper.parseDate(imageDatabaseLastChangedString);

								// Insert the news pictures.
								if (imageDatabaseLastChanged.before(imageLastChanged)) {
									newsSynchronization.insertPicture(newsObject);
								} else {
									newsObject.setImageString(newsCursor.getString(newsCursor.getColumnIndex(NewsDatabaseAdapter.KEY_DETAILS_IMAGE_STRING)));
								}
							} else {
								newsSynchronization.insertPicture(newsObject);
							}
						}

						// Delete the news if it already exists in the database.
						if ((newsCursor != null) && (newsCursor.getCount() > 0)) {
							newsSynchronization.deleteNews(newsDetailsURL);
						}

						// Store the new or updated news in the database
						long newsId = newsSynchronization.insertANews(newsObject);

						NewsListObject newsList = newsObject.getNewsList();
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
					} catch (Exception e) {
						e.printStackTrace();
					}
				}

				newsCursor.close();
			}
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
			if(AppConstant.isFragmentSupported){
				intent.setClass(activity, NewsActivityTablet.class);
			}else{
				intent.setClass(activity, NewsActivity.class);
			}
			intent.putExtra(NewsActivity.SHOW_NEWS_KEY, true);
			activity.startActivity(intent);
		} catch (Exception e) {
			// System.out.println("Exception e SynchronizeUpdateNewsTask "+
			// e.getMessage());
		}
	}
}
