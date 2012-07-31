package de.bundestag.android.storage;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import android.app.Activity;
import android.content.Context;
import android.content.res.AssetManager;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDiskIOException;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;
import de.bundestag.android.R;

/**
 * Base database helper.
 * 
 * Manages the database. Contains methods to create and drop all the tables.
 */
public class BaseDatabaseHelper extends SQLiteOpenHelper {
	protected static final String NEWS_TABLE_NAME = "news";
	protected static final String NEWS_LIST_TABLE_NAME = "news_lists";

	protected static final String PLENUM_TABLE_NAME = "plenums";

	protected static final String MEMBERS_TABLE_NAME = "members";
	protected static final String MEMBERS_GROUPS_TABLE_NAME = "member_groups";
	protected static final String MEMBERS_WEBSITES_TABLE_NAME = "member_websites";

	protected static final String COMMITTEES_TABLE_NAME = "committees";
	protected static final String COMMITTEES_NEWS_TABLE_NAME = "committees_news";

	protected static final String VISITORS_TABLE_NAME = "visitors";
	protected static final String VISITORS_ARTICLE_LIST_TABLE_NAME = "visitors_article_lists";
	protected static final String VISITORS_ARTICLES_TABLE_NAME = "visitors_articles";
	protected static final String VISITORS_GALLERIES_TABLE_NAME = "visitors_galleries";
	protected static final String VISITORS_PICTURES_TABLE_NAME = "visitors_pictures";

	private Context context;

	public SQLiteDatabase database;

	/**
	 * Constructor for database helper.
	 */
	public BaseDatabaseHelper(Context context) {
		super(context, BaseDatabaseConstants.DATABASE_NAME, null, BaseDatabaseConstants.DATABASE_VERSION);

		this.context = context;
	}

	public void openDatabase() {
		try {
			database.close();
			super.close();
			database = null;
		} catch (Exception e) {
		}
		try {
			database = super.getWritableDatabase();
		} catch (Exception e) {
			// e.printStackTrace();
			database.close();
			super.close();
			database = super.getWritableDatabase();
		}
	}

	/**
	 * Database setup.
	 * 
	 * Creates each of the tables needed.
	 */
	@Override
	public void onCreate(SQLiteDatabase database) {
		try {
			InputStream in = context.getResources().openRawResource(R.raw.create_database_sql);
			DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			Document doc = builder.parse(in, null);
			NodeList statements = doc.getElementsByTagName("statement");

			String s;
			for (int i = 0; i < statements.getLength(); i++) {
				s = statements.item(i).getChildNodes().item(0).getNodeValue();
				database.execSQL(s);
			}
		} catch (Throwable t) {
			t.printStackTrace();
		}
	}

	/**
	 * Database upgrade.
	 * 
	 * Drops each table needed.
	 */
	@Override
	public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
		database.execSQL("DROP TABLE IF EXISTS " + NEWS_TABLE_NAME);

		database.execSQL("DROP TABLE IF EXISTS " + PLENUM_TABLE_NAME);

		database.execSQL("DROP TABLE IF EXISTS " + MEMBERS_TABLE_NAME);
		database.execSQL("DROP TABLE IF EXISTS " + MEMBERS_GROUPS_TABLE_NAME);
		database.execSQL("DROP TABLE IF EXISTS " + MEMBERS_WEBSITES_TABLE_NAME);

		database.execSQL("DROP TABLE IF EXISTS " + COMMITTEES_TABLE_NAME);
		database.execSQL("DROP TABLE IF EXISTS " + COMMITTEES_NEWS_TABLE_NAME);

		database.execSQL("DROP TABLE IF EXISTS " + VISITORS_TABLE_NAME);
		database.execSQL("DROP TABLE IF EXISTS " + VISITORS_ARTICLE_LIST_TABLE_NAME);
		database.execSQL("DROP TABLE IF EXISTS " + VISITORS_ARTICLES_TABLE_NAME);
		database.execSQL("DROP TABLE IF EXISTS " + VISITORS_GALLERIES_TABLE_NAME);
		database.execSQL("DROP TABLE IF EXISTS " + VISITORS_PICTURES_TABLE_NAME);

		onCreate(database);
	}

	/**
	 * Method to "import" or rather copy the split up database from the assets
	 * into the working database.
	 * 
	 * @throws IOException
	 */
	public void importDatabase(Activity activity) throws SQLiteDiskIOException, IOException {
		AssetManager am = context.getAssets();
		OutputStream os;
		try {
			os = new FileOutputStream("/data/data/de.bundestag.android/databases/bundestag_db");

			byte[] buffer = new byte[1024];
			String[] files = am.list("database");
			Arrays.sort(files);
			for (int i = 0; i < files.length; i++) {
				int length;
				InputStream is = am.open("database/" + files[i]);
				while ((length = is.read(buffer)) > 0) {
					os.write(buffer, 0, length);
				}
				is.close();
			}
			os.flush();
			os.close();
		} catch (FileNotFoundException e) {
			// e.printStackTrace();
		}
	}

	/**
	 * Method to "export" or rather copy the database to the SD card, so it can
	 * be copied.
	 */
	public void exportDatabase() {
		try {
			File f1 = new File("/data/data/de.bundestag.android/databases/bundestag_db");
			if (f1.exists()) {
				File f2 = new File(Environment.getExternalStorageDirectory() + File.separator + "bundestag_db");
				f2.createNewFile();
				InputStream in = new FileInputStream(f1);
				FileOutputStream out = new FileOutputStream(f2);
				byte[] buf = new byte[1024];
				int len;
				while ((len = in.read(buf)) > 0) {
					out.write(buf, 0, len);
				}
				in.close();
				out.close();
			}
		} catch (FileNotFoundException ex) {
			// System.out.println(ex.getMessage() +
			// " in the specified directory.");
			System.exit(0);
		} catch (IOException e) {
			// e.printStackTrace();
			// System.out.println(e.getMessage());
		}
	}
}
