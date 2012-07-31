package de.bundestag.android.sections.members;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import de.bundestag.android.BaseActivity;
import de.bundestag.android.R;
import de.bundestag.android.fragments.GeneralDetailsFragment;
import de.bundestag.android.parser.objects.MembersDetailsObject;
import de.bundestag.android.parser.objects.MembersDetailsWebsitesObject;
import de.bundestag.android.storage.MembersDatabaseAdapter;

public class MembersDetailsContactActivity extends BaseActivity
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

        detailsFragment.showMembersDetailsContact(membersDetailsObject);
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

        // Member websites
        List<MembersDetailsWebsitesObject> websites = new ArrayList<MembersDetailsWebsitesObject>();
        MembersDetailsWebsitesObject website;

        String homepage = memberCursor.getString(memberCursor.getColumnIndex(MembersDatabaseAdapter.KEY_DETAILS_HOMEPAGE));
        if ((homepage != null) && (!homepage.equals("")))
        {
            website = new MembersDetailsWebsitesObject();
            website.setWebsiteTitle("Homepage");
            website.setWebsiteURL(homepage);
            websites.add(website);
        }
        memberCursor.close();

        Cursor websiteCursor = membersDatabaseAdapter.getWebsites(memberId);
        websiteCursor.moveToFirst();
        while (websiteCursor.isAfterLast() == false)
        {
            website = new MembersDetailsWebsitesObject();
            website.setWebsiteTitle(websiteCursor.getString(websiteCursor.getColumnIndex(MembersDatabaseAdapter.KEY_DETAILS_WEBSITE_TITLE)));
            website.setWebsiteURL(websiteCursor.getString(websiteCursor.getColumnIndex(MembersDatabaseAdapter.KEY_DETAILS_WEBSITE_URL)));
            websites.add(website);

            websiteCursor.moveToNext();
        }
        websiteCursor.close();

        membersDetailsObject.setWebsites(websites);

        membersDatabaseAdapter.close();

        return membersDetailsObject;
    }
}