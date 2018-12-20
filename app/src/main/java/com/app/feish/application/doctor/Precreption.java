package com.app.feish.application.doctor;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.app.feish.application.Adpter.SmartFragmentStatePagerAdapter;
import com.app.feish.application.R;
import com.app.feish.application.fragment.Addfamilyreport;
import com.app.feish.application.fragment.Addpre;
import com.app.feish.application.fragment.ListFamilyhistory;
import com.app.feish.application.fragment.Listpre;

public class Precreption extends AppCompatActivity {
    Toolbar toolbar;
    TabLayout tabLayout;
    ViewPager vpPager;
    Context context=this;
    MyPagerAdapter adapterViewPager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_family_history);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setTitle("Prescription");
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
    }
    public static class MyPagerAdapter extends SmartFragmentStatePagerAdapter
    {
        private static int NUM_ITEMS = 2;

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
                    return Addpre.newInstance(0,"Institute");
                case 1: // Fragment # 0 - This will show FirstFragment different title
                    return Listpre.newInstance(1,"Institute");
                default:
                    return null;
            }
        }

        // Returns the page title for the top indicator
        @Override
        public CharSequence getPageTitle(int position)
        {
            if(position==0) {
                return "Add Prescription";
            }
            else if(position==1) {
                return "List";
            }
            return null;
        }

    }

}
