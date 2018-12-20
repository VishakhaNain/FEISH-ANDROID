package com.app.feish.application.doctor;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.app.feish.application.R;
import com.app.feish.application.model.ContactServiceForFetchingMsgPatient;
import com.app.feish.application.model.Datum_FetchingMsg;
import com.app.feish.application.modelclassforapi.ContactServiceforMsg;
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

public class DoctorMessage extends AppCompatActivity  {
    Toolbar toolbar;
    public static final MediaType JSON = MediaType.parse("application/json:charset=utf-8");
    LinearLayoutManager layoutManager;
    ContactServiceforMsg contactServiceforMsg;
    RecyclerView recyclerView;
    private ContactServiceForFetchingMsgPatient serviceResponse;
    RecyclerViewadt_drmsg recyclerViewadt_drmsg;
    Context context=this;
    String user_id;
    List<Datum_FetchingMsg> l;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_message);
        initViews();

    }
    private void initViews()
    {
        user_id=getIntent().getStringExtra("user_id");
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setTitle("Message");
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        recyclerView= findViewById(R.id.recycler_view);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setNestedScrollingEnabled(false);
        fetchdata();
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
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (isSuccessful) {

                            Toast.makeText(getApplication(), "List of Message ", Toast.LENGTH_SHORT).show();
                            recyclerViewadt_drmsg= new RecyclerViewadt_drmsg(l,DoctorMessage.this);
                            recyclerView.setAdapter(recyclerViewadt_drmsg);
                        } else {
                            Toast.makeText(getApplication(), "Could not load the list!!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }
    private Request listservices() {
        JSONObject postdata = new JSONObject();
        try {
            postdata.put("user_id",user_id);
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
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
        LayoutInflater inflater = ( LayoutInflater )context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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


                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (isSuccessful) {
                            Toast.makeText(context, "Message sent! will respond you shortly!!!", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(context, "OOPS!!", Toast.LENGTH_LONG).show();
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
            postdata.put("user_id",user_id);
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



    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater=getMenuInflater();
        menuInflater.inflate(R.menu.menusearch,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId()==R.id.search)
        {

        }
        return true;
    }



}
