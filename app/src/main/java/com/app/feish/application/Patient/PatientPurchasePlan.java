package com.app.feish.application.Patient;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import com.app.feish.application.Adpter.CustomAdapter_listpatientplan;
import com.app.feish.application.Connectiondetector;
import com.app.feish.application.R;
import com.app.feish.application.Remote.ApiUtils;
import com.app.feish.application.model.patientplanmodel;
import com.app.feish.application.sessiondata.Prefhelper;
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
import java.util.HashMap;

import static com.app.feish.application.fragment.Addfamilyreport.JSON;

public class PatientPurchasePlan extends AppCompatActivity {
LinearLayoutManager layoutManager;
GridView recyclerView;
public static HashMap<Integer,String> speciality= new HashMap<>();
    CustomAdapter_listpatientplan customAdapter_listpatientplan;
Context context=this;
Connectiondetector connectiondetector;
    ArrayList<patientplanmodel>  drplanmodels= new ArrayList<>();
ImageView imageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_purchase_plan);
        recyclerView=findViewById(R.id.recycler_view);
        imageView=findViewById(R.id.img_back);
        connectiondetector= new Connectiondetector(getApplicationContext());
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        layoutManager = new LinearLayoutManager(this);
       /* recyclerView.setLayoutManager(layoutManager);
        products.add(new Product("Plan Price","Plan Validity","Value","Dr Name"));
        products.add(new Product("Plan Price","Plan Validity","Value","Dr Name"));
        products.add(new Product("Plan Price","Plan Validity","Value","Dr Name"));
        products.add(new Product("Plan Price","Plan Validity","Value","Dr Name"));
        products.add(new Product("Plan Price","Plan Validity","Value","Dr Name"));
        patientpuchaseplanadt= new patientpuchaseplanadt(context,products);
        recyclerView.setAdapter(patientpuchaseplanadt);*/

        recyclerView =  findViewById(R.id.recycler_view);
        if(connectiondetector.isConnectingToInternet())
            fetchdata();
        else
            Toast.makeText(context, "No Internet!!", Toast.LENGTH_SHORT).show();

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

                runOnUiThread(new Runnable() {
                                  @Override
                                  public void run()
                                  {
                                     try {
                                          JSONObject jsonObject= new JSONObject(body);
                                            if(jsonObject.getBoolean("status"))
                                            {
                                                  JSONArray jsonArray = jsonObject.getJSONArray("data");
                                                  JSONArray jsonArraySpecialtyList = jsonObject.getJSONArray("SpecialtyList");
                                                  JSONArray jsonArraydoctordata = jsonObject.getJSONArray("doctordata");
                                                  JSONArray jsonArraydoctordataspe = jsonObject.getJSONArray("doctorapedata");
                                                for (int k = 0; k <jsonArraySpecialtyList.length() ; k++)
                                                {
                                                    JSONObject  jsonObject1=jsonArraySpecialtyList.getJSONObject(k);
                                                    speciality.put(jsonObject1.getInt("id"),jsonObject1.getString("specialty_name"));

                                                }
                                                     for (int i = 0; i < jsonArray.length(); i++)
                                                     {

                                                        JSONObject jsonobject = jsonArray.getJSONObject(i);
                                                        JSONArray jsonobjectdd = jsonArraydoctordata.getJSONArray(i);
                                                         JSONArray jsonobjectdsd = jsonArraydoctordataspe.getJSONArray(i);
                                                         int id = jsonobject.getInt("id");
                                                           String name = jsonobject.getString("name");
                                                          //  String planid = jsonobject.getString("plan_details");
                                                           String planid = jsonobject.getString("planid");
                                                           String doctor_id = jsonobject.getString("doctor_id");
                                                           String totalvisit = jsonobject.getString("valid_visits");
                                                           String usedvisit = jsonobject.getString("nooftimeused");
                                                           String remainingvisit = String.valueOf((Integer.parseInt(totalvisit) - Integer.parseInt(usedvisit)));
                                                           String price = jsonobject.getString("price");

                                                             //String percentage_per_visit = jsonobject.getString("percentage_per_visit");
                                                              // String validity = jsonobject.getString("validity");
                                                              drplanmodels.add(new patientplanmodel(id, name, price, remainingvisit, totalvisit, usedvisit, planid, doctor_id,jsonobjectdd,jsonobjectdsd));


                                                     }
                                                               customAdapter_listpatientplan = new CustomAdapter_listpatientplan(drplanmodels, context, 0);
                                                                recyclerView.setAdapter(customAdapter_listpatientplan);
                                                     }
                                                        else
                                                       {
                                                           Toast.makeText(context, "No Record found", Toast.LENGTH_SHORT).show();

                                                        }
                                      }
                                      catch (JSONException e)
                                      {
                                          Toast.makeText(PatientPurchasePlan.this, "" + e, Toast.LENGTH_SHORT).show();
                                      }
                                  }
                              }
                );
            }
        });

    }

    private Request listservices() {
        JSONObject postdata = new JSONObject();
        try {
            postdata.put("user_id", Prefhelper.getInstance(context).getUserid());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(JSON, postdata.toString());
        return new Request.Builder()
                .addHeader("X-Api-Key", "AB5433GMDF657VBB")
                .addHeader("Content-Type", "application/json")
                .url(ApiUtils.BASE_URL+"fetchpatientpackages")
                .post(body)
                .build();
    }
}
