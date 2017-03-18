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
import ru.marat.smarthome.model.Device;

/**
 * Custom ArrayAdapter for spinner in ControllerEditActivity
 */
public class DeviceArrayAdapter extends CustomArrayAdapter<Device> {

  public DeviceArrayAdapter(Context context, int viewResourceId,
      List<Device> elemList) {
    super(context, viewResourceId, elemList);
  }

  /**
   * Custom view for spinner
   */
  public View getCustomView(int position, Device device, View view, View convertView,
      ViewGroup parent) {
    TextView deviceName = (TextView) view.findViewById(R.id.device_name);
    deviceName.setText(device.getName());

    TextView deviceActive = (TextView) view.findViewById(R.id.device_active);
    deviceActive.setText(device.isActive() ? "Active" : "Inactive");

    ImageView deviceImage = (ImageView) view.findViewById(R.id.device_type_icon);
    String deviceImageValue = device.getDeviceType().getImage();
    if (deviceImageValue != null) {
      int imageResID = this.context.getResources()
          .getIdentifier(deviceImageValue, "drawable", this.context.getPackageName());
      deviceImage.setImageResource(imageResID);
    }

    return view;
  }
}
