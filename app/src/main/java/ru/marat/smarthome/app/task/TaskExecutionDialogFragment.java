package ru.marat.smarthome.app.task;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.TextView;
import ru.marat.smarthome.R;

public class TaskExecutionDialogFragment extends DialogFragment {

  private Context context;
  private ProgressDialog progressDialog;
  private int queueSize;

  static TaskExecutionDialogFragment newInstance(int queueSize) {
    TaskExecutionDialogFragment taskExecutionDialogFragment = new TaskExecutionDialogFragment();

    Bundle args = new Bundle();
    args.putInt("queueSize", queueSize);
    taskExecutionDialogFragment.setArguments(args);

    return taskExecutionDialogFragment;
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setRetainInstance(true);
    queueSize = getArguments().getInt("queueSize");
  }

  @Override
  public void onDestroyView() {
    if (getDialog() != null && getRetainInstance()) {
      getDialog().setDismissMessage(null);
    }
    super.onDestroyView();
  }

  @Override
  public void onAttach(Context context) {
    super.onAttach(context);
    this.context = context;
  }

  @Override
  public Dialog onCreateDialog(Bundle savedInstanceState) {
    if (progressDialog == null) {
      progressDialog = new ProgressDialog(context);
      progressDialog.setTitle(R.string.scenario_execution);
      progressDialog.setCancelable(true);
      progressDialog.setOnCancelListener(this);
      progressDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancel",
          new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
              dialog.cancel();
            }
          });
      if (queueSize > 0) {
        progressDialog.setMax(queueSize);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setIndeterminate(false);
      } else {
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setProgressNumberFormat(null);
        progressDialog.setProgressPercentFormat(null);
        progressDialog.setIndeterminate(true);
      }
    }
    return progressDialog;
  }

  public void clearDialog() {
    TextView progressMessage = (TextView) getDialog()
        .findViewById(R.id.scnr_exec_dialog_message);
    progressMessage.setText("");
    progressDialog.setProgress(0);
  }

  public void updateProgress(int value) {
    ProgressDialog dialog = (ProgressDialog) getDialog();
    if (dialog != null) {
      dialog.setProgress(value);
    }
  }

  public int getProgress() {
    ProgressDialog dialog = (ProgressDialog) getDialog();
    if (dialog != null) {
      return dialog.getProgress();
    }
    return 0;
  }

  public int updateProgressMessage(String text) {
    Log.d("cmd: ", text);
    ProgressDialog dialog = (ProgressDialog) getDialog();
    if (dialog != null) {
      TextView progressMessage = (TextView) dialog
          .findViewById(R.id.scnr_exec_dialog_message);
      if (progressMessage == null) {
        prepareCustomDialogView();
        progressMessage = (TextView) dialog
            .findViewById(R.id.scnr_exec_dialog_message);
      }
      progressMessage
          .append((progressMessage.getText().length() == 0 ? "" : "\n\n")
              + text);
    }
    return 0;
  }

  public void prepareCustomDialogView() {
    LayoutInflater inflater = ((AppCompatActivity) context).getLayoutInflater();
    FrameLayout fl = (FrameLayout) getDialog().findViewById(android.R.id.custom);
    fl.addView(inflater.inflate(R.layout.scnr_exec_dialog_view, null),
        new LayoutParams(MATCH_PARENT, WRAP_CONTENT));
    TextView progressMessage = (TextView) getDialog()
        .findViewById(R.id.scnr_exec_dialog_message);
    progressMessage.setMovementMethod(new ScrollingMovementMethod());
  }
}
