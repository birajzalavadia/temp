package de.bundestag.android.sections.news;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import de.bundestag.android.BaseActivity;
import de.bundestag.android.R;
import de.bundestag.android.constant.DataHolder;

public class NewsListActivity extends BaseActivity
{
	public static final String KEY_NEWS_LISTID = "NEWS_LISTID";

	public static int listId = -1;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState, this);

        if (DataHolder.isOnline(this))
        {
            Intent launchingIntent = getIntent();
            Bundle extras = launchingIntent.getExtras();
            NewsListActivity.listId = (Integer) extras.get(KEY_NEWS_LISTID);

            setContentView(R.layout.news_sub_news);
        }
        else
        {
            setContentView(R.layout.news_offline);
        }
    }

    /**
     * Hack to handle the back button for news sub list.
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if (keyCode == KeyEvent.KEYCODE_BACK)
        {
            Intent intent = new Intent();
            intent.putExtra(NewsActivity.SHOW_NEWS_KEY, true);
            intent.setClass(this, NewsActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            this.startActivity(intent);
            overridePendingTransition(0,0);
            DataHolder._subId = -1;
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }
}
