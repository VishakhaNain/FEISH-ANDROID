package com.app.feish.application.model;

public class bookedappointmentpatient {
  private   String dr_name,dr_loc,dr_date,dr_time,pat_id;
  private  int flag;

    public bookedappointmentpatient(String dr_name, String dr_loc, String dr_date, String dr_time,int flag,String pat_id) {
        this.dr_name = dr_name;
        this.dr_loc = dr_loc;
        this.dr_date = dr_date;
        this.dr_time = dr_time;
        this.flag=flag;
        this.pat_id=pat_id;
    }

    public String getPat_id() {
        return pat_id;
    }

    public void setPat_id(String pat_id) {
        this.pat_id = pat_id;
    }

    public String getDr_name() {
        return dr_name;
    }

    public String getDr_loc() {
        return dr_loc;
    }

    public String getDr_date() {
        return dr_date;
    }

    public String getDr_time() {
        return dr_time;
    }

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }
}
