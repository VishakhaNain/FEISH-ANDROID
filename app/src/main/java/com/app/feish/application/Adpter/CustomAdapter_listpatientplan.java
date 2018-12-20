package com.app.feish.application.Adpter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.app.feish.application.Patient.PatientPurchasePlan;
import com.app.feish.application.PaymentDetail.PayUMoneyActivityforDoctorPackage;
import com.app.feish.application.R;
import com.app.feish.application.model.patientplanmodel;

import org.json.JSONException;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class CustomAdapter_listpatientplan extends BaseAdapter {

    private List<patientplanmodel> dataSet;
    int code;
    Context context;
    String currentdate="",increasedoneyeardate="";

    @Override
    public int getCount() {
        return dataSet.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int listPosition, View convertView, ViewGroup parent) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.patientplandetail, parent, false);
        TextView textView_planname,textView_planprice;
        TextView textView_remaining,textView_usedvisit;
        TextView textView_totalvisit,textView_drname,textView_drspe;
        CardView cardView;

            textView_planname=itemView.findViewById(R.id.planname);
            textView_planprice=itemView.findViewById(R.id.price);
            textView_remaining=itemView.findViewById(R.id.remaining_visits);
            textView_usedvisit=itemView.findViewById(R.id.used_visit);
            textView_totalvisit=itemView.findViewById(R.id.totalvisit);
            textView_drname=itemView.findViewById(R.id.drname);
            textView_drspe=itemView.findViewById(R.id.drspeciality);
        try {
            textView_planname.setText(dataSet.get(listPosition).getPlanname());
            textView_planprice.setText("Rs. " + dataSet.get(listPosition).getPlanprice());
            textView_remaining.setText(dataSet.get(listPosition).getPlanremainingvisit());
            textView_totalvisit.setText(dataSet.get(listPosition).getPlantotalvisit());
            textView_usedvisit.setText(dataSet.get(listPosition).getPlanusedvisit());
            textView_drname.setText(dataSet.get(listPosition).getDoctorname().getJSONObject(0).getString("first_name") + " " + dataSet.get(listPosition).getDoctorname().getJSONObject(0).getString("last_name"));

            textView_drspe.setText(PatientPurchasePlan.speciality.get(dataSet.get(listPosition).getDoctorspe().getJSONObject(0).getInt("dr_specilization")));
        }
        catch (JSONException e)
        {
            Toast.makeText(context, ""+e, Toast.LENGTH_SHORT).show();
        }



        return itemView;
    }



    public CustomAdapter_listpatientplan(List<patientplanmodel> data, Context context, int code) {
        this.dataSet = data;
        this.code=code;
        this.context=context;
        Date cDate = new Date();
        String fDate = new SimpleDateFormat("yyyy-MM-dd").format(cDate);
         Calendar calendar= Calendar.getInstance();
        calendar.setTime(cDate);
        calendar.add(Calendar.YEAR,1);
        Date currentDatePlusOne = calendar.getTime();
        String increasedate = new SimpleDateFormat("yyyy-MM-dd").format(currentDatePlusOne);

        this.currentdate=fDate;
        this.increasedoneyeardate=increasedate;


    }

}
