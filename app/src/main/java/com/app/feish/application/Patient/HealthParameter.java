package com.app.feish.application.Patient;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.app.feish.application.R;
import com.app.feish.application.Remote.EncryptionDecryption;
import com.app.feish.application.modelclassforapi.ContactService_getDetails;
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

public class HealthParameter extends AppCompatActivity {
    Toolbar toolbar;
    TextView tv_name,tv_mainname,tv_mob,tv_email,tv_acsta,tv_ge,tv_regdate,tv_dob,tv_pid;
    ImageView imageView_edit;
    ContactService_getDetails contactService_getDetails;
    public static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");
    RelativeLayout relativeLayout;

    AsyncTaskRunner runner=new AsyncTaskRunner();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_health_parameter);




        fetchingdata();
        initView();

        }
   private void initView()
   {
       tv_name=findViewById(R.id.pname);
       relativeLayout=findViewById(R.id.addhabit);
       relativeLayout.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {


               Intent intent;
               if (contactService_getDetails != null) {
                   intent = new Intent(HealthParameter.this, AddHabit.class);
                   intent.putExtra("userid", Prefhelper.getInstance(HealthParameter.this).getUserid());
                   startActivity(intent);
               }else{
                   Toast.makeText(HealthParameter.this, "no value found", Toast.LENGTH_SHORT).show();

               }

           }
       });
       tv_mainname=findViewById(R.id.name);
       tv_mob=findViewById(R.id.pmobno);
       tv_email=findViewById(R.id.pemail);
       tv_acsta=findViewById(R.id.acc_status);
       tv_ge=findViewById(R.id.pge);
       imageView_edit=findViewById(R.id.edit_profile);
       tv_regdate=findViewById(R.id.reg_id);
       tv_dob=findViewById(R.id.pdob);
       tv_pid=findViewById(R.id.pid);
       toolbar = findViewById(R.id.toolbar);
       toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
       toolbar.setTitleTextColor(Color.WHITE);
       toolbar.setTitle("Health Parameter");
       setSupportActionBar(toolbar);
       toolbar.setNavigationOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               finish();
           }
       });
       imageView_edit.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               Intent intent=new Intent(HealthParameter.this,SetupProfile.class);
               intent.putExtra("firstname",contactService_getDetails.getData().getFirstName());
               intent.putExtra("lastname",contactService_getDetails.getData().getLastName());
               intent.putExtra("email",contactService_getDetails.getData().getIs_active().toString());

               String decryptphone2= EncryptionDecryption.decode(contactService_getDetails.getData().getMobile());

               intent.putExtra("mobile",decryptphone2);
               if(contactService_getDetails.getData().getBloodGroup()!=null)
               intent.putExtra("bloodgroup",contactService_getDetails.getData().getBloodGroup().toString());
               else
                   intent.putExtra("bloodgroup","0");
               intent.putExtra("gen",contactService_getDetails.getData().getGender());
               if(contactService_getDetails.getData().getMaritalStatus()!=null)
               intent.putExtra("m_status",contactService_getDetails.getData().getMaritalStatus().toString());
               else
                   intent.putExtra("m_status","0");

               if(contactService_getDetails.getData().getAvatar()!=null)
               intent.putExtra("pic",contactService_getDetails.getData().getAvatar().toString());
               else
                   intent.putExtra("pic","");
            if(contactService_getDetails.getData().getIdentityId()!=null)
               intent.putExtra("IdentityId",contactService_getDetails.getData().getIdentityId());
               else
                   intent.putExtra("IdentityId","");

               if(contactService_getDetails.getData().getEthnicityId()!=null)
               intent.putExtra("ethn",contactService_getDetails.getData().getEthnicityId().toString());
               else
                   intent.putExtra("ethn","");
               if(contactService_getDetails.getData().getOccupationId()!=null)
               intent.putExtra("occupation",contactService_getDetails.getData().getOccupationId().toString());
               else
                   intent.putExtra("occupation","");

               if(contactService_getDetails.getData().getIdentityType()!=null)
                   intent.putExtra("identitytype",contactService_getDetails.getData().getIdentityType().toString());
               else
                   intent.putExtra("identitytype","");

               if(contactService_getDetails.getData().getAddress()==null)
                   intent.putExtra("address","");
               else
                   intent.putExtra("address",contactService_getDetails.getData().getAddress().toString());

               if(contactService_getDetails.getData().getHeight()==null)
                   intent.putExtra("Height","");
               else
                   intent.putExtra("Height",contactService_getDetails.getData().getHeight().toString());

               if(contactService_getDetails.getData().getWeight()==null)
                   intent.putExtra("Weight","");
               else
                   intent.putExtra("Weight",contactService_getDetails.getData().getWeight().toString());
               startActivity(intent);
               finish();
           }
       });
   }

    private Request Patient_detail() {
        JSONObject postdata = new JSONObject();
        try {
            postdata.put("user_id", Prefhelper.getInstance(HealthParameter.this).getUserid());
            //postdata.put("password",lpassword);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(JSON, postdata.toString());
        final Request request = new Request.Builder()
                .addHeader("X-Api-Key", "AB5433GMDF657VBB")
                .addHeader("Content-Type", "application/json")
                .url("http://feish.online/apis/getPatientdetails")
                .post(body)
                .build();
        return request;
    }
    public void pdetailJSON(String response) {
        Gson gson = new GsonBuilder().create();
        contactService_getDetails = gson.fromJson(response, ContactService_getDetails.class);
    }
    private void fetchingdata()
    {
        OkHttpClient client = new OkHttpClient();
        Request request = Patient_detail();
        Log.i("", "onClick: "+request);
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                Log.i("Activity", "onFailure: Fail");
            }
            @Override
            public void onResponse(final Response response) throws IOException {

                String body=response.body().string();
                Log.i("", "onResponse: "+body);
                pdetailJSON(body);
                final String message = contactService_getDetails.getMessage();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //pb.setVisibility(View.GONE);
                        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

                        if (message.compareTo("success") == 0) {

                            String comname=contactService_getDetails.getData().getFirstName()+" "+contactService_getDetails.getData().getLastName();
                            tv_name.setText(comname);
                            tv_mainname.setText("Welcome "+comname);
                            tv_email.setText(contactService_getDetails.getData().getIs_active().toString());
                           // String decryptidentity= EncryptionDecryption.decode(contactService_getDetails.getData().getIdentityId());
                            //Toast.makeText(HealthParameter.this, ""+decryptidentity, Toast.LENGTH_SHORT).show();
                          //  tv_regdate.setText(decryptidentity);
                          //String decryptphone= EncryptionDecryption.decode(contactService_getDetails.getData().getMobile());
                           //Toast.makeText(HealthParameter.this, "decrpt"+decryptphone, Toast.LENGTH_SHORT).show();
                         //tv_mob.setText(decryptphone);


                            tv_dob.setText(contactService_getDetails.getData().getBirthDate());


                           // Toast.makeText(HealthParameter.this, ""+decryptidentity, Toast.LENGTH_SHORT).show();



                            tv_pid.setText(contactService_getDetails.getData().getUserId());
                            if(contactService_getDetails.getData().getGender().equals("1"))
                            tv_ge.setText("Male");
                            else
                                tv_ge.setText("Female");

                            if (contactService_getDetails.getData().getEmail().equals("1"))
                            tv_acsta.setText("Active");

                            runner.execute();


                        }

                        else {
                            Toast.makeText(getApplicationContext(), "Fail", Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }

        });

    }



    class AsyncTaskRunner extends AsyncTask<String, String, String> {


        String decryptphone;
        String decryptidentity;

        @Override
        protected String doInBackground(String... params) {
            publishProgress("Loading..."); // Calls onProgressUpdate()
                decryptphone= EncryptionDecryption.decode(contactService_getDetails.getData().getMobile());

                 decryptidentity= EncryptionDecryption.decode(contactService_getDetails.getData().getIdentityId()).toString();


            return null;

        }


        @Override
        protected void onPostExecute(String result) {




                tv_mob.setText(decryptphone);

                tv_regdate.setText(decryptidentity);

                }




        @Override
        protected void onPreExecute() {

        }


        @Override
        protected void onProgressUpdate(String... text) {

        }
    }

}
