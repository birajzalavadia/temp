package de.bundestag.android.sections.plenum;

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
public class PlenumSpeakersListAdapter extends BaseAdapter {
	private LayoutInflater mInflater;

	private List<HashMap<String, Object>> items;

	private Context context;

	private static final int TOP_LAYOUT = 0;

	private static final int GENERAL_LAYOUT = 1;
	
	public void setItem(ArrayList<HashMap<String, Object>> items){
		this.items=items;
	}

	public PlenumSpeakersListAdapter(Context context, ArrayList<HashMap<String, Object>> items) {
		mInflater = LayoutInflater.from(context);

		this.context = context;

		this.items = items;
	}

	@Override
	public int getViewTypeCount() {
		return 2;
	}

	@Override
	public int getItemViewType(int position) {
		// if (AppConstant.isFragmentSupported) {
		// return GENERAL_LAYOUT;
		// } else {

		if (AppConstant.isFragmentSupported) {
			return GENERAL_LAYOUT;
		} else {
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

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		int type = getItemViewType(position);

		if (type == TOP_LAYOUT) {
			convertView = mInflater.inflate(R.layout.plenum_speaker_list_first_item, parent, false);

			if (AppConstant.isFragmentSupported) {

				holder = new ViewHolder();
				holder.txtHeader = (TextView) convertView.findViewById(R.id.txtHeader);
				try {

					holder.txtHeader.setText("Redner\n" + (DataHolder.txtAgenda.getText().toString().split(":")[1]).trim());
				} catch (Exception e) {
				}
				convertView.getLayoutParams().width = DataHolder.calculatedScreenResolution;
				convertView.getLayoutParams().height = convertView.getLayoutParams().height * 3;
			}
		} else {
			if (convertView == null) {
				convertView = mInflater.inflate(R.layout.plenum_speaker_list_item, parent, false);

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

				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			if(AppConstant.isFragmentSupported){
				holder.name.setText((String) items.get(position).get(PlenumSpeakersViewHelper.KEY_SPEAKER_NAME) + ", "+ (String) items.get(position).get(PlenumSpeakersViewHelper.KEY_SPEAKER_FUNCTION));
			}else{
				holder.name.setText((String) items.get(position).get(PlenumSpeakersViewHelper.KEY_SPEAKER_NAME));
			}
			// holder.name.setTypeface(faceArial);

			String state = (String) items.get(position).get(PlenumSpeakersViewHelper.KEY_SPEAKER_STATE);

			// holder.status.setTypeface(faceArial);
			if (state.equals("")) {
				holder.dot.setVisibility(View.GONE);
				holder.status.setText("");
			} else if (state.equalsIgnoreCase("live") || state.equalsIgnoreCase("läuft")) {
				holder.dot.setImageDrawable(this.context.getResources().getDrawable(R.drawable.plenum_redner_dot_2));
				holder.dot.setVisibility(View.VISIBLE);
				holder.status.setText("läuft");
				// convertView.setSelected(position);
				// convertView.setSelected(true);
				// setSelectionFromTop(position, 0);
			} else if (state.equalsIgnoreCase("following") || state.equalsIgnoreCase("folgt")) {
				holder.dot.setImageDrawable(this.context.getResources().getDrawable(R.drawable.plenum_redner_dot_3));
				holder.dot.setVisibility(View.VISIBLE);
				holder.status.setText("folgt");
			} else if (state.equalsIgnoreCase("previous") || state.equalsIgnoreCase("beendet")) {
				holder.dot.setImageDrawable(this.context.getResources().getDrawable(R.drawable.plenum_redner_dot_1));
				holder.dot.setVisibility(View.VISIBLE);
				holder.status.setText("beendet");
			}
			String stringDate = (String) items.get(position).get(PlenumSpeakersViewHelper.KEY_SPEAKER_START_TIME);
			java.util.Date currentDateTime = DateHelper.parseArticleDate(stringDate);

			String formattedCurrentDateTime = DateHelper.formatPlenumDate(currentDateTime);

			if(AppConstant.isFragmentSupported){
				holder.start_time.setText(formattedCurrentDateTime + " Uhr");
			}else{
				holder.start_time.setText(formattedCurrentDateTime);
			}
			// holder.start_time.setTypeface(faceArial);

			holder.topic.setText((String) items.get(position).get(PlenumSpeakersViewHelper.KEY_SPEAKER_TOPIC));
			// holder.topic.setTypeface(faceArial);
			if (!AppConstant.isFragmentSupported) {
				holder.funtion.setText((String) items.get(position).get(PlenumSpeakersViewHelper.KEY_SPEAKER_FUNCTION));
			}
			// holder.funtion.setTypeface(faceArial);
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
		TextView txtHeader;
	}

	@Override
	protected void finalize() throws Throwable {
		super.finalize();
		mInflater = null;
		items = null;
		context = null;
	}
}