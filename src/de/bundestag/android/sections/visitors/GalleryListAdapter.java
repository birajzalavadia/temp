package de.bundestag.android.sections.visitors;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import de.bundestag.android.helpers.ImageHelper;
import de.bundestag.android.storage.VisitorsDatabaseAdapter;

public class GalleryListAdapter extends CursorAdapter
{
    int mGalleryItemBackground;
    private Context mContext;

    public GalleryListAdapter(Context context, Cursor cursor)
    {
        super(context, cursor, true);
        mContext = context;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor)
    {
        // TODO Auto-generated method stub
        ImageView img = (ImageView) view;

        String imageString = cursor.getString(cursor.getColumnIndex(VisitorsDatabaseAdapter.KEY_IMAGE_STRING));
        String imageId = cursor.getString(cursor.getColumnIndex(VisitorsDatabaseAdapter.KEY_IMAGE_ID));
        if (imageString != null)
        {
            img.setImageBitmap(ImageHelper.convertStringToBitmap(imageString));
            img.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent)
    {
        // TODO Auto-generated method stub
        ImageView img = new ImageView(mContext);
        img.setPadding(0, 0, 5, 0);
        img.setAdjustViewBounds(true);
        return img;
    }
}