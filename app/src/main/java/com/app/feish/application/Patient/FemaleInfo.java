package com.app.feish.application.Patient;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.app.feish.application.R;

public class FemaleInfo extends AppCompatActivity {
    TextView female,pragnancy,femalereport,femalecyclereport,pregnancyreport;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_female_info);
        female = findViewById(R.id.femalecycle);
        pragnancy = findViewById(R.id.pragnancy);
        femalecyclereport = findViewById(R.id.femalecyclereport);
        pregnancyreport = findViewById(R.id.pregnancyreport);


        female.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FemaleInfo.this, Female.class);
                startActivity(intent);
            }
        });

        pragnancy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FemaleInfo.this, PregnancyReport.class);
                startActivity(intent);

            }
        });
        pregnancyreport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FemaleInfo.this, ListViewOfPregnnacyReport.class);
                startActivity(intent);
            }
        });

        femalecyclereport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FemaleInfo.this, MenstruationReport.class);
                startActivity(intent);
            }
        });

    }
}
