package de.bundestag.android.sections.news;

import android.database.Cursor;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v4.widget.CursorAdapter;
import android.view.View;
import android.widget.ListView;
import de.bundestag.android.R;
import de.bundestag.android.constant.DataHolder;
import de.bundestag.android.storage.NewsDatabaseAdapter;

public class NewsSubListFragment extends ListFragment {
	public static final String KEY_LIST_ID = "LIST_ID";
	private NewsDetailsFragment newsDetailsFragment = null;

	private int id;

	public void setId(int id) {
		this.id = id;
	}

	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		if (DataHolder.isOnline(getActivity())) {
			DataHolder.mLockScreenRotation(getActivity());
			CursorAdapter tmp = (CursorAdapter) getListAdapter();
			NewsListAdapter ntmp = (NewsListAdapter) tmp;
			Cursor c = (Cursor) ntmp.getItem(position);
			int rowId = c.getInt(c.getColumnIndex(NewsDatabaseAdapter.KEY_ROWID));
			newsDetailsFragment = (NewsDetailsFragment) getFragmentManager().findFragmentById(R.id.detailsFragment);
			newsDetailsFragment.setNewsId(rowId);
			newsDetailsFragment.setIsMasterList(false);
			newsDetailsFragment.createNewsDetailsObject();
			DataHolder._subId = rowId;
			DataHolder._oldPosition = DataHolder.rowDBSelectedIndex = position;
			// DataHolder.setListItemSelection(l, v);
		} else {
			DataHolder.alertboxOk(getActivity());
		}
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

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		NewsDatabaseAdapter visitorsDatabaseAdapter = new NewsDatabaseAdapter(getActivity());
		visitorsDatabaseAdapter.open();

		Cursor articlesCursor = visitorsDatabaseAdapter.fetchSubListNews(id);
		getActivity().startManagingCursor(articlesCursor);
		setListAdapter(new NewsListAdapter(getActivity(), articlesCursor));
	}
}
