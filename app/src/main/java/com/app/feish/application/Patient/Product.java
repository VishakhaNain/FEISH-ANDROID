package com.app.feish.application.Patient;

import static android.R.attr.rating;

/**
 * Created by Vishakha on 12-07-2018.
 */

public class Product {
    private String Dname;
    private String Planpri;
    private String Planval;
    private String value;


    public Product(String Planpri,String Planval, String value,String Dname) {
        this.Planpri = Planpri;
        this.Planval = Planval;
        this.value = value;
        this.Dname = Dname;


    }



    public String getDname() {
        return Dname;
    }

    public String getPlanpri() {
        return Planpri;
    }

    public String getPlanval() {
        return Planval;
    }


    public String getValue() {
        return value;
    }

}
