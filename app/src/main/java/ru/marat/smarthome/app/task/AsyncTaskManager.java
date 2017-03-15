package ru.marat.smarthome.app.task;

import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.support.v4.app.FragmentActivity;
import java.util.Arrays;
import java.util.LinkedList;
import org.apache.log4j.Logger;
import ru.marat.smarthome.R;
import ru.marat.smarthome.app.logger.ALogger;
import ru.marat.smarthome.app.task.exception.EmptyTaskQueueException;
import ru.marat.smarthome.app.task.impl.TimeoutTask;

public final class AsyncTaskManager implements TaskProgressTracker, OnCancelListener {

  private Logger logger = ALogger.getLogger(AsyncTaskManager.class);
  private OnTaskCompleteListener taskCompleteListener;
  private Task asyncTask;
  private LinkedList<Task> taskQueue = new LinkedList<>();
  private FragmentActivity context;
  private String progressDialogTag = "progressDialogTag";

  public AsyncTaskManager(FragmentActivity context, OnTaskCompleteListener taskCompleteListener) {
    this.context = context;
    // Save reference to complete listener (fragment)
    this.taskCompleteListener = taskCompleteListener;
    if (asyncTask != null) {
      this.asyncTask.setTaskProgressTracker(this);
    }
  }

  public void setContext(FragmentActivity context) {
    this.context = context;
  }

  /**
   * Initial setup of ProgressDialog
   */
  private void initProgressDialog(int queueSize) {
    TaskExecutionDialogFragment progressDialogFragment = (TaskExecutionDialogFragment) context
        .getSupportFragmentManager().findFragmentByTag(progressDialogTag);
    if (progressDialogFragment == null) {
      TaskExecutionDialogFragment.newInstance(queueSize)
          .show(context.getSupportFragmentManager(), progressDialogTag);
    } else {
      progressDialogFragment.clearDialog();
    }
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
    initProgressDialog(taskQueue.size());
    executeTask(taskQueue.poll());
  }

  public void terminateScenarioExec() {
    taskQueue.clear();
    if (asyncTask != null) {
      asyncTask.setResult(TaskStatus.CANCELLED);
    }
  }

  /**
   * Setup dialog and execute task
   */
  public void setupTask(Task asyncTask) {
    initProgressDialog(0);
    executeTask(asyncTask);
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
    // Show current message in progress dialog
    if (progressUpdateData != null && progressUpdateData instanceof ProgressUpdateDataWrraper) {
      TaskExecutionDialogFragment progressDialogFragment = (TaskExecutionDialogFragment) context
          .getSupportFragmentManager().findFragmentByTag(progressDialogTag);
      if (((ProgressUpdateDataWrraper) progressUpdateData).isUpdateProgressBar()) {
        progressDialogFragment.updateProgress(progressDialogFragment.getProgress() + 1);
      }
      progressDialogFragment.updateProgressMessage(
          ((ProgressUpdateDataWrraper) progressUpdateData).getMessage().toString());
    }
  }

  /**
   * On Cancel listener for Dialog
   */
  @Override
  public void onCancel(DialogInterface dialog) {
    // Cancel task
    asyncTask.cancel(true);
    // Cancel scenario execution
    terminateScenarioExec();
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
        TaskExecutionDialogFragment progressDialogFragment = (TaskExecutionDialogFragment) context
            .getSupportFragmentManager().findFragmentByTag(progressDialogTag);
        if (progressDialogFragment != null) {
          context.getSupportFragmentManager().beginTransaction().remove(progressDialogFragment)
              .commitAllowingStateLoss();
        }
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