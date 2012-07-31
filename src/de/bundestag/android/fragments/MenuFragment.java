package de.bundestag.android.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import de.bundestag.android.R;
import de.bundestag.android.constant.AppConstant;
import de.bundestag.android.constant.DataHolder;
import de.bundestag.android.parser.PlenumXMLParser;
import de.bundestag.android.parser.objects.PlenumObject;
import de.bundestag.android.sections.committees.CommitteesActivity;
import de.bundestag.android.sections.committees.CommitteesActivityTablet;
import de.bundestag.android.sections.committees.CommitteesDetailsMemberBiographyActivity;
import de.bundestag.android.sections.committees.CommitteesDetailsMemberCommitteesActivity;
import de.bundestag.android.sections.committees.CommitteesDetailsMemberContactActivity;
import de.bundestag.android.sections.committees.CommitteesDetailsMemberDetailsActivity;
import de.bundestag.android.sections.committees.CommitteesDetailsMemberInfoActivity;
import de.bundestag.android.sections.committees.CommitteesDetailsMembersActivity;
import de.bundestag.android.sections.committees.CommitteesDetailsMembersByFractionActivity;
import de.bundestag.android.sections.committees.CommitteesDetailsNewsActivity;
import de.bundestag.android.sections.committees.CommitteesDetailsNewsDetailsActivity;
import de.bundestag.android.sections.committees.CommitteesDetailsNewsDetailsFragmentActivity;
import de.bundestag.android.sections.committees.CommitteesDetailsTasksActivity;
import de.bundestag.android.sections.members.MembersCityActivity;
import de.bundestag.android.sections.members.MembersCommitteesDetailsMembersActivity;
import de.bundestag.android.sections.members.MembersCommitteesDetailsNewsActivity;
import de.bundestag.android.sections.members.MembersCommitteesDetailsTasksActivity;
import de.bundestag.android.sections.members.MembersDetailsActivity;
import de.bundestag.android.sections.members.MembersDetailsBiographyActivity;
import de.bundestag.android.sections.members.MembersDetailsCommitteesActivity;
import de.bundestag.android.sections.members.MembersDetailsContactActivity;
import de.bundestag.android.sections.members.MembersDetailsInfoActivity;
import de.bundestag.android.sections.members.MembersElectionActivity;
import de.bundestag.android.sections.members.MembersFractionActivity;
import de.bundestag.android.sections.members.MembersListActivity;
import de.bundestag.android.sections.members.MembersSubListDetailsActivity;
import de.bundestag.android.sections.members.MembersSubpageMembersActivity;
import de.bundestag.android.sections.news.NewsActivity;
import de.bundestag.android.sections.news.NewsActivityTablet;
import de.bundestag.android.sections.news.NewsDetailsActivity;
import de.bundestag.android.sections.news.NewsListActivity;
import de.bundestag.android.sections.news.NewsStartActivity;
import de.bundestag.android.sections.plenum.DebateNewsActivity;
import de.bundestag.android.sections.plenum.DebateNewsDetailsActivity;
import de.bundestag.android.sections.plenum.PlenumAudioActivity;
import de.bundestag.android.sections.plenum.PlenumAufgabeActivity;
import de.bundestag.android.sections.plenum.PlenumDebatesActivity;
import de.bundestag.android.sections.plenum.PlenumDebatesNewsDetailsActivity;
import de.bundestag.android.sections.plenum.PlenumNewsActivity;
import de.bundestag.android.sections.plenum.PlenumNewsDetailsActivity;
import de.bundestag.android.sections.plenum.PlenumObjectViewActivity;
import de.bundestag.android.sections.plenum.PlenumSitzungenActivity;
import de.bundestag.android.sections.plenum.PlenumSpeakersActivity;
import de.bundestag.android.sections.plenum.PlenumSpeakersActivityTablet;
import de.bundestag.android.sections.plenum.PlenumTvActivity;
import de.bundestag.android.sections.plenum.PlenumVideoActivity;
import de.bundestag.android.sections.visitors.VisitorsContactActivity;
import de.bundestag.android.sections.visitors.VisitorsDetailsActivity;
import de.bundestag.android.sections.visitors.VisitorsLocationsActivity;
import de.bundestag.android.sections.visitors.VisitorsLocationsActivityTablet;
import de.bundestag.android.sections.visitors.VisitorsLocationsDetailsActivity;
import de.bundestag.android.sections.visitors.VisitorsLocationsListActivity;
import de.bundestag.android.sections.visitors.VisitorsNewsActivity;
import de.bundestag.android.sections.visitors.VisitorsNewsActivityTablet;
import de.bundestag.android.sections.visitors.VisitorsNewsDetailsActivity;
import de.bundestag.android.sections.visitors.VisitorsOffersActivity;
import de.bundestag.android.sections.visitors.VisitorsOffersActivityTablet;
import de.bundestag.android.sections.visitors.VisitorsOffersDetailsActivity;
import de.bundestag.android.sections.visitors.VisitorsOffersListActivity;

public class MenuFragment extends Fragment {
	private LinearLayout layout;

	private ImageView tab1;

	private ImageView tab2;

	private ImageView tab3;

	private ImageView tab4;

	private ImageView tab5;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		layout = (LinearLayout) inflater.inflate(R.layout.tab_menu, container, false);

		tab1 = (ImageView) layout.findViewById(R.id.tab1);
		tab1.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// DataHolder.disableMainMenu(getActivity());
				// DataHolder.disableSubMenu(getActivity());
				DataHolder.isVisitors = false;
				if (!((getActivity() instanceof NewsActivity) || (getActivity() instanceof NewsActivityTablet))) {

					setNullFlags();

					Intent intent = new Intent();
					if (AppConstant.isFragmentSupported)
						intent.setClass(getActivity(), NewsActivityTablet.class);
					else
						intent.setClass(getActivity(), NewsActivity.class);

					intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					startActivity(intent);
					getActivity().overridePendingTransition(0, 0);
				}
			}
		});

		tab2 = (ImageView) layout.findViewById(R.id.tab2);
		tab2.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				DataHolder.isVisitors = false;
				if (!((getActivity() instanceof PlenumSpeakersActivityTablet) || (getActivity() instanceof PlenumSpeakersActivity))) {

					setNullFlags();
					Intent intent = new Intent();
					// if(AppConstant.isFragmentSupported){
					// DataHolder.createProgressDialog(getActivity());
					// }
					if (isOnline() && isPLenarySitting()) {
						if (AppConstant.isFragmentSupported) {
							intent.setClass(getActivity(), PlenumSpeakersActivityTablet.class);
						} else {
							intent.setClass(getActivity(), PlenumNewsActivity.class);
						}

					} else {
						intent.setClass(getActivity(), PlenumSitzungenActivity.class);
					}
					intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					startActivity(intent);
					getActivity().overridePendingTransition(0, 0);
				}
			}
		});

		tab3 = (ImageView) layout.findViewById(R.id.tab3);
		tab3.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				DataHolder.isVisitors = false;
				if (!(getActivity() instanceof MembersListActivity)) {

					setNullFlags();
					if (AppConstant.isFragmentSupported) {
						DataHolder.createProgressDialog(getActivity());
					}
					Intent intent = new Intent();
					intent.setClass(getActivity(), MembersListActivity.class);
					intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					startActivity(intent);
					getActivity().overridePendingTransition(0, 0);
				}
			}
		});

		tab4 = (ImageView) layout.findViewById(R.id.tab4);
		tab4.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				DataHolder.isVisitors = false;
				if (!((getActivity() instanceof CommitteesActivity) ||( getActivity() instanceof CommitteesActivityTablet)|| (getActivity() instanceof CommitteesDetailsTasksActivity))) {

					setNullFlags();
					if (AppConstant.isFragmentSupported) {
						DataHolder.createProgressDialog(getActivity());
					}
					Intent intent = new Intent();
					if (DataHolder.isOnline(getActivity()) || AppConstant.isFragmentSupported) {
						intent.setClass(getActivity(), CommitteesActivityTablet.class);
					} else {
						intent.setClass(getActivity(), CommitteesDetailsTasksActivity.class);
					}
					intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					startActivity(intent);
					getActivity().overridePendingTransition(0, 0);
				}
			}
		});
		tab5 = (ImageView) layout.findViewById(R.id.tab5);
		tab5.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				if (!(getActivity() instanceof VisitorsOffersActivity)) {
					setNullFlags();
					if (AppConstant.isFragmentSupported) {
						DataHolder.createProgressDialog(getActivity());
						Intent intent = new Intent();
						intent.setClass(getActivity(), VisitorsOffersActivityTablet.class);
						intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
						startActivity(intent);
						getActivity().overridePendingTransition(0, 0);
					} else {
						Intent intent = new Intent();
						intent.setClass(getActivity(), VisitorsOffersActivity.class);
						intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
						startActivity(intent);
						getActivity().overridePendingTransition(0, 0);
					}
				}
			}
		});

		Activity activity = getActivity();
		if ((activity instanceof NewsStartActivity) || (activity instanceof NewsActivity) || (activity instanceof NewsActivityTablet) || (activity instanceof NewsListActivity)
				|| (activity instanceof NewsDetailsActivity)) {
			ImageView tab = (ImageView) layout.findViewById(R.id.tab1);
			if (isLandscapeOriented())
				tab.setImageResource(R.drawable.tab_item_land_active_1);
			else
				tab.setImageResource(R.drawable.tab_item_active_1);

		}

		else if ((activity instanceof PlenumTvActivity) || (activity instanceof PlenumNewsActivity) || (activity instanceof PlenumNewsDetailsActivity)
				|| (activity instanceof PlenumSpeakersActivity) || (activity instanceof PlenumSpeakersActivityTablet) || (activity instanceof PlenumDebatesActivity)
				|| (activity instanceof PlenumVideoActivity) || (activity instanceof PlenumAudioActivity) || (activity instanceof PlenumSitzungenActivity)
				|| (activity instanceof PlenumAufgabeActivity) || (activity instanceof PlenumObjectViewActivity) || (activity instanceof DebateNewsActivity)
				|| (activity instanceof DebateNewsDetailsActivity) || (activity instanceof PlenumDebatesNewsDetailsActivity)) {
			ImageView tab = (ImageView) layout.findViewById(R.id.tab2);
			if (isLandscapeOriented())
				tab.setImageResource(R.drawable.tab_item_land_active_2);
			else
				tab.setImageResource(R.drawable.tab_item_active_2);
		}

		else if ((activity instanceof CommitteesActivity) ||( activity instanceof CommitteesActivityTablet)|| (activity instanceof CommitteesDetailsNewsDetailsFragmentActivity)
				|| ((activity instanceof CommitteesDetailsMembersActivity) && !(activity instanceof MembersCommitteesDetailsMembersActivity))
				|| ((activity instanceof CommitteesDetailsNewsActivity) && !(activity instanceof MembersCommitteesDetailsNewsActivity))
				|| (activity instanceof CommitteesDetailsNewsDetailsActivity)
				|| ((activity instanceof CommitteesDetailsTasksActivity) && !(activity instanceof MembersCommitteesDetailsTasksActivity))
				|| (activity instanceof CommitteesDetailsMembersByFractionActivity) || (activity instanceof CommitteesDetailsMemberBiographyActivity)
				|| (activity instanceof CommitteesDetailsMemberCommitteesActivity) || (activity instanceof CommitteesDetailsMemberDetailsActivity)
				|| (activity instanceof CommitteesDetailsMemberContactActivity) || (activity instanceof CommitteesDetailsMemberInfoActivity)) {
			ImageView tab = (ImageView) layout.findViewById(R.id.tab4);
			if (isLandscapeOriented())
				tab.setImageResource(R.drawable.tab_item_land_active_4);
			else
				tab.setImageResource(R.drawable.tab_item_active_4);
		}

		else if ((activity instanceof MembersListActivity) || (activity instanceof MembersDetailsActivity) || (activity instanceof MembersFractionActivity)
				|| (activity instanceof MembersDetailsBiographyActivity) || (activity instanceof MembersCityActivity) || (activity instanceof MembersElectionActivity)
				|| (activity instanceof MembersSubpageMembersActivity) || (activity instanceof MembersDetailsCommitteesActivity)
				|| (activity instanceof MembersDetailsInfoActivity) || (activity instanceof MembersDetailsContactActivity)
				|| (activity instanceof MembersCommitteesDetailsNewsActivity) || (activity instanceof MembersCommitteesDetailsMembersActivity)
				|| (activity instanceof MembersCommitteesDetailsTasksActivity) || (activity instanceof MembersSubListDetailsActivity)) {
			ImageView tab = (ImageView) layout.findViewById(R.id.tab3);
			if (isLandscapeOriented())
				tab.setImageResource(R.drawable.tab_item_land_active_3);
			else
				tab.setImageResource(R.drawable.tab_item_active_3);
		}

		else if ((activity instanceof VisitorsOffersActivity) || (activity instanceof VisitorsOffersActivityTablet) || (activity instanceof VisitorsOffersListActivity)
				|| (activity instanceof VisitorsLocationsListActivity) || (activity instanceof VisitorsOffersDetailsActivity) || (activity instanceof VisitorsDetailsActivity)
				|| (activity instanceof VisitorsLocationsActivity) || (activity instanceof VisitorsLocationsActivityTablet)
				|| (activity instanceof VisitorsLocationsDetailsActivity) || (activity instanceof VisitorsNewsActivity) || (activity instanceof VisitorsNewsActivityTablet) || (activity instanceof VisitorsNewsDetailsActivity)
				|| (activity instanceof VisitorsContactActivity)) {
			ImageView tab = (ImageView) layout.findViewById(R.id.tab5);
			if (isLandscapeOriented())
				tab.setImageResource(R.drawable.tab_item_land_active_5);
			else
				tab.setImageResource(R.drawable.tab_item_active_5);
		}

		return layout;
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
		}

		return false;
	}

	/**
	 * Checks if the orientation of the device is Landscape
	 * 
	 * @return true if it is
	 */
	private boolean isLandscapeOriented() {
		return getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE;
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

		tab1 = null;

		tab2 = null;

		tab3 = null;

		tab4 = null;

		tab5 = null;
	}

}