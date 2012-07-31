package de.bundestag.android.sections.members;

import android.database.Cursor;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import de.bundestag.android.R;
import de.bundestag.android.constant.DataHolder;
import de.bundestag.android.customviews.CustomScrollView;
import de.bundestag.android.parser.objects.MembersDetailsObject;
import de.bundestag.android.storage.MembersDatabaseAdapter;

public class MembersDetailsBiographyFragment extends MembersFragmentBase {

	private View view = null;
	public int selectId = 1;
	private GestureDetector detector;
	private LinearLayout lnr;
	private static final int SWIPE_MIN_DISTANCE = 100;
	private static final int SWIPE_THRESHOLD_VELOCITY = 200;
	private CustomScrollView scrollView;

	public MembersDetailsBiographyFragment() {

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.members_detail_biography, container, false);

		return view;
	}

	private void initComponent() {
		detector = new GestureDetector(getActivity(), new MyGestureDetector(), null, false);

		scrollView = (CustomScrollView) view.findViewById(R.id.scrview);
		scrollView.fullScroll(ScrollView.FOCUS_UP);

		scrollView.setGestureDetector(detector);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		initComponent();
		createMembersDetailsObject();

		super.onActivityCreated(savedInstanceState);
	}

	public void setSelectedID(int selectId) {
		this.selectId = selectId;
	}

	public MembersDetailsObject createMembersDetailsObject() {
		// initComponent();
		MembersDetailsObject membersDetailsObject = new MembersDetailsObject();
		try {
			MembersDatabaseAdapter membersDatabaseAdapter = new MembersDatabaseAdapter(getActivity());
			membersDatabaseAdapter.open();

			Cursor memberCursor = membersDatabaseAdapter.fetchMembers(selectId);

			membersDetailsObject.setMediaPhoto(memberCursor.getString(memberCursor.getColumnIndex(MembersDatabaseAdapter.KEY_DETAILS_MEDIAPHOTO)));
			membersDetailsObject.setMediaPhotoImageString(memberCursor.getString(memberCursor.getColumnIndex(MembersDatabaseAdapter.KEY_DETAILS_MEDIAPHOTOSTRING)));
			membersDetailsObject.setMediaPhotoCopyright(memberCursor.getString(memberCursor.getColumnIndex(MembersDatabaseAdapter.KEY_DETAILS_MEDIAPHOTOCOPYRIGHT)));

			membersDetailsObject.setCourse(memberCursor.getString(memberCursor.getColumnIndex(MembersDatabaseAdapter.KEY_DETAILS_COURSE)));
			membersDetailsObject.setFirstName(memberCursor.getString(memberCursor.getColumnIndex(MembersDatabaseAdapter.KEY_DETAILS_FIRSTNAME)));
			membersDetailsObject.setTitle(memberCursor.getString(memberCursor.getColumnIndex(MembersDatabaseAdapter.KEY_DETAILS_TITLE)));
			membersDetailsObject.setLastName(memberCursor.getString(memberCursor.getColumnIndex(MembersDatabaseAdapter.KEY_DETAILS_LASTNAME)));

			membersDetailsObject.setInformation(memberCursor.getString(memberCursor.getColumnIndex(MembersDatabaseAdapter.KEY_DETAILS_INFORMATION)));

			membersDetailsObject.setFraction(memberCursor.getString(memberCursor.getColumnIndex(MembersDatabaseAdapter.KEY_DETAILS_FRACTION)));
			membersDetailsObject.setProfession(memberCursor.getString(memberCursor.getColumnIndex(MembersDatabaseAdapter.KEY_DETAILS_PROFESSION)));

			membersDetailsObject.setStatus(memberCursor.getString(memberCursor.getColumnIndex(MembersDatabaseAdapter.KEY_DETAILS_STATUS)));
			membersDetailsObject.setExitDate(memberCursor.getString(memberCursor.getColumnIndex(MembersDatabaseAdapter.KEY_DETAILS_EXITDATE)));
			membersDetailsObject.setElected(memberCursor.getString(memberCursor.getColumnIndex(MembersDatabaseAdapter.KEY_DETAILS_ELECTED)));
			membersDetailsObject.setCity(memberCursor.getString(memberCursor.getColumnIndex(MembersDatabaseAdapter.KEY_DETAILS_CITY)));
			membersDetailsObject.setElectionName(memberCursor.getString(memberCursor.getColumnIndex(MembersDatabaseAdapter.KEY_DETAILS_ELECTIONNAME)));

			memberCursor.close();
			membersDatabaseAdapter.close();
			MembersDetailsBiographyFragmentViewHelper.createDetailsView(membersDetailsObject, view, getActivity());
			DataHolder.releaseScreenLock(this.getActivity());
		} catch (Exception e) {

		}
		return membersDetailsObject;
	}

	private class MyGestureDetector extends SimpleOnGestureListener {
		@Override
		public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {

			System.out.println("OnFling called");

			if (e1.getX() - e2.getX() > DataHolder.SWIPE_MIN_DISTANCE && Math.abs(velocityX) > DataHolder.SWIPE_THRESHOLD_VELOCITY) {
				DataHolder.currentFragmentIndex++;
				checkIndex(DataHolder.currentFragmentIndex, selectId);

				return false;// right to Left
			} else if (e2.getX() - e1.getX() > DataHolder.SWIPE_MIN_DISTANCE && Math.abs(velocityX) > DataHolder.SWIPE_THRESHOLD_VELOCITY) {
				DataHolder.currentFragmentIndex--;

				checkIndex(DataHolder.currentFragmentIndex, selectId);

				return false; // Left to right
			} else {
				scrollView.setEnabled(true);
				scrollView.setSmoothScrollingEnabled(true);
				return false;
			}
		}

	}

	@Override
	protected void finalize() throws Throwable {
		super.finalize();
		view = null;
		detector = null;
		lnr = null;
		scrollView = null;
	}
}