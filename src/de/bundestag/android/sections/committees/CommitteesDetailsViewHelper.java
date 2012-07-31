package de.bundestag.android.sections.committees;

//import android.graphics.Typeface;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v4.app.FragmentActivity;
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
import de.bundestag.android.parser.objects.CommitteesDetailsObject;

public class CommitteesDetailsViewHelper {
	public static void createDetailsNewsView(CommitteesDetailsObject committeesDetailsObject, FragmentActivity activity) {
		// Typeface faceGeorgia = Typeface.createFromAsset(activity.getAssets(),
		// "fonts/Georgia.ttf");
		// Typeface faceArial = Typeface.createFromAsset(activity.getAssets(),
		// "fonts/Arial.ttf");

		TextView name = (TextView) activity.findViewById(R.id.name);
		name.setText(TextHelper.customizedFromHtml(committeesDetailsObject.getName()));
		// name.setTypeface(faceGeorgia);

		String imageString = committeesDetailsObject.getPhotoString();
		if (imageString != null) {
			ImageView image = (ImageView) activity.findViewById(R.id.image);
			image.setImageBitmap(ImageHelper.convertStringToBitmap(imageString));

			TextView copyright = (TextView) activity.findViewById(R.id.copyright);
			copyright.setText(TextHelper.customizedFromHtml("\u00A9 " + committeesDetailsObject.getPhotoCopyright()));
			// copyright.setTypeface(faceArial);
		}

		TextView text = (TextView) activity.findViewById(R.id.text);
		text.setText(TextHelper.customizedFromHtml(committeesDetailsObject.getDescription()));
		text.setMovementMethod(LinkMovementMethod.getInstance());
		// text.setTypeface(faceArial);
	}

	public static void createDetailsMembersView(CommitteesDetailsObject committeesDetailsObject, FragmentActivity activity) {
		// Typeface faceArial = Typeface.createFromAsset(activity.getAssets(),
		// "fonts/Arial.ttf");

		TextView name = (TextView) activity.findViewById(R.id.name);
		name.setText(TextHelper.customizedFromHtml(committeesDetailsObject.getName()));
		// name.setTypeface(faceArial);

		String imageString = committeesDetailsObject.getPhotoString();
		ImageView image = (ImageView) activity.findViewById(R.id.image);
		if (imageString != null) {
			image.setImageBitmap(ImageHelper.convertStringToBitmap(imageString));
		} else {
			image.setImageBitmap(null);
		}

		TextView copyright = (TextView) activity.findViewById(R.id.copyright);
		copyright.setText(TextHelper.customizedFromHtml(committeesDetailsObject.getPhotoCopyright()));
		// copyright.setTypeface(faceArial);

		TextView text = (TextView) activity.findViewById(R.id.text);
		text.setText(TextHelper.customizedFromHtml(committeesDetailsObject.getDescription()));
		text.setMovementMethod(LinkMovementMethod.getInstance());
		// text.setTypeface(faceArial);
	}

	public static void createDetailsTasksView(CommitteesDetailsObject committeesDetailsObject, FragmentActivity activity) {
		// Typeface faceArial = Typeface.createFromAsset(activity.getAssets(),
		// "fonts/Arial.ttf");
		// Typeface faceGeorgia = Typeface.createFromAsset(activity.getAssets(),
		// "fonts/Georgia.ttf");

		TextView name = (TextView) activity.findViewById(R.id.name);
		name.setText(TextHelper.customizedFromHtml(committeesDetailsObject.getName()));
		// name.setTypeface(faceGeorgia);

		String imageString = committeesDetailsObject.getPhotoString();
		ImageView image = (ImageView) activity.findViewById(R.id.image);
		TextView copyright = (TextView) activity.findViewById(R.id.copyright);
		if (imageString != null) {
			image.setVisibility(View.VISIBLE);
			copyright.setVisibility(View.VISIBLE);
			image.setImageBitmap(ImageHelper.getScaleImage(activity, imageString));

			copyright.setText(TextHelper.customizedFromHtml("\u00A9 " + committeesDetailsObject.getPhotoCopyright()));
			// copyright.setTypeface(faceArial);
		} else {
			image.setVisibility(View.GONE);
			copyright.setVisibility(View.GONE);
		}

		TextView text = (TextView) activity.findViewById(R.id.text);
		text.setText(TextHelper.customizedFromHtml(committeesDetailsObject.getDescription()));
		text.setMovementMethod(LinkMovementMethod.getInstance());
		// text.setTypeface(faceArial);
	}

	public static void createDetailsView(CommitteesDetailsObject committeesDetailsObject, FragmentActivity activity) {
		TextView title = (TextView) activity.findViewById(R.id.title_tab);
		title.setTypeface(Typeface.createFromAsset(activity.getAssets(), "fonts/DroidSerif-Regular.ttf"));
		title.setText(TextHelper.customizedFromHtmlTab(committeesDetailsObject.getName()));
		// name.setTypeface(faceGeorgia);

		String imageString = committeesDetailsObject.getPhotoString();
		if (imageString != null) {
			ImageView image = (ImageView) activity.findViewById(R.id.image_tab);
			image.setImageBitmap(ImageHelper.getScaleImage(activity, imageString));
			TextView copyright = (TextView) activity.findViewById(R.id.copyright_tab);
			copyright.setText("\u00A9 " + TextHelper.customizedFromHtmlTab(committeesDetailsObject.getPhotoCopyright()));
		}

		WebView text = (WebView) activity.findViewById(R.id.text_tab);
		text.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
		String reformatedText = TextHelper.customizedFromHtmlforWebViewTab(committeesDetailsObject.getDescription());
		text.loadDataWithBaseURL("file:///android_asset/", "", "text/html", "UTF-8", null);
		text.loadDataWithBaseURL("file:///android_asset/", reformatedText, "text/html", "UTF-8", null);
		text.setBackgroundColor(0x00000000);
		text.setBackgroundResource(R.drawable.eagle_bg1);
	}

	public static void createDetailsViewTab(final CommitteesDetailsObject committeesDetailsObject, final FragmentActivity activity) {
		DataHolder.createProgressDialog(activity);
		TextView title = (TextView) activity.findViewById(R.id.title_tab);
		title.setTypeface(Typeface.createFromAsset(activity.getAssets(), "fonts/DroidSerif-Regular.ttf"));
		title.setText(TextHelper.customizedFromHtmlTab(committeesDetailsObject.getName()));
		// name.setTypeface(faceGeorgia);

		ImageView image = (ImageView) activity.findViewById(R.id.image_tab);
		TextView copyright = (TextView) activity.findViewById(R.id.copyright_tab);
		String imageString = committeesDetailsObject.getPhotoString();
		if (imageString != null) {
			image.setVisibility(View.VISIBLE);
			copyright.setVisibility(View.VISIBLE);
			image.setImageBitmap(ImageHelper.getScaleImage(activity, imageString));
			copyright.setText("\u00A9 " + TextHelper.customizedFromHtmlTab(committeesDetailsObject.getPhotoCopyright()));
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
				String reformatedText = TextHelper.customizedFromHtmlforWebViewTab(committeesDetailsObject.getDescription());
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
