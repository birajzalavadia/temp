package de.bundestag.android.storage;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import de.bundestag.android.parser.objects.MembersDetailsGroupsObject;
import de.bundestag.android.parser.objects.MembersDetailsObject;
import de.bundestag.android.parser.objects.MembersDetailsWebsitesObject;
import de.bundestag.android.parser.objects.MembersObject;
import de.bundestag.android.synchronization.MembersSynchronization;

/**
 * Members database adapter.
 * 
 * Contains helper methods to interact with the members table in the database.
 */
public class MembersDatabaseAdapter {
	public static final String KEY_ROWID = "_id";
	public static final String KEY_ID = "member_id";
	public static final String KEY_NAME = "name";
	public static final String KEY_BIOURL = "bio_url";
	public static final String KEY_INFOURL = "info_url";
	public static final String KEY_INFOURLMORE = "info_url_more";
	public static final String KEY_LAND = "land";
	public static final String KEY_PHOTO = "photo";
	public static final String KEY_PHOTO_STRING = "photo_string";
	public static final String KEY_PHOTOLASTCHANGED = "photo_last_changed";
	public static final String KEY_PHOTOCHANGEDDATETIME = "photo_changed_date_time";
	public static final String KEY_LASTCHANGED = "last_changed";
	public static final String KEY_CHANGEDDATETIME = "changed_date_time";

	// details
	public static final String KEY_DETAILS_STATUS = "details_status";
	public static final String KEY_DETAILS_EXITDATE = "details_exit_date";
	public static final String KEY_DETAILS_LASTNAME = "details_last_name";
	public static final String KEY_DETAILS_FIRSTNAME = "details_first_name";
	public static final String KEY_DETAILS_TITLE = "details_title";
	public static final String KEY_DETAILS_COURSE = "details_course";
	public static final String KEY_DETAILS_LOCAL = "details_local";
	public static final String KEY_DETAILS_BIRTHDATE = "details_birthdate";
	public static final String KEY_DETAILS_RELIGION = "details_religion";
	public static final String KEY_DETAILS_DEGREE = "details_degree";
	public static final String KEY_DETAILS_HIGHEREDUCATION = "details_higher_education";
	public static final String KEY_DETAILS_PROFESSION = "details_profession";
	public static final String KEY_DETAILS_PROFESSIONFIELD = "details_profession_field";
	public static final String KEY_DETAILS_SEX = "details_sex";
	public static final String KEY_DETAILS_MARITALSTATUS = "details_marital_status";
	public static final String KEY_DETAILS_CHILDREN = "details_children";
	public static final String KEY_DETAILS_FRACTION = "details_fraction";
	public static final String KEY_DETAILS_PARTY = "details_party";
	public static final String KEY_DETAILS_CITY = "details_city";
	public static final String KEY_DETAILS_ELECTED = "details_elected";
	public static final String KEY_DETAILS_BIOURL = "details_bio_url";
	public static final String KEY_DETAILS_INFORMATION = "details_information";
	public static final String KEY_DETAILS_MORE = "details_more";
	public static final String KEY_DETAILS_HOMEPAGE = "details_homepage";
	public static final String KEY_DETAILS_PHONE = "details_phone";
	public static final String KEY_DETAILS_DISCLOSUREINFORMATION = "details_disclosure_information";
	public static final String KEY_DETAILS_MEDIAPHOTO = "details_media_photo";
	public static final String KEY_DETAILS_MEDIAPHOTOSTRING = "details_media_photo_string";
	public static final String KEY_DETAILS_MEDIAPHOTOCOPYRIGHT = "details_media_photo_copyright";
	public static final String KEY_DETAILS_SPEAKERURL = "details_speaker_url";
	public static final String KEY_DETAILS_SPEAKERRSS = "details_speaker_rss";
	public static final String KEY_DETAILS_ELECTIONNUMBER = "details_election_number";
	public static final String KEY_DETAILS_ELECTIONNAME = "details_election_name";
	public static final String KEY_DETAILS_ELECTIONURL = "details_election_url";
	public static final String KEY_DETAILS_ELECTIONCITY = "details_election_city";

	// details groups
	public static final String KEY_DETAILS_GROUP_ROWID = "_id";
	public static final String KEY_DETAILS_GROUP_MEMBERID = "member_id";
	public static final String KEY_DETAILS_GROUP_TYPE = "type";
	public static final String KEY_DETAILS_GROUP_ID = "group_id";
	public static final String KEY_DETAILS_GROUP_TITLE = "title";
	public static final String KEY_DETAILS_GROUP_NAME = "name";
	public static final String KEY_DETAILS_GROUP_URL = "url";
	public static final String KEY_DETAILS_GROUP_RSS = "rss";

	// details websites
	public static final String KEY_DETAILS_WEBSITE_ROWID = "_id";
	public static final String KEY_DETAILS_WEBSITE_MEMBERID = "member_id";
	public static final String KEY_DETAILS_WEBSITE_TITLE = "title";
	public static final String KEY_DETAILS_WEBSITE_URL = "url";

	private Context context;

	private BaseDatabaseHelper dbHelper;

	/**
	 * Members database adapter constructor.
	 */
	public MembersDatabaseAdapter(Context context) {
		this.context = context;
	}

	/**
	 * Open connection to members database table.
	 */
	public MembersDatabaseAdapter open() throws SQLException {
		if (dbHelper == null) {
			dbHelper = new BaseDatabaseHelper(context);
		}
		if (dbHelper.database == null) {
			dbHelper.openDatabase();
		}

		return this;
	}

	/**
	 * Close database helper.
	 */
	public void close() {
		if (dbHelper != null) {
			dbHelper.close();
			dbHelper.database.close();
		}
	}

	/**
	 * Inserts a new members object.
	 */
	public long createMember(MembersObject membersObject) {
		ContentValues initialValues = createContentValues(membersObject);

		return dbHelper.database.insert(BaseDatabaseHelper.MEMBERS_TABLE_NAME, null, initialValues);
	}

	/**
	 * Inserts a new members group object.
	 */
	public long createMemberGroup(long memberId, int groupType, MembersDetailsGroupsObject groupObject) {
		ContentValues initialValues = createGroupContentValues(groupObject, memberId, groupType);

		return dbHelper.database.insert(BaseDatabaseHelper.MEMBERS_GROUPS_TABLE_NAME, null, initialValues);
	}

	/**
	 * Inserts a new members website object.
	 */
	public long createMemberWebsite(long memberId, MembersDetailsWebsitesObject websiteObject) {
		ContentValues initialValues = createWebsiteContentValues(websiteObject, memberId);

		return dbHelper.database.insert(BaseDatabaseHelper.MEMBERS_WEBSITES_TABLE_NAME, null, initialValues);
	}

	/**
	 * Delete a members object in the database.
	 */
	public boolean deleteMember(long rowId) {
		return dbHelper.database.delete(BaseDatabaseHelper.MEMBERS_TABLE_NAME, KEY_ROWID + "=" + rowId, null) > 0;
	}

	/**
	 * Delete a members object in the database from the member id.
	 */
	public boolean deleteMember(String memberId) {
		return dbHelper.database.delete(BaseDatabaseHelper.MEMBERS_TABLE_NAME, KEY_ID + "=" + memberId, null) > 0;
	}

	/**
	 * Drop the members table.
	 */
	public int deleteAllMembers() {
		return dbHelper.database.delete(BaseDatabaseHelper.MEMBERS_TABLE_NAME, "1", null);
	}

	/**
	 * Database table cursor method to fetch all members.
	 */
	public Cursor fetchAllMembers() {
		return dbHelper.database.query(BaseDatabaseHelper.MEMBERS_TABLE_NAME, new String[] { KEY_ROWID, KEY_ID, KEY_NAME, KEY_BIOURL, KEY_INFOURL, KEY_INFOURLMORE, KEY_LAND,
				KEY_PHOTO, KEY_PHOTOLASTCHANGED, KEY_PHOTOCHANGEDDATETIME, KEY_LASTCHANGED, KEY_CHANGEDDATETIME, KEY_DETAILS_STATUS, KEY_DETAILS_EXITDATE, KEY_DETAILS_LASTNAME,
				KEY_DETAILS_FIRSTNAME, KEY_DETAILS_TITLE, KEY_DETAILS_COURSE, KEY_DETAILS_LOCAL, KEY_DETAILS_BIRTHDATE, KEY_DETAILS_RELIGION, KEY_DETAILS_DEGREE,
				KEY_DETAILS_HIGHEREDUCATION, KEY_DETAILS_PROFESSION, KEY_DETAILS_PROFESSIONFIELD, KEY_DETAILS_SEX, KEY_DETAILS_MARITALSTATUS, KEY_DETAILS_CHILDREN,
				KEY_DETAILS_FRACTION, KEY_DETAILS_PARTY, KEY_DETAILS_CITY, KEY_DETAILS_ELECTED, KEY_DETAILS_BIOURL, KEY_DETAILS_INFORMATION, KEY_DETAILS_MORE,
				KEY_DETAILS_HOMEPAGE, KEY_DETAILS_PHONE, KEY_DETAILS_DISCLOSUREINFORMATION, KEY_DETAILS_MEDIAPHOTO, KEY_DETAILS_MEDIAPHOTOCOPYRIGHT, KEY_DETAILS_SPEAKERURL,
				KEY_DETAILS_SPEAKERRSS, KEY_DETAILS_ELECTIONNUMBER, KEY_DETAILS_ELECTIONNAME, KEY_DETAILS_ELECTIONURL, KEY_DETAILS_ELECTIONCITY, KEY_PHOTO_STRING,
				KEY_DETAILS_MEDIAPHOTOSTRING }, null, null, null, null, KEY_NAME);
	}

	public Cursor fetchAllMembersOptimized() {
		dbHelper.database.releaseMemory();
		// photo_string
		final String QUERY = "SELECT DISTINCT _id, member_id, name, land, details_fraction FROM members ORDER BY name COLLATE LOCALIZED";

		Cursor cursor = dbHelper.database.rawQuery(QUERY, null);
		if (cursor != null) {
			cursor.moveToFirst();
		}
		dbHelper.database.releaseMemory();
		return cursor;

		// return database.query(BaseDatabaseHelper.MEMBERS_TABLE_NAME, new
		// String[]
		// {
		// KEY_ROWID, KEY_ID, KEY_NAME, KEY_LAND, KEY_DETAILS_FRACTION,
		// KEY_PHOTO_STRING
		// },
		// null, null, null, null, KEY_NAME);
	}

	/**
	 * Gets the active (status = 'Aktiv') members that belong to a certain
	 * fraction.
	 */
	public Cursor fetchAllFractions(String fractionName) {
		return dbHelper.database.query(BaseDatabaseHelper.MEMBERS_TABLE_NAME, new String[] { KEY_ROWID, KEY_ID, KEY_DETAILS_FRACTION }, KEY_DETAILS_FRACTION + "='" + fractionName
				+ "' AND " + KEY_DETAILS_STATUS + "='Aktiv'", null, null, null, null, null);
	}

	/**
	 * Gets the members that belong to a certain fraction (independent of
	 * status).
	 */
	public Cursor fetchFractionMembers(String fraction) {
		return dbHelper.database.query(BaseDatabaseHelper.MEMBERS_TABLE_NAME, new String[] { KEY_ROWID, KEY_ID, KEY_NAME, KEY_LAND, KEY_LASTCHANGED, KEY_CHANGEDDATETIME,
				KEY_DETAILS_STATUS, KEY_DETAILS_EXITDATE, KEY_DETAILS_LASTNAME, KEY_DETAILS_FIRSTNAME, KEY_DETAILS_TITLE, KEY_DETAILS_FRACTION, KEY_DETAILS_PARTY,
				KEY_DETAILS_CITY, KEY_DETAILS_ELECTED, KEY_DETAILS_BIOURL, KEY_DETAILS_INFORMATION, KEY_DETAILS_MORE, KEY_DETAILS_HOMEPAGE, KEY_DETAILS_PHONE,
				KEY_DETAILS_DISCLOSUREINFORMATION, KEY_DETAILS_MEDIAPHOTO, KEY_DETAILS_MEDIAPHOTOCOPYRIGHT, KEY_DETAILS_SPEAKERURL, KEY_DETAILS_SPEAKERRSS,
				KEY_DETAILS_ELECTIONNUMBER, KEY_DETAILS_ELECTIONNAME, KEY_DETAILS_ELECTIONURL, KEY_DETAILS_ELECTIONCITY }, KEY_DETAILS_FRACTION + "='" + fraction + "'", null,
				null, null, KEY_NAME + " COLLATE LOCALIZED", null);
	}

	public Cursor fetchGroupMembers(String committeeId, int groupType) {
		final String MY_QUERY = "SELECT DISTINCT a.member_id, a.details_course, a.details_first_name, a.details_last_name, a.details_title, a.details_fraction, a.details_media_photo_string FROM members a INNER JOIN member_groups b ON a._id=b.member_id WHERE b.group_id=? AND b.type=? ORDER BY a.details_last_name COLLATE LOCALIZED, a.details_first_name COLLATE LOCALIZED";

		Cursor cursor = dbHelper.database.rawQuery(MY_QUERY, new String[] { committeeId, String.valueOf(groupType) });

		if (cursor != null) {
			cursor.moveToFirst();
		}

		return cursor;
	}

	/**
	 * Gets the active (status = 'Aktiv') members that belong to a certain
	 * fraction and committee.
	 */
	public Cursor fetchAllFractionsCommittee(String fractionName, String committeeId) {
		final String MY_QUERY = "SELECT DISTINCT a.member_id, a.details_fraction FROM members a INNER JOIN member_groups b ON a._id=b.member_id WHERE a.details_fraction=? AND b.group_id=? AND a.details_status='Aktiv' ORDER BY a.details_last_name COLLATE LOCALIZED, a.details_first_name COLLATE LOCALIZED";

		return dbHelper.database.rawQuery(MY_QUERY, new String[] { fractionName, committeeId });
	}

	/**
	 * Gets the count of members of committee.
	 */
	public Cursor fetchCommitteeMembersCount(String committeeId) {
		final String MY_QUERY = "SELECT count(distinct(a._id)) as members_count FROM members a INNER JOIN member_groups b ON a._id=b.member_id WHERE b.group_id=? AND a.details_status='Aktiv' AND b.type NOT IN ("
				+ MembersSynchronization.GROUP_TYPE_DEPUTYMEMBER + "," + MembersSynchronization.GROUP_TYPE_OTHER_DEPUTYMEMBER + ")";

		return dbHelper.database.rawQuery(MY_QUERY, new String[] { committeeId });
	}

	/**
	 * Gets the active (status = 'Aktiv') members that belong to a certain
	 * fraction and committee.
	 */
	public Cursor fetchAllMembersByFractionsCommittee(String fractionName, String committeeId, int type) {
		final String MY_QUERY = "SELECT DISTINCT a.member_id, a.land, a.name, a.details_first_name, a.details_last_name, a.details_title, a.details_fraction, a.details_media_photo_string FROM members a INNER JOIN member_groups b ON a._id=b.member_id WHERE a.details_fraction=? AND b.group_id=? AND b.type=? AND a.details_status='Aktiv' ORDER BY a.details_last_name COLLATE LOCALIZED, a.details_first_name COLLATE LOCALIZED";
		Cursor cursor = dbHelper.database.rawQuery(MY_QUERY, new String[] { fractionName, committeeId, String.valueOf(type) });

		if (cursor != null) {
			cursor.moveToFirst();
		}

		return cursor;
	}

	/**
	 * Gets the active (status = 'Aktiv') members that belong to a certain
	 * fraction and committee.
	 */
	// public Cursor fetchAllMembersByFractionsCommitteePresidents(String
	// fractionName, String committeeId, int type)
	// {
	// final String MY_QUERY =
	// "SELECT COUNT(DISTINCT a.member_id) FROM members a INNER JOIN member_groups b ON a._id=b.member_id WHERE a.details_fraction=? AND b.group_id=? AND b.type=? AND a.details_status='Aktiv'";
	// Cursor cursor = dbHelper.database.rawQuery(MY_QUERY, new
	// String[]{fractionName, committeeId, String.valueOf(type)});
	//
	// if (cursor != null)
	// {
	// cursor.moveToFirst();
	// }
	//
	// return cursor;
	// }

	/**
	 * Gets the active (status = 'Aktiv') members that belong to a certain
	 * fraction and committee.
	 */
	public Cursor fetchAllOtherMembersByFractionsCommittee(String fractionName, String committeeId, int type) {
		final String MY_QUERY = "SELECT DISTINCT a.member_id, a.land, a.name, a.details_first_name, a.details_last_name, a.details_title, a.details_fraction, a.details_media_photo_string FROM members a INNER JOIN member_groups b ON a._id=b.member_id WHERE a.details_fraction=? AND b.group_id=? AND b.type!=? AND a.details_status='Aktiv' ORDER BY a.details_last_name COLLATE LOCALIZED, a.details_first_name  COLLATE LOCALIZED";
		Cursor cursor = dbHelper.database.rawQuery(MY_QUERY, new String[] { fractionName, committeeId, String.valueOf(type) });

		if (cursor != null) {
			cursor.moveToFirst();
		}

		return cursor;
	}

	/**
	 * Gets the members that belong to a certain fraction (independent of
	 * status).
	 */
	public Cursor fetchFractionMembersTwo(String fraction1, String fraction2) {
		return dbHelper.database.query(BaseDatabaseHelper.MEMBERS_TABLE_NAME, new String[] { KEY_ROWID, KEY_ID, KEY_NAME, KEY_BIOURL, KEY_INFOURL, KEY_INFOURLMORE, KEY_LAND,
				KEY_PHOTO, KEY_PHOTOLASTCHANGED, KEY_PHOTOCHANGEDDATETIME, KEY_LASTCHANGED, KEY_CHANGEDDATETIME, KEY_DETAILS_STATUS, KEY_DETAILS_EXITDATE, KEY_DETAILS_LASTNAME,
				KEY_DETAILS_FIRSTNAME, KEY_DETAILS_TITLE, KEY_DETAILS_COURSE, KEY_DETAILS_LOCAL, KEY_DETAILS_BIRTHDATE, KEY_DETAILS_RELIGION, KEY_DETAILS_DEGREE,
				KEY_DETAILS_HIGHEREDUCATION, KEY_DETAILS_PROFESSION, KEY_DETAILS_PROFESSIONFIELD, KEY_DETAILS_SEX, KEY_DETAILS_MARITALSTATUS, KEY_DETAILS_CHILDREN,
				KEY_DETAILS_FRACTION, KEY_DETAILS_PARTY, KEY_DETAILS_CITY, KEY_DETAILS_ELECTED, KEY_DETAILS_BIOURL, KEY_DETAILS_INFORMATION, KEY_DETAILS_MORE,
				KEY_DETAILS_HOMEPAGE, KEY_DETAILS_PHONE, KEY_DETAILS_DISCLOSUREINFORMATION, KEY_DETAILS_MEDIAPHOTO, KEY_DETAILS_MEDIAPHOTOCOPYRIGHT, KEY_DETAILS_SPEAKERURL,
				KEY_DETAILS_SPEAKERRSS, KEY_DETAILS_ELECTIONNUMBER, KEY_DETAILS_ELECTIONNAME, KEY_DETAILS_ELECTIONURL, KEY_DETAILS_ELECTIONCITY, KEY_PHOTO_STRING,
				KEY_DETAILS_MEDIAPHOTOSTRING }, KEY_DETAILS_FRACTION + "='" + fraction1 + "' OR " + KEY_DETAILS_FRACTION + "='" + fraction2 + "'", null, null, null, KEY_NAME
				+ " COLLATE LOCALIZED", null);
	}

	/**
	 * Gets the members that belong to a certain city (independent of status).
	 */
	public Cursor fetchCityMembers(String city) {
		return dbHelper.database.query(BaseDatabaseHelper.MEMBERS_TABLE_NAME, new String[] { KEY_ROWID, KEY_ID, KEY_NAME, KEY_BIOURL, KEY_INFOURL, KEY_INFOURLMORE, KEY_LAND,
				KEY_PHOTO, KEY_PHOTOLASTCHANGED, KEY_PHOTOCHANGEDDATETIME, KEY_LASTCHANGED, KEY_CHANGEDDATETIME, KEY_DETAILS_STATUS, KEY_DETAILS_EXITDATE, KEY_DETAILS_LASTNAME,
				KEY_DETAILS_FIRSTNAME, KEY_DETAILS_TITLE, KEY_DETAILS_COURSE, KEY_DETAILS_LOCAL, KEY_DETAILS_BIRTHDATE, KEY_DETAILS_RELIGION, KEY_DETAILS_DEGREE,
				KEY_DETAILS_HIGHEREDUCATION, KEY_DETAILS_PROFESSION, KEY_DETAILS_PROFESSIONFIELD, KEY_DETAILS_SEX, KEY_DETAILS_MARITALSTATUS, KEY_DETAILS_CHILDREN,
				KEY_DETAILS_FRACTION, KEY_DETAILS_PARTY, KEY_DETAILS_CITY, KEY_DETAILS_ELECTED, KEY_DETAILS_BIOURL, KEY_DETAILS_INFORMATION, KEY_DETAILS_MORE,
				KEY_DETAILS_HOMEPAGE, KEY_DETAILS_PHONE, KEY_DETAILS_DISCLOSUREINFORMATION, KEY_DETAILS_ELECTIONNUMBER, KEY_DETAILS_ELECTIONNAME, KEY_DETAILS_ELECTIONURL,
				KEY_DETAILS_ELECTIONCITY }, KEY_DETAILS_CITY + "='" + city + "'", null, null, null, KEY_NAME + " COLLATE LOCALIZED", null);
	}

	/**
	 * Gets the members that belong to a certain election (independent of
	 * status).
	 */
	public Cursor fetchElectionMembers(String election) {
		return dbHelper.database.query(BaseDatabaseHelper.MEMBERS_TABLE_NAME, new String[] { KEY_ROWID, KEY_ID, KEY_NAME, KEY_BIOURL, KEY_INFOURL, KEY_INFOURLMORE, KEY_LAND,
				KEY_PHOTO, KEY_PHOTOLASTCHANGED, KEY_PHOTOCHANGEDDATETIME, KEY_LASTCHANGED, KEY_CHANGEDDATETIME, KEY_DETAILS_STATUS, KEY_DETAILS_EXITDATE, KEY_DETAILS_LASTNAME,
				KEY_DETAILS_FIRSTNAME, KEY_DETAILS_TITLE, KEY_DETAILS_COURSE, KEY_DETAILS_LOCAL, KEY_DETAILS_BIRTHDATE, KEY_DETAILS_RELIGION, KEY_DETAILS_DEGREE,
				KEY_DETAILS_HIGHEREDUCATION, KEY_DETAILS_PROFESSION, KEY_DETAILS_PROFESSIONFIELD, KEY_DETAILS_SEX, KEY_DETAILS_MARITALSTATUS, KEY_DETAILS_CHILDREN,
				KEY_DETAILS_FRACTION, KEY_DETAILS_PARTY, KEY_DETAILS_CITY, KEY_DETAILS_ELECTED, KEY_DETAILS_BIOURL, KEY_DETAILS_INFORMATION, KEY_DETAILS_MORE,
				KEY_DETAILS_HOMEPAGE, KEY_DETAILS_PHONE, KEY_DETAILS_DISCLOSUREINFORMATION, KEY_DETAILS_ELECTIONNUMBER, KEY_DETAILS_ELECTIONNAME, KEY_DETAILS_ELECTIONURL,
				KEY_DETAILS_ELECTIONCITY }, KEY_DETAILS_ELECTIONNUMBER + "='" + election + "'", null, null, null, KEY_NAME + " COLLATE LOCALIZED", null);
	}

	/**
	 * dbHelper.database table cursor method to fetch one members from a specfic
	 * row.
	 */
	public Cursor fetchMembers(long rowId) throws SQLException {
		Cursor mCursor = dbHelper.database.query(true, BaseDatabaseHelper.MEMBERS_TABLE_NAME, new String[] { KEY_ROWID, KEY_ID, KEY_NAME, KEY_BIOURL, KEY_INFOURL, KEY_INFOURLMORE,
				KEY_LAND, KEY_PHOTO, KEY_PHOTOLASTCHANGED, KEY_PHOTOCHANGEDDATETIME, KEY_LASTCHANGED, KEY_CHANGEDDATETIME, KEY_DETAILS_STATUS, KEY_DETAILS_EXITDATE,
				KEY_DETAILS_LASTNAME, KEY_DETAILS_FIRSTNAME, KEY_DETAILS_TITLE, KEY_DETAILS_COURSE, KEY_DETAILS_LOCAL, KEY_DETAILS_BIRTHDATE, KEY_DETAILS_RELIGION,
				KEY_DETAILS_DEGREE, KEY_DETAILS_HIGHEREDUCATION, KEY_DETAILS_PROFESSION, KEY_DETAILS_PROFESSIONFIELD, KEY_DETAILS_SEX, KEY_DETAILS_MARITALSTATUS,
				KEY_DETAILS_CHILDREN, KEY_DETAILS_FRACTION, KEY_DETAILS_PARTY, KEY_DETAILS_CITY, KEY_DETAILS_ELECTED, KEY_DETAILS_BIOURL, KEY_DETAILS_INFORMATION,
				KEY_DETAILS_MORE, KEY_DETAILS_HOMEPAGE, KEY_DETAILS_PHONE, KEY_DETAILS_DISCLOSUREINFORMATION, KEY_DETAILS_MEDIAPHOTO, KEY_DETAILS_MEDIAPHOTOCOPYRIGHT,
				KEY_DETAILS_SPEAKERURL, KEY_DETAILS_SPEAKERRSS, KEY_DETAILS_ELECTIONNUMBER, KEY_DETAILS_ELECTIONNAME, KEY_DETAILS_ELECTIONURL, KEY_DETAILS_ELECTIONCITY,
				KEY_PHOTO_STRING, KEY_DETAILS_MEDIAPHOTOSTRING }, KEY_ROWID + "=" + rowId, null, null, null, KEY_NAME + " COLLATE LOCALIZED", null);

		if (mCursor != null) {
			mCursor.moveToFirst();
		}

		return mCursor;
	}

	/**
	 * Database table cursor method to fetch all member groups.
	 */
	public Cursor fetchAllMemberGroups(int memberId) {
		return dbHelper.database.query(BaseDatabaseHelper.MEMBERS_GROUPS_TABLE_NAME, new String[] { KEY_DETAILS_GROUP_MEMBERID, KEY_DETAILS_GROUP_TYPE, KEY_DETAILS_GROUP_ID,
				KEY_DETAILS_GROUP_TITLE, KEY_DETAILS_GROUP_NAME, KEY_DETAILS_GROUP_URL, KEY_DETAILS_GROUP_RSS, KEY_PHOTO_STRING, KEY_DETAILS_MEDIAPHOTOSTRING },
				KEY_DETAILS_GROUP_MEMBERID + "=" + memberId, null, null, null, KEY_DETAILS_GROUP_NAME, null);
	}

	/**
	 * Database table cursor method to fetch all member websites.
	 */
	public Cursor fetchAllMemberWebsites(int memberId) {
		return dbHelper.database.query(BaseDatabaseHelper.MEMBERS_WEBSITES_TABLE_NAME, new String[] { KEY_DETAILS_WEBSITE_MEMBERID, KEY_DETAILS_WEBSITE_TITLE,
				KEY_DETAILS_WEBSITE_URL }, KEY_DETAILS_WEBSITE_MEMBERID + "=" + memberId, null, null, null, null, null);
	}

	public Cursor fetchAllCities(String city) {
		return dbHelper.database.query(BaseDatabaseHelper.MEMBERS_TABLE_NAME, new String[] { KEY_ROWID, KEY_ID, KEY_LAND }, KEY_LAND + "='" + city + "' AND " + KEY_DETAILS_STATUS
				+ "='Aktiv'", null, null, null, null, null);
	}

	/**
	 * Create a members content value object from the general members object.
	 * 
	 * Needed for inserting a members object.
	 */
	private ContentValues createContentValues(MembersObject membersObject) {
		ContentValues values = new ContentValues();

		values.put(KEY_ID, membersObject.getId());
		values.put(KEY_NAME, membersObject.getName());
		values.put(KEY_BIOURL, membersObject.getBioURL());
		values.put(KEY_INFOURL, membersObject.getInfoURL());
		values.put(KEY_INFOURLMORE, membersObject.getInfoURLMore());
		values.put(KEY_LAND, String.valueOf(membersObject.getLand()));
		values.put(KEY_PHOTO, String.valueOf(membersObject.getPhoto()));
		values.put(KEY_PHOTO_STRING, membersObject.getPhotoString());
		values.put(KEY_PHOTOLASTCHANGED, String.valueOf(membersObject.getPhotoLastChanged()));
		values.put(KEY_PHOTOCHANGEDDATETIME, String.valueOf(membersObject.getPhotoChangedDateTime()));
		values.put(KEY_LASTCHANGED, String.valueOf(membersObject.getLastChanged()));
		values.put(KEY_CHANGEDDATETIME, String.valueOf(membersObject.getChangedDateTime()));

		MembersDetailsObject membersDetails = membersObject.getMembersDetails();
		if (membersDetails != null) {
			values.put(KEY_DETAILS_STATUS, membersDetails.getStatus());
			values.put(KEY_DETAILS_EXITDATE, membersDetails.getExitDate());
			values.put(KEY_DETAILS_LASTNAME, membersDetails.getLastName());
			values.put(KEY_DETAILS_FIRSTNAME, membersDetails.getFirstName());
			values.put(KEY_DETAILS_TITLE, membersDetails.getTitle());
			values.put(KEY_DETAILS_COURSE, membersDetails.getCourse());
			values.put(KEY_DETAILS_LOCAL, membersDetails.getLocal());
			values.put(KEY_DETAILS_BIRTHDATE, membersDetails.getBirthdate());
			values.put(KEY_DETAILS_RELIGION, membersDetails.getReligion());
			values.put(KEY_DETAILS_DEGREE, membersDetails.getDegree());
			values.put(KEY_DETAILS_HIGHEREDUCATION, membersDetails.getHigherEducation());
			values.put(KEY_DETAILS_PROFESSION, membersDetails.getProfession());
			values.put(KEY_DETAILS_PROFESSIONFIELD, membersDetails.getProfessionField());
			values.put(KEY_DETAILS_SEX, membersDetails.getSex());
			values.put(KEY_DETAILS_MARITALSTATUS, membersDetails.getMaritalStatus());
			values.put(KEY_DETAILS_CHILDREN, membersDetails.getChildren());
			values.put(KEY_DETAILS_FRACTION, membersDetails.getFraction());
			values.put(KEY_DETAILS_PARTY, membersDetails.getParty());
			values.put(KEY_DETAILS_CITY, membersDetails.getCity());
			values.put(KEY_DETAILS_ELECTED, membersDetails.getElected());
			values.put(KEY_DETAILS_BIOURL, membersDetails.getBioURL());
			values.put(KEY_DETAILS_INFORMATION, membersDetails.getInformation());
			values.put(KEY_DETAILS_MORE, membersDetails.getMore());
			values.put(KEY_DETAILS_HOMEPAGE, membersDetails.getHomepage());
			values.put(KEY_DETAILS_PHONE, membersDetails.getPhone());
			values.put(KEY_DETAILS_DISCLOSUREINFORMATION, membersDetails.getDisclosureInformation());
			values.put(KEY_DETAILS_MEDIAPHOTO, membersDetails.getMediaPhoto());
			values.put(KEY_DETAILS_MEDIAPHOTOSTRING, membersDetails.getMediaPhotoImageString());
			values.put(KEY_DETAILS_MEDIAPHOTOCOPYRIGHT, membersDetails.getMediaPhotoCopyright());
			values.put(KEY_DETAILS_SPEAKERURL, membersDetails.getSpeakerURL());
			values.put(KEY_DETAILS_SPEAKERRSS, membersDetails.getSpeakerRSS());
			values.put(KEY_DETAILS_ELECTIONNUMBER, membersDetails.getElectionNumber());
			values.put(KEY_DETAILS_ELECTIONNAME, membersDetails.getElectionName());
			values.put(KEY_DETAILS_ELECTIONURL, membersDetails.getElectionURL());
			values.put(KEY_DETAILS_ELECTIONCITY, membersDetails.getElectionCity());
		}

		return values;
	}

	/**
	 * Create a members content value object from the general members object.
	 * 
	 * Needed for inserting a members object.
	 */
	private ContentValues createGroupContentValues(MembersDetailsGroupsObject membersGroupObject, long membersObjectId, int groupType) {
		ContentValues values = new ContentValues();

		values.put(KEY_DETAILS_GROUP_MEMBERID, membersObjectId);
		values.put(KEY_DETAILS_GROUP_TYPE, groupType);
		values.put(KEY_DETAILS_GROUP_ID, membersGroupObject.getGroupId());
		values.put(KEY_DETAILS_GROUP_TITLE, membersGroupObject.getGroupTitle());
		values.put(KEY_DETAILS_GROUP_NAME, membersGroupObject.getGroupName());
		values.put(KEY_DETAILS_GROUP_URL, membersGroupObject.getGroupURL());
		values.put(KEY_DETAILS_GROUP_RSS, membersGroupObject.getGroupRSS());

		return values;
	}

	/**
	 * Create a members website content value object from the general members
	 * object.
	 * 
	 * Needed for inserting a members object.
	 */
	private ContentValues createWebsiteContentValues(MembersDetailsWebsitesObject membersWebsiteObject, long membersObjectId) {
		ContentValues values = new ContentValues();

		values.put(KEY_DETAILS_WEBSITE_MEMBERID, membersObjectId);
		values.put(KEY_DETAILS_WEBSITE_TITLE, membersWebsiteObject.getWebsiteTitle());
		values.put(KEY_DETAILS_WEBSITE_URL, membersWebsiteObject.getWebsiteURL());

		return values;
	}

	public Cursor getWebsites(int memberId) {
		return dbHelper.database.query(BaseDatabaseHelper.MEMBERS_WEBSITES_TABLE_NAME, new String[] { KEY_DETAILS_WEBSITE_MEMBERID, KEY_DETAILS_WEBSITE_TITLE,
				KEY_DETAILS_WEBSITE_URL }, KEY_DETAILS_WEBSITE_MEMBERID + "=" + memberId, null, null, null, null, null);
	}

	public Cursor getGroups(int memberId) {
		return dbHelper.database.query(BaseDatabaseHelper.MEMBERS_GROUPS_TABLE_NAME, new String[] { KEY_DETAILS_GROUP_MEMBERID, KEY_DETAILS_GROUP_TYPE, KEY_DETAILS_GROUP_ID,
				KEY_DETAILS_GROUP_TITLE, KEY_DETAILS_GROUP_NAME, KEY_DETAILS_GROUP_URL, KEY_DETAILS_GROUP_RSS }, KEY_DETAILS_GROUP_MEMBERID + "=" + memberId, null, null, null,
				null, null);
	}

	/**
	 * Get the member update data from the member id.
	 */
	public Cursor fetchMemberFromId(String memberId) {
		Cursor mCursor = dbHelper.database.query(true, BaseDatabaseHelper.MEMBERS_TABLE_NAME, new String[] { KEY_ID, KEY_PHOTOCHANGEDDATETIME, KEY_CHANGEDDATETIME, KEY_ROWID,
				KEY_PHOTO_STRING, KEY_DETAILS_MEDIAPHOTOSTRING, KEY_DETAILS_FIRSTNAME }, KEY_ID + "='" + memberId + "'", null, null, null, null, null);

		if (mCursor != null) {
			mCursor.moveToFirst();
		}

		return mCursor;
	}

	public String fetchMemberPhotoById(String memberId) {
		Cursor mCursor = dbHelper.database.query(true, BaseDatabaseHelper.MEMBERS_TABLE_NAME, new String[] { KEY_ID, KEY_PHOTO_STRING }, KEY_ID + "='" + memberId + "'", null,
				null, null, null, null);

		if (mCursor != null) {
			mCursor.moveToFirst();
		}
		String photoString = mCursor.getString(mCursor.getColumnIndex(MembersDatabaseAdapter.KEY_PHOTO_STRING));
		mCursor.close();
		return photoString;
	}
}
