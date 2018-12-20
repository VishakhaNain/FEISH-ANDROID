package com.app.feish.application.modelclassforapi;

/**
 * Created by RahulReign on 27-03-2018.
 */

import android.util.Log;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ListServicesContact {

    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("status")
    @Expose
    private Boolean status;
    @SerializedName("data")
    @Expose
//    private Map<String, Object> data = new HashMap<String, Object>();
    private List<Datum2> data = null;
//    private List<Datum2> data = new ArrayList<Datum2>(0);

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public List<Datum2> getData() {
        return data;
    }

    public void setData(List<Datum2> data) {
            this.data = data;
    }

}