package com.app.feish.application.clinic;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DoctorList {

    @SerializedName("user_id")
    @Expose
    private Integer userId;

    @SerializedName("first_name")
    @Expose
    private String firstName;

    @SerializedName("last_name")
    @Expose
    private String lastName;

    @SerializedName("speciality")
    @Expose
    private String speciality;

    public DoctorList(Integer userId, String firstName, String lastName, String speciality) {
        this.userId = userId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.speciality = speciality;
    }

    public DoctorList(){}
    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getSpeciality() {
        return speciality;
    }
    public void setSpeciality(String speciality) {
        this.speciality = speciality;
    }

    @Override
    public String toString() {
        return "DoctorList{" +
                "userId=" + userId +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", speciality='" + speciality + '\'' +
                '}';
    }
}
