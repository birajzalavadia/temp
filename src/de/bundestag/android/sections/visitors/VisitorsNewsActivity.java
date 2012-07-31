package de.bundestag.android.sections.visitors;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import de.bundestag.android.BaseActivity;
import de.bundestag.android.R;
import de.bundestag.android.constant.AppConstant;
import de.bundestag.android.constant.DataHolder;
import de.bundestag.android.storage.NewsDatabaseAdapter;
import de.bundestag.android.synchronization.SynchronizeCheckVisitorsNewsTask;
import de.bundestag.android.synchronization.SynchronizeVisitorsNewsTask;

public class VisitorsNewsActivity extends BaseActivity {
	public static final String SHOW_VISITOR_NEWS_KEY = "SHOW_VISITOR_NEWS";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState, this);
		if(AppConstant.isFragmentSupported){
			DataHolder.dismissProgress();
			}

        if (DataHolder.isOnline(this))
        {
            boolean showNews = false;
            Intent launchingIntent = getIntent();
            Bundle extras = launchingIntent.getExtras();
            if (extras != null)
            {
                showNews = extras.containsKey(SHOW_VISITOR_NEWS_KEY);
            }
            if (showNews)
            {
                setContentView(R.layout.visitors_news);
            }
            else
            {
                checkVisitorNewsNeedsUpdating();
            }
        }
        else
        {
            Intent intent = new Intent();
            intent.setClass(this, VisitorsOffersActivity.class);
            startActivity(intent);
            overridePendingTransition(0,0);
        }
    }

	/**
	 * Update the visitor news.
	 */
	private void checkVisitorNewsNeedsUpdating() {
		NewsDatabaseAdapter newsDatabaseAdapter = new NewsDatabaseAdapter(this);
		newsDatabaseAdapter.open();
		boolean existNews = newsDatabaseAdapter.existVisitorNews();
		newsDatabaseAdapter.close();

		if (existNews) {
			SynchronizeCheckVisitorsNewsTask synchronizeCheckVisitorsNewsTask = new SynchronizeCheckVisitorsNewsTask();
			synchronizeCheckVisitorsNewsTask.execute(this);
		} else {
			SynchronizeVisitorsNewsTask synchronizeVisitorsNewsTask = new SynchronizeVisitorsNewsTask();
			synchronizeVisitorsNewsTask.execute(this);
		}
	}

	/**
	 * Hack to handle the back button.
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			Intent intent = new Intent();
			intent.setClass(this, VisitorsOffersActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			this.startActivity(intent);
			overridePendingTransition(0, 0);
			return true;
		}

		return super.onKeyDown(keyCode, event);
	}
	
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
//		DataHolder.dismissProgress();
	}
}
