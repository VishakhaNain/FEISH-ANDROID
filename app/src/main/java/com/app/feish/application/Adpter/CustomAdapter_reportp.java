package com.app.feish.application.Adpter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.feish.application.R;
import com.app.feish.application.model.vitalsignlist;

import java.util.List;

/**
 * Created by lenovo on 8/14/2017.
 */

public class CustomAdapter_reportp extends BaseAdapter {

    Context context;
    List<vitalsignlist> list;
    private  LayoutInflater inflater=null;
    public CustomAdapter_reportp(Context context, List<vitalsignlist> list) {
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
        TextView tv_name,tv_mob,tv_add;
        LinearLayout linearLayout_book,ll_map;
        }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        Holder holder=new Holder();
        View rowView;

        rowView = inflater.inflate(R.layout.vitalsignlist, null);
       /* holder.tv_name=(TextView) rowView.findViewById(R.id.docNameid);
        holder.tv_mob=(TextView) rowView.findViewById(R.id.mobNumid);
        holder.tv_add=(TextView) rowView.findViewById(R.id.addressid);
        holder.linearLayout_book=(LinearLayout) rowView.findViewById(R.id.ll_bookappo);
        holder.ll_map=(LinearLayout) rowView.findViewById(R.id.navigate_to_map);
        holder.linearLayout_book.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(new Intent(context, CommunicatetoDoc.class));
            }
        });*/


        return rowView;
    }

}
