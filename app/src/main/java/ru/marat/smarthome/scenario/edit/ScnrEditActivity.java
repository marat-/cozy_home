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

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.activeandroid.query.Select;
import ru.marat.smarthome.R;
import ru.marat.smarthome.app.core.BaseActivity;
import ru.marat.smarthome.app.validator.TextValidator;
import ru.marat.smarthome.model.Cmd;
import ru.marat.smarthome.model.Scnr;

public class ScnrEditActivity extends BaseActivity {

  @BindView(R.id.scnr_edit_name)
  EditText scnrEditName;

  @BindView(R.id.scnr_edit_active)
  CheckBox scnrEditActive;

  @BindView(R.id.add_cmd_to_scnr_fab)
  FloatingActionButton addCmdToScnrFab;

  private String scnrId;

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
        startActivity(intent);
      }
    });


    Intent intent = getIntent();
    scnrId = intent.getStringExtra("scnr_id");

    if (scnrId != null) {
      Scnr scnr = new Select()
          .from(Scnr.class).as("scnr")
          .where("scnr._id = ?", new String[]{scnrId})
          .executeSingle();
      scnrEditName.setText(scnr.getName());
      scnrEditActive.setChecked(scnr.isActive());
    }

    validateFields();
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
          Scnr scnr;
          if (scnrId != null) {
            scnr = new Select().from(Cmd.class).where("_id = ?", new String[]{scnrId})
                .executeSingle();
          } else {
            scnr = new Scnr();
          }
          scnr.setName(scnrEditName.getText().toString());
          scnr.setActive(scnrEditActive.isChecked());
          scnr.save();
          ScnrEditActivity.this.finish();
        }
        return true;
      case R.id.scnr_edit_cancel_action:
        ScnrEditActivity.this.finish();
    }

    return super.onOptionsItemSelected(item);
  }

}
