package com.app.feish.application.Patient;

import android.content.DialogInterface;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.content.Context;
import android.support.v7.widget.ThemedSpinnerAdapter;
import android.content.res.Resources.Theme;

import android.widget.TextView;
import android.widget.Toast;

import com.app.feish.application.Adpter.CustomAdapter_blooddonorlist;
import com.app.feish.application.Connectiondetector;
import com.app.feish.application.R;
import com.app.feish.application.Remote.ApiUtils;
import com.app.feish.application.model.bookedappointmentpatient;
import com.app.feish.application.model.doctormsglist;
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
import java.util.List;

import static com.app.feish.application.fragment.ListFamilyhistory.JSON;

public class BloodDonor extends AppCompatActivity {
Context context=this;
String bloodgroup;
Connectiondetector connectiondetector;
CardView blooddonor;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blood_donor);
        blooddonor=findViewById(R.id.bloddonor);
connectiondetector= new Connectiondetector(getApplicationContext());
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {@Override public void onClick(View v) { finish(); }});
        getSupportActionBar().setDisplayShowTitleEnabled(false);
         bloodgroup=getIntent().getStringExtra("bloodgroup");
        // Setup spinner
        Spinner spinner = (Spinner) findViewById(R.id.spinner);
        spinner.setAdapter(new MyAdapter(
                toolbar.getContext(),
                new String[]{
                        "Find Blood Donar/Bank ",
                        "Become Blood Donor",

                }));

        spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // When the given dropdown item is selected, show its contents in the
                // container view.
                if(position==0)
                {

                    PlaceholderFragment placeholderFragment=PlaceholderFragment.newInstance(position + 1);
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.container, placeholderFragment)
                            .commit();
                }
                else
                {
                    Bundle bundle= new Bundle();
                    bundle.putString("bloodgroup",bloodgroup);
                    BecomeBloodDonor becomeBloodDonor=BecomeBloodDonor.newInstance(position + 1);
                    becomeBloodDonor.setArguments(bundle);
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.container, becomeBloodDonor)
                            .commit();
                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

    }

    private static class MyAdapter extends ArrayAdapter<String> implements ThemedSpinnerAdapter {
        private final ThemedSpinnerAdapter.Helper mDropDownHelper;

        public MyAdapter(Context context, String[] objects) {
            super(context, android.R.layout.simple_list_item_1, objects);
            mDropDownHelper = new ThemedSpinnerAdapter.Helper(context);
        }

        @Override
        public View getDropDownView(int position, View convertView, ViewGroup parent) {
            View view;

            if (convertView == null) {
                // Inflate the drop down using the helper's LayoutInflater
                LayoutInflater inflater = mDropDownHelper.getDropDownViewInflater();
                view = inflater.inflate(android.R.layout.simple_list_item_1, parent, false);
            } else {
                view = convertView;
            }

            TextView textView = (TextView) view.findViewById(android.R.id.text1);
            textView.setText(getItem(position));

            return view;
        }

        @Override
        public Theme getDropDownViewTheme() {
            return mDropDownHelper.getDropDownViewTheme();
        }

        @Override
        public void setDropDownViewTheme(Theme theme) {
            mDropDownHelper.setDropDownViewTheme(theme);
        }
    }


    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        View rootView;
        ListView listView;
        Connectiondetector connectiondetector;
        Spinner spinner_state_spinner, spinner_city_spinner;
        CustomAdapter_blooddonorlist customAdapter_blooddonorlist;
        ArrayList<bookedappointmentpatient> searchdoctorpojos= new ArrayList<>();
        ArrayList<doctormsglist> statelist= new ArrayList<>();
        ArrayList<doctormsglist> citylist= new ArrayList<>();
        int str_stateid=0,str_cityid=0;
        CardView cardView_cardView_bd;
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
             rootView = inflater.inflate(R.layout.fragment_find_blood_donor, container, false);
            return rootView;
        }

        @Override
        public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
            super.onViewCreated(view, savedInstanceState);
            connectiondetector= new Connectiondetector(getActivity());
            if(connectiondetector.isConnectingToInternet())
                        addingdata();
            else
                Toast.makeText(getActivity(), "No Internet!!", Toast.LENGTH_SHORT).show();
            listView=rootView.findViewById(R.id.list);
            spinner_state_spinner=rootView.findViewById(R.id.state_spinner);
            spinner_city_spinner=rootView.findViewById(R.id.city_spinner);
            cardView_cardView_bd=rootView.findViewById(R.id.cardView_bd);

            cardView_cardView_bd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(connectiondetector.isConnectingToInternet())
                    bloodbanklist();
                    else
                        Toast.makeText(getActivity(), "No Internet!!", Toast.LENGTH_SHORT).show();
                }
            });
        }


        private Request fetchingmedicalcondition() {
            JSONObject postdata = new JSONObject();
            RequestBody body = RequestBody.create(MedicalHitoryp.JSON, postdata.toString());
            return new Request.Builder()
                    .addHeader("X-Api-Key", "AB5433GMDF657VBB")
                    .addHeader("Content-Type", "application/json")
                    .url(ApiUtils.BASE_URL+"listcityandstate")
                    .post(body)
                    .build();

        }
        private void addingdata()
        {
            OkHttpClient client = new OkHttpClient();
            Request request = fetchingmedicalcondition();

            Log.i("", "onClick: "+request);
            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Request request, IOException e) {
                    Log.i("Activity", "onFailure: Fail");
                }
                @Override
                public void onResponse(final Response response) throws IOException {

                    final String body=response.body().string();
                   // medicalconditionJSON(body);
                    Log.i("1234add", "onResponse: "+body);
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                JSONObject jsonObject= new JSONObject(body);
                                JSONArray jsonArray=jsonObject.getJSONArray("State");
                                if(statelist.size()>0)
                                    statelist.clear();
                                for (int i = 0; i <jsonArray.length() ; i++) {
                                    JSONObject jsonObject1=jsonArray.getJSONObject(i);
                                    statelist.add(new doctormsglist(jsonObject1.getInt("id"),jsonObject1.getString("name")));
                                }
                                CustomAdapter_bd customAdapter=new CustomAdapter_bd(getActivity(),statelist);
                                spinner_state_spinner.setAdapter(customAdapter);
                                spinner_state_spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
                                    @Override
                                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                        str_stateid=statelist.get(position).getId();
                                        if(connectiondetector.isConnectingToInternet())
                                        citylist();
                                        else
                                            Toast.makeText(getActivity(), "No internet!!", Toast.LENGTH_SHORT).show();
                                    }

                                    @Override
                                    public void onNothingSelected(AdapterView<?> parent) {

                                    }
                                });
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }

            });

        }
        private Request fetchcity() {
            JSONObject postdata = new JSONObject();
            try {
                postdata.put("state_id",str_stateid);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            RequestBody body = RequestBody.create(MedicalHitoryp.JSON, postdata.toString());
            return new Request.Builder()
                    .addHeader("X-Api-Key", "AB5433GMDF657VBB")
                    .addHeader("Content-Type", "application/json")
                    .url(ApiUtils.BASE_URL+"listcity")
                    .post(body)
                    .build();

        }
        private void citylist()
        {
            OkHttpClient client = new OkHttpClient();
            Request request = fetchcity();

            Log.i("", "onClick: "+request);
            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Request request, IOException e) {
                    Log.i("Activity", "onFailure: Fail");
                }
                @Override
                public void onResponse(final Response response) throws IOException {

                    final String body=response.body().string();
                    // medicalconditionJSON(body);
                    Log.i("1234add", "onResponse: "+body);
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                JSONObject jsonObject= new JSONObject(body);
                                JSONArray jsonArray=jsonObject.getJSONArray("City");
                                if(citylist.size()>0)
                                    citylist.clear();
                                for (int i = 0; i <jsonArray.length() ; i++) {
                                    JSONObject jsonObject1=jsonArray.getJSONObject(i);
                                    citylist.add(new doctormsglist(jsonObject1.getInt("id"),jsonObject1.getString("name")));
                                }
                                CustomAdapter_bd customAdapter=new CustomAdapter_bd(getActivity(),citylist);
                                spinner_city_spinner.setAdapter(customAdapter);
                                spinner_city_spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
                                    @Override
                                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                        str_cityid=citylist.get(position).getId();
                                    }

                                    @Override
                                    public void onNothingSelected(AdapterView<?> parent) {

                                    }
                                });
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }

            });

        }

        private Request fetchbloodbank() {
            JSONObject postdata = new JSONObject();
            try {
                postdata.put("state",str_stateid);
                postdata.put("city",str_cityid);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            RequestBody body = RequestBody.create(MedicalHitoryp.JSON, postdata.toString());
            return new Request.Builder()
                    .addHeader("X-Api-Key", "AB5433GMDF657VBB")
                    .addHeader("Content-Type", "application/json")
                    .url(ApiUtils.BASE_URL+"findbloodbank")
                    .post(body)
                    .build();

        }
        private void bloodbanklist()
        {
            OkHttpClient client = new OkHttpClient();
            Request request = fetchbloodbank();

            Log.i("", "onClick: "+request);
            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Request request, IOException e) {
                    Log.i("Activity", "onFailure: Fail");
                }
                @Override
                public void onResponse(final Response response) throws IOException {

                    final String body=response.body().string();
                    // medicalconditionJSON(body);
                    Log.i("1234add", "onResponse: "+body);
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                JSONObject jsonObject= new JSONObject(body);
                                if(jsonObject.getString("message").equals("Success"))
                                {
                                    JSONArray jsonArray=jsonObject.getJSONArray("BloodBanks");
                                    for (int i = 0; i <jsonArray.length() ; i++)
                                    {
                                        JSONObject jsonObject1=jsonArray.getJSONObject(i);
                                        searchdoctorpojos.add(new bookedappointmentpatient(jsonObject1.getString("name"),"",jsonObject1.getString("phone"),jsonObject1.getString("address"),0,""));

                                    }
                                }
                                customAdapter_blooddonorlist= new CustomAdapter_blooddonorlist(getActivity(),searchdoctorpojos);
                                listView.setAdapter(customAdapter_blooddonorlist);

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }

            });

        }
    }
    public static class BecomeBloodDonor extends Fragment
    {
        Spinner spinner_state_spinner, spinner_city_spinner;
        ArrayList<doctormsglist> statelist= new ArrayList<>();
        ArrayList<doctormsglist> citylist= new ArrayList<>();
        int str_stateid=0,str_cityid=0;
        Connectiondetector connectiondetector;


        private Request fetchingmedicalcondition() {
            JSONObject postdata = new JSONObject();
            RequestBody body = RequestBody.create(MedicalHitoryp.JSON, postdata.toString());
            return new Request.Builder()
                    .addHeader("X-Api-Key", "AB5433GMDF657VBB")
                    .addHeader("Content-Type", "application/json")
                    .url(ApiUtils.BASE_URL+"listcityandstate")
                    .post(body)
                    .build();

        }
        private void addingdata()
        {
            OkHttpClient client = new OkHttpClient();
            Request request = fetchingmedicalcondition();

            Log.i("", "onClick: "+request);
            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Request request, IOException e) {
                    Log.i("Activity", "onFailure: Fail");
                }
                @Override
                public void onResponse(final Response response) throws IOException {

                    final String body=response.body().string();
                    // medicalconditionJSON(body);
                    Log.i("1234add", "onResponse: "+body);
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                JSONObject jsonObject= new JSONObject(body);
                                JSONArray jsonArray=jsonObject.getJSONArray("State");
                                if(statelist.size()>0)
                                    statelist.clear();
                                for (int i = 0; i <jsonArray.length() ; i++) {
                                    JSONObject jsonObject1=jsonArray.getJSONObject(i);
                                    statelist.add(new doctormsglist(jsonObject1.getInt("id"),jsonObject1.getString("name")));
                                }
                                CustomAdapter_bd customAdapter=new CustomAdapter_bd(getActivity(),statelist);
                                spinner_state_spinner.setAdapter(customAdapter);
                                spinner_state_spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
                                    @Override
                                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                        str_stateid=statelist.get(position).getId();
                                        if(connectiondetector.isConnectingToInternet())
                                            citylist();
                                        else
                                            Toast.makeText(getActivity(), "No internet!!", Toast.LENGTH_SHORT).show();
                                    }

                                    @Override
                                    public void onNothingSelected(AdapterView<?> parent) {

                                    }
                                });
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }

            });

        }
        private Request fetchcity() {
            JSONObject postdata = new JSONObject();
            try {
                postdata.put("state_id",str_stateid);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            RequestBody body = RequestBody.create(MedicalHitoryp.JSON, postdata.toString());
            return new Request.Builder()
                    .addHeader("X-Api-Key", "AB5433GMDF657VBB")
                    .addHeader("Content-Type", "application/json")
                    .url(ApiUtils.BASE_URL+"listcity")
                    .post(body)
                    .build();

        }
        private void citylist()
        {
            OkHttpClient client = new OkHttpClient();
            Request request = fetchcity();

            Log.i("", "onClick: "+request);
            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Request request, IOException e) {
                    Log.i("Activity", "onFailure: Fail");
                }
                @Override
                public void onResponse(final Response response) throws IOException {

                    final String body=response.body().string();
                    // medicalconditionJSON(body);
                    Log.i("1234add", "onResponse: "+body);
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                JSONObject jsonObject= new JSONObject(body);
                                JSONArray jsonArray=jsonObject.getJSONArray("City");
                                if(citylist.size()>0)
                                    citylist.clear();
                                for (int i = 0; i <jsonArray.length() ; i++) {
                                    JSONObject jsonObject1=jsonArray.getJSONObject(i);
                                    citylist.add(new doctormsglist(jsonObject1.getInt("id"),jsonObject1.getString("name")));
                                }
                                CustomAdapter_bd customAdapter=new CustomAdapter_bd(getActivity(),citylist);
                                spinner_city_spinner.setAdapter(customAdapter);
                                spinner_city_spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
                                    @Override
                                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                        str_cityid=citylist.get(position).getId();
                                    }

                                    @Override
                                    public void onNothingSelected(AdapterView<?> parent) {

                                    }
                                });
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }

            });

        }

        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";
        View rootView;
        String bloodg;
        Spinner spinner_bd;
        CardView cardView_bd;
        public BecomeBloodDonor() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static BecomeBloodDonor newInstance(int sectionNumber) {
            BecomeBloodDonor fragment = new BecomeBloodDonor();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            bloodg=getArguments().getString("bloodgroup");
            connectiondetector= new Connectiondetector(getActivity());
             rootView = inflater.inflate(R.layout.fragment_blood_donor, container, false);
             spinner_bd=rootView.findViewById(R.id.bloodg_spinner);
             cardView_bd=rootView.findViewById(R.id.cardView_bd);
            spinner_state_spinner=rootView.findViewById(R.id.state_spinner);
            spinner_city_spinner=rootView.findViewById(R.id.city_spinner);
             int pos=Integer.parseInt(bloodg);
             spinner_bd.setSelection(pos-1);
             cardView_bd.setOnClickListener(new View.OnClickListener() {
                 @Override
                 public void onClick(View v) {
                     fetchdata();
                 }
             });
            if(connectiondetector.isConnectingToInternet())
                addingdata();
            else
                Toast.makeText(getActivity(), "No Internet!!", Toast.LENGTH_SHORT).show();
            return rootView;
        }
        private Request becomeblooddonor()
        {
            JSONObject postdata = new JSONObject();
            try {
                postdata.put("user_id",Integer.parseInt(Prefhelper.getInstance(getActivity()).getUserid()));

            } catch (JSONException e) {
                e.printStackTrace();
            }
            RequestBody body = RequestBody.create(JSON, postdata.toString());
            return new Request.Builder()
                    .addHeader("X-Api-Key", "AB5433GMDF657VBB")
                    .addHeader("Content-Type", "application/json")
                    .url(ApiUtils.BASE_URL+"becomeblooddonor")
                    .post(body)
                    .build();
        }
        private void fetchdata()
        {
            OkHttpClient client = new OkHttpClient();
            Request request = becomeblooddonor();
            Log.i("", "onClick: " + request);
            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Request request, IOException e) {
                    Log.i("Activity", "onFailure: Fail");
                }

                @Override
                public void onResponse(final Response response) throws IOException {

                    final String body = response.body().string();
                    Log.i("1234check", "onResponse: " + body);

                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            try {
                                AlertDialog.Builder builder;
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                    builder = new AlertDialog.Builder(getActivity(), android.R.style.Theme_Material_Dialog_Alert);
                                } else {
                                    builder = new AlertDialog.Builder(getActivity());
                                }
                                JSONObject jsonObject = new JSONObject(body);
                                if (jsonObject.getInt("Success") == 1) {

                                    builder.setTitle("Message")
                                            .setMessage(jsonObject.getString("message"))
                                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int which) {
                                                    // continue with delete
                                                    getActivity().finish();
                                                }
                                            })

                                            .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int which) {
                                                    // do nothing
                                                }
                                            })

                                            // .setIcon(android.R.drawable.m)

                                            .show();
                                    builder.setCancelable(false);

                                }
                                else if(jsonObject.getInt("Success") == 2)
                                {

                                    builder.setTitle("Message")
                                            .setMessage(jsonObject.getString("message"))
                                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int which) {
                                                    // continue with delete
                                                    getActivity().finish();
                                                }
                                            })

                                            .setNegativeButton("Remove", new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int which) {
                                                    delete();
                                                    // do nothing
                                                }
                                            })

                                            // .setIcon(android.R.drawable.m)

                                            .show();
                                    builder.setCancelable(false);

                                }
                                else {
                                    Toast.makeText(getActivity(), "Error", Toast.LENGTH_SHORT).show();
                                }
                            } catch (Exception e) {
                                Toast.makeText(getActivity(), "" + e, Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }

            });
        }

        private Request removeblooddonor()
        {
            JSONObject postdata = new JSONObject();
            try {
                postdata.put("user_id",Integer.parseInt(Prefhelper.getInstance(getActivity()).getUserid()));

            } catch (JSONException e) {
                e.printStackTrace();
            }
            RequestBody body = RequestBody.create(JSON, postdata.toString());
            return new Request.Builder()
                    .addHeader("X-Api-Key", "AB5433GMDF657VBB")
                    .addHeader("Content-Type", "application/json")
                    .url(ApiUtils.BASE_URL+"removeblooddonor")
                    .post(body)
                    .build();
        }
        private void delete()
        {
            OkHttpClient client = new OkHttpClient();
            Request request = removeblooddonor();
            Log.i("", "onClick: " + request);
            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Request request, IOException e) {
                    Log.i("Activity", "onFailure: Fail");
                }

                @Override
                public void onResponse(final Response response) throws IOException {

                    final String body = response.body().string();
                    Log.i("1234check", "onResponse: " + body);

                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            try {
                                AlertDialog.Builder builder;
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                    builder = new AlertDialog.Builder(getActivity(), android.R.style.Theme_Material_Dialog_Alert);
                                } else {
                                    builder = new AlertDialog.Builder(getActivity());
                                }
                                JSONObject jsonObject = new JSONObject(body);
                                if (jsonObject.getInt("Success") == 1)
                                {

                                    builder.setTitle("Message")
                                            .setMessage(jsonObject.getString("message"))
                                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int which) {
                                                    // continue with delete
                                                    getActivity().finish();
                                                }
                                            })

                                            .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int which) {
                                                    // do nothing
                                                }
                                            })

                                            // .setIcon(android.R.drawable.m)

                                            .show();
                                    builder.setCancelable(false);

                                }
                                else {
                                    Toast.makeText(getActivity(), "Error", Toast.LENGTH_SHORT).show();
                                }
                            } catch (Exception e) {
                                Toast.makeText(getActivity(), "" + e, Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }

            });
        }}



}

class CustomAdapter_bd extends BaseAdapter {
    Context context;
    LayoutInflater inflter;
    List<doctormsglist> medicalconditionlists;
    public CustomAdapter_bd(Context applicationContext, List<doctormsglist> medicalconditionlists) {
        this.context = applicationContext;
        this.medicalconditionlists=medicalconditionlists;
        inflter = (LayoutInflater.from(applicationContext));
    }

    @Override
    public int getCount() {
        return medicalconditionlists.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = inflter.inflate(R.layout.spinner_item, null);
        TextView names = (TextView) view.findViewById(R.id.txt);
        names.setText(medicalconditionlists.get(i).getName());
        return view;
    }


}
