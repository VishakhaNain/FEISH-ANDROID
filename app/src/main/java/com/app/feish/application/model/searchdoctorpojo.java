package com.app.feish.application.model;

public class searchdoctorpojo {
    String name,location,phoneno;

    public searchdoctorpojo(String name, String location, String phoneno) {
        this.name = name;
        this.location = location;
        this.phoneno = phoneno;
    }

    public String getName() {
        return name;
    }

    public String getLocation() {
        return location;
    }

    public String getPhoneno() {
        return phoneno;
    }
}
