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

  private NumberPicker timeoutAfterCmdTimePicker;

  private AlertDialog.Builder timeoutAfterCmdDialogBuilder;

  public void setupDialogBuilder(Activity activity) {
    timeoutAfterCmdDialogBuilder = new AlertDialog.Builder(activity);
    timeoutAfterCmdDialogBuilder.setTitle(R.string.timeout_after_cmd_dialog_title);
    View dialogView = activity.getLayoutInflater()
        .inflate(R.layout.timeout_after_cmd_select_dialog, null);
    timeoutAfterCmdDialogBuilder.setView(dialogView);
    timeoutAfterCmdTimePicker = (NumberPicker) dialogView
        .findViewById(R.id.timeout_after_cmd_time_picker);
    timeoutAfterCmdTimePicker.setMaxValue(100);
    timeoutAfterCmdTimePicker.setMinValue(0);
    timeoutAfterCmdTimePicker.setValue(3);
    timeoutAfterCmdTimePicker.setWrapSelectorWheel(false);
  }

  public int getTimeoutAfterCmdTimePickerValue() {
    return timeoutAfterCmdTimePicker.getValue();
  }

  public Builder setPositiveButton(int textId, final OnClickListener listener) {
    return timeoutAfterCmdDialogBuilder.setPositiveButton(textId, listener);
  }

  public Builder setNegativeButton(int textId, final OnClickListener listener) {
    return timeoutAfterCmdDialogBuilder.setNegativeButton(textId, listener);
  }

  public void show() {
    timeoutAfterCmdDialogBuilder.create().show();
  }
}
