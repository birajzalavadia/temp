package de.bundestag.android.sections.committees;

import java.net.MalformedURLException;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.support.v4.app.FragmentActivity;
import android.view.View;
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
import de.bundestag.android.parser.objects.CommitteesDetailsNewsDetailsObject;

public class CommitteesDetailsNewsDetailsViewHelper {
	public static void createCommitteesDetailsNewsDetailsView(final CommitteesDetailsNewsDetailsObject committeesDetailsNewsDetailsObject, final FragmentActivity activity) {
		TextView title = (TextView) activity.findViewById(R.id.title);
		title.setText(TextHelper.customizedFromHtml(committeesDetailsNewsDetailsObject.getTitle()));

		String imageURL = committeesDetailsNewsDetailsObject.getImageURL();
		if (imageURL != null) {
			final ImageView image = (ImageView) activity.findViewById(R.id.image);
			ImageThreadLoader imageLoader = new ImageThreadLoader();
			try {
				imageLoader.loadImage(imageURL, new ImageLoadedListener() {
					public void imageLoaded(Bitmap imageBitmap) {
						image.setImageBitmap(ImageHelper.getScaleImageURL(activity, imageBitmap));

						TextView copyright = (TextView) activity.findViewById(R.id.copyright);
						copyright.setText(TextHelper.customizedFromHtml("\u00A9 " + committeesDetailsNewsDetailsObject.getImageCopyright()));
					}
				});
			} catch (MalformedURLException e) {
				// Log.e("Yikes", "Bad remote image URL: " + imageURL, e);
			}
		}

		// String imageString =
		// committeesDetailsNewsDetailsObject.getImageString();
		// if (imageString != null)
		// {
		// ImageView image = (ImageView) activity.findViewById(R.id.image);
		// image.setImageBitmap(ImageHelper.getScaleImage(activity,
		// imageString));
		//
		// TextView copyright = (TextView)
		// activity.findViewById(R.id.copyright);
		// copyright.setText(TextHelper.customizedFromHtml("\u00A9 " +
		// committeesDetailsNewsDetailsObject.getImageCopyright()));
		// }

		// TextView text = (TextView) activity.findViewById(R.id.text);
		// text.setText(TextHelper.customizedFromHtml(committeesDetailsNewsDetailsObject.getText()));
		// text.setMovementMethod(LinkMovementMethod.getInstance());

		WebView text = (WebView) activity.findViewById(R.id.text);
		String reformatedText = TextHelper.customizedFromHtmlforWebView(committeesDetailsNewsDetailsObject.getText());
		text.loadDataWithBaseURL("file:///android_asset/", "", "text/html", "UTF-8", null);
		text.loadDataWithBaseURL("file:///android_asset/", reformatedText, "text/html", "UTF-8", null);

		// TextHelper.textViewHTMLTest(committeesDetailsNewsDetailsObject,
		// activity);
	}

	public static void createCommitteesDetailsNewsDetailsViewTab(final CommitteesDetailsNewsDetailsObject committeesDetailsNewsDetailsObject, final FragmentActivity activity) {
		DataHolder.createProgressDialog(activity);
		TextView title = (TextView) activity.findViewById(R.id.title_tab);
		title.setText(TextHelper.customizedFromHtml(committeesDetailsNewsDetailsObject.getTitle()));

		String imageURL = committeesDetailsNewsDetailsObject.getImageURL();
		final ImageView image = (ImageView) activity.findViewById(R.id.image_tab);
		final TextView copyright = (TextView) activity.findViewById(R.id.copyright_tab);
		if (imageURL != null) {
			ImageThreadLoader imageLoader = new ImageThreadLoader();
			try {
				imageLoader.loadImage(imageURL, new ImageLoadedListener() {

					public void imageLoaded(Bitmap imageBitmap) {
						if (imageBitmap != null) {
							image.setVisibility(View.VISIBLE);
							copyright.setVisibility(View.VISIBLE);
							image.setImageBitmap(ImageHelper.getScaleImageURL(activity, imageBitmap));
							copyright.setText(TextHelper.customizedFromHtml("\u00A9 " + committeesDetailsNewsDetailsObject.getImageCopyright()));
						} else {
							image.setVisibility(View.GONE);
							copyright.setVisibility(View.GONE);
						}

					}
				});

			} catch (MalformedURLException e) {
				// Log.e("Yikes", "Bad remote image URL: " + imageURL, e);
			}
		} else {
			image.setVisibility(View.GONE);
			copyright.setVisibility(View.GONE);
		}

		final CustomScrollView scrView = (CustomScrollView) activity.findViewById(R.id.scrView);
		scrView.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT));
		scrView.postDelayed(new Runnable() {

			@Override
			public void run() {
				scrView.fullScroll(ScrollView.FOCUS_UP);
				WebView text = (WebView) activity.findViewById(R.id.text_tab);
				
				scrView.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT));
				text.setBackgroundColor(0x00000000);
				String reformatedText = TextHelper.customizedFromHtmlforWebViewTab(committeesDetailsNewsDetailsObject.getText());
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
