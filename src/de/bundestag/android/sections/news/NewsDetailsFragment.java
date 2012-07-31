package de.bundestag.android.sections.news;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.os.Bundle;
import android.view.Display;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
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
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.VideoView;
import de.bundestag.android.BaseFragment;
import de.bundestag.android.R;
import de.bundestag.android.constant.DataHolder;
import de.bundestag.android.customviews.CustomScrollView;
import de.bundestag.android.customviews.WebActivityClient;
import de.bundestag.android.helpers.TextHelper;
import de.bundestag.android.parser.PlenumXMLParser;
import de.bundestag.android.parser.objects.NewsDetailsObject;
import de.bundestag.android.parser.objects.PlenumObject;
import de.bundestag.android.sections.members.MembersListActivity;
import de.bundestag.android.sections.plenum.PlenumSpeakersActivity;
import de.bundestag.android.sections.plenum.PlenumTvActivity;
import de.bundestag.android.storage.NewsDatabaseAdapter;
import de.bundestag.android.synchronization.SynchronizeNewsDetailsTask;

/**
 * 
 * @author GTL use to show details of news
 */
public class NewsDetailsFragment extends BaseFragment {
	public Integer listId;

	public int newsId = -1;
	private static final int SWIPE_MIN_DISTANCE = 100;
	private static final int SWIPE_THRESHOLD_VELOCITY = 200;

	public String detailsXMLURL;
	private GestureDetector detector;
	private View view = null;
	LinearLayout lnr = null;
	LinearLayout subLnr = null;
	ImageView imageView = null;
	public static CustomScrollView scrView = null;
	LinearLayout playerControl = null;
	LinearLayout imagelayout = null;
	boolean isFling = false;
	boolean isMasterList = false;
	static NewsDetailsObject newsDetailsObject = new NewsDetailsObject();
	// ProgressDialog dialog = null;
	public static VideoView videoView = null;
	private ImageView btnVideoFullScreen;
	boolean wasPlaying = false;
	public static boolean isFullScreen = false;
	private ImageView btnVideoPlayPause;
	private int videoViewWidth, videoViewHeight;
	private TextView title_tab;
	private TextView copyright_tab;
	private WebView web;
	public static NewsDetailsFragment refNewsDetailsFragment;
	LinearLayout progressDialog;

	// public static boolean isVideoLoad = false;

	public void setNewsId(int newsId) {
		this.newsId = newsId;
	}

	public void setIsMasterList(boolean master) {
		this.isMasterList = master;
	}

	private void initComponent() {
		lnr = (LinearLayout) getView().findViewById(R.id.lnrFling);
		playerControl = (LinearLayout) getView().findViewById(R.id.playerControl);
		// playerControl.setOnTouchListener(this);
		imageView = (ImageView) getView().findViewById(R.id.image_tab);
		// imageView.setOnTouchListener(this);
		imagelayout = (LinearLayout) getView().findViewById(R.id.imagelayout);
		subLnr = (LinearLayout) getView().findViewById(R.id.layout);
		progressDialog = (LinearLayout) getView().findViewById(R.id.progressDialog);
		scrView = (CustomScrollView) getView().findViewById(R.id.scrView);
		scrView.setGestureDetector(detector);
		web = (WebView) getView().findViewById(R.id.text_tab);
		btnVideoPlayPause = (ImageView) getView().findViewById(R.id.btnVideoPlayPause);
		btnVideoFullScreen = (ImageView) getView().findViewById(R.id.btnVideoFullScreen);
		videoView = (VideoView) getView().findViewById(R.id.video_tab);
		title_tab = (TextView) getView().findViewById(R.id.title_tab);
		copyright_tab = (TextView) getView().findViewById(R.id.copyright_tab);
		btnVideoPlayPause.setImageResource(R.drawable.video_play);
		playerControl.setVisibility(View.GONE);
		imageView.setVisibility(View.VISIBLE);
		if (videoView.isPlaying()) {
			videoView.start();
		}
		videoView.setVisibility(View.GONE);
		btnVideoFullScreen.setVisibility(View.GONE);
		imageView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				if (v.getId() == R.id.image_tab && playerControl.getVisibility() == View.VISIBLE) {
					if (playerControl.getTag().toString().length() > 0) {
						Intent i;
						if (isPLenarySitting()) {
							i = new Intent(getActivity(), PlenumSpeakersActivity.class);
						} else {
							i = new Intent(getActivity(), PlenumTvActivity.class);
						}
						i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
						getActivity().startActivity(i);
					} else {
						createVideoPlayer();
					}
				}
			}
		});
		btnVideoFullScreen.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (videoView != null) {
					LinearLayout vv = null;
					LinearLayout childLayot = (LinearLayout) getActivity().findViewById(R.id.lnrScrollable);
					LinearLayout mainMenuLayout = (LinearLayout) getActivity().findViewById(R.id.mainMenu);
					LinearLayout sideBar = (LinearLayout) getActivity().findViewById(R.id.headerGradient);
					LinearLayout detailLayout = (LinearLayout) getActivity().findViewById(R.id.detailLayout);
					LinearLayout bottomPort = (LinearLayout) getActivity().findViewById(R.id.bottomPort);
					LinearLayout topPort = (LinearLayout) getActivity().findViewById(R.id.topPort);
					vv = (LinearLayout) getActivity().findViewById(R.id.news_main);
					ImageView imgEgle = (ImageView) getView().findViewById(R.id.imgEgle);
					LinearLayout headerG = (LinearLayout) getActivity().findViewById(R.id.headerG);
					if (vv == null) {
						vv = (LinearLayout) getActivity().findViewById(R.id.master);
					}
					if (!isFullScreen) {
						vv.setBackgroundColor(Color.BLACK);
						imgEgle.setVisibility(View.GONE);
						try {

							headerG.setVisibility(View.GONE);
						} catch (Exception e) {
						}
						isFullScreen = true;

						if (NewsActivityTablet.orienation == NewsActivityTablet.newOrientation) {
							childLayot.setVisibility(View.GONE);
							mainMenuLayout.setVisibility(View.GONE);
							sideBar.setVisibility(View.GONE);
							bottomPort.setVisibility(View.GONE);
							topPort.setVisibility(View.GONE);
							setViewsVisiblityGone();
							Display display = getActivity().getWindowManager().getDefaultDisplay();
							detailLayout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.FILL_PARENT));
							videoView.setLayoutParams(new FrameLayout.LayoutParams(display.getWidth(), display.getHeight()));
							scrView.setLayoutParams(new FrameLayout.LayoutParams(display.getWidth(), display.getHeight()));
						} else {
							bottomPort.setVisibility(View.GONE);
							topPort.setVisibility(View.GONE);
							childLayot.setVisibility(View.GONE);
							mainMenuLayout.setVisibility(View.GONE);
							sideBar.setVisibility(View.GONE);
							setViewsVisiblityGone();
							Display display = getActivity().getWindowManager().getDefaultDisplay();
							detailLayout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.FILL_PARENT));
							videoView.setLayoutParams(new FrameLayout.LayoutParams(display.getWidth(), display.getHeight()));
							scrView.setLayoutParams(new FrameLayout.LayoutParams(display.getWidth(), display.getHeight()));
						}

					} else {
						isFullScreen = false;
						vv.setBackgroundColor(Color.WHITE);
						imgEgle.setVisibility(View.VISIBLE);
						try {

							headerG.setVisibility(View.VISIBLE);
						} catch (Exception e) {
						}
						if (NewsActivityTablet.orienation == NewsActivityTablet.newOrientation) {
							childLayot.setVisibility(View.VISIBLE);
							mainMenuLayout.setVisibility(View.VISIBLE);
							sideBar.setVisibility(View.VISIBLE);
							bottomPort.setVisibility(View.GONE);
							topPort.setVisibility(View.GONE);
							setViewsVisiblity();
							detailLayout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.FILL_PARENT));
							videoView.setLayoutParams(new FrameLayout.LayoutParams(videoViewWidth, videoViewHeight));
							scrView.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT));
							scrView.postDelayed(new Runnable() {

								@Override
								public void run() {
									scrView.fullScroll(ScrollView.FOCUS_UP);
									scrView.setEnabled(false);

								}
							}, 100);
						} else {
							childLayot.setVisibility(View.VISIBLE);
							mainMenuLayout.setVisibility(View.GONE);
							sideBar.setVisibility(View.GONE);
							bottomPort.setVisibility(View.VISIBLE);
							topPort.setVisibility(View.VISIBLE);
							setViewsVisiblity();
							detailLayout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.FILL_PARENT));
							videoView.setLayoutParams(new FrameLayout.LayoutParams(videoViewWidth, videoViewHeight));
							scrView.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT));
							scrView.postDelayed(new Runnable() {

								@Override
								public void run() {
									scrView.fullScroll(ScrollView.FOCUS_UP);
									scrView.setEnabled(false);

								}
							}, 100);
						}

					}
				}
			}
		});

		btnVideoPlayPause.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				if (playerControl.getTag().toString().length() > 0) {
					Intent i;
					if (isPLenarySitting()) {
						i = new Intent(getActivity(), PlenumSpeakersActivity.class);
					} else {
						i = new Intent(getActivity(), PlenumTvActivity.class);
					}
					i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					getActivity().startActivity(i);
				} else {

					if (videoView.isPlaying()) {
						btnVideoPlayPause.setImageResource(R.drawable.video_play);
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
		web.setVisibility(View.GONE);
		title_tab.setVisibility(View.GONE);
		copyright_tab.setVisibility(View.GONE);
		scrView.setEnabled(false);
		lnr.setEnabled(false);
		subLnr.setEnabled(false);
		web.setEnabled(false);
	}

	private void setViewsVisiblity() {
		web.setVisibility(View.VISIBLE);
		title_tab.setVisibility(View.VISIBLE);
		copyright_tab.setVisibility(View.VISIBLE);
		scrView.setEnabled(true);
		lnr.setEnabled(true);
		subLnr.setEnabled(true);
		web.setEnabled(true);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		refNewsDetailsFragment = this;
		initComponent();
		scrView.postDelayed(new Runnable() {

			@Override
			public void run() {
				scrView.fullScroll(View.FOCUS_UP);
				scrView.pageScroll(View.FOCUS_UP);
				scrView.setEnabled(false);

			}
		}, 2000);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.news_detail_item, container, false);
		detector = new GestureDetector(getActivity(), new MyGestureDetector(), null, false);
		return view;
	}

	public void createNewsVideo(boolean isVideoLoad) {
		DataHolder.isVideoLoad = isVideoLoad;
		createNewsDetailsObject();
	}

	public NewsDetailsObject createNewsDetailsObject() {
		initComponent();
		NewsDatabaseAdapter newsDatabaseAdapter = new NewsDatabaseAdapter(getActivity());
		newsDatabaseAdapter.open();

		Cursor newsCursor = newsDatabaseAdapter.fetchNewsDetails(newsId);
		if (isFling || newsCursor.getString(newsCursor.getColumnIndex(NewsDatabaseAdapter.KEY_DETAILS_TITLE)) == null) {
			isFling = false;
			this.detailsXMLURL = newsCursor.getString(newsCursor.getColumnIndex(NewsDatabaseAdapter.KEY_DETAILSXML));
			if (detailsXMLURL == null || detailsXMLURL.trim().length() <= 0) {
				DataHolder.rowDBSelectedIndex++;
				if (DataHolder.rowDBSelectedIndex < DataHolder.RowDBIds.size()) {
					setNewsId((DataHolder.RowDBIds.get(DataHolder.rowDBSelectedIndex)));
					DataHolder.lastRowDBId = (DataHolder.RowDBIds.get(DataHolder.rowDBSelectedIndex));
					createNewsDetailsObject();
				} else {
					Intent intent = new Intent();
					intent.setClass(this.getActivity(), MembersListActivity.class);
					startActivity(intent);
				}
			} else {
				newsCursor.close();
				newsDatabaseAdapter.close();
				SynchronizeNewsDetailsTask synchronizeNewsDetails = new SynchronizeNewsDetailsTask(newsId, detailsXMLURL, this, this.view);
				synchronizeNewsDetails.execute(getActivity());
			}
		} else {
			this.resetView();

			newsDetailsObject.setTitle(newsCursor.getString(newsCursor.getColumnIndex(NewsDatabaseAdapter.KEY_DETAILS_TITLE)));
			if (newsCursor.getString(newsCursor.getColumnIndex(NewsDatabaseAdapter.KEY_DETAILS_IMAGE_GROSS_URL)) != null
					&& newsCursor.getString(newsCursor.getColumnIndex(NewsDatabaseAdapter.KEY_DETAILS_IMAGEURL)).trim().length() > 0) {
				newsDetailsObject.setImageURL(newsCursor.getString(newsCursor.getColumnIndex(NewsDatabaseAdapter.KEY_DETAILS_IMAGE_GROSS_URL)));
			} else {
				newsDetailsObject.setImageURL(newsCursor.getString(newsCursor.getColumnIndex(NewsDatabaseAdapter.KEY_DETAILS_IMAGEURL)));
			}
			newsDetailsObject.setImageString(newsCursor.getString(newsCursor.getColumnIndex(NewsDatabaseAdapter.KEY_DETAILS_IMAGE_STRING)));
			newsDetailsObject.setImageCopyright(newsCursor.getString(newsCursor.getColumnIndex(NewsDatabaseAdapter.KEY_DETAILS_IMAGECOPYRIGHT)));
			newsDetailsObject.setText(newsCursor.getString(newsCursor.getColumnIndex(NewsDatabaseAdapter.KEY_DETAILS_TEXT)));
			newsDetailsObject.setVideoURL(newsCursor.getString(newsCursor.getColumnIndex(NewsDatabaseAdapter.KEY_VIDEO_STREAMURL)));
			newsDetailsObject.setStatusTxt(newsCursor.getString(newsCursor.getColumnIndex(NewsDatabaseAdapter.KEY_STATUS)));
			newsDetailsObject.setStartTeaser(newsCursor.getString(newsCursor.getColumnIndex(NewsDatabaseAdapter.KEY_STARTTEASER)));
			newsCursor.close();
			newsDatabaseAdapter.close();
			if (newsDetailsObject != null) {
				NewsDetailsViewHelper.createDetailsView(newsDetailsObject, true, false, this.getActivity());
			}
			DataHolder.releaseScreenLock(this.getActivity());
			scrView.postDelayed(new Runnable() {

				@Override
				public void run() {
					scrView.fullScroll(ScrollView.FOCUS_UP);
					scrView.setEnabled(false);

				}
			}, 100);
			if (DataHolder.isVideoLoad) {
				DataHolder.isVideoLoad = false;
				createVideoPlayer();
			}
			return newsDetailsObject;
		}
		return null;

	}

	public void createVideoPlayer() {

		if (videoView != null && videoView.isPlaying()) {
			videoView = null;
			videoView = (VideoView) getView().findViewById(R.id.video_tab);
		}

		playerControl.setVisibility(View.GONE);
		imageView.setVisibility(View.GONE);
		videoView.setVisibility(View.VISIBLE);

		// videoView = new VideoView(this.getActivity());
		if (!isFullScreen)
			videoView.setLayoutParams(new FrameLayout.LayoutParams(imageView.getWidth(), imageView.getHeight()));
		//
		videoViewWidth = imageView.getWidth();
		videoViewHeight = imageView.getHeight();

		videoView.setOnPreparedListener(new OnPreparedListener() {

			@Override
			public void onPrepared(MediaPlayer mp) {
				// if (dialog != null) {
				// dialog.dismiss();
				progressDialog.setVisibility(View.GONE);
				playerControl.setVisibility(View.GONE);
				btnVideoFullScreen.setVisibility(View.VISIBLE);
				// dialog = null;
				// }
			}
		});
		videoView.setOnErrorListener(new OnErrorListener() {

			@Override
			public boolean onError(MediaPlayer mp, int what, int extra) {
				// if (dialog != null) {
				// dialog.dismiss();
				// dialog=null;
				progressDialog.setVisibility(View.GONE);
				playerControl.setVisibility(View.VISIBLE);
				btnVideoPlayPause.setImageResource(R.drawable.video_play);
				imageView.setVisibility(View.VISIBLE);
				videoView.setVisibility(View.GONE);
				// }
				return false;
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

				scrView.setEnabled(false);
				return true;
			}
		});

		videoView.setZOrderMediaOverlay(false);
		videoView.setVideoPath(newsDetailsObject.getVideoURL());
		progressDialog.setVisibility(View.VISIBLE);
		// if (dialog == null)
		// dialog = ProgressDialog.show(getActivity(), "", "Video wird geladen",
		// true);

		// dialog.setCancelable(true);
		// imagelayout.removeViewAt(0);
		// imagelayout.addView(videoView, 0);
		videoView.start();
		btnVideoPlayPause.setImageResource(R.drawable.pause);
	}

	private class MyGestureDetector extends SimpleOnGestureListener {
		@Override
		public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {

			if (!isFullScreen) {

				if (e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
					DataHolder.rowDBSelectedIndex++;
					checkIndex(false);

					return false;// right to Left
				} else if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
					DataHolder.rowDBSelectedIndex--;
					checkIndex(true);

					return false; // Left to right
				} else {
					// WebView.HitTestResult hr = ((WebView)
					// web).getHitTestResult();
					//
					// if (hr.getExtra() != null) {
					// Intent myIntent = new Intent(Intent.ACTION_VIEW,
					// Uri.parse(hr.getExtra()));
					// startActivity(myIntent);
					// }
					return false;
				}
			}
			return false;
		}

		@Override
		public boolean onSingleTapConfirmed(MotionEvent e) {
			web.setWebViewClient(new WebActivityClient(getActivity()));
			return false;
		}
	}

	private void checkIndex(boolean isLeftToRight) {
		if (isMasterList && DataHolder.rowDBSelectedIndex == (DataHolder.RowDBIds.size() - 2)) {
			DataHolder.rowDBSelectedIndex = isLeftToRight ? DataHolder.rowDBSelectedIndex -= 1 : 0;
		} else if (isMasterList && DataHolder.rowDBSelectedIndex == (DataHolder.RowDBIds.size() - 1)) {
			DataHolder.rowDBSelectedIndex = isLeftToRight ? DataHolder.rowDBSelectedIndex -= 1 : 0;
		} else if (DataHolder.rowDBSelectedIndex > (DataHolder.RowDBIds.size() - 1)) {
			DataHolder.rowDBSelectedIndex = 0;
		} else if (DataHolder.rowDBSelectedIndex < 0) {
			DataHolder.rowDBSelectedIndex = isMasterList ? DataHolder.RowDBIds.size() - 2 : DataHolder.RowDBIds.size() - 1;
		}
		isFling = true;
		DataHolder._oldPosition = DataHolder.rowDBSelectedIndex;
		this.setNewsId(DataHolder.RowDBIds.get(DataHolder.rowDBSelectedIndex));
		DataHolder.lastRowDBId = DataHolder.RowDBIds.get(DataHolder.rowDBSelectedIndex);
		this.createNewsDetailsObject();
	}

	private void resetView() {
		((TextView) view.findViewById(R.id.title_tab)).setText("");
		((ImageView) view.findViewById(R.id.image_tab)).setImageBitmap(null);
		((TextView) view.findViewById(R.id.copyright_tab)).setText("");
		((WebView) view.findViewById(R.id.text_tab)).loadUrl("");
	}

	public boolean isPLenarySitting() {
		PlenumXMLParser pp = new PlenumXMLParser();
		PlenumObject plenum = null;
		try {
			plenum = pp.parseMain(PlenumXMLParser.MAIN_PLENUM_URL);
			if ((plenum != null) && (plenum.getStatus().intValue() == 1)) {
				return true;
			}
		} catch (Exception e) {
		}

		return false;
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}

	public void handleWebview() {
		final CustomScrollView scrView = (CustomScrollView) getActivity().findViewById(R.id.scrView);
		scrView.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT));
		scrView.postDelayed(new Runnable() {

			@Override
			public void run() {
				scrView.fullScroll(ScrollView.FOCUS_UP);
				WebView text = (WebView) getActivity().findViewById(R.id.text_tab);
				scrView.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT));
				text.setBackgroundColor(0x00000000);
				String reformatedText = TextHelper.customizedFromHtmlforWebViewTab(newsDetailsObject.getText());
				text.clearView();
				text.measure(10, 10);
				text.loadDataWithBaseURL("file:///android_asset/", "", "text/html", "UTF-8", null);
				text.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
				text.loadDataWithBaseURL("file:///android_asset/", reformatedText, "text/html", "UTF-8", null);
				text.setBackgroundColor(Color.TRANSPARENT);
				DataHolder.finishDialog(text);
			}
		}, 1000);

	}
}