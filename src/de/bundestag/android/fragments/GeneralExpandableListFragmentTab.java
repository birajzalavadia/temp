package de.bundestag.android.fragments;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.LinearLayout;
import de.bundestag.android.R;
import de.bundestag.android.constant.DataHolder;
import de.bundestag.android.sections.members.ExpandableListAdapter;
import de.bundestag.android.sections.members.ExpandableListFragment;
import de.bundestag.android.sections.members.MembersDetailsExpandable;
import de.bundestag.android.sections.members.MembersDetailsFragment;
import de.bundestag.android.sections.members.MembersSubpageMembersActivity;
import de.bundestag.android.sections.members.MembersSubpageViewHelper;
import de.bundestag.android.sections.news.NewsDetailsFragment;
import de.bundestag.android.storage.MembersDatabaseAdapter;
import de.bundestag.android.storage.NewsDatabaseAdapter;

/**
 * General list fragment class that holds the list. This is a fragment so it can
 * be used together with another fragment inside the same activity.
 * 
 * See http://developer.android.com/guide/topics/fundamentals/fragments.html
 */
public class GeneralExpandableListFragmentTab extends ExpandableListFragment {
	int mCurCheckPosition = 0;
	NewsDetailsFragment newsDetailsFragment = null;
	MembersDetailsFragment membersDetailsFragment = null;
	static boolean isFirstTime = true;
	private ExpandableListAdapter adapter;
	ArrayList<HashMap<Integer, MembersDetailsExpandable>> memberList = new ArrayList<HashMap<Integer, MembersDetailsExpandable>>();

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		Activity activity = getActivity();
		mList.setOnChildClickListener(new OnChildClickListener() {
			public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
				// some calculations here....
				DataHolder.rowDBSelectedIndex = childPosition;
				membersDetailsFragment.setSelectedID(Integer.parseInt(memberList.get(groupPosition).get(childPosition).getKey_rowId()));
				membersDetailsFragment.createMembersDetailsObject();
				return false;
			}
		});
		mList.setOnGroupClickListener(new OnGroupClickListener() {

			@Override
			public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
				// TODO Auto-generated method stub
				DataHolder.rowDBSelectedIndex = 0;

				DataHolder.RowDBIds = DataHolder.RowDBIdsFraction.get(groupPosition);
				membersDetailsFragment.setSelectedID(DataHolder.RowDBIds.get(0));
				membersDetailsFragment.createMembersDetailsObject();
				return false;
			}
		});
		handleMembersSubpage();

	}

	private void handleMembersSubpage() {
		ArrayList<HashMap<String, Object>> fractions = MembersSubpageViewHelper.createMembersFractions(getActivity());

		Cursor membersCursor = null;
		DataHolder.RowDBIdsFraction = new HashMap<Integer, ArrayList<Integer>>();

		for (int i = 0; i < fractions.size(); i++) {
			ArrayList values = MembersSubpageViewHelper.createMembersSubpage(getActivity(), MembersSubpageMembersActivity.SUB_PAGE_FRACTION, i);
			membersCursor = (Cursor) values.get(0);
			String selected = (String) values.get(1);

			HashMap<Integer, MembersDetailsExpandable> members_list = new LinkedHashMap<Integer, MembersDetailsExpandable>();
			membersCursor.moveToFirst();
			DataHolder.RowDBIds = new ArrayList<Integer>();
			for (int j = 0; j < membersCursor.getCount(); j++) {
				MembersDetailsExpandable tempExp = new MembersDetailsExpandable();

				tempExp.setKey_rowId(membersCursor.getString(membersCursor.getColumnIndex(MembersDatabaseAdapter.KEY_ROWID)));
				tempExp.setKey_id(membersCursor.getString(membersCursor.getColumnIndex(MembersDatabaseAdapter.KEY_ID)));
				tempExp.setKey_name(membersCursor.getString(membersCursor.getColumnIndex(MembersDatabaseAdapter.KEY_NAME)));
				tempExp.setKey_details_fraction(membersCursor.getString(membersCursor.getColumnIndex(MembersDatabaseAdapter.KEY_DETAILS_FRACTION)));
				tempExp.setKey_land(membersCursor.getString(membersCursor.getColumnIndex(MembersDatabaseAdapter.KEY_LAND)));
				members_list.put(j, tempExp);
				tempExp = null;

				DataHolder.RowDBIds.add(membersCursor.getInt(membersCursor.getColumnIndex(NewsDatabaseAdapter.KEY_ROWID)));

				membersCursor.moveToNext();
			}
			DataHolder.RowDBIdsFraction.put(i, DataHolder.RowDBIds);
			memberList.add(members_list);
		}

		DataHolder.RowDBIds = DataHolder.RowDBIdsFraction.get(0);

		adapter = new ExpandableListAdapter(getActivity(), fractions, getActivity(), memberList);
		LinearLayout childLayot = (LinearLayout) getActivity().findViewById(R.id.lnrScrollable);
		childLayot.setLayoutParams(new LinearLayout.LayoutParams(DataHolder.calculatedScreenResolution, LinearLayout.LayoutParams.FILL_PARENT));
		childLayot.setBackgroundResource(R.drawable.bg);
		// this.getListView().addHeaderView(valueTV);///neeraj
		setListAdapter(adapter);

		membersDetailsFragment = (MembersDetailsFragment) getFragmentManager().findFragmentById(R.id.detailsFragment);
		membersDetailsFragment.setSelectedID(DataHolder.RowDBIds.get(0));
		DataHolder.rowDBSelectedIndex = 0;
		membersDetailsFragment.createMembersDetailsObject();

	}

	@Override
	protected void finalize() throws Throwable {
		super.finalize();
		newsDetailsFragment = null;
		membersDetailsFragment = null;
		isFirstTime = true;
		adapter = null;
		memberList = null;
	}

}
