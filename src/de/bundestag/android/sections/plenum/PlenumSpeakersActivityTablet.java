package de.bundestag.android.sections.plenum;

import java.util.Date;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.Display;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
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
import de.bundestag.android.sections.visitors.VisitorsOffersActivity;
import de.bundestag.android.sections.visitors.VisitorsOffersActivityTablet;

public class PlenumSpeakersActivityTablet extends BaseActivity {

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

	private TextView subMenu2;
	private TextView subMenu21;
	private TextView subMenu3;
	private TextView subMenu31;

	private ProgressDialog dialog;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState, this);
		// if(AppConstant.isFragmentSupported){
		// DataHolder.dismissProgress();
		// }
		setContentView(R.layout.plenum_speaker);

		orienation = getResources().getConfiguration().orientation;
		newOrientation = orienation;
		final Activity a = this;

		subMenu2 = (TextView) findViewById(R.id.subMenu2);
		subMenu21 = (TextView) findViewById(R.id.subMenu21);
		subMenu3 = (TextView) findViewById(R.id.subMenu3);
		subMenu31 = (TextView) findViewById(R.id.subMenu31);

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
					if (!((a instanceof PlenumSpeakersActivityTablet) || (a instanceof PlenumSitzungenActivity) || (a instanceof PlenumTvActivity))) {

						Intent intent = new Intent();

						if (isOnline() && isPLenarySitting()) {
							if (AppConstant.isFragmentSupported) {
								intent.setClass(a, PlenumSpeakersActivityTablet.class);
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
					if (!((a instanceof PlenumSpeakersActivityTablet) || (a instanceof PlenumSitzungenActivity) || (a instanceof PlenumTvActivity))) {

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

			subMenu2.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					if (AppConstant.isFragmentSupported) {
						try {
							subMenu2.setBackgroundResource(R.drawable.top_navigation_gradient_active);
							subMenu3.setBackgroundResource(R.drawable.top_navigation_gradient);
							subMenu21.setBackgroundResource(R.drawable.top_navigation_gradient_active);
							subMenu31.setBackgroundResource(R.drawable.top_navigation_gradient);
						} catch (Exception e) {
						}
						DataHolder.currentPlanumTab = "speaker";
						DataHolder.generalListFragmentTab.handlePlenumSpeakersList();
						try {
							((TextView) findViewById(R.id.txtHeader)).setText("Redner\n" + (DataHolder.txtAgenda.getText().toString().trim().split(":")[1].trim()));
						} catch (Exception e) {
						}
					} else {
						Intent intent = new Intent();
						intent.setClass(PlenumSpeakersActivityTablet.this, PlenumSpeakersActivity.class);
						intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
						startActivity(intent);
						PlenumSpeakersActivityTablet.this.overridePendingTransition(0, 0);
					}
				}
			});

			subMenu3.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					if (AppConstant.isFragmentSupported) {
						try {
							subMenu3.setBackgroundResource(R.drawable.top_navigation_gradient_active);
							subMenu2.setBackgroundResource(R.drawable.top_navigation_gradient);
							subMenu31.setBackgroundResource(R.drawable.top_navigation_gradient_active);
							subMenu21.setBackgroundResource(R.drawable.top_navigation_gradient);
						} catch (Exception e) {
						}
						DataHolder.currentPlanumTab = "debate";
						try {
							Date currentDate = new Date();
							((TextView) findViewById(R.id.txtHeader)).setText("Debatten am " + (currentDate.getDate()) + "." + (currentDate.getMonth() + 1) + "."
									+ (currentDate.getYear() + 1900));
						} catch (Exception e) {
						}
						DataHolder.generalListFragmentTab.handlePlenumDebatesList();
					} else {
						Intent intent = new Intent();
						intent.setClass(PlenumSpeakersActivityTablet.this, PlenumDebatesActivity.class);
						intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
						startActivity(intent);
						PlenumSpeakersActivityTablet.this.overridePendingTransition(0, 0);
					}
				}
			});

		} else {
			subMenu21.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					if (AppConstant.isFragmentSupported) {
						try {
							subMenu21.setBackgroundResource(R.drawable.top_navigation_gradient_active);
							subMenu31.setBackgroundResource(R.drawable.top_navigation_gradient);
							subMenu2.setBackgroundResource(R.drawable.top_navigation_gradient_active);
							subMenu3.setBackgroundResource(R.drawable.top_navigation_gradient);
						} catch (Exception e) {
						}
						DataHolder.currentPlanumTab = "speaker";
						DataHolder.generalListFragmentTab.handlePlenumSpeakersList();
						try {
							((TextView) findViewById(R.id.txtHeader)).setText("Redner\n" + (DataHolder.txtAgenda.getText().toString().trim().split(":")[1].trim()));
						} catch (Exception e) {
						}
					} else {
						Intent intent = new Intent();
						intent.setClass(PlenumSpeakersActivityTablet.this, PlenumSpeakersActivity.class);
						intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
						startActivity(intent);
						PlenumSpeakersActivityTablet.this.overridePendingTransition(0, 0);
					}
				}
			});

			subMenu31.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					if (AppConstant.isFragmentSupported) {
						try {
							subMenu31.setBackgroundResource(R.drawable.top_navigation_gradient_active);
							subMenu21.setBackgroundResource(R.drawable.top_navigation_gradient);
							subMenu3.setBackgroundResource(R.drawable.top_navigation_gradient_active);
							subMenu2.setBackgroundResource(R.drawable.top_navigation_gradient);
						} catch (Exception e) {
						}
						DataHolder.currentPlanumTab = "debate";
						try {
							Date currentDate = new Date();
							((TextView) findViewById(R.id.txtHeader)).setText("Debatten am " + (currentDate.getDate()) + "." + (currentDate.getMonth() + 1) + "."
									+ (currentDate.getYear() + 1900));
						} catch (Exception e) {
						}
						DataHolder.generalListFragmentTab.handlePlenumDebatesList();
					} else {
						Intent intent = new Intent();
						intent.setClass(PlenumSpeakersActivityTablet.this, PlenumDebatesActivity.class);
						intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
						startActivity(intent);
						PlenumSpeakersActivityTablet.this.overridePendingTransition(0, 0);
					}
				}
			});
		}

		try {
			if (orienation == android.content.res.Configuration.ORIENTATION_LANDSCAPE) {

				try {
					tab21.setImageResource(R.drawable.tab_item_land_active_2);
				} catch (Exception e) {
					tab2.setImageResource(R.drawable.tab_item_land_active_2);
				}
			} else {
				try {
					tab21.setImageResource(R.drawable.tab_item_active_2);
				} catch (Exception e) {
					tab2.setImageResource(R.drawable.tab_item_active_2);
				}
			}

		} catch (Exception e) {
		}

		if (DataHolder.currentPlanumTab.equals("speaker")) {

			try {
				subMenu21.setBackgroundResource(R.drawable.top_navigation_gradient_active);
				subMenu31.setBackgroundResource(R.drawable.top_navigation_gradient);
			} catch (Exception e) {
				subMenu2.setBackgroundResource(R.drawable.top_navigation_gradient_active);
				subMenu3.setBackgroundResource(R.drawable.top_navigation_gradient);
			}
		} else {
			try {
				subMenu31.setBackgroundResource(R.drawable.top_navigation_gradient_active);
				subMenu21.setBackgroundResource(R.drawable.top_navigation_gradient);
			} catch (Exception e) {
				subMenu3.setBackgroundResource(R.drawable.top_navigation_gradient_active);
				subMenu2.setBackgroundResource(R.drawable.top_navigation_gradient);
			}
		}

	}

	protected void onResume() {
		super.onResume();
	}

	protected void onPause() {
		super.onPause();
	}

	/**
	 * Hack to handle the back button.
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (!AppConstant.isFragmentSupported) {
				Intent intent = new Intent();
				intent.setClass(this, PlenumNewsActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
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
		ViewGroup v = (ViewGroup) getWindow().getDecorView().findViewById(R.id.plenum_video);
		LinearLayout mainMenuLayout = (LinearLayout) findViewById(R.id.mainMenu);
		LinearLayout sideBar = (LinearLayout) findViewById(R.id.headerGradient);
		LinearLayout bottomPort = (LinearLayout) findViewById(R.id.bottomPort);
		LinearLayout topPort = (LinearLayout) findViewById(R.id.topPort);
		LinearLayout headerSubMenu = (LinearLayout) findViewById(R.id.headerSubMenu);

		try {
			if (newOrientation == android.content.res.Configuration.ORIENTATION_LANDSCAPE) {

				try {
					tab21.setImageResource(R.drawable.tab_item_land_active_2);
				} catch (Exception e) {
					tab2.setImageResource(R.drawable.tab_item_land_active_2);
				}
			} else {
				try {
					tab21.setImageResource(R.drawable.tab_item_active_2);
				} catch (Exception e) {
					tab2.setImageResource(R.drawable.tab_item_active_2);
				}
			}

		} catch (Exception e) {
		}

		if (DataHolder.currentPlanumTab.equals("speaker")) {

			try {
				subMenu21.setBackgroundResource(R.drawable.top_navigation_gradient_active);
				subMenu31.setBackgroundResource(R.drawable.top_navigation_gradient);
			} catch (Exception e) {
				subMenu2.setBackgroundResource(R.drawable.top_navigation_gradient_active);
				subMenu3.setBackgroundResource(R.drawable.top_navigation_gradient);
			}
		} else {
			try {
				subMenu31.setBackgroundResource(R.drawable.top_navigation_gradient_active);
				subMenu21.setBackgroundResource(R.drawable.top_navigation_gradient);
			} catch (Exception e) {
				subMenu3.setBackgroundResource(R.drawable.top_navigation_gradient_active);
				subMenu2.setBackgroundResource(R.drawable.top_navigation_gradient);
			}
		}

		if (orienation == newConfig.ORIENTATION_PORTRAIT) {

			if (newConfig.orientation != newConfig.ORIENTATION_PORTRAIT) {

				sideBar.setVisibility(View.GONE);
				mainMenuLayout.setVisibility(View.GONE);

				if (!PlenumSittingDetailFragment.isFullScreen) {

					bottomPort.setVisibility(View.VISIBLE);
					topPort.setVisibility(View.VISIBLE);
					try {
						headerSubMenu.setVisibility(View.GONE);
					} catch (Exception e) {
					}
				} else {
					Display display = getWindow().getWindowManager().getDefaultDisplay();
					PlenumSittingDetailFragment.videoView.setLayoutParams(new FrameLayout.LayoutParams(display.getWidth(), display.getHeight()));
				}

				v.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.FILL_PARENT, 1.0f));
				//

			} else {
				if (!PlenumSittingDetailFragment.isFullScreen) {

					sideBar.setVisibility(View.VISIBLE);
					mainMenuLayout.setVisibility(View.VISIBLE);
					try {
						headerSubMenu.setVisibility(View.VISIBLE);
					} catch (Exception e) {
					}
				} else {
					Display display = getWindow().getWindowManager().getDefaultDisplay();
					PlenumSittingDetailFragment.videoView.setLayoutParams(new FrameLayout.LayoutParams(display.getWidth(), display.getHeight()));
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

				if (!PlenumSittingDetailFragment.isFullScreen) {
					bottomPort.setVisibility(View.VISIBLE);
					topPort.setVisibility(View.VISIBLE);
				} else {
					Display display = getWindow().getWindowManager().getDefaultDisplay();
					PlenumSittingDetailFragment.videoView.setLayoutParams(new FrameLayout.LayoutParams(display.getWidth(), display.getHeight()));
				}
				v.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1.0f));
				//

			} else {
				if (!PlenumSittingDetailFragment.isFullScreen) {

					sideBar.setVisibility(View.VISIBLE);
					mainMenuLayout.setVisibility(View.VISIBLE);
					try {
						headerSubMenu.setVisibility(View.VISIBLE);
					} catch (Exception e) {
					}
				} else {
					Display display = getWindow().getWindowManager().getDefaultDisplay();
					PlenumSittingDetailFragment.videoView.setLayoutParams(new FrameLayout.LayoutParams(display.getWidth(), display.getHeight()));
				}

				bottomPort.setVisibility(View.GONE);
				topPort.setVisibility(View.GONE);

				v.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1.0f));
			}
		}

	}
}
