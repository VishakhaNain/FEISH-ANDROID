package com.app.feish.application.fragment;

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
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.app.feish.application.Connectiondetector;
import com.app.feish.application.R;
import com.app.feish.application.Remote.ApiUtils;
import com.app.feish.application.Remote.EncryptionDecryption;
import com.app.feish.application.sessiondata.Prefhelper;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import static com.app.feish.application.fragment.ListAllremainder.JSON;

/**
 * Created by lenovo on 6/4/2016.
 */
public class AddPatient extends Fragment{
    // Store instance variables
  View view1;
  Spinner spinner_saluation;
  RelativeLayout relativeLayout_addpatient;
  EditText et_fname,et_lname,et_mob,et_email,et_pass,et_cpass;
  RadioGroup  radioGroup_gender;
  RadioButton radioButton_male,radioButton_fenale;
  String str_fname="",str_lname,str_email="",str_mob="",str_salu="",str_gen="",str_password="",str_cpass="";
  Connectiondetector connectiondetector;
String user_id;
    public static AddPatient newInstance(int page, String title) {
        AddPatient fragmentFirst = new AddPatient();
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
        relativeLayout_addpatient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                str_email=et_email.getText().toString();
                str_mob=et_mob.getText().toString();
                str_fname=et_fname.getText().toString();
                str_lname=et_lname.getText().toString();
                str_password=et_pass.getText().toString();
                str_cpass=et_cpass.getText().toString();
                if(connectiondetector.isConnectingToInternet())
                {
                    if(validateassistantsign())
                    {
                        addpatient();
                    }
                }
                else
                {
                    Toast.makeText(getActivity(), "No Internet!!", Toast.LENGTH_SHORT).show();
                }

            }
        });
        radioGroup_gender.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(checkedId==R.id.radiomale)
                {
                    str_gen="1";
                }
                else if(checkedId==R.id.radiofemale)
                {
                    str_gen="2";
                }
            }
        });
        spinner_saluation.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                str_salu=String.valueOf(position+1);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    // Inflate the view for the fragment based on layout XML
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
         view1 = inflater.inflate(R.layout.fragment_patient, container, false);
         connectiondetector= new Connectiondetector(getActivity());
        spinner_saluation=view1.findViewById(R.id.salutation_spinner);
        et_fname=view1.findViewById(R.id.firstname);
        et_lname=view1.findViewById(R.id.lastname);
        et_mob=view1.findViewById(R.id.phone);
        et_email=view1.findViewById(R.id.email);
        et_pass=view1.findViewById(R.id.password);
        et_cpass=view1.findViewById(R.id.cpassword);
        radioGroup_gender=view1.findViewById(R.id.radiogender);
        radioButton_male=view1.findViewById(R.id.radiomale);
        relativeLayout_addpatient=view1.findViewById(R.id.addpatient);
        radioButton_fenale=view1.findViewById(R.id.radiofemale);

        //     getActivity().setTitle("CollegeName");


        return view1;
    }
    private void addpatient()
    {
        OkHttpClient client = new OkHttpClient();
        Request validation_request = adddatapatient();
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
                        try {
                            JSONObject jsonObject = new JSONObject(body);
                            if(jsonObject.getInt("Success")==1) {
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
    private Request adddatapatient() {
        JSONObject postdata = new JSONObject();

        String encryptmobile= EncryptionDecryption.encode(str_mob);

        try {
            postdata.put("salutation", str_salu);
            postdata.put("first_name", str_fname);
            postdata.put("last_name", str_lname);
            postdata.put("gender", str_gen);
            postdata.put("mobile", encryptmobile);
            postdata.put("email", str_email);
            postdata.put("added_by_doctor_id",user_id);
            postdata.put("password",str_password);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(JSON, postdata.toString());
        return new Request.Builder()
                .addHeader("X-Api-Key", "AB5433GMDF657VBB")
                .addHeader("Content-Type", "application/json")
                .url(ApiUtils.BASE_URL+"addpatient")
                .post(body)
                .build();
    }
    private boolean validateassistantsign(){
        if(str_salu.compareTo("")==0)
        {
            Toast.makeText(getActivity(),"Select Salutation",Toast.LENGTH_LONG).show();
            return false;
        }
        else if (str_email.compareTo("")==0)
        {
            Toast.makeText(getActivity(),"Enter Email Id",Toast.LENGTH_LONG).show();
            return false;
        }
        else if (str_mob.compareTo("")==0)
        {
            Toast.makeText(getActivity(),"Enter Mob No",Toast.LENGTH_LONG).show();
            return false;
        }
        else if(str_fname.compareTo("")==0)
        {
            Toast.makeText(getActivity(),"Enter First Name",Toast.LENGTH_LONG).show();
            return false;
        }
        else if (str_lname.compareTo("")==0)
        {
            Toast.makeText(getActivity(),"Enter Last Name",Toast.LENGTH_LONG).show();
            return false;
        } else if (str_password.compareTo("")==0)
        {
            Toast.makeText(getActivity(),"Enter Password",Toast.LENGTH_LONG).show();
            return false;
        } else if (str_cpass.compareTo("")==0)
        {
            Toast.makeText(getActivity(),"Confirm Password",Toast.LENGTH_LONG).show();
            return false;
        } else if (str_gen.compareTo("")==0)
        {
            Toast.makeText(getActivity(),"Select Gender",Toast.LENGTH_LONG).show();
            return false;
        }
        else {
            return true;
        }
    }


}