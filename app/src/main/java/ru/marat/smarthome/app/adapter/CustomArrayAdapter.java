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

package ru.marat.smarthome.app.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import java.util.List;

public abstract class CustomArrayAdapter<T> extends ArrayAdapter<T> {

  protected Context context;
  protected List<T> elemList;
  protected int rowViewResourceId;

  public CustomArrayAdapter(Context context, int viewResourceId,
      List<T> elemList) {
    super(context, viewResourceId, elemList);
    this.context = context;
    this.elemList = elemList;
    this.rowViewResourceId = viewResourceId;
  }

  @Override
  public View getView(int position, View convertView, ViewGroup parent) {
    return this.prepareAndGetView(position, convertView, parent);
  }

  @Override
  public View getDropDownView(int position, View convertView,
      ViewGroup parent) {
    return this.prepareAndGetView(position, convertView, parent);
  }

  public View prepareAndGetView(int position, View convertView, ViewGroup parent) {
    LayoutInflater inflater = ((Activity) context).getLayoutInflater();
    View view = inflater.inflate(this.rowViewResourceId, parent, false);
    T value = elemList.get(position);
    return this.getCustomView(value, view, convertView, parent);
  }

  /**
   * Custom view for spinner
   */
  public abstract View getCustomView(T value, View view, View convertView, ViewGroup parent);
}
