package de.bundestag.android.sections.committees;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import de.bundestag.android.R;

/**
 * Committees list adapter (list item renderer).
 * 
 * This class knows how to show news elements for a list.
 * 
 * Uses the database cursor to extract the data for the list.
 */
public class CommitteesDetailsMembersListAdapter extends BaseAdapter {
	private LayoutInflater mInflater;

	private List<HashMap<String, Object>> items;

	public CommitteesDetailsMembersListAdapter(Context context, ArrayList<HashMap<String, Object>> items) {
		mInflater = LayoutInflater.from(context);

		this.items = items;
	}

	@Override
	public int getCount() {
		return items.size();
	}

	@Override
	public Object getItem(int position) {
		return items.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;

		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.members_fraction_list_item, parent, false);

			holder = new ViewHolder();
			holder.name = (TextView) convertView.findViewById(R.id.name);
			holder.number = (TextView) convertView.findViewById(R.id.number);
			holder.image = (ImageView) convertView.findViewById(R.id.image);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		// Typeface faceGeorgia = Typeface.createFromAsset(context.getAssets(),
		// "fonts/Georgia.ttf");
		// Typeface faceArial = Typeface.createFromAsset(context.getAssets(),
		// "fonts/Arial.ttf");

		// holder.name.setText((String)
		// items.get(position).get(KEY_FRACTION_NAME));
		// holder.name.setTypeface(faceGeorgia);

		return convertView;
	}

	class ViewHolder {
		ImageView image;
		TextView name;
		TextView number;
	}

	// @Override
	// public View newView(Context context, Cursor cursor, ViewGroup parent)
	// {
	// final View view;
	//
	// int type = getItemViewType(cursor.getPosition());
	// if (type == TOP_LAYOUT)
	// {
	// view = mInflater.inflate(R.layout.committees_detail_members_first_item,
	// parent, false);
	// }
	// else
	// {
	// view = mInflater.inflate(R.layout.committees_detail_members_item, parent,
	// false);
	// }
	//
	// return view;
	// }

	// @Override
	// public void bindView(View view, Context context, Cursor cursor)
	// {
	// TextView name = (TextView) view.findViewById(R.id.title);
	// name.setText(TextHelper.customizedFromHtml(cursor.getString(cursor.getColumnIndex(CommitteesDatabaseAdapter.KEY_NEWS_TITLE))));
	// name.setTypeface(faceGeorgia);
	//
	// // TextView fraction = (TextView) view.findViewById(R.id.fraction);
	// // fraction.setText("CSU | Bayern");
	// // fraction.setTypeface(faceArial);
	// }

	@Override
	protected void finalize() throws Throwable {
		super.finalize();
		mInflater = null;

		items = null;
	}
}