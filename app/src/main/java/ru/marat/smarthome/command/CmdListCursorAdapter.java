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
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import ru.marat.smarthome.R;

public class CmdListCursorAdapter extends AbstractCmdCursorAdapter {

  public CmdListCursorAdapter(Context context, int layout, Cursor c, int flags) {
    super(context, layout, c, flags);
  }

  @Override
  public void bindCustomView(View view, Context context, Cursor cursor) {
    TextView cmdName = (TextView) view.findViewById(R.id.cmd_in_scnr_cmd_name);
    ImageView cmdDeviceImage = (ImageView) view.findViewById(R.id.cmd_in_scnr_device_image);

    int cmdDeviceIdIndex = cursor.getColumnIndexOrThrow("_id");
    int deviceImageIndex = cursor.getColumnIndexOrThrow("image");
    int cmdNameIndex = cursor.getColumnIndexOrThrow("cmd_name");

    cmdName.setText(cursor.getString(cmdNameIndex));
    int imageResID = this.context.getResources()
        .getIdentifier(cursor.getString(deviceImageIndex), "drawable",
            this.context.getPackageName());
    cmdDeviceImage.setImageResource(imageResID);

    if (cursor.getLong(cmdDeviceIdIndex) == selectedCmdId) {
      view.setBackgroundResource(R.drawable.round_grey_shape_selected);
    } else {
      view.setBackgroundResource(R.drawable.round_grey_shape);
    }
  }
}
