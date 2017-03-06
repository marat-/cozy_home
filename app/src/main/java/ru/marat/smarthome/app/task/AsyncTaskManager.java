package ru.marat.smarthome.app.task;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.TextView;
import java.util.Arrays;
import java.util.LinkedList;
import org.apache.log4j.Logger;
import ru.marat.smarthome.R;
import ru.marat.smarthome.app.logger.ALogger;
import ru.marat.smarthome.app.task.exception.EmptyTaskQueueException;
import ru.marat.smarthome.app.task.impl.TimeoutTask;

public final class AsyncTaskManager implements TaskProgressTracker, OnCancelListener {

  private Logger logger = ALogger.getLogger(AsyncTaskManager.class);
  private final OnTaskCompleteListener taskCompleteListener;
  private ProgressDialog progressDialog;
  private Task asyncTask;
  private LinkedList<Task> taskQueue = new LinkedList<>();
  private Activity context;

  public AsyncTaskManager(Activity context, OnTaskCompleteListener taskCompleteListener) {
    this.context = context;
    // Save reference to complete listener (activity)
    this.taskCompleteListener = taskCompleteListener;
    // Setup progress dialog
    initProgressDialog();
  }

  /**
   * Initial setup of ProgressDialog
   */
  private void initProgressDialog() {
    progressDialog = new ProgressDialog(context);
    progressDialog.setIndeterminate(false);
    progressDialog.setTitle(R.string.scenario_execution);
    progressDialog.setCancelable(true);
    progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
    progressDialog.setOnCancelListener(this);
    progressDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancel",
        new DialogInterface.OnClickListener() {
          @Override
          public void onClick(DialogInterface dialog, int which) {
            dialog.dismiss();
          }
        });
  }

  /**
   * Accumulates tasks in scenario
   */
  public void submitTask(Task asyncTask, long timeoutAfter) {
    taskQueue.add(asyncTask);
    Task timeoutAfterTask = new TimeoutTask(context, Arrays
        .asList(timeoutAfter));
    taskQueue.add(timeoutAfterTask);
  }

  /**
   * Execute commands in scenario one by one with specified timeout
   */
  public void executeScenario() throws EmptyTaskQueueException {
    if (taskQueue.isEmpty()) {
      throw new EmptyTaskQueueException(context.getString(R.string.task_queue_empty_exception));
    }
    progressDialog.setMax(taskQueue.size());
    executeTask(taskQueue.poll());
  }

  public void terminateScenarioExec() {
    taskQueue.clear();
  }

  /**
   * Executes one task
   */
  public void executeTask(Task asyncTask) {
    // Keep task
    this.asyncTask = asyncTask;
    // Wire task to tracker (this)
    this.asyncTask.setTaskProgressTracker(this);
    // Start task
    this.asyncTask.execute(asyncTask.getParams().toArray());
  }

  @Override
  public void onProgress(Object progressUpdateData) {
    // Show dialog if it wasn't shown yet or was removed on configuration (rotation) change
    if (!progressDialog.isShowing()) {
      progressDialog.show();
      LayoutInflater inflater = context.getLayoutInflater();
      FrameLayout fl = (FrameLayout) progressDialog.findViewById(android.R.id.custom);
      fl.addView(inflater.inflate(R.layout.scnr_exec_dialog_view, null),
          new LayoutParams(MATCH_PARENT, WRAP_CONTENT));
      TextView progressMessage = (TextView) progressDialog
          .findViewById(R.id.scnr_exec_dialog_message);
      progressMessage.setMovementMethod(new ScrollingMovementMethod());
    }
    // Show current message in progress dialog
    if (progressUpdateData != null && progressUpdateData instanceof ProgressUpdateDataWrraper) {
      if (((ProgressUpdateDataWrraper) progressUpdateData).isUpdateProgressBar()) {
        progressDialog.setProgress(progressDialog.getProgress() + 1);
      }
      TextView progressMessage = (TextView) progressDialog
          .findViewById(R.id.scnr_exec_dialog_message);
      progressMessage
          .append((progressMessage.getText().length() == 0 ? "" : "\n\n")
              + ((ProgressUpdateDataWrraper) progressUpdateData).getMessage().toString());
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
    if (asyncTask.getResult().equals(TaskStatus.ERROR)) {
      terminateScenarioExec();
    } else {
      if (!taskQueue.isEmpty()) {
        Task asyncTask = taskQueue.poll();
        executeTask(asyncTask);
      } else {
        // Close progress dialog
        progressDialog.dismiss();
        // Notify activity about completion
        taskCompleteListener.onTaskComplete(asyncTask);
        // Reset task
        asyncTask = null;
      }
    }
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