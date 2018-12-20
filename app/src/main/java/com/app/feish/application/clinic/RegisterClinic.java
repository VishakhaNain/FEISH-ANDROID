package com.app.feish.application.clinic;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.util.JsonReader;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.app.feish.application.LoginActivity;
import com.app.feish.application.R;
import com.app.feish.application.VerificationChoiceActivity;
import com.app.feish.application.model.Clinic_registermodel;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.StringReader;

import static com.app.feish.application.LoginActivity.JSON;

public class RegisterClinic extends AppCompatActivity {

    private TextView oname, olname, oemail, ophone, cname, cemail, cmobile, ccity, caddress, cpincode;
    private TextView owner, clinic, signup, _response;
    CardView register;
    Clinic_registermodel registerResponse;
    String name, email, mobile, city, pincode, address;
    String fname, lname, email1, mobile1;

    Spinner salutation;
    private int salutation_val;
    private ProgressBar pb;

    LinearLayout linearLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.clinic_register);

        linearLayout = findViewById(R.id.form);
        pb = (ProgressBar) findViewById(R.id.clinicprogress);
        salutation = (Spinner) findViewById(R.id.owner_spinner);
        oname = findViewById(R.id.oname);

        olname = findViewById(R.id.olname);

        oemail = findViewById(R.id.oemail);

        ophone = findViewById(R.id.ophone);

        cname =findViewById(R.id.cname);
        cemail =findViewById(R.id.cemail);
        cmobile =findViewById(R.id.cmobile);
        ccity =  findViewById(R.id.ccity);
        cpincode =findViewById(R.id.cpin);
        caddress =findViewById(R.id.caddress);

        register = findViewById(R.id.cardView1);
        clinic = (TextView) findViewById(R.id.cclinic);
        _response = findViewById(R.id.response);

        signup = findViewById(R.id.cardView);
        owner = (TextView) findViewById(R.id.Cowner);

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


        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            /*    Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();*/


            }
        });

      register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterClinic.this, ClinicDashboard.class);
                startActivity(intent);

            }
        });




        signup.setOnClickListener(new View.OnClickListener() {
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

                        Toast.makeText(getApplicationContext(), "Fail", Toast.LENGTH_LONG).show();
                        Log.i("Activity", "onFailure: Fail");
                    }

                    @Override
                    public void onResponse(final Response response) throws IOException {


                      //  Log.d("response",response.body().string());


                        register_response(response.body().string());

                        //Log.d( "onResponse: ",response.body().string());
                       // Log.d( "onResponse: ",registerResponse.toString());

                        //final String isSuccessful=register_response(response.body().string());
                        final Boolean isSuccessful = registerResponse.getMessage();
                        // Log.i(TAG, "onResponse: "+registerResponse.getData().getId());
                        Log.d( "onResponse: ", String.valueOf(isSuccessful));


                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                pb.setVisibility(View.GONE);
                                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                               // Log.d("response",isSuccessful);
                                if (isSuccessful.compareTo(Boolean.valueOf("User Created Successfully")) == 0) {
                                    String userid = registerResponse.getUser().getId();
                                    String token=registerResponse.getToken();

                                    Toast.makeText(getApplicationContext(), "Successful registration", Toast.LENGTH_LONG).show();
                                    Intent intent = new Intent(RegisterClinic.this, VerificationChoiceActivity.class);
                                    intent.putExtra("userid", userid);
                                    intent.putExtra("token", token);

                                    startActivity(intent);
                                } else {
                                    Toast.makeText(getApplicationContext(), "User Already Registered with same Email or Phone", Toast.LENGTH_LONG).show();
                                }
                            }
                        });
                }
                });
            }


            private com.squareup.okhttp.Request clinic_register_request() {

                JSONObject postdata = new JSONObject();


                try {
                    postdata.put("email", oemail.getText());
                    postdata.put("first_name", oname.getText());
                    postdata.put("last_name", olname.getText());
                    postdata.put("mobile", ophone.getText());
                    postdata.put("salutation", Integer.toString(salutation_val));
                    postdata.put("clinicname", name);
                    postdata.put("clinic_address", address);
                    postdata.put("clinic_city", city);
                    postdata.put("clinic_pincode", pincode);
                    postdata.put("clinic_mobile", mobile);
                    postdata.put("clinic_email", email);
                    postdata.put("user_type", 5);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                RequestBody body = RequestBody.create(JSON, postdata.toString());
                 Log.d( "onResponse: ",body.toString());
                //Log.d("response",body.toString());
                final com.squareup.okhttp.Request request = new com.squareup.okhttp.Request.Builder()
                        .addHeader("X-Api-Key", "AB5433GMDF657VBB")
                        .addHeader("Content-Type", "application/json")
                        //.url("http://feish.online/apis/Registerowner")
                        //for testing purpose
                        .url("http://feish.online/MyApi/clinic/register")
                        .post(body)
                        .build();
                return request;
            }


            public void register_response(String response) {
                Gson gson = new GsonBuilder().create();
                com.google.gson.stream.JsonReader reader = new com.google.gson.stream.JsonReader(new StringReader(response));
                reader.setLenient(true);
//                Log.d( "onResponse: ",registerResponse.toString());
                registerResponse = gson.fromJson(reader, Clinic_registermodel.class);
                //return registerResponse.getMessage();
            }





        });

    }

    public boolean register_validation() {
        name = cname.getText().toString();
        email = cemail.getText().toString();
        pincode = cpincode.getText().toString();
        mobile = cmobile.getText().toString();
        address = caddress.getText().toString();
        city = ccity.getText().toString();


        if (name.compareTo("") == 0) {
            Toast.makeText(getApplicationContext(), "clinic name is required", Toast.LENGTH_SHORT).show();
            return false;
        } else if (email.compareTo("") == 0) {
            Toast.makeText(getApplicationContext(), "Email is required", Toast.LENGTH_SHORT).show();
            return false;
        } else if (pincode.compareTo("") == 0) {


            Toast.makeText(getApplicationContext(), "pincode is required", Toast.LENGTH_SHORT).show();
            return false;
        } else if (mobile.compareTo("") < 10) {
            Toast.makeText(getApplicationContext(), "phone is not less then 10 digit", Toast.LENGTH_SHORT).show();
            return false;
        } else if (address.compareTo("") == 0) {
            Toast.makeText(getApplicationContext(), "address is required", Toast.LENGTH_SHORT).show();
            return false;
        } else if (city.length() == 0) {
            Toast.makeText(getApplicationContext(), "city is required", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

        public void login (View view){
            startActivity(new Intent(RegisterClinic.this, LoginActivity.class));
            finish();


    }
}



