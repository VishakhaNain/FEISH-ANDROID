package com.app.feish.application.clinic;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Doctors {
    @SerializedName("doctor_list")
    @Expose
    private List<DoctorList> doctorList = null;

    public List<DoctorList> getDoctorList() {
        return doctorList;
    }

    public void setDoctorList(List<DoctorList> doctorList) {
        this.doctorList = doctorList;
    }
}
