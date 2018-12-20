package com.app.feish.application.Patient;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import com.app.feish.application.Adpter.CustomAdapter;
import com.app.feish.application.R;
import com.app.feish.application.model.searchdoctorpojo;
import com.app.feish.application.modelclassforapi.DoctorDatum;

import java.util.ArrayList;
import java.util.List;

public class SearchDoctorList extends AppCompatActivity {
List<searchdoctorpojo>  strings= new ArrayList<>();
ListView  listView;
Context context=this;
  public static List<DoctorDatum> ls;
CustomAdapter customAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_doctor_list);
       initView();
    }

    private void  initView()
    {

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setTitle("Doctor List");
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        listView=findViewById(R.id.list);
        ls=(List<DoctorDatum>) getIntent().getSerializableExtra("doctordata");

        for (int i = 0; i <ls.size() ; i++)
        {
            strings.add(new searchdoctorpojo(ls.get(i).getUser().getFirstName(),ls.get(i).getService().getAddress()+" "+ls.get(i).getService().getLocality(),ls.get(i).getService().getPhone()) );

        }
        customAdapter= new CustomAdapter(context,strings);
        listView.setAdapter(customAdapter);

    }

}
