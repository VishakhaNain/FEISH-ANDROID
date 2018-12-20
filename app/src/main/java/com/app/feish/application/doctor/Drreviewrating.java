package com.app.feish.application.doctor;

import android.content.Context;
import android.graphics.Color;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.app.feish.application.Connectiondetector;
import com.app.feish.application.R;
import com.app.feish.application.Remote.ApiUtils;
import com.app.feish.application.model.pojorating;
import com.app.feish.application.sessiondata.Prefhelper;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import me.zhanghai.android.materialratingbar.MaterialRatingBar;

import static com.app.feish.application.doctor.SetupProfileForDoctor.JSON;

public class Drreviewrating extends AppCompatActivity {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;
    int idofreview=0;
    MaterialRatingBar materialRatingBar;
    EditText editText;
    TextView textView;
    String str_review="";
    float userrating;
    Context context=this;
    RelativeLayout relativeLayout;
    Connectiondetector connectiondetector;
  static   int doc_id=0;
  static   int id=0;
  int posno=0,negno=0;
     @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drreviewrating);
        relativeLayout=findViewById(R.id.header);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setTitle("Review and Rating");
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        doc_id=getIntent().getIntExtra("doc_id",0);
        if(doc_id==0) {
            id=Integer.parseInt(Prefhelper.getInstance(context).getUserid());
            relativeLayout.setVisibility(View.GONE);
        }
        else
        {
            id=doc_id;
        }
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        connectiondetector= new Connectiondetector(getApplicationContext());
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        materialRatingBar=findViewById(R.id.ratingbar);
        editText=findViewById(R.id.review);
        textView=findViewById(R.id.reviewsubmit);
        materialRatingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                userrating=rating;
            }
        });
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                str_review=editText.getText().toString();

if(connectiondetector.isConnectingToInternet())
{
    insertingdrreview();
}
   else
{
    Toast.makeText(context, "No Internet", Toast.LENGTH_SHORT).show();

}

            }
        });
            if (connectiondetector.isConnectingToInternet()) {
                fetchinggdrreview(id);
            } else {
                Toast.makeText(context, "No Internet", Toast.LENGTH_SHORT).show();
            }


        // Set up the ViewPager with the sections adapter.
        mViewPager =findViewById(R.id.container);

        TabLayout tabLayout =findViewById(R.id.tabs);

        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        //tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));
tabLayout.setupWithViewPager(mViewPager);


       /* TextView tv = (TextView)(((LinearLayout)((LinearLayout)tabLayout.getChildAt(0)).getChildAt(0)).getChildAt(0));
        tv.setText("Hello world!"+"\n"+ 30);
        TextView tv1 = (TextView)(((LinearLayout)((LinearLayout)tabLayout.getChildAt(0)).getChildAt(1)).getChildAt(0));
        tv1.setText("Hello world!");*/


    }
    private Request FetchReview(int id)
    {
        JSONObject postdata = new JSONObject();
        try {
            postdata.put("doctor_id", id);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(JSON, postdata.toString());
        return new Request.Builder()
                .addHeader("X-Api-Key", "AB5433GMDF657VBB")
                .addHeader("Content-Type", "application/json")
                .url(ApiUtils.BASE_URL+"fetchdoctorating")
                .post(body)
                .build();
    }
    private void fetchinggdrreview(int id)
    {
        OkHttpClient client = new OkHttpClient();
        Request request = FetchReview(id);
        Log.i("", "onClick: "+request);
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                Log.i("Activity", "onFailure: Fail");
            }
            @Override
            public void onResponse(final Response response) throws IOException {

                final String body=response.body().string();
                Log.i("1234check", "onResponse: "+body);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                         //   Toast.makeText(context, ""+body, Toast.LENGTH_SHORT).show();
                            JSONObject jsonObject=new JSONObject(body);

                            if(jsonObject.getInt("Success")==1)
                            {
                                JSONArray jsonArray=jsonObject.optJSONArray("data");
                                for (int i = 0; i <jsonArray.length() ; i++) {
                                    JSONObject jsonObject1=jsonArray.getJSONObject(i);
                                    JSONObject jsonObjectrating= jsonObject1.getJSONObject("Review");
                                 //   JSONObject jsonObjectuser= jsonObject1.getJSONObject("User");
                                   if(jsonObjectrating.getString("user_id").equals(Prefhelper.getInstance(context).getUserid()))
                                   {
                                       idofreview=Integer.parseInt(jsonObjectrating.getString("id"));
                                       editText.setText(jsonObjectrating.getString("review"));
                                       materialRatingBar.setRating(Float.parseFloat(jsonObjectrating.getString("rating")));

                                   }

                                    float rating=Float.parseFloat(jsonObjectrating.getString("rating"));
                                    if(rating>=3)
                                        posno++;
                                        else
                                            negno++;

                                }
                                mViewPager.setAdapter(mSectionsPagerAdapter);


                            }
                            else
                            {
                                Toast.makeText(context, "No Review ", Toast.LENGTH_SHORT).show();
                            }

                        }
                        catch (Exception e)
                        {
                            Toast.makeText(context, ""+e, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }

        });

    }



    private Request AddReview()
    {
        JSONObject postdata = new JSONObject();
        try {
            postdata.put("user_id", Prefhelper.getInstance(context).getUserid());
            postdata.put("doctor_id", doc_id);
            postdata.put("id", idofreview);
            postdata.put("rating", userrating);
            postdata.put("review",str_review);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(JSON, postdata.toString());
        return new Request.Builder()
                .addHeader("X-Api-Key", "AB5433GMDF657VBB")
                .addHeader("Content-Type", "application/json")
                .url(ApiUtils.BASE_URL+"adddoctorrating")
                .post(body)
                .build();
    }
    private void insertingdrreview()
    {
        OkHttpClient client = new OkHttpClient();
        Request request = AddReview();
        Log.i("", "onClick: "+request);
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                Log.i("Activity", "onFailure: Fail");
            }
            @Override
            public void onResponse(final Response response) throws IOException {

                final String body=response.body().string();
                Log.i("1234check", "onResponse: "+body);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                     if(body.contains(":1"))
                     {
                         Toast.makeText(context, "rating given successfully", Toast.LENGTH_SHORT).show();
                     }

                       /* try {
                            JSONObject jsonObject=new JSONObject(body);

                            if(jsonObject.getInt("Success")==1)
                            {

                            }
                            else
                            {
                                Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show();
                            }

                        }
                        catch (Exception e)
                        {
                            Toast.makeText(context, ""+e, Toast.LENGTH_SHORT).show();
                        }*/
                    }
                });
            }

        });

    }



    /**
     * A placeholder fragment containing a simple view.
     */

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        private SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            if(position==0)
            return PlaceholderFragment.newInstance(position + 1);
            else
                return NegativeFeedback.newInstance(position + 1);

        }
        @Override
        public CharSequence getPageTitle(int position)
        {
            if(position==0) {
                return "Positive Review"+"\n"+ posno;
            }
            else if(position==1) {
                return "Negative Review"+"\n"+ negno;
            }

            return null;
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 2;
        }
    }
    public static class PlaceholderFragment extends Fragment {
        ArrayList<pojorating> pojoratings= new ArrayList<>();
        Ratingadt ratingadt;
        Connectiondetector connectiondetector;
        RecyclerView recyclerView;
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_drreviewrating, container, false);
            recyclerView = (RecyclerView) rootView.findViewById(R.id.recylerview);
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
            recyclerView.setLayoutManager(mLayoutManager);
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            container= new RelativeLayout(getActivity());
            connectiondetector=new Connectiondetector(getActivity());
            if(connectiondetector.isConnectingToInternet()) {
                if(doc_id!=0)
                    fetchinggdrreview(id);
                else
                    fetchinggdrreviewfordoc(id);
            }
            else
                Toast.makeText(getActivity(), "No Internet", Toast.LENGTH_SHORT).show();

            return rootView;
        }
        private Request FetchReview(int id)
        {
            JSONObject postdata = new JSONObject();
            try {
                postdata.put("doctor_id", id);

            } catch (JSONException e) {
                e.printStackTrace();
            }
            RequestBody body = RequestBody.create(JSON, postdata.toString());
            return new Request.Builder()
                    .addHeader("X-Api-Key", "AB5433GMDF657VBB")
                    .addHeader("Content-Type", "application/json")
                    .url(ApiUtils.BASE_URL+"fetchdoctorating")
                    .post(body)
                    .build();
        }
        private void fetchinggdrreview(int id)
        {
            OkHttpClient client = new OkHttpClient();
            Request request = FetchReview(id);
            Log.i("", "onClick: "+request);
            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Request request, IOException e) {
                    Log.i("Activity", "onFailure: Fail");
                }
                @Override
                public void onResponse(final Response response) throws IOException {

                    final String body=response.body().string();
                    Log.i("1234check", "onResponse: "+body);
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                JSONObject jsonObject=new JSONObject(body);

                                if(jsonObject.getInt("Success")==1)
                                {
                                    JSONArray jsonArray=jsonObject.optJSONArray("data");
                                    for (int i = 0; i <jsonArray.length() ; i++) {
                                        JSONObject jsonObject1=jsonArray.getJSONObject(i);
                                        JSONObject jsonObjectrating= jsonObject1.getJSONObject("Review");
                                        JSONObject jsonObjectuser= jsonObject1.getJSONObject("User");


                                        float rating=Float.parseFloat(jsonObjectrating.getString("rating"));
                                        if(rating>=3)
                                        pojoratings.add(new pojorating(Integer.parseInt(jsonObjectrating.getString("id")),jsonObjectuser.getString("first_name")+""+jsonObjectuser.getString("last_name"),jsonObjectrating.getString("rating"),"",jsonObjectrating.getString("review"),""));

                                        ratingadt= new Ratingadt(pojoratings,getActivity(),doc_id);
                                        recyclerView.setAdapter(ratingadt);

                                    }

                                }
                                else
                                {
                                    Toast.makeText(getActivity(), "Error", Toast.LENGTH_SHORT).show();
                                }

                            }
                            catch (Exception e)
                            {
                                Toast.makeText(getActivity(), ""+e, Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }

            });

        }
        private Request FetchReviewfordoc(int id)
        {
            JSONObject postdata = new JSONObject();
            try {
                postdata.put("doctor_id", id);

            } catch (JSONException e) {
                e.printStackTrace();
            }
            RequestBody body = RequestBody.create(JSON, postdata.toString());
            return new Request.Builder()
                    .addHeader("X-Api-Key", "AB5433GMDF657VBB")
                    .addHeader("Content-Type", "application/json")
                    .url(ApiUtils.BASE_URL+"fetchdoctoratingfordoctor")
                    .post(body)
                    .build();
        }
        private void fetchinggdrreviewfordoc(int id)
        {
            OkHttpClient client = new OkHttpClient();
            Request request = FetchReviewfordoc(id);
            Log.i("", "onClick: "+request);
            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Request request, IOException e) {
                    Log.i("Activity", "onFailure: Fail");
                }
                @Override
                public void onResponse(final Response response) throws IOException {

                    final String body=response.body().string();
                    Log.i("1234check", "onResponse: "+body);
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                JSONObject jsonObject=new JSONObject(body);

                                if(jsonObject.getInt("Success")==1)
                                {
                                    JSONArray jsonArray=jsonObject.optJSONArray("data");
                                    for (int i = 0; i <jsonArray.length() ; i++) {
                                        JSONObject jsonObject1=jsonArray.getJSONObject(i);
                                        JSONObject jsonObjectrating= jsonObject1.getJSONObject("Review");
                                        JSONObject jsonObjectuser= jsonObject1.getJSONObject("User");


                                        float rating=Float.parseFloat(jsonObjectrating.getString("rating"));
                                        if(rating>=3&& Integer.parseInt(jsonObjectrating.getString("is_published"))==1)
                                            pojoratings.add(new pojorating(Integer.parseInt(jsonObjectrating.getString("id")),jsonObjectuser.getString("first_name")+""+jsonObjectuser.getString("last_name"),jsonObjectrating.getString("rating"),"",jsonObjectrating.getString("review"),"Published"));
                                         else  if(rating>=3&& Integer.parseInt(jsonObjectrating.getString("is_published"))==0)
                                            pojoratings.add(new pojorating(Integer.parseInt(jsonObjectrating.getString("id")),jsonObjectuser.getString("first_name")+""+jsonObjectuser.getString("last_name"),jsonObjectrating.getString("rating"),"",jsonObjectrating.getString("review"),"Unpublished"));

                                        ratingadt= new Ratingadt(pojoratings,getActivity(),doc_id);
                                        recyclerView.setAdapter(ratingadt);

                                    }

                                }
                                else
                                {
                                    Toast.makeText(getActivity(), "Error", Toast.LENGTH_SHORT).show();
                                }

                            }
                            catch (Exception e)
                            {
                                Toast.makeText(getActivity(), ""+e, Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }

            });

        }


    }

    public static class NegativeFeedback extends Fragment {
        ArrayList<pojorating> pojoratings= new ArrayList<>();
        Ratingadt ratingadt;
        RecyclerView recyclerView;
        Connectiondetector connectiondetector;
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        public NegativeFeedback() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static NegativeFeedback newInstance(int sectionNumber) {
            NegativeFeedback fragment = new NegativeFeedback();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_drreviewrating, container, false);
            recyclerView =  rootView.findViewById(R.id.recylerview);
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
            recyclerView.setLayoutManager(mLayoutManager);
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            connectiondetector= new Connectiondetector(getActivity());
            if(connectiondetector.isConnectingToInternet())
            {
                if(doc_id!=0)
                    fetchinggdrreview(id);
                else
                    fetchinggdrreviewfordoc(id);
            }
            else
                Toast.makeText(getActivity(), "No Internet", Toast.LENGTH_SHORT).show();
       //     pojoratings.add(new pojorating("Patient Name","1","02/06/2018","Bad","Patient"));

            return rootView;
        }
        private Request FetchReview(int id)
        {
            JSONObject postdata = new JSONObject();
            try {
                postdata.put("doctor_id", id);

            } catch (JSONException e) {
                e.printStackTrace();
            }
            RequestBody body = RequestBody.create(JSON, postdata.toString());
            return new Request.Builder()
                    .addHeader("X-Api-Key", "AB5433GMDF657VBB")
                    .addHeader("Content-Type", "application/json")
                    .url(ApiUtils.BASE_URL+"fetchdoctorating")
                    .post(body)
                    .build();
        }
        private void fetchinggdrreview(int id)
        {
            OkHttpClient client = new OkHttpClient();
            Request request = FetchReview(id);
            Log.i("", "onClick: "+request);
            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Request request, IOException e) {
                    Log.i("Activity", "onFailure: Fail");
                }
                @Override
                public void onResponse(final Response response) throws IOException {

                    final String body=response.body().string();
                    Log.i("1234check", "onResponse: "+body);
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                JSONObject jsonObject=new JSONObject(body);

                                if(jsonObject.getInt("Success")==1)
                                {
                                    JSONArray jsonArray=jsonObject.optJSONArray("data");
                                    for (int i = 0; i <jsonArray.length() ; i++) {
                                        JSONObject jsonObject1=jsonArray.getJSONObject(i);
                                        JSONObject jsonObjectrating= jsonObject1.getJSONObject("Review");
                                        JSONObject jsonObjectuser= jsonObject1.getJSONObject("User");


                                        float rating=Float.parseFloat(jsonObjectrating.getString("rating"));
                                        if(rating<3)
                                            pojoratings.add(new pojorating(Integer.parseInt(jsonObjectrating.getString("id")),jsonObjectuser.getString("first_name")+""+jsonObjectuser.getString("last_name"),jsonObjectrating.getString("rating"),"",jsonObjectrating.getString("review"),""));

                                        ratingadt= new Ratingadt(pojoratings,getActivity(),doc_id);
                                        recyclerView.setAdapter(ratingadt);

                                }

                                }
                                else
                                {
                                    Toast.makeText(getActivity(), "Error", Toast.LENGTH_SHORT).show();
                                }

                            }
                            catch (Exception e)
                            {
                                Toast.makeText(getActivity(), ""+e, Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }

            });

        }

        private Request FetchReviewfordoc(int id)
        {
            JSONObject postdata = new JSONObject();
            try {
                postdata.put("doctor_id", id);

            } catch (JSONException e) {
                e.printStackTrace();
            }
            RequestBody body = RequestBody.create(JSON, postdata.toString());
            return new Request.Builder()
                    .addHeader("X-Api-Key", "AB5433GMDF657VBB")
                    .addHeader("Content-Type", "application/json")
                    .url(ApiUtils.BASE_URL+"fetchdoctoratingfordoctor")
                    .post(body)
                    .build();
        }
        private void fetchinggdrreviewfordoc(int id)
        {
            OkHttpClient client = new OkHttpClient();
            Request request = FetchReviewfordoc(id);
            Log.i("", "onClick: "+request);
            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Request request, IOException e) {
                    Log.i("Activity", "onFailure: Fail");
                }
                @Override
                public void onResponse(final Response response) throws IOException {

                    final String body=response.body().string();
                    Log.i("1234check", "onResponse: "+body);
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                JSONObject jsonObject=new JSONObject(body);

                                if(jsonObject.getInt("Success")==1)
                                {
                                    JSONArray jsonArray=jsonObject.optJSONArray("data");
                                    for (int i = 0; i <jsonArray.length() ; i++) {
                                        JSONObject jsonObject1=jsonArray.getJSONObject(i);
                                        JSONObject jsonObjectrating= jsonObject1.getJSONObject("Review");
                                        JSONObject jsonObjectuser= jsonObject1.getJSONObject("User");


                                        float rating=Float.parseFloat(jsonObjectrating.getString("rating"));
                                        if(rating<3&& Integer.parseInt(jsonObjectrating.getString("is_published"))==1)
                                            pojoratings.add(new pojorating(Integer.parseInt(jsonObjectrating.getString("id")),jsonObjectuser.getString("first_name")+""+jsonObjectuser.getString("last_name"),jsonObjectrating.getString("rating"),"",jsonObjectrating.getString("review"),"Published"));
                                        else  if(rating<3&& Integer.parseInt(jsonObjectrating.getString("is_published"))==0)
                                            pojoratings.add(new pojorating(Integer.parseInt(jsonObjectrating.getString("id")),jsonObjectuser.getString("first_name")+""+jsonObjectuser.getString("last_name"),jsonObjectrating.getString("rating"),"",jsonObjectrating.getString("review"),"Unpublished"));

                                        ratingadt= new Ratingadt(pojoratings,getActivity(),doc_id);
                                        recyclerView.setAdapter(ratingadt);

                                    }

                                }
                                else
                                {
                                    Toast.makeText(getActivity(), "Error", Toast.LENGTH_SHORT).show();
                                }

                            }
                            catch (Exception e)
                            {
                                Toast.makeText(getActivity(), ""+e, Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }

            });

        }

    }
}
