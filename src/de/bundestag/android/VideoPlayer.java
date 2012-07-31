package de.bundestag.android;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.res.Configuration;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnPreparedListener;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.VideoView;

public class VideoPlayer extends Activity implements OnPreparedListener
{
    VideoView videoView;
    String videoUrl;
    int displayWidth;
    int displayHeight;
    int smallWidth;
    int smallHeight;
    ProgressDialog dialog;
    boolean wasPlaying = false;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fullscreen_player);

        videoView = (VideoView) findViewById(R.id.video_view1);
        videoUrl = getIntent().getExtras().getString("streamURL");
        videoView.setOnPreparedListener(this);
        videoView.setZOrderMediaOverlay(false);

        videoView.setVideoPath(videoUrl);
        dialog = ProgressDialog.show(this, "", "Video wird geladen", true);
        videoView.start();
        videoView.setVisibility(View.VISIBLE);
    }

    private boolean ctrlOn = false;

    public void toggleVideoCtrl(View v)
    {
        if (!ctrlOn)
        {
            RelativeLayout player = ((RelativeLayout) this.findViewById(R.id.fullscreen_player));
            RelativeLayout ctrl = (RelativeLayout) this.getLayoutInflater().inflate(R.layout.fullscreen_player_ctrl, player, true);
            ctrlOn = true;
            if (!videoView.isPlaying())
            {
                ((ImageView) findViewById(R.id.video_pause_play)).setImageResource(R.drawable.video_play);
            }
        }
        else
        {
            ((RelativeLayout) this.findViewById(R.id.fullscreen_player)).removeView(this.findViewById(R.id.video_ctrl_overlay));
            ctrlOn = false;
        }
    }

    public void closeVideoPlayer(View c)
    {
        this.finish();
    }

    public void toggleVideoPlayer(View c)
    {
        if (videoView.isPlaying())
        {
            if (videoView.canPause())
            {
                videoView.pause();
            }
            else
            {
                videoView.stopPlayback();
            }
            ((ImageView) findViewById(R.id.video_pause_play)).setImageResource(R.drawable.video_play);
        }
        else
        {
            if (!videoView.canPause())
            {
                videoView.setVideoPath(videoUrl);
                dialog = ProgressDialog.show(this, "", "Video wird geladen", true);
            }

            videoView.start();
            toggleVideoCtrl(c);
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig)
    {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public void onPrepared(MediaPlayer arg0)
    {
        if (dialog != null)
        {
            dialog.dismiss();
        }
    }
@Override
protected void onResume() {
	// TODO Auto-generated method stub
	super.onResume();
	if(wasPlaying){
	if (!videoView.canPause())
    {
        videoView.setVideoPath(videoUrl);
        dialog = ProgressDialog.show(this, "", "Video wird geladen", true);
    }

    videoView.start();
    
    toggleVideoCtrl(videoView);
    wasPlaying =false;
	}
}

    @Override
    public void onPause()
    {
        super.onPause();
     //   this.finish();
		if (videoView.isPlaying()) {
			if (videoView.canPause()) {
				videoView.pause();
			} else {
				videoView.stopPlayback();
			}
			toggleVideoCtrl(videoView);
			wasPlaying = true;
		}
        
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();

        if (this.videoView != null)
        {
            if (this.videoView.isPlaying())
            {
                videoView.stopPlayback();
            }
        }
    }
}
