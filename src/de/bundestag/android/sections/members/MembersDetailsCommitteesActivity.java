package de.bundestag.android.sections.members;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import de.bundestag.android.BaseActivity;
import de.bundestag.android.BundestagSharedPreferences;
import de.bundestag.android.R;
import de.bundestag.android.fragments.GeneralDetailsFragment;
import de.bundestag.android.parser.objects.MembersDetailsGroupsObject;
import de.bundestag.android.parser.objects.MembersDetailsObject;
import de.bundestag.android.storage.CommitteesDatabaseAdapter;
import de.bundestag.android.storage.MembersDatabaseAdapter;
import de.bundestag.android.synchronization.MembersSynchronization;

public class MembersDetailsCommitteesActivity extends BaseActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState, this);

        setContentView(R.layout.members_detail);

        Intent launchingIntent = getIntent();
        Bundle extras = launchingIntent.getExtras();
        int memberId = (Integer) extras.get("index");

        BundestagSharedPreferences.setCurrentMember(this, memberId);
        
        MembersDetailsObject membersDetailsObject = createMembersDetailsObject(memberId);

        GeneralDetailsFragment detailsFragment = (GeneralDetailsFragment) getSupportFragmentManager().findFragmentById(R.id.detailsFragment);

        detailsFragment.showMembersDetailsCommittees(membersDetailsObject, this, memberId);
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

        // Member groups
        List<MembersDetailsGroupsObject> presidentGroups = new ArrayList<MembersDetailsGroupsObject>();
        List<MembersDetailsGroupsObject> subPresidentGroups = new ArrayList<MembersDetailsGroupsObject>();
        List<MembersDetailsGroupsObject> coordinateGroups = new ArrayList<MembersDetailsGroupsObject>();
        List<MembersDetailsGroupsObject> fullMemberGroups = new ArrayList<MembersDetailsGroupsObject>();
        List<MembersDetailsGroupsObject> deputyMemberGroups = new ArrayList<MembersDetailsGroupsObject>();
        List<MembersDetailsGroupsObject> substituteMemberGroups = new ArrayList<MembersDetailsGroupsObject>();

        List<MembersDetailsGroupsObject> otherPresidentGroups = new ArrayList<MembersDetailsGroupsObject>();
        List<MembersDetailsGroupsObject> otherSubPresidentGroups = new ArrayList<MembersDetailsGroupsObject>();
        List<MembersDetailsGroupsObject> otherCoordinateGroups = new ArrayList<MembersDetailsGroupsObject>();
        List<MembersDetailsGroupsObject> otherFullMemberGroups = new ArrayList<MembersDetailsGroupsObject>();
        List<MembersDetailsGroupsObject> otherDeputyMemberGroups = new ArrayList<MembersDetailsGroupsObject>();
        List<MembersDetailsGroupsObject> otherSubstituteMemberGroups = new ArrayList<MembersDetailsGroupsObject>();
        MembersDetailsGroupsObject group;

        Cursor groupCursor = membersDatabaseAdapter.getGroups(memberId);
        groupCursor.moveToFirst();
        while (groupCursor.isAfterLast() == false)
        {
            group = new MembersDetailsGroupsObject();
            group.setGroupName(groupCursor.getString(groupCursor.getColumnIndex(MembersDatabaseAdapter.KEY_DETAILS_GROUP_NAME)));
            group.setGroupTitle(groupCursor.getString(groupCursor.getColumnIndex(MembersDatabaseAdapter.KEY_DETAILS_GROUP_TITLE)));
            group.setGroupURL(groupCursor.getString(groupCursor.getColumnIndex(MembersDatabaseAdapter.KEY_DETAILS_GROUP_URL)));
            group.setGroupRSS(groupCursor.getString(groupCursor.getColumnIndex(MembersDatabaseAdapter.KEY_DETAILS_GROUP_RSS)));

            // NOTE, since not all groups (committees) actually exist in the
            // database, only insert a groupId, if it actually exists in db
            String committeeId = groupCursor.getString(groupCursor.getColumnIndex(MembersDatabaseAdapter.KEY_DETAILS_GROUP_ID));
            if (getCommitteeId(committeeId))
            {
                group.setGroupId(committeeId);
            }
            else
            {
                group.setGroupId(null);
            }

            int groupType = groupCursor.getInt(groupCursor.getColumnIndex(MembersDatabaseAdapter.KEY_DETAILS_GROUP_TYPE));

            if (groupType == MembersSynchronization.GROUP_TYPE_COORDINATE)
            {
                coordinateGroups.add(group);
            }
            else if (groupType == MembersSynchronization.GROUP_TYPE_FULLMEMBER)
            {
                fullMemberGroups.add(group);
            }
            else if (groupType == MembersSynchronization.GROUP_TYPE_DEPUTYMEMBER)
            {
                deputyMemberGroups.add(group);
            }
            else if (groupType == MembersSynchronization.GROUP_TYPE_SUBSTITUTE)
            {
                substituteMemberGroups.add(group);
            }
            else if (groupType == MembersSynchronization.GROUP_TYPE_PRESIDENT)
            {
                presidentGroups.add(group);
            }
            else if (groupType == MembersSynchronization.GROUP_TYPE_SUB_PRESIDENT)
            {
                subPresidentGroups.add(group);
            }
            else if (groupType == MembersSynchronization.GROUP_TYPE_OTHER_COORDINATE)
            {
                otherCoordinateGroups.add(group);
            }
            else if (groupType == MembersSynchronization.GROUP_TYPE_OTHER_FULLMEMBER)
            {
                otherFullMemberGroups.add(group);
            }
            else if (groupType == MembersSynchronization.GROUP_TYPE_OTHER_DEPUTYMEMBER)
            {
                otherDeputyMemberGroups.add(group);
            }
            else if (groupType == MembersSynchronization.GROUP_TYPE_OTHER_SUBSTITUTE)
            {
                otherSubstituteMemberGroups.add(group);
            }
            else if (groupType == MembersSynchronization.GROUP_TYPE_OTHER_PRESIDENT)
            {
                otherPresidentGroups.add(group);
            }
            else if (groupType == MembersSynchronization.GROUP_TYPE_OTHER_SUB_PRESIDENT)
            {
                otherSubPresidentGroups.add(group);
            }

            groupCursor.moveToNext();
        }
        groupCursor.close();

        membersDetailsObject.setPresidentGroups(presidentGroups);
        membersDetailsObject.setSubPresidentGroups(subPresidentGroups);
        membersDetailsObject.setCoordinateGroups(coordinateGroups);
        membersDetailsObject.setFullMemberGroups(fullMemberGroups);
        membersDetailsObject.setDeputyMemberGroups(deputyMemberGroups);
        membersDetailsObject.setSubstituteMemberGroups(substituteMemberGroups);
        //
        membersDetailsObject.setOtherPresidentGroups(otherPresidentGroups);
        membersDetailsObject.setOtherSubPresidentGroups(otherSubPresidentGroups);
        membersDetailsObject.setOtherCoordinateGroups(otherCoordinateGroups);
        membersDetailsObject.setOtherFullMemberGroups(otherFullMemberGroups);
        membersDetailsObject.setOtherDeputyMemberGroups(otherDeputyMemberGroups);
        membersDetailsObject.setOtherSubstituteMemberGroups(otherSubstituteMemberGroups);

        membersDatabaseAdapter.close();

        return membersDetailsObject;
    }

    private boolean getCommitteeId(String committeesIdString)
    {
        CommitteesDatabaseAdapter committeesDatabaseAdapter = new CommitteesDatabaseAdapter(this);
        committeesDatabaseAdapter.open();

        int committeeId = -1;
        Cursor memberCursor = committeesDatabaseAdapter.fetchCommitteesById(committeesIdString);
        if (memberCursor.getCount() > 0)
        {
            committeeId = memberCursor.getInt(memberCursor.getColumnIndex(CommitteesDatabaseAdapter.KEY_ROWID));
        }
        memberCursor.close();
        committeesDatabaseAdapter.close();

        return committeeId != -1;
    }

}