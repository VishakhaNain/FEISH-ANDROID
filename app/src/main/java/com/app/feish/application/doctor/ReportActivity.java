package com.app.feish.application.doctor;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.app.feish.application.Connectiondetector;
import com.app.feish.application.DatabaseAdapter;
import com.app.feish.application.R;
import com.app.feish.application.Remote.ApiUtils;
import com.app.feish.application.model.encountersmodel;
import com.app.feish.application.modelclassforapi.DoctorEncounters;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import static com.app.feish.application.doctor.SetupProfileForDoctor.JSON;

public class ReportActivity extends AppCompatActivity {
    Toolbar toolbar;
    Context context=this;
    ArrayList<encountersmodel> encountersmodels= new ArrayList<>();
    RecyclerView recyclerView;
    CustomAdapter_report customAdapter_report;
       TextView picktime;
    Button btn_s;
    DatabaseAdapter databaseAdapter;
    CalendarPickerView calendar_s;
    long avgwaittime=0,avgspendtime=0;
    SimpleDateFormat DATE_FORMAT;
    DoctorEncounters doctorEncounters_book;
    int cash=0,card=0;
    TextView tv_totalamt,tv_bycash,tv_bycard,tv_avgtime,tv_waittime;
    Connectiondetector connectiondetector;

    private void  initView()
    {
        tv_totalamt=findViewById(R.id.Totalcollectedamount);
        tv_bycash=findViewById(R.id.bycash);
        tv_bycard=findViewById(R.id.bycard);
        tv_avgtime=findViewById(R.id.avgtime);
        tv_waittime=findViewById(R.id.drtime);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report2);
        initView();
        DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
         toolbar =findViewById(R.id.toolbar);
         toolbar.setTitleTextColor(Color.WHITE);
         toolbar.setTitle("Reports");
         toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        databaseAdapter= new DatabaseAdapter(context);
        picktime=findViewById(R.id.picktime);
        picktime.setText(new SimpleDateFormat("yyyy-MM-dd").format(new java.util.Date()));
        picktime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickdate_sdate();

            }
        });
     recyclerView=findViewById(R.id.recycler_view);


        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ShowAlertDialogWithListview();
            }
        });
        LinearLayoutManager layoutManager= new LinearLayoutManager(context);
        recyclerView.setLayoutManager(layoutManager);

        connectiondetector= new Connectiondetector(context);
        if(connectiondetector.isConnectingToInternet())
        {
            fetchBookedappointment(new SimpleDateFormat("yyyy-MM-dd").format(new java.util.Date()));
        }
        else
        {
            Toast.makeText(context, "No Internet", Toast.LENGTH_SHORT).show();
        }

      /*  Cursor cursor= databaseAdapter.getData(picktime.getText().toString());
        while (cursor.moveToNext())
        {
            int id=cursor.getInt(0);
            String pid=cursor.getString(1);
            String appid=cursor.getString(2);
            String retry=cursor.getString(3);
            String rexit=cursor.getString(4);
            String drentry=cursor.getString(5);
            String drexit=cursor.getString(6);
            String date=cursor.getString(7);
            encountersmodels.add(new encountersmodel(pid,appid,retry,rexit,drentry,drexit,date));
        }*/


    }
    public void ShowAlertDialogWithListview()
    {

        List<String> mAnimals = new ArrayList<String>();
        mAnimals.add("Sort By Payment Mode");
        mAnimals.add("Sort By Time");
        //Create sequence of items
        final CharSequence[] sort = mAnimals.toArray(new String[mAnimals.size()]);
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        //Create alert dialog object via builder

        dialogBuilder.setItems(sort, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                String selectedText = sort[item].toString();  //Selected item in listview
            }
        });

        //Show the dialog
        AlertDialog alertDialogObject = dialogBuilder.create();
        Window dialogWindow = alertDialogObject.getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();

        dialogWindow.setGravity(Gravity.RIGHT | Gravity.BOTTOM);
        lp.x = 100; // The new position of the X coordinates
        lp.y = 100; // The new position of the Y coordinates
        lp.width = 200; // Width
        lp.height = 300; // Height
        lp.alpha = 0.7f; // Transparency
        dialogWindow.setAttributes(lp);
        alertDialogObject.show();
    }
    public class CustomAdapter_report extends RecyclerView.Adapter<CustomAdapter_report.MyViewHolder> {

        private List<encountersmodel> dataSet;
        Context context;

         class MyViewHolder extends RecyclerView.ViewHolder {
TextView tv_appid,tv_waittime,tv_timespend,tv_paymentmode,tv_diname;
            LinearLayout linearLayout;
            CardView cardView;
            MyViewHolder(View itemView) {
                super(itemView);
                cardView=itemView.findViewById(R.id.card_view);
                linearLayout=itemView.findViewById(R.id.ll);
                tv_appid=itemView.findViewById(R.id.appid);
                tv_waittime=itemView.findViewById(R.id.waittime);
                tv_timespend=itemView.findViewById(R.id.timespend);
                tv_paymentmode=itemView.findViewById(R.id.paymentmode);
                tv_diname=itemView.findViewById(R.id.diname);


            }
        }
        public CustomAdapter_report(List<encountersmodel> data, Context context) {
            this.dataSet = data;
            this.context=context;
        }

        @NonNull
        @Override
        public CustomAdapter_report.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent,
                                                                         int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.layout_reports, parent, false);

            return new MyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull final CustomAdapter_report.MyViewHolder holder,  int listPosition) {
             holder.tv_appid.setText("Appointment Id : "+dataSet.get(listPosition).appid);
             holder.tv_paymentmode.setText("Payment Mode By: "+dataSet.get(listPosition).pid);
             holder.tv_waittime.setText("Wait Time "+dataSet.get(listPosition).rentry);
             holder.tv_timespend.setText("Spend Time "+dataSet.get(listPosition).rexit);

        }

        @Override
        public int getItemCount() {
            return dataSet.size();
        }
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
        btn_s=(Button)dialog1.findViewById(R.id.ok);
        calendar_s = (CalendarPickerView) dialog1.findViewById(R.id.calendar_view);
        calendar_s.init(lastYear.getTime(), nextYear.getTime()) //
                .inMode(CalendarPickerView.SelectionMode.SINGLE) //
                .withSelectedDate(new Date());
        calendar_s.setCustomDayView(new DefaultDayViewAdapter());
        Date today =new Date();
        calendar_s.setDecorators(Collections.<CalendarCellDecorator>emptyList());
        calendar_s.init(lastYear.getTime(),nextYear.getTime())
                .withSelectedDate(today).inMode(CalendarPickerView.SelectionMode.SINGLE);
        btn_s.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayList<Date> mydates = (ArrayList<Date>)calendar_s.getSelectedDates();
                for (int i = 0; i <mydates.size() ; i++) {
                    Date tempdate = mydates.get(i);
                    String testdate = DATE_FORMAT.format(tempdate);
                    picktime.setText(testdate);
                    fetchBookedappointment(testdate);
                 /*  // Cursor cursor= databaseAdapter.getData("11/07/2018");
                    Cursor cursor= databaseAdapter.getAllData();

                    if(encountersmodels.size()>0)
                        encountersmodels.clear();

                    if(cursor.getCount()>0) {
                        while (cursor.moveToNext()) {
                            Toast.makeText(context, "in loop", Toast.LENGTH_SHORT).show();
                            int id = cursor.getInt(0);
                            String pid = cursor.getString(1);
                            String appid = cursor.getString(2);
                            String retry = cursor.getString(3);
                            String rexit = cursor.getString(4);
                            String drentry = cursor.getString(5);
                            String drexit = cursor.getString(6);
                            String date = cursor.getString(7);
                            Toast.makeText(context, "" + pid, Toast.LENGTH_SHORT).show();
                            SimpleDateFormat formatter = new SimpleDateFormat("hh:mm:ss");

                            // Create a calendar object that will convert the date and time value in milliseconds to date.
                            Calendar calendar = Calendar.getInstance();
                            calendar.setTimeInMillis(Date.parse(retry));
                            Calendar calendar1 = Calendar.getInstance();
                            calendar1.setTimeInMillis(Date.parse(drentry));
                            Toast.makeText(context, "" + formatter.format(calendar.getTime()), Toast.LENGTH_SHORT).show();
                            Toast.makeText(context, "" + formatter.format(calendar1.getTime()), Toast.LENGTH_SHORT).show();
                            printDifference(calendar.getTime(), calendar1.getTime());

                            encountersmodels.add(new encountersmodel(pid, appid, retry, rexit, drentry, drexit, date));

                        }
                    }
                    customAdapter_report.notifyDataSetChanged();*/
                    dialog1.dismiss();

                }

            }
        });
        dialog1.show();

    }
    public long printDifference(Date startDate, Date endDate) {
        //milliseconds
        long different = startDate.getTime() - endDate.getTime();

        System.out.println("startDate : " + startDate);
        System.out.println("endDate : "+ endDate);
        System.out.println("different : " + different);

        long secondsInMilli = 1000;
        long minutesInMilli = secondsInMilli * 60;
        long hoursInMilli = minutesInMilli * 60;
        long daysInMilli = hoursInMilli * 24;

        long elapsedDays = different / daysInMilli;
        different = different % daysInMilli;

        long elapsedHours = different / hoursInMilli;
        different = different % hoursInMilli;

        long elapsedMinutes = different / minutesInMilli;
        different = different % minutesInMilli;

        long elapsedSeconds = different / secondsInMilli;


      return elapsedMinutes;
    }
    private void fetchBookedappointment(String appointment_date)
    {
        card=0;
        cash=0;
        OkHttpClient client = new OkHttpClient();
        Request validation_request = givebookappodata(appointment_date);
        client.newCall(validation_request).enqueue(new Callback() {

            @Override
            public void onFailure(Request request, IOException e) {

                // Toast.makeText(context,"Fail",Toast.LENGTH_LONG).show();
                Log.i("Activity", "onFailure: Fail");
            }

            @Override
            public void onResponse(final Response response) throws IOException {
                final String body = response.body().string();

               runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try
                        {
                            JSONObject jsonObject = new JSONObject(body);
                        if(jsonObject.getBoolean("status"))
                        {
                            listbookappresponse(body);
                            JSONArray  jsonArray=jsonObject.optJSONArray("paymentdetail");
                                for (int i = 0; i <jsonArray.length() ; i++) {
                                    JSONArray  jsonArray1=jsonArray.getJSONArray(i);
                                    JSONObject jsonObject1=jsonArray1.getJSONObject(0);
                                    if(jsonObject1.getString("payment_mode").equals("Cash"))
                                    {
                                        cash=cash+jsonObject1.getInt("amount");
                                    }
                                    else if(jsonObject1.getString("payment_mode").equals("Card"))
                                    {
                                        card=card+jsonObject1.getInt("amount");
                                    }
                                    SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                                        Date date_arrival = simpleDateFormat.parse(doctorEncounters_book.getAppointmentdata().get(i).getAppointment().getPatient_arrival_time());
                                        Date date_in = simpleDateFormat.parse(doctorEncounters_book.getAppointmentdata().get(i).getAppointment().getPatient_in_time());
                                        Date date_out = simpleDateFormat.parse(doctorEncounters_book.getAppointmentdata().get(i).getAppointment().getPatient_out_time());
                                        Date date_exit = simpleDateFormat.parse(doctorEncounters_book.getAppointmentdata().get(i).getAppointment().getPatient_exit_time());
                                       long waittime=printDifference(date_arrival,date_in);
                                       long spendtime=printDifference(date_in,date_out);
                                       avgwaittime=avgwaittime+waittime;
                                       avgspendtime=avgspendtime+spendtime;


                                    customAdapter_report = new CustomAdapter_report(encountersmodels,context);
                                    encountersmodels.add(new encountersmodel(jsonObject1.getString("payment_mode"),doctorEncounters_book.getAppointmentdata().get(i).getAppointment().getId(),String.valueOf(waittime),String.valueOf(spendtime),"","",""));
                                    recyclerView.setAdapter(customAdapter_report);

                                }
                                tv_waittime.setText("Average Wait Time "+String.valueOf(avgwaittime/jsonArray.length())+" min / patient");
                                tv_avgtime.setText("Average Spend Time "+String.valueOf(avgspendtime/jsonArray.length())+" min / patient");
                                tv_bycash.setText("In Cash Rs "+cash+" /-" );
                                tv_bycard.setText("In Card Rs "+card+" /-" );
                                tv_totalamt.setText("Total Collected Amount: Rs "+(cash+card)+" /-");

                        }
                        else
                        {
                            Toast.makeText(context, "No Data Found", Toast.LENGTH_SHORT).show();
                            encountersmodels.clear();
                           // customAdapter_report = new CustomAdapter_report(encountersmodels,context);
                           customAdapter_report.notifyDataSetChanged();
                            tv_waittime.setText("Average Wait Time ");
                            tv_avgtime.setText("Average Spend Time ");
                            tv_bycash.setText("In Cash ");
                            tv_bycard.setText("In Card " );
                            tv_totalamt.setText("Total Collected Amount: ");

                        }
                        } catch (Exception e) {

                        }
                    }
                });
            }
        });

    }
    private Request givebookappodata(String appointment_date) {
        JSONObject postdata = new JSONObject();
        try {
            postdata.put("doctor_id", Prefhelper.getInstance(context).getUserid());
            postdata.put("appointed_timing", appointment_date);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(JSON, postdata.toString());
        return new Request.Builder()
                .addHeader("X-Api-Key", "AB5433GMDF657VBB")
                .addHeader("Content-Type", "application/json")
                .url(ApiUtils.BASE_URL+"reportfecth")
                .post(body)
                .build();
    }
    public void listbookappresponse(String response) {
        Gson gson = new GsonBuilder().create();
        doctorEncounters_book = gson.fromJson(response, DoctorEncounters.class);
    }

}
