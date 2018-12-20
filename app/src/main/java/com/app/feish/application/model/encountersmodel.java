package com.app.feish.application.model;

public class encountersmodel {
  private   String app_id,ser_name,pa_id,pa_name,status,timing;
  private int flag;
  public String pid, appid,  rentry,  rexit,  drentry,  drexit, date;

    public encountersmodel(String app_id, String ser_name, String pa_id, String pa_name, String status, String timing,int flag) {
        this.app_id = app_id;
        this.ser_name = ser_name;
        this.pa_id = pa_id;
        this.pa_name = pa_name;
        this.status = status;
        this.timing = timing;
        this.flag=flag;
    }
   /* public encountersmodel(String pid, String appid, String rentry, String rexit, String drentry, String drexit,String date) {

    }*/

    public encountersmodel(String pid, String appid, String rentry, String rexit, String drentry, String drexit, String date)
    {
        this.pid = pid;
        this.appid = appid;
        this.rentry = rentry;
        this.rexit = rexit;
        this.drentry = drentry;
        this.drexit = drexit;
        this.date = date;
    }

    public String getApp_id() {
        return app_id;
    }

    public String getSer_name() {
        return ser_name;
    }

    public String getPa_id() {
        return pa_id;
    }

    public String getPa_name() {
        return pa_name;
    }

    public String getStatus() {
        return status;
    }

    public String getTiming() {
        return timing;
    }

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }
}
