package com.app.feish.application.doctor;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.app.feish.application.Adpter.CustomAdapter_encounteres;
import com.app.feish.application.Connectiondetector;
import com.app.feish.application.R;
import com.app.feish.application.Remote.ApiUtils;
import com.app.feish.application.fragment.Addfamilyreport;
import com.app.feish.application.model.encountersmodel;
import com.app.feish.application.modelclassforapi.Datum2;
import com.app.feish.application.modelclassforapi.DoctorEncounters;
import com.app.feish.application.modelclassforapi.ListServicesContact;
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

public class Encounters extends AppCompatActivity {
Toolbar toolbar;
RecyclerView recyclerView,recyclerView_ba;
LinearLayout linearLayout;
TabLayout tabLayout;
EditText editText_edit;
LinearLayoutManager layoutManager,layoutManager1;
Context context=this;
    List<Datum2> l;
    private ListServicesContact serviceResponse;
    private DoctorEncounters doctorEncounters_new,doctorEncounters_book;
    Connectiondetector connectiondetector;
    SimpleDateFormat DATE_FORMAT;
  CustomAdapter_bd  customAdapter_bd;
    static    ArrayList<getdata>mEvents=new ArrayList<>();
CustomAdapter_encounteres customAdapter_encounteres;
ArrayList<encountersmodel> encountersmodels= new ArrayList<>();
CustomAdapter_encounteres customAdapter_encounteres_ba;
ArrayList<encountersmodel> encountersmodels_ba= new ArrayList<>();
TextView picktime;
Spinner spinner_sername_spinner;
CalendarPickerView calendar_s;
Button btn_s;
CalendarCustomView  calendarCustomView;
TextView textView;
String userid="";
String str_sername="";
int assicode=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
        setContentView(R.layout.activity_encounters);
        userid=getIntent().getStringExtra("user_id");
        assicode=getIntent().getIntExtra("assicode",0);
         connectiondetector= new Connectiondetector(context);
        spinner_sername_spinner=findViewById(R.id.sername_spinner);
        if(assicode==1) {
            spinner_sername_spinner.setVisibility(View.GONE);
            str_sername=String.valueOf(getIntent().getIntExtra("service_id",0));
        }
        else
        {
            if(connectiondetector.isConnectingToInternet())
                fetchdata();
            else
                Toast.makeText(context, "No Internet", Toast.LENGTH_SHORT).show();

        }
        picktime=findViewById(R.id.picktime);
        textView=findViewById(R.id.msg);
        editText_edit=findViewById(R.id.edit);
        picktime.setText(new SimpleDateFormat("yyyy-MM-dd").format(new java.util.Date()));
        picktime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickdate_sdate();

            }
        });
        initViews();
        for (int i = 0; i <31 ; i++) {
            if(i==7)
                mEvents.add(new getdata("appo 1","08/07/2018","30"));
            else if(i==6)
                mEvents.add(new getdata("appo 2","07/07/2018","40"));
            else
                mEvents.add(new getdata("","",""));

        }
               }

    private void initViews()
    {
        calendarCustomView=findViewById(R.id.custom_calendar);
        recyclerView=findViewById(R.id.recycler_view);
        recyclerView_ba=findViewById(R.id.recycler_viewba);
        linearLayout=findViewById(R.id.reviewll);
        layoutManager= new LinearLayoutManager(context);
        layoutManager1= new LinearLayoutManager(context);
        tabLayout=findViewById(R.id.tabs);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if(tab.getPosition()==0&& recyclerView.getVisibility()==View.GONE)
                {

                    recyclerView.setVisibility(View.VISIBLE);
                    recyclerView_ba.setVisibility(View.GONE);
                    if(doctorEncounters_new!=null && doctorEncounters_new.getAppointmentdata().size()==0)
                        textView.setVisibility(View.VISIBLE);
                    else
                        textView.setVisibility(View.GONE);

                }
                else if(tab.getPosition()==1&& recyclerView_ba.getVisibility()==View.GONE)
                {
                    recyclerView_ba.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.GONE);

                    if(doctorEncounters_book!=null && doctorEncounters_book.getAppointmentdata().size()==0)
                        textView.setVisibility(View.VISIBLE);
                    else
                        textView.setVisibility(View.GONE);
                }

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
       // imageView=findViewById(R.id.calenderView);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView_ba.setLayoutManager(layoutManager1);
         toolbar = findViewById(R.id.toolbar);
         toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
       /*  toolbar.setTitleTextColor(Color.WHITE);
         toolbar.setTitle("Appointment");*/
         toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
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
    static class GridAdapter extends ArrayAdapter {

        private  final String TAG = GridAdapter.class.getSimpleName();
        private LayoutInflater mInflater;
        private List<Date> monthlyDates;
        private Calendar currentDate;
        Context context;
        // private List<getdata> allEvents;
        public GridAdapter(Context context, List<Date> monthlyDates, Calendar currentDate) {
            super(context, R.layout.single_cell_layout);
            this.monthlyDates = monthlyDates;
            this.currentDate = currentDate;
            this.context=context;
            mInflater = LayoutInflater.from(context);
        }
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // DateFormat formatter = new SimpleDateFormat("MMM dd HH:mm:ss z yyyy");
            Date mDate = monthlyDates.get(position);

            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
            SimpleDateFormat formatter7 = new SimpleDateFormat("MM");
            String format = formatter.format(mDate);
            String format1=formatter7.format(mDate);

            if(format1.startsWith("0"))
            {
                format1=format1.substring(1);
            }

            Calendar dateCal = Calendar.getInstance();
            dateCal.setTime(mDate);
            int dayValue = dateCal.get(Calendar.DAY_OF_MONTH);
            int displayMonth = dateCal.get(Calendar.MONTH) + 1;
            int displayYear = dateCal.get(Calendar.YEAR);
            int curretntdate= currentDate.get(Calendar.DATE);
            int currentMonth = currentDate.get(Calendar.MONTH) + 1;
            int currentYear = currentDate.get(Calendar.YEAR);

           /* if(mEvents.size()<monthlyDates.size() && Integer.parseInt(format1)==currentMonth)
            {


            }
*/

            View view = convertView;
            //   Toast.makeText(context, ""+dateCal, Toast.LENGTH_SHORT).show();
            if (view == null) {
                view = mInflater.inflate(R.layout.single_cell_layout, parent, false);
            }
            if (displayMonth == currentMonth && displayYear == currentYear) {
                view.setBackgroundColor(Color.parseColor("#FFEBEE"));
            } else {
                view.setBackgroundColor(Color.parseColor("#E1BEE7"));
            }
            TextView cellNumber = (TextView) view.findViewById(R.id.calendar_date_id);
            cellNumber.setText(String.valueOf(dayValue));
        TextView eventIndicator = (TextView) view.findViewById(R.id.event_id);
            if(position<mEvents.size()&& format.equals(mEvents.get(position).date))
            {
                view.setBackgroundColor(Color.RED);
                eventIndicator.setText("Event 1");
                eventIndicator.setTextColor(Color.WHITE);
            }

            Calendar eventCalendar = Calendar.getInstance();
            if(position==(getCount()-1))
            {
                //   Toast.makeText(context, "in if"+position, Toast.LENGTH_SHORT).show();
            }
            return view;
        }


        @Override
        public int getCount() {
            return monthlyDates.size();
        }
        @Nullable
        @Override
        public Object getItem(int position) {
            return monthlyDates.get(position);
        }
        @Override
        public int getPosition(Object item) {
            return monthlyDates.indexOf(item);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater= getMenuInflater();
        menuInflater.inflate(R.menu.menuapp,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.calender:
              /*  if(linearLayout.getVisibility()==View.VISIBLE) {
                    calendarCustomView.setVisibility(View.VISIBLE);
                    linearLayout.setVisibility(View.GONE);
                }
                else {
                    calendarCustomView.setVisibility(View.GONE);
                    linearLayout.setVisibility(View.VISIBLE);
                }*/
                break;
                default:
                    break;
        }
        return true;
    }

    void pickdate_sdate()
    {
        final Dialog dialog1 = new Dialog(context);
        dialog1.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog1.setContentView(R.layout.camgal);
        dialog1.setCanceledOnTouchOutside(false);
        final Calendar nextYear=Calendar.getInstance();
        nextYear.add(Calendar.YEAR,4);
        final  Calendar lastYear=Calendar.getInstance();
        lastYear.add(Calendar.YEAR,-1);
        btn_s=(Button)dialog1.findViewById(R.id.ok);
        calendar_s = (CalendarPickerView) dialog1.findViewById(R.id.calendar_view);
        calendar_s.init(lastYear.getTime(), nextYear.getTime()) //
                .inMode(CalendarPickerView.SelectionMode.SINGLE) //
                .withSelectedDate(new Date());
        calendar_s.setCustomDayView(new DefaultDayViewAdapter());
        Date today =new Date();
        Date date= new Date();
        today.setTime(-19800000);
        calendar_s.setDecorators(Collections.<CalendarCellDecorator>emptyList());
        calendar_s.init(today,nextYear.getTime())
                .withSelectedDate(date).inMode(CalendarPickerView.SelectionMode.SINGLE);
        btn_s.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayList<Date> mydates = (ArrayList<Date>)calendar_s.getSelectedDates();
                for (int i = 0; i <mydates.size() ; i++) {
                    Date tempdate = mydates.get(i);
                    String testdate = DATE_FORMAT.format(tempdate);
                    picktime.setText(testdate);
                    fetchBookedappointment(str_sername,picktime.getText().toString());
                    fetchnewappointment(str_sername,picktime.getText().toString());
                    dialog1.dismiss();

                }

            }
        });
        dialog1.show();

    }

    //////////////////////////////fetch new appointment////////////////////

    private void fetchnewappointment(String service_id,String scheduled_date)
    {
        OkHttpClient client = new OkHttpClient();
        Request validation_request = givenewappodata(service_id,scheduled_date);
        client.newCall(validation_request).enqueue(new Callback() {

            @Override
            public void onFailure(Request request, IOException e) {

                // Toast.makeText(context,"Fail",Toast.LENGTH_LONG).show();
                Log.i("Activity", "onFailure: Fail");
            }

            @Override
            public void onResponse(final Response response) throws IOException {
                final String body = response.body().string();
                listnewappresponse(body);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (doctorEncounters_new.getStatus()) {
                            if(doctorEncounters_new.getAppointmentdata().size()==0) {

                                textView.setVisibility(View.VISIBLE);
                                if(customAdapter_encounteres_ba!=null) {
                                    customAdapter_encounteres_ba.notifyDataSetChanged();
                                }
                            }
                            else {
                                customAdapter_encounteres = new CustomAdapter_encounteres(encountersmodels, doctorEncounters_new, context, 1,assicode);
                                recyclerView.setAdapter(customAdapter_encounteres);
                                textView.setVisibility(View.GONE);
                            }
                        }

                    }
                });
            }
        });

    }
    private Request givenewappodata(String service_id,String scheduled_date) {
        JSONObject postdata = new JSONObject();
        try {
            postdata.put("doctor_id", userid);
            postdata.put("service_id", service_id);
            postdata.put("scheduled_date", scheduled_date);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(JSON, postdata.toString());
        return new Request.Builder()
                .addHeader("X-Api-Key", "AB5433GMDF657VBB")
                .addHeader("Content-Type", "application/json")
                .url(ApiUtils.BASE_URL+"listnewappointment")
                .post(body)
                .build();
    }

    ////////////////////////////////////
    public void listnewappresponse(String response) {
        Gson gson = new GsonBuilder().create();
        doctorEncounters_new = gson.fromJson(response, DoctorEncounters.class);
    }

    //////////////////////////////fetch Booked appointment////////////////////

    private void fetchBookedappointment(String service_id,String appointment_date)
    {
        OkHttpClient client = new OkHttpClient();
        Request validation_request = givebookappodata(service_id,appointment_date);
        client.newCall(validation_request).enqueue(new Callback() {

            @Override
            public void onFailure(Request request, IOException e) {

                // Toast.makeText(context,"Fail",Toast.LENGTH_LONG).show();
                Log.i("Activity", "onFailure: Fail");
            }

            @Override
            public void onResponse(final Response response) throws IOException {
                final String body = response.body().string();
                listbookappresponse(body);
               runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(doctorEncounters_book.getStatus())
                        {
                            if(doctorEncounters_book.getAppointmentdata().size()==0) {
                                textView.setVisibility(View.VISIBLE);
                                if(customAdapter_encounteres_ba!=null) {
                                    customAdapter_encounteres_ba.notifyDataSetChanged();
                                }


                            }
                            else {
                                customAdapter_encounteres_ba = new CustomAdapter_encounteres(encountersmodels_ba, doctorEncounters_book, context, 0,assicode);
                                recyclerView_ba.setAdapter(customAdapter_encounteres_ba);
                                textView.setVisibility(View.GONE);
                            }
                        }
                        else {
                            Toast.makeText(context, "Something Went Wrong", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

    }
    private Request givebookappodata(String service_id,String appointment_date) {
        JSONObject postdata = new JSONObject();
        try {
            postdata.put("doctor_id",userid);
            postdata.put("service_id", service_id);
            postdata.put("appointed_timing", appointment_date);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(JSON, postdata.toString());
        return new Request.Builder()
                .addHeader("X-Api-Key", "AB5433GMDF657VBB")
                .addHeader("Content-Type", "application/json")
                .url(ApiUtils.BASE_URL+"listBookedAppointments")
                .post(body)
                .build();
    }

    ////////////////////////////////////

    public void listbookappresponse(String response) {
        Gson gson = new GsonBuilder().create();
        doctorEncounters_book = gson.fromJson(response, DoctorEncounters.class);
    }

    private void fetchdata()
    {
        OkHttpClient client = new OkHttpClient();
        Request validation_request = listservices();
        client.newCall(validation_request).enqueue(new Callback() {

            @Override
            public void onFailure(Request request, IOException e) {

                // Toast.makeText(getActivity(),"Fail",Toast.LENGTH_LONG).show();
                Log.i("Activity", "onFailure: Fail");
            }

            @Override
            public void onResponse(final Response response) throws IOException {
                // final String body=response.body().string();

                listServiceResponse(response.body().string());
                final boolean isSuccessful=serviceResponse.getStatus();
                l=serviceResponse.getData();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (isSuccessful)
                        {
                            customAdapter_bd= new CustomAdapter_bd(context,l);
                            str_sername=l.get(0).getService().getId();
                            spinner_sername_spinner.setAdapter(customAdapter_bd);
                            fetchBookedappointment(str_sername,picktime.getText().toString());
                            fetchnewappointment(str_sername,picktime.getText().toString());

                        } else {
                            Toast.makeText(context, "Could not load the list!!", Toast.LENGTH_SHORT).show();
                        }
                        spinner_sername_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                str_sername=l.get(position).getService().getId();
                                fetchBookedappointment(str_sername,picktime.getText().toString());
                                fetchnewappointment(str_sername,picktime.getText().toString());
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {


                            }
                        });
                    }
                });
            }
        });

    }

    private Request listservices() {
        JSONObject postdata = new JSONObject();
        try {
            postdata.put("user_id",userid);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(Addfamilyreport.JSON, postdata.toString());
        return new Request.Builder()
                .addHeader("X-Api-Key", "AB5433GMDF657VBB")
                .addHeader("Content-Type", "application/json")
                .url(ApiUtils.BASE_URL+"listService")
                .post(body)
                .build();
    }
    public void listServiceResponse(String response) {
        Gson gson = new GsonBuilder().create();
        serviceResponse = gson.fromJson(response, ListServicesContact.class);
    }

    class CustomAdapter_bd extends BaseAdapter {
        Context context;
        LayoutInflater inflter;
        List<Datum2> medicalconditionlists;
        public CustomAdapter_bd(Context applicationContext, List<Datum2> medicalconditionlists) {
            this.context = applicationContext;
            this.medicalconditionlists=medicalconditionlists;
            inflter = (LayoutInflater.from(applicationContext));
        }

        @Override
        public int getCount() {
            return medicalconditionlists.size();
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
            view = inflter.inflate(R.layout.spinner_itemwhite, null);
            TextView names = (TextView) view.findViewById(R.id.txt);
            names.setText(medicalconditionlists.get(i).getService().getTitle());
            return view;
        }


    }
}
