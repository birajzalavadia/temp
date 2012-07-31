package de.bundestag.android.synchronization;

import java.util.Date;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import de.bundestag.android.BaseActivity;
import de.bundestag.android.constant.DataHolder;
import de.bundestag.android.helpers.DateHelper;
import de.bundestag.android.parser.CommitteesXMLParser;
import de.bundestag.android.parser.objects.CommitteesDetailsObject;
import de.bundestag.android.parser.objects.CommitteesObject;
import de.bundestag.android.sections.members.MembersListActivity;
import de.bundestag.android.storage.CommitteesDatabaseAdapter;

public class SynchronizeCommitteesTask extends BaseSynchronizeTask
{
    @Override
    protected Void doInBackground(Context... context)
    {
        this.activity = context[0];
        publishProgress("Ausschüsse werden analysiert");
        CommitteesSynchronization committeesSynchronization = new CommitteesSynchronization();
        committeesSynchronization.setup(context[0]);

        committeesSynchronization.openDatabase();

        

        List<CommitteesObject> committees = null;
        try
        {
        	
            committees = committeesSynchronization.parseMainCommittees();
        }
        catch (Exception e)
        {
            BaseActivity baseActivity = (BaseActivity) activity;
            if (DataHolder.isOnline(baseActivity))
            {
                publishProgress("Ein Problem ist beim Analysieren der Ausschüsse aufgetreten. Bitte versuchen Sie es später erneut.");
            }
            else
            {
                publishProgress("Die Aktualisierung der Daten kann leider nur mit einer aktiven Internetverbindung durchgeführt werden. Die Anwendung befindet sich im Offline-Modus.");
                
                this.cancel(false);
            }
            
            committeesSynchronization.closeDatabase();

            return null;
        }
        if (progressDialog.cancelSynchronization)
        {
            Intent intent = new Intent();
            intent.setClass(activity, MembersListActivity.class);
            activity.startActivity(intent);
            this.cancel(false);

            progressDialog.dismiss();

            committeesSynchronization.closeDatabase();

            return null;
        }

        // Fill in the committee details
        int insertsSize =
                BaseActivity.isDebugOn() ? ((BaseActivity.DEBUG_SYNCHRONIZE_NUMBER > committees.size()) ? committees.size() : BaseActivity.DEBUG_SYNCHRONIZE_NUMBER)
                        : committees.size();
        for (int i = 0; i < insertsSize; i++)
        {
//            publishProgress("Ausschüsse werden auf vorhandene Aktualisierungen überprüft " + (i + 1) + "/" + insertsSize + ".");

            CommitteesObject committeesObject = (CommitteesObject) committees.get(i);

            // Get the committee from the id
            Cursor committeeCursor = committeesSynchronization.getCommitteeFromId(committeesObject.getId());
            Date committeeDatabaseLastChanged = null;
            Date committeeLastChanged = null;
            if ((committeeCursor != null) && (committeeCursor.getCount() > 0))
            {
                String committeeDatabaseLastChangedString = committeeCursor.getString(committeeCursor.getColumnIndex(CommitteesDatabaseAdapter.KEY_CHANGEDDATETIME));
                committeeDatabaseLastChanged = DateHelper.parseDate(committeeDatabaseLastChangedString);
                committeeLastChanged = committeesObject.getChangedDateTimeDate();
            }

            // If committee doesn't exist or has been changed, insert it in the database.
            if ((committeeCursor == null) || (committeeCursor.getCount() == 0) || (committeeDatabaseLastChanged.before(committeeLastChanged)))
            {
                publishProgress("Aktualisiert Ausschuss " + (i + 1) + " von " + insertsSize + " Ausschüssen.");

                try
                {
                    // Parse the new or updated committee.
                    CommitteesXMLParser committeesXMLParser = new CommitteesXMLParser();
                    CommitteesDetailsObject committeesDetailsObject = committeesXMLParser.parseDetails(committeesObject.getDetailsXML(), committeesObject.getId());
                    committeesObject.setCommitteeDetails(committeesDetailsObject);

                    // Check the committee image date, to see if it has been updated.
                    if ((committeeCursor != null) && (committeeCursor.getCount() > 0))
                    {
                        Date imageLastChanged = committeesObject.getImageChangedDateTimeDate();
                        String imageDatabaseLastChangedString =
                                committeeCursor.getString(committeeCursor.getColumnIndex(CommitteesDatabaseAdapter.KEY_IMAGECHANGEDDATETIME));
                        Date imageDatabaseLastChanged = DateHelper.parseDate(imageDatabaseLastChangedString);

                        // Insert the committee picture.
                        if ((imageDatabaseLastChanged == null) || (imageDatabaseLastChanged.before(imageLastChanged)))
                        {
                            committeesSynchronization.insertPicture(committeesObject);
                        }
                        else
                        {
                            committeesDetailsObject.setPhotoString(committeeCursor.getString(committeeCursor.getColumnIndex(CommitteesDatabaseAdapter.KEY_DETAILS_PHOTOSTRING)));
                        }
                    }
                    else
                    {
                        committeesSynchronization.insertPicture(committeesObject);
                    }

                    // Delete the committee if it already exists in the database.
                    if ((committeeCursor != null) && (committeeCursor.getCount() > 0))
                    {
                        committeesSynchronization.deleteCommittee(committeesObject.getId());
                    }

                    // Store the new or updated committee in the database
                    committeesSynchronization.insertACommittee(committeesObject);
                } catch (Exception e)
                {
                    BaseActivity baseActivity = (BaseActivity) activity;
                    if (DataHolder.isOnline(baseActivity))
                    {
                        publishProgress("Ein Problem ist beim Einfügen des Ausschusses in die Datenbank aufgetreten. Bitte versuchen Sie es später erneut.");
                    }
                    else
                    {
                        publishProgress("Die Aktualisierung der Daten kann leider nur mit einer aktiven Internetverbindung durchgeführt werden. Die Anwendung befindet sich im Offline-Modus.");
                        
                        this.cancel(false);
                    }
                    
                    committeesSynchronization.closeDatabase();

                    return null;
                }
            }
            committeeCursor.close();

            if (progressDialog.cancelSynchronization)
            {
//                Intent intent = new Intent();
//                intent.setClass(activity, MembersListActivity.class);
//                activity.startActivity(intent);

                this.cancel(false);

                progressDialog.dismiss();

                committeesSynchronization.closeDatabase();

                return null;
            }
        }

        committeesSynchronization.closeDatabase();

        progressDialog.dismiss();

        return null;
    }
}
