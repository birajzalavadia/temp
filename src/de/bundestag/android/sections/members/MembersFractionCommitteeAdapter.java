package de.bundestag.android.sections.members;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import de.bundestag.android.R;
import de.bundestag.android.constant.AppConstant;
import de.bundestag.android.helpers.ImageHelper;
import de.bundestag.android.parser.objects.CommitteesObject;
import de.bundestag.android.parser.objects.MembersDetailsObject;
import de.bundestag.android.sections.committees.CommitteesDetailsMemberDetailsActivity;
import de.bundestag.android.sections.committees.CommitteesDetailsMembersActivity;
import de.bundestag.android.storage.MembersDatabaseAdapter;

/**
 * Members fraction list adapter for committee (list item renderer).
 */
public class MembersFractionCommitteeAdapter extends BaseAdapter {
	public static final String KEY_FRACTION_NAME = "FRACTION";

	public static final String KEY_NUMBERS = "NUMBERS";

	public static final String KEY_COMMITTEE = "COMMITTEE";

	public static final String KEY_PRESIDENTS = "PRESIDENTS";

	public static final String KEY_VICEPRESIDENTS = "VICEPRESIDENTS";

	public static final String KEY_IMAGEURL = "IMAGE_URL";

	private LayoutInflater mInflater;

	private List<HashMap<String, Object>> items;

	private Context context;

	private List<MembersDetailsObject> presidents;

	private List<MembersDetailsObject> subPresidents;

	private CommitteesObject committeesObject;

	private int membersCount;

	public MembersFractionCommitteeAdapter(Context context, ArrayList<HashMap<String, Object>> items, List<MembersDetailsObject> presidents,
			List<MembersDetailsObject> subPresidents, CommitteesObject committeesObject, int membersCount) {
		mInflater = LayoutInflater.from(context);
		this.context = context;
		if (!AppConstant.isFragmentSupported) {
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put(KEY_PRESIDENTS, presidents);
			map.put(KEY_VICEPRESIDENTS, subPresidents);
			map.put(KEY_COMMITTEE, committeesObject);
			items.add(0, map);
		}

		this.items = items;

		this.presidents = presidents;

		this.subPresidents = subPresidents;

		this.committeesObject = committeesObject;

		this.membersCount = membersCount;
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
		if (!AppConstant.isFragmentSupported) {
			if (position == 0) {
				if (convertView == null) {
					convertView = mInflater.inflate(R.layout.committees_detail_members_first_item, parent, false);

					holder = new ViewHolder();
					holder.name = (TextView) convertView.findViewById(R.id.name);

					LayoutInflater inflater = (LayoutInflater) convertView.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

					TextView committeeName = (TextView) convertView.findViewById(R.id.title);
					CommitteesObject committeesObject = (CommitteesObject) items.get(position).get(KEY_COMMITTEE);
					committeeName.setText((String) committeesObject.getName() + " (" + membersCount + " Mitglieder)");

					// Typeface faceGeorgia =
					// Typeface.createFromAsset(context.getAssets(),
					// "fonts/Georgia.ttf");
					// committeeName.setTypeface(faceGeorgia);

					List<MembersDetailsObject> vicepresidents = (List<MembersDetailsObject>) items.get(position).get(KEY_VICEPRESIDENTS);
					LinearLayout vicepresident_holder = (LinearLayout) convertView.findViewById(R.id.vicepresident_group);

					if (vicepresidents.size() > 0) {

						for (int i = 0; i < vicepresidents.size(); i++) {
							LinearLayout memberItemLayout = (LinearLayout) inflater.inflate(R.layout.committees_detail_members_first_item_member_item, null);

							TextView memberName = (TextView) memberItemLayout.findViewById(R.id.member_title);
							memberName.setText("Stellv. Vorsitz");

							TextView committeeInfo = (TextView) memberItemLayout.findViewById(R.id.member_info);
							committeeInfo.setText(vicepresidents.get(i).getFirstName() + " " + vicepresidents.get(i).getLastName() + ", " + vicepresidents.get(i).getFraction());

							String imageString = vicepresidents.get(i).getMediaPhotoImageString();
							if (imageString != null) {
								ImageView image = (ImageView) memberItemLayout.findViewById(R.id.image);
								image.setImageBitmap(ImageHelper.convertStringToBitmap(imageString));
							}

							String committeeID = vicepresidents.get(i).getId();
							memberItemLayout.setTag(committeeID);
							memberItemLayout.setOnClickListener(new OnClickListener() {
								@Override
								public void onClick(View view) {
									String memberID = (String) view.getTag();
									showDetailsMember(memberID);
								}
							});
							vicepresident_holder.addView(memberItemLayout);
						}
					} else {
						vicepresident_holder.setVisibility(View.GONE);
					}

					List<MembersDetailsObject> presidents = (List<MembersDetailsObject>) items.get(position).get(KEY_PRESIDENTS);
					LinearLayout president_holder = (LinearLayout) convertView.findViewById(R.id.president_group);

					if (presidents.size() > 0) {
						for (int i = 0; i < presidents.size(); i++) {
							LinearLayout memberItemLayout = (LinearLayout) inflater.inflate(R.layout.committees_detail_members_first_item_member_item, null);

							TextView memberName = (TextView) memberItemLayout.findViewById(R.id.member_title);
							memberName.setText("Vorsitz");

							TextView committeeInfo = (TextView) memberItemLayout.findViewById(R.id.member_info);
							committeeInfo.setText(presidents.get(i).getFirstName() + " " + presidents.get(i).getLastName() + ", " + presidents.get(i).getFraction());

							String imageString = presidents.get(i).getMediaPhotoImageString();
							ImageView image = (ImageView) memberItemLayout.findViewById(R.id.image);
							if (imageString != null) {
								image.setImageBitmap(ImageHelper.convertStringToBitmap(imageString));
							} else {
								image.setImageBitmap(null);
							}

							String committeeID = presidents.get(i).getId();
							memberItemLayout.setTag(committeeID);
							memberItemLayout.setOnClickListener(new OnClickListener() {
								@Override
								public void onClick(View view) {
									String memberID = (String) view.getTag();
									showDetailsMember(memberID);
								}
							});
							president_holder.addView(memberItemLayout);
						}
					} else {
						president_holder.setVisibility(View.GONE);
					}

					convertView.setTag(holder);
				} else {
					holder = (ViewHolder) convertView.getTag();
				}
			} else {

				if (convertView == null) {
					convertView = mInflater.inflate(R.layout.committees_detail_members_fraction_item, parent, false);

					holder = new ViewHolder();
					holder.name = (TextView) convertView.findViewById(R.id.name);

					convertView.setTag(holder);
				} else {
					holder = (ViewHolder) convertView.getTag();
				}

				// Typeface faceGeorgia =
				// Typeface.createFromAsset(context.getAssets(),
				// "fonts/Georgia.ttf");
				// Typeface faceArial =
				// Typeface.createFromAsset(context.getAssets(),
				// "fonts/Arial.ttf");

				holder.name.setText((String) "Mitglieder " + items.get(position).get(KEY_FRACTION_NAME));
				// holder.name.setTypeface(faceGeorgia);
			}
		} else {
			if (convertView == null) {
				convertView = mInflater.inflate(R.layout.committees_detail_members_fraction_item, parent, false);

				holder = new ViewHolder();
				holder.name = (TextView) convertView.findViewById(R.id.name);
				holder.count = (TextView) convertView.findViewById(R.id.count);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			holder.name.setText((String) items.get(position).get(KEY_FRACTION_NAME));
			holder.count.setText((items.get(position).get(KEY_NUMBERS) + " Mitglieder"));
		}
		return convertView;
	}

	public void showDetailsMember(String memberId) {
		Activity activity = (Activity) context;
		MembersDatabaseAdapter membersDatabaseAdapter = new MembersDatabaseAdapter(activity);
		membersDatabaseAdapter.open();
		Cursor memberCursor = membersDatabaseAdapter.fetchMemberFromId(memberId);

		// MembersDetailsObject membersDetailsObject = new
		// MembersDetailsObject();

		int memberIndex = memberCursor.getInt(memberCursor.getColumnIndex(MembersDatabaseAdapter.KEY_ROWID));

		memberCursor.close();
		membersDatabaseAdapter.close();

		Intent intent = new Intent();
		intent.putExtra("index", memberIndex);

		if (activity instanceof CommitteesDetailsMembersActivity) {
			intent.setClass(activity, CommitteesDetailsMemberDetailsActivity.class);
		}
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		context.startActivity(intent);
		activity.overridePendingTransition(0, 0);
	}

	class ViewHolder {
		TextView name;
		TextView count;
	}

	@Override
	protected void finalize() throws Throwable {
		super.finalize();
		mInflater = null;
		items = null;
		context = null;
		presidents = null;
		subPresidents = null;
		committeesObject = null;
	}
}