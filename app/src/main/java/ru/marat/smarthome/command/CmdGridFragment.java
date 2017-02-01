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
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.Toast;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.activeandroid.query.Select;
import com.getbase.floatingactionbutton.FloatingActionButton;
import ru.marat.smarthome.R;
import ru.marat.smarthome.app.task.AsyncTaskManager;
import ru.marat.smarthome.app.task.OnTaskCompleteListener;
import ru.marat.smarthome.app.task.Task;
import ru.marat.smarthome.app.task.TaskStatus;
import ru.marat.smarthome.app.task.impl.IrSenderTask;
import ru.marat.smarthome.command.edit.CmdEditActivity;
import ru.marat.smarthome.model.Cmd;
import ru.marat.smarthome.scenario.edit.ScnrEditActivity;

public class CmdGridFragment extends AbstractCmdListFragment implements OnTaskCompleteListener {

  @BindView(R.id.cmd_fab_menu_add_cmd)
  FloatingActionButton addCmdFabButton;

  @BindView(R.id.cmd_fab_menu_add_scnr)
  FloatingActionButton addScnrFabButton;

  @BindView(R.id.commands_grid_view)
  GridView commandsGridView;

  private AsyncTaskManager<String> asyncTaskManager;

  public static final String irSenderIp = "192.168.1.204:7474";

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_cmd_grid, null);
    ButterKnife.bind(this, view);

    // Create manager and set this activity as context and listener
    asyncTaskManager = new AsyncTaskManager<>(getActivity(), this);
    // Handle task that can be retained before
    //asyncTaskManager.handleRetainedTask(getActivity().getLastNonConfigurationInstance());

    return view;
  }

  @Override
  public AbsListView getListView() {
    return commandsGridView;
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

    addScnrFabButton.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        Intent intent = new Intent(getActivity(), ScnrEditActivity.class);
        startActivity(intent);
      }
    });
  }

  @Override
  public CmdListCursorAdapter getCustomAdapter(Cursor cursor) {
    CmdListCursorAdapter cmdListCursorAdapter;
    if (cursor != null) {
      cmdListCursorAdapter = new CmdListCursorAdapter(
          this.getActivity(),
          R.layout.cmd_grid_row,
          cursor,
          CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);

      commandsGridView.setAdapter(cmdListCursorAdapter);
    } else {
      cmdListCursorAdapter = (CmdListCursorAdapter) commandsGridView.getAdapter();
    }
    return cmdListCursorAdapter;
  }

  @Override
  public void setUpOnItemClickListener() {
    commandsGridView.setOnItemClickListener(new OnItemClickListener() {
      @Override
      public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Cmd cmd = new Select().from(Cmd.class).where("_id = ?", new String[]{String.valueOf(id)})
            .executeSingle();
        asyncTaskManager.setupTask(new IrSenderTask(getActivity()),
            String.format("http://%s/%s", irSenderIp, cmd.getValue()));
      }
    });
  }

  @Override
  public void onTaskComplete(Task task) {
    IrSenderTask irSenderTask = (IrSenderTask) task;
    if (irSenderTask.isCancelled()) {
      // Report about cancel
      Toast.makeText(getActivity(), R.string.task_cancelled, Toast.LENGTH_LONG)
          .show();
    } else {
      // Get result
      TaskStatus result = null;
      try {
        result = irSenderTask.get();
      } catch (Exception e) {
        e.printStackTrace();
      }
      // Report about result
      Toast.makeText(getActivity(),
          getString(R.string.task_completed),
          Toast.LENGTH_LONG).show();
    }
  }
}