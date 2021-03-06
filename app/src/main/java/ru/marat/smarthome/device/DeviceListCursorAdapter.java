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

package ru.marat.smarthome.device;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import ru.marat.smarthome.R;

public class DeviceListCursorAdapter extends CursorAdapter {

  private Context context;
  private int layout;
  private final LayoutInflater inflater;

  public DeviceListCursorAdapter(Context context, int layout, Cursor c, int flags) {
    super(context, c, flags);
    this.layout = layout;
    this.context = context;
    this.inflater = LayoutInflater.from(context);
  }

  @Override
  public View newView(Context context, Cursor cursor, ViewGroup parent) {
    return inflater.inflate(layout, null);
  }

  @Override
  public void bindView(View view, Context context, Cursor cursor) {
    TextView deviceName = (TextView) view.findViewById(R.id.device_name);
    ImageView deviceImage = (ImageView) view.findViewById(R.id.device_image);

    int deviceNameIndex = cursor.getColumnIndexOrThrow("name");
    int deviceImageIndex = cursor.getColumnIndexOrThrow("image");

    deviceName.setText(cursor.getString(deviceNameIndex));
    String deviceImageValue = cursor.getString(deviceImageIndex);
    if (deviceImageValue != null) {
      int imageResID = this.context.getResources()
          .getIdentifier(cursor.getString(deviceImageIndex), "drawable",
              this.context.getPackageName());
      deviceImage.setImageResource(imageResID);
    }

    view.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {

      }
    });

    view.setOnLongClickListener(new View.OnLongClickListener() {
      @Override
      public boolean onLongClick(View v) {
        return false;
      }
    });
  }
}
