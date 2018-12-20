package com.app.feish.application.fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.app.feish.application.Connectiondetector;
import com.app.feish.application.R;
import com.app.feish.application.Remote.ApiUtils;
import com.app.feish.application.model.doctormsglist;
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
import java.util.List;

/**
 * Created by lenovo on 6/4/2016.
 */
public class Addpre extends Fragment {
    // Store instance variables
  View view1;
  Button button;
   EditText editText_prename,editText_medname,editText_power,editText_advice,editText_unitquan,editText_totalquan;
   String str_prename="",str_medname="",str_power="",str_advice="",str_unitquan="",str_totalquan="",str_medtype="",str_ingestiontime="",str_medtime="";
   RadioGroup radioGroup_ingestiontime;
   RadioButton radioButton_bm,radioButton_am;
   CheckBox checkBox_m,checkBox_a,checkBox_n,checkBox_e;
    Connectiondetector connectiondetector;
    ArrayList<doctormsglist> diseaseslists= new ArrayList<>();
    CustomAdapter_bd customAdapter_bd;
    int diseasesid=0;
    String diseasesname="";
  Spinner spinner,spinner_medtype;
    public static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");
   
    public static Addpre newInstance(int page, String title) {
        Addpre fragmentFirst = new Addpre();
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
        connectiondetector=  new Connectiondetector(getActivity());
        if(connectiondetector.isConnectingToInternet())
            fetchdata();
        else
            Toast.makeText(getActivity(), "No Internet", Toast.LENGTH_SHORT).show();

    }


    // Inflate the view for the fragment based on layout XML
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
         view1 = inflater.inflate(R.layout.fragment_precription, container, false);
         spinner=view1.findViewById(R.id.disease_spinner);
         spinner_medtype=view1.findViewById(R.id.medtype_spinner);
         spinner_medtype.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
             @Override
             public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                 str_medtype=String.valueOf(position);
             }

             @Override
             public void onNothingSelected(AdapterView<?> parent) {

             }
         });

         button=view1.findViewById(R.id.save);
         editText_prename=view1.findViewById(R.id.prename);
         editText_medname=view1.findViewById(R.id.medname);
         editText_power=view1.findViewById(R.id.medpower);
         editText_unitquan=view1.findViewById(R.id.unitquantity);
         editText_totalquan=view1.findViewById(R.id.totalquantity);
         editText_advice=view1.findViewById(R.id.medesc);
         radioGroup_ingestiontime=view1.findViewById(R.id.ingestiontime);
         radioButton_bm=view1.findViewById(R.id.radio_bm);
         radioButton_am=view1.findViewById(R.id.radio_am);
         checkBox_a=view1.findViewById(R.id.checkBox_a);
         checkBox_e=view1.findViewById(R.id.checkBox_e);
         checkBox_m=view1.findViewById(R.id.checkBox_m);
         checkBox_n=view1.findViewById(R.id.checkBox_n);



         radioGroup_ingestiontime.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
             @Override
             public void onCheckedChanged(RadioGroup group, int checkedId) {
                 if(checkedId==R.id.radio_bm)
                 {
                     str_ingestiontime="1";
                 }
                 else if(checkedId==R.id.radio_am)
                 {
                     str_ingestiontime="2";
                 }
             }
         });
         button.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 if(checkBox_m.isChecked())
                 {
                     str_medtime=str_medtime+", 1";
                 }
                  if(checkBox_a.isChecked())
                 {
                     str_medtime=str_medtime+", 2";
                 }
                  if(checkBox_e.isChecked())
                 {
                     str_medtime=str_medtime+", 3";

                 }
                  if(checkBox_n.isChecked())
                 {
                     str_medtime=str_medtime+", 4";
                 }
                 str_prename=editText_prename.getText().toString();
                 str_medname=editText_medname.getText().toString();
                 str_power=editText_power.getText().toString();
                 str_unitquan=editText_unitquan.getText().toString();
                 str_totalquan=editText_totalquan.getText().toString();
                 str_advice=editText_advice.getText().toString();
                 if(connectiondetector.isConnectingToInternet()) {
                     if(validateassistantsign())
                         addingdata();
                 }
                 else
                     Toast.makeText(getActivity(), "No Internet!", Toast.LENGTH_SHORT).show();

             }
         });



        return view1;
    }
    private Request addpre() {
        JSONObject postdata = new JSONObject();
        try {
            postdata.put("user_id",Integer.parseInt(Prefhelper.getInstance(getActivity()).getUserid()));
            postdata.put("pre_name",str_prename);
            postdata.put("med_name",str_medname);
            postdata.put("des_name",diseasesname);
            postdata.put("unit_dose",Integer.parseInt(str_unitquan));
            postdata.put("total_dose",Integer.parseInt(str_totalquan));
            postdata.put("ingestiontime",Integer.parseInt(str_ingestiontime));
            postdata.put("power",Integer.parseInt(str_power));
            postdata.put("med_type",Integer.parseInt(str_medtype));
            postdata.put("advise",str_advice);
            postdata.put("med_time",str_medtime);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(JSON, postdata.toString());
        return new Request.Builder()
                .addHeader("X-Api-Key", "AB5433GMDF657VBB")
                .addHeader("Content-Type", "application/json")
                .url(ApiUtils.BASE_URL+"addprescrption")
                .post(body)
                .build();

    }
    private void addingdata()
    {
        OkHttpClient client = new OkHttpClient();
        Request request = addpre();
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
                   //     Toast.makeText(getActivity(), ""+body, Toast.LENGTH_SHORT).show();
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
        if(str_prename.compareTo("")==0)
        {
            Toast.makeText(getActivity(),"Enter prescrption Name",Toast.LENGTH_LONG).show();
            return false;
        }
        else if (str_medname.compareTo("")==0)
        {
            Toast.makeText(getActivity(),"Enter medicine Name",Toast.LENGTH_LONG).show();
            return false;
        }
        if(diseasesname.compareTo("")==0)
        {
            Toast.makeText(getActivity(),"Enter disease",Toast.LENGTH_LONG).show();
            return false;
        }
        else if (str_power.compareTo("")==0)
        {
            Toast.makeText(getActivity(),"Enter Power",Toast.LENGTH_LONG).show();
            return false;
        } if(str_advice.compareTo("")==0)
        {
            Toast.makeText(getActivity(),"Enter Desc",Toast.LENGTH_LONG).show();
            return false;
        }
        else if (str_unitquan.compareTo("")==0)
        {
            Toast.makeText(getActivity(),"Enter Unit Quantity",Toast.LENGTH_LONG).show();
            return false;
        } else if (str_totalquan.compareTo("")==0)
        {
            Toast.makeText(getActivity(),"Enter Total Quantity",Toast.LENGTH_LONG).show();
            return false;
        }
        else if (str_medtime.compareTo("")==0)
        {
            Toast.makeText(getActivity(),"Enter Time",Toast.LENGTH_LONG).show();
            return false;
        }
        else if (str_ingestiontime.compareTo("")==0)
        {
            Toast.makeText(getActivity(),"Select ",Toast.LENGTH_LONG).show();
            return false;
        }
        else {
            return true;
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
try{
    JSONObject jsonObject= new JSONObject(body);
    if(jsonObject.getBoolean("status"))
    {
        JSONArray jsonArray= jsonObject.optJSONArray("data");
        for (int i = 0; i <jsonArray.length() ; i++) {
            JSONObject jsonObject1=jsonArray.getJSONObject(i);
            diseaseslists.add(new doctormsglist(jsonObject1.getInt("id"),jsonObject1.getString("name")));
            }
        customAdapter_bd= new CustomAdapter_bd(getActivity(),diseaseslists);
        spinner.setAdapter(customAdapter_bd);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
               diseasesid=diseaseslists.get(position).getId();
               diseasesname=diseaseslists.get(position).getName();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
    else
    {

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

    private Request listservices() {
        JSONObject postdata = new JSONObject();

        RequestBody body = RequestBody.create(JSON, postdata.toString());
        return new Request.Builder()
                .addHeader("X-Api-Key", "AB5433GMDF657VBB")
                .addHeader("Content-Type", "application/json")
                .url(ApiUtils.BASE_URL+"listDisease")
                .post(body)
                .build();
    }

}
class CustomAdapter_bd extends BaseAdapter {
    Context context;
    LayoutInflater inflter;
    List<doctormsglist> medicalconditionlists;
    public CustomAdapter_bd(Context applicationContext, List<doctormsglist> medicalconditionlists) {
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
