package de.bundestag.android.synchronization;

import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import de.bundestag.android.BaseActivity;
import de.bundestag.android.helpers.ImageHelper;
import de.bundestag.android.parser.CommitteesXMLParser;
import de.bundestag.android.parser.objects.CommitteesDetailsNewsDetailsObject;
import de.bundestag.android.parser.objects.CommitteesDetailsNewsObject;
import de.bundestag.android.parser.objects.CommitteesDetailsObject;
import de.bundestag.android.parser.objects.CommitteesObject;
import de.bundestag.android.storage.CommitteesDatabaseAdapter;

/**
 * Committees synchronization class.
 * 
 * Knows how to synchronize the committees.
 */
public class CommitteesSynchronization {
	private Context context;

	private CommitteesDatabaseAdapter committeesDatabaseAdapter;

	/**
	 * Synchronizes committees.
	 * 
	 * Removes all committees from the database, parses the committees from the
	 * online XML and inserts them in the database.
	 */
	public void setup(Context context) {
		this.context = context;
	}

	/**
	 * Call the committees parser and parse the latest committees.
	 */
	public List<CommitteesObject> parseCommittees() {
		CommitteesXMLParser committeesParser = new CommitteesXMLParser();
		List<CommitteesObject> committees = committeesParser.parse(true);

		return committees;
	}

	public List<CommitteesObject> parseMainCommittees() {
		CommitteesXMLParser committeesParser = new CommitteesXMLParser();
		List<CommitteesObject> committees = committeesParser.parse(false);

		return committees;
	}

	/**
	 * Open the database and remove all committees.
	 */
	public void deleteAllCommittees() {
		committeesDatabaseAdapter.deleteAllCommittees();
	}

	public long deleteCommitteeNews(String committeeId) {
		return committeesDatabaseAdapter.deleteCommittees(committeeId);
	}

	/**
	 * Open the database and insert the updated committees.
	 */
	// private void insertCommittees(List<CommitteesObject> committees)
	// {
	// openDatabase();
	//
	// for (int i = 0; i < committees.size(); i++)
	// {
	// CommitteesObject committeesObject = (CommitteesObject) committees.get(i);
	//
	// committeesDatabaseAdapter.createCommittee(committeesObject);
	//
	// CommitteesDetailsObject committeesDetailsObject =
	// committeesObject.getCommitteeDetails();
	//
	// List<CommitteesDetailsNewsObject> committeesDetailsNews =
	// committeesDetailsObject.getDetailNews();
	//
	// for (int j = 0; j < committeesDetailsNews.size(); j++)
	// {
	// CommitteesDetailsNewsObject committeesDetailsNewsObject =
	// (CommitteesDetailsNewsObject) committeesDetailsNews.get(j);
	//
	// committeesDatabaseAdapter.createCommitteeNews(committeesDetailsNewsObject);
	// }
	// }
	//
	// closeDatabase();
	// }

	/**
	 * Insert a committee object in the database.
	 */
	public void insertACommittee(CommitteesObject committeesObject) {
		committeesDatabaseAdapter.createCommittee(committeesObject);

		// CommitteesDetailsObject committeesDetailsObject =
		// committeesObject.getCommitteeDetails();
		// List<CommitteesDetailsNewsObject> committeesDetailsNews =
		// committeesDetailsObject.getDetailNews();
		// for (int j = 0; j < committeesDetailsNews.size(); j++)
		// {
		// CommitteesDetailsNewsObject committeesDetailsNewsObject =
		// (CommitteesDetailsNewsObject) committeesDetailsNews.get(j);
		//
		// committeesDatabaseAdapter.createCommitteeNews(committeesDetailsNewsObject);
		// }
	}

	/**
	 * Open the database.
	 */
	public void openDatabase() {
		if (committeesDatabaseAdapter == null) {
			committeesDatabaseAdapter = new CommitteesDatabaseAdapter(context);
		}

		committeesDatabaseAdapter.open();
	}

	/**
	 * Load and insert the pictures.
	 */
	public void insertPictures(List<CommitteesObject> committees) {
		int insertsSize = BaseActivity.isDebugOn() ? ((BaseActivity.DEBUG_SYNCHRONIZE_NUMBER > committees.size()) ? committees.size() : BaseActivity.DEBUG_SYNCHRONIZE_NUMBER)
				: committees.size();
		for (int i = 0; i < insertsSize; i++) {
			CommitteesObject committeesObject = (CommitteesObject) committees.get(i);

			insertPicture(committeesObject);
		}
	}

	/**
	 * Load and insert a picture.
	 */
	public void insertPicture(CommitteesObject committeesObject) {
		CommitteesDetailsObject committeesDetailsObject = committeesObject.getCommitteeDetails();
		Bitmap bitmap = ImageHelper.loadBitmapFromUrl(committeesDetailsObject.getPhotoURL());
		if (bitmap != null) {
			committeesDetailsObject.setPhotoString(ImageHelper.convertBitmapToString(bitmap));
		}

		// Committees news details
		// List<CommitteesDetailsNewsObject> detailNews =
		// committeesDetailsObject.getDetailNews();
		// if (detailNews != null)
		// {
		// for (int j = 0; j < detailNews.size(); j++)
		// {
		// CommitteesDetailsNewsObject committeesDetailsNewsObject =
		// (CommitteesDetailsNewsObject) detailNews.get(j);
		// if (committeesDetailsNewsObject != null)
		// {
		// CommitteesDetailsNewsDetailsObject committeesDetailsNewsDetailsObject
		// = committeesDetailsNewsObject.getNewsDetails();
		//
		// if (committeesDetailsNewsDetailsObject != null)
		// {
		// bitmap =
		// ImageHelper.loadBitmapFromUrl(committeesDetailsNewsObject.getImageURL());
		// if (bitmap != null)
		// {
		// committeesDetailsNewsObject.setImageString(ImageHelper.convertBitmapToString(bitmap));
		// committeesDetailsNewsDetailsObject.setImageString(ImageHelper.convertBitmapToString(bitmap));
		// }
		// }
		// }
		// }
		// }
	}

	/**
	 * Load and insert a picture.
	 */
	public void insertNewsPicture(CommitteesDetailsNewsObject committeesDetailsNewsObject) {
		if (committeesDetailsNewsObject != null) {
			CommitteesDetailsNewsDetailsObject committeesDetailsNewsDetailsObject = committeesDetailsNewsObject.getNewsDetails();

			if (committeesDetailsNewsDetailsObject != null) {
				Bitmap bitmap = ImageHelper.loadBitmapFromUrl(committeesDetailsNewsObject.getImageURL());
				if (bitmap != null) {
					committeesDetailsNewsObject.setImageString(ImageHelper.convertBitmapToString(bitmap));
				}

				CommitteesDetailsNewsDetailsObject newsDetails = committeesDetailsNewsObject.getNewsDetails();
				if (newsDetails != null) {
					bitmap = ImageHelper.loadBitmapFromUrl(newsDetails.getImageURL());
					if (bitmap != null) {
						committeesDetailsNewsDetailsObject.setImageString(ImageHelper.convertBitmapToString(bitmap));
					}
				}
			}
		}
	}

	/**
	 * Close the database.
	 */
	public void closeDatabase() {
		committeesDatabaseAdapter.close();
	}

	public Cursor getCommitteeFromId(String committeeId) {
		return committeesDatabaseAdapter.getCommitteeFromId(committeeId);
	}

	public void deleteCommittee(String committeeId) {
		committeesDatabaseAdapter.deleteCommittee(committeeId);
	}

	public Cursor getCommitteeNewsFromIdString(String committeeId) {
		return committeesDatabaseAdapter.getCommitteeNewsFromIdString(committeeId);
	}

	public void insertCommitteeNews(CommitteesDetailsNewsObject committeesNewsObject) {
		committeesDatabaseAdapter.createCommitteeNews(committeesNewsObject);
	}

	public void insertFirstItemNews(CommitteesDetailsNewsDetailsObject committeesNewsObject) {
		committeesDatabaseAdapter.createCommitteeFirstNews(committeesNewsObject);
	}

	public void deleteAllCommitteeNews() {
		committeesDatabaseAdapter.deleteAllCommitteesNews();
	}

	public long updateCommitteeNews(int newsRowId, CommitteesDetailsNewsDetailsObject committeesDetailsNewsDetailsObject) {
		return committeesDatabaseAdapter.updateCommitteeNews(newsRowId, committeesDetailsNewsDetailsObject);
	}
}
