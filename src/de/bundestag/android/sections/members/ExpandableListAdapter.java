package de.bundestag.android.sections.members;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import de.bundestag.android.R;
import de.bundestag.android.helpers.ImageHelper;
import de.bundestag.android.helpers.TextHelper;
import de.bundestag.android.storage.MembersDatabaseAdapter;

public class ExpandableListAdapter extends BaseExpandableListAdapter {

	@Override
	public boolean areAllItemsEnabled() {
		return true;
	}

	public static final String KEY_FRACTION_NAME = "FRACTION";

	public static final String KEY_NUMBERS = "NUMBERS";

	public static final String KEY_IMAGEURL = "IMAGE_URL";
	ArrayList<HashMap<String, Object>> fractions;
	private Context context;
	private LayoutInflater mInflater;
	Activity activity;
	// HashMap<Integer, Object> members_list = new HashMap<Integer, Object>() ;
	int child_length = 1;
	ArrayList<HashMap<Integer, MembersDetailsExpandable>> memberList;

	public ExpandableListAdapter(Context context,
			ArrayList<HashMap<String, Object>> fractions, Activity activity,
			ArrayList<HashMap<Integer, MembersDetailsExpandable>> memberList) {
		this.context = context;
		this.fractions = fractions;
		this.activity = activity;
		this.memberList = memberList;
	}

	/**
	 * A general add method, that allows you to add a Vehicle to this list
	 * 
	 * Depending on if the category opf the vehicle is present or not, the
	 * corresponding item will either be added to an existing group if it
	 * exists, else the group will be created and then the item will be added
	 * 
	 * @param vehicle
	 */

	@Override
	public Object getChild(int groupPosition, int childPosition) {
		return memberList.get(groupPosition);
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return childPosition;
	}

	// Return a child view. You can load your custom layout here.
	@Override
	public View getChildView(int groupPosition, int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {

		ViewHolderMember holder;

		if (convertView == null) {
			LayoutInflater infalInflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = infalInflater.inflate(R.layout.members_list_item,
					null);
//			if (AppConstant.isFragmentSupported) {
//				convertView.getLayoutParams().width=DataHolder.calculatedScreenResolution;
//
//			}

			// convertView =
			// mInflater.inflate(R.layout.members_fraction_list_item, parent,
			// false);

			holder = new ViewHolderMember();
			holder.name = (TextView) convertView.findViewById(R.id.name);
			holder.land = (TextView) convertView.findViewById(R.id.land);
			holder.image = (ImageView) convertView.findViewById(R.id.image);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolderMember) convertView.getTag();
		}

		MembersDetailsExpandable mDE = null;
		mDE = new MembersDetailsExpandable();
		HashMap<Integer, MembersDetailsExpandable> temp = new HashMap<Integer, MembersDetailsExpandable>();
		temp = memberList.get(groupPosition);
		mDE = temp.get(childPosition);
		String memberRow = mDE.getKey_rowId();
		String memberID = mDE.getKey_id();
		holder.name.setText(TextHelper.customizedFromHtml(mDE.getKey_name()));
		holder.land.setText(TextHelper.customizedFromHtml(mDE.getKey_land()));
		MembersDatabaseAdapter memPhoto = new MembersDatabaseAdapter(context);
		memPhoto.open();
		String imageString = memPhoto.fetchMemberPhotoById(memberID);

		if (imageString != null) {
			holder.image.setImageBitmap(ImageHelper
					.convertStringToBitmap(imageString));
		} else {
			holder.image.setImageBitmap(null);
		}
		memPhoto.close();
	
		return convertView;
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		return memberList.get(groupPosition).size();
	}

	@Override
	public Object getGroup(int groupPosition) {
		return fractions.get(groupPosition);
	}

	@Override
	public int getGroupCount() {
		return fractions.size();
	}

	@Override
	public long getGroupId(int groupPosition) {
		return groupPosition;
	}

	// Return a group view. You can load your custom layout here.
	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {
		ViewHolder holder;

		if (convertView == null) {
			LayoutInflater infalInflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = infalInflater.inflate(
					R.layout.members_fraction_list_item, null);
//
//			if (AppConstant.isFragmentSupported) {
//				convertView.getLayoutParams().width=DataHolder.calculatedScreenResolution;
//
//			}
			// convertView =
			// mInflater.inflate(R.layout.members_fraction_list_item, parent,
			// false);

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

		holder.name.setText((String) fractions.get(groupPosition).get(
				KEY_FRACTION_NAME));
		// holder.name.setTypeface(faceGeorgia);

		holder.number.setText((String) fractions.get(groupPosition).get(
				KEY_NUMBERS)
				+ " Mitglieder");
		// holder.number.setTypeface(faceArial);

		holder.image.setImageResource(context.getResources().getIdentifier(
				"drawable/" + fractions.get(groupPosition).get(KEY_IMAGEURL),
				null, context.getPackageName()));

		return convertView;
	}

	@Override
	public boolean hasStableIds() {
		return true;
	}

	@Override
	public boolean isChildSelectable(int arg0, int arg1) {
		return true;
	}

	class ViewHolder {
		ImageView image;
		TextView name;
		TextView number;
	}

	class ViewHolderMember {
		ImageView image;
		TextView name;
		TextView land;
	}
}
