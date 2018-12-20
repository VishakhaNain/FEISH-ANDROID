package com.app.feish.application;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.app.feish.application.Remote.EncryptionDecryption;
import com.app.feish.application.modelclassforapi.Contact_verify_email;
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

public class ForgotPassword extends AppCompatActivity {
    Toolbar toolbar;
    private static final String TAG = "VerifyRegEmail" ;
    private EditText femail;
    private Button verifysubmit;
    Contact_verify_email veremailreponse;
    private ProgressBar pb;
    Connectiondetector connectiondetector;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
         toolbar = (Toolbar) findViewById(R.id.toolbar);
         toolbar.setTitleTextColor(Color.WHITE);
         toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
         setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        connectiondetector= new Connectiondetector(getApplicationContext());
        getSupportActionBar().setTitle("Forgot Password");
        femail=(EditText)findViewById(R.id.verifyemail);
        verifysubmit=(Button)findViewById(R.id.verifysubmit);
        pb=(ProgressBar)findViewById(R.id.verifyemailprogress);
        verifysubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(connectiondetector.isConnectingToInternet()) {
                    pb.setVisibility(View.VISIBLE);
                    getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                    OkHttpClient client = new OkHttpClient();
                    Request veremail_request = verify_email_request();
                    client.newCall(veremail_request).enqueue(new Callback() {

                        @Override
                        public void onFailure(Request request, IOException e) {

                            Toast.makeText(getApplicationContext(), "Fail", Toast.LENGTH_LONG).show();
                            Log.i("Activity", "onFailure: Fail");
                        }

                        @Override
                        public void onResponse(final Response response) throws IOException {
                            verify_email_response(response.body().string());
                            final boolean isSuccessful = veremailreponse.getStatus();
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    pb.setVisibility(View.GONE);
                                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                                    if (isSuccessful) {
                                        String userid = veremailreponse.getData().getUserId();
                                        Toast.makeText(getApplicationContext(), "You have entered the registered email", Toast.LENGTH_LONG).show();
                                        Intent intent = new Intent(ForgotPassword.this, SetPasswordActivity.class);
                                        intent.putExtra("userid", userid);
                                        Log.i(TAG, "run: " + userid);
                                        startActivity(intent);
                                    } else {
                                        Toast.makeText(getApplicationContext(), "Email is not registered", Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                        }
                    });
                }
                else
                {
                    Toast.makeText(ForgotPassword.this, "No Internet!", Toast.LENGTH_SHORT).show();
                }

            }

        });

    }

    private Request verify_email_request(){
       // String encryptemail= EncryptionDecryption.encode(femail.getText().toString());
        JSONObject postdata = new JSONObject();
        try {
            postdata.put("email",femail.getText().toString());
        } catch(JSONException e){
            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(JSON,postdata.toString());
        final Request request = new Request.Builder()
                .addHeader("X-Api-Key","AB5433GMDF657VBB")
                .addHeader("Content-Type", "application/json")
                .url("http://feish.online/apis/forgotPassword")
                .post(body)
                .build();
        return request;
    }

    public void verify_email_response(String response) {
        Gson gson = new GsonBuilder().create();
        veremailreponse = gson.fromJson(response,Contact_verify_email.class);
    }
}
