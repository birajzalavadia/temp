package de.bundestag.android.sections.visitors;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import de.bundestag.android.BaseActivity;
import de.bundestag.android.R;
import de.bundestag.android.constant.AppConstant;
import de.bundestag.android.constant.DataHolder;
import de.bundestag.android.sections.committees.CommitteesActivity;
import de.bundestag.android.sections.committees.CommitteesActivityTablet;
import de.bundestag.android.sections.committees.CommitteesDetailsTasksActivity;
import de.bundestag.android.sections.members.MembersListActivity;
import de.bundestag.android.sections.news.NewsActivity;
import de.bundestag.android.sections.news.NewsActivityTablet;
import de.bundestag.android.sections.plenum.PlenumNewsActivity;
import de.bundestag.android.sections.plenum.PlenumSitzungenActivity;
import de.bundestag.android.sections.plenum.PlenumSpeakersActivity;
import de.bundestag.android.sections.plenum.PlenumTvActivity;

public class VisitorsOffersActivityTablet extends BaseActivity {
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

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState, this);
		if (AppConstant.isFragmentSupported) {
			DataHolder.dismissProgress();
		}
		DataHolder.isVisitors = true;
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
		// DataHolder.createProgressDialog(this);
		setContentView(R.layout.visitors_offers);

		Fragment f = getSupportFragmentManager().findFragmentById(R.id.detailsFragment);
		if (f != null) {
			AppConstant.isFragmentSupported = true;
		}
		checkShowOptionsMenu(this);

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

		if (orienation == android.content.res.Configuration.ORIENTATION_LANDSCAPE) {

			TextView subMenu1 = (TextView) findViewById(R.id.subMenu1);
			subMenu1.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					if (!(a instanceof VisitorsOffersActivityTablet)) {

						if (AppConstant.isFragmentSupported) {
							DataHolder.createProgressDialog(VisitorsOffersActivityTablet.this);
						}
						Intent intent = new Intent();
						intent.setClass(VisitorsOffersActivityTablet.this, VisitorsOffersActivityTablet.class);
						intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
						startActivity(intent);
						VisitorsOffersActivityTablet.this.overridePendingTransition(0, 0);
					}
				}
			});

			TextView subMenu2 = (TextView) findViewById(R.id.subMenu2);
			subMenu2.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					if (!(a instanceof VisitorsLocationsActivityTablet)) {

						if (AppConstant.isFragmentSupported) {
							DataHolder.createProgressDialog(VisitorsOffersActivityTablet.this);
						}
						Intent intent = new Intent();
						intent.setClass(VisitorsOffersActivityTablet.this, VisitorsLocationsActivityTablet.class);
						intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
						startActivity(intent);
						VisitorsOffersActivityTablet.this.overridePendingTransition(0, 0);
					}
				}
			});

			if (DataHolder.isOnline(VisitorsOffersActivityTablet.this)) {
				TextView subMenu3 = (TextView) findViewById(R.id.subMenu3);
				subMenu3.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						if (!(a instanceof VisitorsNewsActivityTablet)) {

							if (AppConstant.isFragmentSupported) {
								DataHolder.createProgressDialog(VisitorsOffersActivityTablet.this);
							}
							Intent intent = new Intent();
							intent.setClass(VisitorsOffersActivityTablet.this, VisitorsNewsActivityTablet.class);
							intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
							startActivity(intent);
							VisitorsOffersActivityTablet.this.overridePendingTransition(0, 0);
						}
					}
				});
			}

			TextView subMenu4 = (TextView) findViewById(R.id.subMenu4);
			subMenu4.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					if (!(a instanceof VisitorsContactActivity)) {

						if (AppConstant.isFragmentSupported) {
							DataHolder.createProgressDialog(VisitorsOffersActivityTablet.this);
						}
						Intent intent = new Intent();
						intent.setClass(VisitorsOffersActivityTablet.this, VisitorsContactActivity.class);
						intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
						startActivity(intent);
						VisitorsOffersActivityTablet.this.overridePendingTransition(0, 0);
					}
				}
			});
		} else {
			subMenu1.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					if (!(a instanceof VisitorsOffersActivityTablet)) {

						DataHolder.createProgressDialog(VisitorsOffersActivityTablet.this);
						Intent intent = new Intent();
						intent.setClass(VisitorsOffersActivityTablet.this, VisitorsOffersActivityTablet.class);
						intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
						startActivity(intent);
						VisitorsOffersActivityTablet.this.overridePendingTransition(0, 0);
					}
				}
			});

			TextView subMenu21 = (TextView) findViewById(R.id.subMenu21);
			subMenu21.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					if (!(a instanceof VisitorsLocationsActivityTablet)) {

						if (AppConstant.isFragmentSupported) {
							DataHolder.createProgressDialog(VisitorsOffersActivityTablet.this);
						}
						Intent intent = new Intent();
						intent.setClass(VisitorsOffersActivityTablet.this, VisitorsLocationsActivityTablet.class);
						intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
						startActivity(intent);
						VisitorsOffersActivityTablet.this.overridePendingTransition(0, 0);
					}
				}
			});

			if (DataHolder.isOnline(VisitorsOffersActivityTablet.this)) {
				TextView subMenu31 = (TextView) findViewById(R.id.subMenu31);
				subMenu31.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						if (!(a instanceof VisitorsNewsActivityTablet)) {

							if (AppConstant.isFragmentSupported) {
								DataHolder.createProgressDialog(VisitorsOffersActivityTablet.this);
							}
							Intent intent = new Intent();
							intent.setClass(VisitorsOffersActivityTablet.this, VisitorsNewsActivityTablet.class);
							intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
							startActivity(intent);
							VisitorsOffersActivityTablet.this.overridePendingTransition(0, 0);
						}
					}
				});
			}

			TextView subMenu41 = (TextView) findViewById(R.id.subMenu41);
			subMenu41.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					if (!(a instanceof VisitorsContactActivity)) {

						if (AppConstant.isFragmentSupported) {
							DataHolder.createProgressDialog(VisitorsOffersActivityTablet.this);
						}
						Intent intent = new Intent();
						intent.setClass(VisitorsOffersActivityTablet.this, VisitorsContactActivity.class);
						intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
						startActivity(intent);
						VisitorsOffersActivityTablet.this.overridePendingTransition(0, 0);
					}
				}
			});
		}

		try {
			if (orienation == android.content.res.Configuration.ORIENTATION_LANDSCAPE) {

				try {
					tab51.setImageResource(R.drawable.tab_item_land_active_5);
				} catch (Exception e) {
					tab5.setImageResource(R.drawable.tab_item_land_active_5);
				}
			} else {
				try {
					tab51.setImageResource(R.drawable.tab_item_active_5);
				} catch (Exception e) {
					tab5.setImageResource(R.drawable.tab_item_active_5);
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
	 * Hack to handle the back button.
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (DataHolder.isSublistLoaded && AppConstant.isFragmentSupported) {
				DataHolder.isSublistLoaded = false;
				DataHolder.rowDBSelectedIndex = 0;
				Intent intent = new Intent();
				intent.setClass(this, VisitorsOffersActivityTablet.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent);
				overridePendingTransition(0, 0);
			} else {
				Intent intent = new Intent();
				if (AppConstant.isFragmentSupported)
					intent.setClass(this, NewsActivityTablet.class);
				else
					intent.setClass(this, NewsActivity.class);
				this.startActivity(intent);
				overridePendingTransition(0, 0);
				return true;
			}
		}

		return super.onKeyDown(keyCode, event);
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
					tab51.setImageResource(R.drawable.tab_item_land_active_5);
				} catch (Exception e) {
					tab5.setImageResource(R.drawable.tab_item_land_active_5);
				}
			} else {
				try {
					tab51.setImageResource(R.drawable.tab_item_active_5);
				} catch (Exception e) {
					tab5.setImageResource(R.drawable.tab_item_active_5);
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
