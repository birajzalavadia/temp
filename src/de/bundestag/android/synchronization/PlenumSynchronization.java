package de.bundestag.android.synchronization;

import android.content.Context;
import android.graphics.Bitmap;
import de.bundestag.android.helpers.ImageHelper;
import de.bundestag.android.parser.PlenumXMLParser;
import de.bundestag.android.parser.objects.PlenumObject;
import de.bundestag.android.parser.objects.PlenumSimpleObject;
import de.bundestag.android.storage.PlenumDatabaseAdapter;

/**
 * Plenum synchronization class.
 * 
 * Knows how to synchronize the plenum.
 */
public class PlenumSynchronization
{
    public static final int PLENUM_TYPE_MAIN = 1;

    public static final int PLENUM_TYPE_TASK = 2;

    public static final int PLENUM_TYPE_SIMPLE1 = 3;

    public static final int PLENUM_TYPE_SIMPLE2 = 4;

    public static final int PLENUM_TYPE_SIMPLE3 = 5;

    public static final int PLENUM_TYPE_SIMPLE4 = 6;

    private Context context;

    private PlenumDatabaseAdapter plenumDatabaseAdapter;

    /**
     * Setup plenum.
     */
    public void setup(Context context)
    {
        this.context = context;
    }

    /**
     * Call the plenum parser and parse the latest plenum.
     */
    public PlenumObject parsePlenum(String url)
    {
        PlenumXMLParser plenumParser = new PlenumXMLParser();
        return plenumParser.parseMain(url);
    }

    /**
     * Call the plenum parser and parse the latest plenum.
     */
    public PlenumSimpleObject parseSimplePlenum(String url)
    {
        PlenumXMLParser plenumParser = new PlenumXMLParser();
        PlenumSimpleObject pobj = plenumParser.parseSimpleObject(url);
        return pobj;
    }

    /**
     * Load and insert the plenum picture.
     */
    public void insertPicture(PlenumObject plenumObject)
    {
        Bitmap bitmap = ImageHelper.loadBitmapFromUrl(plenumObject.getImageURL());
        if (bitmap != null)
        {
            plenumObject.setImageString(ImageHelper.convertBitmapToString(bitmap));
        }
    }

    /**
     * Open the database and remove all plenum.
     */
    public void deleteAllPlenum()
    {
        plenumDatabaseAdapter.deleteAllPlenums();
    }

    /**
     * Insert a plenum object in the database.
     */
    public void insertAPlenum(PlenumObject plenum)
    {
        plenumDatabaseAdapter.createPlenum(plenum);
    }

    /**
     * Insert a simple plenum object in the database.
     */
    public void insertASimplePlenum(PlenumSimpleObject simplePlenum)
    {
        plenumDatabaseAdapter.createSimplePlenum(simplePlenum);
    }

    /**
     * Open the database.
     */
    public void openDatabase()
    {
        if (plenumDatabaseAdapter == null)
        {
            plenumDatabaseAdapter = new PlenumDatabaseAdapter(context);
        }

        plenumDatabaseAdapter.open();
    }

    /**
     * Close the database.
     */
    public void closeDatabase()
    {
        plenumDatabaseAdapter.close();
    }
}
