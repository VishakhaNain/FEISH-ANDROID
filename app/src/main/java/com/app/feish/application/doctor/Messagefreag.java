package com.app.feish.application.doctor;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.app.feish.application.Adpter.CustomAdapter_encounteres;
import com.app.feish.application.Connectiondetector;
import com.app.feish.application.R;
import com.app.feish.application.model.ContactServiceForFetchingMsgPatient;
import com.app.feish.application.model.Datum_FetchingMsg;
import com.app.feish.application.model.encountersmodel;
import com.app.feish.application.modelclassforapi.ContactServiceforMsg;
import com.app.feish.application.sessiondata.Prefhelper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.app.feish.application.doctor.DoctorDashboard.JSON;


public class Messagefreag extends Fragment {
    Connectiondetector connectiondetector;
    private ContactServiceForFetchingMsgPatient serviceResponse;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    ContactServiceforMsg contactServiceforMsg;
    List<Datum_FetchingMsg> l;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    ArrayList<encountersmodel> encountersmodels= new ArrayList<>();
    View v;
    RecyclerViewadt_drmsg recyclerViewadt_drmsg;
    RecyclerView recyclerView;
 public Messagefreag() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Messagefreag.
     */
    // TODO: Rename and change types and number of parameters
    public static Messagefreag newInstance(String param1, String param2) {
        Messagefreag fragment = new Messagefreag();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v= inflater.inflate(R.layout.fragment_messagefreag, container, false);
        // Inflate the layout for this fragment
        recyclerView=v.findViewById(R.id.recycler_view);
        LinearLayoutManager layoutManager= new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        connectiondetector= new Connectiondetector(getActivity());
        if(connectiondetector.isConnectingToInternet())
            fetchdata();
        else
            Toast.makeText(getActivity(), "No Internet!", Toast.LENGTH_SHORT).show();
      /*  recyclerViewadt_drmsg = new RecyclerViewadt_drmsg(encountersmodels,getActivity());
        encountersmodels.add(new encountersmodel("Appointment id : 202","","802","abc","active","20/12/2018 20:02:15",0));
        encountersmodels.add(new encountersmodel("Appointment id : 202","","802","abc","active","20/12/2018 20:02:15",0));
        encountersmodels.add(new encountersmodel("Appointment id : 202","","802","abc","active","20/12/2018 20:02:15",0));
        encountersmodels.add(new encountersmodel("Appointment id : 202","","802","abc","active","20/12/2018 20:02:15",0));
        recyclerView.setAdapter(recyclerViewadt_drmsg);*/
        return v;

    }
    public void fetchdata()
    {
        OkHttpClient client = new OkHttpClient();
        Request validation_request = listservices();
        client.newCall(validation_request).enqueue(new Callback() {

            @Override
            public void onFailure(Request  request, IOException e) {
                Log.i("Activity", "onFailure: Fail");
            }

            @Override
            public void onResponse(final Response response) throws IOException {
                listServiceResponse(response.body().string());
                final boolean isSuccessful=serviceResponse.getStatus();
                l=serviceResponse.getData();
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (isSuccessful) {

                            Toast.makeText(getActivity(), "List of Message ", Toast.LENGTH_SHORT).show();
                            recyclerViewadt_drmsg= new RecyclerViewadt_drmsg(l,getActivity());
                            recyclerView.setAdapter(recyclerViewadt_drmsg);
                       TextView textView= getActivity().findViewById(R.id.totalmsg);
                       textView.setText(""+l.size());
                        } else {
                            Toast.makeText(getActivity(), "Could not load the list!!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }
    private Request listservices() {
        JSONObject postdata = new JSONObject();
        try {
            postdata.put("user_id", Prefhelper.getInstance(getActivity()).getUserid());
            postdata.put("message_type", 2);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(JSON, postdata.toString());
        final Request request = new Request.Builder()
                .addHeader("X-Api-Key", "AB5433GMDF657VBB")
                .addHeader("Content-Type", "application/json")
                .url("http://feish.online/apis/patientCommunications")
                .post(body)
                .build();
        return request;
    }

    public void listServiceResponse(String response) {
        Gson gson = new GsonBuilder().create();
        serviceResponse = gson.fromJson(response, ContactServiceForFetchingMsgPatient.class);
    }
    public void  openDialog(final int position){
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = ( LayoutInflater )getActivity().
                getSystemService(getActivity().LAYOUT_INFLATER_SERVICE);
        final View dialogView = inflater.inflate(R.layout.custom_dialog, null);
        dialogBuilder.setView(dialogView);

        final EditText edt = (EditText) dialogView.findViewById(R.id.edit1);

        dialogBuilder.setTitle("Reply here for "+l.get(position).getCommunication().getCreated());
        dialogBuilder.setMessage("");
        dialogBuilder.setPositiveButton("Done", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                //do something with edt.getText().toString();
                if(!edt.getText().toString().equals("")) {
                    apiCall(edt.getText().toString(),position);
                }
            }
        });
        dialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                //pass
            }
        });
        AlertDialog b = dialogBuilder.create();
        b.show();
    }
    private void apiCall(String msg,int position) {
        OkHttpClient client = new OkHttpClient();
        Request validation_request = loginRequest(position,msg);
        client.newCall(validation_request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                Log.i("Activity", "onFailure: Fail");
            }

            @Override
            public void onResponse(final Response response) throws IOException {
                final boolean isSuccessful = loginJSON(response.body().string());


                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (isSuccessful) {
                            Toast.makeText(getActivity(), "Message sent! will respond you shortly!!!", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(getActivity(), "OOPS!!", Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });
    }
    public boolean loginJSON(String response) {
        Gson gson = new GsonBuilder().create();
        contactServiceforMsg = gson.fromJson(response, ContactServiceforMsg.class);
        return contactServiceforMsg.getStatus();
    }


    private Request loginRequest(int position, String msg) {
        JSONObject postdata = new JSONObject();
        try {
            postdata.put("user_id", Prefhelper.getInstance(getActivity()).getUserid());
            postdata.put("subject", l.get(position).getCommunication().getSubject());
            postdata.put("message", msg);
            postdata.put("reciever_user_id", l.get(position).getCommunication().getUserId());

             //      postdata.put("message_type", 2);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(JSON, postdata.toString());
        final Request request = new Request.Builder()
                .addHeader("X-Api-Key", "AB5433GMDF657VBB")
                .addHeader("Content-Type", "application/json")
                .url("http://feish.online/apis/communicateToDoctor")
                .post(body)
                .build();
        return request;
    }
    public class RecyclerViewadt_drmsg extends RecyclerView.Adapter<RecyclerViewadt_drmsg.MyViewHolder>
    {

        private List<Datum_FetchingMsg> dataSet;
        Context context;

        public class MyViewHolder extends RecyclerView.ViewHolder  {

            TextView textView_pname,textView_date;
            TextView  textView_time,textView_msg,tv_reply;
            CardView cardView;
            MyViewHolder(View itemView) {
                super(itemView);
                this.textView_pname =itemView.findViewById(R.id.patient_name);
                this.textView_time =    itemView.findViewById(R.id.msgtime);
                this.textView_date =    itemView.findViewById(R.id.msgdate);
                this.textView_msg =    itemView.findViewById(R.id.mainmsg);
                this.tv_reply =    itemView.findViewById(R.id.txt_reply);
                this.cardView = itemView.findViewById(R.id.cardview);
            }

        }

        public RecyclerViewadt_drmsg(List<Datum_FetchingMsg> data, Context context) {
            this.dataSet = data;
            this.context=context;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent,
                                               int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.doctortopaientmsg, parent, false);

            MyViewHolder myViewHolder = new MyViewHolder(view);
            return myViewHolder;
        }

        @Override
        public void onBindViewHolder(final MyViewHolder holder, final int listPosition) {
            holder.textView_pname.setText(dataSet.get(listPosition).getUser().getFirstName()+" "+dataSet.get(listPosition).getUser().getFirstName());
            holder.textView_time.setText(dataSet.get(listPosition).getCommunication().getCreated());
            holder.textView_date.setText(dataSet.get(listPosition).getCommunication().getSubject());
            holder.textView_msg.setText(dataSet.get(listPosition).getCommunication().getMessage());
            holder.cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    openDialog(listPosition);
                }
            });
        }

        @Override
        public int getItemCount() {
            return dataSet.size();
        }



    }}
