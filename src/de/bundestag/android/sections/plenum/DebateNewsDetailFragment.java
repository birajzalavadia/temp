package de.bundestag.android.sections.plenum;

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
import de.bundestag.android.BaseFragment;
import de.bundestag.android.R;
import de.bundestag.android.constant.DataHolder;
import de.bundestag.android.parser.objects.NewsDetailsObject;
import de.bundestag.android.sections.news.NewsDetailsViewHelper;
import de.bundestag.android.storage.NewsDatabaseAdapter;
import de.bundestag.android.synchronization.SynchronizeDebateNewsDetailsTask;

public class DebateNewsDetailFragment extends BaseFragment implements OnTouchListener {
	private View view;
	public String detailsXMLURL;
	private int newsId = -1;

	private static final int SWIPE_MIN_DISTANCE = 100;
	private static final int SWIPE_THRESHOLD_VELOCITY = 200;

	private View tmpViewForWeb = null;
	private GestureDetector detector;
	LinearLayout lnr = null;
	LinearLayout subLnr = null;
	ScrollView scrView = null;
	boolean isFling = false;
	WebView web;
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		initComponent();
		scrView.setEnabled(true);
		scrView.postDelayed(new Runnable() {

			@Override
			public void run() {
				scrView.fullScroll(ScrollView.FOCUS_UP);
				scrView.pageScroll(View.FOCUS_UP);
			}
		}, 1);
		scrView.setEnabled(false);
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		// TODO Auto-generated method stub
		WebView text = (WebView) getActivity().findViewById(R.id.text_tab);
		text.setOnTouchListener(new View.OnTouchListener() {

			public boolean onTouch(View v, MotionEvent event) {
				tmpViewForWeb = v;
				detector.onTouchEvent(event);
				return false;
			}
		});
		return true;
	}

	private void initComponent() {
		lnr = (LinearLayout) getView().findViewById(R.id.lnrFling);
		subLnr = (LinearLayout) getView().findViewById(R.id.layout);
		scrView = (ScrollView) getView().findViewById(R.id.scrView);
		scrView.setEnabled(false);
		web = (WebView) getView().findViewById(R.id.text_tab);
		lnr.setOnTouchListener(this);
		subLnr.setOnTouchListener(this);
		web.setOnTouchListener(this);
	}

	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.news_detail_item, container, false);
		detector = new GestureDetector(getActivity(), new MyGestureDetector(), null, false);
		return view;
	}

	public void setNewsId(int newsId) {
		this.newsId = newsId;
	}

	public NewsDetailsObject createNewsDetailsObject() {

		NewsDatabaseAdapter newsDatabaseAdapter = new NewsDatabaseAdapter(getActivity());
		newsDatabaseAdapter.open();
		initComponent();
		Cursor newsCursor = newsDatabaseAdapter.fetchNewsDetails(newsId);
		if (newsCursor.getString(newsCursor.getColumnIndex(NewsDatabaseAdapter.KEY_DETAILS_TITLE)) == null) {
			this.detailsXMLURL = newsCursor.getString(newsCursor.getColumnIndex(NewsDatabaseAdapter.KEY_DETAILSXML));
			SynchronizeDebateNewsDetailsTask synchronizeNewsDetails = new SynchronizeDebateNewsDetailsTask();
			synchronizeNewsDetails.execute(getActivity());
			newsCursor.close();
			newsDatabaseAdapter.close();
		} else {
			resetView();
			NewsDetailsObject newsDetailsObject = new NewsDetailsObject();

			newsDetailsObject.setTitle(newsCursor.getString(newsCursor.getColumnIndex(NewsDatabaseAdapter.KEY_DETAILS_TITLE)));
			newsDetailsObject.setImageURL(newsCursor.getString(newsCursor.getColumnIndex(NewsDatabaseAdapter.KEY_DETAILS_IMAGEURL)));
			// newsDetailsObject.setImageString(newsCursor.getString(newsCursor.getColumnIndex(NewsDatabaseAdapter.KEY_DETAILS_IMAGE_STRING)));
			newsDetailsObject.setImageCopyright(newsCursor.getString(newsCursor.getColumnIndex(NewsDatabaseAdapter.KEY_DETAILS_IMAGECOPYRIGHT)));
			newsDetailsObject.setText(newsCursor.getString(newsCursor.getColumnIndex(NewsDatabaseAdapter.KEY_DETAILS_TEXT)));

			newsCursor.close();
			newsDatabaseAdapter.close();
			NewsDetailsViewHelper.createDetailsView(newsDetailsObject, true, true, getActivity());
			scrView.setEnabled(true);
			scrView.postDelayed(new Runnable() {

				@Override
				public void run() {
					scrView.fullScroll(ScrollView.FOCUS_UP);
					scrView.pageScroll(View.FOCUS_UP);
				}
			}, 1);
			scrView.setEnabled(false);
			DataHolder.releaseScreenLock(this.getActivity());
			return newsDetailsObject;
		}

		return null;
	}

	private void resetView() {
		((TextView) view.findViewById(R.id.title_tab)).setText("");
		((TextView) view.findViewById(R.id.copyright_tab)).setText("");
		((ImageView) view.findViewById(R.id.image_tab)).setImageBitmap(null);
		((WebView) view.findViewById(R.id.text_tab)).loadUrl("");
	}

	private class MyGestureDetector extends SimpleOnGestureListener {
		@Override
		public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {

			try {

				if (e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
					DataHolder.rowDBSelectedIndex++;
					checkIndex();
					return false;
				} else if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
					DataHolder.rowDBSelectedIndex--;
					checkIndex();
					return false; // Left to right
				} else {

					WebView.HitTestResult hr = ((WebView) web).getHitTestResult();

					if (hr.getExtra() != null) {
						Intent myIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(hr.getExtra()));
						startActivity(myIntent);
					}
					return false;
				}
			}

			catch (Exception e) {
				// e.printStackTrace();
				return false;
			}
		}
	}

	private void checkIndex() {
		if (DataHolder.rowDBSelectedIndex > (DataHolder.RowDBIds.size() - 1)) {
			DataHolder.rowDBSelectedIndex = 0;
		} else if (DataHolder.rowDBSelectedIndex < 0) {
			DataHolder.rowDBSelectedIndex = DataHolder.RowDBIds.size() - 1;
		}
		DataHolder._oldPosition = DataHolder.rowDBSelectedIndex;
		isFling = true;
		this.setNewsId(DataHolder.RowDBIds.get(DataHolder.rowDBSelectedIndex));
		this.createNewsDetailsObject();
	}

	@Override
	protected void finalize() throws Throwable {
		super.finalize();

		view = null;
		detailsXMLURL = null;
		tmpViewForWeb = null;
		detector = null;
		lnr = null;
		subLnr = null;
		scrView = null;
	}
}