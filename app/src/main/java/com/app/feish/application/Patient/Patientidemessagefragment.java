package com.app.feish.application.Patient;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.app.feish.application.ChatActivity;
import com.app.feish.application.R;
import com.app.feish.application.Remote.ApiUtils;
import com.app.feish.application.model.ContactServiceForFetchingMsgPatient;
import com.app.feish.application.model.Datum_FetchingMsg;
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
import java.util.List;

import static com.app.feish.application.Patient.PatientDashboard.JSON;

public class Patientidemessagefragment extends Fragment
{
    View rootView;
    private ContactServiceForFetchingMsgPatient serviceResponse;
    RecyclerViewadt_patimsg recyclerViewadt_drmsg;
    List<Datum_FetchingMsg> l;
    RecyclerView recyclerView;
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static   final  String ARG_SECTION_NUMBER = "section_number";

    public Patientidemessagefragment() {
    }

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public  static   Patientidemessagefragment newInstance(int sectionNumber) {
        Patientidemessagefragment fragment = new Patientidemessagefragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.content_doctor_message, container, false);

        recyclerView= rootView.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        if(getArguments().getInt(ARG_SECTION_NUMBER)==1)
        {
            fetchdata(2);
        }
        else
        {
            fetchdata(1);
        }
        return rootView;
    }
    private Request listservices(int msgcode) {
        JSONObject postdata = new JSONObject();
        try {
            postdata.put("user_id", Prefhelper.getInstance(getActivity()).getUserid());
            postdata.put("message_type", msgcode);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(JSON, postdata.toString());
        return new Request.Builder()
                .addHeader("X-Api-Key", "AB5433GMDF657VBB")
                .addHeader("Content-Type", "application/json")
                .url(ApiUtils.BASE_URL+"patientCommunications")
                .post(body)
                .build();
    }
    public void listServiceResponse(String response) {
        Gson gson = new GsonBuilder().create();
        serviceResponse = gson.fromJson(response, ContactServiceForFetchingMsgPatient.class);
    }
    public void fetchdata(int msgcode)
    {
        OkHttpClient client = new OkHttpClient();
        Request validation_request = listservices(msgcode);
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
                final String body=response.body().string();
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (isSuccessful) {

                            recyclerViewadt_drmsg= new RecyclerViewadt_patimsg(l,getActivity());
                            recyclerView.setAdapter(recyclerViewadt_drmsg);
                        } else {
                            Toast.makeText(getActivity(), "Could not load the list!!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }
    class RecyclerViewadt_patimsg extends RecyclerView.Adapter<RecyclerViewadt_patimsg.MyViewHolder>
    {
        ContactServiceforMsg contactServiceforMsg;
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

         RecyclerViewadt_patimsg(List<Datum_FetchingMsg> data, Context context) {
            this.dataSet = data;
            this.context=context;
        }

        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent,
                                               int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.doctortopaientmsg, parent, false);

            return new MyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final MyViewHolder holder, final int listPosition) {
            holder.textView_pname.setText(dataSet.get(listPosition).getUser().getFirstName()+" "+dataSet.get(listPosition).getUser().getFirstName());
            holder.textView_time.setText(dataSet.get(listPosition).getCommunication().getCreated());
            holder.textView_date.setText(dataSet.get(listPosition).getCommunication().getSubject());
            holder.textView_msg.setText(dataSet.get(listPosition).getCommunication().getMessage());
            holder.tv_reply.setVisibility(View.VISIBLE);
            holder.tv_reply.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent= new Intent(context, ChatActivity.class);
                    intent.putExtra("user_id",dataSet.get(listPosition).getCommunication().getUserId());
                    intent.putExtra("my_id",dataSet.get(listPosition).getCommunication().getRecieverUserId());
                    intent.putExtra("name",dataSet.get(listPosition).getUser().getFirstName());
                    intent.putExtra("lastname",dataSet.get(listPosition).getUser().getLastName());
                    intent.putExtra("msg_type",getArguments().getInt(ARG_SECTION_NUMBER)+1);
                    context.startActivity(intent);
                   // openDialog(holder.getAdapterPosition());
                }
            });
        }

        @Override
        public int getItemCount() {
            return dataSet.size();
        }

    }

}