package com.app.feish.application.Patient;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import com.app.feish.application.Adpter.SmartFragmentStatePagerAdapter;
import com.app.feish.application.Connectiondetector;
import com.app.feish.application.R;
import com.app.feish.application.fragment.Addreport;
import com.app.feish.application.fragment.Listreport;
import com.app.feish.application.sessiondata.Prefhelper;

public class Report  extends AppCompatActivity {
    Toolbar toolbar;
    TabLayout tabLayout;
    ViewPager vpPager;
    Connectiondetector connectiondetector;
    Context context=this;
    MyPagerAdapter adapterViewPager;
    private static final int REQUEST_CAMERA_PERMISSION=101;
    String[] PERMISSIONS = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA,Manifest.permission.READ_EXTERNAL_STORAGE};
String userid="";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setTitle("Lab Report");
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        if(!hasPermissions(this, PERMISSIONS)){
            ActivityCompat.requestPermissions(this, PERMISSIONS, REQUEST_CAMERA_PERMISSION);
        }
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

            userid = Prefhelper.getInstance(Report.this).getUserid();
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
                    return Addreport.newInstance(0,userid);
                case 1: // Fragment # 0 - This will show FirstFragment different title
                    return Listreport.newInstance(1,userid);
                             default:
                    return null;
            }
        }

        // Returns the page title for the top indicator
        @Override
        public CharSequence getPageTitle(int position)
        {
            if(position==0) {
                return "Add Lab Result";
            }
            else if(position==1) {
                return "List";
            }
            return null;
        }

    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_CAMERA_PERMISSION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if(grantResults[1]==PackageManager.PERMISSION_GRANTED) {

                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Permission Denied!", Toast.LENGTH_SHORT).show();
                }

        }
    }
    public static boolean hasPermissions(Context context, String... permissions) {
        if (context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }
}
