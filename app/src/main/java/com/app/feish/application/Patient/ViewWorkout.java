package com.app.feish.application.Patient;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

import com.app.feish.application.R;
import com.squareup.timessquare.CalendarCellDecorator;
import com.squareup.timessquare.CalendarPickerView;
import com.squareup.timessquare.DefaultDayViewAdapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;

public class ViewWorkout extends AppCompatActivity {
    Button additionalinfo;
    EditText startdatee, starttime, endtime;
    com.takisoft.datetimepicker.widget.TimePicker timePicker;
    CalendarPickerView calendar_s, calendar_e;
    SimpleDateFormat DATE_FORMAT, DATE_FORMAT_HH;
    Button btn_s, btn_e;
    Button btn_time;
    Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_workout);
        additionalinfo = findViewById(R.id.additionalinfo);
        starttime = findViewById(R.id.starttime);
        endtime = findViewById(R.id.endtime);
        DATE_FORMAT_HH = new SimpleDateFormat("dd");
        startdatee = findViewById(R.id.startdatee);
        additionalinfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ViewWorkout.this, AdditionalInfoWorkout.class);
                startActivity(intent);
            }
        });

        DATE_FORMAT = new SimpleDateFormat("dd/MM/yyyy");

        startdatee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickdate_sdate();
            }
        });

        starttime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickdate_time(0);
            }
        });
        endtime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickendtime(0);
            }
        });


    }

    void pickdate_sdate() {
        final Dialog dialog1 = new Dialog(context);
        dialog1.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog1.setContentView(R.layout.camgal);
        dialog1.setCanceledOnTouchOutside(false);
        final Calendar nextYear = Calendar.getInstance();
        nextYear.add(Calendar.YEAR, 1);
        final Calendar lastYear = Calendar.getInstance();
        lastYear.add(Calendar.YEAR, -1);
        btn_s = (Button) dialog1.findViewById(R.id.ok);
        calendar_s = (CalendarPickerView) dialog1.findViewById(R.id.calendar_view);
        calendar_s.init(lastYear.getTime(), nextYear.getTime()) //
                .inMode(CalendarPickerView.SelectionMode.SINGLE) //
                .withSelectedDate(new Date());
        calendar_s.setCustomDayView(new DefaultDayViewAdapter());
        Date today = new Date();
        calendar_s.setDecorators(Collections.<CalendarCellDecorator>emptyList());
        calendar_s.init(today, nextYear.getTime())
                .withSelectedDate(today).inMode(CalendarPickerView.SelectionMode.SINGLE);
        btn_s.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayList<Date> mydates = (ArrayList<Date>) calendar_s.getSelectedDates();
                for (int i = 0; i < mydates.size(); i++) {
                    Date tempdate = mydates.get(i);
                    String testdate = DATE_FORMAT.format(tempdate);
                    startdatee.setText(testdate);
                    dialog1.dismiss();

                }

            }
        });
        dialog1.show();

    }

    void pickdate_time(final int flag)
    {
        final Dialog dialog1 = new Dialog(context);
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
                if(flag==0)
                {
                    if(String.valueOf(timePicker.getHour()).length()==1)
                    {
                        if (String.valueOf(timePicker.getMinute()).equals("0")) {
                            starttime.setText("0" + timePicker.getHour() + ":" + "00" + ":" + "00");

                        }
                        else {
                            starttime.setText("0" + timePicker.getHour() + ":" + timePicker.getMinute() + ":" + "00");

                        }
                    }
                    else
                    {
                        if (String.valueOf(timePicker.getMinute()).equals("0")) {
                            starttime.setText(timePicker.getHour() + ":" + "00" + ":" + "00");

                        }
                        else {
                            starttime.setText(timePicker.getHour() + ":" + timePicker.getMinute() + ":" + "00");

                        }
                    }
                }

                dialog1.dismiss();
            }
        });
        dialog1.show();

    }

    void pickendtime(final int flag)
    {
        final Dialog dialog1 = new Dialog(context);
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
                if(flag==0)
                {
                    if(String.valueOf(timePicker.getHour()).length()==1)
                    {
                        if (String.valueOf(timePicker.getMinute()).equals("0")) {
                            endtime.setText("0" + timePicker.getHour() + ":" + "00" + ":" + "00");

                        }
                        else {
                            endtime.setText("0" + timePicker.getHour() + ":" + timePicker.getMinute() + ":" + "00");

                        }
                    }
                    else
                    {
                        if (String.valueOf(timePicker.getMinute()).equals("0")) {
                            endtime.setText(timePicker.getHour() + ":" + "00" + ":" + "00");

                        }
                        else {
                            endtime.setText(timePicker.getHour() + ":" + timePicker.getMinute() + ":" + "00");

                        }
                    }
                }

                dialog1.dismiss();
            }
        });
        dialog1.show();

    }



}










