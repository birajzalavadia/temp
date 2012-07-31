package de.bundestag.android.sections.committees;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v4.widget.CursorAdapter;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.ListView;
import android.widget.ScrollView;
import de.bundestag.android.constant.DataHolder;
import de.bundestag.android.storage.CommitteesDatabaseAdapter;

public class CommitteesSubListFragment extends ListFragment {
	public static final String KEY_LIST_ID = "LIST_ID";

	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		// call another activity with list item
		Intent intent = new Intent();
		intent.setClass(getActivity(), CommitteesDetailsNewsActivity.class);
		CursorAdapter tmp = (CursorAdapter) getListAdapter();
		CommitteesDetailsNewsListAdapter ntmp = (CommitteesDetailsNewsListAdapter) tmp;
		Cursor c = (Cursor) ntmp.getItem(position);
		String committeeId = c.getString(c.getColumnIndex(CommitteesDatabaseAdapter.KEY_ID));
		intent.putExtra(CommitteesDetailsNewsDetailsActivity.KEY_COMMITTEE_ID, committeeId);
		DataHolder.committee_id = position;
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(intent);
		getActivity().overridePendingTransition(0, 0);

	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		getListView().setFastScrollEnabled(true);
		ColorDrawable line_color = new ColorDrawable(0xFF8fa3a7);
		getListView().setDivider(line_color);
		getListView().setDividerHeight(1);
		getListView().setCacheColorHint(0x00000000);
		getListView().setDivider(null);
		getListView().setDividerHeight(0);
		getListView().setFastScrollEnabled(false);
	}

	public void createSubList(String committeeString) {
		CommitteesDatabaseAdapter committeesDatabaseAdapter = new CommitteesDatabaseAdapter(getActivity());
		committeesDatabaseAdapter.open();
		Cursor articlesCursor = committeesDatabaseAdapter.fetchCommitteesNews(committeeString);
		getActivity().startManagingCursor(articlesCursor);
		setListAdapter(new CommitteesDetailsNewsListAdapter(getActivity(), articlesCursor, "Test"));

		final HorizontalScrollView s = (HorizontalScrollView) getActivity().findViewById(108);
		s.postDelayed(new Runnable() {

			@Override
			public void run() {
				s.fullScroll(ScrollView.FOCUS_RIGHT);
			}
		}, 2000);

	}
}
