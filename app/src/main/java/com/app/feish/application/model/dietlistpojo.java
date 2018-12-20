package com.app.feish.application.model;

import java.io.Serializable;
import java.util.ArrayList;

public class dietlistpojo implements Serializable {
   private String weekday,time,desc;
   private  int id;
   private String foodname, foodcalories,mealtime,foodid;

    public dietlistpojo(String weekday, String time, String desc,String foodname,String foodcalories,String mealtime,String foodid,int id) {
        this.weekday = weekday;
        this.time = time;
        this.desc = desc;
        this.foodcalories=foodcalories;
        this.foodname=foodname;
this.mealtime=mealtime;
this.foodid=foodid;
this.id=id;

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFoodid() {
        return foodid;
    }

    public void setFoodid(String foodid) {
        this.foodid = foodid;
    }

    public String getMealtime() {
        return mealtime;
    }

    public void setMealtime(String mealtime) {
        this.mealtime = mealtime;
    }

    public String getWeekday() {
        return weekday;
    }

    public void setWeekday(String weekday) {
        this.weekday = weekday;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getFoodname() {
        return foodname;
    }

    public void setFoodname(String foodname) {
        this.foodname = foodname;
    }

    public String getFoodcalories() {
        return foodcalories;
    }

    public void setFoodcalories(String foodcalories) {
        this.foodcalories = foodcalories;
    }
}
