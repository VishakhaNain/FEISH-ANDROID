package com.app.feish.application.Adpter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.feish.application.R;
import com.app.feish.application.doctor.ServiceDetailActivity;
import com.app.feish.application.doctor.ViewProfileDoctor;
import com.app.feish.application.model.  listservicepojo;
import com.app.feish.application.modelclassforapi.Datum2;
import com.squareup.picasso.Picasso;

import java.io.Serializable;
import java.util.List;

/**
 * Created by lenovo on 8/14/2017.
 */

public class CustomAdapter_listservice extends BaseAdapter {

    Context context;
    List<Datum2> list;
    private  LayoutInflater inflater=null;
    public CustomAdapter_listservice(Context context, List<Datum2> list) {
        // TODO Auto-generated constructor stub
        this.list=list;
        this.context=context;
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
        TextView tv_title,tv_add,tv_phone,tv_active;
        ImageView imageView;
        }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        Holder holder=new Holder();
        View rowView;

        rowView = inflater.inflate(R.layout.listservice, null);
        holder.tv_title=rowView.findViewById(R.id.ser_title);
        holder.tv_add=rowView.findViewById(R.id.ser_add);
        holder.tv_phone=rowView.findViewById(R.id.ser_phone);
        holder.imageView=rowView.findViewById(R.id.serlogo);
        holder.tv_active=rowView.findViewById(R.id.ser_active);
        holder.tv_title.setText(list.get(position).getService().getTitle());
        holder.tv_add.setText(list.get(position).getService().getCity());
        holder.tv_phone.setText(list.get(position).getService().getPhone());
        holder.tv_active.setText(list.get(position).getService().getAddress());

        if(!list.get(position).getService().getLogo().equals("not_available.gif")) {

            Picasso.with(context)
                    .load("http://feish.online/img/services/"+list.get(position).getService().getLogo())
                    .into(holder.imageView);
        }
        else {
            holder.imageView.setImageResource(R.drawable.doctor_images);
        }


        rowView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent= new Intent(context, ServiceDetailActivity.class);
                intent.putExtra("MyClass", list.get(position).getService());
                context.startActivity(intent);
            }
        });


        return rowView;
    }

}
