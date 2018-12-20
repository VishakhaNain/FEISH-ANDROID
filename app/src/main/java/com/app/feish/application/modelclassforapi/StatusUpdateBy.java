
package com.app.feish.application.modelclassforapi;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class StatusUpdateBy implements Serializable {

    @SerializedName("id")
    @Expose
    private Object id;
    @SerializedName("email")
    @Expose
    private Object email;
    @SerializedName("password")
    @Expose
    private Object password;
    @SerializedName("salutation")
    @Expose
    private Object salutation;
    @SerializedName("first_name")
    @Expose
    private Object firstName;
    @SerializedName("last_name")
    @Expose
    private Object lastName;
    @SerializedName("birth_date")
    @Expose
    private Object birthDate;
    @SerializedName("registration_no")
    @Expose
    private Object registrationNo;
    @SerializedName("gender")
    @Expose
    private Object gender;
    @SerializedName("mobile")
    @Expose
    private Object mobile;
    @SerializedName("qualification")
    @Expose
    private Object qualification;
    @SerializedName("mci_number")
    @Expose
    private Object mciNumber;
    @SerializedName("consultation_time")
    @Expose
    private Object consultationTime;
    @SerializedName("patient_key")
    @Expose
    private Object patientKey;
    @SerializedName("avatar")
    @Expose
    private Object avatar;
    @SerializedName("facebook")
    @Expose
    private Object facebook;
    @SerializedName("google_plus")
    @Expose
    private Object googlePlus;
    @SerializedName("twitter")
    @Expose
    private Object twitter;
    @SerializedName("user_type")
    @Expose
    private Object userType;
    @SerializedName("ip_address")
    @Expose
    private Object ipAddress;
    @SerializedName("identity_type")
    @Expose
    private Object identityType;
    @SerializedName("identity_id")
    @Expose
    private Object identityId;
    @SerializedName("is_active")
    @Expose
    private Object isActive;
    @SerializedName("is_verified")
    @Expose
    private Object isVerified;
    @SerializedName("is_deleted")
    @Expose
    private Object isDeleted;
    @SerializedName("prev_rating")
    @Expose
    private Object prevRating;
    @SerializedName("avg_rating")
    @Expose
    private Object avgRating;
    @SerializedName("added_by_doctor_id")
    @Expose
    private Object addedByDoctorId;
    @SerializedName("added_by_laboratory_id")
    @Expose
    private Object addedByLaboratoryId;
    @SerializedName("otp")
    @Expose
    private Object otp;
    @SerializedName("created")
    @Expose
    private Object created;
    @SerializedName("modified")
    @Expose
    private Object modified;
    @SerializedName("marital_status")
    @Expose
    private Object maritalStatus;
    @SerializedName("blood_group")
    @Expose
    private Object bloodGroup;
    @SerializedName("occupation_id")
    @Expose
    private Object occupationId;
    @SerializedName("address")
    @Expose
    private Object address;
    @SerializedName("state")
    @Expose
    private Object state;
    @SerializedName("city")
    @Expose
    private Object city;
    @SerializedName("ethnicity_id")
    @Expose
    private Object ethnicityId;
    @SerializedName("identity")
    @Expose
    private Object identity;
    @SerializedName("full_name")
    @Expose
    private String fullName;

    public Object getId() {
        return id;
    }

    public void setId(Object id) {
        this.id = id;
    }

    public Object getEmail() {
        return email;
    }

    public void setEmail(Object email) {
        this.email = email;
    }

    public Object getPassword() {
        return password;
    }

    public void setPassword(Object password) {
        this.password = password;
    }

    public Object getSalutation() {
        return salutation;
    }

    public void setSalutation(Object salutation) {
        this.salutation = salutation;
    }

    public Object getFirstName() {
        return firstName;
    }

    public void setFirstName(Object firstName) {
        this.firstName = firstName;
    }

    public Object getLastName() {
        return lastName;
    }

    public void setLastName(Object lastName) {
        this.lastName = lastName;
    }

    public Object getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Object birthDate) {
        this.birthDate = birthDate;
    }

    public Object getRegistrationNo() {
        return registrationNo;
    }

    public void setRegistrationNo(Object registrationNo) {
        this.registrationNo = registrationNo;
    }

    public Object getGender() {
        return gender;
    }

    public void setGender(Object gender) {
        this.gender = gender;
    }

    public Object getMobile() {
        return mobile;
    }

    public void setMobile(Object mobile) {
        this.mobile = mobile;
    }

    public Object getQualification() {
        return qualification;
    }

    public void setQualification(Object qualification) {
        this.qualification = qualification;
    }

    public Object getMciNumber() {
        return mciNumber;
    }

    public void setMciNumber(Object mciNumber) {
        this.mciNumber = mciNumber;
    }

    public Object getConsultationTime() {
        return consultationTime;
    }

    public void setConsultationTime(Object consultationTime) {
        this.consultationTime = consultationTime;
    }

    public Object getPatientKey() {
        return patientKey;
    }

    public void setPatientKey(Object patientKey) {
        this.patientKey = patientKey;
    }

    public Object getAvatar() {
        return avatar;
    }

    public void setAvatar(Object avatar) {
        this.avatar = avatar;
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

    public Object getUserType() {
        return userType;
    }

    public void setUserType(Object userType) {
        this.userType = userType;
    }

    public Object getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(Object ipAddress) {
        this.ipAddress = ipAddress;
    }

    public Object getIdentityType() {
        return identityType;
    }

    public void setIdentityType(Object identityType) {
        this.identityType = identityType;
    }

    public Object getIdentityId() {
        return identityId;
    }

    public void setIdentityId(Object identityId) {
        this.identityId = identityId;
    }

    public Object getIsActive() {
        return isActive;
    }

    public void setIsActive(Object isActive) {
        this.isActive = isActive;
    }

    public Object getIsVerified() {
        return isVerified;
    }

    public void setIsVerified(Object isVerified) {
        this.isVerified = isVerified;
    }

    public Object getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(Object isDeleted) {
        this.isDeleted = isDeleted;
    }

    public Object getPrevRating() {
        return prevRating;
    }

    public void setPrevRating(Object prevRating) {
        this.prevRating = prevRating;
    }

    public Object getAvgRating() {
        return avgRating;
    }

    public void setAvgRating(Object avgRating) {
        this.avgRating = avgRating;
    }

    public Object getAddedByDoctorId() {
        return addedByDoctorId;
    }

    public void setAddedByDoctorId(Object addedByDoctorId) {
        this.addedByDoctorId = addedByDoctorId;
    }

    public Object getAddedByLaboratoryId() {
        return addedByLaboratoryId;
    }

    public void setAddedByLaboratoryId(Object addedByLaboratoryId) {
        this.addedByLaboratoryId = addedByLaboratoryId;
    }

    public Object getOtp() {
        return otp;
    }

    public void setOtp(Object otp) {
        this.otp = otp;
    }

    public Object getCreated() {
        return created;
    }

    public void setCreated(Object created) {
        this.created = created;
    }

    public Object getModified() {
        return modified;
    }

    public void setModified(Object modified) {
        this.modified = modified;
    }

    public Object getMaritalStatus() {
        return maritalStatus;
    }

    public void setMaritalStatus(Object maritalStatus) {
        this.maritalStatus = maritalStatus;
    }

    public Object getBloodGroup() {
        return bloodGroup;
    }

    public void setBloodGroup(Object bloodGroup) {
        this.bloodGroup = bloodGroup;
    }

    public Object getOccupationId() {
        return occupationId;
    }

    public void setOccupationId(Object occupationId) {
        this.occupationId = occupationId;
    }

    public Object getAddress() {
        return address;
    }

    public void setAddress(Object address) {
        this.address = address;
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

    public Object getEthnicityId() {
        return ethnicityId;
    }

    public void setEthnicityId(Object ethnicityId) {
        this.ethnicityId = ethnicityId;
    }

    public Object getIdentity() {
        return identity;
    }

    public void setIdentity(Object identity) {
        this.identity = identity;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

}
