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

package ru.marat.smarthome.model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

@Table(name = "scnr_cmd", id = "_id")
public class ScnrCmd extends Model {

  @Column(name = "scnr_id")
  public long scnrId;

  @Column(name = "cmd_id")
  public Cmd cmd;

  @Column(name = "timeout_after")
  public int timeoutAfter;

  @Column(name = "sort")
  public int sort;

  public ScnrCmd(long scnrId, Cmd cmd, int timeoutAfter, int sort) {
    this.cmd = cmd;
    this.scnrId = scnrId;
    this.sort = sort;
    this.timeoutAfter = timeoutAfter;
  }

  public ScnrCmd() {
  }

  public long getScnrId() {
    return scnrId;
  }

  public void setScnrId(long scnrId) {
    this.scnrId = scnrId;
  }

  public Cmd getCmd() {
    return cmd;
  }

  public void setCmd(Cmd cmd) {
    this.cmd = cmd;
  }

  public int getTimeoutAfter() {
    return timeoutAfter;
  }

  public void setTimeoutAfter(int waitTime) {
    this.timeoutAfter = waitTime;
  }

  public int getSort() {
    return sort;
  }

  public void setSort(int sort) {
    this.sort = sort;
  }
}
