package de.bundestag.android.sections.visitors;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import de.bundestag.android.R;
import de.bundestag.android.constant.AppConstant;
import de.bundestag.android.constant.DataHolder;
import de.bundestag.android.helpers.EllipsizingTextView;
import de.bundestag.android.helpers.ImageHelper;
import de.bundestag.android.helpers.TextHelper;
import de.bundestag.android.storage.VisitorsDatabaseAdapter;

/**
 * News list adapter (list item renderer).
 * 
 * This class knows how to show news elements for a list.
 * 
 * Uses the database cursor to extract the data for the list.
 */
public class VisitorsListAdapter extends CursorAdapter {
	private static final int TOP_LAYOUT = 0;

	private static final int GENERAL_LAYOUT = 1;

	public static final int GALLERY_LAYOUT = 2;

	private static final String LIST_ITEM_TYPE_ARTICLE = "article";
	private static final String LIST_ITEM_TYPE_GALLERY = "gallery";

	private LayoutInflater mInflater;
	private HorizontalListView galleryList;
	private VisitorsDatabaseAdapter visitorsDatabaseAdapter;
	private GalleryListAdapter galListAdapter;
	private Context context;

	public VisitorsListAdapter(Context context, Cursor cursor, VisitorsDatabaseAdapter visitorsDatabaseAdapter) {
		super(context, cursor, true);
		mInflater = LayoutInflater.from(context);
		this.visitorsDatabaseAdapter = visitorsDatabaseAdapter;
		this.context = context;
	}

	@Override
	public int getViewTypeCount() {
		return 3;
	}

	@Override
	public int getItemViewType(int position) {
		// return (position == 0) ? TOP_LAYOUT : GENERAL_LAYOUT;
		if (!AppConstant.isFragmentSupported) {
			if (position == 0) {
				return TOP_LAYOUT;
			} else if (position == 1) {
				return GALLERY_LAYOUT;
			} else {
				return GENERAL_LAYOUT;
			}
		} else {
			if (position == 0) {
				return GALLERY_LAYOUT;
			} else {
				return GENERAL_LAYOUT;
			}

		}

	}

	public int getItemViewType(Cursor cursor) {
		int position = cursor.getPosition();
		String itemType = cursor.getString(cursor.getColumnIndex(VisitorsDatabaseAdapter.KEY_ARTICLE_TYPE));

		if (itemType.equals(LIST_ITEM_TYPE_GALLERY)) {
			return GALLERY_LAYOUT;
		} else {
			if (!AppConstant.isFragmentSupported) {
				if (position == 0) {
					return TOP_LAYOUT;
				} else {
					return GENERAL_LAYOUT;
				}
			} else {
				return GENERAL_LAYOUT;
			}
		}
	}

	@Override
	public View newView(Context context, Cursor cursor, ViewGroup parent) {
		final View view;

		int type = getItemViewType(cursor);

		if (AppConstant.isFragmentSupported) {
			if (type == GALLERY_LAYOUT) {
				view = mInflater.inflate(R.layout.visitors_gallery_item, parent, false);
			} else {
				view = mInflater.inflate(R.layout.news_list_item, parent, false);
			}
		} else {
			if (type == TOP_LAYOUT) {
				view = mInflater.inflate(R.layout.visitors_list_first_item, parent, false);
			} else if (type == GALLERY_LAYOUT) {
				view = mInflater.inflate(R.layout.visitors_gallery_item, parent, false);
			} else {
				view = mInflater.inflate(R.layout.news_list_item, parent, false);
			}
		}
		// if (DataHolder.gallaryFragmentWidth != -1
		// && AppConstant.isFragmentSupported && type == GALLERY_LAYOUT) {
		// view.getLayoutParams().width = DataHolder.gallaryFragmentWidth;
		// } else if (DataHolder.listFragmentWidth != -1
		// && AppConstant.isFragmentSupported) {
		// view.getLayoutParams().width = DataHolder.listFragmentWidth;
		// }
		if (AppConstant.isFragmentSupported) {
			view.getLayoutParams().width = DataHolder.calculatedScreenResolution;
		}

		return view;
	}

	@Override
	public void bindView(View view, Context cont, Cursor cursor) {
		int type = getItemViewType(cursor);
		if (type == GALLERY_LAYOUT) {
			galleryList = (HorizontalListView) view.findViewById(R.id.gallery_list);

			// VisitorsDatabaseAdapter visitorsDatabaseAdapter = new
			// VisitorsDatabaseAdapter(context);
			visitorsDatabaseAdapter.open();
			String listId = cursor.getString(cursor.getColumnIndex(VisitorsDatabaseAdapter.KEY_ARTICLE_ARTICLELISTID));

			Cursor listGalleriesCursor = visitorsDatabaseAdapter.fetchListGalleries(listId);
			galListAdapter = new GalleryListAdapter(context, listGalleriesCursor);
			galleryList.setAdapter(galListAdapter);

			galleryList.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> arg0, View galleryImage, int galleryIndex, long arg3) {
					Cursor gc = (Cursor) galListAdapter.getItem(galleryIndex);
					String galId = gc.getString(gc.getColumnIndex(VisitorsDatabaseAdapter.KEY_IMAGE_GALLERYID));
					gc.close();

					Intent i = new Intent(context, VisitorsGalleryActivity.class);
					i.putExtra("galleryId", galId);
					context.startActivity(i);
				}
			});

		} else {
			String articleType = cursor.getString(cursor.getColumnIndex(VisitorsDatabaseAdapter.KEY_ARTICLE_TYPE));
			try {
				ImageView imgArrow = (ImageView) view.findViewById(R.id.arrow);
				if (articleType.equals("article-list")) {
					imgArrow.setVisibility(View.VISIBLE);
				} else {
					imgArrow.setVisibility(View.INVISIBLE);
				}
			} catch (Exception e) {
			}
			TextView title = (TextView) view.findViewById(R.id.title);
			// title.setVisibility(View.VISIBLE);
			title.setText(TextHelper.customizedFromHtml(cursor.getString(cursor.getColumnIndex(VisitorsDatabaseAdapter.KEY_ARTICLE_TITLE))));
			// title.setTypeface(faceGeorgia);

			// Watermark background height adaptation
			if (cursor.getPosition() == 0 && !AppConstant.isFragmentSupported) {
				View content = view.findViewById(R.id.image_container);
				View img = view.findViewById(R.id.back_img);
				if (content != null && img != null)
					img.getLayoutParams().height = content.getMeasuredHeight();
			}

			// TODO - fix list adapter scrolling
			ImageView image = (ImageView) view.findViewById(R.id.image);
			TextView imagecopy = (TextView) view.findViewById(R.id.image_copy);
			String imageString = cursor.getString(cursor.getColumnIndex(VisitorsDatabaseAdapter.KEY_ARTICLE_IMAGESTRING));
			LinearLayout imageHolder = (LinearLayout) view.findViewById(R.id.imageHolder);
			if (imageString != null) {
				if (imageHolder != null) {
					imageHolder.setVisibility(View.VISIBLE);
				}

				if (type == TOP_LAYOUT) {
					image.setImageBitmap(ImageHelper.getScaleImage((Activity) cont, imageString));
					TextView mainTitle = (TextView) view.findViewById(R.id.mainTitle);
					mainTitle.setVisibility(View.VISIBLE);
					// mainTitle.setTypeface(faceGeorgia);
					if ((Activity) cont instanceof VisitorsOffersActivity || (Activity) cont instanceof VisitorsOffersActivityTablet) {
						mainTitle.setText("Besuchen Sie den Deutschen Bundestag");
					} else if ((Activity) cont instanceof VisitorsLocationsActivity || (Activity) cont instanceof VisitorsLocationsActivityTablet) {
						mainTitle.setText("Parlamentsgebäude");
					} else {
						mainTitle.setVisibility(View.GONE);
						// mainTitle.setText(TextHelper.customizedFromHtml(cursor.getString(cursor.getColumnIndex(VisitorsDatabaseAdapter.KEY_ARTICLE_TITLE))));
					}
				} else {
					image.setImageBitmap(ImageHelper.convertStringToBitmap(imageString));
				}

				image.setVisibility(View.VISIBLE);
				imagecopy.setVisibility(View.VISIBLE);

				TextView image_copy = (TextView) view.findViewById(R.id.image_copy);
				image_copy.setText("\u00A9 " + cursor.getString(cursor.getColumnIndex(VisitorsDatabaseAdapter.KEY_ARTICLE_IMAGECOPYRIGHT)));
				// image_copy.setTypeface(faceArial);
			} else {
				if (imageHolder != null) {
					imageHolder.setVisibility(View.GONE);
				}
				image.setVisibility(View.GONE);
				imagecopy.setVisibility(View.GONE);
				image.setImageBitmap(null);
			}
			if (AppConstant.isFragmentSupported) {
				EllipsizingTextView description = (EllipsizingTextView) view.findViewById(R.id.description);
				description.setMaxLines(4);
				description.setText(TextHelper.customizedFromHtml(cursor.getString(cursor.getColumnIndex(VisitorsDatabaseAdapter.KEY_ARTICLE_TEASER))));
			} else {
				TextView description = (TextView) view.findViewById(R.id.description);

				description.setText(TextHelper.customizedFromHtml(cursor.getString(cursor.getColumnIndex(VisitorsDatabaseAdapter.KEY_ARTICLE_TEASER))));

			}
		}

	}

	// @Override
	// public void finalize() throws Throwable {
	// super.finalize();
	// // mInflater=null;
	// // visitorsDatabaseAdapter=null;
	// // for (int i = 0; i < galleryList.getChildCount(); i++) {
	// // galleryList.removeViewAt(i);
	// // }
	// // galleryList=null;
	// // galListAdapter=null;
	// // context=null;
	// }
}