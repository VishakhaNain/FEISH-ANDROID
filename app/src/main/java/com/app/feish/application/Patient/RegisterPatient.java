package com.app.feish.application.Patient;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.app.feish.application.Connectiondetector;
import com.app.feish.application.LoginActivity;
import com.app.feish.application.R;
import com.app.feish.application.Remote.ApiUtils;
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
import com.squareup.timessquare.CalendarCellDecorator;
import com.squareup.timessquare.CalendarPickerView;
import com.squareup.timessquare.DefaultDayViewAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;

import static com.app.feish.application.LoginActivity.JSON;

public class RegisterPatient extends AppCompatActivity {
    LinearLayout linearLayout;
  //  final String TAG="RegisterActivity";
    private String mfirst_name,mlast_name,memail,mphone;
    String manufacturer="",model="",release="",macAddress="",str_dob="";
    private EditText firstname,lastname,email,phone,et_dob;
    CalendarPickerView calendar_s;
    private RadioButton radioButtonMale, radioButtonFemale;
    Button btn_s;
    private  int gender=0;
    public static final String VALID_EMAIL_ADDRESS_REGEX ="^\\w+@[a-zA-Z_]+?\\.[a-zA-Z]{3,4}$";
    private TextView register;
    String[] PERMISSIONS = {Manifest.permission.READ_PHONE_STATE, Manifest.permission.READ_SMS, Manifest.permission.ACCESS_WIFI_STATE, Manifest.permission.READ_PHONE_NUMBERS};
    Contact_register registerResponse;
    Spinner salutation;
    private int salutation_val=1;
    private static final int REQUEST_WRITE_PERMISSION = 200;
    private ProgressBar pb;
    Context context=this;
    SimpleDateFormat DATE_FORMAT;
    Connectiondetector connectiondetector;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_register_patient);
        initView();
        DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
        connectiondetector= new Connectiondetector(getApplicationContext());
        //  if(Prefhelper.getInstance(context).getdeviceinfostore()!=1) {
        if (!hasPermissions(this, PERMISSIONS)&& Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            ActivityCompat.requestPermissions(this, PERMISSIONS, REQUEST_WRITE_PERMISSION);

        }
        else {
            AsyncTaskRunner asyncTaskRunner = new AsyncTaskRunner();
            asyncTaskRunner.execute();
        }
      //  }

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
        et_dob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickdate_sdate();
            }
        });


    }
    public void Open(View view)
    {
        startActivity(new Intent(RegisterPatient.this, LoginActivity.class));
        finish();
    }
public void  initView()
{
    linearLayout=findViewById(R.id.form);
    salutation=findViewById(R.id.salutation_spinner);
    pb=findViewById(R.id.patientregprogress);
    firstname=findViewById(R.id.firstname);
    lastname=findViewById(R.id.lastname);
    email=findViewById(R.id.email);
    phone=findViewById(R.id.phone);
    register=findViewById(R.id.signup);
    radioButtonMale = findViewById(R.id.radioMale);
    radioButtonFemale =findViewById(R.id.radioFemale);
    et_dob=findViewById(R.id.dob);
}


    public void OpenPatient(View view)
    {
        if(connectiondetector.isConnectingToInternet()) {
            //startActivity(new Intent(RegisterPatient.this,PatientDashboard.class));

            if (!register_validation()) {
                return;
            }
            pb.setVisibility(View.VISIBLE);
            linearLayout.setVisibility(View.GONE);
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                     callapi();
            //         callapiMDB();
        }
        else
        {
            Toast.makeText(this, "No Internet!!", Toast.LENGTH_SHORT).show();
        }
    }
    public void CloseActivity(View view)
    {
        finish();
    }
private void callapi()
{
    OkHttpClient client = new OkHttpClient();
    Request register_request = build_request();
    client.newCall(register_request).enqueue(new Callback() {

        @Override
        public void onFailure(Request request, IOException e) {

            Toast.makeText(getApplicationContext(), "Fail", Toast.LENGTH_LONG).show();
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
                    if (isSuccessful.compareTo("Saved") == 0) {
                        String userid = registerResponse.getData().getId();
                        String token=registerResponse.getToken().getToken();
                      //  callapiMDB(userid);
                        Toast.makeText(getApplicationContext(), "Successful registration", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(RegisterPatient.this, VerificationChoiceActivity.class);
                        intent.putExtra("userid", userid);
                        intent.putExtra("token",token);

                        startActivity(intent);
                    } else {
                        Toast.makeText(getApplicationContext(), "User Already Registered with same Email or Phone", Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
    });
}
    private Request build_request(){
       /* String encryptmail= EncryptionDecryption.encode(memail);*/
        //String encryptphone= EncryptionDecryption.encode(mphone);
        JSONObject postdata = new JSONObject();
        try {
            postdata.put("salutation",Integer.toString(salutation_val));
            postdata.put("first_name",mfirst_name);
            postdata.put("last_name",mlast_name);
            postdata.put("email",memail);
            postdata.put("user_type",4);
            postdata.put("mobile",mphone);
            postdata.put("birth_date",str_dob);
            postdata.put("gender", Integer.toString(gender));
        } catch(JSONException e){
            e.printStackTrace();
        }
         //HttpUrl.Builder urlBuilder = HttpUrl.parse("http://feish.online/apis/login").newBuilder();
        //String url = urlBuilder.build().toString();
        RequestBody body = RequestBody.create(JSON,postdata.toString());
        final Request request = new Request.Builder()
                .addHeader("X-Api-Key","AB5433GMDF657VBB")
                .addHeader("Content-Type", "application/json")
                .url("http://feish.online/apis/register")
                .post(body)
                .build();
        return request;
    }

   /* private void callapiMDB(final String userid)
    {
        OkHttpClient client = new OkHttpClient();
        Request register_request = build_requestMDB(userid);
        client.newCall(register_request).enqueue(new Callback() {

            @Override
            public void onFailure(Request request, IOException e) {


                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        pb.setVisibility(View.GONE);
                        // Toast.makeText(RegisterPatient.this, ""+body, Toast.LENGTH_SHORT).show();
                        Toast.makeText(getApplicationContext(), "Successful registration", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(RegisterPatient.this, VerificationChoiceActivity.class);
                        intent.putExtra("userid", userid);
                        startActivity(intent);
                    }
                });
            }

            @Override
            public void onResponse(final Response response) throws IOException {
                final String body=response.body().string();

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        pb.setVisibility(View.GONE);
                       // Toast.makeText(RegisterPatient.this, ""+body, Toast.LENGTH_SHORT).show();
                        Toast.makeText(getApplicationContext(), "Successful registration", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(RegisterPatient.this, VerificationChoiceActivity.class);
                        intent.putExtra("userid", userid);
                        startActivity(intent);
                    }
                });
            }
        });
    }
    private Request build_requestMDB(String userid){
        JSONObject postdatauser = new JSONObject();
        JSONObject postdatadevive = new JSONObject();
      //  JSONObject postdatavalue = new JSONObject();
       // JSONArray  jsonArrayrecord= new JSONArray();
        JSONObject postdata = new JSONObject();
        String encryptemail=EncryptionDecryption.encode(memail);
        String encryptmobile=EncryptionDecryption.encode(mphone);
        String encryptdob=EncryptionDecryption.encode(str_dob);
        String encryptmodel=EncryptionDecryption.encode(model);
        String encryptmac=EncryptionDecryption.encode(macAddress);
        String encryptmanufac=EncryptionDecryption.encode(manufacturer);
        String encryptrelease=EncryptionDecryption.encode(release);
        String encryptphone=EncryptionDecryption.encode(mphone);

       // JSONObject postdatamain = new JSONObject();
        try {
            postdatauser.put("salutation",Integer.toString(salutation_val));
            postdatauser.put("first_name",mfirst_name);
            postdatauser.put("last_name",mlast_name);
            postdatauser.put("email",encryptemail);
            postdatauser.put("user_type",4);
            postdatauser.put("mobile",encryptmobile);
            postdatauser.put("birth_date",encryptdob);
            postdatauser.put("gender", Integer.toString(gender));

            postdatadevive.put("model",encryptmodel);
            postdatadevive.put("macadd",encryptmac);
            postdatadevive.put("manufacturer",encryptmanufac);
            postdatadevive.put("release",encryptrelease);
            postdatadevive.put("phone",encryptphone);
            postdatadevive.put("appname","Feish");

            postdata.put("Patient",postdatauser);
            postdata.put("Device",postdatadevive);
            
            postdata.put("created_by",userid);
            postdata.put("created_at",new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(new java.util.Date()));
            postdata.put("modified_by",userid);
            postdata.put("modified_at",new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(new java.util.Date()));
            postdata.put("source_type","mobile");
            postdata.put("deleted_flag","0");

            *//*postdatavalue.put("value",postdata);

            jsonArrayrecord.put(postdatavalue);
            
            postdatamain.put("record",jsonArrayrecord);*//*
        } catch(JSONException e){
            e.printStackTrace();
        }
        Log.d("register data",postdata.toString());

        //HttpUrl.Builder urlBuilder = HttpUrl.parse("http://feish.online/apis/login").newBuilder();
        //String url = urlBuilder.build().toString();
        RequestBody body = RequestBody.create(JSON,postdata.toString());
        return new Request.Builder()
            *//*    .addHeader("X-Api-Key","AB5433GMDF657VBB")
                .addHeader("Content-Type", "application/json")*//*
                .url(ApiUtils.BASE_URLMAngoDB+"add/newpatient")
                .post(body)
                .build();
    }*/


    public void register_response(String response) {
        Gson gson = new GsonBuilder().create();
        registerResponse = gson.fromJson(response,Contact_register.class);
        //return registerResponse.getMessage();
    }

    public boolean register_validation()
    {
        if (radioButtonMale.isChecked()) {
            gender = 1;
        } else if (radioButtonFemale.isChecked()) {
            gender = 2;
        }
        mfirst_name=firstname.getText().toString();
        mlast_name=lastname.getText().toString();
        memail =email.getText().toString();
        mphone=phone.getText().toString();
        str_dob=et_dob.getText().toString();
        if(mfirst_name.compareTo("")==0){
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
        else if (str_dob.compareTo("")==0){
            Toast.makeText(getApplicationContext(),"Select DOB",Toast.LENGTH_SHORT).show();
            return false;
        }
        else if (!memail.matches(VALID_EMAIL_ADDRESS_REGEX)){
            Toast.makeText(getApplicationContext(),"email id is not valid",Toast.LENGTH_SHORT).show();
            return false;

        }
        else if (gender==0){
            Toast.makeText(getApplicationContext(),"choose gender",Toast.LENGTH_SHORT).show();
            return false;

        }

        return true;

    }
    private class AsyncTaskRunner extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... params) {
            //checkPermission();

             manufacturer = Build.MANUFACTURER;
                model = Build.MODEL;
                release = android.os.Build.VERSION.RELEASE;
                if (model.startsWith(manufacturer)) {
                    model = capitalize(model);
                } else {
                    manufacturer = capitalize(manufacturer) + " " + model;
                }
                WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
                WifiInfo wInfo = wifiManager.getConnectionInfo();
                macAddress = wInfo.getMacAddress();
                /*Log.d("manufacture",manufacturer);
                Log.d("model",model);
                Log.d("release",release);
                Log.d("macAddress",macAddress);*/
                return "";
        }


        @Override
        protected void onPostExecute(String result) {
        }


        @Override
        protected void onPreExecute() {

        }


        @Override
        protected void onProgressUpdate(String... text) {

        }
    }
    private void checkPermission() {
        if (!hasPermissions(this, PERMISSIONS)&& Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
           ActivityCompat.requestPermissions(this, PERMISSIONS, REQUEST_WRITE_PERMISSION);

           }


    }
    public static boolean hasPermissions(Context context, String... permissions) {
        if (context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_WRITE_PERMISSION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (grantResults[1] == PackageManager.PERMISSION_GRANTED) {

                        AsyncTaskRunner asyncTaskRunner = new AsyncTaskRunner();
                        asyncTaskRunner.execute();

                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Permission Denied!", Toast.LENGTH_SHORT).show();
                }

        }
    }


    private String capitalize(String s) {
        if (s == null || s.length() == 0) {
            return "";
        }
        char first = s.charAt(0);
        if (Character.isUpperCase(first)) {
            return s;
        } else {
            return Character.toUpperCase(first) + s.substring(1);
        }
    }
    void pickdate_sdate()
    {
        final Dialog dialog1 = new Dialog(context);
        dialog1.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog1.setContentView(R.layout.camgal);
        dialog1.setCanceledOnTouchOutside(false);
        final Calendar nextYear=Calendar.getInstance();
        nextYear.add(Calendar.YEAR,4);
        final  Calendar lastYear=Calendar.getInstance();
        lastYear.add(Calendar.YEAR,-1);
        btn_s=dialog1.findViewById(R.id.ok);
        calendar_s =  dialog1.findViewById(R.id.calendar_view);
        calendar_s.init(lastYear.getTime(), nextYear.getTime()) //
                .inMode(CalendarPickerView.SelectionMode.SINGLE) //
                .withSelectedDate(new Date());
        calendar_s.setCustomDayView(new DefaultDayViewAdapter());
        Date today =new Date();
        Date date= new Date();
        today.setTime(-19800000);
        calendar_s.setDecorators(Collections.<CalendarCellDecorator>emptyList());
        calendar_s.init(today,nextYear.getTime())
                .withSelectedDate(date).inMode(CalendarPickerView.SelectionMode.SINGLE);
        btn_s.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayList<Date> mydates = (ArrayList<Date>)calendar_s.getSelectedDates();
                for (int i = 0; i <mydates.size() ; i++) {
                    Date tempdate = mydates.get(i);
                    String testdate = DATE_FORMAT.format(tempdate);
                    et_dob.setText(testdate);
                    dialog1.dismiss();

                }

            }
        });
        dialog1.show();

    }



}
