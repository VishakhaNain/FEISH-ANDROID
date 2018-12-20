
package com.app.feish.application.modelclassforapi;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class UserDetaildata implements Serializable {

    @SerializedName("0")
    @Expose
    private com.app.feish.application.modelclassforapi._0 _0;
    @SerializedName("name")
    @Expose
    private String name;

    public com.app.feish.application.modelclassforapi._0 get0() {
        return _0;
    }

    public void set0(com.app.feish.application.modelclassforapi._0 _0) {
        this._0 = _0;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
