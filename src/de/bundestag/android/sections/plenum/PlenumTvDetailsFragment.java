package de.bundestag.android.sections.plenum;

import java.util.ArrayList;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.VideoView;
import de.bundestag.android.BaseFragment;
import de.bundestag.android.R;
import de.bundestag.android.constant.DataHolder;
import de.bundestag.android.helpers.ImageHelper;
import de.bundestag.android.parser.PlenumXMLParser;
import de.bundestag.android.parser.objects.NewsDetailsObject;
import de.bundestag.android.parser.objects.PlenumStreamObject;
import de.bundestag.android.parser.objects.PlenumStreamSourceObject;

/**
 * 
 * @author GTL use to show details of news
 */
public class PlenumTvDetailsFragment extends BaseFragment {
	public Integer listId;
	public LinearLayout imgEgle;
	public String detailsXMLURL;
	private View view = null;
	LinearLayout lnr = null;
	LinearLayout subLnr = null;
	ImageView previewImage = null;
	LinearLayout playerControl = null;
	LinearLayout imagelayout = null;
	boolean isFling = false;
	boolean isMasterList = false;
	NewsDetailsObject newsDetailsObject = new NewsDetailsObject();
	ProgressDialog dialog = null;
	public static VideoView videoView = null;
	private ImageView btnVideoFullScreen;
	boolean wasPlaying = false;
	public static boolean isFullScreen = false;
	private ImageView btnVideoPlayPause;
	private int videoViewWidth, videoViewHeight;
	private TextView title_tab;
	private TextView copyright_tab;
	private WebView web;
	private TextView desc_tab;
	private TextView playText;
	private PlenumStreamObject plenumStreamData;
	private String videoStreamUrl;
	private int videoStreamBandwidth;
	private Bitmap bitmap;
	private double imageRatio = 0;
	private LinearLayout headerG;
	LinearLayout progressDialog;

	public void setIsMasterList(boolean master) {
		this.isMasterList = master;
	}

	private void initComponent() {
		headerG = (LinearLayout) getActivity().findViewById(R.id.headerG);
		imgEgle = (LinearLayout) getView().findViewById(R.id.imgEgle);
		progressDialog = (LinearLayout) getView().findViewById(R.id.progressDialog);
		lnr = (LinearLayout) getView().findViewById(R.id.lnrFling);
		playerControl = (LinearLayout) getView().findViewById(R.id.playerControl);
		// playerControl.setOnTouchListener(this);
		previewImage = (ImageView) getView().findViewById(R.id.image_tab);
		// imageView.setOnTouchListener(this);
		imagelayout = (LinearLayout) getView().findViewById(R.id.imagelayout);
		subLnr = (LinearLayout) getView().findViewById(R.id.layout);
		web = (WebView) getView().findViewById(R.id.text_tab);
		btnVideoPlayPause = (ImageView) getView().findViewById(R.id.btnVideoPlayPause);
		btnVideoFullScreen = (ImageView) getView().findViewById(R.id.btnVideoFullScreen);
		videoView = (VideoView) getView().findViewById(R.id.video_tab);
		title_tab = (TextView) getView().findViewById(R.id.title_tab);
		copyright_tab = (TextView) getView().findViewById(R.id.copyright_tab);
		btnVideoPlayPause.setImageResource(R.drawable.video_play);
		playText = (TextView) getView().findViewById(R.id.playText);
		web.setVisibility(View.GONE);

		playerControl.setVisibility(View.VISIBLE);
		previewImage.setVisibility(View.VISIBLE);
		btnVideoFullScreen.setVisibility(View.GONE);

		videoView.setVisibility(View.GONE);
		desc_tab = (TextView) getView().findViewById(R.id.desc_tab);
		desc_tab.setVisibility(View.VISIBLE);

		// if (!DataHolder.isLandscape) {
		previewImage.setLayoutParams(new FrameLayout.LayoutParams(436, 289));
		// }

		btnVideoFullScreen.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				LinearLayout childLayot = (LinearLayout) getActivity().findViewById(R.id.lnrScrollable);
				LinearLayout mainMenuLayout = (LinearLayout) getActivity().findViewById(R.id.mainMenu);
				LinearLayout sideBar = (LinearLayout) getActivity().findViewById(R.id.headerGradient);
				LinearLayout detailLayout = (LinearLayout) getActivity().findViewById(R.id.detailLayout);
				LinearLayout headerSubMenu = (LinearLayout) getActivity().findViewById(R.id.headerSubMenu);
				LinearLayout plenum_video = (LinearLayout) getActivity().findViewById(R.id.plenum_video);
				LinearLayout bottomPort = (LinearLayout) getActivity().findViewById(R.id.bottomPort);
				LinearLayout topPort = (LinearLayout) getActivity().findViewById(R.id.topPort);
				imgEgle.setVisibility(View.INVISIBLE);
				if (!isFullScreen) {
					plenum_video.setBackgroundColor(Color.BLACK);
					isFullScreen = true;

					if (PlenumTvActivity.orienation == PlenumTvActivity.newOrientation) {

						childLayot.setVisibility(View.GONE);
						mainMenuLayout.setVisibility(View.GONE);
						sideBar.setVisibility(View.GONE);
						if (!DataHolder.isLandscape) {
							headerSubMenu.setVisibility(View.GONE);
						}
						setViewsVisiblityGone();
						Display display = getActivity().getWindowManager().getDefaultDisplay();
						detailLayout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.FILL_PARENT));
						videoView.setLayoutParams(new FrameLayout.LayoutParams(display.getWidth(), display.getHeight()));
					} else {
						childLayot.setVisibility(View.GONE);
						mainMenuLayout.setVisibility(View.GONE);
						sideBar.setVisibility(View.GONE);
						try {
							headerSubMenu.setVisibility(View.GONE);
						} catch (Exception e) {
						}
						bottomPort.setVisibility(View.GONE);
						topPort.setVisibility(View.GONE);
						setViewsVisiblityGone();
						Display display = getActivity().getWindowManager().getDefaultDisplay();
						detailLayout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.FILL_PARENT));
						videoView.setLayoutParams(new FrameLayout.LayoutParams(display.getWidth(), display.getHeight()));
					}

				} else {
					isFullScreen = false;

					if (PlenumTvActivity.orienation == PlenumTvActivity.newOrientation) {
						plenum_video.setBackgroundColor(Color.WHITE);
						childLayot.setVisibility(View.VISIBLE);
						mainMenuLayout.setVisibility(View.VISIBLE);
						sideBar.setVisibility(View.VISIBLE);
						imgEgle.setVisibility(View.VISIBLE);
						if (!DataHolder.isLandscape) {
							try {
								headerSubMenu.setVisibility(View.VISIBLE);
							} catch (Exception e) {
							}
						}
						setViewsVisiblity();
						detailLayout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.FILL_PARENT, 1.0f));
						videoView.setLayoutParams(new FrameLayout.LayoutParams(videoViewWidth, videoViewHeight));
					} else {
						plenum_video.setBackgroundColor(Color.WHITE);
						childLayot.setVisibility(View.VISIBLE);
						mainMenuLayout.setVisibility(View.GONE);
						sideBar.setVisibility(View.GONE);
						imgEgle.setVisibility(View.VISIBLE);

						try {

							headerSubMenu.setVisibility(View.GONE);
						} catch (Exception e) {
						}
						bottomPort.setVisibility(View.VISIBLE);
						topPort.setVisibility(View.VISIBLE);
						setViewsVisiblity();
						detailLayout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.FILL_PARENT, 1.0f));
						videoView.setLayoutParams(new FrameLayout.LayoutParams(videoViewWidth, videoViewHeight));
					}

				}
			}

		});

		btnVideoPlayPause.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				if (!DataHolder.isOnline(getActivity())) {
					DataHolder.alertboxOk(getActivity());
				} else {

					try {
						if (videoView.isPlaying()) {
							btnVideoPlayPause.setImageResource(R.drawable.video_play);
							playText.setText("Live Stream starten");
							if (videoView.canPause()) {
								videoView.pause();
							} else {
								videoView.stopPlayback();
							}

						} else {
							btnVideoPlayPause.setImageResource(R.drawable.pause);
							playText.setText("");
							if (!videoView.canPause()) {
								createVideoPlayer();
							}
							videoView.start();

						}
					} catch (Exception e) {
					}
				}
			}
		});
	}

	private void setViewsVisiblityGone() {
		web.setVisibility(View.GONE);
		title_tab.setVisibility(View.GONE);
		copyright_tab.setVisibility(View.GONE);
		desc_tab.setVisibility(View.GONE);
		lnr.setEnabled(false);
		subLnr.setEnabled(false);
		web.setEnabled(false);
		headerG.setVisibility(View.GONE);
	}

	private void setViewsVisiblity() {
		// web.setVisibility(View.VISIBLE);
		title_tab.setVisibility(View.VISIBLE);
		copyright_tab.setVisibility(View.VISIBLE);
		desc_tab.setVisibility(View.VISIBLE);
		lnr.setEnabled(true);
		subLnr.setEnabled(true);
		web.setEnabled(true);
		headerG.setVisibility(View.VISIBLE);
	}

	// @Override
	// public void onCreate(Bundle savedInstanceState) {
	// super.onCreate(savedInstanceState);
	// setRetainInstance(true);
	// }
	// @Override
	// public void onSaveInstanceState(Bundle outState) {
	// super.onSaveInstanceState(outState);
	//
	// getActivity().getSupportFragmentManager().putFragment(outState,
	// PlenumTvDetailsFragment.class.getName(), this);
	// }

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		initComponent();

		new AsyncTask<PlenumXMLParser, PlenumXMLParser, ArrayList<PlenumStreamSourceObject>>() {

			@Override
			protected void onPreExecute() {
				super.onPreExecute();
				if (dialog == null)
					dialog = ProgressDialog.show(getActivity(), "", "Daten werden geladen");
				else if (!dialog.isShowing())
					dialog.show();
			}

			@Override
			protected ArrayList<PlenumStreamSourceObject> doInBackground(PlenumXMLParser... plenumParser) {

				try {
					plenumStreamData = plenumParser[0].parseStream();
					return plenumParser[0].parseStreamSource(plenumStreamData.getVideoStreamSource());
				} catch (Exception e) {
					return null;
				}

			}

			@Override
			protected void onPostExecute(ArrayList<PlenumStreamSourceObject> streams) {
				super.onPostExecute(streams);
				if ((dialog != null) && (dialog.isShowing()))
					dialog.dismiss();
				if (streams != null) {

					try {
						// Chooses the best bandwidth rtsp stream
						for (PlenumStreamSourceObject stream : streams) {
							if (stream.getType().equals("rtsp")) {
								if (videoStreamUrl == null || videoStreamBandwidth < Integer.parseInt(stream.getBandwidth())) {
									videoStreamUrl = stream.getHref();
									videoStreamBandwidth = Integer.parseInt(stream.getBandwidth());
								}
							}
						}

						bitmap = ImageHelper.loadBitmapFromUrl(plenumStreamData.getVideoStreamImage());
						if (bitmap != null) {

							float h = bitmap.getHeight();
							float w = bitmap.getWidth();

							imageRatio = h / w;

							previewImage.postDelayed(new Runnable() {

								@Override
								public void run() {
									ImageView imagePreview = (ImageView) view.findViewById(R.id.image_tab);
									imagePreview.setImageBitmap(bitmap);
									imagePreview.setVisibility(View.VISIBLE);
								}

							}, 50);

						}

						title_tab.setText(plenumStreamData.getVideoStreamTitle());
						desc_tab.setText(plenumStreamData.getVideoStreamDescription());

						DataHolder.releaseScreenLock(PlenumTvDetailsFragment.this.getActivity());
					} catch (Exception e) {
					}
				}
			}

		}.execute(new PlenumXMLParser());

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.tv_detail_item, container, false);
		return view;
	}

	private void createVideoPlayer() {

		if (videoView != null && videoView.isPlaying()) {
			videoView = null;
			videoView = (VideoView) getView().findViewById(R.id.video_tab);
		}

		playerControl.setVisibility(View.VISIBLE);
		videoView.setVisibility(View.VISIBLE);
		playText.setText("Live Stream");
		progressDialog.setVisibility(View.VISIBLE);
		// videoView = new VideoView(this.getActivity());

		if (!isFullScreen) {
			videoView.setLayoutParams(new FrameLayout.LayoutParams(previewImage.getWidth(), previewImage.getHeight()));
			//
			videoViewWidth = previewImage.getWidth();
			videoViewHeight = previewImage.getHeight();
		}
		previewImage.setVisibility(View.GONE);
		videoView.setOnPreparedListener(new OnPreparedListener() {

			@Override
			public void onPrepared(MediaPlayer mp) {
				progressDialog.setVisibility(View.GONE);
				btnVideoFullScreen.setVisibility(View.VISIBLE);
				playText.setText("");
				// }
			}
		});

		videoView.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {

				if (event.getAction() == MotionEvent.ACTION_DOWN) {

					if (playerControl.getVisibility() == View.VISIBLE)
						playerControl.setVisibility(View.GONE);
					else
						playerControl.setVisibility(View.VISIBLE);
				}

				return true;
			}
		});

		videoView.setOnErrorListener(new OnErrorListener() {

			@Override
			public boolean onError(MediaPlayer mp, int what, int extra) {
				progressDialog.setVisibility(View.GONE);
				playerControl.setVisibility(View.VISIBLE);
				playText.setText("Live Stream starten");
				btnVideoPlayPause.setImageResource(R.drawable.video_play);
				videoView.setVisibility(View.GONE);
				previewImage.setVisibility(View.VISIBLE);
				return false;
			}
		});

		videoView.setZOrderMediaOverlay(false);
		videoView.setVideoPath(videoStreamUrl);
		videoView.start();
		btnVideoPlayPause.setImageResource(R.drawable.pause);
	}

	@Override
	public void onPause() {
		super.onPause();
		// if (videoView.isPlaying()) {
		// videoView.stopPlayback();
		// btnVideoPlayPause.setImageResource(R.drawable.video_play);
		// playerControl.setVisibility(View.VISIBLE);
		// }
	}

	@Override
	public void onStop() {
		super.onStop();
	}

	@Override
	protected void finalize() throws Throwable {
		super.finalize();

		// listId = null;
		// detailsXMLURL = null;
		// view = null;
		// lnr = null;
		// subLnr = null;
		// previewImage = null;
		// playerControl = null;
		// imagelayout = null;
		// newsDetailsObject = null;
		// dialog = null;
		// videoView = null;
		// btnVideoFullScreen = null;
		// btnVideoPlayPause = null;
		// title_tab = null;
		// copyright_tab = null;
		// web = null;
		// desc_tab = null;
		// plenumStreamData = null;
		// videoStreamUrl = null;
		// bitmap = null;
	}

}