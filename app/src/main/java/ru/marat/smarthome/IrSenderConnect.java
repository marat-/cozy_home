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

package ru.marat.smarthome;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class IrSenderConnect extends AsyncTask<String, Void, String> {

  private Context mContext;

  public IrSenderConnect(Context context) {
    super();
    this.mContext = context;
  }

  @Override
  protected void onPreExecute() {
    super.onPreExecute();
  }

  @Override
  protected String doInBackground(String... command) {
    BufferedReader reader = null;
    HttpURLConnection urlConnection = null;
    StringBuilder response = new StringBuilder();
    try {
      URL url = new URL(command[0]);
      urlConnection = (HttpURLConnection) url.openConnection();
      reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
      String line = null;
      while ((line = reader.readLine()) != null) {
        response.append(line + "\n");
      }
    } catch (MalformedURLException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    } finally {
      if (reader != null) {
        try {
          reader.close();
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
      if (urlConnection != null) {
        urlConnection.disconnect();
      }
    }
    return response.toString();
  }

  @Override
  protected void onPostExecute(String result) {
    super.onPostExecute(result);
    Toast.makeText(mContext, result,
        Toast.LENGTH_SHORT).show();
  }
}
