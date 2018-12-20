package com.app.feish.application.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ListView;
import android.widget.Toast;
import com.app.feish.application.Adpter.CustomAdapter_listservice;
import com.app.feish.application.R;
import com.app.feish.application.Remote.ApiUtils;
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
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by lenovo on 6/4/2016.
 */
public class ListService extends Fragment {
    List<Datum2> l;
    // Store instance variables
    public static final MediaType JSON = MediaType.parse("application/json:charset=utf-8");
  View view1;
    private ListServicesContact serviceResponse;
    CustomAdapter_listservice customAdapter_listassistant;
    ListView listView;
    ArrayList<listservicepojo>  listassistantmodels= new ArrayList<>();
    //AIzaSyAK0_mt4pnKbt8Dr--Wdaf8ABuBwElvvA8Config
    // newInstance constructor for creating fragment with arguments
    public static ListService newInstance(int page, String title) {
        ListService fragmentFirst = new ListService();
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
      /*  listassistantmodels.add(new listservicepojo("Service1","Noida","05648975648","Active"));
        listassistantmodels.add(new listservicepojo("Service1","Noida","05648975648","Active"));
        listassistantmodels.add(new listservicepojo("Service1","Noida","05648975648","Active"));
        listassistantmodels.add(new listservicepojo("Service1","Noida","05648975648","Active"));
        listassistantmodels.add(new listservicepojo("Service1","Noida","05648975648","Active"));
        customAdapter_listassistant=new CustomAdapter_listservice(getActivity(),listassistantmodels);
        listView.setAdapter(customAdapter_listassistant);*/
      fetchdata();


    }


    // Inflate the view for the fragment based on layout XML
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
         view1 = inflater.inflate(R.layout.fragment_listservice, container, false);
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
                Log.i("Activity", "onFailure: Fail");
            }

            @Override
            public void onResponse(final Response response) throws IOException {
               // final String body=response.body().string();
                listServiceResponse(response.body().string());
                final boolean isSuccessful=serviceResponse.getStatus();
                 l=serviceResponse.getData();
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                       if (isSuccessful) {

                            customAdapter_listassistant=new CustomAdapter_listservice(getActivity(),l);
                            listView.setAdapter(customAdapter_listassistant);

                        } else {
                            Toast.makeText(getActivity(), "Could not load the list!!", Toast.LENGTH_SHORT).show();
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
                .url(ApiUtils.BASE_URL+"listService")
                .post(body)
                .build();
    }

    public void listServiceResponse(String response) {
        Gson gson = new GsonBuilder().create();
        serviceResponse = gson.fromJson(response, ListServicesContact.class);
    }

}