package com.app.feish.application.clinic;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.app.feish.application.R;

import java.util.ArrayList;

public class ListAdapter  extends RecyclerView.Adapter<ListAdapter.ListViewHolder> {
    private Context mCtx;
        ArrayList<listviewpojo> clinicdoctorList;


    public ListAdapter(Context mCtx, ArrayList<listviewpojo> clinicdoctorList) {
        this.mCtx = mCtx;
        this.clinicdoctorList = clinicdoctorList;
    }

    @NonNull
    @Override
    public ListAdapter.ListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.doctor_details, parent, false);
        ListViewHolder myViewHolder = new ListViewHolder(view);
        return myViewHolder;


    }

    @Override
    public void onBindViewHolder(@NonNull ListAdapter.ListViewHolder holder, final int position) {
       final listviewpojo list = clinicdoctorList.get(position);
if(list.val==1)
    holder.title.setVisibility(View.VISIBLE);
        //binding the data with the viewholder views
        holder.name.setText(list.getname());
        holder.email.setText(list.getemail());
        holder.img.setImageResource(list.getImg());
holder.card.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        if(list.val==0) {
            Intent i = new Intent(mCtx, ClinicDoctorSignup.class);
            i.putExtra("pos", position);
            ((Activity) mCtx).startActivityForResult(i, 1);
        }
        else {
            Toast.makeText(mCtx, "already registerd", Toast.LENGTH_SHORT).show();
        }
    }
});





    }

    @Override
    public int getItemCount() {
        return clinicdoctorList.size();


    }


    class ListViewHolder extends RecyclerView.ViewHolder {

        TextView name,email,title;
        ImageView img;
        CardView card;

        public ListViewHolder(View itemView) {
            super(itemView);

            name = (TextView) itemView.findViewById(R.id.name);
            email = (TextView) itemView.findViewById(R.id.email);
            img =  itemView.findViewById(R.id.image);
            card=itemView.findViewById(R.id.card);
            title=itemView.findViewById(R.id.title);

        }
    }
}

