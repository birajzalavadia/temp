package de.bundestag.android.sections.members;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import de.bundestag.android.R;

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
public class MembersElectionListAdapter extends BaseAdapter {
	public static final String KEY_FRACTION_NAME = "FRACTION";

	public static final String KEY_NUMBERS = "NUMBERS";

	private LayoutInflater mInflater;

	private List<HashMap<String, Object>> items;

	public MembersElectionListAdapter(Context context, ArrayList<HashMap<String, Object>> items) {
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
			convertView = mInflater.inflate(R.layout.members_election_list_item, parent, false);

			holder = new ViewHolder();
			holder.name = (TextView) convertView.findViewById(R.id.name);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		// Typeface faceGeorgia = Typeface.createFromAsset(context.getAssets(),
		// "fonts/Georgia.ttf");

		holder.name.setText((String) items.get(position).get(KEY_FRACTION_NAME));
		// holder.name.setTypeface(faceGeorgia);

		return convertView;
	}

	class ViewHolder {
		TextView name;
	}

	@Override
	protected void finalize() throws Throwable {
		super.finalize();
		mInflater = null;
		items = null;
	}
}