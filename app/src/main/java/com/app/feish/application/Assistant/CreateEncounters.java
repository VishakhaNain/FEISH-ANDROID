package com.app.feish.application.Assistant;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.app.feish.application.R;
import com.app.feish.application.Remote.ApiUtils;
import com.app.feish.application.doctor.DocAddPlan;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;
import com.squareup.timessquare.CalendarCellDecorator;
import com.squareup.timessquare.CalendarPickerView;
import com.squareup.timessquare.DefaultDayViewAdapter;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import static com.app.feish.application.Patient.MedicalHitoryp.JSON;

public class CreateEncounters extends AppCompatActivity {
String user_id;
    Button btn_s,btn_time;
    com.takisoft.datetimepicker.widget.TimePicker timePicker;
    CalendarPickerView calendar_s;
    EditText et_time,et_date,et_patientid;
    TextView tv_serviceid;
    int service_id;
    Context context=this;
    Button button_createappo;
    String mainappotime="";

    SimpleDateFormat DATE_FORMAT,DATE_FORMAT_HH;
    String patient_id="";
    String str_date="",str_time="",str_patientname="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_encounters);
        user_id=getIntent().getStringExtra("user_id");
        service_id=getIntent().getIntExtra("service_id",0);
        Toast.makeText(context, ""+service_id, Toast.LENGTH_SHORT).show();
        DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
        DATE_FORMAT_HH = new SimpleDateFormat("dd");
        button_createappo=findViewById(R.id.createappo);
        et_time=findViewById(R.id.apptime);
        et_date=findViewById(R.id.date);
        et_patientid=findViewById(R.id.patientid);
        tv_serviceid=findViewById(R.id.serviceid);
        tv_serviceid.setText(""+service_id);

        et_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               pickdate_sdate();
            }
        });
        et_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickdate_time();
            }
        });
        button_createappo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                str_time=et_time.getText().toString();
                str_date=et_date.getText().toString();
                patient_id=et_patientid.getText().toString();
                str_patientname=et_patientid.getText().toString();
                SimpleDateFormat input = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                mainappotime=et_date.getText().toString()+" "+et_time.getText().toString();
                Toast.makeText(context, ""+mainappotime, Toast.LENGTH_SHORT).show();
                Date d = null;
                try
                {
                    d = input.parse(mainappotime);
                    String formatted = input.format(d);
                    Toast.makeText(context, ""+formatted, Toast.LENGTH_SHORT).show();

                } catch (ParseException e) {
                    e.printStackTrace();
                    Toast.makeText(context, ""+e, Toast.LENGTH_SHORT).show();
                }
                if(validateVitalsign())
                {
                    addingdata();
                }
            }
        });

    }
    private boolean validateVitalsign(){
        if(mainappotime.compareTo("")==0)
        {
            Toast.makeText(getApplicationContext(),"Choose Appointment Date And Time",Toast.LENGTH_LONG).show();
            return false;
        }
        else  if(str_date.compareTo("")==0)
        {
            Toast.makeText(getApplicationContext(),"Choose Appointment Date",Toast.LENGTH_LONG).show();
            return false;
        }
        else  if(str_time.compareTo("")==0)
        {
            Toast.makeText(getApplicationContext(),"Choose Appointment Time",Toast.LENGTH_LONG).show();
            return false;
        }
        else  if(str_patientname.compareTo("")==0)
        {
            Toast.makeText(getApplicationContext(),"Enter Patient Id",Toast.LENGTH_LONG).show();
            return false;
        }
        else {
            return true;
        }
    }
    public void openentry(View view)
    {
        startActivity(new Intent(CreateEncounters.this,PatientEntry.class));
    }
    public void openpatient(View view)
    {
        Intent intent=new Intent(CreateEncounters.this,DocAddPlan.class);
        intent.putExtra("user_id",user_id);
        startActivity(intent);
    }
    private Request family_his() {
        JSONObject postdata = new JSONObject();
        try {
            postdata.put("appointed_timing", mainappotime);
            postdata.put("user_id",Integer.parseInt(patient_id));
            postdata.put("doctor_id",Integer.parseInt(user_id));
            postdata.put("service_id",service_id);
            postdata.put("scheduled_date",String.valueOf(new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(new java.util.Date())));
            postdata.put("status","0");
            //postdata.put("password",lpassword);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(JSON, postdata.toString());
        return new Request.Builder()
                .addHeader("X-Api-Key", "AB5433GMDF657VBB")
                .addHeader("Content-Type", "application/json")
                .url(ApiUtils.BASE_URL+"addBookAppointmentfromassistant")
                .post(body)
                .build();

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
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try
                        {
                            JSONObject jsonObject=new JSONObject(body);
                            if(jsonObject.getBoolean("status"))
                            {
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
                                                finish();
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
    void pickdate_sdate()
    {
        final Dialog dialog1 = new Dialog(context);
        dialog1.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog1.setContentView(R.layout.camgal);
        dialog1.setCanceledOnTouchOutside(false);
        final Calendar nextYear=Calendar.getInstance();
        nextYear.add(Calendar.YEAR,1);
        final  Calendar lastYear=Calendar.getInstance();
        lastYear.add(Calendar.YEAR,-1);
        btn_s=dialog1.findViewById(R.id.ok);
        calendar_s = dialog1.findViewById(R.id.calendar_view);
        calendar_s.init(lastYear.getTime(), nextYear.getTime()) //
                .inMode(CalendarPickerView.SelectionMode.SINGLE) //
                .withSelectedDate(new Date());
        calendar_s.setCustomDayView(new DefaultDayViewAdapter());
        Date today =new Date();
        calendar_s.setDecorators(Collections.<CalendarCellDecorator>emptyList());
        calendar_s.init(today,nextYear.getTime())
                .withSelectedDate(today).inMode(CalendarPickerView.SelectionMode.SINGLE);
        btn_s.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayList<Date> mydates = (ArrayList<Date>)calendar_s.getSelectedDates();
                for (int i = 0; i <mydates.size() ; i++) {
                    Date tempdate = mydates.get(i);
                    String testdate = DATE_FORMAT.format(tempdate);
                        et_date.setText(testdate);

                    dialog1.dismiss();

                }

            }
        });
        dialog1.show();

    }
    void pickdate_time()
    {
        final Dialog dialog1 = new Dialog(context);
        dialog1.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog1.setContentView(R.layout.timepickfer);
        dialog1.setCanceledOnTouchOutside(false);
        final Calendar nextYear=Calendar.getInstance();
        nextYear.add(Calendar.YEAR,1);
        final  Calendar lastYear=Calendar.getInstance();
        lastYear.add(Calendar.YEAR,-1);
        btn_time=dialog1.findViewById(R.id.ok);
        timePicker=dialog1.findViewById(R.id.calendar_view);
        btn_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                                  if(String.valueOf(timePicker.getHour()).length()==1) {
                        if (String.valueOf(timePicker.getMinute()).equals("0"))
                            et_time.setText("0"+timePicker.getHour() + ":" + "00" + ":" + "00");
                        else
                            et_time.setText("0"+timePicker.getHour() + ":" + timePicker.getMinute() + ":" + "00");
                    }
                    else
                    {
                        if (String.valueOf(timePicker.getMinute()).equals("0"))
                            et_time.setText(timePicker.getHour() + ":" + "00" + ":" + "00");
                        else
                            et_time.setText(timePicker.getHour() + ":" + timePicker.getMinute() + ":" + "00");
                    }


                dialog1.dismiss();
            }
        });
        dialog1.show();

    }
}
