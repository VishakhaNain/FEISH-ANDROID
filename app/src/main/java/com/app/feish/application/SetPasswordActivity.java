package com.app.feish.application;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.app.feish.application.doctor.CheckOtpResponse;
import com.app.feish.application.modelclassforapi.Contact_request_otp;
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

/**
 * Created by This Pc on 3/3/2018.
 */

public class SetPasswordActivity extends AppCompatActivity {

    private String userid;
    private EditText newpass,confirmpass;
    private Button submitpass;
    private ProgressBar pb;
    public void  onClickclose(View view)
    {
        finish();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setpasswordactivity);
        Bundle bundle = getIntent().getExtras();
        userid = bundle.getString("userid");
        Log.i("SetPasswordActivity", "onCreate: "+userid);
        pb=(ProgressBar)findViewById(R.id.setpassprogress);
        newpass=(EditText)findViewById(R.id.enternewpassword);
        confirmpass=(EditText)findViewById(R.id.confirmpassword);
        submitpass=(Button)findViewById(R.id.passwordsubmit);

        submitpass.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                if(validatepassword()){
                    pb.setVisibility(View.VISIBLE);
                    getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                    OkHttpClient client = new OkHttpClient();
                    Request passvalidation_request=set_password_request();
                    client.newCall(passvalidation_request).enqueue(new Callback() {

                        @Override
                        public void onFailure(Request request, IOException e) {

                            Toast.makeText(getApplicationContext(),"Fail",Toast.LENGTH_LONG).show();
                            Log.i("Activity", "onFailure: Fail");
                        }
                        @Override
                        public void onResponse(final Response response) throws IOException {

                            final boolean isSuccessful=set_password_response(response.body().string());
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    pb.setVisibility(View.GONE);
                                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                                    if(isSuccessful){
                                        Toast.makeText(getApplicationContext(),"Password set successfully",Toast.LENGTH_LONG).show();
                                        Intent intent=new Intent(SetPasswordActivity.this,LoginActivity.class);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        SetPasswordActivity.this.finish();
                                        startActivity(intent);
                                    }
                                    else{
                                        Toast.makeText(getApplicationContext(),"Could not set Password",Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                        }
                    });
                }
            }
        });
    }

    private boolean validatepassword(){
        String np=newpass.getText().toString();
        //Log.i("validate", "validatepassword: "+np);
        String cp=confirmpass.getText().toString();
    if(np.compareTo("")==0)
    {
        Toast.makeText(getApplicationContext(),"Set New Password field empty",Toast.LENGTH_LONG).show();
        return false;
    }
    else if (cp.compareTo("")==0)
    {
        Toast.makeText(getApplicationContext(),"Confirm Password field empty",Toast.LENGTH_LONG).show();
        return false;
    }
    else if (np.compareTo(cp)!=0){
        Toast.makeText(getApplicationContext(),"Passwords do not match",Toast.LENGTH_LONG).show();
        return false;
    }
    else {
        return true;
    }
    }

    private Request set_password_request(){
        JSONObject postdata = new JSONObject();
        try {
            postdata.put("user_id",userid);
            postdata.put("password",newpass.getText().toString());
            Log.i("SetPasswordActivity", "set_password_request: "+newpass);
        } catch(JSONException e){
            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(JSON,postdata.toString());


        final Request request = new Request.Builder()
                .addHeader("X-Api-Key","AB5433GMDF657VBB")
                .addHeader("Content-Type", "application/json")
                .url("http://feish.online/apis/resetPassword")
                .post(body)
                .build();
        return request;
    }

    public boolean set_password_response(String response) {
        Gson gson = new GsonBuilder().create();
        //Log.i("TAG", "set_password_response: "+response);
       // Contact_request_otp otpreponse = gson.fromJson(response,Contact_request_otp.class);
        //return otpreponse.getError();
        CheckOtpResponse checkOtpResponse = gson.fromJson(response, CheckOtpResponse.class);

        return checkOtpResponse.getErrorStatus();
    }

}
