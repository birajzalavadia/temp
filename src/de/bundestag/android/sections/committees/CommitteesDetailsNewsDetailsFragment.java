package de.bundestag.android.sections.committees;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
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
import de.bundestag.android.customviews.WebActivityClient;
import de.bundestag.android.parser.objects.CommitteesDetailsNewsDetailsObject;
import de.bundestag.android.parser.objects.CommitteesDetailsObject;
import de.bundestag.android.storage.CommitteesDatabaseAdapter;
import de.bundestag.android.synchronization.BaseSynchronizeTask;
import de.bundestag.android.synchronization.SynchronizeCommitteeNewsDetailsTask;

public class CommitteesDetailsNewsDetailsFragment extends BaseFragment implements OnTouchListener {
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

	private BaseSynchronizeTask task = null;

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
		detector = new GestureDetector(getActivity(), new MyGestureDetector(), null, false);
		return view;
	}

	public CommitteesDetailsNewsDetailsObject createNewsDetailsObject() {
		CommitteesDatabaseAdapter committeesNewsDetailsDatabaseAdapter = new CommitteesDatabaseAdapter(getActivity());
		committeesNewsDetailsDatabaseAdapter.open();

		Cursor newsCursor = committeesNewsDetailsDatabaseAdapter.fetchCommitteesDetailsNewsDetails(rowId);
		
		if (newsCursor.getString(newsCursor.getColumnIndex(CommitteesDatabaseAdapter.KEY_NEWS_DETAILS_TITLE)) == null) {
			detailsXMLURL = newsCursor.getString(newsCursor.getColumnIndex(CommitteesDatabaseAdapter.KEY_NEWS_DETAILSXML));
			SynchronizeCommitteeNewsDetailsTask synchronizeCommitteeNewsDetailsTask = new SynchronizeCommitteeNewsDetailsTask(rowId, detailsXMLURL, this, this.view);
			task = synchronizeCommitteeNewsDetailsTask;
			synchronizeCommitteeNewsDetailsTask.execute(getActivity());
			newsCursor.close();
			committeesNewsDetailsDatabaseAdapter.close();
		} else {
			this.resetView();
			CommitteesDetailsNewsDetailsObject committeesDetailsNewsDetailsObject = new CommitteesDetailsNewsDetailsObject();

			committeesDetailsNewsDetailsObject.setTitle(newsCursor.getString(newsCursor.getColumnIndex(CommitteesDatabaseAdapter.KEY_NEWS_DETAILS_TITLE)));
			committeesDetailsNewsDetailsObject.setImageURL(newsCursor.getString(newsCursor.getColumnIndex(CommitteesDatabaseAdapter.KEY_NEWS_DETAILS_IMAGEURL)));
			committeesDetailsNewsDetailsObject.setImageCopyright(newsCursor.getString(newsCursor.getColumnIndex(CommitteesDatabaseAdapter.KEY_NEWS_DETAILS_IMAGECOPYRIGHT)));
			committeesDetailsNewsDetailsObject.setText(newsCursor.getString(newsCursor.getColumnIndex(CommitteesDatabaseAdapter.KEY_NEWS_DETAILS_TEXT)));

			newsCursor.close();
			committeesNewsDetailsDatabaseAdapter.close();
			CommitteesDetailsNewsDetailsViewHelper.createCommitteesDetailsNewsDetailsViewTab(committeesDetailsNewsDetailsObject, getActivity());
			DataHolder.releaseScreenLock(this.getActivity());
			DataHolder.lastRowDBId = rowId;
			
			final ScrollView sV = (ScrollView) getActivity().findViewById(R.id.scrView);
			sV.postDelayed(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					sV.fullScroll(ScrollView.FOCUS_UP);
				}
			}, 1000);
			return committeesDetailsNewsDetailsObject;
		}

		return null;
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		{
			if (!isFullScreen) {

			}
			detector.onTouchEvent(event);
			scrView.setEnabled(false);
			scrView.setSmoothScrollingEnabled(false);
			WebView text = (WebView) getActivity().findViewById(R.id.text_tab);
			text.setOnTouchListener(new View.OnTouchListener() {

				public boolean onTouch(View v, MotionEvent event) {

					/* if (event.getAction() == MotionEvent.ACTION_MOVE) { */
					tmpViewForWeb = v;
					detector.onTouchEvent(event);
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
				return false; 
			}
		}
		@Override
		public boolean onSingleTapConfirmed(MotionEvent e) {
			WebView text = (WebView) getActivity().findViewById(R.id.text_tab);
			text.setWebViewClient(new WebActivityClient(getActivity()));
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
		this.setRowId(DataHolder.RowDBIds.get(DataHolder.rowDBSelectedIndex));
		DataHolder.lastRowDBId = DataHolder.RowDBIds.get(DataHolder.rowDBSelectedIndex);
		this.createNewsDetailsObject();
	}

	private void resetView() {
		((TextView) view.findViewById(R.id.title_tab)).setText("");
		((ImageView) view.findViewById(R.id.image_tab)).setImageBitmap(null);
		((TextView) view.findViewById(R.id.copyright_tab)).setText("");
		((WebView) view.findViewById(R.id.text_tab)).loadUrl("");
	}

	@Override
	protected void finalize() throws Throwable {
		super.finalize();

		detailsXMLURL = null;
		detector = null;
		view = null;
		tmpViewForWeb = null;
		lnr = null;
		subLnr = null;
		imageView = null;
		scrView = null;
		playerControl = null;
		imagelayout = null;

		committeesDetailsObject = null;
		dialog = null;
		videoView = null;
		btnVideoPlayPause = null;
		copyright_tab = null;
		web = null;
		task = null;
	}

}