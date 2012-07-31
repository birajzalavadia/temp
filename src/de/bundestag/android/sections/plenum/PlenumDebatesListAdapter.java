package de.bundestag.android.sections.plenum;

import java.util.ArrayList;
import java.util.Date;
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
import de.bundestag.android.helpers.DateHelper;

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
public class PlenumDebatesListAdapter extends BaseAdapter {
	private LayoutInflater mInflater;

	private List<HashMap<String, Object>> items;

	private Context context;

	private static final int TOP_LAYOUT = 0;

	private static final int GENERAL_LAYOUT = 1;

	public PlenumDebatesListAdapter(Context context, ArrayList<HashMap<String, Object>> items) {
		mInflater = LayoutInflater.from(context);

		this.context = context;

		this.items = items;
	}

	public void setItem(ArrayList<HashMap<String, Object>> items){
		this.items=items;
	}
	@Override
	public int getViewTypeCount() {
		return 2;
	}

	@Override
	public int getItemViewType(int position) {
		
		if(AppConstant.isFragmentSupported){
			return GENERAL_LAYOUT;
		}else{
			
			if (position == 0) {
				return TOP_LAYOUT;
			} else {
				return GENERAL_LAYOUT;
			}
		}
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

	public boolean areAllItemsEnabled() {
		return false;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		int type = getItemViewType(position);

		if (type == TOP_LAYOUT) {
			convertView = mInflater.inflate(R.layout.plenum_debates_list_first_item, parent, false);

			if (AppConstant.isFragmentSupported) {

				Date currentDate = new Date();
				holder = new ViewHolder();
				holder.redner_title = (TextView) convertView.findViewById(R.id.redner_title);
				holder.redner_title.setText("Debatten am " + (currentDate.getDate()) + "." + (currentDate.getMonth() + 1) + "." + (currentDate.getYear() + 1900));
				convertView.setTag(holder);
				convertView.getLayoutParams().width = DataHolder.calculatedScreenResolution;
				convertView.getLayoutParams().height = convertView.getLayoutParams().height * 3;

			}
		} else {
			if (convertView == null) {
				convertView = mInflater.inflate(R.layout.plenum_debates_list_item, parent, false);

				if (AppConstant.isFragmentSupported) {
					convertView.getLayoutParams().width = DataHolder.calculatedScreenResolution;
				}
				holder = new ViewHolder();
				holder.name = (TextView) convertView.findViewById(R.id.name);
				holder.status = (TextView) convertView.findViewById(R.id.status);
				holder.start_time = (TextView) convertView.findViewById(R.id.start_time);
				holder.topic = (TextView) convertView.findViewById(R.id.topic);
				holder.funtion = (TextView) convertView.findViewById(R.id.funtion);
				holder.dot = (ImageView) convertView.findViewById(R.id.dot);
				holder.arrow = (ImageView) convertView.findViewById(R.id.arrow);

				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			holder.name.setText((String) items.get(position).get(PlenumDebatesViewHelper.KEY_SPEAKER_NAME));

			String state = (String) items.get(position).get(PlenumDebatesViewHelper.KEY_SPEAKER_STATE);

			if (state.equals("")) {
				holder.dot.setVisibility(View.GONE);
				holder.status.setText("");
			} else if (state.equalsIgnoreCase("live") || state.equalsIgnoreCase("läuft")) {
				holder.dot.setImageDrawable(this.context.getResources().getDrawable(R.drawable.plenum_redner_dot_2));
				holder.dot.setVisibility(View.VISIBLE);
				if (AppConstant.isFragmentSupported) {
					holder.status.setText("Status: läuft");
				} else {
					holder.status.setText("Läuft");
				}
				convertView.setSelected(true);
			} else if (state.equalsIgnoreCase("following") || state.equalsIgnoreCase("folgt")) {
				holder.dot.setImageDrawable(this.context.getResources().getDrawable(R.drawable.plenum_redner_dot_3));
				holder.dot.setVisibility(View.VISIBLE);
				if (AppConstant.isFragmentSupported) {
					holder.status.setText("Status: folgt");
				} else {
					holder.status.setText("Folgt");
				}
			} else if (state.equalsIgnoreCase("previous") || state.equalsIgnoreCase("beendet")) {
				holder.dot.setImageDrawable(this.context.getResources().getDrawable(R.drawable.plenum_redner_dot_1));
				holder.dot.setVisibility(View.VISIBLE);
				if (AppConstant.isFragmentSupported) {
					holder.status.setText("Status: beendet");
				} else {
					holder.status.setText("Beendet");
				}
			}
			String stringDate = (String) items.get(position).get(PlenumDebatesViewHelper.KEY_SPEAKER_START_TIME);
			java.util.Date currentDateTime = DateHelper.parseArticleDate(stringDate);

			String formattedCurrentDateTime = DateHelper.formatPlenumDate(currentDateTime);
			if (AppConstant.isFragmentSupported) {
				holder.start_time.setText(formattedCurrentDateTime + " Uhr");
				holder.topic.setText("Top " + (String) items.get(position).get(PlenumDebatesViewHelper.KEY_SPEAKER_TOPIC));
			} else {
				holder.start_time.setText(formattedCurrentDateTime);
				holder.topic.setText((String) items.get(position).get(PlenumDebatesViewHelper.KEY_SPEAKER_TOPIC));
			}

			String detail_xml = (String) items.get(position).get(PlenumDebatesViewHelper.KEY_DETAIL_XML);
			if (!AppConstant.isFragmentSupported) {
				if (detail_xml.equals("")) {
					holder.arrow.setVisibility(View.INVISIBLE);
				} else {
					holder.arrow.setVisibility(View.VISIBLE);
				}
			}
		}

		return convertView;
	}

	class ViewHolder {
		TextView name;
		TextView status;
		TextView start_time;
		TextView topic;
		TextView funtion;
		ImageView dot;
		ImageView arrow;

		TextView redner_title;

	}

	@Override
	protected void finalize() throws Throwable {
		super.finalize();
		mInflater = null;
		items = null;
		context = null;

	}
}