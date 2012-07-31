package de.bundestag.android.sections.visitors;

//import android.graphics.Typeface;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.text.method.LinkMovementMethod;
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
import de.bundestag.android.helpers.TextHelper;
import de.bundestag.android.parser.objects.VisitorsArticleObject;

public class VisitorsDetailsFragmentViewHelper {
	public static void createDetailsView(final VisitorsArticleObject visitorsDetailsObject, final Activity activity) {
		DataHolder.createProgressDialog(activity);
		TextView title = (TextView) activity.findViewById(R.id.title_tab);
		title.setText(TextHelper.customizedFromHtmlTab(visitorsDetailsObject.getTitle()));
		// title.setTypeface(faceGeorgia);

		String imageString = visitorsDetailsObject.getImageString();
		TextView copyright = (TextView) activity.findViewById(R.id.copyright_tab);
		ImageView image = (ImageView) activity.findViewById(R.id.image_tab);
		if (imageString != null) {
			image.setVisibility(View.VISIBLE);
			copyright.setVisibility(View.VISIBLE);
			Bitmap img = ImageHelper.getScaleImage(activity, imageString);

			if (img != null) {
				image.setImageBitmap(img);
				copyright.setText("\u00A9 " + TextHelper.customizedFromHtmlTab(visitorsDetailsObject.getImageCopyright()));
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
				text.clearView();
				text.measure(10,10);
				scrView.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT));
				String reformatedText = TextHelper.customizedFromHtmlforWebViewTab(visitorsDetailsObject.getText());
				text.loadDataWithBaseURL("file:///android_asset/", "", "text/html", "UTF-8", null);
				text.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
				text.loadDataWithBaseURL("file:///android_asset/", reformatedText, "text/html", "UTF-8", null);
				text.setBackgroundColor(Color.TRANSPARENT);
				DataHolder.finishDialog(text);
			}
		}, 1000);
	}

	public static void createNewsDetailsView(final VisitorsArticleObject visitorsDetailsObject, final Activity activity) {

		TextView title = (TextView) activity.findViewById(R.id.title);
		title.setTypeface(Typeface.createFromAsset(activity.getAssets(), "fonts/DroidSerif-Regular.ttf"));
		title.setText(TextHelper.customizedFromHtmlTab(visitorsDetailsObject.getTitle()));
		// title.setTypeface(faceGeorgia);

		String imageString = visitorsDetailsObject.getImageString();
		if (imageString != null) {
			ImageView image = (ImageView) activity.findViewById(R.id.image);
			image.setImageBitmap(ImageHelper.convertStringToBitmap(imageString));

			TextView copyright = (TextView) activity.findViewById(R.id.copyright);
			copyright.setText(TextHelper.customizedFromHtmlTab("\u00A9 " + visitorsDetailsObject.getImageCopyright()));
			// copyright.setTypeface(faceArial);
		}

		// TextView text = (TextView) activity.findViewById(R.id.text);
		// text.setText(TextHelper.customizedFromHtml(visitorsDetailsObject.getText()));
		// // text.setTypeface(faceArial);
		// text.setMovementMethod(LinkMovementMethod.getInstance());

		final CustomScrollView scrView = (CustomScrollView) activity.findViewById(R.id.scrView);
		scrView.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT));
		scrView.postDelayed(new Runnable() {

			@Override
			public void run() {
				scrView.fullScroll(ScrollView.FOCUS_UP);
				WebView text = (WebView) activity.findViewById(R.id.text);
				text.clearView();
				text.measure(10, 10);
				scrView.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT));
				String reformatedText = TextHelper.customizedFromHtmlforWebViewTab(visitorsDetailsObject.getText());
				text.loadDataWithBaseURL("file:///android_asset/", "", "text/html", "UTF-8", null);
				text.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
				text.loadDataWithBaseURL("file:///android_asset/", reformatedText, "text/html", "UTF-8", null);
				text.setBackgroundColor(Color.TRANSPARENT);
			}
		}, 1000);
	}

	public static void createContactDetailsView(VisitorsArticleObject visitorContactDetailsObject, Activity activity) {
		ScrollView sV = (ScrollView) activity.findViewById(R.id.scrView);
		sV.fullScroll(View.FOCUS_UP);

		sV.pageScroll(View.FOCUS_UP);
		// Typeface faceArial = Typeface.createFromAsset(activity.getAssets(),
		// "fonts/Arial.ttf");
		// Typeface faceGeorgia = Typeface.createFromAsset(activity.getAssets(),
		// "fonts/Georgia.ttf");

		TextView title = (TextView) activity.findViewById(R.id.title);
		title.setTypeface(Typeface.createFromAsset(activity.getAssets(), "fonts/DroidSerif-Regular.ttf"));
		title.setText(TextHelper.customizedFromHtmlTab(visitorContactDetailsObject.getTitle()));
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
		// // text.setTypeface(faceArial);
		text.setMovementMethod(LinkMovementMethod.getInstance());
		// WebView text = (WebView) activity.findViewById(R.id.text);
		// String reformatedText =
		// TextHelper.customizedFromHtmlforWebView(visitorContactDetailsObject.getText());
		// text.loadDataWithBaseURL(null, reformatedText, "text/html", "UTF-8",
		// null);
		// WebSettings webSettings = text.getSettings();
		// webSettings.setDefaultFontSize(12);
	}
}
