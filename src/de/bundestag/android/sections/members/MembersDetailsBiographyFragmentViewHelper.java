package de.bundestag.android.sections.members;

//import android.graphics.Typeface;
import android.app.Activity;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.TextView;
import de.bundestag.android.R;
import de.bundestag.android.helpers.TextHelper;
import de.bundestag.android.parser.objects.MembersDetailsObject;

public class MembersDetailsBiographyFragmentViewHelper {
	public static void createDetailsView(MembersDetailsObject membersDetailsObject, View view, Activity activity) {
		try {
			TextView bioText = null;
			bioText = (TextView) view.findViewById(R.id.bio_details_tab);
			if (membersDetailsObject.getInformation().equals("")) {
				bioText.setText(TextHelper.customizedFromHtmlTab("Biografische Informationen liegen noch nicht vor."));
			} else {
				bioText.setText(TextHelper.customizedFromHtmlTab(membersDetailsObject.getInformation()));
				bioText.setMovementMethod(LinkMovementMethod.getInstance());
			}
		} catch (Exception e) {

		}
	}
}
