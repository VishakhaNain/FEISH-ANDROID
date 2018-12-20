package com.app.feish.application.doctor;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.app.feish.application.Adpter.SmartFragmentStatePagerAdapter;
import com.app.feish.application.Patient.AddHabit;
import com.app.feish.application.Patient.DietPlan;
import com.app.feish.application.Patient.FamilyHistory;
import com.app.feish.application.Patient.MedicalHitoryp;
import com.app.feish.application.Patient.Report;
import com.app.feish.application.Patient.VitalSigns;
import com.app.feish.application.R;
import com.app.feish.application.Remote.ApiUtils;
import com.app.feish.application.fragment.AddHabitfrg;
import com.app.feish.application.fragment.ListFamilyhistory;
import com.app.feish.application.fragment.ListMedicalhistory;
import com.app.feish.application.fragment.ListSoapNotes;
import com.app.feish.application.fragment.Listreport;
import com.app.feish.application.modelclassforapi.Appointmentdatum;
import com.app.feish.application.sessiondata.Prefhelper;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;
import com.squareup.picasso.Picasso;
import com.squareup.timessquare.CalendarCellDecorator;
import com.squareup.timessquare.CalendarPickerView;
import com.squareup.timessquare.DefaultDayViewAdapter;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import static com.app.feish.application.Patient.MedicalHitoryp.JSON;

public class PreviousPatientdetail extends AppCompatActivity {
    Toolbar toolbar;
    String name[]={"Health Profile","Vital Signs","Medical History","Family History","Diet Plan","Lab Result","Soap Note"};
    int img[]={R.drawable.healthprofile,R.drawable.vitalsigns,R.drawable.medicalhistory,R.drawable.familyhistory,R.drawable.diet_plan, R.drawable.labresult,R.drawable.diet_plan};
  CustomAdapter customAdapter;
    TextView tv_book,tv_resche,tv_confirm,tv_cancel;
    Context context=this;
    String newdate="",newtime="",mainreschduletime="";
    Button btn_s,btn_time;
    com.takisoft.datetimepicker.widget.TimePicker timePicker;
    CalendarPickerView calendar_s;
    SimpleDateFormat DATE_FORMAT,DATE_FORMAT_HH;
  ViewPager vpPager;
    AlertDialog alertDialog;
  String userid="";
  ImageView img_patient;
  TextView tv_paname,tv_paid,tv_page,tv_appdatetime,textView_bloodgrp;
  int code=0;
 public int  positionmain=0;
  TextView textView_opensoapnote;
  Appointmentdatum appointmentdatum;
  TextView textView_title,textView_adddetail;
  ListView listView;

    MyPagerAdapter adapterViewPager;
    public void Home(View view)
    {
       /* Intent in = new Intent(PreviousPatientdetail.this, DoctorDashboard.class);
        in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        //supportFinishAfterTransition();
        PreviousPatientdetail.this.finish();
        startActivity(in);*/
       finish();
    }
    private void setTextViewDrawableColor(TextView textView, int color) {
        for (Drawable drawable : textView.getCompoundDrawables()) {
            if (drawable != null) {
                drawable.setColorFilter(new PorterDuffColorFilter(color, PorterDuff.Mode.LIGHTEN));
            }
        }
    }
    public void Soapnotes (View view)
    {
        context.startActivity(new Intent(context, CompletePtientdetail.class));
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_previous_patientdetail);
        initView();
        DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
        DATE_FORMAT_HH = new SimpleDateFormat("dd");

    }
    private void initView()
    {
        tv_book=findViewById(R.id.appobooked);
        tv_resche=findViewById(R.id.apporesch);
        tv_confirm=findViewById(R.id.appoconfirm);
        tv_cancel=findViewById(R.id.appocancel);
        
        

        userid=getIntent().getStringExtra("userid");
        appointmentdatum= (Appointmentdatum)getIntent().getSerializableExtra("data");
        code=getIntent().getIntExtra("code",0);

        img_patient=findViewById(R.id.picpatient);
        tv_paname=findViewById(R.id.patientname);
        tv_paid=findViewById(R.id.patieentid);
        tv_page=findViewById(R.id.patientgender);
        textView_bloodgrp=findViewById(R.id.bloodgrp);
        tv_appdatetime=findViewById(R.id.appodatetime);

        if(appointmentdatum.getAppointment().getStatus().equals("1"))
        {
            tv_confirm.setTextColor(ContextCompat.getColor(context,R.color.holo_green));
            setTextViewDrawableColor(tv_confirm,R.color.holo_green);

        }
        if(appointmentdatum.getAppointment().getStatus().equals("2"))
        {

            tv_book.setTextColor(ContextCompat.getColor(context,R.color.pink));
            setTextViewDrawableColor(tv_book,R.color.pink);
        }
        if(appointmentdatum.getAppointment().getStatus().equals("3"))
        {
            tv_resche.setTextColor(ContextCompat.getColor(context,R.color.colorAccent2));
            setTextViewDrawableColor(tv_resche,R.color.colorAccent2);

            tv_confirm.setTextColor(ContextCompat.getColor(context,R.color.holo_green));
            setTextViewDrawableColor(tv_confirm,R.color.holo_green);

        }
        if(appointmentdatum.getAppointment().getStatus().equals("4"))
        {
            tv_book.setVisibility(View.GONE);
            tv_resche.setVisibility(View.GONE);
            tv_confirm.setVisibility(View.GONE);
            tv_cancel.setTextColor(ContextCompat.getColor(context,R.color.red));
            setTextViewDrawableColor(tv_cancel,R.color.red);
        }
///////////////////////////////statuscode///////////////////////////////
        tv_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(tv_confirm.getCurrentTextColor() != context.getResources().getColor( R.color.holo_green))
                {

                    updatestatus("Confirm this appointment....",1,tv_confirm,R.color.holo_green);
                }
                else
                {
                    Toast.makeText(context, "Already confirm", Toast.LENGTH_SHORT).show();
                }
            }
        });
        tv_book.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(tv_book.getCurrentTextColor() != context.getResources().getColor( R.color.pink))
                {

                    updatestatus("Booked this appointment....",2,tv_book,R.color.pink);
                }
                else
                {
                    Toast.makeText(context, "Already Booked", Toast.LENGTH_SHORT).show();
                }
            }
        });
        tv_resche.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(tv_resche.getCurrentTextColor() != context.getResources().getColor( R.color.colorAccent2))
                {
                    updatestatus("Reschedule this appointment....",3,tv_resche, R.color.colorAccent2);

                }
                else
                {
                    Toast.makeText(context, "Already Reschedule", Toast.LENGTH_SHORT).show();
                }
            }
        });
        tv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(tv_cancel.getCurrentTextColor() != context.getResources().getColor( R.color.red))
                {

                    updatestatus("Cancel this appointment....",4,tv_cancel,R.color.red);

                }
                else
                {
                    Toast.makeText(context, "Already Cancel", Toast.LENGTH_SHORT).show();
                }
            }
        });
        ///////////////////////////////


        if(appointmentdatum.getUser().getGender().equals("1"))
        {
            tv_page.setText("MALE");
        }
        else
        {

            tv_page.setText("FEMALE");
        }
        tv_paid.setText("P_ID:" + appointmentdatum.getUser().getId());
        tv_paname.setText("P_Name:" + appointmentdatum.getUser().getFullName());
        tv_appdatetime.setText(appointmentdatum.getAppointment().getAppointedTiming());
        if(appointmentdatum.getUser().getAvatar()!=null) {
            Picasso.with(context)
                    .load("http://feish.online/img/user_avtar/"+appointmentdatum.getUser().getAvatar().toString())
                    .into(img_patient);
        }

        textView_opensoapnote=findViewById(R.id.opensoapnote);
        listView=findViewById(R.id.list);
        textView_title=findViewById(R.id.title);
        textView_adddetail=findViewById(R.id.adddetail);
        customAdapter= new CustomAdapter(context,name,img);
        listView.setAdapter(customAdapter);
        textView_adddetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent;
                  switch (positionmain)
                  {
                         case 0:
                              intent= new Intent(PreviousPatientdetail.this, AddHabit.class);
                             intent.putExtra("userid",userid);
                             startActivity(intent);
                          break;
                          case 1:
                               intent= new Intent(PreviousPatientdetail.this, VitalSigns.class);
                              intent.putExtra("userid",userid);
                              startActivity(intent);
                          break;
                          case 2:
                              intent= new Intent(PreviousPatientdetail.this, MedicalHitoryp.class);
                              intent.putExtra("userid",userid);
                              startActivity(intent);
                          break;
                          case 3:
                              intent= new Intent(PreviousPatientdetail.this, FamilyHistory.class);
                              intent.putExtra("userid",userid);
                              startActivity(intent);
                          break;
                          case 4:
                              intent= new Intent(PreviousPatientdetail.this, DietPlan.class);
                              intent.putExtra("userid",userid);
                              startActivity(intent);
                          break;
                          case 5:
                              intent= new Intent(PreviousPatientdetail.this, Report.class);
                              intent.putExtra("userid",userid);
                              startActivity(intent);
                          break;
                          case 6:
                            /*  intent= new Intent(PreviousPatientdetail.this, Sop.class);
                              intent.putExtra("userid","758");
                              startActivity(intent);*/
                              textView_adddetail.setVisibility(View.GONE);
                          break;
                  }
            }
        });



        switch (code)
        {
            case  0:
                textView_opensoapnote.setVisibility(View.GONE);
                break;
                case  1:
                    textView_opensoapnote.setVisibility(View.GONE);
                break;
                case  2:
                    textView_opensoapnote.setVisibility(View.GONE);
                break;
        }
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        vpPager = findViewById(R.id.vpPager);
        adapterViewPager = new MyPagerAdapter(getSupportFragmentManager(),userid,appointmentdatum.getDoctor().getId(),appointmentdatum);
        vpPager.setAdapter(adapterViewPager);
        vpPager.setOffscreenPageLimit(2);
       vpPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
           @Override
           public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
               textView_title.setText(adapterViewPager.getPageTitle(position));
               if(position!=6&&textView_adddetail.getVisibility()==View.GONE) {
                   textView_adddetail.setVisibility(View.VISIBLE);

               }
               positionmain = position;
           }
           @Override
           public void onPageSelected(int position) { }
           @Override
           public void onPageScrollStateChanged(int state) { }
       });
    }
    public static class MyPagerAdapter extends SmartFragmentStatePagerAdapter
    {
        private static int NUM_ITEMS = 7;
        String userid,doc_id;
        Appointmentdatum appointmentdatum;

        public MyPagerAdapter(FragmentManager fragmentManager, String userid, String doc_id, Appointmentdatum  appointmentdatum) {
            super(fragmentManager);
            this.userid=userid;
            this.doc_id=doc_id;
            this.appointmentdatum=appointmentdatum;
        }

        // Returns total number of pages
        @Override
        public int getCount() {
            return NUM_ITEMS;
        }

        // Returns the fragment to display for that page
        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0: // Fragment # 0 - This will show FirstFragment
                    return AddHabitfrg.newInstance(2,userid);
                case 1: // Fragment # 0 - This will show FirstFragm// ent different title
                    return DrVitalsignfrg.newInstance("",userid);
                    case 2: // Fragment # 0 - This will show FirstFragment
                    return ListMedicalhistory.newInstance(2,userid);
                case 3: // Fragment # 0 - This will show FirstFragment different title
                    return ListFamilyhistory.newInstance(2,userid);
                case 4: // Fragment # 0 - This will show FirstFragm// ent different title
                    return DrDietPlanfrg.newInstance("",userid);
                case 5: // Fragment # 0 - This will show FirstFragm// ent different title
                    return Listreport.newInstance(2,userid);
                case 6: // Fragment # 0 - This will show FirstFragm// ent different title
                    return ListSoapNotes.newInstance(doc_id,userid,appointmentdatum);
                default:
                    return null;
            }
        }

        // Returns the page title for the top indicator
        @Override
        public CharSequence getPageTitle(int position)
        {
            if(position==0) {
                return "Health Profile";
            }
            else if(position==1) {
                return "Vital Sign";
            }
            else if(position==2) {
                return "Medical History";
            }
            else if(position==3) {
                return "Family History";
            }
            else if(position==4) {
                return "Diet Plan";
            }
            else if(position==5) {
                return "Lab Report";
            }
            else if(position==6) {
                return "Soap Notes";
            }
            return null;
        }

    }
    class CustomAdapter extends BaseAdapter {
        String [] result;
        Context context;
        int [] imageId;
        private  LayoutInflater inflater;
        private CustomAdapter(Context context,String[] prgmNameList, int[] prgmImages) {
            // TODO Auto-generated constructor stub
            result=prgmNameList;
            imageId=prgmImages;
            this.context=context;
            inflater = ( LayoutInflater )context.
                    getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }
        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return result.length;
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
            TextView tv;
            ImageView img;
        }
        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            // TODO Auto-generated method stub
            Holder holder=new Holder();
            View rowView;
            rowView = inflater.inflate(R.layout.drsidepatientlayout, null);
            holder.tv= rowView.findViewById(R.id.txt);
            holder.img= rowView.findViewById(R.id.img);
            holder.tv.setText(result[position]);
            holder.img.setImageResource(imageId[position]);
            rowView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                  vpPager.setCurrentItem(position);
               textView_title.setText(adapterViewPager.getPageTitle(position));
                }
            });
            return rowView;
        }

    }
    private Request family_his(int stausvalue) {
        JSONObject postdata = new JSONObject();
        try
        {
            postdata.put("id",appointmentdatum.getAppointment().getId());
            postdata.put("appointed_timing", appointmentdatum.getAppointment().getAppointedTiming());
            postdata.put("user_id",appointmentdatum.getUser().getId());
            postdata.put("doctor_id",Integer.parseInt(appointmentdatum.getDoctor().getId()));
            postdata.put("status_updated_by",Integer.parseInt(Prefhelper.getInstance(context).getUserid()));
            postdata.put("service_id",appointmentdatum.getService().getId());
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
    private void updatestatus(String title, final int statusvalue, final TextView  textView, final int color)
    {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        alertDialogBuilder.setMessage(title);
        alertDialogBuilder.setPositiveButton("yes",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        if(statusvalue==3)
                            pickdateandtime(statusvalue,textView,color);
                        else
                            addingdata(statusvalue,textView,color);
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
    private void pickdateandtime(final int statusvalue, final TextView textView, final int color)
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
                addingdata(statusvalue,textView,color);
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
    private void addingdata(int stausvalue, final TextView textView, final int color)
    {
        OkHttpClient client = new OkHttpClient();
        Request request = family_his(stausvalue);
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
                            else
                            {
                                Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show();
                            }
                        }
                        catch (Exception e)
                        {
                            Toast.makeText(context, ""+e, Toast.LENGTH_SHORT).show();
                        }

                    }
                });
            }

        });

    }
}


