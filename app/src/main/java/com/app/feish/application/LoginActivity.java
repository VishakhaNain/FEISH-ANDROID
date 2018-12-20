package com.app.feish.application;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.app.feish.application.Assistant.AssistentLogin;
import com.app.feish.application.Patient.PatientDashboard;
import com.app.feish.application.clinic.ClinicDashboard;
import com.app.feish.application.clinic.RegisterClinic;
import com.app.feish.application.doctor.DoctorDashboard;
import com.app.feish.application.sessiondata.Prefhelper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import com.app.feish.application.Patient.RegisterPatient;
import com.app.feish.application.doctor.RegisterDoctor;
import com.app.feish.application.modelclassforapi.ContactLogin;
import java.util.ArrayList;
import java.util.Locale;

public class

LoginActivity extends AppCompatActivity {
    private RadioButton radioButtonDoctor;
    private RadioButton radioButtonPatient;
    private RadioButton radioButtonClinic;
    private Button continueButton;
    LinearLayout linearLayout;
    public int p=4;
    Button loginForEnter;
    ContactLogin loginResponse;
    TextView register,login,forgotpass,tv_assistentlogin;
    EditText luser,lpass;
    String lusername,lpassword;
    private ProgressBar pb;
    protected static String userid;
    protected static String full_name;
    protected static String user_type;
    protected static String clinic_id;
    protected static String token;
    public static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");
    private String TAG="LoginActivity";
    View popupView;
    Connectiondetector connectiondetector;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        connectiondetector= new Connectiondetector(getApplicationContext());
        Sigupway();
        initViews();
     
    }
   public void initViews()
   {
       tv_assistentlogin=findViewById(R.id.assistentlogin);
       register=findViewById(R.id.register_here);
       forgotpass=findViewById(R.id.forgotpassword);
       luser=findViewById(R.id.loginusername);
       lpass=findViewById(R.id.loginpassword);
       pb =findViewById(R.id.progress);
       linearLayout =findViewById(R.id.websitevisit);
       linearLayout.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://feish.online"));
               startActivity(browserIntent);
           }
       });
       pb.setVisibility(View.GONE);
       loginForEnter=findViewById(R.id.buttonforlogin);
       connectiondetector= new Connectiondetector(getApplicationContext());
       tv_assistentlogin.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v)
           {
                   startActivity(new Intent(LoginActivity.this, AssistentLogin.class));
           }
       });
   }
  
    public void Openactivity(View view)
    {

        if(validatelogin())
        {
         if(connectiondetector.isConnectingToInternet()) {
             pb.setVisibility(View.VISIBLE);
             getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                     WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
             OkHttpClient client = new OkHttpClient();
             Request request = login_request();
             Log.i(TAG, "onClick: " + request);
             client.newCall(request).enqueue(new Callback() {
                 @Override
                 public void onFailure(Request request, IOException e) {
                     Log.i("Activity", "onFailure: Fail");

                 }

                 @Override
                 public void onResponse(final Response response) throws IOException {

                     String body = response.body().string();
                     Log.i(TAG, "onResponse: " + body);
                     loginJSON(body);
                     final String message = loginResponse.getMessage();
                     runOnUiThread(new Runnable() {
                         @Override
                         public void run() {
                             pb.setVisibility(View.GONE);
                             getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

                             if (message.compareTo("Login done Successfully") == 0) {

                                 Toast.makeText(getApplicationContext(), "Login Successful", Toast.LENGTH_LONG).show();
                                 userid = loginResponse.getData().getId();
                                 user_type = loginResponse.getData().getUserType();

                                 Prefhelper.getInstance(LoginActivity.this).setLoggedIn(true);
                                 Prefhelper.getInstance(LoginActivity.this).setUserid(userid);

                                 Prefhelper.getInstance(LoginActivity.this).setUsertype(user_type);

                                 full_name = loginResponse.getData().getFullName();

                                 //Intent intent2=new Intent(LoginActivity.this,DashboardActivity.class);

                                 if ((loginResponse.getData().getUserType()).compareTo("4") == 0) {
                                     Intent intent = new Intent(LoginActivity.this, PatientDashboard.class);
                                     // intent.putExtra("userid",loginResponse.getData().getId());



                                     startActivity(intent);
                                     finish();
                                 } else  if ((loginResponse.getData().getUserType()).compareTo("2") == 0) {
                                     Intent intent = new Intent(LoginActivity.this, DoctorDashboard.class);
                                     //   intent.putExtra("userid", loginResponse.getData().getId());
                                     startActivity(intent);
                                     finish();

                                 }else  if ((loginResponse.getData().getUserType()).compareTo("5") == 0) {
                                     //set clinic id and token for doctor dashboar
                                     String str =  loginResponse.toString();
                                     if(loginResponse.getData().getClinic_id()!=null)
                                     {
                                         clinic_id = Integer.toString(loginResponse.getData().getClinic_id());
                                         Prefhelper.getInstance(LoginActivity.this).setClinic_id(clinic_id);
                                     }
                                     if(loginResponse.getData().getToken()!=null)
                                     {
                                         token = loginResponse.getData().getToken();
                                         Prefhelper.getInstance(LoginActivity.this).setToken(token);
                                     }

                                     Intent intent = new Intent(LoginActivity.this, ClinicDashboard.class);
                                     //   intent.putExtra("userid", loginResponse.getData().getId());
                                     startActivity(intent);
                                     finish();
                                 } else
                                 {
                                     Toast.makeText(LoginActivity.this, "Invalid credentials!!", Toast.LENGTH_SHORT).show();
                                 }
                             } else if (message.compareTo("Invalid username or password") == 0) {
                                 Toast.makeText(getApplicationContext(), "Invalid Email or Password", Toast.LENGTH_LONG).show();
                             } else {
                                 Toast.makeText(getApplicationContext(), "Fail", Toast.LENGTH_LONG).show();
                             }
                         }
                     });
                 }
             });
         }
         else
         {
             Toast.makeText(this, "No Internet!!", Toast.LENGTH_SHORT).show();
         }
        }
    }
     public void Openactivityforgot(View view)
    {
        startActivity(new Intent(LoginActivity.this,ForgotPassword.class));
    }

    public void onButtonShowPopupWindowClick(View view) {

        // get a reference to the already created main layout
        RelativeLayout mainLayout = findViewById(R.id.login_register_activity);

        // inflate the layout of the popup window
        LayoutInflater inflater = (LayoutInflater)
                getSystemService(LAYOUT_INFLATER_SERVICE);
        assert inflater != null;
        @SuppressLint("InflateParams")
        View popupView = inflater.inflate(R.layout.choice_popup_dialog, null);

        // create the popup window
        int width = RelativeLayout.LayoutParams.WRAP_CONTENT;
        int height = RelativeLayout.LayoutParams.WRAP_CONTENT;
     /*   boolean focusable; // lets taps outside the popup also dismiss it
        focusable = true;*/
        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, true);

        // show the popup window
        popupWindow.showAtLocation(mainLayout, Gravity.CENTER, 0, 0);
        //popupWindow.setOutsideTouchable(true);
        //popupWindow.setFocusable(true);
        popupWindow.setAnimationStyle(R.xml.animation);
       radioButtonDoctor = popupView.findViewById(R.id.radioDoctor);
        radioButtonPatient = popupView.findViewById(R.id.radioPatient);
        radioButtonClinic = popupView.findViewById(R.id.radioClinic);

        continueButton=popupView.findViewById(R.id.continueButton);
        continueButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(radioButtonDoctor.isChecked())
                {

                    popupWindow.dismiss();
                    Intent intent=new Intent(LoginActivity.this,RegisterDoctor.class);
                    startActivity(intent);

                }
               if (radioButtonPatient.isChecked())
                {

                    popupWindow.dismiss();
                    Intent intent=new Intent(LoginActivity.this,RegisterPatient.class);
                    startActivity(intent);

                }
                else if (radioButtonClinic.isChecked())
                {

                    popupWindow.dismiss();
                    Intent intent=new Intent(LoginActivity.this,RegisterClinic.class);
                    startActivity(intent);

                }
            }
        });
    }
    @SuppressLint("InflateParams")
    public void Sigupway()
    {
        LayoutInflater inflater = (LayoutInflater)
                getSystemService(LAYOUT_INFLATER_SERVICE);
        assert inflater != null;
        popupView = inflater.inflate(R.layout.choice_popup_dialog, null);

        int width = RelativeLayout.LayoutParams.WRAP_CONTENT;
        int height = RelativeLayout.LayoutParams.WRAP_CONTENT;
      /*  boolean focusable; // lets taps outside the popup also dismiss it
        focusable = true;*/
        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, true);

       // radioButtonDoctor = popupView.findViewById(R.id.radioDoctor);
        radioButtonPatient = popupView.findViewById(R.id.radioPatient);
       radioButtonClinic = popupView.findViewById(R.id.radioClinic);
        continueButton=popupView.findViewById(R.id.continueButton);
        continueButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(radioButtonDoctor.isChecked())
                {

                    popupWindow.dismiss();
                    Intent intent=new Intent(LoginActivity.this,RegisterDoctor.class);
                    startActivity(intent);

                }
                else if (radioButtonPatient.isChecked())
                {

                    popupWindow.dismiss();
                    Intent intent=new Intent(LoginActivity.this,RegisterPatient.class);
                    startActivity(intent);

                }
                else if (radioButtonPatient.isChecked())
                {

                    popupWindow.dismiss();
                    Intent intent=new Intent(LoginActivity.this,RegisterClinic.class);
                    startActivity(intent);

                }


            }
        });
    }
    public void getSpeechInput(View view)
    {

        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());

        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(intent, 10);
        } else {
            Toast.makeText(this, "Your Device Don't Support Speech Input", Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case 10:
                if (resultCode == RESULT_OK && data != null) {
                    ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    luser.setText(result.get(0));
                }
                break;
        }
    }


    private Request login_request()
    {
        JSONObject postdata = new JSONObject();
        try {
            postdata.put("username",lusername);
            postdata.put("password",lpassword);
        } catch(JSONException e){
            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(JSON,postdata.toString());
        return new Request.Builder()
                .addHeader("X-Api-Key","AB5433GMDF657VBB")
                .addHeader("Content-Type", "application/json")
               // .url("http://10.0.2.2:8089/apis/login")
//               .url("http://feish.online/apis/login")
                .url("http://feish.online/MyApi/login")
                .post(body)
                .build();
    }

    public void loginJSON(String response) {
        Gson gson = new GsonBuilder().create();
        loginResponse = gson.fromJson(response,ContactLogin.class);
    }

    private boolean validatelogin(){
        lusername=luser.getText().toString();
        Log.i("validate", "validatepassword: "+lusername);
        lpassword=lpass.getText().toString();
        Log.i("validate", "validatepassword: "+lpassword);
        if(lusername.compareTo("")==0)
        {
            Toast.makeText(getApplicationContext(),"Username field empty",Toast.LENGTH_LONG).show();
            return false;
        }
        else if (lpassword.compareTo("")==0)
        {
            Toast.makeText(getApplicationContext(),"Password field empty",Toast.LENGTH_LONG).show();
            return false;
        }
        else {
            return true;
        }
    }

}
