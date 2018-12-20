package com.app.feish.application.clinic;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ClinicDashboardDetails {

    @SerializedName("id")
    @Expose
    private Integer id;

    @SerializedName("clinic_name")
    @Expose
    private String clinicName;

    @SerializedName("total_doc")
    @Expose
    private Integer totalDoc;

    @SerializedName("status")
    @Expose
    private Integer status;

    @SerializedName("doctor_list")
    @Expose
    private List<DoctorList> doctorList = null;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getClinicName() {
        return clinicName;
    }

    public void setClinicName(String clinicName) {
        this.clinicName = clinicName;
    }

    public Integer getTotalDoc() {
        return totalDoc;
    }

    public void setTotalDoc(Integer totalDoc) {
        this.totalDoc = totalDoc;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public List<DoctorList> getDoctorList() {
        return doctorList;
    }

    public void setDoctorList(List<DoctorList> doctorList) {
        this.doctorList = doctorList;
    }
}
