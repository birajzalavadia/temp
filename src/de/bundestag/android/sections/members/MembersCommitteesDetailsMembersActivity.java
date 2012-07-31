package de.bundestag.android.sections.members;

import android.view.KeyEvent;
import de.bundestag.android.sections.committees.CommitteesDetailsMembersActivity;

public class MembersCommitteesDetailsMembersActivity extends CommitteesDetailsMembersActivity
{
    /**
     * Hack to handle the back button for committees news details.
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        return super.onKeyDownSuper(keyCode, event);
    }
}