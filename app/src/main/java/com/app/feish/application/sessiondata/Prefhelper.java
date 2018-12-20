package com.app.feish.application.sessiondata;

import android.content.Context;
import android.content.SharedPreferences;
import android.location.Location;

public class Prefhelper {
    private static Prefhelper instance;

    private SharedPreferences sharedPreferences;
   // private String role;
    Location loc;
    String userid;
    String clinic_id;
    String token;
    String usertype;
    Boolean isLoggedIn;

    private Prefhelper(Context context) {
        sharedPreferences = context.getSharedPreferences("FreshDaililiesPref", Context.MODE_PRIVATE);
    }

    public static Prefhelper getInstance(Context context) {
        if (instance == null)
            instance = new Prefhelper(context);
        return instance;
    }

    public Location getLoc() {
        Location loc=new Location("");
        loc.setLatitude(Double.parseDouble(sharedPreferences.getString("lat","")));
        loc.setLongitude(Double.parseDouble(sharedPreferences.getString("long","")));
        return loc;
    }

    public void setLoc(Location loc) {
        this.loc = loc;
        sharedPreferences.edit().putString("lat",""+0).apply();
        sharedPreferences.edit().putString("long",""+0).apply();
    }


    public String getUserid() {
        return sharedPreferences.getString("id","");
    }

    public void setUserid(String userid) {
        this.userid = userid;
        sharedPreferences.edit().putString("id",userid).apply();
    }

    public String getUsertype() {
        return sharedPreferences.getString("type","");
    }

    public void setUsertype(String usertype) {
        this.usertype = usertype;
        sharedPreferences.edit().putString("type",usertype).apply();
    }


    public String getClinic_id() {
        return sharedPreferences.getString("clinic_id","");
    }

    public void setClinic_id(String clinic_id) {
        this.clinic_id = clinic_id;
        sharedPreferences.edit().putString("clinic_id",clinic_id).apply();
    }


    public String getToken() {
        return sharedPreferences.getString("token","");
    }

    public void setToken(String token) {
        this.token = token;
        sharedPreferences.edit().putString("token",token).apply();
    }


    public int getdeviceinfostore() {
        return sharedPreferences.getInt("device",0);
    }

    public void setdeviceinfo(int device) {
        sharedPreferences.edit().putInt("device",device).apply();
    }


    public Boolean getLoggedIn() {
        return sharedPreferences.getBoolean("login",false);
    }

    public void setLoggedIn(Boolean loggedIn) {
        isLoggedIn = loggedIn;
        sharedPreferences.edit().putBoolean("login",loggedIn).apply();
    }

    public void setPatientsessiondata(int AppointID,int sessionid,String entrytime)
    {
        sharedPreferences.edit().putInt("appointID",AppointID).apply();
        sharedPreferences.edit().putInt("session",sessionid).apply();
        sharedPreferences.edit().putString("entrytime",entrytime).apply();
    }
    public int getPatientsession()
    {
        return sharedPreferences.getInt("session",0);
    }
    public String getPatiententrytime()
    {
        return sharedPreferences.getString("entrytime","");
    }

  public void setPatientsessiondataatrecep(int AppointID,int sessionid,String entrytime)
    {
        sharedPreferences.edit().putInt("appointIDre",AppointID).apply();
        sharedPreferences.edit().putInt("sessionre",sessionid).apply();
        sharedPreferences.edit().putString("entrytimere",entrytime).apply();
    }

    public void paymentseesion(String tokenid,String paymentmode,int ID)
    {
        sharedPreferences.edit().putString("tokenid",tokenid).apply();
        sharedPreferences.edit().putString("paymentmode",paymentmode).apply();
        sharedPreferences.edit().putInt("appointIDp",ID).apply();

    }
    public int getPatientid()
    {
        return sharedPreferences.getInt("appointIDp",0);
    }

}
