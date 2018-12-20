package com.app.feish.application.clinic;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ResponseDashboard {
    @SerializedName("clinicDetails")
    @Expose
    private ClinicDashboardDetails clinicDashboardDetails;

    @SerializedName("error")
    @Expose
    private Boolean error;

    @SerializedName("message")
    @Expose
    private String message;

    public ClinicDashboardDetails getClinicDashboardDetails() {
        return clinicDashboardDetails;
    }

    public void setClinicDashboardDetails(ClinicDashboardDetails clinicDashboardDetails) {
        this.clinicDashboardDetails = clinicDashboardDetails;
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
