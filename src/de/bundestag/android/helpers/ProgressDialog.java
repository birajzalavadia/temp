package de.bundestag.android.helpers;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import de.bundestag.android.R;

public class ProgressDialog extends Dialog implements OnClickListener
{
    public TextView tvTitle;

    private String title;

    private Button cancelButton;

    public boolean cancelSynchronization;

    public ProgressDialog(Context context, String title, String description)
    {
        super(context);

        this.title = title;

        cancelSynchronization = false;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.progress_dialog);
        
        tvTitle = (TextView) findViewById(R.id.titleMain);
        tvTitle.setText(title);

        cancelButton = (Button) findViewById(R.id.cancelButton);
        cancelButton.setOnClickListener(this);
//        if ((this.context instanceof NewsStartActivity) || (this.context instanceof NewsActivity) || (this.context instanceof NewsDetailsActivity)
//                || (this.context instanceof DebateNewsActivity) || (this.context instanceof DebateNewsDetailsActivity)
//                || (this.context instanceof CommitteesDetailsNewsActivity) || (this.context instanceof CommitteesDetailsNewsDetailsActivity)
//                || (this.context instanceof VisitorsNewsActivity) || (this.context instanceof VisitorsNewsDetailsActivity))
//        {
//            cancelButton.setVisibility(View.GONE);
//        }
    }

    @Override
    public void onClick(View v)
    {
        cancelSynchronization = true;

        // TODO - figure out where we started and go back to that
//        if (this.context instanceof NewsStartActivity)
//        {
//            Intent debugIntent = new Intent();
//            debugIntent.setClass(this.context, MembersListActivity.class);
//            this.context.startActivity(debugIntent);
//        }

        dismiss();
    }
}
