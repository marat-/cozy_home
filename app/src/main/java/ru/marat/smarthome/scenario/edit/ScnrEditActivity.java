/*******************************************************************************
 * Copyright (c) 2017. Marat Shaidullin. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification, are permitted
 * provided that the following conditions are met: - Redistributions of source code must retain the
 * above copyright notice, this list of conditions and the following disclaimer. - Redistributions
 * in binary form must reproduce the above copyright notice, this list of conditions and the
 * following disclaimer in the documentation and/or other materials provided with the distribution.
 * - Neither the name of Oracle or the names of its contributors may be used to endorse or promote
 * products derived from this software without specific prior written permission. THIS SOFTWARE IS
 * PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES,
 * INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A
 * PARTICULAR PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA,
 * OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF
 * THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 ******************************************************************************/

package ru.marat.smarthome.scenario.edit;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.activeandroid.ActiveAndroid;
import com.activeandroid.query.Delete;
import com.activeandroid.query.Select;
import java.util.ArrayList;
import java.util.List;
import ru.marat.smarthome.R;
import ru.marat.smarthome.app.cab.OnActionModeListener;
import ru.marat.smarthome.app.core.BaseActivity;
import ru.marat.smarthome.app.validator.TextValidator;
import ru.marat.smarthome.command.edit.CmdEditActivity;
import ru.marat.smarthome.model.Cmd;
import ru.marat.smarthome.model.Scnr;
import ru.marat.smarthome.model.ScnrCmd;

public class ScnrEditActivity extends BaseActivity {

  @BindView(R.id.scnr_edit_name)
  EditText scnrEditName;

  @BindView(R.id.scnr_edit_active)
  CheckBox scnrEditActive;

  @BindView(R.id.add_cmd_to_scnr_fab)
  FloatingActionButton addCmdToScnrFab;

  @BindView(R.id.scnr_edit_cmd_list_view)
  ListView scnrEditCmdListView;

  private long scnrId;
  private List<ScnrCmd> cmdInScnr = new ArrayList<>();
  private ActionMode.Callback actionModeCallback;
  private ActionMode actionMode;

  private OnActionModeListener<Long> onActionModeListener;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_scnr_edit);
    ButterKnife.bind(this);

    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    addCmdToScnrFab.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        Intent intent = new Intent(ScnrEditActivity.this, CmdPickActivity.class);
        startActivityForResult(intent, 1);
      }
    });

    Intent intent = getIntent();
    scnrId = intent.getLongExtra("item_id", -1);

    if (scnrId != -1) {
      Scnr scnr = new Select()
          .from(Scnr.class).as("scnr")
          .where("scnr._id = ?", Long.toString(scnrId))
          .executeSingle();
      scnrEditName.setText(scnr.getName());
      scnrEditActive.setChecked(scnr.isActive());

      List<ScnrCmd> cmdInScnrFromDB = new Select()
          .from(ScnrCmd.class).as("scnr_cmd")
          .where("scnr_cmd.scnr_id = ?", Long.toString(scnrId))
          .orderBy("scnr_cmd.sort ASC").execute();
      cmdInScnr.addAll(cmdInScnrFromDB);
    }

    CmdInScnrArrayAdapter cmdTypeAdapter = new CmdInScnrArrayAdapter(ScnrEditActivity.this,
        R.layout.cmd_in_scnr_row,
        cmdInScnr);
    scnrEditCmdListView.setAdapter(cmdTypeAdapter);

    onActionModeListener = cmdTypeAdapter;

    validateFields();

    initCallbackActionMode();

    setUpContextualActionToolbar();
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    if (data == null) {
      return;
    }
    long cmdId = data.getLongExtra("cmd_id", -1);
    int timeoutAfterCmd = data.getIntExtra("timeout_after_cmd", -1);
    Cmd cmd = new Select().from(Cmd.class).where("_id = ?", new String[]{String.valueOf(cmdId)})
        .executeSingle();
    List<ScnrCmd> adapterList = ((CmdInScnrArrayAdapter) scnrEditCmdListView.getAdapter())
        .getList();
    ScnrCmd scnrCmd = new ScnrCmd(scnrId, cmd, timeoutAfterCmd, adapterList.size());
    adapterList.add(scnrCmd);
    ((CmdInScnrArrayAdapter) scnrEditCmdListView.getAdapter()).notifyDataSetChanged();
  }

  protected void validateFields() {
    scnrEditName.addTextChangedListener(new TextValidator(scnrEditName) {
      @Override
      public void validate(TextView textView, String text) {
        validataScnrEditName(textView, text);
      }
    });
  }

  private boolean validataScnrEditName(TextView textView, String text) {
    if (text != null && text.isEmpty()) {
      scnrEditName.setError(getString(R.string.cmd_edit_name_empty_error));
      return false;
    }
    return true;
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.scnr_edit_menu, menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    // Handle action bar item clicks here. The action bar will
    // automatically handle clicks on the Home/Up button, so long
    // as you specify a parent activity in AndroidManifest.xml.
    int id = item.getItemId();
    switch (id) {
      case R.id.scnr_edit_save_action:
        if (validataScnrEditName(scnrEditName, scnrEditName.getText().toString())) {
          ActiveAndroid.beginTransaction();
          try {
            Scnr scnr;
            if (scnrId != -1) {
              scnr = new Select().from(Scnr.class).where("_id = ?", Long.toString(scnrId))
                  .executeSingle();
            } else {
              scnr = new Scnr();
            }
            scnr.setName(scnrEditName.getText().toString());
            scnr.setActive(scnrEditActive.isChecked());

            scnr.save();
            long scnrId = scnr.getId();

            if (scnrId != -1) {
              new Delete().from(ScnrCmd.class).where("scnr_id = ?", scnrId).execute();
            }

            for (ScnrCmd scnrCmd : ((CmdInScnrArrayAdapter) scnrEditCmdListView.getAdapter())
                .getList()) {
              new ScnrCmd(scnrId, scnrCmd.getCmd(), scnrCmd.getTimeoutAfter(), scnrCmd.getSort())
                  .save();
            }
            ActiveAndroid.setTransactionSuccessful();
          } catch (Exception e) {
            Toast.makeText(this, getString(R.string.scnr_edit_save_to_db_error), Toast.LENGTH_SHORT)
                .show();
          } finally {
            ActiveAndroid.endTransaction();
            ScnrEditActivity.this.finish();
          }
        }
        return true;
      case R.id.scnr_edit_cancel_action:
        ScnrEditActivity.this.finish();
    }

    return super.onOptionsItemSelected(item);
  }

  /**
   * Binds contextual action toolbar on listview's long click
   */
  protected void setUpContextualActionToolbar() {
    scnrEditCmdListView.setOnItemLongClickListener(new OnItemLongClickListener() {
      @Override
      public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        if (actionMode != null) {
          return false;
        }

        // Start the CAB using the ActionMode.Callback defined above
        actionMode = startSupportActionMode(actionModeCallback);
        actionMode.setTag(id);
        view.setSelected(true);
        onActionModeListener.onCreateActionMode(id);
        ((CmdInScnrArrayAdapter) scnrEditCmdListView.getAdapter()).notifyDataSetChanged();

        return true;
      }
    });

    //setUpOnItemClickListener();
  }

  /**
   * Initialize callback action mode
   */
  private void initCallbackActionMode() {
    actionModeCallback = new ActionMode.Callback() {

      // Called when the action mode is created; startActionMode() was called
      @Override
      public boolean onCreateActionMode(ActionMode mode, Menu menu) {
        // Inflate a menu resource providing context muenu items
        MenuInflater inflater = mode.getMenuInflater();
        inflater.inflate(R.menu.cmd_in_scnr_edit_menu, menu);
        return true;
      }

      // Called each time the action mode is shown. Always called after onCreateActionMode, but
      // may be called multiple times if the mode is invalidated.
      @Override
      public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
        return false; // Return false if nothing is done
      }

      // Called when the user selects a contextual menu item
      @Override
      public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
        long scnrCmdOrder = (Long) mode.getTag();
        switch (item.getItemId()) {
          case R.id.cmd_in_scnr_edit_modify_action:
            ScnrCmd scnrCmd = ((CmdInScnrArrayAdapter) scnrEditCmdListView.getAdapter()).getList()
                .get((int) scnrCmdOrder);
            Intent intent = new Intent(ScnrEditActivity.this, CmdEditActivity.class);
            intent.putExtra("item_id", scnrCmd.getCmd().getId());
            startActivity(intent);
            mode.finish(); // Action picked, so close the CAB
            return true;
          case R.id.cmd_in_scnr_remove_action:
            ((CmdInScnrArrayAdapter) scnrEditCmdListView
                .getAdapter()).getList().remove((int) scnrCmdOrder);
            Toast
                .makeText(ScnrEditActivity.this, R.string.cmd_in_scnr_list_removed_cmd_successfully,
                    Toast.LENGTH_SHORT)
                .show();
            mode.finish(); // Action picked, so close the CAB
            return true;
          case R.id.cmd_in_scnr_list_change_after_cmd_timeout:
            scnrCmd = ((CmdInScnrArrayAdapter) scnrEditCmdListView.getAdapter()).getList()
                .get((int) scnrCmdOrder);
            showWaitTimeSelectDialog(scnrCmd, mode);
            return true;
          default:
            return false;
        }
      }

      // Called when the user exits the action mode
      @Override
      public void onDestroyActionMode(ActionMode mode) {
        actionMode = null;
        onActionModeListener.onDestroyActionMode();
        ((CmdInScnrArrayAdapter) scnrEditCmdListView.getAdapter()).notifyDataSetChanged();
      }
    };
  }

  public void showWaitTimeSelectDialog(final ScnrCmd scnrCmd, final ActionMode mode) {
    final TimeoutAfterCmdDialog timeoutAfterCmdDialog = new TimeoutAfterCmdDialog();
    timeoutAfterCmdDialog.setupDialogBuilder(this);
    timeoutAfterCmdDialog.setPositiveButton(R.string.timeout_after_cmd_set,
        new DialogInterface.OnClickListener() {
          @Override
          public void onClick(DialogInterface dialog, int which) {
            scnrCmd.setTimeoutAfter(timeoutAfterCmdDialog.getTimeoutAfterCmdTimePickerValue());
            ((CmdInScnrArrayAdapter) scnrEditCmdListView.getAdapter()).notifyDataSetChanged();
            mode.finish();
          }
        });
    timeoutAfterCmdDialog.setNegativeButton(R.string.timeout_after_cmd_cancel,
        new DialogInterface.OnClickListener() {
          @Override
          public void onClick(DialogInterface dialog, int which) {

          }
        });

    timeoutAfterCmdDialog.show();
  }

}
