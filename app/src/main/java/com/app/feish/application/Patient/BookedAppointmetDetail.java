package com.app.feish.application.Patient;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.app.feish.application.R;
import com.app.feish.application.Remote.EncryptionDecryption;
import com.app.feish.application.doctor.ViewProfileDoctor;
import com.app.feish.application.model.serviceworkinghours;
import com.app.feish.application.modelclassforapi.Appodatum;
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
import static com.app.feish.application.doctor.AddWorkingHours.JSON;
import static com.app.feish.application.doctor.DoctorDashboard.contactService_getDetails;

public class BookedAppointmetDetail extends AppCompatActivity {
    Toolbar toolbar;
    Context context = this;
    Dialog dialog;
    CardView cardView, cardView_viewprescription;
    Appodatum userdata;
    Appointmentdatum appointmentdatum;
    TextView textView_doc_name, textView_doc_spe, textView_doc_mob, textView_doc_email, textView_aboutdoc, textView_clinicadd, textView_appdate, textView_apptime, textView_docfee;
    int code = 0;
    TextView tv_observation, tv_comment, tv_referencename, tv_referenceadd, tv_referncecomment;
    ArrayList<serviceworkinghours> serviceworkinghourslist = new ArrayList<>();
    LinearLayout linearLayout_llresh;
    TextView tv_dieasename;

    TextView tv_res, tv_appredate, tv_appretime, tv_referto, tv_appoid;
    ImageView imageView_confirm, imageView_booked, imageView_re, imageView_cancel;
    private static final int PERMISSIONS_REQUEST_SEND_PERMISSION = 105;
    AlertDialog alert11;
    RelativeLayout relativeLayout;








    private void initview() {
        cardView_viewprescription = findViewById(R.id.viewprescription);
        textView_doc_name = findViewById(R.id.doc_name);
        relativeLayout = findViewById(R.id.rldone);
        relativeLayout.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP_MR1)
            @Override
            public void onClick(View v) {
                finish();

            }
        });
        textView_doc_spe = findViewById(R.id.doc_spe);
        textView_doc_mob = findViewById(R.id.doc_mob);
        textView_doc_email = findViewById(R.id.doc_email);
        tv_appoid = findViewById(R.id.appoid);
        textView_aboutdoc = findViewById(R.id.aboutdoc);
        textView_clinicadd = findViewById(R.id.clinicadd);
        textView_appdate = findViewById(R.id.appdate);
        textView_apptime = findViewById(R.id.apptime);
        textView_docfee = findViewById(R.id.docfee);
        tv_appredate = findViewById(R.id.appredate);
        tv_appretime = findViewById(R.id.appretime);
        tv_res = findViewById(R.id.res);
        linearLayout_llresh = findViewById(R.id.llresh);

        imageView_confirm = findViewById(R.id.img_confirm);
        imageView_booked = findViewById(R.id.img_booked);
        imageView_re = findViewById(R.id.img_reschudule);
        imageView_cancel = findViewById(R.id.img_cancel);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_booked_appointmet_detail);
        initview();

        cardView_viewprescription.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP_MR1)
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"Helo",Toast.LENGTH_LONG);
                open();
            }
        });
        code = getIntent().getIntExtra("code", 0);
        if (code == 0)
            cardView_viewprescription.setVisibility(View.GONE);

        appointmentdatum = (Appointmentdatum) getIntent().getSerializableExtra("appointmentlist");
        userdata = (Appodatum) getIntent().getSerializableExtra("userdetaillist");
        tv_appoid.setText(appointmentdatum.getAppointment().getId());

        textView_doc_name.setText("Dr " + appointmentdatum.getDoctor().getFirstName() + " " + appointmentdatum.getDoctor().getLastName());
        textView_clinicadd.setText(appointmentdatum.getService().getAddress() + " " + appointmentdatum.getService().getLocality() + " " + appointmentdatum.getService().getCity() + " " + appointmentdatum.getService().getPinCode());
        textView_doc_spe.setText(userdata.getUserDetaildata().getName());
        textView_doc_mob.setText(appointmentdatum.getService().getPhone());
        textView_doc_email.setText(appointmentdatum.getDoctor().getEmail());
        textView_aboutdoc.setText(userdata.getUserDetaildata().get0().getAboutDoctor());
        String datetime[] = appointmentdatum.getAppointment().getAppointedTiming().split(" ");
        textView_appdate.setText(datetime[0]);
        textView_apptime.setText(datetime[1]);
        textView_docfee.setText("Rs " + userdata.getUserDetaildata().get0().getFee() + " /Session");

        if (appointmentdatum.getAppointment().getStatus().equals("1")) {
            imageView_confirm.setImageResource(R.drawable.filledcircle);

        }
        if (appointmentdatum.getAppointment().getStatus().equals("2")) {
            imageView_booked.setImageResource(R.drawable.filledcircle);
            imageView_confirm.setImageResource(R.drawable.filledcircle);
        }
        if (appointmentdatum.getAppointment().getStatus().equals("3")) {
            imageView_re.setImageResource(R.drawable.filledcircle);
            String datetime12[] = appointmentdatum.getAppointment().getScheduledDate().split(" ");
            tv_appredate.setText(datetime12[0]);
            tv_appretime.setText(datetime12[1]);
            linearLayout_llresh.setVisibility(View.VISIBLE);

        }


        if (appointmentdatum.getAppointment().getStatus().equals("4")) {
            imageView_re.setVisibility(View.GONE);
            imageView_booked.setVisibility(View.GONE);
            imageView_confirm.setVisibility(View.GONE);
            imageView_cancel.setImageResource(R.drawable.cancel);
            if (appointmentdatum.getAppointment().getReason() != null)
                tv_res.setText(appointmentdatum.getAppointment().getReason().toString());
        }


        textView_doc_mob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makecall(textView_doc_mob.getText().toString());

            }
        });


    }

    public void makecall(final String number) {

        final AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
        builder1.setMessage("Make Call  " + number + " ?");
        builder1.setCancelable(true);
        builder1.setPositiveButton(
                "call",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        try {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                                requestPermissions(new String[]{android.Manifest.permission.CALL_PHONE}, PERMISSIONS_REQUEST_SEND_PERMISSION);
                                //After this point you wait for callback in onRequestPermissionsResult(int, String[], int[]) overriden method
                            } else {

                                Intent callIntent = new Intent(Intent.ACTION_CALL);
                                callIntent.setData(Uri.parse("tel:" + number));
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
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        if (requestCode == PERMISSIONS_REQUEST_SEND_PERMISSION) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                try {
                    // Permission is granted
                    Intent callIntent = new Intent(Intent.ACTION_CALL);
                    callIntent.setData(Uri.parse("tel:" + textView_doc_mob.getText().toString()));
                    context.startActivity(callIntent);
                } catch (SecurityException e) {
                }
            } else {
                Toast.makeText(BookedAppointmetDetail.this, "Until you grant the permission, we canot display the names", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP_MR1)
    public void open() {
        TextView tv_drname, tv_drspe, tv_drphone;
        TextView tv_aptientname, tv_pagen, tv_add;
        ImageView imageView_imgser;
        GridView gridview;
        fetchsoapnotes();
        dialog = new Dialog(context, android.R.style.Theme_Light);
        if (dialog.getWindow() != null)
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.soapnotetemplate);
        dialog.setCanceledOnTouchOutside(false);
        cardView = dialog.findViewById(R.id.cardView);
        gridview = dialog.findViewById(R.id.grid);
        gridview.setNestedScrollingEnabled(false);
        tv_drname = dialog.findViewById(R.id.drname);
        tv_drspe = dialog.findViewById(R.id.drspe);
        tv_add = dialog.findViewById(R.id.address);
        tv_drphone = dialog.findViewById(R.id.serphone);
        imageView_imgser = dialog.findViewById(R.id.imgser);
        tv_aptientname = dialog.findViewById(R.id.patientname);
        tv_dieasename = dialog.findViewById(R.id.dieasename);

        tv_observation = dialog.findViewById(R.id.observation);
        tv_referto = dialog.findViewById(R.id.referto);
        tv_comment = dialog.findViewById(R.id.Componencommentt);
        tv_referencename = dialog.findViewById(R.id.referencename);
        tv_referenceadd = dialog.findViewById(R.id.referenadd);
        tv_referncecomment = dialog.findViewById(R.id.referencecomment);

        tv_pagen = dialog.findViewById(R.id.patientge);
        tv_drname.setText(" Dr " + appointmentdatum.getDoctor().getFirstName() + " " + appointmentdatum.getDoctor().getLastName());
        tv_drspe.setText(userdata.getUserDetaildata().getName());
        tv_drphone.setText(appointmentdatum.getService().getPhone());

        tv_aptientname.setText(appointmentdatum.getUser().getFirstName() + " " + appointmentdatum.getUser().getLastName());

        tv_add.setText(appointmentdatum.getService().getAddress() + " " + appointmentdatum.getService().getLocality() + " " + appointmentdatum.getService().getCity() + " " + appointmentdatum.getService().getPinCode());
        if (appointmentdatum.getService().getLogo() != null) {
            Picasso.with(context).load("http://feish.online/img/services/" + appointmentdatum.getService().getLogo()).into(imageView_imgser);
        }
        // tv_patiage.setText(appointmentdatum.getUser().get);
        if (appointmentdatum.getUser().getGender().equals("1"))
            tv_pagen.setText("MALE");
        else
            tv_pagen.setText("FEMALE");

        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        RecyclerView listView = dialog.findViewById(R.id.list);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        listView.setHasFixedSize(true);
        listView.setLayoutManager(linearLayoutManager);
        listView.setNestedScrollingEnabled(false);
        Customadt customadt = new Customadt(context);
        if (appointmentdatum.getPrescription().size() > 0) {
            listView.setAdapter(customadt);
            Toast.makeText(context, "" + appointmentdatum.getPrescription().size(), Toast.LENGTH_SHORT).show();
            dialog.show();
        }
        if (serviceworkinghourslist.size() > 0) {

        } else {
            fetchworkinghr(gridview);
        }

    }

    class Customadt extends RecyclerView.Adapter<Customadt.MyViewHolder> {


        Context context;

        public class MyViewHolder extends RecyclerView.ViewHolder {

            TextView tv_medname, tv_unit, tv_tdose, tv_medtime, tv_ingestiontime;
            CardView cardView;

            public MyViewHolder(View rowView) {
                super(rowView);
                this.tv_medname = rowView.findViewById(R.id.medname);
                this.tv_unit = rowView.findViewById(R.id.unitdose);
                this.tv_tdose = rowView.findViewById(R.id.totaldose);
                this.tv_medtime = rowView.findViewById(R.id.medtime);
                this.tv_ingestiontime = rowView.findViewById(R.id.ingestiontime);
            }
        }

        public Customadt(Context context) {
            this.context = context;
        }

        @Override
        public Customadt.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                         int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.prescriptionreport, parent, false);

            return new MyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull final Customadt.MyViewHolder holder, final int position) {

          //  String decryptmedicine = EncryptionDecryption.decode(appointmentdatum.getPrescription().get(position).getMedicineName());
        //    holder.tv_medname.setText(decryptmedicine);

            if (appointmentdatum.getPrescription().get(position).getMedicineType().equals("0")) {
                holder.tv_unit.setText(appointmentdatum.getPrescription().get(position).getUnitQty() + "spoon ");
                holder.tv_tdose.setText(appointmentdatum.getPrescription().get(position).getTotalQty() + " spoon ");
            } else {
                holder.tv_unit.setText(appointmentdatum.getPrescription().get(position).getUnitQty() + "tab ");
                holder.tv_tdose.setText(appointmentdatum.getPrescription().get(position).getTotalQty() + " tab ");
            }

            if (Integer.parseInt(appointmentdatum.getPrescription().get(position).getAfterMeal()) == 1)
                holder.tv_ingestiontime.setText("Before Lunch");
            else
                holder.tv_ingestiontime.setText("After Lunch");

            tv_dieasename.setText(tv_dieasename.getText().toString() + " ," + appointmentdatum.getPrescription().get(position).getDiseasename());

            List<String> l = Arrays.asList(appointmentdatum.getPrescription().get(position).getMedicineTime().split(","));
            String text = "";
            for (int i = 0; i < l.size(); i++) {
                switch (l.get(i)) {
                    case " 1":
                        text = text + ", Morn";
                        break;
                    case " 2":
                        text = text + ", Afternoon";
                        break;
                    case " 3":
                        text = text + ", Evening";
                        break;
                    case " 4":
                        text = text + ", Night";
                        break;
                    default:
                        break;
                }
                holder.tv_medtime.setText(text);
            }

        }

        @Override
        public int getItemCount() {
            return appointmentdatum.getPrescription().size();
        }
    }

    public void fetchworkinghr(final GridView gridView) {
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
                final String body = response.body().string();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONObject jsonObject = new JSONObject(body);
                            if (jsonObject.getInt("Success") == 1) {
                                JSONArray jsonArray = jsonObject.getJSONArray("ServicesWorkingTiming");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                    serviceworkinghourslist.add(new serviceworkinghours(jsonObject1.getString("open_time"), jsonObject1.getString("close_time"), jsonObject1.getInt("id"), jsonObject1.getInt("day_of_week")));
                                }
                                CustomAdapter_grid arrayAdapter = new CustomAdapter_grid(serviceworkinghourslist);
                                gridView.setAdapter(arrayAdapter);
                            } else {
                                Toast.makeText(context, "Error!!", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            Toast.makeText(context, "" + e, Toast.LENGTH_SHORT).show();
                        }

                    }
                });
            }
        });

    }

    private Request listworkinghr() {
        JSONObject postdata = new JSONObject();
        try {
            postdata.put("service_id", Integer.parseInt(appointmentdatum.getService().getId()));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(JSON, postdata.toString());
        return new Request.Builder()
                .addHeader("X-Api-Key", "AB5433GMDF657VBB")
                .addHeader("Content-Type", "application/json")
                .url("http://feish.online/apis/ListWokingHours")
                .post(body)
                .build();
    }

    public void fetchsoapnotes() {
        OkHttpClient client = new OkHttpClient();
        Request validation_request = listsoapnotes();
        client.newCall(validation_request).enqueue(new Callback() {

            @Override
            public void onFailure(Request request, IOException e) {

                // Toast.makeText(getApplicationContext(),"Fail",Toast.LENGTH_LONG).show();
                Log.i("Activity", "onFailure: Fail");
            }

            @Override
            public void onResponse(final Response response) throws IOException {
                final String body = response.body().string();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        try {

                            JSONArray jsonArray = new JSONArray(body);
                            JSONObject jsonObject = jsonArray.getJSONObject(0);
                            //String decryptobservation = EncryptionDecryption.decode(jsonObject.getString("observation"));

                            //tv_observation.setText(decryptobservation);

                            // String decryptcomment = EncryptionDecryption.decode(jsonObject.getString("comments"));
                            // tv_comment.setText(decryptcomment);
                            if (jsonObject.getInt("is_reference") == 1) {
                                //String decryptrefname = EncryptionDecryption.decode(jsonObject.getString("reference_name"));
                                // String decryptrefaddress = EncryptionDecryption.decode(jsonObject.getString("reference_address"));
                                // String decryptrefcomment = EncryptionDecryption.decode(jsonObject.getString("reference_comments"));

                                //tv_referencename.setText(decryptrefname);

                                //tv_referenceadd.setText(decryptrefaddress);

                                //tv_referncecomment.setText(decryptrefcomment);
                                AsyncTaskRunner runner=new AsyncTaskRunner();
                                runner.execute(jsonObject.getString("observation"),jsonObject.getString("comments"),jsonObject.getString("reference_name"),jsonObject.getString("reference_address"),jsonObject.getString("reference_comments"),"1");

                            }
                            else
                            {
                                AsyncTaskRunner runner=new AsyncTaskRunner();
                                runner.execute(jsonObject.getString("observation"),jsonObject.getString("comments"),"","","","0");


                                }
                        } catch (JSONException e) {

                        }

                    }
                });
            }
        });

    }









    private Request listsoapnotes() {
        JSONObject postdata = new JSONObject();
        try {
            postdata.put("id",Integer.parseInt(appointmentdatum.getAppointment().getId()));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(JSON, postdata.toString());
        return new Request.Builder()
                .addHeader("X-Api-Key", "AB5433GMDF657VBB")
                .addHeader("Content-Type", "application/json")
                .url("http://feish.online/apis/fetchsoapnotes")
                .post(body)
                .build();
    }




    class AsyncTaskRunner extends AsyncTask<String, String, String> {


        String decryptobservation;
        String decryptcomment;
        String decryptrefname ="";
        String decryptrefaddress="" ;
        String decryptrefcomment="";

        @Override
        protected String doInBackground(String... params) {
            publishProgress("Loading..."); // Calls onProgressUpdate()

if(Integer.parseInt(params[5])==0)
{
    decryptobservation = EncryptionDecryption.decode(params[0]);
    decryptcomment = EncryptionDecryption.decode(params[1]);
}
else {
    decryptobservation = EncryptionDecryption.decode(params[0]);
    decryptcomment = EncryptionDecryption.decode(params[1]);

    decryptrefname = EncryptionDecryption.decode(params[2]);

    decryptrefaddress = EncryptionDecryption.decode(params[3]);

    decryptrefcomment = EncryptionDecryption.decode(params[4]);
}



            return null;

        }


        @Override
        protected void onPostExecute(String result) {




            tv_observation.setText(decryptobservation);
            tv_comment.setText(decryptcomment);
            tv_referencename.setText(decryptrefname);
            tv_referenceadd.setText(decryptrefaddress);
            tv_referncecomment.setText(decryptrefcomment);



        }


        @Override
        protected void onPreExecute() {

        }


        @Override
        protected void onProgressUpdate(String... text) {

        }
    }















    public class CustomAdapter_grid extends BaseAdapter {
        LayoutInflater inflter;
        List<serviceworkinghours> servicesWorkingTimings;

        CustomAdapter_grid( ArrayList<serviceworkinghours> servicesWorkingTimings)
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



