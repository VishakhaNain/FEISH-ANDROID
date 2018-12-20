package com.app.feish.application.Patient;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.app.feish.application.Adpter.BookedAppointmentadt;
import com.app.feish.application.Adpter.SmartFragmentStatePagerAdapter;
import com.app.feish.application.Connectiondetector;
import com.app.feish.application.R;
import com.app.feish.application.YourFragmentInterface;
import com.app.feish.application.model.bookedappointmentpatient;
import com.app.feish.application.modelclassforapi.Appodatum;
import com.app.feish.application.modelclassforapi.Appointmentdatum;
import com.app.feish.application.modelclassforapi.PatientBookedappointment;
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
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import static com.app.feish.application.Patient.PatientDashboard.JSON;

public class MyBookedappointment extends AppCompatActivity {
Toolbar toolbar;
Context context=this;
MyPagerAdapter adapterViewPager;
ViewPager vpPager;
TabLayout tabLayout;
SimpleDateFormat DATE_FORMAT;
TextView picktime;
CalendarPickerView calendar_s;
Button btn_s;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_bookedappointment);
        DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
        picktime=findViewById(R.id.picktime);
        picktime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickdate_sdate();

            }
        });
        picktime.setText(new SimpleDateFormat("yyyy-MM-dd").format(new java.util.Date()));

        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setTitle("Appointments");
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        vpPager =findViewById(R.id.vpPager);
        tabLayout =findViewById(R.id.tabLayout);
        adapterViewPager = new MyPagerAdapter(getSupportFragmentManager(),new SimpleDateFormat("yyyy-MM-dd").format(new java.util.Date()));
        //  vpPager.setOffscreenPageLimit(1);
        tabLayout.setupWithViewPager(vpPager);
        tabLayout.setSelectedTabIndicatorColor(ContextCompat.getColor(context,R.color.colorAccent));
        vpPager.setAdapter(adapterViewPager);
        vpPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                YourFragmentInterface fragment = (YourFragmentInterface) adapterViewPager.instantiateItem(vpPager, position);
                fragment.fragmentBecameVisible(picktime.getText().toString());
            }

            @Override
            public void onPageScrollStateChanged(int state) {

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

                    dialog1.dismiss();

                }

            }
        });



        dialog1.show();

    }

    class MyPagerAdapter extends SmartFragmentStatePagerAdapter
    {
        private  int NUM_ITEMS = 2;
        String date;

        private MyPagerAdapter(FragmentManager fragmentManager,String date) {
            super(fragmentManager);
            this.date=date;
        }

        // Returns total number of pages
        @Override
        public int getCount() {
            return NUM_ITEMS;
        }

        // Returns the fragment to display for that page
        @Override
        public Fragment getItem(int position) {
            if(position==0)
               return PatientnewappointmentsFragment.newInstance(position,date);
            else
                return   PatientbookedappointmentsFragment.newInstance(position,date);

        }

        // Returns the page title for the top indicator
        @Override
        public CharSequence getPageTitle(int position)
        {
            if(position==0) {
                return "New Appointment";
            }
            else if(position==1) {
                return "Booked Appointment";
            }
            return null;
        }

    }
    public static class PatientnewappointmentsFragment extends Fragment implements YourFragmentInterface {
        View rootView;
        Connectiondetector connectiondetector;
        RecyclerView recyclerView;
        LinearLayoutManager layoutManager;
        List<bookedappointmentpatient>bookedappointmentpatients = new ArrayList<>();
        BookedAppointmentadt bookedAppointmentadt;
        EditText editText;
        PatientBookedappointment patientBookedappointment;
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        public PatientnewappointmentsFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PatientnewappointmentsFragment newInstance(int sectionNumber,String date) {
            PatientnewappointmentsFragment fragment = new PatientnewappointmentsFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            args.putString("date", date);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            rootView = inflater.inflate(R.layout.fragment_listplan, container, false);
            return rootView;
        }

        @Override
        public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
            super.onViewCreated(view, savedInstanceState);
            connectiondetector= new Connectiondetector(getActivity());
               editText=rootView.findViewById(R.id.edit);
            recyclerView = rootView.findViewById(R.id.recycler_view);
            recyclerView.setHasFixedSize(true);

            layoutManager = new LinearLayoutManager(getActivity());
            recyclerView.setLayoutManager(layoutManager);
            // recyclerView.setAdapter(bookedAppointmentadt);
            if(connectiondetector.isConnectingToInternet())
                fetchingdata(getArguments().getString("date"));
            else
                Toast.makeText(getActivity(), "No Internet!!", Toast.LENGTH_SHORT).show();


        }
        private Request Patient_detail(String date) {
            JSONObject postdata = new JSONObject();
            try {
                postdata.put("user_id", Prefhelper.getInstance(getActivity()).getUserid());
                postdata.put("scheduled_date", date);

            } catch (JSONException e) {
                e.printStackTrace();
            }
            RequestBody body = RequestBody.create(JSON, postdata.toString());
            return new Request.Builder()
                    .addHeader("X-Api-Key", "AB5433GMDF657VBB")
                    .addHeader("Content-Type", "application/json")
                    .url("http://feish.online/apis/newappodetail")
                    .post(body)
                    .build();
        }
        public void DocprofileJSON(String response) {
            Gson gson = new GsonBuilder().create();
            patientBookedappointment = gson.fromJson(response,PatientBookedappointment.class);
        }

        private void fetchingdata(String date)
        {
            OkHttpClient client = new OkHttpClient();
            Request request = Patient_detail(date);
            Log.i("new app body", "onClick: "+request);
            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Request request, IOException e) {
                    Log.i("Activity", "onFailure: Fail");
                }
                @Override
                public void onResponse(final Response response) throws IOException {

                    final String body=response.body().string();

                    Log.i("new app body", "onResponse: "+body);
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                editText.setText(body);

                                JSONObject jsonObject= new JSONObject(body);
                                if(jsonObject.getBoolean("status")) {
                                    DocprofileJSON(body);
                                    if (patientBookedappointment.getStatus()) {
                                        bookedAppointmentadt = new BookedAppointmentadt(getArguments().getInt(ARG_SECTION_NUMBER), patientBookedappointment, getActivity());
                                        recyclerView.setAdapter(bookedAppointmentadt);
                                        bookedAppointmentadt.setFilter(patientBookedappointment);
                                    } else {
                                        Toast.makeText(getActivity(), "No Appointment on this date", Toast.LENGTH_SHORT).show();
                                    }
                                }
                                else
                                {
                                    Toast.makeText(getActivity(), "No Appointment on this date", Toast.LENGTH_SHORT).show();
                                }
                            }
                            catch (JSONException e)
                            {
                                Toast.makeText(getActivity(), ""+e, Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }

            });

        }

        @Override
        public void fragmentBecameVisible(String date) {
        /*    PatientBookedappointment patientBookedappointmentfilter=  filter(patientBookedappointment.getAppointmentdata(),patientBookedappointment.getAppodata(),date);
            bookedAppointmentadt.setFilter(patientBookedappointmentfilter);

*/
        fetchingdata(date);
        }
    }

    public static class PatientbookedappointmentsFragment extends Fragment implements YourFragmentInterface {
        View rootView;
        Connectiondetector connectiondetector;
        RecyclerView recyclerView;
        LinearLayoutManager layoutManager;
        List<bookedappointmentpatient>bookedappointmentpatients = new ArrayList<>();
        BookedAppointmentadt bookedAppointmentadt;
        EditText editText;
        PatientBookedappointment patientBookedappointment;
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        public PatientbookedappointmentsFragment() {
        }
        public static PatientbookedappointmentsFragment newInstance(int sectionNumber,String date) {
            PatientbookedappointmentsFragment fragment = new PatientbookedappointmentsFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            args.putString("date", date);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            rootView = inflater.inflate(R.layout.fragment_listplan, container, false);
            return rootView;
        }

        @Override
        public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
            super.onViewCreated(view, savedInstanceState);
            connectiondetector= new Connectiondetector(getActivity());
          editText=rootView.findViewById(R.id.edit);
            recyclerView = rootView.findViewById(R.id.recycler_view);
            recyclerView.setHasFixedSize(true);

            layoutManager = new LinearLayoutManager(getActivity());
            recyclerView.setLayoutManager(layoutManager);
           // recyclerView.setAdapter(bookedAppointmentadt);
            if(connectiondetector.isConnectingToInternet())
               fetchingdata(getArguments().getString("date"));
            else
                Toast.makeText(getActivity(), "No Internet!!", Toast.LENGTH_SHORT).show();


        }
        private Request Patient_detail(String date) {
            JSONObject postdata = new JSONObject();
            try {
                postdata.put("user_id", Prefhelper.getInstance(getActivity()).getUserid());
                postdata.put("scheduled_date", date);

            } catch (JSONException e) {
                e.printStackTrace();
            }
            RequestBody body = RequestBody.create(JSON, postdata.toString());
            return new Request.Builder()
                    .addHeader("X-Api-Key", "AB5433GMDF657VBB")
                    .addHeader("Content-Type", "application/json")
                    .url("http://feish.online/apis/bookedappodetail")
                    .post(body)
                    .build();
        }
        public void DocprofileJSON(String response) {
            Gson gson = new GsonBuilder().create();
           patientBookedappointment = gson.fromJson(response,PatientBookedappointment.class);
        }

        private void fetchingdata(String date)
        {
            OkHttpClient client = new OkHttpClient();
            Request request = Patient_detail(date);
            Log.i("bodybooked", "onClick: "+request);
            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Request request, IOException e) {
                    Log.i("Activity", "onFailure: Fail");
                }
                @Override
                public void onResponse(final Response response) throws IOException {

                    final String body=response.body().string();

                    Log.i("bodybooked", "onResponse: "+body);
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                editText.setText(body);
                                JSONObject jsonObject = new JSONObject(body);
                                if (jsonObject.getBoolean("status"))
                                {
                                    DocprofileJSON(body);
                                    if (patientBookedappointment.getStatus()) {
                                        bookedAppointmentadt = new BookedAppointmentadt(getArguments().getInt(ARG_SECTION_NUMBER), patientBookedappointment, getActivity());
                                        recyclerView.setAdapter(bookedAppointmentadt);
                                    } else {
                                        Toast.makeText(getActivity(), "No Appointment on this date", Toast.LENGTH_SHORT).show();
                                    }
                                }
                                else
                                {
                                    Toast.makeText(getActivity(), "Errror", Toast.LENGTH_SHORT).show();
                                }
                            }
                            catch (JSONException e)
                            {
                                Toast.makeText(getActivity(), ""+e, Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }

            });

        }

        @Override
        public void fragmentBecameVisible(String date) {
      /*    PatientBookedappointment patientBookedappointmentfilter=  filter(patientBookedappointment.getAppointmentdata(),patientBookedappointment.getAppodata(),date);
            bookedAppointmentadt.setFilter(patientBookedappointmentfilter);
*/
      fetchingdata(date);
        }
    }



}
