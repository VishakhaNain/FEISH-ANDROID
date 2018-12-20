package com.app.feish.application.Patient;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class AvailableTimeSlot {
    @SerializedName("available_time_slot")
    @Expose
    private List<String> availableTimeSlot = null;
    @SerializedName("error")
    @Expose
    private Boolean error;

    public List<String> getAvailableTimeSlot() {
        return availableTimeSlot;
    }

    public void setAvailableTimeSlot(List<String> availableTimeSlot) {
        this.availableTimeSlot = availableTimeSlot;
    }

    public Boolean getError() {
        return error;
    }

    public void setError(Boolean error) {
        this.error = error;
    }
}
