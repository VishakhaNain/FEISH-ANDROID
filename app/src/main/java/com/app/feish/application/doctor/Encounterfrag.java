package com.app.feish.application.doctor;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.app.feish.application.Adpter.CustomAdapter_encounteres;
import com.app.feish.application.Connectiondetector;
import com.app.feish.application.R;
import com.app.feish.application.Remote.ApiUtils;
import com.app.feish.application.TodayEncounters;
import com.app.feish.application.fragment.Addfamilyreport;
import com.app.feish.application.model.encountersmodel;
import com.app.feish.application.modelclassforapi.Datum2;
import com.app.feish.application.modelclassforapi.DoctorEncounters;
import com.app.feish.application.modelclassforapi.ListServicesContact;
import com.app.feish.application.sessiondata.Prefhelper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import static com.app.feish.application.doctor.SetupProfileForDoctor.JSON;


public class Encounterfrag extends Fragment implements TodayEncounters {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    CustomAdapter_encounteres customAdapter_encounteres;
    ArrayList<encountersmodel> encountersmodels= new ArrayList<>();
    CustomAdapter_encounteres customAdapter_encounteres_ba;
    DoctorEncounters doctorEncounters_book;
    Connectiondetector connectiondetector;

RecyclerView recyclerView;
    // TODO: Rename and change types of parameters
    private String mParam1;
    View v;
    private String mParam2;

   // private OnFragmentInteractionListener mListener;

    public Encounterfrag() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Encounterfrag.
     */
    // TODO: Rename and change types and number of parameters
    public static Encounterfrag newInstance(String param1, String param2) {
        Encounterfrag fragment = new Encounterfrag();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v= inflater.inflate(R.layout.fragment_encounterfrag, container, false);
       // doctorEncounters= new DoctorEncounters();
        recyclerView=v.findViewById(R.id.recycler_view);
      LinearLayoutManager  layoutManager= new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        connectiondetector= new Connectiondetector(getActivity());
        if(connectiondetector.isConnectingToInternet())
        {
            fetchBookedappointment(getArguments().getString(ARG_PARAM1),new SimpleDateFormat("yyyy-MM-dd").format(new java.util.Date()));
        }
        else
        {
            Toast.makeText(getActivity(), "No Internet", Toast.LENGTH_SHORT).show();
        }
        return v;


    }

    private void fetchBookedappointment(String service_id,String appointment_date)
    {
        OkHttpClient client = new OkHttpClient();
        Request validation_request = givebookappodata(service_id,appointment_date);
        client.newCall(validation_request).enqueue(new Callback() {

            @Override
            public void onFailure(Request request, IOException e) {

                // Toast.makeText(context,"Fail",Toast.LENGTH_LONG).show();
                Log.i("Activity", "onFailure: Fail");
            }

            @Override
            public void onResponse(final Response response) throws IOException {
                final String body = response.body().string();
                listbookappresponse(body);
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(doctorEncounters_book.getStatus()) {
                            encountersmodels.add(new encountersmodel("Appointment id : 202","","802","abc","active","20/12/2018 20:02:15",0));
                                                       customAdapter_encounteres_ba = new CustomAdapter_encounteres(encountersmodels, doctorEncounters_book, getActivity(), 0,0);
                            recyclerView.setAdapter(customAdapter_encounteres_ba);
                            TextView textView= getActivity().findViewById(R.id.totalappo);
                            textView.setText(""+doctorEncounters_book.getAppointmentdata().size());
                        }
                    }
                });
            }
        });

    }
    private Request givebookappodata(String service_id,String appointment_date) {
        JSONObject postdata = new JSONObject();
        try {
            postdata.put("doctor_id", Prefhelper.getInstance(getActivity()).getUserid());
            postdata.put("service_id", service_id);
            postdata.put("appointed_timing", appointment_date);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(JSON, postdata.toString());
        return new Request.Builder()
                .addHeader("X-Api-Key", "AB5433GMDF657VBB")
                .addHeader("Content-Type", "application/json")
                .url(ApiUtils.BASE_URL+"listBookedAppointments")
                .post(body)
                .build();
    }
    public void listbookappresponse(String response) {
        Gson gson = new GsonBuilder().create();
        doctorEncounters_book = gson.fromJson(response, DoctorEncounters.class);
    }

    @Override
    public void Encountersfetch(String date) {
        fetchBookedappointment(date,new SimpleDateFormat("yyyy-MM-dd").format(new java.util.Date()));
    }

    ////////////////////////////////////


}
