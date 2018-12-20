package com.app.feish.application.doctor;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
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
import com.app.feish.application.Patient.RegisterPatient;
import com.app.feish.application.R;
import com.app.feish.application.Remote.EncryptionDecryption;
import com.app.feish.application.VerificationChoiceActivity;
import com.app.feish.application.modelclassforapi.Contact_register;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import static com.app.feish.application.LoginActivity.JSON;

public class RegisterDoctor extends AppCompatActivity {
    final String TAG="RegisterActivity";
    private String mfirst_name,mlast_name,memail,mphone,mmcinumber;
    private EditText firstname,lastname,email,phone,mcinumber;
    private TextView register;
    Contact_register registerResponse;
    Spinner salutation;
    private int salutation_val;
    private ProgressBar pb;
    LinearLayout linearLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
setContentView(R.layout.content_register_doctor);
        initView();
        salutation.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                        salutation_val=pos+1;
                        //Log.i(TAG, "onItemSelected: "+pos);
                        //Log.i(TAG, "onItemSelected: "+id);


                    }
                    public void onNothingSelected(AdapterView<?> parent) {
                        salutation_val=1;
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
    }

    public void  initView()
    {
        linearLayout=findViewById(R.id.form);
        pb = (ProgressBar) findViewById(R.id.doctorregprogress);
        salutation=(Spinner)findViewById(R.id.dsalutation_spinner);
        firstname=(EditText)findViewById(R.id.dfirstname);

        lastname=(EditText)findViewById(R.id.dlastname);

        mcinumber=(EditText)findViewById(R.id.mcinumber);

        email=(EditText)findViewById(R.id.demail);

        phone=(EditText)findViewById(R.id.dphone);

        register=(TextView)findViewById(R.id.signup);

    }

    public void OpenDoctor(View view)
    {
        OkHttpClient client = new OkHttpClient();
        if(!register_validation()) {
            return;
        }

        pb.setVisibility(View.VISIBLE);
        linearLayout.setVisibility(View.GONE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        Request register_request=doctor_register_request();
        client.newCall(register_request).enqueue(new Callback() {

            @Override
            public void onFailure(Request request, IOException e) {

                Toast.makeText(getApplicationContext(),"Fail",Toast.LENGTH_LONG).show();
                Log.i("Activity", "onFailure: Fail");
            }

            @Override
            public void onResponse(final Response response) throws IOException {
                register_response(response.body().string());
                //final String isSuccessful=register_response(response.body().string());
                final String isSuccessful = registerResponse.getMessage();
                //Log.i(TAG, "onResponse: "+registerResponse.getData().getId());
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        pb.setVisibility(View.GONE);
                        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                        if(isSuccessful.compareTo("Saved")==0){
                            String userid=registerResponse.getData().getId();
                            String token=registerResponse.getToken().getToken();
                            Toast.makeText(getApplicationContext(),"Successful registration",Toast.LENGTH_LONG).show();
                            Intent intent=new Intent(RegisterDoctor.this,VerificationChoiceActivity.class);
                            intent.putExtra("userid",userid);
                            intent.putExtra("token",token);

                            startActivity(intent);
                        }
                        else{
                            Toast.makeText(getApplicationContext(),"User Already Registered with same Email or Phone",Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });
    }
    public void Open(View view)
    {
        startActivity(new Intent(RegisterDoctor.this, LoginActivity.class));
        finish();
    }
    private Request  doctor_register_request(){
        JSONObject postdata = new JSONObject();

       // String encryptmobile= EncryptionDecryption.encode(mphone);

        try {
            postdata.put("salutation",Integer.toString(salutation_val));
            postdata.put("first_name",mfirst_name);
            postdata.put("last_name",mlast_name);
            postdata.put("mci_number",mmcinumber);
            postdata.put("email",memail);
            postdata.put("user_type",2);
            //postdata.put("mobile",encryptmobile);
            postdata.put("mobile",mphone);


        } catch(JSONException e){
            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(JSON,postdata.toString());
        final Request request = new Request.Builder()
                .addHeader("X-Api-Key","AB5433GMDF657VBB")
                .addHeader("Content-Type", "application/json")
                .url("http://feish.online/apis/register")
                .post(body)
                .build();
        return request;
    }


    public void register_response(String response) {


        Gson gson = new GsonBuilder().create();
        registerResponse = gson.fromJson(response,Contact_register.class);
        //return registerResponse.getMessage();
    }

    public boolean register_validation(){
        mfirst_name=firstname.getText().toString();
        mlast_name=lastname.getText().toString();
        memail =email.getText().toString();
        mphone=phone.getText().toString();
        mmcinumber=mcinumber.getText().toString();
        if(mmcinumber.compareTo("")==0){
            Toast.makeText(getApplicationContext(),"MCI number is required",Toast.LENGTH_SHORT).show();
            return false;
        }
        else if(mfirst_name.compareTo("")==0){
            Toast.makeText(getApplicationContext(),"First name is required",Toast.LENGTH_SHORT).show();
            return false;
        }
        else if(mlast_name.compareTo("")==0){
            Toast.makeText(getApplicationContext(),"Last name is required",Toast.LENGTH_SHORT).show();
            return false;
        }
        else if(memail.compareTo("")==0){
            Toast.makeText(getApplicationContext(),"Email is required",Toast.LENGTH_SHORT).show();
            return false;
        }
        else if(mphone.compareTo("")==0){
            Toast.makeText(getApplicationContext(),"Phone Number is required",Toast.LENGTH_SHORT).show();
            return false;
        }
        else if (mphone.length()<10){
            Toast.makeText(getApplicationContext(),"Phone Number has less than 10 digits",Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;

    }

}
