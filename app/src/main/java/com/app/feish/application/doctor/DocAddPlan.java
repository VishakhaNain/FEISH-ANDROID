package com.app.feish.application.doctor;

import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.app.feish.application.Adpter.SmartFragmentStatePagerAdapter;
import com.app.feish.application.R;
import com.app.feish.application.fragment.AddPatient;
import com.app.feish.application.fragment.ListPatient;

public class DocAddPlan extends AppCompatActivity {
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;
    Toolbar toolbar;
    String user_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doc_create_plan);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Patient");
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        user_id=getIntent().getStringExtra("user_id");
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager(),user_id);

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.setCurrentItem(1);


        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);

        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater= getMenuInflater();
        menuInflater.inflate(R.menu.menusearch,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==R.id.search)
        {

        }
       return  true;
    }

    public static class SectionsPagerAdapter extends SmartFragmentStatePagerAdapter {
        private static int NUM_ITEMS = 2;
        String user_id;

        public SectionsPagerAdapter(FragmentManager fm,String user_id) {
            super(fm);
            this.user_id=user_id;
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0: // Fragment # 0 - This will show FirstFragment
                    return AddPatient.newInstance(0,user_id);
                case 1: // Fragment # 0 - This will show FirstFragment different title
                    return ListPatient.newInstance(1,user_id);
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return NUM_ITEMS;
        }
        @Override
        public CharSequence getPageTitle(int position)
        {
            if(position==0) {
                return "Add Assistant";
            }
            else if(position==1) {
                return "List Assistant";
            }
            return null;
        }
    }

}
