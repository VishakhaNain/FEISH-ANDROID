
package com.app.feish.application.modelclassforapi;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class PatientPackageLog implements Serializable {

    @SerializedName("id")
    @Expose
    private Object id;
    @SerializedName("patient_package_id")
    @Expose
    private Object patientPackageId;
    @SerializedName("used_visits")
    @Expose
    private Object usedVisits;
    @SerializedName("remaining_visits")
    @Expose
    private Object remainingVisits;
    @SerializedName("user_id")
    @Expose
    private Object userId;
    @SerializedName("start_date")
    @Expose
    private Object startDate;
    @SerializedName("end_date")
    @Expose
    private Object endDate;
    @SerializedName("package_name")
    @Expose
    private Object packageName;
    @SerializedName("validity")
    @Expose
    private Object validity;
    @SerializedName("price")
    @Expose
    private Object price;
    @SerializedName("commission")
    @Expose
    private Object commission;
    @SerializedName("valid_visits")
    @Expose
    private Object validVisits;
    @SerializedName("plan_type")
    @Expose
    private Object planType;
    @SerializedName("plan_details")
    @Expose
    private Object planDetails;
    @SerializedName("service_id")
    @Expose
    private Object serviceId;
    @SerializedName("is_active")
    @Expose
    private Object isActive;
    @SerializedName("mihpayid")
    @Expose
    private Object mihpayid;
    @SerializedName("status")
    @Expose
    private Object status;
    @SerializedName("mode")
    @Expose
    private Object mode;
    @SerializedName("generate_invoice")
    @Expose
    private Object generateInvoice;
    @SerializedName("paid_flag")
    @Expose
    private Object paidFlag;
    @SerializedName("created")
    @Expose
    private Object created;
    @SerializedName("modified")
    @Expose
    private Object modified;

    public Object getId() {
        return id;
    }

    public void setId(Object id) {
        this.id = id;
    }

    public Object getPatientPackageId() {
        return patientPackageId;
    }

    public void setPatientPackageId(Object patientPackageId) {
        this.patientPackageId = patientPackageId;
    }

    public Object getUsedVisits() {
        return usedVisits;
    }

    public void setUsedVisits(Object usedVisits) {
        this.usedVisits = usedVisits;
    }

    public Object getRemainingVisits() {
        return remainingVisits;
    }

    public void setRemainingVisits(Object remainingVisits) {
        this.remainingVisits = remainingVisits;
    }

    public Object getUserId() {
        return userId;
    }

    public void setUserId(Object userId) {
        this.userId = userId;
    }

    public Object getStartDate() {
        return startDate;
    }

    public void setStartDate(Object startDate) {
        this.startDate = startDate;
    }

    public Object getEndDate() {
        return endDate;
    }

    public void setEndDate(Object endDate) {
        this.endDate = endDate;
    }

    public Object getPackageName() {
        return packageName;
    }

    public void setPackageName(Object packageName) {
        this.packageName = packageName;
    }

    public Object getValidity() {
        return validity;
    }

    public void setValidity(Object validity) {
        this.validity = validity;
    }

    public Object getPrice() {
        return price;
    }

    public void setPrice(Object price) {
        this.price = price;
    }

    public Object getCommission() {
        return commission;
    }

    public void setCommission(Object commission) {
        this.commission = commission;
    }

    public Object getValidVisits() {
        return validVisits;
    }

    public void setValidVisits(Object validVisits) {
        this.validVisits = validVisits;
    }

    public Object getPlanType() {
        return planType;
    }

    public void setPlanType(Object planType) {
        this.planType = planType;
    }

    public Object getPlanDetails() {
        return planDetails;
    }

    public void setPlanDetails(Object planDetails) {
        this.planDetails = planDetails;
    }

    public Object getServiceId() {
        return serviceId;
    }

    public void setServiceId(Object serviceId) {
        this.serviceId = serviceId;
    }

    public Object getIsActive() {
        return isActive;
    }

    public void setIsActive(Object isActive) {
        this.isActive = isActive;
    }

    public Object getMihpayid() {
        return mihpayid;
    }

    public void setMihpayid(Object mihpayid) {
        this.mihpayid = mihpayid;
    }

    public Object getStatus() {
        return status;
    }

    public void setStatus(Object status) {
        this.status = status;
    }

    public Object getMode() {
        return mode;
    }

    public void setMode(Object mode) {
        this.mode = mode;
    }

    public Object getGenerateInvoice() {
        return generateInvoice;
    }

    public void setGenerateInvoice(Object generateInvoice) {
        this.generateInvoice = generateInvoice;
    }

    public Object getPaidFlag() {
        return paidFlag;
    }

    public void setPaidFlag(Object paidFlag) {
        this.paidFlag = paidFlag;
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

}
