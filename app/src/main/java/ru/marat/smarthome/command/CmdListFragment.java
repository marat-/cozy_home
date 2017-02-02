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

import static android.app.Activity.RESULT_OK;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.activeandroid.query.Select;
import ru.marat.smarthome.R;
import ru.marat.smarthome.model.Cmd;

public class CmdListFragment extends AbstractCmdListFragment {

  @BindView(R.id.commands_list_view)
  ListView commandsListView;

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_cmd_list, null);
    ButterKnife.bind(this, view);
    return view;
  }

  @Override
  public AbsListView getListView() {
    return commandsListView;
  }

  @Override
  public void onViewCreated(View view, Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
  }

  @Override
  public CmdListCursorAdapter getCustomAdapter(Cursor cursor) {
    CmdListCursorAdapter cmdListCursorAdapter;
    if (cursor != null) {
      cmdListCursorAdapter = new CmdListCursorAdapter(
          this.getActivity(),
          R.layout.cmd_list_row,
          cursor,
          CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);

      commandsListView.setAdapter(cmdListCursorAdapter);
    } else {
      cmdListCursorAdapter = (CmdListCursorAdapter) commandsListView.getAdapter();
    }
    return cmdListCursorAdapter;
  }

  @Override
  public void setUpOnItemClickListener() {
    commandsListView.setOnItemClickListener(new OnItemClickListener() {
      @Override
      public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Cmd cmd = new Select().from(Cmd.class).where("_id = ?", new String[]{String.valueOf(id)})
            .executeSingle();
        Intent intent = new Intent();
        intent.putExtra("cmd_id", cmd.getId());
        getActivity().setResult(RESULT_OK, intent);
        getActivity().finish();
      }
    });
  }
}