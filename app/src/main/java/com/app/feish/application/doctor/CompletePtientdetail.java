package com.app.feish.application.doctor;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.app.feish.application.Connectiondetector;
import com.app.feish.application.Patient.MedicalHitoryp;
import com.app.feish.application.Prede;
import com.app.feish.application.R;
import com.app.feish.application.Remote.ApiUtils;
import com.app.feish.application.Remote.EncryptionDecryption;
import com.app.feish.application.fragment.AddPlan;
import com.app.feish.application.fragment.ListFamilyhistory;
import com.app.feish.application.model.vitalsignlist;
import com.app.feish.application.modelclassforapi.Appointmentdatum;
import com.app.feish.application.sessiondata.Prefhelper;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;
import com.williamww.silkysignature.views.SignaturePad;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import static com.app.feish.application.doctor.SetupProfileForDoctor.JSON;

public class CompletePtientdetail extends AppCompatActivity
{
    Toolbar toolbar;
    ImageView imageView_cmd,imageView_obse;
    Button button,button_btn_pd;
    EditText editText_observation,editText_comment;
    String str_observation="",str_comment="",str_diease="",str_dignosis="";
    ImageView imageView_di_list;
    SignaturePad mSignaturePad;
    int flag=0;
    JSONArray jsonArray;
    Dialog dialog_sign,dialog_diease;
    ImageView imageView_close,imageView_delete,imageView_clicksign;
    ImageView img_finalsignimgfirst,mainimage;
    Connectiondetector connectiondetector;
    ListView listView;
    ArrayList<String> diseaseswithprescription= new ArrayList<>();
    ArrayList<Integer> postionpharmacy= new ArrayList<>();
    ArrayList<String> pharmacyname= new ArrayList<>();
    TextView tv_drname,tv_drsp,tv_labname,tv_phname,textView_t6,textView_txtpintime,tv_cmt;
    List<String> searchlist= new ArrayList<>();
    List<String> searchlisttemp= new ArrayList<>();
    Bitmap signedimagebitmap;
    EditText editText,editText_dignosis;
    String tempname="";
    int pharmacy_id=0;
    int sessionon=0;
    Context context=this;
    Appointmentdatum appointmentdatum;
    int code=0;
    Dialog  dialog2,dialog;
    CardView cardView;
    CheckBox checkBox_isrefer,checkBox_sendlab,checkBox_sendpharmacy;
    ArrayAdapter<String> stringArrayAdapter;
    TextView textView_name,textView_gender,textView_id,textView_add;
    EditText editText_dia;
    ArrayList<vitalsignlist> vitalsignlists= new ArrayList<>();
    ArrayList<vitalsignlist> vitalsignlistsfordatabase= new ArrayList<>();
    AlertDialog alertDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complete_ptientdetail);
        initView();
        searchdi();
        listenermenthod();

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 8&& resultCode == RESULT_OK) {
            vitalsignlist vitalsignlist =(vitalsignlist)data.getSerializableExtra("precriptiodetail1");
            int pos=data.getIntExtra("pos",0);
            vitalsignlistsfordatabase.set(pos,vitalsignlist);
            diseaseswithprescription.set(pos,vitalsignlist.getRelation()+"  "+vitalsignlist.getName());
        }

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater= getMenuInflater();
        menuInflater.inflate(R.menu.menusort,menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId()==R.id.soapformat)
        {
            open();
        }
        return true;
    }
    @Override
    public void onBackPressed() {
        if(sessionon==1)
        {
            openalertdialog2();
        }
        else
        {
            super.onBackPressed();
        }
    }


    private Request listpre() {
        JSONObject postdata = new JSONObject();
        try {
            postdata.put("user_id",Integer.parseInt(Prefhelper.getInstance(context).getUserid()));

        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(JSON, postdata.toString());
        return new Request.Builder()
                .addHeader("X-Api-Key", "AB5433GMDF657VBB")
                .addHeader("Content-Type", "application/json")
                .url(ApiUtils.BASE_URL+"listprescrption")
                .post(body)
                .build();

    }
    private void listingdata()
    {
        OkHttpClient client = new OkHttpClient();
        Request request = listpre();
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
                        try
                        {
                            JSONObject jsonObject=new JSONObject(body);
                            if(jsonObject.getString("message").equals("success"))
                            {
                                JSONArray jsonArray = jsonObject.optJSONArray("data");
                                for (int i = 0; i <jsonArray.length() ; i++) {
                                    JSONObject jsonObject1=jsonArray.getJSONObject(i);
                                    vitalsignlists.add(new vitalsignlist(jsonObject1.getString("pre_name"), jsonObject1.getString("med_name"), jsonObject1.getString("des_name"), jsonObject1.getString("unit_dose"), jsonObject1.getString("total_dose"), jsonObject1.getString("ingestiontime"), jsonObject1.getString("power"),jsonObject1.getString("advise"),jsonObject1.getString("med_type"),jsonObject1.getString("med_time")));
                                    searchlist.add(jsonObject1.getString("des_name"));
                                    searchlisttemp.add(jsonObject1.getString("des_name"));
                                }
                                stringArrayAdapter= new ArrayAdapter<>(context,android.R.layout.simple_list_item_1,searchlist);
                                listView.setAdapter(stringArrayAdapter);
                            }
                            else
                            {
                                Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show();
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
    public void openalertdialog(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle("Patient Seession");
        alertDialogBuilder.setMessage("Is The Patient In?");
        alertDialogBuilder.setPositiveButton("Yes",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        sessionon=1;
                        String time=new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(new java.util.Date());
                        textView_txtpintime.setText(Calendar.getInstance().getTime().toString());
                        Prefhelper.getInstance(context).setPatientsessiondata(Integer.parseInt(appointmentdatum.getAppointment().getId()),1,time);
                       appointmentdatum.getAppointment().setPatient_in_time(time);
                       update(0);
                        alertDialog.dismiss();
                    }
                });

        alertDialogBuilder.setNegativeButton("No",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                sessionon=0;
                Prefhelper.getInstance(context).setPatientsessiondata(0,0,"");
                alertDialog.dismiss();
            }
        });

        alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }
    private Request updateappodetail() {
        JSONObject postdata = new JSONObject();


        try{
            postdata.put("id",appointmentdatum.getAppointment().getId());
            postdata.put("appointed_timing",appointmentdatum.getAppointment().getAppointedTiming());
            postdata.put("user_id",appointmentdatum.getUser().getId());
            postdata.put("doctor_id",appointmentdatum.getDoctor().getId());
            postdata.put("service_id",appointmentdatum.getService().getId());
            postdata.put("scheduled_date",appointmentdatum.getAppointment().getScheduledDate());
            postdata.put("patient_arrival_time",appointmentdatum.getAppointment().getPatient_arrival_time());
            postdata.put("patient_in_time",appointmentdatum.getAppointment().getPatient_in_time());
            postdata.put("patient_out_time",appointmentdatum.getAppointment().getPatient_out_time());
            postdata.put("patient_exit_time",appointmentdatum.getAppointment().getPatient_exit_time());
            postdata.put("token_id",appointmentdatum.getAppointment().getToken_id());

        }
        catch (JSONException e)
        {
            Toast.makeText(context, ""+e, Toast.LENGTH_SHORT).show();
        }
        RequestBody body = RequestBody.create(MedicalHitoryp.JSON, postdata.toString());
        return new Request.Builder()
                .addHeader("X-Api-Key", "AB5433GMDF657VBB")
                .addHeader("Content-Type", "application/json")
                .url(ApiUtils.BASE_URL+"updatedatafromdoctor")
                .post(body)
                .build();

    }
    private void update( final int codetofinish)
    {
        OkHttpClient client = new OkHttpClient();
        Request request = updateappodetail();

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
                        finish();
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
                                                if(codetofinish==1)
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
    public void openalertdialog2(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle("Patient Seession");
        alertDialogBuilder.setMessage("Is The Patient Session Complete?");
        alertDialogBuilder.setPositiveButton("Yes",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        textView_txtpintime.setText(Calendar.getInstance().getTime().toString());
                        Prefhelper.getInstance(context).setPatientsessiondata(0,0,"");
                        String time=new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(new java.util.Date());

                        alertDialog.dismiss();
                        appointmentdatum.getAppointment().setPatient_out_time(time);
                        update(1);


                    }
                });

        alertDialogBuilder.setNegativeButton("No",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });

        alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }
    private void sign()
    {
        dialog_sign = new Dialog(context,android.R.style.Theme_Light);
        if(dialog_sign.getWindow()!=null)
            dialog_sign.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        // Include dialog.xml file
        dialog_sign.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog_sign.setContentView(R.layout.digitalsignxml);
        imageView_close=dialog_sign.findViewById(R.id.close);
        imageView_delete=dialog_sign.findViewById(R.id.deletesign);
        imageView_clicksign=dialog_sign.findViewById(R.id.checksign);
        mSignaturePad=dialog_sign.findViewById(R.id.signature_pad);
        img_finalsignimgfirst=dialog_sign.findViewById(R.id.finalsignimgfirst);
        imageView_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mSignaturePad.clear();
            }
        });

        imageView_clicksign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mainimage.setVisibility(View.VISIBLE);
                mainimage.setImageBitmap(signedimagebitmap);
                mSignaturePad.clear();
                dialog_sign.dismiss();
            }
        });

        mSignaturePad.setOnSignedListener(new SignaturePad.OnSignedListener() {
            @Override
            public void onStartSigning() {}
            @Override
            public void onSigned()
            {
                imageView_clicksign.setVisibility(View.VISIBLE);
                imageView_delete.setVisibility(View.VISIBLE);
                signedimagebitmap=mSignaturePad.getSignatureBitmap();
            }
            @Override
            public void onClear() {
//Event triggered when the pad is cleared
            }
        });
        imageView_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog_sign.dismiss();
            }
        });

    }
    public void open()
    {
        dialog= new Dialog(context,android.R.style.Theme_Light);
        if(dialog.getWindow()!=null)
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.soapnotetemplate);
        dialog.setCanceledOnTouchOutside(false);
        cardView=dialog.findViewById(R.id.cardView);
        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();

    }
    private void listenermenthod()
    {
        connectiondetector= new Connectiondetector(context);
        if(connectiondetector.isConnectingToInternet()) {
            listingdata();
            fetchpharmacydata();
        }
        else
            Toast.makeText(context, "No Internet!!", Toast.LENGTH_SHORT).show();

        imageView_di_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog_diease.show();
            }
        });


        editText_dignosis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //    startActivity(new Intent(CompletePtientdetail.this, Prede.class));
                CharSequence[]   dialogList = diseaseswithprescription.toArray(new CharSequence[diseaseswithprescription.size()]);


                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Dignosis List");
                builder.setItems(dialogList, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int item) {
                        // Do something with the selection items[item]
                        Intent intent= new Intent(CompletePtientdetail.this, Prede.class);
                        intent.putExtra("precriptiodetail", vitalsignlistsfordatabase.get(item));
                        intent.putExtra("postion", item);
                        startActivityForResult(intent,8);
                    }
                });
                AlertDialog alert = builder.create();
                alert.show();
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                if(sessionon==0)
                {
                    Toast.makeText(context, "Patient is Not In", Toast.LENGTH_SHORT).show();
                }
                else {
                    if (flag == 1) {
                        Toast.makeText(context, "Alredy saved", Toast.LENGTH_SHORT).show();
                    } else {

                        str_observation = editText_observation.getText().toString();
                        str_comment = editText_comment.getText().toString();
                        if (validateVitalsign()) {
                   /* if( vitalsignlistsfordatabase.get(i).getRelation().equals("")|| vitalsignlistsfordatabase.get(i).getAge().equals("")||vitalsignlistsfordatabase.get(i).getDisease().equals("")||
                            vitalsignlistsfordatabase.get(i).getStatus().equals("")|| vitalsignlistsfordatabase.get(i).getAdvise().equals("")) { */
                            try {
                                jsonArray = new JSONArray();
                                for (int i = 0; i < vitalsignlistsfordatabase.size(); i++) {
                                    if (str_diease.equals("")) {
                                        str_diease = vitalsignlistsfordatabase.get(i).getRelation();
                                    } else {
                                        str_diease = str_diease + " , " + vitalsignlistsfordatabase.get(i).getRelation();
                                    }
                                    if (str_dignosis.equals("")) {
                                        str_dignosis = vitalsignlistsfordatabase.get(i).getName();
                                    } else {
                                        str_dignosis = str_dignosis + " , " + vitalsignlistsfordatabase.get(i).getName();
                                    }


                                    JSONObject obj = new JSONObject();

                                    String encryptmedicine=EncryptionDecryption.encode( vitalsignlistsfordatabase.get(i).getAge());
                                    String encryptcomment=EncryptionDecryption.encode(  vitalsignlistsfordatabase.get(i).getAdvise());

                                    obj.put("diseasename", vitalsignlistsfordatabase.get(i).getRelation())
                                            .put("medicine_name",encryptmedicine)
                                            .put("unit_qty", vitalsignlistsfordatabase.get(i).getDisease())
                                            .put("total_qty", vitalsignlistsfordatabase.get(i).getStatus())
                                            .put("comments", encryptcomment)
                                            .put("medicine_time", vitalsignlistsfordatabase.get(i).getMed_time())
                                            .put("medicine_type", vitalsignlistsfordatabase.get(i).getMedtype())
                                            .put("after_meal", vitalsignlistsfordatabase.get(i).getYear())
                                            .put("things_to_do", "")
                                            .put("appointment_id", appointmentdatum.getAppointment().getId())
                                            .put("doctor_by", Prefhelper.getInstance(context).getUserid())
                                            .put("patient_to", appointmentdatum.getUser().getId());

                                    jsonArray.put(obj);

                                }
                                addingsoapnote();
                            } catch (JSONException e) {
                                Toast.makeText(context, "" + e, Toast.LENGTH_SHORT).show();
                                e.printStackTrace();
                            }
                    /*}
                    else
                    {
                        Toast.makeText(context, "Prescription fields is empty!!", Toast.LENGTH_SHORT).show();
                    }*/
                        }

                    }
                }

            }
        });



        checkBox_isrefer.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                {
                    Dialoginit(0,"Doctor Name","Address");
                }
                else
                {
                    tv_drname.setText("");
                    tv_drsp.setText("");
                    tv_cmt.setText("");
                }
            }
        });
        checkBox_sendlab.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                {
                    Dialoginit(1,"Lab Name","");
                }

            }
        });
        checkBox_sendpharmacy.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                {

                    CharSequence[]   dialogList = pharmacyname.toArray(new CharSequence[pharmacyname.size()]);


                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setTitle("Dignosis List");
                    builder.setItems(dialogList, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int item) {
                            // Do something with the selection items[item]
                            tv_phname.setText(pharmacyname.get(item));
                            pharmacy_id=postionpharmacy.get(item);
                        }
                    });
                    AlertDialog alert = builder.create();
                    alert.show();

                    //  Dialoginit(2,"Pharmacy Name","");
                }
                else {
                    pharmacy_id=0;
                    tv_phname.setText("");
                }
            }
        });

    }
    private void initView()
    {
        appointmentdatum= (Appointmentdatum)getIntent().getSerializableExtra("data");
        editText_observation=findViewById(R.id.observation);
        editText_comment=findViewById(R.id.comment);
        toolbar =findViewById(R.id.toolbar);
        toolbar.setTitle("Soap Note");
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        textView_name=findViewById(R.id.patientname);
        textView_gender=findViewById(R.id.patientge);
        textView_id=findViewById(R.id.patientid);
        textView_add=findViewById(R.id.patientadd);
        imageView_di_list=findViewById(R.id.di_list);
        textView_name.setText(appointmentdatum.getUser().getFirstName()+" "+appointmentdatum.getUser().getLastName());
        if(appointmentdatum.getUser().getGender().equals("1"))
        {
            textView_gender.setText("MALE");
        }
        else
        {

            textView_gender.setText("FEMALE");
        }

        textView_id.setText("Patient ID: "+appointmentdatum.getUser().getId());
        textView_add.setText(appointmentdatum.getService().getAddress()+" "+appointmentdatum.getService().getCity()+" "+appointmentdatum.getService().getPinCode());

        code=getIntent().getIntExtra("code",0);
        textView_txtpintime=findViewById(R.id.txtpintime);
        button_btn_pd =  findViewById(R.id.btn_pd);
        switch (code)
        {
            case  0:
                button_btn_pd.setVisibility(View.VISIBLE);
                if(Prefhelper.getInstance(context).getPatientsession()==1)
                {
                    sessionon=Prefhelper.getInstance(context).getPatientsession();
                    textView_txtpintime.setText(Prefhelper.getInstance(context).getPatiententrytime());
                }
                else
                    openalertdialog();
                break;
            case  1:
                break;
            case  2:
                break;
        }
        sign();
        button_btn_pd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i= new Intent(CompletePtientdetail.this,PreviousPatientdetail.class);
                i.putExtra("code",code);
                i.putExtra("userid",appointmentdatum.getUser().getId());
                i.putExtra("data",appointmentdatum);
                startActivity(i);
            }
        });

        checkBox_isrefer=findViewById(R.id.isreffered);
        checkBox_sendlab=findViewById(R.id.selab);
        checkBox_sendpharmacy=findViewById(R.id.sephar);


        tv_drname=findViewById(R.id.txt_redrname);
        tv_drsp=findViewById(R.id.txt_redrspe);
        tv_cmt=findViewById(R.id.txt_redrcmt);
        textView_t6=findViewById(R.id.t6);
        tv_cmt.setText("");
        tv_drsp.setText("");
        tv_cmt.setText("");

        mainimage=findViewById(R.id.img);
        tv_labname=findViewById(R.id.labname);
        tv_phname=findViewById(R.id.ph_name);
        tv_phname.setText("");

        imageView_cmd=findViewById(R.id.cmdpen);
        imageView_obse=findViewById(R.id.obrpen);
        imageView_cmd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog_sign.show();
            }
        });
        imageView_obse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog_sign.show();
            }
        });


        editText_dignosis=findViewById(R.id.dignosis);
        editText=findViewById(R.id.di_name);
       /* editText.setOnEditorActionListener(
                new EditText.OnEditorActionListener() {
                    @Override
                    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                        if (actionId == EditorInfo.IME_ACTION_SEARCH ||
                                actionId == EditorInfo.IME_ACTION_DONE ||
                                event != null &&
                                        event.getAction() == KeyEvent.ACTION_DOWN &&
                                        event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
                            if (event == null || !event.isShiftPressed()) {
                                // the user is done typing.
                                diseaseswithprescription.add(editText.getText().toString()+"  "+"   Add Prescription");
                                editText_dignosis.setText(editText_dignosis.getText().toString() + " , " + "   Add Prescription");
                                vitalsignlistsfordatabase.add(new vitalsignlist("","",editText.getText().toString(), "", "", "1","","","1",""));


                                return true; // consume.
                            }
                        }
                        return false; // pass on to other listeners.
                    }
                }
        );*/
        editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    //Toast.makeText(getApplicationContext(), "Got the focus", Toast.LENGTH_LONG).show();
                } else {


                    diseaseswithprescription.add(editText.getText().toString()+"  "+"   Add Prescription");
                    editText_dignosis.setText(editText_dignosis.getText().toString() + " , " + "   Add Prescription");
                    vitalsignlistsfordatabase.add(new vitalsignlist("","",editText.getText().toString(), "", "", "1","","","1",""));
                    editText.setText("");

                    //Toast.makeText(getApplicationContext(), "Lost the focus", Toast.LENGTH_LONG).show();
                }
            }
        });





        button=findViewById(R.id.btn_pre);
        editText_dignosis.setText("");

    }
    private void fetchpharmacydata()
    {
        OkHttpClient client = new OkHttpClient();
        Request validation_request = listpharmacy();
        client.newCall(validation_request).enqueue(new Callback() {

            @Override
            public void onFailure(Request request, IOException e)
            {
                Log.i("Activity", "onFailure: Fail");
            }

            @Override
            public void onResponse(final Response response) throws IOException {
                final String body=response.body().string();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        JSONObject jsonObject= null;
                        try {
                            jsonObject = new JSONObject(body);

                            if(jsonObject.getString("message").equals("success"))
                            {
                                JSONArray jsonArray= jsonObject.getJSONArray("data");
                                for (int i = 0; i <jsonArray.length() ; i++)
                                {
                                    JSONObject jsonObject1=jsonArray.getJSONObject(i);
                                    postionpharmacy.add(jsonObject1.getInt("id"));
                                    pharmacyname.add(jsonObject1.getString("name"));

                                }
                            }
                            else
                            {
                                Toast.makeText(context, ""+jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
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
    private Request listpharmacy() {
        JSONObject postdata = new JSONObject();
        try {
            postdata.put("added_by", Prefhelper.getInstance(context).getUserid());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(AddPlan.JSON, postdata.toString());
        return new Request.Builder()
                .addHeader("X-Api-Key", "AB5433GMDF657VBB")
                .addHeader("Content-Type", "application/json")
                .url(ApiUtils.BASE_URL+"listPharmacy")
                .post(body)
                .build();
    }
    private void searchdi()
    {
        dialog_diease = new Dialog(context,android.R.style.Theme_Light);
        dialog_diease.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog_diease.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog_diease.setContentView(R.layout.listviewwithedittext);
        listView =  dialog_diease.findViewById(R.id.list);
        listView.setTextFilterEnabled(true);
        editText_dia=dialog_diease.findViewById(R.id.di_name);
        editText_dia.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }
            @Override
            public void afterTextChanged(Editable s) {
                stringArrayAdapter.getFilter().filter(s.toString());
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(tempname.contains(stringArrayAdapter.getItem(position))) {
                    Toast.makeText(context, "You have already selected this", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    tempname = tempname + ", " + stringArrayAdapter.getItem(position);
                    textView_t6.setText(tempname);
                    editText.setText(null);
                    editText_dignosis.setText(editText_dignosis.getText().toString() + " , " + vitalsignlists.get(position).getName());
                    diseaseswithprescription.add(stringArrayAdapter.getItem(position) +"     Prename: "+vitalsignlists.get(position).getName());
                    vitalsignlistsfordatabase.add(new vitalsignlist(vitalsignlists.get(position).getName(),vitalsignlists.get(position).getAge(),vitalsignlists.get(position).getRelation(),vitalsignlists.get(position).getDisease(),vitalsignlists.get(position).getStatus(),vitalsignlists.get(position).getYear(), vitalsignlists.get(position).getDesc(),vitalsignlists.get(position).getAdvise(),vitalsignlists.get(position).getMedtype(),vitalsignlists.get(position).getMed_time()));

                    dialog_diease.dismiss();
                }
            }
        });
    }
    private void Dialoginit(final int pos, String tiet1, String tiet2)
    {
      //  String str1="",str2="",str3="";
        dialog2 = new Dialog(context);
        dialog2.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog2.setContentView(R.layout.dialog);
        dialog2.setCanceledOnTouchOutside(false);
        final EditText  et_1 =dialog2.findViewById( R.id.et1);
        final EditText  et_2 = dialog2.findViewById( R.id.et2);
        final EditText  et_comment = dialog2.findViewById( R.id.et_comment);
        final TextInputLayout tnl_comment = dialog2.findViewById( R.id.input_layout_comment);
        et_1.setHint(tiet1);
        if(tiet2.equals(""))
        {
            et_2.setVisibility(View.GONE);
        }
        final Button btn_submit = dialog2.findViewById(R.id.buttondetail);
        switch (pos)
        {
            case  0:
                btn_submit.setText("Refer");
                tnl_comment.setVisibility(View.VISIBLE);
                break;
            case  1:
                btn_submit.setText("Submit");
                break;
            case  2:
                btn_submit.setText("Submit");
                break;
        }

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                switch (pos)
                {
                    case  0:
                        tv_drname.setText(et_1.getText().toString());
                        tv_drsp.setText(et_2.getText().toString());
                        tv_cmt.setText(et_comment.getText().toString());
                        dialog2.dismiss();
                        break;
                    case  1:
                        tv_labname.setText(et_1.getText().toString());
                        dialog2.dismiss();
                        break;
                    case  2:
                        tv_phname.setText(et_1.getText().toString());
                        dialog2.dismiss();
                        break;
                }
            }
        });

        dialog2.show();
    }


    private Request AddSoapnote()
    {
        JSONObject postdata = new JSONObject();

        String encryptobservation= EncryptionDecryption.encode(str_observation);
        String encryptdignosis= EncryptionDecryption.encode(str_dignosis);
        String encryptcomment= EncryptionDecryption.encode(str_comment);
        try {

            postdata.put("appointment_id",appointmentdatum.getAppointment().getId());
            postdata.put("disease",str_diease);
            postdata.put("observation",encryptobservation);
            postdata.put("dignosis",encryptdignosis);
            postdata.put("comments",encryptcomment);
            if(checkBox_isrefer.isChecked()) {

                String encryptrefname=EncryptionDecryption.encode(tv_drname.getText().toString());
                String encryptrefaddres=EncryptionDecryption.encode(tv_drsp.getText().toString());
                String encryptrefcomment=EncryptionDecryption.encode(tv_cmt.getText().toString());
                postdata.put("is_reference", 1);
                postdata.put("reference_name", encryptrefname);
                postdata.put("reference_address", encryptrefaddres);
                postdata.put("reference_comments", encryptrefcomment);
            }
            else
            {
                postdata.put("is_reference", 0);
                postdata.put("reference_name", "");
                postdata.put("reference_address", "");
                postdata.put("reference_comments", "");
            }

            if(checkBox_sendpharmacy.isChecked())
            {
                postdata.put("pharmacyid", pharmacy_id);
            }
            else {
                postdata.put("pharmacyid", 0);
            }

            postdata.put("prescriptions",jsonArray);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(ListFamilyhistory.JSON, postdata.toString());
        return new Request.Builder()
                .addHeader("X-Api-Key", "AB5433GMDF657VBB")
                .addHeader("Content-Type", "application/json")
                .url(ApiUtils.BASE_URL+"createsoapnoteandpres")
                .post(body)
                .build();
    }
    private void addingsoapnote()
    {
        OkHttpClient client = new OkHttpClient();
        Request request = AddSoapnote();
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
                        if(body.contains("Soap Notes Saved Successfully"))
                        {
                            AlertDialog.Builder builder;
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                builder = new AlertDialog.Builder(context, android.R.style.Theme_Material_Dialog_Alert);
                            } else {
                                builder = new AlertDialog.Builder(context);
                            }
                            builder.setTitle("Message")
                                    .setMessage("Soap Notes Saved Successfully")
                                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                         flag=1;
                                        }
                                    })

                                    .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            // do nothing
                                        }
                                    })

                                    .show();
                            builder.setCancelable(false);
                        }
                        else
                        {
                            Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show();

                        }
                      //  Toast.makeText(context, ""+body, Toast.LENGTH_SHORT).show();

                      /*  try {
                            JSONObject jsonObject = new JSONObject(body);
                            if (jsonObject.getString("message").equals("success"))
                            {
                                AlertDialog.Builder builder;
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                    builder = new AlertDialog.Builder(context, android.R.style.Theme_Material_Dialog_Alert);
                                } else {
                                    builder = new AlertDialog.Builder(context);
                                }
                                builder.setTitle("Message")
                                        .setMessage(jsonObject.getString("data"))
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

                                       .show();
                                       builder.setCancelable(false);

                            } else {
                                Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception e) {
                            Toast.makeText(context, "" + e, Toast.LENGTH_SHORT).show();
                        }*/
                    }
                });
            }

        });

    }

    private boolean validateVitalsign(){
        if(str_observation.compareTo("")==0)
        {
            Toast.makeText(getApplicationContext(),"Observation field empty",Toast.LENGTH_LONG).show();
            return false;
        }
       /* else if (str_dignosis.compareTo("")==0)
        {
            Toast.makeText(getApplicationContext(),"Dignosis field empty",Toast.LENGTH_LONG).show();
            return false;
        }
       else if(str_diease.compareTo("")==0)
        {
            Toast.makeText(getApplicationContext(),"Disease field empty",Toast.LENGTH_LONG).show();
            return false;
        }*/
        else if (str_comment.compareTo("")==0)
        {
            Toast.makeText(getApplicationContext(),"Comment field empty",Toast.LENGTH_LONG).show();
            return false;
        }
        else {
            return true;
        }
    }
   }
