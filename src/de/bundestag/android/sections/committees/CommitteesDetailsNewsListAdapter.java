package de.bundestag.android.sections.committees;

import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import de.bundestag.android.R;
import de.bundestag.android.constant.AppConstant;
import de.bundestag.android.constant.DataHolder;
import de.bundestag.android.helpers.EllipsizingTextView;
import de.bundestag.android.helpers.ImageHelper;
import de.bundestag.android.helpers.ImageThreadLoader;
import de.bundestag.android.helpers.ImageThreadLoader.ImageLoadedListener;
import de.bundestag.android.helpers.TextHelper;
import de.bundestag.android.storage.CommitteesDatabaseAdapter;

/**
 * Committees list adapter (list item renderer).
 * 
 * This class knows how to show news elements for a list.
 * 
 * Uses the database cursor to extract the data for the list.
 */
public class CommitteesDetailsNewsListAdapter extends CursorAdapter {
	private static final int TOP_LAYOUT = 0;

	private static final int GENERAL_LAYOUT = 1;

	private static String COMMITTE_NAME = "";

	private LayoutInflater mInflater;

	private ImageThreadLoader imageLoader;

	private Map<String, Bitmap> cachedImages;

	public CommitteesDetailsNewsListAdapter(Context context, Cursor cursor, String committeeName) {
		super(context, cursor, true);
		mInflater = LayoutInflater.from(context);

		COMMITTE_NAME = committeeName;

		imageLoader = new ImageThreadLoader();

		cachedImages = new HashMap<String, Bitmap>();
	}

	@Override
	public int getViewTypeCount() {
		return 2;
	}

	@Override
	public int getItemViewType(int position) {
		if (AppConstant.isFragmentSupported) {
			return GENERAL_LAYOUT;
		} else {
			return (position == 0) ? TOP_LAYOUT : GENERAL_LAYOUT;
		}
	}

	@Override
	public View newView(Context context, Cursor cursor, ViewGroup parent) {
		final View view;

		int type = getItemViewType(cursor.getPosition());
		if (type == TOP_LAYOUT) {
			view = mInflater.inflate(R.layout.committees_detail_news_first_item, parent, false);
		} else {
			view = mInflater.inflate(R.layout.news_list_item, parent, false);
		}
		if (AppConstant.isFragmentSupported) {
			view.getLayoutParams().width = DataHolder.calculatedScreenResolution;
		}

		return view;
	}

	@Override
	public void bindView(View view, Context context, Cursor cursor) {

		LinearLayout imageHolder = (LinearLayout) view.findViewById(R.id.imageHolder);
		ImageView image = (ImageView) view.findViewById(R.id.image);

		final String imageURL = cursor.getString(cursor.getColumnIndex(CommitteesDatabaseAdapter.KEY_NEWS_IMAGEURL));
		if ((imageURL != null) && (!imageURL.equals(""))) {
			if (imageHolder != null) {
				imageHolder.setVisibility(View.VISIBLE);
			}

			int type = getItemViewType(cursor.getPosition());
			if (type == TOP_LAYOUT) {
				Bitmap imageToScale = getImage(imageURL, image, type);
				if (imageToScale != null) {
					image.setImageBitmap(ImageHelper.getScaleImageURL((Activity) context, imageToScale));
				} else {
					image.setImageBitmap(imageToScale);
				}
			} else {
				image.setImageBitmap(getImage(imageURL, image, type));
			}

			TextView image_copy = (TextView) view.findViewById(R.id.image_copy);
			image_copy.setText("\u00A9 " + cursor.getString(cursor.getColumnIndex(CommitteesDatabaseAdapter.KEY_NEWS_IMAGECOPYRIGHT)));
		} else {
			if (imageHolder != null) {
				imageHolder.setVisibility(View.GONE);
			}
		}

		TextView title = (TextView) view.findViewById(R.id.title);
		title.setText(TextHelper.customizedFromHtml(cursor.getString(cursor.getColumnIndex(CommitteesDatabaseAdapter.KEY_NEWS_TITLE))));
//        System.out.println("-------For Check---------->"+TextHelper.customizedFromHtml(cursor.getString(cursor.getColumnIndex(CommitteesDatabaseAdapter.KEY_NEWS_TEASER))));
		if(AppConstant.isFragmentSupported){
		EllipsizingTextView description = (EllipsizingTextView) view.findViewById(R.id.description);
		description.setMaxLines(4);
		description.setText(TextHelper.customizedFromHtml(cursor.getString(cursor.getColumnIndex(CommitteesDatabaseAdapter.KEY_NEWS_TEASER))));
		}else{
			TextView description = (TextView) view.findViewById(R.id.description);
			description.setText(TextHelper.customizedFromHtml(cursor.getString(cursor.getColumnIndex(CommitteesDatabaseAdapter.KEY_NEWS_TEASER))));
		}
		
		String date = cursor.getString(cursor.getColumnIndex(CommitteesDatabaseAdapter.KEY_NEWS_DATE));
		TextView dateField = ((TextView) view.findViewById(R.id.article_date));
		dateField.setText(date);
		dateField.setVisibility(View.VISIBLE);
		int type = getItemViewType(cursor.getPosition());
		if (type == TOP_LAYOUT) {

			TextView committeeName = (TextView) view.findViewById(R.id.committeeName);
			committeeName.setText(COMMITTE_NAME);
		}
		// Watermark background height adaptation
		if (cursor.getPosition() == 0) {
			View content = view.findViewById(R.id.image_container);
			ImageView img = (ImageView) view.findViewById(R.id.back_img);

			if (content != null && img != null) {
				img.getLayoutParams().height = content.getMeasuredHeight();
			}
		}
	}

	/**
	 * Method to assist lazy loading and caching of images.
	 * 
	 * First checks to see if we have the image in the activity.
	 * 
	 * If not, then go and check with the image loader.
	 */
	private Bitmap getImage(final String imageURL, final ImageView image, final int type) {
		Bitmap cachedImage = null;

		if (cachedImages.containsKey(imageURL)) {
			// Log.d("yikes", "found cached image");
			return cachedImages.get(imageURL);
		} else {
			try {
				cachedImage = imageLoader.loadImage(imageURL, new ImageLoadedListener() {
					public void imageLoaded(Bitmap imageBitmap) {
						cachedImages.put(imageURL, imageBitmap);
						notifyDataSetChanged();
					}
				});
			} catch (MalformedURLException e) {
//				Log.e("Yikes", "Bad remote image URL: " + imageURL, e);
			}
		}

		return cachedImage;
	}

	@Override
	public void finalize() throws Throwable {
		super.finalize();
		COMMITTE_NAME = null;

		mInflater = null;

		imageLoader = null;

		cachedImages = null;
	}
}