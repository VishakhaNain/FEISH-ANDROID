package com.app.feish.application.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.app.feish.application.Adpter.CustomAdapter_familyhistory;
import com.app.feish.application.Adpter.CustomAdapter_reportp;
import com.app.feish.application.R;
import com.app.feish.application.Remote.ApiUtils;
import com.app.feish.application.Remote.EncryptionDecryption;
import com.app.feish.application.model.vitalsignlist;
import com.app.feish.application.sessiondata.Prefhelper;
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

/**
 * Created by lenovo on 6/4/2016.
 */
public class ListFamilyhistory extends Fragment {
    int userid;
    private Request listfamily_his() {
        JSONObject postdata = new JSONObject();
        try {
            postdata.put("user_id",userid);
            //postdata.put("password",lpassword);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(JSON, postdata.toString());
        return new Request.Builder()
                .addHeader("X-Api-Key", "AB5433GMDF657VBB")
                .addHeader("Content-Type", "application/json")
                .url(ApiUtils.BASE_URL+"listFamilyHistory")
                .post(body)
                .build();

    }
    private void fetch()
    {
        OkHttpClient client = new OkHttpClient();
        Request request = listfamily_his();
        Log.i("", "onClick: "+request);
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                Log.i("Activity", "onFailure: Fail");
            }
            @Override
            public void onResponse(final Response response) throws IOException {

                final String body=response.body().string();
                Log.i("1234", "onResponse: "+body);
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try
                        {
                            JSONObject jsonObject= new JSONObject(body);
                            if(jsonObject.getString("message").equals("Success")) {

                                JSONArray jsonArray=jsonObject.getJSONArray("data");
                                for (int i = 0; i <jsonArray.length() ; i++) {
                                    JSONObject jsonObject1=jsonArray.getJSONObject(i);

                                    String decryptmember= EncryptionDecryption.decode(jsonObject1.getString("member_name"));
                                    vitalsignlists.add(new vitalsignlist(jsonObject1.getInt("id"),decryptmember, jsonObject1.getString("age"), jsonObject1.getString("relationship_id"), jsonObject1.getString("disease_id"), jsonObject1.getString("current_status"), jsonObject1.getString("year"), jsonObject1.getString("description")));


                                }
                                 customAdapter_vitalsigns = new CustomAdapter_familyhistory(getActivity(), vitalsignlists);
                                listView.setAdapter(customAdapter_vitalsigns);
                            }
                            else {
                                Toast.makeText(getActivity(), ""+jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
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

    // Store instance variables
    public static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");
  View view1;
    CustomAdapter_familyhistory customAdapter_vitalsigns;
    ListView listView;
    ArrayList<vitalsignlist> vitalsignlists= new ArrayList<>();
    //AIzaSyAK0_mt4pnKbt8Dr--Wdaf8ABuBwElvvA8Config
    // newInstance constructor for creating fragment with arguments
    public static ListFamilyhistory newInstance(int page, String title) {
        ListFamilyhistory fragmentFirst = new ListFamilyhistory();
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
        userid=Integer.parseInt(getArguments().getString("Stringlist"));

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        listView= view1.findViewById(R.id.list);
        fetch();
        }


    // Inflate the view for the fragment based on layout XML
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
         view1 = inflater.inflate(R.layout.fragment_list, container, false);
/*
        if(getArguments().getInt("someInt")==2)
        {
            userid=Integer.parseInt("798");

        }
        else
        {
            userid=Integer.parseInt(Prefhelper.getInstance(getActivity()).getUserid());

        }*/
   //     getActivity().setTitle("CollegeName");


        return view1;
    }
    public void hideSoftKeyboard() {
        if (getActivity().getCurrentFocus() != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getContext().getSystemService(getContext().INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
        }
    }

}