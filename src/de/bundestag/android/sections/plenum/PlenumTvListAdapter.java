package de.bundestag.android.sections.plenum;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.TextView;
import de.bundestag.android.R;
import de.bundestag.android.constant.DataHolder;
import de.bundestag.android.helpers.Base64;

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
public class PlenumTvListAdapter extends BaseAdapter {
	private LayoutInflater mInflater;

	private List<HashMap<String, Object>> items;

	private Context context;

	private static final int TOP_LAYOUT = 0;

	private static final int GENERAL_LAYOUT = 1;
	List<String> output;
	private ListView lst;
	private int currentCheckedId = 0;
	private int noOfDisableTabs = 0;
	int currentCheckedIndex = -1;

	public void setListItemData(ArrayList<HashMap<String, Object>> items) {
		this.items = items;
	}

	public PlenumTvListAdapter(Context context, ArrayList<HashMap<String, Object>> items, ListView lst) {
		mInflater = LayoutInflater.from(context);

		this.context = context;

		this.items = items;
		output = new ArrayList<String>();
		this.lst = lst;
		getCalendar();
	}

	@Override
	public int getViewTypeCount() {
		return 1;
	}

	@Override
	public int getItemViewType(int position) {

		// if (position == 0) {
		// return TOP_LAYOUT;
		// } else {
		return GENERAL_LAYOUT;
		// }
		// }
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

		} else {
			if (convertView == null) {
				convertView = mInflater.inflate(R.layout.plenum_tv_list_item, parent, false);

				convertView.getLayoutParams().width = DataHolder.calculatedScreenResolution;
				holder = new ViewHolder();
				holder.time = (TextView) convertView.findViewById(R.id.time);
				holder.title = (TextView) convertView.findViewById(R.id.title);
				holder.longTitle = (TextView) convertView.findViewById(R.id.longTitle);
				holder.recordingDate = (TextView) convertView.findViewById(R.id.recordingDate);

				//
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			String time = items.get(position).get("dispatchDate").toString();
			time = time.substring(time.length() - 4, time.length());
			time = time.substring(0, 2) + ":" + time.substring(2, 4);
			holder.time.setText(time + " Uhr");
			
			holder.title.setText(Html.fromHtml(items.get(position).get("longTitle").toString()));
			holder.longTitle.setText(Html.fromHtml(items.get(position).get("info").toString()));
			holder.recordingDate.setText("(Aufzeichnung vom " + items.get(position).get("recodingDate").toString() + ")");

			convertView.setTag(holder);

		}

		return convertView;
	}

	class ViewHolder {
		TextView time;
		TextView title;
		TextView longTitle;
		TextView recordingDate;
	}

	class FirstItemViewHolder {
		TextView mainTitle;
		TextView dateTitle;
		RadioGroup tvWeek;
	}

	private List<String> getCalendar() {

		Calendar c = Calendar.getInstance();
		String month = ("" + (c.getTime().getMonth() + 1)).length() == 1 ? ("0" + (c.getTime().getMonth() + 1)) : ("" + (c.getTime().getMonth() + 1));
		String date = ("" + (c.getTime().getDate())).length() == 1 ? ("0" + (c.getTime().getDate())) : ("" + (c.getTime().getDate()));
		output.add("" + (c.getTime().getYear() + 1900) + "-" + month + "-" + date);
		for (int x = new Date().getDay(); x < (new Date().getDay() + 6); x++) {
			c.add(Calendar.DATE, 1);
			month = ("" + (c.getTime().getMonth() + 1)).length() == 1 ? ("0" + (c.getTime().getMonth() + 1)) : ("" + (c.getTime().getMonth() + 1));
			date = ("" + (c.getTime().getDate())).length() == 1 ? ("0" + (c.getTime().getDate())) : ("" + (c.getTime().getDate()));
			output.add("" + (c.getTime().getYear() + 1900) + "-" + month + "-" + date);
		}
		return output;
	}

	@Override
	public void finalize() throws Throwable {
		super.finalize();
		mInflater = null;

		items = null;

		context = null;
	}
}