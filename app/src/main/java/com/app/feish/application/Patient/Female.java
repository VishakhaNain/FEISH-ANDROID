package com.app.feish.application.Patient;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.app.feish.application.Adpter.CustomAdapter_cycle;
import com.app.feish.application.Adpter.CustomAdapter_reportp;
import com.app.feish.application.R;
import com.app.feish.application.model.searchdoctorpojo;
import com.squareup.timessquare.CalendarCellDecorator;
import com.squareup.timessquare.CalendarPickerView;
import com.squareup.timessquare.DefaultDayViewAdapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;

public class Female extends AppCompatActivity {
ListView listView;
Context context=this;
CustomAdapter_cycle customAdapter_vitalsigns;
    CalendarPickerView calendar_s,calendar_e;
    Button btn_s,btn_e;
    EditText et_startdate,et_enddate;
    SimpleDateFormat DATE_FORMAT;

    Toolbar toolbar;
    ArrayList<searchdoctorpojo> searchDoctorLists= new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_female);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setTitle("She Section");
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        listView= findViewById(R.id.list);
        /////////////
        /////////////
        searchDoctorLists.add(new searchdoctorpojo("","",""));
        searchDoctorLists.add(new searchdoctorpojo("","",""));
        searchDoctorLists.add(new searchdoctorpojo("","",""));
        searchDoctorLists.add(new searchdoctorpojo("","",""));
        customAdapter_vitalsigns=new CustomAdapter_cycle(context,searchDoctorLists);
        listView.setAdapter(customAdapter_vitalsigns);

        et_startdate=findViewById(R.id.sdate);
        et_enddate=findViewById(R.id.edate);
        DATE_FORMAT = new SimpleDateFormat("dd/MM/yyyy");
        //     getActivity().setTitle("CollegeName");

        et_startdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickdate_sdate();
            }
        });
        et_enddate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickdate_end();
            }
        });
    }
    void pickdate_sdate()
    {
        final Dialog dialog1 = new Dialog(context);
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
                    et_startdate.setText(testdate);
                    dialog1.dismiss();

                }

            }
        });
        dialog1.show();

    }
    void pickdate_end()
    {
        final Dialog dialog1 = new Dialog(context);
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
                    et_enddate.setText(testdate);
                    dialog1.dismiss();

                }

            }
        });
        dialog1.show();

    }

}
