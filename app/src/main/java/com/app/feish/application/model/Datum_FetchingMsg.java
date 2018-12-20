package com.app.feish.application.model;

/**
 * Created by RahulReign on 03-04-2018.
 */

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Datum_FetchingMsg {


    @SerializedName("Communication")
    @Expose
    private Communication communication;
    @SerializedName("User")
    @Expose
    private UserClass_for_showing_patient_name user;

    @SerializedName("ChildCommunication")
    @Expose
    private List<Object> childCommunication = null;

    public Communication getCommunication() {
        return communication;
    }

    public void setCommunication(Communication communication) {
        this.communication = communication;
    }

    public UserClass_for_showing_patient_name getUser() {
        return user;

    }

    public void setUser(UserClass_for_showing_patient_name user) {
        this.user = user;
    }

    public List<Object> getChildCommunication() {
        return childCommunication;
    }

    public void setChildCommunication(List<Object> childCommunication) {
        this.childCommunication = childCommunication;
    }
}
/*

@SerializedName("Communication")
@Expose
private Communication communication;
@SerializedName("User")
@Expose
private User user;
@SerializedName("ChildCommunication")
@Expose
private List<Object> childCommunication = null;

public Communication getCommunication() {
return communication;
}

public void setCommunication(Communication communication) {
this.communication = communication;
}

public User getUser() {
return user;
}

public void setUser(User user) {
this.user = user;
}

public List<Object> getChildCommunication() {
return childCommunication;
}

public void setChildCommunication(List<Object> childCommunication) {
this.childCommunication = childCommunication;
}

}

 */