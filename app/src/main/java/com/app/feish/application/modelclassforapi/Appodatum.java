
package com.app.feish.application.modelclassforapi;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Appodatum implements Serializable
{

    @SerializedName("UserDetaildata")
    @Expose
    private UserDetaildata userDetaildata;

    public UserDetaildata getUserDetaildata() {
        return userDetaildata;
    }

    public void setUserDetaildata(UserDetaildata userDetaildata) {
        this.userDetaildata = userDetaildata;
    }

}
