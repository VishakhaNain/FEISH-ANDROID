package com.app.feish.application.fragment;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.app.feish.application.Connectiondetector;
import com.app.feish.application.R;
import com.app.feish.application.Remote.ApiUtils;
import com.app.feish.application.Remote.EncryptionDecryption;
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
import java.util.List;
import static com.app.feish.application.Patient.MedicalHitoryp.JSON;

/**
 * Created by lenovo on 6/4/2016.
 */
public class Addmedicalhistory extends Fragment {
    // Store instance variables
  View view1;
  EditText editText_ctype,et_testdate,editText_t,editText_mhdesc;
  Spinner spinner_mcond,spinner_ys;
    CalendarPickerView calendar1;
    Button  button;
    SimpleDateFormat DATE_FORMAT;
    Connectiondetector connectiondetector;
    Button btn;
    String userid="";
    Medicalcondition  medicalcondition;
    List<Medicalconditionlist> medicalconditionlists;
String str_mcon="",str_ctype="",str_cumedication="",str_date="",str_desc="";
   
    public static Addmedicalhistory newInstance(int page, String title) {
        Addmedicalhistory fragmentFirst = new Addmedicalhistory();
        Bundle args = new Bundle();
        args.putInt("someInt", page);
        args.putString("Stringlist", title);
        fragmentFirst.setArguments(args);
        return fragmentFirst;
    }

    // Store instance variables based on arguments passed
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userid=getArguments().getString("Stringlist");

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        editText_t=view1.findViewById(R.id.et);
        et_testdate=view1.findViewById(R.id.testdate);
        button=view1.findViewById(R.id.medicalhis);
        editText_ctype=view1.findViewById(R.id.ctype);
        editText_mhdesc=view1.findViewById(R.id.mhdesc);
        spinner_ys=view1.findViewById(R.id.cmedication_spinner);
        spinner_mcond=view1.findViewById(R.id.mcondtion_spinner);
        et_testdate=view1.findViewById(R.id.testdate);
        DATE_FORMAT = new SimpleDateFormat("yyyy/MM/dd");
        connectiondetector=new Connectiondetector(getActivity());
        et_testdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              pickdate();
            }
        });
        if(connectiondetector.isConnectingToInternet())
        addingdata();
        else
            Toast.makeText(getActivity(), "No internet!!", Toast.LENGTH_SHORT).show();

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                str_ctype=editText_ctype.getText().toString();
                str_desc=editText_mhdesc.getText().toString();
                str_cumedication=spinner_ys.getSelectedItem().toString();
                if(str_cumedication.equals("Yes"))
                    str_cumedication="1";
                else  str_cumedication="0";
                if(validatereport()) {
                    if(connectiondetector.isConnectingToInternet())
                    {
                               addmhdata();
                            //     addmhdataMDB();
                    }
                    else
                        Toast.makeText(getActivity(), "No Internet!!", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }

    // Inflate the view for the fragment based on layout XML
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
         view1 = inflater.inflate(R.layout.fragment_medicalhistory, container, false);
   //     getActivity().setTitle("CollegeName");


        return view1;
    }
    public void hideSoftKeyboard() {
        if (getActivity().getCurrentFocus() != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getContext().getSystemService(getContext().INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
        }
    }
    void pickdate()
    {
        final Dialog dialog1 = new Dialog(getActivity());
        dialog1.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog1.setContentView(R.layout.camgal);
        dialog1.setCanceledOnTouchOutside(false);
        final Calendar nextYear=Calendar.getInstance();
        nextYear.add(Calendar.YEAR,1);
        final  Calendar lastYear=Calendar.getInstance();
        lastYear.add(Calendar.YEAR,-1);
        btn=(Button)dialog1.findViewById(R.id.ok);
        calendar1 = (CalendarPickerView) dialog1.findViewById(R.id.calendar_view);
        calendar1.init(lastYear.getTime(), nextYear.getTime()) //
                .inMode(CalendarPickerView.SelectionMode.SINGLE) //
                .withSelectedDate(new Date());
        calendar1.setCustomDayView(new DefaultDayViewAdapter());
        Date today =new Date();
        calendar1.setDecorators(Collections.<CalendarCellDecorator>emptyList());
        calendar1.init(today,nextYear.getTime())
                .withSelectedDate(today).inMode(CalendarPickerView.SelectionMode.SINGLE);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayList<Date> mydates = (ArrayList<Date>)calendar1.getSelectedDates();
                for (int i = 0; i <mydates.size() ; i++) {
                    Date tempdate = mydates.get(i);
                    String testdate = DATE_FORMAT.format(tempdate);
                    et_testdate.setText(testdate);
                    str_date=testdate;
                    dialog1.dismiss();

                }

            }
        });
        dialog1.show();

    }

    private Request addingmedicalhistory() {
        JSONObject postdata = new JSONObject();
        try {
            postdata.put("user_id",userid);
            postdata.put("conditions",Integer.parseInt(str_mcon));
            postdata.put("condition_type",str_ctype);
            postdata.put("current_medication",Integer.parseInt(str_cumedication));
            postdata.put("mh_date",str_date);
            postdata.put("description",str_desc);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d("medicaldata",postdata.toString());
        RequestBody body = RequestBody.create(JSON, postdata.toString());
        return new Request.Builder()
                .addHeader("X-Api-Key", "AB5433GMDF657VBB")
                .addHeader("Content-Type", "application/json")
                .url(ApiUtils.BASE_URL+"addMadicalHistory")
                .post(body)
                .build();

    }
    private void addmhdata()
    {
        OkHttpClient client = new OkHttpClient();
        Request request = addingmedicalhistory();

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
                medicalconditionJSON(body);

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try
                        {
                            String newres=body.substring(body.lastIndexOf(">")+1,body.lastIndexOf("}"))+"}";
                                  /*  Toast.makeText(getActivity(), ""+newres, Toast.LENGTH_SHORT).show();
                                    editText.setText(""+newres);*/
                            JSONObject jsonObject=new JSONObject(newres);

                            // Toast.makeText(getActivity(), ""+body, Toast.LENGTH_SHORT).show();
                            if(jsonObject.getInt("Success")==1)
                            {
                                //   Toast.makeText(getActivity(), "Success", Toast.LENGTH_SHORT).show();


                                AlertDialog.Builder builder;
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                    builder = new AlertDialog.Builder(getActivity(), android.R.style.Theme_Material_Dialog_Alert);
                                } else {
                                    builder = new AlertDialog.Builder(getActivity());
                                }
                                builder.setTitle("Message")
                                        .setMessage(jsonObject.getString("message"))
                                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {
                                                // continue with delete
                                                getActivity().finish();
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
                            else
                            {
                                Toast.makeText(getActivity(), "Error", Toast.LENGTH_SHORT).show();
                            }
                        }
                        catch (Exception e)
                        {
                            Toast.makeText(getActivity(), ""+e, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }

        });

    }
   /* private Request addingmedicalhistoryMDB() {
        JSONObject postdatauser = new JSONObject();
        String encryptdate= EncryptionDecryption.encode(str_date);

        try {

       *//*     JSONObject postdatavalue = new JSONObject();
            JSONArray jsonArrayrecord = new JSONArray();
            JSONObject postdata = new JSONObject();*//*

            postdatauser.put("user_id", userid);
            postdatauser.put("conditions", Integer.parseInt(str_mcon));
            postdatauser.put("condition_type", str_ctype);
            postdatauser.put("current_medication", Integer.parseInt(str_cumedication));
            postdatauser.put("mh_date", encryptdate);
            postdatauser.put("description", str_desc);

            postdatauser.put("modified_by", Prefhelper.getInstance(getActivity()).getUserid());
            postdatauser.put("modified_at",new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(new java.util.Date()));
            postdatauser.put("source_type","mobile");
            postdatauser.put("deleted_flag","0");

         //   postdata.put("MedicalHistory", postdatauser);

          //  postdatavalue.put("value", postdata);

//            jsonArrayrecord.put(postdatavalue);

          //  postdatamain.put("record", jsonArrayrecord);
           // Log.d("register data", postdatamain.toString());

        } catch (JSONException e) {
            e.printStackTrace();
        }
       // Log.d("medicaldata",postdatamain.toString());
        RequestBody body = RequestBody.create(JSON, postdatauser.toString());
        return new Request.Builder()
                .url(ApiUtils.BASE_URLMAngoDB+"add/medicalhistory")
                .post(body)
                .build();

    }
    private void addmhdataMDB()
    {
        OkHttpClient client = new OkHttpClient();
        Request request = addingmedicalhistoryMDB();

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
                              getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getActivity(), ""+body, Toast.LENGTH_SHORT).show();


                    }
                });
            }

        });

    }*/

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
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        medicalconditionlists=medicalcondition.getData();
                        CustomAdapter customAdapter=new CustomAdapter(getActivity(),medicalconditionlists);
                        spinner_mcond.setAdapter(customAdapter);
                        spinner_mcond.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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

    public void medicalconditionJSON(String response) {
        Gson gson = new GsonBuilder().create();
        medicalcondition = gson.fromJson(response, Medicalcondition.class);
    }
    private boolean validatereport(){
        if(str_date.compareTo("")==0)
        {
            Toast.makeText(getActivity(),"Date field empty",Toast.LENGTH_LONG).show();
            return false;
        }
        else if (str_desc.compareTo("")==0)
        {
            Toast.makeText(getActivity(),"Description field empty",Toast.LENGTH_LONG).show();
            return false;
        }
        if(str_ctype.compareTo("")==0)
        {
            Toast.makeText(getActivity(),"Condition type field empty",Toast.LENGTH_LONG).show();
            return false;
        }
        if(str_cumedication.compareTo("Current Medication")==0)
        {
            Toast.makeText(getActivity(),"test field empty",Toast.LENGTH_LONG).show();
            return false;
        }
        if(str_mcon.compareTo("")==0)
        {
            Toast.makeText(getActivity(),"test field empty",Toast.LENGTH_LONG).show();
            return false;
        }

        else {
            return true;
        }
    }
}
 class CustomAdapter extends BaseAdapter {
    Context context;
    LayoutInflater inflter;
     List<Medicalconditionlist> medicalconditionlists;
    public CustomAdapter(Context applicationContext, List<Medicalconditionlist> medicalconditionlists) {
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