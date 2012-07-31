package de.bundestag.android.sections.plenum;

import android.os.Bundle;
import de.bundestag.android.BaseActivity;
import de.bundestag.android.R;
import de.bundestag.android.fragments.GeneralDetailsFragment;
import de.bundestag.android.parser.PlenumXMLParser;
import de.bundestag.android.parser.objects.PlenumObject;

public class PlenumNewsDetailsActivity extends BaseActivity
{
	private PlenumObject plenumObject;
	
	
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState, this);
        setContentView(R.layout.news_detail);
        
        String plenumDetailsXML = getIntent().getExtras().getString("plenumDetailsXML");
        this.plenumObject = createPlenumDetailsObject(plenumDetailsXML);
        GeneralDetailsFragment detailsFragment = (GeneralDetailsFragment) getSupportFragmentManager().findFragmentById(R.id.detailsFragment);
        detailsFragment.showPlenumNewsDetails(plenumObject);
    }



    @Override
    protected void onStart()
    {
        super.onStart();

        checkShowOptionsMenu(this);
    }

    
    private PlenumObject createPlenumDetailsObject(String url)
    {
        PlenumXMLParser plenumXMLParser = new PlenumXMLParser();

        PlenumObject news = null;
        try
        {
            news = plenumXMLParser.parseMain(url);
        } catch (Exception e)
        {
//            e.printStackTrace();
        }

        return news;
    }

}
