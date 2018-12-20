package com.app.feish.application.model;

import java.io.Serializable;
import java.util.ArrayList;

public class addhabitpojo implements Serializable {
   private String unit,timeperiod,habittitle;
   private Boolean habit;
   int id,frequency,habitsince,isstop,useredit;

    public addhabitpojo(int id,Boolean habit,String habittitle, int frequency, String unit, int habitsince, String timeperiod, int isstop,int useredit) {
        this.habit = habit;
        this.habittitle=habittitle;
        this.frequency = frequency;
        this.unit = unit;
        this.habitsince = habitsince;
        this.timeperiod = timeperiod;
        this.isstop = isstop;
        this.id=id;
        this.useredit=useredit;
    }

    public String getHabittitle() {
        return habittitle;
    }

    public int getUseredit() {
        return useredit;
    }

    public void setUseredit(int useredit) {
        this.useredit = useredit;
    }

    public int getId() {
        return id;
    }

    public void setFrequency(int frequency) {
        this.frequency = frequency;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public void setHabitsince(int habitsince) {
        this.habitsince = habitsince;
    }

    public void settimeperiod(String stopwhen) {
        this.timeperiod = stopwhen;
    }

    public void setHabit(Boolean habit) {
        this.habit = habit;
    }

    public void setIsstop(int isstop) {
        this.isstop = isstop;
    }

    public int getFrequency() {
        return frequency;
    }

    public String getUnit() {
        return unit;
    }

    public int getHabitsince() {
        return habitsince;
    }

    public String gettimeperiod() {
        return timeperiod;
    }

    public Boolean getHabit() {
        return habit;
    }

    public int getIsstop() {
        return isstop;
    }
}
