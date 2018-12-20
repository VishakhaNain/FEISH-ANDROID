package com.app.feish.application.clinic;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.app.feish.application.R;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DoctorsEntry  extends AppCompatActivity {

    ArrayList<listviewpojo> personNames=new ArrayList<>();
    RecyclerView recyclerView;
    String[] arrayList;
    Spinner spinner;
TextView addbtn;

    ListAdapter listAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.doctors_entry);
        addbtn=findViewById(R.id.addbtn);

        addbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(DoctorsEntry.this,ClinicDoctorSignup.class);
                startActivity(intent);
            }
        });
       /* arrayList = getResources().getStringArray(R.array.number_spinner);

        Log.d("Array",arrayList[0].toString());
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view2);

        spinner=findViewById(R.id.clinic_spinner);
         listAdapter = new ListAdapter(DoctorsEntry.this, personNames);



        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if(personNames.size()>0){
                    personNames.clear();
                }

                for (int i = 0; i <Integer.parseInt(arrayList[position]) ; i++) {
                    personNames.add(new listviewpojo("name","email",R.drawable.doctor,0));
                }
                recyclerView.setAdapter(listAdapter);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });



        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(linearLayoutManager);




    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if(resultCode == RESULT_OK) {
                String name = data.getStringExtra("firstname");
                String email = data.getStringExtra("email");
                int pos=data.getIntExtra("pos",0);
            //    Toast.makeText(this, ""+email+name, Toast.LENGTH_SHORT).show();
                personNames.set(pos,new listviewpojo(name,email,R.drawable.doctor,1));
                listAdapter.notifyDataSetChanged();

            }
        }*/
    }
}



