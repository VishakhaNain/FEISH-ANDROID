package com.app.feish.application.Patient;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.app.feish.application.Adpter.SmartFragmentStatePagerAdapter;
import com.app.feish.application.Connectiondetector;
import com.app.feish.application.R;
import com.app.feish.application.fragment.Addfamilyreport;
import com.app.feish.application.fragment.Addreport;
import com.app.feish.application.fragment.ListFamilyhistory;
import com.app.feish.application.fragment.Listreport;
import com.app.feish.application.sessiondata.Prefhelper;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;

public class FamilyHistory extends AppCompatActivity {
    Toolbar toolbar;
    TabLayout tabLayout;
    ViewPager vpPager;
    Context context=this;
    MyPagerAdapter adapterViewPager;
    String userid="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_family_history);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setTitle("Family History");
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        tabLayout = (TabLayout) findViewById(R.id.tabLayout);

        vpPager = (ViewPager) findViewById(R.id.vpPager);
        adapterViewPager = new MyPagerAdapter(getSupportFragmentManager());
        //  vpPager.setOffscreenPageLimit(1);
        tabLayout.setupWithViewPager(vpPager);
        tabLayout.setSelectedTabIndicatorColor(ContextCompat.getColor(context,R.color.colorAccent));
        vpPager.setAdapter(adapterViewPager);
        if(getIntent()!=null)
        {
            userid=getIntent().getStringExtra("userid");
        }
        else {

            userid = Prefhelper.getInstance(FamilyHistory.this).getUserid();
        }
    }
    class MyPagerAdapter extends SmartFragmentStatePagerAdapter
    {
        private  int NUM_ITEMS = 2;

        public MyPagerAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
        }

        // Returns total number of pages
        @Override
        public int getCount() {
            return NUM_ITEMS;
        }

        // Returns the fragment to display for that page
        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0: // Fragment # 0 - This will show FirstFragment
                    return Addfamilyreport.newInstance(0,userid);
                case 1: // Fragment # 0 - This will show FirstFragment different title
                    return ListFamilyhistory.newInstance(1,userid);
                default:
                    return null;
            }
        }

        // Returns the page title for the top indicator
        @Override
        public CharSequence getPageTitle(int position)
        {
            if(position==0) {
                return "Family History";
            }
            else if(position==1) {
                return "List";
            }
            return null;
        }

    }

}
