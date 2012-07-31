package de.bundestag.android.sections.members;

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
import de.bundestag.android.constant.AppConstant;
import de.bundestag.android.constant.DataHolder;

/**
 * Members fraction list adapter (list item renderer).
 */
public class MembersFractionListAdapter extends BaseAdapter {
	public static final String KEY_FRACTION_NAME = "FRACTION";

	public static final String KEY_NUMBERS = "NUMBERS";

	public static final String KEY_IMAGEURL = "IMAGE_URL";

	private LayoutInflater mInflater;

	private List<HashMap<String, Object>> items;

	private Context context;

	public MembersFractionListAdapter(Context context, ArrayList<HashMap<String, Object>> items) {
		mInflater = LayoutInflater.from(context);

		this.context = context;

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
			if (AppConstant.isFragmentSupported) {
				convertView.getLayoutParams().width = DataHolder.calculatedScreenResolution;

			}
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

		holder.name.setText((String) items.get(position).get(KEY_FRACTION_NAME));
		// holder.name.setTypeface(faceGeorgia);

		holder.number.setText((String) items.get(position).get(KEY_NUMBERS) + " Mitglieder");
		// holder.number.setTypeface(faceArial);

//		holder.image.setImageResource(context.getResources().getIdentifier("drawable/" + items.get(position).get(KEY_IMAGEURL), null, context.getPackageName()));

		return convertView;
	}

	class ViewHolder {
		ImageView image;
		TextView name;
		TextView number;
	}

	@Override
	protected void finalize() throws Throwable {
		super.finalize();
		mInflater = null;
		items = null;
		context = null;
	}
}