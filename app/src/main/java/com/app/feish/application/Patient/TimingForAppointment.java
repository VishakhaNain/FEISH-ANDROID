package com.app.feish.application.Patient;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.app.feish.application.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class TimingForAppointment extends AppCompatActivity {

    Button btn_time;
    private Button mTimeButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timing_for_appointment);
        GridView gv = (GridView) findViewById(R.id.gv);
        btn_time = findViewById(R.id.time_slot_ok);
        final TextView tv = (TextView) findViewById(R.id.tv);
        final String[] plants = new String[]{
                "09:00 AM",
                "09:05 AM",
                "09:10 AM",
                "09:15 AM",
                "09:20 AM",
                "09:25 AM",
                "09:30 AM",
                "09:35 AM",
                "09:40 AM",
                "09:45 AM",
                "09:50 AM",
                "09:55 AM",
                "10:00 AM",
                "10:05 AM",
                "10:10 AM",
                "10:15 AM",
                "10:20 AM",
                "10:25 AM",
                "10:30 AM",
                "10:35 AM",
                "10:40 AM",
                "10:45 AM",
                "10:50 AM",
                "10:55 AM",
                "11:00 AM",
                "11:05 AM",
                "11:10 AM",
                "11:15 AM",
                "11:20 AM",
                "11:25 AM",
                "11:30 AM",
                "11:35 AM",
                "11:40 AM",
                "11:45 AM",
                "11:50 AM",
                "11:55 AM",
                "12:00 PM",
                "12:05 PM",
                "12:10 PM",
                "12:15 PM",
                "12:20 PM",
                "12:25 PM",
                "12:30 PM",
                "12:35 PM",
                "12:40 PM",
                "12:45 PM",
                "12:50 PM",
                "12:55 PM",
                "01:00 PM",
                "01:05 PM",
                "01:10 PM",
                "01:15 PM",
                "01:20 PM",
                "01:25 PM",
                "01:30 PM",
                "01:35 PM",
                "01:40 PM",
                "01:45 PM",
                "01:50 PM",
                "01:55 PM",
                "02:00 PM",
                "02:05 PM",
                "02:10 PM",
                "02:15 PM",
                "02:20 PM",
                "02:25 PM",
                "02:30 PM",
                "02:35 PM",
                "02:40 PM",
                "02:45 PM",
                "02:50 PM",
                "02:55 PM",
                "03:00 PM",
                "03:05 PM",
                "03:10 PM",
                "03:15 PM",
                "03:20 PM",
                "03:25 PM",
                "03:30 PM",
                "03:35 PM",
                "03:40 PM",
                "03:45 PM",
                "03:50 PM",
                "03:55 PM",
                "04:00 PM",
                "04:05 PM",
                "04:10 PM",
                "04:15 PM",
                "04:20 PM",
                "04:25 PM",
                "04:30 PM",
                "04:35 PM",
                "04:40 PM",
                "04:45 PM",
                "04:50 PM",
                "04:55 PM",
                "05:05 PM",
                "05:10 PM",
                "05:15 PM",
                "05:20 PM",
                "05:25 PM",
                "05:30 PM",
                "05:35 PM",
                "05:40 PM",
                "05:45 PM",
                "05:50 PM",
                "05:55 PM",
                "06:00 PM",
                "06:05 PM",
                "06:10 PM",
                "06:15 PM",
                "06:20 PM",
                "06:25 PM",
                "06:30 PM",
                "06:50 PM",
                "06:55 PM",
                "07:00 PM"
        };

        final List<String> plantsList = new ArrayList<String>(Arrays.asList(plants));

        // Create a new ArrayAdapter
        final ArrayAdapter<String> gridViewArrayAdapter = new ArrayAdapter<String>
                (this,android.R.layout.simple_list_item_1, plantsList);

        // Data bind GridView with ArrayAdapter (String Array elements)
        gv.setAdapter(gridViewArrayAdapter);

        gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Get the GridView selected/clicked item text
                String selectedItem = parent.getItemAtPosition(position).toString();
                Intent intent = getIntent();
                intent.putExtra("timing",selectedItem);
               // mTimeButton = BookNewAppointment.mTimeButton;
               // BookNewAppointment.mTimeButton.setText(selectedItem);
            }

        });

        btn_time = findViewById(R.id.time_slot_ok);
        btn_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String a ="plants";
                finish();
            }
        });
    }
}
