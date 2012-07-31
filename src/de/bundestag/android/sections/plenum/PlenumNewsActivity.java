package de.bundestag.android.sections.plenum;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import de.bundestag.android.BaseActivity;
import de.bundestag.android.R;
import de.bundestag.android.constant.AppConstant;
import de.bundestag.android.fragments.GeneralDetailsFragment;
import de.bundestag.android.parser.PlenumXMLParser;
import de.bundestag.android.parser.objects.PlenumObject;
import de.bundestag.android.sections.news.NewsActivity;
import de.bundestag.android.sections.news.NewsActivityTablet;

public class PlenumNewsActivity extends BaseActivity
{
    private PlenumObject plenumObject;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState, this);
        setContentView(R.layout.plenum_news);

        // checkPlenumNews();

        GeneralDetailsFragment detailsFragment = (GeneralDetailsFragment) getSupportFragmentManager().findFragmentById(R.id.detailsFragment);
        this.plenumObject = createPlenumNewsObject();
        detailsFragment.showPlenumNews(plenumObject);
    }

    @Override
    protected void onStart()
    {
        super.onStart();

        checkShowOptionsMenu(this);
    }

    private PlenumObject createPlenumNewsObject()
    {
        PlenumXMLParser plenumXMLParser = new PlenumXMLParser();

        PlenumObject news = null;
        try
        {
            news = plenumXMLParser.parseMain(PlenumXMLParser.MAIN_PLENUM_URL);
        } catch (Exception e)
        {
//            e.printStackTrace();
        }

        return news;
    }

    public void openDetails(View v)
    {

        if (plenumObject.getDetailsXML() != null && plenumObject.getDetailsXML().length() > 0)
        {
            Intent i = new Intent(this, PlenumNewsDetailsActivity.class);
            i.putExtra("plenumDetailsXML", plenumObject.getDetailsXML());
            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            this.startActivity(i);
        }
    }

    /**
     * Hack to handle the back button.
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if (keyCode == KeyEvent.KEYCODE_BACK)
        {
            Intent intent = new Intent();
            if(AppConstant.isFragmentSupported)
				intent.setClass(this, NewsActivityTablet.class);
			else
				intent.setClass(this, NewsActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            this.startActivity(intent);
            overridePendingTransition(0,0);
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }
}
