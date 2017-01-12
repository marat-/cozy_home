package ru.marat.smarthome.model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

@Table(name = "scnr_cmd")
public class ScnrCmd extends Model{
    @Column(name = "scnr_id")
    public int scnrId;

    @Column(name = "cmd_id")
    public int cmdId;

    @Column(name = "wait_time")
    public int waitTime;

    @Column(name = "order")
    public int order;

    public int getScnrId() {
        return scnrId;
    }

    public void setScnrId(int scnrId) {
        this.scnrId = scnrId;
    }

    public int getCmdId() {
        return cmdId;
    }

    public void setCmdId(int cmdId) {
        this.cmdId = cmdId;
    }

    public int getWaitTime() {
        return waitTime;
    }

    public void setWaitTime(int waitTime) {
        this.waitTime = waitTime;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }
}
