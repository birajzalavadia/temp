package de.bundestag.android.sections.visitors;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Gallery;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import de.bundestag.android.BaseActivity;
import de.bundestag.android.R;
import de.bundestag.android.constant.AppConstant;
import de.bundestag.android.constant.DataHolder;
import de.bundestag.android.sections.news.NewsActivity;
import de.bundestag.android.sections.news.NewsActivityTablet;
import de.bundestag.android.storage.VisitorsDatabaseAdapter;

public class VisitorsGalleryActivity extends BaseActivity {
	public static int SLIDESHOW_DELAY = 3000;

	GalleryAdapter galAdapter;
	VisitorsDatabaseAdapter visitorsDatabaseAdapter;
	private boolean isPlaying = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState, this);

		visitorsDatabaseAdapter = new VisitorsDatabaseAdapter(this);
		visitorsDatabaseAdapter.open();
		setContentView(R.layout.gallery);

		Intent launchingIntent = getIntent();
		Bundle extras = launchingIntent.getExtras();
		String galleryId = (String) extras.get("galleryId");

		Cursor galleryInfo = visitorsDatabaseAdapter.fetchGallery(galleryId);
		String galTitle = galleryInfo.getString(galleryInfo.getColumnIndex(VisitorsDatabaseAdapter.KEY_GALLERY_TITLE));
		String galleryRowId = galleryInfo.getString(galleryInfo.getColumnIndex(VisitorsDatabaseAdapter.KEY_GALLERY_ROWID));

		Cursor imgCursor = visitorsDatabaseAdapter.fetchGalleryImages(galleryRowId);

		Gallery gal = (Gallery) this.findViewById(R.id.gallery);
		galAdapter = new GalleryAdapter(this, imgCursor);
		gal.setAdapter(galAdapter);

		TextView galleryTitle = (TextView) this.findViewById(R.id.galleryTitle);
		// Typeface faceGeorgia = Typeface.createFromAsset(getAssets(),
		// "fonts/Georgia.ttf");
		// galleryTitle.setTypeface(faceGeorgia);

		TextView desc = (TextView) this.findViewById(R.id.galleryImageDesc);
		// Typeface faceArial = Typeface.createFromAsset(this.getAssets(),
		// "fonts/Arial.ttf");
		// desc.setTypeface(faceArial);

		LinearLayout dots = (LinearLayout) this.findViewById(R.id.galleryDots);
		for (int i = 0; i < imgCursor.getCount(); i++) {
			ImageView dot = new ImageView(this);
			dot.setImageResource(R.drawable.gallery_dot_state);
			if (i == 0)
				dot.setSelected(true);
			dot.setPadding(6, 0, 6, 0);
			dots.addView(dot);
		}

		gal.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				View overlay = VisitorsGalleryActivity.this.findViewById(R.id.gallery_overlay);
				if (overlay.getVisibility() == View.VISIBLE)
					overlay.setVisibility(View.INVISIBLE);
				else
					overlay.setVisibility(View.VISIBLE);
			}
		});

		gal.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				Cursor gc = (Cursor) galAdapter.getItem(arg2);
				String imgText = gc.getString(gc.getColumnIndex(VisitorsDatabaseAdapter.KEY_IMAGE_TEXT));
				String imgCopy = gc.getString(gc.getColumnIndex(VisitorsDatabaseAdapter.KEY_IMAGE_COPYRIGHT));
				TextView copy = (TextView) VisitorsGalleryActivity.this.findViewById(R.id.galleryImageCopy);
				TextView text = (TextView) VisitorsGalleryActivity.this.findViewById(R.id.galleryImageDesc);
				copy.setText("Bild: \u00A9 " + imgCopy);
				text.setText(imgText);

				LinearLayout dots = (LinearLayout) VisitorsGalleryActivity.this.findViewById(R.id.galleryDots);
				for (int i = 0; i < dots.getChildCount(); i++) {
					((ImageView) dots.getChildAt(i)).setSelected(i == arg2);
				}

				ImageButton forwardBtn = ((ImageButton) VisitorsGalleryActivity.this.findViewById(R.id.galleryForward));
				ImageButton playBtn = ((ImageButton) VisitorsGalleryActivity.this.findViewById(R.id.galleryPlay));
				ImageButton backBtn = ((ImageButton) VisitorsGalleryActivity.this.findViewById(R.id.galleryBack));

				if ((arg2 + 1) == gc.getCount()) {
					// Last item
					if (isPlaying) {
						stopSlideshow();
					}

					playBtn.setEnabled(false);
					playBtn.setAlpha(50);

					forwardBtn.setEnabled(false);

					forwardBtn.setAlpha(50);
				} else {

					forwardBtn.setEnabled(true);
					forwardBtn.setAlpha(255);

					playBtn.setEnabled(true);
					playBtn.setAlpha(255);
				}

				if (arg2 == 0) {
					// first item

					backBtn.setEnabled(false);
					backBtn.setAlpha(50);
				} else {
					backBtn.setEnabled(true);
					backBtn.setAlpha(255);
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
			}
		});
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		// DataHolder.dismissProgress();
	}

	public void dummyHandler(View v) {
	}

	public void playSlideshow() {
		Gallery gal = (Gallery) this.findViewById(R.id.gallery);

		if (!isPlaying) {
			ImageButton playBtn = ((ImageButton) this.findViewById(R.id.galleryPlay));
			playBtn.setImageResource(R.drawable.pause);
			playBtn.setEnabled(true);
			isPlaying = true;
		}

		gal.postDelayed(new Runnable() {

			@Override
			public void run() {
				if (isPlaying) {
					galleryForward(null);
					playSlideshow();
				}
			}
		}, SLIDESHOW_DELAY);
	}

	public void stopSlideshow() {
		Gallery gal = (Gallery) this.findViewById(R.id.gallery);
		if (isPlaying) {
			ImageButton playBtn = ((ImageButton) this.findViewById(R.id.galleryPlay));
			playBtn.setImageResource(R.drawable.gallery_play);
		}
		isPlaying = false;
	}

	/**
	 * Handler for close gallery top bar
	 */
	public void closeGallery(View v) {
		this.finish();
	}

	/**
	 * Handler for forward gallery button
	 * 
	 */
	public void galleryForward(View v) {
		Gallery gal = (Gallery) this.findViewById(R.id.gallery);
		int nextPos = gal.getSelectedItemPosition() + 1;
		if (nextPos < gal.getCount()) {
			gal.setSelection(nextPos);
		}
	}

	/**
	 * Handler for back gallery button
	 * 
	 */
	public void galleryBack(View v) {
		Gallery gal = (Gallery) this.findViewById(R.id.gallery);
		int nextPos = gal.getSelectedItemPosition() - 1;
		if (nextPos >= 0) {
			gal.setSelection(nextPos);
		}
	}

	/**
	 * Handler for play button
	 * 
	 */
	public void galleryPlay(View v) {
		if (isPlaying)
			stopSlideshow();
		else
			playSlideshow();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (visitorsDatabaseAdapter != null)
			visitorsDatabaseAdapter.close();
	}

	@Override
	protected void finalize() throws Throwable {
		super.finalize();
		galAdapter = null;
		visitorsDatabaseAdapter = null;
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (AppConstant.isFragmentSupported) {

				DataHolder.rowDBSelectedIndex = 0;
				Intent intent = new Intent();
				if (!AppConstant.isFragmentSupported)
					intent.setClass(this, VisitorsOffersActivity.class);
				else
					intent.setClass(this, VisitorsOffersActivityTablet.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent);
				overridePendingTransition(0, 0);
			}
		}

		return super.onKeyDown(keyCode, event);
	}
}