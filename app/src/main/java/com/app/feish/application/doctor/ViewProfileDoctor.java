package com.app.feish.application.doctor;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
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
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ViewProfileDoctor extends AppCompatActivity {
ImageView toolbar;
     ContactService_getDetails contactService_getDetails;
Context context=this;
ArrayList<String> Specialtylist= new ArrayList<>();
    protected static String userid;
    public static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");
    private String TAG="MainActivity";
    private TextView et_google,et_fb,et_twitter,mcinumber, address;
    Spinner et_specilization;
    ImageView imageView_imgdocprofile;
    String imageurl="";
    AsyncTaskRunner runner = new AsyncTaskRunner();



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
    TextView first_name_tv,last_name_tv,email_tv,mobile_tv,qualification_tv,consultation_time_tv,identity_type_tv,identity_val_tv;
    TextView text_feepersession,tv_about;

    Button editButton;
    Bundle bundle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_profile_doctor);
        initView();
         fetchdata();
        fetchSpecialtylist();
    }
    private void initView()
    {
        et_specilization = (Spinner) findViewById(R.id.enterspecilizationID);
        et_google = (TextView) findViewById(R.id.googlepluslink);
        et_fb = (TextView) findViewById(R.id.fblink);
        tv_about = (TextView) findViewById(R.id.about);
        et_twitter = (TextView) findViewById(R.id.twitterlink);
        mcinumber = (TextView) findViewById(R.id.mcinumber);
        address = (TextView) findViewById(R.id.addressdr);
        first_name_tv=(TextView)findViewById(R.id.firstNameis);
        last_name_tv=(TextView)findViewById(R.id.lastNameis);
        email_tv=(TextView)findViewById(R.id.Emailis);
        text_feepersession=(TextView)findViewById(R.id.feepersession);
        mobile_tv=(TextView)findViewById(R.id.Mobilis);
        imageView_imgdocprofile=findViewById(R.id.imgdocprofile);
        editButton=(Button)findViewById(R.id.editprofileID);
        qualification_tv=(TextView)findViewById(R.id.qualificationIs);
        consultation_time_tv=(TextView)findViewById(R.id.consultationTimeIs);
       toolbar=findViewById(R.id.img_back);
       toolbar.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               finish();
           }
       });
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int pos;
                if (contactService_getDetails.getData().getDr_specilization() != null) {
                     pos = Integer.parseInt(contactService_getDetails.getData().getDr_specilization().toString()) - 1;
                } else
                {
                      pos=0;
                }
                Bundle args = new Bundle();
                args.putSerializable("ARRAYLIST",(Serializable)Specialtylist);
                Intent intent=new Intent(ViewProfileDoctor.this,SetupProfileForDoctor.class);
                intent.putExtra("firstname",first_name_tv.getText().toString());
                intent.putExtra("aboutDoctor",tv_about.getText().toString());
                intent.putExtra("fee",text_feepersession.getText().toString());
                intent.putExtra("lastname",last_name_tv.getText().toString());
                intent.putExtra("email",email_tv.getText().toString());
                intent.putExtra("mobile",mobile_tv.getText().toString());
                intent.putExtra("fblink",et_fb.getText().toString());
                intent.putExtra("googlelink",et_google.getText().toString());
                intent.putExtra("twitterlink",et_twitter.getText().toString());
                intent.putExtra("addtess",address.getText().toString());
                intent.putExtra("image",imageurl);
                intent.putExtra("specilization",pos);
                intent.putExtra("BUNDLE",args);
                if(consultation_time_tv.getText()!=null) {
                    intent.putExtra("consultationtime", consultation_time_tv.getText().toString());
                    intent.putExtra("qualification", qualification_tv.getText().toString());
                }
                else
                    intent.putExtra("i",true);

                startActivity(intent);
                finish();
            }
        });
    }
    private void fetchSpecialtylist()
    {
        OkHttpClient client = new OkHttpClient();
        Request request = occupationrequest();

        Log.i("", "onClick: "+request);
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                Log.i("Activity", "onFailure: Fail");
            }
            @Override
            public void onResponse(final Response response) throws IOException {

                final String body=response.body().string();
                // medicalconditionJSON(body);
                Log.i("1234add", "onResponse: "+body);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                       try {
                            JSONObject  jsonObject = new JSONObject(body);
                            JSONArray jsonArray= jsonObject.getJSONArray("Specialty");
                            for (int i = 0; i <jsonArray.length() ; i++)
                            {
                                JSONObject jsonObject1= jsonArray.getJSONObject(i);
                                Specialtylist.add(jsonObject1.getString("specialty_name"));
                            }
                            CustomAdapter_setupprofile customAdapter=new CustomAdapter_setupprofile(context,Specialtylist);
                            et_specilization.setAdapter(customAdapter);
                        }


                        catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(context, ""+e, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }

        });

    }

    private Request occupationrequest()
    {
        JSONObject postdata = new JSONObject();
        RequestBody body = RequestBody.create(JSON, postdata.toString());
        return new Request.Builder()
                .addHeader("X-Api-Key", "AB5433GMDF657VBB")
                .addHeader("Content-Type", "application/json")
                .url("http://feish.online/apis/listspeciality")
                .post(body)
                .build();
    }

    private void fetchdata()
    {
        OkHttpClient client = new OkHttpClient();
        Request request = drprofile_request();
        Log.i(TAG, "onClick: "+request);
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                Log.i("Activity", "onFailure: Fail");
            }
            @Override
            public void onResponse(final Response response) throws IOException {

                String body=response.body().string();
                Log.i(TAG, "onResponse: "+body);
                drprofileJSON(body);
                final String message = contactService_getDetails.getMessage();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //pb.setVisibility(View.GONE);
                        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

                        if (message.compareTo("success") == 0) {
                            Toast.makeText(context, "here", Toast.LENGTH_SHORT).show();

                            first_name_tv.setText(contactService_getDetails.getData().getFirstName());
                            last_name_tv.setText(contactService_getDetails.getData().getLastName());
                            email_tv.setText(contactService_getDetails.getData().getIs_active().toString());





                            if(contactService_getDetails.getData().getFee()!=null)
                            text_feepersession.setText(contactService_getDetails.getData().getFee().toString());




                            if(contactService_getDetails.getData().getAboutDoctor()!=null)
                                tv_about.setText(contactService_getDetails.getData().getAboutDoctor().toString());
                            else
                                tv_about.setText("");







                            mcinumber.setVisibility(View.GONE);
                            if(contactService_getDetails.getData().getAddress()!=null)
                            address.setText(contactService_getDetails.getData().getAddress().toString());
                            else
                                address.setText("update address");
                                if(contactService_getDetails.getData().getDr_specilization()!=null) {
                                    int i=Integer.parseInt(contactService_getDetails.getData().getDr_specilization().toString())-1;
                                    et_specilization.setSelection(i);
                                }

                            if(contactService_getDetails.getData().getAvatar()!=null)
                            {
                                Picasso.with(ViewProfileDoctor.this)
                                        .load(contactService_getDetails.getData().getAvatar().toString())
                                        .into(imageView_imgdocprofile);
                                imageurl=contactService_getDetails.getData().getAvatar().toString();
                            }
                            else {
                                    imageurl="";
                            }

                            if(contactService_getDetails.getData().getQualification()!=null)
                                qualification_tv.setText(contactService_getDetails.getData().getQualification());
                            if(contactService_getDetails.getData().getConsultationTime()!=null)
                                consultation_time_tv.setText(contactService_getDetails.getData().getConsultationTime());

                            runner.execute();

                        }else {
                            Toast.makeText(getApplicationContext(), "Fail", Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }

            });

    }
    public void drprofileJSON(String response) {
        Gson gson = new GsonBuilder().create();
        contactService_getDetails = gson.fromJson(response, ContactService_getDetails.class);
    }
    private Request drprofile_request() {
        JSONObject postdata = new JSONObject();
        try {
            postdata.put("user_id", Prefhelper.getInstance(ViewProfileDoctor.this).getUserid());
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
     class AsyncTaskRunner extends AsyncTask<String, String, String> {


        String decryptmobile;
        String decryptfacebbok;
        String decryptgoogle;
        String decrypttwitter;
        @Override
        protected String doInBackground(String... params) {
            publishProgress("Loading..."); // Calls onProgressUpdate()

            if(contactService_getDetails.getData().getMobile()!=null)
             decryptmobile= EncryptionDecryption.decode(contactService_getDetails.getData().getMobile());

            if(contactService_getDetails.getData().getFacebook()!=null)
            decryptfacebbok= EncryptionDecryption.decode(contactService_getDetails.getData().getFacebook().toString());

            if(contactService_getDetails.getData().getGooglePlus()!=null)
            decryptgoogle= EncryptionDecryption.decode( contactService_getDetails.getData().getGooglePlus().toString());

            if(contactService_getDetails.getData().getTwitter()!=null)
            decrypttwitter = EncryptionDecryption.decode(contactService_getDetails.getData().getTwitter().toString());



            return null;

        }


        @Override
        protected void onPostExecute(String result) {


            if(contactService_getDetails.getData().getFacebook()!=null) {

                et_fb.setText(decryptfacebbok);
            }
            else
                et_fb.setText("update facebook link");

            if(contactService_getDetails.getData().getGooglePlus()!=null) {

                et_google.setText(decryptgoogle);
            }
            else
                et_google.setText("update Google Plus link");


            if(contactService_getDetails.getData().getTwitter()!=null) {

                et_twitter.setText(decrypttwitter);
            }
            else
                et_twitter.setText("update twitter link");


            if(contactService_getDetails.getData().getMobile()!=null) {

                mobile_tv.setText(decryptmobile);
            }
            else
                mobile_tv.setText("update mobile");

        }


        @Override
        protected void onPreExecute() {

        }


        @Override
        protected void onProgressUpdate(String... text) {

        }
    }

}
class CustomAdapter_setupprofile extends BaseAdapter {
    Context context;
    LayoutInflater inflter;
    List<String> medicalconditionlists;

    public CustomAdapter_setupprofile(Context applicationContext, List<String> medicalconditionlists) {
        this.context = applicationContext;
        this.medicalconditionlists = medicalconditionlists;
        inflter = (LayoutInflater.from(applicationContext));
    }

    @Override
    public int getCount() {
        return medicalconditionlists.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = inflter.inflate(R.layout.spinner_item, null);
        TextView names = (TextView) view.findViewById(R.id.txt);
        names.setText(medicalconditionlists.get(i));
        return view;
    }



}





