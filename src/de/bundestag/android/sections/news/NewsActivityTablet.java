package de.bundestag.android.sections.news;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.Display;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import de.bundestag.android.BaseActivity;
import de.bundestag.android.R;
import de.bundestag.android.constant.AppConstant;
import de.bundestag.android.constant.DataHolder;
import de.bundestag.android.customviews.CustomScrollView;
import de.bundestag.android.sections.committees.CommitteesActivity;
import de.bundestag.android.sections.committees.CommitteesActivityTablet;
import de.bundestag.android.sections.committees.CommitteesDetailsTasksActivity;
import de.bundestag.android.sections.members.MembersListActivity;
import de.bundestag.android.sections.plenum.PlenumNewsActivity;
import de.bundestag.android.sections.plenum.PlenumSitzungenActivity;
import de.bundestag.android.sections.plenum.PlenumSpeakersActivity;
import de.bundestag.android.sections.plenum.PlenumTvActivity;
import de.bundestag.android.sections.visitors.VisitorsOffersActivity;
import de.bundestag.android.sections.visitors.VisitorsOffersActivityTablet;
import de.bundestag.android.storage.NewsDatabaseAdapter;
import de.bundestag.android.synchronization.SynchronizeCheckNewsTask;
import de.bundestag.android.synchronization.SynchronizeNewsTask;

public class NewsActivityTablet extends BaseActivity {
	public static final String SHOW_NEWS_KEY = "SHOW_NEWS";

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

	private ProgressDialog dialog;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState, this);

		DataHolder.currentActiviry = this;
		Display display = getWindowManager().getDefaultDisplay();
		if (display.getWidth() < display.getHeight()) {
			AppConstant.LEFT_FRAGMENT_LAND = display.getHeight() * 40 / 100;
			AppConstant.LEFT_FRAGMENT_PORT = display.getWidth() * 40 / 100;
			AppConstant.RIGHT_FRAGMENT_LAND = display.getHeight() * 60 / 100;
			AppConstant.RIGHT_FRAGMENT_PORT = display.getWidth() * 60 / 100;
		} else {
			AppConstant.LEFT_FRAGMENT_PORT = display.getHeight() * 40 / 100;
			AppConstant.LEFT_FRAGMENT_LAND = display.getWidth() * 40 / 100;
			AppConstant.RIGHT_FRAGMENT_PORT = display.getHeight() * 60 / 100;
			AppConstant.RIGHT_FRAGMENT_LAND = display.getWidth() * 60 / 100;
		}
		if (this.getResources().getConfiguration().orientation == android.content.res.Configuration.ORIENTATION_LANDSCAPE) {
			DataHolder.listFragmentWidth = AppConstant.LEFT_FRAGMENT_LAND;
			DataHolder.detailFragmentWidth = AppConstant.RIGHT_FRAGMENT_LAND;
			DataHolder.isLandscape = true;
		} else {
			DataHolder.listFragmentWidth = AppConstant.LEFT_FRAGMENT_PORT;
			DataHolder.detailFragmentWidth = AppConstant.RIGHT_FRAGMENT_PORT;
			DataHolder.isLandscape = false;
		}

		if (DataHolder.isOnline(this)) {
			boolean showNews = false;
			Intent launchingIntent = getIntent();
			Bundle extras = launchingIntent.getExtras();
			if (extras != null) {
				showNews = extras.containsKey(SHOW_NEWS_KEY);
			}

			if (showNews) {
				setContentView(R.layout.news);
				SharedPreferences settings = getSharedPreferences("Identify", 0);
				SharedPreferences.Editor editor = settings.edit();
				if (settings.getInt("LeftFragmentSize", -1) != -1) {
					AppConstant.isFragmentSupported = settings.getBoolean("FragmentSupport", false);
					DataHolder.calculatedScreenResolution = settings.getInt("LeftFragmentSize", -1);
				} else {
					editor.putBoolean("FragmentSupport", AppConstant.isFragmentSupported);
					editor.putInt("LeftFragmentSize", DataHolder.calculatedScreenResolution);
					editor.commit();
				}

				orienation = getResources().getConfiguration().orientation;
				newOrientation = orienation;
				final Activity a = NewsActivityTablet.this;

				if (orienation == android.content.res.Configuration.ORIENTATION_PORTRAIT) {
					tab1 = (ImageView) findViewById(R.id.tab1);
					tab1.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							// DataHolder.disableMainMenu(a);
							// DataHolder.disableSubMenu(a);
							if (!(a instanceof NewsActivity)) {

								Intent intent = new Intent();
								intent.setClass(a, NewsActivity.class);
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
							if (!(a instanceof VisitorsOffersActivityTablet)) {
								Intent intent = new Intent();
								intent.setClass(a, VisitorsOffersActivityTablet.class);
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
							if (!(a instanceof NewsActivity)) {

								Intent intent = new Intent();
								intent.setClass(a, NewsActivity.class);
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
							if (!(a instanceof VisitorsOffersActivityTablet)) {
								Intent intent = new Intent();
								intent.setClass(a, VisitorsOffersActivityTablet.class);
								intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
								startActivity(intent);
								a.overridePendingTransition(0, 0);
							}
						}
					});
				}

			} else {
				checkNewsNeedsUpdating();
			}

		} else {
			setContentView(R.layout.news_offline);
			// Intent intent = new Intent();
			// intent.setClass(this, MembersListActivity.class);
			// startActivity(intent);
		}

		setProgressBarIndeterminateVisibility(false);
		try {
			if (orienation == android.content.res.Configuration.ORIENTATION_LANDSCAPE) {

				try {
					tab11.setImageResource(R.drawable.tab_item_land_active_1);
				} catch (Exception e) {
					tab1.setImageResource(R.drawable.tab_item_land_active_1);
				}
			} else {
				try {
					tab11.setImageResource(R.drawable.tab_item_active_1);
				} catch (Exception e) {
					tab1.setImageResource(R.drawable.tab_item_active_1);
				}
			}

		} catch (Exception e) {

		}

	}

	/**
	 * Hack to handle the back button for news list.
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (DataHolder.isSublistLoaded && AppConstant.isFragmentSupported) {
				Intent intent = new Intent();
				intent.setClass(this, NewsActivityTablet.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent);
				return true;
			} else {
				moveTaskToBack(true);
				DataHolder._id = -1;
				return true;
			}
		}

		return super.onKeyDown(keyCode, event);
	}

	/**
	 * Check if the news needs updating.
	 */
	private void checkNewsNeedsUpdating() {
		NewsDatabaseAdapter newsDatabaseAdapter = new NewsDatabaseAdapter(this);
		newsDatabaseAdapter.open();
		boolean existNews = newsDatabaseAdapter.existNews();
		newsDatabaseAdapter.close();

		if (existNews) {
			SynchronizeCheckNewsTask synchronizeCheckNewsTask = new SynchronizeCheckNewsTask();
			synchronizeCheckNewsTask.execute(this);
		} else {
			SynchronizeNewsTask newsTask = new SynchronizeNewsTask();
			newsTask.execute(this);
		}
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		// DataHolder.finishDialog();
		// DataHolder.createProgressDialog(this);
		final ScrollView scrView = (ScrollView) findViewById(R.id.scrView);

		newOrientation = newConfig.orientation;
		ViewGroup v = null;
		v = (ViewGroup) getWindow().getDecorView().findViewById(R.id.news_main);
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
					tab11.setImageResource(R.drawable.tab_item_land_active_1);
				} catch (Exception e) {
					tab1.setImageResource(R.drawable.tab_item_land_active_1);
				}
			} else {
				try {
					tab11.setImageResource(R.drawable.tab_item_active_1);
				} catch (Exception e) {
					tab1.setImageResource(R.drawable.tab_item_active_1);
				}
			}

		} catch (Exception e) {
		}

		if (orienation == newConfig.ORIENTATION_PORTRAIT) {

			if (newConfig.orientation != newConfig.ORIENTATION_PORTRAIT) {

				sideBar.setVisibility(View.GONE);
				mainMenuLayout.setVisibility(View.GONE);

				if (!NewsDetailsFragment.isFullScreen) {

					bottomPort.setVisibility(View.VISIBLE);
					topPort.setVisibility(View.VISIBLE);
					try {
						headerSubMenu.setVisibility(View.GONE);
					} catch (Exception e) {
					}
				} else {
					Display display = getWindow().getWindowManager().getDefaultDisplay();
					NewsDetailsFragment.videoView.setLayoutParams(new FrameLayout.LayoutParams(display.getWidth(), display.getHeight()));
					NewsDetailsFragment.scrView.setLayoutParams(new FrameLayout.LayoutParams(display.getWidth(), display.getHeight()));
				}

				v.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.FILL_PARENT, 1.0f));
				//

			} else {

				if (!NewsDetailsFragment.isFullScreen) {

					sideBar.setVisibility(View.VISIBLE);
					mainMenuLayout.setVisibility(View.VISIBLE);
					try {
						headerSubMenu.setVisibility(View.VISIBLE);
					} catch (Exception e) {
					}
				} else {
					Display display = getWindow().getWindowManager().getDefaultDisplay();
					NewsDetailsFragment.videoView.setLayoutParams(new FrameLayout.LayoutParams(display.getWidth(), display.getHeight()));
					NewsDetailsFragment.scrView.setLayoutParams(new FrameLayout.LayoutParams(display.getWidth(), display.getHeight()));
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

				if (!NewsDetailsFragment.isFullScreen) {
					bottomPort.setVisibility(View.VISIBLE);
					topPort.setVisibility(View.VISIBLE);
				} else {
					Display display = getWindow().getWindowManager().getDefaultDisplay();
					NewsDetailsFragment.videoView.setLayoutParams(new FrameLayout.LayoutParams(display.getWidth(), display.getHeight()));
					NewsDetailsFragment.scrView.setLayoutParams(new FrameLayout.LayoutParams(display.getWidth(), display.getHeight()));
				}
				v.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1.0f));
				//

			} else {
				if (!NewsDetailsFragment.isFullScreen) {

					sideBar.setVisibility(View.VISIBLE);
					mainMenuLayout.setVisibility(View.VISIBLE);
					try {
						headerSubMenu.setVisibility(View.VISIBLE);
					} catch (Exception e) {
					}
				} else {
					Display display = getWindow().getWindowManager().getDefaultDisplay();
					NewsDetailsFragment.videoView.setLayoutParams(new FrameLayout.LayoutParams(display.getWidth(), display.getHeight()));
					NewsDetailsFragment.scrView.setLayoutParams(new FrameLayout.LayoutParams(display.getWidth(), display.getHeight()));
				}

				bottomPort.setVisibility(View.GONE);
				topPort.setVisibility(View.GONE);

				v.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1.0f));
			}
		}

		NewsDetailsFragment.refNewsDetailsFragment.handleWebview();
		// DataHolder.finishDialog();

	}
	// ////neeraj added start ///
	// @Override
	// protected void onResume() {
	// // TODO Auto-generated method stub
	// super.onResume();
	// if (!DataHolder.isOnline(this)){
	// DataHolder.releaseScreenLock(this);
	// }
	//
	// }
	// ////neeraj added end ///
}
