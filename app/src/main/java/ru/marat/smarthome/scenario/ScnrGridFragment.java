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

package ru.marat.smarthome.scenario;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
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
import java.util.Arrays;
import java.util.List;
import org.apache.log4j.Logger;
import ru.marat.smarthome.R;
import ru.marat.smarthome.app.logger.ALogger;
import ru.marat.smarthome.app.task.AsyncTaskManager;
import ru.marat.smarthome.app.task.OnTaskCompleteListener;
import ru.marat.smarthome.app.task.Task;
import ru.marat.smarthome.app.task.exception.EmptyTaskQueueException;
import ru.marat.smarthome.app.task.impl.IrSenderTask;
import ru.marat.smarthome.command.edit.CmdEditActivity;
import ru.marat.smarthome.model.ScnrCmd;
import ru.marat.smarthome.scenario.edit.ScnrEditActivity;

public class ScnrGridFragment extends AbstractScnrListFragment implements
    OnTaskCompleteListener {

  private Logger logger = ALogger.getLogger(ScnrGridFragment.class);

  @BindView(R.id.scnr_list_grid_view)
  GridView scnrListGridView;

  @BindView(R.id.scnr_fab_menu_add_cmd)
  FloatingActionButton addCmdFabButton;

  @BindView(R.id.scnr_fab_menu_add_scnr)
  FloatingActionButton addScnrFabButton;

  private AsyncTaskManager asyncTaskManager;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setRetainInstance(true);
    if (asyncTaskManager == null) {
      asyncTaskManager = new AsyncTaskManager(getActivity(), this);
    }
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_scenario_grid, null);
    ButterKnife.bind(this, view);
    return view;
  }

  @Override
  public void onAttach(Context context) {
    super.onAttach(context);
    if (asyncTaskManager != null) {
      asyncTaskManager.setContext((FragmentActivity) context);
    }
  }

  @Override
  public AbsListView getListView() {
    return scnrListGridView;
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
  public ScnrListCursorAdapter getCustomAdapter(Cursor cursor) {
    ScnrListCursorAdapter scnrListCursorAdapter;
    if (cursor != null) {
      scnrListCursorAdapter = new ScnrListCursorAdapter(
          this.getActivity(),
          R.layout.scnr_grid_row,
          cursor,
          CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);

      scnrListGridView.setAdapter(scnrListCursorAdapter);
    } else {
      scnrListCursorAdapter = (ScnrListCursorAdapter) scnrListGridView.getAdapter();
    }
    return scnrListCursorAdapter;
  }

  @Override
  public void setUpOnItemClickListener() {
    scnrListGridView.setOnItemClickListener(new OnItemClickListener() {
      @Override
      public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        List<ScnrCmd> cmdInScnrFromDB = new Select()
            .from(ScnrCmd.class).as("scnr_cmd")
            .where("scnr_cmd.scnr_id = ?", Long.toString(id))
            .orderBy("scnr_cmd.sort ASC").execute();
        for (ScnrCmd scnrCmd : cmdInScnrFromDB) {
          Task asyncTask = new IrSenderTask(getActivity(),
              Arrays
                  .asList(String
                      .format("http://%s/%s", scnrCmd.getCmd().getController().getIpAddress(),
                          scnrCmd.getCmd().getValue())));
          asyncTaskManager.submitTask(asyncTask, scnrCmd.getTimeoutAfter());
        }
        try {
          asyncTaskManager.executeScenario();
        } catch (EmptyTaskQueueException e) {
          logger.warn(e);
        }
      }
    });
  }

  @Override
  public void onTaskComplete(Task task) {
    if (task.isCancelled()) {
      // Report about cancel
      Toast.makeText(getActivity(), R.string.scenario_cancelled, Toast.LENGTH_LONG)
          .show();
    } else {
      // Report about result
      Toast.makeText(getActivity(),
          getString(R.string.scenario_completed),
          Toast.LENGTH_LONG).show();
    }
  }
}