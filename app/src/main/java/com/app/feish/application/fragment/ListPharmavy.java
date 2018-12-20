package com.app.feish.application.fragment;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.app.feish.application.Connectiondetector;
import com.app.feish.application.R;
import com.app.feish.application.Remote.ApiUtils;
import com.app.feish.application.model.drplanmodel;
import com.app.feish.application.model.listassistantmodel;
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
public class ListPharmavy extends Fragment {
    // Store instance variables
  View view1;
    CustomAdapter_listpharmacy customAdapter_listassistant;
    Connectiondetector connectiondetector;
    ListView listView;
    ArrayList<listassistantmodel>  listassistantmodels= new ArrayList<>();
    //AIzaSyAK0_mt4pnKbt8Dr--Wdaf8ABuBwElvvA8Config
    // newInstance constructor for creating fragment with arguments
    public static ListPharmavy newInstance(int page, String title) {
        ListPharmavy fragmentFirst = new ListPharmavy();
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
        listView= view1.findViewById(R.id.list);
        connectiondetector= new Connectiondetector(getActivity());
      /*  listassistantmodels.add(new listassistantmodel("Pharmacy Name","Address","Pha@gmail.com","9456123022","Fax No","",R.drawable.doctor_images));
        listassistantmodels.add(new listassistantmodel("Pharmacy Name","Address","Pha@gmail.com","9456123022","Fax No","",R.drawable.doctor_images));
        listassistantmodels.add(new listassistantmodel("Pharmacy Name","Address","Pha@gmail.com","9456123022","Fax No","",R.drawable.doctor_images));
        listassistantmodels.add(new listassistantmodel("Pharmacy Name","Address","Pha@gmail.com","9456123022","Fax No","",R.drawable.doctor_images));
        listassistantmodels.add(new listassistantmodel("Pharmacy Name","Address","Pha@gmail.com","9456123022","Fax No","",R.drawable.doctor_images));
        customAdapter_listassistant=new CustomAdapter_listpharmacy(getActivity(),listassistantmodels);
        listView.setAdapter(customAdapter_listassistant);*/

      if(connectiondetector.isConnectingToInternet())
          fetchdata();
      else
          Toast.makeText(getActivity(), "No Internet!!", Toast.LENGTH_SHORT).show();


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
                        JSONObject jsonObject= null;
                        try {
                            jsonObject = new JSONObject(body);

                            if(jsonObject.getString("message").equals("success"))
                            {
                                JSONArray jsonArray= jsonObject.getJSONArray("data");
                                for (int i = 0; i <jsonArray.length() ; i++)
                                {
                                    JSONObject jsonObject1=jsonArray.getJSONObject(i);
                                    listassistantmodels.add(new listassistantmodel(jsonObject1.getString("name"),jsonObject1.getString("locality")+" "+jsonObject1.getString("address"),jsonObject1.getString("email"),jsonObject1.getString("mob"),jsonObject1.getString("pincode")+" "+jsonObject1.getString("city")+" "+jsonObject1.getString("state"),"",R.drawable.doctor_images));
                                    customAdapter_listassistant=new CustomAdapter_listpharmacy(getActivity(),listassistantmodels);
                                    listView.setAdapter(customAdapter_listassistant);

                                }
                            }
                            else
                            {
                                Toast.makeText(getActivity(), ""+jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            Toast.makeText(getActivity(), ""+e, Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                        }

                    }
                });
            }
        });

    }

    private Request listservices() {
        JSONObject postdata = new JSONObject();
        try {
            postdata.put("added_by", Prefhelper.getInstance(getActivity()).getUserid());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(JSON, postdata.toString());
        return new Request.Builder()
                .addHeader("X-Api-Key", "AB5433GMDF657VBB")
                .addHeader("Content-Type", "application/json")
                .url(ApiUtils.BASE_URL+"listPharmacy")
                .post(body)
                .build();
    }


    // Inflate the view for the fragment based on layout XML
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
         view1 = inflater.inflate(R.layout.fragment_listpharmacy, container, false);
   //     getActivity().setTitle("CollegeName");


        return view1;
    }
    public void hideSoftKeyboard() {
        if (getActivity().getCurrentFocus() != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getContext().getSystemService(getContext().INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
        }
    }
    public class CustomAdapter_listpharmacy extends BaseAdapter {

        Context context;
        List<listassistantmodel> list;
        private  LayoutInflater inflater=null;
        public CustomAdapter_listpharmacy(Context context, List<listassistantmodel> list) {
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
            TextView tv_name,tv_mob,tv_email,tv_pos,tv_servicename;
            ImageView imageView;
        }
        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            // TODO Auto-generated method stub
            Holder holder=new Holder();
            View rowView;

            rowView = inflater.inflate(R.layout.listassistant, null);
            holder.tv_name=rowView.findViewById(R.id.assi_name);
            holder.tv_servicename=rowView.findViewById(R.id.servicename);
            holder.tv_servicename.setVisibility(View.GONE);
            holder.tv_mob=rowView.findViewById(R.id.assis_mob);
            holder.imageView=rowView.findViewById(R.id.img);
            holder.tv_email=rowView.findViewById(R.id.assis_email);
            holder.tv_pos=rowView.findViewById(R.id.assisal);
            holder.tv_name.setText(list.get(position).getSal()+" "+" "+list.get(position).getLname());
            holder.tv_mob.setText(list.get(position).getMob());
            holder.tv_email.setText(list.get(position).getEmail());
            holder.tv_pos.setText(list.get(position).getFname());
            holder.imageView.setImageResource(list.get(position).getImg());


            return rowView;
        }

    }

}