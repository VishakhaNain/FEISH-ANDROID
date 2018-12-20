package com.app.feish.application.Adpter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.app.feish.application.Patient.BookedAppointmetDetail;
import com.app.feish.application.PaymentDetail.PayUMoneyActivityforPurchasePlan;
import com.app.feish.application.R;
import com.app.feish.application.model.drplanmodel;
import com.app.feish.application.modelclassforapi.Appointmentdatum;
import com.app.feish.application.modelclassforapi.PatientBookedappointment;

import java.util.ArrayList;
import java.util.List;

public class Plandetailforpatientadt extends RecyclerView.Adapter<Plandetailforpatientadt.MyViewHolder> {

    Context context;
    String doc_id, service_id;
    ArrayList<drplanmodel> drplanmodels;
    String name;

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView textView_plannameprice,textView_docname,textView_purchasenow;
        TextView textView_validity,textView_noofvisit;
        CardView cardView;
        public MyViewHolder(View itemView) {
            super(itemView);
            this.textView_plannameprice = itemView.findViewById(R.id.plannamewithprice);
            this.textView_docname =itemView.findViewById(R.id.docname);
            this.textView_validity = itemView.findViewById(R.id.planvalidity);
            this.textView_noofvisit =itemView.findViewById(R.id.noofvisit);
            this.textView_purchasenow =itemView.findViewById(R.id.purchasenow);
            this.cardView = itemView.findViewById(R.id.card_view);
        }
    }

    public Plandetailforpatientadt(ArrayList<drplanmodel> drplanmodels, Context context,String  name,String doc_id,String service_id) {
        this.drplanmodels=drplanmodels;
        this.context=context;
        this.name=name;
        this.doc_id=doc_id;
        this.service_id=service_id;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent,
                                           int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.plandetaildesign, parent, false);

        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int listPosition)
    {
            holder.textView_plannameprice.setText(drplanmodels.get(listPosition).getPlanname()+" ( Rs "+drplanmodels.get(listPosition).getPlanprice()+" /- )");
holder.textView_docname.setText("Dr "+name);
            holder.textView_noofvisit.setText("Percentage Per Visit "+drplanmodels.get(listPosition).getPlanvalidvisit());
holder.textView_validity.setText("Valid Visit: "+ drplanmodels.get(listPosition).getPlanvalidity());

holder.textView_purchasenow.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        Intent intent= new Intent(context, PayUMoneyActivityforPurchasePlan.class);
        intent.putExtra("plandata",drplanmodels.get(listPosition));
        intent.putExtra("doc_id",drplanmodels.get(listPosition));
        intent.putExtra("service_td",drplanmodels.get(listPosition));
        context.startActivity(intent);
    }
});

    }



    @Override
    public int getItemCount() {
        return drplanmodels.size();
    }
}
