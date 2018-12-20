package com.app.feish.application.Adpter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.app.feish.application.R;
import com.app.feish.application.model.ingredientitempojo;

import java.util.List;

public class Ingrementadt extends RecyclerView.Adapter<Ingrementadt.MyViewHolder> {

    private List<ingredientitempojo> dataSet;
    Context context;

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView textView_cal,textView_protein,tv_ingrename;
        TextView textView_carb,textView_kcal;
        public MyViewHolder(View itemView) {
            super(itemView);
            this.textView_cal =itemView.findViewById(R.id.tv_cal);
            this.tv_ingrename =itemView.findViewById(R.id.ingrename);
            this.textView_protein =itemView.findViewById(R.id.tv_protein);
            this.textView_carb = itemView.findViewById(R.id.txtcarb);
            this.textView_kcal =itemView.findViewById(R.id.txtkacl);
        }
    }

    public Ingrementadt(List<ingredientitempojo> data, Context context) {
        this.dataSet = data;
        this.context=context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent,
                                           int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.ingredientitem, parent, false);

        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int listPosition) {
        holder.tv_ingrename.setText(dataSet.get(listPosition).getName()+"( "+dataSet.get(listPosition).getQuantity()+" )");
        holder.textView_cal.setText(dataSet.get(listPosition).getCal());
        holder.textView_carb.setText(dataSet.get(listPosition).getCabohy());
       holder.textView_protein.setText(dataSet.get(listPosition).getProtein());
        holder.textView_kcal.setText(dataSet.get(listPosition).getKcal());

    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }
}
