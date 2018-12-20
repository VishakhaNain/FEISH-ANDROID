
package com.app.feish.application.modelclassforapi;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Doctor implements Serializable {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("password")
    @Expose
    private String password;
    @SerializedName("salutation")
    @Expose
    private String salutation;
    @SerializedName("first_name")
    @Expose
    private String firstName;
    @SerializedName("last_name")
    @Expose
    private String lastName;
    @SerializedName("birth_date")
    @Expose
    private Object birthDate;
    @SerializedName("registration_no")
    @Expose
    private String registrationNo;
    @SerializedName("gender")
    @Expose
    private String gender;
    @SerializedName("mobile")
    @Expose
    private String mobile;
    @SerializedName("qualification")
    @Expose
    private String qualification;
    @SerializedName("mci_number")
    @Expose
    private String mciNumber;
    @SerializedName("consultation_time")
    @Expose
    private String consultationTime;
    @SerializedName("patient_key")
    @Expose
    private Object patientKey;
    @SerializedName("avatar")
    @Expose
    private String avatar;
    @SerializedName("facebook")
    @Expose
    private String facebook;
    @SerializedName("google_plus")
    @Expose
    private String googlePlus;
    @SerializedName("twitter")
    @Expose
    private String twitter;
    @SerializedName("user_type")
    @Expose
    private String userType;
    @SerializedName("ip_address")
    @Expose
    private String ipAddress;
    @SerializedName("identity_type")
    @Expose
    private String identityType;
    @SerializedName("identity_id")
    @Expose
    private String identityId;
    @SerializedName("is_active")
    @Expose
    private String isActive;
    @SerializedName("is_verified")
    @Expose
    private String isVerified;
    @SerializedName("is_deleted")
    @Expose
    private String isDeleted;
    @SerializedName("prev_rating")
    @Expose
    private String prevRating;
    @SerializedName("avg_rating")
    @Expose
    private String avgRating;
    @SerializedName("added_by_doctor_id")
    @Expose
    private Object addedByDoctorId;
    @SerializedName("added_by_laboratory_id")
    @Expose
    private Object addedByLaboratoryId;
    @SerializedName("otp")
    @Expose
    private String otp;
    @SerializedName("created")
    @Expose
    private String created;
    @SerializedName("modified")
    @Expose
    private String modified;
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
    private String address;
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSalutation() {
        return salutation;
    }

    public void setSalutation(String salutation) {
        this.salutation = salutation;
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

    public Object getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Object birthDate) {
        this.birthDate = birthDate;
    }

    public String getRegistrationNo() {
        return registrationNo;
    }

    public void setRegistrationNo(String registrationNo) {
        this.registrationNo = registrationNo;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getQualification() {
        return qualification;
    }

    public void setQualification(String qualification) {
        this.qualification = qualification;
    }

    public String getMciNumber() {
        return mciNumber;
    }

    public void setMciNumber(String mciNumber) {
        this.mciNumber = mciNumber;
    }

    public String getConsultationTime() {
        return consultationTime;
    }

    public void setConsultationTime(String consultationTime) {
        this.consultationTime = consultationTime;
    }

    public Object getPatientKey() {
        return patientKey;
    }

    public void setPatientKey(Object patientKey) {
        this.patientKey = patientKey;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getFacebook() {
        return facebook;
    }

    public void setFacebook(String facebook) {
        this.facebook = facebook;
    }

    public String getGooglePlus() {
        return googlePlus;
    }

    public void setGooglePlus(String googlePlus) {
        this.googlePlus = googlePlus;
    }

    public String getTwitter() {
        return twitter;
    }

    public void setTwitter(String twitter) {
        this.twitter = twitter;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String getIdentityType() {
        return identityType;
    }

    public void setIdentityType(String identityType) {
        this.identityType = identityType;
    }

    public String getIdentityId() {
        return identityId;
    }

    public void setIdentityId(String identityId) {
        this.identityId = identityId;
    }

    public String getIsActive() {
        return isActive;
    }

    public void setIsActive(String isActive) {
        this.isActive = isActive;
    }

    public String getIsVerified() {
        return isVerified;
    }

    public void setIsVerified(String isVerified) {
        this.isVerified = isVerified;
    }

    public String getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(String isDeleted) {
        this.isDeleted = isDeleted;
    }

    public String getPrevRating() {
        return prevRating;
    }

    public void setPrevRating(String prevRating) {
        this.prevRating = prevRating;
    }

    public String getAvgRating() {
        return avgRating;
    }

    public void setAvgRating(String avgRating) {
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

    public String getOtp() {
        return otp;
    }

    public void setOtp(String otp) {
        this.otp = otp;
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
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
