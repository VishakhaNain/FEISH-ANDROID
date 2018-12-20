package com.app.feish.application.fragment;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ListView;
import android.widget.Toast;

import com.app.feish.application.Adpter.CustomAdapter_listassistant;
import com.app.feish.application.Connectiondetector;
import com.app.feish.application.R;
import com.app.feish.application.Remote.ApiUtils;
import com.app.feish.application.Remote.EncryptionDecryption;
import com.app.feish.application.model.listassistantmodel;
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

import static com.app.feish.application.fragment.Addfamilyreport.JSON;

/**
 * Created by lenovo on 6/4/2016.
 */
public class ListPatient extends Fragment {

    View view1;
    RecyclerView recyclerView;
    ListView listView;
    String sal[]={"Mr.","Mrs.","Miss","Dr"};
    CustomAdapter_listassistant customAdapter_listassistant;
    ArrayList<listassistantmodel> listassistantmodels= new ArrayList<>();
    String user_id;
    Connectiondetector connectiondetector;
    public static ListPatient newInstance(int page, String title) {
        ListPatient fragmentFirst = new ListPatient();
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
        user_id=getArguments().getString("Stringlist",Prefhelper.getInstance(getActivity()).getUserid());

    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        listView=view1.findViewById(R.id.recycler_view);
       /* LinearLayoutManager layoutManager= new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);*/
        connectiondetector= new Connectiondetector(getActivity());
        if(connectiondetector.isConnectingToInternet())
        {
            listassistant();
        }
        else
        {
            Toast.makeText(getActivity(), "No Internet!!", Toast.LENGTH_SHORT).show();
        }
    }

    // Inflate the view for the fragment based on layout XML
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
         view1 = inflater.inflate(R.layout.fragment_listpatient, container, false);

        return view1;
    }
    private Request addassistant() {
        JSONObject postdata = new JSONObject();
        try {
            postdata.put("added_by_doctor_id",user_id);

            //postdata.put("password",lpassword);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(JSON, postdata.toString());
        return new Request.Builder()
                .addHeader("X-Api-Key", "AB5433GMDF657VBB")
                .addHeader("Content-Type", "application/json")
                .url(ApiUtils.BASE_URL+"listaddedpatient")
                .post(body)
                .build();

    }
    private void listassistant()
    {
        OkHttpClient client = new OkHttpClient();
        Request request = addassistant();
        Log.i("", "onClick: "+request);
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                Log.i("Activity", "onFailure: Fail");
            }
            @Override
            public void onResponse(final Response response) throws IOException {

                final String body=response.body().string();
                Log.i("1234add", "onResponse: "+body);
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try{
                            JSONObject jsonObject= new JSONObject(body);
                            if(jsonObject.getInt("Success")==1)
                            {
                                JSONArray jsonArray= jsonObject.optJSONArray("userdetail");
                                for (int i = 0; i <jsonArray.length() ; i++) {
                                    JSONObject jsonObject1= jsonArray.getJSONObject(i);
                                    JSONObject jsonObject2=jsonObject1.getJSONObject("User");

                                    String decryptmobile= EncryptionDecryption.decode(jsonObject2.optString("mobile"));

                                    listassistantmodels.add(new listassistantmodel(sal[jsonObject2.getInt("salutation")],jsonObject2.getString("first_name"),jsonObject2.getString("last_name"),decryptmobile,jsonObject2.getString("email"),String.valueOf(jsonObject2.getInt("id")),R.drawable.patienr));
                                    customAdapter_listassistant=new CustomAdapter_listassistant(getActivity(),listassistantmodels,1);
                                    listView.setAdapter(customAdapter_listassistant);
                                }

                            }
                            else
                            {
                                Toast.makeText(getActivity(), "Error", Toast.LENGTH_SHORT).show();
                            }
                        }
                        catch (JSONException e)
                        {

                        }
                    }
                });
            }

        });

    }
    public void hideSoftKeyboard() {
        if (getActivity().getCurrentFocus() != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getContext().getSystemService(getContext().INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
        }
    }

}