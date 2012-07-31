package de.bundestag.android.sections.members;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import de.bundestag.android.BaseFragment;
import de.bundestag.android.R;
import de.bundestag.android.constant.DataHolder;

public class MembersFragmentBase extends BaseFragment {
   public static OnMyViewCreated viewCreated;
public void checkIndex(int index,int selectedId) {

		
		if (DataHolder.currentFragmentIndex > DataHolder.noOfSubTab) {
			DataHolder.currentFragmentIndex = 0;
		} else if (DataHolder.currentFragmentIndex < 0) {
			DataHolder.currentFragmentIndex = DataHolder.noOfSubTab;
		}
		viewCreated.onFling(DataHolder.currentFragmentIndex);
//	    setFragment(DataHolder.currentFragmentIndex, selectedId);
	}
//private void setFragment(int index,int selectId) {
//	FragmentManager fragmentManager = getFragmentManager();
//	FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction(); // add
//	// initComponents();
//	
//	
//	switch (index) {
//	case 0:
//		// a
//		// fragment
//
////		subMenu1.setBackgroundResource(R.drawable.top_navigation_gradient_active);
////		subMenu2.setBackgroundResource(R.drawable.top_navigation_gradient);
////		subMenu3.setBackgroundResource(R.drawable.top_navigation_gradient);
////		subMenu4.setBackgroundResource(R.drawable.top_navigation_gradient);
//
//		MembersDetailsBiographyFragment myFragmentBio = new MembersDetailsBiographyFragment();
//		
//		try {
//
//			fragmentTransaction.remove(fragmentManager.findFragmentByTag("subFragment"));
//		} catch (Exception e) {
//		}
//		fragmentTransaction.add(R.id.base_fragment, myFragmentBio, "subFragment");
//		// fragmentTransaction.replace(R.id.base_fragment, myFragmentBio);
//		fragmentTransaction.commit();
//		myFragmentBio.setSelectedID(selectId);
//		
//
//		break;
//	case 1:
//		// fragment
////		subMenu2.setBackgroundResource(R.drawable.top_navigation_gradient_active);
////		subMenu1.setBackgroundResource(R.drawable.top_navigation_gradient);
////		subMenu3.setBackgroundResource(R.drawable.top_navigation_gradient);
////		subMenu4.setBackgroundResource(R.drawable.top_navigation_gradient);
//
//		MembersDetailsInfoFragment myFragmentInfo = new MembersDetailsInfoFragment();
//
//		try {
//
//			fragmentTransaction.remove(fragmentManager.findFragmentByTag("subFragment"));
//		} catch (Exception e) {
//		}
//		fragmentTransaction.add(R.id.base_fragment, myFragmentInfo, "subFragment");
//		// fragmentTransaction.replace(R.id.base_fragment, myFragmentInfo);
//		fragmentTransaction.commit();
//		myFragmentInfo.setSelectedID(selectId);
//
//		break;
//	case 2:
////		subMenu3.setBackgroundResource(R.drawable.top_navigation_gradient_active);
////		subMenu2.setBackgroundResource(R.drawable.top_navigation_gradient);
////		subMenu1.setBackgroundResource(R.drawable.top_navigation_gradient);
////		subMenu4.setBackgroundResource(R.drawable.top_navigation_gradient);
//		MembersDetailsCommitteesFragment myFragmentCommittee = new MembersDetailsCommitteesFragment();
//
//		try {
//
//			fragmentTransaction.remove(fragmentManager.findFragmentByTag("subFragment"));
//		} catch (Exception e) {
//		}
//		fragmentTransaction.add(R.id.base_fragment, myFragmentCommittee, "subFragment");
//		// fragmentTransaction.replace(R.id.base_fragment,
//		// myFragmentCommittee);
//		fragmentTransaction.commit();
//		myFragmentCommittee.setSelectedID(selectId);
//
//		break;
//	case 3:
////		subMenu4.setBackgroundResource(R.drawable.top_navigation_gradient_active);
////		subMenu2.setBackgroundResource(R.drawable.top_navigation_gradient);
////		subMenu3.setBackgroundResource(R.drawable.top_navigation_gradient);
////		subMenu1.setBackgroundResource(R.drawable.top_navigation_gradient);
//
//		MembersDetailsContactFragment myFragmentContact = new MembersDetailsContactFragment();
//
//		try {
//
//			fragmentTransaction.remove(fragmentManager.findFragmentByTag("subFragment"));
//		} catch (Exception e) {
//		}
//		fragmentTransaction.add(R.id.base_fragment, myFragmentContact, "subFragment");
//		// fragmentTransaction.replace(R.id.base_fragment,
//		// myFragmentContact);
//		fragmentTransaction.commit();
//		myFragmentContact.setSelectedID(selectId);
//		break;
//
//	default:
//		break;
//	}
//}

}
