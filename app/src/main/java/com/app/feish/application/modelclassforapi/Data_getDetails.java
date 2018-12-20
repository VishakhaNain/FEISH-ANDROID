package com.app.feish.application.modelclassforapi;

/**
 * Created by RahulReign on 28-03-2018.
 */

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Data_getDetails implements Serializable {

    @SerializedName("user_id")
    @Expose
    private String userId;
    @SerializedName("first_name")
    @Expose
    private String firstName;
    @SerializedName("last_name")
    @Expose
    private String lastName;
    @SerializedName("birth_date")
    @Expose
    private String birthDate;
    @SerializedName("avatar")
    @Expose
    private Object avatar;
    @SerializedName("qualification")
    @Expose
    private String qualification;
    @SerializedName("facebook")
    @Expose
    private Object facebook;
    @SerializedName("google_plus")
    @Expose
    private Object googlePlus;
    @SerializedName("twitter")
    @Expose
    private Object twitter;
    @SerializedName("address")
    @Expose
    private Object address;
    @SerializedName("occupation_id")
    @Expose
    private Object occupationId;
    @SerializedName("consultation_time")
    @Expose
    private String consultationTime;
    @SerializedName("ethnicity_id")
    @Expose
    private Object ethnicityId;
    @SerializedName("identity_id")
    @Expose
    private String identityId;
    @SerializedName("identity_type")
    @Expose
    private String identityType;
    @SerializedName("blood_group")
    @Expose
    private Object bloodGroup;
    @SerializedName("gender")
    @Expose
    private String gender;
    @SerializedName("marital_status")
    @Expose
    private Object maritalStatus;
    @SerializedName("state")
    @Expose
    private Object state;
    @SerializedName("city")
    @Expose
    private Object city;
    @SerializedName("email")
    @Expose
    private Object is_active;
    @SerializedName("is_active")
    @Expose
    private String email;
    @SerializedName("mobile")
    @Expose
    private String mobile;
    @SerializedName("salutation")
    @Expose
    private String salutation;
    @SerializedName("height")
    @Expose
    private Object height;
    @SerializedName("weight")
    @Expose
    private Object weight;
    @SerializedName("waist_size")
    @Expose
    private Object waistSize;
    @SerializedName("dr_specilization")
    @Expose
    private Object dr_specilization;
    @SerializedName("Fee")
    @Expose
    private Object Fee;
    @SerializedName("aboutDoctor")
    @Expose
    private Object aboutDoctor;

    public Object getAboutDoctor() {
        return aboutDoctor;
    }

    public void setAboutDoctor(Object aboutDoctor) {
        this.aboutDoctor = aboutDoctor;
    }

    public Object getFee() {
        return Fee;
    }

    public void setFee(Object fee) {
        Fee = fee;
    }

    public Object getDr_specilization() {
        return dr_specilization;
    }

    public void setDr_specilization(Object dr_specilization) {
        this.dr_specilization = dr_specilization;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
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

    public String getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    public Object getAvatar() {
        return avatar;
    }

    public void setAvatar(Object avatar) {
        this.avatar = avatar;
    }

    public String getQualification() {
        return qualification;
    }

    public void setQualification(String qualification) {
        this.qualification = qualification;
    }

    public Object getFacebook() {
        return facebook;
    }

    public void setFacebook(Object facebook) {
        this.facebook = facebook;
    }

    public Object getGooglePlus() {
        return googlePlus;
    }

    public void setGooglePlus(Object googlePlus) {
        this.googlePlus = googlePlus;
    }

    public Object getTwitter() {
        return twitter;
    }

    public void setTwitter(Object twitter) {
        this.twitter = twitter;
    }

    public Object getAddress() {
        return address;
    }

    public void setAddress(Object address) {
        this.address = address;
    }

    public Object getOccupationId() {
        return occupationId;
    }

    public void setOccupationId(Object occupationId) {
        this.occupationId = occupationId;
    }

    public String getConsultationTime() {
        return consultationTime;
    }

    public void setConsultationTime(String consultationTime) {
        this.consultationTime = consultationTime;
    }

    public Object getEthnicityId() {
        return ethnicityId;
    }

    public void setEthnicityId(Object ethnicityId) {
        this.ethnicityId = ethnicityId;
    }

    public String getIdentityId() {
        return identityId;
    }

    public void setIdentityId(String identityId) {
        this.identityId = identityId;
    }

    public String getIdentityType() {
        return identityType;
    }

    public void setIdentityType(String identityType) {
        this.identityType = identityType;
    }

    public Object getBloodGroup() {
        return bloodGroup;
    }

    public void setBloodGroup(Object bloodGroup) {
        this.bloodGroup = bloodGroup;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public Object getMaritalStatus() {
        return maritalStatus;
    }

    public void setMaritalStatus(Object maritalStatus) {
        this.maritalStatus = maritalStatus;
    }

    public Object getState() {
        return state;
    }

    public void setState(Object state) {
        this.state = state;
    }

    public Object getCity() {
        return city;
    }

    public void setCity(Object city) {
        this.city = city;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getSalutation() {
        return salutation;
    }

    public void setSalutation(String salutation) {
        this.salutation = salutation;
    }

    public Object getHeight() {
        return height;
    }

    public void setHeight(Object height) {
        this.height = height;
    }

    public Object getWeight() {
        return weight;
    }

    public void setWeight(Object weight) {
        this.weight = weight;
    }

    public Object getWaistSize() {
        return waistSize;
    }

    public void setWaistSize(Object waistSize) {
        this.waistSize = waistSize;
    }

    public Object getIs_active() {
        return is_active;
    }

    public void setIs_active(Object is_active) {
        this.is_active = is_active;
    }
}