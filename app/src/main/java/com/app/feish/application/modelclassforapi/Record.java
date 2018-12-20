
package com.app.feish.application.modelclassforapi;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Record {

    @SerializedName("data")
    @Expose
    private List<Userdetail> data = null;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("maindata")
    @Expose
    private List<Maindatum> maindata = null;

    public List<Userdetail> getData() {
        return data;
    }

    public void setData(List<Userdetail> data) {
        this.data = data;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Maindatum> getMaindata() {
        return maindata;
    }

    public void setMaindata(List<Maindatum> maindata) {
        this.maindata = maindata;
    }

}
