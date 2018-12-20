package com.app.feish.application.Adpter;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.app.feish.application.Patient.Createdietplan;
import com.app.feish.application.R;
import com.app.feish.application.Remote.ApiUtils;
import com.app.feish.application.model.dietplanfulldetailpojo;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.util.List;

import static com.app.feish.application.fragment.ListFamilyhistory.JSON;

/**
 * Created by lenovo on 8/14/2017.
 */

public class CustomAdapter_dietplan extends BaseAdapter {

    Context context;
    List<dietplanfulldetailpojo> list;
    private int userid;
    private  LayoutInflater inflater=null;
    public CustomAdapter_dietplan(Context context, List<dietplanfulldetailpojo> list,int userid) {
        // TODO Auto-generated constructor stub
        this.list=list;
        this.context=context;
        this.userid=userid;
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
      TextView textView_planname,textView_sdate,textView_edate;
      ImageView imageView_edit, imageView_delete;
        }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        Holder holder=new Holder();
        View rowView;

        rowView = inflater.inflate(R.layout.dietplanlist, null);
        holder.textView_planname=rowView.findViewById(R.id.planname);
        holder.textView_sdate=rowView.findViewById(R.id.startdate);
        holder.textView_edate=rowView.findViewById(R.id.enddate);
        holder.imageView_edit=rowView.findViewById(R.id.editdetail);
        holder.imageView_delete=rowView.findViewById(R.id.deletedietplan);

        holder.textView_planname.setText(list.get(position).getPlanname());
        holder.textView_sdate.setText(list.get(position).getSdate());
        holder.textView_edate.setText(list.get(position).getEdate());
        
        holder.imageView_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fetchdata(position);
                
            }
        });
        holder.imageView_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(context,Createdietplan.class);
                intent.putExtra("update",1);
                intent.putExtra("diet_id",list.get(position).getId());
                intent.putExtra("title",list.get(position).getPlanname());
                intent.putExtra("sdate",list.get(position).getSdate());
                intent.putExtra("edate",list.get(position).getEdate());
                Bundle args = new Bundle();
                args.putSerializable("list",list.get(position).getDietlistpojos());
                intent.putExtra("BUNDLE",args);
                context.startActivity(intent);
                
            }
        });


        return rowView;
    }
    private Request DeleteDietplan(int pos)
    {
        JSONObject postdata = new JSONObject();
        try {
            postdata.put("patient_id",userid);
            postdata.put("id",list.get(pos).getId());

        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(JSON, postdata.toString());
        return new Request.Builder()
                .addHeader("X-Api-Key", "AB5433GMDF657VBB")
                .addHeader("Content-Type", "application/json")
                .url(ApiUtils.BASE_URL+"deleteDietPlan")
                .post(body)
                .build();
    }
    private void fetchdata(final int pos)
    {
        OkHttpClient client = new OkHttpClient();
        Request request = DeleteDietplan(pos);
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

                        try {
                            JSONObject jsonObject = new JSONObject(body);
                            if (jsonObject.getBoolean("status"))
                            {
                                list.remove(pos);
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

                            }
                            else
                                {
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
}
