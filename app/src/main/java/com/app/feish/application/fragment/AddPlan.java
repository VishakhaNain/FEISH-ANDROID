package com.app.feish.application.fragment;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
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
import com.app.feish.application.modelclassforapi.Datum2;
import com.app.feish.application.modelclassforapi.ListServicesContact;
import com.app.feish.application.sessiondata.Prefhelper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;
import com.squareup.timessquare.CalendarCellDecorator;
import com.squareup.timessquare.CalendarPickerView;
import com.squareup.timessquare.DefaultDayViewAdapter;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * Created by lenovo on 6/4/2016.
 */
public class AddPlan extends Fragment {
    // Store instance variables
  View view1;
    public static final MediaType JSON = MediaType.parse("application/json:charset=utf-8");
    private EditText planname, planprice, plandesc, planvalidvisit, planvalidity;
    private String str_planname, str_planprice, str_plandesc, str_planvalidvisit, str_planvalidity;
    private ListServicesContact serviceResponse;
    //CustomAdapter_bd customAdapter_bd;
    Spinner spinner,plantype;
   // String str_sername;
    List<Datum2> l;
    CalendarPickerView calendar_s,calendar_e;
    Button btn_s,btn_e,button_addplan;
    Connectiondetector connectiondetector;
    SimpleDateFormat DATE_FORMAT;
    EditText et_startdate,et_enddate;
    String str_sdate,str_edate;

    public static AddPlan newInstance(int page, String title) {
        AddPlan fragmentFirst = new AddPlan();
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

    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    // Inflate the view for the fragment based on layout XML
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
         view1 = inflater.inflate(R.layout.fragment_plan, container, false);
        et_startdate=view1.findViewById(R.id.sdate);
        et_enddate=view1.findViewById(R.id.edate);
        connectiondetector= new Connectiondetector(getActivity());
        et_enddate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickdate_end();
            }
        });
        et_startdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickdate_sdate();
            }
        });
         DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
         planname=view1.findViewById(R.id.planname);
         planprice=view1.findViewById(R.id.planprice);
         plantype=view1.findViewById(R.id.spinner_plantype);
         plandesc=view1.findViewById(R.id.doctordescriptionID);
         planvalidvisit=view1.findViewById(R.id.planvalidvisit);
         planvalidity=view1.findViewById(R.id.validity);
        // spinner = view1.findViewById(R.id.spinner_sername);
         button_addplan=view1.findViewById(R.id.addplan);
         button_addplan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                str_planname=planname.getText().toString();
                str_planprice=planprice.getText().toString();
                str_plandesc=plandesc.getText().toString();
                str_planvalidity=planvalidity.getText().toString();
                str_planvalidvisit=planvalidvisit.getText().toString();
                if(connectiondetector.isConnectingToInternet())
                {
                    if(validateassistantsign())
                    {
                        addplan();
                    }
                }
                else
                {

                    Toast.makeText(getActivity(), "No Internet!!", Toast.LENGTH_SHORT).show();
                }

            }
        });

      return view1;
    }

    void pickdate_sdate()
    {
        final Dialog dialog1 = new Dialog(getActivity());
        dialog1.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog1.setContentView(R.layout.camgal);
        dialog1.setCanceledOnTouchOutside(false);
        final Calendar nextYear=Calendar.getInstance();
        nextYear.add(Calendar.YEAR,1);
        final  Calendar lastYear=Calendar.getInstance();
        lastYear.add(Calendar.YEAR,-1);
        btn_s=(Button)dialog1.findViewById(R.id.ok);
        calendar_s = (CalendarPickerView) dialog1.findViewById(R.id.calendar_view);
        calendar_s.init(lastYear.getTime(), nextYear.getTime()) //
                .inMode(CalendarPickerView.SelectionMode.SINGLE) //
                .withSelectedDate(new Date());
        calendar_s.setCustomDayView(new DefaultDayViewAdapter());
        Date today =new Date();
        calendar_s.setDecorators(Collections.<CalendarCellDecorator>emptyList());
        calendar_s.init(today,nextYear.getTime())
                .withSelectedDate(today).inMode(CalendarPickerView.SelectionMode.SINGLE);
        btn_s.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayList<Date> mydates = (ArrayList<Date>)calendar_s.getSelectedDates();
                for (int i = 0; i <mydates.size() ; i++) {
                    Date tempdate = mydates.get(i);
                    String testdate = DATE_FORMAT.format(tempdate);
                    str_sdate=testdate;
                    et_startdate.setText(testdate);
                    dialog1.dismiss();

                }

            }
        });
        dialog1.show();

    }
    void pickdate_end()
    {
        final Dialog dialog1 = new Dialog(getActivity());
        dialog1.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog1.setContentView(R.layout.camgal);
        dialog1.setCanceledOnTouchOutside(false);
        final Calendar nextYear=Calendar.getInstance();
        nextYear.add(Calendar.YEAR,1);
        final  Calendar lastYear=Calendar.getInstance();
        lastYear.add(Calendar.YEAR,-1);
        btn_e=(Button)dialog1.findViewById(R.id.ok);
        calendar_e = (CalendarPickerView) dialog1.findViewById(R.id.calendar_view);
        calendar_e.init(lastYear.getTime(), nextYear.getTime()) //
                .inMode(CalendarPickerView.SelectionMode.SINGLE) //
                .withSelectedDate(new Date());
        calendar_e.setCustomDayView(new DefaultDayViewAdapter());
        Date today =new Date();
        calendar_e.setDecorators(Collections.<CalendarCellDecorator>emptyList());
        calendar_e.init(today,nextYear.getTime())
                .withSelectedDate(today).inMode(CalendarPickerView.SelectionMode.SINGLE);
        btn_e.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayList<Date> mydates = (ArrayList<Date>)calendar_e.getSelectedDates();
                for (int i = 0; i <mydates.size() ; i++) {
                    Date tempdate = mydates.get(i);
                    String testdate = DATE_FORMAT.format(tempdate);
                    str_edate=testdate;
                    et_enddate.setText(testdate);
                    dialog1.dismiss();

                }

            }
        });
        dialog1.show();
    }
    private void addplan()
    {
        OkHttpClient client = new OkHttpClient();
        Request validation_request = adddrplan();
        client.newCall(validation_request).enqueue(new Callback() {

            @Override
            public void onFailure(Request request, IOException e) {

                // Toast.makeText(getActivity(),"Fail",Toast.LENGTH_LONG).show();
                Log.i("Activity", "onFailure: Fail");
            }

            @Override
            public void onResponse(final Response response) throws IOException {
                final String body = response.body().string();
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONObject jsonObject = new JSONObject(body);
                            if(jsonObject.getBoolean("status")) {
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
                                Toast.makeText(getActivity(), ""+jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                            }
                        }
                        catch (JSONException e)
                        {

                            Toast.makeText(getActivity(), ""+e, Toast.LENGTH_SHORT).show();
                        }

                    }
                });
            }
        });

    }
    private Request adddrplan() {
        JSONObject postdata = new JSONObject();
        try {
            postdata.put("user_id", Prefhelper.getInstance(getActivity()).getUserid());
            postdata.put("name", str_planname);
            postdata.put("price", str_planprice);
            postdata.put("plan_details", str_plandesc);
            postdata.put("validity", str_planvalidity);
            postdata.put("percentage_per_visit", str_planvalidvisit);
            postdata.put("start_date", str_sdate);
            postdata.put("end_date", str_edate);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(JSON, postdata.toString());
        return new Request.Builder()
                .addHeader("X-Api-Key", "AB5433GMDF657VBB")
                .addHeader("Content-Type", "application/json")
                .url(ApiUtils.BASE_URL+"adddrplan")
                .post(body)
                .build();
    }
    private boolean validateassistantsign(){
        if(str_sdate.compareTo("")==0)
        {
            Toast.makeText(getActivity(),"Select Start Date",Toast.LENGTH_LONG).show();
            return false;
        }
        else if (str_edate.compareTo("")==0)
        {
            Toast.makeText(getActivity(),"Select End Date",Toast.LENGTH_LONG).show();
            return false;
        }
        else if (str_planname.compareTo("")==0)
        {
            Toast.makeText(getActivity(),"Select Plan Name",Toast.LENGTH_LONG).show();
            return false;
        }
        else if(str_planprice.compareTo("")==0)
        {
            Toast.makeText(getActivity(),"Select Plan Price",Toast.LENGTH_LONG).show();
            return false;
        }
        else if (str_plandesc.compareTo("")==0)
        {
            Toast.makeText(getActivity(),"Select Plan Desc",Toast.LENGTH_LONG).show();
            return false;
        } else if (str_planvalidity.compareTo("")==0)
        {
            Toast.makeText(getActivity(),"Select Plan Validity",Toast.LENGTH_LONG).show();
            return false;
        } else if (str_planvalidvisit.compareTo("")==0)
        {
            Toast.makeText(getActivity(),"Select Plan Valid Visit",Toast.LENGTH_LONG).show();
            return false;
        }
        else {
            return true;
        }
    }




    /*private void fetchdata()
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
                // final String body=response.body().string();

                listServiceResponse(response.body().string());
                final boolean isSuccessful=serviceResponse.getStatus();
                l=serviceResponse.getData();
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (isSuccessful) {
                            customAdapter_bd= new CustomAdapter_bd(getActivity(),l);
                            spinner.setAdapter(customAdapter_bd);

                        } else {
                            Toast.makeText(getActivity(), "Could not load the list!!", Toast.LENGTH_SHORT).show();
                        }
                        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                str_sername=l.get(position).getService().getId();
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

    private Request listservices() {
        JSONObject postdata = new JSONObject();
        try {
            postdata.put("user_id", Prefhelper.getInstance(getActivity()).getUserid());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(JSON, postdata.toString());
        return new Request.Builder()
                .addHeader("X-Api-Key", "AB5433GMDF657VBB")
                .addHeader("Content-Type", "application/json")
                .url(ApiUtils.BASE_URL+"listService")
                .post(body)
                .build();
    }
    public void listServiceResponse(String response) {
        Gson gson = new GsonBuilder().create();
        serviceResponse = gson.fromJson(response, ListServicesContact.class);
    }
    class CustomAdapter_bd extends BaseAdapter {
        Context context;
        LayoutInflater inflter;
        List<Datum2> medicalconditionlists;
        public CustomAdapter_bd(Context applicationContext, List<Datum2> medicalconditionlists) {
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
            TextView names = view.findViewById(R.id.txt);
            names.setText(medicalconditionlists.get(i).getService().getTitle());
            return view;
        }


    }
    */

}