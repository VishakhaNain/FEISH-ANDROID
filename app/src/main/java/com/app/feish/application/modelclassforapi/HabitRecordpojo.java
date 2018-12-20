
package com.app.feish.application.modelclassforapi;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class HabitRecordpojo {

    @SerializedName("record")
    @Expose
    private List<Userhabitdetails> record = null;
    @SerializedName("norecord")
    @Expose
    private Integer norecord;
    @SerializedName("success")
    @Expose
    private Integer success;
    @SerializedName("data")
    @Expose
    private List<Mainhabitpojo> data = null;
    @SerializedName("message")
    @Expose
    private String message;

    public List<Userhabitdetails> getRecord() {
        return record;
    }

    public void setRecord(List<Userhabitdetails> record) {
        this.record = record;
    }

    public Integer getNorecord() {
        return norecord;
    }

    public void setNorecord(Integer norecord) {
        this.norecord = norecord;
    }

    public Integer getSuccess() {
        return success;
    }

    public void setSuccess(Integer success) {
        this.success = success;
    }

    public List<Mainhabitpojo> getData() {
        return data;
    }

    public void setData(List<Mainhabitpojo> data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
