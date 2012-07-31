package de.bundestag.android.sections.committees;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import de.bundestag.android.sections.members.MembersDetailsActivity;

public class CommitteesDetailsMemberDetailsActivity extends MembersDetailsActivity
{
    /**
     * Hack to handle the back button for committees member details.
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        return super.onKeyDownSuper(keyCode, event);
    }

    @Override
    public void showBiography(View view)
    {
        Intent launchingIntent = getIntent();
        Bundle extras = launchingIntent.getExtras();
        int memberId = (Integer) extras.get("index");

        Intent biographyIntent = new Intent(view.getContext(), CommitteesDetailsMemberBiographyActivity.class);
        biographyIntent.putExtra("index", memberId);
        biographyIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(biographyIntent);
        overridePendingTransition(0, 0);
    }

    @Override
    public void showInfo(View view)
    {
        Intent launchingIntent = getIntent();
        Bundle extras = launchingIntent.getExtras();
        int memberId = (Integer) extras.get("index");

        Intent contactIntent = new Intent(view.getContext(), CommitteesDetailsMemberInfoActivity.class);
        contactIntent.putExtra("index", memberId);
        contactIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(contactIntent);
        overridePendingTransition(0, 0);
    }

    @Override
    public void showCommittees(View view)
    {
        Intent launchingIntent = getIntent();
        Bundle extras = launchingIntent.getExtras();
        int memberId = (Integer) extras.get("index");

        Intent contactIntent = new Intent(view.getContext(), CommitteesDetailsMemberCommitteesActivity.class);
        contactIntent.putExtra("index", memberId);
        contactIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(contactIntent);
        overridePendingTransition(0, 0);
    }

    @Override
    public void showContact(View view)
    {
        Intent launchingIntent = getIntent();
        Bundle extras = launchingIntent.getExtras();
        int memberId = (Integer) extras.get("index");

        Intent contactIntent = new Intent(view.getContext(), CommitteesDetailsMemberContactActivity.class);
        contactIntent.putExtra("index", memberId);
        contactIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(contactIntent);
        overridePendingTransition(0, 0);
    }
}
