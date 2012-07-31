package de.bundestag.android.sections.members;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import de.bundestag.android.BaseFragment;
import de.bundestag.android.R;
import de.bundestag.android.constant.DataHolder;
import de.bundestag.android.parser.objects.MembersDetailsObject;
import de.bundestag.android.sections.committees.CommitteesDetailsMembersByFractionActivity;
import de.bundestag.android.storage.MembersDatabaseAdapter;

public class MembersDetailsFragment extends BaseFragment implements OnTouchListener {
	private GestureDetector detector;
	private View view = null;
	public int selectId = 0;

	LinearLayout lnr = null;
	private ScrollView scrollView = null;
	private ImageView base_fragment = null;
	private boolean isBaseFragmentClick = false;
	// private int buttonIndex = 0;
	LinearLayout subMenu1Tab = null;
	LinearLayout subMenu2Tab = null;
	LinearLayout subMenu3Tab = null;
	LinearLayout subMenu4Tab = null;

	// LinearLayout lnrSubFling=null;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.members_detail_item, container, false);
		initComponents();
		final LinearLayout subMenu1Tab = (LinearLayout) view.findViewById(R.id.subMenuHolder1Tab);
		final LinearLayout subMenu2Tab = (LinearLayout) view.findViewById(R.id.subMenuHolder2Tab);
		final LinearLayout subMenu3Tab = (LinearLayout) view.findViewById(R.id.subMenuHolder3Tab);
		final LinearLayout subMenu4Tab = (LinearLayout) view.findViewById(R.id.subMenuHolder4Tab);

		subMenu1Tab.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				subMenu1Tab.setBackgroundResource(R.drawable.top_navigation_gradient_active);
				subMenu2Tab.setBackgroundResource(R.drawable.top_navigation_gradient);
				subMenu3Tab.setBackgroundResource(R.drawable.top_navigation_gradient);
				subMenu4Tab.setBackgroundResource(R.drawable.top_navigation_gradient);

				FragmentManager fragmentManager = getFragmentManager();
				FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction(); // add
																								// a
				// fragment
				MembersDetailsBiographyFragment myFragment = new MembersDetailsBiographyFragment();
				myFragment.setSelectedID(selectId);
				try {
					fragmentTransaction.replace(R.id.base_fragment, myFragment);
					fragmentTransaction.commit();
				} catch (Exception e) {

				}

				DataHolder.currentFragmentIndex = 0;
				// initComponents();

			}
		});

		subMenu2Tab.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				subMenu2Tab.setBackgroundResource(R.drawable.top_navigation_gradient_active);
				subMenu1Tab.setBackgroundResource(R.drawable.top_navigation_gradient);
				subMenu3Tab.setBackgroundResource(R.drawable.top_navigation_gradient);
				subMenu4Tab.setBackgroundResource(R.drawable.top_navigation_gradient);
				FragmentManager fragmentManager = getFragmentManager();
				FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction(); // add
																								// a
																								// fragment
				MembersDetailsInfoFragment myFragment = new MembersDetailsInfoFragment();
				myFragment.setSelectedID(selectId);
				try {
					fragmentTransaction.replace(R.id.base_fragment, myFragment);// ,
					fragmentTransaction.commit();
				} catch (Exception e) {

				}
				DataHolder.currentFragmentIndex = 1;

			}
		});

		subMenu3Tab.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				subMenu3Tab.setBackgroundResource(R.drawable.top_navigation_gradient_active);
				subMenu2Tab.setBackgroundResource(R.drawable.top_navigation_gradient);
				subMenu1Tab.setBackgroundResource(R.drawable.top_navigation_gradient);
				subMenu4Tab.setBackgroundResource(R.drawable.top_navigation_gradient);
				FragmentManager fragmentManager = getFragmentManager();
				FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction(); // add
																								// a
																								// fragment
				MembersDetailsCommitteesFragment myFragment = new MembersDetailsCommitteesFragment();
				myFragment.setSelectedID(selectId);
				try {
					fragmentTransaction.replace(R.id.base_fragment, myFragment);
					fragmentTransaction.commit();
				} catch (Exception e) {

				}
				DataHolder.currentFragmentIndex = 2;

			}
		});

		subMenu4Tab.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				subMenu4Tab.setBackgroundResource(R.drawable.top_navigation_gradient_active);
				subMenu2Tab.setBackgroundResource(R.drawable.top_navigation_gradient);
				subMenu3Tab.setBackgroundResource(R.drawable.top_navigation_gradient);
				subMenu1Tab.setBackgroundResource(R.drawable.top_navigation_gradient);
				FragmentManager fragmentManager = getFragmentManager();
				FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction(); // add
																								// a
																								// fragment
				MembersDetailsContactFragment myFragment = new MembersDetailsContactFragment();
				myFragment.setSelectedID(selectId);
				try {

					fragmentTransaction.replace(R.id.base_fragment, myFragment);
					fragmentTransaction.commit();
				} catch (Exception e) {

				}
				DataHolder.currentFragmentIndex = 3;

			}
		});

		subMenu1Tab.setBackgroundResource(R.drawable.top_navigation_gradient_active);
		return view;
	}

	public void initComponents() {
		detector = new GestureDetector(getActivity(), new MyGestureDetector(), null, false);
		lnr = (LinearLayout) view.findViewById(R.id.lnrFling);
		lnr.setOnTouchListener(this);
		// scrollView = (ScrollView) view.findViewById(R.id.scrview);
		// scrollView.setEnabled(false);
		// base_fragment = (ImageView) view.findViewById(R.id.parent_fragment);
		// base_fragment.setOnTouchListener(this);
		// base_fragment.setTag("basefragment");
		// base_fragment.setVisibility(View.VISIBLE);

		subMenu1Tab = (LinearLayout) view.findViewById(R.id.subMenuHolder1Tab);
		subMenu2Tab = (LinearLayout) view.findViewById(R.id.subMenuHolder2Tab);
		subMenu3Tab = (LinearLayout) view.findViewById(R.id.subMenuHolder3Tab);
		subMenu4Tab = (LinearLayout) view.findViewById(R.id.subMenuHolder4Tab);

		DataHolder.currentFragmentIndex = 0;
		MembersFragmentBase.viewCreated = new OnMyViewCreated() {

			@Override
			public void onFling(int index) {
				setFragment(index);
			}
		};
	}

	public void setSelectedID(int selectId) {
		this.selectId = selectId;
	}

	public MembersDetailsObject createMembersDetailsObject() {
		MembersDatabaseAdapter membersDatabaseAdapter = new MembersDatabaseAdapter(getActivity());
		membersDatabaseAdapter.open();

		Cursor memberCursor = membersDatabaseAdapter.fetchMembers(selectId);

		MembersDetailsObject membersDetailsObject = new MembersDetailsObject();

		membersDetailsObject.setMediaPhoto(memberCursor.getString(memberCursor.getColumnIndex(MembersDatabaseAdapter.KEY_DETAILS_MEDIAPHOTO)));
		membersDetailsObject.setMediaPhotoImageString(memberCursor.getString(memberCursor.getColumnIndex(MembersDatabaseAdapter.KEY_DETAILS_MEDIAPHOTOSTRING)));
		membersDetailsObject.setMediaPhotoCopyright(memberCursor.getString(memberCursor.getColumnIndex(MembersDatabaseAdapter.KEY_DETAILS_MEDIAPHOTOCOPYRIGHT)));
		membersDetailsObject.setFraction(memberCursor.getString(memberCursor.getColumnIndex(MembersDatabaseAdapter.KEY_DETAILS_FRACTION)));
		membersDetailsObject.setCity(memberCursor.getString(memberCursor.getColumnIndex(MembersDatabaseAdapter.KEY_LAND)));
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

		memberCursor.close();

		Cursor memberGroupsCursor = membersDatabaseAdapter.getGroups(selectId);

		memberGroupsCursor.close();

		membersDatabaseAdapter.close();
		MembersDetailsFragmentViewHelper.createDetailsView(membersDetailsObject, getActivity(), getMemberGroups(selectId));
		FragmentManager fragmentManager = getFragmentManager();// getSupportFragmentManager();
		FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

		MembersDetailsBiographyFragment myFragment = new MembersDetailsBiographyFragment();
		myFragment.setSelectedID(selectId);
		try {
			fragmentTransaction.replace(R.id.base_fragment, myFragment);
			fragmentTransaction.commit();
		} catch (Exception e) {

		}
		// base_fragment.setOnTouchListener(myFragment);
		subMenu1Tab.setBackgroundResource(R.drawable.top_navigation_gradient_active);
		subMenu2Tab.setBackgroundResource(R.drawable.top_navigation_gradient);
		subMenu3Tab.setBackgroundResource(R.drawable.top_navigation_gradient);
		subMenu4Tab.setBackgroundResource(R.drawable.top_navigation_gradient);
		DataHolder.currentFragmentIndex = 0;

		DataHolder.releaseScreenLock(this.getActivity());
		return membersDetailsObject;
	}

	private int getMemberGroups(int memberId) {
		MembersDatabaseAdapter membersDatabaseAdapter = new MembersDatabaseAdapter(getActivity());
		membersDatabaseAdapter.open();

		Cursor memberGroupsCursor = membersDatabaseAdapter.getGroups(memberId);

		int groupsCount = memberGroupsCursor.getCount();

		memberGroupsCursor.close();

		membersDatabaseAdapter.close();

		return groupsCount;
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		// if (v.getTag() != null) {
		// if (v.getTag().toString().equalsIgnoreCase("basefragment")) {
		// isBaseFragmentClick = true;
		// }
		// }

		detector.onTouchEvent(event);
		// base_fragment.setVisibility(View.VISIBLE);
		// scrollView.setEnabled(false);

		return true;
	}

	private class MyGestureDetector extends SimpleOnGestureListener {
		@Override
		public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {

			if (e1.getX() - e2.getX() > DataHolder.SWIPE_MIN_DISTANCE && Math.abs(velocityX) > DataHolder.SWIPE_THRESHOLD_VELOCITY) {

				subMenu1Tab.setBackgroundResource(R.drawable.top_navigation_gradient_active);
				subMenu2Tab.setBackgroundResource(R.drawable.top_navigation_gradient);
				subMenu3Tab.setBackgroundResource(R.drawable.top_navigation_gradient);
				subMenu4Tab.setBackgroundResource(R.drawable.top_navigation_gradient);

				DataHolder.rowDBSelectedIndex++;
				if (getActivity() instanceof CommitteesDetailsMembersByFractionActivity) {

					try {
						if (DataHolder.RowDBIds.get(DataHolder.rowDBSelectedIndex) == -1) {
							DataHolder.rowDBSelectedIndex++;
						}
					} catch (Exception e) {
						DataHolder.rowDBSelectedIndex = 1;
					}
				}
				checkIndex(false);

				return true;// right to Left
			} else if (e2.getX() - e1.getX() > DataHolder.SWIPE_MIN_DISTANCE && Math.abs(velocityX) > DataHolder.SWIPE_THRESHOLD_VELOCITY) {

				subMenu1Tab.setBackgroundResource(R.drawable.top_navigation_gradient_active);
				subMenu2Tab.setBackgroundResource(R.drawable.top_navigation_gradient);
				subMenu3Tab.setBackgroundResource(R.drawable.top_navigation_gradient);
				subMenu4Tab.setBackgroundResource(R.drawable.top_navigation_gradient);
				DataHolder.rowDBSelectedIndex--;

				if (getActivity() instanceof CommitteesDetailsMembersByFractionActivity) {

					try {

						if (DataHolder.RowDBIds.get(DataHolder.rowDBSelectedIndex) == -1) {
							DataHolder.rowDBSelectedIndex--;
						}
					} catch (Exception e) {

					}
				}

				checkIndex(true);

				return true; // Left to right
			} else {
				// scrollView.setEnabled(true);
				// scrollView.setSmoothScrollingEnabled(true);
			}

			return false;
		}
	}

	private void checkIndex(boolean isLeftToRight) {

		if (DataHolder.rowDBSelectedIndex > (DataHolder.RowDBIds.size() - 1)) {
			DataHolder.rowDBSelectedIndex = 0;
		} else if (DataHolder.rowDBSelectedIndex < 0) {
			DataHolder.rowDBSelectedIndex = DataHolder.RowDBIds.size() - 1;
		}

		DataHolder._oldPosition = DataHolder.rowDBSelectedIndex;
		setSelectedID(DataHolder.RowDBIds.get(DataHolder.rowDBSelectedIndex));
		DataHolder.lastRowDBId = DataHolder.RowDBIds.get(DataHolder.rowDBSelectedIndex);
		createMembersDetailsObject();
	}

	private void setFragment(int index) {
		FragmentManager fragmentManager = getFragmentManager();
		FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction(); // add
		// initComponents();

		DataHolder.currentFragmentIndex = index;
		switch (index) {
		case 0:
			subMenu1Tab.setBackgroundResource(R.drawable.top_navigation_gradient_active);
			subMenu2Tab.setBackgroundResource(R.drawable.top_navigation_gradient);
			subMenu3Tab.setBackgroundResource(R.drawable.top_navigation_gradient);
			subMenu4Tab.setBackgroundResource(R.drawable.top_navigation_gradient);
			MembersDetailsBiographyFragment myFragmentBio = null;
			myFragmentBio = new MembersDetailsBiographyFragment();
			myFragmentBio.setSelectedID(selectId);
			try {

				fragmentTransaction.replace(R.id.base_fragment, myFragmentBio);
				fragmentTransaction.commit();
			} catch (Exception e) {

			}
			break;
		case 1:
			// fragment
			subMenu2Tab.setBackgroundResource(R.drawable.top_navigation_gradient_active);
			subMenu1Tab.setBackgroundResource(R.drawable.top_navigation_gradient);
			subMenu3Tab.setBackgroundResource(R.drawable.top_navigation_gradient);
			subMenu4Tab.setBackgroundResource(R.drawable.top_navigation_gradient);
			MembersDetailsInfoFragment myFragmentInfo = null;
			myFragmentInfo = new MembersDetailsInfoFragment();
			myFragmentInfo.setSelectedID(selectId);
			try {
				fragmentTransaction.replace(R.id.base_fragment, myFragmentInfo);
				fragmentTransaction.commit();
			} catch (Exception e) {

			}
			break;
		case 2:

			if (DataHolder.noOfSubTab == 2) {
				subMenu4Tab.setBackgroundResource(R.drawable.top_navigation_gradient_active);
				subMenu2Tab.setBackgroundResource(R.drawable.top_navigation_gradient);
				subMenu3Tab.setBackgroundResource(R.drawable.top_navigation_gradient);
				subMenu1Tab.setBackgroundResource(R.drawable.top_navigation_gradient);
				MembersDetailsContactFragment myFragmentContact = new MembersDetailsContactFragment();
				myFragmentContact.setSelectedID(selectId);
				try {

					fragmentTransaction.replace(R.id.base_fragment, myFragmentContact);

					fragmentTransaction.commit();
				} catch (Exception e) {

				}
			} else {
				subMenu3Tab.setBackgroundResource(R.drawable.top_navigation_gradient_active);
				subMenu2Tab.setBackgroundResource(R.drawable.top_navigation_gradient);
				subMenu1Tab.setBackgroundResource(R.drawable.top_navigation_gradient);
				subMenu4Tab.setBackgroundResource(R.drawable.top_navigation_gradient);
				MembersDetailsCommitteesFragment myFragmentCommittee = null;
				myFragmentCommittee = new MembersDetailsCommitteesFragment();
				myFragmentCommittee.setSelectedID(selectId);
				try {

					fragmentTransaction.replace(R.id.base_fragment, myFragmentCommittee);

					fragmentTransaction.commit();
				} catch (Exception e) {

				}
			}

			break;
		case 3:
			subMenu4Tab.setBackgroundResource(R.drawable.top_navigation_gradient_active);
			subMenu2Tab.setBackgroundResource(R.drawable.top_navigation_gradient);
			subMenu3Tab.setBackgroundResource(R.drawable.top_navigation_gradient);
			subMenu1Tab.setBackgroundResource(R.drawable.top_navigation_gradient);

			MembersDetailsContactFragment myFragmentContact = new MembersDetailsContactFragment();
			myFragmentContact.setSelectedID(selectId);
			try {
				fragmentTransaction.replace(R.id.base_fragment, myFragmentContact);

				fragmentTransaction.commit();
			} catch (Exception e) {

			}
			break;

		default:
			break;
		}
	}

	public void alertboxOk(Context c, String title, String mymessage) {
		AlertDialog.Builder altDialog = new AlertDialog.Builder(c);
		altDialog.setMessage("Currently Blocked"); // here add your message
		altDialog.setNeutralButton("OK", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub

			}
		});
		altDialog.show();

		// new AlertDialog.Builder(c).setMessage(mymessage).setTitle(title)
		// .setCancelable(true).setNeutralButton(android.R.string.ok,
		// new DialogInterface.OnClickListener() {
		// public void onClick(DialogInterface dialog,
		// int whichButton) {
		// }
		// }).show();
	}

	@Override
	protected void finalize() throws Throwable {
		super.finalize();

		detector = null;
		view = null;
		lnr = null;
		scrollView = null;
		base_fragment = null;
		subMenu1Tab = null;
		subMenu2Tab = null;
		subMenu3Tab = null;
		subMenu4Tab = null;
	}

}