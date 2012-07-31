package de.bundestag.android;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import de.bundestag.android.constant.AppConstant;
import de.bundestag.android.constant.DataHolder;
import de.bundestag.android.parser.PlenumXMLParser;
import de.bundestag.android.parser.objects.PlenumObject;
import de.bundestag.android.sections.impressum.ImpressumActivity;
import de.bundestag.android.synchronization.MainSynchronization;

/**
 * All main activities extends this activity that adds menu functionality.
 */
public class BaseActivity extends FragmentActivity {
	// Default false. When debug is on, it doesn't load the latest News in
	// Aktuell at startup
	// Furthermore it only loads the DEBUG_SYNCHRONIZE_NUMBER at the parsing for
	// each section.
	private static final boolean DEBUG_ON = false;
	// Default false. When on, the database file is copied to the internal SD
	// card at startup (before importing)
	private static final boolean EXPORT_ON = false;

	// Default true, when false, it will not run the database insert at startup.
	private static final boolean IMPORT_ON = true;

	// The amount of articles (items) in each section to synchronize, parse and
	// insert in database.
	// This is ignored when DEBUG_ON = false.
	public static final int DEBUG_SYNCHRONIZE_NUMBER = 3;

	// Options menu settings
	private static final int OPTION_SYNCHRONIZE = 0;

	private static final int OPTION_IMPRESS = 1;

	private static final int OPTION_SHUTDOWN = 2;

	private MainSynchronization mainSynchronization;

	public void onCreate(Bundle savedInstanceState, BaseActivity activity) {
		super.onCreate(savedInstanceState);
		DataHolder.currentActiviry = this;
		if (AppConstant.isFragmentSupported) {
			DataHolder.mLockScreenRotation(this);
		}
		Log.isLoggable("de.bundestag.android", Log.ASSERT);
		// if (!(activity instanceof NewsActivity) && !(activity instanceof
		// NewsStartActivity)) {
		// SharedPreferences settings = getSharedPreferences("Identify", 0);
		//
		// int calculatedScreensize = settings.getInt("LeftFragmentSize", -1);
		// if (calculatedScreensize == 0) {
		// SharedPreferences.Editor editor = settings.edit();
		// editor.putBoolean("FragmentSupport",
		// AppConstant.isFragmentSupported);
		// editor.putInt("LeftFragmentSize",
		// DataHolder.calculatedScreenResolution);
		// editor.commit();
		// } else {
		// AppConstant.isFragmentSupported =
		// settings.getBoolean("FragmentSupport", false);
		// DataHolder.calculatedScreenResolution =
		// settings.getInt("LeftFragmentSize", -1);
		// }
		// }
		DataHolder.context = this;
		this.mainSynchronization = new MainSynchronization();
	}

	protected void cleanupStartSettings() {
		BundestagSharedPreferences.cleanupStartSettings(this);
	}

	/**
	 * Check if options menu should be shown.
	 * 
	 * This is used the first time you enter Members, Committees, Plenum or
	 * Visitors.
	 */
	protected void checkShowOptionsMenu(Activity activity) {
		if (DataHolder.isOnline(this)) {
			if (!BundestagSharedPreferences.getCheckedSynchronization(activity)) {
				BundestagSharedPreferences.setCheckedSynchronization(activity, true);
				new Handler().postDelayed(new Runnable() {
					@Override
					public void run() {
						try {
							openOptionsMenu();
						} catch (Exception e) {
							// e.printStackTrace();
						}
					}
				}, 3000);
			}
		} else {
			BundestagSharedPreferences.setCheckedSynchronization(activity, true);
		}
	}

	/**
	 * Check with the Bundestag shared preferences if Plenum needs new news.
	 */
	protected boolean checkPlenumNeedsNews(Activity activity) {
		if (BundestagSharedPreferences.getPlenumNeedsNews(activity)) {
			BundestagSharedPreferences.setPlenumNeedsNews(activity, false);

			return true;
		}

		return false;
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();

		// try {
		// if (AppConstant.isFragmentSupported != true) {
		// Fragment f =
		// getSupportFragmentManager().findFragmentById(R.id.detailsFragment);
		// if (f != null) {
		// AppConstant.isFragmentSupported = true;
		// }
		// }
		// } catch (Exception e) {
		//
		// }
	}

	/**
	 * Setup the options menu.
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuItem menu_1 = menu.add(0, OPTION_SYNCHRONIZE, 0, "Aktualisieren");
		menu_1.setIcon(R.drawable.menu_refresh);

		MenuItem menu_2 = menu.add(0, OPTION_IMPRESS, 1, "Impressum");
		menu_2.setIcon(R.drawable.menu_info);
		/*
		 * MenuItem menu_3 = menu.add(0, OPTION_SHUTDOWN, 2, "App schließen");
		 * menu_3.setIcon(R.drawable.menu_shutdown);
		 */
		return super.onCreateOptionsMenu(menu);
	}

	/**
	 * Called when an item in the options menu is selected.
	 */
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case OPTION_SYNCHRONIZE:

			if (DataHolder.isOnline(this)) {
				setProgressBarIndeterminateVisibility(true);
				final MainSynchronization finalMainSynchronization = mainSynchronization;
				final Context context = this;
				AlertDialog.Builder builder = new AlertDialog.Builder(this);
				builder.setMessage("Das Update aller Daten kann einige Minuten dauern. Wollen Sie es jetzt durchführen?").setCancelable(false)
						.setPositiveButton("Ja", new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {

								finalMainSynchronization.synchronizeAll(context);
							}
						}).setNegativeButton("Abbrechen", new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								dialog.cancel();
							}
						});

				AlertDialog alert = builder.create();
				alert.show();
			} else {
				AlertDialog.Builder builder = new AlertDialog.Builder(this);
				builder.setMessage(
						"Die Aktualisierung der Daten kann leider nur mit einer aktiven Internetverbindung durchgeführt werden. Die Anwendung befindet sich im Offline-Modus.")
						.setCancelable(false).setNegativeButton("Ok", new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								dialog.cancel();
							}
						});

				AlertDialog alert = builder.create();
				alert.show();
			}

			return true;

		case OPTION_IMPRESS:

			final Context context = this;
			Intent contactIntent = new Intent(context, ImpressumActivity.class);
			startActivity(contactIntent);
			overridePendingTransition(0, 0);
			return true;

		case OPTION_SHUTDOWN:
			finish();

			return true;
		}
		return false;
	}

	/**
	 * Check if we are online.
	 */
	public boolean isOnline() {
		ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();

		if (activeNetworkInfo == null) {
			return false;
		} else if (!activeNetworkInfo.isConnected()) {
			return false;
		} else if (!activeNetworkInfo.isAvailable()) {
			return false;
		}

		return true;
	}

	/**
	 * Return true if we are in debug mode.
	 */
	public static boolean isDebugOn() {
		return DEBUG_ON;
	}

	/**
	 * Return true if we are in export mode.
	 */
	public static boolean isExportOn() {
		return EXPORT_ON;
	}

	/**
	 * Return true if we are in import mode.
	 */
	public static boolean isImportOn() {
		return IMPORT_ON;
	}

	@Override
	protected void onPause() {
		super.onPause();

		overridePendingTransition(0, 0);
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		// overridePendingTransition(0, 0);
	}

	public boolean isPLenarySitting() {
		PlenumXMLParser pp = new PlenumXMLParser();
		PlenumObject plenum = null;
		try {
			plenum = pp.parseMain(PlenumXMLParser.MAIN_PLENUM_URL);
			if ((plenum != null) && (plenum.getStatus().intValue() == 1)) {
				return true;
			}
		} catch (Exception e) {
			// e.printStackTrace();
		}

		return false;
	}

}
