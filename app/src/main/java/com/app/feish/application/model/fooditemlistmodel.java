package com.app.feish.application.model;

public class fooditemlistmodel {
   private   String name, calories,used_ingre,used_calo;
   private int id,user_id;

    public fooditemlistmodel(int id,String name, String calories, String used_ingre,String used_calo,int user_id) {
        this.name = name;
        this.calories = calories;
        this.used_ingre = used_ingre;
        this.used_calo = used_calo;
        this.id=id;
        this.user_id=user_id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCalories() {
        return calories;
    }

    public void setCalories(String calories) {
        this.calories = calories;
    }

    public String getUsed_ingre() {
        return used_ingre;
    }

    public void setUsed_ingre(String used_ingre) {
        this.used_ingre = used_ingre;
    }

    public String getUsed_calo() {
        return used_calo;
    }

    public void setUsed_calo(String used_calo) {
        this.used_calo = used_calo;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
