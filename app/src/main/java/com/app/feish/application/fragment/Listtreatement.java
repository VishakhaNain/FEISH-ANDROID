package com.app.feish.application.fragment;


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
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
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
import java.util.List;
import static com.app.feish.application.Patient.VitalSigns.JSON;

/**
 * Created by lenovo on 6/4/2016.
 */
public class Listtreatement extends Fragment {
    ProgressBar progressBar;
    private Request listfamily_his() {
        JSONObject postdata = new JSONObject();
        try {
            postdata.put("user_id", Integer.parseInt(Prefhelper.getInstance(getActivity()).getUserid()));
            //postdata.put("password",lpassword);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(JSON, postdata.toString());
        return new Request.Builder()
                .addHeader("X-Api-Key", "AB5433GMDF657VBB")
                .addHeader("Content-Type", "application/json")
                .url(ApiUtils.BASE_URL+"listTreatment")
                .post(body)
                .build();

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
                                    JSONObject jsonObject2=jsonObject1.getJSONObject("TreatmentHistory");
                                    JSONObject jsonObject3=jsonObject1.getJSONObject("Procedure");
                                    vitalsignlists.add(new vitalsignlist(0,jsonObject2.getString("name"),jsonObject2.getString("start_date"),jsonObject2.getString("end_date"),jsonObject2.getString("is_cured"),jsonObject2.getString("is_running"),jsonObject3.getString("name"),jsonObject2.getString("description")));

                                }
                                customAdapter_vitalsigns = new CustomAdapter_listtreatement(getActivity(), vitalsignlists);
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

    // Store instance variables
  View view1;
    CustomAdapter_listtreatement customAdapter_vitalsigns;
    ListView listView;
    ArrayList<vitalsignlist> vitalsignlists= new ArrayList<>();
    //AIzaSyAK0_mt4pnKbt8Dr--Wdaf8ABuBwElvvA8Config
    // newInstance constructor for creating fragment with arguments
    public static Listtreatement newInstance(int page, String title) {
        Listtreatement fragmentFirst = new Listtreatement();
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
        listView= view1.findViewById(R.id.list);
        progressBar=view1.findViewById(R.id.progressBar);
        fetch();
      /*  vitalsignlists.add(new vitalsignlist("","","","","",""));
        vitalsignlists.add(new vitalsignlist("","","","","",""));
        vitalsignlists.add(new vitalsignlist("","","","","",""));
        vitalsignlists.add(new vitalsignlist("","","","","",""));
        vitalsignlists.add(new vitalsignlist("","","","","",""));
        customAdapter_vitalsigns=new CustomAdapter_listtreatement(getActivity(),vitalsignlists);
        listView.setAdapter(customAdapter_vitalsigns);*/


    }


    // Inflate the view for the fragment based on layout XML
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
         view1 = inflater.inflate(R.layout.fragment_listtreatment, container, false);
   //     getActivity().setTitle("CollegeName");


        return view1;
    }
    public void hideSoftKeyboard() {
        if (getActivity().getCurrentFocus() != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getContext().getSystemService(getContext().INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
        }
    }


}
 class CustomAdapter_listtreatement extends BaseAdapter {

    Context context;
    List<vitalsignlist> list;
    private  LayoutInflater inflater=null;
    public CustomAdapter_listtreatement(Context context, List<vitalsignlist> list) {
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
        TextView textView_diname,textView_staus,textView_desc,textView_cured,textView_running;
        LinearLayout linearLayout;
    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        Holder holder=new Holder();
        View rowView;

        rowView = inflater.inflate(R.layout.vitalsignlist, null);
        holder.textView_signnameunit=rowView.findViewById(R.id.signnameunit);
        holder.textView_maxob = rowView.findViewById(R.id.maxob);
        holder.textView_minob = rowView.findViewById(R.id.minob);
        holder.textView_cured = rowView.findViewById(R.id.txtiscu);
        holder.textView_running = rowView.findViewById(R.id.txtrunn);
        holder.linearLayout = rowView.findViewById(R.id.treat);
        holder.textView_totalob = rowView.findViewById(R.id.totalob);
        holder.textView_remark =rowView.findViewById(R.id.remark);
        holder.textView_memame= rowView.findViewById(R.id.memname);
        holder.textView_re =  rowView.findViewById(R.id.relation);
        holder.textView_diname = rowView.findViewById(R.id.dieasename);
        holder.textView_staus =  rowView.findViewById(R.id.stausyear);
        holder.textView_desc = rowView.findViewById(R.id.desc);
        holder.linearLayout.setVisibility(View.VISIBLE);

        holder.textView_memame.setText("Name :");
        holder.textView_re.setText("S_Date :");
        holder.textView_diname.setText("E_Date :");
        holder.textView_staus.setText("Procedure Name :");
        holder.textView_desc.setText("Desc :");

        holder.textView_signnameunit.setText(list.get(position).getName());

        holder.textView_maxob.setText(list.get(position).getAge().substring(0,list.get(position).getAge().indexOf(" ")));
        holder.textView_minob.setText(list.get(position).getRelation().substring(0,list.get(position).getRelation().indexOf(" ")));


        holder.textView_totalob.setText(list.get(position).getYear());
        holder.textView_remark.setText(list.get(position).getDesc());


        if(list.get(position).getStatus().equals("1"))
        holder.textView_running.setText("Yes");
        else
            holder.textView_running.setText("No");

        if(list.get(position).getDisease().equals("1"))
            holder.textView_cured.setText("Yes");
        else
        holder.textView_cured.setText("No");

        rowView.findViewById(R.id.delete).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DeleteTreatment(list.get(position).getId(),position);
            }
        });


        return rowView;
    }
     private void DeleteTreatment(int id, final int pos)
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
                 .url(ApiUtils.BASE_URL+"deteleTreatmentHistory")
                 .post(body)
                 .build();
     }


 }
