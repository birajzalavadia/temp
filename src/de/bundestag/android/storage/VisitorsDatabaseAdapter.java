package de.bundestag.android.storage;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import de.bundestag.android.constant.AppConstant;
import de.bundestag.android.parser.objects.VisitorsArticleListItemGalleryObject;
import de.bundestag.android.parser.objects.VisitorsArticleListItemObject;
import de.bundestag.android.parser.objects.VisitorsArticleListObject;
import de.bundestag.android.parser.objects.VisitorsArticleObject;
import de.bundestag.android.parser.objects.VisitorsGalleryImageObject;
import de.bundestag.android.parser.objects.VisitorsGalleryObject;
import de.bundestag.android.parser.objects.VisitorsItemObject;
import de.bundestag.android.synchronization.VisitorsSynchronization;

/**
 * Visitors database adapter.
 * 
 * Contains helper methods to interact with the visitors table in the database.
 */
public class VisitorsDatabaseAdapter {
	// Visitor object
	public static final String KEY_ROWID = "_id";
	public static final String KEY_DATE = "date";
	public static final String KEY_TYPE = "type";
	public static final String KEY_ID = "id";
	public static final String KEY_URL = "url";
	public static final String KEY_IMAGE_STRING = "image_string";

	// Visitor article
	public static final String KEY_ARTICLE_ROWID = "_id";
	public static final String KEY_ARTICLE_DATE = "date";
	public static final String KEY_ARTICLE_TYPE = "type";
	public static final String KEY_ARTICLE_SPECIFIC_TYPE = "specific_type";
	public static final String KEY_ARTICLE_ARTICLELISTID = "article_list_id";
	public static final String KEY_ARTICLE_ID = "id";
	public static final String KEY_ARTICLE_TITLE = "title";
	public static final String KEY_ARTICLE_TEASER = "teaser";
	public static final String KEY_ARTICLE_TEXT = "text";
	public static final String KEY_ARTICLE_IMAGEID = "image_id";
	public static final String KEY_ARTICLE_IMAGESTRING = "image_string";
	public static final String KEY_ARTICLE_IMAGECOPYRIGHT = "image_copyright";

	// Visitor gallery
	public static final String KEY_GALLERY_ROWID = "_id";
	public static final String KEY_GALLERY_TITLE = "title";
	public static final String KEY_GALLERY_ID = "gallery_id";

	// Visitor image
	public static final String KEY_IMAGE_ROWID = "_id";
	public static final String KEY_IMAGE_ID = "image_id";
	public static final String KEY_IMAGE_DATE = "image_date";
	public static final String KEY_IMAGE_IMAGESTRING = "image_string";
	public static final String KEY_IMAGE_COPYRIGHT = "image_copyright";
	public static final String KEY_IMAGE_TEXT = "text";
	public static final String KEY_IMAGE_GALLERYID = "gallery_id";
	public static final String KEY_IMAGE_ARTICLEID = "article_id";
	public static final String KEY_IMAGE_TEASER = "teaser";
	public static final String KEY_IMAGE_LISTID = "list_id";

	// Visitor article list
	public static final String KEY_ARTICLELIST_ROWID = "_id";
	public static final String KEY_ARTICLELIST_TYPE = "type";
	public static final String KEY_ARTICLELIST_TITLE = "title";
	public static final String KEY_ARTICLELIST_PARENTARTICLEID = "parent_article_id";

	private Context context;

	private BaseDatabaseHelper dbHelper;

	/**
	 * Visitors database adapter constructor.
	 */
	public VisitorsDatabaseAdapter(Context context) {
		this.context = context;
	}

	/**
	 * Open connection to visitors database table.
	 */
	public VisitorsDatabaseAdapter open(){
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
	 * Inserts a new visitors object.
	 */
	public long createVisitor(VisitorsItemObject visitorsItemObject) {
		ContentValues initialValues = createContentValues(visitorsItemObject);

		return dbHelper.database.insert(BaseDatabaseHelper.VISITORS_TABLE_NAME, null, initialValues);
	}

	/**
	 * Inserts a new visitors article object.
	 */
	public long createVisitorArticle(VisitorsArticleObject contact, int type, Long articleListId) {
		ContentValues initialValues = createVisitorArticleValues(contact, type, articleListId);

		return dbHelper.database.insert(BaseDatabaseHelper.VISITORS_ARTICLES_TABLE_NAME, null, initialValues);
	}

	/**
	 * Inserts a new visitors gallery object.
	 */
	public long createVisitorGallery(VisitorsGalleryObject gallery) {
		ContentValues initialValues = createVisitorGalleryValues(gallery);

		return dbHelper.database.insert(BaseDatabaseHelper.VISITORS_GALLERIES_TABLE_NAME, null, initialValues);
	}

	/**
	 * Inserts a new visitors gallery image object.
	 */
	public Object createVisitorGalleryImage(VisitorsGalleryImageObject galleryImage, long galleryId) {
		ContentValues initialValues = createVisitorImageValues(galleryImage, galleryId);

		return dbHelper.database.insert(BaseDatabaseHelper.VISITORS_PICTURES_TABLE_NAME, null, initialValues);
	}

	/**
	 * Inserts a new visitors article list object.
	 */
	public long createVisitorArticleList(VisitorsArticleListObject locationsObject, int articleListType) {
		ContentValues initialValues = createVisitorArticleListValues(locationsObject, articleListType);

		return dbHelper.database.insert(BaseDatabaseHelper.VISITORS_ARTICLE_LIST_TABLE_NAME, null, initialValues);
	}

	/**
	 * Inserts a new visitors article list article object.
	 */
	public Long createVisitorArticleListArticle(VisitorsArticleListItemObject articleObject, int specificType, Long articleListId) {
		ContentValues initialValues = createVisitorArticleListArticleValues(articleObject, specificType, articleListId);

		return dbHelper.database.insert(BaseDatabaseHelper.VISITORS_ARTICLES_TABLE_NAME, null, initialValues);
	}

	/**
	 * Inserts a new visitors article gallery image object.
	 */
	public void createVisitorArticleGalleryImage(VisitorsArticleListItemGalleryObject articleGalleryImage, long articleListId) {
		ContentValues initialValues = createVisitorArticleGalleryImageValues(articleGalleryImage, articleListId);

		dbHelper.database.insert(BaseDatabaseHelper.VISITORS_PICTURES_TABLE_NAME, null, initialValues);
	}

	/**
	 * Delete a visitors object in the database.
	 */
	public boolean deleteVisitor(long rowId) {
		return dbHelper.database.delete(BaseDatabaseHelper.VISITORS_TABLE_NAME, KEY_ROWID + "=" + rowId, null) > 0;
	}

	/**
	 * Drop the visitors table.
	 */
	public int deleteAllVisitors() {
		return dbHelper.database.delete(BaseDatabaseHelper.VISITORS_TABLE_NAME, "1", null)
				+ dbHelper.database.delete(BaseDatabaseHelper.VISITORS_ARTICLE_LIST_TABLE_NAME, "1", null)
				+ dbHelper.database.delete(BaseDatabaseHelper.VISITORS_ARTICLES_TABLE_NAME, "1", null)
				+ dbHelper.database.delete(BaseDatabaseHelper.VISITORS_GALLERIES_TABLE_NAME, "1", null)
				+ dbHelper.database.delete(BaseDatabaseHelper.VISITORS_PICTURES_TABLE_NAME, "1", null);
	}

	/**
	 * Delete the visitor news.
	 */
	public int deleteAllVisitorsNews() {
		return dbHelper.database.delete(BaseDatabaseHelper.VISITORS_TABLE_NAME, KEY_ARTICLE_TYPE + "=" + VisitorsSynchronization.ARTICLE_SPECIFIC_TYPE_NORMAL, null)
				+ dbHelper.database.delete(BaseDatabaseHelper.VISITORS_ARTICLES_TABLE_NAME, KEY_ARTICLE_SPECIFIC_TYPE + "!="
						+ VisitorsSynchronization.ARTICLE_SPECIFIC_TYPE_CONTACT + " AND " + KEY_ARTICLE_SPECIFIC_TYPE + "!=" + VisitorsSynchronization.ARTICLE_SPECIFIC_TYPE_OFFERS
						+ " AND " + KEY_ARTICLE_SPECIFIC_TYPE + "!=" + VisitorsSynchronization.ARTICLE_SPECIFIC_TYPE_LOCATIONS, null);
	}

	/**
	 * Database table cursor method to fetch all visitors.
	 */
	public Cursor fetchAllVisitors() {
		Cursor mCursor = dbHelper.database.query(BaseDatabaseHelper.VISITORS_TABLE_NAME, new String[] { KEY_ROWID, KEY_DATE, KEY_TYPE, KEY_ID, KEY_URL, KEY_IMAGE_STRING }, null,
				null, null, null, null);

		if (mCursor != null) {
			mCursor.moveToFirst();
		}

		return mCursor;
	}

	/**
	 * Database table cursor method to fetch one visitors from a specfic row.
	 */
	public Cursor fetchVisitors(long rowId) throws SQLException {
		Cursor mCursor = dbHelper.database.query(true, BaseDatabaseHelper.VISITORS_TABLE_NAME, new String[] { KEY_ROWID, KEY_DATE, KEY_TYPE, KEY_ID, KEY_URL, KEY_IMAGE_STRING },
				KEY_ROWID + "=" + rowId, null, null, null, KEY_DATE, null);

		if (mCursor != null) {
			mCursor.moveToFirst();
		}

		return mCursor;
	}

	/**
	 * Database table cursor method to fetch the contact article.
	 */
	public Cursor fetchVisitorsContactDetails() {
		Cursor mCursor = dbHelper.database.query(BaseDatabaseHelper.VISITORS_ARTICLES_TABLE_NAME, new String[] { KEY_ARTICLE_ROWID, KEY_ARTICLE_DATE, KEY_ARTICLE_TYPE,
				KEY_ARTICLE_SPECIFIC_TYPE, KEY_ARTICLE_ARTICLELISTID, KEY_ARTICLE_ID, KEY_ARTICLE_TITLE, KEY_ARTICLE_TEASER, KEY_ARTICLE_TEXT, KEY_ARTICLE_IMAGEID,
				KEY_ARTICLE_IMAGESTRING, KEY_ARTICLE_IMAGECOPYRIGHT }, KEY_ARTICLE_SPECIFIC_TYPE + "=" + VisitorsSynchronization.ARTICLE_SPECIFIC_TYPE_CONTACT, null, null, null,
				null, null);

		if (mCursor != null) {
			mCursor.moveToFirst();
		}

		return mCursor;
	}

	/**
	 * Return the article list cursor with the specified article list type.
	 */
	public Cursor getVisitorArticleList(int articleListType) {
		Cursor mCursor = dbHelper.database.query(BaseDatabaseHelper.VISITORS_ARTICLE_LIST_TABLE_NAME, new String[] { KEY_ARTICLELIST_ROWID, KEY_ARTICLELIST_TYPE,
				KEY_ARTICLELIST_TITLE, KEY_ARTICLELIST_PARENTARTICLEID }, KEY_ARTICLELIST_TYPE + "=" + articleListType, null, null, null, null, null);

		if (mCursor != null) {
			mCursor.moveToFirst();
		}

		return mCursor;
	}

	/**
	 * Return the article list that belongs to the article with the given id.
	 */
	public Cursor getArticleListId(String articleId) {
		Cursor mCursor = dbHelper.database.query(BaseDatabaseHelper.VISITORS_ARTICLE_LIST_TABLE_NAME, new String[] { KEY_ARTICLELIST_ROWID, KEY_ARTICLELIST_TYPE,
				KEY_ARTICLELIST_TITLE, KEY_ARTICLELIST_PARENTARTICLEID }, KEY_ARTICLELIST_PARENTARTICLEID + "='" + articleId + "'", null, null, null, null, null);

		if (mCursor != null) {
			mCursor.moveToFirst();
		}

		return mCursor;
	}

	/**
	 * Return the article list that belongs to the article with the given id.
	 */
	public Cursor getPicture(String pictureId) {
		Cursor mCursor = dbHelper.database.query(BaseDatabaseHelper.VISITORS_PICTURES_TABLE_NAME, new String[] { KEY_IMAGE_ROWID, KEY_IMAGE_ID, KEY_IMAGE_IMAGESTRING },
				KEY_IMAGE_ID + "='" + pictureId + "'", null, null, null, null, null);

		if (mCursor != null) {
			mCursor.moveToFirst();
		}

		return mCursor;
	}

	/**
	 * Database table cursor method to fetch all offer articles.
	 * 
	 * NOTE, it doesn't get the article-list types.
	 */
	public Cursor fetchOffers() {
		Cursor mCursor = null;
		if (!AppConstant.isFragmentSupported) {
			mCursor = dbHelper.database.query(BaseDatabaseHelper.VISITORS_ARTICLES_TABLE_NAME, new String[] { KEY_ARTICLE_ROWID, KEY_ARTICLE_DATE, KEY_ARTICLE_TYPE,
					KEY_ARTICLE_SPECIFIC_TYPE, KEY_ARTICLE_ARTICLELISTID, KEY_ARTICLE_ID, KEY_ARTICLE_TITLE, KEY_ARTICLE_TEASER, KEY_ARTICLE_TEXT, KEY_ARTICLE_IMAGEID,
					KEY_ARTICLE_IMAGESTRING, KEY_ARTICLE_IMAGECOPYRIGHT }, KEY_ARTICLE_SPECIFIC_TYPE + "=" + VisitorsSynchronization.ARTICLE_SPECIFIC_TYPE_OFFERS, null, null,
					null, KEY_ARTICLE_DATE, null);
		} else {
			mCursor = dbHelper.database.rawQuery("select " + KEY_ARTICLE_ROWID + "," + KEY_ARTICLE_DATE + "," + KEY_ARTICLE_TYPE + "," + KEY_ARTICLE_SPECIFIC_TYPE + ","
					+ KEY_ARTICLE_ARTICLELISTID + "," + KEY_ARTICLE_ID + "," + KEY_ARTICLE_TITLE + "," + KEY_ARTICLE_TEASER + "," + KEY_ARTICLE_TEXT + "," + KEY_ARTICLE_IMAGEID
					+ "," + KEY_ARTICLE_IMAGESTRING + "," + KEY_ARTICLE_IMAGECOPYRIGHT
					+ " from visitors_articles where specific_type=3 and type='gallery' union all select * from visitors_articles where specific_type='"
					+ VisitorsSynchronization.ARTICLE_SPECIFIC_TYPE_OFFERS + "' and type<>'gallery'", null);
		}
		if (mCursor != null) {
			mCursor.moveToFirst();
		}
		return mCursor;
	}

	/**
	 * Database table cursor method to fetch all location articles.
	 * 
	 * NOTE, it doesn't get the article-list types.
	 */
	public Cursor fetchLocations() {
		Cursor mCursor = null;
		if (!AppConstant.isFragmentSupported) {
			mCursor = dbHelper.database.query(BaseDatabaseHelper.VISITORS_ARTICLES_TABLE_NAME, new String[] { KEY_ARTICLE_ROWID, KEY_ARTICLE_DATE, KEY_ARTICLE_TYPE,
					KEY_ARTICLE_SPECIFIC_TYPE, KEY_ARTICLE_ARTICLELISTID, KEY_ARTICLE_ID, KEY_ARTICLE_TITLE, KEY_ARTICLE_TEASER, KEY_ARTICLE_TEXT, KEY_ARTICLE_IMAGEID,
					KEY_ARTICLE_IMAGESTRING, KEY_ARTICLE_IMAGECOPYRIGHT }, KEY_ARTICLE_SPECIFIC_TYPE + "=" + VisitorsSynchronization.ARTICLE_SPECIFIC_TYPE_LOCATIONS, null, null,
					null, KEY_ARTICLE_DATE, null);
		} else {
			mCursor = dbHelper.database.rawQuery("select " + KEY_ARTICLE_ROWID + "," + KEY_ARTICLE_DATE + "," + KEY_ARTICLE_TYPE + "," + KEY_ARTICLE_SPECIFIC_TYPE + ","
					+ KEY_ARTICLE_ARTICLELISTID + "," + KEY_ARTICLE_ID + "," + KEY_ARTICLE_TITLE + "," + KEY_ARTICLE_TEASER + "," + KEY_ARTICLE_TEXT + "," + KEY_ARTICLE_IMAGEID
					+ "," + KEY_ARTICLE_IMAGESTRING + "," + KEY_ARTICLE_IMAGECOPYRIGHT
					+ " from visitors_articles where specific_type=3 and type='gallery' union all select * from visitors_articles where specific_type='"
					+ VisitorsSynchronization.ARTICLE_SPECIFIC_TYPE_LOCATIONS + "' and type<>'gallery'", null);
		}

		if (mCursor != null) {
			mCursor.moveToFirst();
		}

		return mCursor;
	}

	/**
	 * Get the article lists belonging to a sub list.
	 */
	public Cursor fetchArticleList(int listId) {
		Cursor mCursor = dbHelper.database.query(BaseDatabaseHelper.VISITORS_ARTICLES_TABLE_NAME, new String[] { KEY_ARTICLE_ROWID, KEY_ARTICLE_DATE, KEY_ARTICLE_TYPE,
				KEY_ARTICLE_SPECIFIC_TYPE, KEY_ARTICLE_ARTICLELISTID, KEY_ARTICLE_ID, KEY_ARTICLE_TITLE, KEY_ARTICLE_TEASER, KEY_ARTICLE_TEXT, KEY_ARTICLE_IMAGEID,
				KEY_ARTICLE_IMAGESTRING, KEY_ARTICLE_IMAGECOPYRIGHT }, KEY_ARTICLE_ARTICLELISTID + "=" + listId, null, null, null, KEY_ARTICLE_DATE, null);

		if (mCursor != null) {
			mCursor.moveToFirst();
		}

		return mCursor;
	}

	/**
	 * Database table cursor method to fetch all sub list articles.
	 */
	// public Cursor fetchSubList(int listId)
	// {
	// TODO
	// Cursor mCursor =
	// database.query(BaseDatabaseHelper.VISITORS_ARTICLES_TABLE_NAME, new
	// String[]
	// {
	// KEY_ARTICLE_ROWID, KEY_ARTICLE_DATE, KEY_ARTICLE_TYPE,
	// KEY_ARTICLE_SPECIFIC_TYPE, KEY_ARTICLE_ARTICLELISTID, KEY_ARTICLE_ID,
	// KEY_ARTICLE_TITLE, KEY_ARTICLE_TEASER, KEY_ARTICLE_TEXT,
	// KEY_ARTICLE_IMAGEID, KEY_ARTICLE_IMAGESTRING, KEY_ARTICLE_IMAGECOPYRIGHT
	// },
	// KEY_ARTICLE_ARTICLELISTID + "=" +
	// VisitorsSynchronization.ARTICLE_SPECIFIC_TYPE_LOCATIONS, null, null,
	// null, null, null);
	// // KEY_ARTICLE_SPECIFIC_TYPE + "=" +
	// VisitorsSynchronization.ARTICLE_SPECIFIC_TYPE_LOCATIONS + " AND " +
	// KEY_ARTICLE_TYPE + "!='article-list'", null, null, null, null, null);
	//
	// if (mCursor != null)
	// {
	// mCursor.moveToFirst();
	// }
	//
	// return mCursor;
	// }

	/**
	 * Database table cursor method to fetch all list galleries.
	 */
	public Cursor fetchListGalleries(String listId) {
		Cursor mCursor = dbHelper.database.query(BaseDatabaseHelper.VISITORS_PICTURES_TABLE_NAME, new String[] { KEY_ARTICLE_ROWID, KEY_IMAGE_ID, KEY_IMAGE_DATE, KEY_IMAGE_STRING,
				KEY_IMAGE_ARTICLEID, KEY_IMAGE_GALLERYID, KEY_IMAGE_COPYRIGHT }, KEY_IMAGE_LISTID + "=" + listId, null, null, null, null, null);

		if (mCursor != null) {
			mCursor.moveToFirst();
		}

		return mCursor;
	}

	public Cursor fetchGallery(String galleryId) {
		Cursor mCursor = dbHelper.database.query(BaseDatabaseHelper.VISITORS_GALLERIES_TABLE_NAME, new String[] { KEY_GALLERY_ROWID, KEY_GALLERY_ID, KEY_GALLERY_TITLE },
				KEY_GALLERY_ID + " = '" + galleryId + "'", null, null, null, null, null);

		if (mCursor != null) {
			mCursor.moveToFirst();
		}

		return mCursor;
	}

	/**
	 * Database table cursor method to fetch all images and info on a specific
	 * image gallery.
	 */
	public Cursor fetchGalleryImages(String galleryId) {
		Cursor mCursor = dbHelper.database.query(BaseDatabaseHelper.VISITORS_PICTURES_TABLE_NAME, new String[] { KEY_ARTICLE_ROWID, KEY_IMAGE_ID, KEY_IMAGE_DATE, KEY_IMAGE_STRING,
				KEY_IMAGE_COPYRIGHT, KEY_IMAGE_TEXT }, KEY_IMAGE_GALLERYID + " = '" + galleryId + "'", null, null, null, null, null);

		if (mCursor != null) {
			mCursor.moveToFirst();
		}

		return mCursor;
	}

	/**
	 * Database table cursor method to fetch all news articles.
	 */
	public Cursor fetchNews() {
		Cursor mCursor = dbHelper.database.query(BaseDatabaseHelper.VISITORS_ARTICLES_TABLE_NAME, new String[] { KEY_ARTICLE_ROWID, KEY_ARTICLE_DATE, KEY_ARTICLE_TYPE,
				KEY_ARTICLE_SPECIFIC_TYPE, KEY_ARTICLE_ARTICLELISTID, KEY_ARTICLE_ID, KEY_ARTICLE_TITLE, KEY_ARTICLE_TEASER, KEY_ARTICLE_TEXT, KEY_ARTICLE_IMAGEID,
				KEY_ARTICLE_IMAGESTRING, KEY_ARTICLE_IMAGECOPYRIGHT }, KEY_ARTICLE_SPECIFIC_TYPE + "=" + VisitorsSynchronization.ARTICLE_SPECIFIC_TYPE_NORMAL, null, null, null,
				KEY_ARTICLE_DATE, null);

		if (mCursor != null) {
			mCursor.moveToFirst();
		}

		return mCursor;
	}

	/**
	 * Database table cursor method to see if any news exists.
	 */
	public boolean existNews() {
		Cursor mCursor = dbHelper.database.query(BaseDatabaseHelper.VISITORS_ARTICLES_TABLE_NAME, new String[] { KEY_ARTICLE_ROWID }, KEY_ARTICLE_SPECIFIC_TYPE + "="
				+ VisitorsSynchronization.ARTICLE_SPECIFIC_TYPE_NORMAL, null, null, null, null, null);

		if (mCursor != null) {
			mCursor.moveToFirst();
		}
		boolean existNews = mCursor.getCount() > 0;
		mCursor.close();

		return existNews;
	}

	/**
	 * Database table cursor method to fetch all news articles.
	 */
	public Cursor articleDetails(long rowId) throws SQLException {
		Cursor mCursor = dbHelper.database.query(BaseDatabaseHelper.VISITORS_ARTICLES_TABLE_NAME, new String[] { KEY_ARTICLE_ROWID, KEY_ARTICLE_DATE, KEY_ARTICLE_TYPE,
				KEY_ARTICLE_SPECIFIC_TYPE, KEY_ARTICLE_ARTICLELISTID, KEY_ARTICLE_ID, KEY_ARTICLE_TITLE, KEY_ARTICLE_TEASER, KEY_ARTICLE_TEXT, KEY_ARTICLE_IMAGEID,
				KEY_ARTICLE_IMAGESTRING, KEY_ARTICLE_IMAGECOPYRIGHT }, KEY_ROWID + "=" + rowId, null, null, null, null, null);

		if (mCursor != null) {
			mCursor.moveToFirst();
		}

		return mCursor;
	}

	/**
	 * Database table cursor method to fetch a particular visitor article.
	 */
	public Cursor getVisitorArticle(String articleId) {
		Cursor mCursor = dbHelper.database.query(BaseDatabaseHelper.VISITORS_ARTICLES_TABLE_NAME, new String[] { KEY_ARTICLE_ROWID, KEY_ARTICLE_ID }, KEY_ARTICLE_ID + "='"
				+ articleId + "'", null, null, null, null, null);

		if (mCursor != null) {
			mCursor.moveToFirst();
		}

		return mCursor;
	}

	public Cursor getVisitor(String visitorId) {
		Cursor mCursor = dbHelper.database.query(BaseDatabaseHelper.VISITORS_TABLE_NAME, new String[] { KEY_ROWID, KEY_ID }, KEY_ID + "='" + visitorId + "'", null, null, null,
				null, null);

		if (mCursor != null) {
			mCursor.moveToFirst();
		}

		return mCursor;
	}

	/**
	 * The articles that contain detail information for orte and angebote have
	 * article list id = null. This is so they are not confused with the article
	 * for orte and angebote list, which only have teaser, but not the main
	 * text.
	 */
	public Cursor articleDetailsFromNewsId(String newsId) {
		Cursor mCursor = dbHelper.database.query(BaseDatabaseHelper.VISITORS_ARTICLES_TABLE_NAME, new String[] { KEY_ARTICLE_DATE, KEY_ARTICLE_TYPE, KEY_ARTICLE_SPECIFIC_TYPE,
				KEY_ARTICLE_ARTICLELISTID, KEY_ARTICLE_ID, KEY_ARTICLE_TITLE, KEY_ARTICLE_TEASER, KEY_ARTICLE_TEXT, KEY_ARTICLE_IMAGEID, KEY_ARTICLE_IMAGESTRING,
				KEY_ARTICLE_IMAGECOPYRIGHT }, KEY_ARTICLE_ID + "='" + newsId + "' AND " + KEY_ARTICLE_ARTICLELISTID + " isnull", null, null, null, null, null);

		if (mCursor != null) {
			mCursor.moveToFirst();
		}

		return mCursor;
	}

	/**
	 * Create a visitors content value object from the general visitors object.
	 * 
	 * Needed for inserting a visitors object.
	 */
	private ContentValues createContentValues(VisitorsItemObject visitorsItemObject) {
		ContentValues values = new ContentValues();

		String dateString = visitorsItemObject.getDate();
		if (dateString != null) {
			values.put(KEY_DATE, dateString);
		}
		values.put(KEY_TYPE, visitorsItemObject.getType());
		values.put(KEY_ID, visitorsItemObject.getId());
		values.put(KEY_URL, visitorsItemObject.getUrl());
		values.put(KEY_IMAGE_STRING, visitorsItemObject.getImageString());

		return values;
	}

	/**
	 * Create a visitors content value object from the general visitors object.
	 * 
	 * Needed for inserting a visitors object.
	 */
	private ContentValues createVisitorArticleValues(VisitorsArticleObject articleObject, int specificType, Long articleListId) {
		ContentValues values = new ContentValues();

		String dateString = articleObject.getDate();
		if (dateString != null) {
			values.put(KEY_ARTICLE_DATE, dateString);
		}
		values.put(KEY_ARTICLE_TYPE, articleObject.getType());
		values.put(KEY_ARTICLE_SPECIFIC_TYPE, specificType);
		values.put(KEY_ARTICLE_ARTICLELISTID, articleListId);
		values.put(KEY_ARTICLE_ID, articleObject.getId());
		values.put(KEY_ARTICLE_TITLE, articleObject.getTitle());
		values.put(KEY_ARTICLE_TEASER, articleObject.getTeaser());
		values.put(KEY_ARTICLE_TEXT, articleObject.getText());
		values.put(KEY_ARTICLE_IMAGEID, articleObject.getImageId());
		values.put(KEY_ARTICLE_IMAGESTRING, articleObject.getImageString());
		values.put(KEY_ARTICLE_IMAGECOPYRIGHT, articleObject.getImageCopyright());

		return values;
	}

	private ContentValues createVisitorGalleryValues(VisitorsGalleryObject gallery) {
		ContentValues values = new ContentValues();

		values.put(KEY_GALLERY_TITLE, gallery.getTitle());
		values.put(KEY_GALLERY_ID, gallery.getGalleryId());
		return values;
	}

	private ContentValues createVisitorImageValues(VisitorsGalleryImageObject galleryImage, long galleryId) {
		ContentValues values = new ContentValues();

		values.put(KEY_IMAGE_ID, galleryImage.getImageId());
		String dateString = galleryImage.getImageDate();
		if (dateString != null) {
			values.put(KEY_IMAGE_DATE, galleryImage.getImageDate());
		}
		values.put(KEY_IMAGE_IMAGESTRING, galleryImage.getImageString());
		values.put(KEY_IMAGE_COPYRIGHT, galleryImage.getImageCopyright());
		values.put(KEY_IMAGE_TEXT, galleryImage.getImageText());
		values.put(KEY_IMAGE_GALLERYID, galleryId);

		return values;
	}

	private ContentValues createVisitorArticleListValues(VisitorsArticleListObject articleListObject, int articleListType) {
		ContentValues values = new ContentValues();

		values.put(KEY_ARTICLELIST_TITLE, articleListObject.getTitle());
		values.put(KEY_ARTICLELIST_PARENTARTICLEID, articleListObject.getParentArticleId());
		values.put(KEY_ARTICLELIST_TYPE, articleListType);

		return values;
	}

	private ContentValues createVisitorArticleListArticleValues(VisitorsArticleListItemObject articleObject, int specificType, Long articleListId) {
		ContentValues values = new ContentValues();

		values.put(KEY_ARTICLE_TYPE, articleObject.getType());
		values.put(KEY_ARTICLE_SPECIFIC_TYPE, specificType);
		values.put(KEY_ARTICLE_ARTICLELISTID, articleListId);
		values.put(KEY_ARTICLE_ID, articleObject.getId());
		values.put(KEY_ARTICLE_TITLE, articleObject.getTitle());
		values.put(KEY_ARTICLE_TEASER, articleObject.getTeaser());
		values.put(KEY_ARTICLE_IMAGEID, articleObject.getImageId());
		values.put(KEY_ARTICLE_IMAGESTRING, articleObject.getImageString());
		values.put(KEY_ARTICLE_IMAGECOPYRIGHT, articleObject.getImageCopyright());

		return values;
	}

	private ContentValues createVisitorArticleGalleryImageValues(VisitorsArticleListItemGalleryObject articleGalleryImage, long articleListId) {
		ContentValues values = new ContentValues();

		values.put(KEY_IMAGE_ID, articleGalleryImage.getImageId());
		values.put(KEY_IMAGE_IMAGESTRING, articleGalleryImage.getImageString());
		values.put(KEY_IMAGE_COPYRIGHT, articleGalleryImage.getImageCopyright());
		values.put(KEY_IMAGE_GALLERYID, articleGalleryImage.getGalleryId());
		values.put(KEY_IMAGE_TEASER, articleGalleryImage.getTeaser());
		// values.put(KEY_IMAGE_ARTICLEID, articleId);
		values.put(KEY_IMAGE_LISTID, articleListId);

		return values;
	}
}
