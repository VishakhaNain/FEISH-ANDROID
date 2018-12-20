package com.app.feish.application.clinic;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.feish.application.R;

import java.util.ArrayList;

/**
 * Created by Rp on 3/16/2016.
 */
public class ListbaseAdapter extends BaseAdapter {

    Context context;
    ArrayList<DoctorSummary> beans;


    public ListbaseAdapter(Context context, ArrayList<DoctorSummary> beans) {
        this.context = context;
        this.beans = beans;
    }

    @Override
    public int getCount() {
        return beans.size() ;
    }

    @Override
    public Object getItem(int position) {
        return beans.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder = null;





        if (convertView == null){

            LayoutInflater layoutInflater = (LayoutInflater)context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            Typeface type = Typeface.createFromAsset(context.getAssets(), "fonts/Montserrat-Regular.ttf");
          viewHolder = new ViewHolder();

            convertView = layoutInflater.inflate(R.layout.listview,null);


            viewHolder.image= (ImageView)convertView.findViewById(R.id.image);
            viewHolder.title= (TextView)convertView.findViewById(R.id.title);
            viewHolder.desc= (TextView)convertView.findViewById(R.id.desc);
            viewHolder.date= (TextView)convertView.findViewById(R.id.date);

            viewHolder.title.setTypeface(type);
            viewHolder.desc.setTypeface(type);
            viewHolder.date.setTypeface(type);


            convertView.setTag(viewHolder);


        }else {

            viewHolder = (ViewHolder)convertView.getTag();

        }


        DoctorSummary beans = (DoctorSummary)getItem(position);

       // viewHolder.image.setImageResource(beans.getImage());
        viewHolder.title.setText(beans.getTitle());
        viewHolder.desc.setText(beans.getDesc());
        viewHolder.date.setText(beans.getDate());

        return convertView;
    }



    private class ViewHolder{
        ImageView image;
        TextView title;
        TextView desc;
        TextView date;

    }





}
