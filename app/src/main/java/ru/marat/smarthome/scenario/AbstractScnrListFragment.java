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

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Toast;
import com.activeandroid.ActiveAndroid;
import com.activeandroid.query.Delete;
import com.activeandroid.query.From;
import com.activeandroid.query.Select;
import ru.marat.smarthome.R;
import ru.marat.smarthome.app.cab.OnActionModeListener;
import ru.marat.smarthome.model.Scnr;
import ru.marat.smarthome.model.ScnrCmd;
import ru.marat.smarthome.scenario.edit.ScnrEditActivity;

public abstract class AbstractScnrListFragment extends Fragment {

  private ActionMode.Callback actionModeCallback;
  private ActionMode actionMode;

  private OnActionModeListener<Long> onActionModeListener;

  public abstract AbsListView getListView();

  @Override
  public void onViewCreated(View view, Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);

    fillScnrListView();

    initCallbackActionMode();

    setUpContextualActionToolbar();
  }

  @Override
  public void onResume() {
    super.onResume();
    fillScnrListView();
  }

  protected void fillScnrListView() {
    From query = new Select(
        "scnr._id, scnr.name AS scnr_name, scnr.description AS scnr_description")
        .from(Scnr.class).as("scnr");

    Cursor cursor = ActiveAndroid.getDatabase().rawQuery(query.toSql(), query.getArguments());

    ScnrListCursorAdapter cmdCursorAdapter = getCustomAdapter(cursor);

    onActionModeListener = cmdCursorAdapter;
  }

  public abstract ScnrListCursorAdapter getCustomAdapter(Cursor cursor);

  /**
   * Binds contextual action toolbar on listview's long click
   */
  protected void setUpContextualActionToolbar() {
    getListView().setOnItemLongClickListener(new OnItemLongClickListener() {
      @Override
      public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        if (actionMode != null) {
          return false;
        }

        // Start the CAB using the ActionMode.Callback defined above
        actionMode = ((AppCompatActivity) getActivity()).startSupportActionMode(actionModeCallback);
        actionMode.setTag(id);
        view.setSelected(true);
        onActionModeListener.onCreateActionMode(id);
        getCustomAdapter(null).notifyDataSetChanged();
        return true;
      }
    });

    setUpOnItemClickListener();
  }

  public abstract void setUpOnItemClickListener();

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
        inflater.inflate(R.menu.scnr_list_menu, menu);
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
        long scnrId = Long.parseLong(mode.getTag().toString());
        switch (item.getItemId()) {
          case R.id.modify_scnr:
            Intent intent = new Intent(getActivity(), ScnrEditActivity.class);
            intent.putExtra("item_id", scnrId);
            startActivity(intent);
            mode.finish(); // Action picked, so close the CAB
            return true;
          case R.id.delete_scnr:
            ActiveAndroid.beginTransaction();
            try {
              Scnr.delete(Scnr.class, scnrId);
              new Delete().from(ScnrCmd.class).where("scnr_id = ?", scnrId).execute();
              fillScnrListView();
              ActiveAndroid.setTransactionSuccessful();
            } catch (Exception e) {
              Toast.makeText(getActivity(), getString(R.string.scnr_edit_delete_from_db_error),
                  Toast.LENGTH_SHORT)
                  .show();
            } finally {
              ActiveAndroid.endTransaction();
              mode.finish();
            }
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
        getCustomAdapter(null).notifyDataSetChanged();
      }
    };
  }
}