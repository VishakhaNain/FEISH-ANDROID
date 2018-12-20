package com.app.feish.application.doctor;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.app.feish.application.Adpter.CustomAdapter_dietplan;
import com.app.feish.application.Adpter.CustomAdapter_vitalsigns;
import com.app.feish.application.Connectiondetector;
import com.app.feish.application.R;
import com.app.feish.application.Remote.ApiUtils;
import com.app.feish.application.model.dietlistpojo;
import com.app.feish.application.model.dietplanfulldetailpojo;
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

import static com.app.feish.application.fragment.ListFamilyhistory.JSON;


public class DrDietPlanfrg extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    ProgressBar progressBar;
ListView listView;
    String recipename="",recipecalories="";

    ArrayList<dietplanfulldetailpojo>  dietplanfulldetailpojos= new ArrayList<>();
ArrayList<dietlistpojo> dietlistpojos= new ArrayList<>();
    CustomAdapter_dietplan customAdapter_dietplan;

    // TODO: Rename and change types of parameters
    private String mParam1;
    View v;
    CustomAdapter_vitalsigns customAdapter_vitalsigns;
    private String mParam2;

   // private OnFragmentInteractionListener mListener;
Connectiondetector connectiondetector;
    public DrDietPlanfrg() {
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
    public static DrDietPlanfrg newInstance(String param1, String param2) {
        DrDietPlanfrg fragment = new DrDietPlanfrg();
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
        v= inflater.inflate(R.layout.fragment_list, container, false);
        v.findViewById(R.id.progressBar).setVisibility(View.GONE);
        listView=v.findViewById(R.id.list);
        progressBar=v.findViewById(R.id.progressBar);
        connectiondetector= new Connectiondetector(getActivity());
        if(connectiondetector.isConnectingToInternet())
        {
            fetchdata();
        }
        else
        {
            Toast.makeText(getActivity(), "No Internet!!", Toast.LENGTH_SHORT).show();
        }

         return v;
    }
    private Request AddDietplan()
    {
        JSONObject postdata = new JSONObject();
        try {
            postdata.put("patient_id",Integer.parseInt(mParam2));

        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(JSON, postdata.toString());
        return new Request.Builder()
                .addHeader("X-Api-Key", "AB5433GMDF657VBB")
                .addHeader("Content-Type", "application/json")
                .url(ApiUtils.BASE_URL+"listDietPlan")
                .post(body)
                .build();
    }
    private void fetchdata()
    {
        OkHttpClient client = new OkHttpClient();
        Request request = AddDietplan();
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

                        progressBar.setVisibility(View.GONE);
                        try {
                            //      editText.setText(body);
                            JSONObject jsonObject = new JSONObject(body);
                            if (jsonObject.getString("message").equals("success"))
                            {
                                JSONArray jsonArray=jsonObject.getJSONArray("data");
                                for (int i = 0; i <jsonArray.length() ; i++)
                                {
                                    JSONObject jsonObject1=jsonArray.getJSONObject(i);
                                    JSONObject jsonObject2=jsonObject1.getJSONObject("DietPlan");
                                    JSONArray jsonObject3=jsonObject1.getJSONArray("DietPlansDetail");
                                    JSONArray jsonObject_recipename=jsonObject1.getJSONArray("recipe_name");
                                    ArrayList<dietlistpojo>  dietlistpojos= new ArrayList<>();
                                    for (int j = 0; j <jsonObject3.length() ; j++)
                                    {
                                        JSONObject  jsonObject4=jsonObject3.getJSONObject(j);
                                        JSONObject jsonObject5=jsonObject4.getJSONObject("DietPlansDetail");
                                        if(jsonObject5.getInt("recipe_code")!=0) {

                                            //  for (int k = 0; k <jsonObject_recipename.length() ; k++) {
                                            JSONArray jsonElements= jsonObject_recipename.getJSONArray(j);
                                            //   Toast.makeText(getActivity(), ""+jsonElements.getJSONObject(0), Toast.LENGTH_SHORT).show();
                                            if(jsonElements.length()>0) {
                                                JSONObject recipemainname = jsonElements.getJSONObject(0);
                                                recipename = recipemainname.getString("recipe_name");
                                                recipecalories = recipemainname.getString("recipe_calories");
                                            }
                                            else
                                            {
                                                recipename="recipe_name";
                                                recipecalories="calories";
                                            }

                                            //    }
                                            dietlistpojos.add(new dietlistpojo(jsonObject5.getString("weekday"), jsonObject5.getString("time"), jsonObject5.getString("description"), recipename, recipecalories, jsonObject5.getString("food_type"), String.valueOf(jsonObject5.getInt("recipe_code")), jsonObject5.getInt("id")));
                                        }
                                        else
                                            dietlistpojos.add(new dietlistpojo(jsonObject5.getString("weekday"),jsonObject5.getString("time"),jsonObject5.getString("description"),"","","","",jsonObject5.getInt("id")));

                                    }
                                    dietplanfulldetailpojos.add(new dietplanfulldetailpojo(jsonObject2.getInt("id"),jsonObject2.optString("plan_name"),jsonObject2.optString("start_date"),jsonObject2.optString("end_date"),dietlistpojos));


                                }
                                customAdapter_dietplan= new CustomAdapter_dietplan(getActivity(),dietplanfulldetailpojos,Integer.parseInt(mParam2));
                                listView.setAdapter(customAdapter_dietplan);

                            }
                            else
                            {
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

}
