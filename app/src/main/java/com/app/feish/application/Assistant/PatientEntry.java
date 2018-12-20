package com.app.feish.application.Assistant;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.app.feish.application.Adpter.CustomAdapter_listpatientplan;
import com.app.feish.application.Connectiondetector;
import com.app.feish.application.DatabaseAdapter;
import com.app.feish.application.Patient.MedicalHitoryp;
import com.app.feish.application.Patient.PatientPurchasePlan;
import com.app.feish.application.PaymentDetail.PayUMoneyActivity;
import com.app.feish.application.R;
import com.app.feish.application.Remote.ApiUtils;
import com.app.feish.application.model.patientplanmodel;
import com.app.feish.application.modelclassforapi.Contact_verify_email;
import com.app.feish.application.modelclassforapi.DoctorEncounters;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;

import static com.app.feish.application.fragment.Addfamilyreport.JSON;

public class PatientEntry extends AppCompatActivity {
    Toolbar toolbar;
    private static final String TAG = "VerifyRegEmail" ;
    private EditText femail;
    private Button verifysubmit;
    Contact_verify_email veremailreponse;
    public static HashMap<Integer,String> speciality= new HashMap<>();
    CustomAdapter_listpatientplan customAdapter_listpatientplan;
    Context context=this;
    GridView recyclerView;
    Connectiondetector connectiondetector;
    ArrayList<patientplanmodel> drplanmodels= new ArrayList<>();
    private ProgressBar pb;
    TextView tv_appodate,tv_appotime,tv_appoid,tv_pis,tv_pname,tv_ploc,tv_feedetail;
    LinearLayout findpn;
    String PAymentmode="";
    String tokenid="";
    TextView tv_drname,tv_drnumber;
    CardView cardView , cardView_cashpay,cardView_cardpay,cardView_cardpackageplan;
    TextView textView_entrytimetxt;
    DoctorEncounters  appointmentdatum;
    String EntryTime="";
    Button button_btn_entry;
    int sessionforentry=0;
    TextView textView_token;
    int assiscode;
    String fee="";
    DatabaseAdapter  databaseAdapter;
    LinearLayout linearLayout_payment;
    String str_appoid="";
    String service_id="",doctor_id="";

    private void  initView()
    {
        tv_drname=findViewById(R.id.drname);
        tv_drnumber=findViewById(R.id.sernumber);
        tv_appodate=findViewById(R.id.appodate);
        tv_appotime=findViewById(R.id.appotime);
        tv_appoid=findViewById(R.id.appoid);
        tv_feedetail=findViewById(R.id.feedetail);
        tv_pis=findViewById(R.id.patientid);
        tv_ploc=findViewById(R.id.patientadd);
        tv_pname=findViewById(R.id.patientname);
        femail=findViewById(R.id.verifyemail);
        //  femail.setText(Calendar.getInstance().getTime().toString());
        toolbar =  findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        getSupportActionBar().setTitle("Patient Entry");
        femail=findViewById(R.id.verifyemail);
        textView_entrytimetxt=findViewById(R.id.entrytimetxt);
        button_btn_entry=findViewById(R.id.btn_entry);
        verifysubmit=findViewById(R.id.verifysubmit);
        pb=findViewById(R.id.verifyemailprogress);
        findpn=findViewById(R.id.fappid);
        cardView=findViewById(R.id.pedetail);
        textView_token=findViewById(R.id.token);
        linearLayout_payment=findViewById(R.id.paymentLayout);
        cardView_cashpay=findViewById(R.id.cashpay);
        cardView_cardpay=findViewById(R.id.cardpay);
        cardView_cardpackageplan=findViewById(R.id.cardpackageplan);

        assiscode=getIntent().getIntExtra("assiscode",0);
        if(assiscode==1) {
            femail.setText(getIntent().getStringExtra("appoid"));
            fee=AssistantDashbord.docfee;
        }
        else
        {
            fee=getIntent().getStringExtra("fee");
            service_id=getIntent().getStringExtra("service_id");
            doctor_id=getIntent().getStringExtra("doctor_id");
              }
        tv_feedetail.setText("Rs "+fee+" /- Session");
     //   Toast.makeText(context, ""+fee, Toast.LENGTH_SHORT).show();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_entry);
        initView();

        ///////////////////////////database////////////////////////////
       /* databaseAdapter= new DatabaseAdapter(context);
       if(databaseAdapter.insertData("758","Integer.parseInt(appointmentdatum.getAppointmentdata().get(0).getAppointment().getId())","Wed Jul 11 15:14:34 GMT+05:30 2018","Wed Jul 11 16:00:34 GMT+05:30 2018","Wed Jul 11 15:30:34 GMT+05:30 2018","Wed Jul 11 15:45:34 GMT+05:30 2018","11/07/2018")>0)
       {
           Toast.makeText(context, "insert", Toast.LENGTH_SHORT).show();
       }
       else
       {
           Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show();
       }*/
       /* if(!Prefhelper.getInstance(context).getPatientpaymentmode().equals(""))
        {
            textView_token.setText("Fee accept in"+ Prefhelper.getInstance(context).getPatientpaymentmode() +"Mode . Token No is : Dr00768");
        }
        if(Prefhelper.getInstance(context).getPatientsessionre()==1)
        {
            sessionforentry=1;
            button_btn_entry.setBackgroundColor(ContextCompat.getColor(context,R.color.red));
            button_btn_entry.setText("Exit");
            findpn.setVisibility(View.GONE);
            cardView.setVisibility(View.VISIBLE);
            textView_entrytimetxt.setText(Prefhelper.getInstance(context).getPatiententrytimere());
        }
*/
        verifysubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                str_appoid=femail.getText().toString();
                if(str_appoid.equals(""))
                {
                    Toast.makeText(context, "Enter Appointment Id", Toast.LENGTH_SHORT).show();
                }
                else
                {
                 fetchdata(str_appoid);
                }

            }
        });


        button_btn_entry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(sessionforentry==0)
                {
                    linearLayout_payment.setVisibility(View.VISIBLE);
                    sessionforentry =1;
                    EntryTime=new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(new java.util.Date());
                    textView_entrytimetxt.setText(EntryTime);
                    appointmentdatum.getAppointmentdata().get(0).getAppointment().setPatient_arrival_time(EntryTime);
                    button_btn_entry.setBackgroundColor(ContextCompat.getColor(context, R.color.red));
                    button_btn_entry.setText("New Entry");
                    Prefhelper.getInstance(context).setPatientsessiondataatrecep(Integer.parseInt(appointmentdatum.getAppointmentdata().get(0).getAppointment().getId()),1,EntryTime);

                }
                else
                {
                    if(PAymentmode.equals(""))
                    {
                        Toast.makeText(context, "Choose payment mode", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Prefhelper.getInstance(context).setPatientsessiondataatrecep(0, 0, "");
                        Prefhelper.getInstance(context).paymentseesion("", "", Integer.parseInt(appointmentdatum.getAppointmentdata().get(0).getAppointment().getId()));
                        appointmentdatum.getAppointmentdata().get(0).getAppointment().setPatient_exit_time(new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(new java.util.Date()));
                        update(PAymentmode, 1);
                    }
                }

            }
        });
        cardView_cardpay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tokenid=String.valueOf(appointmentdatum.getAppointmentdata().get(0).getService().getId())+""+String.valueOf(appointmentdatum.getAppointmentdata().get(0).getAppointment().getId());
          appointmentdatum.getAppointmentdata().get(0).getAppointment().setToken_id(tokenid);
                Prefhelper.getInstance(context).paymentseesion(tokenid,"Card",Integer.parseInt(appointmentdatum.getAppointmentdata().get(0).getAppointment().getId()));
                textView_token.setText("Fee accept via online payment  . Token No is :"+tokenid );
                linearLayout_payment.setVisibility(View.GONE);
                PAymentmode="Card";
                Intent intent= new Intent(PatientEntry.this, PayUMoneyActivity.class);
                intent.putExtra("data",appointmentdatum);
                intent.putExtra("fee",fee);
                startActivity(intent);
              //  update("Card",0);
            }
        });

        cardView_cashpay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tokenid=String.valueOf(appointmentdatum.getAppointmentdata().get(0).getService().getId())+""+String.valueOf(appointmentdatum.getAppointmentdata().get(0).getAppointment().getId());
                appointmentdatum.getAppointmentdata().get(0).getAppointment().setToken_id(tokenid);
                Prefhelper.getInstance(context).paymentseesion("Dr00769","Cash",Integer.parseInt(appointmentdatum.getAppointmentdata().get(0).getAppointment().getId()));
                textView_token.setText("Fee accept in cash . Token No is : "+tokenid);
                linearLayout_payment.setVisibility(View.GONE);
                PAymentmode="Cash";
                update("Cash",0);
            }
        });
        cardView_cardpackageplan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tokenid=String.valueOf(appointmentdatum.getAppointmentdata().get(0).getService().getId())+""+String.valueOf(appointmentdatum.getAppointmentdata().get(0).getAppointment().getId());
                appointmentdatum.getAppointmentdata().get(0).getAppointment().setToken_id(tokenid);
                Prefhelper.getInstance(context).paymentseesion("Dr00769","Cash",Integer.parseInt(appointmentdatum.getAppointmentdata().get(0).getAppointment().getId()));
                textView_token.setText("Fee accept by Plan Pakage . Token No is : "+tokenid);
                linearLayout_payment.setVisibility(View.GONE);
                PAymentmode="Plan Package";
            //    update("Plan Package",0);
                Dialogplan();
            }
        });

    }
    private void Dialogplan()
    {
        final Dialog dialog = new Dialog(context,android.R.style.Theme_Light);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.activity_patient_purchase_plan);
        dialog.setCancelable(false);

         recyclerView =  dialog.findViewById(R.id.recycler_view);
         recyclerView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
             @Override
             public void onItemClick(AdapterView<?> parent, View view, int position, long id)
             {
                 if(Integer.parseInt(drplanmodels.get(position).getPlanusedvisit())==Integer.parseInt(drplanmodels.get(position).getPlantotalvisit()))
                 {
                     Toast.makeText(context, "plan Expire", Toast.LENGTH_SHORT).show();
                 }
                 else {
                     int used = Integer.parseInt(drplanmodels.get(position).getPlanusedvisit()) + 1;
                     applypatientpakage(drplanmodels.get(position).getId(), used,dialog);
                 }


             }
         });
      fetchdata(dialog);
    }

    private void applypatientpakage(int id, int noofused, final Dialog dialog)
    {
        OkHttpClient client = new OkHttpClient();
        Request validation_request = apply(id,noofused);
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
                                  public void run()
                                  {
                                      try {
                                          JSONObject jsonObject= new JSONObject(body);
                                          if(jsonObject.getBoolean("status"))
                                          {
                                              update("Plan Package",0);
                                              dialog.dismiss();
                                          }
                                          else
                                          {
                                              Toast.makeText(context, "No Record found", Toast.LENGTH_SHORT).show();

                                          }
                                      }
                                      catch (JSONException e)
                                      {
                                          Toast.makeText(PatientEntry.this, "" + e, Toast.LENGTH_SHORT).show();
                                      }
                                  }
                              }
                );
            }
        });

    }

    private Request apply(int id,int noofused) {
        JSONObject postdata = new JSONObject();
        try {
            postdata.put("id",id);
            postdata.put("nooftimeused",noofused);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(JSON, postdata.toString());
        return new Request.Builder()
                .addHeader("X-Api-Key", "AB5433GMDF657VBB")
                .addHeader("Content-Type", "application/json")
                .url(ApiUtils.BASE_URL+"Updatepatientpackagerecord")
                .post(body)
                .build();
    }


    private void fetchdata(final  Dialog dialogmain)
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
                                  public void run()
                                  {
                                      try {
                                          JSONObject jsonObject= new JSONObject(body);
                                          if(jsonObject.getBoolean("status"))
                                          {
                                              JSONArray jsonArray = jsonObject.getJSONArray("data");
                                              JSONArray jsonArraySpecialtyList = jsonObject.getJSONArray("SpecialtyList");
                                              JSONArray jsonArraydoctordata = jsonObject.getJSONArray("doctordata");
                                              JSONArray jsonArraydoctordataspe = jsonObject.getJSONArray("doctorapedata");
                                              for (int k = 0; k <jsonArraySpecialtyList.length() ; k++)
                                              {
                                                  JSONObject  jsonObject1=jsonArraySpecialtyList.getJSONObject(k);
                                                  speciality.put(jsonObject1.getInt("id"),jsonObject1.getString("specialty_name"));

                                              }
                                              for (int i = 0; i < jsonArray.length(); i++)
                                              {

                                                  JSONObject jsonobject = jsonArray.getJSONObject(i);
                                                  JSONArray jsonobjectdd = jsonArraydoctordata.getJSONArray(i);
                                                  JSONArray jsonobjectdsd = jsonArraydoctordataspe.getJSONArray(i);
                                                  int id = jsonobject.getInt("id");
                                                  String name = jsonobject.getString("name");
                                                  //  String planid = jsonobject.getString("plan_details");
                                                  String planid = jsonobject.getString("planid");
                                                  String doctor_id = jsonobject.getString("doctor_id");
                                                  String totalvisit = jsonobject.getString("valid_visits");
                                                  String usedvisit = jsonobject.getString("nooftimeused");
                                                  String remainingvisit = String.valueOf((Integer.parseInt(totalvisit) - Integer.parseInt(usedvisit)));
                                                  String price = jsonobject.getString("price");

                                                  //String percentage_per_visit = jsonobject.getString("percentage_per_visit");
                                                  // String validity = jsonobject.getString("validity");
                                                  drplanmodels.add(new patientplanmodel(id, name, price, remainingvisit, totalvisit, usedvisit, planid, doctor_id,jsonobjectdd,jsonobjectdsd));


                                              }
                                              customAdapter_listpatientplan = new CustomAdapter_listpatientplan(drplanmodels, context, 0);
                                              recyclerView.setAdapter(customAdapter_listpatientplan);
                                              dialogmain.show();
                                          }
                                          else
                                          {
                                              Toast.makeText(context, "No Record found", Toast.LENGTH_SHORT).show();

                                          }
                                      }
                                      catch (JSONException e)
                                      {
                                          Toast.makeText(PatientEntry.this, "" + e, Toast.LENGTH_SHORT).show();
                                      }
                                  }
                              }
                );
            }
        });

    }

    private Request listservices() {
        JSONObject postdata = new JSONObject();
        try {
            postdata.put("user_id",appointmentdatum.getAppointmentdata().get(0).getUser().getId());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(JSON, postdata.toString());
        return new Request.Builder()
                .addHeader("X-Api-Key", "AB5433GMDF657VBB")
                .addHeader("Content-Type", "application/json")
                .url(ApiUtils.BASE_URL+"fetchpatientpackages")
                .post(body)
                .build();
    }
    private Request fetchappodata(String id) {
        JSONObject postdata = new JSONObject();
        try{
            postdata.put("id",id);
            postdata.put("doctor_id",doctor_id);
            postdata.put("service_id",service_id);
        }
        catch (JSONException e)
        {
            Toast.makeText(context, ""+e, Toast.LENGTH_SHORT).show();
        }
        RequestBody body = RequestBody.create(MedicalHitoryp.JSON, postdata.toString());
        return new Request.Builder()
                .addHeader("X-Api-Key", "AB5433GMDF657VBB")
                .addHeader("Content-Type", "application/json")
                .url(ApiUtils.BASE_URL+"fetchappointmentdetail")
                .post(body)
                .build();

    }
    private void fetchdata(String id)
    {
        OkHttpClient client = new OkHttpClient();
        Request request = fetchappodata(id);

        Log.i("", "onClick: "+request);
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                Log.i("Activity", "onFailure: Fail");
            }
            @Override
            public void onResponse(final Response response) throws IOException {

                final String body=response.body().string();
               //
                Log.i("1234add", "onResponse: "+body);
               runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                    //    Toast.makeText(context, ""+body, Toast.LENGTH_SHORT).show();
                        try {
                               JSONObject jsonObject= new JSONObject(body);

                           if(jsonObject.getBoolean("status")) {
                               medicalconditionJSON(body);
                               if (appointmentdatum.getStatus() && !appointmentdatum.getAppointmentdata().get(0).getAppointment().getIsVisited().equals("1")) {
                                   findpn.setVisibility(View.GONE);
                                   cardView.setVisibility(View.VISIBLE);
                                   String dattime[] = appointmentdatum.getAppointmentdata().get(0).getAppointment().getAppointedTiming().split(" ");
                                   tv_appodate.setText("Appo Date: " + dattime[0]);
                                   tv_appotime.setText("Appo Time: " + dattime[1]);
                                   tv_pis.setText("Patient ID: " + appointmentdatum.getAppointmentdata().get(0).getUser().getId());
                                   tv_pname.setText("Patient Name: " + appointmentdatum.getAppointmentdata().get(0).getUser().getFirstName() + " " + appointmentdatum.getAppointmentdata().get(0).getUser().getLastName());
                                   tv_ploc.setText("Patient  Address: " + appointmentdatum.getAppointmentdata().get(0).getUser().getAddress());
                                   tv_appoid.setText("Address: " + appointmentdatum.getAppointmentdata().get(0).getAppointment().getId());
                                   tv_drname.setText("Dr " + appointmentdatum.getAppointmentdata().get(0).getDoctor().getFirstName() + " " + appointmentdatum.getAppointmentdata().get(0).getDoctor().getLastName());
                                   tv_drnumber.setText(appointmentdatum.getAppointmentdata().get(0).getDoctor().getMobile());
                               } else if (appointmentdatum.getStatus()) {
                                   Toast.makeText(context, "Already Visited", Toast.LENGTH_SHORT).show();
                               }
                           }
                            else
                            {
                                Toast.makeText(context, "Something Went Wrong,No Record", Toast.LENGTH_SHORT).show();
                            }

                        } catch (Exception e) {
                            Toast.makeText(context, ""+e, Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                        }
                    }
                });
            }

        });

    }
    public void medicalconditionJSON(String response) {
        Gson gson = new GsonBuilder().create();
        appointmentdatum = gson.fromJson(response, DoctorEncounters.class);
    }


    private Request updateappodetail(String paymentmpde) {
        JSONObject postdata = new JSONObject();
        try{
            postdata.put("id",appointmentdatum.getAppointmentdata().get(0).getAppointment().getId());
            postdata.put("appointed_timing",appointmentdatum.getAppointmentdata().get(0).getAppointment().getAppointedTiming());
            postdata.put("user_id",appointmentdatum.getAppointmentdata().get(0).getUser().getId());
            postdata.put("doctor_id",appointmentdatum.getAppointmentdata().get(0).getDoctor().getId());
            postdata.put("service_id",appointmentdatum.getAppointmentdata().get(0).getService().getId());
            postdata.put("scheduled_date",appointmentdatum.getAppointmentdata().get(0).getAppointment().getScheduledDate());
            postdata.put("patient_arrival_time",appointmentdatum.getAppointmentdata().get(0).getAppointment().getPatient_arrival_time());
            postdata.put("patient_in_time",appointmentdatum.getAppointmentdata().get(0).getAppointment().getPatient_in_time());
            postdata.put("patient_out_time",appointmentdatum.getAppointmentdata().get(0).getAppointment().getPatient_out_time());
            postdata.put("patient_exit_time",appointmentdatum.getAppointmentdata().get(0).getAppointment().getPatient_exit_time());
            postdata.put("token_id",appointmentdatum.getAppointmentdata().get(0).getAppointment().getToken_id());
            postdata.put("paymentmode",paymentmpde);
            postdata.put("amount",Integer.parseInt(fee));
            postdata.put("tranid","");
            postdata.put("collected_by",Prefhelper.getInstance(context).getUserid());
        }
        catch (JSONException e)
        {
            Toast.makeText(context, ""+e, Toast.LENGTH_SHORT).show();
        }
        RequestBody body = RequestBody.create(MedicalHitoryp.JSON, postdata.toString());
        return new Request.Builder()
                .addHeader("X-Api-Key", "AB5433GMDF657VBB")
                .addHeader("Content-Type", "application/json")
                .url(ApiUtils.BASE_URL+"updateappointmentdetail")
                .post(body)
                .build();

    }
    private void update(String paymentmode, final int code)
    {
        OkHttpClient client = new OkHttpClient();
        Request request = updateappodetail(paymentmode);

        Log.i("", "onClick: "+request);
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                Log.i("Activity", "onFailure: Fail");
            }
            @Override
            public void onResponse(final Response response) throws IOException {

                final String body=response.body().string();
                Log.i("1234add", "onResponse: "+body);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(context, ""+body, Toast.LENGTH_SHORT).show();
                        try {
                            JSONObject  jsonObject= new JSONObject(body);

                            if(jsonObject.getBoolean("status"))
                            {
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
                                                if(code==1)
                                                    finish();
                                                    else
                                               dialog.dismiss();
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
                            }

                        } catch (JSONException e) {
                            Toast.makeText(context, ""+e, Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                        }
                    }
                });
            }

        });

    }

}
