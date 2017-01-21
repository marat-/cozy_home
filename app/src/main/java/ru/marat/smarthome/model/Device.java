package ru.marat.smarthome.model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

@Table(name = "device", id = "_id")
public class Device extends Model{
    @Column(name = "name")
    public String name;

    @Column(name = "active")
    public boolean active;

    @Column(name = "type_id")
    public long type_id;

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getTypeId() {
        return type_id;
    }

    public void setTypeId(long type_id) {
        this.type_id = type_id;
    }
}
