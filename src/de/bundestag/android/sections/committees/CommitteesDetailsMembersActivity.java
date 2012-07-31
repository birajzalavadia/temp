package de.bundestag.android.sections.committees;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import de.bundestag.android.BaseActivity;
import de.bundestag.android.R;
import de.bundestag.android.constant.AppConstant;

public class CommitteesDetailsMembersActivity extends BaseActivity
{
    public int committeesId;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState, this);

        Intent launchingIntent = getIntent();
        Bundle extras = launchingIntent.getExtras();
        int committeesId = (Integer) extras.get("index");
        this.committeesId = committeesId;

        setContentView(R.layout.committees_detail_list);
    }

    /**
     * Hack to handle the back button for committees news details.
     */
    public boolean onKeyDownSuper(int keyCode, KeyEvent event)
    {
        return super.onKeyDown(keyCode, event);
    }

    /**
     * Hack to handle the back button for committees news details.
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if (keyCode == KeyEvent.KEYCODE_BACK)
        {
            Intent intent = new Intent();
            intent.putExtra("index", this.committeesId);
            if(!AppConstant.isFragmentSupported){
            intent.setClass(this, CommitteesActivity.class);
            }else{
            intent.setClass(this, CommitteesActivityTablet.class);
            }
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            this.startActivity(intent);
            overridePendingTransition(0, 0);
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }
}