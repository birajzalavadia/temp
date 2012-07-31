package de.bundestag.android.sections.members;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;
import de.bundestag.android.R;
import de.bundestag.android.helpers.TextHelper;
import de.bundestag.android.parser.objects.MembersDetailsObject;
import de.bundestag.android.parser.objects.MembersDetailsWebsitesObject;

public class MembersDetailsContactFragmentViewHelper {
	private static final String CONTACT_TEXT = "Deutscher Bundestag<br>Platz der Republik 1<br>D-11011 Berlin";

	public static void createDetailsView(MembersDetailsObject membersDetailsObject, final FragmentActivity activity) {

		try {
			TextView subtitle = (TextView) activity.findViewById(R.id.subtitle);
			subtitle.setText("Briefanschrift");
			// subtitle.setTypeface(faceGeorgia);

			TextView contactName = (TextView) activity.findViewById(R.id.contact_name);
			contactName.setText(TextHelper.customizedFromHtmlTab(membersDetailsObject.getCourse() + " " + membersDetailsObject.getFirstName() + " "
					+ membersDetailsObject.getTitle() + " " + membersDetailsObject.getLastName() + ", MdB"));
			// contactName.setTypeface(faceArialBold);

			TextView contactText = (TextView) activity.findViewById(R.id.text);
			contactText.setText(TextHelper.customizedFromHtmlTab(CONTACT_TEXT));
			// contactText.setTypeface(faceArial);

			// websites
			LinearLayout websitesHolder = (LinearLayout) activity.findViewById(R.id.websites);
			Context context = activity.getApplicationContext();
			LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

			List<MembersDetailsWebsitesObject> websites = membersDetailsObject.getWebsites();
			for (int i = 0; i < websites.size(); i++) {
				LinearLayout websiteItemLayout = (LinearLayout) inflater.inflate(R.layout.members_detail_contact_website, null);
				TextView websiteName = (TextView) websiteItemLayout.findViewById(R.id.websiteName);
				websiteName.setText(websites.get(i).getWebsiteTitle());
				websitesHolder.addView(websiteItemLayout);

				websiteItemLayout.setTag(websites.get(i).getWebsiteURL());
				websiteItemLayout.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View view) {
						String websiteURL = (String) view.getTag();
						Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(websiteURL));
						browserIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
						activity.startActivity(browserIntent);
						activity.overridePendingTransition(0, 0);
					}
				});
			}
		} catch (Exception e) {

		}
	}
}
