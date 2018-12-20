package com.app.feish.application.clinic;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.app.feish.application.R;
import com.app.feish.application.doctor.AddWorkingHours;
import com.app.feish.application.doctor.ServiceDetailActivity;
import com.app.feish.application.modelclassforapi.ServiceData;

public class ClinicWorkingHours extends AppCompatActivity {
TextView clinicworking;
    ServiceData serviceData;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clinic_working_hours);
        clinicworking=findViewById(R.id.clinicworking);
        clinicworking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(ClinicWorkingHours.this, AddWorkingHours.class);
                startActivity(intent);
            }
        });
    }
}
