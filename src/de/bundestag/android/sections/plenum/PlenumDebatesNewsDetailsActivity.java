package de.bundestag.android.sections.plenum;

import android.content.Intent;
import android.os.Bundle;
import de.bundestag.android.BaseActivity;
import de.bundestag.android.R;
import de.bundestag.android.fragments.GeneralDetailsFragment;
import de.bundestag.android.parser.objects.NewsDetailsObject;

public class PlenumDebatesNewsDetailsActivity extends BaseActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState, this);

        setContentView(R.layout.news_detail);

        Intent launchingIntent = getIntent();
        Bundle extras = launchingIntent.getExtras();
        int newsId = (Integer) extras.get("index");

        NewsDetailsObject newsDetailsObject = PlenumDebatesNewsDetailViewHelper.createNewsDetailsObject(newsId);

        GeneralDetailsFragment detailsFragment = (GeneralDetailsFragment) getSupportFragmentManager().findFragmentById(R.id.detailsFragment);

        detailsFragment.showPlenumDebatesNewsDetails(newsDetailsObject);
    }
}