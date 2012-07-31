package de.bundestag.android.sections.members;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import de.bundestag.android.BaseActivity;
import de.bundestag.android.R;
import de.bundestag.android.constant.DataHolder;
import de.bundestag.android.helpers.ImageHelper;
import de.bundestag.android.helpers.TextHelper;
import de.bundestag.android.parser.objects.MembersDetailsGroupsObject;
import de.bundestag.android.parser.objects.MembersDetailsObject;
import de.bundestag.android.sections.committees.CommitteesDetailsNewsActivity;

public class MembersDetailsCommitteesViewHelper {
	private static final String COORDINATE_TITLE = "Obfrau/Obmann";
	private static final String MEMBER_TITLE = "Stellvertretendes Mitglied";
	private static final String FULLMEMBER_TITLE = "Ordentliches Mitglied";
	private static final String SUBSTITUTE_TITLE = "Mitgliedschaften in sonstigen Gremien";
	private static final String PRESIDENT_TITLE = "Vorsitz";
	private static final String SUB_PRESIDENT_TITLE = "Stellvertretender Vorsitz";

	public static void createDetailsView(MembersDetailsObject membersDetailsObject, final FragmentActivity activity, final Activity mainActivity, final int memberRowId) {
		// Typeface faceArial = Typeface.createFromAsset(activity.getAssets(),
		// "fonts/Arial.ttf");
		// Typeface faceArialBold =
		// Typeface.createFromAsset(activity.getAssets(),
		// "fonts/ArialBold.ttf");
		// Typeface faceGeorgia = Typeface.createFromAsset(activity.getAssets(),
		// "fonts/Georgia.ttf");

		// name, party, occupation, status
		String imageString = membersDetailsObject.getMediaPhotoImageString();
		ImageView image = (ImageView) activity.findViewById(R.id.image);
		if (imageString != null) {
			image.setImageBitmap(ImageHelper.convertStringToBitmap(imageString));

			// TODO - add click for more details
			TextView copyright = (TextView) activity.findViewById(R.id.copy);
			copyright.setText(TextHelper.customizedFromHtml("\u00A9 " + membersDetailsObject.getMediaPhotoCopyright()));
			// copyright.setTypeface(faceArial);
		} else {
			image.setImageBitmap(null);
		}

		TextView name = (TextView) activity.findViewById(R.id.name);
		name.setText(TextHelper.customizedFromHtml(membersDetailsObject.getCourse() + " " + membersDetailsObject.getFirstName() + " " + membersDetailsObject.getTitle() + " "
				+ membersDetailsObject.getLastName() + ",<br>" + membersDetailsObject.getFraction()));
		// name.setTypeface(faceGeorgia);

		TextView occupation = (TextView) activity.findViewById(R.id.occupation);
		occupation.setText(TextHelper.customizedFromHtml(membersDetailsObject.getProfession()));
		// occupation.setTypeface(faceArialBold);

		TextView status = (TextView) activity.findViewById(R.id.status);

		String statusText = "";
		if (membersDetailsObject.getStatus().equals("Verstorben")) {
			statusText = "Verstorben am " + membersDetailsObject.getExitDate();
		} else if (membersDetailsObject.getStatus().equals("Ausgeschieden")) {
			statusText = "Ausgeschieden am " + membersDetailsObject.getExitDate();
		} else if (membersDetailsObject.getStatus().equals("Aktiv")) {
			if (membersDetailsObject.getElected().equals("Landesliste")) {
				statusText = "Gewählt über die Landesliste " + membersDetailsObject.getCity();
			} else if (membersDetailsObject.getElected().equals("direkt")) {
				statusText = "Direkt gewählt im Wahlkreis " + membersDetailsObject.getElectionName();
			}

		}
		status.setText(TextHelper.customizedFromHtml(statusText));
		// status.setTypeface(faceArial);

		TextView title = (TextView) activity.findViewById(R.id.title);
		title.setText("Mitgliedschaften in Gremien des Bundestages");
		// title.setTypeface(faceGeorgia);

		Context context = activity.getApplicationContext();
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		// president member
		List<MembersDetailsGroupsObject> sub_president_groups = membersDetailsObject.getSubPresidentGroups();
		if (sub_president_groups.size() > 0) {
			TextView president_member = (TextView) activity.findViewById(R.id.sub_president_member);
			president_member.setText(SUB_PRESIDENT_TITLE);
			// president_member.setTypeface(faceGeorgia);
			LinearLayout coordinateHolder = (LinearLayout) activity.findViewById(R.id.sub_president_member_committees);

			for (int i = 0; i < sub_president_groups.size(); i++) {
				LinearLayout committeeItemLayout = (LinearLayout) inflater.inflate(R.layout.members_detail_committees_item, null);
				TextView committeeName = (TextView) committeeItemLayout.findViewById(R.id.committeeName);
				committeeName.setText(sub_president_groups.get(i).getGroupName());

				LinearLayout committee = (LinearLayout) committeeItemLayout.findViewById(R.id.committee);

				String committeeID = sub_president_groups.get(i).getGroupId();

				if (committeeID != null) {

					committee.setTag(committeeID);
					committee.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View view) {
							String committeeId = (String) view.getTag();
							Intent contactIntent;
							if (DataHolder.isOnline(((BaseActivity) mainActivity))) {
								contactIntent = new Intent(view.getContext(), MembersCommitteesDetailsNewsActivity.class);
								contactIntent.putExtra("index", memberRowId);
								contactIntent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
							} else {
								contactIntent = new Intent(view.getContext(), MembersCommitteesDetailsTasksActivity.class);
							}
							contactIntent.putExtra(CommitteesDetailsNewsActivity.KEY_COMMITTEE_ID, committeeId);
							activity.startActivity(contactIntent);
							activity.overridePendingTransition(0, 0);
						}
					});
				} else if (sub_president_groups.get(i).getGroupURL() != null && !sub_president_groups.get(i).getGroupURL().equals("")) {
					committee.setTag(sub_president_groups.get(i).getGroupURL());
					committee.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View view) {
							String committeeUrl = (String) view.getTag();
							Uri uriUrl = Uri.parse(committeeUrl);
							Intent launchBrowser = new Intent(Intent.ACTION_VIEW, uriUrl);
							activity.startActivity(launchBrowser);
						}

					});
				} else {
					ImageView committeeArrow = (ImageView) committeeItemLayout.findViewById(R.id.arrow);
					committeeArrow.setVisibility(View.GONE);
				}
				coordinateHolder.addView(committeeItemLayout);
			}
		} else {
			LinearLayout sub_president_group = (LinearLayout) activity.findViewById(R.id.sub_president_group);
			sub_president_group.setVisibility(View.GONE);
		}

		// president member
		List<MembersDetailsGroupsObject> president_groups = membersDetailsObject.getPresidentGroups();
		if (president_groups.size() > 0) {
			TextView president_member = (TextView) activity.findViewById(R.id.president_member);
			president_member.setText(PRESIDENT_TITLE);
			// president_member.setTypeface(faceGeorgia);
			LinearLayout coordinateHolder = (LinearLayout) activity.findViewById(R.id.president_member_committees);

			for (int i = 0; i < president_groups.size(); i++) {
				LinearLayout committeeItemLayout = (LinearLayout) inflater.inflate(R.layout.members_detail_committees_item, null);
				TextView committeeName = (TextView) committeeItemLayout.findViewById(R.id.committeeName);
				committeeName.setText(president_groups.get(i).getGroupName());

				LinearLayout committee = (LinearLayout) committeeItemLayout.findViewById(R.id.committee);

				String committeeID = president_groups.get(i).getGroupId();
				if (committeeID != null) {

					committee.setTag(committeeID);
					committee.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View view) {
							String committeeId = (String) view.getTag();
							// Intent contactIntent = new
							// Intent(view.getContext(),
							// CommitteesDetailsNewsActivity.class);
							Intent contactIntent;
							if (DataHolder.isOnline(((BaseActivity) mainActivity))) {
								contactIntent = new Intent(view.getContext(), MembersCommitteesDetailsNewsActivity.class);
								contactIntent.putExtra("index", memberRowId);
								contactIntent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
							} else {
								contactIntent = new Intent(view.getContext(), MembersCommitteesDetailsTasksActivity.class);
							}
							contactIntent.putExtra(CommitteesDetailsNewsActivity.KEY_COMMITTEE_ID, committeeId);
							activity.startActivity(contactIntent);
							activity.overridePendingTransition(0, 0);
						}
					});
				} else {
					ImageView committeeArrow = (ImageView) committeeItemLayout.findViewById(R.id.arrow);
					committeeArrow.setVisibility(View.GONE);
				}
				coordinateHolder.addView(committeeItemLayout);
			}
		} else {
			LinearLayout president_group = (LinearLayout) activity.findViewById(R.id.president_group);
			president_group.setVisibility(View.GONE);
		}

		// coordinate member
		List<MembersDetailsGroupsObject> coordinate_groups = membersDetailsObject.getCoordinateGroups();
		if (coordinate_groups.size() > 0) {
			TextView coordinate_member = (TextView) activity.findViewById(R.id.coordinate_member);
			coordinate_member.setText(COORDINATE_TITLE);
			// coordinate_member.setTypeface(faceGeorgia);
			LinearLayout coordinateHolder = (LinearLayout) activity.findViewById(R.id.coordinate_member_committees);

			for (int i = 0; i < coordinate_groups.size(); i++) {
				LinearLayout committeeItemLayout = (LinearLayout) inflater.inflate(R.layout.members_detail_committees_item, null);
				TextView committeeName = (TextView) committeeItemLayout.findViewById(R.id.committeeName);
				committeeName.setText(coordinate_groups.get(i).getGroupName());

				LinearLayout committee = (LinearLayout) committeeItemLayout.findViewById(R.id.committee);

				String committeeID = coordinate_groups.get(i).getGroupId();

				if (committeeID != null) {

					committee.setTag(committeeID);
					committee.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View view) {
							// TODO
							String committeeId = (String) view.getTag();

							Intent contactIntent;
							if (DataHolder.isOnline(((BaseActivity) mainActivity))) {
								contactIntent = new Intent(view.getContext(), MembersCommitteesDetailsNewsActivity.class);
								contactIntent.putExtra("index", memberRowId);
								contactIntent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
							} else {
								contactIntent = new Intent(view.getContext(), MembersCommitteesDetailsTasksActivity.class);
							}
							// Intent contactIntent = new
							// Intent(view.getContext(),
							// CommitteesDetailsNewsActivity.class);
							contactIntent.putExtra(CommitteesDetailsNewsActivity.KEY_COMMITTEE_ID, committeeId);
							activity.startActivity(contactIntent);
							activity.overridePendingTransition(0, 0);
						}
					});
				} else if (coordinate_groups.get(i).getGroupURL() != null && !coordinate_groups.get(i).getGroupURL().equals("")) {
					committee.setTag(coordinate_groups.get(i).getGroupURL());
					committee.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View view) {
							String committeeUrl = (String) view.getTag();
							Uri uriUrl = Uri.parse(committeeUrl);
							Intent launchBrowser = new Intent(Intent.ACTION_VIEW, uriUrl);
							activity.startActivity(launchBrowser);
						}

					});
				} else {
					ImageView committeeArrow = (ImageView) committeeItemLayout.findViewById(R.id.arrow);
					committeeArrow.setVisibility(View.GONE);

				}

				coordinateHolder.addView(committeeItemLayout);
			}
		} else {
			LinearLayout coordinate_group = (LinearLayout) activity.findViewById(R.id.coordinate_group);
			coordinate_group.setVisibility(View.GONE);
		}

		// full member
		List<MembersDetailsGroupsObject> full_groups = membersDetailsObject.getFullMemberGroups();
		if (full_groups.size() > 0) {
			TextView full_member = (TextView) activity.findViewById(R.id.full_member);
			full_member.setText(FULLMEMBER_TITLE);
			// full_member.setTypeface(faceGeorgia);
			LinearLayout fullHolder = (LinearLayout) activity.findViewById(R.id.full_member_committees);

			for (int i = 0; i < full_groups.size(); i++) {
				LinearLayout committeeItemLayout = (LinearLayout) inflater.inflate(R.layout.members_detail_committees_item, null);
				TextView committeeName = (TextView) committeeItemLayout.findViewById(R.id.committeeName);
				committeeName.setText(full_groups.get(i).getGroupName());
				LinearLayout committee = (LinearLayout) committeeItemLayout.findViewById(R.id.committee);

				String committeeID = full_groups.get(i).getGroupId();

				if (committeeID != null) {

					committee.setTag(committeeID);
					committee.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View view) {
							String committeeId = (String) view.getTag();
							Intent contactIntent;
							if (DataHolder.isOnline(((BaseActivity) mainActivity))) {
								contactIntent = new Intent(view.getContext(), MembersCommitteesDetailsNewsActivity.class);
								contactIntent.putExtra("index", memberRowId);
								contactIntent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
							} else {
								contactIntent = new Intent(view.getContext(), MembersCommitteesDetailsTasksActivity.class);
							}
							// Intent contactIntent = new
							// Intent(view.getContext(),
							// CommitteesDetailsNewsActivity.class);
							contactIntent.putExtra(CommitteesDetailsNewsActivity.KEY_COMMITTEE_ID, committeeId);
							activity.startActivity(contactIntent);
							activity.overridePendingTransition(0, 0);
						}
					});
				} else if (full_groups.get(i).getGroupURL() != null && !full_groups.get(i).getGroupURL().equals("")) {
					committee.setTag(full_groups.get(i).getGroupURL());
					committee.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View view) {
							String committeeUrl = (String) view.getTag();
							Uri uriUrl = Uri.parse(committeeUrl);
							Intent launchBrowser = new Intent(Intent.ACTION_VIEW, uriUrl);
							activity.startActivity(launchBrowser);
						}

					});
				} else {
					ImageView committeeArrow = (ImageView) committeeItemLayout.findViewById(R.id.arrow);
					committeeArrow.setVisibility(View.GONE);
				}
				fullHolder.addView(committeeItemLayout);
			}
		} else {
			LinearLayout full_group = (LinearLayout) activity.findViewById(R.id.full_group);
			full_group.setVisibility(View.GONE);
		}

		// deputy member
		List<MembersDetailsGroupsObject> deputy_groups = membersDetailsObject.getDeputyMemberGroups();
		if (deputy_groups.size() > 0) {
			TextView deputy_member = (TextView) activity.findViewById(R.id.deputy_member);
			deputy_member.setText(MEMBER_TITLE);
			// deputy_member.setTypeface(faceGeorgia);
			LinearLayout deputyHolder = (LinearLayout) activity.findViewById(R.id.deputy_member_committees);

			for (int i = 0; i < deputy_groups.size(); i++) {
				LinearLayout committeeItemLayout = (LinearLayout) inflater.inflate(R.layout.members_detail_committees_item, null);
				TextView committeeName = (TextView) committeeItemLayout.findViewById(R.id.committeeName);
				committeeName.setText(deputy_groups.get(i).getGroupName());
				LinearLayout committee = (LinearLayout) committeeItemLayout.findViewById(R.id.committee);

				String committeeID = deputy_groups.get(i).getGroupId();

				if (committeeID != null) {

					committee.setTag(committeeID);
					committee.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View view) {
							String committeeId = (String) view.getTag();
							Intent contactIntent;
							if (DataHolder.isOnline(((BaseActivity) mainActivity))) {
								contactIntent = new Intent(view.getContext(), MembersCommitteesDetailsNewsActivity.class);
								contactIntent.putExtra("index", memberRowId);
								contactIntent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
							} else {
								contactIntent = new Intent(view.getContext(), MembersCommitteesDetailsTasksActivity.class);
							}
							// Intent contactIntent = new
							// Intent(view.getContext(),
							// CommitteesDetailsNewsActivity.class);
							contactIntent.putExtra(CommitteesDetailsNewsActivity.KEY_COMMITTEE_ID, committeeId);
							activity.startActivity(contactIntent);
							activity.overridePendingTransition(0, 0);
						}
					});
				} else if (deputy_groups.get(i).getGroupURL() != null && !deputy_groups.get(i).getGroupURL().equals("")) {
					committee.setTag(deputy_groups.get(i).getGroupURL());
					committee.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View view) {
							String committeeUrl = (String) view.getTag();
							Uri uriUrl = Uri.parse(committeeUrl);
							Intent launchBrowser = new Intent(Intent.ACTION_VIEW, uriUrl);
							activity.startActivity(launchBrowser);
						}

					});
				} else {
					ImageView committeeArrow = (ImageView) committeeItemLayout.findViewById(R.id.arrow);
					committeeArrow.setVisibility(View.GONE);
				}
				deputyHolder.addView(committeeItemLayout);
			}
		} else {
			LinearLayout deputy_group = (LinearLayout) activity.findViewById(R.id.deputy_group);
			deputy_group.setVisibility(View.GONE);
		}

		// substitute member
		List<MembersDetailsGroupsObject> substitute_groups = membersDetailsObject.getSubstituteMemberGroups();
		if (substitute_groups.size() > 0) {
			TextView substitute_member = (TextView) activity.findViewById(R.id.substitute_member);
			substitute_member.setText(SUBSTITUTE_TITLE);
			// substitute_member.setTypeface(faceGeorgia);
			LinearLayout substituteHolder = (LinearLayout) activity.findViewById(R.id.substitute_member_committees);

			for (int i = 0; i < substitute_groups.size(); i++) {
				LinearLayout committeeItemLayout = (LinearLayout) inflater.inflate(R.layout.members_detail_committees_item, null);
				TextView committeeName = (TextView) committeeItemLayout.findViewById(R.id.committeeName);
				committeeName.setText(substitute_groups.get(i).getGroupName());

				LinearLayout committee = (LinearLayout) committeeItemLayout.findViewById(R.id.committee);

				String committeeID = substitute_groups.get(i).getGroupId();

				if (committeeID != null) {
					committee.setTag(committeeID);
					committee.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View view) {
							String committeeId = (String) view.getTag();
							Intent contactIntent;
							if (DataHolder.isOnline(((BaseActivity) mainActivity))) {
								contactIntent = new Intent(view.getContext(), MembersCommitteesDetailsNewsActivity.class);
								contactIntent.putExtra("index", memberRowId);
								contactIntent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
							} else {
								contactIntent = new Intent(view.getContext(), MembersCommitteesDetailsTasksActivity.class);
							}
							// Intent contactIntent = new
							// Intent(view.getContext(),
							// CommitteesDetailsNewsActivity.class);
							contactIntent.putExtra(CommitteesDetailsNewsActivity.KEY_COMMITTEE_ID, committeeId);
							activity.startActivity(contactIntent);
							activity.overridePendingTransition(0, 0);
						}
					});
				} else if (substitute_groups.get(i).getGroupURL() != null && !substitute_groups.get(i).getGroupURL().equals("")) {
					committee.setTag(substitute_groups.get(i).getGroupURL());
					committee.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View view) {
							String committeeUrl = (String) view.getTag();
							Uri uriUrl = Uri.parse(committeeUrl);
							Intent launchBrowser = new Intent(Intent.ACTION_VIEW, uriUrl);
							activity.startActivity(launchBrowser);
						}

					});
				}

				substituteHolder.addView(committeeItemLayout);
			}
		} else {
			LinearLayout substitute_group = (LinearLayout) activity.findViewById(R.id.substitute_group);
			substitute_group.setVisibility(View.GONE);
		}

		// ///////////////////////////////////////
		// president member
		TextView otherTitle = (TextView) activity.findViewById(R.id.other_title);
		otherTitle.setVisibility(View.GONE);
		List<MembersDetailsGroupsObject> other_sub_president_groups = membersDetailsObject.getOtherSubPresidentGroups();
		if (other_sub_president_groups.size() > 0) {
			otherTitle.setVisibility(View.VISIBLE);
			TextView president_member = (TextView) activity.findViewById(R.id.other_sub_president_member);
			president_member.setText(SUB_PRESIDENT_TITLE);
			// president_member.setTypeface(faceGeorgia);
			LinearLayout coordinateHolder = (LinearLayout) activity.findViewById(R.id.other_sub_president_member_committees);

			for (int i = 0; i < other_sub_president_groups.size(); i++) {
				LinearLayout committeeItemLayout = (LinearLayout) inflater.inflate(R.layout.members_detail_committees_item, null);
				TextView committeeName = (TextView) committeeItemLayout.findViewById(R.id.committeeName);
				committeeName.setText(other_sub_president_groups.get(i).getGroupName());

				LinearLayout committee = (LinearLayout) committeeItemLayout.findViewById(R.id.committee);

				String committeeID = other_sub_president_groups.get(i).getGroupId();

				if (committeeID != null) {

					committee.setTag(committeeID);
					committee.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View view) {
							String committeeId = (String) view.getTag();

							Intent contactIntent;
							if (DataHolder.isOnline(((BaseActivity) mainActivity))) {
								contactIntent = new Intent(view.getContext(), MembersCommitteesDetailsNewsActivity.class);
								contactIntent.putExtra("index", memberRowId);
								contactIntent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
							} else {
								contactIntent = new Intent(view.getContext(), MembersCommitteesDetailsTasksActivity.class);
							}
							contactIntent.putExtra(CommitteesDetailsNewsActivity.KEY_COMMITTEE_ID, committeeId);
							activity.startActivity(contactIntent);
							activity.overridePendingTransition(0, 0);
						}
					});
				} else if (other_sub_president_groups.get(i).getGroupURL() != null && !other_sub_president_groups.get(i).getGroupURL().equals("")) {
					committee.setTag(other_sub_president_groups.get(i).getGroupURL());
					committee.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View view) {
							String committeeUrl = (String) view.getTag();
							Uri uriUrl = Uri.parse(committeeUrl);
							Intent launchBrowser = new Intent(Intent.ACTION_VIEW, uriUrl);
							activity.startActivity(launchBrowser);
						}

					});
				} else {
					ImageView committeeArrow = (ImageView) committeeItemLayout.findViewById(R.id.arrow);
					committeeArrow.setVisibility(View.GONE);
				}
				coordinateHolder.addView(committeeItemLayout);
			}
		} else {
			LinearLayout sub_president_group = (LinearLayout) activity.findViewById(R.id.other_sub_president_group);
			sub_president_group.setVisibility(View.GONE);
		}

		// president member
		List<MembersDetailsGroupsObject> other_president_groups = membersDetailsObject.getOtherPresidentGroups();
		if (other_president_groups.size() > 0) {
			otherTitle.setVisibility(View.VISIBLE);
			TextView president_member = (TextView) activity.findViewById(R.id.other_president_member);
			president_member.setText(PRESIDENT_TITLE);
			// president_member.setTypeface(faceGeorgia);
			LinearLayout coordinateHolder = (LinearLayout) activity.findViewById(R.id.other_president_member_committees);

			for (int i = 0; i < other_president_groups.size(); i++) {
				LinearLayout committeeItemLayout = (LinearLayout) inflater.inflate(R.layout.members_detail_committees_item, null);
				TextView committeeName = (TextView) committeeItemLayout.findViewById(R.id.committeeName);
				committeeName.setText(other_president_groups.get(i).getGroupName());

				LinearLayout committee = (LinearLayout) committeeItemLayout.findViewById(R.id.committee);

				String committeeID = other_president_groups.get(i).getGroupId();

				if (committeeID != null) {

					committee.setTag(committeeID);
					committee.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View view) {
							String committeeId = (String) view.getTag();
							// Intent contactIntent = new
							// Intent(view.getContext(),
							// CommitteesDetailsNewsActivity.class);
							Intent contactIntent;
							if (DataHolder.isOnline(((BaseActivity) mainActivity))) {
								contactIntent = new Intent(view.getContext(), MembersCommitteesDetailsNewsActivity.class);
								contactIntent.putExtra("index", memberRowId);
								contactIntent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
							} else {
								contactIntent = new Intent(view.getContext(), MembersCommitteesDetailsTasksActivity.class);
							}
							contactIntent.putExtra(CommitteesDetailsNewsActivity.KEY_COMMITTEE_ID, committeeId);
							activity.startActivity(contactIntent);
							activity.overridePendingTransition(0, 0);
						}
					});
				} else if (other_president_groups.get(i).getGroupURL() != null && !other_president_groups.get(i).getGroupURL().equals("")) {
					committee.setTag(other_president_groups.get(i).getGroupURL());
					committee.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View view) {
							String committeeUrl = (String) view.getTag();
							Uri uriUrl = Uri.parse(committeeUrl);
							Intent launchBrowser = new Intent(Intent.ACTION_VIEW, uriUrl);
							activity.startActivity(launchBrowser);
						}

					});
				} else {
					ImageView committeeArrow = (ImageView) committeeItemLayout.findViewById(R.id.arrow);
					committeeArrow.setVisibility(View.GONE);
				}
				coordinateHolder.addView(committeeItemLayout);
			}
		} else {
			LinearLayout president_group = (LinearLayout) activity.findViewById(R.id.other_president_group);
			president_group.setVisibility(View.GONE);
		}

		// coordinate member
		List<MembersDetailsGroupsObject> other_coordinate_groups = membersDetailsObject.getOtherCoordinateGroups();
		if (other_coordinate_groups.size() > 0) {
			otherTitle.setVisibility(View.VISIBLE);
			TextView coordinate_member = (TextView) activity.findViewById(R.id.other_coordinate_member);
			coordinate_member.setText(COORDINATE_TITLE);
			// coordinate_member.setTypeface(faceGeorgia);
			LinearLayout coordinateHolder = (LinearLayout) activity.findViewById(R.id.other_coordinate_member_committees);

			for (int i = 0; i < other_coordinate_groups.size(); i++) {
				LinearLayout committeeItemLayout = (LinearLayout) inflater.inflate(R.layout.members_detail_committees_item, null);
				TextView committeeName = (TextView) committeeItemLayout.findViewById(R.id.committeeName);
				committeeName.setText(other_coordinate_groups.get(i).getGroupName());

				LinearLayout committee = (LinearLayout) committeeItemLayout.findViewById(R.id.committee);

				String committeeID = other_coordinate_groups.get(i).getGroupId();

				if (committeeID != null) {

					committee.setTag(committeeID);
					committee.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View view) {
							// TODO
							String committeeId = (String) view.getTag();

							Intent contactIntent;
							if (DataHolder.isOnline(((BaseActivity) mainActivity))) {
								contactIntent = new Intent(view.getContext(), MembersCommitteesDetailsNewsActivity.class);
								contactIntent.putExtra("index", memberRowId);
								contactIntent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
							} else {
								contactIntent = new Intent(view.getContext(), MembersCommitteesDetailsTasksActivity.class);
							}
							// Intent contactIntent = new
							// Intent(view.getContext(),
							// CommitteesDetailsNewsActivity.class);
							contactIntent.putExtra(CommitteesDetailsNewsActivity.KEY_COMMITTEE_ID, committeeId);
							activity.startActivity(contactIntent);
							activity.overridePendingTransition(0, 0);
						}
					});
				} else if (other_coordinate_groups.get(i).getGroupURL() != null && !other_coordinate_groups.get(i).getGroupURL().equals("")) {
					committee.setTag(other_coordinate_groups.get(i).getGroupURL());
					committee.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View view) {
							String committeeUrl = (String) view.getTag();
							Uri uriUrl = Uri.parse(committeeUrl);
							Intent launchBrowser = new Intent(Intent.ACTION_VIEW, uriUrl);
							activity.startActivity(launchBrowser);
						}

					});
				} else {
					ImageView committeeArrow = (ImageView) committeeItemLayout.findViewById(R.id.arrow);
					committeeArrow.setVisibility(View.GONE);

				}

				coordinateHolder.addView(committeeItemLayout);
			}
		} else {
			LinearLayout coordinate_group = (LinearLayout) activity.findViewById(R.id.other_coordinate_group);
			coordinate_group.setVisibility(View.GONE);
		}

		// full member
		List<MembersDetailsGroupsObject> other_full_groups = membersDetailsObject.getOtherFullMemberGroups();
		if (other_full_groups.size() > 0) {
			otherTitle.setVisibility(View.VISIBLE);
			TextView full_member = (TextView) activity.findViewById(R.id.other_full_member);
			full_member.setText(FULLMEMBER_TITLE);
			// full_member.setTypeface(faceGeorgia);
			LinearLayout fullHolder = (LinearLayout) activity.findViewById(R.id.other_full_member_committees);

			for (int i = 0; i < other_full_groups.size(); i++) {
				LinearLayout committeeItemLayout = (LinearLayout) inflater.inflate(R.layout.members_detail_committees_item, null);
				TextView committeeName = (TextView) committeeItemLayout.findViewById(R.id.committeeName);
				committeeName.setText(other_full_groups.get(i).getGroupName());
				LinearLayout committee = (LinearLayout) committeeItemLayout.findViewById(R.id.committee);

				String committeeID = other_full_groups.get(i).getGroupId();

				if (committeeID != null) {

					committee.setTag(committeeID);
					committee.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View view) {
							String committeeId = (String) view.getTag();
							Intent contactIntent;
							if (DataHolder.isOnline(((BaseActivity) mainActivity))) {
								contactIntent = new Intent(view.getContext(), MembersCommitteesDetailsNewsActivity.class);
								contactIntent.putExtra("index", memberRowId);
								contactIntent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
							} else {
								contactIntent = new Intent(view.getContext(), MembersCommitteesDetailsTasksActivity.class);
							}
							// Intent contactIntent = new
							// Intent(view.getContext(),
							// CommitteesDetailsNewsActivity.class);
							contactIntent.putExtra(CommitteesDetailsNewsActivity.KEY_COMMITTEE_ID, committeeId);
							activity.startActivity(contactIntent);
							activity.overridePendingTransition(0, 0);
						}
					});
				} else if (other_full_groups.get(i).getGroupURL() != null && !other_full_groups.get(i).getGroupURL().equals("")) {
					committee.setTag(other_full_groups.get(i).getGroupURL());
					committee.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View view) {
							String committeeUrl = (String) view.getTag();
							Uri uriUrl = Uri.parse(committeeUrl);
							Intent launchBrowser = new Intent(Intent.ACTION_VIEW, uriUrl);
							activity.startActivity(launchBrowser);
						}

					});
				} else {
					ImageView committeeArrow = (ImageView) committeeItemLayout.findViewById(R.id.arrow);
					committeeArrow.setVisibility(View.GONE);
				}
				fullHolder.addView(committeeItemLayout);
			}
		} else {
			LinearLayout full_group = (LinearLayout) activity.findViewById(R.id.other_full_group);
			full_group.setVisibility(View.GONE);
		}

		// deputy member
		List<MembersDetailsGroupsObject> other_deputy_groups = membersDetailsObject.getOtherDeputyMemberGroups();
		if (other_deputy_groups.size() > 0) {
			otherTitle.setVisibility(View.VISIBLE);
			TextView deputy_member = (TextView) activity.findViewById(R.id.other_deputy_member);
			deputy_member.setText(MEMBER_TITLE);
			// deputy_member.setTypeface(faceGeorgia);
			LinearLayout deputyHolder = (LinearLayout) activity.findViewById(R.id.other_deputy_member_committees);

			for (int i = 0; i < other_deputy_groups.size(); i++) {
				LinearLayout committeeItemLayout = (LinearLayout) inflater.inflate(R.layout.members_detail_committees_item, null);
				TextView committeeName = (TextView) committeeItemLayout.findViewById(R.id.committeeName);
				committeeName.setText(other_deputy_groups.get(i).getGroupName());
				LinearLayout committee = (LinearLayout) committeeItemLayout.findViewById(R.id.committee);

				String committeeID = other_deputy_groups.get(i).getGroupId();

				if (committeeID != null) {

					committee.setTag(committeeID);
					committee.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View view) {
							String committeeId = (String) view.getTag();

							Intent contactIntent;
							if (DataHolder.isOnline(((BaseActivity) mainActivity))) {
								contactIntent = new Intent(view.getContext(), MembersCommitteesDetailsNewsActivity.class);
								contactIntent.putExtra("index", memberRowId);
								contactIntent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
							} else {
								contactIntent = new Intent(view.getContext(), MembersCommitteesDetailsTasksActivity.class);
							}
							// Intent contactIntent = new
							// Intent(view.getContext(),
							// CommitteesDetailsNewsActivity.class);
							contactIntent.putExtra(CommitteesDetailsNewsActivity.KEY_COMMITTEE_ID, committeeId);
							activity.startActivity(contactIntent);
							activity.overridePendingTransition(0, 0);
						}
					});
				} else if (other_deputy_groups.get(i).getGroupURL() != null && !other_deputy_groups.get(i).getGroupURL().equals("")) {
					committee.setTag(other_deputy_groups.get(i).getGroupURL());
					committee.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View view) {
							String committeeUrl = (String) view.getTag();
							Uri uriUrl = Uri.parse(committeeUrl);
							Intent launchBrowser = new Intent(Intent.ACTION_VIEW, uriUrl);
							activity.startActivity(launchBrowser);
						}

					});
				} else {
					ImageView committeeArrow = (ImageView) committeeItemLayout.findViewById(R.id.arrow);
					committeeArrow.setVisibility(View.GONE);
				}
				deputyHolder.addView(committeeItemLayout);
			}
		} else {
			LinearLayout deputy_group = (LinearLayout) activity.findViewById(R.id.other_deputy_group);
			deputy_group.setVisibility(View.GONE);
		}

		// substitute member
		List<MembersDetailsGroupsObject> other_substitute_groups = membersDetailsObject.getOtherSubstituteMemberGroups();
		if (other_substitute_groups.size() > 0) {
			otherTitle.setVisibility(View.VISIBLE);
			TextView substitute_member = (TextView) activity.findViewById(R.id.other_substitute_member);
			substitute_member.setText(MEMBER_TITLE);
			// substitute_member.setTypeface(faceGeorgia);
			LinearLayout substituteHolder = (LinearLayout) activity.findViewById(R.id.other_substitute_member_committees);

			for (int i = 0; i < other_substitute_groups.size(); i++) {
				LinearLayout committeeItemLayout = (LinearLayout) inflater.inflate(R.layout.members_detail_committees_item, null);
				TextView committeeName = (TextView) committeeItemLayout.findViewById(R.id.committeeName);
				committeeName.setText(other_substitute_groups.get(i).getGroupName());

				LinearLayout committee = (LinearLayout) committeeItemLayout.findViewById(R.id.committee);

				String committeeID = other_substitute_groups.get(i).getGroupId();

				if (committeeID != null) {
					committee.setTag(committeeID);
					committee.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View view) {
							String committeeId = (String) view.getTag();
							Intent contactIntent;
							if (DataHolder.isOnline(((BaseActivity) mainActivity))) {
								contactIntent = new Intent(view.getContext(), MembersCommitteesDetailsNewsActivity.class);
								contactIntent.putExtra("index", memberRowId);
								contactIntent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
							} else {
								contactIntent = new Intent(view.getContext(), MembersCommitteesDetailsTasksActivity.class);
							}
							// Intent contactIntent = new
							// Intent(view.getContext(),
							// CommitteesDetailsNewsActivity.class);
							contactIntent.putExtra(CommitteesDetailsNewsActivity.KEY_COMMITTEE_ID, committeeId);
							activity.startActivity(contactIntent);
							activity.overridePendingTransition(0, 0);
						}
					});
				} else if (other_substitute_groups.get(i).getGroupURL() != null && !other_substitute_groups.get(i).getGroupURL().equals("")) {
					committee.setTag(other_substitute_groups.get(i).getGroupURL());
					committee.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View view) {
							String committeeUrl = (String) view.getTag();
							Uri uriUrl = Uri.parse(committeeUrl);
							Intent launchBrowser = new Intent(Intent.ACTION_VIEW, uriUrl);
							activity.startActivity(launchBrowser);
						}

					});
				}

				substituteHolder.addView(committeeItemLayout);
			}
		} else {
			LinearLayout substitute_group = (LinearLayout) activity.findViewById(R.id.other_substitute_group);
			substitute_group.setVisibility(View.GONE);
		}

		// ///////////////////////////////////////
	}

}
