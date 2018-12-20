package com.app.feish.application.clinic;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.app.feish.application.LoginActivity;
import com.app.feish.application.R;
import com.app.feish.application.VerificationChoiceActivity;
import com.app.feish.application.model.Clinic_registermodel;
import com.app.feish.application.modelclassforapi.Contact_register;
import com.app.feish.application.modelclassforapi.ListServicesContact;
import com.app.feish.application.sessiondata.Prefhelper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.StringReader;

import static com.app.feish.application.LoginActivity.JSON;

public class ClinicDoctorSignup extends AppCompatActivity {

    final String TAG = "RegisterActivity";

    private EditText firstname, lastname, email, phone;
    private TextView register;
    CardView adddoctor;
    DoctorList addresponse;
    Spinner salutation;
    private int salutation_val;
    private ProgressBar pb;
    LinearLayout linearLayout;
    String fname, lname, mobile, emailid;
    int pos = 0;

    String clinic_id;
    String tokens;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.clinicdoctorsignup);

        clinic_id = Prefhelper.getInstance(ClinicDoctorSignup.this).getClinic_id();
        tokens = Prefhelper.getInstance(ClinicDoctorSignup.this).getToken();

        linearLayout = findViewById(R.id.form);
        pb=findViewById(R.id.doctorregprogress);
        salutation=findViewById(R.id.dsalutation_spinner);
        firstname=findViewById(R.id.dfirstname);
        lastname=findViewById(R.id.dlastname);
        email=findViewById(R.id.demail);
        phone=findViewById(R.id.dphone);
       // register=findViewById(R.id.signup);
         adddoctor=findViewById(R.id.cardView);

        pos = getIntent().getIntExtra("pos", 0);

        salutation.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                        salutation_val = pos + 1;
                        //Log.i(TAG, "onItemSelected: "+pos);
                        //Log.i(TAG, "onItemSelected: "+id);
                    }
                    public void onNothingSelected(AdapterView<?> parent) {
                        salutation_val = 1;
                    }
                });
/*
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                String name = firstname.getText().toString();
                String em = email.getText().toString();
                intent.putExtra("firstname", name);
                intent.putExtra("email", em);
                intent.putExtra("pos", pos);
                setResult(RESULT_OK, intent);
                finish();
            }
        });*/

        adddoctor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OkHttpClient client = new OkHttpClient();
                if (!register_validation()) {
                    return;
                }
                com.squareup.okhttp.Request register_request = clinic_register_request();
                client.newCall(register_request).enqueue(new Callback() {

                    @Override
                    public void onFailure(com.squareup.okhttp.Request request, IOException e) {
                        //Toast.makeText(getApplicationContext(), "Fail", Toast.LENGTH_SHORT).show();
                       Log.i("Activity", "onFailure: Fail");
                    }

                    @Override
                    public void onResponse(final Response response) throws IOException {
                        addresponse(response.body().string());
                        final Integer isSuccessful = addresponse.getUserId();
                        Log.d("onResponse: ", String.valueOf(isSuccessful));
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                pb.setVisibility(View.GONE);
                                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                                if(Boolean.valueOf(String.valueOf(isSuccessful))){

                                    Toast.makeText(getApplicationContext(),"User Added Successfully",Toast.LENGTH_LONG).show();
                                    Intent intent = new Intent(ClinicDoctorSignup.this,ClinicDashboard.class);
                                    startActivity(intent);
                                } else {
                                    Toast.makeText(getApplicationContext(),"User Added Successfully",Toast.LENGTH_LONG).show();
                                    Intent intent = new Intent(ClinicDoctorSignup.this,ClinicDashboard.class);
                                    startActivity(intent);
                                }

                            }

                        });
                    }
                });
            }

            private com.squareup.okhttp.Request clinic_register_request() {

                JSONObject postdata = new JSONObject();
                try {
                    //postdata.put("user_id", "1065");
                    postdata.put("doctor_salutation", Integer.toString(salutation_val));
                    postdata.put("doctor_first_name", fname);
                    postdata.put("doctor_last_name", lname);
                    postdata.put("doctor_mobile", mobile);
                    postdata.put("doctor_email", emailid);
                    postdata.put("user_type", 2);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                RequestBody body = RequestBody.create(JSON, postdata.toString());
                Log.d("onResponse: ", body.toString());
                final com.squareup.okhttp.Request request = new com.squareup.okhttp.Request.Builder()
                        .addHeader("X-Api-Key", tokens)
                        .addHeader("Content-Type", "application/json")
                        .url("http://feish.online/MyApi/clinic/doctor/add/"+clinic_id
                        )
                        .post(body)
                        .build();
                return request;
            }


            public void addresponse(String response) {
                Gson gson = new GsonBuilder().create();

                JsonObject json =    gson.fromJson(response, JsonObject.class);
                if(json.get("data") instanceof JsonArray)
                {
                    addresponse = gson.fromJson(response, DoctorList.class);
                }
                else
                {
                    JsonObject data = (JsonObject) json.get("data");
                    JsonArray arrayData = new JsonArray();
                    arrayData.add(data);
                    json.add("data", arrayData);
                    addresponse = gson.fromJson(json, DoctorList.class);

                }

            }

        });

    }

              public boolean register_validation() {
                fname = firstname.getText().toString();
                emailid = email.getText().toString();
                lname = lastname.getText().toString();
                mobile = phone.getText().toString();
                if (fname.compareTo("") == 0) {
                    Toast.makeText(getApplicationContext(), "First name is required", Toast.LENGTH_SHORT).show();
                    return false;
                } else if (emailid.compareTo("") == 0) {
                    Toast.makeText(getApplicationContext(), "Email is required", Toast.LENGTH_SHORT).show();
                    return false;
                } else if (lname.compareTo("") == 0) {
                    Toast.makeText(getApplicationContext(), "lastname is required", Toast.LENGTH_SHORT).show();
                    return false;
                } else if (mobile.compareTo("") < 10) {
                    Toast.makeText(getApplicationContext(), "phone is not less then 10 digit", Toast.LENGTH_SHORT).show();
                    return false;
                }
                return true;
            }
            }




