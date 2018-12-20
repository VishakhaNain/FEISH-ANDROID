package com.app.feish.application.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ListView;
import android.widget.Toast;

import com.app.feish.application.Adpter.CustomAdapter_listplan;
import com.app.feish.application.Adpter.CustomAdapter_listservice;
import com.app.feish.application.Connectiondetector;
import com.app.feish.application.R;
import com.app.feish.application.Remote.ApiUtils;
import com.app.feish.application.model.drplanmodel;
import com.app.feish.application.model.listservicepojo;
import com.app.feish.application.modelclassforapi.Datum2;
import com.app.feish.application.modelclassforapi.ListServicesContact;
import com.app.feish.application.sessiondata.Prefhelper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.MediaType;
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

/**
 * Created by lenovo on 6/4/2016.
 */
public class ListPlan extends Fragment {
    // Store instance variables
    public static final MediaType JSON = MediaType.parse("application/json:charset=utf-8");
  View view1;
    private ListServicesContact serviceResponse;
    CustomAdapter_listplan customAdapter_listplan;
    ListView listView;
    RecyclerView recyclerView;
    ArrayList<drplanmodel>  drplanmodels= new ArrayList<>();
    Connectiondetector connectiondetector;
    //AIzaSyAK0_mt4pnKbt8Dr--Wdaf8ABuBwElvvA8Config
    // newInstance constructor for creating fragment with arguments
    public static ListPlan newInstance(int page, String title) {
        ListPlan fragmentFirst = new ListPlan();
        Bundle args = new Bundle();
        args.putInt("someInt", page);
        args.putString("Stringlist", title);
        fragmentFirst.setArguments(args);
        return fragmentFirst;
    }

    // Store instance variables based on arguments passed
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        listView= view1.findViewById(R.id.list);
        recyclerView = (RecyclerView) view1.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        recyclerView.setNestedScrollingEnabled(false);
        connectiondetector= new Connectiondetector(getActivity());
        if(connectiondetector.isConnectingToInternet())
        {
            fetchdata();
        }
        else
        {
            Toast.makeText(getActivity(), "No Internet!!", Toast.LENGTH_SHORT).show();
        }

      /*  customAdapter_listplan= new CustomAdapter_listplan(drplanmodels,getActivity());
        recyclerView.setAdapter(customAdapter_listplan);*/
    }


    // Inflate the view for the fragment based on layout XML
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
         view1 = inflater.inflate(R.layout.fragment_listplan, container, false);
   //     getActivity().setTitle("CollegeName");


        return view1;
    }
    public void hideSoftKeyboard() {
        if (getActivity().getCurrentFocus() != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getContext().getSystemService(getContext().INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
        }
    }
    private void fetchdata()
    {
        OkHttpClient client = new OkHttpClient();
        Request validation_request = listservices();
        client.newCall(validation_request).enqueue(new Callback() {

            @Override
            public void onFailure(Request request, IOException e) {

                // Toast.makeText(getActivity(),"Fail",Toast.LENGTH_LONG).show();
                Log.i("Activity", "onFailure: Fail");
            }

            @Override
            public void onResponse(final Response response) throws IOException {
                 final String body=response.body().string();
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                       // Toast.makeText(getActivity(), ""+body, Toast.LENGTH_SHORT).show();
                        JSONObject jsonObject= null;
                        try {
                            jsonObject = new JSONObject(body);

                        if(jsonObject.getBoolean("status"))
                        {
                            JSONArray jsonArray= jsonObject.getJSONArray("data");
                            for (int i = 0; i <jsonArray.length() ; i++)
                            {
                              JSONObject jsonObject1=jsonArray.getJSONObject(i);
                              JSONObject jsonObject2=jsonObject1.getJSONObject("DoctorPlanDetail");
                              drplanmodels.add(new drplanmodel(jsonObject2.getInt("id"),jsonObject2.getString("name"),jsonObject2.getString("price"),jsonObject2.getString("validity"),jsonObject2.getString("percentage_per_visit"),jsonObject2.getString("plan_details")));

                            }
                            customAdapter_listplan= new CustomAdapter_listplan(drplanmodels,getActivity(),0);
                            recyclerView.setAdapter(customAdapter_listplan);

                        }
                        else
                        {
                            Toast.makeText(getActivity(), ""+jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                        }
                        } catch (JSONException e) {
                            Toast.makeText(getActivity(), ""+e, Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                        }

                    }
                });
            }
        });

    }

    private Request listservices() {
        JSONObject postdata = new JSONObject();
        try {
            postdata.put("user_id", Prefhelper.getInstance(getActivity()).getUserid());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(JSON, postdata.toString());
        return new Request.Builder()
                .addHeader("X-Api-Key", "AB5433GMDF657VBB")
                .addHeader("Content-Type", "application/json")
                .url(ApiUtils.BASE_URL+"listdrplan")
                .post(body)
                .build();
    }


}