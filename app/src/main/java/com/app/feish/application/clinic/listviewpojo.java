package com.app.feish.application.clinic;

public class listviewpojo {

    private   String  name,email;
    int img, val;


    public int getVal() {
        return val;
    }

    public void setVal(int val) {
        this.val = val;
    }

    public listviewpojo(String name, String email, int img, int val) {
        this.name = name;
        this.email = email;
        this.img = img;
        this.val = val;


    }

    public String getname() {
        return name;
    }

    public String getemail() {
        return email;
    }

    public int getImg() {
        return img;
    }

    public void setImg(int img) {
        this.img = img;
    }


}
