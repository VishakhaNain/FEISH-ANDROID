package com.app.feish.application.doctor;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import com.app.feish.application.Adpter.CustomAdapter_vitalsigns;
import com.app.feish.application.R;
import com.app.feish.application.Remote.ApiUtils;
import com.app.feish.application.model.vitalsignlist;
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
import java.util.HashMap;
import java.util.Iterator;
import static com.app.feish.application.doctor.DoctorMessage.JSON;


public class DrVitalsignfrg extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
RecyclerView recyclerView;
    HashMap<Integer,String> VitalSignlist= new HashMap<>();
    HashMap<Integer,String> VitalUnitlist= new HashMap<>();
    HashMap<Integer,String> VitalSignunit= new HashMap<>();
    ArrayList<String>  vitalSignname= new ArrayList<>();
    ArrayList<vitalsignlist>  vitalsignlists= new ArrayList<>();
    // TODO: Rename and change types of parameters
    private String mParam1;
    View v;
    CustomAdapter_vitalsigns customAdapter_vitalsigns;
    private String mParam2;

   // private OnFragmentInteractionListener mListener;

    public DrVitalsignfrg() {
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
    public static DrVitalsignfrg newInstance(String param1, String param2) {
        DrVitalsignfrg fragment = new DrVitalsignfrg();
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
        recyclerView=v.findViewById(R.id.recycler_view);
      LinearLayoutManager  layoutManager= new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        fetchingdata();


        return v;
    }
    private Request listVitalSign()
    {
        JSONObject postdata = new JSONObject();
        try {
            postdata.put("patient_id", "798");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(JSON, postdata.toString());
        return new Request.Builder()
                .addHeader("X-Api-Key", "AB5433GMDF657VBB")
                .addHeader("Content-Type", "application/json")
                .url(ApiUtils.BASE_URL+"newlistVitalSign")
                .post(body)
                .build();
    }
    public HashMap<Integer, String> toMap(JSONObject object) throws JSONException {
        HashMap<Integer, String> map = new HashMap();
        Iterator keys = object.keys();
        while (keys.hasNext()) {
            String key = (String) keys.next();
            vitalSignname.add(fromJson(object.get(key)).toString());
            map.put(Integer.parseInt(key), fromJson(object.get(key)).toString());
        }
        return map;
    }
    private  Object fromJson(Object json) throws JSONException {
        if (json == JSONObject.NULL) {
            return null;
        } else if (json instanceof JSONObject) {
            return toMap((JSONObject) json);
        } /*else if (json instanceof JSONArray) {
            return toList((JSONArray) json);
        }*/ else {
            return json;
        }
    }
    private void fetchingdata()
    {
        OkHttpClient client = new OkHttpClient();
        Request request = listVitalSign();
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
                            // editText.setVisibility(View.GONE);
                            //  editText.setText(""+body);
                            if(jsonObject.getInt("Success")==1)
                            {
                                VitalSignlist=toMap(jsonObject.getJSONObject("vital_sign_lists_record"));
                                JSONArray jsonArray=jsonObject.getJSONArray("vital_units_record");
                                JSONArray jsonArray1=jsonObject.getJSONArray("vital_signs_user_record");
                              //  ArrayAdapter aa_vitalsign = new ArrayAdapter(getActivity(),R.layout.spinner_item,vitalSignname);
                                for (int i = 0; i <jsonArray.length() ; i++)
                                {
                                    JSONObject  jsonObject1=jsonArray.getJSONObject(i);
                                    VitalUnitlist.put(jsonObject1.getInt("id"),jsonObject1.getString("name"));
                                    VitalSignunit.put(jsonObject1.getInt("vital_sign_list_id"),jsonObject1.getString("name"));
                                }
                                for (int k = 0; k <jsonArray1.length() ; k++)
                                {
                                    JSONObject jsonObject1=jsonArray1.getJSONObject(k);
                                    String vitalsignname=VitalSignlist.get(jsonObject1.getInt("vital_sign_list_id"));
                                    String unitname=VitalUnitlist.get(jsonObject1.getInt("vital_unit_id"));
                                    vitalsignlists.add(new vitalsignlist(jsonObject1.getInt("id"),vitalsignname,unitname,jsonObject1.getString("max_observation"),jsonObject1.getString("min_observation"),jsonObject1.getString("observation"),jsonObject1.getString("remark")));

                                }
                                customAdapter_vitalsigns=new CustomAdapter_vitalsigns(vitalsignlists,getActivity());
                                recyclerView.setAdapter(customAdapter_vitalsigns);
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
