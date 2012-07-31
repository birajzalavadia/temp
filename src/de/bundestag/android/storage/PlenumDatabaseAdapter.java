package de.bundestag.android.storage;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import de.bundestag.android.parser.objects.PlenumObject;
import de.bundestag.android.parser.objects.PlenumSimpleObject;

/**
 * Plenum database adapter.
 * 
 * Contains helper methods to interact with the plenum table in the database.
 */
public class PlenumDatabaseAdapter
{
    public static final String KEY_ROWID = "_id";

    public static final String KEY_STATUS = "status";
    public static final String KEY_TYPE = "type";
    public static final String KEY_DATE = "date";
    public static final String KEY_TITLE = "title";
    public static final String KEY_TEASER = "teaser";
    public static final String KEY_TEXT = "text";
    public static final String KEY_IMAGEURL = "image_url";
    public static final String KEY_IMAGECOPYRIGHT = "image_copyright";
    public static final String KEY_IMAGESTRING = "image_string";

    private Context context;

    private BaseDatabaseHelper dbHelper;

    /**
     * Plenum database adapter constructor.
     */
    public PlenumDatabaseAdapter(Context context)
    {
        this.context = context;
    }

    /**
     * Open connection to plenum database table.
     */
    public PlenumDatabaseAdapter open() throws SQLException
    {
        if (dbHelper == null)
        {
            dbHelper = new BaseDatabaseHelper(context);
        }
        if (dbHelper.database == null)
        {
            dbHelper.openDatabase();
        }

        return this;
    }

    /**
     * Close database helper.
     */
    public void close()
    {
        if (dbHelper != null)
        {
            dbHelper.close();
            dbHelper.database.close();
        }
    }

    /**
     * Inserts a new plenum object.
     */
    public long createPlenum(PlenumObject plenumObject)
    {
        ContentValues initialValues = createContentValues(plenumObject);

        return dbHelper.database.insert(BaseDatabaseHelper.PLENUM_TABLE_NAME, null, initialValues);
    }

    /**
     * Inserts a new plenum object.
     */
    public long createSimplePlenum(PlenumSimpleObject plenumSimpleObject)
    {
        ContentValues initialValues = createSimpleContentValues(plenumSimpleObject);

        return dbHelper.database.insert(BaseDatabaseHelper.PLENUM_TABLE_NAME, null, initialValues);
    }

    /**
     * Delete a plenum object in the dbHelper.database.
     */
    public boolean deletePlenum(long rowId)
    {
        return dbHelper.database.delete(BaseDatabaseHelper.PLENUM_TABLE_NAME, KEY_ROWID + "=" + rowId, null) > 0;
    }

    /**
     * Drop the plenums table.
     */
    public int deleteAllPlenums()
    {
        return dbHelper.database.delete(BaseDatabaseHelper.PLENUM_TABLE_NAME, "1", null);
    }

    /**
     * Database table cursor method to fetch all plenum.
     */
    public Cursor fetchAllPlenum()
    {
        return dbHelper.database.query(BaseDatabaseHelper.PLENUM_TABLE_NAME, new String[] {
                KEY_ROWID, KEY_STATUS, KEY_TYPE, KEY_DATE, KEY_TITLE, KEY_TEASER, KEY_TEXT, KEY_IMAGEURL, KEY_IMAGECOPYRIGHT, KEY_IMAGESTRING
        }, null, null, null, null, null);
    }

    /**
     * Database table cursor method to fetch all plenum.
     */
    public Cursor fetchPlenumByType(int type)
    {
        return dbHelper.database.query(BaseDatabaseHelper.PLENUM_TABLE_NAME, new String[] {
                KEY_ROWID, KEY_STATUS, KEY_TYPE, KEY_DATE, KEY_TITLE, KEY_TEASER, KEY_TEXT, KEY_IMAGEURL, KEY_IMAGECOPYRIGHT, KEY_IMAGESTRING
        }, KEY_TYPE + "=" + type, null, null, null, null, null);
    }

    /**
     * Database table cursor method to fetch one plenum from a specfic row.
     */
    public Cursor fetchPlenum(long rowId) throws SQLException
    {
        Cursor mCursor = dbHelper.database.query(true, BaseDatabaseHelper.PLENUM_TABLE_NAME, new String[] {
                KEY_ROWID, KEY_STATUS, KEY_TYPE, KEY_DATE, KEY_TITLE, KEY_TEASER, KEY_TEXT, KEY_IMAGEURL, KEY_IMAGECOPYRIGHT, KEY_IMAGESTRING
        }, KEY_ROWID + "=" + rowId, null, null, null, null, null);

        if (mCursor != null)
        {
            mCursor.moveToFirst();
        }

        return mCursor;
    }

    /**
     * Create a plenum content value object from the general plenum object.
     * 
     * Needed for inserting a plenum object.
     */
    private ContentValues createContentValues(PlenumObject plenumObject)
    {
        ContentValues values = new ContentValues();

        values.put(KEY_STATUS, plenumObject.getStatus());
        values.put(KEY_TYPE, plenumObject.getType());
        values.put(KEY_DATE, plenumObject.getDate());
        values.put(KEY_TITLE, plenumObject.getTitle());
        values.put(KEY_TEASER, plenumObject.getTeaser());
        values.put(KEY_TEXT, plenumObject.getText());
        values.put(KEY_IMAGEURL, plenumObject.getImageURL());
        values.put(KEY_IMAGECOPYRIGHT, plenumObject.getImageCopyright());
        values.put(KEY_IMAGESTRING, plenumObject.getImageString());

        return values;
    }

    /**
     * Create a plenum content value object from the general plenum object.
     * 
     * Needed for inserting a plenum object.
     */
    private ContentValues createSimpleContentValues(PlenumSimpleObject plenumSimpleObject)
    {
        ContentValues values = new ContentValues();

        values.put(KEY_TYPE, plenumSimpleObject.getType());
        values.put(KEY_DATE, plenumSimpleObject.getDate());
        values.put(KEY_TITLE, plenumSimpleObject.getTitle());
        values.put(KEY_TEXT, plenumSimpleObject.getText());

        return values;
    }
}
