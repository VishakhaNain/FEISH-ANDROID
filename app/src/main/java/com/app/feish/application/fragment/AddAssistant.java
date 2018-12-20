package com.app.feish.application.fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.app.feish.application.Connectiondetector;
import com.app.feish.application.R;
import com.app.feish.application.Remote.ApiUtils;
import com.app.feish.application.modelclassforapi.Datum2;
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
import java.util.List;

import static com.app.feish.application.fragment.Addfamilyreport.JSON;

/**
 * Created by lenovo on 6/4/2016.
 */
public class AddAssistant extends Fragment {
    // Store instance variables
  View view1;
  Spinner spinner_sername_spinner,spinner_dsalutation_spinner,spinner_assistant_spinner;
  EditText editText_dfirstname,editText_dlastname,editText_dphone,editText_demail,editText_assipass;
  Button button_btn_addassistant;
  String str_fname="",str_lname="",str_mob="",str_email="",str_pass="",str_assis="",str_sername="",str_dsal="";
    List<Datum2> l;
    private ListServicesContact serviceResponse;
    Connectiondetector connectiondetector;
    CustomAdapter_bd customAdapter_bd;
    public static AddAssistant newInstance(int page, String title) {
        AddAssistant fragmentFirst = new AddAssistant();
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
    }
    // Inflate the view for the fragment based on layout XML
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
         view1 = inflater.inflate(R.layout.fragment_assistant, container, false);

         connectiondetector= new Connectiondetector(getActivity());
        spinner_sername_spinner=view1.findViewById(R.id.sername_spinner);
        spinner_dsalutation_spinner=view1.findViewById(R.id.dsalutation_spinner);
        spinner_assistant_spinner=view1.findViewById(R.id.assistant_spinner);
        editText_dfirstname=view1.findViewById(R.id.dfirstname);
        editText_dlastname=view1.findViewById(R.id.dlastname);
        editText_dphone=view1.findViewById(R.id.dphone);
        editText_demail=view1.findViewById(R.id.demail);
        editText_assipass=view1.findViewById(R.id.assipass);
        button_btn_addassistant=view1.findViewById(R.id.btn_addassistant);
        button_btn_addassistant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                str_fname=editText_dfirstname.getText().toString();
                str_lname=editText_dlastname.getText().toString();
                str_mob=editText_dphone.getText().toString();
                str_email=editText_demail.getText().toString();
                str_pass=editText_assipass.getText().toString();
                
                if(connectiondetector.isConnectingToInternet()) {
                    if(validateassistantsign())
                    addingdata();
                }
                else
                    Toast.makeText(getActivity(), "No Internet!", Toast.LENGTH_SHORT).show();

            }
        });

        spinner_assistant_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spinner_dsalutation_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                       str_dsal=String.valueOf((position+1));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        if(connectiondetector.isConnectingToInternet())
            fetchdata();


        return view1;
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
                // final String body=response.body().string();

                listServiceResponse(response.body().string());
                final boolean isSuccessful=serviceResponse.getStatus();
                l=serviceResponse.getData();
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (isSuccessful) {
                            customAdapter_bd= new CustomAdapter_bd(getActivity(),l);
                            spinner_sername_spinner.setAdapter(customAdapter_bd);

                        } else {
                            Toast.makeText(getActivity(), "Could not load the list!!", Toast.LENGTH_SHORT).show();
                        }
                        spinner_sername_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                str_sername=l.get(position).getService().getId();
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {

                            }
                        });
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

    class CustomAdapter_bd extends BaseAdapter {
        Context context;
        LayoutInflater inflter;
        List<Datum2> medicalconditionlists;
        public CustomAdapter_bd(Context applicationContext, List<Datum2> medicalconditionlists) {
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
            names.setText(medicalconditionlists.get(i).getService().getTitle());
            return view;
        }


    }
    private Request addassistant() {
        JSONObject postdata = new JSONObject();
        try {
            postdata.put("added_by_doctor_id",Prefhelper.getInstance(getActivity()).getUserid());
            postdata.put("email",str_email);
            postdata.put("first_name",str_fname);
            postdata.put("last_name",str_lname);
            postdata.put("mobile",str_mob);
            postdata.put("password",str_pass);
            postdata.put("service_id",str_sername);
            postdata.put("salutation",str_dsal);
            //postdata.put("password",lpassword);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(JSON, postdata.toString());
        return new Request.Builder()
                .addHeader("X-Api-Key", "AB5433GMDF657VBB")
                .addHeader("Content-Type", "application/json")
                .url(ApiUtils.BASE_URL+"AddAssistantNew")
                .post(body)
                .build();

    }
    private void addingdata()
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
                        try
                        {
                            JSONObject jsonObject=new JSONObject(body);

                            // Toast.makeText(context, ""+body, Toast.LENGTH_SHORT).show();
                            if(jsonObject.getBoolean("status"))
                            {
                                //   Toast.makeText(context, "Success", Toast.LENGTH_SHORT).show();


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
    private boolean validateassistantsign(){
        if(str_fname.compareTo("")==0)
        {
            Toast.makeText(getActivity(),"Enter First Name",Toast.LENGTH_LONG).show();
            return false;
        }
        else if (str_lname.compareTo("")==0)
        {
            Toast.makeText(getActivity(),"Enter Last Name",Toast.LENGTH_LONG).show();
            return false;
        }
        if(str_mob.compareTo("")==0)
        {
            Toast.makeText(getActivity(),"Enter Mob Number",Toast.LENGTH_LONG).show();
            return false;
        }
        else if (str_pass.compareTo("")==0)
        {
            Toast.makeText(getActivity(),"Enter Password",Toast.LENGTH_LONG).show();
            return false;
        } if(str_email.compareTo("")==0)
        {
            Toast.makeText(getActivity(),"Enter Email Id",Toast.LENGTH_LONG).show();
            return false;
        }
        else if (str_sername.compareTo("")==0)
        {
            Toast.makeText(getActivity(),"Select Service Name",Toast.LENGTH_LONG).show();
            return false;
        } else if (str_dsal.compareTo("")==0)
        {
            Toast.makeText(getActivity(),"Select Salutation",Toast.LENGTH_LONG).show();
            return false;
        }
        else {
            return true;
        }
    }
}