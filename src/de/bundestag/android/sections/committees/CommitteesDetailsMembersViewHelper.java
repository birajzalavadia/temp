package de.bundestag.android.sections.committees;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.database.Cursor;
import android.support.v4.app.FragmentActivity;
import de.bundestag.android.parser.objects.CommitteesObject;
import de.bundestag.android.parser.objects.MembersDetailsObject;
import de.bundestag.android.sections.members.MembersFractionListAdapter;
import de.bundestag.android.storage.CommitteesDatabaseAdapter;
import de.bundestag.android.storage.MembersDatabaseAdapter;
import de.bundestag.android.synchronization.MembersSynchronization;

public class CommitteesDetailsMembersViewHelper {
	public static List<MembersDetailsObject> getPresident(FragmentActivity activity, String committeeId) {
		MembersDatabaseAdapter membersDatabaseAdapter = new MembersDatabaseAdapter(activity);
		membersDatabaseAdapter.open();
		Cursor memberCursor = membersDatabaseAdapter.fetchGroupMembers(committeeId, MembersSynchronization.GROUP_TYPE_PRESIDENT);

		List<MembersDetailsObject> members = new ArrayList<MembersDetailsObject>();
		MembersDetailsObject membersObject;
		while (memberCursor.isAfterLast() == false) {
			membersObject = new MembersDetailsObject();
			membersObject.setId(memberCursor.getString(memberCursor.getColumnIndex(MembersDatabaseAdapter.KEY_ID)));
			membersObject.setCourse(memberCursor.getString(memberCursor.getColumnIndex(MembersDatabaseAdapter.KEY_DETAILS_COURSE)));
			membersObject.setFirstName(memberCursor.getString(memberCursor.getColumnIndex(MembersDatabaseAdapter.KEY_DETAILS_FIRSTNAME)));
			membersObject.setTitle(memberCursor.getString(memberCursor.getColumnIndex(MembersDatabaseAdapter.KEY_DETAILS_TITLE)));
			membersObject.setLastName(memberCursor.getString(memberCursor.getColumnIndex(MembersDatabaseAdapter.KEY_DETAILS_LASTNAME)));
			membersObject.setFraction(memberCursor.getString(memberCursor.getColumnIndex(MembersDatabaseAdapter.KEY_DETAILS_FRACTION)));
			membersObject.setMediaPhotoImageString(memberCursor.getString(memberCursor.getColumnIndex(MembersDatabaseAdapter.KEY_DETAILS_MEDIAPHOTOSTRING)));

			members.add(membersObject);
			memberCursor.moveToNext();
		}

		memberCursor.close();
		membersDatabaseAdapter.close();

		return members;
	}

	public static List<MembersDetailsObject> getSubPresidents(FragmentActivity activity, String committeeId) {
		MembersDatabaseAdapter membersDatabaseAdapter = new MembersDatabaseAdapter(activity);
		membersDatabaseAdapter.open();
		Cursor memberCursor = membersDatabaseAdapter.fetchGroupMembers(committeeId, MembersSynchronization.GROUP_TYPE_SUB_PRESIDENT);

		List<MembersDetailsObject> members = new ArrayList<MembersDetailsObject>();
		MembersDetailsObject membersObject;
		while (memberCursor.isAfterLast() == false) {
			membersObject = new MembersDetailsObject();
			membersObject.setId(memberCursor.getString(memberCursor.getColumnIndex(MembersDatabaseAdapter.KEY_ID)));
			membersObject.setCourse(memberCursor.getString(memberCursor.getColumnIndex(MembersDatabaseAdapter.KEY_DETAILS_COURSE)));
			membersObject.setFirstName(memberCursor.getString(memberCursor.getColumnIndex(MembersDatabaseAdapter.KEY_DETAILS_FIRSTNAME)));
			membersObject.setTitle(memberCursor.getString(memberCursor.getColumnIndex(MembersDatabaseAdapter.KEY_DETAILS_TITLE)));
			membersObject.setLastName(memberCursor.getString(memberCursor.getColumnIndex(MembersDatabaseAdapter.KEY_DETAILS_LASTNAME)));
			membersObject.setFraction(memberCursor.getString(memberCursor.getColumnIndex(MembersDatabaseAdapter.KEY_DETAILS_FRACTION)));
			membersObject.setMediaPhotoImageString(memberCursor.getString(memberCursor.getColumnIndex(MembersDatabaseAdapter.KEY_DETAILS_MEDIAPHOTOSTRING)));

			members.add(membersObject);
			memberCursor.moveToNext();
		}

		memberCursor.close();
		membersDatabaseAdapter.close();

		return members;
	}

	public static ArrayList<HashMap<String, Object>> getMembersByFraction(FragmentActivity activity, int committeesId, String committeesIdString, String fractionName) {
		MembersDatabaseAdapter membersDatabaseAdapter = new MembersDatabaseAdapter(activity);
		membersDatabaseAdapter.open();

		// Full members
		Cursor memberCursor = membersDatabaseAdapter.fetchAllMembersByFractionsCommittee(fractionName, committeesIdString, MembersSynchronization.GROUP_TYPE_FULLMEMBER);

		Map<String, String> memberMap = new HashMap<String, String>();
		ArrayList<HashMap<String, Object>> membersByFraction = new ArrayList<HashMap<String, Object>>();
		HashMap<String, Object> hashMap;

		while (memberCursor.isAfterLast() == false) {
			hashMap = new HashMap<String, Object>();
			// hashMap.put(MembersDatabaseAdapter.KEY_DETAILS_FIRSTNAME,
			// "teste");
			hashMap.put(MembersDatabaseAdapter.KEY_ID, memberCursor.getString(memberCursor.getColumnIndex(MembersDatabaseAdapter.KEY_ID)));
			hashMap.put(MembersDatabaseAdapter.KEY_DETAILS_FIRSTNAME, memberCursor.getString(memberCursor.getColumnIndex(MembersDatabaseAdapter.KEY_DETAILS_FIRSTNAME)));
			hashMap.put(MembersDatabaseAdapter.KEY_NAME, memberCursor.getString(memberCursor.getColumnIndex(MembersDatabaseAdapter.KEY_NAME)));
			hashMap.put(MembersDatabaseAdapter.KEY_DETAILS_FRACTION, memberCursor.getString(memberCursor.getColumnIndex(MembersDatabaseAdapter.KEY_DETAILS_FRACTION)));
			hashMap.put(MembersDatabaseAdapter.KEY_DETAILS_LASTNAME, memberCursor.getString(memberCursor.getColumnIndex(MembersDatabaseAdapter.KEY_DETAILS_LASTNAME)));
			hashMap.put(MembersDatabaseAdapter.KEY_LAND, memberCursor.getString(memberCursor.getColumnIndex(MembersDatabaseAdapter.KEY_LAND)));
			hashMap.put(MembersDatabaseAdapter.KEY_DETAILS_MEDIAPHOTOSTRING,
					memberCursor.getString(memberCursor.getColumnIndex(MembersDatabaseAdapter.KEY_DETAILS_MEDIAPHOTOSTRING)));
			//
			membersByFraction.add(hashMap);

			memberMap.put(memberCursor.getString(memberCursor.getColumnIndex(MembersDatabaseAdapter.KEY_ID)),
					memberCursor.getString(memberCursor.getColumnIndex(MembersDatabaseAdapter.KEY_ID)));

			memberCursor.moveToNext();
		}

		memberCursor.close();

		// Insert separator
		hashMap = new HashMap<String, Object>();
		hashMap.put(MembersDatabaseAdapter.KEY_ID, -1);
		hashMap.put(MembersDatabaseAdapter.KEY_DETAILS_FIRSTNAME, "");
		hashMap.put(MembersDatabaseAdapter.KEY_NAME, "");
		hashMap.put(MembersDatabaseAdapter.KEY_DETAILS_FRACTION, "");
		hashMap.put(MembersDatabaseAdapter.KEY_DETAILS_LASTNAME, "");
		hashMap.put(MembersDatabaseAdapter.KEY_LAND, "");
		hashMap.put(MembersDatabaseAdapter.KEY_DETAILS_MEDIAPHOTOSTRING, "");
		membersByFraction.add(hashMap);

		// Other members
		memberCursor = membersDatabaseAdapter.fetchAllOtherMembersByFractionsCommittee(fractionName, committeesIdString, MembersSynchronization.GROUP_TYPE_FULLMEMBER);

		while (memberCursor.isAfterLast() == false) {
			hashMap = new HashMap<String, Object>();
			// hashMap.put(MembersDatabaseAdapter.KEY_DETAILS_FIRSTNAME,
			// "teste");
			hashMap.put(MembersDatabaseAdapter.KEY_ID, memberCursor.getString(memberCursor.getColumnIndex(MembersDatabaseAdapter.KEY_ID)));
			hashMap.put(MembersDatabaseAdapter.KEY_DETAILS_FIRSTNAME, memberCursor.getString(memberCursor.getColumnIndex(MembersDatabaseAdapter.KEY_DETAILS_FIRSTNAME)));
			hashMap.put(MembersDatabaseAdapter.KEY_NAME, memberCursor.getString(memberCursor.getColumnIndex(MembersDatabaseAdapter.KEY_NAME)));
			hashMap.put(MembersDatabaseAdapter.KEY_DETAILS_FRACTION, memberCursor.getString(memberCursor.getColumnIndex(MembersDatabaseAdapter.KEY_DETAILS_FRACTION)));
			hashMap.put(MembersDatabaseAdapter.KEY_DETAILS_LASTNAME, memberCursor.getString(memberCursor.getColumnIndex(MembersDatabaseAdapter.KEY_DETAILS_LASTNAME)));
			hashMap.put(MembersDatabaseAdapter.KEY_LAND, memberCursor.getString(memberCursor.getColumnIndex(MembersDatabaseAdapter.KEY_LAND)));
			hashMap.put(MembersDatabaseAdapter.KEY_DETAILS_MEDIAPHOTOSTRING,
					memberCursor.getString(memberCursor.getColumnIndex(MembersDatabaseAdapter.KEY_DETAILS_MEDIAPHOTOSTRING)));
			//

			if (!memberMap.containsKey(memberCursor.getString(memberCursor.getColumnIndex(MembersDatabaseAdapter.KEY_ID)))) {
				membersByFraction.add(hashMap);
			}

			memberCursor.moveToNext();
		}

		memberCursor.close();

		membersDatabaseAdapter.close();

		return membersByFraction;
	}

	/**
	 * Get the committee with the row id committeeId.
	 */
	public static CommitteesObject getCommittee(FragmentActivity activity, int committeeId) {
		CommitteesObject committeesObject = new CommitteesObject();

		CommitteesDatabaseAdapter committeesDatabaseAdapter = new CommitteesDatabaseAdapter(activity);
		committeesDatabaseAdapter.open();
		Cursor committeeCursor = committeesDatabaseAdapter.fetchCommittees(committeeId);

		committeesObject.setId(committeeCursor.getString(committeeCursor.getColumnIndex(CommitteesDatabaseAdapter.KEY_ID)));
		committeesObject.setName(committeeCursor.getString(committeeCursor.getColumnIndex(CommitteesDatabaseAdapter.KEY_NAME)));

		committeeCursor.close();
		committeesDatabaseAdapter.close();

		return committeesObject;
	}

	/**
	 * Get the committee with the row id committeeId.
	 */
	public static CommitteesObject getCommittee(Activity activity, int committeeId) {
		CommitteesObject committeesObject = new CommitteesObject();

		CommitteesDatabaseAdapter committeesDatabaseAdapter = new CommitteesDatabaseAdapter(activity);
		committeesDatabaseAdapter.open();
		Cursor committeeCursor = committeesDatabaseAdapter.fetchCommittees(committeeId);

		committeesObject.setId(committeeCursor.getString(committeeCursor.getColumnIndex(CommitteesDatabaseAdapter.KEY_ID)));
		committeesObject.setName(committeeCursor.getString(committeeCursor.getColumnIndex(CommitteesDatabaseAdapter.KEY_NAME)));

		committeeCursor.close();
		committeesDatabaseAdapter.close();

		return committeesObject;
	}

	/**
	 * Creates hashmap list of fractions with number of members in each.
	 * 
	 * Used by general list fragment.
	 */
	public static ArrayList<HashMap<String, Object>> createMembersFractions(FragmentActivity activity, String committeeId) {
		MembersDatabaseAdapter membersDatabaseAdapter = new MembersDatabaseAdapter(activity);
		membersDatabaseAdapter.open();

		Cursor fractionsCDUCSUCursor = membersDatabaseAdapter.fetchAllMembersByFractionsCommittee("CDU/CSU", committeeId, MembersSynchronization.GROUP_TYPE_FULLMEMBER);
		Cursor fractionsSPDCursor = membersDatabaseAdapter.fetchAllMembersByFractionsCommittee("SPD", committeeId, MembersSynchronization.GROUP_TYPE_FULLMEMBER);
		Cursor fractionsFDPCursor = membersDatabaseAdapter.fetchAllMembersByFractionsCommittee("FDP", committeeId, MembersSynchronization.GROUP_TYPE_FULLMEMBER);
		Cursor fractionsDIELINKECursor = membersDatabaseAdapter.fetchAllMembersByFractionsCommittee("DIE LINKE.", committeeId, MembersSynchronization.GROUP_TYPE_FULLMEMBER);
		Cursor fractionsBUNDNISGRUNENCursor = membersDatabaseAdapter.fetchAllMembersByFractionsCommittee("BÜNDNIS 90/DIE GRÜNEN", committeeId, MembersSynchronization.GROUP_TYPE_FULLMEMBER);
		int cducsuCount = 0, spdCount = 0, fdpCount = 0, dielinkCount = 0, bundnistgCount = 0;
		boolean hasFractionsCDUCSU = (fractionsCDUCSUCursor.getCount() > 0);
		if (hasFractionsCDUCSU) {
			cducsuCount = fractionsCDUCSUCursor.getCount();
		}
		boolean hasFractionsSPD = (fractionsSPDCursor.getCount() > 0);
		if (hasFractionsSPD) {
			spdCount = fractionsSPDCursor.getCount();
		}
		boolean hasFractionsFDP = (fractionsFDPCursor.getCount() > 0);
		if (hasFractionsFDP) {
			fdpCount = fractionsFDPCursor.getCount();
		}
		boolean hasFractionsDIELINKE = (fractionsDIELINKECursor.getCount() > 0);
		if (hasFractionsDIELINKE) {
			dielinkCount = fractionsDIELINKECursor.getCount();
		}
		boolean hasFractionsBUNDNISGRUNEN = (fractionsBUNDNISGRUNENCursor.getCount() > 0);
		if (hasFractionsBUNDNISGRUNEN) {
			bundnistgCount = fractionsBUNDNISGRUNENCursor.getCount();
		}
		fractionsCDUCSUCursor.close();
		fractionsSPDCursor.close();
		fractionsFDPCursor.close();
		fractionsDIELINKECursor.close();
		fractionsBUNDNISGRUNENCursor.close();

		ArrayList<HashMap<String, Object>> fractions = new ArrayList<HashMap<String, Object>>();

		HashMap<String, Object> hashMap;
		if (hasFractionsCDUCSU) {
			hashMap = new HashMap<String, Object>();
			hashMap.put(MembersFractionListAdapter.KEY_FRACTION_NAME, "CDU/CSU");
			hashMap.put(MembersFractionListAdapter.KEY_NUMBERS, cducsuCount);
			fractions.add(hashMap);
		}

		if (hasFractionsSPD) {
			hashMap = new HashMap<String, Object>();
			hashMap.put(MembersFractionListAdapter.KEY_FRACTION_NAME, "SPD");
			hashMap.put(MembersFractionListAdapter.KEY_NUMBERS, spdCount);
			fractions.add(hashMap);
		}

		if (hasFractionsFDP) {
			hashMap = new HashMap<String, Object>();
			hashMap.put(MembersFractionListAdapter.KEY_FRACTION_NAME, "FDP");
			hashMap.put(MembersFractionListAdapter.KEY_NUMBERS, fdpCount);
			fractions.add(hashMap);
		}

		if (hasFractionsDIELINKE) {
			hashMap = new HashMap<String, Object>();
			hashMap.put(MembersFractionListAdapter.KEY_FRACTION_NAME, "DIE LINKE.");
			hashMap.put(MembersFractionListAdapter.KEY_NUMBERS, dielinkCount);
			fractions.add(hashMap);
		}

		if (hasFractionsBUNDNISGRUNEN) {
			hashMap = new HashMap<String, Object>();
			hashMap.put(MembersFractionListAdapter.KEY_FRACTION_NAME, "BÜNDNIS 90/DIE GRÜNEN");
			hashMap.put(MembersFractionListAdapter.KEY_NUMBERS, bundnistgCount);
			fractions.add(hashMap);
		}

		membersDatabaseAdapter.close();

		return fractions;
	}

	public static int getCommitteeMembersCount(FragmentActivity activity, String committeeId) {
		MembersDatabaseAdapter membersDatabaseAdapter = new MembersDatabaseAdapter(activity);
		membersDatabaseAdapter.open();

		Cursor c = membersDatabaseAdapter.fetchCommitteeMembersCount(committeeId);
		int count = 0;
		if (c != null && c.getCount() > 0) {
			c.moveToFirst();
			count = c.getInt(c.getColumnIndex("members_count"));
		} else {
			count = 0;
		}
		c.close();
		membersDatabaseAdapter.close();
		return count;
	}

}
