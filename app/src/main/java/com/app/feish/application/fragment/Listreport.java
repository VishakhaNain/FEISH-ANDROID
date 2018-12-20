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
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.app.feish.application.Connectiondetector;
import com.app.feish.application.Patient.VitalSigns;
import com.app.feish.application.R;
import com.app.feish.application.Remote.ApiUtils;
import com.app.feish.application.model.listassistantmodel;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;
import com.squareup.picasso.Picasso;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import static com.app.feish.application.fragment.ListMedicalhistory.JSON;

/**
 * Created by lenovo on 6/4/2016.
 */
public class Listreport extends Fragment {
    // Store instance variables
  View view1;
    CustomAdapter_listreport customAdapter_listreport;
    ProgressBar progressBar;
    Connectiondetector connectiondetector;
    ListView listView;
    int userid=0;
    ArrayList<listassistantmodel>  listassistantmodels= new ArrayList<>();
    ArrayList<Integer>  listassistantmodelsid= new ArrayList<>();
    //AIzaSyAK0_mt4pnKbt8Dr--Wdaf8ABuBwElvvA8Config
    // newInstance constructor for creating fragment with arguments
    public static Listreport newInstance(int page, String title) {
        Listreport fragmentFirst = new Listreport();
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
        connectiondetector=new Connectiondetector(getActivity());
        if(connectiondetector.isConnectingToInternet())
        fetch();
        else
            Toast.makeText(getActivity(), "No Internet", Toast.LENGTH_SHORT).show();
    }


    // Inflate the view for the fragment based on layout XML
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
         view1 = inflater.inflate(R.layout.fragment_list, container, false);
   //     getActivity().setTitle("CollegeName");

       /* if(getArguments().getInt("someInt")==2)
        {
            userid=Integer.parseInt("798");

        }
        else
        {
            userid=Integer.parseInt(Prefhelper.getInstance(getActivity()).getUserid());

        }*/
        return view1;
    }
    public void hideSoftKeyboard() {
        if (getActivity().getCurrentFocus() != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getContext().getSystemService(getContext().INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
        }
    }
    private Request listfamily_his() {
        JSONObject postdata = new JSONObject();
        try {
            postdata.put("user_id",Integer.parseInt(getArguments().getString("Stringlist")));
            //postdata.put("password",lpassword);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(JSON, postdata.toString());
        return new Request.Builder()
                .addHeader("X-Api-Key", "AB5433GMDF657VBB")
                .addHeader("Content-Type", "application/json")
                .url(ApiUtils.BASE_URL+"listLabResult")
                .post(body)
                .build();

    }
    public HashMap<Integer, String> toMap(JSONObject object) throws JSONException {
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
                            if(jsonObject.getString("message").equals("success"))
                            {

                                JSONArray jsonArray=jsonObject.getJSONArray("data");
                                for (int i = 0; i <jsonArray.length() ; i++)
                                {
                                    JSONObject jsonObject1=jsonArray.getJSONObject(i);
                                    JSONObject jsonObject2=jsonObject1.getJSONObject("LabTestResult");
                                    JSONObject jsonObject3=jsonObject1.getJSONObject("Test");
                                    listassistantmodels.add(new listassistantmodel(jsonObject3.getString("test_name"),jsonObject2.getString("test_date"),jsonObject2.getString("observed_value"),jsonObject2.getString("description"),"http://dev.feish.online/img/lab_test_reports/"+jsonObject2.getString("report"),"",R.drawable.patienr));
                                     listassistantmodelsid.add(jsonObject2.getInt("id"));
                                }
                                customAdapter_listreport = new CustomAdapter_listreport(getActivity(), listassistantmodels,listassistantmodelsid);
                                listView.setAdapter(customAdapter_listreport);
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


}
 class CustomAdapter_listreport extends BaseAdapter {

    Context context;
    List<listassistantmodel> list;
    List<Integer> listid;
    private  LayoutInflater inflater=null;
    public CustomAdapter_listreport(Context context, List<listassistantmodel> list, List<Integer> listid) {
        // TODO Auto-generated constructor stub
        this.list=list;
        this.context=context;
        this.listid=listid;
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
        TextView tv_name,tv_mob,tv_email,tv_pos;
        ImageView imageView;
        TextView  textView_servicename;
    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        Holder holder=new Holder();
        View rowView;

        rowView = inflater.inflate(R.layout.listassistant, null);
        holder.imageView=rowView.findViewById(R.id.img);
        holder.tv_name=rowView.findViewById(R.id.assi_name);
        holder.tv_mob=rowView.findViewById(R.id.assis_mob);
        holder.tv_email=rowView.findViewById(R.id.assis_email);
        holder.tv_pos=rowView.findViewById(R.id.assisal);
        holder.textView_servicename=rowView.findViewById(R.id.servicename);

        holder.tv_name.setText(list.get(position).getSal());
        holder.tv_mob.setText("Test :"+list.get(position).getFname());
        holder.tv_email.setText("Ob Value:: "+ list.get(position).getLname());
        holder.tv_pos.setText( "Desc : "+list.get(position).getMob());
        holder.textView_servicename.setVisibility(View.GONE);
        Picasso.with(context)
                .load(list.get(position).getEmail())
                 //this is also optional if some error has occurred in downloading the image this image would be displayed
                .into( holder.imageView);


rowView.findViewById(R.id.delete).setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        DeleteReport(listid.get(position),position);
    }
});
        return rowView;
    }
     private void DeleteReport(int id, final int pos)
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
         RequestBody body = RequestBody.create(VitalSigns.JSON, postdata.toString());
         return new Request.Builder()
                 .addHeader("X-Api-Key", "AB5433GMDF657VBB")
                 .addHeader("Content-Type", "application/json")
                 .url(ApiUtils.BASE_URL+"detelelabresult")
                 .post(body)
                 .build();
     }

}
