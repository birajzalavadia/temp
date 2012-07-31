package de.bundestag.android.sections.plenum;

import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import de.bundestag.android.BaseFragment;
import de.bundestag.android.R;
import de.bundestag.android.constant.DataHolder;
import de.bundestag.android.parser.objects.PlenumObject;
import de.bundestag.android.storage.PlenumDatabaseAdapter;
import de.bundestag.android.synchronization.PlenumSynchronization;

public class PlenumAufgabeFragment extends BaseFragment {
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.plenum_detail_item, container, false);
		return view;
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		getPlenumTaskObject();
	}

	/**
	 * Create Plenum object from database.
	 * 
	 * @return
	 */
	public PlenumObject getPlenumTaskObject() {
		PlenumObject plenumObject = new PlenumObject();

		PlenumDatabaseAdapter pdb = new PlenumDatabaseAdapter(getActivity());
		pdb.open();

		Cursor obj = pdb.fetchPlenumByType(PlenumSynchronization.PLENUM_TYPE_TASK);
		if ((obj != null) && (obj.getCount() > 0)) {
			obj.moveToFirst();

			plenumObject.setTitle(obj.getString(obj.getColumnIndex(PlenumDatabaseAdapter.KEY_TITLE)));
			plenumObject.setImageString(obj.getString(obj.getColumnIndex(PlenumDatabaseAdapter.KEY_IMAGESTRING)));
			plenumObject.setImageCopyright(obj.getString(obj.getColumnIndex(PlenumDatabaseAdapter.KEY_IMAGECOPYRIGHT)));

			plenumObject.setText(obj.getString(obj.getColumnIndex(PlenumDatabaseAdapter.KEY_TEXT)));
		}
		obj.close();
		pdb.close();
		PlenumAufgabeViewHelper.createViewTab(plenumObject, getActivity());
		DataHolder.releaseScreenLock(this.getActivity());
		return plenumObject;
	}
}
