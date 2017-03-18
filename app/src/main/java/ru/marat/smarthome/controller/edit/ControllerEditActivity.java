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

package ru.marat.smarthome.controller.edit;

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
import ru.marat.smarthome.model.Controller;
import ru.marat.smarthome.model.ControllerType;

public class ControllerEditActivity extends BaseActivity {

  @BindView(R.id.controller_edit_name)
  EditText controllerEditName;

  @BindView(R.id.controller_ip_address)
  EditText controllerEditIpAddress;

  @BindView(R.id.controller_edit_active)
  CheckBox controllerEditActive;

  @BindView(R.id.controller_edit_type)
  Spinner controllerEditType;

  private String controllerId;

  protected List<ControllerType> controllerTypeList;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_controller_edit);
    ButterKnife.bind(this);

    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    Intent intent = getIntent();
    controllerId = intent.getStringExtra("item_id");

    controllerTypeList = new Select().from(ControllerType.class).orderBy("active DESC").execute();
    ArrayAdapter controllerTypeAdapter = new ControllerTypeArrayAdapter(ControllerEditActivity.this,
        R.layout.controller_edit_spinner_row,
        controllerTypeList);
    controllerEditType.setAdapter(controllerTypeAdapter);
    if (controllerId != null) {
      Controller controller = new Select()
          .from(Controller.class).as("controller")
          .orderBy("controller.active DESC")
          .executeSingle();
      controllerEditName.setText(controller.getName());
      controllerEditIpAddress.setText(controller.getIpAddress());
      controllerEditActive.setChecked(controller.isActive());
      for (int i = 0; i < controllerEditType.getCount(); i++) {
        if (controllerTypeList.get(i).getId() == Long
            .valueOf(controller.getControllerType().getId())) {
          controllerEditType.setSelection(i);
        }
      }
    }

    validateFields();
  }

  protected void validateFields() {
    controllerEditName.addTextChangedListener(new TextValidator(controllerEditName) {
      @Override
      public void validate(TextView textView, String text) {
        validateControllerEditName(textView, text);
      }
    });
  }

  private boolean validateControllerEditName(TextView textView, String text) {
    if (text != null && text.isEmpty()) {
      controllerEditName.setError(getString(R.string.controller_edit_name_empty_error));
      return false;
    }
    return true;
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.controller_edit_menu, menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    // Handle action bar item clicks here. The action bar will
    // automatically handle clicks on the Home/Up button, so long
    // as you specify a parent activity in AndroidManifest.xml.
    int id = item.getItemId();
    switch (id) {
      case R.id.controller_edit_save_action:
        if (validateControllerEditName(controllerEditName,
            controllerEditName.getText().toString())) {
          Controller controller;
          if (controllerId != null) {
            controller = new Select().from(Controller.class)
                .where("_id = ?", new String[]{controllerId})
                .executeSingle();
          } else {
            controller = new Controller();
          }
          controller.setName(controllerEditName.getText().toString());
          controller.setIpAddress(controllerEditIpAddress.getText().toString());
          controller.setActive(controllerEditActive.isChecked());

          controller.setControllerType(
              controllerTypeList.get(controllerEditType.getSelectedItemPosition()));
          controller.save();
          ControllerEditActivity.this.finish();
        }
        return true;
      case R.id.controller_edit_cancel_action:
        ControllerEditActivity.this.finish();
    }

    return super.onOptionsItemSelected(item);
  }

}
