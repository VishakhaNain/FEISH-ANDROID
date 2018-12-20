
package com.app.feish.application.modelclassforapi;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MainDietlist {

    @SerializedName("DietPlan")
    @Expose
    private DietPlan dietPlan;
    @SerializedName("DietPlansDetail")
    @Expose
    private List<DietPlansDetail> dietPlansDetail = null;

    public DietPlan getDietPlan() {
        return dietPlan;
    }

    public void setDietPlan(DietPlan dietPlan) {
        this.dietPlan = dietPlan;
    }

    public List<DietPlansDetail> getDietPlansDetail() {
        return dietPlansDetail;
    }

    public void setDietPlansDetail(List<DietPlansDetail> dietPlansDetail) {
        this.dietPlansDetail = dietPlansDetail;
    }

}
