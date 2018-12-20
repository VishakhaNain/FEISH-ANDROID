package com.app.feish.application.Adpter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.app.feish.application.Assistant.PatientEntry;
import com.app.feish.application.R;
import com.app.feish.application.Remote.ApiUtils;
import com.app.feish.application.doctor.CompletePtientdetail;
import com.app.feish.application.doctor.PreviousPatientdetail;
import com.app.feish.application.model.encountersmodel;
import com.app.feish.application.modelclassforapi.DoctorEncounters;
import com.app.feish.application.sessiondata.Prefhelper;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;
import com.squareup.timessquare.CalendarCellDecorator;
import com.squareup.timessquare.CalendarPickerView;
import com.squareup.timessquare.DefaultDayViewAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import static com.app.feish.application.Patient.MedicalHitoryp.JSON;

public class CustomAdapter_encounteres extends RecyclerView.Adapter<CustomAdapter_encounteres.MyViewHolder> {

     private List<encountersmodel> dataSet;
     Context context;
     private   DoctorEncounters doctorEncounters;
     ProgressDialog  progressDialog;
     AlertDialog alertDialog;
    ArrayList<String> tokenid= new ArrayList<>();
     private int code=0,assicode;
     String newdate="",newtime="",mainreschduletime="";
     Button btn_s,btn_time;
     com.takisoft.datetimepicker.widget.TimePicker timePicker;
     CalendarPickerView calendar_s;
     SimpleDateFormat DATE_FORMAT,DATE_FORMAT_HH;

     static class MyViewHolder extends RecyclerView.ViewHolder
     {

        LinearLayout  linearLayout,linearLayout_llaction;
        TextView tv_appoid,tv_patientid,tv_patientname,tv_servicename,tv_appotime,tv_book,tv_resche,tv_confirm,tv_cancel;
        CardView cardView;
         TextView tv_donorbgrpp;
         MyViewHolder(View itemView) {
            super(itemView);
            cardView=itemView.findViewById(R.id.card_view);
            linearLayout=itemView.findViewById(R.id.ll);
            linearLayout_llaction=itemView.findViewById(R.id.llaction);
            tv_appoid=itemView.findViewById(R.id.appoid);
             tv_patientid=itemView.findViewById(R.id.patientid);
             tv_patientname=itemView.findViewById(R.id.patientname);
             tv_servicename=itemView.findViewById(R.id.servicename);
             tv_appotime=itemView.findViewById(R.id.appotime);
             tv_book=itemView.findViewById(R.id.appobooked);
             tv_resche=itemView.findViewById(R.id.apporesch);
             tv_confirm=itemView.findViewById(R.id.appoconfirm);
             tv_donorbgrpp=itemView.findViewById(R.id.donorbgrpp);
             tv_cancel=itemView.findViewById(R.id.appocancel);


        }
    }
    public CustomAdapter_encounteres(List<encountersmodel> data, DoctorEncounters doctorEncounters, Context context,int code,int assicode) {
        this.dataSet = data;
        this.context=context;
        this.doctorEncounters=doctorEncounters;
        this.code=code;
        this.assicode=assicode;
        DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
        DATE_FORMAT_HH = new SimpleDateFormat("dd");
        progressDialog= new ProgressDialog(context);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setTitle("Please Wait");
    }

    @NonNull
    @Override
    public CustomAdapter_encounteres.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent,
                                                                     int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_encounters, parent, false);

        return new MyViewHolder(view);
    }
    private void setTextViewDrawableColor(TextView textView, int color) {
        for (Drawable drawable : textView.getCompoundDrawables()) {
            if (drawable != null) {
                drawable.setColorFilter(new PorterDuffColorFilter(color, PorterDuff.Mode.LIGHTEN));
            }
        }
    }

    @Override
    public void onBindViewHolder(@NonNull final CustomAdapter_encounteres.MyViewHolder holder, final int listPosition) {
    if(doctorEncounters!=null)
    {
        holder.tv_appoid.setText("Appointment ID : "+doctorEncounters.getAppointmentdata().get(listPosition).getAppointment().getId());
        holder.tv_appotime.setText(doctorEncounters.getAppointmentdata().get(listPosition).getAppointment().getAppointedTiming());
        holder.tv_patientid.setText("Patient ID : "+doctorEncounters.getAppointmentdata().get(listPosition).getUser().getId());
        holder.tv_patientname.setText(doctorEncounters.getAppointmentdata().get(listPosition).getUser().getFirstName()+" "+doctorEncounters.getAppointmentdata().get(listPosition).getUser().getLastName());
        holder.tv_servicename.setText("Service Name :"+doctorEncounters.getAppointmentdata().get(listPosition).getService().getTitle());
        holder.tv_donorbgrpp.setText("schedule date: "+doctorEncounters.getAppointmentdata().get(listPosition).getAppointment().getScheduledDate());

        if(doctorEncounters.getAppointmentdata().get(listPosition).getAppointment().getStatus().equals("1"))
        {
          holder.tv_confirm.setTextColor(ContextCompat.getColor(context,R.color.holo_green));
            setTextViewDrawableColor(holder.tv_confirm,R.color.holo_green);

        }
        if(doctorEncounters.getAppointmentdata().get(listPosition).getAppointment().getStatus().equals("2"))
        {

            holder.tv_book.setTextColor(ContextCompat.getColor(context,R.color.pink));
            setTextViewDrawableColor(holder.tv_book,R.color.pink);
        }
        if(doctorEncounters.getAppointmentdata().get(listPosition).getAppointment().getStatus().equals("3"))
        {
            holder.tv_resche.setTextColor(ContextCompat.getColor(context,R.color.colorAccent2));
            setTextViewDrawableColor(holder.tv_resche,R.color.colorAccent2);

            holder.tv_confirm.setTextColor(ContextCompat.getColor(context,R.color.holo_green));
            setTextViewDrawableColor(holder.tv_confirm,R.color.holo_green);

        }
         if(doctorEncounters.getAppointmentdata().get(listPosition).getAppointment().getStatus().equals("4"))
        {
            holder.tv_book.setVisibility(View.GONE);
            holder.tv_resche.setVisibility(View.GONE);
            holder.tv_confirm.setVisibility(View.GONE);
            holder.tv_cancel.setTextColor(ContextCompat.getColor(context,R.color.red));
            setTextViewDrawableColor(holder.tv_cancel,R.color.red);
            }

            holder.tv_confirm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                 if(holder.tv_confirm.getCurrentTextColor() != context.getResources().getColor( R.color.holo_green))
                 {

                     updatestatus("Confirm this appointment....",listPosition,1,holder.tv_confirm,R.color.holo_green);
                 }
                 else
                 {
                     Toast.makeText(context, "Already confirm", Toast.LENGTH_SHORT).show();
                 }
                }
            });
        holder.tv_book.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(holder.tv_book.getCurrentTextColor() != context.getResources().getColor( R.color.pink))
                {

                    updatestatus("Booked this appointment....",listPosition,2,holder.tv_book,R.color.pink);
                }
                else
                {
                    Toast.makeText(context, "Already Booked", Toast.LENGTH_SHORT).show();
                }
            }
        });
        holder.tv_resche.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(holder.tv_resche.getCurrentTextColor() != context.getResources().getColor( R.color.colorAccent2))
                {
                    updatestatus("Reschedule this appointment....",listPosition,3,holder.tv_resche, R.color.colorAccent2);

                }
                else
                {
                    Toast.makeText(context, "Already Reschedule", Toast.LENGTH_SHORT).show();
                }
            }
        });
        holder.tv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(holder.tv_cancel.getCurrentTextColor() != context.getResources().getColor( R.color.red))
                {

                    updatestatus("Cancel this appointment....",listPosition,4,holder.tv_cancel,R.color.red);

                }
                else
                {
                    Toast.makeText(context, "Already Cancel", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


        if(code==1)
        {
                holder.linearLayout_llaction.setVisibility(View.VISIBLE);
        }
        else
        {
            holder.linearLayout_llaction.setVisibility(View.GONE);
        }

       holder.linearLayout.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v)
    {
        /////////////// New Appointment///////////////////

        if(code==1)
        {
            String userid;

               userid =doctorEncounters.getAppointmentdata().get(listPosition).getUser().getId();

            Intent  intent= new Intent(context, PreviousPatientdetail.class);
            intent.putExtra("code",code);
            intent.putExtra("userid",userid);
            intent.putExtra("data",doctorEncounters.getAppointmentdata().get(listPosition));
            context.startActivity(intent);

        }
        ///////////////Booked Appointment///////////////////
        else if(code==0 && doctorEncounters!=null)
        {
            if(assicode==0) {
                Intent intent = new Intent(context, CompletePtientdetail.class);
                intent.putExtra("code", code);
                intent.putExtra("data", doctorEncounters.getAppointmentdata().get(listPosition));
                context.startActivity(intent);
            }
            else
            {
                Intent intent = new Intent(context, PatientEntry.class);
                intent.putExtra("assiscode",assicode);
                intent.putExtra("appoid",doctorEncounters.getAppointmentdata().get(listPosition).getAppointment().getId());
                context.startActivity(intent);
            }
        }
        ///////////////old Patient///////////////////
        else if(code==2)
        {
            String userid;
                userid =doctorEncounters.getAppointmentdata().get(listPosition).getUser().getId();
            Intent  intent= new Intent(context, PreviousPatientdetail.class);
            intent.putExtra("code",code);
            intent.putExtra("userid",userid);
            intent.putExtra("data",doctorEncounters.getAppointmentdata().get(listPosition));
            context.startActivity(intent);
        }

           // context.startActivity(new Intent(context, PreviousPatientdetail.class));

    }
});
    }

    @Override
    public int getItemCount() {
         if(doctorEncounters!=null)
        return doctorEncounters.getAppointmentdata().size();
         else
           return   0;
    }
    private Request family_his(int pos,int stausvalue) {
        JSONObject postdata = new JSONObject();
        try
        {
            postdata.put("id",doctorEncounters.getAppointmentdata().get(pos).getAppointment().getId());
            postdata.put("appointed_timing", doctorEncounters.getAppointmentdata().get(pos).getAppointment().getAppointedTiming());
            postdata.put("user_id",doctorEncounters.getAppointmentdata().get(pos).getUser().getId());
            postdata.put("doctor_id",Integer.parseInt(doctorEncounters.getAppointmentdata().get(pos).getDoctor().getId()));
            postdata.put("status_updated_by",Integer.parseInt(Prefhelper.getInstance(context).getUserid()));
            postdata.put("service_id",doctorEncounters.getAppointmentdata().get(pos).getService().getId());
            if(!mainreschduletime.equals(""))
            postdata.put("scheduled_date",mainreschduletime);
            else
                postdata.put("scheduled_date",String.valueOf(new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(new java.util.Date())));
            postdata.put("status",String.valueOf(stausvalue));

        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(JSON, postdata.toString());
        return new Request.Builder()
                .addHeader("X-Api-Key", "AB5433GMDF657VBB")
                .addHeader("Content-Type", "application/json")
                .url(ApiUtils.BASE_URL+"UpdateBookAppointment")
                .post(body)
                .build();

    }
    private void updatestatus(String title, final int pos, final int statusvalue, final TextView  textView, final int color)
    {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        alertDialogBuilder.setMessage(title);
                alertDialogBuilder.setPositiveButton("yes",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {
                                if(statusvalue==3)
                                    pickdateandtime(pos,statusvalue,textView,color);
                                    else
                                addingdata(pos,statusvalue,textView,color);
                            }
                        });

        alertDialogBuilder.setNegativeButton("No",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }
    private void pickdateandtime(final int pos, final int statusvalue, final TextView textView, final int color)
    {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        alertDialogBuilder.setCancelable(false);
        alertDialogBuilder.setMessage("New Date and Time");
        alertDialogBuilder.setPositiveButton("Pick New Date and Time",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        pickdate_sdate();
                    }
                });

        alertDialogBuilder.setNeutralButton("Reschdule",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                  addingdata(pos,statusvalue,textView,color);
            }
        });

         alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }
    void pickdate_sdate()
    {
        final Dialog dialog1 = new Dialog(context);
        dialog1.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog1.setContentView(R.layout.camgal);
        dialog1.setCanceledOnTouchOutside(false);
        final Calendar nextYear=Calendar.getInstance();
        nextYear.add(Calendar.YEAR,1);
        final  Calendar lastYear=Calendar.getInstance();
        lastYear.add(Calendar.YEAR,-1);
        btn_s=dialog1.findViewById(R.id.ok);
        calendar_s = dialog1.findViewById(R.id.calendar_view);
        calendar_s.init(lastYear.getTime(), nextYear.getTime()) //
                .inMode(CalendarPickerView.SelectionMode.SINGLE) //
                .withSelectedDate(new Date());
        calendar_s.setCustomDayView(new DefaultDayViewAdapter());
        Date today =new Date();
        calendar_s.setDecorators(Collections.<CalendarCellDecorator>emptyList());
        calendar_s.init(today,nextYear.getTime())
                .withSelectedDate(today).inMode(CalendarPickerView.SelectionMode.SINGLE);
        btn_s.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayList<Date> mydates = (ArrayList<Date>)calendar_s.getSelectedDates();
                for (int i = 0; i <mydates.size() ; i++) {
                    Date tempdate = mydates.get(i);
                    String testdate = DATE_FORMAT.format(tempdate);

                        newdate=testdate;
                        pickdate_time();
                       // alertDialog.setTitle(newdate+" "+newtime);

                    dialog1.dismiss();

                }

            }
        });
        dialog1.show();

    }
    void pickdate_time()
    {
        final Dialog dialog1 = new Dialog(context);
        dialog1.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog1.setContentView(R.layout.timepickfer);
        dialog1.setCanceledOnTouchOutside(false);
        final Calendar nextYear=Calendar.getInstance();
        nextYear.add(Calendar.YEAR,1);
        final  Calendar lastYear=Calendar.getInstance();
        lastYear.add(Calendar.YEAR,-1);
        btn_time=dialog1.findViewById(R.id.ok);
        timePicker=dialog1.findViewById(R.id.calendar_view);
        btn_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    if(String.valueOf(timePicker.getHour()).length()==1) {
                        if (String.valueOf(timePicker.getMinute()).equals("0"))
                           newtime="0"+timePicker.getHour() + ":" + "00" + ":" + "00";
                        else
                            newtime="0"+timePicker.getHour() + ":" + timePicker.getMinute() + ":" + "00";
                    }
                    else
                    {
                        if (String.valueOf(timePicker.getMinute()).equals("0"))
                            newtime=timePicker.getHour() + ":" + "00" + ":" + "00";
                        else
                            newtime=timePicker.getHour() + ":" + timePicker.getMinute() + ":" + "00";
                    }
                    mainreschduletime=newdate+" "+newtime;
                Toast.makeText(context, ""+mainreschduletime, Toast.LENGTH_SHORT).show();
                alertDialog.show();
               // alertDialog.setTitle(newdate+" "+newtime);


                dialog1.dismiss();
            }
        });
        dialog1.show();

    }
    private void addingdata(final int pos, int stausvalue, final TextView textView, final int color)
    {
        progressDialog.show();
        final OkHttpClient client = new OkHttpClient();
        Request request = family_his(pos,stausvalue);
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
                ((Activity)context).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try
                        {
                            JSONObject jsonObject=new JSONObject(body);
                            if(jsonObject.getBoolean("status"))
                            {
                                finalfetchtoken(jsonObject.getString("message"),textView,color,pos);

                            }
                            else
                            {
                                Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show();
                            }
                        }
                        catch (Exception e)
                        {
                            progressDialog.dismiss();
                            Toast.makeText(context, "Something went wrong!!", Toast.LENGTH_SHORT).show();
                        }

                    }
                });
            }

        });

    }
    @SuppressLint("StaticFieldLeak")
    private void SendNotification(final String msg, final TextView textView, final int color, final int pos)
    {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                progressDialog.dismiss();
                AlertDialog.Builder builder;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    builder = new AlertDialog.Builder(context, android.R.style.Theme_Material_Dialog_Alert);
                } else {
                    builder = new AlertDialog.Builder(context);
                }
                builder.setTitle("Message")
                        .setMessage(msg)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // continue with delete
                                dialog.dismiss();
                                textView.setTextColor(ContextCompat.getColor(context,color));
                                setTextViewDrawableColor(textView,color);

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


            }

            @Override
            protected Void doInBackground(Void... params) {
                try{

                    OkHttpClient client = new OkHttpClient();
                    JSONObject json = new JSONObject();
                    JSONObject dataJson = new JSONObject();
                    JSONObject dataJson1 = new JSONObject();
                    dataJson1.put("code","0");
                    dataJson1.put("userid",doctorEncounters.getAppointmentdata().get(pos).getUser().getId());
                    dataJson1.put("appointmentid",doctorEncounters.getAppointmentdata().get(pos).getAppointment().getId());
                    dataJson.put("key", "Feish");
                    dataJson.put("title", " Appointment Status");
                    dataJson.put("click_action", "com.app.feish.application.fcm_TARGET");
                    json.put("data", dataJson1);
                    json.put("notification", dataJson);
                    json.put("registration_ids", new JSONArray(tokenid));
                    RequestBody body = RequestBody.create(JSON, json.toString());
                    Request request = new Request.Builder()
                            .header("Authorization", "key=" + "AIzaSyBWOYZxRPWsLA6P6Wmrj5esOj7tXfEmNQY")
                            .url("https://fcm.googleapis.com/fcm/send")
                            .post(body)
                            .build();
                    Response response = client.newCall(request).execute();
                    Log.i("response",response.body().string());
                } catch (Exception e) {
                    //Log.d(TAG,e+"");
                }
                return null;
            }
        }.execute();
    }

    private Request fetchdoctokenid(int pos) {
        JSONObject postdata = new JSONObject();
        try {
            postdata.put("doctor_id",Integer.parseInt(doctorEncounters.getAppointmentdata().get(pos).getUser().getId()));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(JSON, postdata.toString());
        return new Request.Builder()
                .addHeader("X-Api-Key", "AB5433GMDF657VBB")
                .addHeader("Content-Type", "application/json")
                .url(ApiUtils.BASE_URL+"fetchTokenIDfornotification")
                .post(body)
                .build();

    }
    private void finalfetchtoken(final String msg,final TextView textView,final int color,final int pos)
    {
        OkHttpClient client = new OkHttpClient();
        Request request = fetchdoctokenid(pos);
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

                ((Activity)context).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try
                        {
                                 JSONObject jsonObject= new JSONObject(body);
                                if(jsonObject.getString("message").equals("success"))
                                {
                                    JSONArray jsonArray= jsonObject.optJSONArray("data");
                                    for (int i = 0; i <jsonArray.length() ; i++) {
                                        tokenid.add(jsonArray.getString(i));
                //                        Toast.makeText(context, ""+jsonArray.getString(i), Toast.LENGTH_SHORT).show();
                                    }

                                }
                                SendNotification(msg,textView,color,pos);
                        }
                        catch (Exception e)
                        {
                            progressDialog.dismiss();
                            Toast.makeText(context, "Something went wrong!!", Toast.LENGTH_SHORT).show();
                        }

                    }
                });
            }

        });

    }
}
