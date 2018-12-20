package com.app.feish.application.modelclassforapi;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Contact_verify_email {

    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("data")
    @Expose
    private Verify_email_data data;
    @SerializedName("status")
    @Expose
    private Boolean status;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Verify_email_data getData() {
        return data;
    }

    public void setData(Verify_email_data data) {
        this.data = data;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

}