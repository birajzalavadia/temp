package de.bundestag.android.sections.members;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import de.bundestag.android.BaseActivity;
import de.bundestag.android.R;
import de.bundestag.android.fragments.GeneralDetailsFragment;
import de.bundestag.android.parser.objects.MembersDetailsObject;
import de.bundestag.android.storage.MembersDatabaseAdapter;

public class MembersDetailsActivity extends BaseActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState, this);

        setContentView(R.layout.members_detail);

        Intent launchingIntent = getIntent();
        Bundle extras = launchingIntent.getExtras();
        int memberId = (Integer) extras.get("index");

        MembersDetailsObject membersDetailsObject = createMembersDetailsObject(memberId);

        GeneralDetailsFragment detailsFragment = (GeneralDetailsFragment) getSupportFragmentManager().findFragmentById(R.id.detailsFragment);

        detailsFragment.showMembersDetails(membersDetailsObject, getMemberGroups(memberId));
    }

    /**
     * Hack to handle the back button for committees news details.
     */
    public boolean onKeyDownSuper(int keyCode, KeyEvent event)
    {
        return super.onKeyDown(keyCode, event);
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
            intent.setClass(this, MembersListActivity.class);
            this.startActivity(intent);
            overridePendingTransition(0, 0);

            return true;
        }

        return super.onKeyDown(keyCode, event);
    }

    private MembersDetailsObject createMembersDetailsObject(int memberId)
    {
        MembersDatabaseAdapter membersDatabaseAdapter = new MembersDatabaseAdapter(this);
        membersDatabaseAdapter.open();

        Cursor memberCursor = membersDatabaseAdapter.fetchMembers(memberId);

        MembersDetailsObject membersDetailsObject = new MembersDetailsObject();

        membersDetailsObject.setMediaPhoto(memberCursor.getString(memberCursor.getColumnIndex(MembersDatabaseAdapter.KEY_DETAILS_MEDIAPHOTO)));
        membersDetailsObject.setMediaPhotoImageString(memberCursor.getString(memberCursor.getColumnIndex(MembersDatabaseAdapter.KEY_DETAILS_MEDIAPHOTOSTRING)));
        membersDetailsObject
                .setMediaPhotoCopyright(memberCursor.getString(memberCursor.getColumnIndex(MembersDatabaseAdapter.KEY_DETAILS_MEDIAPHOTOCOPYRIGHT)));

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

        Cursor memberGroupsCursor = membersDatabaseAdapter.getGroups(memberId);

        memberGroupsCursor.close();

        membersDatabaseAdapter.close();

        return membersDetailsObject;
    }

    private int getMemberGroups(int memberId)
    {
        MembersDatabaseAdapter membersDatabaseAdapter = new MembersDatabaseAdapter(this);
        membersDatabaseAdapter.open();

        Cursor memberGroupsCursor = membersDatabaseAdapter.getGroups(memberId);

        int groupsCount = memberGroupsCursor.getCount();

        memberGroupsCursor.close();

        membersDatabaseAdapter.close();

        return groupsCount;
    }

    public void showBiography(View view)
    {
        Intent launchingIntent = getIntent();
        Bundle extras = launchingIntent.getExtras();
        int memberId = (Integer) extras.get("index");

        Intent biographyIntent = new Intent(view.getContext(), MembersDetailsBiographyActivity.class);
        biographyIntent.putExtra("index", memberId);
        biographyIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(biographyIntent);
        overridePendingTransition(0, 0);

    }

    public void showInfo(View view)
    {
        Intent launchingIntent = getIntent();
        Bundle extras = launchingIntent.getExtras();
        int memberId = (Integer) extras.get("index");

        Intent contactIntent = new Intent(view.getContext(), MembersDetailsInfoActivity.class);
        contactIntent.putExtra("index", memberId);
        contactIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        startActivity(contactIntent);
        overridePendingTransition(0, 0);
    }

    public void showCommittees(View view)
    {
        Intent launchingIntent = getIntent();
        Bundle extras = launchingIntent.getExtras();
        int memberId = (Integer) extras.get("index");

        Intent contactIntent = new Intent(view.getContext(), MembersDetailsCommitteesActivity.class);
        contactIntent.putExtra("index", memberId);
        contactIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        startActivity(contactIntent);
        overridePendingTransition(0, 0);
    }

    public void showContact(View view)
    {
        Intent launchingIntent = getIntent();
        Bundle extras = launchingIntent.getExtras();
        int memberId = (Integer) extras.get("index");

        Intent contactIntent = new Intent(view.getContext(), MembersDetailsContactActivity.class);
        contactIntent.putExtra("index", memberId);
        contactIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        startActivity(contactIntent);
        overridePendingTransition(0, 0);
    }
}