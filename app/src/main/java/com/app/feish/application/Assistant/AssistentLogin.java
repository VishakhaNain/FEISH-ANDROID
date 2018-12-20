package com.app.feish.application.Assistant;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.app.feish.application.Connectiondetector;
import com.app.feish.application.R;
import com.app.feish.application.modelclassforapi.ContactLogin;
import com.app.feish.application.sessiondata.Prefhelper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import static com.app.feish.application.fragment.Addfamilyreport.JSON;

public class AssistentLogin extends AppCompatActivity {

    ContactLogin loginResponse;
    TextView textView, Go,error;
    EditText email, password;
    TextView assistent2;
    Button login;
    String userid,user_type,full_name;
    String mail="",str_password="";
    Connectiondetector connectiondetector;
    Context context=this;
    private String res;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.assistentlogin);
        textView =findViewById(R.id.assistent);
        Go =findViewById(R.id.go);
        error =findViewById(R.id.error_show);
        email =findViewById(R.id.loginusername);
        connectiondetector= new Connectiondetector(getApplicationContext());
        assistent2 = findViewById(R.id.assistent2);
        password =findViewById(R.id.loginpassword);
        login = findViewById(R.id.buttonforlogin);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
mail=email.getText().toString();
str_password=password.getText().toString();
                if(validatelogin())
                {
                    if(connectiondetector.isConnectingToInternet())
                    {
                     //   pb.setVisibility(View.VISIBLE);
                        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                        OkHttpClient client = new OkHttpClient();
                        Request request = login_request();
                        Log.i("", "onClick: " + request);
                        client.newCall(request).enqueue(new Callback() {
                            @Override
                            public void onFailure(Request request, IOException e) {
                                Log.i("Activity", "onFailure: Fail");
                            }

                            @Override
                            public void onResponse(final Response response) throws IOException {

                                String body = response.body().string();
                                Log.i("", "onResponse: " + body);
                                loginJSON(body);
                                final String message = loginResponse.getMessage();
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                       // pb.setVisibility(View.GONE);
                                        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

                                        if (message.compareTo("success") == 0) {

                                            Toast.makeText(getApplicationContext(), "Login Successful", Toast.LENGTH_LONG).show();
                                            userid = loginResponse.getData().getId();
                                            user_type = loginResponse.getData().getUserType();

                                            Prefhelper.getInstance(AssistentLogin.this).setLoggedIn(true);
                                            Prefhelper.getInstance(AssistentLogin.this).setUserid(userid);
                                            Prefhelper.getInstance(AssistentLogin.this).setUsertype(user_type);
                                            full_name = loginResponse.getData().getFullName();


                                            //Intent intent2=new Intent(AssistentLogin.this,DashboardActivity.class);

                                            if ((loginResponse.getData().getUserType()).compareTo("3") == 0) {
                                                Intent intent = new Intent(AssistentLogin.this, AssistantDashbord.class);
                                                // intent.putExtra("userid",loginResponse.getData().getId());
                                                startActivity(intent);
                                                finish();
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
                        Toast.makeText(context, "No Internet!!", Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });

        Go.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mail=email.getText().toString();

                retrofit();
            }
        });
    }

    private Request login_request()
    {
        JSONObject postdata = new JSONObject();
        try {
            postdata.put("username",mail);
            postdata.put("password",str_password);
        } catch(JSONException e){
            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(JSON,postdata.toString());
        return new Request.Builder()
                .addHeader("X-Api-Key","AB5433GMDF657VBB")
                .addHeader("Content-Type", "application/json")
                .url("http://feish.online/apis/login")
                .post(body)
                .build();
    }

    public void loginJSON(String response) {
        Gson gson = new GsonBuilder().create();
        loginResponse = gson.fromJson(response,ContactLogin.class);
    }

    private boolean validatelogin(){
        mail=email.getText().toString();
        Log.i("validate", "validatepassword: "+mail);
        str_password=password.getText().toString();
        Log.i("validate", "validatepassword: "+str_password);
        if(mail.compareTo("")==0)
        {
            Toast.makeText(getApplicationContext(),"UserEmail field empty",Toast.LENGTH_LONG).show();
            return false;
        }
        else if (str_password.compareTo("")==0)
        {
            Toast.makeText(getApplicationContext(),"Password field empty",Toast.LENGTH_LONG).show();
            return false;
        }
        else {
            return true;
        }
    }
    private Request listservices()
    {
        JSONObject postdata = new JSONObject();
        try {
            postdata.put("email",mail);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(JSON, postdata.toString());
        return new Request.Builder()
                .addHeader("X-Api-Key", "AB5433GMDF657VBB")
                .addHeader("Content-Type", "application/json")
                .url("http://feish.online/apis/assistentlogin")
                .post(body)
                .build();
    }
    private void retrofit()
    {

        OkHttpClient client = new OkHttpClient();
        Request validation_request = listservices();
        client.newCall(validation_request).enqueue(new Callback() {

            @Override
            public void onFailure(Request request, IOException e) {

                // Toast.makeText(getActivity(),"Fail",Toast.LENGTH_LONG).show();
                Log.i("Activity", "onFailure: Fail");
            }

            @Override
            public void onResponse(final Response response) throws IOException {
                 final String body=response.body().string();

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        String mystring = body;
                        int lastIndex = mystring.lastIndexOf("}");

                        res = body.substring(0, (lastIndex + 1));
                       try {

                            JSONObject jsonObject = new JSONObject(body);

                            int success = jsonObject.getInt("Success");

                            if (success == 1) {
                                JSONArray var = jsonObject.getJSONArray("value");
                                String user_type= jsonObject.getString("user_type");
                                String user_id=jsonObject.getString("user_id");
                                JSONObject jsonObject1=var.getJSONObject(0);
                                String name = jsonObject1.getString("first_name");
                                String lastname = jsonObject1.getString("last_name");
                                assistent2.setText("Dr. "+name+" "+lastname);
                                assistent2.setVisibility(View.VISIBLE);
                                password.setVisibility(View.VISIBLE);
                                login.setVisibility(View.VISIBLE);
                                Go.setVisibility(View.GONE);
 }
 else {
                                Toast.makeText(AssistentLogin.this, "" + jsonObject.getString("Message"), Toast.LENGTH_SHORT).show();

                            }
                        } catch (JSONException e) {
                            Toast.makeText(AssistentLogin.this, "" + e, Toast.LENGTH_SHORT).show();
                        }
                        }

                    }
                );
                assistent2.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void afterTextChanged(Editable editable) {


                        login.setVisibility(View.VISIBLE);
                    }
                });

            }
        });
                    }



}