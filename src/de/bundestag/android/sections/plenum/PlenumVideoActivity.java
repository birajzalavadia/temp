package de.bundestag.android.sections.plenum;

import java.util.ArrayList;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import de.bundestag.android.BaseActivity;
import de.bundestag.android.R;
import de.bundestag.android.VideoPlayer;
import de.bundestag.android.constant.DataHolder;
import de.bundestag.android.helpers.ImageHelper;
import de.bundestag.android.parser.PlenumXMLParser;
import de.bundestag.android.parser.objects.PlenumStreamObject;
import de.bundestag.android.parser.objects.PlenumStreamSourceObject;

public class PlenumVideoActivity extends BaseActivity {
	private PlenumStreamObject plenumStreamData;
	private String videoStreamUrl;
	private int videoStreamBandwidth;
	private Bitmap bitmap;
	private double imageRatio = 0;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState, this);
		setContentView(R.layout.plenum_video_item);

		PlenumXMLParser plenumParser = new PlenumXMLParser();
		try {
			plenumStreamData = plenumParser.parseStream();
			ArrayList<PlenumStreamSourceObject> streams = plenumParser.parseStreamSource(plenumStreamData.getVideoStreamSource());
			// Chooses the best bandwidth rtsp stream
			for (PlenumStreamSourceObject stream : streams) {
				if (stream.getType().equals("rtsp")) {
					if (videoStreamUrl == null || videoStreamBandwidth < Integer.parseInt(stream.getBandwidth())) {
						videoStreamUrl = stream.getHref();
						videoStreamBandwidth = Integer.parseInt(stream.getBandwidth());
					}
				}
			}

			ImageView previewImage = (ImageView) findViewById(R.id.previewImage);

			bitmap = ImageHelper.loadBitmapFromUrl(plenumStreamData.getVideoStreamImage());
			if (bitmap != null) {
				// previewImage.setImageBitmap(bitmap);

				float h = bitmap.getHeight();
				float w = bitmap.getWidth();

				imageRatio = h / w;

				// previewImage.getLayoutParams().height = imgHeight;

				previewImage.postDelayed(new Runnable() {

					@Override
					public void run() {
						ImageView imagePreview = (ImageView) findViewById(R.id.previewImage);
						int imgHeight = (int) Math.round((imageRatio * imagePreview.getWidth()));
						imagePreview.getLayoutParams().height = imgHeight;
						imagePreview.setImageBitmap(bitmap);
						imagePreview.setVisibility(View.VISIBLE);
					}

				}, 50);

			}

			((TextView) findViewById(R.id.videoStreamTitle)).setText(plenumStreamData.getVideoStreamTitle());
			((TextView) findViewById(R.id.videoStreamDescription)).setText(plenumStreamData.getVideoStreamDescription());
		} catch (Exception e) {
//			e.printStackTrace();
		}
	}

	/**
	 * Handler for video play
	 */
	public void playVideo(View v) {
		if (videoStreamUrl != null) {
			// Intent i = new Intent(this, PlenumVideoPlayerActivity.class);
			Intent i = new Intent(this, VideoPlayer.class);
			i.putExtra("streamURL", videoStreamUrl);
			this.startActivity(i);
			this.overridePendingTransition(0, 0);
		}
	}

	/**
	 * Hack to handle the back button.
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			Intent intent = new Intent();

			if (DataHolder.isOnline(this) && isPLenarySitting()) {
				intent.setClass(this, PlenumNewsActivity.class);
			} else {
				intent.setClass(this, PlenumSitzungenActivity.class);
			}
		}
		return super.onKeyDown(keyCode, event);
	}

}
