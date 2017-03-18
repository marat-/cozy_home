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

package ru.marat.smarthome.command.edit;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.List;
import ru.marat.smarthome.R;
import ru.marat.smarthome.app.adapter.CustomArrayAdapter;
import ru.marat.smarthome.model.Controller;

/**
 * Custom ArrayAdapter for spinner in ControllerEditActivity
 */
public class ControllerArrayAdapter extends CustomArrayAdapter<Controller> {

  public ControllerArrayAdapter(Context context, int viewResourceId,
      List<Controller> elemList) {
    super(context, viewResourceId, elemList);
  }

  /**
   * Custom view for spinner
   */
  public View getCustomView(int position, Controller controller, View view, View convertView,
      ViewGroup parent) {
    TextView cmdControllerName = (TextView) view.findViewById(R.id.cmd_controller_name);
    cmdControllerName.setText(controller.getName());

    TextView cmdControllerIpAddress = (TextView) view.findViewById(R.id.cmd_controller_ip_address);
    cmdControllerIpAddress.setText(controller.getIpAddress());

    TextView cmdControllerActive = (TextView) view.findViewById(R.id.cmd_controller_active);
    cmdControllerActive.setText(controller.isActive() ? "Active" : "Inactive");

    ImageView controllerImage = (ImageView) view.findViewById(R.id.cmd_controller_type_icon);
    String controllerImageValue = controller.getControllerType().getImage();
    if (controllerImageValue != null) {
      int imageResID = this.context.getResources()
          .getIdentifier(controllerImageValue, "drawable", this.context.getPackageName());
      controllerImage.setImageResource(imageResID);
    }

    return view;
  }
}
