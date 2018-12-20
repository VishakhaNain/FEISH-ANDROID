package com.app.feish.application.Patient;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.app.feish.application.Connectiondetector;
import com.app.feish.application.R;
import com.app.feish.application.modelclassforapi.ContactListDoctor;
import com.app.feish.application.modelclassforapi.DoctorDatum;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;

public class ConsultDoctor extends AppCompatActivity {
TextView textView;
    RadioButton free,paid;
    String enteraddLabDoc,enterYouradd;
    EditText enteraddresslabname,enteryourAddress;
    TextView submit;
    private int specialist_doc_val;
    Spinner specialityspinner;
    ContactListDoctor searchresponse;
    ImageView imageView_back;
    Connectiondetector connectiondetector;
    Context context=this;
    public  static  final MediaType JSON= MediaType.parse("application/json:charset=utf-8");
    ArrayList<String> Specialtylist= new ArrayList<>();

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
                            specialityspinner.setAdapter(customAdapter);
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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consult_doctor);
        initView();

        specialityspinner.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                        specialist_doc_val=pos+1;

                    }
                    public void onNothingSelected(AdapterView<?> parent) {
                        specialist_doc_val=1;
                    }
                });

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog d= new Dialog(ConsultDoctor.this);
                d.requestWindowFeature(Window.FEATURE_NO_TITLE);
                d.setContentView(R.layout.help);
                Button b=d.findViewById(R.id.dia_dismissok);
                b.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        d.dismiss();
                    }
                });
                d.show();
            }
        });
    }
    public  void initView()
    {
        connectiondetector= new Connectiondetector(getApplicationContext());
        if(connectiondetector.isConnectingToInternet())
            fetchSpecialtylist();
        else
            Toast.makeText(context, "No Internet!!!", Toast.LENGTH_SHORT).show();
        enteraddresslabname=findViewById(R.id.searchdoctorID);
        enteryourAddress=findViewById(R.id.searchaddressId);
        specialityspinner=findViewById(R.id.specialistID);
        free=findViewById(R.id.freeplanID);
        paid=findViewById(R.id.purchaseplanID);
        submit=findViewById(R.id.submitDetailsId);
         imageView_back=findViewById(R.id.img_back);
        textView=findViewById(R.id.help);

        imageView_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    public void Vlivk(View view)
    {
        if(validatelogin()) {
            OkHttpClient client = new OkHttpClient();
            Request validation_request = search_request();
            client.newCall(validation_request).enqueue(new Callback() {

                @Override
                public void onFailure(Request request, IOException e) {

                    // Toast.makeText(getApplicationContext(),"Fail",Toast.LENGTH_LONG).show();
                    Log.i("Activity", "onFailure: Fail");
                }

                @Override
                public void onResponse(final Response response) throws IOException {
                    final String body = response.body().string();
                    Log.i(TAG, "onResponse: " + body);
                    searchJSON(body);
                    final String message = searchresponse.getMessage();
                    final ArrayList<DoctorDatum> doctorData = searchresponse.getData();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                    //        Toast.makeText(ConsultDoctor.this, ""+body, Toast.LENGTH_SHORT).show();
//                                pb.setVisibility(View.GONE);
                            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                            if (message.compareTo("success") == 0) {
                                Toast.makeText(getApplicationContext(), "search Successful", Toast.LENGTH_LONG).show();
                                Intent i = new Intent(ConsultDoctor.this, SearchDoctorList.class);
                                i.putExtra("doctordata",doctorData);
                                startActivity(i);

                            }  else {
                                Toast.makeText(getApplicationContext(), "No service found", Toast.LENGTH_LONG).show();


                            }
                        }
                    });
                }
            });
        }
    }

    private boolean validatelogin(){
        enteraddLabDoc=enteraddresslabname.getText().toString();
        enterYouradd=enteryourAddress.getText().toString();
        if(enteraddLabDoc.compareTo("")==0)
        {
            Toast.makeText(getApplicationContext()," field empty",Toast.LENGTH_LONG).show();
            return false;
        }
        else if (enterYouradd.compareTo("")==0)
        {
            Toast.makeText(getApplicationContext()," field empty",Toast.LENGTH_LONG).show();
            return false;
        }
        else {
            return true;
        }
    }

    private Request search_request(){
        JSONObject postdata = new JSONObject();
        try {
            postdata.put("specialty_id",Integer.toString(specialist_doc_val));
            postdata.put("search",enteraddresslabname.getText().toString());
            postdata.put("address",enteryourAddress.getText().toString());
        } catch(JSONException e){
            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(JSON,postdata.toString());
        return new Request.Builder()
                .addHeader("X-Api-Key","AB5433GMDF657VBB")
                .addHeader("Content-Type", "application/json")
                .url("http://feish.online/apis/listAvailableDoctor")
                .post(body)
                .build();
    }

    public void searchJSON(String response) {
        Gson gson = new GsonBuilder().create();
        searchresponse= gson.fromJson(response,ContactListDoctor.class);
        //Data data= gson.fromJson(response,Data.class);
        // data.getAddress();

    }
    class CustomAdapter_setupprofile extends BaseAdapter {
        Context context;
        LayoutInflater inflter;
        List<String> medicalconditionlists;
        public CustomAdapter_setupprofile(Context applicationContext, List<String> medicalconditionlists) {
            this.context = applicationContext;
            this.medicalconditionlists=medicalconditionlists;
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

}
