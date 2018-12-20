package com.app.feish.application.model;

import java.io.Serializable;

public class serviceworkinghours  {
   private String open_time,close_time;
   int id,day_of_week;

    public serviceworkinghours(String open_time, String close_time, int id, int day_of_week) {
        this.open_time = open_time;
        this.close_time = close_time;
        this.id = id;
        this.day_of_week = day_of_week;
    }

    public String getOpen_time() {
        return open_time;
    }

    public void setOpen_time(String open_time) {
        this.open_time = open_time;
    }

    public String getClose_time() {
        return close_time;
    }

    public void setClose_time(String close_time) {
        this.close_time = close_time;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getDay_of_week() {
        return day_of_week;
    }

    public void setDay_of_week(int day_of_week) {
        this.day_of_week = day_of_week;
    }
}
