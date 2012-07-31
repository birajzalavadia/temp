package de.bundestag.android.sections.news;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDiskIOException;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Window;
import android.widget.TextView;
import de.bundestag.android.BaseActivity;
import de.bundestag.android.R;
import de.bundestag.android.constant.AppConstant;
import de.bundestag.android.constant.DataHolder;
import de.bundestag.android.sections.members.MembersListActivity;
import de.bundestag.android.storage.BaseDatabaseHelper;
import de.bundestag.android.storage.CommitteesDatabaseAdapter;
import de.bundestag.android.synchronization.CommitteesSynchronization;
import de.bundestag.android.synchronization.NewsSynchronization;
import de.bundestag.android.synchronization.SynchronizeNewsTask;

/**
 * Main starting activity.
 * 
 * This handles start tasks, like exporting, importing database, etc.
 */
public class NewsStartActivity extends BaseActivity {
	SharedPreferences settings;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState, this);
		settings = getSharedPreferences("Identify", 0);
		DataHolder.setCurrentResolution(this);
		DataHolder.currentActiviry = this;
		DataHolder.mLockScreenRotation(this);
		{

			requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);

			cleanupStartSettings();
			// DEBUG - export the database
			if (BaseActivity.isExportOn()) {
				BaseDatabaseHelper dbHelper = new BaseDatabaseHelper(this);
				dbHelper.getWritableDatabase();
				dbHelper.exportDatabase();
			}

			boolean failedInstallation = false;
			this.setContentView(R.layout.splash_screen);
			if (BaseActivity.isImportOn()) {
				try {
					
					TextView tv = (TextView) findViewById(R.id.txtFragment);

					if (tv != null){
						AppConstant.isFragmentSupported = true;
						if (!DataHolder.isOnline(this)) {
							this.setContentView(R.layout.splash_screen_offline);
						}
					}
					SharedPreferences.Editor editor = settings.edit();
					if (settings.getInt("LeftFragmentSize", -1) != -1) {
						AppConstant.isFragmentSupported = settings.getBoolean("FragmentSupport", false);
						DataHolder.calculatedScreenResolution = settings.getInt("LeftFragmentSize", -1);
					} else {
						editor.putBoolean("FragmentSupport", AppConstant.isFragmentSupported);
						editor.putInt("LeftFragmentSize", DataHolder.calculatedScreenResolution);
						editor.commit();
					}
					checkImportDatabase();
				} catch (IOException e) {
					// e.printStackTrace();

					AlertDialog.Builder builder = new AlertDialog.Builder(this);
					builder.setMessage("Es ist zu wenig freier Speicherplatz vorhanden, um die Anwendung ausführen zu können.").setCancelable(false);
					builder.show();

					this.setContentView(R.layout.splash_screen);
					TextView tv = (TextView) findViewById(R.id.txtFragment);

					if (tv != null)
						AppConstant.isFragmentSupported = true;

					SharedPreferences.Editor editor = settings.edit();
					editor.putBoolean("FragmentSupport", AppConstant.isFragmentSupported);
					editor.putInt("LeftFragmentSize", DataHolder.calculatedScreenResolution);
					editor.commit();

					AppConstant.isFragmentSupported = settings.getBoolean("FragmentSupport", false);
					DataHolder.calculatedScreenResolution = settings.getInt("LeftFragmentSize", -1);

					failedInstallation = true;
				}
			}

			if (!failedInstallation) {
				if (DataHolder.isOnline(this)) {
					if (!BaseActivity.isDebugOn()) {
						deleteNewsContent();
					}
					loadNews();
				} else {
					
					TimerTask timerT = new TimerTask() {
						
						@Override
						public void run() {
							// TODO Auto-generated method stub
							finish();
							Intent intent = new Intent();
							intent.setClass(NewsStartActivity.this, MembersListActivity.class);
							intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
							 startActivity(intent);
						}
					};
					Timer timer = new Timer();
					timer.schedule(timerT, 100);

				}
			}
		}
	}

	/**
	 * Check if the database needs to be updated.
	 * 
	 * @throws IOException
	 */
	private void checkImportDatabase() throws SQLiteDiskIOException, IOException {
		CommitteesDatabaseAdapter adapter = new CommitteesDatabaseAdapter(this);
		adapter.open();
		Cursor cursor = adapter.fetchAllCommittees();
		boolean noCommittees = (cursor.getCount() == 0);
		cursor.close();
		adapter.close();

		if (noCommittees) {
			BaseDatabaseHelper dbHelper = new BaseDatabaseHelper(this);
			dbHelper.importDatabase(this);
		}
	}

	/**
	 * When we launch the application the first time we clean up all the news
	 * content.
	 */
	private void deleteNewsContent() {
		NewsSynchronization newsSynchronization = new NewsSynchronization();
		newsSynchronization.setup(this);
		newsSynchronization.openDatabase();
		newsSynchronization.deleteAllNews();
		newsSynchronization.closeDatabase();

		CommitteesSynchronization committeesSynchronization = new CommitteesSynchronization();
		committeesSynchronization.setup(this);
		committeesSynchronization.openDatabase();
		committeesSynchronization.deleteAllCommitteeNews();
		committeesSynchronization.closeDatabase();
	}

	/**
	 * Call the synchronize news task to update all news.
	 */
	private void loadNews() {
		// Splash Screen
		this.setContentView(R.layout.splash_screen);
		TextView tv = (TextView) findViewById(R.id.txtFragment);

		if (tv != null)
			AppConstant.isFragmentSupported = true;

		SharedPreferences.Editor editor = settings.edit();
		editor.putBoolean("FragmentSupport", AppConstant.isFragmentSupported);
		editor.putInt("LeftFragmentSize", DataHolder.calculatedScreenResolution);
		editor.commit();

		AppConstant.isFragmentSupported = settings.getBoolean("FragmentSupport", false);
		DataHolder.calculatedScreenResolution = settings.getInt("LeftFragmentSize", -1);

		SynchronizeNewsTask newsTask = new SynchronizeNewsTask();
		newsTask.execute(this);
	}
	
	
}
