package com.app.feish.application.fragment;

import android.app.Dialog;
import android.app.ProgressDialog;
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
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;
import com.app.feish.application.R;
import com.app.feish.application.Remote.ApiUtils;
import com.app.feish.application.sessiondata.Prefhelper;
import com.squareup.okhttp.Callback;
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

import static com.app.feish.application.fragment.Listpre.JSON;

/**
 * Created by lenovo on 6/4/2016.
 */
public class Addremainder extends Fragment {
    // Store instance variables
  View view1;
    CalendarPickerView calendar_s;
    String str_sub="",str_desc="",str_monthdate="",str_enddate="",str_date="",str_time="",str_intervaldetail="", weekday="";
    Button btn_s,btn_time;
    com.takisoft.datetimepicker.widget.TimePicker timePicker;
    SimpleDateFormat DATE_FORMAT,DATE_FORMAT_HH;
    private EditText mTitle,et_desc;
    int flag_isremainder=0,flad_objtype=1;
    private CheckBox mAlarmEnabled;
    LinearLayout ll_isremainder,ll_forrepeated,ll_hours;
    private Spinner mOccurence,spinner_hoursinterval_spinner;
    private Button mDateButton;
    private Button mTimeButton;
    int drnote=0;
     Button button_save;
     EditText editText_remainder_e_date,editText_remainder_s_date,editText_monthdate,editText_intervalhours;
     RadioGroup radioGroup_rtype;
     LinearLayout linearLayout_ll_weekdayscheck;
ProgressDialog progressDialog;
     CheckBox checkBox_mo,checkBox_tu,checkBox_we,checkBox_th,checkBox_fr,checkBox_sa,checkBox_sun;


    public static Addremainder newInstance(int page, String title) {
        Addremainder fragmentFirst = new Addremainder();
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
        drnote=getArguments().getInt("someInt");

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        
        progressDialog= new ProgressDialog(getActivity());
        progressDialog.setTitle("Inserting............");
        
        DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
        DATE_FORMAT_HH = new SimpleDateFormat("dd");

        mTitle =view1.findViewById(R.id.title_sub);
        et_desc =view1.findViewById(R.id.desc);
        mOccurence = view1.findViewById(R.id.occurence_spinner);
        spinner_hoursinterval_spinner = view1.findViewById(R.id.hoursinterval_spinner);
        ll_hours=view1.findViewById(R.id.llhours);
        mDateButton = view1.findViewById(R.id.date_button);
        mTimeButton =view1.findViewById(R.id.time_button);
        ll_isremainder = view1.findViewById(R.id.ll_isremainder);
        ll_forrepeated = view1.findViewById(R.id.forrepeated);
        linearLayout_ll_weekdayscheck = view1.findViewById(R.id.ll_weekdayscheck);
        mAlarmEnabled =view1.findViewById(R.id.alarm_checkbox);
        radioGroup_rtype=view1.findViewById(R.id.radio_rtype);
       // editText_remainder_s_date=view1.findViewById(R.id.sdate);
        editText_remainder_e_date=view1.findViewById(R.id.edate);
        editText_intervalhours=view1.findViewById(R.id.intervalhours);
        editText_intervalhours.setText("");
        editText_monthdate=view1.findViewById(R.id.monthdate);
        checkBox_mo = view1.findViewById(R.id.checkBox_mon);
        checkBox_tu = view1.findViewById(R.id.checkBox_tues);
        checkBox_we = view1.findViewById(R.id.checkBox_wed);
        checkBox_th = view1.findViewById(R.id.checkBox_thus);
        checkBox_fr = view1.findViewById(R.id.checkBox_fri);
        checkBox_sa = view1.findViewById(R.id.checkBox_sat);
        checkBox_sun = view1.findViewById(R.id.checkBox_sun);

     /*   editText_retime.setText("");
        editText_retime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickdate_time(1);
            }
        });*/
        editText_intervalhours.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickdate_time(2);
            }
        });
        radioGroup_rtype.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(checkedId==R.id.radioweekday)
                {
                    linearLayout_ll_weekdayscheck.setVisibility(View.VISIBLE);
                    editText_monthdate.setVisibility(View.GONE);
                    ll_hours.setVisibility(View.GONE);
                    spinner_hoursinterval_spinner.setVisibility(View.GONE);
                    editText_intervalhours.setVisibility(View.GONE);
                    flad_objtype=1;
                }
                else if(checkedId==R.id.radiohours)
                {
                    linearLayout_ll_weekdayscheck.setVisibility(View.GONE);
                    editText_monthdate.setVisibility(View.GONE);
                    ll_hours.setVisibility(View.VISIBLE);
                    spinner_hoursinterval_spinner.setVisibility(View.VISIBLE);
                    editText_intervalhours.setVisibility(View.VISIBLE);
                    flad_objtype=3;
                }
                else if(checkedId==R.id.radiodatewise)
                {
                    flad_objtype=2;
                    linearLayout_ll_weekdayscheck.setVisibility(View.GONE);
                    editText_monthdate.setVisibility(View.VISIBLE);
                    ll_hours.setVisibility(View.GONE);
                    spinner_hoursinterval_spinner.setVisibility(View.GONE);
                    editText_intervalhours.setVisibility(View.GONE);
                }

            }
        });

        editText_monthdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickdate_sdate(3);
            }
        });

    /*    editText_remainder_s_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickdate_sdate(1);
            }
        });*/
        editText_remainder_e_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickdate_sdate(2);
            }
        });

        mOccurence.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position==1)
                {
                    ll_forrepeated.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        mAlarmEnabled.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                {
                    ll_isremainder.setVisibility(View.VISIBLE);
                    flag_isremainder=1;
                }
                else
                {
                    ll_isremainder.setVisibility(View.GONE);
                    flag_isremainder=0;
                }
            }
        });
        mDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               pickdate_sdate(0);
            }
        });
        mTimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickdate_time(0);
            }
        });
        button_save=view1.findViewById(R.id.save);
        button_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                str_desc=et_desc.getText().toString();
                str_sub=mTitle.getText().toString();
                str_monthdate=editText_monthdate.getText().toString();
                str_date=mDateButton.getText().toString();
                str_time=mTimeButton.getText().toString();
                str_intervaldetail=editText_intervalhours.getText().toString();
                str_enddate=editText_remainder_e_date.getText().toString();

                if(linearLayout_ll_weekdayscheck.getVisibility()==View.VISIBLE)
                {
                    if(checkBox_mo.isChecked())
                        weekday="1";
                    if(checkBox_tu.isChecked()) {
                        if(!weekday.equals(""))
                        weekday = weekday + ",2";
                        else
                            weekday = weekday + "2";
                    }
                    if(checkBox_we.isChecked()) {
                        if(!weekday.equals(""))
                            weekday = weekday + ",3";
                        else
                            weekday = weekday + "3";
                    }
                    if(checkBox_th.isChecked()) {
                        if(!weekday.equals(""))
                            weekday = weekday + ",4";
                        else
                            weekday = weekday + "4";
                    }
                    if(checkBox_fr.isChecked()) {
                        if(!weekday.equals(""))
                            weekday = weekday + ",5";
                        else
                            weekday = weekday + "5";
                    }
                    if(checkBox_sa.isChecked()) {
                        if(!weekday.equals(""))
                            weekday = weekday + ",6";
                        else
                            weekday = weekday + "6";
                    }
                    if(checkBox_sun.isChecked()) {
                        if(!weekday.equals(""))
                            weekday = weekday + ",7";
                        else
                            weekday = weekday + "7";
                    }
                }
                insertdata(str_sub,str_desc,weekday,str_monthdate,str_intervaldetail,str_date,str_time,str_enddate);

            }
        });

    }
    private void insertdata(String sub,String desc,String weekday,String monthdate,String hrsdeatil,String Date,String Time,String end_date)
    {
        progressDialog.show();
        OkHttpClient client = new OkHttpClient();
        Request request = AddPatientnotes(sub,desc,weekday,monthdate,hrsdeatil,Date,Time,end_date);
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

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        try {
                            String newres = body.substring(body.lastIndexOf(">") + 1, body.lastIndexOf("}")) + "}";
                                  /*  Toast.makeText(getActivity(), ""+newres, Toast.LENGTH_SHORT).show();
                                    editText.setText(""+newres);*/
                            JSONObject jsonObject = new JSONObject(newres);

                            // Toast.makeText(getActivity(), ""+body, Toast.LENGTH_SHORT).show();
                            if (jsonObject.getInt("Success") == 1) {
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

                            } else {
                                Toast.makeText(getActivity(), "Error", Toast.LENGTH_SHORT).show();
                            }
                            progressDialog.dismiss();
                        } catch (Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(getActivity(), "" + e, Toast.LENGTH_SHORT).show();
                        }
                        progressDialog.dismiss();
                    }
                });
            }

        });
    }

    private Request AddPatientnotes(String sub,String desc,String weekday,String monthdate,String hrsdeatil,String Date,String Time,String end_date)
    {
        String url="";
        if(drnote==0)
            url=ApiUtils.BASE_URL+"PatientNotes";
            else
            url=ApiUtils.BASE_URL+"DoctorNotes";
        JSONObject postdata = new JSONObject();
        try {
            postdata.put("Subject",sub);
            postdata.put("Description",desc);
            postdata.put("is_remender",flag_isremainder);
            postdata.put("User_id",Integer.parseInt(Prefhelper.getInstance(getActivity()).getUserid()));
            postdata.put("is_type",Integer.parseInt("1"));
            postdata.put("obj_type",flad_objtype);
            postdata.put("no_of_day",weekday);
            postdata.put("month_date",monthdate);
            postdata.put("end_date",end_date);
            postdata.put("hrs_detail",hrsdeatil);
            postdata.put("Date",Date);
            postdata.put("Time",Time);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(JSON, postdata.toString());
        return new Request.Builder()
                .addHeader("X-Api-Key", "AB5433GMDF657VBB")
                .addHeader("Content-Type", "application/json")
                .url(url)
                .post(body)
                .build();
    }

    void pickdate_sdate(final int flag)
    {
        final Dialog dialog1 = new Dialog(getActivity());
        dialog1.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog1.setContentView(R.layout.camgal);
        dialog1.setCanceledOnTouchOutside(false);
        final Calendar nextYear=Calendar.getInstance();
        nextYear.add(Calendar.YEAR,1);
        final  Calendar lastYear=Calendar.getInstance();
        lastYear.add(Calendar.YEAR,-1);
        btn_s=dialog1.findViewById(R.id.ok);
        calendar_s = dialog1.findViewById(R.id.calendar_view);
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
                    if (flag == 0)
                    {
                        mDateButton.setText(testdate);
                    }
                    else if(flag==1)
                    {
                       // editText_remainder_s_date.setText(testdate);
                    }
                    else  if(flag==2)
                    {
                        editText_remainder_e_date.setText(testdate);
                    }
                    else if(flag==3)
                    {
                        String maindate=DATE_FORMAT_HH.format(tempdate);

                        if(!editText_monthdate.getText().toString().equals(""))
                        {
                            editText_monthdate.setText(editText_monthdate.getText().toString()+", "+maindate);
                        }
                        else
                        {
                            editText_monthdate.setText(maindate);
                        }
                    }

                    dialog1.dismiss();

                }

            }
        });
        dialog1.show();

    }
    void pickdate_time(final int flag)
    {
        final Dialog dialog1 = new Dialog(getActivity());
        dialog1.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog1.setContentView(R.layout.timepickfer);
        dialog1.setCanceledOnTouchOutside(false);
        final Calendar nextYear=Calendar.getInstance();
        nextYear.add(Calendar.YEAR,1);
        final  Calendar lastYear=Calendar.getInstance();
        lastYear.add(Calendar.YEAR,-1);
        btn_time=dialog1.findViewById(R.id.ok);
       timePicker=dialog1.findViewById(R.id.calendar_view);
        btn_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(flag==0) {
                    mTimeButton.setText(timePicker.getHour() + " :" + timePicker.getMinute());
                }
                else  if(flag==1)
                    {
                      /*  if(!editText_retime.getText().toString().equals(""))
                            editText_retime.setText(editText_retime.getText().toString()+","+timePicker.getHour() + " :" + timePicker.getMinute());
                    else*/
                           // editText_retime.setText(timePicker.getHour() + " :" + timePicker.getMinute());
                }
                else if(flag==2)
                {
                       if(!editText_intervalhours.getText().toString().equals(""))
                           editText_intervalhours.setText(editText_intervalhours.getText().toString()+","+timePicker.getHour() + " :" + timePicker.getMinute());
                    else
                           editText_intervalhours.setText(timePicker.getHour() + " :" + timePicker.getMinute());
                }
             dialog1.dismiss();
            }
        });
        dialog1.show();

    }
    // Inflate the view for the fragment based on layout XML
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
         view1 = inflater.inflate(R.layout.fragment_addremainder, container, false);


        return view1;
    }


}





 /* public void hideSoftKeyboard()
    {
        if (getActivity()().getCurrentFocus() != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getContext().getSystemService(getContext().INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getActivity()().getCurrentFocus().getWindowToken(), 0);
        }
    }*/