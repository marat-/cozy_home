package ru.marat.smarthome.model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

@Table(name = "cmd_attr_value", id = "_id")
public class CmdAttrValue extends Model{
    @Column(name = "cmd_id")
    public String cmdId;

    @Column(name = "cmd_attr_id")
    public int cmdAttrId;

    @Column(name = "value")
    public int value;

    public String getCmdId() {
        return cmdId;
    }

    public void setCmdId(String cmdId) {
        this.cmdId = cmdId;
    }

    public int getCmdAttrId() {
        return cmdAttrId;
    }

    public void setCmdAttrId(int cmdAttrId) {
        this.cmdAttrId = cmdAttrId;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
}
