package com.app.feish.application.Patient;


import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.app.feish.application.R;
import com.app.feish.application.modelclassforapi.ContactServiceforMsg;
import com.app.feish.application.modelclassforapi.DoctorDatum;
import com.app.feish.application.modelclassforapi.DoctorService;
import com.app.feish.application.sessiondata.Prefhelper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;


public class CommunicatetoDoc extends AppCompatActivity {
    //ServiceData serviceData;
    DoctorService doctorService;
    DoctorDatum doctorData;
    Context context=this;
    ContactServiceforMsg contactServiceforMsg;
   /* Datum_FetchingMsg datum_fetchingMsg;
    ContactServiceforMsg contactServiceforMsg;*/
    protected static String userid;
    public static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");
    EditText msgtoDoctor_et, enterMsg_et, enterSub_et;
    Button submitMsgbtn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_communicateto_doc1);
        initView();
    }

    private void initView()
    {
        Toolbar toolbar =findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setTitle("Message");
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        doctorData= (DoctorDatum) getIntent().getSerializableExtra("userData");
        msgtoDoctor_et = (EditText) findViewById(R.id.msgToID);
        msgtoDoctor_et.setText(doctorData.getUser().getFirstName()+" "+doctorData.getService().getUserId());
        enterMsg_et = (EditText) findViewById(R.id.enterMessgId);
        enterSub_et = (EditText) findViewById(R.id.enterSubId);
        submitMsgbtn = (Button) findViewById(R.id.submitMsgButtonId);

        submitMsgbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                OkHttpClient client = new OkHttpClient();
                Request validation_request = msgtodoc_request();
                client.newCall(validation_request).enqueue(new Callback() {

                    @Override
                    public void onFailure(Request request, IOException e) {


                        Log.i("Activity", "onFailure: Fail");
                    }

                    @Override
                    public void onResponse(final Response response) throws IOException {

                        final boolean isSuccessful = msgtodocJSON(response.body().string());
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (isSuccessful)
                                {

                                    Toast.makeText(getApplicationContext(), "Message sent! will respond you shortly!!!", Toast.LENGTH_LONG).show();

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
                                                    finish();
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
                                    Toast.makeText(getApplicationContext(), "OOPS!!", Toast.LENGTH_LONG).show();
                                }
                            }
                        });
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


    private Request msgtodoc_request() {
        JSONObject postdata = new JSONObject();
        try {
            postdata.put("user_id", Prefhelper.getInstance(CommunicatetoDoc.this).getUserid());
            postdata.put("subject", enterSub_et.getText().toString().trim());
            postdata.put("message", enterMsg_et.getText().toString().trim());
            postdata.put("reciever_user_id", doctorData.getService().getUserId());

        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(JSON, postdata.toString());
        final Request request = new Request.Builder()
                .addHeader("X-Api-Key", "AB5433GMDF657VBB")
                .addHeader("Content-Type", "application/json")
                .url("http://feish.online/apis/communicateToDoctor")
                .post(body)
                .build();
        return request;
    }
}
