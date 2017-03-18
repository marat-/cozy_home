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

package ru.marat.smarthome.app.task.impl;

import android.content.Context;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import org.apache.log4j.Logger;
import ru.marat.smarthome.R;
import ru.marat.smarthome.app.logger.ALogger;
import ru.marat.smarthome.app.task.ProgressUpdateDataWrraper;
import ru.marat.smarthome.app.task.Task;
import ru.marat.smarthome.app.task.TaskStatus;

public class IrSenderTask extends Task<String, ProgressUpdateDataWrraper, TaskStatus> {

  private Logger logger = ALogger.getLogger(IrSenderTask.class);
  private Context context;

  public IrSenderTask(Context context, List<String> params) {
    super(context, params);
    this.context = context;
  }

  @Override
  protected void onPreExecute() {
    super.onPreExecute();
  }

  @Override
  protected TaskStatus doInBackground(String... command) {
    TaskStatus taskStatus = TaskStatus.DONE;
    publishProgress(new ProgressUpdateDataWrraper(resources.getString(R.string.task_connecting)));
    BufferedReader reader = null;
    HttpURLConnection urlConnection = null;
    StringBuilder response = new StringBuilder();
    try {
      URL url = new URL(command[0]);
      urlConnection = (HttpURLConnection) url.openConnection();
      reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
      String line = null;
      while ((line = reader.readLine()) != null) {
        if (isCancelled()) {
          // This return causes onPostExecute call on UI thread
          return TaskStatus.ERROR;
        }
        response.append(line + "\n");
      }
      publishProgress(
          new ProgressUpdateDataWrraper(resources.getString(R.string.task_sent_command)));
    } catch (MalformedURLException e) {
      logger.error(R.string.task_malformed_url_exception, e);
      publishProgress(new ProgressUpdateDataWrraper(
          resources.getString(R.string.task_malformed_url_exception)));
      taskStatus = TaskStatus.ERROR;
    } catch (IOException e) {
      logger.error(R.string.task_io_exception, e);
      publishProgress(
          new ProgressUpdateDataWrraper(resources.getString(R.string.task_io_exception)));
      taskStatus = TaskStatus.ERROR;
    } finally {
      if (reader != null) {
        try {
          reader.close();
        } catch (IOException e) {
          logger.error(R.string.general_io_exception, e);
          taskStatus = TaskStatus.ERROR;
        }
      }
      if (urlConnection != null) {
        urlConnection.disconnect();
      }
    }
    publishProgress(
        new ProgressUpdateDataWrraper(
            taskStatus.equals(TaskStatus.DONE) ? resources.getString(R.string.task_completed)
                : resources.getString(R.string.task_completed_with_errors), true));
    return taskStatus;
  }
}
