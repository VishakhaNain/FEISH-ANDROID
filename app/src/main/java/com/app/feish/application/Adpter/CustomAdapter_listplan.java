package com.app.feish.application.Adpter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.app.feish.application.Alarmsclass.DateTime;
import com.app.feish.application.PaymentDetail.PayUMoneyActivityforDoctorPackage;
import com.app.feish.application.R;
import com.app.feish.application.model.drplanmodel;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class CustomAdapter_listplan extends RecyclerView.Adapter<CustomAdapter_listplan.MyViewHolder> {

    private List<drplanmodel> dataSet;
    int code;
    Context context;
    String currentdate="",increasedoneyeardate="";

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView textView_planname,textView_planprice;
        TextView textView_plandesc,textView_validity;
        ImageView imageView;

        Button button_buynow;
        CardView cardView;
        public MyViewHolder(View itemView) {
            super(itemView);
           // imageView=itemView.findViewById(R.id.removetimetable);
            textView_planname=itemView.findViewById(R.id.Planname);
            textView_planprice=itemView.findViewById(R.id.planprice);
            textView_plandesc=itemView.findViewById(R.id.plandesc);
            imageView=itemView.findViewById(R.id.delete);
            button_buynow=itemView.findViewById(R.id.buynow);
            textView_validity=itemView.findViewById(R.id.planvalidity);

        }
    }

    public CustomAdapter_listplan(List<drplanmodel> data, Context context,int code) {
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

    @Override
    public CustomAdapter_listplan.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                                  int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.listplanxl, parent, false);

        CustomAdapter_listplan.MyViewHolder myViewHolder = new CustomAdapter_listplan.MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(final CustomAdapter_listplan.MyViewHolder holder, final int listPosition) {
        holder.textView_planname.setText(dataSet.get(listPosition).getPlanname());
        holder.textView_planprice.setText("Rs. "+dataSet.get(listPosition).getPlanprice());
        holder.textView_plandesc.setText(dataSet.get(listPosition).getPlandesc());
        holder.textView_validity.setText(dataSet.get(listPosition).getPlanvalidity()+" Days");

      if(code==1) {
          holder.button_buynow.setVisibility(View.VISIBLE);
          holder.imageView.setVisibility(View.GONE);
          holder.button_buynow.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View v) {
                  Intent intent= new Intent(context, PayUMoneyActivityforDoctorPackage.class);
                  intent.putExtra("fee",dataSet.get(listPosition).getPlanprice());
                  intent.putExtra("planid",dataSet.get(listPosition).getId());
                  intent.putExtra("puchase_sdate",currentdate);
                  intent.putExtra("puchase_edate",increasedoneyeardate);
                  context.startActivity(intent);

              }
          });
      }

    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }
}
