package de.bundestag.android.fragments;

import java.util.Date;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import de.bundestag.android.R;
import de.bundestag.android.constant.AppConstant;
import de.bundestag.android.constant.DataHolder;
import de.bundestag.android.parser.PlenumXMLParser;
import de.bundestag.android.parser.objects.PlenumObject;
import de.bundestag.android.sections.committees.CommitteesDetailsMembersActivity;
import de.bundestag.android.sections.committees.CommitteesDetailsMembersByFractionActivity;
import de.bundestag.android.sections.committees.CommitteesDetailsNewsActivity;
import de.bundestag.android.sections.committees.CommitteesDetailsNewsDetailsFragmentActivity;
import de.bundestag.android.sections.committees.CommitteesDetailsTasksActivity;
import de.bundestag.android.sections.committees.CommitteesMembersTabletActivity;
import de.bundestag.android.sections.members.MembersCityActivity;
import de.bundestag.android.sections.members.MembersCityActivityTab;
import de.bundestag.android.sections.members.MembersCommitteesDetailsMembersActivity;
import de.bundestag.android.sections.members.MembersCommitteesDetailsNewsActivity;
import de.bundestag.android.sections.members.MembersCommitteesDetailsTasksActivity;
import de.bundestag.android.sections.members.MembersElectionActivity;
import de.bundestag.android.sections.members.MembersElectionActivityTab;
import de.bundestag.android.sections.members.MembersFractionActivity;
import de.bundestag.android.sections.members.MembersFractionActivityTab;
import de.bundestag.android.sections.members.MembersListActivity;
import de.bundestag.android.sections.members.MembersSubpageMembersActivity;
import de.bundestag.android.sections.plenum.DebateNewsActivity;
import de.bundestag.android.sections.plenum.PlenumAudioActivity;
import de.bundestag.android.sections.plenum.PlenumAufgabeActivity;
import de.bundestag.android.sections.plenum.PlenumDebatesActivity;
import de.bundestag.android.sections.plenum.PlenumNewsActivity;
import de.bundestag.android.sections.plenum.PlenumSitzungenActivity;
import de.bundestag.android.sections.plenum.PlenumSpeakersActivity;
import de.bundestag.android.sections.plenum.PlenumSpeakersActivityTablet;
import de.bundestag.android.sections.plenum.PlenumTvActivity;
import de.bundestag.android.sections.plenum.PlenumVideoActivity;
import de.bundestag.android.sections.visitors.VisitorsContactActivity;
import de.bundestag.android.sections.visitors.VisitorsDetailsActivity;
import de.bundestag.android.sections.visitors.VisitorsLocationsActivity;
import de.bundestag.android.sections.visitors.VisitorsLocationsActivityTablet;
import de.bundestag.android.sections.visitors.VisitorsLocationsListActivity;
import de.bundestag.android.sections.visitors.VisitorsNewsActivity;
import de.bundestag.android.sections.visitors.VisitorsNewsActivityTablet;
import de.bundestag.android.sections.visitors.VisitorsOffersActivity;
import de.bundestag.android.sections.visitors.VisitorsOffersActivityTablet;
import de.bundestag.android.sections.visitors.VisitorsOffersListActivity;
import de.bundestag.android.storage.CommitteesDatabaseAdapter;

/**
 * General sub menu fragment.
 * 
 * Takes care of creating a sub menu for each section needing it.
 */
public class GeneralSubMenuFragment extends Fragment {
	private LinearLayout layout;

	private LayoutInflater inflater;

	private ViewGroup container;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		this.inflater = inflater;

		this.container = container;

		Activity activity = getActivity();
		if ((activity instanceof PlenumNewsActivity) || (activity instanceof PlenumSpeakersActivity) || (activity instanceof PlenumSpeakersActivityTablet)
				|| (activity instanceof PlenumDebatesActivity) || (activity instanceof PlenumVideoActivity) || (activity instanceof PlenumAudioActivity)
				|| (activity instanceof PlenumSitzungenActivity || (activity instanceof PlenumAufgabeActivity || (activity instanceof DebateNewsActivity)))
				|| (activity instanceof PlenumTvActivity && AppConstant.isFragmentSupported)) {
			int selectedSubmenu = -1;
			if (activity instanceof PlenumNewsActivity) {
				selectedSubmenu = 1;
			} else if ((activity instanceof PlenumSpeakersActivity) || (activity instanceof PlenumSpeakersActivityTablet)) {
				selectedSubmenu = 2;
			} else if (activity instanceof PlenumDebatesActivity || activity instanceof DebateNewsActivity) {
				selectedSubmenu = 3;
			} else if (activity instanceof PlenumVideoActivity) {
				selectedSubmenu = 4;
			} else if (activity instanceof PlenumAudioActivity) {
				selectedSubmenu = 5;
			} else if (activity instanceof PlenumSitzungenActivity) {
				selectedSubmenu = 1;
			} else if (activity instanceof PlenumAufgabeActivity) {
				if (isOnline())
					selectedSubmenu = 5;
				else
					selectedSubmenu = 4;
			} else if (activity instanceof PlenumTvActivity) {
				selectedSubmenu = 4;
			}

			if (isOnline() && isPLenarySitting()) {
				handlePlenumSubMenu(selectedSubmenu);
			} else {
				if (isOnline()) {
					handlePlenumSubMenuNositting(selectedSubmenu);
				} else {
					handlePlenumSubMenuOffline(selectedSubmenu);
				}
			}
		}

		else if ((activity instanceof MembersListActivity) || (activity instanceof MembersCityActivity) || (activity instanceof MembersFractionActivity)
				|| (activity instanceof MembersElectionActivity) || (activity instanceof MembersSubpageMembersActivity)) {
			int selectedSubmenu = -1;
			if (activity instanceof MembersListActivity) {
				selectedSubmenu = 1;
			} else if (activity instanceof MembersFractionActivity) {
				selectedSubmenu = 2;
			} else if (activity instanceof MembersCityActivity) {
				selectedSubmenu = 3;
			} else if (activity instanceof MembersElectionActivity) {
				selectedSubmenu = 4;
			} else if (activity instanceof MembersSubpageMembersActivity) {

				MembersSubpageMembersActivity membersSubpageMembersActivity = (MembersSubpageMembersActivity) activity;
				selectedSubmenu = membersSubpageMembersActivity.subPageId + 1;

			}

			handleMembersSubMenu(selectedSubmenu);
		}

		else if ((activity instanceof CommitteesDetailsNewsActivity) || (activity instanceof CommitteesDetailsMembersActivity)
				|| (activity instanceof CommitteesDetailsNewsDetailsFragmentActivity) || (activity instanceof CommitteesDetailsTasksActivity)
				|| (activity instanceof MembersCommitteesDetailsNewsActivity) || (activity instanceof MembersCommitteesDetailsMembersActivity)
				|| (activity instanceof MembersCommitteesDetailsTasksActivity) || (activity instanceof CommitteesDetailsMembersByFractionActivity)
				&& AppConstant.isFragmentSupported) {
			int committeeId = -1;
			int selectedSubmenu = -1;
			if (activity instanceof CommitteesDetailsNewsActivity) {
				selectedSubmenu = 1;
				CommitteesDetailsNewsActivity committeesDetailsNewsActivity = (CommitteesDetailsNewsActivity) activity;
				committeeId = committeesDetailsNewsActivity.committeesId;
			} else if (activity instanceof CommitteesDetailsMembersActivity) {
				selectedSubmenu = 2;
				CommitteesDetailsMembersActivity committeesDetailsMembersActivity = (CommitteesDetailsMembersActivity) activity;
				committeeId = committeesDetailsMembersActivity.committeesId;
			} else if (activity instanceof CommitteesDetailsMembersByFractionActivity) {
				selectedSubmenu = 2;
				CommitteesDetailsMembersByFractionActivity committeesDetailsMembersByFractionActivity = (CommitteesDetailsMembersByFractionActivity) activity;
				committeeId = committeesDetailsMembersByFractionActivity.committeesId;
			}

			else if (activity instanceof CommitteesDetailsTasksActivity) {
				selectedSubmenu = 3;
				CommitteesDetailsTasksActivity committeesDetailsTasksActivity = (CommitteesDetailsTasksActivity) activity;
				committeeId = committeesDetailsTasksActivity.committeesId;
			} else if (activity instanceof CommitteesDetailsNewsDetailsFragmentActivity) {
				selectedSubmenu = 1;
				CommitteesDetailsNewsDetailsFragmentActivity committeesDetailsNewsDetailsFragmentActivity = (CommitteesDetailsNewsDetailsFragmentActivity) activity;
				committeeId = committeesDetailsNewsDetailsFragmentActivity.committeeId;
			}

			// Aktuell will not appear if there are no news
			boolean hasNews = true;
			// int committee= 0;
			// if(getActivity() instanceof CommitteesDetailsNewsActivity) {
			// committee = ((CommitteesDetailsNewsActivity)
			// getActivity()).committeesId;
			// } else

			CommitteesDatabaseAdapter committeesDatabaseAdapter = new CommitteesDatabaseAdapter(getActivity());
			committeesDatabaseAdapter.open();

			int count = committeesDatabaseAdapter.countCommitteesNews(committeeId);

			if (count == 0) {
				hasNews = false;
			}

			if (isOnline() && hasNews)
				handleCommitteesSubMenu(committeeId, selectedSubmenu, activity);
			else
				handleCommitteesSubMenuOffline(committeeId, selectedSubmenu, activity);

		}

		else if ((activity instanceof VisitorsOffersActivity) || (activity instanceof VisitorsOffersActivityTablet) || (activity instanceof VisitorsOffersListActivity)
				|| (activity instanceof VisitorsDetailsActivity) || (activity instanceof VisitorsLocationsActivity) || (activity instanceof VisitorsLocationsActivityTablet)
				|| (activity instanceof VisitorsLocationsListActivity) || (activity instanceof VisitorsNewsActivity) || (activity instanceof VisitorsNewsActivityTablet)
				|| (activity instanceof VisitorsContactActivity)) {
			int selectedSubmenu = -1;
			if ((activity instanceof VisitorsOffersActivity) || (activity instanceof VisitorsOffersActivityTablet) || (activity instanceof VisitorsOffersListActivity)) {
				selectedSubmenu = 1;
			} else if ((activity instanceof VisitorsLocationsActivityTablet) || (activity instanceof VisitorsLocationsActivity)
					|| (activity instanceof VisitorsLocationsListActivity)) {
				selectedSubmenu = 2;
			} else if (activity instanceof VisitorsNewsActivity || activity instanceof VisitorsNewsActivityTablet) {
				selectedSubmenu = 3;
			} else if (activity instanceof VisitorsContactActivity) {
				selectedSubmenu = 4;
			}
			handleVisitorsSubMenu(selectedSubmenu);
		}

		return layout;
	}

	private void handlePlenumSubMenu(int selectedSubmenu) {
		layout = (LinearLayout) inflater.inflate(R.layout.sub_menu_plenum, container, false);

		if (AppConstant.isFragmentSupported && DataHolder.currentPlanumTab.equalsIgnoreCase("speaker")) {
			selectedSubmenu = 2;
		} else if (AppConstant.isFragmentSupported && DataHolder.currentPlanumTab.equalsIgnoreCase("debate")) {
			selectedSubmenu = 3;
		}
		LinearLayout menuHolder = null;
		if (selectedSubmenu == 1) {
			menuHolder = (LinearLayout) layout.findViewById(R.id.subMenuHolder1);
		} else if (selectedSubmenu == 2) {
			menuHolder = (LinearLayout) layout.findViewById(R.id.subMenuHolder2);
		} else if (selectedSubmenu == 3) {
			menuHolder = (LinearLayout) layout.findViewById(R.id.subMenuHolder3);
		} else if (selectedSubmenu == 4) {
			menuHolder = (LinearLayout) layout.findViewById(R.id.subMenuHolder4);
		} else if (selectedSubmenu == 5) {
			menuHolder = (LinearLayout) layout.findViewById(R.id.subMenuHolder5);
		}
		if (menuHolder != null) {
			menuHolder.setBackgroundResource(R.drawable.top_navigation_gradient_active);
		}

		TextView subMenu1 = (TextView) layout.findViewById(R.id.subMenu1);
		subMenu1.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (!(getActivity() instanceof PlenumNewsActivity)) {

					setNullFlags();
					Intent intent = new Intent();
					intent.setClass(getActivity(), PlenumNewsActivity.class);
					intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					startActivity(intent);
					getActivity().overridePendingTransition(0, 0);
				}
			}
		});

		final TextView subMenu2 = (TextView) layout.findViewById(R.id.subMenu2);
		final TextView subMenu3 = (TextView) layout.findViewById(R.id.subMenu3);
		subMenu2.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				setNullFlags();
				if (AppConstant.isFragmentSupported) {
					subMenu2.setBackgroundResource(R.drawable.top_navigation_gradient_active);
					subMenu3.setBackgroundResource(R.drawable.top_navigation_gradient);
					DataHolder.currentPlanumTab = "speaker";
					DataHolder.generalListFragmentTab.handlePlenumSpeakersList();
					try {
						((TextView) getActivity().findViewById(R.id.txtHeader)).setText("Redner\n" + (DataHolder.txtAgenda.getText().toString().trim().split(":")[1].trim()));
					} catch (Exception e) {
					}
				} else {
					Intent intent = new Intent();
					intent.setClass(getActivity(), PlenumSpeakersActivity.class);
					intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					startActivity(intent);
					getActivity().overridePendingTransition(0, 0);
				}
			}
		});

		subMenu3.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				setNullFlags();
				if (AppConstant.isFragmentSupported) {
					subMenu3.setBackgroundResource(R.drawable.top_navigation_gradient_active);
					subMenu2.setBackgroundResource(R.drawable.top_navigation_gradient);
					DataHolder.currentPlanumTab = "debate";
					try {
						Date currentDate = new Date();
						((TextView) getActivity().findViewById(R.id.txtHeader)).setText("Debatten am " + (currentDate.getDate()) + "." + (currentDate.getMonth() + 1) + "."
								+ (currentDate.getYear() + 1900));
					} catch (Exception e) {
					}
					DataHolder.generalListFragmentTab.handlePlenumDebatesList();
				} else {
					Intent intent = new Intent();
					intent.setClass(getActivity(), PlenumDebatesActivity.class);
					intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					startActivity(intent);
					getActivity().overridePendingTransition(0, 0);
				}
			}
		});

		TextView subMenu4 = (TextView) layout.findViewById(R.id.subMenu4);
		subMenu4.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				setNullFlags();
				Intent intent = new Intent();
				intent.setClass(getActivity(), PlenumVideoActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent);
				getActivity().overridePendingTransition(0, 0);
			}
		});

		TextView subMenu5 = (TextView) layout.findViewById(R.id.subMenu5);
		subMenu5.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				setNullFlags();
				Intent intent = new Intent();
				intent.setClass(getActivity(), PlenumAudioActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent);
				getActivity().overridePendingTransition(0, 0);
			}
		});
	}

	private void handlePlenumSubMenuOffline(int selectedSubmenu) {
		layout = (LinearLayout) inflater.inflate(R.layout.sub_menu_plenum_offline, container, false);

		LinearLayout menuHolder = null;
		if (selectedSubmenu == 1) {
			menuHolder = (LinearLayout) layout.findViewById(R.id.subMenuHolder1);
		} else if (selectedSubmenu == 3) {
			menuHolder = (LinearLayout) layout.findViewById(R.id.subMenuHolder3);
		} else if (selectedSubmenu == 4) {
			menuHolder = (LinearLayout) layout.findViewById(R.id.subMenuHolder4);
		}

		if (menuHolder != null) {
			menuHolder.setBackgroundResource(R.drawable.top_navigation_gradient_active);
		}

		TextView subMenu1 = (TextView) layout.findViewById(R.id.subMenu1);
		subMenu1.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (!(getActivity() instanceof PlenumSitzungenActivity)) {

					setNullFlags();
					Intent intent = new Intent();
					intent.setClass(getActivity(), PlenumSitzungenActivity.class);
					intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					startActivity(intent);
					getActivity().overridePendingTransition(0, 0);
				}
			}
		});

		TextView subMenu4 = (TextView) layout.findViewById(R.id.subMenu4);
		subMenu4.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				setNullFlags();
				Intent intent = new Intent();
				intent.setClass(getActivity(), PlenumAufgabeActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent);
				getActivity().overridePendingTransition(0, 0);
			}
		});

	}

	private void handlePlenumSubMenuNositting(int selectedSubmenu) {
		layout = (LinearLayout) inflater.inflate(R.layout.sub_menu_plenum_nosittings, container, false);

		LinearLayout menuHolder = null;
		if (selectedSubmenu == 1) {
			menuHolder = (LinearLayout) layout.findViewById(R.id.subMenuHolder1);
		} else if (selectedSubmenu == 3) {
			menuHolder = (LinearLayout) layout.findViewById(R.id.subMenuHolder3);
		} else if (selectedSubmenu == 4) {
			menuHolder = (LinearLayout) layout.findViewById(R.id.subMenuHolder4);
		} else if (selectedSubmenu == 5) {
			menuHolder = (LinearLayout) layout.findViewById(R.id.subMenuHolder5);
		}

		if (menuHolder != null) {
			menuHolder.setBackgroundResource(R.drawable.top_navigation_gradient_active);
		}

		TextView subMenu1 = (TextView) layout.findViewById(R.id.subMenu1);
		subMenu1.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				if (!(getActivity() instanceof PlenumSitzungenActivity)) {

					setNullFlags();
					Intent intent = new Intent();
					intent.setClass(getActivity(), PlenumSitzungenActivity.class);
					intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					startActivity(intent);
					getActivity().overridePendingTransition(0, 0);
				}
			}
		});

		TextView subMenu3 = (TextView) layout.findViewById(R.id.subMenu3);
		subMenu3.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (!(getActivity() instanceof DebateNewsActivity)) {

					setNullFlags();
					Intent intent = new Intent();
					intent.setClass(getActivity(), DebateNewsActivity.class);
					intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					startActivity(intent);
					getActivity().overridePendingTransition(0, 0);
				}
			}
		});

		TextView subMenu4 = (TextView) layout.findViewById(R.id.subMenu4);
		subMenu4.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				setNullFlags();
				Intent intent = new Intent();
				if (AppConstant.isFragmentSupported) {
					intent.setClass(getActivity(), PlenumTvActivity.class);
				} else {

					intent.setClass(getActivity(), PlenumVideoActivity.class);
				}
				if (!(getActivity() instanceof PlenumTvActivity)) {
					intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					startActivity(intent);
					getActivity().overridePendingTransition(0, 0);
				}
			}
		});

		TextView subMenu5 = (TextView) layout.findViewById(R.id.subMenu5);
		subMenu5.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				setNullFlags();
				Intent intent = new Intent();
				intent.setClass(getActivity(), PlenumAufgabeActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent);
				getActivity().overridePendingTransition(0, 0);
			}
		});
	}

	private void handleVisitorsSubMenu(int selectedSubmenu) {
		boolean isOnline = false;
		if (getActivity() instanceof VisitorsOffersActivity) {
			VisitorsOffersActivity visitorsOffersActivity = (VisitorsOffersActivity) getActivity();
			isOnline = DataHolder.isOnline(visitorsOffersActivity);
		} else if (getActivity() instanceof VisitorsOffersActivityTablet) {
			VisitorsOffersActivityTablet visitorsOffersActivityTablet = (VisitorsOffersActivityTablet) getActivity();
			isOnline = DataHolder.isOnline(visitorsOffersActivityTablet);
		} else if (getActivity() instanceof VisitorsOffersListActivity) {
			VisitorsOffersListActivity visitorsOffersListActivity = (VisitorsOffersListActivity) getActivity();
			isOnline = DataHolder.isOnline(visitorsOffersListActivity);
		} else if (getActivity() instanceof VisitorsLocationsActivity) {
			VisitorsLocationsActivity visitorsLocationsActivity = (VisitorsLocationsActivity) getActivity();
			isOnline = DataHolder.isOnline(visitorsLocationsActivity);
		} else if (getActivity() instanceof VisitorsLocationsActivityTablet) {
			VisitorsLocationsActivityTablet visitorsLocationsActivityTablet = (VisitorsLocationsActivityTablet) getActivity();
			isOnline = DataHolder.isOnline(visitorsLocationsActivityTablet);
		} else if (getActivity() instanceof VisitorsLocationsListActivity) {
			VisitorsLocationsListActivity visitorsLocationsListActivity = (VisitorsLocationsListActivity) getActivity();
			isOnline = DataHolder.isOnline(visitorsLocationsListActivity);
		} else if (getActivity() instanceof VisitorsNewsActivity) {
			VisitorsNewsActivity visitorsNewsActivity = (VisitorsNewsActivity) getActivity();
			isOnline = DataHolder.isOnline(visitorsNewsActivity);
		} else if (getActivity() instanceof VisitorsNewsActivityTablet) {
			VisitorsNewsActivityTablet visitorsNewsActivityTablet = (VisitorsNewsActivityTablet) getActivity();
			isOnline = DataHolder.isOnline(visitorsNewsActivityTablet);
		} else if (getActivity() instanceof VisitorsContactActivity) {
			VisitorsContactActivity visitorsContactActivity = (VisitorsContactActivity) getActivity();
			isOnline = DataHolder.isOnline(visitorsContactActivity);
		}

		if (isOnline) {
			layout = (LinearLayout) inflater.inflate(R.layout.sub_menu_visitors, container, false);
		} else {
			layout = (LinearLayout) inflater.inflate(R.layout.sub_menu_visitors_offline, container, false);

			if (selectedSubmenu == 3) {
				selectedSubmenu = 1;
			}
		}

		LinearLayout menuHolder = null;
		if (selectedSubmenu == 1) {
			menuHolder = (LinearLayout) layout.findViewById(R.id.subMenuHolder1);
		} else if (selectedSubmenu == 2) {
			menuHolder = (LinearLayout) layout.findViewById(R.id.subMenuHolder2);
		} else if (selectedSubmenu == 3) {
			menuHolder = (LinearLayout) layout.findViewById(R.id.subMenuHolder3);
		} else if (selectedSubmenu == 4) {
			menuHolder = (LinearLayout) layout.findViewById(R.id.subMenuHolder4);
		}
		if (menuHolder != null) {
			menuHolder.setBackgroundResource(R.drawable.top_navigation_gradient_active);
		}

		TextView subMenu1 = (TextView) layout.findViewById(R.id.subMenu1);
		subMenu1.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (!((getActivity() instanceof VisitorsOffersActivity) || (getActivity() instanceof VisitorsOffersActivityTablet))) {

					setNullFlags();
					Intent intent = new Intent();
					if (AppConstant.isFragmentSupported) {
						// DataHolder.createProgressDialog(getActivity());
						intent.setClass(getActivity(), VisitorsOffersActivityTablet.class);
					} else {

						intent.setClass(getActivity(), VisitorsOffersActivity.class);
					}
					intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					startActivity(intent);
					getActivity().overridePendingTransition(0, 0);
				}
			}
		});

		TextView subMenu2 = (TextView) layout.findViewById(R.id.subMenu2);
		subMenu2.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (!(getActivity() instanceof VisitorsLocationsActivity)) {

					setNullFlags();
					Intent intent = new Intent();
					if (AppConstant.isFragmentSupported) {
						intent.setClass(getActivity(), VisitorsLocationsActivityTablet.class);
					} else {
						intent.setClass(getActivity(), VisitorsLocationsActivity.class);
					}

					intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					startActivity(intent);
					getActivity().overridePendingTransition(0, 0);
				}
			}
		});

		if (isOnline) {
			TextView subMenu3 = (TextView) layout.findViewById(R.id.subMenu3);
			subMenu3.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					if (!(getActivity() instanceof VisitorsNewsActivity)) {

						setNullFlags();
						Intent intent = new Intent();
						if (AppConstant.isFragmentSupported) {
							intent.setClass(getActivity(), VisitorsNewsActivityTablet.class);
						} else {
							intent.setClass(getActivity(), VisitorsNewsActivity.class);
						}

						intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
						startActivity(intent);
						getActivity().overridePendingTransition(0, 0);
					}
				}
			});
		}

		TextView subMenu4 = (TextView) layout.findViewById(R.id.subMenu4);
		subMenu4.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (!(getActivity() instanceof VisitorsContactActivity)) {

					setNullFlags();
					if (AppConstant.isFragmentSupported) {
						DataHolder.createProgressDialog(getActivity());
					}
					Intent intent = new Intent();
					intent.setClass(getActivity(), VisitorsContactActivity.class);
					intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					startActivity(intent);
					getActivity().overridePendingTransition(0, 0);
				}
			}
		});
	}

	LinearLayout menuHolder = null;

	private void setLayoutHolder(int selectedSubmenu) {

		if (menuHolder != null) {
			menuHolder.setBackgroundResource(R.drawable.top_navigation_gradient);
		}
		if (selectedSubmenu == 1) {
			menuHolder = (LinearLayout) layout.findViewById(R.id.subMenuHolder1);
		} else if (selectedSubmenu == 2) {
			menuHolder = (LinearLayout) layout.findViewById(R.id.subMenuHolder2);
		} else if (selectedSubmenu == 3) {
			menuHolder = (LinearLayout) layout.findViewById(R.id.subMenuHolder3);
		} else if (selectedSubmenu == 4) {
			menuHolder = (LinearLayout) layout.findViewById(R.id.subMenuHolder4);
		}
		if (menuHolder != null) {
			menuHolder.setBackgroundResource(R.drawable.top_navigation_gradient_active);
		}
	}

	private void handleMembersSubMenu(int selectedSubmenu) {
		layout = (LinearLayout) inflater.inflate(R.layout.sub_menu_members, container, false);

		setLayoutHolder(selectedSubmenu);
		final int selectedItem = selectedSubmenu;
		TextView subMenu1 = (TextView) layout.findViewById(R.id.subMenu1);
		subMenu1.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (!(getActivity() instanceof MembersListActivity)) {

					setNullFlags();
					Intent intent = new Intent();
					intent.setClass(getActivity(), MembersListActivity.class);
					intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					startActivity(intent);
					getActivity().overridePendingTransition(0, 0);
				}
			}
		});

		final TextView subMenu2 = (TextView) layout.findViewById(R.id.subMenu2);
		subMenu2.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// setNullFlags();
				Intent intent = new Intent();
				if (!AppConstant.isFragmentSupported) {

					intent.setClass(getActivity(), MembersFractionActivity.class);
					startActivity(intent);
				} else {
					DataHolder._id = -1;
					DataHolder._subId = -1;
					DataHolder._oldPosition = -1;
					Rect rect = new Rect();
					int[] xy = new int[2];
					DataHolder.RowDBIds.size();
					// subMenu3.getWindowVisibleDisplayFrame(rect);
					subMenu2.getLocalVisibleRect(rect);
					subMenu2.getLocationOnScreen(xy);

					intent.putExtra("x", xy[0]);
					intent.putExtra("y", xy[1]);
					intent.putExtra("rect", rect);
					intent.putExtra("selectedSubmenu", selectedItem);
					intent.setClass(getActivity(), MembersFractionActivityTab.class);
					setLayoutHolder(2);
					startActivityForResult(intent, 0);
				}
			}
		});

		final TextView subMenu3 = (TextView) layout.findViewById(R.id.subMenu3);
		subMenu3.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				if (!AppConstant.isFragmentSupported) {

					intent.setClass(getActivity(), MembersCityActivity.class);
					intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					startActivity(intent);

				} else {
					if (!(getActivity() instanceof MembersCityActivityTab)) {

						DataHolder._id = -1;
						DataHolder._subId = -1;
						DataHolder._oldPosition = -1;
						Rect rect = new Rect();
						int[] xy = new int[2];
						DataHolder.RowDBIds.size();
						// subMenu3.getWindowVisibleDisplayFrame(rect);
						subMenu3.getLocalVisibleRect(rect);
						subMenu3.getLocationOnScreen(xy);

						intent.putExtra("x", xy[0]);
						intent.putExtra("y", xy[1]);
						intent.putExtra("rect", rect);
						intent.putExtra("selectedSubmenu", selectedItem);
						intent.setClass(getActivity(), MembersCityActivityTab.class);
						setLayoutHolder(3);
						startActivityForResult(intent, 0);
					}
				}

				getActivity().overridePendingTransition(0, 0);
			}
		});

		final TextView subMenu4 = (TextView) layout.findViewById(R.id.subMenu4);
		subMenu4.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				if (!AppConstant.isFragmentSupported) {

					intent.setClass(getActivity(), MembersElectionActivity.class);
					startActivity(intent);

				} else {
					if (!(getActivity() instanceof MembersElectionActivityTab)) {

						DataHolder._id = -1;
						DataHolder._subId = -1;
						DataHolder._oldPosition = -1;
						Rect rect = new Rect();
						int[] xy = new int[2];

						// subMenu3.getWindowVisibleDisplayFrame(rect);
						subMenu4.getLocalVisibleRect(rect);
						subMenu4.getLocationOnScreen(xy);
						intent.putExtra("x", xy[0]);
						intent.putExtra("y", xy[1]);
						intent.putExtra("rect", rect);
						intent.putExtra("selectedSubmenu", selectedItem);
						intent.setClass(getActivity(), MembersElectionActivityTab.class);
						setLayoutHolder(4);
						startActivityForResult(intent, 0);
					}

				}

				getActivity().overridePendingTransition(0, 0);
			}
		});
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if ((requestCode == 0) && (resultCode == getActivity().RESULT_OK)) {

			setLayoutHolder(data.getExtras().getInt("selectedSubmenu"));

		}
	}

	// private void setLayoutHolderCommittee(int selectedSubmenu) {
	//
	// if (menuHolder != null) {
	// menuHolder.setBackgroundResource(R.drawable.top_navigation_gradient);
	// }
	// if (selectedSubmenu == 1) {
	// menuHolder = (LinearLayout) layout.findViewById(R.id.subMenuHolder1);
	// } else if (selectedSubmenu == 2) {
	// menuHolder = (LinearLayout) layout.findViewById(R.id.subMenuHolder2);
	// } else if (selectedSubmenu == 3) {
	// menuHolder = (LinearLayout) layout.findViewById(R.id.subMenuHolder3);
	// }
	// if (menuHolder != null) {
	// menuHolder.setBackgroundResource(R.drawable.top_navigation_gradient_active);
	// }
	//
	// }
	private void handleCommitteesSubMenu(final int committeeId, final int selectedSubmenu, final Activity activity) {
		layout = (LinearLayout) inflater.inflate(R.layout.sub_menu_committees, container, false);

		setLayoutHolder(selectedSubmenu);

		TextView subMenu1 = (TextView) layout.findViewById(R.id.subMenu1);
		subMenu1.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				if (!((getActivity() instanceof MembersCommitteesDetailsNewsActivity) || (getActivity() instanceof CommitteesDetailsNewsActivity))) {

					setNullFlags();
					Intent intent = new Intent();
					intent.putExtra("index", committeeId);

					if ((activity instanceof MembersCommitteesDetailsMembersActivity) || (activity instanceof MembersCommitteesDetailsNewsActivity)
							|| (activity instanceof MembersCommitteesDetailsTasksActivity)) {
						intent.setClass(getActivity(), MembersCommitteesDetailsNewsActivity.class);
					} else {
						if (AppConstant.isFragmentSupported) {
							intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
							intent.putExtra(CommitteesDetailsNewsDetailsFragmentActivity.KEY_COMMITTEE_ID, DataHolder.committesStringId);
							intent.setClass(getActivity(), CommitteesDetailsNewsDetailsFragmentActivity.class);

						} else {
							intent.setClass(getActivity(), CommitteesDetailsNewsActivity.class);
						}
					}

					intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					startActivity(intent);
					getActivity().overridePendingTransition(0, 0);
				}
			}
		});

		final TextView subMenu2 = (TextView) layout.findViewById(R.id.subMenu2);
		subMenu2.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// setNullFlags();
				Intent intent = new Intent();
				intent.putExtra("index", committeeId);
				if (AppConstant.isFragmentSupported) {

					if (!(getActivity() instanceof CommitteesMembersTabletActivity)) {

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
						intent.putExtra("selectedSubmenu", selectedSubmenu);
						intent.setClass(getActivity(), CommitteesMembersTabletActivity.class);
						setLayoutHolder(2);
						startActivityForResult(intent, 0);
					}
				} else {
					if ((activity instanceof MembersCommitteesDetailsMembersActivity) || (activity instanceof MembersCommitteesDetailsNewsActivity)
							|| (activity instanceof MembersCommitteesDetailsTasksActivity)) {
						intent.setClass(getActivity(), MembersCommitteesDetailsMembersActivity.class);
					} else {
						intent.setClass(getActivity(), CommitteesDetailsMembersActivity.class);
					}
					startActivity(intent);
				}

				getActivity().overridePendingTransition(0, 0);
			}
		});

		TextView subMenu3 = (TextView) layout.findViewById(R.id.subMenu3);
		subMenu3.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// setNullFlags();
				if (!((getActivity() instanceof MembersCommitteesDetailsTasksActivity) || (getActivity() instanceof CommitteesDetailsTasksActivity))) {

					Intent intent = new Intent();
					intent.putExtra("index", committeeId);

					if ((activity instanceof MembersCommitteesDetailsMembersActivity) || (activity instanceof MembersCommitteesDetailsNewsActivity)
							|| (activity instanceof MembersCommitteesDetailsTasksActivity)) {
						intent.setClass(getActivity(), MembersCommitteesDetailsTasksActivity.class);
					} else {
						intent.setClass(getActivity(), CommitteesDetailsTasksActivity.class);
					}

					intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					startActivity(intent);
					getActivity().overridePendingTransition(0, 0);
				}
			}
		});
	}

	/**
	 * @param committeeId
	 * @param selectedSubmenu
	 * @param activity
	 */
	private void handleCommitteesSubMenuOffline(final int committeeId, final int selectedSubmenu, final Activity activity) {
		layout = (LinearLayout) inflater.inflate(R.layout.sub_menu_committees_offline, container, false);

		// LinearLayout menuHolder = null;
		// if (selectedSubmenu == 2) {
		// menuHolder = (LinearLayout) layout.findViewById(R.id.subMenuHolder2);
		// } else if (selectedSubmenu == 3) {
		// menuHolder = (LinearLayout) layout.findViewById(R.id.subMenuHolder3);
		// }
		// if (menuHolder != null) {
		// menuHolder.setBackgroundResource(R.drawable.top_navigation_gradient_active);
		// }
		setLayoutHolder(selectedSubmenu);
		final TextView subMenu2 = (TextView) layout.findViewById(R.id.subMenu2);
		subMenu2.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				setNullFlags();
				Intent intent = new Intent();
				intent.putExtra("index", committeeId);
				if (AppConstant.isFragmentSupported) {
					if (!(getActivity() instanceof CommitteesMembersTabletActivity)) {

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
						intent.putExtra("selectedSubmenu", selectedSubmenu);
						intent.setClass(getActivity(), CommitteesMembersTabletActivity.class);
						setLayoutHolder(2);
						startActivityForResult(intent, 0);
					}
				} else {
					if ((activity instanceof MembersCommitteesDetailsMembersActivity) || (activity instanceof MembersCommitteesDetailsNewsActivity)
							|| (activity instanceof MembersCommitteesDetailsTasksActivity)) {
						intent.setClass(getActivity(), MembersCommitteesDetailsMembersActivity.class);
					} else {
						intent.setClass(getActivity(), CommitteesDetailsMembersActivity.class);
					}
					startActivity(intent);

				}

				getActivity().overridePendingTransition(0, 0);
			}
		});

		TextView subMenu3 = (TextView) layout.findViewById(R.id.subMenu3);
		subMenu3.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				if (!((getActivity() instanceof MembersCommitteesDetailsTasksActivity) || (getActivity() instanceof CommitteesDetailsTasksActivity))) {

					setNullFlags();
					Intent intent = new Intent();
					intent.putExtra("index", committeeId);

					if ((activity instanceof MembersCommitteesDetailsMembersActivity) || (activity instanceof MembersCommitteesDetailsNewsActivity)
							|| (activity instanceof MembersCommitteesDetailsTasksActivity)) {
						intent.setClass(getActivity(), MembersCommitteesDetailsTasksActivity.class);
					} else {
						intent.setClass(getActivity(), CommitteesDetailsTasksActivity.class);
					}

					intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					startActivity(intent);
					getActivity().overridePendingTransition(0, 0);
				}
			}
		});
	}

	/**
	 * Check if we are online.
	 */
	public boolean isOnline() {
		ConnectivityManager connectivityManager = (ConnectivityManager) this.getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
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

	private void setNullFlags() {
		DataHolder._id = -1;
		DataHolder._subId = -1;
		DataHolder._oldPosition = -1;
		DataHolder.RowDBIds = null;
		DataHolder.rowDBSelectedIndex = 0;
	}

	@Override
	protected void finalize() throws Throwable {
		super.finalize();
		layout = null;
		inflater = null;
		container = null;
	}
}