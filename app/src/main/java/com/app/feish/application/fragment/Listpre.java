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
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.app.feish.application.Connectiondetector;
import com.app.feish.application.R;
import com.app.feish.application.Remote.ApiUtils;
import com.app.feish.application.model.vitalsignlist;
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
import java.util.Arrays;
import java.util.List;

/**
 * Created by lenovo on 6/4/2016.
 */
public class Listpre extends Fragment {
    // Store instance variables
    public static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");
  View view1;
    CustomAdapter_Pre customAdapter_vitalsigns;
    ListView listView;
    ArrayList<vitalsignlist> vitalsignlists= new ArrayList<>();
    //AIzaSyAK0_mt4pnKbt8Dr--Wdaf8ABuBwElvvA8Config
    // newInstance constructor for creating fragment with arguments
    Connectiondetector connectiondetector;
    public static Listpre newInstance(int page, String title) {
        Listpre fragmentFirst = new Listpre();
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
    /*    vitalsignlists.add(new vitalsignlist("","","","","","",""));
        vitalsignlists.add(new vitalsignlist("","","","","","",""));
        vitalsignlists.add(new vitalsignlist("","","","","","",""));
        vitalsignlists.add(new vitalsignlist("","","","","","",""));*/

        connectiondetector= new Connectiondetector(getActivity());
        if(connectiondetector.isConnectingToInternet())
            listingdata();
        else
            Toast.makeText(getActivity(), "No Internet!!", Toast.LENGTH_SHORT).show();
        }


    // Inflate the view for the fragment based on layout XML
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
         view1 = inflater.inflate(R.layout.fragment_list, container, false);
   //     getActivity().setTitle("CollegeName");


        return view1;
    }
    public void hideSoftKeyboard() {
        if (getActivity().getCurrentFocus() != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getContext().getSystemService(getContext().INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
        }
    }
    private Request listpre() {
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
                .url(ApiUtils.BASE_URL+"listprescrption")
                .post(body)
                .build();

    }
    private void listingdata()
    {
        OkHttpClient client = new OkHttpClient();
        Request request = listpre();
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
                            if(jsonObject.getString("message").equals("success"))
                            {
                                JSONArray jsonArray = jsonObject.optJSONArray("data");
                                for (int i = 0; i <jsonArray.length() ; i++) {
                                    JSONObject jsonObject1=jsonArray.getJSONObject(i);
                                    vitalsignlists.add(new vitalsignlist(jsonObject1.getString("pre_name"), jsonObject1.getString("med_name"), jsonObject1.getString("des_name"), jsonObject1.getString("unit_dose"), jsonObject1.getString("total_dose"), jsonObject1.getString("ingestiontime"), jsonObject1.getString("power"),jsonObject1.getString("advise"),jsonObject1.getString("med_type"),jsonObject1.getString("med_time")));
                                }
                                customAdapter_vitalsigns = new CustomAdapter_Pre(getActivity(), vitalsignlists);
                                listView.setAdapter(customAdapter_vitalsigns);
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

}
 class CustomAdapter_Pre extends BaseAdapter {

    Context context;
    List<vitalsignlist> list;
    private  LayoutInflater inflater=null;
    public CustomAdapter_Pre(Context context, List<vitalsignlist> list) {
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
        TextView textView_signnameunit,textView_maxob;
        TextView textView_minob,textView_totalob,textView_remark;
        TextView textView_memame,textView_re;
        TextView textView_diname,textView_staus,textView_desc,textView_name,textView_txt,textView_txtrunn;
        LinearLayout linearLayout_llisrun,textView_treat;
    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        Holder holder=new Holder();
        View rowView;

        rowView = inflater.inflate(R.layout.vitalsignlist, null);
        holder.textView_signnameunit= (TextView) rowView.findViewById(R.id.signnameunit);
        holder.linearLayout_llisrun=rowView.findViewById(R.id.llisrun);
        holder.textView_maxob = (TextView) rowView.findViewById(R.id.maxob);
        holder.textView_name = (TextView) rowView.findViewById(R.id.name);
        holder.textView_txt = (TextView) rowView.findViewById(R.id.txt);
        holder.textView_txtrunn = (TextView) rowView.findViewById(R.id.txtrunn);
        holder.textView_treat =  rowView.findViewById(R.id.treat);
        holder.textView_minob = (TextView) rowView.findViewById(R.id.minob);
        holder.textView_totalob = (TextView) rowView.findViewById(R.id.totalob);
        holder.textView_remark = (TextView) rowView.findViewById(R.id.remark);
        holder.textView_memame= (TextView) rowView.findViewById(R.id.memname);
        holder.textView_re = (TextView) rowView.findViewById(R.id.relation);
        holder.textView_diname = (TextView) rowView.findViewById(R.id.dieasename);
        holder.textView_staus = (TextView) rowView.findViewById(R.id.stausyear);
        holder.textView_desc = (TextView) rowView.findViewById(R.id.desc);

        holder.textView_memame.setText("Medicine Name  :");
        holder.textView_re.setText("Dose  :");
        holder.textView_diname.setText("Time :");
        holder.textView_staus.setText("Disease Name  :");
        holder.textView_desc.setText("Desc  :");
        holder.textView_txt.setText("Shift  :");
        holder.textView_name.setVisibility(View.VISIBLE);
        holder.textView_name.setText(list.get(position).getName());

        holder.textView_signnameunit.setText(list.get(position).getAge()+" ("+list.get(position).getDesc()+"  mg )");


        holder.textView_totalob.setText(list.get(position).getRelation());

        if(list.get(position).getMedtype().equals("0"))
            holder.textView_maxob.setText(list.get(position).getStatus()+"spoon "+"( "+list.get(position).getDisease()+" spoon )");
        else
            holder.textView_maxob.setText(list.get(position).getStatus()+"tab "+"( "+list.get(position).getDisease()+" tab )");


        if(Integer.parseInt(list.get(position).getYear())==1)
        holder.textView_minob.setText("Before Lunch");
        else
            holder.textView_minob.setText("After Lunch");

        holder.textView_remark.setText(list.get(position).getAdvise());

        holder.textView_treat.setVisibility(View.VISIBLE);
        holder.linearLayout_llisrun.setVisibility(View.GONE);

        List<String> l = Arrays.asList(list.get(position).getMed_time().split(","));
        String text="";
        for (int i = 0; i <l.size() ; i++) {
            switch (l.get(i))
            {
                  case " 1":
                    text=text+", Morn";
                    break;
                    case " 2":
                        text=text+", Aftnoon";
                    break;
                    case " 3":
                        text=text+", Evening";
                    break;
                    case " 4":
                        text=text+", Night";
                    break;
                    default:
                        break;
            }
            holder.textView_txtrunn.setText(text);
        }
        return rowView;
    }

}
