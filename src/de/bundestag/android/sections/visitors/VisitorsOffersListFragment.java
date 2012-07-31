package de.bundestag.android.sections.visitors;

import android.database.Cursor;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v4.widget.CursorAdapter;
import android.view.View;
import android.widget.ListView;
import de.bundestag.android.R;
import de.bundestag.android.constant.DataHolder;
import de.bundestag.android.storage.VisitorsDatabaseAdapter;

public class VisitorsOffersListFragment extends ListFragment {
	public static final String KEY_LIST_ID = "LIST_ID";
	private VisitorsOffersDetailsFragment visitorsOffersDetailsFragment = null;

	private int id;

	public void setId(int id) {
		this.id = id;
	}

	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		// TODO Auto-generated method stub
		super.onListItemClick(l, v, position, id);
		if (DataHolder.isOnline(this.getActivity())) {
			CursorAdapter tmp = (CursorAdapter) getListAdapter();
			VisitorsListAdapter ntmp = (VisitorsListAdapter) tmp;
			Cursor c = (Cursor) ntmp.getItem(position);
			int rowId = c.getInt(c.getColumnIndex(VisitorsDatabaseAdapter.KEY_ROWID));
			visitorsOffersDetailsFragment = (VisitorsOffersDetailsFragment) getFragmentManager().findFragmentById(R.id.detailsFragment);
			visitorsOffersDetailsFragment.setVisitorsId(rowId);
			visitorsOffersDetailsFragment.createVisitorsDetailsObject();
			DataHolder._subId = rowId;
			for (int i = 0; i < DataHolder.RowDBIds.size(); i++) {
				if (DataHolder.RowDBIds.get(i) == DataHolder._id) {
					DataHolder.rowDBSelectedIndex = i;
				}
			}
		} else {
			DataHolder.alertboxOk(getActivity());
		}
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onViewCreated(view, savedInstanceState);
		getListView().setFastScrollEnabled(true);
		ColorDrawable line_color = new ColorDrawable(0xFF8fa3a7);
		getListView().setDivider(line_color);
		getListView().setDividerHeight(1);
		getListView().setCacheColorHint(0x00000000);
		getListView().setFastScrollEnabled(false);

	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub

		super.onActivityCreated(savedInstanceState);
		VisitorsDatabaseAdapter visitorsDatabaseAdapter = new VisitorsDatabaseAdapter(getActivity());
		visitorsDatabaseAdapter.open();

		Cursor articlesCursor = visitorsDatabaseAdapter.fetchArticleList(id);
		getActivity().startManagingCursor(articlesCursor);
		setListAdapter(new VisitorsListAdapter(getActivity(), articlesCursor, visitorsDatabaseAdapter));

	}
}
