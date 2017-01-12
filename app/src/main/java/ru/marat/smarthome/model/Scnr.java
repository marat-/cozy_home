package ru.marat.smarthome.model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

@Table(name = "scnr", id = "_id")
public class Scnr extends Model{
    @Column(name = "name")
    public String name;

    @Column(name = "description")
    public String description;

    @Column(name = "sort")
    public int sort;

    @Column(name = "active")
    public int active;

    @Column(name = "date_create")
    public String date_create;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getSort() {
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }

    public int getActive() {
        return active;
    }

    public void setActive(int active) {
        this.active = active;
    }

    public String getDate_create() {
        return date_create;
    }

    public void setDate_create(String date_create) {
        this.date_create = date_create;
    }
}
