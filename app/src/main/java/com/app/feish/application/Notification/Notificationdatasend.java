package com.app.feish.application.Notification;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import com.app.feish.application.Connectiondetector;
import com.app.feish.application.Patient.BookedAppointmetDetail;
import com.app.feish.application.R;
import com.app.feish.application.Remote.ApiUtils;
import com.app.feish.application.doctor.PreviousPatientdetail;
import com.app.feish.application.modelclassforapi.PatientBookedappointment;
import com.app.feish.application.modelclassforapi.Pojofornotification;
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
import java.text.SimpleDateFormat;

import static com.app.feish.application.Patient.MedicalHitoryp.JSON;

public class Notificationdatasend extends AppCompatActivity {
String userid,appointmentid;
    PatientBookedappointment patientBookedappointment;
Context  context=this;
int code;
Connectiondetector connectiondetector;
    Pojofornotification appointmentdatum;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notificationdatasend);
        userid=getIntent().getStringExtra("userid");
        appointmentid=getIntent().getStringExtra("appointmentid");
        code=Integer.parseInt(getIntent().getStringExtra("code"));
        Log.d("codevalue",String.valueOf(code));
        connectiondetector= new Connectiondetector(getApplicationContext());
        if(connectiondetector.isConnectingToInternet()) {
            if(code==5)
            addingdata();
            else
                status();

        }
        else
            Toast.makeText(context, "No Internet", Toast.LENGTH_SHORT).show();
    }
    private Request family_his() {
        JSONObject postdata = new JSONObject();
        try {
            postdata.put("id", appointmentid);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(JSON, postdata.toString());
        return new Request.Builder()
                .addHeader("X-Api-Key", "AB5433GMDF657VBB")
                .addHeader("Content-Type", "application/json")
                .url(ApiUtils.BASE_URL+"fetchappodetail")
                .post(body)
                .build();

    }
    public void listbookappresponse(String response) {
        Gson gson = new GsonBuilder().create();
        appointmentdatum = gson.fromJson(response, Pojofornotification.class);
    }
    private void addingdata()
    {
        OkHttpClient client = new OkHttpClient();
        Request request = family_his();
        Log.i("", "onClick: "+request);
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                Log.i("Activity", "onFailure: Fail");
            }
            @Override
            public void onResponse(final Response response) throws IOException {

                final String body=response.body().string();
                Log.i("1234add", "onResponse: "+body);
                final EditText editText=findViewById(R.id.et);
                listbookappresponse(body);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try
                        {
                            // Toast.makeText(context, ""+appointmentdatum.getData(), Toast.LENGTH_SHORT).show();
                            JSONObject jsonObject=new JSONObject(body);
                            if(jsonObject.getBoolean("status"))
                            {
                                Intent intent= new Intent(context, PreviousPatientdetail.class);
                                intent.putExtra("code",code);
                                intent.putExtra("userid",userid);
                                intent.putExtra("data",appointmentdatum.getData().get(0));
                                context.startActivity(intent);
                            }
                            else
                            {
                                Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show();
                            }
                        }
                        catch (Exception e)
                        {
                            Toast.makeText(context, ""+e, Toast.LENGTH_SHORT).show();
                        }

                    }
                });
            }

        });

    }
    private Request appostatusdetail() {
        JSONObject postdata = new JSONObject();
        try {
            postdata.put("id", appointmentid);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(JSON, postdata.toString());
        return new Request.Builder()
                .addHeader("X-Api-Key", "AB5433GMDF657VBB")
                .addHeader("Content-Type", "application/json")
                .url(ApiUtils.BASE_URL+"bookedappodetailfornotification")
                .post(body)
                .build();

    }
    public void liststatusdetail(String response) {
        Gson gson = new GsonBuilder().create();
        patientBookedappointment = gson.fromJson(response,PatientBookedappointment.class);
    }
    private void status()
    {
        OkHttpClient client = new OkHttpClient();
        Request request = appostatusdetail();
        Log.i("", "onClick: "+request);
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                Log.i("Activity", "onFailure: Fail");
            }
            @Override
            public void onResponse(final Response response) throws IOException {

                final String body=response.body().string();
                Log.i("1234add", "onResponse: "+body);
                final EditText editText=findViewById(R.id.et);
                liststatusdetail(body);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run()
                    {
                      //  Toast.makeText(context, ""+body, Toast.LENGTH_SHORT).show();
                        Intent i=new Intent(new Intent(context, BookedAppointmetDetail.class));
                        i.putExtra("code",code);
                        i.putExtra("appointmentlist", patientBookedappointment.getAppointmentdata().get(0));
                        i.putExtra("userdetaillist",patientBookedappointment.getAppodata().get(0));
                        context.startActivity(i);
                    }
                });
            }

        });

    }
}
