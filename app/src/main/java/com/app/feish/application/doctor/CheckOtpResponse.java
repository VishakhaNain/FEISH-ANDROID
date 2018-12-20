package com.app.feish.application.doctor;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CheckOtpResponse {
    @SerializedName(value="errorStatus",alternate = {"error","status"})
    @Expose
    private Boolean errorStatus;
    @SerializedName("message")
    @Expose
    private String message;

    public Boolean getErrorStatus() {
        return errorStatus;
    }

    public void setErrorStatus(Boolean errorStatus) {
        this.errorStatus = errorStatus;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
