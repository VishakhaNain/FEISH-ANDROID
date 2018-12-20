
package com.app.feish.application.modelclassforapi;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Userdetail {

    @SerializedName("dr_specilization")
    @Expose
    private String drSpecilization;
    @SerializedName("Fee")
    @Expose
    private String fee;

    public String getDrSpecilization() {
        return drSpecilization;
    }

    public void setDrSpecilization(String drSpecilization) {
        this.drSpecilization = drSpecilization;
    }

    public String getFee() {
        return fee;
    }

    public void setFee(String fee) {
        this.fee = fee;
    }

}
