
package com.app.feish.application.modelclassforapi;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Userhabitdetails {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("user_id")
    @Expose
    private Integer userId;
    @SerializedName("habit_id")
    @Expose
    private Integer habitId;
    @SerializedName("frequency")
    @Expose
    private Integer frequency;
    @SerializedName("unit")
    @Expose
    private String unit;
    @SerializedName("time_period")
    @Expose
    private String timePeriod;
    @SerializedName("habit_since")
    @Expose
    private Integer habitSince;
    @SerializedName("is_stopped")
    @Expose
    private Integer isStopped;
    @SerializedName("stopped_date")
    @Expose
    private Object stoppedDate;
    @SerializedName("added_by")
    @Expose
    private Integer addedBy;
    @SerializedName("last_updated_by")
    @Expose
    private Integer lastUpdatedBy;
    @SerializedName("created")
    @Expose
    private String created;
    @SerializedName("modified")
    @Expose
    private String modified;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getHabitId() {
        return habitId;
    }

    public void setHabitId(Integer habitId) {
        this.habitId = habitId;
    }

    public Integer getFrequency() {
        return frequency;
    }

    public void setFrequency(Integer frequency) {
        this.frequency = frequency;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getTimePeriod() {
        return timePeriod;
    }

    public void setTimePeriod(String timePeriod) {
        this.timePeriod = timePeriod;
    }

    public Integer getHabitSince() {
        return habitSince;
    }

    public void setHabitSince(Integer habitSince) {
        this.habitSince = habitSince;
    }

    public Integer getIsStopped() {
        return isStopped;
    }

    public void setIsStopped(Integer isStopped) {
        this.isStopped = isStopped;
    }

    public Object getStoppedDate() {
        return stoppedDate;
    }

    public void setStoppedDate(Object stoppedDate) {
        this.stoppedDate = stoppedDate;
    }

    public Integer getAddedBy() {
        return addedBy;
    }

    public void setAddedBy(Integer addedBy) {
        this.addedBy = addedBy;
    }

    public Integer getLastUpdatedBy() {
        return lastUpdatedBy;
    }

    public void setLastUpdatedBy(Integer lastUpdatedBy) {
        this.lastUpdatedBy = lastUpdatedBy;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public String getModified() {
        return modified;
    }

    public void setModified(String modified) {
        this.modified = modified;
    }

}
