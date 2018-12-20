package com.app.feish.application;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.app.feish.application.Patient.MedicalHitoryp;
import com.app.feish.application.Remote.ApiUtils;
import com.app.feish.application.model.Communication;
import com.app.feish.application.model.ContactServiceForFetchingMsgPatient;
import com.app.feish.application.model.Datum_FetchingMsg;
import com.app.feish.application.model.UserClass_for_showing_patient_name;
import com.app.feish.application.modelclassforapi.ContactServiceforMsg;
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
import de.hdodenhof.circleimageview.CircleImageView;
import static com.app.feish.application.LoginActivity.JSON;

public class ChatActivity extends AppCompatActivity implements View.OnClickListener {


    private RecyclerView recyclerChat;
    String user_id,my_id;
    private ContactServiceForFetchingMsgPatient serviceResponse;
    int msg_type=0;
    public static final int VIEW_TYPE_USER_MESSAGE = 0;
    public static final int VIEW_TYPE_FRIEND_MESSAGE = 1;
    private EditText editWriteMessage;
    List<Datum_FetchingMsg> l;
    Context context=this;
    ListMessageAdapter listMessageAdapter;
    ImageView imageView;
    ContactServiceforMsg contactServiceforMsg;
    TextView textView;
    String name,lastname;
    LinearLayoutManager linearLayoutManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        imageView=findViewById(R.id.img_back);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        textView=findViewById(R.id.nametitle);
         linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerChat = findViewById(R.id.recyclerChat);
        recyclerChat.setLayoutManager(linearLayoutManager);


        user_id=getIntent().getStringExtra("user_id");
        my_id=getIntent().getStringExtra("my_id");
         name = getIntent().getStringExtra("name");
         lastname = getIntent().getStringExtra("lastname");
        textView.setText(name);
        msg_type=getIntent().getIntExtra("msg_type",0);
        ImageButton btnSend = findViewById(R.id.btnSend);
        btnSend.setOnClickListener(this);

        editWriteMessage = findViewById(R.id.editWriteMessage);
        fetchingdata();

    }
    private void fetchingdata()
    {
        OkHttpClient client = new OkHttpClient();
        Request request = listVitalSign();
        Log.i("", "onClick: "+request);
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                Log.i("Activity", "onFailure: Fail");
            }
            @Override
            public void onResponse(final Response response) throws IOException {
                final String body=response.body().string();

                listServiceResponse(body);

                l=serviceResponse.getData();
                final boolean isSuccessful=serviceResponse.getStatus();
                Log.i("1234check", "onResponse: "+body);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        if (isSuccessful) {
                             listMessageAdapter=new ListMessageAdapter(context,l,my_id,user_id,msg_type);
                            recyclerChat.setAdapter(listMessageAdapter);

                        } else {
                            Toast.makeText(ChatActivity.this, "Error", Toast.LENGTH_SHORT).show();
                        }

                    }
                });
            }

        });

    }
    public void listServiceResponse(String response) {
        Gson gson = new GsonBuilder().create();
        serviceResponse = gson.fromJson(response, ContactServiceForFetchingMsgPatient.class);
    }


    private Request listVitalSign()
    {
        JSONObject postdata = new JSONObject();
        try {
            postdata.put("user_id", Integer.parseInt(user_id));
            postdata.put("reciever_user_id", my_id);
            postdata.put("message_type", msg_type);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(JSON, postdata.toString());
        return new Request.Builder()
                .addHeader("X-Api-Key", "AB5433GMDF657VBB")
                .addHeader("Content-Type", "application/json")
                .url(ApiUtils.BASE_URL+"AllCommunicationsMsg")
                .post(body)
                .build();
    }


    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btnSend) {
            String content = editWriteMessage.getText().toString().trim();
            apiCall(content,(l.size()-1));


        }
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
                            Communication  communication= new Communication();
                            UserClass_for_showing_patient_name userClass_for_showing_patient_name= new UserClass_for_showing_patient_name();
                            userClass_for_showing_patient_name.setFirstName(name);
                            userClass_for_showing_patient_name.setLastName(lastname);
                            communication.setMessage(editWriteMessage.getText().toString());
                            communication.setRecieverUserId(user_id);
                            communication.setUserId(my_id);
                            communication.setSubject("");
                            Datum_FetchingMsg datum_fetchingMsg= new Datum_FetchingMsg();
                            datum_fetchingMsg.setCommunication(communication);
                            datum_fetchingMsg.setUser(userClass_for_showing_patient_name);
                            l.add(datum_fetchingMsg);
                            listMessageAdapter.notifyDataSetChanged();
                            linearLayoutManager.scrollToPosition(l.size() - 1);
                            editWriteMessage.setText("");

                        } else {
                            Toast.makeText(context, "OOPS!!", Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });
    }
    private boolean loginJSON(String response) {
        Gson gson = new GsonBuilder().create();
        contactServiceforMsg = gson.fromJson(response, ContactServiceforMsg.class);
        return contactServiceforMsg.getStatus();
    }
    private Request loginRequest(int position, String msg) {
        String url;
        if(msg_type==2)
        {
            url=ApiUtils.BASE_URL+"communicateToDoctor";
        }
        else {
            url=ApiUtils.BASE_URL+"communicateToPatient";

        }

        JSONObject postdata = new JSONObject();
        try {
            postdata.put("user_id", my_id);
            postdata.put("subject", l.get(position).getCommunication().getSubject());
            postdata.put("message", msg);
            postdata.put("reciever_user_id",user_id);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(MedicalHitoryp.JSON, postdata.toString());
        return new Request.Builder()
                .addHeader("X-Api-Key", "AB5433GMDF657VBB")
                .addHeader("Content-Type", "application/json")
                .url(url)
                .post(body)
                .build();
    }
}

class ListMessageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private  List<Datum_FetchingMsg> datum_fetchingMsgs;
    private  String my_id,myfriendid;
  private   int msg_type;

    public ListMessageAdapter(Context context, List<Datum_FetchingMsg> datum_fetchingMsgs,String my_id,String myfriendid,int msg_type) {
        this.context = context;
this.datum_fetchingMsgs=datum_fetchingMsgs;
this.my_id=my_id;
this.myfriendid=myfriendid;
this.msg_type=msg_type;

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == ChatActivity.VIEW_TYPE_FRIEND_MESSAGE) {
            View view = LayoutInflater.from(context).inflate(R.layout.rc_item_message_friend, parent, false);
            return new ItemMessageFriendHolder(view);
        } else if (viewType == ChatActivity.VIEW_TYPE_USER_MESSAGE) {
            View view = LayoutInflater.from(context).inflate(R.layout.rc_item_message_user, parent, false);
            return new ItemMessageUserHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ItemMessageFriendHolder)
        {
          ((ItemMessageFriendHolder) holder).txtContent.setText(datum_fetchingMsgs.get(position).getCommunication().getMessage());
          if(msg_type==2)
              ((ItemMessageFriendHolder) holder).avata.setImageResource(R.drawable.doctor);
        }
        else if (holder instanceof ItemMessageUserHolder)
        {
         ((ItemMessageUserHolder) holder).txtContent.setText(datum_fetchingMsgs.get(position).getCommunication().getMessage());
        }
    }

    @Override
    public int getItemViewType(int position) {

        return datum_fetchingMsgs.get(position).getCommunication().getRecieverUserId().equals(myfriendid) ? ChatActivity.VIEW_TYPE_USER_MESSAGE : ChatActivity.VIEW_TYPE_FRIEND_MESSAGE;
    }

    @Override
    public int getItemCount() {
        return datum_fetchingMsgs.size();
    }

}

class ItemMessageUserHolder extends RecyclerView.ViewHolder {
    public TextView txtContent;
    public CircleImageView avata;

    public ItemMessageUserHolder(View itemView) {
        super(itemView);
        txtContent =itemView.findViewById(R.id.textContentUser);
        avata =itemView.findViewById(R.id.imageView2);
    }
}

class ItemMessageFriendHolder extends RecyclerView.ViewHolder {
    public TextView txtContent;
    public CircleImageView avata;

    public ItemMessageFriendHolder(View itemView) {
        super(itemView);
        txtContent = itemView.findViewById(R.id.textContentFriend);
        avata = itemView.findViewById(R.id.imageView3);
    }

}
