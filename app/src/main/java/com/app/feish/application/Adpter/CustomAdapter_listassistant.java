package com.app.feish.application.Adpter;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.app.feish.application.R;
import com.app.feish.application.Remote.ApiUtils;
import com.app.feish.application.fragment.Listassistant;
import com.app.feish.application.model.listassistantmodel;
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

public class CustomAdapter_listassistant extends BaseAdapter {

    Context context;
    List<listassistantmodel> list;
    private  LayoutInflater inflater=null;
    int code;
    public CustomAdapter_listassistant(Context context, List<listassistantmodel> list,int code) {
        // TODO Auto-generated constructor stub
        this.list=list;
        this.context=context;
        inflater = ( LayoutInflater )context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.code=code;

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
        TextView tv_name,tv_mob,tv_email,tv_pos,tv_servicename;
        ImageView imageView;
        }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        Holder holder=new Holder();
        View rowView;

        rowView = inflater.inflate(R.layout.listassistant, null);
        holder.tv_name=rowView.findViewById(R.id.assi_name);
        holder.tv_servicename=rowView.findViewById(R.id.servicename);
        holder.tv_mob=rowView.findViewById(R.id.assis_mob);
        holder.imageView=rowView.findViewById(R.id.img);
        holder.tv_email=rowView.findViewById(R.id.assis_email);
        holder.tv_pos=rowView.findViewById(R.id.assisal);
        holder.tv_name.setText(list.get(position).getSal()+" "+list.get(position).getFname()+" "+list.get(position).getLname());
        holder.tv_mob.setText(list.get(position).getMob());
        holder.tv_email.setText(list.get(position).getEmail());
        holder.tv_pos.setText(list.get(position).getPos());
        holder.imageView.setImageResource(list.get(position).getImg());
        if(code==1)
            holder.tv_servicename.setVisibility(View.GONE);
        else
            holder.tv_servicename.setText(list.get(position).getPos());



rowView.findViewById(R.id.delete).setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        if(code==0)
        {

               DeleteTreatment(Listassistant.listassistantmodels2.get(position).getId(),position,Listassistant.listassistantmodels2.get(position).getUser_id());
        }

    }
});
        return rowView;
    }
    private void DeleteTreatment(int id, final int pos,int user_id)
    {
        OkHttpClient client = new OkHttpClient();
        Request request = AddVitalSignApp(id,user_id);
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
                            if (jsonObject.getBoolean("status")) {
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
    private Request AddVitalSignApp(int id,int user_id)
    {
        JSONObject postdata = new JSONObject();
        try {
            postdata.put("id",id);
            postdata.put("user_id",user_id);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(JSON, postdata.toString());
        return new Request.Builder()
                .addHeader("X-Api-Key", "AB5433GMDF657VBB")
                .addHeader("Content-Type", "application/json")
                .url(ApiUtils.BASE_URL+"deteleDoctorAssistant")
                .post(body)
                .build();
    }

}
