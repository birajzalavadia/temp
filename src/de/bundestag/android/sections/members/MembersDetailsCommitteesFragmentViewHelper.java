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
import de.bundestag.android.R;
import de.bundestag.android.constant.DataHolder;
import de.bundestag.android.parser.objects.MembersDetailsGroupsObject;
import de.bundestag.android.parser.objects.MembersDetailsObject;
import de.bundestag.android.sections.committees.CommitteesDetailsMembersByFractionActivity;
import de.bundestag.android.sections.committees.CommitteesDetailsTasksActivity;
import de.bundestag.android.storage.CommitteesDatabaseAdapter;
import de.bundestag.android.synchronization.BaseSynchronizeTask;
import de.bundestag.android.synchronization.SynchronizeCheckCommitteeNewsTask;
import de.bundestag.android.synchronization.SynchronizeCommitteeNewsTask;

public class MembersDetailsCommitteesFragmentViewHelper {
	private static final String COORDINATE_TITLE = "Obfrau/Obmann";
	private static final String MEMBER_TITLE = "Stellvertretendes Mitglied";
	private static final String FULLMEMBER_TITLE = "Ordentliches Mitglied";
	private static final String SUBSTITUTE_TITLE = "Mitgliedschaften in sonstigen Gremien";
	private static final String PRESIDENT_TITLE = "Vorsitz";
	private static final String SUB_PRESIDENT_TITLE = "Stellvertretender Vorsitz";
	private static Activity activity = null;

	// final private static boolean isBlocked=false;
	public static void createDetailsView(MembersDetailsObject membersDetailsObject, final FragmentActivity activity, final Activity mainActivity, final int memberRowId,
			final boolean isOnline) {
		try {
			MembersDetailsCommitteesFragmentViewHelper.activity = activity;
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

								checkCommitteeData(committeeId, isOnline);
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
								Intent contactIntent;
								checkCommitteeData(committeeId, isOnline);
							}
						});
					} else if (president_groups.get(i).getGroupURL() != null && !president_groups.get(i).getGroupURL().equals("")) {
						committee.setTag(president_groups.get(i).getGroupURL());
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
				if (activity instanceof CommitteesDetailsMembersByFractionActivity) {
					try {
						TextView president_member = (TextView) activity.findViewById(R.id.president_member);
						president_member.setVisibility(View.GONE);
						((ImageView) activity.findViewById(R.id.president_seperator)).setVisibility(View.GONE);
						ImageView iv = (ImageView) activity.findViewById(R.id.presidentGroupIdSeperator);
						if (iv != null) {
							iv.setVisibility(View.GONE);
						}
					} catch (Exception e) {

					}
				} else {
					LinearLayout president_group = (LinearLayout) activity.findViewById(R.id.president_group);
					president_group.setVisibility(View.GONE);
				}
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
								checkCommitteeData(committeeId, isOnline);
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
								checkCommitteeData(committeeId, isOnline);
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
								checkCommitteeData(committeeId, isOnline);
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
								checkCommitteeData(committeeId, isOnline);
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
								checkCommitteeData(committeeId, isOnline);
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
								checkCommitteeData(committeeId, isOnline);
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
								checkCommitteeData(committeeId, isOnline);
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
								checkCommitteeData(committeeId, isOnline);
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
								checkCommitteeData(committeeId, isOnline);
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
								checkCommitteeData(committeeId, isOnline);
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
		} catch (Exception e) {

		}
	}

	private static void checkCommitteeData(String committeeId, boolean isOnline) {
		if (isOnline) {

			DataHolder.committesStringId = committeeId;// getCommitteeIdString(memberRowId);
			checkCommitteeNewsNeedsUpdating(DataHolder.committesStringId);

		} else {

			Intent intent = new Intent();
			intent.putExtra("COMMITTEE_ID", committeeId);// getCommitteeIdString(memberRowId));
			intent.setClass(activity, CommitteesDetailsTasksActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			activity.startActivity(intent);
			activity.overridePendingTransition(0, 0);

		}

	}

	private static void checkCommitteeNewsNeedsUpdating(String committeesIdString) {
		CommitteesDatabaseAdapter committeesDatabaseAdapter = new CommitteesDatabaseAdapter(activity);
		committeesDatabaseAdapter.open();
		boolean existNews = committeesDatabaseAdapter.existNews(committeesIdString);
		committeesDatabaseAdapter.close();
		DataHolder.isFromMember = true;
		if (existNews) {
			SynchronizeCheckCommitteeNewsTask synchronizeCheckCommitteeNewsTask = new SynchronizeCheckCommitteeNewsTask(true, DataHolder.committesStringId);
			BaseSynchronizeTask task = synchronizeCheckCommitteeNewsTask;
			synchronizeCheckCommitteeNewsTask.execute(activity);
		} else {
			SynchronizeCommitteeNewsTask committeeNewsUpdateTask = new SynchronizeCommitteeNewsTask();
			BaseSynchronizeTask task = committeeNewsUpdateTask;
			committeeNewsUpdateTask.committeeId = committeesIdString;
			committeeNewsUpdateTask.execute(activity);
		}
	}

}
