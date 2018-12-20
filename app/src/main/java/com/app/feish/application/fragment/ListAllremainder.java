package com.app.feish.application.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
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
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.app.feish.application.Connectiondetector;
import com.app.feish.application.R;
import com.app.feish.application.Remote.ApiUtils;
import com.app.feish.application.modelclassforapi.PatientAllNotes;
import com.app.feish.application.modelclassforapi.PatientNoteRecord;
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
import java.util.List;
/**
 * Created by lenovo on 6/4/2016.
 */
public class ListAllremainder extends Fragment {
    // Store instance variables
  View view1;
  PatientAllNotes patientAllNotes;
    CustomAdapter_allremainder customAdapter_vitalsigns;
    ListView listView;
    ProgressBar progressBar;
    Connectiondetector connectiondetector;
    int drnote=0;
   
    public static ListAllremainder newInstance(int page, String title) {
        ListAllremainder fragmentFirst = new ListAllremainder();
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
        drnote=getArguments().getInt("someInt");
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        listView= view1.findViewById(R.id.list);
        progressBar= view1.findViewById(R.id.progressBar);
        connectiondetector= new Connectiondetector(getActivity());
        if(connectiondetector.isConnectingToInternet())
        fetch();
        else
            Toast.makeText(getActivity(), "No Internet!!", Toast.LENGTH_SHORT).show();
       }

    // Inflate the view for the fragment based on layout XML
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
         view1 = inflater.inflate(R.layout.fragment_listremainder, container, false);
   //     getActivity().setTitle("CollegeName");


        return view1;
    }
   /* public void hideSoftKeyboard() {
        if (getActivity().getCurrentFocus() != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getContext().getSystemService(getContext().INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
        }
    }*/


    private Request listfamily_his() {
        String url="";
        if(drnote==0)
            url=ApiUtils.BASE_URL+"ListPatientNotes";
            else
            url=ApiUtils.BASE_URL+"ListDoctorNotes";

        JSONObject postdata = new JSONObject();
        try {
            postdata.put("user_id",Integer.parseInt(Prefhelper.getInstance(getActivity()).getUserid()));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(JSON, postdata.toString());
        return new Request.Builder()
                .addHeader("X-Api-Key", "AB5433GMDF657VBB")
                .addHeader("Content-Type", "application/json")
                .url(url)
                .post(body)
                .build();

    }
    public void Patientfecthnotes(String response) {
        Gson gson = new GsonBuilder().create();
        patientAllNotes = gson.fromJson(response, PatientAllNotes.class);
    }
    private void fetch()
    {
        OkHttpClient client = new OkHttpClient();
        Request request = listfamily_his();
        Log.i("", "onClick: "+request);
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                Log.i("Activity", "onFailure: Fail");
            }
            @Override
            public void onResponse(final Response response) throws IOException {

                final String body=response.body().string();
                Patientfecthnotes(body);
                Log.i("1234", "onResponse: "+body);
                
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try
                        {
                            progressBar.setVisibility(View.GONE);
                            if(patientAllNotes.getSuccess().equals(1))
                            {
                                customAdapter_vitalsigns = new CustomAdapter_allremainder(getActivity(), patientAllNotes.getPatientNoteRecord());
                                listView.setAdapter(customAdapter_vitalsigns);
                            }
                            else
                                {
                                Toast.makeText(getActivity(), "Error", Toast.LENGTH_SHORT).show();
                            }


                        }
                        catch (Exception e)
                        {
                            progressBar.setVisibility(View.GONE);
                            Toast.makeText(getActivity(), ""+e, Toast.LENGTH_SHORT).show();
                        }

                    }
                });
            }

        });

    }

    // Store instance variables
    public static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");


    public class CustomAdapter_allremainder extends BaseAdapter {

        Context context;
        List<PatientNoteRecord> list;
        private  LayoutInflater inflater;
        CustomAdapter_allremainder(Context context, List<PatientNoteRecord> list) {
            // TODO Auto-generated constructor stub
            this.list=list;
            this.context=context;
            inflater = ( LayoutInflater )context.
                    getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            // TODO Auto-generated method stub
            return position;
        }

        @Override
        public long getItemId(int position) {
            // TODO Auto-generated method stub
            return position;
        }

        public class Holder
        {
            TextView textView_subject,textView_desc;
            TextView textView_sdate,textView_edate,textView_time;
            TextView textView_info;
        }
        @SuppressLint({"ViewHolder", "InflateParams"})
        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            // TODO Auto-generated method stub
            Holder holder=new Holder();
            View rowView;

            rowView = inflater.inflate(R.layout.layout_patientallnotes, null);
           holder.textView_subject=rowView.findViewById(R.id.pn_sub);
            holder.textView_desc =rowView.findViewById(R.id.pn_desc);
            holder.textView_sdate =rowView.findViewById(R.id.pn_sdate);
            holder.textView_edate =rowView.findViewById(R.id.pn_enddate);
            holder.textView_time =rowView.findViewById(R.id.pn_time);
            holder.textView_info=rowView.findViewById(R.id.pn_info);

            holder.textView_subject.setText("Subject:  "+list.get(position).getSubject());
            holder.textView_desc.setText("Description:  "+list.get(position).getDescription());
            if(list.get(position).getIsRemender()) {
                holder.textView_time.setText("Time: "+list.get(position).getTime());
                holder.textView_sdate.setText("Date: "+list.get(position).getDate());
            }
            else
            {
                holder.textView_time.setVisibility(View.GONE);
                holder.textView_sdate.setVisibility(View.GONE);
            }
            if(list.get(position).getIsRemender()&& list.get(position).getObjType()!=null)
            {
                holder.textView_edate.setText("E_Date: "+list.get(position).getEndDate());
                switch (Integer.parseInt(list.get(position).getObjType()))
                {
                    case 1:
                        if(list.get(position).getNoOfDay()!=null)
                        {
                        String noday[]=list.get(position).getNoOfDay().toString().split(",");
                        String weekday="";
                        for (String aNoday : noday) {
                            if (aNoday.equals("1"))
                                weekday = "M";
                            if (aNoday.equals("2")) {
                                if (!weekday.equals(""))
                                    weekday = weekday + ",Tu";
                                else
                                    weekday = weekday + "Tu";
                            }
                            if (aNoday.equals("3")) {
                                if (!weekday.equals(""))
                                    weekday = weekday + ",We";
                                else
                                    weekday = weekday + "We";
                            }
                            if (aNoday.equals("4")) {
                                if (!weekday.equals(""))
                                    weekday = weekday + ",Th";
                                else
                                    weekday = weekday + "Th";
                            }
                            if (aNoday.equals("5")) {
                                if (!weekday.equals(""))
                                    weekday = weekday + ",Fr";
                                else
                                    weekday = weekday + "Fr";
                            }
                            if (aNoday.equals("6")) {
                                if (!weekday.equals(""))
                                    weekday = weekday + ",Sa";
                                else
                                    weekday = weekday + "Sa";
                            }
                            if (aNoday.equals("7")) {
                                if (!weekday.equals(""))
                                    weekday = weekday + ",Su";
                                else
                                    weekday = weekday + "Su";
                            }
                            holder.textView_info.setText("No of Days:  " + weekday);
                        }


                        }


                        break;
                    case 2:
                        holder.textView_info.setText(list.get(position).getMonthDate().toString());
                        break;
                    case 3:
                        holder.textView_info.setText(list.get(position).getHrsDetail());
                        break;
                }

            }
            else
            {
                holder.textView_info.setVisibility(View.GONE);
                holder.textView_edate.setVisibility(View.GONE);
            }
            rowView.findViewById(R.id.delete).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DeleteNotes(Integer.parseInt(list.get(position).getId()),position);
                }
            });

            return rowView;
        }
        private void DeleteNotes(int id, final int pos)
        {
            OkHttpClient client = new OkHttpClient();
            Request request = AddVitalSignApp(id);
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

                    ((Activity)context).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                JSONObject jsonObject = new JSONObject(body);
                                if (jsonObject.getBoolean("status")) {
                                    list.remove(pos);
                                    notifyDataSetChanged();
                                    AlertDialog.Builder builder;
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                        builder = new AlertDialog.Builder(context, android.R.style.Theme_Material_Dialog_Alert);
                                    } else {
                                        builder = new AlertDialog.Builder(context);
                                    }
                                    builder.setTitle("Message")
                                            .setMessage(jsonObject.getString("message"))
                                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int which) {
                                                    // continue with delete
                                                    dialog.dismiss();
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

                                } else {
                                    Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show();
                                }
                            } catch (Exception e) {
                                Toast.makeText(context, "" + e, Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }

            });
        }
        private Request AddVitalSignApp(int id)
        {
            String url="";
            if(drnote==0)
            {
                url=ApiUtils.BASE_URL+"detelePatientNote";
            }
            else
            {
                url=ApiUtils.BASE_URL+"deteleDoctorHistory";
            }
            JSONObject postdata = new JSONObject();
            try {
                postdata.put("id",id);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            RequestBody body = RequestBody.create(JSON, postdata.toString());
            return new Request.Builder()
                    .addHeader("X-Api-Key", "AB5433GMDF657VBB")
                    .addHeader("Content-Type", "application/json")
                    .url(url)
                    .post(body)
                    .build();
        }


    }

}