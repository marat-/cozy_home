package ru.marat.smarthome.scenario.edit;

import android.app.Activity;
import android.content.DialogInterface.OnClickListener;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AlertDialog.Builder;
import android.view.View;
import android.widget.NumberPicker;
import ru.marat.smarthome.R;

/**
 * Created by Марат on 11.02.2017.
 */

public class TimeoutAfterCmdDialog {

  private NumberPicker cmdWaitTimePicker;

  private AlertDialog.Builder waitTimeSelectDialogBuilder;

  public void setupDialogBuilder(Activity activity) {
    waitTimeSelectDialogBuilder = new AlertDialog.Builder(activity);
    waitTimeSelectDialogBuilder.setTitle(R.string.cmd_wait_time_select_dialog_title);
    View dialogView = activity.getLayoutInflater()
        .inflate(R.layout.cmd_wait_time_select_dialog, null);
    waitTimeSelectDialogBuilder.setView(dialogView);
    cmdWaitTimePicker = (NumberPicker) dialogView
        .findViewById(R.id.cmd_wait_time_picker);
    cmdWaitTimePicker.setMaxValue(100);
    cmdWaitTimePicker.setMinValue(0);
    cmdWaitTimePicker.setValue(3);
    cmdWaitTimePicker.setWrapSelectorWheel(false);
  }

  public int getCmdWaitTimePickerValue() {
    return cmdWaitTimePicker.getValue();
  }

  public Builder setPositiveButton(int textId, final OnClickListener listener) {
    return waitTimeSelectDialogBuilder.setPositiveButton(textId, listener);
  }

  public Builder setNegativeButton(int textId, final OnClickListener listener) {
    return waitTimeSelectDialogBuilder.setNegativeButton(textId, listener);
  }

  public void show() {
    waitTimeSelectDialogBuilder.create().show();
  }
}
