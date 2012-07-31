package de.bundestag.android.sections.plenum;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.support.v4.app.FragmentActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.webkit.WebView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import de.bundestag.android.R;
import de.bundestag.android.constant.DataHolder;
import de.bundestag.android.customviews.CustomScrollView;
import de.bundestag.android.customviews.WebActivityClient;
import de.bundestag.android.helpers.ImageHelper;
import de.bundestag.android.helpers.TextHelper;
import de.bundestag.android.parser.objects.PlenumObject;

public class PlenumAufgabeViewHelper {
	public static void createView(PlenumObject plenumObject, FragmentActivity activity) {
		TextView title = (TextView) activity.findViewById(R.id.title);
		// title.setTypeface(faceGeorgia);
		title.setText(plenumObject.getTitle());

		String imageString = plenumObject.getImageString();
		if (imageString != null) {
			ImageView img = (ImageView) activity.findViewById(R.id.image);
			img.setImageBitmap(ImageHelper.getScaleImage(activity, imageString));
			img.setVisibility(View.VISIBLE);

			String copy = plenumObject.getImageCopyright();
			if ((copy != null) && !copy.equals("")) {
				((TextView) activity.findViewById(R.id.copyright)).setText("\u00A9 " + copy);
			}
		}

		TextView text = (TextView) activity.findViewById(R.id.text);
        text.setText(TextHelper.customizedFromHtml(plenumObject.getText()));

	}

	public static void createViewTab(final PlenumObject plenumObject, final FragmentActivity activity) {
		DataHolder.createProgressDialog(activity);
		TextView title = (TextView) activity.findViewById(R.id.title);
		// title.setTypeface(faceGeorgia);
		title.setText(plenumObject.getTitle());

		String imageString = plenumObject.getImageString();
		if (imageString != null) {
			ImageView img = (ImageView) activity.findViewById(R.id.image);
			img.setImageBitmap(ImageHelper.getScaleImage(activity, imageString));
			img.setVisibility(View.VISIBLE);

			String copy = plenumObject.getImageCopyright();
			if ((copy != null) && !copy.equals("")) {
				((TextView) activity.findViewById(R.id.copyright)).setText("\u00A9 " + copy);
			}
		}

		final ScrollView scrView = (ScrollView) activity.findViewById(R.id.scrView);
		scrView.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT));
		scrView.postDelayed(new Runnable() {

			@Override
			public void run() {
				scrView.fullScroll(ScrollView.FOCUS_UP);
				final WebView text = (WebView) activity.findViewById(R.id.textWebView);
				text.setOnTouchListener(new OnTouchListener() {

					@Override
					public boolean onTouch(View v, MotionEvent event) {
						// TODO Auto-generated method stub
						text.setWebViewClient(new WebActivityClient(activity));
						return false;
					}
				});
				text.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
				text.setBackgroundColor(0x00000000);
				String reformatedText = TextHelper.customizedFromHtmlforWebViewTab(plenumObject.getText());
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
