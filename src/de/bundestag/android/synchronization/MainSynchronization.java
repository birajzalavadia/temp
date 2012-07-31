package de.bundestag.android.synchronization;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;

/**
 * Main synchronization class.
 * 
 * Knows how to handle synchronization of all content (Members, Committees and
 * Visitors).
 */
public class MainSynchronization implements TaskDoneListener, CancelListener {
	private Context context;

	private int taskDone = 1;

	public void synchronizeAll(Context context) {
		this.context = context;
		// Stop the screen orientation changing during an event
		switch (context.getResources().getConfiguration().orientation) {
		case Configuration.ORIENTATION_PORTRAIT:
			((Activity) context).setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
			break;
		case Configuration.ORIENTATION_LANDSCAPE:
			((Activity) context).setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
			break;
		}

		// ((Activity) context)
		// .setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);

		SynchronizeMembersTask membersTask = new SynchronizeMembersTask();
		membersTask.setOnTaskDoneListener(this);
		membersTask.setOnCancelListener(this);
		membersTask.execute(context);

		// DEBUG
		// taskDone = 2;
		// SynchronizePlenumTask plenumsTask = new SynchronizePlenumTask();
		// plenumsTask.setOnTaskDoneListener(this);
		// plenumsTask.execute(context);
	}

	@Override
	public void onTaskDone() {
		if (taskDone == 1) {
			SynchronizeCommitteesTask committeesTask = new SynchronizeCommitteesTask();
			committeesTask.setOnTaskDoneListener(this);
			committeesTask.setOnCancelListener(this);
			committeesTask.execute(context);
		} else if (taskDone == 2) {
			SynchronizePlenumTask plenumsTask = new SynchronizePlenumTask();
			plenumsTask.setOnTaskDoneListener(this);
			plenumsTask.setOnCancelListener(this);
			plenumsTask.execute(context);
		} else if (taskDone == 3) {
			SynchronizeVisitorsTask visitorsTask = new SynchronizeVisitorsTask();
			visitorsTask.setOnTaskDoneListener(this);
			visitorsTask.setOnCancelListener(this);
			visitorsTask.execute(context);
		} else if (taskDone == 4) {
			((Activity) context).setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
			try {
				Intent intent = ((Activity) context).getIntent();
				((Activity) context).finish();
				((Activity) context).startActivity(intent);
			} catch (Exception expt) {
				// Just to prevent an unexpected context
				expt.printStackTrace();
			}
		}
		// else if (taskDone == 4)
		// {
		// Intent intent = new Intent();
		// intent.setClass(context, NewsActivity.class);
		// context.startActivity(intent);
		// }

		taskDone++;
	}

	@Override
	public void onCancel() {
		// TODO Auto-generated method stub
		((Activity) context).setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
	}
}
