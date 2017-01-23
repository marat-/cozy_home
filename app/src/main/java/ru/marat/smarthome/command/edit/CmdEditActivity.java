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

package ru.marat.smarthome.command.edit;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.activeandroid.query.Select;
import java.util.List;
import ru.marat.smarthome.R;
import ru.marat.smarthome.app.core.BaseActivity;
import ru.marat.smarthome.app.validator.TextValidator;
import ru.marat.smarthome.model.Cmd;
import ru.marat.smarthome.model.CmdType;
import ru.marat.smarthome.model.Device;

public class CmdEditActivity extends BaseActivity {

  @BindView(R.id.cmd_edit_name)
  EditText cmdEditName;

  @BindView(R.id.cmd_edit_active)
  CheckBox cmdEditActive;

  @BindView(R.id.cmd_edit_type)
  Spinner cmdEditType;

  @BindView(R.id.cmd_edit_device)
  Spinner cmdEditDevice;

  @BindView(R.id.cmd_edit_value)
  EditText cmdEditValue;

  private String cmdId;

  protected List<CmdType> cmdTypeList;
  protected List<Device> deviceList;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_cmd_edit);
    ButterKnife.bind(this);

    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    Intent intent = getIntent();
    cmdId = intent.getStringExtra("item_id");

    cmdTypeList = new Select().from(CmdType.class).orderBy("active DESC").execute();
    ArrayAdapter cmdTypeAdapter = new CmdTypeArrayAdapter(CmdEditActivity.this,
        R.layout.cmd_type_edit_spinner_row,
        cmdTypeList);
    cmdEditType.setAdapter(cmdTypeAdapter);

    deviceList = new Select()
        .from(Device.class).as("device")
        .orderBy("active DESC")
        .execute();
    ArrayAdapter deviceAdapter = new DeviceArrayAdapter(CmdEditActivity.this,
        R.layout.cmd_device_edit_spinner_row,
        deviceList);
    cmdEditDevice.setAdapter(deviceAdapter);

    if (cmdId != null) {
      Cmd cmd = new Select()
          .from(Cmd.class).as("cmd")
          .where("cmd._id = ?", new String[]{cmdId})
          .executeSingle();
      cmdEditName.setText(cmd.getName());
      cmdEditActive.setChecked(cmd.isActive());
      for (int i = 0; i < cmdEditType.getCount(); i++) {
        if (cmdTypeList.get(i).getId() == Long.valueOf(cmd.getType().getId())) {
          cmdEditType.setSelection(i);
        }
      }
      for (int i = 0; i < cmdEditDevice.getCount(); i++) {
        if (deviceList.get(i).getId() == Long.valueOf(cmd.getDevice().getId())) {
          cmdEditDevice.setSelection(i);
        }
      }
      cmdEditValue.setText(cmd.getValue());
    }

    validateFields();
  }

  protected void validateFields() {
    cmdEditName.addTextChangedListener(new TextValidator(cmdEditName) {
      @Override
      public void validate(TextView textView, String text) {
        validataCmdEditName(textView, text);
      }
    });
  }

  private boolean validataCmdEditName(TextView textView, String text) {
    if (text != null && text.isEmpty()) {
      cmdEditName.setError(getString(R.string.cmd_edit_name_empty_error));
      return false;
    }
    return true;
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.device_edit_menu, menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    // Handle action bar item clicks here. The action bar will
    // automatically handle clicks on the Home/Up button, so long
    // as you specify a parent activity in AndroidManifest.xml.
    int id = item.getItemId();
    switch (id) {
      case R.id.device_edit_save_action:
        if (validataCmdEditName(cmdEditName, cmdEditName.getText().toString())) {
          Cmd cmd;
          if (cmdId != null) {
            cmd = new Select().from(Device.class).where("_id = ?", new String[]{cmdId})
                .executeSingle();
          } else {
            cmd = new Cmd();
          }
          cmd.setName(cmdEditName.getText().toString());
          cmd.setActive(cmdEditActive.isChecked());
          cmd.setType(cmdTypeList.get(cmdEditType.getSelectedItemPosition()));
          cmd.setDevice(deviceList.get(cmdEditDevice.getSelectedItemPosition()));
          cmd.setValue(cmdEditValue.getText().toString());
          cmd.save();
          CmdEditActivity.this.finish();
        }
        return true;
      case R.id.device_edit_cancel_action:
        CmdEditActivity.this.finish();
    }

    return super.onOptionsItemSelected(item);
  }

}
