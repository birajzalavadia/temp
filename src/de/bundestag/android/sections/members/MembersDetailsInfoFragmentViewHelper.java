package de.bundestag.android.sections.members;

//import android.graphics.Typeface;
import android.support.v4.app.FragmentActivity;
import android.webkit.WebView;
import android.widget.LinearLayout;
import de.bundestag.android.R;
import de.bundestag.android.helpers.TextHelper;
import de.bundestag.android.parser.objects.MembersDetailsObject;

public class MembersDetailsInfoFragmentViewHelper {
	public static void createDetailsView(MembersDetailsObject membersDetailsObject, FragmentActivity activity) {
		try {
			WebView text = (WebView) activity.findViewById(R.id.textWeb);
			text.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
			text.setBackgroundColor(0x00000000);
			String reformatedText = "";
			if (membersDetailsObject.getDisclosureInformation().equals("")) {
				reformatedText = TextHelper.customizedFromHtmlforWebViewTabInfo("Veröffentlichungspflichtige Angaben liegen noch nicht vor.");
			} else {

				reformatedText = TextHelper.customizedFromHtmlforWebViewTabInfo(membersDetailsObject.getDisclosureInformation());

			}
			text.loadDataWithBaseURL("file:///android_asset/", "", "text/html", "UTF-8", null);
			text.loadDataWithBaseURL("file:///android_asset/", reformatedText, "text/html", "UTF-8", null);
		} catch (Exception e) {

		}
	}
}
