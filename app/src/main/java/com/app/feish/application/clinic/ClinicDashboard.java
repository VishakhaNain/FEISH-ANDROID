package com.app.feish.application.clinic;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.app.feish.application.Assistant.AssistantDashbord;
import com.app.feish.application.Connectiondetector;
import com.app.feish.application.LoginActivity;
import com.app.feish.application.R;
import com.app.feish.application.Remote.ApiUtils;
import com.app.feish.application.doctor.DoctorDashboard;
import com.app.feish.application.doctor.ServiceDetailActivity;
import com.app.feish.application.doctor.ServiceDoc;
import com.app.feish.application.doctor.ViewProfileDoctor;
import com.app.feish.application.modelclassforapi.ContactLogin;
import com.app.feish.application.modelclassforapi.ContactService_getDetails;
import com.app.feish.application.sessiondata.Prefhelper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonReader;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import static com.app.feish.application.fragment.Addfamilyreport.JSON;

public class ClinicDashboard extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    TextView clinicname,totaldoctor;
    Context context = this;
    ClinicDashboardDetails clinicDashboardDetails;
Intent intent;
    ListView listview;
    Connectiondetector connectiondetector;
    String clinic_id;
    String tokens;

    private int[] Image = {R.drawable.doctor, R.drawable.doctor, R.drawable.doctor};

   /* private String[] Title = {"Doctor'Name", "Doctor'Name", "Doctor'Name"};

    private String[] Desc = {"about doctor", "about doctor ", "about doctor"};

    private String[] Date = {"Therepist", "Specialist", " Other"};*/

    private ArrayList<DoctorSummary> doctorSummaries;

    private ListbaseAdapter listbaseAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main6);

        clinicname = findViewById(R.id.clinicname);
        totaldoctor=findViewById(R.id.totaldoctor);
        listview = (ListView) findViewById(R.id.listview);
        clinic_id = Prefhelper.getInstance(ClinicDashboard.this).getClinic_id();
        tokens = Prefhelper.getInstance(ClinicDashboard.this).getToken();
        fetchdata();

      /*  List<DoctorList> doctorLists = clinicDashboardDetails.getDoctorList();
        doctorSummaries = new ArrayList<DoctorSummary>();

        for (int i = 0; i < doctorLists.size(); i++) {

        }
        listbaseAdapter = new ListbaseAdapter(ClinicDashboard.this, doctorSummaries);

        listview.setAdapter(listbaseAdapter);*/

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("DashBoard");


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main6, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long

        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.entry) {


            Intent intent = new Intent(ClinicDashboard.this, DoctorsEntry.class);
            startActivity(intent);


            // Handle the camera action
        } else if (id == R.id.profile) {

            Intent intent = new Intent(ClinicDashboard.this, ClinicProfileUpdate.class);
            startActivity(intent);

        }else if (id == R.id.nav_Drservice) {
            startActivity(new Intent(ClinicDashboard.this,ServiceDoc.class));
        } else if (id == R.id.logout) {

            AlertDialog.Builder builder;
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
                            startActivity(new Intent(ClinicDashboard.this, LoginActivity.class));
                            Prefhelper.getInstance(ClinicDashboard.this).setLoggedIn(false);
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


        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    private void fetchdata() {
        OkHttpClient client = new OkHttpClient();
        Request request = clinicprofile_request();
        //Log.i(TAG, "onClick: " + request);
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                Log.i("Activity", "onFailure: Fail");
            }

            @Override
            public void onResponse(final Response response) throws IOException {

                final String body = response.body().string();
                //Log.i(TAG, "onResponse: " + body);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Gson gson = new GsonBuilder().create();
                            JsonReader reader = gson.newJsonReader(new StringReader(body));
                            reader.setLenient(true);
                            JsonObject jsonObject = gson.fromJson(reader, JsonObject.class);
                            clinicDashboardDetails = gson.fromJson(jsonObject.get("clinicDetails").getAsJsonObject(), ClinicDashboardDetails.class);
                            clinicname.setText(clinicDashboardDetails.getClinicName());
                            totaldoctor.setText(Integer.toString(clinicDashboardDetails.getTotalDoc()));


                            List<DoctorList> doctorLists = clinicDashboardDetails.getDoctorList();
                            Integer innn = doctorLists.size();
                            doctorSummaries = new ArrayList<DoctorSummary>();

                            for (int i = 0; i < doctorLists.size(); i++) {

                                    DoctorList dl = doctorLists.get(i);
                                    DoctorSummary ds= new DoctorSummary(dl.getFirstName()+dl.getLastName(),"about", dl.getSpeciality());
                                    doctorSummaries.add(ds);
                                }
                            listbaseAdapter = new ListbaseAdapter(ClinicDashboard.this, doctorSummaries);

                            listview.setAdapter(listbaseAdapter);

                          /*  listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                public void onItemClick(AdapterView<?> listView, View itemView, int itemPosition, long itemId)
                                {
                                    Intent intent=new Intent(ClinicDashboard.this,ServiceDetailActivity.class);
                                    startActivity(intent);
                                }
                            });*/
                            }
                        catch (Exception e) {
                            e.printStackTrace();
                            Toast.makeText(context, ""+e, Toast.LENGTH_SHORT).show();

                        }
                    }
                });
            }
            });
        }
    private Request clinicprofile_request() {
        final Request request = new Request.Builder()
                .addHeader("X-Api-Key", tokens)
                .addHeader("Content-Type", "application/json")
               .url("http://feish.online/MyApi/clinicDashboard/"+clinic_id)
                //.url("http://10.0.2.2:8089/apis/clinic/"+getIntent().getStringExtra("clinic_id"))
                .get()
                .build();
        return request;
    }
}

















