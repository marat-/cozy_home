package ru.marat.smarthome.app.task;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import java.util.PriorityQueue;

public final class AsyncTaskManager<Params> implements TaskProgressTracker, OnCancelListener {

  private final OnTaskCompleteListener taskCompleteListener;
  private final ProgressDialog progressDialog;
  private Task asyncTask;
  private PriorityQueue<Task> taskQueue = new PriorityQueue();

  public AsyncTaskManager(Context context, OnTaskCompleteListener taskCompleteListener) {
    // Save reference to complete listener (activity)
    this.taskCompleteListener = taskCompleteListener;
    // Setup progress dialog
    progressDialog = new ProgressDialog(context);
    progressDialog.setIndeterminate(true);
    progressDialog.setCancelable(true);
    progressDialog.setOnCancelListener(this);
    progressDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancel",
        new DialogInterface.OnClickListener() {
          @Override
          public void onClick(DialogInterface dialog, int which) {
            dialog.dismiss();
          }
        });
  }

  public void submitTask(Task asyncTask) {

  }

  public void executeTask(Task asyncTask) {
    // Keep task
    this.asyncTask = asyncTask;
    // Wire task to tracker (this)
    this.asyncTask.setTaskProgressTracker(this);
    // Start task
    this.asyncTask.execute(asyncTask.getParams().toArray());
  }

  @Override
  public void onProgress(Object message) {
    // Show dialog if it wasn't shown yet or was removed on configuration (rotation) change
    if (!progressDialog.isShowing()) {
      progressDialog.show();
    }
    // Show current message in progress dialog
    if (message instanceof String) {
      progressDialog.setMessage(message.toString());
    }
  }

  @Override
  public void onCancel(DialogInterface dialog) {
    // Cancel task
    asyncTask.cancel(true);
    // Notify activity about completion
    taskCompleteListener.onTaskComplete(asyncTask);
    // Reset task
    asyncTask = null;
  }

  @Override
  public void onComplete() {
    // Close progress dialog
    progressDialog.dismiss();
    // Notify activity about completion
    taskCompleteListener.onTaskComplete(asyncTask);
    // Reset task
    asyncTask = null;
  }

  public Object retainTask() {
    // Detach task from tracker (this) before retain
    if (asyncTask != null) {
      asyncTask.setTaskProgressTracker(null);
    }
    // Retain task
    return asyncTask;
  }

  public void handleRetainedTask(Object instance) {
    // Restore retained task and attach it to tracker (this)
    if (instance instanceof Task) {
      asyncTask = (Task) instance;
      asyncTask.setTaskProgressTracker(this);
    }
  }

  public boolean isWorking() {
    // Track current status
    return asyncTask != null;
  }
}