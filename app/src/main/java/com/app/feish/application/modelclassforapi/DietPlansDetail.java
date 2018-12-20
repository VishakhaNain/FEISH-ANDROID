
package com.app.feish.application.modelclassforapi;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DietPlansDetail {

    @SerializedName("DietPlansDetail")
    @Expose
    private DietPlansDetail_ dietPlansDetail;

    public DietPlansDetail_ getDietPlansDetail() {
        return dietPlansDetail;
    }

    public void setDietPlansDetail(DietPlansDetail_ dietPlansDetail) {
        this.dietPlansDetail = dietPlansDetail;
    }

}
