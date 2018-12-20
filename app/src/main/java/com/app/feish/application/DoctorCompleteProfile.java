package com.app.feish.application;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;
import com.app.feish.application.Patient.BookNewAppointment;
import com.app.feish.application.Remote.ApiUtils;
import com.app.feish.application.Remote.EncryptionDecryption;
import com.app.feish.application.doctor.AddWorkingHours;
import com.app.feish.application.doctor.Drreviewrating;
import com.app.feish.application.modelclassforapi.CompleterDocProfile;
import com.app.feish.application.modelclassforapi.ContactService_getDetails;
import com.app.feish.application.modelclassforapi.DoctorDatum;
import com.app.feish.application.modelclassforapi.ServicesWorkingTiming;
import com.app.feish.application.sessiondata.Prefhelper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
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
import de.hdodenhof.circleimageview.CircleImageView;
import me.zhanghai.android.materialratingbar.MaterialRatingBar;

import static com.app.feish.application.LoginActivity.JSON;

public class
DoctorCompleteProfile extends AppCompatActivity {
    Context context=this;
    CustomAdapter customAdapter;
    ContactService_getDetails contactService_getDetails;
    GridView gridView;
    CardView cardView_g,cardView_reviewrat;
    ImageView imageView;
    ArrayList<String> photolist= new ArrayList<>();
    DoctorDatum doctorData;
   // Handler handler;
    private static final int PERMISSIONS_REQUEST_SEND_PERMISSION = 105;
    CircleImageView imageView_drpic;
    CompleterDocProfile completerDocProfile;
    int patientcode=0;
    Dialog  dia_rating;
    TextView txtfee;
    ArrayList<Integer> arrayList;
    int userid=0;
    AlertDialog alert11;
    EditText  editText;
    TextView tv_drname,tv_drspeciality,dr_email,dr_phone,dr_add,tv_aboutdoc;
    Integer image[]={R.drawable.doctor_images,R.drawable.doctor,R.drawable.pateint,R.drawable.assistant,R.drawable.doctor_images,R.drawable.doctor_images};
    Connectiondetector connectiondetector;
    RecyclerView recyclerView;

    private void initView()
    {
        tv_drname=findViewById(R.id.drname);
        tv_drspeciality=findViewById(R.id.drspeciality);
        dr_email=findViewById(R.id.dremail);
        dr_phone=findViewById(R.id.drmob);
        dr_phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makecall(dr_phone.getText().toString());
            }
        });
        txtfee=findViewById(R.id.txtfee);
        dr_add=findViewById(R.id.dradd);
        editText=findViewById(R.id.et);
        tv_aboutdoc=findViewById(R.id.aboutdoc);
        imageView_drpic=findViewById(R.id.profilepic);
        connectiondetector= new Connectiondetector(context);
        patientcode=getIntent().getIntExtra("patientcode",0);
        if(patientcode==1)
        doctorData= (DoctorDatum) getIntent().getSerializableExtra("drinfo");
        fetchservicepic();
        recyclerView=findViewById(R.id.recycler_view);
        LinearLayoutManager linearLayoutManager= new LinearLayoutManager(context);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setNestedScrollingEnabled(false);

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_complete_profile);
        initView();

        if(connectiondetector.isConnectingToInternet()) {
            if (patientcode == 1)
            {
                userid=Integer.parseInt(doctorData.getService().getUserId());
               // tv_drname.setText("Dr. "+doctorData.getUser().getFirstName()+" "+doctorData.getUser().getLastName());
              //  dr_email.setText(doctorData.getUser().getEmail());
              //  dr_phone.setText(doctorData.getUser().getMobile());
              //  dr_add.setText(doctorData.getUser().getAddress());

                if(doctorData.getUser().getAvatar()!=null)
                {
                    Picasso.with(DoctorCompleteProfile.this)

                            .load("http://feish.online/img/user_avtar/"+doctorData.getUser().getAvatar())
                            .into(imageView_drpic);
                }
                else
                    imageView_drpic.setImageResource(R.drawable.doctor);

            }
            else
                {
                userid=Integer.parseInt( Prefhelper.getInstance(context).getUserid());
                }
            fetchdata();
            addingdata();
        }
        else
            Toast.makeText(context, "No Internet!!", Toast.LENGTH_SHORT).show();

        customAdapter= new CustomAdapter() ;
        gridView=findViewById(R.id.grid);
        imageView=findViewById(R.id.img);
        cardView_g=findViewById(R.id.cardView_g);
        cardView_reviewrat=findViewById(R.id.reviewrat);

        if(patientcode!=1)
            cardView_reviewrat.setVisibility(View.GONE);

        cardView_reviewrat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(DoctorCompleteProfile.this,Drreviewrating.class);
                intent.putExtra("doc_id", Integer.parseInt(doctorData.getService().getUserId()));
                startActivity(intent);
            }
        });
        cardView_g.setOnClickListener(new View.OnClickListener() {
                                          @Override
                                          public void onClick(View v) {
                                              if(photolist.size()>0) {
                                                  Intent intent = new Intent(DoctorCompleteProfile.this, ShowImage.class);
                                                  Bundle args = new Bundle();
                                                  args.putSerializable("imagelist", photolist);
                                                  intent.putExtra("BUNDLE", args);
                                                  intent.putExtra("code", 2);
                                                  startActivity(intent);
                                              }
                                              else
                                              {
                                                  Toast.makeText(context, "No Photo Available!!", Toast.LENGTH_SHORT).show();
                                              }
                                          }
                                      });
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
         gridView.setAdapter(customAdapter);
        // gridView.setNestedScrollingEnabled(false);
      arrayList = new ArrayList<>(Arrays.asList(image));


    }
    private void fetchdata()
    {
        OkHttpClient client = new OkHttpClient();
        Request request = drprofile_request();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                Log.i("Activity", "onFailure: Fail");
            }
            @Override
            public void onResponse(final Response response) throws IOException {

                String body=response.body().string();
                drprofileJSON(body);
                final String message = contactService_getDetails.getMessage();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //pb.setVisibility(View.GONE);
                        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                        if (message.compareTo("success") == 0)
                        {
                            createthread();
                            tv_drname.setText(String.format("Dr. %s %s", contactService_getDetails.getData().getFirstName(), contactService_getDetails.getData().getLastName()));
                            dr_email.setText(contactService_getDetails.getData().getIs_active().toString());

                            if(contactService_getDetails.getData().getAddress()!=null)
                            if(contactService_getDetails.getData().getAddress()!=null)
                            dr_add.setText(contactService_getDetails.getData().getAddress().toString());

                            if(contactService_getDetails.getData().getAvatar()!=null)
                            {
                                Picasso.with(DoctorCompleteProfile.this)
                                        .load(contactService_getDetails.getData().getAvatar().toString())
                                        .into(imageView_drpic);
                            }
                            else
                                imageView_drpic.setImageResource(R.drawable.doctor);

                            if(contactService_getDetails.getData().getAboutDoctor()!=null)
                            tv_aboutdoc.setText(contactService_getDetails.getData().getAboutDoctor().toString());


                        }else {
                            Toast.makeText(getApplicationContext(), "Fail", Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }

        });

    }
    public void drprofileJSON(String response) {
        Gson gson = new GsonBuilder().create();
        contactService_getDetails = gson.fromJson(response, ContactService_getDetails.class);
    }
    private Request drprofile_request() {
        JSONObject postdata = new JSONObject();
        try {
            postdata.put("user_id",userid);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(JSON, postdata.toString());
        return new Request.Builder()
                .addHeader("X-Api-Key", "AB5433GMDF657VBB")
                .addHeader("Content-Type", "application/json")
                .url("http://feish.online/apis/getPatientdetails")
                .post(body)
                .build();
    }
    private Request family_his() {
        JSONObject postdata = new JSONObject();
        try {
            postdata.put("user_id", userid);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(JSON, postdata.toString());
        return new Request.Builder()
                .addHeader("X-Api-Key", "AB5433GMDF657VBB")
                .addHeader("Content-Type", "application/json")
                .url(ApiUtils.BASE_URL+"completedrprofilefetch")
                .post(body)
                .build();

    }
    private void addingdata()
    {
        OkHttpClient client = new OkHttpClient();
        Request request = family_his();
        Log.i("", "onClick: "+request);
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                Log.i("Activity", "onFailure: Fail");
            }
            @Override
            public void onResponse(final Response response) throws IOException {

                final String body=response.body().string();
                DocprofileJSON(body);
                Log.i("1234add", "onResponse: "+body);
             runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                 editText.setVisibility(View.GONE);
                 if(completerDocProfile.getSuccess()==1) {
                     tv_drspeciality.setText(completerDocProfile.getRecord().getName());
                     txtfee.setText(String.format("Rs %s / Session", completerDocProfile.getRecord().getData().get(0).getFee()));
                     ListServiceAdt listServiceAdt= new ListServiceAdt(context,patientcode);
                     recyclerView.setAdapter(listServiceAdt);


                 }

                    }
                });
            }

        });


    }
    public void DocprofileJSON(String response) {
        Gson gson = new GsonBuilder().create();
        completerDocProfile= gson.fromJson(response,CompleterDocProfile.class);
    }
    public class CustomAdapter extends BaseAdapter {
        LayoutInflater inflter;

        private CustomAdapter()
        {
            inflter = (LayoutInflater.from(context));
        }
        @Override
        public int getCount() {
            return image.length;
        }
        @Override
        public Object getItem(int i) {
            return null;
        }
        @Override
        public long getItemId(int i) {
            return 0;
        }
        @SuppressLint({"ViewHolder", "InflateParams"})
        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            view = inflter.inflate(R.layout.imageviewlayout, null); // inflate the layout
            ImageView icon =  view.findViewById(R.id.img); // get the reference of ImageView
            icon.setImageResource(image[i]); // set logo images
            return view;
        }
    }
    public class ListServiceAdt extends RecyclerView.Adapter<ListServiceAdt.MyViewHolder> {

        Context context;
        int code;
        float userrating,avgrating;

        public  class MyViewHolder extends RecyclerView.ViewHolder {

            TextView textView_sername,textView_serloc,txt_txt_bookapp1;
            MaterialRatingBar materialRatingBar;
            GridView  gridView_sdate;
            CardView cardView;
            public MyViewHolder(View itemView) {
                super(itemView);
                this.textView_sername =  itemView.findViewById(R.id.sername);
                this.txt_txt_bookapp1 =  itemView.findViewById(R.id.txt_bookapp1);
                this.textView_serloc =  itemView.findViewById(R.id.serloc);
                this.materialRatingBar = itemView.findViewById(R.id.ratingbar);
                this.gridView_sdate=itemView.findViewById(R.id.gridsdate);
                this.cardView =  itemView.findViewById(R.id.card_view);


            }
        }

        private ListServiceAdt(Context context,int code) {
            this.context=context;
            this.code=code;
        }

        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent,
                                               int viewType) {

            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.servicelist, parent, false);

            return new MyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull final MyViewHolder holder, int listPosition) {
            if(code==1)
                holder.txt_txt_bookapp1.setVisibility(View.VISIBLE);
            holder.textView_sername.setText(completerDocProfile.getRecord().getMaindata().get(listPosition).getService().getTitle());
            String add=completerDocProfile.getRecord().getMaindata().get(listPosition).getService().getAddress()+" "+completerDocProfile.getRecord().getMaindata().get(listPosition).getService().getCity()+" "+completerDocProfile.getRecord().getMaindata().get(listPosition).getService().getLocality();
            holder.textView_serloc.setText(add);
            holder.materialRatingBar.setRating(Float.parseFloat(completerDocProfile.getRecord().getMaindata().get(listPosition).getService().getAvg_rating()));
            CustomAdapter_grid arrayAdapter= new CustomAdapter_grid(completerDocProfile.getRecord().getMaindata().get(listPosition).getServicesWorkingTiming());
            holder.gridView_sdate.setAdapter(arrayAdapter);
            holder.materialRatingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
                @Override
                public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                    if(code==1)
                    servicerating(holder.getAdapterPosition(),holder.materialRatingBar);
                }
            });
            holder.txt_txt_bookapp1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v)
                {
                    Intent intent= new Intent(context, BookNewAppointment.class);
                    intent.putExtra("doc_id",doctorData.getUser().getId());
                    intent.putExtra("doc_name",doctorData.getUser().getFirstName()+" "+doctorData.getUser().getLastName());
                    intent.putExtra("fulldetail",  completerDocProfile.getRecord().getMaindata().get(holder.getAdapterPosition()));
                    startActivity(intent);

                }
            });
        }
        private  void servicerating(final int pos, final MaterialRatingBar materialRatingBar)
        {
            dia_rating = new Dialog(context);
            if(dia_rating.getWindow()!=null)
            dia_rating.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            dia_rating.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dia_rating.setContentView(R.layout.firstdialog);
            dia_rating.setCanceledOnTouchOutside(false);
          MaterialRatingBar  rb =  dia_rating.findViewById(R.id.ratingBar);

            // rb.setNumStars(5);
            rb.setRating(0);/*Float.parseFloat(userrating)*/
         Button   btn_send =  dia_rating.findViewById(R.id.sendcomment);
            btn_send.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    avgrating=Float.parseFloat(completerDocProfile.getRecord().getMaindata().get(pos).getService().getAvg_rating());
                    if(avgrating==0.00)
                        avgrating=userrating;
                    else
                        avgrating=(userrating+avgrating)/2;

                    addingserating(userrating,avgrating,completerDocProfile.getRecord().getMaindata().get(pos).getService().getId(),materialRatingBar);



                }
            });
            rb.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
                @Override
                public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                   userrating = v;

                }
            });

            dia_rating.show();
        }
        private Request serrating(float prev_rating,float avg_rating,String id) {
            JSONObject postdata = new JSONObject();
            try {
                postdata.put("prev_rating", prev_rating);
                postdata.put("avg_rating",avg_rating);
                postdata.put("id",Integer.parseInt(id));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            RequestBody body = RequestBody.create(JSON, postdata.toString());
            return new Request.Builder()
                    .addHeader("X-Api-Key", "AB5433GMDF657VBB")
                    .addHeader("Content-Type", "application/json")
                    .url(ApiUtils.BASE_URL+"addservicerating")
                    .post(body)
                    .build();

        }
        private void addingserating(float prev_rating, final float avg_rating, String id, final MaterialRatingBar materialRatingBar)
        {
            OkHttpClient client = new OkHttpClient();
            Request request = serrating(prev_rating,avg_rating,id);
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
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                JSONObject jsonObject = new JSONObject(body);
                                if (jsonObject.getInt("Success") == 1) {
                                    Toast.makeText(context, "" + jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                                    dia_rating.dismiss();
                                    materialRatingBar.setRating(avg_rating);
                                } else {
                                    Toast.makeText(context, "" + jsonObject.getString("message"), Toast.LENGTH_SHORT).show();

                                    dia_rating.dismiss();
                                }
                            }
                            catch (JSONException e)
                            {
                                Toast.makeText(context, ""+e, Toast.LENGTH_SHORT).show();
                            }


                        }
                    });
                }

            });


        }

        @Override
        public int getItemCount() {
            return completerDocProfile.getRecord().getMaindata().size();
        }

        public class CustomAdapter_grid extends BaseAdapter {
            LayoutInflater inflter;
            List<ServicesWorkingTiming> servicesWorkingTimings;

             CustomAdapter_grid( List<ServicesWorkingTiming> servicesWorkingTimings)
            {
                this.servicesWorkingTimings=servicesWorkingTimings;
                inflter = (LayoutInflater.from(context));
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
            @SuppressLint({"ViewHolder", "InflateParams"})
            @Override
            public View getView(int i, View view, ViewGroup viewGroup) {
                view = inflter.inflate(R.layout.serviceworktime, null); // inflate the layout
                TextView day =  view.findViewById(R.id.day); // get the reference of ImageView
                TextView sdate =  view.findViewById(R.id.opentime); // get the reference of ImageView
                TextView edate = view.findViewById(R.id.closetime); // get the reference of ImageView
                switch (servicesWorkingTimings.get(i).getServicesWorkingTiming().getDayOfWeek()){
                      case "0":
                          day.setText("Mon");
                        break;
                        case "1":
                            day.setText("Tue");
                        break;
                        case "2":
                            day.setText("Wed");
                        break;
                        case "3":
                            day.setText("Thus");
                        break;
                        case "4":
                            day.setText("Fri");
                        break;
                        case "5":
                            day.setText("Sat");
                        break;
                        case "6":
                            day.setText("Sun");
                        break;

                }

             sdate.setText(servicesWorkingTimings.get(i).getServicesWorkingTiming().getOpenTime());
                edate.setText(servicesWorkingTimings.get(i).getServicesWorkingTiming().getCloseTime());

                return view;
            }
        }
    }
    public void makecall(final String phonenumber) {
        final AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
            builder1.setMessage("Make Call  " + phonenumber + " ?");
        builder1.setCancelable(true);
        builder1.setPositiveButton(
                "call",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        try {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                                requestPermissions(new String[]{android.Manifest.permission.CALL_PHONE}, PERMISSIONS_REQUEST_SEND_PERMISSION);
                                //After this point you wait for callback in onRequestPermissionsResult(int, String[], int[]) overriden method
                            }
                            else
                            {

                                Intent callIntent = new Intent(Intent.ACTION_CALL);
                                callIntent.setData(Uri.parse("tel:" + phonenumber));
                                startActivity(callIntent);

                            }


                        } catch (SecurityException e) {
                            Toast.makeText(context, "" + e, Toast.LENGTH_SHORT).show();
                        }

                        alert11.dismiss();
                    }
                });

        builder1.setNegativeButton(
                "cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        alert11.dismiss();
                    }
                });

        alert11 = builder1.create();
        alert11.show();
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == PERMISSIONS_REQUEST_SEND_PERMISSION) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                try {
                    // Permission is granted
                    Intent callIntent = new Intent(Intent.ACTION_CALL);
                    callIntent.setData(Uri.parse("tel:" + "8177002073"));
                    context.startActivity(callIntent);
                } catch (SecurityException e) {
                    Toast.makeText(context, ""+e, Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(DoctorCompleteProfile.this, "Until you grant the permission, we canot display the names", Toast.LENGTH_SHORT).show();
            }
        }
    }
    private void fetchservicepic()
    {
        OkHttpClient client = new OkHttpClient();
        Request request = AddVitalSignApp();
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

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                    //    Toast.makeText(context, ""+body, Toast.LENGTH_SHORT).show();
                        try {
                            JSONArray jsonArray= new JSONArray(body);
                            for (int i = 0; i <jsonArray.length() ; i++) {
                                JSONObject jsonObject=jsonArray.getJSONObject(i);
                                photolist.add(jsonObject.optString("url"));
                                }

                        } catch (Exception e) {
                            Toast.makeText(context, "" + e, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }

        });
    }
    private Request AddVitalSignApp()
    {

        JSONObject postdata = new JSONObject();
        try {
            if(patientcode==1)
            postdata.put("doctor_id",doctorData.getUser().getId());
            else
            postdata.put("doctor_id",Prefhelper.getInstance(context).getUserid());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(AddWorkingHours.JSON, postdata.toString());
        return new Request.Builder()
                .addHeader("X-Api-Key", "AB5433GMDF657VBB")
                .addHeader("Content-Type", "application/json")
                .url(ApiUtils.BASE_URL+"fetchservicephotofordoc")
                .post(body)
                .build();
    }
    private void createthread()
    {
        Thread background = new Thread(new Runnable() {

            // After call for background.start this run method call
            public void run() {
                try {
                    String decryptphone= EncryptionDecryption.decode(contactService_getDetails.getData().getMobile());


                    threadMsg(decryptphone);

                } catch (Throwable t) {
// just end the background thread
                    Log.i("Animation", "Thread exception " + t);
                }
            }

            private void threadMsg(String msg) {

                if (!msg.equals(null) && !msg.equals("")) {
                    Message msgObj = handler.obtainMessage();
                    Bundle b = new Bundle();
                    b.putString("message", msg);
                    msgObj.setData(b);
                    handler.sendMessage(msgObj);
                }
            }

            // Define the Handler that receives messages from the thread and update the progress
            @SuppressLint("HandlerLeak")
            Handler  handler = new Handler()
            {

                public void handleMessage(Message msg) {

                    String aResponse = msg.getData().getString("message");

                    dr_phone.setText(aResponse);

                }
            };

        });
// Start Thread
        background.start();
    }


}
