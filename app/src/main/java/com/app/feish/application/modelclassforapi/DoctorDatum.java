package com.app.feish.application.modelclassforapi;

/**
 * Created by RahulReign on 27-03-2018.
 */


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;


public class DoctorDatum implements Serializable {

    @SerializedName("Service")
    @Expose
    private DoctorService service;
    @SerializedName("User")
    @Expose
    private UserForListAvailbleDoctor user;
    @SerializedName("0")
    @Expose
    private Doctor_0 _0;

    public DoctorService getService() {
        return service;
    }

    public void setService(DoctorService service) {
        this.service = service;
    }

    public Doctor_0 get0() {
        return _0;
    }

    public void set0( Doctor_0 _0) {
        this._0 = _0;
    }

    public UserForListAvailbleDoctor getUser() {
        return user;
    }

    public void setUser(UserForListAvailbleDoctor user) {
        this.user = user;
    }


}