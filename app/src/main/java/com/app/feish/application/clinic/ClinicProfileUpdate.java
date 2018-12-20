package com.app.feish.application.clinic;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.app.feish.application.R;
import com.app.feish.application.doctor.ViewProfileDoctor;
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
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.StringReader;

import static com.app.feish.application.doctor.ViewProfileDoctor.JSON;

public class ClinicProfileUpdate  extends AppCompatActivity {
    Button clinicupdatebtn;
    private ClinicDetails clinicDetails;
    TextView getfirstname, getlastname, getmobile, getemail, getclinicname, getclinicemail, getclinicmobile, getclinicaddress;
Context context;
    String clinic_id;
    String tokens;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.clinicprofileupdate);
        getfirstname = findViewById(R.id.GetFirstName);
        getlastname = findViewById(R.id.GetLastName);
        getmobile = findViewById(R.id.GetMobile);
        getemail = findViewById(R.id.GetEmail);
        getclinicname = findViewById(R.id.GetClinicName);
        getclinicemail = findViewById(R.id.GetClinicEmail);
        getclinicmobile = findViewById(R.id.GetClinicMobile);
        getclinicaddress = findViewById(R.id.GetClinicAddress);
        clinicupdatebtn = findViewById(R.id.UpdateClinicBtn);

        clinic_id = Prefhelper.getInstance(ClinicProfileUpdate.this).getClinic_id();
        tokens = Prefhelper.getInstance(ClinicProfileUpdate.this).getToken();

        fetchdata();
        clinicupdatebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ClinicProfileUpdate.this, ClinicUpdateProfile2.class);
                startActivity(intent);
            }
        });
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
                            clinicDetails = gson.fromJson(jsonObject.get("clinicDetails").getAsJsonObject(), ClinicDetails.class);
                            getfirstname.setText(clinicDetails.getFirstName());
                            getlastname.setText(clinicDetails.getLastName());
                            getemail.setText(clinicDetails.getUserEmail());
                            getmobile.setText(clinicDetails.getUserMobile());
                            getclinicname.setText(clinicDetails.getClinicName());
                            getclinicemail.setText(clinicDetails.getEmail());
                            getclinicmobile.setText(clinicDetails.getMobile());
                            getclinicaddress.setText(clinicDetails.getAddress());
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
                .addHeader("X-Api-Key",tokens)
                .addHeader("Content-Type", "application/json")
                .url("http://feish.online/MyApi/clinic/"+clinic_id)
                .get()
                .build();
        return request;
    }
}
