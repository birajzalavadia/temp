package de.bundestag.android.sections.plenum;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnBufferingUpdateListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.net.Uri;
import android.os.Bundle;
import android.view.Display;
import android.view.GestureDetector;
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
import de.bundestag.android.helpers.TextHelper;
import de.bundestag.android.parser.PlenumXMLParser;
import de.bundestag.android.parser.objects.NewsDetailsObject;
import de.bundestag.android.parser.objects.PlenumObject;
import de.bundestag.android.parser.objects.PlenumStreamObject;
import de.bundestag.android.parser.objects.PlenumStreamSourceObject;

/**
 * 
 * @author GTL use to show details of news
 */
public class PlenumSittingDetailFragment extends BaseFragment implements
		OnPreparedListener, OnBufferingUpdateListener {
	public Integer listId;
	Timer agendaTimer = null;
	private TextView txtImgBottom;
	public int newsId = -1;
	public boolean isAudioPlayerSelected = false;
	public String detailsXMLURL;
	private GestureDetector detector;
	private View view = null;
	LinearLayout lnr = null;
	LinearLayout subLnr = null;
	ImageView previewImage = null;
	// ScrollView scrView = null;
	LinearLayout playerControl = null;
	LinearLayout imagelayout = null;
	boolean isFling = false;
	boolean isMasterList = false;
	NewsDetailsObject newsDetailsObject = new NewsDetailsObject();
	// ProgressDialog dialog = null;
	public static VideoView videoView = null;
	private ImageView btnVideoFullScreen;
	boolean wasPlaying = false;
	public static boolean isFullScreen = false;
	private ImageView btnVideoPlayPause;
	private int videoViewWidth, videoViewHeight;
	private TextView title_tab;
	private TextView progressMsg;
	private TextView copyright_tab;
	private WebView web;
	private PlenumStreamObject plenumStreamData;
	private String videoStreamUrl;
	private int videoStreamBandwidth;
	private Bitmap bitmap;
	private double imageRatio = 0;
	private LinearLayout playSelection = null;
	private LinearLayout videoPlayer = null;
	private LinearLayout audioPlayer = null;
	private TextView currentPlayerSelection = null;
	private TextView txtAgenda;
	private TextView txtAgendaTitle;
	private MediaPlayer mp;
	private TextView txtHeader;
	private PlenumObject plenumObject;
	private WebView text;
	LinearLayout childLayot;
	LinearLayout mainMenuLayout;
	LinearLayout sideBar;
	LinearLayout detailLayout;
	LinearLayout headerSubMenu;
	LinearLayout progressDialog;
	private ImageView videoPlayPause;
	private TextView txtVideo;
	private ImageView audioPlayPause;
	private TextView txtAudio;

	public void setNewsId(int newsId) {
		this.newsId = newsId;
	}

	public void setIsMasterList(boolean master) {
		this.isMasterList = master;
	}

	@Override
	public void onPause() {
		// if (videoView.isPlaying()) {
		// videoView.stopPlayback();
		// }
		//
		// if (mp != null && mp.isPlaying()) {
		// mp.stop();
		// mp.release();
		// mp = null;
		// }

		super.onPause();
	}

	private void initComponent() {

		videoPlayPause = (ImageView) getView()
				.findViewById(R.id.videoPlayPause);
		audioPlayPause = (ImageView) getView()
				.findViewById(R.id.audioPlayPause);
		txtVideo = (TextView) getView().findViewById(R.id.txtVideo);
		txtAudio = (TextView) getView().findViewById(R.id.txtAudio);

		childLayot = (LinearLayout) getActivity().findViewById(
				R.id.lnrScrollable);
		mainMenuLayout = (LinearLayout) getActivity().findViewById(
				R.id.mainMenu);
		sideBar = (LinearLayout) getActivity()
				.findViewById(R.id.headerGradient);
		detailLayout = (LinearLayout) getActivity().findViewById(
				R.id.detailLayout);
		headerSubMenu = (LinearLayout) getActivity().findViewById(
				R.id.headerSubMenu);
		progressDialog = (LinearLayout) getActivity().findViewById(
				R.id.progressDialog);
		text = (WebView) getView().findViewById(R.id.text);
		txtAgenda = (TextView) getView().findViewById(R.id.txtAgenda);
		txtImgBottom = (TextView) getView().findViewById(R.id.txtImgBottom);
		txtAgendaTitle = (TextView) getView().findViewById(R.id.txtAgendaTitle);
		lnr = (LinearLayout) getView().findViewById(R.id.lnrFling);
		playerControl = (LinearLayout) getView().findViewById(
				R.id.playerControl);
		// playerControl.setOnTouchListener(this);
		previewImage = (ImageView) getView().findViewById(R.id.image_tab);
		// imageView.setOnTouchListener(this);
		imagelayout = (LinearLayout) getView().findViewById(R.id.imagelayout);
		subLnr = (LinearLayout) getView().findViewById(R.id.layout);
		// scrView = (ScrollView) getView().findViewById(R.id.scrView);
		// scrView.setEnabled(false);
		web = (WebView) getView().findViewById(R.id.text_tab);
		btnVideoPlayPause = (ImageView) getView().findViewById(
				R.id.btnVideoPlayPause);
		btnVideoFullScreen = (ImageView) getView().findViewById(
				R.id.btnVideoFullScreen);
		videoView = (VideoView) getView().findViewById(R.id.video_tab);
		title_tab = (TextView) getView().findViewById(R.id.title_tab);
		progressMsg = (TextView) getView().findViewById(R.id.progressMsg);
		copyright_tab = (TextView) getView().findViewById(R.id.copyright_tab);
		btnVideoPlayPause.setImageResource(R.drawable.video_play);

		playSelection = (LinearLayout) getView().findViewById(
				R.id.playSelection);
		videoPlayer = (LinearLayout) getView().findViewById(R.id.videoPlayer);
		audioPlayer = (LinearLayout) getView().findViewById(R.id.audioPlayer);

		currentPlayerSelection = (TextView) getView().findViewById(
				R.id.currentPlayerSelection);

		previewImage.setVisibility(View.VISIBLE);

		videoView.setVisibility(View.GONE);

		previewImage.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				if (mp != null) {
					if (playSelection.getVisibility() == View.VISIBLE)
						playSelection.setVisibility(View.GONE);
					else
						playSelection.setVisibility(View.VISIBLE);
				}

			}
		});
		audioPlayer.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				if (isFullScreen) {
					childLayot.setVisibility(View.VISIBLE);
					mainMenuLayout.setVisibility(View.VISIBLE);
					sideBar.setVisibility(View.VISIBLE);
					if (!DataHolder.isLandscape && headerSubMenu != null) {
						headerSubMenu.setVisibility(View.VISIBLE);
					}
					setViewsVisiblity();
					detailLayout.setLayoutParams(new LinearLayout.LayoutParams(
							LinearLayout.LayoutParams.WRAP_CONTENT,
							LinearLayout.LayoutParams.FILL_PARENT, 1.0f));

					videoView.setLayoutParams(new FrameLayout.LayoutParams(
							videoViewWidth, videoViewHeight));
				}
				if (videoView.isPlaying()) {
					videoView.stopPlayback();
					videoView = null;
				}

				// currentPlayerSelection.setText("");
				// // playSelection.setVisibility(View.GONE);
				// btnVideoFullScreen.setVisibility(View.INVISIBLE);
				if (mp != null) {
					mp.stop();
					mp.release();
					mp = null;
					btnVideoFullScreen.setVisibility(View.INVISIBLE);
					currentPlayerSelection.setText("");
					playSelection.setVisibility(View.GONE);
					isAudioPlayerSelected = false;
					if (!videoView.canPause()) {
						createVideoPlayer();
					} else {
						videoView.start();
						btnVideoPlayPause.setImageResource(R.drawable.pause);
						btnVideoFullScreen.setVisibility(View.VISIBLE);
					}
				} else {

					isAudioPlayerSelected = true;
					try {
						createAudioPlayer();
					} catch (Exception e) {
					}
				}
			}
		});

		videoPlayer.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (mp != null && mp.isPlaying()) {
					mp.stop();
					mp.release();
					mp = null;

					videoPlayPause.setImageResource(R.drawable.video_play);
					txtVideo.setText("Video-Livestream starten");
					txtAudio.setText("Audio-Livestream starten");
				} else {
					btnVideoFullScreen.setVisibility(View.INVISIBLE);
					currentPlayerSelection.setText("");
					playSelection.setVisibility(View.GONE);
					isAudioPlayerSelected = false;
					if (!videoView.canPause()) {
						createVideoPlayer();
					} else {
						videoView.start();
						btnVideoPlayPause.setImageResource(R.drawable.pause);
						btnVideoFullScreen.setVisibility(View.VISIBLE);
					}
				}

			}
		});

		btnVideoFullScreen.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				LinearLayout bottomPort = (LinearLayout) getActivity()
						.findViewById(R.id.bottomPort);
				LinearLayout topPort = (LinearLayout) getActivity()
						.findViewById(R.id.topPort);
				LinearLayout plenum_video = (LinearLayout) getActivity()
						.findViewById(R.id.plenum_video);
				ImageView imgEgle = (ImageView) getActivity().findViewById(
						R.id.imgEgle);
				if (!isAudioPlayerSelected) {

					if (videoView != null) {

						if (!isFullScreen) {
							plenum_video.setBackgroundColor(Color.BLACK);
							imgEgle.setVisibility(View.GONE);
							isFullScreen = true;

							if (PlenumSpeakersActivityTablet.orienation == PlenumSpeakersActivityTablet.newOrientation) {
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
								Display display = getActivity()
										.getWindowManager().getDefaultDisplay();
								detailLayout
										.setLayoutParams(new LinearLayout.LayoutParams(
												LinearLayout.LayoutParams.FILL_PARENT,
												LinearLayout.LayoutParams.FILL_PARENT));
								videoView
										.setLayoutParams(new FrameLayout.LayoutParams(
												display.getWidth(), display
														.getHeight()));
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
								Display display = getActivity()
										.getWindowManager().getDefaultDisplay();
								detailLayout
										.setLayoutParams(new LinearLayout.LayoutParams(
												LinearLayout.LayoutParams.FILL_PARENT,
												LinearLayout.LayoutParams.FILL_PARENT));
								videoView
										.setLayoutParams(new FrameLayout.LayoutParams(
												display.getWidth(), display
														.getHeight()));
							}

						} else {
							isFullScreen = false;
							plenum_video.setBackgroundColor(Color.WHITE);
							imgEgle.setVisibility(View.VISIBLE);
							if (PlenumSpeakersActivityTablet.orienation == PlenumSpeakersActivityTablet.newOrientation) {
								childLayot.setVisibility(View.VISIBLE);
								mainMenuLayout.setVisibility(View.VISIBLE);
								sideBar.setVisibility(View.VISIBLE);
								try {
									headerSubMenu.setVisibility(View.VISIBLE);
								} catch (Exception e) {
								}
								setViewsVisiblity();
								detailLayout
										.setLayoutParams(new LinearLayout.LayoutParams(
												LinearLayout.LayoutParams.WRAP_CONTENT,
												LinearLayout.LayoutParams.FILL_PARENT,
												1.0f));
								videoView
										.setLayoutParams(new FrameLayout.LayoutParams(
												videoViewWidth, videoViewHeight));
							} else {
								childLayot.setVisibility(View.VISIBLE);
								mainMenuLayout.setVisibility(View.GONE);
								sideBar.setVisibility(View.GONE);
								try {
									headerSubMenu.setVisibility(View.GONE);
								} catch (Exception e) {
								}
								bottomPort.setVisibility(View.VISIBLE);
								topPort.setVisibility(View.VISIBLE);
								setViewsVisiblity();
								detailLayout
										.setLayoutParams(new LinearLayout.LayoutParams(
												LinearLayout.LayoutParams.WRAP_CONTENT,
												LinearLayout.LayoutParams.FILL_PARENT,
												1.0f));

								videoView
										.setLayoutParams(new FrameLayout.LayoutParams(
												videoViewWidth, videoViewHeight));
							}

						}
					}
				} else {

				}
			}
		});

		btnVideoPlayPause.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				if (isAudioPlayerSelected) {
					if (mp != null && mp.isPlaying()) {
						btnVideoPlayPause
								.setImageResource(R.drawable.video_play);
						mp.stop();
						playerControl.setVisibility(View.GONE);
						playSelection.setVisibility(View.VISIBLE);
						currentPlayerSelection
								.setText("Audio-Livestream starten");

					} else {
						btnVideoPlayPause.setImageResource(R.drawable.pause);
						try {
							createAudioPlayer();
						} catch (Exception e) {
						}

					}

				} else {

					if (videoView.isPlaying()) {
						btnVideoPlayPause
								.setImageResource(R.drawable.video_play);
						currentPlayerSelection
								.setText("Video-Livestream starten");
						playerControl.setVisibility(View.GONE);
						playSelection.setVisibility(View.VISIBLE);
						if (videoView.canPause()) {
							videoView.pause();
						} else {
							videoView.stopPlayback();
						}

					} else {
						btnVideoPlayPause.setImageResource(R.drawable.pause);
						if (!videoView.canPause()) {
							createVideoPlayer();
						}
						videoView.start();

					}
				}

			}

		});
	}

	private void setViewsVisiblityGone() {
		// web.setVisibility(View.GONE);
		// title_tab.setVisibility(View.GONE);
		// copyright_tab.setVisibility(View.GONE);
		// scrView.setEnabled(false);
		lnr.setEnabled(false);
		subLnr.setEnabled(false);
		txtImgBottom.setVisibility(View.GONE);
		text.setVisibility(View.GONE);
		txtAgenda.setVisibility(View.GONE);
		txtAgendaTitle.setVisibility(View.GONE);
		// web.setEnabled(false);
	}

	private void setViewsVisiblity() {
		// web.setVisibility(View.VISIBLE);
		// title_tab.setVisibility(View.VISIBLE);
		// copyright_tab.setVisibility(View.VISIBLE);
		// scrView.setEnabled(true);
		lnr.setEnabled(true);
		subLnr.setEnabled(true);
		txtImgBottom.setVisibility(View.VISIBLE);
		text.setVisibility(View.VISIBLE);
		txtAgenda.setVisibility(View.VISIBLE);
		txtAgendaTitle.setVisibility(View.VISIBLE);
		// web.setEnabled(true);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		initComponent();

		// playSelection.setLayoutParams(new
		// FrameLayout.LayoutParams(previewImage.getWidth(),previewImage.getHeight()));
		// playSelection.setWeightSum(1);
		String cdate = new Date().getDate() + ". " + getMonthName() + " "
				+ (new Date().getYear() + 1900) + ": ";
		txtImgBottom.setText(cdate
				+ "Liveübertragung der Sitzung des Bundestages.");
		final ArrayList<HashMap<String, Object>> items = PlenumDebatesViewHelper
				.createDebates();
		if (items != null) {

			for (int i = 0; i < items.size(); i++) {
				String state = (String) items.get(i).get(
						PlenumSpeakersViewHelper.KEY_SPEAKER_STATE);
				if (state != null) {
					if (state.equalsIgnoreCase("live") || state.equals("läuft")) {
						updateAgenda(items, i);
					}
				}
			}

		}

		plenumObject = createPlenumDetailsObject();
		text.setLayoutParams(new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.WRAP_CONTENT,
				LinearLayout.LayoutParams.WRAP_CONTENT));
		text.setBackgroundColor(Color.TRANSPARENT);
		String reformatedText = TextHelper
				.customizedFromHtmlforWebViewTab(plenumObject.getTeaser());
		text.loadDataWithBaseURL("file:///android_asset/", reformatedText,
				"text/html", "UTF-8", null);

		PlenumXMLParser plenumParser = new PlenumXMLParser();
		try {
			plenumStreamData = plenumParser.parseStream();
			ArrayList<PlenumStreamSourceObject> streams = plenumParser
					.parseStreamSource(plenumStreamData.getVideoStreamSource());
			// Chooses the best bandwidth rtsp stream
			for (PlenumStreamSourceObject stream : streams) {
				if (stream.getType().equals("rtsp")) {
					if (videoStreamUrl == null
							|| videoStreamBandwidth < Integer.parseInt(stream
									.getBandwidth())) {
						videoStreamUrl = stream.getHref();
						videoStreamBandwidth = Integer.parseInt(stream
								.getBandwidth());
					}
				}
			}

			bitmap = ImageHelper.loadBitmapFromUrl(plenumStreamData
					.getVideoStreamImage());
			if (bitmap != null) {

				float h = bitmap.getHeight();
				float w = bitmap.getWidth();

				imageRatio = h / w;

				previewImage.postDelayed(new Runnable() {

					@Override
					public void run() {
						ImageView imagePreview = (ImageView) view
								.findViewById(R.id.image_tab);
						imagePreview.setImageBitmap(bitmap);
						imagePreview.setVisibility(View.VISIBLE);
					}

				}, 50);

			}

			plenumStreamData.getVideoStreamTitle();
			plenumStreamData.getVideoStreamDescription();

			agendaTimer = new Timer();

			try {
				agendaTimer.scheduleAtFixedRate(new TimerTask() {
					@Override
					public void run() {
						if (!isFullScreen) {
							final ArrayList<HashMap<String, Object>> items = PlenumDebatesViewHelper
									.createDebates();
							if (items != null) {
								try {

									getActivity().runOnUiThread(new Runnable() {

										@Override
										public void run() {
											if (!isFullScreen) {
												plenumObject = createPlenumDetailsObject();
												text.setLayoutParams(new LinearLayout.LayoutParams(
														LinearLayout.LayoutParams.WRAP_CONTENT,
														LinearLayout.LayoutParams.WRAP_CONTENT));
												text.setBackgroundColor(Color.TRANSPARENT);
												String reformatedText = TextHelper
														.customizedFromHtmlforWebViewTab(plenumObject
																.getTeaser());
												text.loadDataWithBaseURL(
														"file:///android_asset/",
														reformatedText,
														"text/html", "UTF-8",
														null);
												for (int i = 0; i < items
														.size(); i++) {
													String state = (String) items
															.get(i)
															.get(PlenumSpeakersViewHelper.KEY_SPEAKER_STATE);
													if (state != null) {
														if (state
																.equalsIgnoreCase("live")
																|| state.equalsIgnoreCase("läuft")) {
															updateAgenda(items,
																	i);
														}
													}
												}
											}

										}
									});
								} catch (Exception e) {
								}
							}
						}

					}

				}, 10000, 10000);
			} catch (Exception e) {
			}

		} catch (Exception e) {
		}

		DataHolder.releaseScreenLock(this.getActivity());
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.plenum_sitting_video_view, container,
				false);
		return view;
	}

	private void updateAgenda(ArrayList<HashMap<String, Object>> items,
			int position) {
		if (items != null && items.size() > 0) {
			txtAgenda.setText("Aktuell: Top"
					+ (String) items.get(position).get(
							PlenumSpeakersViewHelper.KEY_SPEAKER_TOPIC));
			txtAgendaTitle.setText((String) items.get(position).get(
					PlenumSpeakersViewHelper.KEY_SPEAKER_NAME));
			DataHolder.txtAgenda = txtAgenda;
			DataHolder.txtAgendaTitle = txtAgendaTitle;
			Date currentDate = new Date();
			try {
				if (DataHolder.currentPlanumTab.equals("speaker")
						|| DataHolder.currentPlanumTab.equals("")) {
					((TextView) getActivity().findViewById(R.id.txtHeader))
							.setText("Redner\n"
									+ "Top"
									+ (String) items
											.get(position)
											.get(PlenumSpeakersViewHelper.KEY_SPEAKER_TOPIC));
				} else {
					((TextView) getActivity().findViewById(R.id.txtHeader))
							.setText("Debatten am " + (currentDate.getDate())
									+ "." + (currentDate.getMonth() + 1) + "."
									+ (currentDate.getYear() + 1900));
				}
			} catch (Exception e) {

			}
		}
	}

	private void createVideoPlayer() {

		if (videoView != null && videoView.isPlaying()) {
			videoView = null;
			videoView = (VideoView) getView().findViewById(R.id.video_tab);
		}

		playerControl.setVisibility(View.VISIBLE);
		previewImage.setVisibility(View.GONE);
		videoView.setVisibility(View.VISIBLE);
		progressMsg.setText("Video wird geladen");
		progressDialog.setVisibility(View.VISIBLE);

		if (!isFullScreen) {

			videoViewWidth = previewImage.getWidth();
			videoViewHeight = previewImage.getHeight();

			videoView.setLayoutParams(new FrameLayout.LayoutParams(previewImage
					.getWidth(), previewImage.getHeight()));
		}

		videoView.setOnPreparedListener(new OnPreparedListener() {

			@Override
			public void onPrepared(MediaPlayer mp) {
				try {
					progressDialog.setVisibility(View.GONE);
					// if (dialog != null) {
					// dialog.dismiss();
					btnVideoFullScreen.setVisibility(View.VISIBLE);
					// dialog = null;
					// }
				} catch (Exception e) {
					btnVideoPlayPause.setImageResource(R.drawable.video_play);
				}
			}
		});

		videoView.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {

				if (event.getAction() == MotionEvent.ACTION_DOWN) {

					if (playSelection.getVisibility() != View.VISIBLE) {
						if (playerControl.getVisibility() == View.VISIBLE)
							playerControl.setVisibility(View.GONE);
						else
							playerControl.setVisibility(View.VISIBLE);
					}
				}

				// scrView.setEnabled(false);
				return true;
			}
		});

		videoView.setZOrderMediaOverlay(false);
		videoView.setVideoPath(videoStreamUrl);
		// if (dialog == null)
		// dialog = ProgressDialog.show(getActivity(), "", "Video wird geladen",
		// true);
		// dialog.setCancelable(false);
		videoView.start();
		btnVideoPlayPause.setImageResource(R.drawable.pause);
	}

	private void createAudioPlayer() throws IllegalArgumentException,
			SecurityException, IllegalStateException, IOException {
		if (mp != null) {
			mp.release();
			mp = null;
		}
		progressDialog.setVisibility(View.GONE);
		progressMsg.setText("Audio wird geladen");
		// if (dialog == null)
		// dialog = ProgressDialog.show(getActivity(), "", "Audio wird geladen",
		// true);
		// dialog.setCancelable(false);
		mp = new MediaPlayer();
		mp.setOnPreparedListener(this);
		mp.setOnBufferingUpdateListener(this);
		mp.setDataSource(getActivity(),
				Uri.parse(plenumStreamData.getStreamURL()));
		mp.prepareAsync();
		videoPlayPause.setImageResource(R.drawable.pause);
		txtVideo.setText("                        ");
		txtAudio.setText("Video-Livestream starten");
		// btnVideoPlayPause.setImageResource(R.drawable.pause);
		// playerControl.setVisibility(View.VISIBLE);

	}

	@Override
	public void onBufferingUpdate(MediaPlayer mp, int percent) {

	}

	@Override
	public void onPrepared(MediaPlayer mp) {
		try {
			// if (dialog != null) {
			// dialog.dismiss();
			mp.start();
			// dialog = null;
			// }

		} catch (Exception e) {
		}

	}

	@Override
	protected void finalize() throws Throwable {
		super.finalize();
		listId = null;
		detailsXMLURL = null;
		detector = null;
		view = null;
		lnr = null;
		subLnr = null;
		previewImage = null;
		playerControl = null;
		imagelayout = null;
		newsDetailsObject = null;
		// dialog = null;
		videoView = null;
		btnVideoFullScreen = null;
		btnVideoPlayPause = null;
		title_tab = null;
		copyright_tab = null;
		web = null;
		plenumStreamData = null;
		videoStreamUrl = null;
		bitmap = null;
		playSelection = null;
		videoPlayer = null;
		audioPlayer = null;
		currentPlayerSelection = null;
		mp = null;
		agendaTimer = null;
	}

	private String getMonthName() {

		switch (new Date().getMonth()) {
		case 0:
			return "Januar";
		case 1:
			return "Februar";
		case 2:
			return "März";
		case 3:
			return "April";
		case 4:
			return "Mai";
		case 5:
			return "Juni";
		case 6:
			return "Juli";
		case 7:
			return "August";
		case 8:
			return "September";
		case 9:
			return "Oktober";
		case 10:
			return "November";
		case 11:
			return "Dezember";
		default:
			return "";
		}

	}

	public PlenumObject createPlenumDetailsObject() {
		PlenumXMLParser plenumXMLParser = new PlenumXMLParser();

		PlenumObject news = null;
		try {
			news = plenumXMLParser.parseMain(plenumXMLParser.PLENUM_TASK_URL);
			// news =
			// plenumXMLParser.parseMain("http://192.168.10.72/babiel_xml/index.xml");
		} catch (Exception e) {
		}
		return news;
	}

}