
package com.app.feish.application.modelclassforapi;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ServicesWorkingTiming implements Serializable{

    @SerializedName("ServicesWorkingTiming")
    @Expose
    private ServicesWorkingTiming_ servicesWorkingTiming;
    @SerializedName("Service")
    @Expose
    private Service_ service;

    public ServicesWorkingTiming_ getServicesWorkingTiming() {
        return servicesWorkingTiming;
    }

    public void setServicesWorkingTiming(ServicesWorkingTiming_ servicesWorkingTiming) {
        this.servicesWorkingTiming = servicesWorkingTiming;
    }

    public Service_ getService() {
        return service;
    }

    public void setService(Service_ service) {
        this.service = service;
    }

}
