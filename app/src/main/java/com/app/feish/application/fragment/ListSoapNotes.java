package com.app.feish.application.fragment;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.app.feish.application.Connectiondetector;
import com.app.feish.application.R;
import com.app.feish.application.Remote.ApiUtils;
import com.app.feish.application.doctor.AddWorkingHours;
import com.app.feish.application.model.serviceworkinghours;
import com.app.feish.application.model.vitalsignlist;
import com.app.feish.application.modelclassforapi.Appointmentdatum;
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
import java.util.Arrays;
import java.util.List;

import static com.app.feish.application.fragment.ListMedicalhistory.JSON;

/**
 * Created by lenovo on 6/4/2016.
 */
public class ListSoapNotes extends Fragment {
    String userid;
    Dialog dialog;
    JSONArray jsonArray_prescription;
  View view1;
  ProgressBar progressBar;
    TextView tv_observation,tv_comment,tv_referencename,tv_referenceadd,tv_referncecomment,tv_dieasename,tv_referto;
    CardView cardView;
    ArrayList<serviceworkinghours> serviceworkinghourslist= new ArrayList<>();
    CustomAdapter_soapnotes customAdapter_vitalsigns;
    ListView listView;
    Connectiondetector connectiondetector;
    ArrayList<vitalsignlist> vitalsignlists= new ArrayList<>();
    Appointmentdatum appointmentdatum;
    //AIzaSyAK0_mt4pnKbt8Dr--Wdaf8ABuBwElvvA8Config
    // newInstance constructor for creating fragment with arguments
    public static ListSoapNotes newInstance(String dr_id, String patient_id, Appointmentdatum appointmentdatum) {
        ListSoapNotes fragmentFirst = new ListSoapNotes();
        Bundle args = new Bundle();
        args.putString("dr_id", dr_id);
        args.putString("user_id", patient_id);
        args.putSerializable("appodata",appointmentdatum);


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
        progressBar= view1.findViewById(R.id.progressBar);
        connectiondetector= new Connectiondetector(getActivity());
        appointmentdatum= (Appointmentdatum) getArguments().getSerializable("appodata");
        if(connectiondetector.isConnectingToInternet())
        {
            progressBar.setVisibility(View.VISIBLE);
            fetch();
        }
        else
        {
            Toast.makeText(getActivity(), "No Internet!!", Toast.LENGTH_SHORT).show();
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
                        try {
                            JSONObject jsonObject = new JSONObject(body);
                            if (jsonObject.getString("message").equals("success"))
                            {
                                progressBar.setVisibility(View.GONE);
                                JSONObject jsonObjectdata= jsonObject.getJSONObject("data");
                                JSONArray jsonArray_sopanote=jsonObjectdata.getJSONArray("SoapNote");
                                 jsonArray_prescription=jsonObjectdata.getJSONArray("Prescription");
                                for (int i = 0; i <jsonArray_sopanote.length() ; i++)
                                {
                                    JSONObject jsonObject1=jsonArray_sopanote.getJSONObject(i);
                                    vitalsignlists.add(new vitalsignlist(0,jsonObject1.getString("observation"), jsonObject1.getString("comments"),jsonObject1.getString("reference_name"), "",jsonObject1.getString("reference_address"), jsonObject1.getString("reference_comments"), ""));
                                    customAdapter_vitalsigns = new CustomAdapter_soapnotes(getActivity(), vitalsignlists);
                                    listView.setAdapter(customAdapter_vitalsigns);
                                    }

                            }
                            else
                                {
                                    progressBar.setVisibility(View.GONE);
                                Toast.makeText(getActivity(), "Error", Toast.LENGTH_SHORT).show();
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

    private Request listfamily_his() {
        JSONObject postdata = new JSONObject();
        try {
            postdata.put("user_id",Integer.parseInt(getArguments().getString("user_id")));
            postdata.put("doctor_id",Integer.parseInt(getArguments().getString("dr_id")));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(JSON, postdata.toString());
        return new Request.Builder()
                .addHeader("X-Api-Key", "AB5433GMDF657VBB")
                .addHeader("Content-Type", "application/json")
                .url(ApiUtils.BASE_URL+"listsoapnotefordr")
                .post(body)
                .build();

    }

    // Inflate the view for the fragment based on layout XML
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
         view1 = inflater.inflate(R.layout.fragment_list, container, false);
        // userid=getArguments().getString("Stringlist");

   //     getActivity().setTitle("CollegeName");


        return view1;
    }
    public void hideSoftKeyboard() {
        if (getActivity().getCurrentFocus() != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getContext().getSystemService(getContext().INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
        }
    }
    class CustomAdapter_soapnotes extends BaseAdapter {

        Context context;
        List<vitalsignlist> list;
        private  LayoutInflater inflater=null;
        public CustomAdapter_soapnotes(Context context, List<vitalsignlist> list) {
            // TODO Auto-generated constructor stub
            this.list=list;
            this.context=getActivity();
            inflater = ( LayoutInflater )getActivity().
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
            LinearLayout relativeLayout;
        }
        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            // TODO Auto-generated method stub
            Holder holder=new Holder();
            View rowView;

            rowView = inflater.inflate(R.layout.vitalsignlist, null);
            holder.relativeLayout=rowView.findViewById(R.id.rlmain);
            holder.textView_signnameunit= (TextView) rowView.findViewById(R.id.signnameunit);
            holder.textView_maxob = (TextView) rowView.findViewById(R.id.maxob);
            holder.textView_minob = (TextView) rowView.findViewById(R.id.minob);
            holder.textView_totalob = (TextView) rowView.findViewById(R.id.totalob);
            holder.textView_remark = (TextView) rowView.findViewById(R.id.remark);
            holder.textView_memame= (TextView) rowView.findViewById(R.id.memname);
            holder.textView_re = (TextView) rowView.findViewById(R.id.relation);
            holder.textView_diname = (TextView) rowView.findViewById(R.id.dieasename);
            holder.textView_staus = (TextView) rowView.findViewById(R.id.stausyear);
            holder.textView_desc = (TextView) rowView.findViewById(R.id.desc);

            holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    open(list.get(position).getName(),list.get(position).getAge(),list.get(position).getRelation(),list.get(position).getStatus(),list.get(position).getYear());
                }
            });

            holder.textView_memame.setText("Observation  :");
            holder.textView_re.setText("Comment :");
            holder.textView_diname.setText("");
            holder.textView_staus.setText("Medicine Name:");
            holder.textView_desc.setText("Desc  :");

            holder.textView_signnameunit.setText(list.get(position).getName());
            holder.textView_maxob.setText(list.get(position).getAge());
            holder.textView_minob.setText("");
         /*   if(list.get(position).getMaxo().equals("0"))
                holder.textView_minob.setText("No");
            else
                holder.textView_minob.setText("Yes");*/
            holder.textView_totalob.setText(list.get(position).getDisease());
           holder.textView_remark.setText("");

            return rowView;
        }

    }
    public void open(String observation,String comment,String refername,String referadd,String refercmt)
    {
        TextView tv_drname,tv_drspe,tv_drphone;
        TextView tv_aptientname,tv_pagen,tv_add;
        ImageView imageView_imgser;
        GridView gridview;
     //   fetchsoapnotes();
        dialog= new Dialog(getActivity(),android.R.style.Theme_Light);
        if(dialog.getWindow()!=null)
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.soapnotetemplate);
        dialog.setCanceledOnTouchOutside(false);
        cardView=dialog.findViewById(R.id.cardView);
        gridview=dialog.findViewById(R.id.grid);
        tv_drname=dialog.findViewById(R.id.drname);
        tv_drspe=dialog.findViewById(R.id.drspe);
        tv_add=dialog.findViewById(R.id.address);
        tv_drphone=dialog.findViewById(R.id.serphone);
        imageView_imgser=dialog.findViewById(R.id.imgser);
        tv_aptientname=dialog.findViewById(R.id.patientname);
        tv_dieasename=dialog.findViewById(R.id.dieasename);

        tv_observation=dialog.findViewById(R.id.observation);
        tv_referto=dialog.findViewById(R.id.referto);
        tv_comment=dialog.findViewById(R.id.Componencommentt);
        tv_referencename=dialog.findViewById(R.id.referencename);
        tv_referenceadd=dialog.findViewById(R.id.referenadd);
        tv_referncecomment=dialog.findViewById(R.id.referencecomment);
        tv_observation.setText(observation);
        tv_comment.setText(comment);
        tv_referencename.setText(refername);
        tv_referenceadd.setText(referadd);
        tv_referncecomment.setText(refercmt);

        tv_pagen=dialog.findViewById(R.id.patientge);
        tv_drname.setText(" Dr "+appointmentdatum.getDoctor().getFirstName()+" "+appointmentdatum.getDoctor().getLastName());
     //   tv_drspe.setText(userdata.getUserDetaildata().getName());
        tv_drphone.setText(appointmentdatum.getService().getPhone());

        tv_aptientname.setText(appointmentdatum.getUser().getFirstName()+" "+appointmentdatum.getUser().getLastName());

        tv_add.setText(appointmentdatum.getService().getAddress()+" "+appointmentdatum.getService().getLocality()+" "+appointmentdatum.getService().getCity()+" "+appointmentdatum.getService().getPinCode());
        if(appointmentdatum.getService().getLogo()!=null) {
            Picasso.with(getActivity()).load("http://dev.feish.online/img/services/"+appointmentdatum.getService().getLogo()).into(imageView_imgser);
        }
        // tv_patiage.setText(appointmentdatum.getUser().get);
        if(appointmentdatum.getUser().getGender().equals("1"))
            tv_pagen.setText("MALE");
        else
            tv_pagen.setText("FEMALE");

        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        RecyclerView listView=dialog.findViewById(R.id.list);
        LinearLayoutManager linearLayoutManager= new LinearLayoutManager(getActivity());
        listView.setHasFixedSize(true);
        listView.setLayoutManager(linearLayoutManager);
        listView.setNestedScrollingEnabled(false);
        Customadt customadt= new Customadt(getActivity());
        if(appointmentdatum.getPrescription().size()>0) {
            listView.setAdapter(customadt);
            dialog.show();
        }
        if(serviceworkinghourslist.size()>0)
        {

        }
        else
        {
            fetchworkinghr(gridview);
        }
        dialog.show();
    }
    class Customadt extends  RecyclerView.Adapter<Customadt.MyViewHolder> {

        Context context;

        private LayoutInflater inflater=null;
        public Customadt(Context context) {
            // TODO Auto-generated constructor stub
            this.context=context;
            inflater = ( LayoutInflater )getActivity().
                    getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        }

        public  class MyViewHolder extends RecyclerView.ViewHolder {

            TextView tv_medname,tv_unit,tv_tdose,tv_medtime,tv_ingestiontime;
            CardView cardView;
            public MyViewHolder(View rowView) {
                super(rowView);
                this.tv_medname= rowView.findViewById(R.id.medname);
                this.tv_unit= rowView.findViewById(R.id.unitdose);
                this.tv_tdose= rowView.findViewById(R.id.totaldose);
                this.tv_medtime= rowView.findViewById(R.id.medtime);
                this.tv_ingestiontime= rowView.findViewById(R.id.ingestiontime);


            }
        }



        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.prescriptionreport, parent, false);

            return new MyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
            holder.tv_medname.setText(appointmentdatum.getPrescription().get(position).getMedicineName());

            if(appointmentdatum.getPrescription().get(position).getMedicineType().equals("0")) {
                holder.tv_unit.setText(appointmentdatum.getPrescription().get(position).getUnitQty() + "spoon ");
                holder.tv_tdose.setText(appointmentdatum.getPrescription().get(position).getTotalQty() + " spoon ");
            }
            else {
                holder.tv_unit.setText(appointmentdatum.getPrescription().get(position).getUnitQty() + "tab ");
                holder.tv_tdose.setText(appointmentdatum.getPrescription().get(position).getTotalQty() + " tab ");
            }

            if(Integer.parseInt(appointmentdatum.getPrescription().get(position).getAfterMeal())==1)
                holder.tv_ingestiontime.setText("Before Lunch");
            else
                holder.tv_ingestiontime.setText("After Lunch");
            if(tv_dieasename.getText().toString()!=null)
                tv_dieasename.setText(appointmentdatum.getPrescription().get(position).getDiseasename());
            else
                tv_dieasename.setText(tv_dieasename.getText().toString()+" "+appointmentdatum.getPrescription().get(position).getDiseasename());

            List<String> l = Arrays.asList(appointmentdatum.getPrescription().get(position).getMedicineTime().split(","));
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
                holder.tv_medtime.setText(text);
            }

        }

        @Override
        public long getItemId(int position) {
            // TODO Auto-generated method stub
            return position;
        }

        @Override
        public int getItemCount() {
            return appointmentdatum.getPrescription().size();
        }

   }

    public void fetchworkinghr(final GridView gridView)
    {
        OkHttpClient client = new OkHttpClient();
        Request validation_request = listworkinghr();
        client.newCall(validation_request).enqueue(new Callback() {

            @Override
            public void onFailure(Request request, IOException e) {

                // Toast.makeText(getApplicationContext(),"Fail",Toast.LENGTH_LONG).show();
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
                            if(jsonObject.getInt("Success")==1)
                            {
                                JSONArray jsonArray= jsonObject.getJSONArray("ServicesWorkingTiming");
                                for (int i = 0; i <jsonArray.length() ; i++) {
                                    JSONObject jsonObject1=jsonArray.getJSONObject(i);
                                    serviceworkinghourslist.add(new serviceworkinghours(jsonObject1.getString("open_time"),jsonObject1.getString("close_time"),jsonObject1.getInt("id"),jsonObject1.getInt("day_of_week")));
                                }
                                CustomAdapter_grid arrayAdapter= new CustomAdapter_grid(serviceworkinghourslist);
                                gridView.setAdapter(arrayAdapter);
                            }
                            else
                            {
                                Toast.makeText(getActivity(), "Error!!", Toast.LENGTH_SHORT).show();
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

    private Request listworkinghr() {
        JSONObject postdata = new JSONObject();
        try {
            postdata.put("service_id",Integer.parseInt(appointmentdatum.getService().getId()));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(AddWorkingHours.JSON, postdata.toString());
        return new Request.Builder()
                .addHeader("X-Api-Key", "AB5433GMDF657VBB")
                .addHeader("Content-Type", "application/json")
                .url("http://dev.feish.online/apis/ListWokingHours")
                .post(body)
                .build();
    }

    public class CustomAdapter_grid extends BaseAdapter {
        LayoutInflater inflter;
        List<serviceworkinghours> servicesWorkingTimings;

        CustomAdapter_grid( ArrayList<serviceworkinghours> servicesWorkingTimings)
        {
            this.servicesWorkingTimings=servicesWorkingTimings;
            inflter = (LayoutInflater.from(getActivity()));
        }
        @Override
        public int getCount() {
            return servicesWorkingTimings.size();
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
            view = inflter.inflate(R.layout.serviceworktime, null); // inflate the layout
            TextView day = (TextView) view.findViewById(R.id.day); // get the reference of ImageView
            TextView sdate = (TextView) view.findViewById(R.id.opentime); // get the reference of ImageView
            TextView edate = (TextView) view.findViewById(R.id.closetime); // get the reference of ImageView
            switch (servicesWorkingTimings.get(i).getDay_of_week()){
                case 0:
                    day.setText("Mon");
                    break;
                case 1:
                    day.setText("Tue");
                    break;
                case 2:
                    day.setText("Wed");
                    break;
                case 3:
                    day.setText("Thus");
                    break;
                case 4:
                    day.setText("Fri");
                    break;
                case 5:
                    day.setText("Sat");
                    break;
                case 6:
                    day.setText("Sun");
                    break;

            }

            sdate.setText(servicesWorkingTimings.get(i).getOpen_time());
            edate.setText(servicesWorkingTimings.get(i).getClose_time());

            return view;
        }
    }

}