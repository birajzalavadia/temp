package de.bundestag.android.synchronization;

import android.content.Context;
import android.os.AsyncTask;
import de.bundestag.android.constant.DataHolder;
import de.bundestag.android.helpers.ProgressDialog;

public abstract class BaseSynchronizeTask extends AsyncTask<Context, String, Void> {
	public Context activity;

	public ProgressDialog progressDialog;

	public android.app.ProgressDialog progress;

	public TaskDoneListener taskDoneListener;

	public CancelListener cancelListener;

	public void setOnCancelListener(CancelListener cancelListener) {
		this.cancelListener = cancelListener;
	}

	public void setOnTaskDoneListener(TaskDoneListener taskDoneListener) {
		this.taskDoneListener = taskDoneListener;
	}

	@Override
	protected void onProgressUpdate(String... values) {

		super.onProgressUpdate(values);

		if (values[0].equals("progress")) {
			// if (progress == null) {
			//
			// progress = android.app.ProgressDialog.show(activity, null,
			// "Daten werden geladen", true);
			//
			// } else {
			// progress.show();
			// }
			DataHolder.createProgressDialog(activity);
		} else {
			if (progressDialog == null) {
				progressDialog = new ProgressDialog(activity, values[0], "");
				progressDialog.show();
			} else {
				progressDialog.tvTitle.setText(values[0]);
			}
		}

	}

	/**
	 * Once the task has been executed, hide the alert and make check if we need
	 * to flag back that the task has been executed.
	 */
	@Override
	protected void onPostExecute(Void result) {
		taskDoneListener.onTaskDone();
		DataHolder.releaseScreenLock(DataHolder.currentActiviry);

	}

	public void dismissProgress() {
		DataHolder.dismissProgress();
		// progress.dismiss();
		DataHolder.releaseScreenLock(DataHolder.currentActiviry);

	}

	@Override
	protected void onCancelled() {
		if (cancelListener != null) {
			cancelListener.onCancel();
			DataHolder.releaseScreenLock(DataHolder.currentActiviry);
		}
	}

}
