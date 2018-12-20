package com.app.feish.application.Patient;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.app.feish.application.Adpter.CustomAdapter_vitalsigns;
import com.app.feish.application.Connectiondetector;
import com.app.feish.application.R;
import com.app.feish.application.Remote.ApiUtils;
import com.app.feish.application.Remote.EncryptionDecryption;
import com.app.feish.application.model.vitalsignlist;
import com.app.feish.application.sessiondata.Prefhelper;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class VitalSigns extends AppCompatActivity {
    Toolbar toolbar;
    CustomAdapter_vitalsigns customAdapter_vitalsigns;
    Context context=this;
    HashMap<Integer,String> VitalSignlist= new HashMap<>();
    HashMap<Integer,String> VitalUnitlist= new HashMap<>();
    HashMap<Integer,String> VitalSignunit= new HashMap<>();
    ArrayList<String>  vitalSignname= new ArrayList<>();
    ArrayList<String>  unitname= new ArrayList<>();
    Connectiondetector connectiondetector;

EditText editText;
Button button;
    RecyclerView recyclerView;
    Spinner spinner_vitalsign,getSpinner_vitalsignunit;
    public static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");
  LinearLayoutManager layoutManager;
    TextView textView;
    ProgressDialog progressDialog;
EditText et_maxo,et_mino,et_o,et_remark;
    ArrayList<vitalsignlist> vitalsignlists= new ArrayList<>();
    LinearLayout ll_addsigns;
String userid="";
    String Vsign="",Vunit="",str_maxo="",str_mino="",str_o="",str_remark="";
   // @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void initView()
    {
        connectiondetector= new Connectiondetector(getApplicationContext());
        toolbar = findViewById(R.id.toolbar);
        editText=findViewById(R.id.et);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setTitle("Vital Signs");
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        spinner_vitalsign=findViewById(R.id.vitalsignlist_spinner);
        getSpinner_vitalsignunit=findViewById(R.id.vitalsignlistunit_spinner);
        recyclerView= findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        progressDialog= new ProgressDialog(context);
        progressDialog.setTitle("Loading.........");
        progressDialog.show();
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setNestedScrollingEnabled(false);
        textView=findViewById(R.id.addsign);
        ll_addsigns=findViewById(R.id.addsigns);
        et_maxo=findViewById(R.id.maxobservation);
        et_mino=findViewById(R.id.minobservation);
        et_o=findViewById(R.id.observation);
        et_remark=findViewById(R.id.remark);
        button=findViewById(R.id.btn_submit);
        if(getIntent()!=null)
        {
            userid=getIntent().getStringExtra("userid");
        }
        else {

            userid = Prefhelper.getInstance(VitalSigns.this).getUserid();
        }

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vital_signs);
        initView();
        fetchingdata();

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(textView.getText().toString().equals("Add Signs"))
                {
                 ll_addsigns.setVisibility(View.VISIBLE);
                 textView.setText("Cancel");
                 textView.setBackgroundColor(Color.RED);
                }
                else
                    {
                        ll_addsigns.setVisibility(View.GONE);
                        textView.setText("Add Signs");
                        textView.setBackgroundResource(R.drawable.rounded);
                    }
            }
        });
        spinner_vitalsign.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
               // Vsign= vitalSignname.get(position);
                if(unitname.size()>0)
                    unitname.clear();
                for (Object o : VitalSignlist.keySet()) {
                    if (VitalSignlist.get(o).equals( vitalSignname.get(position))) {
                        Vsign=o.toString();
                        String key=VitalSignunit.get(o);
                        unitname.add(key);

                    }
                }
                ArrayAdapter aa_vitalsignunit = new ArrayAdapter(context,R.layout.spinner_item,unitname);
                getSpinner_vitalsignunit.setAdapter(aa_vitalsignunit);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                if (connectiondetector.isConnectingToInternet()) {
                    str_maxo = et_maxo.getText().toString();
                    str_mino = et_mino.getText().toString();
                    str_o = et_o.getText().toString();
                    str_remark = et_remark.getText().toString();
                    Vunit = getSpinner_vitalsignunit.getSelectedItem().toString();
                    for (Object o : VitalUnitlist.keySet()) {
                        if (VitalUnitlist.get(o).equals(Vunit)) {
                            Vunit = o.toString();
                        }
                    }
                    if (validateVitalsign()) {
                        progressDialog.show();
                        Addingvitalsign();
                       // AddingvitalsignMDB();

                    }
                }
                else
                {
                    Toast.makeText(context, "No Internet!!", Toast.LENGTH_SHORT).show();
                }
            }

        });

    }
    private void fetchingdata()
    {
        OkHttpClient client = new OkHttpClient();
        Request request = listVitalSign();
        Log.i("", "onClick: "+request);
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                Log.i("Activity", "onFailure: Fail");
            }
            @Override
            public void onResponse(final Response response) throws IOException {

                final String body=response.body().string();
                Log.i("1234check", "onResponse: "+body);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONObject jsonObject=new JSONObject(body);
                           // editText.setVisibility(View.GONE);
                          //  editText.setText(""+body);
                            if(jsonObject.getInt("Success")==1)
                            {
                                VitalSignlist=toMap(jsonObject.getJSONObject("vital_sign_lists_record"));
                                JSONArray jsonArray=jsonObject.getJSONArray("vital_units_record");
                                JSONArray jsonArray1=jsonObject.getJSONArray("vital_signs_user_record");
                                ArrayAdapter aa_vitalsign = new ArrayAdapter(context,R.layout.spinner_item,vitalSignname);
                                spinner_vitalsign.setAdapter(aa_vitalsign);
                                for (int i = 0; i <jsonArray.length() ; i++)
                                {
                                       JSONObject  jsonObject1=jsonArray.getJSONObject(i);
                                       VitalUnitlist.put(jsonObject1.getInt("id"),jsonObject1.getString("name"));
                                    VitalSignunit.put(jsonObject1.getInt("vital_sign_list_id"),jsonObject1.getString("name"));
                                }
                                for (int k = 0; k <jsonArray1.length() ; k++)
                                {
                                    JSONObject jsonObject1=jsonArray1.getJSONObject(k);
                                    String vitalsignname=VitalSignlist.get(jsonObject1.getInt("vital_sign_list_id"));
                                    String unitname=VitalUnitlist.get(jsonObject1.getInt("vital_unit_id"));
                                    vitalsignlists.add(new vitalsignlist(jsonObject1.getInt("id"),vitalsignname,unitname,jsonObject1.getString("max_observation"),jsonObject1.getString("min_observation"),jsonObject1.getString("observation"),jsonObject1.getString("remark")));

                                }
                                customAdapter_vitalsigns=new CustomAdapter_vitalsigns(vitalsignlists,context);
                                recyclerView.setAdapter(customAdapter_vitalsigns);
                            }
                            else
                            {
                                Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show();
                            }
                            progressDialog.dismiss();
                        }
                        catch (Exception e)
                        {
                            progressDialog.dismiss();
                            Toast.makeText(context, ""+e, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }

        });

    }


    private Request listVitalSign()
    {
        JSONObject postdata = new JSONObject();
        try {
            postdata.put("patient_id", userid);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(JSON, postdata.toString());
        return new Request.Builder()
                .addHeader("X-Api-Key", "AB5433GMDF657VBB")
                .addHeader("Content-Type", "application/json")
                .url(ApiUtils.BASE_URL+"newlistVitalSign")
                .post(body)
                .build();
    }

    private void Addingvitalsign()
    {
        OkHttpClient client = new OkHttpClient();
        Request request = AddVitalSignApp();
        Log.i("", "onClick: " + request);
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                Log.i("Activity", "onFailure: Fail");
            }

            @Override
            public void onResponse(final Response response) throws IOException {

                final String body = response.body().string();
                Log.i("1234check", "onResponse: " + body);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        try {
                            String newres = body.substring(body.lastIndexOf(">") + 1, body.lastIndexOf("}")) + "}";
                                  /*  Toast.makeText(context, ""+newres, Toast.LENGTH_SHORT).show();
                                    editText.setText(""+newres);*/
                            JSONObject jsonObject = new JSONObject(newres);

                            // Toast.makeText(context, ""+body, Toast.LENGTH_SHORT).show();
                            if (jsonObject.getInt("Success") == 1) {
                                //   Toast.makeText(context, "Success", Toast.LENGTH_SHORT).show();


                                AlertDialog.Builder builder;
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                    builder = new AlertDialog.Builder(context, android.R.style.Theme_Material_Dialog_Alert);
                                } else {
                                    builder = new AlertDialog.Builder(context);
                                }
                                builder.setTitle("Message")
                                        .setMessage(jsonObject.getString("message"))
                                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {
                                                // continue with delete
                                                finish();
                                            }
                                        })

                                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {
                                                // do nothing
                                            }
                                        })

                                        // .setIcon(android.R.drawable.m)

                                        .show();
                                builder.setCancelable(false);

                            } else {
                                Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show();
                            }
                            progressDialog.dismiss();
                        } catch (Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(context, "" + e, Toast.LENGTH_SHORT).show();
                        }
                        progressDialog.dismiss();
                    }
                });
            }

        });
    }
    private Request AddVitalSignApp()
    {
        JSONObject postdata = new JSONObject();
        try {
            postdata.put("user_id",Integer.parseInt(userid));
            postdata.put("vital_sign_list_id",Integer.parseInt(Vsign));
            postdata.put("vital_unit_id",Integer.parseInt(Vunit));
            postdata.put("max_observation",Integer.parseInt(str_maxo));
            postdata.put("min_observation",Integer.parseInt(str_mino));
            postdata.put("observation",Integer.parseInt(str_o));
            postdata.put("remark",str_remark);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(JSON, postdata.toString());
        return new Request.Builder()
                .addHeader("X-Api-Key", "AB5433GMDF657VBB")
                .addHeader("Content-Type", "application/json")
                .url(ApiUtils.BASE_URL+"AddVitalSign")
                .post(body)
                .build();
    }

    private void AddingvitalsignMDB()
    {
        OkHttpClient client = new OkHttpClient();
        Request request = AddVitalSignAppMDB();
        Log.i("", "onClick: " + request);
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                Log.i("Activity", "onFailure: Fail");
            }

            @Override
            public void onResponse(final Response response) throws IOException {

                final String body = response.body().string();
                Log.i("1234check", "onResponse: " + body);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(context, ""+body, Toast.LENGTH_SHORT).show();
     progressDialog.dismiss();
                    }
                });
            }

        });
    }
    private Request AddVitalSignAppMDB()
    {

        JSONObject postdatauser = new JSONObject();
        try {
           /* JSONObject postdatauser = new JSONObject();
            JSONObject postdatavalue = new JSONObject();
            JSONArray jsonArrayrecord = new JSONArray();
            JSONObject postdata = new JSONObject();
*/
            postdatauser.put("user_id",Integer.parseInt(userid));
            postdatauser.put("vital_sign_list_id",Integer.parseInt(Vsign));
            postdatauser.put("vital_unit_id",Integer.parseInt(Vunit));
            postdatauser.put("max_observation",Integer.parseInt(str_maxo));
            postdatauser.put("min_observation",Integer.parseInt(str_mino));
            postdatauser.put("observation",Integer.parseInt(str_o));
            postdatauser.put("remark",str_remark);

            postdatauser.put("modified_by", Prefhelper.getInstance(context).getUserid());
            postdatauser.put("modified_at",new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(new java.util.Date()));
            postdatauser.put("source_type","mobile");
            postdatauser.put("deleted_flag","0");


            /*postdata.put("VitalSign", postdatauser);

            postdatavalue.put("value", postdata);

            jsonArrayrecord.put(postdatavalue);

            postdatamain.put("record", jsonArrayrecord);
            Log.d("register data", postdatamain.toString());*/

        } catch (JSONException e) {
            e.printStackTrace();
        }
       // Log.d("medicaldata",postdatamain.toString());


        RequestBody body = RequestBody.create(JSON, postdatauser.toString());
        return new Request.Builder()
              /*  .addHeader("X-Api-Key", "AB5433GMDF657VBB")
                .addHeader("Content-Type", "application/json")*/
                .url(ApiUtils.BASE_URLMAngoDB+"add/vitalsign")
                .post(body)
                .build();
    }



    public  HashMap<Integer, String> toMap(JSONObject object) throws JSONException {
        HashMap<Integer, String> map = new HashMap();
        Iterator keys = object.keys();
        while (keys.hasNext()) {
            String key = (String) keys.next();
            vitalSignname.add(fromJson(object.get(key)).toString());
            map.put(Integer.parseInt(key), fromJson(object.get(key)).toString());
        }
        return map;
    }
    private  Object fromJson(Object json) throws JSONException {
        if (json == JSONObject.NULL) {
            return null;
        } else if (json instanceof JSONObject) {
            return toMap((JSONObject) json);
        } /*else if (json instanceof JSONArray) {
            return toList((JSONArray) json);
        }*/ else {
            return json;
        }
    }
    private boolean validateVitalsign(){
        if(str_maxo.compareTo("")==0)
        {
            Toast.makeText(getApplicationContext(),"Max Observation empty",Toast.LENGTH_LONG).show();
            return false;
        }
        else if (str_mino.compareTo("")==0)
        {
            Toast.makeText(getApplicationContext(),"Min Observation empty",Toast.LENGTH_LONG).show();
            return false;
        }
        if(str_o.compareTo("")==0)
        {
            Toast.makeText(getApplicationContext()," Observation field empty",Toast.LENGTH_LONG).show();
            return false;
        }
        else if (str_remark.compareTo("")==0)
        {
            Toast.makeText(getApplicationContext(),"Remark field empty",Toast.LENGTH_LONG).show();
            return false;
        } if(Vsign.compareTo("")==0)
        {
            Toast.makeText(getApplicationContext(),"Vital sign field empty",Toast.LENGTH_LONG).show();
            return false;
        }
        else if (Vunit.compareTo("")==0)
        {
            Toast.makeText(getApplicationContext(),"Vital unit field empty",Toast.LENGTH_LONG).show();
            return false;
        }
        else {
            return true;
        }
    }

}
