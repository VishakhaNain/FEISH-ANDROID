package com.app.feish.application.Patient;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;
import com.app.feish.application.Connectiondetector;
import com.app.feish.application.R;
import com.app.feish.application.Remote.ApiUtils;
import com.app.feish.application.Remote.EncryptionDecryption;
import com.app.feish.application.modelclassforapi.ContactService_getDetails;
import com.app.feish.application.modelclassforapi.Maindatum;
import com.app.feish.application.modelclassforapi.Pojofornotification;
import com.app.feish.application.modelclassforapi.ServicesWorkingTiming;
import com.app.feish.application.sessiondata.Prefhelper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;
import com.squareup.timessquare.CalendarCellDecorator;
import com.squareup.timessquare.CalendarPickerView;
import com.squareup.timessquare.DefaultDayViewAdapter;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import me.zhanghai.android.materialratingbar.MaterialRatingBar;

import static com.app.feish.application.Patient.MedicalHitoryp.JSON;

public class BookNewAppointment extends AppCompatActivity {
    public static Maindatum ls;
    TextView textView_sername,textView_address;
    MaterialRatingBar materialRatingBar;
    Button btn_s,btn_time;
    String finalresponse;
    ProgressDialog progressDialog;
    com.takisoft.datetimepicker.widget.TimePicker timePicker;
    CalendarPickerView calendar_s;
    SimpleDateFormat DATE_FORMAT,DATE_FORMAT_HH;
    GridView gridView;
    Context  context=this;
    TextView plandetail;
    ArrayList<String> tokenid= new ArrayList<>();
    ContactService_getDetails contactService_getDetails;
    Connectiondetector connectiondetector;
    TextView tv_fname,tv_lname,tv_mob,tv_email,textView_bookappo;
    private Button mDateButton;
    private Button mTimeButton;
    Pojofornotification appointmentdatum;
    String mainappotime="";
    String doctor_id="",doctor_name="",appodate="",appotime="";

    private  void initView()
    {
        connectiondetector= new Connectiondetector(context);
        progressDialog= new ProgressDialog(getApplicationContext());
        progressDialog.setTitle("Loading.....");
        progressDialog.setCanceledOnTouchOutside(false);
        DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
        DATE_FORMAT_HH = new SimpleDateFormat("dd");
        tv_fname=findViewById(R.id.dfirstname);
        tv_lname=findViewById(R.id.dlastname);
        plandetail=findViewById(R.id.plandetail);
        plandetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(BookNewAppointment.this,PuchaseDoctorPlan.class);
                intent.putExtra("doc_id",doctor_id);
                intent.putExtra("doc_name",doctor_name);
                intent.putExtra("service_id",ls.getService().getId());
                startActivity(intent);
            }
        });
        textView_bookappo=findViewById(R.id.bookappo);
        tv_mob=findViewById(R.id.dphone);
        tv_email=findViewById(R.id.demail);
        mDateButton = findViewById(R.id.appdate);
        mTimeButton =findViewById(R.id.apptime);
        textView_sername=findViewById(R.id.servicename);
        materialRatingBar=findViewById(R.id.ratingbar);
        textView_address=findViewById(R.id.address);
        gridView=findViewById(R.id.gridsdate);
        if(connectiondetector.isConnectingToInternet())
            fetchdata();
        else
            Toast.makeText(context, "No Internet!!", Toast.LENGTH_SHORT).show();

        mDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickdate_sdate(0);
            }
        });
        mTimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickdate_time(0);
            }
        });
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_new_appointment);
        initView();

        textView_bookappo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainappotime =appodate+" "+appotime;
                if(connectiondetector.isConnectingToInternet()) {
                    if (mainappotime.trim().equals("")) {
                        Toast.makeText(context, "Choose Date and Time", Toast.LENGTH_SHORT).show();
                    } else

                    {
                        SimpleDateFormat input = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        //   Toast.makeText(context, ""+mainappotime, Toast.LENGTH_SHORT).show();
                        Date d = null;
                        try {
                            d = input.parse(mainappotime);
                            String formatted = input.format(d);
                            //     Toast.makeText(context, ""+formatted, Toast.LENGTH_SHORT).show();
                        } catch (ParseException e) {
                            e.printStackTrace();
                            Toast.makeText(context, "" + e, Toast.LENGTH_SHORT).show();
                        }
                        addingdata();

                    }
                }
                else
                {
                    Toast.makeText(context, "No Internet!!", Toast.LENGTH_SHORT).show();
                }
            }
        });
        ls=(Maindatum)getIntent().getSerializableExtra("fulldetail");
        doctor_id=getIntent().getStringExtra("doc_id");
        doctor_name=getIntent().getStringExtra("doc_name");
        finalfetchtoken();
        textView_sername.setText(ls.getService().getTitle());
        textView_address.setText(ls.getService().getAddress()+" "+ls.getService().getCity()+" "+ls.getService().getLocality());
        materialRatingBar.setRating(Float.parseFloat(ls.getService().getAvg_rating()));
        CustomAdapter_grid arrayAdapter= new CustomAdapter_grid(ls.getServicesWorkingTiming());
        gridView.setAdapter(arrayAdapter);
    }
    void pickdate_sdate(final int flag)
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
                    if (flag == 0)
                    {
                        appodate=testdate;
                        mDateButton.setText(testdate);
                    }
                    dialog1.dismiss();

                }

            }
        });
        dialog1.show();

    }
    void pickdate_time(final int flag)
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
                if(flag==0)
                {
                    if(String.valueOf(timePicker.getHour()).length()==1)
                    {
                        if (String.valueOf(timePicker.getMinute()).equals("0")) {
                            mTimeButton.setText("0" + timePicker.getHour() + ":" + "00" + ":" + "00");
                            appotime="0" + timePicker.getHour() + ":" + "00" + ":" + "00";
                        }
                        else {
                            mTimeButton.setText("0" + timePicker.getHour() + ":" + timePicker.getMinute() + ":" + "00");
                            appotime="0" + timePicker.getHour() + ":" + timePicker.getMinute() + ":" + "00";
                        }
                    }
                    else
                    {
                        if (String.valueOf(timePicker.getMinute()).equals("0")) {
                            mTimeButton.setText(timePicker.getHour() + ":" + "00" + ":" + "00");
                            appotime=timePicker.getHour() + ":" + "00" + ":" + "00";
                        }
                        else {
                            mTimeButton.setText(timePicker.getHour() + ":" + timePicker.getMinute() + ":" + "00");
                            appotime=timePicker.getHour() + ":" + timePicker.getMinute() + ":" + "00";
                        }
                    }
                }

                dialog1.dismiss();
            }
        });
        dialog1.show();

    }
    private boolean validateVitalsign(){
        if(mainappotime.compareTo("")==0)
        {
            Toast.makeText(getApplicationContext(),"Choose Appointment Date And Time",Toast.LENGTH_LONG).show();
            return false;
        }
        else {
            return true;
        }
    }
    private Request family_his() {
        JSONObject postdata = new JSONObject();
        try {
            postdata.put("appointed_timing", mainappotime);
            postdata.put("user_id",Integer.parseInt(Prefhelper.getInstance(context).getUserid()));
            postdata.put("doctor_id",Integer.parseInt(doctor_id));
            postdata.put("service_id",Integer.parseInt(ls.getService().getId()));
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
                .url(ApiUtils.BASE_URL+"addBookAppointment")
                .post(body)
                .build();

    }
    private void addingdata()
    {
        //   progressDialog.show();
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

                                SendNotification(jsonObject.getString("message"));
                            }
                            else
                            {
                                Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show();
                            }
                        }
                        catch (Exception e)
                        {
                            //   progressDialog.dismiss();
                            Toast.makeText(context, ""+e, Toast.LENGTH_SHORT).show();
                        }

                    }
                });
            }

        });

    }
    private Request fetchdoctokenid() {
        JSONObject postdata = new JSONObject();
        try {
            postdata.put("doctor_id",Integer.parseInt(doctor_id));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(JSON, postdata.toString());
        return new Request.Builder()
                .addHeader("X-Api-Key", "AB5433GMDF657VBB")
                .addHeader("Content-Type", "application/json")
                .url(ApiUtils.BASE_URL+"fetchTokenIDfornotification")
                .post(body)
                .build();

    }
    private void finalfetchtoken()
    {
        OkHttpClient client = new OkHttpClient();
        Request request = fetchdoctokenid();
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
                            //    Toast.makeText(context, ""+body, Toast.LENGTH_SHORT).show();
                            try{
                                JSONObject jsonObject= new JSONObject(body);
                                if(jsonObject.getString("message").equals("success"))
                                {
                                    JSONArray jsonArray= jsonObject.optJSONArray("data");
                                    for (int i = 0; i <jsonArray.length() ; i++) {
                                        tokenid.add(jsonArray.getString(i));
                                    }



                                }
                            }
                            catch (JSONException e)
                            {
                                Toast.makeText(context, ""+e, Toast.LENGTH_SHORT).show();
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
    public class CustomAdapter_grid extends BaseAdapter {
        LayoutInflater inflter;
        List<ServicesWorkingTiming> servicesWorkingTimings;

        CustomAdapter_grid( List<ServicesWorkingTiming> servicesWorkingTimings)
        {
            this.servicesWorkingTimings=servicesWorkingTimings;
            inflter = (LayoutInflater.from(context));
        }
        @Override
        public int getCount() {
            return servicesWorkingTimings.size();
        }
        @Override
        public Object getItem(int i) {
            return null;
        }
        @Override
        public long getItemId(int i) {
            return 0;
        }
        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            view = inflter.inflate(R.layout.serviceworktime, null); // inflate the layout
            TextView day =  view.findViewById(R.id.day); // get the reference of ImageView
            TextView sdate =  view.findViewById(R.id.opentime); // get the reference of ImageView
            TextView edate =  view.findViewById(R.id.closetime); // get the reference of ImageView
            switch (servicesWorkingTimings.get(i).getServicesWorkingTiming().getDayOfWeek()){
                case "0":
                    day.setText("Mon");
                    break;
                case "1":
                    day.setText("Tue");
                    break;
                case "2":
                    day.setText("Wed");
                    break;
                case "3":
                    day.setText("Thus");
                    break;
                case "4":
                    day.setText("Fri");
                    break;
                case "5":
                    day.setText("Sat");
                    break;
                case "6":
                    day.setText("Sun");
                    break;

            }

            sdate.setText(servicesWorkingTimings.get(i).getServicesWorkingTiming().getOpenTime());
            edate.setText(servicesWorkingTimings.get(i).getServicesWorkingTiming().getCloseTime());

            return view;
        }
    }
    private void fetchdata()
    {
        OkHttpClient client = new OkHttpClient();
        Request request = drprofile_request();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                Log.i("Activity", "onFailure: Fail");
            }
            @Override
            public void onResponse(final Response response) throws IOException {

                String body=response.body().string();
                drprofileJSON(body);
                final String message = contactService_getDetails.getMessage();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //pb.setVisibility(View.GONE);
                        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                        String decryptphone= EncryptionDecryption.decode(contactService_getDetails.getData().getMobile());
                        if (message.compareTo("success") == 0) {
                            tv_fname.setText(contactService_getDetails.getData().getFirstName());
                            tv_lname.setText(contactService_getDetails.getData().getLastName());
                            tv_email.setText(contactService_getDetails.getData().getIs_active().toString());
                            tv_mob.setText(decryptphone);


                        }else {
                            Toast.makeText(getApplicationContext(), "Fail", Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }

        });

    }
    public void drprofileJSON(String response) {
        Gson gson = new GsonBuilder().create();
        contactService_getDetails = gson.fromJson(response, ContactService_getDetails.class);
    }

    public void listbookappresponse(String response) {
        Gson gson = new GsonBuilder().create();
        appointmentdatum = gson.fromJson(response, Pojofornotification.class);
    }

    private Request drprofile_request() {
        JSONObject postdata = new JSONObject();
        try {
            postdata.put("user_id", Prefhelper.getInstance(BookNewAppointment.this).getUserid());
            //postdata.put("password",lpassword);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(JSON, postdata.toString());
        return new Request.Builder()
                .addHeader("X-Api-Key", "AB5433GMDF657VBB")
                .addHeader("Content-Type", "application/json")
                .url("http://feish.online/apis/getPatientdetails")
                .post(body)
                .build();
    }
    @SuppressLint("StaticFieldLeak")
    public void SendNotification(final String msg)
    {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                //     progressDialog.dismiss();
                AlertDialog.Builder builder;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    builder = new AlertDialog.Builder(context, android.R.style.Theme_Material_Dialog_Alert);
                } else {
                    builder = new AlertDialog.Builder(context);
                }
                builder.setTitle("Message")
                        .setMessage(msg)
                        //.setMessage(appointmentdatum.getAppointment().getDoctorId())

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

            @Override
            protected Void doInBackground(Void... params) {
                try{

                    OkHttpClient client = new OkHttpClient();
                    JSONObject json = new JSONObject();
                    JSONObject dataJson = new JSONObject();
                    JSONObject dataJson1 = new JSONObject();
                    dataJson1.put("code","5");
                    dataJson1.put("userid",appointmentdatum.getData().get(0).getUser().getId());
                    dataJson1.put("appointmentid",appointmentdatum.getData().get(0).getAppointment().getId());
                    dataJson.put("key", "Feish");
                    dataJson.put("title", "New Appointment");
                    dataJson.put("click_action", "com.app.feish.application.fcm_TARGET");
                    json.put("data", dataJson1);
                    json.put("notification", dataJson);
                    json.put("registration_ids", new JSONArray(tokenid));
                    RequestBody body = RequestBody.create(JSON, json.toString());
                    Request request = new Request.Builder()
                            .header("Authorization", "key=" + "AIzaSyBWOYZxRPWsLA6P6Wmrj5esOj7tXfEmNQY")
                            .url("https://fcm.googleapis.com/fcm/send")
                            .post(body)
                            .build();
                    Response response = client.newCall(request).execute();
                    Log.i("response",response.body().string());
                    finalresponse = response.body().string();
                } catch (Exception e) {
                    //Log.d(TAG,e+"");
                }
                return null;
            }
        }.execute();
    }
    private Request appodetail_MDB() {
        JSONObject postdataappo = new JSONObject();
     /*   JSONObject postdatavalue = new JSONObject();
        JSONArray  jsonArrayrecord= new JSONArray();
        JSONObject postdata = new JSONObject();
        JSONObject postdatamain = new JSONObject();*/
        try {
            postdataappo.put("user_id", Integer.parseInt(Prefhelper.getInstance(context).getUserid()));
            postdataappo.put("appodaterime",mainappotime);
            postdataappo.put("doctorname",doctor_name);
            postdataappo.put("doctor_id",doctor_id);
            postdataappo.put("service_id",ls.getService().getId());
            postdataappo.put("address",ls.getService().getAddress());
            postdataappo.put("city",ls.getService().getCity());
            postdataappo.put("service_name",ls.getService().getTitle());

            postdataappo.put("modified_by", Prefhelper.getInstance(context).getUserid());
            postdataappo.put("modified_at",new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(new java.util.Date()));
            postdataappo.put("source_type","mobile");
            postdataappo.put("deleted_flag","0");


        /*    postdata.put("appointment",postdataappo);
            postdatavalue.put("value",postdata);

            jsonArrayrecord.put(postdatavalue);

            postdatamain.put("record",jsonArrayrecord);*/
        } catch(JSONException e){
            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(JSON, postdataappo.toString());
        return new Request.Builder()
                /*.addHeader("X-Api-Key", "AB5433GMDF657VBB")
                .addHeader("Content-Type", "application/json")*/
                .url(ApiUtils.BASE_URLMAngoDB+"add/appointment")
                .post(body)
                .build();

    }
    private void addingdataMDB()
    {
        OkHttpClient client = new OkHttpClient();
        Request request = appodetail_MDB();
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
                        Toast.makeText(context, ""+body, Toast.LENGTH_SHORT).show();
                    }
                });
            }

        });

    }

}
