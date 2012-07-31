package de.bundestag.android.synchronization;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import de.bundestag.android.BaseActivity;
import de.bundestag.android.parser.objects.NewsListObject;
import de.bundestag.android.parser.objects.NewsObject;
import de.bundestag.android.sections.plenum.DebateNewsActivity;

public class SynchronizeDebateNewsTask extends BaseSynchronizeTask
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
            news = newsSynchronization.parseDebateNews();
        } catch (Exception e)
        {
            e.printStackTrace();

            newsSynchronization.closeDatabase();

            return null;
        }

        if (news != null)
        {
            try
            {
                int insertsSize =
                        BaseActivity.isDebugOn()
                                ? ((BaseActivity.DEBUG_SYNCHRONIZE_NUMBER > news.size()) ? news.size() : BaseActivity.DEBUG_SYNCHRONIZE_NUMBER) : news.size();
                for (int i = 0; i < insertsSize; i++)
                {
                    NewsObject newsObject = (NewsObject) news.get(i);

                    NewsListObject newsList = newsObject.getNewsList();
                    // Hack to fix problem with wrong type for news articles.
                    if (newsList == null)
                    {
                        newsObject.setType(NewsSynchronization.NEWS_TYPE_DEBATE);
                    }

                    newsSynchronization.insertANews(newsObject);
                    if (newsList != null)
                    {
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
            } catch (Exception e)
            {
                e.printStackTrace();
            }
        }

        newsSynchronization.closeDatabase();

        //progress.dismiss();

        return null;
    }

    /**
     * Once the debate news synchronize task has been executed, launch the
     * debate news activity.
     */
    @Override
    protected void onPostExecute(Void result)
    {
    	try{
        Intent intent = new Intent();
        intent.setClass(activity, DebateNewsActivity.class);
        intent.putExtra(DebateNewsActivity.SHOW_DEBATE_NEWS_KEY, true);
        activity.startActivity(intent);

        if (activity instanceof DebateNewsActivity)
        {
            DebateNewsActivity debateNewsActivity = (DebateNewsActivity) activity;
            debateNewsActivity.overridePendingTransition(0, 0);
        }
    	}catch(Exception e){
//    		System.out.println("Exception e SynchronizeDebateNewsDetailsTask :" + e.getMessage());
    	}
    }
}
