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

package ru.marat.smarthome.command;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.CursorAdapter;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.GridView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.activeandroid.ActiveAndroid;
import com.activeandroid.query.From;
import com.activeandroid.query.Select;
import com.getbase.floatingactionbutton.FloatingActionButton;
import ru.marat.smarthome.R;
import ru.marat.smarthome.command.edit.CmdEditActivity;
import ru.marat.smarthome.model.Cmd;
import ru.marat.smarthome.model.CmdType;
import ru.marat.smarthome.model.Device;
import ru.marat.smarthome.model.DeviceType;

public class CmdListFragment extends Fragment {

  @BindView(R.id.cmd_fab_menu_add_cmd)
  FloatingActionButton addCmdFabButton;

  @BindView(R.id.commands_grid_view)
  GridView commandsGridView;

  private ActionMode.Callback actionModeCallback;
  private ActionMode actionMode;

  private int selectedRow;

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_cmd_list, null);
    ButterKnife.bind(this, view);

    return view;
  }

  @Override
  public void onViewCreated(View view, Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);

    addCmdFabButton.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        Intent intent = new Intent(getActivity(), CmdEditActivity.class);
        startActivity(intent);
      }
    });

    fillCmdGridView();

    initCallbackActionMode();

    setUpContextualActionToolbar();
  }

  @Override
  public void onResume() {
    super.onResume();
    fillCmdGridView();
  }

  protected void fillCmdGridView() {
    From query = new Select(
        "cmd._id, cmd.name AS cmd_name, device.name AS device_name, cmd.value, device_type.image")
        .from(Cmd.class).as("cmd")
        .innerJoin(CmdType.class).as("cmd_type")
        .on("cmd.type_id=cmd_type._id")
        .innerJoin(Device.class).as("device")
        .on("cmd.device_id=device._id")
        .innerJoin(DeviceType.class).as("device_type")
        .on("device.type_id = device_type._id");

    Cursor cursor = ActiveAndroid.getDatabase().rawQuery(query.toSql(), query.getArguments());

    CursorAdapter cmdListCursorAdapter = new CmdListCursorAdapter(
        this.getActivity(),
        R.layout.cmd_grid_row,
        cursor,
        CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);

    commandsGridView.setAdapter(cmdListCursorAdapter);
  }

  /**
   * Binds contextual action toolbar on listview's long click
   */
  protected void setUpContextualActionToolbar() {
    commandsGridView.setOnItemLongClickListener(new OnItemLongClickListener() {
      @Override
      public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        if (actionMode != null) {
          return false;
        }

        // Start the CAB using the ActionMode.Callback defined above
        actionMode = ((AppCompatActivity) getActivity()).startSupportActionMode(actionModeCallback);
        actionMode.setTag(id);
        view.setSelected(true);
        view.setBackgroundResource(R.drawable.round_rect_shape_selected);
        selectedRow = position;
        return true;
      }
    });

    commandsGridView.setOnItemSelectedListener(new OnItemSelectedListener() {
      @Override
      public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        view.setBackgroundResource(R.drawable.round_rect_shape_selected);
      }

      @Override
      public void onNothingSelected(AdapterView<?> parent) {
        //view.setBackgroundResource(R.drawable.round_rect_shape);
      }
    });

    commandsGridView.setOnItemClickListener(new OnItemClickListener() {
      @Override
      public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        int a = 1;
      }
    });
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
        inflater.inflate(R.menu.cmd_list_menu, menu);
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
        String cmdId = mode.getTag().toString();
        switch (item.getItemId()) {
          case R.id.modify_cmd:
            Intent intent = new Intent(getActivity(), CmdEditActivity.class);
            intent.putExtra("item_id", cmdId);
            startActivity(intent);
            mode.finish(); // Action picked, so close the CAB
            return true;
          case R.id.delete_cmd:
            Cmd.delete(Cmd.class, Long.valueOf(cmdId));
            fillCmdGridView();
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
        commandsGridView.getChildAt(selectedRow).setBackgroundResource(R.drawable.round_rect_shape);
      }
    };
  }
}