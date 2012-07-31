package de.bundestag.android.storage;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import de.bundestag.android.constant.DataHolder;
import de.bundestag.android.parser.objects.CommitteesDetailsNewsDetailsObject;
import de.bundestag.android.parser.objects.CommitteesDetailsNewsObject;
import de.bundestag.android.parser.objects.CommitteesDetailsObject;
import de.bundestag.android.parser.objects.CommitteesObject;

/**
 * Committees database adapter.
 * 
 * Contains helper methods to interact with the committees table in the
 * database.
 */
public class CommitteesDatabaseAdapter {
	// Committee table
	public static final String KEY_ROWID = "_id";
	public static final String KEY_ID = "committee_id";
	public static final String KEY_NAME = "name";
	public static final String KEY_COURSE = "course";
	public static final String KEY_TEASER = "teaser";
	public static final String KEY_LASTCHANGED = "last_changed";
	public static final String KEY_CHANGEDDATETIME = "changed_date_time";
	public static final String KEY_IMAGELASTCHANGED = "image_last_changed";
	public static final String KEY_IMAGECHANGEDDATETIME = "image_changed_date_time";
	public static final String KEY_DETAILSXML = "details_xml";
	public static final String KEY_DETAILS_NAME = "details_name";
	public static final String KEY_DETAILS_PHOTOURL = "details_photo_url";
	public static final String KEY_DETAILS_PHOTOSTRING = "details_photo_string";
	public static final String KEY_DETAILS_PHOTOCOPYRIGHT = "details_photo_copyright";
	public static final String KEY_DETAILS_DESCRIPTION = "description";

	// Committee news table
	public static final String KEY_NEWS_ROWID = "_id";
	public static final String KEY_NEWS_COMMITTEEID = "committee_id";
	public static final String KEY_NEWS_DATE = "date";
	public static final String KEY_NEWS_TITLE = "title";
	public static final String KEY_NEWS_TEASER = "teaser";
	public static final String KEY_NEWS_IMAGEURL = "image_url";
	public static final String KEY_NEWS_IMAGESTRING = "image_string";
	public static final String KEY_NEWS_IMAGECOPYRIGHT = "image_copyright";
	public static final String KEY_NEWS_DETAILSXML = "details_xml";
	public static final String KEY_NEWS_LASTCHANGED = "last_changed";

	public static final String KEY_NEWS_DETAILS_DATE = "details_date";
	public static final String KEY_NEWS_DETAILS_TITLE = "details_title";
	public static final String KEY_NEWS_DETAILS_TEXT = "details_text";
	public static final String KEY_NEWS_DETAILS_IMAGEURL = "details_image_url";
	public static final String KEY_NEWS_DETAILS_IMAGESTRING = "details_image_string";
	public static final String KEY_NEWS_DETAILS_IMAGECOPYRIGHT = "details_image_copyright";
	public static final String KEY_NEWS_DETAILS_IMAGELASTCHANGED = "details_image_last_changed";
	public static final String KEY_NEWS_DETAILS_IMAGECHANGEDDATETIME = "details_image_changed_date_time";

	private Context context;

	public BaseDatabaseHelper dbHelper;

	/**
	 * Committees database adapter constructor.
	 */
	public CommitteesDatabaseAdapter(Context context) {
		this.context = context;
	}

	/**
	 * Open connection to committees database table.
	 */
	public CommitteesDatabaseAdapter open() throws SQLException {
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
	 * Inserts a new committees object.
	 */
	public long createCommittee(CommitteesObject committeesObject) {
		ContentValues initialValues = createContentValues(committeesObject);

		return dbHelper.database.insert(BaseDatabaseHelper.COMMITTEES_TABLE_NAME, null, initialValues);
	}

	/**
	 * Delete a committees object in the database.
	 */
	public boolean deleteCommittee(long rowId) {
		return dbHelper.database.delete(BaseDatabaseHelper.COMMITTEES_TABLE_NAME, KEY_ROWID + "=" + rowId, null) > 0;
	}

	/**
	 * Delete a committees object from the committee id in the database.
	 */
	public boolean deleteCommittee(String committeeId) {
		return dbHelper.database.delete(BaseDatabaseHelper.COMMITTEES_TABLE_NAME, KEY_ID + "='" + committeeId + "'", null) > 0;
	}

	/**
	 * Drop the committees table.
	 */
	public int deleteAllCommittees() {
		return dbHelper.database.delete(BaseDatabaseHelper.COMMITTEES_TABLE_NAME, "1", null);
	}

	/**
	 * Database table cursor method to fetch all committees.
	 */
	public Cursor fetchAllCommittees() {
		return dbHelper.database.query(BaseDatabaseHelper.COMMITTEES_TABLE_NAME, new String[] { KEY_ROWID, KEY_ID, KEY_NAME, KEY_COURSE, KEY_TEASER, KEY_LASTCHANGED,
				KEY_CHANGEDDATETIME, KEY_IMAGELASTCHANGED, KEY_IMAGECHANGEDDATETIME, KEY_DETAILSXML, KEY_DETAILS_NAME, KEY_DETAILS_PHOTOURL, KEY_DETAILS_PHOTOCOPYRIGHT,
				KEY_DETAILS_DESCRIPTION, KEY_DETAILS_PHOTOSTRING }, null, null, null, null, KEY_COURSE);
	}

	/**
	 * Database table cursor method to fetch one committees from a specfic row.
	 */
	public Cursor fetchCommittees(long rowId) throws SQLException {
		Cursor mCursor = dbHelper.database.query(true, BaseDatabaseHelper.COMMITTEES_TABLE_NAME, new String[] { KEY_ROWID, KEY_ID, KEY_NAME, KEY_COURSE, KEY_TEASER,
				KEY_LASTCHANGED, KEY_CHANGEDDATETIME, KEY_IMAGELASTCHANGED, KEY_IMAGECHANGEDDATETIME, KEY_DETAILSXML, KEY_DETAILS_NAME, KEY_DETAILS_PHOTOURL,
				KEY_DETAILS_PHOTOCOPYRIGHT, KEY_DETAILS_DESCRIPTION, KEY_DETAILS_PHOTOSTRING }, KEY_ROWID + "=" + rowId, null, null, null, KEY_NAME, null);

		if (mCursor != null) {
			mCursor.moveToFirst();
		}

		return mCursor;
	}

	public Cursor fetchCommitteesById(String committeesId) {
		Cursor mCursor = dbHelper.database.query(true, BaseDatabaseHelper.COMMITTEES_TABLE_NAME, new String[] { KEY_ROWID, KEY_ID, KEY_NAME, KEY_COURSE, KEY_TEASER,
				KEY_LASTCHANGED, KEY_CHANGEDDATETIME, KEY_IMAGELASTCHANGED, KEY_IMAGECHANGEDDATETIME, KEY_DETAILSXML, KEY_DETAILS_NAME, KEY_DETAILS_PHOTOURL,
				KEY_DETAILS_PHOTOCOPYRIGHT, KEY_DETAILS_DESCRIPTION, KEY_DETAILS_PHOTOSTRING }, KEY_ID + "='" + committeesId + "'", null, null, null, null, null);

		if (mCursor != null) {
			mCursor.moveToFirst();
		}

		return mCursor;
	}

	/**
	 * Get the committee update data from the committee id.
	 */
	public Cursor getCommitteeFromId(String committeeId) {
		Cursor mCursor = dbHelper.database.query(true, BaseDatabaseHelper.COMMITTEES_TABLE_NAME, new String[] { KEY_ROWID, KEY_ID, KEY_CHANGEDDATETIME, KEY_IMAGECHANGEDDATETIME,
				KEY_DETAILS_PHOTOSTRING }, KEY_ID + "='" + committeeId + "'", null, null, null, null, null);

		if (mCursor != null) {
			mCursor.moveToFirst();
		}

		return mCursor;
	}

	/**
	 * Create a committees content value object from the general committees
	 * object.
	 * 
	 * Needed for inserting a committees object.
	 */
	private ContentValues createContentValues(CommitteesObject committeesObject) {
		ContentValues values = new ContentValues();

		values.put(KEY_ID, committeesObject.getId());
		values.put(KEY_NAME, committeesObject.getName());
		values.put(KEY_COURSE, committeesObject.getCourse());
		values.put(KEY_TEASER, committeesObject.getTeaser());
		values.put(KEY_LASTCHANGED, committeesObject.getLastChanged());
		values.put(KEY_CHANGEDDATETIME, committeesObject.getChangedDateTime());
		values.put(KEY_IMAGELASTCHANGED, committeesObject.getImageLastChanged());
		values.put(KEY_IMAGECHANGEDDATETIME, String.valueOf(committeesObject.getImageChangedDateTime()));
		values.put(KEY_DETAILSXML, committeesObject.getDetailsXML());

		CommitteesDetailsObject committeesDetails = committeesObject.getCommitteeDetails();

		if (committeesDetails != null) {
			values.put(KEY_DETAILS_NAME, committeesDetails.getName());
			values.put(KEY_DETAILS_PHOTOURL, committeesDetails.getPhotoURL());
			values.put(KEY_DETAILS_PHOTOSTRING, committeesDetails.getPhotoString());
			values.put(KEY_DETAILS_PHOTOCOPYRIGHT, committeesDetails.getPhotoCopyright());
			values.put(KEY_DETAILS_DESCRIPTION, committeesDetails.getDescription());
		}
		return values;
	}

	public long createCommitteeFirstNews(CommitteesDetailsNewsDetailsObject committeesDetailsNewsObject) {
		ContentValues initialValues = createNewsFirstContentValues(committeesDetailsNewsObject);

		return dbHelper.database.insert(BaseDatabaseHelper.COMMITTEES_NEWS_TABLE_NAME, null, initialValues);
	}

	public long createCommitteeNews(CommitteesDetailsNewsObject committeesDetailsNewsObject) {
		ContentValues initialValues = createNewsContentValues(committeesDetailsNewsObject);

		return dbHelper.database.insert(BaseDatabaseHelper.COMMITTEES_NEWS_TABLE_NAME, null, initialValues);
	}

	/**
	 * Drop the committees news table.
	 */
	public int deleteAllCommitteesNews() {
		return dbHelper.database.delete(BaseDatabaseHelper.COMMITTEES_NEWS_TABLE_NAME, KEY_NEWS_COMMITTEEID + "!='" + DataHolder.committeeRowId + "'", null);
	}

	public long deleteCommittees(String committeeId) {
		return dbHelper.database.delete(BaseDatabaseHelper.COMMITTEES_NEWS_TABLE_NAME, KEY_NEWS_COMMITTEEID + "='" + committeeId + "'", null);
	}

	/**
	 * Database table cursor method to fetch all committees.
	 */
	public Cursor fetchAllCommitteesNews() {
		return dbHelper.database.query(BaseDatabaseHelper.COMMITTEES_NEWS_TABLE_NAME, new String[] { KEY_NEWS_ROWID, KEY_NEWS_COMMITTEEID, KEY_NEWS_DATE, KEY_NEWS_TITLE,
				KEY_NEWS_TEASER, KEY_NEWS_IMAGEURL, KEY_NEWS_IMAGECOPYRIGHT, KEY_NEWS_DETAILSXML, KEY_NEWS_LASTCHANGED, KEY_NEWS_DETAILS_DATE, KEY_NEWS_DETAILS_TITLE,
				KEY_NEWS_DETAILS_TEXT, KEY_NEWS_IMAGESTRING }, null, null, null, null, null);
	}

	/**
	 * Database table cursor method to see if any news exists.
	 */
	public boolean existNews(String committeesIdString) {
		Cursor mCursor = dbHelper.database.query(BaseDatabaseHelper.COMMITTEES_NEWS_TABLE_NAME, new String[] { KEY_NEWS_ROWID }, KEY_NEWS_COMMITTEEID + "='" + committeesIdString
				+ "'", null, null, null, null, null);

		if (mCursor != null) {
			mCursor.moveToFirst();
		}
		boolean existNews = mCursor.getCount() > 0;
		mCursor.close();

		return existNews;
	}

	/**
	 * Fetch the committee news from the committee id.
	 */
	public Cursor fetchCommitteesNews(String committeesIdString) {
		return dbHelper.database.query(BaseDatabaseHelper.COMMITTEES_NEWS_TABLE_NAME, new String[] { KEY_NEWS_ROWID, KEY_NEWS_COMMITTEEID, KEY_NEWS_DATE, KEY_NEWS_TITLE,
				KEY_NEWS_TEASER, KEY_NEWS_IMAGEURL, KEY_NEWS_IMAGECOPYRIGHT, KEY_NEWS_DETAILSXML, KEY_NEWS_LASTCHANGED, KEY_NEWS_DETAILS_DATE, KEY_NEWS_DETAILS_TITLE,
				KEY_NEWS_DETAILS_TEXT, KEY_NEWS_DETAILS_IMAGEURL, KEY_NEWS_DETAILS_IMAGECOPYRIGHT, KEY_NEWS_DETAILS_IMAGESTRING, KEY_NEWS_DETAILS_IMAGELASTCHANGED,
				KEY_NEWS_DETAILS_IMAGECHANGEDDATETIME, KEY_NEWS_IMAGESTRING }, KEY_NEWS_COMMITTEEID + "='" + committeesIdString + "'", null, null, null, null, null);
	}

	public int countCommitteesNews(int committeesId) {
		Cursor cursor = dbHelper.database.rawQuery("Select " + "Committees." + KEY_ID + " from " +

		BaseDatabaseHelper.COMMITTEES_TABLE_NAME + " As " + "Committees" + " JOIN " + BaseDatabaseHelper.COMMITTEES_NEWS_TABLE_NAME + " As " + "News" + " on " + "Committees."
				+ KEY_ID + "=" + "News." + KEY_NEWS_COMMITTEEID + " Where " + "Committees." + KEY_ROWID + "=" + committeesId, null);

		int count = cursor.getCount();
		cursor.close();

		return count;
	}

	/**
	 * Fetch the committee news from the row id.
	 */
	public Cursor fetchCommitteesNews(int committeesId) {
		return dbHelper.database.query(BaseDatabaseHelper.COMMITTEES_NEWS_TABLE_NAME, new String[] { KEY_NEWS_ROWID, KEY_NEWS_COMMITTEEID, KEY_NEWS_DATE, KEY_NEWS_TITLE,
				KEY_NEWS_TEASER, KEY_NEWS_IMAGEURL, KEY_NEWS_IMAGECOPYRIGHT, KEY_NEWS_DETAILSXML, KEY_NEWS_LASTCHANGED, KEY_NEWS_DETAILS_DATE, KEY_NEWS_DETAILS_TITLE,
				KEY_NEWS_DETAILS_TEXT, KEY_NEWS_IMAGESTRING }, KEY_NEWS_COMMITTEEID + "!='" + DataHolder.committeeRowId + "'", null, null, null, null);
	}

	public Cursor fetchCommitteesNewsByString(String committeesId) {
		return dbHelper.database.query(BaseDatabaseHelper.COMMITTEES_NEWS_TABLE_NAME, new String[] { KEY_NEWS_ROWID, KEY_NEWS_COMMITTEEID, KEY_NEWS_DATE, KEY_NEWS_TITLE,
				KEY_NEWS_TEASER, KEY_NEWS_IMAGEURL, KEY_NEWS_IMAGECOPYRIGHT, KEY_NEWS_DETAILSXML, KEY_NEWS_LASTCHANGED, KEY_NEWS_DETAILS_DATE, KEY_NEWS_DETAILS_TITLE,
				KEY_NEWS_DETAILS_TEXT, KEY_NEWS_IMAGESTRING }, KEY_NEWS_COMMITTEEID + "!='" + DataHolder.committeeRowId + "' AND " + KEY_NEWS_COMMITTEEID + "=='" + committeesId
				+ "'", null, null, null, null);
	}

	/**
	 * Create a committees content value object from the general committees
	 * object.
	 * 
	 * Needed for inserting a committees object.
	 */
	private ContentValues createNewsContentValues(CommitteesDetailsNewsObject committeesDetailsNewsObject) {
		ContentValues values = new ContentValues();

		values.put(KEY_NEWS_DATE, committeesDetailsNewsObject.getDate());
		values.put(KEY_NEWS_COMMITTEEID, committeesDetailsNewsObject.getCommitteeId());
		values.put(KEY_NEWS_TITLE, committeesDetailsNewsObject.getTitle());
		values.put(KEY_NEWS_TEASER, committeesDetailsNewsObject.getTeaser());
		values.put(KEY_NEWS_IMAGEURL, committeesDetailsNewsObject.getImageURL());
		values.put(KEY_NEWS_IMAGESTRING, committeesDetailsNewsObject.getImageString());
		values.put(KEY_NEWS_IMAGECOPYRIGHT, committeesDetailsNewsObject.getImageCopyright());
		values.put(KEY_NEWS_DETAILSXML, committeesDetailsNewsObject.getDetailsXML());
		values.put(KEY_NEWS_LASTCHANGED, committeesDetailsNewsObject.getLastChanged());

		CommitteesDetailsNewsDetailsObject committeesNewsDetails = committeesDetailsNewsObject.getNewsDetails();

		if (committeesNewsDetails != null) {
			values.put(KEY_NEWS_DETAILS_DATE, committeesNewsDetails.getDate());
			values.put(KEY_NEWS_DETAILS_TITLE, committeesNewsDetails.getTitle());
			values.put(KEY_NEWS_DETAILS_TEXT, committeesNewsDetails.getText());
			values.put(KEY_NEWS_DETAILS_IMAGEURL, committeesNewsDetails.getImageURL());
			values.put(KEY_NEWS_DETAILS_IMAGECOPYRIGHT, committeesNewsDetails.getImageCopyright());
			values.put(KEY_NEWS_DETAILS_IMAGESTRING, committeesNewsDetails.getImageString());
			values.put(KEY_NEWS_DETAILS_IMAGELASTCHANGED, committeesNewsDetails.getImageLastChanged());
			values.put(KEY_NEWS_DETAILS_IMAGECHANGEDDATETIME, committeesNewsDetails.getImageChangedDateTime());
		}

		return values;
	}

	private ContentValues createNewsFirstContentValues(CommitteesDetailsNewsDetailsObject committeesDetailsNewsObject) {
		ContentValues values = new ContentValues();

		values.put(KEY_NEWS_DATE, committeesDetailsNewsObject.getDate());
		values.put(KEY_NEWS_COMMITTEEID, DataHolder.committeeRowId);
		values.put(KEY_NEWS_DETAILS_TITLE, committeesDetailsNewsObject.getTitle());
		values.put(KEY_NEWS_IMAGEURL, committeesDetailsNewsObject.getImageURL());
		values.put(KEY_NEWS_IMAGESTRING, committeesDetailsNewsObject.getImageString());
		values.put(KEY_NEWS_IMAGECOPYRIGHT, committeesDetailsNewsObject.getImageCopyright());
		values.put(KEY_NEWS_DETAILS_TEXT, committeesDetailsNewsObject.getText());

		return values;
	}

	/**
	 * Database table cursor method to fetch one news details from a specfic
	 * row.
	 */
	public Cursor fetchCommitteesDetailsNewsDetails(long rowId) throws SQLException {
		Cursor mCursor = dbHelper.database.query(true, BaseDatabaseHelper.COMMITTEES_NEWS_TABLE_NAME, new String[] { KEY_NEWS_ROWID, KEY_NEWS_COMMITTEEID, KEY_NEWS_DETAILS_DATE,
				KEY_NEWS_DETAILS_TITLE, KEY_NEWS_IMAGEURL, KEY_NEWS_IMAGECOPYRIGHT, KEY_NEWS_DETAILS_TEXT, KEY_NEWS_DETAILS_IMAGEURL, KEY_NEWS_DETAILS_IMAGESTRING,
				KEY_NEWS_DETAILS_IMAGECOPYRIGHT, KEY_NEWS_DETAILS_IMAGELASTCHANGED, KEY_NEWS_DETAILS_IMAGECHANGEDDATETIME, KEY_NEWS_IMAGESTRING, KEY_NEWS_DETAILSXML },
				KEY_NEWS_ROWID + "=" + rowId, null, null, null, null, null);

		if (mCursor != null) {
			mCursor.moveToFirst();
		}

		return mCursor;
	}

	/**
	 * Return the committee news that belongs to a committee.
	 */
	public Cursor getCommitteeNewsFromIdString(String committeeIdString) {
		Cursor mCursor = dbHelper.database.query(true, BaseDatabaseHelper.COMMITTEES_NEWS_TABLE_NAME, new String[] { KEY_NEWS_ROWID, KEY_NEWS_COMMITTEEID, KEY_NEWS_DETAILS_DATE,
				KEY_NEWS_DETAILS_TITLE, KEY_NEWS_IMAGEURL, KEY_NEWS_IMAGECOPYRIGHT, KEY_NEWS_DETAILS_TEXT, KEY_NEWS_DETAILS_IMAGEURL, KEY_NEWS_DETAILS_IMAGESTRING,
				KEY_NEWS_DETAILS_IMAGECOPYRIGHT, KEY_NEWS_DETAILS_IMAGELASTCHANGED, KEY_NEWS_DETAILS_IMAGECHANGEDDATETIME, KEY_NEWS_IMAGESTRING, KEY_NEWS_LASTCHANGED },
				KEY_NEWS_COMMITTEEID + "='" + committeeIdString + "'", null, null, null, null, null);
		// KEY_NEWS_ROWID + "='" + committeeIdString + "'", null, null, null,
		// null, null);

		if (mCursor != null) {
			mCursor.moveToFirst();
		}

		return mCursor;
	}

	public long updateCommitteeNews(int newsRowId, CommitteesDetailsNewsDetailsObject committeesNewsDetails) {
		ContentValues values = new ContentValues();

		values.put(KEY_NEWS_DETAILS_DATE, committeesNewsDetails.getDate());
		values.put(KEY_NEWS_DETAILS_TITLE, committeesNewsDetails.getTitle());
		values.put(KEY_NEWS_DETAILS_TEXT, committeesNewsDetails.getText());
		values.put(KEY_NEWS_DETAILS_IMAGEURL, committeesNewsDetails.getImageURL());
		values.put(KEY_NEWS_DETAILS_IMAGECOPYRIGHT, committeesNewsDetails.getImageCopyright());
		values.put(KEY_NEWS_DETAILS_IMAGESTRING, committeesNewsDetails.getImageString());
		values.put(KEY_NEWS_DETAILS_IMAGELASTCHANGED, committeesNewsDetails.getImageLastChanged());
		values.put(KEY_NEWS_DETAILS_IMAGECHANGEDDATETIME, committeesNewsDetails.getImageChangedDateTime());

		return dbHelper.database.update(BaseDatabaseHelper.COMMITTEES_NEWS_TABLE_NAME, values, KEY_NEWS_ROWID + "=" + newsRowId, null);
	}
}
