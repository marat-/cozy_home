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
import java.util.List;
import java.util.concurrent.TimeUnit;
import org.apache.log4j.Logger;
import ru.marat.smarthome.R;
import ru.marat.smarthome.app.logger.ALogger;
import ru.marat.smarthome.app.task.ProgressUpdateDataWrraper;
import ru.marat.smarthome.app.task.Task;
import ru.marat.smarthome.app.task.TaskStatus;

public class TimeoutTask extends Task<Long, ProgressUpdateDataWrraper, TaskStatus> {

  private Context context;
  private Logger logger = ALogger.getLogger(TimeoutTask.class);

  public TimeoutTask(Context context, List<Long> params) {
    super(context, params);
    this.context = context;
  }

  @Override
  protected void onPreExecute() {
    super.onPreExecute();
  }

  @Override
  protected TaskStatus doInBackground(Long... timeout) {
    publishProgress(
        new ProgressUpdateDataWrraper(resources.getString(R.string.task_timeout_after)));
    try {
      Thread.sleep(TimeUnit.SECONDS.toMillis(timeout[0]));
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
      logger.warn(context.getString(R.string.scenario_execution_interrupted_exception), e);
    }
    publishProgress(
        new ProgressUpdateDataWrraper(resources.getString(R.string.task_ready_for_the_next_task),
            true));
    return TaskStatus.DONE;
  }

  @Override
  protected void onPostExecute(TaskStatus taskStatus) {
    super.onPostExecute(taskStatus);
  }
}
