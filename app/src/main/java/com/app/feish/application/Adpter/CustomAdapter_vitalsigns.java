package com.app.feish.application.Adpter;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.app.feish.application.R;
import com.app.feish.application.Remote.ApiUtils;
import com.app.feish.application.model.vitalsignlist;
import com.daimajia.swipe.SwipeLayout;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

import static com.app.feish.application.Patient.VitalSigns.JSON;

/**
 * Created by lenovo on 8/14/2017.
 */

public class CustomAdapter_vitalsigns extends RecyclerView.Adapter<CustomAdapter_vitalsigns.MyViewHolder> {

    private List<vitalsignlist> dataSet;
    Context context;

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView textView_signnameunit,textView_maxob;
        TextView textView_minob,textView_totalob,textView_remark;
        SwipeLayout swipeLayout;
        Button delete;
        public MyViewHolder(View itemView) {
            super(itemView);
            this.textView_signnameunit=  itemView.findViewById(R.id.signnameunit);
            this.textView_maxob =  itemView.findViewById(R.id.maxob);
            this.textView_minob =  itemView.findViewById(R.id.minob);
            this.textView_totalob =  itemView.findViewById(R.id.totalob);
            this.textView_remark =  itemView.findViewById(R.id.remark);
            this.swipeLayout =  itemView.findViewById(R.id.swipe);
            this.delete =  itemView.findViewById(R.id.delete);
        }
    }

    public CustomAdapter_vitalsigns(List<vitalsignlist> data, Context context) {
        this.dataSet = data;
        this.context=context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent,
                                           int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.vitalsignlist, parent, false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int listPosition) {

        holder.textView_signnameunit.setText(String.format("%s (%s )", dataSet.get(listPosition).getSign(), dataSet.get(listPosition).getUnit()));
        holder.textView_maxob.setText(dataSet.get(listPosition).getMaxo());
        holder.textView_minob.setText(dataSet.get(listPosition).getMino());
        holder.textView_totalob.setText(dataSet.get(listPosition).getTotalo());
        holder.textView_remark.setText(dataSet.get(listPosition).getRemark());
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "delete", Toast.LENGTH_SHORT).show();
                Deletevitalsign(dataSet.get(holder.getAdapterPosition()).getId(),holder.getAdapterPosition());
            }
        });



    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }


    private void Deletevitalsign(int id, final int pos)
    {
        OkHttpClient client = new OkHttpClient();
        Request request = AddVitalSignApp(id);
        Log.i("", "onClick: " + request);
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                Log.i("Activity", "onFailure: Fail");
            }

            @Override
            public void onResponse(final Response response) throws IOException {

                final String body = response.body().string();
                Log.i("1234check", "onResponse: " + body);

                ((Activity)context).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(context, ""+body, Toast.LENGTH_SHORT).show();
                        try {
                            JSONObject jsonObject = new JSONObject(body);
                            if (jsonObject.getBoolean("status")) {
                                dataSet.remove(pos);
                                notifyDataSetChanged();
                                AlertDialog.Builder builder;
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                    builder = new AlertDialog.Builder(context, android.R.style.Theme_Material_Dialog_Alert);
                                } else {
                                    builder = new AlertDialog.Builder(context);
                                }
                                builder.setTitle("Message")
                                        .setMessage(jsonObject.getString("message"))
                                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {
                                                // continue with delete
                                               dialog.dismiss();
                                            }
                                        })

                                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {
                                                // do nothing
                                            }
                                        })

                                        // .setIcon(android.R.drawable.m)

                                        .show();
                                builder.setCancelable(false);

                            } else {
                                Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception e) {
                            Toast.makeText(context, "" + e, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }

        });
    }
    private Request AddVitalSignApp(int id)
    {
        JSONObject postdata = new JSONObject();
        try {
            postdata.put("id",id);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(JSON, postdata.toString());
        return new Request.Builder()
                .addHeader("X-Api-Key", "AB5433GMDF657VBB")
                .addHeader("Content-Type", "application/json")
                .url(ApiUtils.BASE_URL+"deleteVitalSign")
                .post(body)
                .build();
    }
}
