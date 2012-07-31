package de.bundestag.android.sections.members;

import android.view.KeyEvent;
import de.bundestag.android.sections.committees.CommitteesDetailsNewsActivity;

public class MembersCommitteesDetailsNewsActivity extends CommitteesDetailsNewsActivity
{
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        return super.onKeyDownSuper(keyCode, event);
    }
}