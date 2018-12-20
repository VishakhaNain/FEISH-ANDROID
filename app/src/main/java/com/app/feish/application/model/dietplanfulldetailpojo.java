package com.app.feish.application.model;

import java.io.Serializable;
import java.util.ArrayList;

public class dietplanfulldetailpojo implements Serializable {
   private String planname,sdate,edate;
   private ArrayList<dietlistpojo> dietlistpojos;
   private int id;

    public dietplanfulldetailpojo(int id,String planname, String sdate, String edate, ArrayList<dietlistpojo> dietlistpojos) {
        this.planname = planname;
        this.sdate = sdate;
        this.edate = edate;
        this.dietlistpojos = dietlistpojos;
        this.id=id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPlanname() {
        return planname;
    }

    public void setPlanname(String planname) {
        this.planname = planname;
    }

    public String getSdate() {
        return sdate;
    }

    public void setSdate(String sdate) {
        this.sdate = sdate;
    }

    public String getEdate() {
        return edate;
    }

    public void setEdate(String edate) {
        this.edate = edate;
    }

    public ArrayList<dietlistpojo> getDietlistpojos() {
        return dietlistpojos;
    }

    public void setDietlistpojos(ArrayList<dietlistpojo> dietlistpojos) {
        this.dietlistpojos = dietlistpojos;
    }
}
