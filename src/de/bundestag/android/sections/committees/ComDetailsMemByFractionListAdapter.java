package de.bundestag.android.sections.committees;

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
import de.bundestag.android.constant.DataHolder;
import de.bundestag.android.helpers.ImageHelper;
import de.bundestag.android.helpers.TextHelper;
import de.bundestag.android.parser.objects.CommitteesObject;
import de.bundestag.android.parser.objects.MembersDetailsObject;
import de.bundestag.android.storage.MembersDatabaseAdapter;

/**
 * Members fraction list adapter for committee (list item renderer).
 */
public class ComDetailsMemByFractionListAdapter extends BaseAdapter {
	private static final int TOP_LAYOUT = 0;

	private static final int GENERAL_LAYOUT = 1;

	private static final int SEPARATOR_LAYOUT = 2;

	public static final String KEY_COMMITTEE = "COMMITTEE";

	public static final String KEY_PRESIDENTS = "PRESIDENTS";

	public static final String KEY_VICEPRESIDENTS = "VICEPRESIDENTS";

	public static final String KEY_FRACTIONNAME = "FRACTIONAME";

	public static final String KEY_IMAGEURL = "IMAGE_URL";

	private LayoutInflater mInflater;

	private List<HashMap<String, Object>> items;

	private final Context context;

	private List<MembersDetailsObject> presidents;

	private List<MembersDetailsObject> subPresidents;

	private CommitteesObject committeesObject;

	private int separatorCount = -1;

	private String fractionName;

	int commiteeMembersCount = 0;

	public ComDetailsMemByFractionListAdapter(Context context, List<HashMap<String, Object>> items, int separatorCount, List<MembersDetailsObject> presidents,
			List<MembersDetailsObject> subPresidents, CommitteesObject committeesObject, String committeeName, int commiteeMembersCount) {
		mInflater = LayoutInflater.from(context);

		this.context = context;

		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put(KEY_PRESIDENTS, presidents);
		map.put(KEY_VICEPRESIDENTS, subPresidents);
		map.put(KEY_COMMITTEE, committeesObject);
		map.put(KEY_FRACTIONNAME, committeeName);
		items.add(0, map);

		this.fractionName = committeeName;

		this.items = items;

		this.presidents = presidents;

		this.subPresidents = subPresidents;

		this.committeesObject = committeesObject;

		this.separatorCount = separatorCount;

		this.commiteeMembersCount = commiteeMembersCount;
	}

	@Override
	public int getViewTypeCount() {
		return 3;
	}

	@Override
	public int getItemViewType(int position) {
		if (position == 0) {
			return TOP_LAYOUT;
		} else if (position == (separatorCount + 1)) {
			return SEPARATOR_LAYOUT;
		} else {
			return GENERAL_LAYOUT;
		}
	}

	public int getCount() {
		return items.size();
	}

	public Object getItem(int position) {
		return items.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		int type = getItemViewType(position);

		if (type == TOP_LAYOUT) {

			if (AppConstant.isFragmentSupported) {
				if (convertView == null) {
					convertView = mInflater.inflate(R.layout.committees_detail_members_separator, parent, false);

					holder = new ViewHolder();
					holder.name = (TextView) convertView.findViewById(R.id.name);
					LinearLayout fraction_name_holder = (LinearLayout) convertView.findViewById(R.id.fraction_name_holder);
					TextView fraction_name = (TextView) convertView.findViewById(R.id.sub_fraction_name);
					fraction_name.setText("Mitglieder " + (String) items.get(position).get(KEY_FRACTIONNAME));
					convertView.setTag(holder);

					if (fractionName.trim().length() <= 0) {
						fraction_name_holder.setVisibility(View.GONE);
						fraction_name.setVisibility(View.GONE);
					}
				} else {
					holder = (ViewHolder) convertView.getTag();
				}
				convertView.getLayoutParams().width = DataHolder.calculatedScreenResolution;
				return convertView;
			}
			if (convertView == null) {
				convertView = mInflater.inflate(R.layout.committees_detail_members_first_item, parent, false);

				holder = new ViewHolder();
				holder.name = (TextView) convertView.findViewById(R.id.name);

				LayoutInflater inflater = (LayoutInflater) convertView.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

				TextView committeeName = (TextView) convertView.findViewById(R.id.title);
				CommitteesObject committeesObject = (CommitteesObject) items.get(position).get(KEY_COMMITTEE);
				committeeName.setText((String) committeesObject.getName() + " (" + this.commiteeMembersCount + " Mitglieder)");
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
						ImageView image = (ImageView) memberItemLayout.findViewById(R.id.image);
						if (imageString != null) {
							image.setImageBitmap(ImageHelper.convertStringToBitmap(imageString));
						} else {
							image.setImageBitmap(null);
						}

						String committeeID = vicepresidents.get(i).getId();
						memberItemLayout.setTag(committeeID);
						memberItemLayout.setOnClickListener(new OnClickListener() {
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
						if (imageString != null) {
							ImageView image = (ImageView) memberItemLayout.findViewById(R.id.image);
							image.setImageBitmap(ImageHelper.convertStringToBitmap(imageString));
						}

						String committeeID = presidents.get(i).getId();
						memberItemLayout.setTag(committeeID);
						memberItemLayout.setOnClickListener(new OnClickListener() {
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
				LinearLayout fraction_name_holder = (LinearLayout) convertView.findViewById(R.id.fraction_name_holder);
				fraction_name_holder.setVisibility(View.VISIBLE);

				TextView fraction_name = (TextView) fraction_name_holder.findViewById(R.id.fraction_name);
				fraction_name.setText("Mitglieder " + (String) items.get(position).get(KEY_FRACTIONNAME));
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
		} else if (type == SEPARATOR_LAYOUT) {
			if (convertView == null) {
				convertView = mInflater.inflate(R.layout.committees_detail_members_separator, parent, false);

				holder = new ViewHolder();
				holder.name = (TextView) convertView.findViewById(R.id.name);

				TextView fraction_name = (TextView) convertView.findViewById(R.id.sub_fraction_name);
				fraction_name.setText("Stellvertretende Mitglieder " + this.fractionName);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
		} else
		// general layout
		{
			if (convertView == null) {
				convertView = mInflater.inflate(R.layout.members_list_item, parent, false);

				holder = new ViewHolder();
				holder.name = (TextView) convertView.findViewById(R.id.name);
				holder.land = (TextView) convertView.findViewById(R.id.land);
				holder.image = (ImageView) convertView.findViewById(R.id.image);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
				if (holder.name == null) {
					holder.name = (TextView) convertView.findViewById(R.id.name);
				}
				if (holder.land == null) {
					holder.land = (TextView) convertView.findViewById(R.id.land);
				}
				if (holder.image == null) {
					holder.image = (ImageView) convertView.findViewById(R.id.image);
				}
			}

			// Typeface faceGeorgia =
			// Typeface.createFromAsset(context.getAssets(),
			// "fonts/Georgia.ttf");
			// Typeface faceArial =
			// Typeface.createFromAsset(context.getAssets(), "fonts/Arial.ttf");
			int pos = position;

			HashMap<String, Object> hashMap = items.get(position);

			holder.name.setText(TextHelper.customizedFromHtml((String) hashMap.get(MembersDatabaseAdapter.KEY_NAME)));

			// holder.name.setTypeface(faceGeorgia);

			holder.land.setText(TextHelper.customizedFromHtml((String) items.get(position).get(MembersDatabaseAdapter.KEY_DETAILS_FRACTION) + " | "
					+ items.get(position).get(MembersDatabaseAdapter.KEY_LAND)));
			// holder.land.setTypeface(faceArial);

			String imageString = (String) items.get(position).get(MembersDatabaseAdapter.KEY_DETAILS_MEDIAPHOTOSTRING);
			if (imageString != null) {
				holder.image.setImageBitmap(ImageHelper.convertStringToBitmap(imageString));
			}
		}

		if (AppConstant.isFragmentSupported) {
			convertView.getLayoutParams().width = DataHolder.calculatedScreenResolution;
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

		if (activity instanceof CommitteesDetailsMembersByFractionActivity) {
			intent.setClass(activity, CommitteesDetailsMemberDetailsActivity.class);
		}
		context.startActivity(intent);
		activity.overridePendingTransition(0, 0);
	}

	class ViewHolder {
		TextView name;
		TextView land;
		ImageView image;
	}

	@Override
	public void finalize() throws Throwable {
		super.finalize();
		
		mInflater = null;

		items = null;

		presidents = null;

		subPresidents = null;

		committeesObject = null;

		fractionName = null;

	}
}