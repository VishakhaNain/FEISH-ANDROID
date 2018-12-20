package com.app.feish.application.fragment;


import android.app.Activity;
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
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.app.feish.application.R;
import com.app.feish.application.Remote.ApiUtils;
import com.app.feish.application.model.vitalsignlist;
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
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import static com.app.feish.application.Patient.VitalSigns.JSON;

/**
 * Created by lenovo on 6/4/2016.
 */
public class ListMedicalhistory extends Fragment {
    HashMap<Integer,String> MedicalCon= new HashMap<>();
    int userid;
    private Request listfamily_his() {
        JSONObject postdata = new JSONObject();
        try {
            postdata.put("user_id",userid);
            //postdata.put("password",lpassword);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(JSON, postdata.toString());
        return new Request.Builder()
                .addHeader("X-Api-Key", "AB5433GMDF657VBB")
                .addHeader("Content-Type", "application/json")
                .url(ApiUtils.BASE_URL+"listMedicalHistory")
                .post(body)
                .build();

    }
    public  HashMap<Integer, String> toMap(JSONObject object) throws JSONException {
        HashMap<Integer, String> map = new HashMap();
        Iterator keys = object.keys();
        while (keys.hasNext()) {
            String key = (String) keys.next();
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
                Log.i("1234", "onResponse: "+body);
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                             try

                        {
                           JSONObject jsonObject= new JSONObject(body);
                            if(jsonObject.getString("message").equals("Success"))
                            {

                                JSONArray jsonArray=jsonObject.getJSONArray("data");
                                MedicalCon=toMap(jsonObject.getJSONObject("conditionslist"));
                                for (int i = 0; i <jsonArray.length() ; i++)
                                {
                                    JSONObject jsonObject1=jsonArray.getJSONObject(i);
                                    vitalsignlists.add(new vitalsignlist(jsonObject1.getInt("id"),jsonObject1.getString("conditions"), jsonObject1.getString("condition_type"), jsonObject1.getString("current_medication"), jsonObject1.getString("mh_date"), jsonObject1.getString("description"),""));
                                    }
                                 customAdapter_vitalsigns = new CustomAdapter_medicalhistory(getActivity(), vitalsignlists);
                                listView.setAdapter(customAdapter_vitalsigns);
                                progressBar.setVisibility(View.GONE);

                            }
                            else {
                                Toast.makeText(getActivity(), ""+jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
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

    // Store instance variables
    public static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");
  View view1;
    CustomAdapter_medicalhistory customAdapter_vitalsigns;
    ListView listView;
    ProgressBar progressBar;
    ArrayList<vitalsignlist> vitalsignlists= new ArrayList<>();
    //AIzaSyAK0_mt4pnKbt8Dr--Wdaf8ABuBwElvvA8Config
    // newInstance constructor for creating fragment with arguments
    public static ListMedicalhistory newInstance(int page, String title) {
        ListMedicalhistory fragmentFirst = new ListMedicalhistory();
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
        userid=Integer.parseInt(getArguments().getString("Stringlist"));

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        listView= view1.findViewById(R.id.list);
        progressBar=view1.findViewById(R.id.progressBar);
        fetch();
        }


    // Inflate the view for the fragment based on layout XML
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
         view1 = inflater.inflate(R.layout.fragment_list, container, false);
        /* if(getArguments().getInt("someInt")==2)
         {
             userid=Integer.parseInt("798");

         }
         else
         {
             userid=Integer.parseInt(Prefhelper.getInstance(getActivity()).getUserid());

         }*/

   //     getActivity().setTitle("CollegeName");


        return view1;
    }
    public void hideSoftKeyboard() {
        if (getActivity().getCurrentFocus() != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getContext().getSystemService(getContext().INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
        }
    }
    class CustomAdapter_medicalhistory extends BaseAdapter {

        Context context;
        List<vitalsignlist> list;
        private  LayoutInflater inflater=null;
        public CustomAdapter_medicalhistory(Context context, List<vitalsignlist> list) {
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

        private class Holder
        {
            TextView textView_signnameunit,textView_maxob;
            TextView textView_minob,textView_totalob,textView_remark;
            TextView textView_memame,textView_re;
            TextView textView_diname,textView_staus,textView_desc;
            Button button;
        }
        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            // TODO Auto-generated method stub
            Holder holder=new Holder();
            View rowView;

            rowView = inflater.inflate(R.layout.vitalsignlist, null);
            holder.textView_signnameunit=   rowView.findViewById(R.id.signnameunit);
            holder.textView_maxob =   rowView.findViewById(R.id.maxob);
            holder.textView_minob =   rowView.findViewById(R.id.minob);
            holder.textView_totalob =   rowView.findViewById(R.id.totalob);
            holder.textView_remark =   rowView.findViewById(R.id.remark);
            holder.button =   rowView.findViewById(R.id.delete);
            holder.textView_memame=   rowView.findViewById(R.id.memname);
            holder.textView_re =   rowView.findViewById(R.id.relation);
            holder.textView_diname =   rowView.findViewById(R.id.dieasename);
            holder.textView_staus =   rowView.findViewById(R.id.stausyear);
            holder.textView_desc =   rowView.findViewById(R.id.desc);

            holder.textView_memame.setText("Conditon  :");
            holder.textView_re.setText("Contiion Type  :");
            holder.textView_diname.setText("C_Medi :");
            holder.textView_staus.setText("Date  :");
            holder.textView_desc.setText("Desc  :");

            String name=MedicalCon.get(Integer.parseInt(list.get(position).getSign()));
                    holder.textView_signnameunit.setText(name);
            holder.textView_maxob.setText(list.get(position).getUnit());
            if(list.get(position).getMaxo().equals("0"))
                holder.textView_minob.setText("No");
            else
                holder.textView_minob.setText("Yes");
            holder.textView_totalob.setText(list.get(position).getMino());
            holder.textView_remark.setText(list.get(position).getTotalo());
            rowView.findViewById(R.id.delete).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DeleteFamilyHistory(list.get(position).getId(),position);
                }
            });

            return rowView;
        }
        private void DeleteFamilyHistory(int id, final int pos)
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
                    .url(ApiUtils.BASE_URL+"detelemedicalhistory")
                    .post(body)
                    .build();
        }

    }

}

