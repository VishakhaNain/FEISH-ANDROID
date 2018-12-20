package com.app.feish.application.fragment;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import com.app.feish.application.Adpter.CustomAdapter_listassistant;
import com.app.feish.application.Connectiondetector;
import com.app.feish.application.R;
import com.app.feish.application.Remote.ApiUtils;
import com.app.feish.application.model.listassistantmodel;
import com.app.feish.application.model.listassistantmodel2;
import com.app.feish.application.sessiondata.Prefhelper;
import com.google.gson.JsonArray;
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
public class Listassistant extends Fragment {
    // Store instance variables
    View view1;
    String sal[]={"Mr.","Mrs.","Miss","Dr"};
    EditText editText;
    CustomAdapter_listassistant customAdapter_listassistant;
    ListView listView;
    ArrayList<listassistantmodel>  listassistantmodels= new ArrayList<>();
   public static ArrayList<listassistantmodel2>  listassistantmodels2= new ArrayList<>();
    Connectiondetector connectiondetector;
    //AIzaSyAK0_mt4pnKbt8Dr--Wdaf8ABuBwElvvA8Config
    // newInstance constructor for creating fragment with arguments
    public static Listassistant newInstance(int page, String title) {
        Listassistant fragmentFirst = new Listassistant();
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
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        listView= view1.findViewById(R.id.list);
    //    editText=view1.findViewById(R.id.edit);
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
         view1 = inflater.inflate(R.layout.fragment_listassistent, container, false);
   //     getActivity().setTitle("CollegeName");


        return view1;
    }
    public void hideSoftKeyboard() {
        if (getActivity().getCurrentFocus() != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getContext().getSystemService(getContext().INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
        }
    }
    private Request addassistant() {
        JSONObject postdata = new JSONObject();
        try {
            postdata.put("doctor_id", Prefhelper.getInstance(getActivity()).getUserid());

            //postdata.put("password",lpassword);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(JSON, postdata.toString());
        return new Request.Builder()
                .addHeader("X-Api-Key", "AB5433GMDF657VBB")
                .addHeader("Content-Type", "application/json")
                .url(ApiUtils.BASE_URL+"listAssistant")
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
                            if(jsonObject.getString("message").equals("success"))
                            {
                                JSONArray jsonArray= jsonObject.optJSONArray("data");
                                JSONArray jsonArray1= jsonObject.optJSONArray("datalist");
                                JSONArray jsonArray_name= jsonObject.optJSONArray("namedata");
                                for (int i = 0; i <jsonArray.length() ; i++) {
                                    JSONObject jsonObject1= jsonArray.getJSONObject(i);
                                    JSONArray jsonObject_name= jsonArray_name.getJSONArray(i);
                                    JSONObject jsonObject_data= jsonArray1.getJSONObject(i);
                                    JSONObject jsonObject2=jsonObject1.getJSONObject("User");
                                    listassistantmodels.add(new listassistantmodel(sal[jsonObject2.getInt("salutation")],jsonObject2.getString("first_name"),jsonObject2.getString("last_name"),jsonObject2.optString("mobile"),jsonObject2.getString("email"),jsonObject_name.getJSONObject(0).getString("title"),R.drawable.assistant));
                                    listassistantmodels2.add(new listassistantmodel2(jsonObject_data.getInt("id"),jsonObject_data.getInt("user_id"),jsonObject_data.getInt("service_id"),jsonObject_data.getInt("doctor_id"),jsonObject_data.getInt("is_deleted")));

                                    customAdapter_listassistant=new CustomAdapter_listassistant(getActivity(),listassistantmodels,0);
                                    listView.setAdapter(customAdapter_listassistant);
                                }

                            }
                            else
                            {

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

}