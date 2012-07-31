package de.bundestag.android.sections.members;

//import android.graphics.Typeface;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import de.bundestag.android.R;
import de.bundestag.android.helpers.ImageHelper;
import de.bundestag.android.helpers.TextHelper;
import de.bundestag.android.parser.objects.MembersDetailsObject;

public class MembersDetailsViewHelper
{
    public static void createDetailsView(MembersDetailsObject membersDetailsObject, FragmentActivity activity, int groupsCount)
    {
        // Typeface faceArial = Typeface.createFromAsset(activity.getAssets(),
        // "fonts/Arial.ttf");
        // // Typeface faceGeorgia =
        // Typeface.createFromAsset(activity.getAssets(), "fonts/Georgia.ttf");
        // Typeface faceArialBold =
        // Typeface.createFromAsset(activity.getAssets(),
        // "fonts/ArialBold.ttf");

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

        TextView bio = (TextView) activity.findViewById(R.id.bio);
        bio.setText("Biografie");
        // bio.setTypeface(faceGeorgia);

        TextView publications = (TextView) activity.findViewById(R.id.publications);
        publications.setText("Veröffentlichungspflichtige Angaben");
        // publications.setTypeface(faceGeorgia);

        TextView groups = (TextView) activity.findViewById(R.id.committees);
        groups.setText(TextHelper.customizedFromHtml("Mitgliedschaften in Ausschüssen & Gremien"));
        // groups.setTypeface(faceGeorgia);

        TextView kontakt = (TextView) activity.findViewById(R.id.kontakt);
        kontakt.setText("Kontakt");
        // kontakt.setTypeface(faceGeorgia);

        if (groupsCount <= 0)
        {
            LinearLayout groupsHolder = (LinearLayout) activity.findViewById(R.id.committeesButton);
            groupsHolder.setVisibility(View.GONE);

            ImageView groupsHolderDivider = (ImageView) activity.findViewById(R.id.committeesButtonLine);
            groupsHolderDivider.setVisibility(View.GONE);
        }
    }
}
