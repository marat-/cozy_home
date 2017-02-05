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

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.List;
import ru.marat.smarthome.R;
import ru.marat.smarthome.app.adapter.CustomArrayAdapter;
import ru.marat.smarthome.model.Cmd;

/**
 * Custom ArrayAdapter for spinner in DeviceEditActivity
 */
public class CmdInScnrArrayAdapter extends CustomArrayAdapter<Cmd> {

  public CmdInScnrArrayAdapter(Context context, int viewResourceId,
      List<Cmd> elemList) {
    super(context, viewResourceId, elemList);
  }

  public View prepareAndGetView(int position, View convertView, ViewGroup parent) {
    View rowView = super.prepareAndGetView(position, convertView, parent);
    rowView.setOnDragListener(new ItemOnDragListener(getList().get(position), context));
    return rowView;
  }

  /**
   * Custom view for spinner
   */
  public View getCustomView(Cmd cmd, View view, View convertView, ViewGroup parent) {
    TextView cmdName = (TextView) view.findViewById(R.id.cmd_name);
    cmdName.setText(cmd.getName());

    ImageView cmdDeviceImage = (ImageView) view.findViewById(R.id.cmd_device_image);
    int imageResID = this.context.getResources()
        .getIdentifier(cmd.getDevice().getDeviceType().getImage(), "drawable",
            this.context.getPackageName());
    cmdDeviceImage.setImageResource(imageResID);

    return view;
  }
}
