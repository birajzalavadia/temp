package de.bundestag.android.synchronization;

import android.content.Context;
import de.bundestag.android.BaseActivity;
import de.bundestag.android.constant.DataHolder;
import de.bundestag.android.parser.PlenumXMLParser;
import de.bundestag.android.parser.objects.PlenumObject;
import de.bundestag.android.parser.objects.PlenumSimpleObject;

public class SynchronizePlenumTask extends BaseSynchronizeTask
{
    @Override
    protected Void doInBackground(Context... context)
    {
        this.activity = context[0];

        publishProgress("Synchronisierung des Plenums wird gestartet");

        PlenumSynchronization plenumSynchronization = new PlenumSynchronization();
        plenumSynchronization.setup(context[0]);

        publishProgress("Altes Plenum wird gelöscht");

        plenumSynchronization.openDatabase();

        plenumSynchronization.deleteAllPlenum();

        publishProgress("Plenum wird analysiert");

        boolean lostConnection = false;
        PlenumObject mainPlenum = null;
        try
        {
            mainPlenum = plenumSynchronization.parsePlenum(PlenumXMLParser.MAIN_PLENUM_URL);
            mainPlenum.setType(PlenumSynchronization.PLENUM_TYPE_MAIN);
            plenumSynchronization.insertPicture(mainPlenum);
            plenumSynchronization.insertAPlenum(mainPlenum);
        } catch (Exception e)
        {
            BaseActivity baseActivity = (BaseActivity) activity;
            if (DataHolder.isOnline(baseActivity))
            {
                publishProgress("Ein Problem ist beim Analysieren der Plenum aufgetreten. Bitte versuchen Sie es später erneut.");
            }
            else
            {
                publishProgress("Die Aktualisierung der Daten kann leider nur mit einer aktiven Internetverbindung durchgeführt werden. Die Anwendung befindet sich im Offline-Modus.");

                this.cancel(false);

                lostConnection = true;
            }

            plenumSynchronization.closeDatabase();

            return null;
        }

        if (!lostConnection)
        {
            try
            {
                PlenumObject taskPlenum = plenumSynchronization.parsePlenum(PlenumXMLParser.PLENUM_TASK_URL);
                taskPlenum.setType(PlenumSynchronization.PLENUM_TYPE_TASK);
                plenumSynchronization.insertPicture(taskPlenum);
                plenumSynchronization.insertAPlenum(taskPlenum);
            } catch (Exception e)
            {
                BaseActivity baseActivity = (BaseActivity) activity;
                if (DataHolder.isOnline(baseActivity))
                {
                    publishProgress("Ein Problem ist beim Analysieren der Plenum aufgetreten. Bitte versuchen Sie es später erneut.");
                }
                else
                {
                    publishProgress("Die Aktualisierung der Daten kann leider nur mit einer aktiven Internetverbindung durchgeführt werden. Die Anwendung befindet sich im Offline-Modus.");

                    this.cancel(false);

                    lostConnection = true;
                }

                plenumSynchronization.closeDatabase();

                return null;
            }

            publishProgress("Plenum wird gespeichert");

        }

        if (!lostConnection)
        {
        	if (mainPlenum!=null && mainPlenum.getTeaser() != null && mainPlenum.getTeaser().indexOf(PlenumXMLParser.SIMPLE1_PLENUM_URL) > 0){
        		try
        		{
        			PlenumSimpleObject simplePlenum1 = plenumSynchronization.parseSimplePlenum(PlenumXMLParser.SIMPLE1_PLENUM_URL);
        			simplePlenum1.setType(PlenumSynchronization.PLENUM_TYPE_SIMPLE1);
        			plenumSynchronization.insertASimplePlenum(simplePlenum1);
        		} catch (Exception e)
        		{
        			BaseActivity baseActivity = (BaseActivity) activity;
        			if (DataHolder.isOnline(baseActivity))
        			{
        				publishProgress("Ein Problem ist beim Analysieren der Plenum aufgetreten. Bitte versuchen Sie es später erneut.");
        			}
        			else
        			{
        				publishProgress("Die Aktualisierung der Daten kann leider nur mit einer aktiven Internetverbindung durchgeführt werden. Die Anwendung befindet sich im Offline-Modus.");

        				this.cancel(false);

        				lostConnection = true;
        			}

        			plenumSynchronization.closeDatabase();

        			return null;
        		}
        	}
        }

        if (!lostConnection)
        {
        	if (mainPlenum!=null && mainPlenum.getTeaser() != null && mainPlenum.getTeaser().indexOf(PlenumXMLParser.SIMPLE2_PLENUM_URL) > 0){

        		try
        		{
        			PlenumSimpleObject simplePlenum2 = plenumSynchronization.parseSimplePlenum(PlenumXMLParser.SIMPLE2_PLENUM_URL);
        			simplePlenum2.setType(PlenumSynchronization.PLENUM_TYPE_SIMPLE2);
        			plenumSynchronization.insertASimplePlenum(simplePlenum2);
        		} catch (Exception e)
        		{
        			BaseActivity baseActivity = (BaseActivity) activity;
        			if (DataHolder.isOnline(baseActivity))
        			{
        				publishProgress("Ein Problem ist beim Analysieren der Plenum aufgetreten. Bitte versuchen Sie es später erneut.");
        			}
        			else
        			{
        				publishProgress("Die Aktualisierung der Daten kann leider nur mit einer aktiven Internetverbindung durchgeführt werden. Die Anwendung befindet sich im Offline-Modus.");

        				this.cancel(false);

        				lostConnection = true;
        			}

        			plenumSynchronization.closeDatabase();

        			return null;
        		}
        	}
        }

        if (!lostConnection)
        {
        	if (mainPlenum!=null && mainPlenum.getTeaser() != null && mainPlenum.getTeaser().indexOf(PlenumXMLParser.SIMPLE3_PLENUM_URL) > 0){
        		try
        		{
        			PlenumSimpleObject simplePlenum3 = plenumSynchronization.parseSimplePlenum(PlenumXMLParser.SIMPLE3_PLENUM_URL);
        			simplePlenum3.setType(PlenumSynchronization.PLENUM_TYPE_SIMPLE3);
        			plenumSynchronization.insertASimplePlenum(simplePlenum3);
        		} catch (Exception e)
        		{
        			BaseActivity baseActivity = (BaseActivity) activity;
        			if (DataHolder.isOnline(baseActivity))
        			{
        				publishProgress("Ein Problem ist beim Analysieren der Plenum aufgetreten. Bitte versuchen Sie es später erneut.");
        			}
        			else
        			{
        				publishProgress("Die Aktualisierung der Daten kann leider nur mit einer aktiven Internetverbindung durchgeführt werden. Die Anwendung befindet sich im Offline-Modus.");

        				this.cancel(false);

        				lostConnection = true;
        			}

        			plenumSynchronization.closeDatabase();

        			return null;
        		}
        	}
        }

        if (!lostConnection)
        {
        	if (mainPlenum!=null && mainPlenum.getTeaser() != null && mainPlenum.getTeaser().indexOf(PlenumXMLParser.SIMPLE4_PLENUM_URL) > 0){
        		try
        		{
        			PlenumSimpleObject simplePlenum4 = plenumSynchronization.parseSimplePlenum(PlenumXMLParser.SIMPLE4_PLENUM_URL);
        			simplePlenum4.setType(PlenumSynchronization.PLENUM_TYPE_SIMPLE4);
        			plenumSynchronization.insertASimplePlenum(simplePlenum4);
        		} catch (Exception e)
        		{
        			BaseActivity baseActivity = (BaseActivity) activity;
        			if (DataHolder.isOnline(baseActivity))
        			{
        				publishProgress("Ein Problem ist beim Analysieren der Plenum aufgetreten. Bitte versuchen Sie es später erneut.");
        			}
        			else
        			{
        				publishProgress("Die Aktualisierung der Daten kann leider nur mit einer aktiven Internetverbindung durchgeführt werden. Die Anwendung befindet sich im Offline-Modus.");

        				this.cancel(false);

        				lostConnection = true;
        			}

        			plenumSynchronization.closeDatabase();

        			return null;
        		}
        	}
        }

        publishProgress("Synchronisierung des Plenums beendet");

        plenumSynchronization.closeDatabase();

        progressDialog.dismiss();

        return null;
    }
}
