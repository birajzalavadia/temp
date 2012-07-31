package de.bundestag.android.sections.plenum;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.KeyEvent;
import de.bundestag.android.BaseActivity;
import de.bundestag.android.R;
import de.bundestag.android.fragments.GeneralDetailsFragment;
import de.bundestag.android.parser.objects.PlenumObject;
import de.bundestag.android.storage.PlenumDatabaseAdapter;
import de.bundestag.android.synchronization.PlenumSynchronization;

public class PlenumAufgabeActivity extends BaseActivity
{
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState, this);

        setContentView(R.layout.plenum_aufgabe);

        PlenumObject plenumTaskObject = getPlenumTaskObject();

        GeneralDetailsFragment detailsFragment = (GeneralDetailsFragment) getSupportFragmentManager().findFragmentById(R.id.detailsFragment);
        detailsFragment.showPlenumTasks(plenumTaskObject);
    }

    /**
     * Create Plenum object from database.
     * @return
     */
    private PlenumObject getPlenumTaskObject()
    {
        PlenumObject plenumObject = new PlenumObject();
        
        PlenumDatabaseAdapter pdb = new PlenumDatabaseAdapter(this);
        pdb.open();

        Cursor obj = pdb.fetchPlenumByType(PlenumSynchronization.PLENUM_TYPE_TASK);
        if ((obj != null) && (obj.getCount() > 0))
        {
            obj.moveToFirst();

            plenumObject.setTitle(obj.getString(obj.getColumnIndex(PlenumDatabaseAdapter.KEY_TITLE)));
            plenumObject.setImageString(obj.getString(obj.getColumnIndex(PlenumDatabaseAdapter.KEY_IMAGESTRING)));
            plenumObject.setImageCopyright(obj.getString(obj.getColumnIndex(PlenumDatabaseAdapter.KEY_IMAGECOPYRIGHT)));

            plenumObject.setText(obj.getString(obj.getColumnIndex(PlenumDatabaseAdapter.KEY_TEXT)));
        }
        obj.close();
        pdb.close();
        
        return plenumObject;
    }

    /**
     * Hack to handle the back button.
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if (keyCode == KeyEvent.KEYCODE_BACK)
        {
            Intent intent = new Intent();
            intent.setClass(this, PlenumSitzungenActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            this.startActivity(intent);
            overridePendingTransition(0,0);
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }
}
