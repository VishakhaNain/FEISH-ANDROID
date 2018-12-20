package com.app.feish.application.doctor;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.app.feish.application.Connectiondetector;
import com.app.feish.application.DoctorCompleteProfile;
import com.app.feish.application.LoginActivity;
import com.app.feish.application.Patient.Remainder;
import com.app.feish.application.R;
import com.app.feish.application.Remote.ApiUtils;
import com.app.feish.application.TodayEncounters;
import com.app.feish.application.fragment.Addfamilyreport;
import com.app.feish.application.modelclassforapi.ContactService_getDetails;
import com.app.feish.application.modelclassforapi.Datum2;
import com.app.feish.application.modelclassforapi.ListServicesContact;
import com.app.feish.application.sessiondata.Prefhelper;
import com.athbk.slidingtablayout.SlidingTabAdapter;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;
import com.squareup.picasso.Picasso;

import net.frakbot.jumpingbeans.JumpingBeans;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class DoctorDashboard extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    ImageView imageView_profile;
    public static   ContactService_getDetails contactService_getDetails;
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    SimpleDateFormat DATE_FORMAT;
    Context context=this;
    SectionsPagerAdapter mSectionsPagerAdapter;
    View  v;
    int npmsg=0,noapp=0;
    ArrayList<String> Specialtylist= new ArrayList<>();
    Connectiondetector connectiondetector;
    TextView pdra_name,pdra_email,tv_lefapp;
    CircleImageView circleImageView_imageView_pp;
    ViewPager  mViewPager;
    CircleImageView circleImageView;
    TextView textView_drname,textView_drspe;
    CustomAdapter_bd customAdapter_bd;
    TextView textView_totalappo,getTextView_totalmsg;
    com.athbk.slidingtablayout.TabLayout tabLayout;
    List<Datum2> l;
    private ListServicesContact serviceResponse;
    Spinner spinner_sername_spinner;
    String str_sername="";
    private static final String TAG = DoctorDashboard.class.getName();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_dr);
        tv_lefapp=findViewById(R.id.leftapp);

        DATE_FORMAT = new SimpleDateFormat("dd/MM/yyyy");
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        textView_totalappo=findViewById(R.id.totalappo);
        getTextView_totalmsg=findViewById(R.id.totalmsg);
        spinner_sername_spinner=findViewById(R.id.spinner);
        mSectionsPagerAdapter= new SectionsPagerAdapter(getSupportFragmentManager());
        mViewPager =findViewById(R.id.container);
connectiondetector= new Connectiondetector(context);
        textView_drname=findViewById(R.id.drname);
        textView_drspe=findViewById(R.id.drspe);
        circleImageView=findViewById(R.id.profilepic);
         tabLayout =findViewById(R.id.tabLayout);
        tabLayout.setViewPager(mViewPager,mSectionsPagerAdapter);
        imageView_profile=findViewById(R.id.profile_update);
        bottomnavigaion();
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();


        NavigationView navigationView =findViewById(R.id.nav_view);
        if (navigationView != null) {
            navigationView.setNavigationItemSelectedListener(this);
            v=navigationView.getHeaderView(0);
        }
        pdra_name=v.findViewById(R.id.pnamed);
        pdra_email=v.findViewById(R.id.pemaild);
        circleImageView_imageView_pp=v.findViewById(R.id.imageView_pp);
        tv_lefapp=findViewById(R.id.leftapp);
        tv_lefapp.setText(" ");
                 JumpingBeans.with(tv_lefapp)
                .makeTextJump(0, tv_lefapp.getText().toString().indexOf(' '))
                .setIsWave(false)
                .setLoopDuration(1000)  // ms
                .build();
imageView_profile.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        startActivity(new Intent(DoctorDashboard.this,ViewProfileDoctor.class));
    }
});
if(connectiondetector.isConnectingToInternet()) {
    fetchdata();
    fetchservicelist();
    updatetkenid();

}
else
{
    Toast.makeText(context, "No Internet!!", Toast.LENGTH_SHORT).show();
}

    }
    public class SectionsPagerAdapter extends SlidingTabAdapter {

        @Override
        protected String getTitle(int position) {
            if(position==0) {
                return " Appoinment";
            }
            else if(position==1) {
                return " Message";
            }
            else
            {
                return "";
            }

        }

        @Override
        protected int getIcon(int position) {
            if(position==0) {
                return R.drawable.tab_2_selected;
            }
            else if(position==1) {
                return R.drawable.tab_1_selected;
            }
            else
            {
                return 0;
            }
        }

        private SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            if(position==0)
                return Encounterfrag.newInstance(str_sername,"");
            else
                return Messagefreag.newInstance("","");

        }



        @Override
        public int getCount() {
            // Show 3 total pages.
            return 2;
        }
    }
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }


    }
  /*  void pickdate_sdate()
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
        calendar_s =dialog1.findViewById(R.id.calendar_view);
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

   *//* public void OpenActivityAssistant(View view)
    {
        startActivity(new Intent(DoctorDashboard.this,AssistantDoc.class));
    }*/

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.nav_prescription) {
                     startActivity(new Intent(DoctorDashboard.this,Precreption.class));
            // Handle the camera action
        } if (id == R.id.nav_setting) {
                     startActivity(new Intent(DoctorDashboard.this,DoctorCompleteProfile.class));
            // Handle the camera action
        }if (id == R.id.nav_report) {
                     startActivity(new Intent(DoctorDashboard.this,ReportActivity.class));
            // Handle the camera action
        }
        if (id == R.id.nav_pharmacy) {
            startActivity(new Intent(DoctorDashboard.this,Pharmacy.class));
            // Handle the camera action
        }
         /*if (id == R.id.nav_assispart) {
            startActivity(new Intent(DoctorDashboard.this,AssistantDashbord.class));
            // Handle the camera action
        }*/

        if (id == R.id.nav_Dashboardassis) {
            startActivity(new Intent(DoctorDashboard.this,AssistantDoc.class));
            // Handle the camera action
        } else if (id == R.id.nav_Drpatient) {
            // Handle the camera action
                Intent intent = new Intent(DoctorDashboard.this, DocAddPlan.class);
                intent.putExtra("user_id",Prefhelper.getInstance(context).getUserid());
                startActivity(intent);

        }else if (id == R.id.nav_Drservice) {
            startActivity(new Intent(DoctorDashboard.this,ServiceDoc.class));
            // Handle the camera action
        } else if (id == R.id.nav_remaindernotes) {
            // Handle the camera action
            Intent intent=new Intent(DoctorDashboard.this,Remainder.class);
            intent.putExtra("drnote",1);
            startActivity(intent);
        } else if (id == R.id.nav_plan) {
            startActivity(new Intent(DoctorDashboard.this, CreatePlans.class));

        } else if (id == R.id.nav_checkmsg) {
            Intent intent=new Intent(DoctorDashboard.this,DoctorMessage.class);
            intent.putExtra("user_id",Prefhelper.getInstance(context).getUserid());
            startActivity(intent);

        } else if (id == R.id.nav_bookedappointment) {
            if(contactService_getDetails==null)
            {
                Toast.makeText(context, "Data not loaded", Toast.LENGTH_SHORT).show();
            }
            else {
                Intent intent = new Intent(DoctorDashboard.this, Encounters.class);

intent.putExtra("user_id",contactService_getDetails.getData().getUserId());
                startActivity(intent);
            }

        } else if (id == R.id.nav_reviewrating) {
            startActivity(new Intent(DoctorDashboard.this,Drreviewrating.class));

        }else if (id == R.id.nav_packagedetail) {
            if(contactService_getDetails!=null) {
                startActivity(new Intent(DoctorDashboard.this, DoctorPackageDetail.class));
            }
            else
            {
                Toast.makeText(context, "Data is not loaded", Toast.LENGTH_SHORT).show();
            }

        } else if (id == R.id.nav_logout) {

        }AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(context, android.R.style.Theme_Material_Dialog_Alert);
        } else {
            builder = new AlertDialog.Builder(context);
        }
        builder.setTitle("Delete entry")
                .setMessage("Are you sure you want to logout?")
                .setPositiveButton("yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // continue with delete
                        startActivity(new Intent(DoctorDashboard.this,LoginActivity.class));
                        Prefhelper.getInstance(DoctorDashboard.this).setLoggedIn(false);
                        finish();
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // do nothing
                        dialog.dismiss();
                    }
                })
                .show();


        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void fetchSpecialtylist(final String pos)
    {
        OkHttpClient client = new OkHttpClient();
        Request request = occupationrequest();

        Log.i("", "onClick: "+request);
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                Log.i("Activity", "onFailure: Fail");
            }
            @Override
            public void onResponse(final Response response) throws IOException {

                final String body=response.body().string();
                // medicalconditionJSON(body);
                Log.i("1234add", "onResponse: "+body);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONObject  jsonObject = new JSONObject(body);
                            JSONArray jsonArray= jsonObject.getJSONArray("Specialty");
                            for (int i = 0; i <jsonArray.length() ; i++)
                            {
                                JSONObject jsonObject1= jsonArray.getJSONObject(i);
                                Specialtylist.add(jsonObject1.getString("specialty_name"));
                            }
                            if(!pos.equals("0"))
                                textView_drspe.setText(Specialtylist.get(Integer.parseInt(pos)-1));

                        }


                        catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(context, ""+e, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }

        });

    }

    private Request occupationrequest()
    {
        JSONObject postdata = new JSONObject();
        RequestBody body = RequestBody.create(JSON, postdata.toString());
        return new Request.Builder()
                .addHeader("X-Api-Key", "AB5433GMDF657VBB")
                .addHeader("Content-Type", "application/json")
                .url("http://feish.online/apis/listspeciality")
                .post(body)
                .build();
    }

    private void bottomnavigaion()
    {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
        }
         ArrayList<AHBottomNavigationItem> bottomNavigationItems = new ArrayList<>();
        AHBottomNavigation bottomNavigation =findViewById(R.id.bottom_navigation);

        AHBottomNavigationItem item1 = new AHBottomNavigationItem("Patient", R.drawable.pateint);
        AHBottomNavigationItem item2 = new AHBottomNavigationItem("Report", R.drawable.diet_plan);
        AHBottomNavigationItem item3 = new AHBottomNavigationItem("Profile", R.drawable.healthprofile);

        bottomNavigationItems.add(item1);
        bottomNavigationItems.add(item2);
        bottomNavigationItems.add(item3);

        bottomNavigation.addItems(bottomNavigationItems);
        bottomNavigation.setCurrentItem(1);
        bottomNavigation.setInactiveColor(ContextCompat.getColor(context,R.color.colorAccent5));
        bottomNavigation.setAccentColor(ContextCompat.getColor(context,R.color.green));
       // bottomNavigation.setColored(true);
        //bottomNavigation.setBackgroundColor(ContextCompat.getColor(context,R.color.tcolorAccent3));
        bottomNavigation.setCurrentItem(1);

// Set listeners
        bottomNavigation.setOnTabSelectedListener(new AHBottomNavigation.OnTabSelectedListener() {
            @Override
            public boolean onTabSelected(int position, boolean wasSelected) {
                // Do something cool here...
                if (position == 0) {
                    startActivity(new Intent(DoctorDashboard.this,DocAddPlan.class));

                }
                else if(position==1)
                {
                    startActivity(new Intent(DoctorDashboard.this,ReportActivity.class));

                }
                else if(position==2)
                {
                    startActivity(new Intent(DoctorDashboard.this,DoctorCompleteProfile.class));

                }
                return true;
            }
        });
        bottomNavigation.setOnNavigationPositionListener(new AHBottomNavigation.OnNavigationPositionListener() {
            @Override public void onPositionChange(int y) {
                // Manage the new y position
            }
        });
    }
    public void drprofileJSON(String response) {
        Gson gson = new GsonBuilder().create();
        contactService_getDetails = gson.fromJson(response, ContactService_getDetails.class);
    }
    private void fetchdata()
    {
        OkHttpClient client = new OkHttpClient();
        Request request = drprofile_request();
        Log.i("", "onClick: "+request);
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                Log.i("Activity", "onFailure: Fail");
            }
            @Override
            public void onResponse(final Response response) throws IOException {

                String body=response.body().string();
                Log.i("", "onResponse: "+body);
                drprofileJSON(body);
                final String message = contactService_getDetails.getMessage();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //pb.setVisibility(View.GONE);
                        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

                        if (message.compareTo("success") == 0)
                        {
                            String name=contactService_getDetails.getData().getFirstName()+" "+contactService_getDetails.getData().getLastName();
                            //   textView_drname.setText("04 July 2018");
                            //   if(contactService_getDetails.getData().getAddress()!=null)
                            // textView_nameloc.setText("Dr. "+contactService_getDetails.getData().getFirstName()+" "+contactService_getDetails.getData().getAddress().toString());
                            //else
                            //  textView_nameloc.setText("Dr. "+contactService_getDetails.getData().getFirstName()+" ");
                            pdra_email.setText(contactService_getDetails.getData().getIs_active().toString());
                            pdra_name.setText(name);
                            textView_drname.setText( "Hello "+name);
                            //     textView_drspe.setText(contactService_getDetails.getData().getDr_specilization().toString());
                            if(contactService_getDetails.getData().getDr_specilization()!=null)
                                fetchSpecialtylist(contactService_getDetails.getData().getDr_specilization().toString());
                            if(contactService_getDetails.getData().getAvatar()!=null)
                            {
                                Picasso.with(DoctorDashboard.this)
                                        .load(contactService_getDetails.getData().getAvatar().toString())
                                        .into(circleImageView);
                                Picasso.with(DoctorDashboard.this)
                                        .load(contactService_getDetails.getData().getAvatar().toString())
                                        .into(circleImageView_imageView_pp);
                            }

                        }else {
                            Toast.makeText(getApplicationContext(), "Fail", Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }

        });

    }
    private Request drprofile_request() {
        JSONObject postdata = new JSONObject();
        try {
            postdata.put("user_id", Prefhelper.getInstance(DoctorDashboard.this).getUserid());
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
    private void fetchservicelist()
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

               // Log.d( "onResponse: ",response.toString());

                listServiceResponse(response.body().string());
                final boolean isSuccessful=serviceResponse.getStatus();
                l=serviceResponse.getData();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (isSuccessful) {
                            customAdapter_bd= new CustomAdapter_bd(context,l);
                            try {
                                str_sername = l.get(0).getService().getId();
                            }catch (NullPointerException npe)
                            {
                                str_sername="0";
                            }
                            mViewPager.setAdapter(mSectionsPagerAdapter);
                            spinner_sername_spinner.setAdapter(customAdapter_bd);
                            fetchdatafordash(str_sername);
                        } else
                            {
                            Toast.makeText(context, "Could not load the list!!", Toast.LENGTH_SHORT).show();
                        }
                        spinner_sername_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                try {


                                    str_sername = l.get(position).getService().getId();
                                }catch (NullPointerException npe)
                                {
                                    str_sername="0";
                                }
                                TodayEncounters fragment = (TodayEncounters) mSectionsPagerAdapter.instantiateItem(mViewPager, 0);
                                fragment.Encountersfetch(str_sername);
                                fetchdatafordash(str_sername);
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
            postdata.put("user_id",Prefhelper.getInstance(context).getUserid());
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
        JsonObject json =    gson.fromJson(response, JsonObject.class);
        if(json.get("data") instanceof JsonArray)
        {
            serviceResponse = gson.fromJson(response, ListServicesContact.class);
        }
        else
        {
            JsonObject data = (JsonObject) json.get("data");
            JsonArray arrayData = new JsonArray();
            arrayData.add(data);
            json.add("data", arrayData);
            serviceResponse = gson.fromJson(json, ListServicesContact.class);

        }

    }

    private void updatetkenid()
    {
        OkHttpClient client = new OkHttpClient();
        Request validation_request = update();
        client.newCall(validation_request).enqueue(new Callback() {

            @Override
            public void onFailure(Request request, IOException e) {

                // Toast.makeText(getActivity(),"Fail",Toast.LENGTH_LONG).show();
                Log.i("Activity", "onFailure: Fail");
            }

            @Override
            public void onResponse(final Response response) throws IOException {
                 final String body=response.body().string();

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                    }
                });
            }
        });

    }

    private Request update() {
        JSONObject postdata = new JSONObject();
        try {
            postdata.put("user_id",Prefhelper.getInstance(context).getUserid());
            postdata.put("token", FirebaseInstanceId.getInstance().getToken());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(Addfamilyreport.JSON, postdata.toString());
        return new Request.Builder()
                .addHeader("X-Api-Key", "AB5433GMDF657VBB")
                .addHeader("Content-Type", "application/json")
                .url(ApiUtils.BASE_URL+"updateUserTokenIDfornotification")
                .post(body)
                .build();
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
            TextView names =  view.findViewById(R.id.txt);
//            String title = (medicalconditionlists.get(i).getService().getTitle() == null)? "Null" : medicalconditionlists.get(i).getService().getTitle();
//            Log.d(TAG, "title: ");
            try {
                names.setText(medicalconditionlists.get(i).getService().getTitle());
            }catch (NullPointerException npe)
            {
                names.setText("N/A");
            }
            return view;
        }


    }
    private void fetchdatafordash(String serid)
    {
        OkHttpClient client = new OkHttpClient();
        Request request = dashboarddetail(serid);

        Log.i("", "onClick: "+request);
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                Log.i("Activity", "onFailure: Fail");
            }
            @Override
            public void onResponse(final Response response) throws IOException {

                final String body=response.body().string();
                // medicalconditionJSON(body);
                Log.i("1234add", "onResponse: "+body);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try
                        {
                            JSONObject jsonObject= new JSONObject(body);
                            tv_lefapp.setText(jsonObject.getString("leftappointment"));
                            noapp=Integer.parseInt(jsonObject.getString("leftappointment"));
                            npmsg=Integer.parseInt(jsonObject.getString("NoofMessage"));
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

    private Request dashboarddetail(String serviceid)
    {
        JSONObject postdata = new JSONObject();
        try {
            postdata.put("appointed_timing",new SimpleDateFormat("yyyy-MM-dd").format(new java.util.Date()));
            postdata.put("service_id",serviceid);
            postdata.put("doctor_id",Prefhelper.getInstance(context).getUserid());
        }
        catch (JSONException e)
        {
            Toast.makeText(context, ""+e, Toast.LENGTH_SHORT).show();
        }
        RequestBody body = RequestBody.create(JSON, postdata.toString());
        return new Request.Builder()
                .addHeader("X-Api-Key", "AB5433GMDF657VBB")
                .addHeader("Content-Type", "application/json")
                .url("http://feish.online/apis/Doctordashboarddetail")
                .post(body)
                .build();
    }

}
