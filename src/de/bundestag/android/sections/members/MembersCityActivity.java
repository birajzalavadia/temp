package de.bundestag.android.sections.members;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import de.bundestag.android.BaseActivity;
import de.bundestag.android.R;

public class MembersCityActivity extends BaseActivity
{
	@Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState, this);
        setContentView(R.layout.members_city);
        
      
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
            intent.setClass(this, MembersListActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            this.startActivity(intent);
            overridePendingTransition(0,0);
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }
}
