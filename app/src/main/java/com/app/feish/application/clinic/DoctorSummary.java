package com.app.feish.application.clinic;

import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

/**
 * Created by Rp on 3/16/2016.
 */
public class DoctorSummary {


   //private int image;
    private String title;
    private String desc;
    private  String date;


/*    public DoctorSummary(int image,String title, String desc, String date) {
        this.title = title;
        this.image = image;
        this.desc = desc;
        this.date = date;
    }*/
    public DoctorSummary(String title, String desc, String date) {
        //this.image = image;
        this.title = title;
        this.desc = desc;
        this.date = date;
    }

  /*  public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }*/

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }



}


