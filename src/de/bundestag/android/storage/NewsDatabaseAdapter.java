package de.bundestag.android.storage;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import de.bundestag.android.parser.objects.NewsDetailsObject;
import de.bundestag.android.parser.objects.NewsListObject;
import de.bundestag.android.parser.objects.NewsObject;
import de.bundestag.android.synchronization.NewsSynchronization;

/**
 * News database adapter.
 * 
 * Contains helper methods to interact with the news table in the database.
 */
public class NewsDatabaseAdapter {
	public static final String KEY_ROWID = "_id";

	public static final String KEY_LISTID = "list_id";
	public static final String KEY_TYPE = "type";
	public static final String KEY_STARTTEASER = "startteaser";
	public static final String KEY_STATUS = "status";
	public static final String KEY_DATE = "date";
	public static final String KEY_TITLE = "title";
	public static final String KEY_TEASER = "teaser";
	public static final String KEY_IMAGEURL = "image_url";
	public static final String KEY_IMAGECOPYRIGHT = "image_copyright";
	public static final String KEY_IMAGE_STRING = "image_string";
	public static final String KEY_DETAILSXML = "details_xml";
	public static final String KEY_LASTCHANGED = "last_changed";
	public static final String KEY_CHANGEDDATETIME = "changed_date_time";
	
	public static final String KEY_VIDEO_URL = "video_url";
	public static final String KEY_VIDEO_STREAMURL = "video_stream_url";

	public static final String KEY_DETAILS_DATE = "details_date";
	public static final String KEY_DETAILS_TITLE = "details_title";
	public static final String KEY_DETAILS_IMAGEURL = "details_image_url";
	public static final String KEY_DETAILS_IMAGE_GROSS_URL = "detail_image_gross_url";
	public static final String KEY_DETAILS_IMAGECOPYRIGHT = "details_image_copyright";
	public static final String KEY_DETAILS_IMAGELASTCHANGED = "details_image_last_changed";
	public static final String KEY_DETAILS_IMAGECHANGEDDATETIME = "details_image_changed_date_time";
	public static final String KEY_DETAILS_IMAGE_STRING = "details_image_string";
	public static final String KEY_DETAILS_TEXT = "details_text";

	// News article list
	public static final String KEY_NEWSLIST_ROWID = "_id";
	public static final String KEY_NEWSLIST_TYPE = "type";
	public static final String KEY_NEWSLIST_TITLE = "title";
	public static final String KEY_NEWSLIST_PARENTNEWSID = "parent_news_id";

	private Context context;

	private BaseDatabaseHelper dbHelper;

	/**
	 * News database adapter constructor.
	 */
	public NewsDatabaseAdapter(Context context) {
		this.context = context;
	}

	/**
	 * Open connection to news database table.
	 */
	public NewsDatabaseAdapter open() {
		
		try{
			
			if (dbHelper == null) {
				dbHelper = new BaseDatabaseHelper(context);
			}
			if (dbHelper.database == null) {
				dbHelper.openDatabase();
			}
		}catch (Exception e) {
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
	 * Inserts a new news object.
	 */
	public long createNews(NewsObject newsObject) {
		ContentValues initialValues = createContentValues(newsObject);

		return dbHelper.database.insert(BaseDatabaseHelper.NEWS_TABLE_NAME, null, initialValues);
	}

	/**
	 * Inserts a new news list object.
	 */
	public long createNewsList(NewsListObject newsListObject) {
		ContentValues initialValues = createNewsListValues(newsListObject);

		return dbHelper.database.insert(BaseDatabaseHelper.NEWS_LIST_TABLE_NAME, null, initialValues);
	}

	/**
	 * Drop the news table.
	 */
	public int deleteAllNews() {
		return dbHelper.database.delete(BaseDatabaseHelper.NEWS_TABLE_NAME, "1", null) + dbHelper.database.delete(BaseDatabaseHelper.NEWS_LIST_TABLE_NAME, "1", null);
	}

	/**
	 * Drop the news table.
	 */
	public int deleteNewsNews() {
		return dbHelper.database.delete(BaseDatabaseHelper.NEWS_TABLE_NAME, KEY_TYPE + "=" + NewsSynchronization.NEWS_TYPE_NORMAL, null)
				+ dbHelper.database.delete(BaseDatabaseHelper.NEWS_TABLE_NAME, KEY_TYPE + "=" + NewsSynchronization.NEWS_TYPE_LIST, null)
				+ dbHelper.database.delete(BaseDatabaseHelper.NEWS_LIST_TABLE_NAME, "1", null);
	}

	/**
	 * Drop the debate news table.
	 */
	public int deleteDebateNews() {
		return dbHelper.database.delete(BaseDatabaseHelper.NEWS_TABLE_NAME, KEY_TYPE + "=" + NewsSynchronization.NEWS_TYPE_DEBATE, null);
	}

	/**
	 * Drop the debate news table.
	 */
	public int deleteVisitorNews() {
		return dbHelper.database.delete(BaseDatabaseHelper.NEWS_TABLE_NAME, KEY_TYPE + "=" + NewsSynchronization.NEWS_TYPE_VISITOR, null);
	}

	/**
	 * Delete a news object in the database.
	 */
	public boolean deleteNews(long rowId) {
		return dbHelper.database.delete(BaseDatabaseHelper.NEWS_TABLE_NAME, KEY_ROWID + "=" + rowId, null) > 0;
	}

	/**
	 * Delete a news object identified by the news details url in the database.
	 */
	public boolean deleteNewsFromDetailsURL(String newsDetailsURL) {
		return dbHelper.database.delete(BaseDatabaseHelper.NEWS_TABLE_NAME, KEY_DETAILSXML + "='" + newsDetailsURL + "'", null) > 0;
	}

	/**
	 * Database table cursor method to see if any news exists.
	 */
	public boolean existNews() {
		Cursor mCursor = dbHelper.database.query(BaseDatabaseHelper.NEWS_TABLE_NAME, new String[] { KEY_ROWID }, KEY_TYPE + "=" + NewsSynchronization.NEWS_TYPE_NORMAL + " OR "
				+ KEY_TYPE + "=" + NewsSynchronization.NEWS_TYPE_LIST, null, null, null, null, null);

		if (mCursor != null) {
			mCursor.moveToFirst();
		}
		boolean existNews = mCursor.getCount() > 0;
		mCursor.close();

		return existNews;
	}

	public boolean existVisitorNews() {
		Cursor mCursor = dbHelper.database.query(BaseDatabaseHelper.NEWS_TABLE_NAME, new String[] { KEY_ROWID }, KEY_TYPE + "=" + NewsSynchronization.NEWS_TYPE_VISITOR, null,
				null, null, null, null);

		if (mCursor != null) {
			mCursor.moveToFirst();
		}
		boolean existNews = mCursor.getCount() > 0;
		mCursor.close();

		return existNews;
	}

	public boolean existDebateNews() {
		Cursor mCursor = dbHelper.database.query(BaseDatabaseHelper.NEWS_TABLE_NAME, new String[] { KEY_ROWID }, KEY_TYPE + "=" + NewsSynchronization.NEWS_TYPE_DEBATE, null, null,
				null, null, null);

		if (mCursor != null) {
			mCursor.moveToFirst();
		}
		boolean existNews = mCursor.getCount() > 0;
		mCursor.close();

		return existNews;
	}

	/**
	 * Database table cursor method to fetch all news.
	 */
	public Cursor fetchAllNews() {
		return dbHelper.database.query(BaseDatabaseHelper.NEWS_TABLE_NAME, new String[] { KEY_ROWID, KEY_LISTID, KEY_TYPE, KEY_STARTTEASER, KEY_STATUS, KEY_DATE, KEY_TITLE,
				KEY_TEASER, KEY_IMAGEURL, KEY_IMAGECOPYRIGHT, KEY_DETAILSXML, KEY_VIDEO_URL, KEY_VIDEO_STREAMURL, KEY_LASTCHANGED, KEY_CHANGEDDATETIME, KEY_DETAILS_TITLE,
				KEY_IMAGE_STRING }, "(" + KEY_TYPE + "=" + NewsSynchronization.NEWS_TYPE_NORMAL + " OR " + KEY_TYPE + "=" + NewsSynchronization.NEWS_TYPE_LIST + ") AND "
				+ KEY_LISTID + " isnull", null, null, null, null, null);
	}

	public Cursor fetchAllNewsListsParentId() {
		return dbHelper.database.query(BaseDatabaseHelper.NEWS_TABLE_NAME, new String[] { KEY_NEWSLIST_PARENTNEWSID }, null, null, null, null, null, null);
	}

	/**
	 * Database table cursor method to fetch all sub list news.
	 */
	public Cursor fetchSubListNews(int listId) {
		return dbHelper.database.query(BaseDatabaseHelper.NEWS_TABLE_NAME, new String[] { KEY_ROWID, KEY_LISTID, KEY_TYPE, KEY_STARTTEASER, KEY_STATUS, KEY_DATE, KEY_TITLE,
				KEY_TEASER, KEY_IMAGEURL, KEY_IMAGECOPYRIGHT, KEY_DETAILSXML, KEY_VIDEO_URL, KEY_VIDEO_STREAMURL, KEY_LASTCHANGED, KEY_CHANGEDDATETIME, KEY_DETAILS_TITLE,
				KEY_IMAGE_STRING }, KEY_LISTID + "=" + listId, null, null, null, null, null);
	}

	/**
	 * Database table cursor method to fetch all visitor news.
	 */
	public Cursor fetchAllVisitorNews() {
		return dbHelper.database.query(BaseDatabaseHelper.NEWS_TABLE_NAME, new String[] { KEY_ROWID, KEY_LISTID, KEY_TYPE, KEY_DATE, KEY_TITLE, KEY_TEASER, KEY_IMAGEURL,
				KEY_IMAGECOPYRIGHT, KEY_DETAILSXML, KEY_VIDEO_URL, KEY_VIDEO_STREAMURL, KEY_LASTCHANGED, KEY_CHANGEDDATETIME, KEY_DETAILS_TITLE, KEY_IMAGE_STRING }, KEY_TYPE + "="
				+ NewsSynchronization.NEWS_TYPE_VISITOR, null, null, null, null, null);
	}

	/**
	 * Database table cursor method to fetch all debate news.
	 */
	public Cursor fetchAllDebateNews() {
		return dbHelper.database.query(BaseDatabaseHelper.NEWS_TABLE_NAME, new String[] { KEY_ROWID, KEY_LISTID, KEY_TYPE, KEY_DATE, KEY_TITLE, KEY_TEASER, KEY_IMAGEURL,
				KEY_IMAGECOPYRIGHT, KEY_DETAILSXML, KEY_VIDEO_URL, KEY_VIDEO_STREAMURL, KEY_LASTCHANGED, KEY_CHANGEDDATETIME, KEY_DETAILS_TITLE, KEY_IMAGE_STRING }, KEY_TYPE + "="
				+ NewsSynchronization.NEWS_TYPE_DEBATE, null, null, null, null, null);
	}

	/**
	 * Database table cursor method to fetch one news from a specfic row.
	 */
	public Cursor fetchNews(long rowId) throws SQLException {
		Cursor mCursor = dbHelper.database.query(true, BaseDatabaseHelper.NEWS_TABLE_NAME, new String[] { KEY_ROWID, KEY_LISTID, KEY_TYPE, KEY_DATE, KEY_TITLE, KEY_TEASER,
				KEY_IMAGEURL, KEY_IMAGECOPYRIGHT, KEY_DETAILSXML, KEY_VIDEO_URL, KEY_VIDEO_STREAMURL, KEY_LASTCHANGED, KEY_CHANGEDDATETIME, KEY_IMAGE_STRING }, KEY_ROWID + "="
				+ rowId, null, null, null, null, null);

		if (mCursor != null) {
			mCursor.moveToFirst();
		}

		return mCursor;
	}

	/**
	 * Database table cursor method to fetch one news from a specfic row.
	 */
	public Cursor getListIdFromNewsId(long rowId) throws SQLException {
		Cursor mCursor = dbHelper.database.query(true, BaseDatabaseHelper.NEWS_LIST_TABLE_NAME, new String[] { KEY_NEWSLIST_ROWID, KEY_NEWSLIST_PARENTNEWSID },
				KEY_NEWSLIST_PARENTNEWSID + "='" + rowId + "'", null, null, null, null, null);

		if (mCursor != null) {
			mCursor.moveToFirst();
		}

		return mCursor;
	}

	/**
	 * Database table cursor method to fetch one news details from a specfic
	 * row.
	 */
	public Cursor fetchNewsDetails(long rowId) throws SQLException {
		Cursor mCursor = dbHelper.database.query(true, BaseDatabaseHelper.NEWS_TABLE_NAME, new String[] { KEY_ROWID, KEY_LISTID, KEY_TYPE, KEY_DETAILS_DATE, KEY_DETAILS_TITLE,
				KEY_DETAILS_IMAGEURL, KEY_DETAILS_IMAGE_GROSS_URL, KEY_DETAILS_IMAGECOPYRIGHT, KEY_DETAILSXML, KEY_DETAILS_IMAGELASTCHANGED, KEY_DETAILS_IMAGECHANGEDDATETIME,
				KEY_DETAILS_TEXT, KEY_DETAILS_IMAGE_STRING, KEY_VIDEO_STREAMURL, KEY_STATUS, KEY_STARTTEASER, KEY_LISTID }, KEY_ROWID + "=" + rowId, null, null, null, null, null);

		if (mCursor != null) {
			mCursor.moveToFirst();
		}

		return mCursor;
	}

	/**
	 * Get a news from the database using the news details URL as id.
	 */
	public Cursor fetchNewsFromDetailsURL(String newsDetailsURL) {
		Cursor mCursor = dbHelper.database.query(true, BaseDatabaseHelper.NEWS_TABLE_NAME, new String[] { KEY_ROWID, KEY_LISTID, KEY_TYPE, KEY_DATE, KEY_TITLE, KEY_TEASER,
				KEY_IMAGEURL, KEY_IMAGECOPYRIGHT, KEY_DETAILSXML, KEY_VIDEO_URL, KEY_VIDEO_STREAMURL, KEY_LASTCHANGED, KEY_CHANGEDDATETIME, KEY_IMAGE_STRING }, KEY_DETAILSXML
				+ "='" + newsDetailsURL + "'", null, null, null, null, null);

		if (mCursor != null) {
			mCursor.moveToFirst();
		}

		return mCursor;
	}

	/**
	 * Create a news content value object from the general news object.
	 * 
	 * Needed for inserting a news object.
	 */
	private ContentValues createContentValues(NewsObject newsObject) {
		ContentValues values = new ContentValues();

		String dateString = newsObject.getDate();
		if (dateString != null) {
			values.put(KEY_DATE, dateString);
		}
		String startTeaser = newsObject.getStartteaser();
		if (startTeaser != null) {
			values.put(KEY_STARTTEASER, startTeaser);
		}

		String status = newsObject.getStatus();
		if (status != null) {
			values.put(KEY_STATUS, status);
		}

		values.put(KEY_TYPE, newsObject.getType());
		values.put(KEY_LISTID, newsObject.getListId());
		values.put(KEY_TITLE, newsObject.getTitle());
		values.put(KEY_TEASER, newsObject.getTeaser());
		values.put(KEY_IMAGEURL, newsObject.getImageURL());
		values.put(KEY_IMAGECOPYRIGHT, newsObject.getImageCopyright());
		values.put(KEY_IMAGE_STRING, newsObject.getImageString());
		values.put(KEY_DETAILSXML, newsObject.getDetailsXMLURL());
		values.put(KEY_LASTCHANGED, String.valueOf(newsObject.getLastChanged()));
		values.put(KEY_CHANGEDDATETIME, String.valueOf(newsObject.getChangedDateTime()));

		if (newsObject.getVideoURL() != null) {
			values.put(KEY_VIDEO_URL, newsObject.getVideoURL());
		}
		if (newsObject.getVideoStreamURL() != null) {
			values.put(KEY_VIDEO_STREAMURL, newsObject.getVideoStreamURL());
		}

		NewsDetailsObject newsDetails = newsObject.getNewsDetails();
		if (newsDetails != null) {
			values.put(KEY_DETAILS_DATE, newsDetails.getDate());
			values.put(KEY_DETAILS_TITLE, newsDetails.getTitle());
			values.put(KEY_DETAILS_IMAGEURL, newsDetails.getImageURL());
			values.put(KEY_DETAILS_IMAGE_GROSS_URL, newsDetails.getImageGrossURL());
			values.put(KEY_DETAILS_IMAGECOPYRIGHT, newsDetails.getImageCopyright());
			values.put(KEY_DETAILS_IMAGELASTCHANGED, String.valueOf(newsDetails.getImageLastChanged()));
			values.put(KEY_DETAILS_IMAGECHANGEDDATETIME, String.valueOf(newsDetails.getImageChangedDateTime()));
			values.put(KEY_DETAILS_IMAGE_STRING, newsDetails.getImageString());
			values.put(KEY_DETAILS_TEXT, String.valueOf(newsDetails.getText()));
		}

		return values;
	}

	/**
	 * Update the news with the bitmap string.
	 */
	public void updatePicture(String newsId, String bitmapString) {
		ContentValues values = new ContentValues();

		values.put(KEY_DETAILS_IMAGE_STRING, bitmapString);

		dbHelper.database.update(BaseDatabaseHelper.NEWS_TABLE_NAME, values, KEY_ROWID + "=" + newsId, null);
	}

	private ContentValues createNewsListValues(NewsListObject newsListObject) {
		ContentValues values = new ContentValues();

		values.put(KEY_NEWSLIST_TITLE, newsListObject.getTitle());
		values.put(KEY_NEWSLIST_PARENTNEWSID, newsListObject.getParentNewsId());

		return values;
	}

	/**
	 * Update a news details with data.
	 */
	public long updateNews(int newsId, NewsDetailsObject newsDetails) {
		ContentValues values = new ContentValues();

		values.put(KEY_DETAILS_DATE, newsDetails.getDate());
		values.put(KEY_DETAILS_TITLE, newsDetails.getTitle());
		values.put(KEY_DETAILS_IMAGEURL, newsDetails.getImageURL());
		values.put(KEY_DETAILS_IMAGE_GROSS_URL, newsDetails.getImageGrossURL());
		values.put(KEY_DETAILS_IMAGECOPYRIGHT, newsDetails.getImageCopyright());
		values.put(KEY_DETAILS_IMAGELASTCHANGED, String.valueOf(newsDetails.getImageLastChanged()));
		values.put(KEY_DETAILS_IMAGECHANGEDDATETIME, String.valueOf(newsDetails.getImageChangedDateTime()));
		values.put(KEY_DETAILS_IMAGE_STRING, newsDetails.getImageString());
		values.put(KEY_DETAILS_TEXT, String.valueOf(newsDetails.getText()));

		return dbHelper.database.update(BaseDatabaseHelper.NEWS_TABLE_NAME, values, KEY_ROWID + "=" + newsId, null);
	}
}
