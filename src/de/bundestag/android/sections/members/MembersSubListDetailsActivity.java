package de.bundestag.android.sections.members;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;

public class MembersSubListDetailsActivity extends MembersDetailsActivity
{
    public static final String LIST_KEY_INDEX = "LIST_KEY_INDEX";

    public int subPageId;

    public int listId;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        Intent launchingIntent = getIntent();
        Bundle extras = launchingIntent.getExtras();
        int subPageId = (Integer) extras.get(MembersSubpageMembersActivity.KEY_SUB_PAGE);
        this.subPageId = subPageId;

        this.listId = (Integer) extras.get(MembersSubpageMembersActivity.KEY_LIST_KEY);
    }

    /**
     * Hack to handle the back button for committees news details.
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if (keyCode == KeyEvent.KEYCODE_BACK)
        {
            Intent intent = new Intent();
            intent.setClass(this, MembersSubpageMembersActivity.class);
            intent.putExtra("index", this.listId);
            intent.putExtra(MembersSubpageMembersActivity.KEY_SUB_PAGE, this.subPageId);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            this.startActivity(intent);
            overridePendingTransition(0, 0);

            return true;
        }

        return super.onKeyDown(keyCode, event);
    }

//    /**
//     * Hack to handle the back button.
//     */
//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event)
//    {
//        if (keyCode == KeyEvent.KEYCODE_BACK)
//        {
//            Intent intent = new Intent();
//            if (subPageId == MembersSubpageMembersActivity.SUB_PAGE_CITY)
//            {
//                intent.setClass(this, MembersCityActivity.class);
//            }
//            else if (subPageId == MembersSubpageMembersActivity.SUB_PAGE_ELECTION)
//            {
//                intent.setClass(this, MembersElectionActivity.class);
//            }
//            else if (subPageId == MembersSubpageMembersActivity.SUB_PAGE_FRACTION)
//            {
//                intent.setClass(this, MembersFractionActivity.class);
//            }
//            this.startActivity(intent);
//            overridePendingTransition(0, 0);
//
//            return true;
//        }
//
//        return super.onKeyDown(keyCode, event);
//    }
}