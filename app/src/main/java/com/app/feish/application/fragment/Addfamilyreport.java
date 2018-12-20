package com.app.feish.application.fragment;

import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import com.app.feish.application.Connectiondetector;
import com.app.feish.application.R;
import com.app.feish.application.Remote.ApiUtils;
import com.app.feish.application.Remote.EncryptionDecryption;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

/**
 * Created by lenovo on 6/4/2016.
 */
public class Addfamilyreport extends Fragment {
    // Store instance variables
  View view1;
    public static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");
  Spinner spinner_re,spinner_heaslth;
  EditText editText_memname,editText_desc,editText_age,editText_diseasename,editText_year;
  Button button_fmhis;
  Connectiondetector connectiondetector;
  String userid;
  String str_memname="",str_desc="",str_age="",str_deseasename="",str_year="",str_re="",str_health="";
HashMap<Integer,String> relationrecord= new HashMap<>();
ArrayList<String> re_name= new ArrayList<>();

    public static Addfamilyreport newInstance(int page, String title) {
        Addfamilyreport fragmentFirst = new Addfamilyreport();
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
        userid=getArguments().getString("Stringlist");

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }


    // Inflate the view for the fragment based on layout XML
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
         view1 = inflater.inflate(R.layout.fragment_familyhistory, container, false);
         connectiondetector= new Connectiondetector(getActivity());
         spinner_re=view1.findViewById(R.id.spinner_memrelation);
         spinner_heaslth=view1.findViewById(R.id.spinner_health);
         editText_year=view1.findViewById(R.id.disease_year);
         editText_desc=view1.findViewById(R.id.fh_desc);
         editText_diseasename=view1.findViewById(R.id.diseasename);
         editText_age=view1.findViewById(R.id.age);
         editText_memname=view1.findViewById(R.id.memname);
        button_fmhis=view1.findViewById(R.id.btn_famhistory);
        button_fmhis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                str_age=editText_age.getText().toString();
                str_desc=editText_desc.getText().toString();
                str_deseasename=editText_diseasename.getText().toString();
                str_health=spinner_heaslth.getSelectedItem().toString();
                str_memname=editText_memname.getText().toString();
                str_re=spinner_re.getSelectedItem().toString();
                for (Object o : relationrecord.keySet()) {
                    if (relationrecord.get(o).equals(str_re)) {
                        str_re = o.toString();

                    }
                }
                str_year=editText_year.getText().toString();
                if(validatefamilyhis()) {
                    if(connectiondetector.isConnectingToInternet()) {

                           addingdata();
                      //  addingdataMDB();
                    }
                    else
                        Toast.makeText(getActivity(), "No Internet!!", Toast.LENGTH_SHORT).show();
                }

            }
        });
        if(connectiondetector.isConnectingToInternet())
        fetch();
        else
            Toast.makeText(getActivity(), "No Internet!!", Toast.LENGTH_SHORT).show();

   //     getActivity().setTitle("CollegeName");


        return view1;
    }
    private Request family_his() {
        JSONObject postdata = new JSONObject();
        String encryptmember= EncryptionDecryption.encode(str_memname);
        try {
            postdata.put("user_id", Integer.parseInt(userid));
            postdata.put("member_name",encryptmember);
            postdata.put("relationship_id",Integer.parseInt(str_re));
            postdata.put("age",Integer.parseInt(str_age));
            postdata.put("disease_id",str_deseasename);
            postdata.put("current_status",str_health);
            postdata.put("year",Integer.parseInt(str_year));
            postdata.put("description",str_desc);
            //postdata.put("password",lpassword);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(JSON, postdata.toString());
        return new Request.Builder()
                .addHeader("X-Api-Key", "AB5433GMDF657VBB")
                .addHeader("Content-Type", "application/json")
                .url(ApiUtils.BASE_URL+"addPatientFamilyHistory")
                .post(body)
                .build();

    }
    private void addingdata()
    {
        OkHttpClient client = new OkHttpClient();
        Request request = family_his();
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
                            // String newres=body.substring(body.lastIndexOf(">")+1,body.lastIndexOf("}"))+"}";
                            //  Toast.makeText(getActivity(), ""+newres, Toast.LENGTH_SHORT).show();

                            JSONObject jsonObject=new JSONObject(body);

                            // Toast.makeText(context, ""+body, Toast.LENGTH_SHORT).show();
                            if(jsonObject.getInt("Success")==1)
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
    private Request family_hisMDB() {
        JSONObject postdatauser = new JSONObject();

        String encryptmember=EncryptionDecryption.encode(str_memname);
            try {
               /* JSONObject postdatauser = new JSONObject();
                JSONObject postdatavalue = new JSONObject();
                JSONArray jsonArrayrecord = new JSONArray();
                JSONObject postdata = new JSONObject();*/
                postdatauser.put("user_id", Integer.parseInt(userid));
                postdatauser.put("member_name",str_memname);
                postdatauser.put("relationship_id",Integer.parseInt(str_re));
                postdatauser.put("age",Integer.parseInt(str_age));
                postdatauser.put("disease_id",str_deseasename);
                postdatauser.put("current_status",str_health);
                postdatauser.put("year",Integer.parseInt(str_year));
                postdatauser.put("description",str_desc);

                postdatauser.put("modified_by", Prefhelper.getInstance(getActivity()).getUserid());
                postdatauser.put("modified_at",new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(new java.util.Date()));
                postdatauser.put("source_type","mobile");
                postdatauser.put("deleted_flag","0");


              /*  postdata.put("FamilyHistory", postdatauser);

                postdatavalue.put("value", postdata);

                jsonArrayrecord.put(postdatavalue);

                postdatamain.put("record", jsonArrayrecord);
                Log.d("register data", postdatamain.toString());
*/
            } catch (JSONException e) {
                e.printStackTrace();
            }
           // Log.d("medicaldata",postdatamain.toString());
            //postdata.put("password",lpassword);

        RequestBody body = RequestBody.create(JSON, postdatauser.toString());
        return new Request.Builder()
                /*.addHeader("X-Api-Key", "AB5433GMDF657VBB")
                .addHeader("Content-Type", "application/json")*/
                .url(ApiUtils.BASE_URLMAngoDB+"add/familyhistory")
                .post(body)
                .build();

    }
    private void addingdataMDB()
    {
        OkHttpClient client = new OkHttpClient();
        Request request = family_hisMDB();
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
                        Toast.makeText(getActivity(), ""+body, Toast.LENGTH_SHORT).show();
                    }
                });
            }

        });

    }

    private void fetch()
    {
        OkHttpClient client = new OkHttpClient();
        Request request = fetchingrelationshiprecord();
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
                            JSONObject jsonObject=new JSONObject(body);
                            relationrecord=toMap(jsonObject);
                            ArrayAdapter aa_vitalsign = new ArrayAdapter(getActivity(),R.layout.spinner_item,re_name);
                            spinner_re.setAdapter(aa_vitalsign);


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

    private Request fetchingrelationshiprecord() {
        JSONObject postdata = new JSONObject();
        RequestBody body = RequestBody.create(JSON, postdata.toString());
        return new Request.Builder()
                .addHeader("X-Api-Key", "AB5433GMDF657VBB")
                .addHeader("Content-Type", "application/json")
                .url(ApiUtils.BASE_URL+"fetchrelationship")
                .post(body)
                .build();

    }


    public void hideSoftKeyboard() {
        if (getActivity().getCurrentFocus() != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getContext().getSystemService(getContext().INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
        }
    }
    private  Object fromJson(Object json) throws JSONException
    {
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
    public HashMap<Integer, String> toMap(JSONObject object) throws JSONException {
        HashMap<Integer, String> map = new HashMap();
        Iterator keys = object.keys();
        while (keys.hasNext()) {
            String key = (String) keys.next();
            re_name.add(fromJson(object.get(key)).toString());
            map.put(Integer.parseInt(key), fromJson(object.get(key)).toString());
        }
        return map;
    }
    private boolean validatefamilyhis(){
        if(str_age.compareTo("")==0)
        {
            Toast.makeText(getActivity(),"age field empty",Toast.LENGTH_LONG).show();
            return false;
        }
        else if (str_desc.compareTo("")==0)
        {
            Toast.makeText(getActivity(),"Description field empty",Toast.LENGTH_LONG).show();
            return false;
        }
        if(str_deseasename.compareTo("")==0)
        {
            Toast.makeText(getActivity(),"Disease  field empty",Toast.LENGTH_LONG).show();
            return false;
        }
        if(str_health.compareTo("Select")==0)
        {
            Toast.makeText(getActivity(),"health field empty",Toast.LENGTH_LONG).show();
            return false;
        }
        if(str_memname.compareTo("")==0)
        {
            Toast.makeText(getActivity(),"member name field empty",Toast.LENGTH_LONG).show();
            return false;
        } if(str_re.compareTo("")==0)
        {
            Toast.makeText(getActivity()," field empty",Toast.LENGTH_LONG).show();
            return false;
        } if(str_year.compareTo("")==0)
        {
            Toast.makeText(getActivity(),"year field empty",Toast.LENGTH_LONG).show();
            return false;
        }

        else {
            return true;
        }
    }

}