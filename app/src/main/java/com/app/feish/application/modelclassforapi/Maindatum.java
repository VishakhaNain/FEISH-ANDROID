
package com.app.feish.application.modelclassforapi;

import java.io.Serializable;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Maindatum implements Serializable {

    @SerializedName("Service")
    @Expose
    private Service service;
    @SerializedName("ServicesWorkingTiming")
    @Expose
    private List<ServicesWorkingTiming> servicesWorkingTiming = null;

    public Service getService() {
        return service;
    }

    public void setService(Service service) {
        this.service = service;
    }

    public List<ServicesWorkingTiming> getServicesWorkingTiming() {
        return servicesWorkingTiming;
    }

    public void setServicesWorkingTiming(List<ServicesWorkingTiming> servicesWorkingTiming) {
        this.servicesWorkingTiming = servicesWorkingTiming;
    }

}
