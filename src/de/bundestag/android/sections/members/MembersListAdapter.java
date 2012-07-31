package de.bundestag.android.sections.members;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AlphabetIndexer;
import android.widget.ImageView;
import android.widget.SectionIndexer;
import android.widget.TextView;
import de.bundestag.android.R;
import de.bundestag.android.constant.AppConstant;
import de.bundestag.android.constant.DataHolder;
import de.bundestag.android.helpers.ImageHelper;
import de.bundestag.android.helpers.TextHelper;
import de.bundestag.android.storage.MembersDatabaseAdapter;

/**
 * Members list adapter (list item renderer).
 * 
 * This class knows how to show news elements for a list.
 * 
 * Uses the database cursor to extract the data for the list.
 * 
 * TODOS:
 * 
 * Add section headers
 */
//
public class MembersListAdapter extends CursorAdapter implements SectionIndexer {
	private LayoutInflater mInflater;

	private AlphabetIndexer alphaIndexer;

	public MembersListAdapter(Context context, Cursor cursor) {
		super(context, cursor, true);
		mInflater = LayoutInflater.from(context);

		// TODO - fix this
		alphaIndexer = new AlphabetIndexer(cursor, cursor.getColumnIndex(MembersDatabaseAdapter.KEY_NAME), "ABCDEFGHIJKLMNOPQRSTUVWXYZ");
	}

	@Override
	public View newView(Context context, Cursor cursor, ViewGroup parent) {
		final View view = mInflater.inflate(R.layout.members_list_item, parent, false);
		if (AppConstant.isFragmentSupported) {
			view.getLayoutParams().width = DataHolder.calculatedScreenResolution;
		}
		return view;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (!mDataValid) {
			throw new IllegalStateException("this should only be called when the cursor is valid");
		}
		if (!mCursor.moveToPosition(position)) {
			throw new IllegalStateException("couldn't move cursor to position " + position);
		}
		View v;
		if (convertView == null) {
			v = newView(mContext, mCursor, parent);
		} else {
			v = convertView;
		}
		bindView(v, mContext, mCursor);
		return v;
	}

	@Override
	public void bindView(View view, Context context, Cursor cursor) {
		try {
			// Typeface faceGeorgia =
			// Typeface.createFromAsset(context.getAssets(),
			// "fonts/Georgia.ttf");
			// Typeface faceArial =
			// Typeface.createFromAsset(context.getAssets(),
			// "fonts/Arial.ttf");
			String memberRow = cursor.getString(cursor.getColumnIndex(MembersDatabaseAdapter.KEY_ROWID));
			// if(memberRow>348){
			String memberID = cursor.getString(cursor.getColumnIndex(MembersDatabaseAdapter.KEY_ID));
			// Log.w("memberID",memberID);
			// }
			TextView name = (TextView) view.findViewById(R.id.name);
			name.setText(TextHelper.customizedFromHtml(cursor.getString(cursor.getColumnIndex(MembersDatabaseAdapter.KEY_NAME))));
			// name.setTypeface(faceGeorgia);

			TextView land = (TextView) view.findViewById(R.id.land);
			land.setText(TextHelper.customizedFromHtml(cursor.getString(cursor.getColumnIndex(MembersDatabaseAdapter.KEY_DETAILS_FRACTION)) + " | "
					+ cursor.getString(cursor.getColumnIndex(MembersDatabaseAdapter.KEY_LAND))));
			// land.setTypeface(faceArial);

			// String imageString =
			// cursor.getString(cursor.getColumnIndex(MembersDatabaseAdapter.KEY_PHOTO_STRING));
			MembersDatabaseAdapter memPhoto = new MembersDatabaseAdapter(context);
			memPhoto.open();
			String imageString = memPhoto.fetchMemberPhotoById(memberID);
			ImageView image = (ImageView) view.findViewById(R.id.image);
			if (imageString != null) {
				image.setImageBitmap(ImageHelper.convertStringToBitmap(imageString));
			} else {
				image.setImageBitmap(null);
			}
			memPhoto.close();
			// Cursor curso = memPhoto.fetchMemberFromId(memberID);
		} catch (Exception e) {

		}
	}

	public int getPositionForSection(int section) {
		return alphaIndexer.getPositionForSection(section);
	}

	public int getSectionForPosition(int position) {
		return alphaIndexer.getSectionForPosition(position);
	}

	public Object[] getSections() {
		return alphaIndexer.getSections();
	}

	@Override
	public void finalize() throws Throwable {
		super.finalize();
		mInflater = null;
		alphaIndexer = null;
	}
}