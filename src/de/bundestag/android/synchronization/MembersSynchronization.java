package de.bundestag.android.synchronization;

import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.util.Log;
import de.bundestag.android.BaseActivity;
import de.bundestag.android.helpers.ImageHelper;
import de.bundestag.android.parser.MembersXMLParser;
import de.bundestag.android.parser.objects.MembersDetailsGroupsObject;
import de.bundestag.android.parser.objects.MembersDetailsObject;
import de.bundestag.android.parser.objects.MembersDetailsWebsitesObject;
import de.bundestag.android.parser.objects.MembersObject;
import de.bundestag.android.storage.MembersDatabaseAdapter;

/**
 * Members synchronization class.
 * 
 * Knows how to synchronize the members.
 */
public class MembersSynchronization
{
    public static final int GROUP_TYPE_COORDINATE = 1;

    public static final int GROUP_TYPE_FULLMEMBER = 2;

    public static final int GROUP_TYPE_DEPUTYMEMBER = 3;

    public static final int GROUP_TYPE_SUBSTITUTE = 4;

    public static final int GROUP_TYPE_PRESIDENT = 5;

    public static final int GROUP_TYPE_SUB_PRESIDENT = 6;
    
    public static final int GROUP_TYPE_OTHER_COORDINATE = 7;

    public static final int GROUP_TYPE_OTHER_FULLMEMBER = 8;

    public static final int GROUP_TYPE_OTHER_DEPUTYMEMBER = 9;

    public static final int GROUP_TYPE_OTHER_SUBSTITUTE = 10;

    public static final int GROUP_TYPE_OTHER_PRESIDENT = 11;

    public static final int GROUP_TYPE_OTHER_SUB_PRESIDENT = 12;

    private Context context;

    private MembersDatabaseAdapter membersDatabaseAdapter;

    /**
     * Setup.
     */
    public void setup(Context context)
    {
        this.context = context;
    }

    /**
     * Call the members parser and parse the latest members. Parse all member details.
     */
    public List<MembersObject> parseMembers()
    {
        MembersXMLParser membersParser = new MembersXMLParser();
        List<MembersObject> members = membersParser.parse(true);
        
        return members;
    }

    /**
     * Call the members parser and parse the latest members. Only parse the main list, not all the member details.
     */
    public List<MembersObject> parseMainMembers()
    {
        MembersXMLParser membersParser = new MembersXMLParser();
        List<MembersObject> members = membersParser.parse(false);
        
        return members;
    }

    /**
     * Load and insert the pictures.
     */
    public void insertPictures(List<MembersObject> members)
    {
        int insertsSize = BaseActivity.isDebugOn() ? ((BaseActivity.DEBUG_SYNCHRONIZE_NUMBER > members.size()) ? members.size() : BaseActivity.DEBUG_SYNCHRONIZE_NUMBER) : members.size();
        for (int i = 0; i < insertsSize; i++)
        {
            MembersObject membersObject = (MembersObject) members.get(i);
            insertPicture(membersObject);
        }
    }

    /**
     * Load and insert a picture.
     */
    public void insertPicture(MembersObject membersObject)
    {
        Bitmap bitmap = ImageHelper.loadBitmapFromUrl(membersObject.getPhoto());
       
        if (bitmap != null)
        {
            membersObject.setPhotoString(ImageHelper.convertBitmapToString(bitmap));
            
        }
        else{
        	Log.e("Failed Bitmap Load:",membersObject.getName());
        }

        MembersDetailsObject membersDetailsObject = membersObject.getMembersDetails();
        if (membersDetailsObject != null)
        {
            bitmap = ImageHelper.loadBitmapFromUrl(membersDetailsObject.getMediaPhoto());
            if (bitmap != null)
            {
                membersDetailsObject.setMediaPhotoImageString(ImageHelper.convertBitmapToString(bitmap));
            }
        }
    }

    /**
     * Open the database and remove all members.
     */
    public void deleteAllMembers()
    {
        membersDatabaseAdapter.deleteAllMembers();
    }

    /**
     * Open the database and insert the updated members.
     */
//    private void insertMembers(List<MembersObject> members)
//    {
//        openDatabase();
//
//        for (int i = 0; i < members.size(); i++)
//        {
//            MembersObject membersObject = (MembersObject) members.get(i);
//            long memberId = membersDatabaseAdapter.createMember(membersObject);
//            
//            MembersDetailsObject membersDetails = membersObject.getMembersDetails();
//
//            List<MembersDetailsGroupsObject> coordinateGroups = membersDetails.getCoordinateGroups();
//            List<MembersDetailsGroupsObject> fullMembersGroups = membersDetails.getFullMemberGroups();
//            List<MembersDetailsGroupsObject> deputyMembersGroups = membersDetails.getDeputyMemberGroups();
//            List<MembersDetailsGroupsObject> substituteGroups = membersDetails.getSubstituteMemberGroups();
//
//            for (int j = 0; j < coordinateGroups.size(); j++)
//            {
//                MembersDetailsGroupsObject membersDetailsGroupsObject = (MembersDetailsGroupsObject) coordinateGroups.get(j);
//                membersDatabaseAdapter.createMemberGroup(memberId, GROUP_TYPE_COORDINATE, membersDetailsGroupsObject);
//            }
//            for (int j = 0; j < fullMembersGroups.size(); j++)
//            {
//                MembersDetailsGroupsObject membersDetailsGroupsObject = (MembersDetailsGroupsObject) fullMembersGroups.get(j);
//                membersDatabaseAdapter.createMemberGroup(memberId, GROUP_TYPE_FULLMEMBER, membersDetailsGroupsObject);
//            }
//            for (int j = 0; j < deputyMembersGroups.size(); j++)
//            {
//                MembersDetailsGroupsObject membersDetailsGroupsObject = (MembersDetailsGroupsObject) deputyMembersGroups.get(j);
//                membersDatabaseAdapter.createMemberGroup(memberId, GROUP_TYPE_DEPUTYMEMBER, membersDetailsGroupsObject);
//            }
//            for (int j = 0; j < substituteGroups.size(); j++)
//            {
//                MembersDetailsGroupsObject membersDetailsGroupsObject = (MembersDetailsGroupsObject) substituteGroups.get(j);
//                membersDatabaseAdapter.createMemberGroup(memberId, GROUP_TYPE_SUBSTITUTE, membersDetailsGroupsObject);
//            }
//
//            List<MembersDetailsWebsitesObject> websites = membersDetails.getWebsites();
//            for (int j = 0; j < coordinateGroups.size(); j++)
//            {
//                MembersDetailsWebsitesObject membersDetailsGroupsObject = (MembersDetailsWebsitesObject) websites.get(j);
//                membersDatabaseAdapter.createMemberWebsite(memberId, membersDetailsGroupsObject);
//            }
//        }
//
//        closeDatabase();
//    }

    /**
     * Insert a member object in the database.
     */
    public void insertAMember(MembersObject membersObject)
    {
        long memberId = membersDatabaseAdapter.createMember(membersObject);

        MembersDetailsObject membersDetails = membersObject.getMembersDetails();

        List<MembersDetailsGroupsObject> presidentGroups = membersDetails.getPresidentGroups();
        List<MembersDetailsGroupsObject> subPresidentGroups = membersDetails.getSubPresidentGroups();
        List<MembersDetailsGroupsObject> coordinateGroups = membersDetails.getCoordinateGroups();
        List<MembersDetailsGroupsObject> fullMembersGroups = membersDetails.getFullMemberGroups();
        List<MembersDetailsGroupsObject> deputyMembersGroups = membersDetails.getDeputyMemberGroups();
        List<MembersDetailsGroupsObject> substituteGroups = membersDetails.getSubstituteMemberGroups();
        
        ///
        List<MembersDetailsGroupsObject> otherPresidentGroups = membersDetails.getOtherPresidentGroups();
        List<MembersDetailsGroupsObject> otherSubPresidentGroups = membersDetails.getOtherSubPresidentGroups();
        List<MembersDetailsGroupsObject> otherCoordinateGroups = membersDetails.getOtherCoordinateGroups();
        List<MembersDetailsGroupsObject> otherFullMembersGroups = membersDetails.getOtherFullMemberGroups();
        List<MembersDetailsGroupsObject> otherDeputyMembersGroups = membersDetails.getOtherDeputyMemberGroups();
        List<MembersDetailsGroupsObject> otherSubstituteGroups = membersDetails.getOtherSubstituteMemberGroups();
        ///
      ////
        for (int j = 0; j < otherPresidentGroups.size(); j++)
        {
            MembersDetailsGroupsObject membersDetailsGroupsObject = (MembersDetailsGroupsObject) otherPresidentGroups.get(j);
            membersDatabaseAdapter.createMemberGroup(memberId, GROUP_TYPE_OTHER_PRESIDENT, membersDetailsGroupsObject);
        }
        for (int j = 0; j < otherSubPresidentGroups.size(); j++)
        {
            MembersDetailsGroupsObject membersDetailsGroupsObject = (MembersDetailsGroupsObject) otherSubPresidentGroups.get(j);
            membersDatabaseAdapter.createMemberGroup(memberId, GROUP_TYPE_OTHER_SUB_PRESIDENT, membersDetailsGroupsObject);
        }
//        for (int j = 0; j < otherCoordinateGroups.size(); j++)
//        {
//            MembersDetailsGroupsObject membersDetailsGroupsObject = (MembersDetailsGroupsObject) otherCoordinateGroups.get(j);
//            membersDatabaseAdapter.createMemberGroup(memberId, GROUP_TYPE_OTHER_COORDINATE, membersDetailsGroupsObject);
//        }
        for (int j = 0; j < otherFullMembersGroups.size(); j++)
        {
            MembersDetailsGroupsObject membersDetailsGroupsObject = (MembersDetailsGroupsObject) otherFullMembersGroups.get(j);
            membersDatabaseAdapter.createMemberGroup(memberId, GROUP_TYPE_OTHER_FULLMEMBER, membersDetailsGroupsObject);
        }
//        for (int j = 0; j < otherDeputyMembersGroups.size(); j++)
//        {
//            MembersDetailsGroupsObject membersDetailsGroupsObject = (MembersDetailsGroupsObject) otherDeputyMembersGroups.get(j);
//            membersDatabaseAdapter.createMemberGroup(memberId, GROUP_TYPE_OTHER_DEPUTYMEMBER, membersDetailsGroupsObject);
//        }
        for (int j = 0; j < otherSubstituteGroups.size(); j++)
        {
            MembersDetailsGroupsObject membersDetailsGroupsObject = (MembersDetailsGroupsObject) otherSubstituteGroups.get(j);
            membersDatabaseAdapter.createMemberGroup(memberId, GROUP_TYPE_OTHER_SUBSTITUTE, membersDetailsGroupsObject);
        }
        ////
        for (int j = 0; j < presidentGroups.size(); j++)
        {
            MembersDetailsGroupsObject membersDetailsGroupsObject = (MembersDetailsGroupsObject) presidentGroups.get(j);
            membersDatabaseAdapter.createMemberGroup(memberId, GROUP_TYPE_PRESIDENT, membersDetailsGroupsObject);
        }
        for (int j = 0; j < subPresidentGroups.size(); j++)
        {
            MembersDetailsGroupsObject membersDetailsGroupsObject = (MembersDetailsGroupsObject) subPresidentGroups.get(j);
            membersDatabaseAdapter.createMemberGroup(memberId, GROUP_TYPE_SUB_PRESIDENT, membersDetailsGroupsObject);
        }
        for (int j = 0; j < coordinateGroups.size(); j++)
        {
            MembersDetailsGroupsObject membersDetailsGroupsObject = (MembersDetailsGroupsObject) coordinateGroups.get(j);
            membersDatabaseAdapter.createMemberGroup(memberId, GROUP_TYPE_COORDINATE, membersDetailsGroupsObject);
        }
        for (int j = 0; j < fullMembersGroups.size(); j++)
        {
            MembersDetailsGroupsObject membersDetailsGroupsObject = (MembersDetailsGroupsObject) fullMembersGroups.get(j);
            membersDatabaseAdapter.createMemberGroup(memberId, GROUP_TYPE_FULLMEMBER, membersDetailsGroupsObject);
        }
        for (int j = 0; j < deputyMembersGroups.size(); j++)
        {
            MembersDetailsGroupsObject membersDetailsGroupsObject = (MembersDetailsGroupsObject) deputyMembersGroups.get(j);
            membersDatabaseAdapter.createMemberGroup(memberId, GROUP_TYPE_DEPUTYMEMBER, membersDetailsGroupsObject);
        }
        for (int j = 0; j < substituteGroups.size(); j++)
        {
            MembersDetailsGroupsObject membersDetailsGroupsObject = (MembersDetailsGroupsObject) substituteGroups.get(j);
            membersDatabaseAdapter.createMemberGroup(memberId, GROUP_TYPE_SUBSTITUTE, membersDetailsGroupsObject);
        }
        

        List<MembersDetailsWebsitesObject> websites = membersDetails.getWebsites();
        for (int j = 0; j < websites.size(); j++)
        {
            MembersDetailsWebsitesObject membersDetailsGroupsObject = (MembersDetailsWebsitesObject) websites.get(j);
            membersDatabaseAdapter.createMemberWebsite(memberId, membersDetailsGroupsObject);
        }
    }

    /**
     * Open the database.
     */
    public void openDatabase()
    {
        if (membersDatabaseAdapter == null)
        {
            membersDatabaseAdapter = new MembersDatabaseAdapter(context);
        }

        membersDatabaseAdapter.open();
    }

    /**
     * Close the database.
     */
    public void closeDatabase()
    {
        membersDatabaseAdapter.close();
    }

    public Cursor getMemberFromId(String id)
    {
        return membersDatabaseAdapter.fetchMemberFromId(id);
    }

    public void deleteMember(String memberId)
    {
        membersDatabaseAdapter.deleteMember(memberId);
    }
}
