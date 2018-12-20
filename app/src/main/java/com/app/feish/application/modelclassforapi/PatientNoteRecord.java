
package com.app.feish.application.modelclassforapi;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PatientNoteRecord {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("Subject")
    @Expose
    private String subject;
    @SerializedName("Description")
    @Expose
    private String description;
    @SerializedName("is_remender")
    @Expose
    private Boolean isRemender;
    @SerializedName("User_id")
    @Expose
    private String userId;
    @SerializedName("is_type")
    @Expose
    private String isType;
    @SerializedName("obj_type")
    @Expose
    private String objType;
    @SerializedName("no_of_day")
    @Expose
    private Object noOfDay;
    @SerializedName("month_date")
    @Expose
    private Object monthDate;
    @SerializedName("end_date")
    @Expose
    private String endDate;
    @SerializedName("hrs_detail")
    @Expose
    private String hrsDetail;
    @SerializedName("Date")
    @Expose
    private String date;
    @SerializedName("Time")
    @Expose
    private String time;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getIsRemender() {
        return isRemender;
    }

    public void setIsRemender(Boolean isRemender) {
        this.isRemender = isRemender;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getIsType() {
        return isType;
    }

    public void setIsType(String isType) {
        this.isType = isType;
    }

    public String getObjType() {
        return objType;
    }

    public void setObjType(String objType) {
        this.objType = objType;
    }

    public Object getNoOfDay() {
        return noOfDay;
    }

    public void setNoOfDay(Object noOfDay) {
        this.noOfDay = noOfDay;
    }

    public Object getMonthDate() {
        return monthDate;
    }

    public void setMonthDate(Object monthDate) {
        this.monthDate = monthDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getHrsDetail() {
        return hrsDetail;
    }

    public void setHrsDetail(String hrsDetail) {
        this.hrsDetail = hrsDetail;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

}
