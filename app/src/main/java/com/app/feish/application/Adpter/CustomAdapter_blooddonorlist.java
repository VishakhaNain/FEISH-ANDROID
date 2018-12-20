package com.app.feish.application.Adpter;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.app.feish.application.Patient.CommunicatetoDoc;
import com.app.feish.application.R;
import com.app.feish.application.model.bookedappointmentpatient;
import com.app.feish.application.model.searchdoctorpojo;
import com.app.feish.application.modelclassforapi.ContactServiceforMsg;
import com.app.feish.application.sessiondata.Prefhelper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

import static com.app.feish.application.Patient.CommunicatetoDoc.JSON;

/**
 * Created by lenovo on 8/14/2017.
 */

public class CustomAdapter_blooddonorlist extends BaseAdapter {

    Context context;
    List<bookedappointmentpatient> list;
    ContactServiceforMsg contactServiceforMsg;
    private  LayoutInflater inflater=null;
    public CustomAdapter_blooddonorlist(Context context, List<bookedappointmentpatient> list) {
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
        TextView tv_name,tv_loc,tv_bg,tv_ge,tv_send;
        LinearLayout linearLayout_book,ll_map;
        }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        final Holder holder=new Holder();
        View rowView;

        rowView = inflater.inflate(R.layout.layout_blooddonor, null);
        holder.tv_name=(TextView) rowView.findViewById(R.id.donorname);
        holder.tv_loc=(TextView) rowView.findViewById(R.id.donorloc);
        holder.tv_bg=(TextView) rowView.findViewById(R.id.donorbgrpp);
        holder.tv_ge=(TextView) rowView.findViewById(R.id.donorgender);
        holder.tv_send=(TextView) rowView.findViewById(R.id.msgtodonor);
         holder.tv_name.setText(list.get(position).getDr_name());
         holder.tv_loc.setText(list.get(position).getDr_date());
         holder.tv_bg.setText(list.get(position).getDr_time());
         holder.tv_ge.setText(list.get(position).getDr_loc());

        holder.tv_send.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(list.get(position).getFlag()==1) {
                            final EditText et_sub,et_msg;
                            Button btn_send;
                            final Dialog dialog = new Dialog(context,android.R.style.Theme_Light);
                            //dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                            dialog.setContentView(R.layout.activity_communicateto_doc1);
                            et_sub=dialog.findViewById(R.id.msgToID);
                            et_sub.setHint("Enter Subject");
                            et_msg=dialog.findViewById(R.id.enterMessgId);
                            btn_send=dialog.findViewById(R.id.submitMsgButtonId);
                            btn_send.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    sendmessage(et_sub.getText().toString(),et_msg.getText().toString(),list.get(position).getPat_id());
                                }
                            });
                            dialog.show();
                        }
                    }
                });



        return rowView;
    }
    private void sendmessage(String subject,String message,String Userid)
    {
        OkHttpClient client = new OkHttpClient();
        Request validation_request = msgtodoc_request(subject,message,Userid);
        client.newCall(validation_request).enqueue(new Callback() {

            @Override
            public void onFailure(Request request, IOException e) {


                Log.i("Activity", "onFailure: Fail");
            }

            @Override
            public void onResponse(final Response response) throws IOException {

                final boolean isSuccessful = msgtodocJSON(response.body().string());

                ((Activity) context).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (isSuccessful)
                        {

                            Toast.makeText(context, "Message sent! will respond you shortly!!!", Toast.LENGTH_LONG).show();

                            AlertDialog.Builder builder;
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                builder = new AlertDialog.Builder(context, android.R.style.Theme_Material_Dialog_Alert);
                            } else {
                                builder = new AlertDialog.Builder(context);
                            }
                            builder.setTitle("Message")
                                    .setMessage("Message sent! will respond you shortly!!!")
                                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            // continue with delete
                                            dialog.dismiss();
                                            ((Activity) context).finish();

                                        }
                                    })
                                    /* .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                         public void onClick(DialogInterface dialog, int which) {
                                             // do nothing
                                         }
                                     })*/
                                    // .setIcon(android.R.drawable.m)
                                    .show();
                        } else {
                            Toast.makeText(context, "OOPS!!", Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });
    }
    public boolean msgtodocJSON(String response) {
        Gson gson = new GsonBuilder().create();
        contactServiceforMsg = gson.fromJson(response, ContactServiceforMsg.class);
        return contactServiceforMsg.getStatus();
    }


    private Request msgtodoc_request(String subject,String message,String Userid) {
        JSONObject postdata = new JSONObject();
        try {
            postdata.put("user_id", Prefhelper.getInstance(context).getUserid());
            postdata.put("subject",subject);
            postdata.put("message",message);
            postdata.put("reciever_user_id", Userid);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(JSON, postdata.toString());
        final Request request = new Request.Builder()
                .addHeader("X-Api-Key", "AB5433GMDF657VBB")
                .addHeader("Content-Type", "application/json")
                .url("http://feish.online/apis/communicateToPatient")
                .post(body)
                .build();
        return request;
    }

}
