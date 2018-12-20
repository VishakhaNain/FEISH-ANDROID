package com.app.feish.application.clinic;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class getclinicdetailsmessage {


    @SerializedName("clinicDetails")
    @Expose
    private getclinicdetailsmessage clinicDetails;
    @SerializedName("error")
    @Expose
    private Boolean error;
    @SerializedName("message")
    @Expose
    private String message;

    public getclinicdetailsmessage getClinicDetails() {
        return clinicDetails;
    }

    public void setClinicDetails(getclinicdetailsmessage clinicDetails) {
        this.clinicDetails = clinicDetails;
    }

    public Boolean getError() {
        return error;
    }

    public void setError(Boolean error) {
        this.error = error;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}


