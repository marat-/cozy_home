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

package ru.marat.smarthome.device.edit;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.List;
import ru.marat.smarthome.R;
import ru.marat.smarthome.model.DeviceType;

/**
 * Custom ArrayAdapter for spinner in ControllerEditActivity
 */
public class DeviceTypeArrayAdapter extends ArrayAdapter<DeviceType> {

  private Context context;
  private List<DeviceType> deviceTypeList;

  public DeviceTypeArrayAdapter(Context context, int viewResourceId,
      List<DeviceType> deviceTypeList) {
    super(context, viewResourceId, deviceTypeList);
    this.context = context;
    this.deviceTypeList = deviceTypeList;
  }

  @Override
  public View getView(int position, View convertView, ViewGroup parent) {
    return this.getCustomView(position, convertView, parent);
  }

  @Override
  public View getDropDownView(int position, View convertView,
      ViewGroup parent) {
    return this.getCustomView(position, convertView, parent);
  }

  /**
   * Custom view for spinner
   */
  public View getCustomView(int position, View convertView, ViewGroup parent) {
    DeviceType deviceType = deviceTypeList.get(position);
    LayoutInflater inflater = ((Activity) context).getLayoutInflater();
    View view = inflater.inflate(R.layout.device_edit_spinner_row, parent, false);

    TextView deviceTypeName = (TextView) view.findViewById(R.id.device_type_name);
    deviceTypeName.setText(deviceType.getName());

    TextView deviceTypeActive = (TextView) view.findViewById(R.id.device_type_active);
    deviceTypeActive.setText(deviceType.isActive() ? "Active" : "Inactive");

    ImageView deviceImage = (ImageView) view.findViewById(R.id.device_type_icon);
    String deviceImageValue = deviceType.getImage();
    if (deviceImageValue != null) {
      int imageResID = this.context.getResources()
          .getIdentifier(deviceImageValue, "drawable", this.context.getPackageName());
      deviceImage.setImageResource(imageResID);
    }
    return view;
  }
}
