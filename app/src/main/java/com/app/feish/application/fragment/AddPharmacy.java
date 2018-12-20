package com.app.feish.application.fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.app.feish.application.Connectiondetector;
import com.app.feish.application.Patient.MedicalHitoryp;
import com.app.feish.application.R;
import com.app.feish.application.Remote.ApiUtils;
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
import static com.app.feish.application.fragment.AddPlan.JSON;

/**
 * Created by lenovo on 6/4/2016.
 */
public class AddPharmacy extends Fragment {
    // Store instance variables
  View view1;  ArrayList<doctormsglist> statelist= new ArrayList<>();
    ArrayList<doctormsglist> citylist= new ArrayList<>();
    int str_stateid=0,str_cityid=0;
    EditText et_phaname,et_phamob,et_phaemail,et_phafulladdressID,et_phalocalityID,et_pincode;
    String str_phaname,str_phamob,str_phaemail,str_phafulladdressID,str_phalocalityID,str_pincode,str_state,str_city;
    Connectiondetector connectiondetector;
    CardView button_btn_addpharmacy;
    Spinner spinner_state_spinner,spinner_city_spinner;
    public static AddPharmacy newInstance(int page, String title) {
        AddPharmacy fragmentFirst = new AddPharmacy();
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
        connectiondetector= new Connectiondetector(getActivity());
        if(connectiondetector.isConnectingToInternet())
            addingdata();
        else
            Toast.makeText(getActivity(), "No Internet!!", Toast.LENGTH_SHORT).show();
    }

    // Inflate the view for the fragment based on layout XML
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
         view1 = inflater.inflate(R.layout.fragment_pharmacy, container, false);
         spinner_city_spinner=view1.findViewById(R.id.city_spinner);
         spinner_state_spinner=view1.findViewById(R.id.state_spinner);
        et_phaname=view1.findViewById(R.id.phaname);
        et_phamob=view1.findViewById(R.id.phaphone);
        et_phaemail=view1.findViewById(R.id.phaemail);
        et_phalocalityID=view1.findViewById(R.id.phalocalityID);
        et_phafulladdressID=view1.findViewById(R.id.phafulladdressID);
        et_pincode=view1.findViewById(R.id.phapincodeID);
        // spinner = view1.findViewById(R.id.spinner_sername);
        button_btn_addpharmacy=view1.findViewById(R.id.btn_addpharmacy);
        button_btn_addpharmacy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                str_phaname=et_phaname.getText().toString();
                str_phamob=et_phamob.getText().toString();
                str_phaemail=et_phaemail.getText().toString();
                str_phalocalityID=et_phalocalityID.getText().toString();
                str_phafulladdressID=et_phafulladdressID.getText().toString();
                str_pincode=et_pincode.getText().toString();

                if(connectiondetector.isConnectingToInternet())
                {
                    if(validateassistantsign())
                    {
                        addpharmacy();
                    }
                }
                else
                {

                    Toast.makeText(getActivity(), "No Internet!!", Toast.LENGTH_SHORT).show();
                }

            }
        });

        return view1;
    }


    private void addpharmacy()
    {
        OkHttpClient client = new OkHttpClient();
        Request validation_request = adddrpharmacy();
        client.newCall(validation_request).enqueue(new Callback() {

            @Override
            public void onFailure(Request request, IOException e) {

                // Toast.makeText(getActivity(),"Fail",Toast.LENGTH_LONG).show();
                Log.i("Activity", "onFailure: Fail");
            }

            @Override
            public void onResponse(final Response response) throws IOException {
                final String body = response.body().string();
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getActivity(), ""+body, Toast.LENGTH_SHORT).show();
                        try {
                            JSONObject jsonObject = new JSONObject(body);
                            if(jsonObject.getBoolean("status")) {
                                AlertDialog.Builder builder;
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                    builder = new AlertDialog.Builder(getActivity(), android.R.style.Theme_Material_Dialog_Alert);
                                } else {
                                    builder = new AlertDialog.Builder(getActivity());
                                }
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
                            else
                            {
                                Toast.makeText(getActivity(), ""+jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                            }
                        }
                        catch (JSONException e)
                        {

                            Toast.makeText(getActivity(), ""+e, Toast.LENGTH_SHORT).show();
                        }

                    }
                });
            }
        });

    }

    private Request adddrpharmacy() {
        JSONObject postdata = new JSONObject();
        try {
            postdata.put("added_by", Prefhelper.getInstance(getActivity()).getUserid());
            postdata.put("name", str_phaname);
            postdata.put("email", str_phaemail);
            postdata.put("mob", str_phamob);
            postdata.put("address", str_phafulladdressID);
            postdata.put("locality", str_phalocalityID);
            postdata.put("state", str_state);
            postdata.put("city", str_city);
            postdata.put("pincode", str_pincode);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(JSON, postdata.toString());
        return new Request.Builder()
                .addHeader("X-Api-Key", "AB5433GMDF657VBB")
                .addHeader("Content-Type", "application/json")
                .url(ApiUtils.BASE_URL+"addPharmacy")
                .post(body)
                .build();
    }
    private boolean validateassistantsign(){
        if(str_phaname.compareTo("")==0)
        {
            Toast.makeText(getActivity(),"Enter Pharmacy Name",Toast.LENGTH_LONG).show();
            return false;
        }
        else if (str_phamob.compareTo("")==0)
        {
            Toast.makeText(getActivity(),"Enter Pharmacy Number",Toast.LENGTH_LONG).show();
            return false;
        }
        else if (str_phaemail.compareTo("")==0)
        {
            Toast.makeText(getActivity(),"Enter Pharmacy Email",Toast.LENGTH_LONG).show();
            return false;
        }
        else if(str_phafulladdressID.compareTo("")==0)
        {
            Toast.makeText(getActivity(),"Enter Pharmacy Full Address",Toast.LENGTH_LONG).show();
            return false;
        }
        else if (str_phalocalityID.compareTo("")==0)
        {
            Toast.makeText(getActivity(),"Enter Pharmacy Locality",Toast.LENGTH_LONG).show();
            return false;
        } else if (str_pincode.compareTo("")==0)
        {
            Toast.makeText(getActivity(),"Enter Pincode",Toast.LENGTH_LONG).show();
            return false;
        }
        else {
            return true;
        }
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
                            CustomAdapter_pharmacy customAdapter=new CustomAdapter_pharmacy(getActivity(),statelist);
                            spinner_state_spinner.setAdapter(customAdapter);
                            spinner_state_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                    str_stateid=statelist.get(position).getId();
                                    str_state=statelist.get(position).getName();
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
                            CustomAdapter_pharmacy customAdapter=new CustomAdapter_pharmacy(getActivity(),citylist);
                            spinner_city_spinner.setAdapter(customAdapter);
                            spinner_city_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                    str_cityid=citylist.get(position).getId();
                                    str_city=citylist.get(position).getName();
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
}
class CustomAdapter_pharmacy extends BaseAdapter {
    Context context;
    LayoutInflater inflter;
    List<doctormsglist> medicalconditionlists;
    public CustomAdapter_pharmacy(Context applicationContext, List<doctormsglist> medicalconditionlists) {
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
