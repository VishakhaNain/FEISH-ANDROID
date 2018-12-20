package com.app.feish.application.model;

import com.app.feish.application.modelclassforapi.Register_data;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Clinic_registermodel {

    @SerializedName("error")
    @Expose
    private Boolean error;

    @SerializedName("message")
    @Expose
    private String message;

    @SerializedName("user")
    @Expose
    private Register_user user;

    @SerializedName("clinic")
    @Expose
    private Register_Clinic clinic;

    @SerializedName("token")
    @Expose
    private String token;

    public Boolean getMessage() {
        return error;
    }

    public void setMessage(Boolean error) {
        this.error = error;
    }

    public String getStatus() {
        return message;
    }

    public void setStatus(String message) {
        this.message = message;
    }

    public Register_user getUser() {
        return user;
    }

    public void setUser(Register_user user) {
        this.user = user;
    }

    public Register_Clinic getClinic() {
        return clinic;
    }

    public void setClinic(Register_Clinic clinic) {
        this.clinic = clinic;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
