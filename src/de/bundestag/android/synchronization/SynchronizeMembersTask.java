package de.bundestag.android.synchronization;

import java.util.Date;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import de.bundestag.android.BaseActivity;
import de.bundestag.android.constant.DataHolder;
import de.bundestag.android.helpers.DateHelper;
import de.bundestag.android.parser.MembersXMLParser;
import de.bundestag.android.parser.objects.MembersDetailsObject;
import de.bundestag.android.parser.objects.MembersObject;
import de.bundestag.android.storage.MembersDatabaseAdapter;

public class SynchronizeMembersTask extends BaseSynchronizeTask
{
    @Override
    protected Void doInBackground(Context... context)
    {
        this.activity = context[0];

        if (progressDialog != null)
        {
            progressDialog.cancelSynchronization = false;
        }

        MembersSynchronization membersSynchronization = new MembersSynchronization();
        membersSynchronization.setup(context[0]);

        membersSynchronization.openDatabase();

        publishProgress("MdB-Informationen werden analysiert");

        // First parse the main members list
        List<MembersObject> members = null;
        try
        {
            members = membersSynchronization.parseMainMembers();
        } catch (Exception e)
        {
            BaseActivity baseActivity = (BaseActivity) activity;
            if (DataHolder.isOnline(baseActivity))
            {
                publishProgress("Ein Problem ist beim Analysieren der MdB-Informationen aufgetreten. Bitte versuchen Sie es später erneut.");
            }
            else
            {
                publishProgress("Die Aktualisierung der Daten kann leider nur mit einer aktiven Internetverbindung durchgeführt werden. Die Anwendung befindet sich im Offline-Modus.");

                this.cancel(false);
            }

            membersSynchronization.closeDatabase();

            return null;
        }

        // Fill in the member details
        int insertsSize =
                BaseActivity.isDebugOn() ? ((BaseActivity.DEBUG_SYNCHRONIZE_NUMBER > members.size()) ? members.size() : BaseActivity.DEBUG_SYNCHRONIZE_NUMBER)
                        : members.size();
        for (int i = 0; i < insertsSize; i++)
        {
//        	if(i==349){
        		String strin = "=> "+ i;
        		Log.w("-------------- i ",strin);
//        	}
            publishProgress("MdB-Informationen werden auf vorhandene Aktualisierungen überprüft " + (i + 1) + "/" + insertsSize + ".");
            
            MembersObject membersObject = (MembersObject) members.get(i);

            // Get the member from the id
            Cursor memberCursor = membersSynchronization.getMemberFromId(membersObject.getId());
            Date memberDatabaseLastChanged = null;
            Date memberLastChanged = null;
            String imageDatabaseLastChangedString = null;
            String memberPhotoString = null;
            String memberImageString = null;
            boolean foundMember = false;
            if ((memberCursor != null) && (memberCursor.getCount() > 0))
            {

            	
                foundMember = true;
                String memberDatabaseLastChangedString = memberCursor.getString(memberCursor.getColumnIndex(MembersDatabaseAdapter.KEY_CHANGEDDATETIME));
                memberDatabaseLastChanged = DateHelper.parseDate(memberDatabaseLastChangedString);
                memberLastChanged = membersObject.getChangedDateTimeDate();
                imageDatabaseLastChangedString = memberCursor.getString(memberCursor.getColumnIndex(MembersDatabaseAdapter.KEY_PHOTOCHANGEDDATETIME));
                memberPhotoString = memberCursor.getString(memberCursor.getColumnIndex(MembersDatabaseAdapter.KEY_PHOTO_STRING));
                memberImageString = memberCursor.getString(memberCursor.getColumnIndex(MembersDatabaseAdapter.KEY_DETAILS_MEDIAPHOTOSTRING));
            }
            memberCursor.close();

            //If Member has no photo it always updates
            boolean no_photo = (memberImageString==null || memberPhotoString==null);
            
            // If member doesn't exist or has been changed, insert it in the database.
            if (!foundMember || (memberDatabaseLastChanged.before(memberLastChanged)) || no_photo)
            {
                publishProgress("MdB-Informationen werden aktualisiert  " + (i + 1) + "/" + insertsSize + ".");

                try
                {
                    // Parse the new or updated member.
                    MembersXMLParser membersXMLParser = new MembersXMLParser();
                    MembersDetailsObject membersDetailsObject = membersXMLParser.parseDetails(membersObject.getInfoURL());
                    membersObject.setMembersDetails(membersDetailsObject);

                    // Check the member image date, to see if it has been updated.
                    if (foundMember)
                    {
                        Date imageLastChanged = membersObject.getPhotoChangedDateTimeDate();
                        Date imageDatabaseLastChanged = DateHelper.parseDate(imageDatabaseLastChangedString);

                        // Insert the member pictures.
                        if (imageDatabaseLastChanged.before(imageLastChanged) || no_photo)
                        {
                            membersSynchronization.insertPicture(membersObject);
                        }
                        else
                        {
                            membersObject.setPhotoString(memberPhotoString);
                            membersDetailsObject.setMediaPhotoImageString(memberImageString);
                        }
                    }
                    else
                    {
                        membersSynchronization.insertPicture(membersObject);
                    }

                    // Delete the member if it already exists in the database.
                    if (foundMember)
                    {
                        membersSynchronization.deleteMember(membersObject.getId());
                    }

                    // Store the new or updated member in the database
                    membersSynchronization.insertAMember(membersObject);
                } catch (Exception e)
                {
                    publishProgress("Ein Problem ist beim Einfügen der MdB-Informationen in die Datenbank aufgetreten. Bitte versuchen Sie es später erneut.");
                }
            }

            if (progressDialog.cancelSynchronization)
            {
                progressDialog.dismiss();

                membersSynchronization.closeDatabase();

//                Intent intent = new Intent();
//                intent.setClass(activity, MembersListActivity.class);
//                activity.startActivity(intent);

                this.cancel(false);

                return null;
            }
        }

        progressDialog.dismiss();

        membersSynchronization.closeDatabase();

        return null;
    }
}
