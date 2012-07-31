package de.bundestag.android.sections.plenum;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import de.bundestag.android.R;
import de.bundestag.android.helpers.ImageHelper;
import de.bundestag.android.helpers.TextHelper;
import de.bundestag.android.storage.NewsDatabaseAdapter;

/**
 * News list adapter (list item renderer).
 * 
 * This class knows how to show news elements for a list.
 * 
 * Uses the database cursor to extract the data for the list.
 */
public class PlenumNewsListAdapter extends CursorAdapter
{
    private static final int TOP_LAYOUT = 0;

    private static final int GENERAL_LAYOUT = 1;

    private final LayoutInflater mInflater;

    public PlenumNewsListAdapter(Context context, Cursor cursor)
    {
        super(context, cursor, true);
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public int getViewTypeCount()
    {
        return 2;
    }

    @Override
    public int getItemViewType(int position)
    {
        return (position == 0) ? TOP_LAYOUT : GENERAL_LAYOUT;
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent)
    {
        final View view;

        int type = getItemViewType(cursor.getPosition());
        if (type == TOP_LAYOUT)
        {
            view = mInflater.inflate(R.layout.news_list_firstitem, parent, false);
        }
        else
        {
            view = mInflater.inflate(R.layout.news_list_item, parent, false);
        }

        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor)
    {
        TextView title = (TextView) view.findViewById(R.id.title);
        title.setText(TextHelper.customizedFromHtml(cursor.getString(cursor.getColumnIndex(NewsDatabaseAdapter.KEY_TITLE))));

        ImageView image = (ImageView) view.findViewById(R.id.image);
        TextView imagecopy = (TextView) view.findViewById(R.id.image_copy);
        String imageString = cursor.getString(cursor.getColumnIndex(NewsDatabaseAdapter.KEY_IMAGE_STRING));
        if (imageString != null)
        {
            image.setImageBitmap(ImageHelper.convertStringToBitmap(imageString));
            image.setVisibility(View.VISIBLE);
            imagecopy.setVisibility(View.VISIBLE);

            TextView image_copy = (TextView) view.findViewById(R.id.image_copy);
            image_copy.setText("\u00A9 " + cursor.getString(cursor.getColumnIndex(NewsDatabaseAdapter.KEY_IMAGECOPYRIGHT)));
        }
        else
        {
            image.setVisibility(View.GONE);
            imagecopy.setVisibility(View.GONE);
        }

        TextView description = (TextView) view.findViewById(R.id.description);
        description.setText(TextHelper.customizedFromHtml(cursor.getString(cursor.getColumnIndex(NewsDatabaseAdapter.KEY_TEASER))));
    }
}