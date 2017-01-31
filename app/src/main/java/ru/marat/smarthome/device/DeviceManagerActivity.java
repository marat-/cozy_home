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

package ru.marat.smarthome.device;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.CursorAdapter;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.activeandroid.ActiveAndroid;
import com.activeandroid.query.From;
import com.activeandroid.query.Select;
import ru.marat.smarthome.R;
import ru.marat.smarthome.app.core.BaseActivity;
import ru.marat.smarthome.device.edit.DeviceEditActivity;
import ru.marat.smarthome.model.Device;
import ru.marat.smarthome.model.DeviceType;

public class DeviceManagerActivity extends BaseActivity {

  @BindView(R.id.device_manager_list_view)
  ListView deviceManagerListView;

  private ActionMode.Callback actionModeCallback;
  private ActionMode actionMode;

  private CursorAdapter deviceListCursorAdapter;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_device_manager);
    ButterKnife.bind(this);

    Toolbar toolbar = (Toolbar) findViewById(R.id.device_manager_toolbar);
    setSupportActionBar(toolbar);
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    getSupportActionBar().setDisplayShowHomeEnabled(true);

    FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.add_device_fab);
    fab.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        Intent intent = new Intent(DeviceManagerActivity.this, DeviceEditActivity.class);
        startActivity(intent);
      }
    });

    initCallbackActionMode();

    setUpContextualActionToolbar();
  }

  /**
   * Binds contextual action toolbar on listview's long click
   */
  protected void setUpContextualActionToolbar() {
    deviceManagerListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
      @Override
      public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        if (actionMode != null) {
          return false;
        }

        // Start the CAB using the ActionMode.Callback defined above
        actionMode = DeviceManagerActivity.this.startSupportActionMode(actionModeCallback);
        actionMode.setTag(id);
        view.setSelected(true);
        return true;
      }
    });
  }

  @Override
  public void onResume() {
    super.onResume();
    fillDeviceListView();
  }

  /**
   * Query database and fill listview using CursorAdapter
   */
  protected void fillDeviceListView() {
    From query = new Select("device._id, device.name, device.active, device_type.image")
        .from(Device.class).as("device")
        .leftJoin(DeviceType.class).as("device_type")
        .on("device.type_id = device_type._id")
        .orderBy("device.active DESC");

    Cursor cursor = ActiveAndroid.getDatabase().rawQuery(query.toSql(), query.getArguments());

    deviceListCursorAdapter = new DeviceListCursorAdapter(
        this,
        R.layout.device_list_row,
        cursor,
        CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);

    deviceManagerListView.setAdapter(deviceListCursorAdapter);
  }

  /**
   * Initialize callback action mode
   */
  private void initCallbackActionMode() {
    actionModeCallback = new ActionMode.Callback() {

      // Called when the action mode is created; startActionMode() was called
      @Override
      public boolean onCreateActionMode(ActionMode mode, Menu menu) {
        // Inflate a menu resource providing context menu items
        MenuInflater inflater = mode.getMenuInflater();
        inflater.inflate(R.menu.device_manager_menu, menu);
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
        String deviceId = mode.getTag().toString();
        switch (item.getItemId()) {
          case R.id.modify_device:
            Intent intent = new Intent(DeviceManagerActivity.this, DeviceEditActivity.class);
            intent.putExtra("item_id", deviceId);
            startActivity(intent);
            mode.finish(); // Action picked, so close the CAB
            return true;
          case R.id.delete_device:
            Device.delete(Device.class, Long.valueOf(deviceId));
            fillDeviceListView();
            mode.finish(); // Action picked, so close the CAB
            return true;
          default:
            return false;
        }
      }

      // Called when the user exits the action mode
      @Override
      public void onDestroyActionMode(ActionMode mode) {
        actionMode = null;
      }
    };
  }
}
