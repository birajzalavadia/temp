package de.bundestag.android.sections.members;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import de.bundestag.android.R;
import de.bundestag.android.helpers.ImageHelper;
import de.bundestag.android.helpers.TextHelper;
import de.bundestag.android.parser.objects.MembersDetailsObject;
import de.bundestag.android.parser.objects.MembersDetailsWebsitesObject;

public class MembersDetailsContactViewHelper
{
    private static final String CONTACT_TEXT = "Deutscher Bundestag<br>Platz der Republik 1<br>D-11011 Berlin";

    public static void createDetailsView(MembersDetailsObject membersDetailsObject, final FragmentActivity activity)
    {
        // Typeface faceArial = Typeface.createFromAsset(activity.getAssets(),
        // "fonts/Arial.ttf");
        // Typeface faceArialBold =
        // Typeface.createFromAsset(activity.getAssets(),
        // "fonts/ArialBold.ttf");
        // Typeface faceGeorgia = Typeface.createFromAsset(activity.getAssets(),
        // "fonts/Georgia.ttf");

        // name, party, occupation, status
        String imageString = membersDetailsObject.getMediaPhotoImageString();
        ImageView image = (ImageView) activity.findViewById(R.id.image);
        if (imageString != null)
        {
            image.setImageBitmap(ImageHelper.convertStringToBitmap(imageString));

            // TODO - add click for more details
            TextView copyright = (TextView) activity.findViewById(R.id.copy);
            copyright.setText(TextHelper.customizedFromHtml("\u00A9 " + membersDetailsObject.getMediaPhotoCopyright()));
            // copyright.setTypeface(faceArial);
        }
        else
        {
            image.setImageBitmap(null);
        }

        TextView name = (TextView) activity.findViewById(R.id.name);
        name.setText(TextHelper.customizedFromHtml(membersDetailsObject.getCourse() + " " + membersDetailsObject.getFirstName() + " "
                + membersDetailsObject.getTitle() + " " + membersDetailsObject.getLastName() + ",<br>" + membersDetailsObject.getFraction()));
        // name.setTypeface(faceGeorgia);

        TextView occupation = (TextView) activity.findViewById(R.id.occupation);
        occupation.setText(TextHelper.customizedFromHtml(membersDetailsObject.getProfession()));
        // occupation.setTypeface(faceArialBold);

        TextView status = (TextView) activity.findViewById(R.id.status);

        String statusText = "";
        if (membersDetailsObject.getStatus().equals("Verstorben"))
        {
            statusText = "Verstorben am " + membersDetailsObject.getExitDate();
        }
        else if (membersDetailsObject.getStatus().equals("Ausgeschieden"))
        {
            statusText = "Ausgeschieden am " + membersDetailsObject.getExitDate();
        }
        else if (membersDetailsObject.getStatus().equals("Aktiv"))
        {
            if (membersDetailsObject.getElected().equals("Landesliste"))
            {
                statusText = "Gewählt über die Landesliste " + membersDetailsObject.getCity();
            }
            else if (membersDetailsObject.getElected().equals("direkt"))
            {
                statusText = "Direkt gewählt im Wahlkreis " + membersDetailsObject.getElectionName();
            }

        }
        status.setText(TextHelper.customizedFromHtml(statusText));
        // status.setTypeface(faceArial);

        TextView title = (TextView) activity.findViewById(R.id.title);
        title.setText("Kontakt");
        // title.setTypeface(faceGeorgia);

        TextView subtitle = (TextView) activity.findViewById(R.id.subtitle);
        subtitle.setText("Briefanschrift");
        // subtitle.setTypeface(faceGeorgia);

        TextView contactName = (TextView) activity.findViewById(R.id.contact_name);
        contactName.setText(TextHelper.customizedFromHtml(membersDetailsObject.getCourse() + " " + membersDetailsObject.getFirstName() + " "
                + membersDetailsObject.getTitle() + " " + membersDetailsObject.getLastName() + ", MdB"));
        // contactName.setTypeface(faceArialBold);

        TextView contactText = (TextView) activity.findViewById(R.id.text);
        contactText.setText(TextHelper.customizedFromHtml(CONTACT_TEXT));
        // contactText.setTypeface(faceArial);

        // websites
        LinearLayout websitesHolder = (LinearLayout) activity.findViewById(R.id.websites);
        Context context = activity.getApplicationContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        List<MembersDetailsWebsitesObject> websites = membersDetailsObject.getWebsites();
        for (int i = 0; i < websites.size(); i++)
        {
            LinearLayout websiteItemLayout = (LinearLayout) inflater.inflate(R.layout.members_detail_contact_website, null);
            TextView websiteName = (TextView) websiteItemLayout.findViewById(R.id.websiteName);
            websiteName.setText(websites.get(i).getWebsiteTitle());
            websitesHolder.addView(websiteItemLayout);

            websiteItemLayout.setTag(websites.get(i).getWebsiteURL());
            websiteItemLayout.setOnClickListener(new OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    String websiteURL = (String) view.getTag();
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(websiteURL));
                    browserIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    activity.startActivity(browserIntent);
                    activity.overridePendingTransition(0, 0);
                }
            });
        }
    }
}
