package de.bundestag.android.fragments;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.ListFragment;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.ScrollView;
import android.widget.TextView;
import de.bundestag.android.R;
import de.bundestag.android.constant.DataHolder;
import de.bundestag.android.helpers.ImageHelper;
import de.bundestag.android.parser.PlenumXMLParser;
import de.bundestag.android.parser.objects.CommitteesObject;
import de.bundestag.android.parser.objects.MembersDetailsObject;
import de.bundestag.android.parser.objects.PlenumStreamObject;
import de.bundestag.android.sections.committees.ComDetailsMemByFractionListAdapter;
import de.bundestag.android.sections.committees.CommitteesActivity;
import de.bundestag.android.sections.committees.CommitteesActivityTablet;
import de.bundestag.android.sections.committees.CommitteesDetailsFragment;
import de.bundestag.android.sections.committees.CommitteesDetailsMembersByFractionActivity;
import de.bundestag.android.sections.committees.CommitteesDetailsMembersViewHelper;
import de.bundestag.android.sections.committees.CommitteesDetailsNewsActivity;
import de.bundestag.android.sections.committees.CommitteesDetailsNewsDetailsActivity;
import de.bundestag.android.sections.committees.CommitteesDetailsNewsDetailsFragment;
import de.bundestag.android.sections.committees.CommitteesDetailsNewsDetailsFragmentActivity;
import de.bundestag.android.sections.committees.CommitteesDetailsNewsListAdapter;
import de.bundestag.android.sections.committees.CommitteesDetailsTasksActivity;
import de.bundestag.android.sections.committees.CommitteesListAdapter;
import de.bundestag.android.sections.members.MembersDetailsFragment;
import de.bundestag.android.sections.members.MembersListActivity;
import de.bundestag.android.sections.members.MembersListAdapter;
import de.bundestag.android.sections.members.MembersSubpageMembersActivity;
import de.bundestag.android.sections.members.MembersSubpageViewHelper;
import de.bundestag.android.sections.news.NewsActivityTablet;
import de.bundestag.android.sections.news.NewsDetailsFragment;
import de.bundestag.android.sections.news.NewsListAdapter;
import de.bundestag.android.sections.news.NewsSubListFragment;
import de.bundestag.android.sections.plenum.DebateNewsActivity;
import de.bundestag.android.sections.plenum.PlenumDebatesActivity;
import de.bundestag.android.sections.plenum.PlenumDebatesListAdapter;
import de.bundestag.android.sections.plenum.PlenumDebatesViewHelper;
import de.bundestag.android.sections.plenum.PlenumSittingDetailFragment;
import de.bundestag.android.sections.plenum.PlenumSpeakersActivityTablet;
import de.bundestag.android.sections.plenum.PlenumSpeakersListAdapter;
import de.bundestag.android.sections.plenum.PlenumSpeakersViewHelper;
import de.bundestag.android.sections.plenum.PlenumTvActivity;
import de.bundestag.android.sections.plenum.PlenumTvListAdapter;
import de.bundestag.android.sections.plenum.PlenumTvViewHelper;
import de.bundestag.android.sections.visitors.VisitorsContactActivity;
import de.bundestag.android.sections.visitors.VisitorsListAdapter;
import de.bundestag.android.sections.visitors.VisitorsLocationsActivityTablet;
import de.bundestag.android.sections.visitors.VisitorsLocationsDetailsFragment;
import de.bundestag.android.sections.visitors.VisitorsNewsActivityTablet;
import de.bundestag.android.sections.visitors.VisitorsNewsDetailsFragment;
import de.bundestag.android.sections.visitors.VisitorsOffersActivityTablet;
import de.bundestag.android.sections.visitors.VisitorsOffersDetailsFragment;
import de.bundestag.android.sections.visitors.VisitorsOffersListFragment;
import de.bundestag.android.storage.CommitteesDatabaseAdapter;
import de.bundestag.android.storage.MembersDatabaseAdapter;
import de.bundestag.android.storage.NewsDatabaseAdapter;
import de.bundestag.android.storage.VisitorsDatabaseAdapter;
import de.bundestag.android.synchronization.BaseSynchronizeTask;
import de.bundestag.android.synchronization.MembersSynchronization;
import de.bundestag.android.synchronization.NewsSynchronization;
import de.bundestag.android.synchronization.SynchronizeCheckCommitteeNewsTask;
import de.bundestag.android.synchronization.SynchronizeCommitteeNewsTask;

/**
 * General list fragment class that holds the list. This is a fragment so it can
 * be used together with another fragment inside the same activity.
 * 
 * See http://developer.android.com/guide/topics/fundamentals/fragments.html
 */
public class GeneralListFragmentTab extends ListFragment {

	int mCurCheckPosition = 0;
	NewsDetailsFragment newsDetailsFragment = null;
	VisitorsOffersDetailsFragment visitorsOffersDetailsFragment = null;
	CommitteesDetailsFragment committeesDetailsFragment = null;
	CommitteesDetailsNewsDetailsFragment committeesDetailsNewsDetailsFragment = null;
	VisitorsLocationsDetailsFragment visitorsLocationsDetailsFragment = null;
	VisitorsNewsDetailsFragment visitorsNewsDetailsFragment = null;
	VisitorsOffersListFragment visitorsOffersListFragment = null;
	MembersDetailsFragment membersDetailsFragment = null;
	CommitteesDetailsNewsDetailsFragment committeeDetailsFragmentNews = null;
	static boolean isFirstTime = true;
	Timer speakerTimer = null;
	Timer debateTimer = null;
	private int currentCheckedId = 0;
	private int noOfDisableTabs = 0;
	int currentCheckedIndex = -1;

	private ArrayList<HashMap<String, Object>> speakers;
	private ArrayList<HashMap<String, Object>> debates;

	private ProgressDialog dialog;

	private PlenumTvListAdapter plenumTvListAdapter = null;

	List<String> output;

	Handler newsHandler = new Handler(new Callback() {

		@Override
		public boolean handleMessage(Message msg) {

			if (msg.what == 1) {
				final HorizontalScrollView s = (HorizontalScrollView) msg.obj;
				s.postDelayed(new Runnable() {

					@Override
					public void run() {
						s.fullScroll(ScrollView.FOCUS_RIGHT);
					}
				}, 2000);

			}
			return false;
		}
	});
	private Timer waitTimer;
	private TextView mainTitle;
	private TextView dateTitle;
	private RadioGroup tvWeek;
	private Cursor offersCursor;
	private Cursor visitorCursor;
	private Cursor subNewsCursor;

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		LinearLayout childLayot = (LinearLayout) getActivity().findViewById(R.id.lnrScrollable);
		LinearLayout detailLayout = (LinearLayout) getActivity().findViewById(R.id.detailLayout);

		if (childLayot != null && detailLayout != null) {
			DataHolder.listFragmentWidth = childLayot.getWidth();
			DataHolder.detailFragmentWidth = detailLayout.getWidth();
		}
		DataHolder.isOriantationChange = true;
		if (newConfig.orientation == android.content.res.Configuration.ORIENTATION_LANDSCAPE) {
			DataHolder.isLandscape = true;
		} else {
			DataHolder.isLandscape = false;
		}
		if (waitTimer != null) {
			waitTimer.cancel();
		}
	}

	@SuppressWarnings("static-access")
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		resetVariables();

		if (android.content.res.Configuration.ORIENTATION_LANDSCAPE == getActivity().getResources().getSystem().getConfiguration().orientation) {
			DataHolder.isLandscape = true;
		} else {
			DataHolder.isLandscape = false;
		}
		getListView().setFastScrollEnabled(true);
		ColorDrawable line_color = new ColorDrawable(0xFF8fa3a7);
		getListView().setDivider(line_color);
		getListView().setDividerHeight(1);
		getListView().setCacheColorHint(0x00000000);
		getListView().setScrollBarStyle(ScrollView.SCROLLBARS_INSIDE_OVERLAY);
		getListView().setSmoothScrollbarEnabled(true);
		getListView().setHorizontalFadingEdgeEnabled(false);
		getListView().setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.FILL_PARENT));

		if (!((getActivity() instanceof PlenumSpeakersActivityTablet) || (getActivity() instanceof PlenumDebatesActivity))) {
			getListView().setDivider(null);
			getListView().setDividerHeight(0);
		}
		// getListView().setFastScrollEnabled(false);
		getListView().setChoiceMode(ListView.CHOICE_MODE_SINGLE);

	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		Activity activity = getActivity();
		System.out.println("Selected index : " + DataHolder.rowDBSelectedIndex);
		if (!(activity instanceof VisitorsContactActivity)) {
			DataHolder.mLockScreenRotation(getActivity());
		}
		if (activity instanceof NewsActivityTablet) {
			handleNewsList();
		} else if (activity instanceof VisitorsOffersActivityTablet) {
			handleVisitorsOffersList();
		} else if (activity instanceof VisitorsLocationsActivityTablet) {
			handleVisitorsLocationsList();
		} else if (activity instanceof VisitorsNewsActivityTablet) {
			handleVisitorsNewsList();
		} else if (activity instanceof MembersListActivity) {
			handleMembersList();
		} else if (activity instanceof MembersSubpageMembersActivity) {
			MembersSubpageMembersActivity membersSubpageMembersActivity = (MembersSubpageMembersActivity) activity;
			handleMembersSubpage(membersSubpageMembersActivity.subPageId, membersSubpageMembersActivity.index);
		} else if (activity instanceof CommitteesActivityTablet) {
			handleCommitteesList();
		} else if (activity instanceof MembersSubpageMembersActivity) {
			MembersSubpageMembersActivity membersSubpageMembersActivity = (MembersSubpageMembersActivity) activity;
			handleMembersSubpage(membersSubpageMembersActivity.subPageId, membersSubpageMembersActivity.index);
		} else if (activity instanceof PlenumSpeakersActivityTablet) {

			if (DataHolder.isOnline(getActivity())) {

				DataHolder.generalListFragmentTab = this;
				if (DataHolder.currentPlanumTab.equals("") || DataHolder.currentPlanumTab.equals("speaker")) {
					handlePlenumSpeakersList();
					DataHolder.currentPlanumTab = "speaker";
					startTimer();
				} else {
					handlePlenumDebatesList();
					DataHolder.currentPlanumTab = "debate";
				}
			} else {
				DataHolder.alertboxOk(getActivity());
			}

		} else if (activity instanceof DebateNewsActivity) {
			handleDebateNewsList();
		} else if (activity instanceof CommitteesDetailsNewsActivity) {
			CommitteesDetailsNewsActivity committeesDetailsNewsActivity = (CommitteesDetailsNewsActivity) activity;
			handleCommitteesDetailNewsList(committeesDetailsNewsActivity.committeesName);
		} else if (activity instanceof VisitorsContactActivity) {
			handleVisitorsContactList();
		} else if (activity instanceof PlenumTvActivity) {
			if (DataHolder.isOnline(getActivity()))
				handleTvList();
			else {
				DataHolder.alertboxOk(getActivity());
			}
		} else if (activity instanceof CommitteesDetailsMembersByFractionActivity) {
			CommitteesDetailsMembersByFractionActivity committeesDetailsMembersByFractionActivity = (CommitteesDetailsMembersByFractionActivity) getActivity();
			handleCommitteesDetailMembersByFractionList(committeesDetailsMembersByFractionActivity.committeesId, committeesDetailsMembersByFractionActivity.committeesIdString,
					committeesDetailsMembersByFractionActivity.fractionName);
		} else if (activity instanceof CommitteesDetailsTasksActivity) {
			CommitteesDetailsTasksActivity committeesDetailsTasksActivity = (CommitteesDetailsTasksActivity) getActivity();
			handleCommitteesDetailMembersByTaskList(committeesDetailsTasksActivity.committeesId);
		} else if (activity instanceof CommitteesDetailsNewsDetailsFragmentActivity) {
			handleCommitteesDetailsNewsDetailsFragmentActivity();
		}
	}

	private void handleCommitteesDetailsNewsDetailsFragmentActivity() {
		CommitteesDatabaseAdapter committeesDatabaseAdapter = new CommitteesDatabaseAdapter(getActivity());
		committeesDatabaseAdapter.open();
		Cursor articlesCursor = committeesDatabaseAdapter.fetchCommitteesNewsByString(DataHolder.committesStringId); // pass
																														// 2
		// id

		getActivity().startManagingCursor(articlesCursor);
		setListAdapter(new CommitteesDetailsNewsListAdapter(getActivity(), articlesCursor, "Test"));
		committeesDetailsNewsDetailsFragment = (CommitteesDetailsNewsDetailsFragment) getFragmentManager().findFragmentById(R.id.detailsFragment);
		DataHolder.RowDBIds = new ArrayList<Integer>();
		articlesCursor.moveToFirst();
		if (articlesCursor.getCount() > 0) {
			for (int i = 0; i < articlesCursor.getCount(); i++) {
				DataHolder.RowDBIds.add(articlesCursor.getInt(articlesCursor.getColumnIndex(CommitteesDatabaseAdapter.KEY_ROWID)));
				articlesCursor.moveToNext();
			}
			if (DataHolder.isFromMember) {
				DataHolder.isFromMember = false;
				committeesDetailsNewsDetailsFragment.setRowId(DataHolder.RowDBIds.get(0));
				DataHolder.lastRowDBId = DataHolder.RowDBIds.get(0);
			} else {
				if (DataHolder._id == -1 || DataHolder.isOriantationChange) {
					DataHolder.isOriantationChange = false;
					if (DataHolder.rowDBSelectedIndex == 0) {
						committeesDetailsNewsDetailsFragment.setRowId(DataHolder.RowDBIds.get(0));
						DataHolder.lastRowDBId = DataHolder.RowDBIds.get(0);
					} else {
						committeesDetailsNewsDetailsFragment.setRowId(DataHolder.lastRowDBId);
					}
				} else {
					committeesDetailsNewsDetailsFragment.setRowId(DataHolder._id);
				}
			}

			TextView mainTitle = (TextView) getActivity().findViewById(R.id.mainTitle);
			mainTitle.setText(DataHolder.committeeName);
			mainTitle.setLayoutParams(new LinearLayout.LayoutParams(DataHolder.calculatedScreenResolution, LinearLayout.LayoutParams.WRAP_CONTENT));
			committeesDetailsNewsDetailsFragment.createNewsDetailsObject();
			LinearLayout childLayot = (LinearLayout) getActivity().findViewById(R.id.lnrScrollable);

			childLayot.setLayoutParams(new LinearLayout.LayoutParams(DataHolder.calculatedScreenResolution, LinearLayout.LayoutParams.FILL_PARENT));
			childLayot.setBackgroundResource(R.drawable.bg);
			articlesCursor.moveToFirst();
		} else {
			Intent intent = new Intent();
			DataHolder.isPrevCommittee = true;
			intent.putExtra(CommitteesDetailsNewsActivity.KEY_COMMITTEE_ID, DataHolder.committesStringId);
			intent.setClass(getActivity(), CommitteesDetailsTasksActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
			getActivity().overridePendingTransition(0, 0);
		}
	}

	// Tv

	public void handleTvList() {
		output = new ArrayList<String>();
		System.err.println(getCalendar());
		LinearLayout childLayot = (LinearLayout) getActivity().findViewById(R.id.lnrScrollable);
		childLayot.setLayoutParams(new LinearLayout.LayoutParams(DataHolder.calculatedScreenResolution, LinearLayout.LayoutParams.FILL_PARENT));
		createTvCalenderLayout();
	}

	private void createTvCalenderLayout() {

		mainTitle = (TextView) getActivity().findViewById(R.id.mainTitle);
		dateTitle = (TextView) getActivity().findViewById(R.id.dateTitle);
		tvWeek = (RadioGroup) getActivity().findViewById(R.id.tvWeek);

		tvWeek.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				currentCheckedId = checkedId;
				switch (checkedId) {
				case R.id.mon:
					updateList(0, noOfDisableTabs);
					break;
				case R.id.tue:
					updateList(1, noOfDisableTabs);
					break;
				case R.id.wed:
					updateList(2, noOfDisableTabs);
					break;
				case R.id.thu:
					updateList(3, noOfDisableTabs);
					break;
				case R.id.fri:
					updateList(4, noOfDisableTabs);
					break;
				case R.id.sat:
					updateList(5, noOfDisableTabs);
					break;
				case R.id.sun:
					updateList(6, noOfDisableTabs);
					break;
				default:
					break;
				}
				setCurrentDaySelection(tvWeek, dateTitle);
			}
		});

		new AsyncTask<PlenumXMLParser, PlenumXMLParser, PlenumStreamObject>() {

			@Override
			protected void onPreExecute() {
				super.onPreExecute();
				if (dialog == null)
					dialog = ProgressDialog.show(getActivity(), "", "Daten werden geladen");
				else if (!dialog.isShowing())
					dialog.show();
			}

			@Override
			protected PlenumStreamObject doInBackground(PlenumXMLParser... plenumParser) {

				return plenumParser[0].parseStream();

			}

			@Override
			protected void onPostExecute(PlenumStreamObject plenumStreamData) {
				super.onPostExecute(plenumStreamData);
				if ((dialog != null) && (dialog.isShowing()))
					dialog.dismiss();
				try {
					Date d = new Date();
					String strDate = output.get(0).split("-")[2] + "." + output.get(0).split("-")[1] + ". bis " + (output.get(output.size() - 1).split("-")[2]) + "."
							+ (output.get(output.size() - 1).split("-")[1]) + "." + output.get(output.size() - 1).split("-")[0];
					mainTitle.setText(plenumStreamData.getVideoStreamTitle() + "\n" + "Programm der Woche vom\n" + strDate);
					dateTitle.setText(getDayName(d.getDay()) + ", " + (d.getDate()) + "." + (d.getMonth() + 1) + "." + (d.getYear() + 1900));
					setCurrentDaySelection(tvWeek, dateTitle);
				} catch (Exception e) {
				}
			}

		}.execute(new PlenumXMLParser());
	}

	public void updateList(final int index, final int noOfDisableTabs) {
		currentCheckedIndex = index;

		new AsyncTask<Void, Void, ArrayList<HashMap<String, Object>>>() {

			@Override
			protected void onPreExecute() {
				super.onPreExecute();
				if (dialog == null)
					dialog = ProgressDialog.show(getActivity(), "", "Daten werden geladen");
				else if (!dialog.isShowing())
					dialog.show();
			}

			@Override
			protected ArrayList<HashMap<String, Object>> doInBackground(Void... params) {
				ArrayList<HashMap<String, Object>> items = PlenumTvViewHelper.createTv(output.get(index - noOfDisableTabs));
				items.remove(0);
				return items;
			}

			@Override
			protected void onPostExecute(ArrayList<HashMap<String, Object>> items) {
				super.onPostExecute(items);
				if ((dialog != null) && (dialog.isShowing()))
					dialog.dismiss();
				try {
					PlenumTvListAdapter tmp = (PlenumTvListAdapter) getListView().getAdapter();
					tmp.setListItemData(items);

					((BaseAdapter) getListAdapter()).notifyDataSetChanged();
				} catch (Exception e) {
					new AsyncTask<Void, Void, Void>() {
						ArrayList<HashMap<String, Object>> items;

						@Override
						protected void onPreExecute() {
							super.onPreExecute();
							if (dialog == null)
								dialog = ProgressDialog.show(getActivity(), "", "Daten werden geladen");
							else if (!dialog.isShowing())
								dialog.show();
						}

						@Override
						protected Void doInBackground(Void... params) {
							items = PlenumTvViewHelper.createTv(output.get(0));
							items.remove(0);
							return null;
						}

						@Override
						protected void onPostExecute(Void result) {
							super.onPostExecute(result);
							if ((dialog != null) && (dialog.isShowing()))
								dialog.dismiss();
							plenumTvListAdapter = new PlenumTvListAdapter(getActivity(), items, getListView());
							setListAdapter(plenumTvListAdapter);
						}

					}.execute();
				}
			}

		}.execute();

	}

	private void setCurrentDaySelection(RadioGroup rg, TextView tv) {
		int id = 0;
		int currentSelectedIndex = 0;
		switch (new Date().getDay()) {

		case 0:
			id = R.id.sun;
			break;
		case 1:
			id = R.id.mon;
			break;
		case 2:
			id = R.id.tue;
			break;
		case 3:
			id = R.id.wed;
			break;
		case 4:
			id = R.id.thu;
			break;
		case 5:
			id = R.id.fri;
			break;
		case 6:
			id = R.id.sat;
			break;

		default:
			break;
		}

		for (int i = 0; i < rg.getChildCount(); i++) {
			RadioButton r = (RadioButton) rg.getChildAt(i);
			if (r.getId() == id) {
				currentSelectedIndex = i;
				break;
			}
		}
		noOfDisableTabs = currentSelectedIndex;
		for (int j = 0; j < currentSelectedIndex; j++) {
			RadioButton disableRadio = (RadioButton) rg.getChildAt(j);
			disableRadio.setEnabled(false);
		}
		if (currentCheckedId != 0) {
			RadioButton rb1 = (RadioButton) rg.findViewById(currentCheckedId);
			rb1.setChecked(true);
		} else {
			RadioButton rb = (RadioButton) rg.findViewById(id);
			rb.setChecked(true);
		}

		if (currentCheckedIndex != -1) {
			Calendar c = Calendar.getInstance();
			c.set(Integer.parseInt(output.get(currentCheckedIndex - noOfDisableTabs).split("-")[0]),
					(Integer.parseInt(output.get(currentCheckedIndex - noOfDisableTabs).split("-")[1]) - 1),
					Integer.parseInt(output.get(currentCheckedIndex - noOfDisableTabs).split("-")[2]));
			Date d = c.getTime();
			tv.setText(getDayName(d.getDay()) + ", " + (d.getDate()) + "." + (d.getMonth() + 1) + "." + (d.getYear() + 1900));
		}
	}

	private String getDayName(int day) {

		String dayName = "";
		switch (day) {
		case 0:
			dayName = "Sonntag";
			break;
		case 1:
			dayName = "Montag";
			break;
		case 2:
			dayName = "Dienstag";
			break;
		case 3:
			dayName = "Mittwoch";
			break;
		case 4:
			dayName = "Donnerstag";
			break;
		case 5:
			dayName = "Freitag";
			break;
		case 6:
			dayName = "Samstag";
			break;

		default:
			break;
		}
		return dayName;
	}

	// News Async

	// News
	public void handleNewsList() {

		final NewsDatabaseAdapter newsDatabaseAdapter = new NewsDatabaseAdapter(getActivity());
		try {
			newsDatabaseAdapter.close();
		} catch (Exception e) {
		}
		newsDatabaseAdapter.open();
		Cursor newsCursor = newsDatabaseAdapter.fetchAllNews();
		getActivity().startManagingCursor(newsCursor);
		setListAdapter(new NewsListAdapter(getActivity(), newsCursor));

		/* for maitaining index for using in flig */
		DataHolder.RowDBIds = new ArrayList<Integer>();
		DataHolder.tmpRowDBIds = new ArrayList<Integer>();
		newsCursor.moveToFirst();
		for (int i = 0; i < newsCursor.getCount(); i++) {
			Integer newsType = newsCursor.getInt(newsCursor.getColumnIndex(NewsDatabaseAdapter.KEY_TYPE));
			if ((newsType != null) && (newsType.intValue() != NewsSynchronization.NEWS_TYPE_LIST)) {
				DataHolder.RowDBIds.add(newsCursor.getInt(newsCursor.getColumnIndex(NewsDatabaseAdapter.KEY_ROWID)));
				DataHolder.tmpRowDBIds.add(newsCursor.getInt(newsCursor.getColumnIndex(NewsDatabaseAdapter.KEY_ROWID)));
			}
			newsCursor.moveToNext();
		}

		// check for fragment of sbulist, if it is there then remove at startup.

		newsDetailsFragment = (NewsDetailsFragment) getFragmentManager().findFragmentById(R.id.detailsFragment);
		newsDetailsFragment.setIsMasterList(true);
		newsCursor.moveToFirst();
		Integer newsType = newsCursor.getInt(newsCursor.getColumnIndex(NewsDatabaseAdapter.KEY_TYPE));

		// check whether list contains sublist

		if ((newsType != null) && (newsType.intValue() == NewsSynchronization.NEWS_TYPE_LIST)) {
			Integer newsId = newsCursor.getInt(newsCursor.getColumnIndex(NewsDatabaseAdapter.KEY_ROWID));
			newsCursor = newsDatabaseAdapter.getListIdFromNewsId(newsId);
			final Integer listId = newsCursor.getInt(newsCursor.getColumnIndex(NewsDatabaseAdapter.KEY_NEWSLIST_ROWID));
			final HorizontalScrollView s = new HorizontalScrollView(getActivity());
			s.setId(108);

			s.postDelayed(new Runnable() {

				@Override
				public void run() {
					if (DataHolder.listFragmentWidth == -1) {
						LinearLayout childLayot = (LinearLayout) getActivity().findViewById(R.id.lnrScrollable);
						LinearLayout detailLayout = (LinearLayout) getActivity().findViewById(R.id.detailLayout);
						DataHolder.gallaryFragmentWidth = DataHolder.listFragmentWidth = childLayot.getWidth();
						DataHolder.detailFragmentWidth = detailLayout.getWidth();
					}
					FragmentManager fragmentManager = getFragmentManager();
					FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction(); // add
					// create sub list
					NewsSubListFragment myFragment = new NewsSubListFragment();

					try {
						fragmentTransaction.add(R.id.lnrScrollable, myFragment, "subList");
					} catch (Exception e) {

					}
					fragmentTransaction.commit();
					myFragment.setId(listId);
					LinearLayout childLayot = (LinearLayout) getActivity().findViewById(R.id.lnrScrollable);
					LinearLayout masterLayout = (LinearLayout) getActivity().findViewById(R.id.master);
					Cursor subNewsCursor = newsDatabaseAdapter.fetchSubListNews(listId);
					getActivity().startManagingCursor(subNewsCursor);
					myFragment.setListAdapter(new NewsListAdapter(getActivity(), subNewsCursor));
					masterLayout.removeView(childLayot);
					s.setLayoutParams(new LinearLayout.LayoutParams(DataHolder.calculatedScreenResolution, LinearLayout.LayoutParams.FILL_PARENT));
					childLayot.setLayoutParams(new LinearLayout.LayoutParams(DataHolder.calculatedScreenResolution, LinearLayout.LayoutParams.FILL_PARENT));
					s.addView(childLayot);
					s.setFadingEdgeLength(0);
					// add new fragment at runtime.
					if (DataHolder.isLandscape) {
						masterLayout.addView(s, 1);
					} else {
						masterLayout.addView(s, 0);
					}

					// code for updating news detail fragment on load....
					if (DataHolder.rowDBSelectedIndex == 0) {
						newsDetailsFragment.setNewsId(DataHolder.RowDBIds.get(0));
						DataHolder.lastRowDBId = DataHolder.RowDBIds.get(0);
					} else {
						newsDetailsFragment.setNewsId(DataHolder.lastRowDBId);
					}
					newsDetailsFragment.setIsMasterList(false);
					// DataHolder.rowDBSelectedIndex = 0;
					newsDetailsFragment.createNewsDetailsObject();
					DataHolder._oldPosition = 0;
					Message m = new Message();
					m.obj = s;
					m.what = 1;
					newsHandler.dispatchMessage(m);

				}
			}, 2000);

			/* for maitaining index for using in flig */
			DataHolder.RowDBIds = new ArrayList<Integer>();
			newsCursor = newsDatabaseAdapter.fetchSubListNews(listId);
			newsCursor.moveToFirst();

			// use for fling
			for (int i = 0; i < newsCursor.getCount(); i++) {
				newsType = newsCursor.getInt(newsCursor.getColumnIndex(NewsDatabaseAdapter.KEY_TYPE));
				if ((newsType != null) && (newsType.intValue() != NewsSynchronization.NEWS_TYPE_LIST)) {
					DataHolder.RowDBIds.add(newsCursor.getInt(newsCursor.getColumnIndex(NewsDatabaseAdapter.KEY_ROWID)));
				}
				newsCursor.moveToNext();
			}
			getListView().setItemChecked(0, true);

		} else {
			if (DataHolder._id == -1 || DataHolder.isOriantationChange) {
				DataHolder.isOriantationChange = false;
				if (DataHolder.rowDBSelectedIndex == 0) {
					newsDetailsFragment.setNewsId(DataHolder.RowDBIds.get(0));
					DataHolder.lastRowDBId = DataHolder.RowDBIds.get(0);
				} else {
					newsDetailsFragment.setNewsId(DataHolder.lastRowDBId);
				}
			} else {
				newsDetailsFragment.setNewsId(DataHolder._id);
			}
			newsDetailsFragment.createNewsDetailsObject();

		}
		LinearLayout childLayot = (LinearLayout) getActivity().findViewById(R.id.lnrScrollable);
		childLayot.setLayoutParams(new LinearLayout.LayoutParams(DataHolder.calculatedScreenResolution, LinearLayout.LayoutParams.FILL_PARENT));
		childLayot.setBackgroundResource(R.drawable.bg);
		getActivity().stopManagingCursor(newsCursor);
	}

	// Committees
	private void handleCommitteesList() {
		CommitteesDatabaseAdapter committeesDatabaseAdapter = new CommitteesDatabaseAdapter(getActivity());
		try {
			committeesDatabaseAdapter.open();
		} catch (Exception e) {
		}

		Cursor committeesCursor = committeesDatabaseAdapter.fetchAllCommittees();
		getActivity().startManagingCursor(committeesCursor);
		setListAdapter(new CommitteesListAdapter(getActivity(), committeesCursor));
		LinearLayout childLayot = (LinearLayout) getActivity().findViewById(R.id.lnrScrollable);
		childLayot.setLayoutParams(new LinearLayout.LayoutParams(DataHolder.calculatedScreenResolution, LinearLayout.LayoutParams.FILL_PARENT));
		childLayot.setBackgroundResource(R.drawable.bg);
		committeesCursor.moveToFirst();
		DataHolder.RowDBIds = new ArrayList<Integer>();
		for (int i = 0; i < committeesCursor.getCount(); i++) {
			DataHolder.RowDBIds.add(committeesCursor.getInt(committeesCursor.getColumnIndex(CommitteesDatabaseAdapter.KEY_ROWID)));
			committeesCursor.moveToNext();
		}
		committeesDetailsFragment = (CommitteesDetailsFragment) getFragmentManager().findFragmentById(R.id.detailsFragment);
		if (DataHolder.rowDBSelectedIndex == 0) {
			committeesDetailsFragment.setRowId(DataHolder.RowDBIds.get(0));
		} else {
			committeesDetailsFragment.setRowId(DataHolder.lastRowDBId);
		}
		committeesDetailsFragment.createCommitteesDetailsObject();
	}

	public void handlePlenumSpeakersList() {
		LinearLayout childLayot = (LinearLayout) getActivity().findViewById(R.id.lnrScrollable);
		childLayot.setLayoutParams(new LinearLayout.LayoutParams(DataHolder.calculatedScreenResolution, LinearLayout.LayoutParams.FILL_PARENT));
		speakers = PlenumSpeakersViewHelper.createSpeakers();
		if (speakers != null) {
			speakers.remove(0);
			setListAdapter(new PlenumSpeakersListAdapter(getActivity(), speakers));
			for (int i = 0; i < speakers.size(); i++) {
				String state = (String) speakers.get(i).get(PlenumSpeakersViewHelper.KEY_SPEAKER_STATE);
				if (state != null) {
					if (state.equals("live") || state.equals("läuft")) {
						getListView().setSelectionFromTop(i, 0);
					}
				}
			}
		}
	}

	public boolean updateSpeakerListData() {

		try {
			if (!PlenumSittingDetailFragment.isFullScreen) {

				if (getListView() != null) {
					speakers = PlenumSpeakersViewHelper.createSpeakers();
					if (speakers != null) {
						speakers.remove(0);
						getActivity().runOnUiThread(new Runnable() {

							@Override
							public void run() {
								if (!PlenumSittingDetailFragment.isFullScreen) {

									try {
										PlenumSpeakersListAdapter tmp = (PlenumSpeakersListAdapter) getListView().getAdapter();
										tmp.setItem(speakers);
										((BaseAdapter) getListAdapter()).notifyDataSetChanged();
										DataHolder.currentPlanumTab = "speaker";
										System.out.println("speaker Kiked " + new Date());
										for (int i = 0; i < speakers.size(); i++) {
											String state = (String) speakers.get(i).get(PlenumSpeakersViewHelper.KEY_SPEAKER_STATE);
											if (state != null) {
												if (state.equals("live") || state.equals("läuft")) {
													getListView().setSelectionFromTop(i, 0);
													((TextView) getActivity().findViewById(R.id.txtHeader)).setText("Redner\n"
															+ DataHolder.txtAgenda.getText().toString().split(":")[1].trim());
													break;
												}
											}
										}
									} catch (Exception e) {

									}
								}
							}
						});

					}
					return true;
				} else {
					return false;
				}
			}
		} catch (Exception e) {
		}

		return false;

	}

	public boolean updateDebateListData() {

		try {
			if (!PlenumSittingDetailFragment.isFullScreen) {

				if (getListView() != null) {
					debates = PlenumDebatesViewHelper.createDebates();

					if (debates != null) {
						debates.remove(0);
						getActivity().runOnUiThread(new Runnable() {

							@Override
							public void run() {
								if (!PlenumSittingDetailFragment.isFullScreen) {

									try {
										PlenumDebatesListAdapter tmp = (PlenumDebatesListAdapter) getListView().getAdapter();
										tmp.setItem(debates);
										((BaseAdapter) getListAdapter()).notifyDataSetChanged();
										DataHolder.currentPlanumTab = "debate";
										System.out.println("Debate Kiked " + new Date());
										for (int i = 0; i < debates.size(); i++) {
											String state = (String) debates.get(i).get(PlenumSpeakersViewHelper.KEY_SPEAKER_STATE);
											if (state != null) {
												if (state.equals("live") || state.equals("läuft")) {
													getListView().setSelectionFromTop(i, 0);
													Date currentDate = new Date();
													((TextView) getActivity().findViewById(R.id.txtHeader)).setText("Debatten am " + (currentDate.getDate()) + "."
															+ (currentDate.getMonth() + 1) + "." + (currentDate.getYear() + 1900));
													break;
												}
											}
										}
									} catch (Exception e) {
									}
								}
							}
						});
					}
					return true;
				} else {
					return false;
				}
			}
		} catch (Exception e) {
		}
		return false;

	}

	public void startTimer() {

		waitTimer = new Timer();

		waitTimer.scheduleAtFixedRate(new TimerTask() {
			@Override
			public void run() {

				if (!PlenumSittingDetailFragment.isFullScreen) {

					if (DataHolder.currentPlanumTab.equals("debate")) {
						if (!updateDebateListData())
							waitTimer.cancel();
					} else if (DataHolder.currentPlanumTab.equals("speaker")) {
						if (!updateSpeakerListData())
							waitTimer.cancel();
					} else {
						waitTimer.cancel();
					}
				}
			}

		}, 10000, 10000);

	}

	public void stopTimer() {
		waitTimer.cancel();
	}

	// planum debate
	public void handlePlenumDebatesList() {
		LinearLayout childLayot = (LinearLayout) getActivity().findViewById(R.id.lnrScrollable);
		childLayot.setLayoutParams(new LinearLayout.LayoutParams(DataHolder.calculatedScreenResolution, LinearLayout.LayoutParams.FILL_PARENT));

		ArrayList<HashMap<String, Object>> debates = PlenumDebatesViewHelper.createDebates();
		PlenumDebatesActivity.debates = debates;
		debates.remove(0);
		if (debates != null) {
			setListAdapter(new PlenumDebatesListAdapter(getActivity(), debates));
			for (int i = 0; i < debates.size(); i++) {
				String state = (String) debates.get(i).get(PlenumSpeakersViewHelper.KEY_SPEAKER_STATE);
				if (state != null) {
					if (state.equals("live") || state.equals("läuft")) {
						getListView().setSelectionFromTop(i, 0);
					}
				}
			}
		}

	}

	private void handleDebateNewsList() {
		LinearLayout childLayot = (LinearLayout) getActivity().findViewById(R.id.lnrScrollable);
		childLayot.setLayoutParams(new LinearLayout.LayoutParams(DataHolder.calculatedScreenResolution, LinearLayout.LayoutParams.FILL_PARENT));
		childLayot.setBackgroundResource(R.drawable.bg);
		TextView mainTitle = (TextView) getActivity().findViewById(R.id.mainTitle);
		mainTitle.setLayoutParams(new LinearLayout.LayoutParams(DataHolder.calculatedScreenResolution, LinearLayout.LayoutParams.WRAP_CONTENT));
		NewsDatabaseAdapter newsDatabaseAdapter = new NewsDatabaseAdapter(getActivity());
		newsDatabaseAdapter.open();

		Cursor newsCursor = newsDatabaseAdapter.fetchAllDebateNews();
		getActivity().startManagingCursor(newsCursor);

		setListAdapter(new NewsListAdapter(getActivity(), newsCursor));
		DataHolder.RowDBIds = new ArrayList<Integer>();

		newsCursor.moveToFirst();

		for (int i = 0; i < newsCursor.getCount(); i++) {

			DataHolder.RowDBIds.add(newsCursor.getInt(newsCursor.getColumnIndex(VisitorsDatabaseAdapter.KEY_ROWID)));
			newsCursor.moveToNext();
		}
		// code for updating visitors detail fragment on load....
		visitorsNewsDetailsFragment = (VisitorsNewsDetailsFragment) getFragmentManager().findFragmentById(R.id.detailsFragment);
		if (DataHolder.rowDBSelectedIndex == 0) {
			visitorsNewsDetailsFragment.setNewsId((DataHolder.RowDBIds.get(0)));
			DataHolder.lastRowDBId = DataHolder.RowDBIds.get(0);
		} else
			visitorsNewsDetailsFragment.setNewsId(DataHolder.lastRowDBId);
		visitorsNewsDetailsFragment.createNewsDetailsObject();
	}

	// Visitors
	private void handleVisitorsOffersList() {

		final VisitorsDatabaseAdapter visitorsDatabaseAdapter = new VisitorsDatabaseAdapter(getActivity());
		visitorsDatabaseAdapter.open();

		offersCursor = visitorsDatabaseAdapter.fetchOffers();
		getActivity().startManagingCursor(offersCursor);
		setListAdapter(new VisitorsListAdapter(getActivity(), offersCursor, visitorsDatabaseAdapter));
		/***********/
		DataHolder.RowDBIds = new ArrayList<Integer>();
		DataHolder.tmpRowDBIds = new ArrayList<Integer>();
		offersCursor.moveToFirst();

		for (int i = 0; i < offersCursor.getCount(); i++) {
			String articleType = offersCursor.getString(offersCursor.getColumnIndex(VisitorsDatabaseAdapter.KEY_ARTICLE_TYPE));
			if (articleType.equals("article")) {
				DataHolder.RowDBIds.add(offersCursor.getInt(offersCursor.getColumnIndex(VisitorsDatabaseAdapter.KEY_ROWID)));
				DataHolder.tmpRowDBIds.add(offersCursor.getInt(offersCursor.getColumnIndex(VisitorsDatabaseAdapter.KEY_ROWID)));
			}
			offersCursor.moveToNext();
		}

		DataHolder.RowDBIds = new ArrayList<Integer>();
		for (int i = 0; i < DataHolder.tmpRowDBIds.size(); i++) {
			DataHolder.RowDBIds.add(DataHolder.tmpRowDBIds.get(i));
		}

		visitorsOffersDetailsFragment = (VisitorsOffersDetailsFragment) getFragmentManager().findFragmentById(R.id.detailsFragment);
		offersCursor.moveToFirst();
		getActivity().stopManagingCursor(offersCursor);

		// visitorsDatabaseAdapter.open();
		visitorCursor = null;
		if (DataHolder.rowDBSelectedIndex == 0) {
			visitorCursor = visitorsDatabaseAdapter.articleDetails(DataHolder.RowDBIds.get(0));
		} else {
			visitorCursor = visitorsDatabaseAdapter.articleDetails(DataHolder.lastRowDBId);
		}
		getActivity().startManagingCursor(visitorCursor);
		String articleType = visitorCursor.getString(visitorCursor.getColumnIndex(VisitorsDatabaseAdapter.KEY_ARTICLE_TYPE));

		if (articleType.equals("article-list")) {
			// linearFragment
			LinearLayout childLayot = (LinearLayout) getActivity().findViewById(R.id.lnrScrollable);
			LinearLayout masterLayout = (LinearLayout) getActivity().findViewById(R.id.lnr);
			FragmentManager fragmentManager = getFragmentManager();
			FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction(); // add
			VisitorsOffersListFragment myFragment = new VisitorsOffersListFragment();

			try {
				fragmentTransaction.replace(R.id.lnrScrollable, myFragment, "subList");
			} catch (Exception e) {
				fragmentTransaction.add(R.id.lnrScrollable, myFragment, "subList");
			}
			fragmentTransaction.commit();

			String articleId = visitorCursor.getString(visitorCursor.getColumnIndex(VisitorsDatabaseAdapter.KEY_ARTICLE_ID));
			visitorCursor = visitorsDatabaseAdapter.getArticleListId(articleId);
			int listId = visitorCursor.getInt(visitorCursor.getColumnIndex(VisitorsDatabaseAdapter.KEY_ARTICLELIST_ROWID));
			myFragment.setId(listId);
			subNewsCursor = visitorsDatabaseAdapter.fetchArticleList(listId);

			getActivity().startManagingCursor(subNewsCursor);
			myFragment.setListAdapter(new NewsListAdapter(getActivity(), subNewsCursor));
			masterLayout.removeView(childLayot);
			childLayot.setLayoutParams(new LinearLayout.LayoutParams(DataHolder.calculatedScreenResolution, LinearLayout.LayoutParams.FILL_PARENT));
			childLayot.setBackgroundResource(R.drawable.bg);
			final HorizontalScrollView s = new HorizontalScrollView(getActivity());
			s.setLayoutParams(new LinearLayout.LayoutParams(DataHolder.calculatedScreenResolution, LinearLayout.LayoutParams.FILL_PARENT));
			s.setId(108);
			s.setFadingEdgeLength(0);
			s.addView(childLayot);
			// add new fragment at runtime.
			masterLayout.addView(s);

			DataHolder._oldPosition = 0;

			// create sub list
			getActivity().startManagingCursor(subNewsCursor);
			myFragment.setListAdapter(new VisitorsListAdapter(getActivity(), subNewsCursor, visitorsDatabaseAdapter));

			// change UI of sub list item's first item
			int rowId = subNewsCursor.getInt(subNewsCursor.getColumnIndex(NewsDatabaseAdapter.KEY_ROWID));

			/* for maitaining index for using in flig */
			DataHolder.RowDBIds = new ArrayList<Integer>();
			subNewsCursor.moveToFirst();

			// use for fling
			for (int i = 0; i < subNewsCursor.getCount(); i++) {
				DataHolder.RowDBIds.add(subNewsCursor.getInt(subNewsCursor.getColumnIndex(NewsDatabaseAdapter.KEY_ROWID)));
				subNewsCursor.moveToNext();
			}

			visitorsOffersDetailsFragment.setVisitorsId(rowId);
			DataHolder.lastRowDBId = rowId;
			visitorsOffersDetailsFragment.createVisitorsDetailsObject();
			DataHolder._subId = rowId;
			Message m = new Message();
			m.obj = s;
			m.what = 1;
			newsHandler.dispatchMessage(m);
		} else {
			LinearLayout childLayot = (LinearLayout) getActivity().findViewById(R.id.lnrScrollable);
			childLayot.setLayoutParams(new LinearLayout.LayoutParams(DataHolder.calculatedScreenResolution, LinearLayout.LayoutParams.FILL_PARENT));
			childLayot.setBackgroundResource(R.drawable.bg);
			//
			// code for updating visitors detail fragment on load....
			visitorsOffersDetailsFragment = (VisitorsOffersDetailsFragment) getFragmentManager().findFragmentById(R.id.detailsFragment);
			if (DataHolder.rowDBSelectedIndex == 0) {
				visitorsOffersDetailsFragment.setVisitorsId(DataHolder.RowDBIds.get(0));
			} else {
				visitorsOffersDetailsFragment.setVisitorsId(DataHolder.lastRowDBId);
			}
			visitorsOffersDetailsFragment.createVisitorsDetailsObject();
		}
	}

	// Visitors
	private void handleVisitorsContactList() {
		final VisitorsDatabaseAdapter visitorsDatabaseAdapter = new VisitorsDatabaseAdapter(getActivity());
		visitorsDatabaseAdapter.open();

		Cursor offersCursor = visitorsDatabaseAdapter.fetchOffers();
		getActivity().startManagingCursor(offersCursor);
		setListAdapter(new VisitorsListAdapter(getActivity(), offersCursor, visitorsDatabaseAdapter));

		/***********/
		DataHolder.RowDBIds = new ArrayList<Integer>();
		DataHolder.tmpRowDBIds = new ArrayList<Integer>();
		offersCursor.moveToFirst();

		for (int i = 0; i < offersCursor.getCount(); i++) {
			String articleType = offersCursor.getString(offersCursor.getColumnIndex(VisitorsDatabaseAdapter.KEY_ARTICLE_TYPE));
			if (articleType.equals("article")) {
				DataHolder.RowDBIds.add(offersCursor.getInt(offersCursor.getColumnIndex(VisitorsDatabaseAdapter.KEY_ROWID)));
				DataHolder.tmpRowDBIds.add(offersCursor.getInt(offersCursor.getColumnIndex(VisitorsDatabaseAdapter.KEY_ROWID)));
			}
			offersCursor.moveToNext();
		}

		DataHolder.RowDBIds = new ArrayList<Integer>();
		for (int i = 0; i < DataHolder.tmpRowDBIds.size(); i++) {
			DataHolder.RowDBIds.add(DataHolder.tmpRowDBIds.get(i));
		}
		LinearLayout childLayot = (LinearLayout) getActivity().findViewById(R.id.lnrScrollable);
		childLayot.setLayoutParams(new LinearLayout.LayoutParams(DataHolder.calculatedScreenResolution, LinearLayout.LayoutParams.FILL_PARENT));
		childLayot.setBackgroundResource(R.drawable.bg);
	}

	private void handleCommitteesDetailMembersByTaskList(int committeesId) {
		CommitteesObject committeesObject = CommitteesDetailsMembersViewHelper.getCommittee(getActivity(), committeesId);

		List<MembersDetailsObject> presidents = CommitteesDetailsMembersViewHelper.getPresident(getActivity(), committeesObject.getId());

		List<MembersDetailsObject> subPresidents = CommitteesDetailsMembersViewHelper.getSubPresidents(getActivity(), committeesObject.getId());

		int committeeMembersCount = CommitteesDetailsMembersViewHelper.getCommitteeMembersCount(getActivity(), committeesObject.getId());

		createTopView((LinearLayout) getActivity().findViewById(R.id.topItem), presidents, subPresidents, committeesObject, committeeMembersCount);
		setListAdapter(new ComDetailsMemByFractionListAdapter(getActivity(), new ArrayList<HashMap<String, Object>>(), 0, new ArrayList<MembersDetailsObject>(),
				new ArrayList<MembersDetailsObject>(), committeesObject, "", 0));
		LinearLayout childLayot = (LinearLayout) getActivity().findViewById(R.id.lnrScrollable);
		childLayot.setLayoutParams(new LinearLayout.LayoutParams(DataHolder.calculatedScreenResolution, LinearLayout.LayoutParams.FILL_PARENT));
		childLayot.setBackgroundResource(R.drawable.bg);
		DataHolder.releaseScreenLock(getActivity());
	}

	private void handleCommitteesDetailMembersByFractionList(int committeesId, String committeesIdString, String fractionName) {
		CommitteesObject committeesObject = CommitteesDetailsMembersViewHelper.getCommittee(getActivity(), committeesId);

		// get members belonging to this committee

		// get the president
		List<MembersDetailsObject> presidents = CommitteesDetailsMembersViewHelper.getPresident(getActivity(), committeesObject.getId());

		// get the sub president
		List<MembersDetailsObject> subPresidents = CommitteesDetailsMembersViewHelper.getSubPresidents(getActivity(), committeesObject.getId());

		// create the members for each fraction in this committee
		ArrayList<HashMap<String, Object>> membersByFraction = CommitteesDetailsMembersViewHelper.getMembersByFraction(getActivity(), committeesId, committeesIdString,
				fractionName);

		int committeeMembersCount = CommitteesDetailsMembersViewHelper.getCommitteeMembersCount(getActivity(), committeesObject.getId());

		MembersDatabaseAdapter membersDatabaseAdapter = new MembersDatabaseAdapter(getActivity());
		membersDatabaseAdapter.open();
		Cursor memberCursor = membersDatabaseAdapter.fetchAllMembersByFractionsCommittee(fractionName, committeesIdString, MembersSynchronization.GROUP_TYPE_FULLMEMBER);
		int membersByFractionPresidentCount = 0;
		if (memberCursor != null) {
			membersByFractionPresidentCount = memberCursor.getCount();
		}
		createTopView((LinearLayout) getActivity().findViewById(R.id.topItem), presidents, subPresidents, committeesObject, committeeMembersCount);
		String memberId = "";
		DataHolder.RowDBIds = new ArrayList<Integer>();
		DataHolder.RowDBIds.add(-1);
		for (int i = 0; i < membersByFraction.size(); i++) {
			memberId = membersByFraction.get(i).get(MembersDatabaseAdapter.KEY_ID).toString();
			if (!memberId.equals("-1")) {
				memberCursor = membersDatabaseAdapter.fetchMemberFromId(memberId);
				int memberIndex = memberCursor.getInt(memberCursor.getColumnIndex(MembersDatabaseAdapter.KEY_ROWID));
				DataHolder.RowDBIds.add(memberIndex);
			} else {
				DataHolder.RowDBIds.add(-1);
			}
			System.out.println("ID : " + membersByFraction.get(i).get(MembersDatabaseAdapter.KEY_ID));

		}

		memberCursor.close();
		membersDatabaseAdapter.close();

		setListAdapter(new ComDetailsMemByFractionListAdapter(getActivity(), membersByFraction, membersByFractionPresidentCount, presidents, subPresidents, committeesObject,
				fractionName, committeeMembersCount));

		LinearLayout childLayot = (LinearLayout) getActivity().findViewById(R.id.lnrScrollable);
		childLayot.setLayoutParams(new LinearLayout.LayoutParams(DataHolder.calculatedScreenResolution, LinearLayout.LayoutParams.FILL_PARENT));
		childLayot.setBackgroundResource(R.drawable.bg);
		showDetailsMember(DataHolder.RowDBIds.get(1));
		DataHolder.rowDBSelectedIndex = 1;

	}

	private void createTopView(LinearLayout layout, List<MembersDetailsObject> president, List<MembersDetailsObject> vicepresidents, CommitteesObject committeesObject,
			int committeeMembersCount) {
		LayoutInflater mInflater = getActivity().getLayoutInflater();
		LinearLayout convertView = (LinearLayout) mInflater.inflate(R.layout.commitee_top_layout, layout, false);

		LayoutInflater inflater = (LayoutInflater) convertView.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		TextView committeeName = (TextView) convertView.findViewById(R.id.title);

		committeeName.setText((String) committeesObject.getName());
		MembersDatabaseAdapter membersDatabaseAdapter = new MembersDatabaseAdapter(getActivity());
		membersDatabaseAdapter.open();
		DataHolder.RowDBIds = new ArrayList<Integer>();
		DataHolder.rowDBSelectedIndex = 0;

		if (president.size() > 0) {
			LinearLayout memberItemLayout = null;

			for (int i = 0; i < president.size(); i++) {
				memberItemLayout = (LinearLayout) inflater.inflate(R.layout.committees_detail_members_first_item_member_item, null);
				memberItemLayout.setOrientation(LinearLayout.VERTICAL);
				TextView memberName = (TextView) memberItemLayout.findViewById(R.id.member_title);
				memberName.setText("Vorsitz");

				TextView committeeInfo = (TextView) memberItemLayout.findViewById(R.id.member_info);
				committeeInfo.setText(president.get(i).getFirstName() + " " + president.get(i).getLastName() + ", " + president.get(i).getFraction());

				String imageString = president.get(i).getMediaPhotoImageString();
				ImageView image = (ImageView) memberItemLayout.findViewById(R.id.image);
				if (imageString != null) {
					image.setImageBitmap(ImageHelper.convertStringToBitmap(imageString));
				} else {
					image.setImageBitmap(null);
				}

				String committeeID = president.get(i).getId();
				memberItemLayout.setTag(committeeID);
				Cursor memberCursor = membersDatabaseAdapter.fetchMemberFromId(committeeID);
				int memberIndex = memberCursor.getInt(memberCursor.getColumnIndex(MembersDatabaseAdapter.KEY_ROWID));
				DataHolder.RowDBIds.add(memberIndex);

				memberItemLayout.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View view) {

						if (getActivity() instanceof CommitteesDetailsTasksActivity) {

							DataHolder.rowDBSelectedIndex = 0;
						} else {
							String memberID = (String) view.getTag();
							MembersDatabaseAdapter membersDatabaseAdapter = new MembersDatabaseAdapter(getActivity());
							membersDatabaseAdapter.open();
							Cursor memberCursor = membersDatabaseAdapter.fetchMemberFromId(memberID);
							int memberIndex = memberCursor.getInt(memberCursor.getColumnIndex(MembersDatabaseAdapter.KEY_ROWID));
							memberCursor.close();
							membersDatabaseAdapter.close();
							showDetailsMember(memberIndex);
						}
					}
				});
				convertView.addView(memberItemLayout);
			}
		}
		if (vicepresidents.size() > 0) {
			LinearLayout memberItemLayout = null;

			for (int i = 0; i < vicepresidents.size(); i++) {
				memberItemLayout = (LinearLayout) inflater.inflate(R.layout.committees_detail_members_first_item_member_item, null);
				memberItemLayout.setOrientation(LinearLayout.VERTICAL);
				TextView memberName = (TextView) memberItemLayout.findViewById(R.id.member_title);
				memberName.setText("Stellv. Vorsitz");

				TextView committeeInfo = (TextView) memberItemLayout.findViewById(R.id.member_info);
				committeeInfo.setText(vicepresidents.get(i).getFirstName() + " " + vicepresidents.get(i).getLastName() + ", " + vicepresidents.get(i).getFraction());

				String imageString = vicepresidents.get(i).getMediaPhotoImageString();
				ImageView image = (ImageView) memberItemLayout.findViewById(R.id.image);
				if (imageString != null) {
					image.setImageBitmap(ImageHelper.convertStringToBitmap(imageString));
				} else {
					image.setImageBitmap(null);
				}

				String committeeID = vicepresidents.get(i).getId();
				memberItemLayout.setTag(committeeID);
				Cursor memberCursor = membersDatabaseAdapter.fetchMemberFromId(committeeID);
				int memberIndex = memberCursor.getInt(memberCursor.getColumnIndex(MembersDatabaseAdapter.KEY_ROWID));
				DataHolder.RowDBIds.add(memberIndex);
				memberItemLayout.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View view) {

						if (getActivity() instanceof CommitteesDetailsTasksActivity) {
							DataHolder.rowDBSelectedIndex = 0;
						} else {
							String memberID = (String) view.getTag();
							MembersDatabaseAdapter membersDatabaseAdapter = new MembersDatabaseAdapter(getActivity());
							membersDatabaseAdapter.open();
							Cursor memberCursor = membersDatabaseAdapter.fetchMemberFromId(memberID);
							int memberIndex = memberCursor.getInt(memberCursor.getColumnIndex(MembersDatabaseAdapter.KEY_ROWID));
							showDetailsMember(memberIndex);
							memberCursor.close();
							membersDatabaseAdapter.close();
						}
					}
				});
				convertView.addView(memberItemLayout);
			}
		}
		layout.addView(convertView);
	}

	private void handleVisitorsLocationsList() {
		VisitorsDatabaseAdapter visitorsDatabaseAdapter = new VisitorsDatabaseAdapter(getActivity());
		visitorsDatabaseAdapter.open();

		Cursor offersCursor = visitorsDatabaseAdapter.fetchLocations();
		getActivity().startManagingCursor(offersCursor);
		setListAdapter(new VisitorsListAdapter(getActivity(), offersCursor, visitorsDatabaseAdapter));

		/***********/
		DataHolder.RowDBIds = new ArrayList<Integer>();

		offersCursor.moveToFirst();

		for (int i = 0; i < offersCursor.getCount(); i++) {
			String articleType = offersCursor.getString(offersCursor.getColumnIndex(VisitorsDatabaseAdapter.KEY_ARTICLE_TYPE));
			if (articleType.equals("article")) {
				DataHolder.RowDBIds.add(offersCursor.getInt(offersCursor.getColumnIndex(VisitorsDatabaseAdapter.KEY_ROWID)));
			}
			offersCursor.moveToNext();
		}
		// code for updating visitors detail fragment on load....
		visitorsLocationsDetailsFragment = (VisitorsLocationsDetailsFragment) getFragmentManager().findFragmentById(R.id.detailsFragment);
		if (DataHolder.rowDBSelectedIndex == 0) {
			visitorsLocationsDetailsFragment.setRowId(DataHolder.RowDBIds.get(0));
			DataHolder.lastRowDBId = DataHolder.RowDBIds.get(0);
		} else {
			visitorsLocationsDetailsFragment.setRowId(DataHolder.lastRowDBId);
		}

		visitorsLocationsDetailsFragment.createNewsDetailsObject();

		LinearLayout childLayot = (LinearLayout) getActivity().findViewById(R.id.lnrScrollable);
		childLayot.setLayoutParams(new LinearLayout.LayoutParams(DataHolder.calculatedScreenResolution, LinearLayout.LayoutParams.FILL_PARENT));
		childLayot.setBackgroundResource(R.drawable.bg);
		//
	}

	private void handleVisitorsNewsList() {
		NewsDatabaseAdapter newsDatabaseAdapter = new NewsDatabaseAdapter(getActivity());
		try {
			newsDatabaseAdapter.open();
		} catch (Exception e) {
		}

		Cursor newsCursor = newsDatabaseAdapter.fetchAllVisitorNews();
		getActivity().startManagingCursor(newsCursor);
		setListAdapter(new NewsListAdapter(getActivity(), newsCursor));
		/***********/
		/* for maitaining index for using in flig */
		DataHolder.RowDBIds = new ArrayList<Integer>();
		newsCursor.moveToFirst();
		for (int i = 0; i < newsCursor.getCount(); i++) {
			Integer newsType = newsCursor.getInt(newsCursor.getColumnIndex(NewsDatabaseAdapter.KEY_TYPE));
			if ((newsType != null) && (newsType.intValue() != NewsSynchronization.NEWS_TYPE_LIST)) {
				DataHolder.RowDBIds.add(newsCursor.getInt(newsCursor.getColumnIndex(NewsDatabaseAdapter.KEY_ROWID)));
			}
			newsCursor.moveToNext();

		}
		// code for updating visitors detail fragment on load....
		visitorsNewsDetailsFragment = (VisitorsNewsDetailsFragment) getFragmentManager().findFragmentById(R.id.detailsFragment);
		CursorAdapter tmp = (CursorAdapter) getListAdapter();
		NewsListAdapter ntmp = (NewsListAdapter) tmp;
		if (DataHolder.rowDBSelectedIndex == 0) {
			Cursor c = (Cursor) ntmp.getItem(0);
			visitorsNewsDetailsFragment.setNewsId(c.getInt(c.getColumnIndex(NewsDatabaseAdapter.KEY_ROWID)));
			DataHolder.lastRowDBId = c.getInt(c.getColumnIndex(NewsDatabaseAdapter.KEY_ROWID));
		} else {
			visitorsNewsDetailsFragment.setNewsId(DataHolder.lastRowDBId);
		}
		visitorsNewsDetailsFragment.createNewsDetailsObject();
		LinearLayout childLayot = (LinearLayout) getActivity().findViewById(R.id.lnrScrollable);
		childLayot.setLayoutParams(new LinearLayout.LayoutParams(DataHolder.calculatedScreenResolution, LinearLayout.LayoutParams.FILL_PARENT));
		childLayot.setBackgroundResource(R.drawable.bg);
		TextView mainTitle = (TextView) getActivity().findViewById(R.id.mainTitle);
		mainTitle.setLayoutParams(new LinearLayout.LayoutParams(DataHolder.calculatedScreenResolution, LinearLayout.LayoutParams.WRAP_CONTENT));
	}

	private void handleMembersList() {
		MembersDatabaseAdapter membersDatabaseAdapter = new MembersDatabaseAdapter(getActivity());
		membersDatabaseAdapter.open();
		LinearLayout titleSubmenu = (LinearLayout) getActivity().findViewById(R.id.titleSubmenu);
		titleSubmenu.setVisibility(View.GONE);
		// TextView valueTV = (TextView)
		// getActivity().findViewById(R.id.headingMembers);
		// valueTV.setVisibility(View.GONE);
		// Cursor newsCursor = membersDatabaseAdapter.fetchAllMembers();
		Cursor newsCursor = membersDatabaseAdapter.fetchAllMembersOptimized();
		getActivity().startManagingCursor(newsCursor);
		setListAdapter(new MembersListAdapter(getActivity(), newsCursor));

		newsCursor.moveToFirst();
		DataHolder.RowDBIds = new ArrayList<Integer>();
		for (int i = 0; i < newsCursor.getCount(); i++) {

			DataHolder.RowDBIds.add(newsCursor.getInt(newsCursor.getColumnIndex(NewsDatabaseAdapter.KEY_ROWID)));

			newsCursor.moveToNext();

		}

		LinearLayout childLayot = (LinearLayout) getActivity().findViewById(R.id.lnrScrollable);
		childLayot.setLayoutParams(new LinearLayout.LayoutParams(DataHolder.calculatedScreenResolution, LinearLayout.LayoutParams.FILL_PARENT));
		childLayot.setBackgroundResource(R.drawable.bg);

		// DataHolder.rowDBSelectedIndex = 0;

		membersDetailsFragment = (MembersDetailsFragment) getFragmentManager().findFragmentById(R.id.detailsFragment);
		if (DataHolder.rowDBSelectedIndex == 0) {
			membersDetailsFragment.setSelectedID(DataHolder.RowDBIds.get(0));
		} else {
			membersDetailsFragment.setSelectedID(DataHolder.lastRowDBId);
		}
		membersDetailsFragment.createMembersDetailsObject();
	}

	private void handleMembersSubpage(int subPageId, int index) {
		ArrayList values = MembersSubpageViewHelper.createMembersSubpage(getActivity(), subPageId, index);
		Cursor membersCursor = (Cursor) values.get(0);
		String selected = (String) values.get(1);
		getActivity().startManagingCursor(membersCursor);
		TextView valueTV = (TextView) getActivity().findViewById(R.id.headingMembers);

		if (subPageId == MembersSubpageMembersActivity.SUB_PAGE_FRACTION) {
			TextView count = (TextView) getActivity().findViewById(R.id.countFraction);
			valueTV.setText(selected);// list
			count.setText(DataHolder.memberCount + " Mitglieder");
		} else {
			// Add textView with corresponding title - fraction, city or
			// election

			String tmp = "";

			try {
				tmp = selected.split(":")[0] + ":";
				tmp = DataHolder.strHeading + " " + tmp;
				tmp = tmp + "\n" + selected.split(":")[1];

			} catch (Exception e) {
				tmp = DataHolder.strHeading + ":\n" + selected;
			}

			valueTV.setText(tmp);
		}
		setListAdapter(new MembersListAdapter(getActivity(), membersCursor));

		membersCursor.moveToFirst();
		DataHolder.RowDBIds = new ArrayList<Integer>();
		for (int i = 0; i < membersCursor.getCount(); i++) {

			DataHolder.RowDBIds.add(membersCursor.getInt(membersCursor.getColumnIndex(NewsDatabaseAdapter.KEY_ROWID)));

			membersCursor.moveToNext();

		}
		// code for updating news detail fragment on load....

		membersDetailsFragment = (MembersDetailsFragment) getFragmentManager().findFragmentById(R.id.detailsFragment);
		// DataHolder.rowDBSelectedIndex = 0;
		if (DataHolder.rowDBSelectedIndex == 0) {
			membersDetailsFragment.setSelectedID(DataHolder.RowDBIds.get(0));
		} else {
			membersDetailsFragment.setSelectedID(DataHolder.lastRowDBId);
		}
		membersDetailsFragment.createMembersDetailsObject();
		LinearLayout childLayot = (LinearLayout) getActivity().findViewById(R.id.lnrScrollable);
		childLayot.setLayoutParams(new LinearLayout.LayoutParams(DataHolder.calculatedScreenResolution, LinearLayout.LayoutParams.FILL_PARENT));
		childLayot.setBackgroundResource(R.drawable.bg);
	}

	/**
	 * List click handler.
	 * 
	 * Whenever a general list fragment is clicked it will call the method
	 * showDetails with the position and the index of the clicked item.
	 * 
	 * Since we both use cursor adapters and base adapters, we need to check
	 * according to the activity, which one we are using.
	 */
	@Override
	public void onListItemClick(ListView listView, View view, int position, long id) {
		try {
			System.out.println("Id : " + id);
			Activity activity = getActivity();
			boolean isOnline = DataHolder.isOnline(activity);
			if (!isOnline && (activity instanceof NewsActivityTablet || activity instanceof VisitorsNewsActivityTablet)) {
				DataHolder.alertboxOk(activity);
				resetVariables();
			}
			if (isOnline
					&& ((activity instanceof NewsActivityTablet && DataHolder._oldPosition != position) || (activity instanceof NewsActivityTablet && DataHolder.isSublistLoaded))) {
				DataHolder.mLockScreenRotation(activity);
				onAktullActivityClick(listView, view, position, id, activity);
			} else if ((activity instanceof VisitorsOffersActivityTablet && DataHolder._oldPosition != position)) {
				CursorAdapter vListTemp = (CursorAdapter) getListAdapter();
				VisitorsListAdapter vList = (VisitorsListAdapter) vListTemp;
				int type = vList.getItemViewType(position);
				if (type != vList.GALLERY_LAYOUT) {
					onVisitorsOfferClick(listView, view, position, id, activity);
				}
			} else if ((activity instanceof VisitorsContactActivity && DataHolder._oldPosition != position) || DataHolder.isSublistLoaded) {
				CursorAdapter vListTemp = (CursorAdapter) getListAdapter();
				VisitorsListAdapter vList = (VisitorsListAdapter) vListTemp;
				DataHolder.rowDBSelectedIndex = position - 1;
				// ///neeraj added start////////////
				VisitorsListAdapter ntmp = (VisitorsListAdapter) vListTemp;
				Cursor c = (Cursor) ntmp.getItem(position);
				int rowId = c.getInt(c.getColumnIndex(NewsDatabaseAdapter.KEY_ROWID));
				DataHolder._id = rowId;
				DataHolder.lastRowDBId = rowId;
				// //////neeraj added end////
				int type = vList.getItemViewType(position);
				if (type != vList.GALLERY_LAYOUT) {
					startActivity(new Intent(this.getActivity(), VisitorsOffersActivityTablet.class));
				}
			} else if ((activity instanceof VisitorsLocationsActivityTablet && DataHolder._oldPosition != position) || DataHolder.isSublistLoaded) {
				CursorAdapter vListTemp = (CursorAdapter) getListAdapter();
				VisitorsListAdapter vList = (VisitorsListAdapter) vListTemp;
				int type = vList.getItemViewType(position);
				if (type != vList.GALLERY_LAYOUT) {
					onVisitorsLocationClick(listView, view, position, id, activity);
				}
			} else if (isOnline && ((activity instanceof VisitorsNewsActivityTablet && DataHolder._oldPosition != position) || DataHolder.isSublistLoaded)) {
				DataHolder.mLockScreenRotation(activity);
				Cursor cursor = (Cursor) getListView().getItemAtPosition(position);
				getActivity().startManagingCursor(cursor);
				DataHolder._id = cursor.getInt(cursor.getColumnIndex(NewsDatabaseAdapter.KEY_ROWID));
				visitorsNewsDetailsFragment.setNewsId(DataHolder._id);
				DataHolder.lastRowDBId = DataHolder._id;
				DataHolder.rowDBSelectedIndex = position;
				visitorsNewsDetailsFragment.createNewsDetailsObject();
			} else if (activity instanceof MembersListActivity) {
				CursorAdapter tmp = (CursorAdapter) getListAdapter();
				MembersListAdapter ntmp = (MembersListAdapter) tmp;
				Cursor c = (Cursor) ntmp.getItem(position);
				int rowId = c.getInt(c.getColumnIndex(NewsDatabaseAdapter.KEY_ROWID));
				DataHolder._id = rowId;
				DataHolder.rowDBSelectedIndex = position;
				membersDetailsFragment.setSelectedID(rowId);
				DataHolder.lastRowDBId = rowId;
				membersDetailsFragment.createMembersDetailsObject();
			} else if (activity instanceof MembersSubpageMembersActivity) {
				CursorAdapter tmp = (CursorAdapter) getListAdapter();
				MembersListAdapter ntmp = (MembersListAdapter) tmp;
				Cursor c = (Cursor) ntmp.getItem(position);
				int rowId = c.getInt(c.getColumnIndex(MembersDatabaseAdapter.KEY_ROWID));
				DataHolder._id = rowId;
				DataHolder.rowDBSelectedIndex = position;
				membersDetailsFragment.setSelectedID(rowId);
				DataHolder.lastRowDBId = rowId;
				membersDetailsFragment.createMembersDetailsObject();
			} else if (activity instanceof CommitteesActivityTablet) {
				onCommmitteActivityClcik(listView, view, position, id, activity);
			} else if (activity instanceof DebateNewsActivity) {
				DataHolder.mLockScreenRotation(activity);
				Cursor cursor = (Cursor) getListView().getItemAtPosition(position);
				getActivity().startManagingCursor(cursor);
				DataHolder._id = cursor.getInt(cursor.getColumnIndex(NewsDatabaseAdapter.KEY_ROWID));
				visitorsNewsDetailsFragment.setNewsId(DataHolder._id);
				DataHolder.lastRowDBId = DataHolder._id;
				DataHolder.rowDBSelectedIndex = position;
				visitorsNewsDetailsFragment.createNewsDetailsObject();
			} else if (activity instanceof CommitteesDetailsNewsActivity) {
				Cursor cursor = (Cursor) getListView().getItemAtPosition(position);
				getActivity().startManagingCursor(cursor);
				DataHolder._id = cursor.getInt(cursor.getColumnIndex(NewsDatabaseAdapter.KEY_ROWID));
				committeeDetailsFragmentNews.setRowId(DataHolder._id);
				DataHolder.rowDBSelectedIndex = position;
				DataHolder.lastRowDBId = DataHolder._id;
				committeeDetailsFragmentNews.createNewsDetailsObject();
			} else if (activity instanceof CommitteesDetailsMembersByFractionActivity) {
				if (DataHolder.RowDBIds.get(position) != -1) {
					showDetailsMember(DataHolder.RowDBIds.get(position));
					DataHolder.lastRowDBId = DataHolder.RowDBIds.get(position);
					DataHolder.rowDBSelectedIndex = position;
				}
			} else if (activity instanceof CommitteesDetailsNewsDetailsFragmentActivity) {
				Cursor cursor = (Cursor) getListView().getItemAtPosition(position);
				getActivity().startManagingCursor(cursor);
				DataHolder._id = cursor.getInt(cursor.getColumnIndex(CommitteesDatabaseAdapter.KEY_ROWID));
				DataHolder.lastRowDBId = DataHolder._id;
				DataHolder.rowDBSelectedIndex = position;
				committeesDetailsNewsDetailsFragment.setRowId(DataHolder._id);
				DataHolder.lastRowDBId = DataHolder.RowDBIds.get(position);
				committeesDetailsNewsDetailsFragment.createNewsDetailsObject();
			}
			DataHolder._oldPosition = position;

			if (activity instanceof VisitorsLocationsActivityTablet || activity instanceof VisitorsOffersActivityTablet || activity instanceof VisitorsNewsActivityTablet
					&& position != 0) {
				DataHolder.rowDBSelectedIndex = position - 1;
			}

			listView.setItemChecked(position, true);
		} catch (Exception e) {
			System.err.println("Error : " + e.getMessage());
		}
	}

	/**
	 * Special to see member by fraction and committee Invoked whenever the
	 * general list fragment is clicked. It will call the activity to handle the
	 * click, depending on the current
	 * 
	 */
	public void showDetailsMember(int memberId) {
		MembersDetailsFragment membersDetailsFragment = (MembersDetailsFragment) getFragmentManager().findFragmentById(R.id.detailsFragment);
		membersDetailsFragment.setSelectedID(memberId);
		DataHolder.lastRowDBId = memberId;
		membersDetailsFragment.createMembersDetailsObject();
	}

	/**
	 * 
	 * This method will call on click of any list item of Aktuell.
	 */
	private void onAktullActivityClick(ListView listView, View view, int position, long id, Activity activity) {
		LinearLayout childLayot = (LinearLayout) getActivity().findViewById(R.id.lnrScrollable);
		LinearLayout detailLayout = (LinearLayout) getActivity().findViewById(R.id.detailLayout);

		newsDetailsFragment = (NewsDetailsFragment) getFragmentManager().findFragmentById(R.id.detailsFragment);
		newsDetailsFragment.setIsMasterList(true);
		CursorAdapter tmp = (CursorAdapter) getListAdapter();
		NewsListAdapter ntmp = (NewsListAdapter) tmp;
		Cursor c = (Cursor) ntmp.getItem(position);
		int rowId = c.getInt(c.getColumnIndex(NewsDatabaseAdapter.KEY_ROWID));
		NewsDatabaseAdapter newsDatabaseAdapter = new NewsDatabaseAdapter(activity);
		newsDatabaseAdapter.open();
		Cursor newsCursor = newsDatabaseAdapter.fetchNews(rowId);
		// this is use for fling
		DataHolder.RowDBIds = new ArrayList<Integer>();
		for (int i = 0; i < DataHolder.tmpRowDBIds.size(); i++) {
			DataHolder.RowDBIds.add(DataHolder.tmpRowDBIds.get(i));
		}
		// check for fragment of sbulist, if it is there then remove at startup.
		newsCursor.moveToFirst();
		Integer newsType = newsCursor.getInt(newsCursor.getColumnIndex(NewsDatabaseAdapter.KEY_TYPE));

		FragmentManager fragmentManager = getFragmentManager();
		FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction(); // add
		LinearLayout masterLayout = (LinearLayout) getActivity().findViewById(R.id.master);

		try {
			fragmentTransaction.remove(fragmentManager.findFragmentByTag("subList"));
		} catch (Exception e) {
		}

		try {
			HorizontalScrollView s = (HorizontalScrollView) getActivity().findViewById(108);
			s.removeView(childLayot);
			masterLayout.removeView(s);
			childLayot.setLayoutParams(new LinearLayout.LayoutParams(DataHolder.calculatedScreenResolution, LinearLayout.LayoutParams.FILL_PARENT));
			childLayot.setBackgroundResource(R.drawable.bg);
			detailLayout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.FILL_PARENT, 1.0f));
			if (DataHolder.isLandscape) {
				masterLayout.addView(childLayot, 1);
			} else {
				masterLayout.addView(childLayot, 0);
			}
		} catch (Exception e) {

		}
		// check whether list contains sublist

		if ((newsType != null) && (newsType.intValue() == NewsSynchronization.NEWS_TYPE_LIST)) {
			DataHolder.isSublistLoaded = true;
			Integer newsId = newsCursor.getInt(newsCursor.getColumnIndex(NewsDatabaseAdapter.KEY_ROWID));
			newsCursor = newsDatabaseAdapter.getListIdFromNewsId(newsId);
			Integer listId = newsCursor.getInt(newsCursor.getColumnIndex(NewsDatabaseAdapter.KEY_NEWSLIST_ROWID));

			// create sub list
			NewsSubListFragment myFragment = new NewsSubListFragment();

			try {
				fragmentTransaction.replace(R.id.lnrScrollable, myFragment, "subList");
			} catch (Exception e) {
				fragmentTransaction.add(R.id.lnrScrollable, myFragment, "subList");
			}

			fragmentTransaction.commit();
			myFragment.setId(listId);
			Cursor subNewsCursor = newsDatabaseAdapter.fetchSubListNews(listId);
			getActivity().startManagingCursor(subNewsCursor);
			myFragment.setListAdapter(new NewsListAdapter(getActivity(), subNewsCursor));
			masterLayout.removeView(childLayot);
			final HorizontalScrollView s = new HorizontalScrollView(getActivity());
			childLayot.setLayoutParams(new LinearLayout.LayoutParams(DataHolder.calculatedScreenResolution, LinearLayout.LayoutParams.FILL_PARENT));
			childLayot.setBackgroundResource(R.drawable.bg);
			s.setLayoutParams(new LinearLayout.LayoutParams(DataHolder.calculatedScreenResolution, LinearLayout.LayoutParams.FILL_PARENT));
			s.setId(108);
			s.addView(childLayot);
			s.setFadingEdgeLength(0);
			// add new fragment at runtime.
			if (DataHolder.isLandscape) {
				masterLayout.addView(s, 1);
			} else {
				masterLayout.addView(s, 0);
			}
			DataHolder._oldPosition = 0;
			Message m = new Message();
			m.obj = s;
			m.what = 1;
			newsHandler.dispatchMessage(m);

			/* for maitaining index for using in flig */
			DataHolder.RowDBIds = new ArrayList<Integer>();
			newsCursor = newsDatabaseAdapter.fetchSubListNews(listId);
			newsCursor.moveToFirst();

			// use for fling
			for (int i = 0; i < newsCursor.getCount(); i++) {
				newsType = newsCursor.getInt(newsCursor.getColumnIndex(NewsDatabaseAdapter.KEY_TYPE));
				if ((newsType != null) && (newsType.intValue() != NewsSynchronization.NEWS_TYPE_LIST)) {
					DataHolder.RowDBIds.add(newsCursor.getInt(newsCursor.getColumnIndex(NewsDatabaseAdapter.KEY_ROWID)));
				}
				newsCursor.moveToNext();
			}

			// code for updating news detail fragment on load....
			newsDetailsFragment = (NewsDetailsFragment) getFragmentManager().findFragmentById(R.id.detailsFragment);
			newsDetailsFragment.setNewsId(DataHolder.RowDBIds.get(0));
			DataHolder.lastRowDBId = DataHolder.RowDBIds.get(0);
			DataHolder.rowDBSelectedIndex = 0;
			newsDetailsFragment.createNewsDetailsObject();
			newsDetailsFragment.setIsMasterList(false);
			DataHolder._id = rowId;
		} else {
			// if news don't have sublist.
			DataHolder._id = rowId;
			DataHolder.rowDBSelectedIndex = position;
			newsDetailsFragment.setNewsId(rowId);
			DataHolder.lastRowDBId = rowId;
			newsDetailsFragment.createNewsDetailsObject();
			childLayot.setLayoutParams(new LinearLayout.LayoutParams(DataHolder.calculatedScreenResolution, LinearLayout.LayoutParams.FILL_PARENT));
			childLayot.setBackgroundResource(R.drawable.bg);

		}

	}

	private void onCommmitteActivityClcik(ListView listView, View view, int position, long id, Activity activity) {
		CursorAdapter tmp = (CursorAdapter) getListAdapter();
		CommitteesListAdapter ntmp = (CommitteesListAdapter) tmp;
		Cursor c = (Cursor) ntmp.getItem(position);
		int rowId = c.getInt(c.getColumnIndex(CommitteesDatabaseAdapter.KEY_ROWID));
		String committeeId = c.getString(c.getColumnIndex(CommitteesDatabaseAdapter.KEY_ID));
		if (DataHolder.isOnline(activity)) {
			CommitteesDatabaseAdapter committeesDatabaseAdapter = new CommitteesDatabaseAdapter(getActivity());
			committeesDatabaseAdapter.open();
			Cursor subTableData = committeesDatabaseAdapter.fetchCommitteesById(c.getString(c.getColumnIndex(CommitteesDatabaseAdapter.KEY_ID)));

			if (subTableData.getCount() > 0) {
				DataHolder.committeeName = c.getString(c.getColumnIndex(CommitteesDatabaseAdapter.KEY_COURSE));
				SynchronizeCheckCommitteeNewsTask synchronizeCheckCommitteeNewsTask = new SynchronizeCheckCommitteeNewsTask(true, committeeId);
				BaseSynchronizeTask task = synchronizeCheckCommitteeNewsTask;
				synchronizeCheckCommitteeNewsTask.execute(this.getActivity());
			} else {
				SynchronizeCommitteeNewsTask committeeNewsUpdateTask = new SynchronizeCommitteeNewsTask();
				BaseSynchronizeTask task = committeeNewsUpdateTask;
				committeeNewsUpdateTask.committeeId = committeeId;
				committeeNewsUpdateTask.execute(this.getActivity());
			}

		} else {

			Intent intent = new Intent();
			intent.putExtra("COMMITTEE_ID", committeeId);
			intent.setClass(getActivity(), CommitteesDetailsTasksActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
			getActivity().overridePendingTransition(0, 0);
			DataHolder._id = rowId;
			committeesDetailsFragment = (CommitteesDetailsFragment) getFragmentManager().findFragmentById(R.id.detailsFragment);
			committeesDetailsFragment.setRowId(rowId);
			DataHolder.lastRowDBId = rowId;
			committeesDetailsFragment.createCommitteesDetailsObject();
		}

	}

	private void onVisitorsOfferClick(ListView listView, View view, int position, long id, Activity activity) {

		LinearLayout childLayot = (LinearLayout) getActivity().findViewById(R.id.lnrScrollable);
		LinearLayout detailLayout = (LinearLayout) getActivity().findViewById(R.id.detailLayout);

		visitorsOffersDetailsFragment = (VisitorsOffersDetailsFragment) getFragmentManager().findFragmentById(R.id.detailsFragment);

		DataHolder.RowDBIds = new ArrayList<Integer>();
		for (int i = 0; i < DataHolder.tmpRowDBIds.size(); i++) {
			DataHolder.RowDBIds.add(DataHolder.tmpRowDBIds.get(i));
		}

		Cursor cursor = (Cursor) getListView().getItemAtPosition(position);
		getActivity().startManagingCursor(cursor);
		int index = cursor.getInt(cursor.getColumnIndex(NewsDatabaseAdapter.KEY_ROWID));

		VisitorsDatabaseAdapter visitorsDatabaseAdapter = new VisitorsDatabaseAdapter(activity);
		visitorsDatabaseAdapter.open();
		Cursor visitorCursor = visitorsDatabaseAdapter.articleDetails(index);

		String articleType = visitorCursor.getString(visitorCursor.getColumnIndex(VisitorsDatabaseAdapter.KEY_ARTICLE_TYPE));

		FragmentManager fragmentManager = getFragmentManager();
		FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction(); // add
		LinearLayout masterLayout = (LinearLayout) getActivity().findViewById(R.id.lnr);

		try {
			fragmentTransaction.remove(fragmentManager.findFragmentByTag("subList"));
		} catch (Exception e) {
		}

		try {
			HorizontalScrollView s = (HorizontalScrollView) getActivity().findViewById(108);
			s.removeView(childLayot);
			masterLayout.removeView(s);
			childLayot.setLayoutParams(new LinearLayout.LayoutParams(DataHolder.calculatedScreenResolution, LinearLayout.LayoutParams.FILL_PARENT));
			childLayot.setBackgroundResource(R.drawable.bg);
			detailLayout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.FILL_PARENT, 1.0f));
			masterLayout.addView(childLayot);
		} catch (Exception e) {

		}

		if (articleType.equals("article-list")) {
			DataHolder.isSublistLoaded = true;
			// linearFragment
			VisitorsOffersListFragment myFragment = new VisitorsOffersListFragment();

			try {
				fragmentTransaction.replace(R.id.lnrScrollable, myFragment, "subList");
			} catch (Exception e) {
				fragmentTransaction.add(R.id.lnrScrollable, myFragment, "subList");
			}
			fragmentTransaction.commit();

			String articleId = visitorCursor.getString(visitorCursor.getColumnIndex(VisitorsDatabaseAdapter.KEY_ARTICLE_ID));
			visitorCursor = visitorsDatabaseAdapter.getArticleListId(articleId);
			int listId = visitorCursor.getInt(visitorCursor.getColumnIndex(VisitorsDatabaseAdapter.KEY_ARTICLELIST_ROWID));
			myFragment.setId(listId);
			Cursor subNewsCursor = visitorsDatabaseAdapter.fetchArticleList(listId);

			getActivity().startManagingCursor(subNewsCursor);
			myFragment.setListAdapter(new NewsListAdapter(getActivity(), subNewsCursor));
			masterLayout.removeView(childLayot);
			childLayot.setLayoutParams(new LinearLayout.LayoutParams(DataHolder.calculatedScreenResolution, LinearLayout.LayoutParams.FILL_PARENT));
			childLayot.setBackgroundResource(R.drawable.bg);
			final HorizontalScrollView s = new HorizontalScrollView(getActivity());
			s.setLayoutParams(new LinearLayout.LayoutParams(DataHolder.calculatedScreenResolution, LinearLayout.LayoutParams.FILL_PARENT));
			s.setId(108);
			s.setFadingEdgeLength(0);
			s.addView(childLayot);
			// add new fragment at runtime.
			masterLayout.addView(s);
			DataHolder._oldPosition = 0;
			// Message m = new Message();
			// m.obj = s;
			// m.what = 1;
			// newsHandler.dispatchMessage(m);
			// create sub list
			getActivity().startManagingCursor(subNewsCursor);
			myFragment.setListAdapter(new VisitorsListAdapter(getActivity(), subNewsCursor, visitorsDatabaseAdapter));

			// change UI of sub list item's first item
			int rowId = subNewsCursor.getInt(subNewsCursor.getColumnIndex(NewsDatabaseAdapter.KEY_ROWID));

			/* for maitaining index for using in flig */
			DataHolder.RowDBIds = new ArrayList<Integer>();
			subNewsCursor.moveToFirst();

			// use for fling
			for (int i = 0; i < subNewsCursor.getCount(); i++) {
				DataHolder.RowDBIds.add(subNewsCursor.getInt(subNewsCursor.getColumnIndex(NewsDatabaseAdapter.KEY_ROWID)));
				subNewsCursor.moveToNext();
			}

			visitorsOffersDetailsFragment.setVisitorsId(rowId);
			DataHolder.lastRowDBId = rowId;
			visitorsOffersDetailsFragment.createVisitorsDetailsObject();
			DataHolder._subId = rowId;
			// s.fullScroll(ScrollView.FOCUS_RIGHT);
			getActivity().stopManagingCursor(visitorCursor);
			visitorCursor.close();

			getActivity().stopManagingCursor(subNewsCursor);
			subNewsCursor.close();
		} else {

			DataHolder._id = index;
			visitorsOffersDetailsFragment.setVisitorsId(index);
			visitorsOffersDetailsFragment.createVisitorsDetailsObject();

			for (int i = 0; i < DataHolder.RowDBIds.size(); i++) {
				if (DataHolder.RowDBIds.get(i) == DataHolder._id) {
					DataHolder.rowDBSelectedIndex = i;
				}
			}
			DataHolder.lastRowDBId = index;
			childLayot.setLayoutParams(new LinearLayout.LayoutParams(DataHolder.calculatedScreenResolution, LinearLayout.LayoutParams.FILL_PARENT));
			childLayot.setBackgroundResource(R.drawable.bg);

		}
	}

	private void onVisitorsLocationClick(ListView listView, View view, int position, long id, Activity activity) {
		visitorsLocationsDetailsFragment = (VisitorsLocationsDetailsFragment) getFragmentManager().findFragmentById(R.id.detailsFragment);

		Cursor cursor = (Cursor) getListView().getItemAtPosition(position);
		getActivity().startManagingCursor(cursor);

		for (int i = 0; i < DataHolder.RowDBIds.size(); i++) {
			if (DataHolder.RowDBIds.get(i) == DataHolder._id) {
				DataHolder.rowDBSelectedIndex = i;
			}
		}
		DataHolder._id = cursor.getInt(cursor.getColumnIndex(NewsDatabaseAdapter.KEY_ROWID));
		visitorsLocationsDetailsFragment.setRowId(DataHolder._id);
		DataHolder.lastRowDBId = DataHolder._id;
		visitorsLocationsDetailsFragment.createNewsDetailsObject();
		LinearLayout childLayot = (LinearLayout) getActivity().findViewById(R.id.lnrScrollable);
		childLayot.setLayoutParams(new LinearLayout.LayoutParams(DataHolder.calculatedScreenResolution, LinearLayout.LayoutParams.FILL_PARENT));
		childLayot.setBackgroundResource(R.drawable.bg);

	}

	private void handleCommitteesDetailNewsList(String committeeName) {
		CommitteesDetailsNewsActivity committeesDetailsNewsActivity = (CommitteesDetailsNewsActivity) getActivity();

		CommitteesDatabaseAdapter committeesDatabaseAdapter = new CommitteesDatabaseAdapter(getActivity());
		committeesDatabaseAdapter.open();

		Cursor committeesNewsCursor = null;
		if (committeesDetailsNewsActivity.committeesIdString != null) {
			committeesNewsCursor = committeesDatabaseAdapter.fetchCommitteesNews(committeesDetailsNewsActivity.committeesIdString);
			committeesNewsCursor.moveToFirst();
		}

		getActivity().startManagingCursor(committeesNewsCursor);

		// If no news are present in a committee go to the Aufgaben
		if (committeesNewsCursor.getCount() == 0) {

			committeesDetailsNewsActivity.finish();
			committeesNewsCursor.close();

			// go to aufgaben
			Intent intent = new Intent();
			intent.setClass(getActivity(), CommitteesDetailsTasksActivity.class);
			intent.putExtra(CommitteesDetailsNewsDetailsActivity.KEY_COMMITTEE_ID, committeesDetailsNewsActivity.committeesIdString);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
		} else {
			setListAdapter(new CommitteesDetailsNewsListAdapter(getActivity(), committeesNewsCursor, committeeName));
		}

		DataHolder.RowDBIds = new ArrayList<Integer>();
		for (int i = 0; i < committeesNewsCursor.getCount(); i++) {
			DataHolder.RowDBIds.add(committeesNewsCursor.getInt(committeesNewsCursor.getColumnIndex(CommitteesDatabaseAdapter.KEY_NEWS_ROWID)));
			committeesNewsCursor.moveToNext();
		}
		committeeDetailsFragmentNews = (CommitteesDetailsNewsDetailsFragment) getFragmentManager().findFragmentById(R.id.detailsFragment);

		// if (DataHolder.rowDBSelectedIndex == 0) {

		committeeDetailsFragmentNews.setRowId(DataHolder.RowDBIds.get(DataHolder.committee_id));
		DataHolder.rowDBSelectedIndex = DataHolder.committee_id;
		DataHolder.lastRowDBId = DataHolder.RowDBIds.get(DataHolder.committee_id);
		// } else {
		// committeeDetailsFragmentNews.setRowId(DataHolder.lastRowDBId);
		// }

		committeeDetailsFragmentNews.createNewsDetailsObject();
		LinearLayout childLayot = (LinearLayout) getActivity().findViewById(R.id.lnrScrollable);
		childLayot.setLayoutParams(new LinearLayout.LayoutParams(DataHolder.calculatedScreenResolution, LinearLayout.LayoutParams.FILL_PARENT));
		childLayot.setBackgroundResource(R.drawable.bg);
	}

	private void resetVariables() {
		DataHolder._id = -1;
		/* id for detail list item */
		DataHolder._oldPosition = -1;
		/* id for sub list view */
		DataHolder._subId = -1;
		/* array for list detail item */
		DataHolder.RowDBIds = null;
		/* array index of row db ids. */
		// DataHolder.rowDBSelectedIndex = 0;
		DataHolder.isSublistLoaded = false;
		DataHolder.isLandscape = true;

		DataHolder.isOriantationChange = false;
		DataHolder.listFragmentWidth = -1;
		DataHolder.detailFragmentWidth = -1;
	}

	private List<String> getCalendar() {

		Calendar c = Calendar.getInstance();
		String month = ("" + (c.getTime().getMonth() + 1)).length() == 1 ? ("0" + (c.getTime().getMonth() + 1)) : ("" + (c.getTime().getMonth() + 1));
		String date = ("" + (c.getTime().getDate())).length() == 1 ? ("0" + (c.getTime().getDate())) : ("" + (c.getTime().getDate()));
		output.add("" + (c.getTime().getYear() + 1900) + "-" + month + "-" + date);
		for (int x = new Date().getDay(); x < (new Date().getDay() + 6); x++) {
			c.add(Calendar.DATE, 1);
			month = ("" + (c.getTime().getMonth() + 1)).length() == 1 ? ("0" + (c.getTime().getMonth() + 1)) : ("" + (c.getTime().getMonth() + 1));
			date = ("" + (c.getTime().getDate())).length() == 1 ? ("0" + (c.getTime().getDate())) : ("" + (c.getTime().getDate()));
			output.add("" + (c.getTime().getYear() + 1900) + "-" + month + "-" + date);
		}
		return output;
	}

}
