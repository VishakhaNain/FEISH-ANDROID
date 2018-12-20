package com.app.feish.application.doctor;


import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.app.feish.application.Adpter.CustomAdapter_listplan;
import com.app.feish.application.Connectiondetector;
import com.app.feish.application.R;
import com.app.feish.application.Remote.ApiUtils;
import com.app.feish.application.fragment.AddAssistant;
import com.app.feish.application.model.drplanmodel;
import com.app.feish.application.modelclassforapi.Datum2;
import com.app.feish.application.modelclassforapi.ListServicesContact;
import com.app.feish.application.sessiondata.Prefhelper;
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
import java.util.ArrayList;
import java.util.List;

import static com.app.feish.application.fragment.Addfamilyreport.JSON;

public class DoctorPackageDetail extends AppCompatActivity {
RecyclerView recyclerView;
Context context=this;
    CustomAdapter_listplan customAdapter_listplan;
    Connectiondetector connectiondetector;
    ArrayList<drplanmodel>  drplanmodels= new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_package_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        toolbar.setTitle("Doctor Package");
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

connectiondetector= new Connectiondetector(getApplicationContext());
if(connectiondetector.isConnectingToInternet())
    fetchdata();
else
    Toast.makeText(context, "No Internet!!", Toast.LENGTH_SHORT).show();
        recyclerView = (RecyclerView)findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(context, 2));
        recyclerView.setNestedScrollingEnabled(false);
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
                final String body=response.body().string();

                listServiceResponse(body);

                runOnUiThread(new Runnable() {
                                  @Override
                                  public void run()
                                  {
                                      // Toast.makeText(context, ""+body, Toast.LENGTH_SHORT).show();

                                      try {
                                          JSONArray jsonArray = new JSONArray(body);

                                          for (int i = 0; i < jsonArray.length(); i++) {

                                              JSONObject jsonobject = jsonArray.getJSONObject(i);
                                              String id = jsonobject.getString("id");
                                              String name = jsonobject.getString("name");
                                              String plan_details = jsonobject.getString("plan_details");
                                              String price = jsonobject.getString("price");

                                              String percentage_per_visit = jsonobject.getString("percentage_per_visit");
                                              String validity = jsonobject.getString("validity");
                                              drplanmodels.add(new drplanmodel(0,name,price,validity,percentage_per_visit,plan_details));


                                          }
                                          customAdapter_listplan= new CustomAdapter_listplan(drplanmodels,context,1);
                                          recyclerView.setAdapter(customAdapter_listplan);

                                        }
                                      catch (JSONException e)
                                      {
                                          Toast.makeText(DoctorPackageDetail.this, "" + e, Toast.LENGTH_SHORT).show();
                                      }

                                  }
                              }
                );
            }
        });

    }

    private Request listservices() {
        JSONObject postdata = new JSONObject();
       /* try {
            postdata.put("email",mail);
        } catch (JSONException e) {
            e.printStackTrace();
        }*/
        RequestBody body = RequestBody.create(JSON, postdata.toString());
        return new Request.Builder()
                .addHeader("X-Api-Key", "AB5433GMDF657VBB")
                .addHeader("Content-Type", "application/json")
                .url(ApiUtils.BASE_URL+"fetchdoctorpackages")
                .get()
                .build();
    }
    public void listServiceResponse(String response) {
        Gson gson = new GsonBuilder().create();

    }

}
