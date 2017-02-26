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

import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import ru.marat.smarthome.R;

public class CmdGridCursorAdapter extends AbstractCmdCursorAdapter {

  public CmdGridCursorAdapter(Context context, int layout, Cursor c, int flags) {
    super(context, layout, c, flags);
  }

  @Override
  public void bindCustomView(View view, Context context, Cursor cursor) {
    TextView cmdName = (TextView) view.findViewById(R.id.cmd_in_scnr_cmd_name);
    TextView cmdDescription = (TextView) view.findViewById(R.id.cmd_description);
    ImageView deviceImage = (ImageView) view.findViewById(R.id.device_image);
    ImageView cmdImage = (ImageView) view.findViewById(R.id.cmd_image);

    int cmdDeviceIdIndex = cursor.getColumnIndexOrThrow("_id");
    int cmdDeviceNameIndex = cursor.getColumnIndexOrThrow("device_name");
    int deviceImageIndex = cursor.getColumnIndexOrThrow("image");
    int commandDescriptionIndex = cursor.getColumnIndexOrThrow("cmd_name");
    int cmdColorIndex = cursor.getColumnIndexOrThrow("color");

    cmdName.setText(cursor.getString(cmdDeviceNameIndex));
    cmdDescription.setText(cursor.getString(commandDescriptionIndex));
    int imageResID = this.context.getResources()
        .getIdentifier(cursor.getString(deviceImageIndex), "drawable",
            this.context.getPackageName());
    deviceImage.setImageResource(imageResID);

    if (cursor.getString(cmdColorIndex) != null) {
      LayerDrawable cmdDrawable = (LayerDrawable) ContextCompat
          .getDrawable(context, R.drawable.round_rect_shape_cmd_image);
      GradientDrawable cmdDrawableShape = (GradientDrawable) cmdDrawable
          .findDrawableByLayerId(R.id.cmd_image_shape);
      int color = Color.parseColor(cursor.getString(cmdColorIndex));
      cmdDrawableShape.setColor(color);
      cmdImage.setBackground(cmdDrawable);
    }

    if (cursor.getLong(cmdDeviceIdIndex) == selectedCmdId) {
      view.setBackgroundResource(R.drawable.round_rect_shape_selected);
    } else {
      view.setBackgroundResource(R.drawable.round_rect_shape);
    }
  }
}
