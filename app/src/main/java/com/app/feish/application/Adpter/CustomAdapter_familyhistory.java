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
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.app.feish.application.R;
import com.app.feish.application.Remote.ApiUtils;
import com.app.feish.application.model.vitalsignlist;
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

public class CustomAdapter_familyhistory extends BaseAdapter {

    Context context;
    List<vitalsignlist> list;
    private  LayoutInflater inflater;
    public CustomAdapter_familyhistory(Context context, List<vitalsignlist> list) {
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
        TextView textView_signnameunit,textView_maxob;
        TextView textView_minob,textView_totalob,textView_remark;
        TextView textView_memame,textView_re;
        TextView textView_diname,textView_staus,textView_desc;
        Button button;
        }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        Holder holder=new Holder();
        View rowView;

        rowView = inflater.inflate(R.layout.vitalsignlist, null);
        holder.textView_signnameunit=  rowView.findViewById(R.id.signnameunit);
        holder.textView_maxob =  rowView.findViewById(R.id.maxob);
        holder.textView_minob =  rowView.findViewById(R.id.minob);
        holder.textView_totalob =  rowView.findViewById(R.id.totalob);
        holder.textView_remark =  rowView.findViewById(R.id.remark);
        holder.textView_memame=  rowView.findViewById(R.id.memname);
        holder.textView_re =  rowView.findViewById(R.id.relation);
        holder.textView_diname =  rowView.findViewById(R.id.dieasename);
        holder.textView_staus =  rowView.findViewById(R.id.stausyear);
        holder.textView_desc =  rowView.findViewById(R.id.desc);
        holder.button =  rowView.findViewById(R.id.delete);

        holder.textView_memame.setText("Name  :");
        holder.textView_re.setText("Relation  :");
        holder.textView_diname.setText("Disease Name  :");
        holder.textView_staus.setText("Status  :");
        holder.textView_desc.setText("Desc  :");

        holder.textView_signnameunit.setText(list.get(position).getName()+" ("+list.get(position).getAge()+" )");
        holder.textView_maxob.setText(list.get(position).getRelation());
        holder.textView_minob.setText(list.get(position).getDisease());
        holder.textView_totalob.setText(list.get(position).getStatus()+" ("+list.get(position).getYear()+" )");
        holder.textView_remark.setText(list.get(position).getDesc());
        rowView.findViewById(R.id.delete).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DeleteFamilyHistory(list.get(position).getId(),position);
            }
        });


        return rowView;
    }
    private void DeleteFamilyHistory(int id, final int pos)
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
                .url(ApiUtils.BASE_URL+"detelefamilyhistory")
                .post(body)
                .build();
    }

}
