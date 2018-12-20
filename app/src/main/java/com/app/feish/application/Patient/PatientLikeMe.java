package com.app.feish.application.Patient;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.app.feish.application.Adpter.CustomAdapter_blooddonorlist;
import com.app.feish.application.Connectiondetector;
import com.app.feish.application.R;
import com.app.feish.application.Remote.ApiUtils;
import com.app.feish.application.model.bookedappointmentpatient;
import com.app.feish.application.modelclassforapi.Medicalcondition;
import com.app.feish.application.modelclassforapi.Medicalconditionlist;
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
import java.util.ArrayList;
import java.util.List;
import static com.app.feish.application.Patient.MedicalHitoryp.JSON;

public class PatientLikeMe extends AppCompatActivity {
    Toolbar toolbar;
    String str_desc="";
    ListView listView;
    CustomAdapter_blooddonorlist customAdapter_blooddonorlist;
    ArrayList<bookedappointmentpatient> searchdoctorpojos= new ArrayList<>();
    Context context=this;
    Spinner spinner_mcon;
    EditText et_desc;
    RelativeLayout relativeLayout;
    String str_mcon="";
    Medicalcondition  medicalcondition;
    List<Medicalconditionlist> medicalconditionlists;
    ProgressBar progressBar;
    TextView textView;
    Connectiondetector connectiondetector;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_like_me);
        connectiondetector= new Connectiondetector(getApplicationContext());
        initView();
        addingdata();

        relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(searchdoctorpojos.size()>0) {
                    searchdoctorpojos.clear();
                    customAdapter_blooddonorlist.notifyDataSetChanged();
                }

                progressBar.setVisibility(View.VISIBLE);
                if(connectiondetector.isConnectingToInternet())
                fetchingplmdata();
                else
                    Toast.makeText(context, "No Internet!!", Toast.LENGTH_SHORT).show();
            }
        });
        customAdapter_blooddonorlist= new CustomAdapter_blooddonorlist(context,searchdoctorpojos);
        listView.setAdapter(customAdapter_blooddonorlist);

    }
    public void initView()
    {
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setTitle("Patient Like Me");
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        listView=findViewById(R.id.list);
        FloatingActionButton fab =findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        spinner_mcon=findViewById(R.id.mcondtion_spinner);
        et_desc=findViewById(R.id.mhdesc);
        relativeLayout=findViewById(R.id.search_go);
        textView=findViewById(R.id.msg);
        progressBar=findViewById(R.id.progressBar);
    }
    private Request fetchingmedicalcondition() {
        JSONObject postdata = new JSONObject();
        RequestBody body = RequestBody.create(JSON, postdata.toString());
        return new Request.Builder()
                .addHeader("X-Api-Key", "AB5433GMDF657VBB")
                .addHeader("Content-Type", "application/json")
                .url(ApiUtils.BASE_URL+"listMedicalCondition")
                .post(body)
                .build();

    }
    public void medicalconditionJSON(String response) {
        Gson gson = new GsonBuilder().create();
        medicalcondition = gson.fromJson(response, Medicalcondition.class);
    }
    private void addingdata()
    {
        OkHttpClient client = new OkHttpClient();
        Request request = fetchingmedicalcondition();

        Log.i("", "onClick: "+request);
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                Log.i("Activity", "onFailure: Fail");
            }
            @Override
            public void onResponse(final Response response) throws IOException {

                final String body=response.body().string();
                medicalconditionJSON(body);
                Log.i("1234add", "onResponse: "+body);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        medicalconditionlists=medicalcondition.getData();
                        CustomAdapter_plm customAdapter=new CustomAdapter_plm(context,medicalconditionlists);
                        spinner_mcon.setAdapter(customAdapter);
                        spinner_mcon.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                str_mcon= medicalconditionlists.get(position).getId();
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {

                            }
                        });
                    }
                });
            }

        });

    }
    private Request paientlikeme() {
        JSONObject postdata = new JSONObject();
        try {
            postdata.put("user_id",Integer.parseInt(Prefhelper.getInstance(context).getUserid()));
            postdata.put("conditions",Integer.parseInt(str_mcon));
            postdata.put("description",str_desc);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(JSON, postdata.toString());
        return new Request.Builder()
                .addHeader("X-Api-Key", "AB5433GMDF657VBB")
                .addHeader("Content-Type", "application/json")
                .url(ApiUtils.BASE_URL+"patientsLikeMe")
                .post(body)
                .build();

    }
    private void fetchingplmdata()
    {
        OkHttpClient client = new OkHttpClient();
        Request request = paientlikeme();

        Log.i("", "onClick: "+request);
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                Log.i("Activity", "onFailure: Fail");
            }
            @Override
            public void onResponse(final Response response) throws IOException {

                final String body=response.body().string();
                Log.i("1234adddata", "onResponse: "+body);
           //     medicalconditionJSON(body);

               runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try
                        {
                            JSONObject jsonObject= new JSONObject(body);
                            if(jsonObject.getString("message").equals("success"))
                            {

                                JSONArray jsonArray=jsonObject.getJSONArray("data");
                                if(jsonArray.length()>0)
                                {
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                        JSONObject jsonObject2 = jsonObject1.getJSONObject("MedicalHistory");
                                        JSONObject jsonObject3 = jsonObject1.getJSONObject("User");
                                        searchdoctorpojos.add(new bookedappointmentpatient(jsonObject3.getString("full_name"), jsonObject2.getString("description"), jsonObject3.getString("mobile"), jsonObject3.getString("email"),1,jsonObject3.getString("id")));
                                    }
                                    customAdapter_blooddonorlist = new CustomAdapter_blooddonorlist(context, searchdoctorpojos);
                                    listView.setVisibility(View.VISIBLE);
                                    listView.setAdapter(customAdapter_blooddonorlist);
                                    progressBar.setVisibility(View.GONE);
                                }
                                else
                                {
                                    progressBar.setVisibility(View.GONE);
                                    listView.setVisibility(View.GONE);
                                    textView.setText("No Result Found");
                                    Toast.makeText(context, "No Result Found", Toast.LENGTH_SHORT).show();
                                }

                            }
                            else {
                                Toast.makeText(context, ""+jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                            }

                        }
                        catch (Exception e)
                        {
                            Toast.makeText(context, ""+e, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }

        });

    }

}
class CustomAdapter_plm extends BaseAdapter {
    Context context;
    LayoutInflater inflter;
    List<Medicalconditionlist> medicalconditionlists;
    public CustomAdapter_plm(Context applicationContext, List<Medicalconditionlist> medicalconditionlists) {
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
        names.setText(medicalconditionlists.get(i).getName());
        return view;
    }
}

