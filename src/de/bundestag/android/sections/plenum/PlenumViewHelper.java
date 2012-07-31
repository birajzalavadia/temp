package de.bundestag.android.sections.plenum;

//import android.graphics.Typeface;
import android.support.v4.app.FragmentActivity;
import android.widget.TextView;
import de.bundestag.android.R;
import de.bundestag.android.helpers.TextHelper;
import de.bundestag.android.parser.objects.PlenumObject;

public class PlenumViewHelper
{
	public static void createView(PlenumObject plenumObject, FragmentActivity activity)
	{
//		Typeface faceArial = Typeface.createFromAsset(activity.getAssets(), "fonts/Arial.ttf");

		TextView title = (TextView) activity.findViewById(R.id.title);
		title.setText(TextHelper.customizedFromHtml(plenumObject.getTitle()));
//		title.setTypeface(faceArial);

		TextView teaser = (TextView) activity.findViewById(R.id.teaser);
		teaser.setText(TextHelper.customizedFromHtml(plenumObject.getTeaser()));
//		teaser.setTypeface(faceArial);

		/*
		TextView link = (TextView) activity.findViewById(R.id.link);
		link.setText(TextHelper.customizedFromHtml(plenumObject.getLink()));
		link.setTypeface(faceArial);
		*/
	}
}
