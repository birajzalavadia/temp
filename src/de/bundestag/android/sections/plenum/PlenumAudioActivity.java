package de.bundestag.android.sections.plenum;

import android.content.Intent;
import android.content.res.Configuration;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnBufferingUpdateListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import de.bundestag.android.BaseActivity;
import de.bundestag.android.R;
import de.bundestag.android.parser.PlenumXMLParser;
import de.bundestag.android.parser.objects.PlenumStreamObject;

public class PlenumAudioActivity extends BaseActivity implements OnPreparedListener, OnBufferingUpdateListener
{
    private PlenumStreamObject plenumStreamData;
    private MediaPlayer mp;
    private boolean waiting = false;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState, this);
        setContentView(R.layout.plenum_audio_item);

        PlenumXMLParser plenumParser = new PlenumXMLParser();
        try
        {
            plenumStreamData = plenumParser.parseStream();

            TextView title = (TextView) findViewById(R.id.audioStreamTitle);
            title.setText(plenumStreamData.getVideoStreamTitle());
            ((TextView) findViewById(R.id.audioStreamDescription)).setText(plenumStreamData.getVideoStreamDescription());
        } catch (Exception e)
        {
//            e.printStackTrace();
        }

        mp = new MediaPlayer();
        mp.setOnPreparedListener(this);
        mp.setOnBufferingUpdateListener(this);
    }

    public void playAudio()
    {

        ImageView playCtrl = ((ImageView) findViewById(R.id.audio_play));
        playCtrl.setVisibility(View.INVISIBLE);
        TextView bufferStatus = (TextView) findViewById(R.id.bufferStatus);
        bufferStatus.setVisibility(View.VISIBLE);
        waiting = true;
        mp.prepareAsync();
    }

    public void stopAudio()
    {
        if (mp.isPlaying())
        {
            mp.stop();
        }
        ImageView playCtrl = ((ImageView) findViewById(R.id.audio_play));
        playCtrl.setImageResource(R.drawable.video_play);
        playCtrl.setVisibility(View.VISIBLE);
        findViewById(R.id.bufferStatus).setVisibility(View.INVISIBLE);
    }

    @Override
    protected void onStop()
    {
        super.onStop();
       
        stopAudio();

        mp.reset();
    }

    @Override
    protected void onStart()
    {
        super.onStart();
        try
        {
            mp.setDataSource(this, Uri.parse(plenumStreamData.getStreamURL()));
        } catch (Exception e)
        {
//            e.printStackTrace();
        }
        waiting = false;
    }

    /**
     * Handler for video play
     */
    public void toggleAudio(View v)
    {
        if (!waiting)
        {
            if (mp.isPlaying())
            {
                stopAudio();
            }
            else
            {
                playAudio();
            }
        }
    }

    @Override
    public void onPrepared(MediaPlayer mp)
    {
        findViewById(R.id.bufferStatus).setVisibility(View.INVISIBLE);
        ImageView playCtrl = ((ImageView) findViewById(R.id.audio_play));
        playCtrl.setImageResource(R.drawable.video_stop);
        playCtrl.setVisibility(View.VISIBLE);
        mp.start();
        waiting = false;
    }

    @Override
    public void onBufferingUpdate(MediaPlayer mp, int percent)
    {
        // ((TextView)findViewById(R.id.bufferStatus)).setText(percent + "%");
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
            this.startActivity(intent);
            overridePendingTransition(0,0);
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }
    
    
    
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
    	// TODO Auto-generated method stub
    	super.onConfigurationChanged(newConfig);
    	LinearLayout ll = (LinearLayout) this.findViewById(R.id.audioWrapper);
    	
    	ll.removeAllViews();
    	this.getLayoutInflater().inflate(R.layout.plenum_audio_item_cont, ll);
    	if(mp.isPlaying()){
    	     ImageView playCtrl = ((ImageView) findViewById(R.id.audio_play));
    	     playCtrl.setImageResource(R.drawable.video_stop);
    	     playCtrl.setVisibility(View.VISIBLE);
    	     waiting = false;
    	}
    	else if(waiting){
    		 findViewById(R.id.bufferStatus).setVisibility(View.VISIBLE);
    	     ImageView playCtrl = ((ImageView) findViewById(R.id.audio_play));
    	     playCtrl.setVisibility(View.INVISIBLE);
    	}
    	//this.getSupportFragmentManager().findFragmentById(R.layout.main_menu).
    }
    

}
