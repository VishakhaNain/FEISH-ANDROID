package com.app.feish.application.doctor;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.app.feish.application.Connectiondetector;
import com.app.feish.application.R;
import com.app.feish.application.model.serviceworkinghours;
import com.appyvet.materialrangebar.RangeBar;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import me.tittojose.www.timerangepicker_library.TimeRangePickerDialog;

public class AddWorkingHours extends AppCompatActivity implements TimeRangePickerDialog.IOnTimeRangeSelected  {
    Toolbar toolbar;
    com.appyvet.materialrangebar.RangeBar rangeBar;
    String day[]={"Monday","Tuesday","Wednesday","Thusday","Friday","Saturday","Sunday"};
    ListView listView;
    String service_id;
    public static final MediaType JSON = MediaType.parse("application/json:charset=utf-8");
    Context context=this;
    ProgressDialog progressDialog;
    Spinner spinner_spinner_weekday;
    int weekday=8;
    TextView textView,txt_stime,txt_etime,textView_addwork;
    public static final String TIMERANGEPICKER_TAG = "timerangepicker";
    Calendar calendar_sdate=Calendar.getInstance();
    Calendar calendar_edate=Calendar.getInstance();
    Connectiondetector connectiondetector;
    TextView textView_listopen_time,textView_listclose_time;
ImageView imageView_updateworkhr,imageView_deleteworkhr;
    ArrayList<serviceworkinghours> serviceworkinghourslist= new ArrayList<>();
    int postoupdate=8;
    ArrayList<Integer> dayofweek= new ArrayList<>();
    int idtoupdate=0;
    private  void  initView()
    {
        progressDialog=new ProgressDialog(context);
        progressDialog.setTitle("Adding....");
        progressDialog.setCanceledOnTouchOutside(false);
        connectiondetector= new Connectiondetector(getApplicationContext());
        listView=findViewById(R.id.list);
        textView=findViewById(R.id.title);
        txt_stime=findViewById(R.id.s_time);
        txt_etime=findViewById(R.id.e_time);
        spinner_spinner_weekday=findViewById(R.id.spinner_weekday);
        rangeBar=findViewById(R.id.rangesb);
        textView_addwork=findViewById(R.id.addwork);
        toolbar =  findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        toolbar.setTitle("Working Hours");
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        textView_listopen_time=findViewById(R.id.listopen_time);
        textView_listclose_time=findViewById(R.id.listclose_time);
        imageView_updateworkhr=findViewById(R.id.updateworkhr);
         imageView_deleteworkhr=findViewById(R.id.deleteworkhrs);

         imageView_updateworkhr.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 if(postoupdate==8)
                 {
                     Toast.makeText(context, "Choose some day to update", Toast.LENGTH_SHORT).show();
                     }
                 else
                 {
                     spinner_spinner_weekday.setSelection(postoupdate+1);
                     txt_stime.setText(textView_listopen_time.getText().toString());
                     txt_etime.setText(textView_listclose_time.getText().toString());

                     if(txt_etime.getText().toString().equals("Add Close Time"))
                     {
                         textView_addwork.setText("Add Working Hours");
                     }
                     else
                     {
                         textView_addwork.setText("Update Working Hours");
                     }

                 }

             }
         });

        FloatingActionButton fab =  findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_working_hours);
      initView();
        service_id=getIntent().getStringExtra("service_id");
        if(connectiondetector.isConnectingToInternet())
            fetchworkinghr();
        else
            Toast.makeText(context, "No Internet!!", Toast.LENGTH_SHORT).show();

        if (savedInstanceState != null) {
            TimeRangePickerDialog tpd = (TimeRangePickerDialog)getSupportFragmentManager()
                    .findFragmentByTag(TIMERANGEPICKER_TAG);
            if (tpd != null) {
                // tpd.(this);
            }
        }
        textView_addwork.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(connectiondetector.isConnectingToInternet())
                {
                    if (weekday == 8 || txt_stime.getText().toString().equals("Add Open Time") || txt_etime.getText().toString().equals("Add Close Time")) {
                        Toast.makeText(context, "Some options may not be selected....", Toast.LENGTH_SHORT).show();
                    }
                    else if(idtoupdate==0)
                        fetchdata();
                    else
                        updateservicedata();
                }
                else
                {
                    Toast.makeText(context, "No Internet!!", Toast.LENGTH_SHORT).show();
                }


            }
        });
        spinner_spinner_weekday.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if(position!=0)
                {
                    weekday=position-1;
                }
                else
                {
                    Toast.makeText(context, "Choose Valid Weekday", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        rangeBar.setOnRangeBarChangeListener(new RangeBar.OnRangeBarChangeListener() {
            @Override
            public void onRangeChangeListener(RangeBar rangeBar, int leftPinIndex, int rightPinIndex, String leftPinValue, String rightPinValue) {
                txt_stime.setText(""+leftPinValue+": 00");
                txt_etime.setText(""+rightPinValue+": 00");
            }
        });
       // rangeBar.setRangePinsByValue(0, (float) (24*0.2));
        ArrayAdapter<String> stringArrayAdapter= new ArrayAdapter<>(context,R.layout.list_item,day);
        listView.setAdapter(stringArrayAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
              // TextView textView1= view.findViewById(R.id.txt);
             //  textView1.setTextColor(Color.WHITE);
                       textView.setText("Add Working Hours For "+day[position]);
                       if(dayofweek.contains(position))
                       {
                           int a=dayofweek.indexOf(position);
                           textView_listopen_time.setText(serviceworkinghourslist.get(a).getOpen_time());
                           textView_listclose_time.setText(serviceworkinghourslist.get(a).getClose_time());
                           postoupdate=position;
                           idtoupdate=serviceworkinghourslist.get(a).getId();
                       }
                       else
                       {
                           textView_listopen_time.setText("Add Open Time");
                           textView_listclose_time.setText("Add Close Time");
                           postoupdate=position;
                           idtoupdate=0;
                       }


            }
        });

    }
    @Override
    public void onTimeRangeSelected(int startHour, int startMin, int endHour, int endMin) {
        calendar_sdate.set(Calendar.HOUR_OF_DAY, startHour);
        calendar_sdate.set(Calendar.MINUTE, startMin);
        calendar_sdate.set(Calendar.SECOND, 0);
        calendar_edate.set(Calendar.HOUR_OF_DAY, endHour);
        calendar_edate.set(Calendar.MINUTE, endMin);
        calendar_edate.set(Calendar.SECOND, 0);

        String stime = new SimpleDateFormat("HH:mm:ss").format(calendar_sdate.getTime());
        String etime = new SimpleDateFormat("HH:mm:ss").format(calendar_edate.getTime());
        txt_stime.setText(stime);
        txt_etime.setText(etime);
    }
    public void Onclickb(View  view)
    {
        TimeRangePickerDialog timeRangePickerDialog=new TimeRangePickerDialog();
        timeRangePickerDialog.show(getSupportFragmentManager(), TIMERANGEPICKER_TAG);
    }
    public void fetchdata()
    {
        progressDialog.show();
        OkHttpClient client = new OkHttpClient();
        Request validation_request = listservices();
        client.newCall(validation_request).enqueue(new Callback() {

            @Override
            public void onFailure(Request request, IOException e) {

                // Toast.makeText(getApplicationContext(),"Fail",Toast.LENGTH_LONG).show();
                Log.i("Activity", "onFailure: Fail");
            }

            @Override
            public void onResponse(final Response response) throws IOException {
                 final String body=response.body().string();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try
                        {
                        JSONObject jsonObject = new JSONObject(body);

                        // Toast.makeText(context, ""+body, Toast.LENGTH_SHORT).show();
                        if (jsonObject.getInt("Success") == 1) {
                            //   Toast.makeText(context, "Success", Toast.LENGTH_SHORT).show();


                            AlertDialog.Builder builder;
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                builder = new AlertDialog.Builder(context, android.R.style.Theme_Material_Dialog_Alert);
                            } else {
                                builder = new AlertDialog.Builder(context);
                            }
                            builder.setTitle("Message")
                                    .setMessage(jsonObject.getString("message")+" Add another one?")
                                    .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            // continue with delete
                                          //  finish();
                                            dialog.dismiss();
                                        }
                                    })

                                    .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            // do nothing
                                            finish();
                                        }
                                    })

                                    // .setIcon(android.R.drawable.m)

                                    .show();
                            builder.setCancelable(false);

                        } else {
                            Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show();
                        }
                        progressDialog.dismiss();
                    } catch (Exception e) {
                        progressDialog.dismiss();
                        Toast.makeText(context, "" + e, Toast.LENGTH_SHORT).show();
                    }

                    }
                });
            }
        });

    }

    private Request listservices() {
        JSONObject postdata = new JSONObject();
        try {
            postdata.put("day_of_week", weekday);
            postdata.put("open_time",txt_stime.getText().toString());
            postdata.put("close_time",txt_etime.getText().toString());
            postdata.put("service_id",Integer.parseInt(service_id));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(JSON, postdata.toString());
        return new Request.Builder()
                .addHeader("X-Api-Key", "AB5433GMDF657VBB")
                .addHeader("Content-Type", "application/json")
                .url("http://feish.online/apis/AddWokingHours")
                .post(body)
                .build();
    }

    public void updateservicedata()
    {
        progressDialog.show();
        OkHttpClient client = new OkHttpClient();
        Request validation_request = updateservices();
        client.newCall(validation_request).enqueue(new Callback() {

            @Override
            public void onFailure(Request request, IOException e) {

                // Toast.makeText(getApplicationContext(),"Fail",Toast.LENGTH_LONG).show();
                Log.i("Activity", "onFailure: Fail");
            }

            @Override
            public void onResponse(final Response response) throws IOException {
                final String body=response.body().string();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try
                        {
                            JSONObject jsonObject = new JSONObject(body);

                            // Toast.makeText(context, ""+body, Toast.LENGTH_SHORT).show();
                            if (jsonObject.getInt("Success") == 1) {
                                //   Toast.makeText(context, "Success", Toast.LENGTH_SHORT).show();


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

                            } else {
                                Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show();
                            }
                            progressDialog.dismiss();
                        } catch (Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(context, "" + e, Toast.LENGTH_SHORT).show();
                        }

                    }
                });
            }
        });

    }

    private Request updateservices() {
        JSONObject postdata = new JSONObject();
        try {
            postdata.put("day_of_week", weekday);
            postdata.put("id", idtoupdate);
            postdata.put("open_time",txt_stime.getText().toString());
            postdata.put("close_time",txt_etime.getText().toString());
            postdata.put("service_id",Integer.parseInt(service_id));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(JSON, postdata.toString());
        return new Request.Builder()
                .addHeader("X-Api-Key", "AB5433GMDF657VBB")
                .addHeader("Content-Type", "application/json")
                .url("http://feish.online/apis/UpdateWokingHours")
                .post(body)
                .build();
    }

    public void fetchworkinghr()
    {
        OkHttpClient client = new OkHttpClient();
        Request validation_request = listworkinghr();
        client.newCall(validation_request).enqueue(new Callback() {

            @Override
            public void onFailure(Request request, IOException e) {

                // Toast.makeText(getApplicationContext(),"Fail",Toast.LENGTH_LONG).show();
                Log.i("Activity", "onFailure: Fail");
            }

            @Override
            public void onResponse(final Response response) throws IOException {
                final String body=response.body().string();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                         try{
                             JSONObject jsonObject= new JSONObject(body);
                             if(jsonObject.getInt("Success")==1)
                             {
                                 JSONArray  jsonArray= jsonObject.getJSONArray("ServicesWorkingTiming");
                                 for (int i = 0; i <jsonArray.length() ; i++) {
                                     JSONObject jsonObject1=jsonArray.getJSONObject(i);
                                     serviceworkinghourslist.add(new serviceworkinghours(jsonObject1.getString("open_time"),jsonObject1.getString("close_time"),jsonObject1.getInt("id"),jsonObject1.getInt("day_of_week")));
                                    dayofweek.add(jsonObject1.getInt("day_of_week"));
                                 }
                             }
                             else
                             {
                                 Toast.makeText(context, "Error!!", Toast.LENGTH_SHORT).show();
                             }
                         }
                         catch (JSONException e)
                         {
                             Toast.makeText(context, ""+e, Toast.LENGTH_SHORT).show();
                         }

                    }
                });
            }
        });

    }

    private Request listworkinghr() {
        JSONObject postdata = new JSONObject();
        try {
            postdata.put("service_id",Integer.parseInt(service_id));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(JSON, postdata.toString());
        return new Request.Builder()
                .addHeader("X-Api-Key", "AB5433GMDF657VBB")
                .addHeader("Content-Type", "application/json")
                .url("http://feish.online/apis/ListWokingHours")
                .post(body)
                .build();
    }


}
