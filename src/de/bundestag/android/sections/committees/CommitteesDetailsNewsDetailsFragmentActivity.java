package de.bundestag.android.sections.committees;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import de.bundestag.android.BaseActivity;
import de.bundestag.android.R;
import de.bundestag.android.constant.AppConstant;
import de.bundestag.android.constant.DataHolder;
import de.bundestag.android.sections.members.MembersCommitteesDetailsMembersActivity;
import de.bundestag.android.sections.members.MembersCommitteesDetailsNewsActivity;
import de.bundestag.android.sections.members.MembersCommitteesDetailsTasksActivity;
import de.bundestag.android.sections.members.MembersListActivity;
import de.bundestag.android.sections.news.NewsActivityTablet;
import de.bundestag.android.sections.plenum.PlenumNewsActivity;
import de.bundestag.android.sections.plenum.PlenumSitzungenActivity;
import de.bundestag.android.sections.plenum.PlenumSpeakersActivity;
import de.bundestag.android.sections.plenum.PlenumTvActivity;
import de.bundestag.android.sections.visitors.VisitorsContactActivity;
import de.bundestag.android.sections.visitors.VisitorsLocationsActivityTablet;
import de.bundestag.android.sections.visitors.VisitorsNewsActivityTablet;

import de.bundestag.android.storage.CommitteesDatabaseAdapter;
import de.bundestag.android.synchronization.BaseSynchronizeTask;

public class CommitteesDetailsNewsDetailsFragmentActivity extends BaseActivity {
	public static int newOrientation = 0;
	public static int orienation = 0;
	private ImageView tab1;

	private ImageView tab2;

	private ImageView tab3;

	private ImageView tab4;

	private ImageView tab5;

	private ImageView tab11;

	private ImageView tab21;

	private ImageView tab31;

	private ImageView tab41;

	private ImageView tab51;

	private TextView subMenu1;
	private TextView subMenu11;

	private ProgressDialog dialog;
	
	public static final String KEY_COMMITTEE_ID = "COMMITTEE_ID";

	public int committeeId = 0;

	public int newsId = 0;

	public String detailsXMLURL;
	public String committeesIdString = null;

	private BaseSynchronizeTask task = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState, this);

		Intent launchingIntent = getIntent();
		Bundle extras = launchingIntent.getExtras();
		String committeesId = (String) extras.get(KEY_COMMITTEE_ID);
		this.committeesIdString = committeesId;

		DataHolder.committesStringId = committeesId;
		DataHolder.committeeName = getCommitteeIdName(committeesId);

		this.committeeId = getCommitteeId(committeesId);
		DataHolder.committeesId = this.committeeId = committeeId;
		setContentView(R.layout.committees_detail_list);
		
		orienation = getResources().getConfiguration().orientation;
		newOrientation = orienation;
		final Activity a = this;
		subMenu1 = (TextView) findViewById(R.id.subMenu1);
		subMenu11 = (TextView) findViewById(R.id.subMenu11);

		if (orienation == android.content.res.Configuration.ORIENTATION_PORTRAIT) {
			tab1 = (ImageView) findViewById(R.id.tab1);
			tab1.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					// DataHolder.disableMainMenu(a);
					// DataHolder.disableSubMenu(a);
					if (!(a instanceof NewsActivityTablet)) {

						Intent intent = new Intent();
						intent.setClass(a, NewsActivityTablet.class);
						intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
						startActivity(intent);
						a.overridePendingTransition(0, 0);
					}
				}
			});

			tab2 = (ImageView) findViewById(R.id.tab2);
			tab2.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					if (!((a instanceof PlenumSpeakersActivity) || (a instanceof PlenumSitzungenActivity) || (a instanceof PlenumTvActivity))) {

						Intent intent = new Intent();

						if (isOnline() && isPLenarySitting()) {
							if (AppConstant.isFragmentSupported) {
								intent.setClass(a, PlenumSpeakersActivity.class);
							} else {
								intent.setClass(a, PlenumNewsActivity.class);
							}

						} else {
							intent.setClass(a, PlenumSitzungenActivity.class);
						}
						intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
						startActivity(intent);
						a.overridePendingTransition(0, 0);
					}
				}
			});

			tab3 = (ImageView) findViewById(R.id.tab3);
			tab3.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					if (!(a instanceof MembersListActivity)) {

						Intent intent = new Intent();
						intent.setClass(a, MembersListActivity.class);
						intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
						startActivity(intent);
						a.overridePendingTransition(0, 0);
					}
				}
			});

			tab4 = (ImageView) findViewById(R.id.tab4);
			tab4.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {

					if (!((a instanceof CommitteesActivityTablet) || (a instanceof CommitteesDetailsTasksActivity))) {

						Intent intent = new Intent();
						if (DataHolder.isOnline(a) || AppConstant.isFragmentSupported) {
							intent.setClass(a, CommitteesActivityTablet.class);
						} else {
							intent.setClass(a, CommitteesDetailsTasksActivity.class);
						}
						intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
						startActivity(intent);
						a.overridePendingTransition(0, 0);
					}
				}
			});
			tab5 = (ImageView) findViewById(R.id.tab5);
			tab5.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					if (!(a instanceof CommitteesDetailsNewsDetailsFragmentActivity)) {
						Intent intent = new Intent();
						intent.setClass(a, CommitteesDetailsNewsDetailsFragmentActivity.class);
						intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
						startActivity(intent);
						a.overridePendingTransition(0, 0);
					}
				}
			});
		} else {
			tab11 = (ImageView) findViewById(R.id.tab11);
			tab11.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					// DataHolder.disableMainMenu(a);
					// DataHolder.disableSubMenu(a);
					if (!(a instanceof NewsActivityTablet)) {

						Intent intent = new Intent();
						intent.setClass(a, NewsActivityTablet.class);
						intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
						startActivity(intent);
						a.overridePendingTransition(0, 0);
					}
				}
			});

			tab21 = (ImageView) findViewById(R.id.tab21);
			tab21.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					if (!((a instanceof PlenumSpeakersActivity) || (a instanceof PlenumSitzungenActivity) || (a instanceof PlenumTvActivity))) {

						Intent intent = new Intent();

						if (isOnline() && isPLenarySitting()) {
							if (AppConstant.isFragmentSupported) {
								intent.setClass(a, PlenumSpeakersActivity.class);
							} else {
								intent.setClass(a, PlenumNewsActivity.class);
							}

						} else {
							intent.setClass(a, PlenumSitzungenActivity.class);
						}
						intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
						startActivity(intent);
						a.overridePendingTransition(0, 0);
					}
				}
			});

			tab31 = (ImageView) findViewById(R.id.tab31);
			tab31.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					if (!(a instanceof MembersListActivity)) {

						Intent intent = new Intent();
						intent.setClass(a, MembersListActivity.class);
						intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
						startActivity(intent);
						a.overridePendingTransition(0, 0);
					}
				}
			});

			tab41 = (ImageView) findViewById(R.id.tab41);
			tab41.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {

					if (!((a instanceof CommitteesActivityTablet) || (a instanceof CommitteesDetailsTasksActivity))) {

						Intent intent = new Intent();
						if (DataHolder.isOnline(a) || AppConstant.isFragmentSupported) {
							intent.setClass(a, CommitteesActivityTablet.class);
						} else {
							intent.setClass(a, CommitteesDetailsTasksActivity.class);
						}
						intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
						startActivity(intent);
						a.overridePendingTransition(0, 0);
					}
				}
			});
			tab51 = (ImageView) findViewById(R.id.tab51);
			tab51.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					if (!(a instanceof CommitteesDetailsNewsDetailsFragmentActivity)) {
						Intent intent = new Intent();
						intent.setClass(a, CommitteesDetailsNewsDetailsFragmentActivity.class);
						intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
						startActivity(intent);
						a.overridePendingTransition(0, 0);
					}
				}
			});
		}

		if (orienation == android.content.res.Configuration.ORIENTATION_LANDSCAPE) {

			TextView subMenu1 = (TextView) findViewById(R.id.subMenu1);
			subMenu1.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {

					if (!((a instanceof MembersCommitteesDetailsNewsActivity) || (a instanceof CommitteesDetailsNewsActivity))) {

//						setNullFlags();
						Intent intent = new Intent();
						intent.putExtra("index", committeeId);

						if ((a instanceof MembersCommitteesDetailsMembersActivity) || (a instanceof MembersCommitteesDetailsNewsActivity)
								|| (a instanceof MembersCommitteesDetailsTasksActivity)) {
							intent.setClass(a, MembersCommitteesDetailsNewsActivity.class);
						} else {
							if (AppConstant.isFragmentSupported) {
								intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
								intent.putExtra(CommitteesDetailsNewsDetailsFragmentActivity.KEY_COMMITTEE_ID, DataHolder.committesStringId);
								intent.setClass(a, CommitteesDetailsNewsDetailsFragmentActivity.class);

							} else {
								intent.setClass(a, CommitteesDetailsNewsActivity.class);
							}
						}

						intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
						startActivity(intent);
						a.overridePendingTransition(0, 0);
					}
				}
			});

			final TextView subMenu2 = (TextView) findViewById(R.id.subMenu2);
			subMenu2.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					// setNullFlags();
					Intent intent = new Intent();
					intent.putExtra("index", committeeId);
					if (AppConstant.isFragmentSupported) {

						if (!(a instanceof CommitteesMembersTabletActivity)) {

							DataHolder._id = -1;
							DataHolder._subId = -1;
							DataHolder._oldPosition = -1;
							Rect rect = new Rect();
							int[] xy = new int[2];

							// subMenu3.getWindowVisibleDisplayFrame(rect);
							subMenu2.getLocalVisibleRect(rect);
							subMenu2.getLocationOnScreen(xy);
							intent.putExtra("x", xy[0]);
							intent.putExtra("y", xy[1]);
							intent.putExtra("rect", rect);
//							intent.putExtra("selectedSubmenu", selectedSubmenu);
							intent.setClass(a, CommitteesMembersTabletActivity.class);
//							setLayoutHolder(2);
							startActivityForResult(intent, 0);
						}
					} else {
						if ((a instanceof MembersCommitteesDetailsMembersActivity) || (a instanceof MembersCommitteesDetailsNewsActivity)
								|| (a instanceof MembersCommitteesDetailsTasksActivity)) {
							intent.setClass(a, MembersCommitteesDetailsMembersActivity.class);
						} else {
							intent.setClass(a, CommitteesDetailsMembersActivity.class);
						}
						startActivity(intent);
					}

					a.overridePendingTransition(0, 0);
				}
			});

			TextView subMenu3 = (TextView) findViewById(R.id.subMenu3);
			subMenu3.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					// setNullFlags();
					if (!((a instanceof MembersCommitteesDetailsTasksActivity) || (a instanceof CommitteesDetailsTasksActivity))) {

						Intent intent = new Intent();
						intent.putExtra("index", committeeId);

						if ((a instanceof MembersCommitteesDetailsMembersActivity) || (a instanceof MembersCommitteesDetailsNewsActivity)
								|| (a instanceof MembersCommitteesDetailsTasksActivity)) {
							intent.setClass(a, MembersCommitteesDetailsTasksActivity.class);
						} else {
							intent.setClass(a, CommitteesDetailsTasksActivity.class);
						}

						intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
						startActivity(intent);
						a.overridePendingTransition(0, 0);
					}
				}
			});
		} else {
			
			subMenu1.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {

					if (!((a instanceof MembersCommitteesDetailsNewsActivity) || (a instanceof CommitteesDetailsNewsActivity))) {

//						setNullFlags();
						Intent intent = new Intent();
						intent.putExtra("index", committeeId);

						if ((a instanceof MembersCommitteesDetailsMembersActivity) || (a instanceof MembersCommitteesDetailsNewsActivity)
								|| (a instanceof MembersCommitteesDetailsTasksActivity)) {
							intent.setClass(a, MembersCommitteesDetailsNewsActivity.class);
						} else {
							if (AppConstant.isFragmentSupported) {
								intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
								intent.putExtra(CommitteesDetailsNewsDetailsFragmentActivity.KEY_COMMITTEE_ID, DataHolder.committesStringId);
								intent.setClass(a, CommitteesDetailsNewsDetailsFragmentActivity.class);

							} else {
								intent.setClass(a, CommitteesDetailsNewsActivity.class);
							}
						}

						intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
						startActivity(intent);
						a.overridePendingTransition(0, 0);
					}
				}
			});

			final TextView subMenu21 = (TextView) findViewById(R.id.subMenu21);
			subMenu21.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					// setNullFlags();
					Intent intent = new Intent();
					intent.putExtra("index", committeeId);
					if (AppConstant.isFragmentSupported) {

						if (!(a instanceof CommitteesMembersTabletActivity)) {

							DataHolder._id = -1;
							DataHolder._subId = -1;
							DataHolder._oldPosition = -1;
							Rect rect = new Rect();
							int[] xy = new int[2];

							// subMenu3.getWindowVisibleDisplayFrame(rect);
							subMenu21.getLocalVisibleRect(rect);
							subMenu21.getLocationOnScreen(xy);
							intent.putExtra("x", xy[0]);
							intent.putExtra("y", xy[1]);
							intent.putExtra("rect", rect);
//							intent.putExtra("selectedSubmenu", selectedSubmenu);
							intent.setClass(a, CommitteesMembersTabletActivity.class);
//							setLayoutHolder(2);
							startActivityForResult(intent, 0);
						}
					} else {
						if ((a instanceof MembersCommitteesDetailsMembersActivity) || (a instanceof MembersCommitteesDetailsNewsActivity)
								|| (a instanceof MembersCommitteesDetailsTasksActivity)) {
							intent.setClass(a, MembersCommitteesDetailsMembersActivity.class);
						} else {
							intent.setClass(a, CommitteesDetailsMembersActivity.class);
						}
						startActivity(intent);
					}

					a.overridePendingTransition(0, 0);
				}
			});

			TextView subMenu31 = (TextView) findViewById(R.id.subMenu31);
			subMenu31.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					// setNullFlags();
					if (!((a instanceof MembersCommitteesDetailsTasksActivity) || (a instanceof CommitteesDetailsTasksActivity))) {

						Intent intent = new Intent();
						intent.putExtra("index", committeeId);

						if ((a instanceof MembersCommitteesDetailsMembersActivity) || (a instanceof MembersCommitteesDetailsNewsActivity)
								|| (a instanceof MembersCommitteesDetailsTasksActivity)) {
							intent.setClass(a, MembersCommitteesDetailsTasksActivity.class);
						} else {
							intent.setClass(a, CommitteesDetailsTasksActivity.class);
						}

						intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
						startActivity(intent);
						a.overridePendingTransition(0, 0);
					}
				}
			});
		}

		try {
			if (orienation == android.content.res.Configuration.ORIENTATION_LANDSCAPE) {

				try {
					tab41.setImageResource(R.drawable.tab_item_land_active_4);
				} catch (Exception e) {
					tab4.setImageResource(R.drawable.tab_item_land_active_4);
				}
			} else {
				try {
					tab41.setImageResource(R.drawable.tab_item_active_4);
				} catch (Exception e) {
					tab4.setImageResource(R.drawable.tab_item_active_4);
				}
			}

		} catch (Exception e) {
		}

		try {
			subMenu11.setBackgroundResource(R.drawable.top_navigation_gradient_active);
		} catch (Exception e) {
			subMenu1.setBackgroundResource(R.drawable.top_navigation_gradient_active);
		}
	}

	/**
	 * Hack to handle the back button for committees news details.
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (DataHolder.appState == "MembersListActivity") {
				Intent intent = new Intent();
				intent.setClass(this, MembersListActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				this.startActivity(intent);
				overridePendingTransition(0, 0);
				DataHolder.appState = "";
				DataHolder.rowDBSelectedIndex = 0;
				return true;
			} else if (DataHolder.appState == "CommitteesDetailsMembersByFractionActivity") {
				Intent intent = new Intent();
				intent.putExtra("committeesId", DataHolder.committeeIdOnBack);
				intent.putExtra("fractionName", DataHolder.fractionNameOnBack);
				intent.putExtra("selectedSubmenu", DataHolder.selectedSubMenuOnBack);

				intent.setClass(this, CommitteesDetailsMembersByFractionActivity.class);
				// intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent);
				// setResult(RESULT_CANCELED, intent);
				finish();
				DataHolder.appState = "";
			} else {
				Intent intent = new Intent();
				// intent.putExtra(CommitteesDetailsNewsActivity.KEY_COMMITTEE_NEWS_UPDATED,
				// true);
				// intent.putExtra("index", this.committeeId);
				// intent.setClass(this, CommitteesDetailsNewsActivity.class);
				// intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				if (!AppConstant.isFragmentSupported) {
					intent.setClass(this, CommitteesActivity.class);
				} else {
					intent.setClass(this, CommitteesActivityTablet.class);
				}
				this.startActivity(intent);
				overridePendingTransition(0, 0);

				return true;
			}
		}

		return super.onKeyDown(keyCode, event);
	}

	private String getCommitteeIdName(String rowId) {
		CommitteesDatabaseAdapter committeesDatabaseAdapter = new CommitteesDatabaseAdapter(this);
		committeesDatabaseAdapter.open();

		String committeeName = "";
		Cursor memberCursor = committeesDatabaseAdapter.fetchCommitteesById(committeesIdString);
		if (memberCursor.getCount() > 0) {
			committeeName = memberCursor.getString(memberCursor.getColumnIndex(CommitteesDatabaseAdapter.KEY_NAME));
		}
		memberCursor.close();
		committeesDatabaseAdapter.close();

		return committeeName;
	}

	@Override
	protected void finalize() throws Throwable {
		super.finalize();

		detailsXMLURL = null;

		task = null;
	}

	private int getCommitteeId(String committeesIdString) {
		CommitteesDatabaseAdapter committeesDatabaseAdapter = new CommitteesDatabaseAdapter(this);
		committeesDatabaseAdapter.open();

		int committeeId = -1;
		Cursor memberCursor = committeesDatabaseAdapter.fetchCommitteesById(committeesIdString);
		if (memberCursor.getCount() > 0) {
			committeeId = memberCursor.getInt(memberCursor.getColumnIndex(CommitteesDatabaseAdapter.KEY_ROWID));
		}
		memberCursor.close();
		committeesDatabaseAdapter.close();

		return committeeId;
	}
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);

		newOrientation = newConfig.orientation;
		ViewGroup v = null;
		v = (ViewGroup) getWindow().getDecorView().findViewById(R.id.visitorMain);
		if (v == null) {
			v = (ViewGroup) getWindow().getDecorView().findViewById(R.id.master);
		}
		LinearLayout mainMenuLayout = (LinearLayout) findViewById(R.id.mainMenu);
		LinearLayout sideBar = (LinearLayout) findViewById(R.id.headerGradient);
		LinearLayout bottomPort = (LinearLayout) findViewById(R.id.bottomPort);
		LinearLayout topPort = (LinearLayout) findViewById(R.id.topPort);
		LinearLayout headerSubMenu = (LinearLayout) findViewById(R.id.headerSubMenu);

		try {
			if (newOrientation == android.content.res.Configuration.ORIENTATION_LANDSCAPE) {

				try {
					tab41.setImageResource(R.drawable.tab_item_land_active_4);
				} catch (Exception e) {
					tab4.setImageResource(R.drawable.tab_item_land_active_4);
				}
			} else {
				try {
					tab41.setImageResource(R.drawable.tab_item_active_4);
				} catch (Exception e) {
					tab4.setImageResource(R.drawable.tab_item_active_4);
				}
			}

		} catch (Exception e) {
		}

		try {
			subMenu11.setBackgroundResource(R.drawable.top_navigation_gradient_active);
		} catch (Exception e) {
			subMenu1.setBackgroundResource(R.drawable.top_navigation_gradient_active);
		}

		if (orienation == newConfig.ORIENTATION_PORTRAIT) {

			if (newConfig.orientation != newConfig.ORIENTATION_PORTRAIT) {

				sideBar.setVisibility(View.GONE);
				mainMenuLayout.setVisibility(View.GONE);

				bottomPort.setVisibility(View.VISIBLE);
				topPort.setVisibility(View.VISIBLE);
				try {
					headerSubMenu.setVisibility(View.GONE);
				} catch (Exception e) {
				}

				v.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.FILL_PARENT, 1.0f));

			} else {

				sideBar.setVisibility(View.VISIBLE);
				mainMenuLayout.setVisibility(View.VISIBLE);
				try {
					headerSubMenu.setVisibility(View.VISIBLE);
				} catch (Exception e) {
				}

				bottomPort.setVisibility(View.GONE);
				topPort.setVisibility(View.GONE);
				v.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.FILL_PARENT, 1.0f));

			}
		} else {

			if (newConfig.orientation == newConfig.ORIENTATION_PORTRAIT) {

				sideBar.setVisibility(View.GONE);
				mainMenuLayout.setVisibility(View.GONE);
				try {
					headerSubMenu.setVisibility(View.GONE);
				} catch (Exception e) {
				}

				bottomPort.setVisibility(View.VISIBLE);
				topPort.setVisibility(View.VISIBLE);

				v.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1.0f));
				//

			} else {

				sideBar.setVisibility(View.VISIBLE);
				mainMenuLayout.setVisibility(View.VISIBLE);
				try {
					headerSubMenu.setVisibility(View.VISIBLE);
				} catch (Exception e) {
				}

				bottomPort.setVisibility(View.GONE);
				topPort.setVisibility(View.GONE);

				v.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1.0f));
			}
		}

	}
}