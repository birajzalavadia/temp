package de.bundestag.android.sections.impressum;

//import android.graphics.Typeface;
import android.support.v4.app.FragmentActivity;
import android.text.method.LinkMovementMethod;
import android.widget.TextView;
import de.bundestag.android.R;
import de.bundestag.android.helpers.TextHelper;

public class ImpressumViewHelper
{
    public static void createImpressumView(FragmentActivity activity)
    {
        // Typeface faceArial = Typeface.createFromAsset(activity.getAssets(),
        // "fonts/Arial.ttf");
        // Typeface faceGeorgia = Typeface.createFromAsset(activity.getAssets(),
        // "fonts/Georgia.ttf");

        TextView name = (TextView) activity.findViewById(R.id.name);
        name.setText(TextHelper.customizedFromHtml("Impressum"));
        // name.setTypeface(faceGeorgia);

        TextView impressumText = (TextView) activity.findViewById(R.id.text);
        impressumText
                .setText(TextHelper.customizedFromHtml(("<h2>Deutscher Bundestag</h2> <p>Verfassungsorgan der Bundesrepublik Deutschland<br /> Platz der Republik 1<br /> 10557 Berlin<br /> Postanschrift<br /> 11011 Berlin</p> <h2>Gesetzlicher Vertreter</h2> <p>Prof. Dr. Norbert Lammert, Präsident des Deutschen Bundestages</p> <h2>Kontakt</h2> <p>Telefon: +49 30 2270<br /> Fax: +49 30 22736655</p> <p>E-Mail: <a href=\"mailto:mail@bundestag.de\">mail@bundestag.de</a></p> <h2>Online-Dienste des Deutschen Bundestages</h2> <p>Deutscher Bundestag<br /> Referat PuK 4<br /> Online-Dienste, Parlamentsfernsehen<br /> Platz der Republik 1<br /> 11011 Berlin</p> <p>Telefon: +49 30 22735408<br /> Fax: +49 30 22736655</p> <p>E-Mail: <a href=\"mailto:vorzimmer.puk4@bundestag.de\">vorzimmer.puk4@bundestag.de</a></p> <p>Gesamtleitung: Dr. Maika Jachmann<br /> Redaktionsleitung: Dr. Volker Müller<br /> Projektmanagement und Koordination: Christian Zentner</p> <h2>Konzept, Design, Entwicklung, Hosting</h2> <p>Babiel GmbH Moskauer Straße 27<br /> D-40227 Düsseldorf</p> <p>Telefon: +49 211 1793490</p> <p>E-Mail: <a href=\"mailto:info@babiel.com\">info@babiel.com</a></p>")));
        // impressumText.setAutoLinkMask(Linkify.EMAIL_ADDRESSES);
        impressumText.setMovementMethod(LinkMovementMethod.getInstance());
        // impressumText.setTypeface(faceArial);
    }
}
