package com.app.feish.application.Adpter;

import android.content.Context;
import android.content.Intent;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.feish.application.DoctorAddress;
import com.app.feish.application.DoctorCompleteProfile;
import com.app.feish.application.Patient.CommunicatetoDoc;
import com.app.feish.application.Patient.SearchDoctorList;
import com.app.feish.application.R;
import com.app.feish.application.model.searchdoctorpojo;
import com.app.feish.application.modelclassforapi.DoctorDatum;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lenovo on 8/14/2017.
 */

public class CustomAdapter extends BaseAdapter {

    Context context;
    List<searchdoctorpojo> list;
    List<DoctorDatum> ls;

    private  LayoutInflater inflater=null;
    public CustomAdapter(Context context, List<searchdoctorpojo> list) {
        // TODO Auto-generated constructor stub
        this.list=list;
        this.context=context;
        ls= SearchDoctorList.ls;
        inflater = ( LayoutInflater )context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    public class Holder
    {
        TextView tv_name,tv_mob,tv_add;
        LinearLayout linearLayout_book,ll_map,linearLayout_message;
        }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        Holder holder=new Holder();
        View rowView;

        rowView = inflater.inflate(R.layout.doctorlist, null);
        holder.tv_name=(TextView) rowView.findViewById(R.id.docNameid);
        holder.tv_mob=(TextView) rowView.findViewById(R.id.mobNumid);
        holder.tv_add=(TextView) rowView.findViewById(R.id.addressid);
        holder.tv_name.setText(list.get(position).getName());
        holder.tv_mob.setText(list.get(position).getPhoneno());
        holder.tv_add.setText(list.get(position).getLocation());

        holder.linearLayout_book=(LinearLayout) rowView.findViewById(R.id.ll_bookappo);
        holder.linearLayout_message=(LinearLayout) rowView.findViewById(R.id.ll_message);
        holder.ll_map=(LinearLayout) rowView.findViewById(R.id.navigate_to_map);

        holder.linearLayout_book.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context,DoctorCompleteProfile.class);
                intent.putExtra("patientcode",1);
                intent.putExtra("drinfo",ls.get(position));
                context.startActivity(intent);
            }
        });

        holder.linearLayout_message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context,CommunicatetoDoc.class);
                intent.putExtra("userData",ls.get(position));
                context.startActivity(intent);
            }
        });

        holder.ll_map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(context, DoctorAddress.class);
                intent.putExtra("address",list.get(position).getLocation());
                intent.putExtra("name",list.get(position).getName());
                context.startActivity(intent);
            }
        });


        return rowView;
    }

}
