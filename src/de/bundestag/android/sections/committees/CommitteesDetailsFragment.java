package de.bundestag.android.sections.committees;

import android.app.ProgressDialog;
import android.database.Cursor;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.VideoView;
import de.bundestag.android.BaseFragment;
import de.bundestag.android.R;
import de.bundestag.android.constant.DataHolder;
import de.bundestag.android.parser.CommitteesXMLParser;
import de.bundestag.android.parser.objects.CommitteesDetailsObject;
import de.bundestag.android.storage.CommitteesDatabaseAdapter;
import de.bundestag.android.synchronization.SynchronizeCommitteeNewsFirstDetailsTask;

/**
 * 
 * @author GTL use to show details of news
 */
public class CommitteesDetailsFragment extends BaseFragment implements OnTouchListener {
	public Integer listId;

	public int rowId = -1;
	private static final int SWIPE_MIN_DISTANCE = 100;
	private static final int SWIPE_THRESHOLD_VELOCITY = 200;

	public String detailsXMLURL;
	private GestureDetector detector;
	private View view = null;
	private View tmpViewForWeb = null;
	LinearLayout lnr = null;
	LinearLayout subLnr = null;
	ImageView imageView = null;
	ScrollView scrView = null;
	LinearLayout playerControl = null;
	LinearLayout imagelayout = null;
	boolean isFling = false;
	boolean isMasterList = false;
	CommitteesDetailsObject committeesDetailsObject = new CommitteesDetailsObject();
	ProgressDialog dialog = null;
	VideoView videoView = null;
	boolean wasPlaying = false;
	boolean isFullScreen = false;
	private ImageView btnVideoPlayPause;
	private TextView copyright_tab;
	private WebView web;

	public void setRowId(int rowId) {
		this.rowId = rowId;
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
		scrView = (ScrollView) getView().findViewById(R.id.scrView);
		scrView.setEnabled(false);
		web = (WebView) getView().findViewById(R.id.text_tab);
		btnVideoPlayPause = (ImageView) getView().findViewById(R.id.btnVideoPlayPause);
		videoView = (VideoView) getView().findViewById(R.id.video_tab);
		copyright_tab = (TextView) getView().findViewById(R.id.copyright_tab);
		lnr.setOnTouchListener(this);
		subLnr.setOnTouchListener(this);
		web.setOnTouchListener(this);
		btnVideoPlayPause.setImageResource(R.drawable.video_play);
		playerControl.setVisibility(View.GONE);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
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
//		detector = new GestureDetector(getActivity(), new MyGestureDetector(), null, false);
		return view;
	}

//	public void createCommitteesDetailsObject() {
//		initComponent();
//		CommitteesDatabaseAdapter committeesDatabaseAdapter = new CommitteesDatabaseAdapter(getActivity());
//		committeesDatabaseAdapter.open();
//
//		Cursor committeeCursor = committeesDatabaseAdapter.fetchCommittees(rowId);
//		committeeCursor.moveToFirst();
//
//		this.resetView();
//
//		committeesDetailsObject.setName(committeeCursor.getString(committeeCursor.getColumnIndex(CommitteesDatabaseAdapter.KEY_DETAILS_NAME)));
//		committeesDetailsObject.setPhotoURL(committeeCursor.getString(committeeCursor.getColumnIndex(CommitteesDatabaseAdapter.KEY_DETAILS_PHOTOURL)));
//		committeesDetailsObject.setPhotoString(committeeCursor.getString(committeeCursor.getColumnIndex(CommitteesDatabaseAdapter.KEY_DETAILS_PHOTOSTRING)));
//		committeesDetailsObject.setPhotoCopyright(committeeCursor.getString(committeeCursor.getColumnIndex(CommitteesDatabaseAdapter.KEY_DETAILS_PHOTOCOPYRIGHT)));
//		committeesDetailsObject.setDescription(committeeCursor.getString(committeeCursor.getColumnIndex(CommitteesDatabaseAdapter.KEY_DETAILS_DESCRIPTION)));
//		committeeCursor.close();
//		committeesDatabaseAdapter.close();
//		if (committeesDetailsObject != null) {
//			CommitteesDetailsViewHelper.createDetailsViewTab(committeesDetailsObject, this.getActivity());
//		}
//
//		scrView.setEnabled(true);
//		scrView.postDelayed(new Runnable() {
//
//			@Override
//			public void run() {
//				scrView.fullScroll(ScrollView.FOCUS_UP);
//				scrView.pageScroll(View.FOCUS_UP);
//				scrView.setEnabled(false);
//			}
//		}, 500);
//		DataHolder.releaseScreenLock(this.getActivity());
//	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		{
			if (!isFullScreen) {

			}
//			detector.onTouchEvent(event);
			scrView.setEnabled(false);
			scrView.setSmoothScrollingEnabled(false);
			WebView text = (WebView) getActivity().findViewById(R.id.text_tab);
			text.setOnTouchListener(new View.OnTouchListener() {

				public boolean onTouch(View v, MotionEvent event) {

					/* if (event.getAction() == MotionEvent.ACTION_MOVE) { */
					tmpViewForWeb = v;
//					detector.onTouchEvent(event);
					/*
					 * scrView.setEnabled(false);
					 * scrView.setSmoothScrollingEnabled(false); return false; }
					 * else if(event.getAction()==MotionEvent.ACTION_UP){
					 * WebView.HitTestResult hr = ((WebView)
					 * v).getHitTestResult(); if (hr.getExtra() != null) { } }
					 */
					return false;
				}
			});

		}
		return true;
	}

	private class MyGestureDetector extends SimpleOnGestureListener {
		@Override
		public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
			if (e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
				DataHolder.rowDBSelectedIndex++;
				checkIndex(false);

				return false;// right to Left
			} else if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
				DataHolder.rowDBSelectedIndex--;
				checkIndex(true);

				return false; // Left to right
			} else {
				if (tmpViewForWeb != null) {
					WebView.HitTestResult hr = ((WebView) tmpViewForWeb).getHitTestResult();

					if (hr.getExtra() != null) {
						scrView.setEnabled(true);
						scrView.setSmoothScrollingEnabled(true);
					}
					return true;
				} else {

					return false;
				}
			}
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
		this.setRowId(DataHolder.RowDBIds.get(DataHolder.rowDBSelectedIndex));
		this.createCommitteesDetailsObject();
	}

	private void resetView() {
		((TextView) view.findViewById(R.id.title_tab)).setText("");
		((ImageView) view.findViewById(R.id.image_tab)).setImageBitmap(null);
		((TextView) view.findViewById(R.id.copyright_tab)).setText("");
		((WebView) view.findViewById(R.id.text_tab)).loadUrl("");
	}

	public void createCommitteesDetailsObject() {
		CommitteesDatabaseAdapter committeesNewsDetailsDatabaseAdapter = new CommitteesDatabaseAdapter(getActivity());
		committeesNewsDetailsDatabaseAdapter.open();

		Cursor newsCursor = committeesNewsDetailsDatabaseAdapter.getCommitteeNewsFromIdString(DataHolder.committeeRowId);

		if (newsCursor == null || newsCursor.getCount() == 0) {
			if(DataHolder.isOnline(getActivity())){
				
				CommitteesXMLParser committeesParser = new CommitteesXMLParser();
				SynchronizeCommitteeNewsFirstDetailsTask synchronizeCommitteeNewsDetailsTask = new SynchronizeCommitteeNewsFirstDetailsTask(this);
				synchronizeCommitteeNewsDetailsTask.execute(this.getActivity());
				newsCursor.close();
				committeesNewsDetailsDatabaseAdapter.close();
			}else{
				TextView title = (TextView) getActivity().findViewById(R.id.title_tab);
				title.setText("Zur Zeit scheint keine Internet-Verbindung verfügbar zu sein.");
//				DataHolder.alertboxOk(getActivity());
			}
		} else {
			initComponent();
			Cursor committeeCursor = committeesNewsDetailsDatabaseAdapter.getCommitteeNewsFromIdString(DataHolder.committeeRowId);
			committeeCursor.moveToFirst();
			
			this.resetView();
			committeesDetailsObject.setName(committeeCursor.getString(committeeCursor.getColumnIndex(CommitteesDatabaseAdapter.KEY_NEWS_DETAILS_TITLE)));
			committeesDetailsObject.setPhotoURL(committeeCursor.getString(committeeCursor.getColumnIndex(CommitteesDatabaseAdapter.KEY_NEWS_IMAGEURL)));
			committeesDetailsObject.setPhotoString(committeeCursor.getString(committeeCursor.getColumnIndex(CommitteesDatabaseAdapter.KEY_NEWS_IMAGESTRING)));
			committeesDetailsObject.setPhotoCopyright(committeeCursor.getString(committeeCursor.getColumnIndex(CommitteesDatabaseAdapter.KEY_NEWS_IMAGECOPYRIGHT)));
			committeesDetailsObject.setDescription(committeeCursor.getString(committeeCursor.getColumnIndex(CommitteesDatabaseAdapter.KEY_NEWS_DETAILS_TEXT)));
			committeeCursor.close();
			committeesNewsDetailsDatabaseAdapter.close();
			if (committeesDetailsObject != null) {
				CommitteesDetailsViewHelper.createDetailsViewTab(committeesDetailsObject, this.getActivity());
			}

			scrView.setEnabled(true);
			scrView.postDelayed(new Runnable() {

				@Override
				public void run() {
					scrView.fullScroll(ScrollView.FOCUS_UP);
					scrView.pageScroll(View.FOCUS_UP);
					scrView.setEnabled(false);
				}
			}, 2000);
			DataHolder.releaseScreenLock(this.getActivity());
		}
	}

}