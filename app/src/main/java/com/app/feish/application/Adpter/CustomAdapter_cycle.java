package com.app.feish.application.Adpter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.feish.application.R;
import com.app.feish.application.model.searchdoctorpojo;
import com.app.feish.application.model.vitalsignlist;

import java.util.List;

/**
 * Created by lenovo on 8/14/2017.
 */

public class CustomAdapter_cycle extends BaseAdapter {

    Context context;
    List<searchdoctorpojo> list;
    private  LayoutInflater inflater=null;
    public CustomAdapter_cycle(Context context, List<searchdoctorpojo> list) {
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
        TextView tv_desc,tv_sdate,tv_edate;
        LinearLayout linearLayout_book,ll_map;
        }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        Holder holder=new Holder();
        View rowView;

        rowView = inflater.inflate(R.layout.programlist_firstgrid, null);
        holder.tv_desc=(TextView) rowView.findViewById(R.id.textView_desc);
        holder.tv_edate=(TextView) rowView.findViewById(R.id.edate);
        holder.tv_sdate=(TextView) rowView.findViewById(R.id.sdate);

        return rowView;
    }

}
