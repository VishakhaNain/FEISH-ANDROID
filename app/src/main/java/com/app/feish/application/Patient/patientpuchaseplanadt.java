package com.app.feish.application.Patient;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.app.feish.application.R;

import java.util.ArrayList;

/**
 * Created by Vishakha on 12-07-2018.
 */

public class patientpuchaseplanadt extends RecyclerView.Adapter<patientpuchaseplanadt.ProductViewHolder> {


    //this context we will use to inflate the layout
    private Context mCtx;

    //we are storing all the products in a list
    private ArrayList<Product> productList;

    //getting the context and product list with constructor
    public patientpuchaseplanadt(Context mCtx, ArrayList<Product> productList) {
        this.mCtx = mCtx;
        this.productList = productList;
    }

    @Override
    public ProductViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //inflating and returning our view holder
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.cardview_puchaseplanpatient, null);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ProductViewHolder holder, int position) {
        //getting the product of the specified position
        Product product = productList.get(position);

        //binding the data with the viewholder views
        holder.Dname.setText(product.getDname());
        holder.Planpri.setText(product.getPlanpri());
        holder.Planval.setText(String.valueOf(product.getPlanval()));
        holder.value.setText(String.valueOf(product.getValue()));



    }


    @Override
    public int getItemCount() {
        return productList.size();
    }


    class ProductViewHolder extends RecyclerView.ViewHolder {

        TextView Dname, Planpri, Planval,value;

        public ProductViewHolder(View itemView) {
            super(itemView);

            Dname = (TextView) itemView.findViewById(R.id.dname);
            Planpri = (TextView)itemView.findViewById(R.id.planpri);
            Planval = (TextView)itemView.findViewById(R.id.planval);
            value = (TextView)itemView.findViewById(R.id.value);

        }
    }
}