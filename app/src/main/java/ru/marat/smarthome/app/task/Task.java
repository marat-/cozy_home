package ru.marat.smarthome.app.task;

import android.content.Context;
import android.content.res.Resources;
import android.os.AsyncTask;
import java.util.List;

public abstract class Task<Params, Progress, Result> extends AsyncTask<Params, Progress, Result> {

  protected final Resources resources;

  private Result result;
  private Progress progressUpdateData;
  private TaskProgressTracker taskProgressTracker;
  private List<Params> params;

  public Task(Context context, List<Params> params) {
    // Keep reference to resources
    this.resources = context.getResources();
    this.params = params;
  }

  public Result getResult() {
    return result;
  }

  public void setResult(Result result) {
    this.result = result;
  }

  public List<Params> getParams() {
    return params;
  }

  /* UI Thread */
  public void setTaskProgressTracker(TaskProgressTracker taskProgressTracker) {
    // Attach to progress tracker
    this.taskProgressTracker = taskProgressTracker;
    // Initialise progress tracker with current task state
    if (this.taskProgressTracker != null) {
      this.taskProgressTracker.onProgress(progressUpdateData);
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
    progressUpdateData = values[0];
    // And send it to progress tracker
    if (taskProgressTracker != null) {
      taskProgressTracker.onProgress(progressUpdateData);
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
}