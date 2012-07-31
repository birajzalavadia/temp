package de.bundestag.android.synchronization;

import android.content.Context;
import android.content.Intent;
import de.bundestag.android.BaseActivity;
import de.bundestag.android.constant.DataHolder;
import de.bundestag.android.parser.PlenumXMLParser;
import de.bundestag.android.parser.objects.PlenumObject;
import de.bundestag.android.parser.objects.PlenumSimpleObject;
import de.bundestag.android.sections.plenum.PlenumNewsActivity;
import de.bundestag.android.sections.plenum.PlenumSitzungenActivity;

public class SynchronizePlenumStartTask extends BaseSynchronizeTask
{
    private PlenumObject mainPlenum;

    @Override
    protected Void doInBackground(Context... context)
    {
        this.activity = context[0];

        publishProgress("Startet Synchronisierung des Plenums");

        PlenumSynchronization plenumSynchronization = new PlenumSynchronization();
        plenumSynchronization.setup(context[0]);

        publishProgress("Altes Plenum wird gelöscht");

        plenumSynchronization.openDatabase();

        plenumSynchronization.deleteAllPlenum();
        
        publishProgress("Plenum wird analysiert");

        try
        {
            PlenumObject mainPlenum = plenumSynchronization.parsePlenum(PlenumXMLParser.MAIN_PLENUM_URL);
            mainPlenum.setType(PlenumSynchronization.PLENUM_TYPE_MAIN);
            this.mainPlenum = mainPlenum;
            plenumSynchronization.insertPicture(mainPlenum);
            plenumSynchronization.insertAPlenum(mainPlenum);
        }
        catch (Exception e)
        {
            publishProgress("Ein Problem ist beim Analysieren der Plenum aufgetreten. Bitte versuchen Sie es später erneut.");
        }

        try
        {
            PlenumObject taskPlenum = plenumSynchronization.parsePlenum(PlenumXMLParser.PLENUM_TASK_URL);
            taskPlenum.setType(PlenumSynchronization.PLENUM_TYPE_TASK);
            plenumSynchronization.insertPicture(taskPlenum);
            plenumSynchronization.insertAPlenum(taskPlenum);
        }
        catch (Exception e)
        {
            publishProgress("Ein Problem ist beim Analysieren der Plenum aufgetreten. Bitte versuchen Sie es später erneut.");
        }

        publishProgress("Plenum wird gespeichert");

//        try
//        {
//            PlenumObject taskPlenum = plenumSynchronization.parsePlenum(PlenumXMLParser.PLENUM_TASK_URL);
//            taskPlenum.setType(PlenumSynchronization.PLENUM_TYPE_TASK);
//            plenumSynchronization.insertPicture(taskPlenum);
//            plenumSynchronization.insertAPlenum(taskPlenum);
//        }
//        catch (Exception e)
//        {
//            publishProgress("Ein Problem ist beim Analysieren der Plenum aufgetreten. Bitte versuchen Sie es später erneut.");
//        }

        
        if (mainPlenum!=null && mainPlenum.getTeaser() != null && mainPlenum.getTeaser().indexOf(PlenumXMLParser.SIMPLE1_PLENUM_URL) > 0){
        	try
        	{
        		PlenumSimpleObject simplePlenum1 = plenumSynchronization.parseSimplePlenum(PlenumXMLParser.SIMPLE1_PLENUM_URL);
        		simplePlenum1.setType(PlenumSynchronization.PLENUM_TYPE_SIMPLE1);
        		plenumSynchronization.insertASimplePlenum(simplePlenum1);
        	}
        	catch (Exception e)
        	{
        		publishProgress("Ein Problem ist beim Analysieren der Plenum aufgetreten. Bitte versuchen Sie es später erneut.");
        	}
        }
        
        if (mainPlenum!=null && mainPlenum.getTeaser() != null && mainPlenum.getTeaser().indexOf(PlenumXMLParser.SIMPLE2_PLENUM_URL) > 0){
        	try
        	{
        		PlenumSimpleObject simplePlenum2 = plenumSynchronization.parseSimplePlenum(PlenumXMLParser.SIMPLE2_PLENUM_URL);
        		simplePlenum2.setType(PlenumSynchronization.PLENUM_TYPE_SIMPLE2);
        		plenumSynchronization.insertASimplePlenum(simplePlenum2);
        	}
        	catch (Exception e)
        	{
        		publishProgress("Ein Problem ist beim Analysieren der Plenum aufgetreten. Bitte versuchen Sie es später erneut.");
        	}
        }

        if (mainPlenum!=null && mainPlenum.getTeaser() != null && mainPlenum.getTeaser().indexOf(PlenumXMLParser.SIMPLE3_PLENUM_URL) > 0){
        	try
        	{
        		PlenumSimpleObject simplePlenum3 = plenumSynchronization.parseSimplePlenum(PlenumXMLParser.SIMPLE3_PLENUM_URL);
        		simplePlenum3.setType(PlenumSynchronization.PLENUM_TYPE_SIMPLE3);
        		plenumSynchronization.insertASimplePlenum(simplePlenum3);
        	}
        	catch (Exception e)
        	{
        		publishProgress("Ein Problem ist beim Analysieren der Plenum aufgetreten. Bitte versuchen Sie es später erneut.");
        	}
        }
        
        if (mainPlenum!=null && mainPlenum.getTeaser() != null && mainPlenum.getTeaser().indexOf(PlenumXMLParser.SIMPLE4_PLENUM_URL) > 0){
        	try
        	{
        		PlenumSimpleObject simplePlenum4 = plenumSynchronization.parseSimplePlenum(PlenumXMLParser.SIMPLE4_PLENUM_URL);
        		simplePlenum4.setType(PlenumSynchronization.PLENUM_TYPE_SIMPLE4);
        		plenumSynchronization.insertASimplePlenum(simplePlenum4);
        	}
        	catch (Exception e)
        	{
        		publishProgress("Ein Problem ist beim Analysieren der Plenum aufgetreten. Bitte versuchen Sie es später erneut.");
        	}
        }
        publishProgress("Synchronisierung des Plenums beendet");

        plenumSynchronization.closeDatabase();

        progressDialog.dismiss();

        return null;
    }

    /**
     * Once the news synchronize task has been executed, launch
     * the news activity.
     */
    @Override
    protected void onPostExecute(Void result)
    {
        Intent intent = new Intent();

        if ((DataHolder.isOnline((BaseActivity) activity)) && isPLenarySitting())
        {
            intent.setClass(activity, PlenumNewsActivity.class);
        }
        else
        {
            intent.setClass(activity, PlenumSitzungenActivity.class);
        }
        activity.startActivity(intent);
    }
    
    public boolean isPLenarySitting()
    {
        if ((this.mainPlenum != null) && (this.mainPlenum.getStatus().intValue() == 1))
        {
            return true;
        }

        return false;
    }
}
