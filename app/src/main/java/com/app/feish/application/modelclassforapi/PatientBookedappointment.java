
package com.app.feish.application.modelclassforapi;

import android.os.Parcelable;

import java.io.Serializable;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PatientBookedappointment implements Serializable {

    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("data")
    @Expose
    private List<Appodatum> appodata = null;
    @SerializedName("appointmentdata")
    @Expose
    private List<Appointmentdatum> appointmentdata = null;
    @SerializedName("status")
    @Expose
    private Boolean status;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<Appodatum> getAppodata() {
        return appodata;
    }

    public void setAppodata(List<Appodatum> appodata) {
        this.appodata = appodata;
    }

    public List<Appointmentdatum> getAppointmentdata() {
        return appointmentdata;
    }

    public void setAppointmentdata(List<Appointmentdatum> appointmentdata) {
        this.appointmentdata = appointmentdata;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

}
