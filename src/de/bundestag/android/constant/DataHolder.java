package de.bundestag.android.constant;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.TextView;
import de.bundestag.android.fragments.GeneralListFragmentTab;
import de.bundestag.android.parser.objects.PlenumTvObject;
import de.bundestag.android.sections.members.MembersListActivity;
import de.bundestag.android.sections.news.NewsActivityTablet;
import de.bundestag.android.sections.news.NewsStartActivity;

public class DataHolder {

	/* id for list view */
	public static int _id = -1;
	/* id for detail list item */
	public static int _oldPosition = -1;
	/* id for sub list view */
	public static int _subId = -1;
	/* array for list detail item */
	public static ArrayList<Integer> RowDBIds = null;
	/* temp array list of detail item */
	public static ArrayList<Integer> tmpRowDBIds = null;
	/* array index of row db ids. */
	public static HashMap<Integer, ArrayList<Integer>> RowDBIdsFraction = new LinkedHashMap<Integer, ArrayList<Integer>>();

	public static android.app.ProgressDialog progress = null;

	public static int rowDBSelectedIndex = 0;

	public static int committee_id = 0;

	public static int rowDBSelectedFragmentIndex = 0;

	public static int listFragmentWidth = -1;
	public static int gallaryFragmentWidth = -1;

	public static int detailFragmentWidth = -1;

	public static boolean isLandscape = true;

	public static boolean isOriantationChange = false;

	public static int startPosition = -1;

	public static Context context;
	public static LinearLayout childLayot = null;

	public static int calculatedScreenResolution = 0;

	public static Activity currentActiviry = null;

	public static String strHeading = null;
	public static boolean isSublistLoaded = false;
	public static int currentFragmentIndex = 0;
	public static int noOfSubTab = 3;
	public static final int SWIPE_MIN_DISTANCE = 50;
	public static final int SWIPE_THRESHOLD_VELOCITY = 100;
	public static GeneralListFragmentTab generalListFragmentTab = null;
	public static int lastRowDBId = -1;
	public static String currentPlanumTab = "";
	public static boolean bForTablet = false;
	public static int committeesId = -1;
	public static PlenumTvObject tvSchedule = null;

	public static TextView txtAgenda;
	public static TextView txtAgendaTitle;
	public static TextView txtHeader;
	public static String committeeRowId = "#**#";
	public static String committeeName = "";
	public static String committesStringId = "";
	public static String appState = "";
	public static boolean isFromMember = false;
     
	
	public static int committeeIdOnBack = 0;
	public static String fractionNameOnBack = "";
	public static int selectedSubMenuOnBack = 0;
	// public static boolean isFromVisitorContact = false;

	public static void setCurrentResolution(Activity a) {

		int totWidth = 0;
		a.getWindow().getWindowManager().getDefaultDisplay().getWidth();

		if (android.content.res.Configuration.ORIENTATION_LANDSCAPE == a.getResources().getSystem().getConfiguration().orientation) {
			totWidth = a.getWindow().getWindowManager().getDefaultDisplay().getWidth();
		} else {
			totWidth = a.getWindow().getWindowManager().getDefaultDisplay().getHeight();
		}

		System.out.println("Total Width : " + totWidth);

		calculatedScreenResolution = (totWidth * 325) / 1280;

		System.out.println("Calculated Width : " + calculatedScreenResolution);
	}

	/**
	 * Check if we are online.
	 */
	public static boolean isOnline(Activity activity) {
		ConnectivityManager connectivityManager = (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
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

	public static void mLockScreenRotation(Activity activity) {
		// Stop the screen orientation changing during an event
		switch (activity.getResources().getConfiguration().orientation) {
		case Configuration.ORIENTATION_PORTRAIT:
			activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
			break;
		case Configuration.ORIENTATION_LANDSCAPE:
			activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
			break;
		}
		if (activity instanceof NewsStartActivity || activity instanceof NewsActivityTablet) {

		} else {
			// DataHolder.createProgressDialog(activity);
		}
	}

	public static void releaseScreenLock(Activity activity) {
		activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
		// dismissProgress();
	}

	public static void createProgressDialog(Context activity) {
		try {
			if (progress == null) {
				progress = android.app.ProgressDialog.show(activity, null, "Daten werden geladen", true);
			} else {
				progress.show();
			}
		} catch (Exception e) {

		}
	}

	public static void dismissProgress() {
		try {
			if (!AppConstant.isFragmentSupported) {

				if (progress != null) {
					progress.dismiss();
					progress = null;
				}
			}
		} catch (Exception e) {

		}
	}

	public static void alertboxOk(final Activity activity) {
		new AlertDialog.Builder(activity).setMessage("Zur Zeit scheint keine Internet-Verbindung verfügbar zu sein.").setCancelable(true)
				.setNeutralButton(android.R.string.ok, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						Intent intent = new Intent();
						intent.setClass(activity, MembersListActivity.class);
						intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
						activity.startActivity(intent);

					}
				}).show();

	}

	public static void finishDialog(WebView webview) {
		webview.setWebViewClient(new WebViewClient() {
			@Override
			public void onPageFinished(WebView view, String url) {
				super.onPageFinished(view, url);
				try {
					if (progress != null) {
						progress.dismiss();
						progress = null;
					}
				} catch (Exception e) {

				}
			}
		});
	}

	public static void finishDialog() {
		try {
			if (progress != null) {
				progress.dismiss();
				progress = null;
			}
		} catch (Exception e) {

		}
	}

	public static boolean isPrevCommittee = false;
	public static int memberCount = 0;
	public static boolean isVisitors = false;
	public static String newsListTitle = "";
	public static String newsListTitlePlenum = "";
	public static boolean isVideoLoad = false;
}
