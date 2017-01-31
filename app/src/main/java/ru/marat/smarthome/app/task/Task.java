package ru.marat.smarthome.app.task;

import android.content.Context;
import android.content.res.Resources;
import android.os.AsyncTask;

public abstract class Task<Params, Progress, Result> extends AsyncTask<Params, Progress, Result> {

  protected final Resources resources;

  private Result result;
  private Progress progressMessage;
  private TaskProgressTracker taskProgressTracker;

  /* UI Thread */
  public Task(Context context) {
    // Keep reference to resources
    this.resources = context.getResources();
  }

  /* UI Thread */
  public void setTaskProgressTracker(TaskProgressTracker taskProgressTracker) {
    // Attach to progress tracker
    this.taskProgressTracker = taskProgressTracker;
    // Initialise progress tracker with current task state
    if (this.taskProgressTracker != null) {
      this.taskProgressTracker.onProgress(progressMessage);
      if (result != null) {
        this.taskProgressTracker.onComplete();
      }
    }
  }

  /* UI Thread */
  @Override
  protected void onCancelled() {
    // Detach from progress tracker
    taskProgressTracker = null;
  }

  /* UI Thread */
  @Override
  protected void onProgressUpdate(Progress... values) {
    // Update progress message
    progressMessage = values[0];
    // And send it to progress tracker
    if (taskProgressTracker != null) {
      taskProgressTracker.onProgress(progressMessage);
    }
  }

  /* UI Thread */
  @Override
  protected void onPostExecute(Result result) {
    super.onPostExecute(result);
    // Update result
    this.result = result;
    // And send it to progress tracker
    if (taskProgressTracker != null) {
      taskProgressTracker.onComplete();
    }
    // Detach from progress tracker
    taskProgressTracker = null;
  }
//
//  /* Separate Thread */
//  @Override
//  protected Boolean doInBackground(Void... arg0) {
//    // Working in separate thread
//    for (int i = 10; i > 0; --i) {
//      // Check if task is cancelled
//      if (isCancelled()) {
//        // This return causes onPostExecute call on UI thread
//        return false;
//      }
//
//      try {
//        // This call causes onProgressUpdate call on UI thread
//        publishProgress(resources.getString(R.string.task_working, i));
//        Thread.sleep(1000);
//      } catch (InterruptedException e) {
//        e.printStackTrace();
//        // This return causes onPostExecute call on UI thread
//        return false;
//      }
//    }
//    // This return causes onPostExecute call on UI thread
//    return true;
//  }
}