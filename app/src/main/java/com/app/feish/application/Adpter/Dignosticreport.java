package com.app.feish.application.Adpter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.Spanned;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.app.feish.application.Patient.VitalandReport;
import com.app.feish.application.R;
import com.ms.square.android.expandabletextview.ExpandableTextView;

import java.util.ArrayList;

public class Dignosticreport extends RecyclerView.Adapter<Dignosticreport.MyViewHolder> {

    Context context;
    ArrayList<String>  strings;
    ArrayList<String> stringstitle;
    SparseBooleanArray mCollapsedStatus;

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView textView_effetctdate,textView_issuedate;
        ExpandableTextView expandableTextView;
        public MyViewHolder(View itemView) {
            super(itemView);
//            this.textView_drname = itemView.findViewById(R.id.htmltext);
            this.expandableTextView=itemView.findViewById(R.id.expand_text_view);
            this.textView_effetctdate=itemView.findViewById(R.id.effetctdate);
            this.textView_issuedate=itemView.findViewById(R.id.issuedate);

        }
    }

    public Dignosticreport(Context context, ArrayList<String> strings, ArrayList<String> stringstitle) {
        this.context=context;
        this.strings=strings;
        mCollapsedStatus = new SparseBooleanArray();
        this.stringstitle=stringstitle;

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent,
                                           int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.content_allreports, parent, false);

        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int listPosition)
    {
        String sourceString = "<b>  <font color='#0072BC'>" + stringstitle.get(listPosition) + " </font> </b> " ;
        Spanned spanned=Html.fromHtml(sourceString);
        Spanned spanned1=Html.fromHtml(strings.get(listPosition));
        //   holder.textView_drname.setText(Html.fromHtml(strings.get(listPosition)));
        String s=spanned+"   "+spanned1;
        holder.expandableTextView.setText(s, mCollapsedStatus, listPosition);
        holder.textView_effetctdate.setText("Effect Date: "+ VitalandReport.effetcdate);
        holder.textView_issuedate.setText("Issued Date: "+ VitalandReport.issuetime);
      //  holder.expandableTextView.setText(stringstitle.get(listPosition));


    }

    @Override
    public int getItemCount() {
        return strings.size();
    }
}
