package de.bundestag.android.sections.visitors;

import android.database.Cursor;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import de.bundestag.android.BaseFragment;
import de.bundestag.android.R;
import de.bundestag.android.constant.DataHolder;
import de.bundestag.android.customviews.CustomScrollView;
import de.bundestag.android.customviews.WebActivityClient;
import de.bundestag.android.parser.objects.VisitorsArticleObject;
import de.bundestag.android.storage.VisitorsDatabaseAdapter;

public class VisitorsOffersDetailsFragment extends BaseFragment {
	public Integer listId;
	private View view = null;
	int vfId = -1;

	public int newsId = -1;
	private static final int SWIPE_MIN_DISTANCE = 100;
	private static final int SWIPE_THRESHOLD_VELOCITY = 200;

	private View tmpViewForWeb = null;
	public String detailsXMLURL;
	private GestureDetector detector;
	LinearLayout lnr = null;
	LinearLayout subLnr = null;
	CustomScrollView scrView = null;
	boolean isFling = false;
	WebView web = null;

	private void initComponent() {
		lnr = (LinearLayout) getView().findViewById(R.id.lnrFling);
		subLnr = (LinearLayout) getView().findViewById(R.id.layout);
		scrView = (CustomScrollView) getView().findViewById(R.id.scrView);
		scrView.setEnabled(false);
		web = (WebView) getView().findViewById(R.id.text_tab);
		scrView.setGestureDetector(detector);
		// subLnr.setOnTouchListener(this);
		// web.setOnTouchListener(this);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		initComponent();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		view = inflater.inflate(R.layout.news_detail_item, container, false);
		detector = new GestureDetector(getActivity(), new MyGestureDetector(), null, false);
		return view;
	}

	public void setVisitorsId(int vfId) {
		this.vfId = vfId;
	}

	public VisitorsArticleObject createVisitorsDetailsObject() {
		resetView();
		VisitorsDatabaseAdapter visitorsDatabaseAdapter = new VisitorsDatabaseAdapter(this.getActivity());
		visitorsDatabaseAdapter.open();

		Cursor newsCursor1 = visitorsDatabaseAdapter.articleDetails(vfId);
		String newsId = newsCursor1.getString(newsCursor1.getColumnIndex(VisitorsDatabaseAdapter.KEY_ID));
		newsCursor1.close();

		final Cursor newsCursor = visitorsDatabaseAdapter.articleDetailsFromNewsId(newsId);

		VisitorsArticleObject newsDetailsObject = new VisitorsArticleObject();

		newsDetailsObject.setTitle(newsCursor.getString(newsCursor.getColumnIndex(VisitorsDatabaseAdapter.KEY_ARTICLE_TITLE)));
		// newsDetailsObject.setImageURL(newsCursor.getString(newsCursor.getColumnIndex(VisitorsDatabaseAdapter.KEY_ARTICLE_IMAGEURL)));
		newsDetailsObject.setImageString(newsCursor.getString(newsCursor.getColumnIndex(VisitorsDatabaseAdapter.KEY_ARTICLE_IMAGESTRING)));
		newsDetailsObject.setImageCopyright(newsCursor.getString(newsCursor.getColumnIndex(VisitorsDatabaseAdapter.KEY_ARTICLE_IMAGECOPYRIGHT)));
		newsDetailsObject.setText(newsCursor.getString(newsCursor.getColumnIndex(VisitorsDatabaseAdapter.KEY_ARTICLE_TEXT)));
		VisitorsDetailsFragmentViewHelper.createDetailsView(newsDetailsObject, getActivity());

		newsCursor.close();
		visitorsDatabaseAdapter.close();
		DataHolder.releaseScreenLock(this.getActivity());
		// DataHolder.dismissProgress();

		try {
			final HorizontalScrollView s = (HorizontalScrollView) getActivity().findViewById(108);
			s.postDelayed(new Runnable() {

				@Override
				public void run() {
					// s.fullScroll(ScrollView.FOCUS_RIGHT);
					s.scrollBy(DataHolder.calculatedScreenResolution +5, 0);

				}
			}, 2000);

		} catch (Exception e) {
		}

		final ScrollView sV = (ScrollView) getActivity().findViewById(R.id.scrView);
		sV.postDelayed(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				sV.fullScroll(ScrollView.FOCUS_UP);
				// newsCursor.close();
			}
		}, 500);

		return newsDetailsObject;
	}

	private void resetView() {
		((TextView) view.findViewById(R.id.title_tab)).setText("");
		((TextView) view.findViewById(R.id.copyright_tab)).setText("");
		((ImageView) view.findViewById(R.id.image_tab)).setImageBitmap(null);
		// ((WebView) view.findViewById(R.id.text_tab)).loadUrl("");
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
					return false;
				}
			} catch (Exception e) {
				return false;
			}

		}

		@Override
		public boolean onSingleTapConfirmed(MotionEvent e) {
			web.setWebViewClient(new WebActivityClient(getActivity()));
			return false;
		}
	}

	private void checkIndex() {
		if (DataHolder.rowDBSelectedIndex > (DataHolder.RowDBIds.size() - 1)) {
			DataHolder.rowDBSelectedIndex = 0;
		} else if (DataHolder.rowDBSelectedIndex < 0) {
			DataHolder.rowDBSelectedIndex = DataHolder.RowDBIds.size() - 1;
		}
		isFling = true;
		DataHolder.lastRowDBId = DataHolder.RowDBIds.get(DataHolder.rowDBSelectedIndex);
		DataHolder._oldPosition = DataHolder.rowDBSelectedIndex;
		this.setVisitorsId(DataHolder.RowDBIds.get(DataHolder.rowDBSelectedIndex));
		this.createVisitorsDetailsObject();
	}

}