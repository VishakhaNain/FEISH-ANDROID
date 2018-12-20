package com.app.feish.application.model;

public class listassistantmodel {
  private   String sal, fname, lname,  mob,  email,  pos;
  int img;

    public listassistantmodel(String sal, String fname, String lname, String mob, String email, String pos ,int img) {
        this.sal = sal;
        this.fname = fname;
        this.lname = lname;
        this.mob = mob;
        this.email = email;
        this.pos = pos;
        this.img=img;
    }

    public String getSal() {
        return sal;
    }

    public String getFname() {
        return fname;
    }

    public String getLname() {
        return lname;
    }

    public String getMob() {
        return mob;
    }

    public String getEmail() {
        return email;
    }

    public String getPos() {
        return pos;
    }

    public int getImg() {
        return img;
    }

    public void setImg(int img) {
        this.img = img;
    }
}
