package com.app.feish.application.model;

public class listservicepojo {
    String title,add,phoneno,status;

    public listservicepojo(String title, String add, String phoneno, String status) {
        this.title = title;
        this.add = add;
        this.phoneno = phoneno;
        this.status = status;
    }

    public String getTitle() {
        return title;
    }

    public String getAdd() {
        return add;
    }

    public String getPhoneno() {
        return phoneno;
    }

    public String getStatus() {
        return status;
    }
}
