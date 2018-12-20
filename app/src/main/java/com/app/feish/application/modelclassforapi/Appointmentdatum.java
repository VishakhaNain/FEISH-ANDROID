
package com.app.feish.application.modelclassforapi;

import java.io.Serializable;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Appointmentdatum implements Serializable {

    @SerializedName("Appointment")
    @Expose
    private Appointment appointment;
    @SerializedName("User")
    @Expose
    private User user;
    @SerializedName("Doctor")
    @Expose
    private Doctor doctor;
    @SerializedName("Service")
    @Expose
    private Serviceappo service;
    @SerializedName("StatusUpdateBy")
    @Expose
    private StatusUpdateBy statusUpdateBy;
    @SerializedName("PatientPackageLog")
    @Expose
    private PatientPackageLog patientPackageLog;
    @SerializedName("Prescription")
    @Expose
    private List<Prescription_> prescription = null;

    public Appointment getAppointment() {
        return appointment;
    }

    public void setAppointment(Appointment appointment) {
        this.appointment = appointment;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Doctor getDoctor() {
        return doctor;
    }

    public void setDoctor(Doctor doctor) {
        this.doctor = doctor;
    }

    public Serviceappo getService() {
        return service;
    }

    public void setService(Serviceappo service) {
        this.service = service;
    }

    public StatusUpdateBy getStatusUpdateBy() {
        return statusUpdateBy;
    }

    public void setStatusUpdateBy(StatusUpdateBy statusUpdateBy) {
        this.statusUpdateBy = statusUpdateBy;
    }

    public PatientPackageLog getPatientPackageLog() {
        return patientPackageLog;
    }

    public void setPatientPackageLog(PatientPackageLog patientPackageLog) {
        this.patientPackageLog = patientPackageLog;
    }

    public List<Prescription_> getPrescription() {
        return prescription;
    }

    public void setPrescription(List<Prescription_> prescription) {
        this.prescription = prescription;
    }
}
