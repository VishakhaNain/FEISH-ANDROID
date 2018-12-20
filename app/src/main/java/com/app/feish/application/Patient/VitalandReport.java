package com.app.feish.application.Patient;


import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.util.SparseArray;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.app.feish.application.Adpter.Dignosticreport;
import com.app.feish.application.R;
import com.app.feish.application.Remote.ApiUtils;
import com.app.feish.application.modelclassforapi.ContactService_getDetails;
import com.app.feish.application.sessiondata.Prefhelper;
import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.app.feish.application.Patient.ConsultDoctor.JSON;

public class VitalandReport extends AppCompatActivity {

    SurfaceView cameraView;
    ContactService_getDetails contactService_getDetails;
    TextView textView;
    CameraSource cameraSource;
    RecyclerView recyclerView;
    Context context=this;
    final int RequestCameraPermissionID = 1001;
TextView textView_name,textView_date,textView_ge;
TextView tv_reportname,tv_reportstatus,tv_effdatetime,tv_issuedate,tv_name,tv_bloodgrp,tv_gen;
public static String effetcdate="",issuetime="";
    List<String> Bllodgrp;
    LinearLayoutManager layoutManager;
    ArrayList<String> reportlist= new ArrayList<>();
    ArrayList<String> reportlisttitle= new ArrayList<>();
    TextView scan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_readingtextvalue);
        recyclerView=findViewById(R.id.recyclerview);
        layoutManager= new LinearLayoutManager(context);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setNestedScrollingEnabled(false);
            scan=findViewById(R.id.scan);


        scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    Intent intent=new Intent(VitalandReport.this,Report.class);
                    intent.putExtra("userid",Prefhelper.getInstance(VitalandReport.this).getUserid());
                    startActivity(intent);

            }
        });

        Bllodgrp = Arrays.asList(getResources().getStringArray(R.array.Blood_spinner));
//        textView=findViewById(R.id.htmltext);
        tv_reportname=findViewById(R.id.reportname);
        tv_reportstatus=findViewById(R.id.reportstatus);
        tv_effdatetime=findViewById(R.id.effdatetime);
        tv_issuedate=findViewById(R.id.issuedate);
        tv_name=findViewById(R.id.patient_name);
        tv_bloodgrp=findViewById(R.id.patient_blood);
        tv_gen=findViewById(R.id.patient_gen);
        contactService_getDetails=(ContactService_getDetails) getIntent().getSerializableExtra("data");
        tv_name.setText(contactService_getDetails.getData().getFirstName()+" "+contactService_getDetails.getData().getLastName());

        if(contactService_getDetails.getData().getBloodGroup()!=null) {
            if (Integer.parseInt(contactService_getDetails.getData().getBloodGroup().toString()) < Bllodgrp.size())
                tv_bloodgrp.setText(Bllodgrp.get(Integer.parseInt(contactService_getDetails.getData().getBloodGroup().toString())));
        }
        else
        {
            tv_bloodgrp.setText("Not Known");
        }

        if(contactService_getDetails.getData().getGender().equals("1"))
        tv_gen.setText("M");
        else
            tv_gen.setText("F");
       // addingdataMDB();

        JSONencode();
        Toast.makeText(context, ""+Prefhelper.getInstance(context).getUserid(), Toast.LENGTH_SHORT).show();

    }
    private Request family_hisMDB() {
        JSONObject postdata = new JSONObject();
        try {
            postdata.put("user_id", Prefhelper.getInstance(context).getUserid());

        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(JSON, postdata.toString());
        return new Request.Builder()
                /*.addHeader("X-Api-Key", "AB5433GMDF657VBB")
                .addHeader("Content-Type", "application/json")*/
                .addHeader("Content-Type", "application/json")
                .url("http://192.168.1.16:8080/PatientReport/get/reports")
                .post(body)
                .build();

    }
    private void JSONencode()
    {
        try {
            JSONArray jsonArray=new JSONArray(ApiUtils.JSONstructre);
            for (int i = 0; i <jsonArray.length() ; i++) {
                JSONObject jsonObject= jsonArray.getJSONObject(i);
                tv_reportname.setText(jsonObject.getString("resourceType"));
                JSONObject jsonObject1=jsonObject.getJSONObject("text");
                if(jsonObject.has("code")) {
                    JSONObject jsonObject_code = jsonObject.getJSONObject("code");
                    if(jsonObject_code.has("text"))
                        reportlisttitle.add(jsonObject_code.optString("text"));
                    else
                        reportlisttitle.add("");
                }
               /* tv_effdatetime.setText(jsonObject.getString("effectiveDateTime"));
                tv_issuedate.setText(jsonObject.getString("issued"));*/

                effetcdate=jsonObject.getString("effectiveDateTime");
                issuetime=jsonObject.getString("issued");

                //   Toast.makeText(this, ""+jsonObject1.getString("div"), Toast.LENGTH_SHORT).show();
                //   textView.setText(Html.fromHtml(jsonObject1.getString("div")));
                reportlist.add(jsonObject1.getString("div"));
                tv_reportstatus.setText("Status  :"+jsonObject.getString("status"));


            }
            Dignosticreport dignosticreport= new Dignosticreport(context,reportlist,reportlisttitle);
            recyclerView.setAdapter(dignosticreport);

        } catch (JSONException e) {
            Toast.makeText(context, ""+e, Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }
    private void addingdataMDB()
    {
        OkHttpClient client = new OkHttpClient();
        Request request = family_hisMDB();
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
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONArray jsonArray=new JSONArray(body);
                            for (int i = 0; i <jsonArray.length() ; i++) {
                                JSONObject jsonObject= jsonArray.getJSONObject(i);
                                tv_reportname.setText(jsonObject.getString("resourceType"));
                                JSONObject jsonObject1=jsonObject.getJSONObject("text");
                                if(jsonObject.has("code")) {
                                    JSONObject jsonObject_code = jsonObject.getJSONObject("code");
                                    if(jsonObject_code.has("text"))
                                    reportlisttitle.add(jsonObject_code.optString("text"));
                                    else
                                        reportlisttitle.add("");
                                }
                                tv_effdatetime.setText(jsonObject.getString("effectiveDateTime"));
                                tv_issuedate.setText(jsonObject.getString("issued"));

                                //   Toast.makeText(this, ""+jsonObject1.getString("div"), Toast.LENGTH_SHORT).show();
                             //   textView.setText(Html.fromHtml(jsonObject1.getString("div")));
                                reportlist.add(jsonObject1.getString("div"));
                                tv_reportstatus.setText("Status  :"+jsonObject.getString("status"));


                            }
                            Dignosticreport dignosticreport= new Dignosticreport(context,reportlist,reportlisttitle);
                            recyclerView.setAdapter(dignosticreport);

                        } catch (JSONException e) {
                            Toast.makeText(context, ""+e, Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                        }
                    }
                });
            }

        });

    }

    private void vitalreport()
    {
        textView_name=findViewById(R.id.text_view_name);
        textView_date=findViewById(R.id.text_view_date);
        textView_ge=findViewById(R.id.text_view_gender);
        cameraView = findViewById(R.id.surface_view);
        // textView =findViewById(R.id.text_view);

        TextRecognizer textRecognizer = new TextRecognizer.Builder(getApplicationContext()).build();
        if (!textRecognizer.isOperational()) {
            Log.w("MainActivity", "Detector dependencies are not yet available");
        } else {

            cameraSource = new CameraSource.Builder(getApplicationContext(), textRecognizer)
                    .setFacing(CameraSource.CAMERA_FACING_BACK)
                    .setRequestedPreviewSize(1280, 1024)
                    .setRequestedFps(2.0f)
                    .setAutoFocusEnabled(true)
                    .build();
            cameraView.getHolder().addCallback(new SurfaceHolder.Callback() {
                @Override
                public void surfaceCreated(SurfaceHolder surfaceHolder) {

                    try {
                        if (ActivityCompat.checkSelfPermission
                                (getApplicationContext(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED) {

                            ActivityCompat.requestPermissions(VitalandReport.this, new String[] {Manifest.permission.CAMERA}, RequestCameraPermissionID);
                            return;
                        }
                        cameraSource.start(cameraView.getHolder());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {

                }

                @Override
                public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
                    cameraSource.stop();
                }
            });

            textRecognizer.setProcessor(new Detector.Processor<TextBlock>() {
                @Override
                public void release() {

                }

                @Override
                public void receiveDetections(Detector.Detections<TextBlock> detections) {

                    final SparseArray<TextBlock> items = detections.getDetectedItems();
                    if(items.size() != 0)
                    {

                        textView_name.post(new Runnable() {
                            @Override
                            public void run() {
                                StringBuilder stringBuilder = new StringBuilder();
                                for(int i =0;i<items.size();++i)
                                {
                                    TextBlock item = items.valueAt(i);
                                    stringBuilder.append(item.getValue());
                                    stringBuilder.append("\n");

                                }
                                textView_date.setText(stringBuilder.toString());
                                textView_name.setText(stringBuilder.toString());
                                textView_ge.setText(stringBuilder.toString());

                            }
                        });

                        /*textView.post(new Runnable() {
                            @Override
                            public void run() {
                                StringBuilder stringBuilder = new StringBuilder();
                                for(int i =0;i<items.size();++i)
                                {
                                    TextBlock item = items.valueAt(i);
                                    stringBuilder.append(item.getValue());
                                    stringBuilder.append("\n");
                                }
                                textView.setText(stringBuilder.toString());
                            }
                        });*/
                    }
                }
            });
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case RequestCameraPermissionID: {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                        return;
                    }
                    try {
                        cameraSource.start(cameraView.getHolder());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
            }
            break;
        }
    }

}

