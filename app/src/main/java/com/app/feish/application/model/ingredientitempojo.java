package com.app.feish.application.model;

import java.io.Serializable;

public class ingredientitempojo implements Serializable {
 private    String cal ,protein,cabohy,kcal,name,quantity;

    public ingredientitempojo(String name,String quantity,String cal, String protein, String cabohy, String kcal) {
        this.cal = cal;
        this.protein = protein;
        this.cabohy = cabohy;
        this.kcal = kcal;
        this.name=name;
        this.quantity=quantity;
    }

    public String getCal() {
        return cal;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public void setCal(String cal) {
        this.cal = cal;
    }

    public String getProtein() {
        return protein;
    }

    public void setProtein(String protein) {
        this.protein = protein;
    }

    public String getCabohy() {
        return cabohy;
    }

    public void setCabohy(String cabohy) {
        this.cabohy = cabohy;
    }

    public String getKcal() {
        return kcal;
    }

    public void setKcal(String kcal) {
        this.kcal = kcal;
    }
}
