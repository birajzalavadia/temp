package de.bundestag.android.sections.visitors;

//import android.graphics.Typeface;
import android.support.v4.app.FragmentActivity;
import android.text.method.LinkMovementMethod;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;
import de.bundestag.android.R;
import de.bundestag.android.helpers.ImageHelper;
import de.bundestag.android.helpers.TextHelper;
import de.bundestag.android.parser.objects.VisitorsArticleObject;

public class VisitorsDetailsViewHelper {
	public static void createDetailsView(VisitorsArticleObject visitorsDetailsObject, FragmentActivity activity) {
		TextView title = (TextView) activity.findViewById(R.id.title);
		title.setText(TextHelper.customizedFromHtml(visitorsDetailsObject.getTitle()));
		// title.setTypeface(faceGeorgia);

		String imageString = visitorsDetailsObject.getImageString();
		if (imageString != null) {
			ImageView image = (ImageView) activity.findViewById(R.id.image);
			image.setImageBitmap(ImageHelper.getScaleImage(activity, imageString));
			TextView copyright = (TextView) activity.findViewById(R.id.copyright);
			copyright.setText("\u00A9 " + TextHelper.customizedFromHtml(visitorsDetailsObject.getImageCopyright()));

		}

		WebView text = (WebView) activity.findViewById(R.id.text);		
		String reformatedText = TextHelper.customizedFromHtmlforWebView(visitorsDetailsObject.getText());
		text.loadDataWithBaseURL("file:///android_asset/", "", "text/html", "UTF-8", null);
		text.loadDataWithBaseURL("file:///android_asset/", reformatedText, "text/html", "UTF-8", null);
		WebSettings webSettings = text.getSettings();
		webSettings.setDefaultFontSize(12);

	}

	public static void createNewsDetailsView(VisitorsArticleObject visitorsDetailsObject, FragmentActivity activity) {
		TextView title = (TextView) activity.findViewById(R.id.title);
		title.setText(TextHelper.customizedFromHtml(visitorsDetailsObject.getTitle()));
		// title.setTypeface(faceGeorgia);

		String imageString = visitorsDetailsObject.getImageString();
		if (imageString != null) {
			ImageView image = (ImageView) activity.findViewById(R.id.image);
			image.setImageBitmap(ImageHelper.convertStringToBitmap(imageString));

			TextView copyright = (TextView) activity.findViewById(R.id.copyright);
			copyright.setText(TextHelper.customizedFromHtml("\u00A9 " + visitorsDetailsObject.getImageCopyright()));
		}

		WebView text = (WebView) activity.findViewById(R.id.text);
		String reformatedText = TextHelper.customizedFromHtmlforWebView(visitorsDetailsObject.getText());
		text.loadDataWithBaseURL("file:///android_asset/", "", "text/html", "UTF-8", null);
		text.loadDataWithBaseURL(null, reformatedText, "text/html", "UTF-8", null);
		WebSettings webSettings = text.getSettings();
		webSettings.setDefaultFontSize(12);
	}

	public static void createContactDetailsView(VisitorsArticleObject visitorContactDetailsObject, FragmentActivity activity) {
		TextView title = (TextView) activity.findViewById(R.id.title);
		title.setText(TextHelper.customizedFromHtml(visitorContactDetailsObject.getTitle()));
		// title.setTypeface(faceGeorgia);

		String imageString = visitorContactDetailsObject.getImageString();
		if (imageString != null) {
			ImageView image = (ImageView) activity.findViewById(R.id.image);
			image.setImageBitmap(ImageHelper.convertStringToBitmap(imageString));

			TextView copyright = (TextView) activity.findViewById(R.id.copyright);
			copyright.setText(TextHelper.customizedFromHtmlTab(visitorContactDetailsObject.getTitle()));
			// copyright.setTypeface(faceGeorgia);
		}

		TextView text = (TextView) activity.findViewById(R.id.text);
		text.setText(TextHelper.customizedFromHtmlTab(visitorContactDetailsObject.getText()));
		text.setMovementMethod(LinkMovementMethod.getInstance());

	}

	public static void createContactDetailsViewTab(VisitorsArticleObject visitorContactDetailsObject, FragmentActivity activity) {
		TextView title = (TextView) activity.findViewById(R.id.title);
		title.setText(TextHelper.customizedFromHtmlTab(visitorContactDetailsObject.getTitle()));
		// title.setTypeface(faceGeorgia);

		String imageString = visitorContactDetailsObject.getImageString();
		if (imageString != null) {
			ImageView image = (ImageView) activity.findViewById(R.id.image);
			image.setImageBitmap(ImageHelper.convertStringToBitmap(imageString));

			TextView copyright = (TextView) activity.findViewById(R.id.copyright);
			copyright.setText(TextHelper.customizedFromHtml(visitorContactDetailsObject.getTitle()));
			// copyright.setTypeface(faceGeorgia);
		}
		try {
			String temp = (visitorContactDetailsObject.getText());
			String part1 = temp.substring(temp.indexOf("<p>"), temp.indexOf("</p>") + "</p>".length());
			TextView text1 = (TextView) activity.findViewById(R.id.textPart1);
			text1.setText(TextHelper.customizedFromHtmlTab(part1));
			text1.setMovementMethod(LinkMovementMethod.getInstance());

			String part2 = temp.substring(temp.indexOf("<p s"), temp.indexOf(".</p>") + ".</p>".length());
			TextView text2 = (TextView) activity.findViewById(R.id.textPart2);
			part2 = part2.replace(".", "");
			text2.setText(TextHelper.customizedFromHtmlTab(part2));
			text2.setMovementMethod(LinkMovementMethod.getInstance());

			String part3 = TextHelper.customizedFromHtmlforWebViewTab(temp.substring(temp.indexOf("<p>D"), temp.lastIndexOf(".</p>") + ".</p>".length()));
			WebView text3 = (WebView) activity.findViewById(R.id.text);
			text3.setBackgroundColor(0x00000000);
			text3.loadDataWithBaseURL("file:///android_asset/", part3, "text/html", "UTF-8", null);
			// text3.setMovementMethod(LinkMovementMethod.getInstance());
		} catch (Exception e) {
			// TODO: handle exception
			TextView text = (TextView) activity.findViewById(R.id.text);
			text.setText(TextHelper.customizedFromHtmlTab(visitorContactDetailsObject.getText()));
			text.setMovementMethod(LinkMovementMethod.getInstance());
		}

	}
}
