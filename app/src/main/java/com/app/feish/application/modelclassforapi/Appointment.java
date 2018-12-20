
package com.app.feish.application.modelclassforapi;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Appointment implements Serializable{

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("appointed_timing")
    @Expose
    private String appointedTiming;
    @SerializedName("user_id")
    @Expose
    private String userId;
    @SerializedName("doctor_id")
    @Expose
    private String doctorId;
    @SerializedName("laboratory_id")
    @Expose
    private Object laboratoryId;
    @SerializedName("service_id")
    @Expose
    private String serviceId;
    @SerializedName("is_visited")
    @Expose
    private String isVisited;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("status_updated_by")
    @Expose
    private String statusUpdatedBy;
    @SerializedName("scheduled_date")
    @Expose
    private String scheduledDate;
    @SerializedName("patient_package_log_id")
    @Expose
    private Object patientPackageLogId;
    @SerializedName("reason")
    @Expose
    private Object reason;
    @SerializedName("upload_drugs")
    @Expose
    private Object uploadDrugs;
    @SerializedName("created")
    @Expose
    private String created;

    @SerializedName("modified")
    @Expose
    private String modified;

    @SerializedName("patient_arrival_time")
    @Expose
    private String patient_arrival_time;

    @SerializedName("patient_in_time")
    @Expose
    private String patient_in_time;

    @SerializedName("patient_out_time")
    @Expose
    private String patient_out_time;

    @SerializedName("patient_exit_time")
    @Expose
    private String patient_exit_time;

    @SerializedName("token_id")
    @Expose
    private String token_id;

    public String getPatient_arrival_time() {
        return patient_arrival_time;
    }

    public void setPatient_arrival_time(String patient_arrival_time) {
        this.patient_arrival_time = patient_arrival_time;
    }

    public String getPatient_in_time() {
        return patient_in_time;
    }

    public void setPatient_in_time(String patient_in_time) {
        this.patient_in_time = patient_in_time;
    }

    public String getPatient_out_time() {
        return patient_out_time;
    }

    public void setPatient_out_time(String patient_out_time) {
        this.patient_out_time = patient_out_time;
    }

    public String getPatient_exit_time() {
        return patient_exit_time;
    }

    public void setPatient_exit_time(String patient_exit_time) {
        this.patient_exit_time = patient_exit_time;
    }

    public String getToken_id() {
        return token_id;
    }

    public void setToken_id(String token_id) {
        this.token_id = token_id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAppointedTiming() {
        return appointedTiming;
    }

    public void setAppointedTiming(String appointedTiming) {
        this.appointedTiming = appointedTiming;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(String doctorId) {
        this.doctorId = doctorId;
    }

    public Object getLaboratoryId() {
        return laboratoryId;
    }

    public void setLaboratoryId(Object laboratoryId) {
        this.laboratoryId = laboratoryId;
    }

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    public String getIsVisited() {
        return isVisited;
    }

    public void setIsVisited(String isVisited) {
        this.isVisited = isVisited;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatusUpdatedBy() {
        return statusUpdatedBy;
    }

    public void setStatusUpdatedBy(String statusUpdatedBy) {
        this.statusUpdatedBy = statusUpdatedBy;
    }

    public String getScheduledDate() {
        return scheduledDate;
    }

    public void setScheduledDate(String scheduledDate) {
        this.scheduledDate = scheduledDate;
    }

    public Object getPatientPackageLogId() {
        return patientPackageLogId;
    }

    public void setPatientPackageLogId(Object patientPackageLogId) {
        this.patientPackageLogId = patientPackageLogId;
    }

    public Object getReason() {
        return reason;
    }

    public void setReason(Object reason) {
        this.reason = reason;
    }

    public Object getUploadDrugs() {
        return uploadDrugs;
    }

    public void setUploadDrugs(Object uploadDrugs) {
        this.uploadDrugs = uploadDrugs;
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
