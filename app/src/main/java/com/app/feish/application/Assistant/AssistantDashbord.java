package com.app.feish.application.Assistant;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;
import com.app.feish.application.Connectiondetector;
import com.app.feish.application.LoginActivity;
import com.app.feish.application.R;
import com.app.feish.application.Remote.ApiUtils;
import com.app.feish.application.doctor.DocAddPlan;
import com.app.feish.application.doctor.DoctorMessage;
import com.app.feish.application.doctor.Encounters;
import com.app.feish.application.fragment.Addfamilyreport;
import com.app.feish.application.modelclassforapi.ContactService_getDetails;
import com.app.feish.application.sessiondata.Prefhelper;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.text.SimpleDateFormat;

import static com.app.feish.application.fragment.Addfamilyreport.JSON;
public class AssistantDashbord extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    Toolbar toolbar;
    TextView textView_appono,textView_nomsg;
Context context=this;
Connectiondetector  connectiondetector;
    ContactService_getDetails contactService_getDetails;
    TextView   servicename,assistentname,txt_patiententry,txt_patientrecord,txt_appointment,txt_message;
    int service_id=0;
    public static String docfee;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assistantdashboard);
         toolbar = findViewById(R.id.toolbar);
         toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
         toolbar.setTitleTextColor(Color.WHITE);
         toolbar.setTitle("Assistant Dashboard");

        setSupportActionBar(toolbar);
        assistentname=findViewById(R.id.assistentname);
        servicename=findViewById(R.id.servicename);
        textView_appono=findViewById(R.id.appono);
        textView_nomsg=findViewById(R.id.nomsg);
        txt_patiententry=findViewById(R.id.txt_newpatient);
        txt_patientrecord=findViewById(R.id.txt_viewpatient);
        txt_appointment=findViewById(R.id.txt_todayappointment);
        txt_message=findViewById(R.id.txt_patientmsg);
connectiondetector= new Connectiondetector(getApplicationContext());

if(connectiondetector.isConnectingToInternet())
{
 retrofit();
    updatetkenid();
}
else
    {
        Toast.makeText(context, "No Internet!!", Toast.LENGTH_SHORT).show();
    }
        txt_patiententry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(contactService_getDetails==null)
                {
                    Toast.makeText(context, "Data not loaded", Toast.LENGTH_SHORT).show();
                }
                else {
                    Intent intent = new Intent(AssistantDashbord.this, PatientEntry.class);
                    intent.putExtra("fee", contactService_getDetails.getData().getFee().toString());
                    intent.putExtra("service_id",String.valueOf(service_id));
                    intent.putExtra("doctor_id",contactService_getDetails.getData().getUserId());
                    startActivity(intent);
                }
            }
        });
        txt_patientrecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(contactService_getDetails==null)
                {
                    Toast.makeText(context, "Data not loaded", Toast.LENGTH_SHORT).show();
                }
                else {
                    Intent intent = new Intent(AssistantDashbord.this, DocAddPlan.class);

                    intent.putExtra("user_id",contactService_getDetails.getData().getUserId());
                    startActivity(intent);
                }
            }
        });
        txt_appointment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(contactService_getDetails==null)
                {
                    Toast.makeText(context, "Data not loaded", Toast.LENGTH_SHORT).show();
                }
                else {
                    docfee=contactService_getDetails.getData().getFee().toString();
                    Intent intent = new Intent(AssistantDashbord.this, Encounters.class);

                    intent.putExtra("user_id",contactService_getDetails.getData().getUserId());
                    intent.putExtra("service_id",service_id);
                    intent.putExtra("assicode",1);
                    startActivity(intent);
                }

            }
        });
        txt_message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(contactService_getDetails==null)
                {
                    Toast.makeText(context, "Data not loaded", Toast.LENGTH_SHORT).show();
                }
                else {
                    Intent intent = new Intent(AssistantDashbord.this, DoctorMessage.class);
                    intent.putExtra("user_id",contactService_getDetails.getData().getUserId());
                    startActivity(intent);
                }
            }
        });
      /*  linearLayout=findViewById(R.id.header);
        ArcShape shape = new ArcShape(200, linearLayout.getWidth());
        ShapeDrawable shapeDrawable = new ShapeDrawable(shape);
        shapeDrawable.getPaint().setColor(ContextCompat.getColor(context,R.color.colorAccent3));
        linearLayout.setBackgroundDrawable(shapeDrawable);*/
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        DrawerLayout drawer =findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
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
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_checkmsg) {
            if(contactService_getDetails==null)
            {
                Toast.makeText(context, "Data not loaded", Toast.LENGTH_SHORT).show();
            }
            else {
                Intent intent = new Intent(AssistantDashbord.this, DoctorMessage.class);
                intent.putExtra("user_id",contactService_getDetails.getData().getUserId());
                startActivity(intent);
            }

        } else if (id == R.id.nav_bookedappointment) {
            if(contactService_getDetails==null)
            {
                Toast.makeText(context, "Data not loaded", Toast.LENGTH_SHORT).show();
            }
            else {
                Intent intent = new Intent(AssistantDashbord.this, Encounters.class);

                intent.putExtra("user_id",contactService_getDetails.getData().getUserId());
                startActivity(intent);
            }


        }else if(id==R.id.nav_patiententry)
        {
            if(contactService_getDetails==null)
            {
                Toast.makeText(context, "Data not loaded", Toast.LENGTH_SHORT).show();
            }
            else {
                Intent intent = new Intent(AssistantDashbord.this, PatientEntry.class);
                intent.putExtra("fee", contactService_getDetails.getData().getFee().toString());
                intent.putExtra("service_id",service_id);
                intent.putExtra("doctor_id",contactService_getDetails.getData().getUserId());
                startActivity(intent);
            }
        }
       else if (id == R.id.nav_Drpatient) {
            // Handle the camera action
            if(contactService_getDetails==null)
            {
                Toast.makeText(context, "Data not loaded", Toast.LENGTH_SHORT).show();
            }
            else {
                Intent intent = new Intent(AssistantDashbord.this, DocAddPlan.class);

                intent.putExtra("user_id",contactService_getDetails.getData().getUserId());
                startActivity(intent);
            }
        }
        else if(id==R.id.createencounters)
        {
            if(contactService_getDetails==null)
            {
                Toast.makeText(context, "Data not loaded", Toast.LENGTH_SHORT).show();
            }
            else {
                Intent intent = new Intent(AssistantDashbord.this, CreateEncounters.class);

                intent.putExtra("user_id",contactService_getDetails.getData().getUserId());
                intent.putExtra("service_id",service_id);
                startActivity(intent);
            }
        }
        else if(id==R.id.nav_setting)
        {
            startActivity(new Intent(AssistantDashbord.this,AssistentProfileView.class));
        }
        else if(id==R.id.nav_logout)
        {
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
                            startActivity(new Intent(AssistantDashbord.this,LoginActivity.class));
                            Prefhelper.getInstance(AssistantDashbord.this).setLoggedIn(false);
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


        DrawerLayout drawer =findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    private void retrofit() {
       // Toast.makeText(context, ""+ Prefhelper.getInstance(context).getUserid(), Toast.LENGTH_SHORT).show();
        OkHttpClient client = new OkHttpClient();
        Request validation_request = listservices();
        client.newCall(validation_request).enqueue(new Callback() {

            @Override
            public void onFailure(Request request, IOException e) {

                //Toast.makeText(context,"Fail",Toast.LENGTH_LONG).show();
                Log.i("Activity", "onFailure: Fail");
            }

            @Override
            public void onResponse(final Response response) throws IOException {
                final String body = response.body().string();

                runOnUiThread(new Runnable() {
                                  @Override
                                  public void run()
                                  {
                                      // Toast.makeText(context, "" + body, Toast.LENGTH_SHORT).show();

                                      try {

                                          JSONObject jsonObject = new JSONObject(body);

                                          int success = jsonObject.getInt("Success");

                                          if (success == 1) {
                                              JSONArray jsonArray=jsonObject.getJSONArray("value");
                                              JSONObject jsonObject1=jsonArray.getJSONObject(0);
                                              String servicenamee = jsonObject1.getString("title");

                                              JSONArray jsonArray1 = jsonObject.getJSONArray("usersdata");
                                              JSONObject jsonObject2=jsonArray1.optJSONObject(0);
                                              String fnamee = jsonObject2.getString("first_name");
                                              String lnamee = jsonObject2.getString("last_name");
                                              fetchdata(jsonObject.getInt("doctor_id"),jsonObject.getInt("service_id"));
                                              service_id=jsonObject.getInt("service_id");
                                              assistentname.setText(fnamee + "" + lnamee);
                                              servicename.setText(servicenamee);


                                          } else
                                              Toast.makeText(AssistantDashbord.this, "" + jsonObject.getString("Message"), Toast.LENGTH_SHORT).show();

                                      } catch (JSONException e) {
                                          Toast.makeText(AssistantDashbord.this, "" + e, Toast.LENGTH_SHORT).show();
                                      }

                                  }

                              }
                );
            }
        });
    }

    private Request listservices() {
        JSONObject postdata = new JSONObject();
        try{
            postdata.put("id",Prefhelper.getInstance(context).getUserid());
        }
        catch (JSONException e)
        {
            Toast.makeText(context, ""+e, Toast.LENGTH_SHORT).show();
        }

        RequestBody body = RequestBody.create(JSON, postdata.toString());
        return new Request.Builder()
                .addHeader("X-Api-Key", "AB5433GMDF657VBB")
                .addHeader("Content-Type", "application/json")
                .url(ApiUtils.BASE_URL+"fetchassistantprofiledashboard")
                .post(body)
                .build();
    }
    public void listServiceResponse(String response) {
        Gson gson = new GsonBuilder().create();

    }
    public void drprofileJSON(String response) {
        Gson gson = new GsonBuilder().create();
        contactService_getDetails = gson.fromJson(response, ContactService_getDetails.class);
    }
    private void fetchdata(int id, final int service_id)
    {
        OkHttpClient client = new OkHttpClient();
        Request request = drprofile_request(id);
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
                        fetchdatafordash(String.valueOf(service_id));

                    }
                });
            }

        });

    }
    private Request drprofile_request(int id) {
        JSONObject postdata = new JSONObject();
        try {
            postdata.put("user_id",id);
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
                            textView_appono.setText(jsonObject.getString("No_of_appointment"));
                            textView_nomsg.setText(jsonObject.getString("NoofMessage"));
                        }
                        catch (JSONException e)
                        {

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
            postdata.put("doctor_id",contactService_getDetails.getData().getUserId());
        }
        catch (JSONException e)
        {

        }
        RequestBody body = RequestBody.create(JSON, postdata.toString());
        return new Request.Builder()
                .addHeader("X-Api-Key", "AB5433GMDF657VBB")
                .addHeader("Content-Type", "application/json")
                .url("http://" + "feish.online/apis/Doctordashboarddetail")
                .post(body)
                .build();
    }


}
