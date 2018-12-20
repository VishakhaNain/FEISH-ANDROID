package com.app.feish.application.Patient;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.app.feish.application.R;
import com.squareup.timessquare.CalendarCellDecorator;
import com.squareup.timessquare.CalendarPickerView;
import com.squareup.timessquare.DefaultDayViewAdapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;

public class PregnancyReport extends AppCompatActivity {

    EditText startdate,enddate,commentforwoemen,complicationforwomen;
    Button submit;
    Spinner spinner_time,Spinner_month;
    CalendarPickerView calendar_s,calendar_e;
    SimpleDateFormat DATE_FORMAT;
    Context context=this;
    Button btn_s,btn_e;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pregnancy_report);
        startdate=findViewById(R.id.startdate);
        enddate=findViewById(R.id.enddate);
        commentforwoemen=findViewById(R.id.commentforwomen);
        complicationforwomen=findViewById(R.id.complicationforwomen);
        spinner_time=findViewById(R.id.time_spinner);
        Spinner_month=findViewById(R.id.month_spinner);
        submit=findViewById(R.id.store);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(PregnancyReport.this,InformationPregnancy.class);
                startActivity(intent);
            }
        });



        DATE_FORMAT = new SimpleDateFormat("dd/MM/yyyy");

        startdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickdate_sdate();
            }
        });
        enddate.setOnClickListener(new View.OnClickListener() {
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
                    startdate.setText(testdate);
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
                    enddate.setText(testdate);
                    dialog1.dismiss();

                }

            }
        });
        dialog1.show();

    }
}
