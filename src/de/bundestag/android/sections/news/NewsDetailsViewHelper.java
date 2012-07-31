package de.bundestag.android.sections.news;

import java.net.MalformedURLException;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import de.bundestag.android.R;
import de.bundestag.android.constant.DataHolder;
import de.bundestag.android.customviews.CustomScrollView;
import de.bundestag.android.helpers.ImageHelper;
import de.bundestag.android.helpers.ImageThreadLoader;
import de.bundestag.android.helpers.ImageThreadLoader.ImageLoadedListener;
import de.bundestag.android.helpers.TextHelper;
import de.bundestag.android.parser.objects.NewsDetailsObject;
import de.bundestag.android.sections.plenum.DebateNewsActivity;

public class NewsDetailsViewHelper {
	public static void createDetailsView(final NewsDetailsObject newsDetailsObject, final FragmentActivity activity, boolean linksOn, boolean isVisitor) {
		TextView title = (TextView) activity.findViewById(R.id.title);
		title.setText(TextHelper.customizedFromHtml(newsDetailsObject.getTitle()));

		String imageURL = newsDetailsObject.getImageURL();
		if (imageURL != null) {
			final ImageView image = (ImageView) activity.findViewById(R.id.image);

			ImageThreadLoader imageLoader = new ImageThreadLoader();
			try {
				imageLoader.loadImage(imageURL, new ImageLoadedListener() {
					public void imageLoaded(Bitmap imageBitmap) {
						image.setImageBitmap(ImageHelper.getScaleImageURL(activity, imageBitmap));

						TextView copyright = (TextView) activity.findViewById(R.id.copyright);
						copyright.setText(TextHelper.customizedFromHtml("\u00A9 " + newsDetailsObject.getImageCopyright()));
					}
				});
			} catch (MalformedURLException e) {
				// Log.e("Yikes", "Bad remote image URL: " + imageURL, e);
			}
		}

		WebView text = (WebView) activity.findViewById(R.id.text);
		String reformatedText = TextHelper.customizedFromHtmlforWebView(newsDetailsObject.getText());
		text.loadDataWithBaseURL("file:///android_asset/", reformatedText, "text/html", "UTF-8", null);

		WebSettings webSettings = text.getSettings();
		webSettings.setDefaultFontSize(12);

	}

	/*
	 * For tablet version using activity
	 */
	public static void createDetailsView(final NewsDetailsObject newsDetailsObject, boolean linksOn, boolean isVisitor, final Activity activity) {
		DataHolder.createProgressDialog(activity);
		TextView title = (TextView) activity.findViewById(R.id.title_tab);
		title.setTypeface(Typeface.createFromAsset(activity.getAssets(), "fonts/DroidSerif-Regular.ttf"));
		title.setText(TextHelper.customizedFromHtmlTab(newsDetailsObject.getTitle()));
		final LinearLayout playerControl = ((LinearLayout) activity.findViewById(R.id.playerControl));
		try {
			if (newsDetailsObject.getStartTeaser() != null && newsDetailsObject.getStartTeaser().equals("1")
					&& (newsDetailsObject.getStatusTxt() != null && newsDetailsObject.getStatusTxt().length() == 0 || newsDetailsObject.getStatusTxt().equals("1"))
					&& newsDetailsObject.getVideoURL() != null && newsDetailsObject.getVideoURL().trim().length() > 0) {
				playerControl.setVisibility(View.VISIBLE);
			} else {
				playerControl.setVisibility(View.GONE);
			}
		} catch (Exception e) {

		}
		String imageURL = newsDetailsObject.getImageURL();
		final ImageView image = (ImageView) activity.findViewById(R.id.image_tab);
		final TextView copyright = (TextView) activity.findViewById(R.id.copyright_tab);
		if (imageURL != null && imageURL.trim().length() != 0) {
			image.setVisibility(View.VISIBLE);
			image.setLayoutParams(new FrameLayout.LayoutParams(436, 289));
			copyright.setVisibility(View.VISIBLE);
			ImageThreadLoader imageLoader = new ImageThreadLoader();
			try {
				imageLoader.loadImage(imageURL, new ImageLoadedListener() {
					public void imageLoaded(Bitmap imageBitmap) {

						copyright.setText(TextHelper.customizedFromHtmlTab("\u00A9 " + newsDetailsObject.getImageCopyright()));
						boolean isPlayControlEnable = false;
						try {
							if (newsDetailsObject.getStartTeaser() != null && newsDetailsObject.getStartTeaser().equals("1")
									&& (newsDetailsObject.getStatusTxt() != null && newsDetailsObject.getStatusTxt().length() == 0 || newsDetailsObject.getStatusTxt().equals("1"))
									&& newsDetailsObject.getVideoURL() != null && newsDetailsObject.getVideoURL().trim().length() > 0) {
								playerControl.setVisibility(View.VISIBLE);
								playerControl.setTag("");
								isPlayControlEnable = true;
							} else if (newsDetailsObject.getStartTeaser() != null && newsDetailsObject.getStartTeaser().equals("0")
									&& (newsDetailsObject.getStatusTxt() != null && newsDetailsObject.getStatusTxt().length() == 0 || newsDetailsObject.getStatusTxt().equals("0"))
									&& newsDetailsObject.getVideoURL() != null && newsDetailsObject.getVideoURL().trim().length() > 0) {
								playerControl.setVisibility(View.VISIBLE);
								playerControl.setTag("");
								isPlayControlEnable = true;
							} else if (newsDetailsObject.getStartTeaser() != null && newsDetailsObject.getStartTeaser().equals("1")
									&& (newsDetailsObject.getStatusTxt() != null && newsDetailsObject.getStatusTxt().length() == 0 || newsDetailsObject.getStatusTxt().equals("1"))
									&& newsDetailsObject.getVideoURL() == null || newsDetailsObject.getVideoURL().trim().length() == 0) {
								playerControl.setVisibility(View.VISIBLE);
								playerControl.setTag("plenum");
								isPlayControlEnable = true;
							} else {
								isPlayControlEnable = false;
								playerControl.setVisibility(View.GONE);
							}
						} catch (Exception e) {

						}
						if (imageBitmap == null && isPlayControlEnable) {
							imageBitmap = ((BitmapDrawable) activity.getResources().getDrawable(R.drawable.startbild_320)).getBitmap();
						}
						image.setImageBitmap(ImageHelper.getScaleImageURL(activity, imageBitmap));
					}
				});
			} catch (MalformedURLException e) {
				// Log.e("Yikes", "Bad remote image URL: " + imageURL, e);
			}
		} else {
			image.setVisibility(View.GONE);
			copyright.setVisibility(View.GONE);
		}
		try {
			TextView mainTitle = (TextView) activity.findViewById(R.id.mainTitle);
			if (mainTitle != null) {
				if (DataHolder.currentActiviry instanceof DebateNewsActivity) {
					mainTitle.setText(TextHelper.customizedFromHtmlTab(DataHolder.newsListTitlePlenum));
				} else {
					mainTitle.setText(TextHelper.customizedFromHtmlTab(DataHolder.newsListTitle));
				}
			}
		} catch (Exception e) {

		}
		final CustomScrollView scrView = (CustomScrollView) activity.findViewById(R.id.scrView);
		scrView.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT));
		scrView.postDelayed(new Runnable() {

			@Override
			public void run() {
				scrView.fullScroll(ScrollView.FOCUS_UP);
				final WebView text = (WebView) activity.findViewById(R.id.text_tab);
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
				scrView.postDelayed(new Runnable() {

					@Override
					public void run() {
						scrView.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT));
						text.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
					}
				}, 500);
			}
		}, 1000);
		DataHolder.dismissProgress();
	}

}
