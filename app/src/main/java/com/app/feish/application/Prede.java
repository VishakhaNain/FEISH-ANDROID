package com.app.feish.application;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.app.feish.application.model.vitalsignlist;

public class Prede extends AppCompatActivity {
    Button button_s;
    vitalsignlist vitalsignlist;
    EditText et_presname,et_disease_name,et_medname,et_medpower,et_unitquantity,et_totalquantity,et_medesc;
    CheckBox checkBox_m,checkBox_a,checkBox_e,checkBox_n;
    RadioGroup radioGroup_ingestiontime;
    RadioButton radioButton_bm,radioButton_am;
    Spinner medtype;
String str_medtime="";
int pos=0;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addprescription);
        button_s=findViewById(R.id.save);
        medtype=findViewById(R.id.medtype_spinner);
        et_presname=findViewById(R.id.prename);
        et_disease_name=findViewById(R.id.disease_name);
        et_medname=findViewById(R.id.medname);
        et_medpower=findViewById(R.id.medpower);
        et_unitquantity=findViewById(R.id.unitquantity);
        et_totalquantity=findViewById(R.id.totalquantity);
        et_medesc=findViewById(R.id.medesc);
        checkBox_a=findViewById(R.id.checkBox_a);
        checkBox_m=findViewById(R.id.checkBox_m);
        checkBox_e=findViewById(R.id.checkBox_e);
        checkBox_n=findViewById(R.id.checkBox_n);
        radioGroup_ingestiontime=findViewById(R.id.ingestiontime);
        radioButton_bm=findViewById(R.id.radio_bm);
        radioButton_am=findViewById(R.id.radio_am);

        vitalsignlist=(vitalsignlist)getIntent().getSerializableExtra("precriptiodetail");
        pos=getIntent().getIntExtra("postion",0);
        if(vitalsignlist!=null) {

            et_presname.setText(vitalsignlist.getName());
            et_disease_name.setText(vitalsignlist.getRelation());
            et_medname.setText(vitalsignlist.getAge());
            et_unitquantity.setText(vitalsignlist.getDisease());
            et_totalquantity.setText(vitalsignlist.getStatus());
            et_medpower.setText(vitalsignlist.getDesc());
            et_medesc.setText(vitalsignlist.getAdvise());

            if(vitalsignlist.getMed_time().contains("1"))
              checkBox_m.setChecked(true);

            if(vitalsignlist.getMed_time().contains("2"))
                checkBox_a.setChecked(true);

            if(vitalsignlist.getMed_time().contains("3"))
                checkBox_e.setChecked(true);

            if(vitalsignlist.getMed_time().contains("4"))
                checkBox_n.setChecked(true);


            if(vitalsignlist.getMedtype().equals("1"))
            {
                medtype.setSelection(0);
            }
            else
            {
                medtype.setSelection(1);
            }

            if(vitalsignlist.getYear().equals("1"))
            {
                radioButton_bm.setChecked(true);
            }
            else
            {
                radioButton_am.setChecked(true);
            }
        }
        else
        {
            Toast.makeText(this, "null", Toast.LENGTH_SHORT).show();
        }
        medtype.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
radioGroup_ingestiontime.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        if(checkedId==R.id.radio_bm)
        {
                      vitalsignlist.setYear("1");
        }
        else
        {
            vitalsignlist.setYear("2");
        }
    }
});
        button_s.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkBox_m.isChecked())
                {
                    str_medtime=str_medtime+", 1";
                }
                if(checkBox_a.isChecked())
                {
                    str_medtime=str_medtime+", 2";
                }
                if(checkBox_e.isChecked())
                {
                    str_medtime=str_medtime+", 3";

                }
                if(checkBox_n.isChecked())
                {
                    str_medtime=str_medtime+", 4";
                }
                vitalsignlist.setName(et_presname.getText().toString());
                vitalsignlist.setAge(et_medname.getText().toString());
                vitalsignlist.setRelation(et_disease_name.getText().toString());
                vitalsignlist.setDisease(et_unitquantity.getText().toString());
                vitalsignlist.setStatus(et_totalquantity.getText().toString());
                vitalsignlist.setDesc(et_medpower.getText().toString());
                vitalsignlist.setAdvise(et_medesc.getText().toString());
                vitalsignlist.setMed_time(str_medtime);
                if(medtype.getSelectedItem().toString().equals("Liquid"))
                vitalsignlist.setMedtype("0");
                else
                    vitalsignlist.setMedtype("1");

                Intent data = new Intent();
                data.putExtra("precriptiodetail1",vitalsignlist);
                data.putExtra("pos", pos);
                 setResult(RESULT_OK, data);
                finish();
            }
        });
    }
}
