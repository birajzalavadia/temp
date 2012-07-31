package de.bundestag.android.sections.plenum;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import de.bundestag.android.BaseActivity;
import de.bundestag.android.R;
import de.bundestag.android.constant.DataHolder;

public class PlenumDebatesActivity extends BaseActivity
{
	public static ArrayList<HashMap<String, Object>> debates;
	private boolean isInFront;
	private Timer waitTimer;
	@Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState, this);
        if (DataHolder.isOnline(this))
        {
            setContentView(R.layout.plenum_debates);
        }
        else
        {
            setContentView(R.layout.plenum_debates_offline);
        }
        
	   	        
		waitTimer = new Timer();
		
		waitTimer.scheduleAtFixedRate(new TimerTask() {
			@Override
			public void run() {
				TimerMethod();
		}
	
		},10000, 10000);
		Log.i("timer onCreate", "################## onCreate ");
		
	}
	
	protected void onResume() {
		super.onResume();
        isInFront = true;
    }

    protected void  onPause() {
    	super.onPause();
    	waitTimer.cancel();
        isInFront = false;
    }
    
	public void TimerMethod() {
	    //called every 300 milliseconds, which could be used to
	    //send messages or some other action
		if(isInFront){
			Intent intent = new Intent();
	        intent.setClass(this, PlenumDebatesActivity.class);
	        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
	        startActivity(intent);
	        this.overridePendingTransition(0, 0);    
		    Log.i("timer", "################## PlenumDebatesActivity refresh ");
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
            intent.setClass(this, PlenumNewsActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            this.startActivity(intent);
            overridePendingTransition(0,0);
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }    

    
}
