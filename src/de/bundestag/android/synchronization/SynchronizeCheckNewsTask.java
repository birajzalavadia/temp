package de.bundestag.android.synchronization;

import java.util.Date;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import de.bundestag.android.constant.AppConstant;
import de.bundestag.android.helpers.DateHelper;
import de.bundestag.android.parser.objects.NewsListObject;
import de.bundestag.android.parser.objects.NewsObject;
import de.bundestag.android.sections.news.NewsActivity;
import de.bundestag.android.sections.news.NewsActivityTablet;
import de.bundestag.android.storage.NewsDatabaseAdapter;

public class SynchronizeCheckNewsTask extends BaseSynchronizeTask
{
    @Override
    protected Void doInBackground(Context... context)
    {
        this.activity = context[0];
        publishProgress("progress");
        NewsSynchronization newsSynchronization = new NewsSynchronization();
        newsSynchronization.setup(context[0]);

        newsSynchronization.openDatabase();

       

        List<NewsObject> news = null;
        try
        {
            news = newsSynchronization.parseNewsDates();
        } catch (Exception e)
        {
            e.printStackTrace();

            newsSynchronization.closeDatabase();

            return null;
        }

        boolean hasChanged = false;
        for (int i = 0; i < news.size(); i++)
        {
            NewsObject newsObject = (NewsObject) news.get(i);

            // we don't have a news id, so we use the xml to check if there are changes
            if ((newsObject.getDetailsXMLURL() != null) && (!newsObject.getDetailsXMLURL().equals("")))
            {
                String newsDetailsURL = newsObject.getDetailsXMLURL();
                Cursor newsCursor = newsSynchronization.getNewsFromDetailsURL(newsDetailsURL);

                Date newsDatabaseLastChanged = null;
                Date newsLastChanged = null;
                if ((newsCursor != null) && (newsCursor.getCount() > 0))
                {
                    String newsDatabaseLastChangedString = newsCursor.getString(newsCursor.getColumnIndex(NewsDatabaseAdapter.KEY_CHANGEDDATETIME));
                    newsDatabaseLastChanged = DateHelper.parseDate(newsDatabaseLastChangedString);
                    newsLastChanged = newsObject.getChangedDateTimeDate();
                }

                if ((newsCursor == null) || (newsCursor.getCount() == 0) || (newsDatabaseLastChanged.before(newsLastChanged)))
                {
                    System.out.println("HAS CHANGED");
                    hasChanged = true;
                }

                newsCursor.close();
            }
        }

        if (hasChanged)
        {
            // something has changed, so throw out all the news
            newsSynchronization.deleteNewsNews();

            List<NewsObject> updatedNews = null;
            try
            {
                updatedNews = newsSynchronization.parseNews();
            } catch (Exception e)
            {
                e.printStackTrace();

                newsSynchronization.closeDatabase();

                return null;
            }

            for (int i = 0; i < updatedNews.size(); i++)
            {
                NewsObject newsObject = (NewsObject) updatedNews.get(i);

                NewsListObject newsList = newsObject.getNewsList();
                // Hack to fix problem with wrong type for news articles.
                if (newsList == null)
                {
                    newsObject.setType(NewsSynchronization.NEWS_TYPE_NORMAL);
                }

                long newsId = newsSynchronization.insertANews(newsObject);

                if (newsList != null)
                {
                    newsList.setParentNewsId(String.valueOf(newsId));

                    long newsListId = newsSynchronization.insertANewsList(newsList);

                    List<NewsObject> newsSubItems = newsList.getItems();
                    for (int j = 0; j < newsSubItems.size(); j++)
                    {
                        NewsObject newsObjectSub = (NewsObject) newsSubItems.get(j);
                        newsObjectSub.setListId(newsListId);
                        newsSynchronization.insertANews(newsObjectSub);
                    }
                }
            }            
        }

        newsSynchronization.closeDatabase();

        //progress.dismiss();

        return null;
    }

    /**
     * Once the news synchronize task has been executed, launch the news
     * activity.
     */
    @Override
    protected void onPostExecute(Void result)
    {
    	try{
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
    	}catch(Exception e){
//    		System.out.println("Excepiton e : SynchronizeCheckNewsTask ");
    	}
    }
}
