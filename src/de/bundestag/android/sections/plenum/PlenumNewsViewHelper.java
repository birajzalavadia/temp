package de.bundestag.android.sections.plenum;

import android.graphics.Color;
import android.support.v4.app.FragmentActivity;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import de.bundestag.android.R;
import de.bundestag.android.helpers.ImageHelper;
import de.bundestag.android.helpers.TextHelper;
import de.bundestag.android.parser.objects.PlenumObject;
import de.bundestag.android.parser.objects.PlenumTeaserObject;

public class PlenumNewsViewHelper {
	public static void createView(PlenumTeaserObject plenumTeaserObject, FragmentActivity activity) {
		if (plenumTeaserObject != null) {
			TextView title = (TextView) activity.findViewById(R.id.name);
			title.setText(TextHelper.customizedFromHtml(plenumTeaserObject.getTitle()));

			String imageString = plenumTeaserObject.getImage();
			if (imageString != null) {
				ImageView image = (ImageView) activity.findViewById(R.id.image);
				image.setImageBitmap(ImageHelper.loadBitmapFromUrlAndResize(activity, imageString));
			}

			TextView teaser = (TextView) activity.findViewById(R.id.teaser);
			teaser.setText(TextHelper.customizedFromHtml(plenumTeaserObject.getTeaser()));
			teaser.setMovementMethod(LinkMovementMethod.getInstance());
		}
	}

	public static void createView(PlenumObject plenumTeaserObject, FragmentActivity activity) {
		if (plenumTeaserObject != null) {
			TextView title = (TextView) activity.findViewById(R.id.name);
			title.setText(TextHelper.customizedFromHtml(plenumTeaserObject.getTitle()));

			String imageString = plenumTeaserObject.getImageURL();
			if (imageString != null) {
				ImageView image = (ImageView) activity.findViewById(R.id.image);
				image.setImageBitmap(ImageHelper.loadBitmapFromUrlAndResize(activity, imageString));
				TextView copy = (TextView) activity.findViewById(R.id.copyright);
				copy.setText(TextHelper.customizedFromHtml("\u00A9 " + plenumTeaserObject.getImageCopyright()));
			}

			TextView teaser = (TextView) activity.findViewById(R.id.teaser);
			teaser.setText(TextHelper.customizedFromHtml(plenumTeaserObject.getTeaser()));

			teaser.setMovementMethod(LinkMovementMethod.getInstance());

			if (plenumTeaserObject.getDetailsXML() != null && plenumTeaserObject.getDetailsXML().length() > 0) {
				TextView details = (TextView) activity.findViewById(R.id.details);
				details.setVisibility(View.VISIBLE);
				ImageView link = (ImageView) activity.findViewById(R.id.link);
				link.setVisibility(View.VISIBLE);
			}
		}
	}

	public static void createDetailsView(PlenumObject plenumObject, FragmentActivity activity) {
		if (plenumObject != null) {
			TextView title = (TextView) activity.findViewById(R.id.title);
			title.setText(TextHelper.customizedFromHtml(plenumObject.getTitle()));

			String imageString = plenumObject.getImageURL();
			if (imageString != null) {
				ImageView image = (ImageView) activity.findViewById(R.id.image);
				image.setImageBitmap(ImageHelper.loadBitmapFromUrlAndResize(activity, imageString));
				TextView copy = (TextView) activity.findViewById(R.id.copyright);
				copy.setText(TextHelper.customizedFromHtml("\u00A9 " + plenumObject.getImageCopyright()));
			}

			WebView text = (WebView) activity.findViewById(R.id.text);
			String reformatedText = TextHelper.customizedFromHtmlforWebView(plenumObject.getText());
			text.loadDataWithBaseURL("file:///android_asset/", "", "text/html", "UTF-8", null);
			text.loadDataWithBaseURL("file:///android_asset/", reformatedText, "text/html", "UTF-8", null);
			WebSettings webSettings = text.getSettings();
			webSettings.setDefaultFontSize(12);
		}
	}

	public static void createDetailsViewTab(PlenumObject plenumObject, FragmentActivity activity) {
		if (plenumObject != null) {
			TextView title = (TextView) activity.findViewById(R.id.title);
			title.setText(TextHelper.customizedFromHtml(plenumObject.getTitle()));

			WebView text = (WebView) activity.findViewById(R.id.text);
			text.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT));
			text.setBackgroundColor(Color.TRANSPARENT);
			String reformatedText = TextHelper.customizedFromHtmlforWebViewTab(plenumObject.getText());
			text.loadDataWithBaseURL("file:///android_asset/", "", "text/html", "UTF-8", null);
			text.loadDataWithBaseURL("file:///android_asset/", reformatedText, "text/html", "UTF-8", null);
		}
	}
}
