package com.app.feish.application.Patient;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.app.feish.application.Adpter.Plandetailforpatientadt;
import com.app.feish.application.Connectiondetector;
import com.app.feish.application.R;
import com.app.feish.application.Remote.ApiUtils;
import com.app.feish.application.model.drplanmodel;
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
import static com.app.feish.application.Patient.ConsultDoctor.JSON;

public class PuchaseDoctorPlan extends AppCompatActivity {
Context context=this;
String doc_id="",doc_name="",service_id="";
Connectiondetector connectiondetector;
Plandetailforpatientadt plandetailforpatientadt;
ArrayList<drplanmodel> drplanmodels= new ArrayList<>();
RecyclerView recyclerView;
    Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_puchase_doctor_plan);
         toolbar =  findViewById(R.id.toolbar);
         toolbar.setTitleTextColor(Color.WHITE);
         toolbar.setTitle("Plan Details");
         toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        doc_id=getIntent().getStringExtra("doc_id");
        doc_name=getIntent().getStringExtra("doc_name");
        service_id=getIntent().getStringExtra("service_id");
        recyclerView=findViewById(R.id.recycler_view);
        LinearLayoutManager linearLayoutManager= new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        connectiondetector= new Connectiondetector(getApplicationContext());
        if(connectiondetector.isConnectingToInternet())
        {
            fetchdata();
        }
        else
        {

            Toast.makeText(context, "No Internet!!", Toast.LENGTH_SHORT).show();
        }

    }
    private void fetchdata()
    {
        OkHttpClient client = new OkHttpClient();
        Request validation_request = listservices();
        client.newCall(validation_request).enqueue(new Callback() {

            @Override
            public void onFailure(Request request, IOException e) {

                // Toast.makeText(context,"Fail",Toast.LENGTH_LONG).show();
                Log.i("Activity", "onFailure: Fail");
            }

            @Override
            public void onResponse(final Response response) throws IOException {
                final String body=response.body().string();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        // Toast.makeText(context, ""+body, Toast.LENGTH_SHORT).show();
                        JSONObject jsonObject= null;
                        try {
                            jsonObject = new JSONObject(body);

                            if(jsonObject.getBoolean("status"))
                            {
                                JSONArray jsonArray= jsonObject.getJSONArray("data");
                                for (int i = 0; i <jsonArray.length() ; i++)
                                {
                                    JSONObject jsonObject1=jsonArray.getJSONObject(i);
                                    JSONObject jsonObject2=jsonObject1.getJSONObject("DoctorPlanDetail");
                                    drplanmodels.add(new drplanmodel(jsonObject2.getInt("id"),jsonObject2.getString("name"),jsonObject2.getString("price"),jsonObject2.getString("validity"),jsonObject2.getString("percentage_per_visit"),jsonObject2.getString("plan_details")));

                                }
                                plandetailforpatientadt= new Plandetailforpatientadt(drplanmodels,context,doc_name,doc_id,service_id);
                                recyclerView.setAdapter(plandetailforpatientadt);

                            }
                            else
                            {
                                Toast.makeText(context, ""+jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            Toast.makeText(context, ""+e, Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                        }

                    }
                });
            }
        });

    }

    private Request listservices() {
        JSONObject postdata = new JSONObject();
        try {
            postdata.put("user_id",doc_id);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(JSON, postdata.toString());
        return new Request.Builder()
                .addHeader("X-Api-Key", "AB5433GMDF657VBB")
                .addHeader("Content-Type", "application/json")
                .url(ApiUtils.BASE_URL+"listdrplan")
                .post(body)
                .build();
    }

}
