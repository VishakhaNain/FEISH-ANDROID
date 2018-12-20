package com.app.feish.application.modelclassforapi;

/**
 * Created by RahulReign on 27-03-2018.
 */

import android.support.annotation.Nullable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Datum2 implements Serializable {

    @SerializedName("Service")
    @Expose
    @Nullable
    private ServiceData service;

    public ServiceData getService() {
        return service;
    }

    public void setService(ServiceData service) {
        this.service = service;
    }

}
