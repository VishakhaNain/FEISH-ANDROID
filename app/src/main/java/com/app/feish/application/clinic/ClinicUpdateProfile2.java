package com.app.feish.application.clinic;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.app.feish.application.R;
import com.app.feish.application.sessiondata.Prefhelper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.StringReader;

import static com.app.feish.application.doctor.SetupProfileForDoctor.JSON;

public class ClinicUpdateProfile2 extends AppCompatActivity {
    private EditText firstname,lastname,email,mobile,clinicemail,clinicmobile,clinicaddress;
    Spinner salutation;
    Button updateclinicbtn;
    private int salutation_val;
    private ProgressBar pb;
    private Updateclinicpojo updateclinicpojo;

    String clinic_id;
    String tokens;

    LinearLayout linearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.clinicprofileupdate2);
        firstname=findViewById(R.id.firstnameupdate);
        lastname=findViewById(R.id.lastnameupdate);
        email=findViewById(R.id.emailupdate);
        mobile=findViewById(R.id.mobileupdate);
        clinicemail=findViewById(R.id.clinicupdateemail);
        clinicmobile=findViewById(R.id.clinicupdatemobile);
        clinicaddress=findViewById(R.id.clinicupdateaddress);
        updateclinicbtn=findViewById(R.id.updateclinic);
        salutation=findViewById(R.id.salutationupdate);

        clinic_id = Prefhelper.getInstance(ClinicUpdateProfile2.this).getClinic_id();
        tokens = Prefhelper.getInstance(ClinicUpdateProfile2.this).getToken();

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

updateclinicbtn.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {

        OkHttpClient client = new OkHttpClient();
        Request validation_request=updateinformation();
        client.newCall(validation_request).enqueue(new Callback() {

            @Override
            public void onFailure(Request request, IOException e) {

                // Toast.makeText(getApplicationContext(),"Fail",Toast.LENGTH_LONG).show();
                Log.i("Activity", "onFailure: Fail");
            }

            @Override
            public void onResponse(final Response response) throws IOException {

                updateresponse(response.body().string());
                final boolean isSuccessful = updateclinicpojo.getError();

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(!isSuccessful){

                            Toast.makeText(getApplicationContext(),"Update successful",Toast.LENGTH_LONG).show();

                            Intent intent=new Intent(ClinicUpdateProfile2.this,ClinicProfileUpdate.class);
                            startActivity(intent);finish();
                        }
                        else{
                            Toast.makeText(getApplicationContext(),updateclinicpojo.getMessage(),Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });
        }
});

}


    private Request updateinformation(){
        JSONObject postdata = new JSONObject();
        try {
           //postdata.put("user_id", "45");
           /* postdata.put("first_name","vivek");
            postdata.put("last_name","singh");
            postdata.put("mobile","9874560250");
            postdata.put("email","hfdsjhfsj@gmail.com");
            postdata.put("clinic_name","Maut ki dukan");
            postdata.put("clinic_email","ijfidejfjn@gmail.com");
            postdata.put("clinic_mobile","7890147025");
            postdata.put("clinic_address","andher nagri");*/
           // postdata.put("user_id", Prefhelper.getInstance(ClinicUpdateProfile2.this).getUserid());
            postdata.put("first_name",firstname.getText().toString());
            postdata.put("last_name",lastname.getText().toString());
            postdata.put("mobile",mobile);
            postdata.put("email",email.getText().toString());
            postdata.put("clinic_name","Maut ki dukan");
            postdata.put("clinic_email",clinicemail.getText().toString());
            postdata.put("clinic_mobile",clinicmobile.getText().toString());
            postdata.put("clinic_address",clinicaddress.getText().toString());

        } catch(JSONException e){
            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(JSON,postdata.toString());
        final Request request = new Request.Builder()
                .addHeader("X-Api-Key",tokens)
                .addHeader("Content-Type", "application/json")
                .url("http://feish.online/MyApi/clinic/"+clinic_id)
                .put(body)
                .build();
        return request;
    }
    public void updateresponse(String response) {
        Gson gson = new GsonBuilder().create();
        JsonReader reader = gson.newJsonReader(new StringReader(response));
        reader.setLenient(true);
        updateclinicpojo = gson.fromJson(reader,Updateclinicpojo.class);
    }
}
