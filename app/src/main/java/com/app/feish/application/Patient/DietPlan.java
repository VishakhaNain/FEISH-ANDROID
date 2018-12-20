package com.app.feish.application.Patient;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;
import com.app.feish.application.Adpter.CustomAdapter_dietplan;
import com.app.feish.application.Connectiondetector;
import com.app.feish.application.R;
import com.app.feish.application.model.dietlistpojo;
import com.app.feish.application.model.dietplanfulldetailpojo;
import com.app.feish.application.sessiondata.Prefhelper;
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
import static com.app.feish.application.fragment.ListFamilyhistory.JSON;

public class DietPlan extends AppCompatActivity {
    Toolbar toolbar;
    ListView listView;
    CustomAdapter_dietplan customAdapter_dietplan;
    Context context=this;
    FloatingActionButton floatingActionButton;
    ProgressBar progressBar;
    String recipename="",recipecalories="";

    ArrayList<dietplanfulldetailpojo>  dietplanfulldetailpojos= new ArrayList<>();
    Connectiondetector connectiondetector;
    String userid="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diet_plan);
        progressBar=findViewById(R.id.progressdialog);
        initView();
        Clickviews();

        if(getIntent()!=null)
        {
            userid=getIntent().getStringExtra("userid");
        }
        else
        {
            userid=Prefhelper.getInstance(context).getUserid();
        }
        connectiondetector= new Connectiondetector(getApplicationContext());
        if(connectiondetector.isConnectingToInternet()) {
            progressBar.setVisibility(View.VISIBLE);
            fetchdata();
        }
        else
            Toast.makeText(context, "No Internet!", Toast.LENGTH_LONG).show();

    }

    private Request AddDietplan()
    {
        JSONObject postdata = new JSONObject();
        try {
            // postdata.put("patient_id",Integer.parseInt(userid));
            postdata.put("patient_id",userid);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(JSON, postdata.toString());
        return new Request.Builder()
                .addHeader("X-Api-Key", "AB5433GMDF657VBB")
                .addHeader("Content-Type", "application/")
                .url("http://feish.online/apis/listDietPlan")
                .post(body)
                .build();
    }
    private void fetchdata()
    {
        OkHttpClient client = new OkHttpClient();
        Request request = AddDietplan();
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

                        progressBar.setVisibility(View.GONE);
                        try {
                            //      editText.setText(body);
                            JSONObject jsonObject = new JSONObject(body);
                            if (jsonObject.getString("message").equals("success"))
                            {
                                JSONArray jsonArray=jsonObject.getJSONArray("data");
                                for (int i = 0; i <jsonArray.length() ; i++)
                                {
                                    JSONObject jsonObject1=jsonArray.getJSONObject(i);
                                    JSONObject jsonObject2=jsonObject1.getJSONObject("DietPlan");
                                    JSONArray jsonObject3=jsonObject1.getJSONArray("DietPlansDetail");
                                    JSONArray jsonObject_recipename=jsonObject1.getJSONArray("recipe_name");
                                    ArrayList<dietlistpojo>  dietlistpojos= new ArrayList<>();
                                    for (int j = 0; j <jsonObject3.length() ; j++)
                                    {
                                        JSONObject  jsonObject4=jsonObject3.getJSONObject(j);
                                        JSONObject jsonObject5=jsonObject4.getJSONObject("DietPlansDetail");
                                        if(jsonObject5.getInt("recipe_code")!=0) {

                                            //  for (int k = 0; k <jsonObject_recipename.length() ; k++) {
                                            JSONArray jsonElements= jsonObject_recipename.getJSONArray(j);
                                            //   Toast.makeText(context, ""+jsonElements.getJSONObject(0), Toast.LENGTH_SHORT).show();
                                            if(jsonElements.length()>0) {
                                                JSONObject recipemainname = jsonElements.getJSONObject(0);
                                                recipename = recipemainname.getString("recipe_name");
                                                recipecalories = recipemainname.getString("recipe_calories");
                                            }
                                            else
                                            {
                                                recipename="recipe_name";
                                                recipecalories="calories";
                                            }

                                            //    }
                                            dietlistpojos.add(new dietlistpojo(jsonObject5.getString("weekday"), jsonObject5.getString("time"), jsonObject5.getString("description"), recipename, recipecalories, jsonObject5.getString("food_type"), String.valueOf(jsonObject5.getInt("recipe_code")), jsonObject5.getInt("id")));
                                        }
                                        else
                                            dietlistpojos.add(new dietlistpojo(jsonObject5.getString("weekday"),jsonObject5.getString("time"),jsonObject5.getString("description"),"","","","",jsonObject5.getInt("id")));

                                    }
                                    dietplanfulldetailpojos.add(new dietplanfulldetailpojo(jsonObject2.getInt("id"),jsonObject2.optString("plan_name"),jsonObject2.optString("start_date"),jsonObject2.optString("end_date"),dietlistpojos));


                                }
                                customAdapter_dietplan= new CustomAdapter_dietplan(context,dietplanfulldetailpojos,Integer.parseInt(Prefhelper.getInstance(context).getUserid()));
                                listView.setAdapter(customAdapter_dietplan);

                            }
                            else
                            {
                                Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception e) {
                            Toast.makeText(context, "" + e, Toast.LENGTH_SHORT).show();
                        }

                    }
                });
            }

        });

    }

    private void   initView()
    {
        toolbar =findViewById(R.id.toolbar);
        floatingActionButton =  findViewById(R.id.fab);
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setTitle("Diet Plan");
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        listView=findViewById(R.id.list);
    }
    private  void Clickviews()
    {
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DietPlan.this,Createdietplan.class));
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent= new Intent(DietPlan.this,Createdietplan.class);
                intent.putExtra("update",1);
                intent.putExtra("diet_id",dietplanfulldetailpojos.get(position).getId());
                intent.putExtra("title",dietplanfulldetailpojos.get(position).getPlanname());
                intent.putExtra("sdate",dietplanfulldetailpojos.get(position).getSdate());
                intent.putExtra("edate",dietplanfulldetailpojos.get(position).getEdate());
                Bundle args = new Bundle();
                args.putSerializable("list",dietplanfulldetailpojos.get(position).getDietlistpojos());
                intent.putExtra("BUNDLE",args);
                startActivity(intent);
            }
        });

    }

}













