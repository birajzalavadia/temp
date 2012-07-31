package de.bundestag.android.sections.plenum;

import android.support.v4.app.FragmentActivity;
import android.text.method.LinkMovementMethod;
import android.widget.ImageView;
import android.widget.TextView;
import de.bundestag.android.R;
import de.bundestag.android.constant.AppConstant;
import de.bundestag.android.helpers.ImageHelper;
import de.bundestag.android.helpers.TextHelper;
import de.bundestag.android.parser.NewsXMLParser;
import de.bundestag.android.parser.objects.NewsDetailsObject;

public class PlenumDebatesNewsDetailViewHelper {
	public static void createDetailsView(NewsDetailsObject newsDetailsObject, FragmentActivity activity) {
		TextView title = (TextView) activity.findViewById(R.id.title);
		title.setText(TextHelper.customizedFromHtml(newsDetailsObject.getTitle()));

		String imageString = newsDetailsObject.getImageURL();
		if (imageString != null) {
			ImageView image = (ImageView) activity.findViewById(R.id.image);
			image.setImageBitmap(ImageHelper.loadBitmapFromUrlAndResize(activity, imageString));

			TextView copyright = (TextView) activity.findViewById(R.id.copyright);
			copyright.setText(TextHelper.customizedFromHtml("\u00A9 " + newsDetailsObject.getImageCopyright()));
		}

		TextView text = (TextView) activity.findViewById(R.id.text);
		if (AppConstant.isFragmentSupported) {
			text.setText(TextHelper.customizedFromHtmlTab(newsDetailsObject.getText()));
		} else {
			text.setText(TextHelper.customizedFromHtml(newsDetailsObject.getText()));
		}
		text.setMovementMethod(LinkMovementMethod.getInstance());

	}

	public static NewsDetailsObject createNewsDetailsObject(int newsId) {
		String detailXML = (String) PlenumDebatesActivity.debates.get(newsId).get(PlenumDebatesViewHelper.KEY_DETAIL_XML);

		NewsXMLParser debatesNewsDetailParser = new NewsXMLParser();
		// List<PlenumAgendaObject> speakers = debatesParser.parseAgenda();

		NewsDetailsObject newsDetailsObject = debatesNewsDetailParser.parseDetails(detailXML);

		return newsDetailsObject;
	}
}
