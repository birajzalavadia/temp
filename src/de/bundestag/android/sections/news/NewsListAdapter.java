package de.bundestag.android.sections.news;

import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;
import de.bundestag.android.R;
import de.bundestag.android.VideoPlayer;
import de.bundestag.android.constant.AppConstant;
import de.bundestag.android.constant.DataHolder;
import de.bundestag.android.helpers.EllipsizingTextView;
import de.bundestag.android.helpers.ImageHelper;
import de.bundestag.android.helpers.ImageThreadLoader;
import de.bundestag.android.helpers.ImageThreadLoader.ImageLoadedListener;
import de.bundestag.android.helpers.TextHelper;
import de.bundestag.android.parser.PlenumXMLParser;
import de.bundestag.android.parser.objects.PlenumObject;
import de.bundestag.android.sections.plenum.PlenumSpeakersActivity;
import de.bundestag.android.sections.plenum.PlenumTvActivity;
import de.bundestag.android.sections.plenum.PlenumVideoActivity;
import de.bundestag.android.storage.NewsDatabaseAdapter;
import de.bundestag.android.synchronization.NewsSynchronization;

/**
 * News list adapter (list item renderer).
 * 
 * This class knows how to show news elements for a list.
 * 
 * Uses the database cursor to extract the data for the list.
 */
public class NewsListAdapter extends CursorAdapter {
	private static final int TOP_LAYOUT = 1;

	private static final int GENERAL_LAYOUT = 0;

	private LayoutInflater mInflater;

	private Activity activity;

	private ImageThreadLoader imageLoader;

	private Map<String, Bitmap> cachedImages;

	public NewsListAdapter(Activity activity, Cursor cursor) {
		super(activity, cursor, true);

		mInflater = LayoutInflater.from(activity);
		this.activity = activity;

		imageLoader = new ImageThreadLoader();

		cachedImages = new HashMap<String, Bitmap>();
	}

	@Override
	public int getViewTypeCount() {
		return 2;
	}

	@Override
	public int getItemViewType(int position) {
		return (position == 0) ? TOP_LAYOUT : GENERAL_LAYOUT;
	}

	@Override
	public View newView(Context context, Cursor cursor, ViewGroup parent) {
		final View view;

		int type = getItemViewType(cursor.getPosition());

		if (type == TOP_LAYOUT && !AppConstant.isFragmentSupported) {
			view = mInflater.inflate(R.layout.news_list_firstitem, parent, false);
		} else {

			view = mInflater.inflate(R.layout.news_list_item, parent, false);
			if (AppConstant.isFragmentSupported) {

				view.getLayoutParams().width = DataHolder.calculatedScreenResolution;
			}
		}
		return view;
	}

	@Override
	public void bindView(View view, Context context, Cursor cursor) {
		TextView title = (TextView) view.findViewById(R.id.title);
		title.setText(TextHelper.customizedFromHtml(cursor.getString(cursor.getColumnIndex(NewsDatabaseAdapter.KEY_TITLE))));
		int newsType = cursor.getInt(cursor.getColumnIndex(NewsDatabaseAdapter.KEY_TYPE));
		int startTeaserInt = 0;
		int teaserStatusInt = 0;
		if (newsType == NewsSynchronization.NEWS_TYPE_NORMAL) {
			String startTeaser = cursor.getString(cursor.getColumnIndex(NewsDatabaseAdapter.KEY_STARTTEASER));
			startTeaserInt = Integer.parseInt(startTeaser.toString());
			String teaserStatus = cursor.getString(cursor.getColumnIndex(NewsDatabaseAdapter.KEY_STATUS));
			if (teaserStatus != null) {
				if (!teaserStatus.equals("")) {
					teaserStatusInt = Integer.parseInt(teaserStatus.toString());
				}
			}
		}

		// Watermark background height adaptation
		if (cursor.getPosition() == 0) {
			View content = view.findViewById(R.id.image_container);
			View img = view.findViewById(R.id.back_img);
			if ((content != null) && (img != null)) {
				img.getLayoutParams().height = content.getMeasuredHeight();
			}

		}

		final ImageView image = (ImageView) view.findViewById(R.id.image);
		TextView imagecopy = (TextView) view.findViewById(R.id.image_copy);
		LinearLayout imageHolder = (LinearLayout) view.findViewById(R.id.imageHolder);

		final String imageURL = cursor.getString(cursor.getColumnIndex(NewsDatabaseAdapter.KEY_IMAGEURL));
		if ((imageURL != null) && (!imageURL.equals(""))) {
			if (imageHolder != null) {
				imageHolder.setVisibility(View.VISIBLE);
			}

			int type = getItemViewType(cursor.getPosition());

			// Video
			boolean isPlayerEnable = false;
			String videoUrl = cursor.getString(cursor.getColumnIndex(NewsDatabaseAdapter.KEY_VIDEO_STREAMURL));
			View play = view.findViewById(R.id.play_video);
			View play_stream = view.findViewById(R.id.play_stream);

			if (teaserStatusInt == 1 && startTeaserInt == 1 && !AppConstant.isFragmentSupported) { //
				play_stream.setVisibility(View.VISIBLE);

				if (image != null && image.getWidth() > 0) {

					RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(image.getWidth(), LayoutParams.WRAP_CONTENT);
					lp.addRule(RelativeLayout.ALIGN_BOTTOM, image.getId());
					lp.addRule(RelativeLayout.CENTER_HORIZONTAL);
					play_stream.setLayoutParams(lp);
					isPlayerEnable = true;

				}
				play_stream.setOnClickListener(new OnPlenumVideoClick());
				image.setOnClickListener(new OnPlenumVideoClick());
				play.setVisibility(View.GONE);
			} else if (teaserStatusInt == 1 && startTeaserInt == 1 && AppConstant.isFragmentSupported) { //
				play.setVisibility(View.VISIBLE);
				play.setOnClickListener(new OnPlenumVideoClick());
				image.setOnClickListener(new OnPlenumVideoClick());
				image.setTag("" + cursor.getPosition());
				play.setTag("" + cursor.getPosition());
				isPlayerEnable = true;
			} else if ((videoUrl != null) && !videoUrl.equals("")) { //
				play.setVisibility(View.VISIBLE);
				play.setOnClickListener(new OnVideoClick(videoUrl));
				image.setOnClickListener(new OnVideoClick(videoUrl));
				image.setTag("" + cursor.getPosition());
				play.setTag("" + cursor.getPosition());
				isPlayerEnable = true;
			} else {
				// if (!AppConstant.isFragmentSupported)
				play.setVisibility(View.GONE);
				isPlayerEnable = false;
			}

			if (type == TOP_LAYOUT && !AppConstant.isFragmentSupported) {
				Bitmap imageToScale = getImage(imageURL, image, type);
				if (imageToScale != null) {
					image.setImageBitmap(ImageHelper.getScaleImageURL((Activity) context, imageToScale));
				} else {
					image.setImageBitmap(imageToScale);
				}
			} else {
				Bitmap tmp = getImage(imageURL, image, type);
				if (tmp != null) {
					image.setImageBitmap(tmp);
				} else if (isPlayerEnable) {
					image.setImageResource(R.drawable.startbild_320);
				} else {
					image.setImageBitmap(null);
				}
			}

			image.setVisibility(View.VISIBLE);
			imagecopy.setVisibility(View.VISIBLE);

			try {
				ImageView img = (ImageView) view.findViewById(R.id.arrow);
				if (newsType == NewsSynchronization.NEWS_TYPE_LIST) {
					img.setVisibility(View.VISIBLE);
				} else {
					img.setVisibility(View.INVISIBLE);
				}
			} catch (Exception e) {

			}

			TextView image_copy = (TextView) view.findViewById(R.id.image_copy);
			image_copy.setText("\u00A9 " + cursor.getString(cursor.getColumnIndex(NewsDatabaseAdapter.KEY_IMAGECOPYRIGHT)));
		} else {
			if (imageHolder != null) {
				imageHolder.setVisibility(View.GONE);
			}
		}
		if (AppConstant.isFragmentSupported) {
			EllipsizingTextView description = (EllipsizingTextView) view.findViewById(R.id.description);
			description.setMaxLines(4);
			description.setText(TextHelper.customizedFromHtml(cursor.getString(cursor.getColumnIndex(NewsDatabaseAdapter.KEY_TEASER))));
		} else {
			TextView description = (TextView) view.findViewById(R.id.description);

			description.setText(TextHelper.customizedFromHtml(cursor.getString(cursor.getColumnIndex(NewsDatabaseAdapter.KEY_TEASER))));
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
				// Log.e("Yikes", "Bad remote image URL: " + imageURL, e);
			}
		}

		return cachedImage;
	}

	class OnVideoClick implements OnClickListener {
		private String videoUrl;

		public OnVideoClick(String videoUrl) {
			this.videoUrl = videoUrl;
		}

		@Override
		public void onClick(View v) {
			Intent i;
			if (AppConstant.isFragmentSupported) {
				if (isPLenarySitting()) {
					i = new Intent(activity, PlenumSpeakersActivity.class);
					activity.startActivity(i);
				} else {
					try {
						// NewsDetailsFragment.refNewsDetailsFragment.createVideoPlayer();
						NewsDetailsFragment.refNewsDetailsFragment.setNewsId(DataHolder.RowDBIds.get(Integer.parseInt(v.getTag().toString())));
						NewsDetailsFragment.refNewsDetailsFragment.createNewsVideo(true);
						DataHolder.rowDBSelectedIndex = Integer.parseInt(v.getTag().toString());
					} catch (Exception e) {
					}
				}
			} else {

				i = new Intent(activity, VideoPlayer.class);
				i.putExtra("streamURL", videoUrl);
				activity.startActivity(i);
			}
		}
	}

	class OnPlenumVideoClick implements OnClickListener {
		private String videoUrl;

		public OnPlenumVideoClick() {
			this.videoUrl = videoUrl;
		}

		@Override
		public void onClick(View v) {

			Intent i;
			if (AppConstant.isFragmentSupported) {
				if (isPLenarySitting()) {
					i = new Intent(activity, PlenumSpeakersActivity.class);
					activity.startActivity(i);
				} else {
					try {
						// NewsDetailsFragment.refNewsDetailsFragment.createVideoPlayer();
						NewsDetailsFragment.refNewsDetailsFragment.setNewsId(DataHolder.RowDBIds.get(Integer.parseInt(v.getTag().toString())));
						NewsDetailsFragment.refNewsDetailsFragment.createNewsVideo(true);
						DataHolder.rowDBSelectedIndex = Integer.parseInt(v.getTag().toString());
					} catch (Exception e) {
					}
				}
			} else {

				i = new Intent(activity, PlenumVideoActivity.class);
				activity.startActivity(i);
			}
		}
	}

	// @Override
	// public void finalize() throws Throwable {
	// super.finalize();
	// mInflater = null;
	//
	// activity = null;
	//
	// imageLoader = null;
	//
	// cachedImages = null;
	// }

	public boolean isPLenarySitting() {
		PlenumXMLParser pp = new PlenumXMLParser();
		PlenumObject plenum = null;
		try {
			plenum = pp.parseMain(PlenumXMLParser.MAIN_PLENUM_URL);
			if ((plenum != null) && (plenum.getStatus().intValue() == 1)) {
				return true;
			}
		} catch (Exception e) {
			// e.printStackTrace();
		}

		return false;
	}

}