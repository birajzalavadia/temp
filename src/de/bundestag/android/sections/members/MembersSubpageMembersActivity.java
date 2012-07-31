package de.bundestag.android.sections.members;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import de.bundestag.android.BaseActivity;
import de.bundestag.android.R;
import de.bundestag.android.constant.AppConstant;

public class MembersSubpageMembersActivity extends BaseActivity {
	public static final String KEY_SUB_PAGE = "subpage";

	public static final String KEY_LIST_KEY = "listKey";

	public static final String KEY_INDEX = "index";

	public static final int SUB_PAGE_FRACTION = 1;

	public static final int SUB_PAGE_CITY = 2;

	public static final int SUB_PAGE_ELECTION = 3;
	

	public int subPageId;

	public int index;
	private int selectedSubmenu = 1;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState, this);
		Intent launchingIntent = getIntent();
		if (launchingIntent != null) {
			Bundle extras = launchingIntent.getExtras();

			int subPageId = (Integer) extras.get(KEY_SUB_PAGE);
			this.subPageId = subPageId;

			int index = (Integer) extras.get(KEY_INDEX);

			this.index = index;

		}
		if (AppConstant.isFragmentSupported) {
			setContentView(R.layout.members_list);
			if (launchingIntent != null) {
				selectedSubmenu = launchingIntent.getExtras().getInt("selectedSubmenu");
				
			}
		} else {
			setContentView(R.layout.members_subpage_members);
		}

	}

	/**
	 * Hack to handle the back button.
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			Intent intent = new Intent();
			if (AppConstant.isFragmentSupported) {
//				if (selectedSubmenu == 1) {
//					intent.setClass(this, MembersListActivity.class);
//					intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//					this.startActivity(intent);
//				}
////				else if (selectedSubmenu == 2) {
////					intent.setClass(this, MembersFractionActivity.class);
////					intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
////					this.startActivity(intent);
////				} 
//				else
				{
					intent.setClass(this, MembersListActivity.class);
					intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					this.startActivity(intent);
				}

				return true;
				// return super.onKeyDown(keyCode, event);
			} else {

				if (subPageId == MembersSubpageMembersActivity.SUB_PAGE_CITY) {
					intent.setClass(this, MembersCityActivity.class);
				} else if (subPageId == MembersSubpageMembersActivity.SUB_PAGE_ELECTION) {
					intent.setClass(this, MembersElectionActivity.class);
				} else if (subPageId == MembersSubpageMembersActivity.SUB_PAGE_FRACTION) {
					intent.setClass(this, MembersFractionActivity.class);
				}
				this.startActivity(intent);
				overridePendingTransition(0, 0);

				return true;
			}
		}

		return super.onKeyDown(keyCode, event);
	}
}
