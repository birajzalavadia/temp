package de.bundestag.android.sections.committees;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import de.bundestag.android.R;
import de.bundestag.android.constant.AppConstant;
import de.bundestag.android.constant.DataHolder;
import de.bundestag.android.helpers.TextHelper;
import de.bundestag.android.storage.CommitteesDatabaseAdapter;

/**
 * Committees list adapter (list item renderer).
 * 
 * This class knows how to show news elements for a list.
 * 
 * Uses the database cursor to extract the data for the list.
 */
public class CommitteesListAdapter extends CursorAdapter {
	private LayoutInflater mInflater;

	public CommitteesListAdapter(Context context, Cursor cursor) {
		super(context, cursor, true);
		mInflater = LayoutInflater.from(context);
	}

	@Override
	public View newView(Context context, Cursor cursor, ViewGroup parent) {
		final View view = mInflater.inflate(R.layout.committees_list_item, parent, false);
		if (AppConstant.isFragmentSupported) {
			view.getLayoutParams().width = DataHolder.calculatedScreenResolution;

		}
		return view;
	}

	@Override
	public void bindView(View view, Context context, Cursor cursor) {
		// Typeface faceGeorgia = Typeface.createFromAsset(context.getAssets(),
		// "fonts/Georgia.ttf");
		// Typeface faceArial = Typeface.createFromAsset(context.getAssets(),
		// "fonts/Arial.ttf");

		TextView title = (TextView) view.findViewById(R.id.title);
		title.setText(TextHelper.customizedFromHtml(cursor.getString(cursor.getColumnIndex(CommitteesDatabaseAdapter.KEY_COURSE))));
		// title.setTypeface(faceGeorgia);

		TextView description = (TextView) view.findViewById(R.id.teaser);
		description.setText(TextHelper.customizedFromHtml(cursor.getString(cursor.getColumnIndex(CommitteesDatabaseAdapter.KEY_TEASER))));
		// description.setTypeface(faceArial);
	}
	
	@Override
	public void finalize() throws Throwable {
		super.finalize();
		mInflater=null;
	}
}