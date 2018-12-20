package com.app.feish.application.model;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.Serializable;

public class patientplanmodel implements Serializable{
   private int id;
  private   String planname,planprice,planremainingvisit,plantotalvisit,planusedvisit,planid,doctor_id;
private JSONArray doctorspe,doctorname;
    public patientplanmodel(int id, String planname, String planprice, String planremainingvisit, String plantotalvisit, String planusedvisit, String planid, String doctor_id, JSONArray doctorname, JSONArray doctorspe) {
        this.id = id;
        this.planname = planname;
        this.planprice = planprice;
        this.planremainingvisit = planremainingvisit;
        this.plantotalvisit = plantotalvisit;
        this.planusedvisit = planusedvisit;
        this.planid = planid;
        this.doctor_id = doctor_id;
        this.doctorname = doctorname;
        this.doctorspe = doctorspe;
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

    public String getPlanprice() {
        return planprice;
    }

    public void setPlanprice(String planprice) {
        this.planprice = planprice;
    }

    public String getPlanremainingvisit() {
        return planremainingvisit;
    }

    public void setPlanremainingvisit(String planremainingvisit) {
        this.planremainingvisit = planremainingvisit;
    }

    public String getPlantotalvisit() {
        return plantotalvisit;
    }

    public void setPlantotalvisit(String plantotalvisit) {
        this.plantotalvisit = plantotalvisit;
    }

    public String getPlanusedvisit() {
        return planusedvisit;
    }

    public void setPlanusedvisit(String planusedvisit) {
        this.planusedvisit = planusedvisit;
    }

    public String getPlanid() {
        return planid;
    }

    public void setPlanid(String planid) {
        this.planid = planid;
    }

    public String getDoctor_id() {
        return doctor_id;
    }

    public void setDoctor_id(String doctor_id) {
        this.doctor_id = doctor_id;
    }

    public JSONArray getDoctorspe() {
        return doctorspe;
    }

    public void setDoctorspe(JSONArray doctorspe) {
        this.doctorspe = doctorspe;
    }

    public JSONArray getDoctorname() {
        return doctorname;
    }

    public void setDoctorname(JSONArray doctorname) {
        this.doctorname = doctorname;
    }
}
