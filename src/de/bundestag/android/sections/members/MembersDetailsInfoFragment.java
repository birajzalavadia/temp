package de.bundestag.android.sections.members;

import android.database.Cursor;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;
import de.bundestag.android.R;
import de.bundestag.android.constant.DataHolder;
import de.bundestag.android.customviews.CustomScrollView;
import de.bundestag.android.parser.objects.MembersDetailsObject;
import de.bundestag.android.storage.MembersDatabaseAdapter;

public class MembersDetailsInfoFragment extends MembersFragmentBase {

	private View view = null;
	public int selectId = 1;
	private GestureDetector detector;

	private CustomScrollView scrollView;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.members_detail_info, container, false);
		// detector = new GestureDetector(getActivity(), new
		// MyGestureDetector());
		// createMembersDetailsObject();
		return view;
	}

	private void initComponent() {
		detector = new GestureDetector(getActivity(), new MyGestureDetector(), null, false);

		scrollView = (CustomScrollView) view.findViewById(R.id.scrview);
		scrollView.fullScroll(ScrollView.FOCUS_UP);
		// scrollView.setEnabled(false);
		scrollView.setSmoothScrollingEnabled(true);

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

	private MembersDetailsObject createMembersDetailsObject() {
		MembersDetailsObject membersDetailsObject = new MembersDetailsObject();
		try{
		MembersDatabaseAdapter membersDatabaseAdapter = new MembersDatabaseAdapter(getActivity());
		membersDatabaseAdapter.open();

		Cursor memberCursor = membersDatabaseAdapter.fetchMembers(selectId);

//		MembersDetailsObject membersDetailsObject = new MembersDetailsObject();

		membersDetailsObject.setMediaPhoto(memberCursor.getString(memberCursor.getColumnIndex(MembersDatabaseAdapter.KEY_DETAILS_MEDIAPHOTO)));
		membersDetailsObject.setMediaPhotoImageString(memberCursor.getString(memberCursor.getColumnIndex(MembersDatabaseAdapter.KEY_DETAILS_MEDIAPHOTOSTRING)));
		membersDetailsObject.setMediaPhotoImageString(memberCursor.getString(memberCursor.getColumnIndex(MembersDatabaseAdapter.KEY_DETAILS_MEDIAPHOTOSTRING)));
		membersDetailsObject.setMediaPhotoCopyright(memberCursor.getString(memberCursor.getColumnIndex(MembersDatabaseAdapter.KEY_DETAILS_MEDIAPHOTOCOPYRIGHT)));

		membersDetailsObject.setCourse(memberCursor.getString(memberCursor.getColumnIndex(MembersDatabaseAdapter.KEY_DETAILS_COURSE)));
		membersDetailsObject.setFirstName(memberCursor.getString(memberCursor.getColumnIndex(MembersDatabaseAdapter.KEY_DETAILS_FIRSTNAME)));
		membersDetailsObject.setTitle(memberCursor.getString(memberCursor.getColumnIndex(MembersDatabaseAdapter.KEY_DETAILS_TITLE)));
		membersDetailsObject.setLastName(memberCursor.getString(memberCursor.getColumnIndex(MembersDatabaseAdapter.KEY_DETAILS_LASTNAME)));

		membersDetailsObject.setFraction(memberCursor.getString(memberCursor.getColumnIndex(MembersDatabaseAdapter.KEY_DETAILS_FRACTION)));
		membersDetailsObject.setProfession(memberCursor.getString(memberCursor.getColumnIndex(MembersDatabaseAdapter.KEY_DETAILS_PROFESSION)));

		membersDetailsObject.setStatus(memberCursor.getString(memberCursor.getColumnIndex(MembersDatabaseAdapter.KEY_DETAILS_STATUS)));
		membersDetailsObject.setExitDate(memberCursor.getString(memberCursor.getColumnIndex(MembersDatabaseAdapter.KEY_DETAILS_EXITDATE)));
		membersDetailsObject.setElected(memberCursor.getString(memberCursor.getColumnIndex(MembersDatabaseAdapter.KEY_DETAILS_ELECTED)));
		membersDetailsObject.setCity(memberCursor.getString(memberCursor.getColumnIndex(MembersDatabaseAdapter.KEY_DETAILS_CITY)));
		membersDetailsObject.setElectionName(memberCursor.getString(memberCursor.getColumnIndex(MembersDatabaseAdapter.KEY_DETAILS_ELECTIONNAME)));

		membersDetailsObject.setDisclosureInformation(memberCursor.getString(memberCursor.getColumnIndex(MembersDatabaseAdapter.KEY_DETAILS_DISCLOSUREINFORMATION)));

		memberCursor.close();
		membersDatabaseAdapter.close();
		MembersDetailsInfoFragmentViewHelper.createDetailsView(membersDetailsObject, getActivity());
		DataHolder.releaseScreenLock(this.getActivity());
		}catch(Exception e){
			
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

				return true;// right to Left
			} else if (e2.getX() - e1.getX() > DataHolder.SWIPE_MIN_DISTANCE && Math.abs(velocityX) > DataHolder.SWIPE_THRESHOLD_VELOCITY) {
				DataHolder.currentFragmentIndex--;

				checkIndex(DataHolder.currentFragmentIndex, selectId);

				return true; // Left to right
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
		scrollView = null;
		
	}
}