package com.app.feish.application.Patient;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.app.feish.application.R;
import com.app.feish.application.sessiondata.Prefhelper;

public class InformationPregnancy extends AppCompatActivity {
    Button adddietplan,viewworkout;
    Button consultdoc,viewconsultreport,addremainder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information_pregnancy);
        adddietplan=findViewById(R.id.dietplan);
        consultdoc=findViewById(R.id.consultdoc);
        viewconsultreport=findViewById(R.id.viewconsultreport);
        addremainder=findViewById(R.id.remainderadd);
        viewworkout=findViewById(R.id.viewworkout);
        adddietplan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(InformationPregnancy.this,Createdietplan.class);
                intent.putExtra("userid", Prefhelper.getInstance(InformationPregnancy.this).getUserid());
                startActivity(intent);
            }
        });
viewworkout.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        Intent intent=new Intent(InformationPregnancy.this,ViewWorkout.class);
        startActivity(intent);




    }
});

addremainder.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        Intent intent=new Intent(InformationPregnancy.this,Remainder.class);
        startActivity(intent);
    }
});

viewconsultreport.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        Intent intent=new Intent(InformationPregnancy.this,MyBookedappointment.class);
        startActivity(intent);
    }
});

        consultdoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(InformationPregnancy.this,ConsultDoctor.class);
                startActivity(intent);
            }
        });
    }
}
