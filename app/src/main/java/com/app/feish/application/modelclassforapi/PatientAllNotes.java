
package com.app.feish.application.modelclassforapi;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PatientAllNotes {

    @SerializedName("Success")
    @Expose
    private Integer success;
    @SerializedName("PatientNote_record")
    @Expose
    private List<PatientNoteRecord> patientNoteRecord = null;

    public Integer getSuccess() {
        return success;
    }

    public void setSuccess(Integer success) {
        this.success = success;
    }

    public List<PatientNoteRecord> getPatientNoteRecord() {
        return patientNoteRecord;
    }

    public void setPatientNoteRecord(List<PatientNoteRecord> patientNoteRecord) {
        this.patientNoteRecord = patientNoteRecord;
    }

}
