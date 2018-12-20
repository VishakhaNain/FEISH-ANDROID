package com.app.feish.application.model;

import java.io.Serializable;

public class drplanmodel implements Serializable{
   private int id;
  private   String planname,planprice,planvalidity,planvalidvisit,plandesc;

    public drplanmodel(int id,String planname, String planprice, String planvalidity, String planvalidvisit,String plandesc) {
        this.planname = planname;
        this.planprice = planprice;
        this.planvalidity = planvalidity;
        this.planvalidvisit = planvalidvisit;
        this.plandesc=plandesc;
        this.id=id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPlanname() {
        return planname;
    }

    public String getPlanprice() {
        return planprice;
    }

    public String getPlanvalidity() {
        return planvalidity;
    }

    public String getPlanvalidvisit() {
        return planvalidvisit;
    }

    public String getPlandesc() {
        return plandesc;
    }

    public void setPlandesc(String plandesc) {
        this.plandesc = plandesc;
    }
}
