package de.bundestag.android.sections.plenum;

import android.database.Cursor;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import de.bundestag.android.BaseActivity;
import de.bundestag.android.R;
import de.bundestag.android.helpers.ImageHelper;
import de.bundestag.android.helpers.TextHelper;
import de.bundestag.android.storage.PlenumDatabaseAdapter;

public class PlenumObjectViewActivity extends BaseActivity
{
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState, this);
        setContentView(R.layout.plenum_detail_item);
        // Typeface faceGeorgia =
        // Typeface.createFromAsset(this.getAssets(),"fonts/Georgia.ttf");
        String plenumObjectType = getIntent().getExtras().getString("plenumObjectType");

        PlenumDatabaseAdapter pdb = new PlenumDatabaseAdapter(this);
        pdb.open();

        Cursor obj = pdb.fetchPlenumByType(Integer.parseInt(plenumObjectType));
        obj.moveToFirst();
        TextView title = (TextView) findViewById(R.id.title);
        // title.setTypeface(faceGeorgia);
        title.setText(obj.getString(obj.getColumnIndex(PlenumDatabaseAdapter.KEY_TITLE)));

        String imageString = obj.getString(obj.getColumnIndex(PlenumDatabaseAdapter.KEY_IMAGESTRING));

        if (imageString != null)
        {
            ImageView img = (ImageView) findViewById(R.id.image);
            img.setImageBitmap(ImageHelper.convertStringToBitmap(imageString));
            img.setVisibility(View.VISIBLE);
            String copy = obj.getString(obj.getColumnIndex(PlenumDatabaseAdapter.KEY_IMAGECOPYRIGHT));
            if (copy != null && !copy.equals(""))
            {
                ((TextView) findViewById(R.id.copyright)).setText("\u00A9 " + copy);
            }
        }

        TextView text = (TextView) findViewById(R.id.text);

        text.setText(TextHelper.customizedFromHtml(obj.getString(obj.getColumnIndex(PlenumDatabaseAdapter.KEY_TEXT))));
        text.setMovementMethod(LinkMovementMethod.getInstance());
        obj.close();
        pdb.close();
    }
}
