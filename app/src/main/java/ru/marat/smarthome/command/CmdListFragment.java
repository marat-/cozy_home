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

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.activeandroid.ActiveAndroid;
import com.activeandroid.query.From;
import com.activeandroid.query.Select;
import com.getbase.floatingactionbutton.FloatingActionsMenu;
import ru.marat.smarthome.R;
import ru.marat.smarthome.model.Cmd;
import ru.marat.smarthome.model.CmdType;
import ru.marat.smarthome.model.Device;
import ru.marat.smarthome.model.DeviceType;

public class CmdListFragment extends Fragment {

  @BindView(R.id.cmd_list_fab_menu)
  FloatingActionsMenu mainFABMenu;

  @BindView(R.id.commands_grid_view)
  GridView commandsGridView;

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
    final com.getbase.floatingactionbutton.FloatingActionButton mainFABMenuEnable = (com.getbase.floatingactionbutton.FloatingActionButton) view
        .findViewById(R.id.main_fab_menu_enable);
    mainFABMenu.setEnabled(false);
    mainFABMenu.setOnFloatingActionsMenuUpdateListener(
        new FloatingActionsMenu.OnFloatingActionsMenuUpdateListener() {
          @Override
          public void onMenuExpanded() {
          }

          @Override
          public void onMenuCollapsed() {
          }
        });

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
}