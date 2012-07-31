package de.bundestag.android.synchronization;

import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import de.bundestag.android.helpers.ImageHelper;
import de.bundestag.android.parser.NewsXMLParser;
import de.bundestag.android.parser.objects.NewsDetailsObject;
import de.bundestag.android.parser.objects.NewsListObject;
import de.bundestag.android.parser.objects.NewsObject;
import de.bundestag.android.storage.NewsDatabaseAdapter;

/**
 * News synchronization class.
 * 
 * Knows how to synchronize the news.
 */
public class NewsSynchronization
{
    public static final int NEWS_TYPE_NORMAL = 1;

    public static final int NEWS_TYPE_VISITOR = 2;

    public static final int NEWS_TYPE_LIST = 3;
    
    public static final int NEWS_TYPE_DEBATE = 4;

    private Context context;
    
    NewsDatabaseAdapter newsDatabaseAdapter;

    /**
     * Setup news.
     */
    public void setup(Context context)
    {
        this.context = context;
    }

    /**
     * Call the news parser and parse the latest news. Fill in all the news details.
     */
    public List<NewsObject> parseNews()
    {
        NewsXMLParser newsParser = new NewsXMLParser();
        List<NewsObject> news = newsParser.parse(true, NEWS_TYPE_NORMAL);
        
        return news;
    }

    /**
     * Call the news parser and parse the latest news. Don't fill in the news details.
     */
    public List<NewsObject> parseMainNews()
    {
        NewsXMLParser newsParser = new NewsXMLParser();
        List<NewsObject> news = newsParser.parse(false, NEWS_TYPE_NORMAL);
        
        return news;
    }


    public List<NewsObject> parseNewsDates()
    {
        NewsXMLParser newsParser = new NewsXMLParser();
        List<NewsObject> news = newsParser.parseDates(NEWS_TYPE_NORMAL, null);
        
        return news;
    }

    /**
     * Parse the news belonging to visitors.
     */
    public List<NewsObject> parseVisitorNews()
    {
        NewsXMLParser newsParser = new NewsXMLParser();
        List<NewsObject> news = newsParser.parse(false, NEWS_TYPE_VISITOR);
        
        return news;
    }
    
    public List<NewsObject> parseVisitorNewsDates()
    {
        NewsXMLParser newsParser = new NewsXMLParser();
        List<NewsObject> news = newsParser.parseDates(NEWS_TYPE_VISITOR, null);
        
        return news;
    }

    /**
     * Parse the news belonging to Debate section.
     */
    public List<NewsObject> parseDebateNews()
    {
        NewsXMLParser newsParser = new NewsXMLParser();
        List<NewsObject> news = newsParser.parse(true, NEWS_TYPE_DEBATE);
        
        return news;
    }

    /**
     * Parse the news belonging to Debate section.
     */
    public List<NewsObject> parseDebateNewsDates()
    {
        NewsXMLParser newsParser = new NewsXMLParser();
        List<NewsObject> news = newsParser.parseDates(NEWS_TYPE_DEBATE, null);
        
        return news;
    }

    /**
     * Load and insert the pictures.
     */
    public void insertPictures(List<NewsObject> news)
    {
        for (int i = 0; i < news.size(); i++)
        {
            NewsObject newsObject = (NewsObject) news.get(i);
            insertPicture(newsObject);
            
            if (newsObject.getIsList())
            {
                NewsListObject newsSubList = newsObject.getNewsList();
                if (newsSubList != null)
                {
                    List<NewsObject> subNews = newsSubList.getItems();
                    for (int j = 0; j < subNews.size(); j++)
                    {
                        NewsObject subNewsObject = (NewsObject) subNews.get(j);
                        insertPicture(subNewsObject);
                    }
                }
            }
        }
    }

    /**
     * Load and insert the pictures.
     */
    public void insertPicture(NewsObject newsObject)
    {
        Bitmap bitmap = ImageHelper.loadBitmapFromUrl(newsObject.getImageURL());
        if (bitmap != null)
        {
            newsObject.setImageString(ImageHelper.convertBitmapToString(bitmap));
        }

        NewsDetailsObject newsDetailsObject = newsObject.getNewsDetails();
        if (newsDetailsObject != null)
        {
            bitmap = ImageHelper.loadBitmapFromUrl(newsDetailsObject.getImageURL());
            if (bitmap != null)
            {
                newsDetailsObject.setImageString(ImageHelper.convertBitmapToString(bitmap));
            }
        }
    }

    /**
     * Open the database and remove all news.
     */
    public void deleteAllNews()
    {
        newsDatabaseAdapter.deleteAllNews();
    }

    /**
     * Open the database and remove all news.
     */
    public void deleteNewsNews()
    {
        newsDatabaseAdapter.deleteNewsNews();
    }

    /**
     * Open the database and remove all news.
     */
    public void deleteVisitorNews()
    {
        newsDatabaseAdapter.deleteVisitorNews();
    }

    /**
     * Open the database and remove all news.
     */
    public void deleteDebateNews()
    {
        newsDatabaseAdapter.deleteDebateNews();
    }

    /**
     * Open the database.
     */
    public void openDatabase()
    {
        if (newsDatabaseAdapter == null)
        {
            newsDatabaseAdapter = new NewsDatabaseAdapter(context);
        }

        newsDatabaseAdapter.open();
    }

    /**
     * Insert a news.
     */
    public long insertANews(NewsObject newsObject)
    {
        return newsDatabaseAdapter.createNews(newsObject);
    }

    /**
     * Insert a news list.
     */
    public long insertANewsList(NewsListObject newsListObject)
    {
        return newsDatabaseAdapter.createNewsList(newsListObject);
    }

    public void closeDatabase()
    {
        newsDatabaseAdapter.close();
    }

    public Cursor getNewsFromDetailsURL(String newsDetailsURL)
    {
        return newsDatabaseAdapter.fetchNewsFromDetailsURL(newsDetailsURL);
    }

    public void deleteNews(String newsDetailsURL)
    {
        newsDatabaseAdapter.deleteNewsFromDetailsURL(newsDetailsURL);
    }

    public long updateNews(int newsId, NewsDetailsObject newsDetailsObject)
    {
        return newsDatabaseAdapter.updateNews(newsId, newsDetailsObject);
    }
}
