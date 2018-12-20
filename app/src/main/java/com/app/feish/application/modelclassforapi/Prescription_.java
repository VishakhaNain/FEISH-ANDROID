
package com.app.feish.application.modelclassforapi;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Prescription_  implements Serializable{

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("diseasename")
    @Expose
    private String diseasename;
    @SerializedName("medicine_name")
    @Expose
    private String medicineName;
    @SerializedName("unit_qty")
    @Expose
    private String unitQty;
    @SerializedName("total_qty")
    @Expose
    private String totalQty;
    @SerializedName("comments")
    @Expose
    private String comments;
    @SerializedName("medicine_time")
    @Expose
    private String medicineTime;
    @SerializedName("medicine_type")
    @Expose
    private String medicineType;
    @SerializedName("after_meal")
    @Expose
    private String afterMeal;
    @SerializedName("things_to_do")
    @Expose
    private String thingsToDo;
    @SerializedName("appointment_id")
    @Expose
    private String appointmentId;
    @SerializedName("soap_id")
    @Expose
    private String soapId;
    @SerializedName("doctor_by")
    @Expose
    private String doctorBy;
    @SerializedName("patient_to")
    @Expose
    private String patientTo;
    @SerializedName("is_viewed")
    @Expose
    private String isViewed;
    @SerializedName("created")
    @Expose
    private String created;
    @SerializedName("modified")
    @Expose
    private String modified;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDiseasename() {
        return diseasename;
    }

    public void setDiseasename(String diseasename) {
        this.diseasename = diseasename;
    }

    public String getMedicineName() {
        return medicineName;
    }

    public void setMedicineName(String medicineName) {
        this.medicineName = medicineName;
    }

    public String getUnitQty() {
        return unitQty;
    }

    public void setUnitQty(String unitQty) {
        this.unitQty = unitQty;
    }

    public String getTotalQty() {
        return totalQty;
    }

    public void setTotalQty(String totalQty) {
        this.totalQty = totalQty;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getMedicineTime() {
        return medicineTime;
    }

    public void setMedicineTime(String medicineTime) {
        this.medicineTime = medicineTime;
    }

    public String getMedicineType() {
        return medicineType;
    }

    public void setMedicineType(String medicineType) {
        this.medicineType = medicineType;
    }

    public String getAfterMeal() {
        return afterMeal;
    }

    public void setAfterMeal(String afterMeal) {
        this.afterMeal = afterMeal;
    }

    public String getThingsToDo() {
        return thingsToDo;
    }

    public void setThingsToDo(String thingsToDo) {
        this.thingsToDo = thingsToDo;
    }

    public String getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(String appointmentId) {
        this.appointmentId = appointmentId;
    }

    public String getSoapId() {
        return soapId;
    }

    public void setSoapId(String soapId) {
        this.soapId = soapId;
    }

    public String getDoctorBy() {
        return doctorBy;
    }

    public void setDoctorBy(String doctorBy) {
        this.doctorBy = doctorBy;
    }

    public String getPatientTo() {
        return patientTo;
    }

    public void setPatientTo(String patientTo) {
        this.patientTo = patientTo;
    }

    public String getIsViewed() {
        return isViewed;
    }

    public void setIsViewed(String isViewed) {
        this.isViewed = isViewed;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public String getModified() {
        return modified;
    }

    public void setModified(String modified) {
        this.modified = modified;
    }

}
