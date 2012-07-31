package de.bundestag.android.sections.plenum;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import de.bundestag.android.BaseActivity;
import de.bundestag.android.R;
import de.bundestag.android.constant.DataHolder;
import de.bundestag.android.storage.NewsDatabaseAdapter;
import de.bundestag.android.synchronization.SynchronizeCheckDebateNewsTask;
import de.bundestag.android.synchronization.SynchronizeDebateNewsTask;

public class DebateNewsActivity extends BaseActivity
{
    public static final String SHOW_DEBATE_NEWS_KEY = "SHOW_DEBATE_NEWS";

	@Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState, this);

        if (DataHolder.isOnline(this))
        {
            boolean showNews = false;
            Intent launchingIntent = getIntent();
            Bundle extras = launchingIntent.getExtras();
            if (extras != null)
            {
                showNews = extras.containsKey(SHOW_DEBATE_NEWS_KEY);
            }
            if (showNews)
            {
                setContentView(R.layout.visitors_news);
            }
            else
            {
                checkDebateNewsNeedsUpdating();
            }
        }
        else
        {
            Intent intent = new Intent();
            intent.setClass(this, PlenumSitzungenActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            overridePendingTransition(0,0);
        }
    }

    /**
     * Update the debate news.
     */
    private void checkDebateNewsNeedsUpdating()
    {
        NewsDatabaseAdapter newsDatabaseAdapter = new NewsDatabaseAdapter(this);
        newsDatabaseAdapter.open();
        boolean existNews = newsDatabaseAdapter.existDebateNews();
        newsDatabaseAdapter.close();

        if (existNews)
        {
            SynchronizeCheckDebateNewsTask synchronizeCheckDebateNewsTask = new SynchronizeCheckDebateNewsTask();
            synchronizeCheckDebateNewsTask.execute(this);
        }
        else
        {
            SynchronizeDebateNewsTask synchronizeDebateNewsTask = new SynchronizeDebateNewsTask();
            synchronizeDebateNewsTask.execute(this);
        }
    }

    /**
     * Hack to handle the back button.
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if (keyCode == KeyEvent.KEYCODE_BACK)
        {
            Intent intent = new Intent();
            intent.setClass(this, PlenumSitzungenActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            this.startActivity(intent);
            overridePendingTransition(0,0);
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }
}
