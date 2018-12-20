package com.app.feish.application.Patient;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.app.feish.application.R;
import com.app.feish.application.Remote.ApiUtils;
import com.app.feish.application.model.dietlistpojo;
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
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import static com.app.feish.application.fragment.ListFamilyhistory.JSON;

public class Createdietplan extends AppCompatActivity
{
     RecyclerView recyclerView;
     LinearLayoutManager layoutManager;
     Context context=this;
     LinearLayout img_addtimetable;
     com.takisoft.datetimepicker.widget.TimePicker timePicker;
     EditText et_title,et_startdate,et_enddate;
     String str_edate="",str_sdate="",str_title="";
     int diet_id=0;
     CalendarPickerView calendar_s,calendar_e;
     Button btn_s,btn_e,btn_submit,btn_time;
     int update_plan=0;
     SimpleDateFormat DATE_FORMAT;
     ImageView imageView;
     ProgressDialog progressDialog;
     String userid="";
     JSONArray jsonArray;
     CardView cardView;
     CustomAdapter_createdietplan customAdapter_dietplan;
     TextView textView;
     ArrayList<dietlistpojo> searchdoctorpojos= new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_createdietplan);
        if(getIntent()!=null)
        {
            userid=getIntent().getStringExtra("userid");
        }
        else
        {
            userid=Prefhelper.getInstance(context).getUserid();
        }
       initViews();
       Clickviews();
       update_plan=getIntent().getIntExtra("update",0);
        if(update_plan!=0)
        {
            cardView.setVisibility(View.GONE);
            textView.setText("Update Diet Plan");
            diet_id=getIntent().getIntExtra("diet_id",0);
          et_title.setText(getIntent().getStringExtra("title"));
          et_startdate.setText(getIntent().getStringExtra("sdate"));
          et_enddate.setText(getIntent().getStringExtra("edate"));
            Bundle args = getIntent().getBundleExtra("BUNDLE");
           searchdoctorpojos = (ArrayList<dietlistpojo>) args.getSerializable("list");
            customAdapter_dietplan = new CustomAdapter_createdietplan(searchdoctorpojos,context);
            recyclerView.setAdapter(customAdapter_dietplan);

        }
        else
            {
                textView.setText("Create Diet Plan");
            searchdoctorpojos.add(new dietlistpojo("", "", "","","","","",0));
            customAdapter_dietplan = new CustomAdapter_createdietplan(searchdoctorpojos,context);
            recyclerView.setAdapter(customAdapter_dietplan);
        }

    }
    private void initViews()
    {
        progressDialog= new ProgressDialog(context);
        progressDialog.setTitle("Loading.........");
        recyclerView =findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        btn_submit=findViewById(R.id.submit);
        cardView=findViewById(R.id.dietplanlist);
        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Createdietplan.this,DietPlan.class);
                intent.putExtra("userid",Prefhelper.getInstance(Createdietplan.this).getUserid());
                startActivity(intent);
                finish();
            }
        });
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setNestedScrollingEnabled(false);
        textView=findViewById(R.id.maintitle);
        img_addtimetable=findViewById(R.id.addtimetable);
        imageView=findViewById(R.id.img_back);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        et_title=findViewById(R.id.diettitle);
        et_startdate=findViewById(R.id.sdate);
        et_enddate=findViewById(R.id.edate);
        DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");

    }
    private  void  Clickviews()
    {
        
        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                str_title = et_title.getText().toString();
                str_sdate=et_startdate.getText().toString();
                str_edate=et_enddate.getText().toString();
               if(validateVitalsign()) {
                   try {
                       jsonArray = new JSONArray();

                       ///  jsonText = jsonArray.toString();
                       if (update_plan != 0) {
                           for (int i = 0; i < searchdoctorpojos.size(); i++) {
                               JSONObject obj = new JSONObject();
                               obj.put("weekday", Integer.parseInt(searchdoctorpojos.get(i).getWeekday()))
                                       .put("recipe_code",Integer.parseInt(searchdoctorpojos.get(i).getFoodid()))
                                       .put("food_type", searchdoctorpojos.get(i).getMealtime())
                                       .put("time", searchdoctorpojos.get(i).getTime())
                                       .put("id", searchdoctorpojos.get(i).getId())
                                       .put("diet_plan_id", diet_id)
                                       .put("description", searchdoctorpojos.get(i).getDesc());

                               jsonArray.put(obj);
                           }
                          updatedata();
                       }
                           else {
                           for (int i = 0; i < searchdoctorpojos.size(); i++) {
                               JSONObject obj = new JSONObject();

                               obj.put("weekday", Integer.parseInt(searchdoctorpojos.get(i).getWeekday()))
                                       .put("recipe_code", searchdoctorpojos.get(i).getFoodid())
                                       .put("food_type", searchdoctorpojos.get(i).getMealtime())
                                       .put("time", searchdoctorpojos.get(i).getTime())
                                       .put("description", searchdoctorpojos.get(i).getDesc());

                               jsonArray.put(obj);
                           }
                           fetchdata();
                       }

                   } catch (JSONException e) {
                       Toast.makeText(context, "" + e, Toast.LENGTH_SHORT).show();
                       e.printStackTrace();
                   }
               }
            }
        });

        img_addtimetable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               /* if(update_plan!=0)
                {
                    dietlistpojos.add(new dietlistpojo("","",""));
                }
                else {*/
                    searchdoctorpojos.add(new dietlistpojo("", "", "","","","","",0));
             //   }
                customAdapter_dietplan.notifyDataSetChanged();
            }
        });
        et_enddate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickdate_end();
            }
        });
        et_startdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickdate_sdate();
            }
        });

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
        btn_s=(Button)dialog1.findViewById(R.id.ok);
        calendar_s = (CalendarPickerView) dialog1.findViewById(R.id.calendar_view);
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
                    str_sdate=testdate;
                    et_startdate.setText(testdate);
                    dialog1.dismiss();

                }

            }
        });
        dialog1.show();

    }
    void pickdate_end()
    {
        final Dialog dialog1 = new Dialog(context);
        dialog1.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog1.setContentView(R.layout.camgal);
        dialog1.setCanceledOnTouchOutside(false);
        final Calendar nextYear=Calendar.getInstance();
        nextYear.add(Calendar.YEAR,1);
        final  Calendar lastYear=Calendar.getInstance();
        lastYear.add(Calendar.YEAR,-1);
        btn_e=(Button)dialog1.findViewById(R.id.ok);
        calendar_e = (CalendarPickerView) dialog1.findViewById(R.id.calendar_view);
        calendar_e.init(lastYear.getTime(), nextYear.getTime()) //
                .inMode(CalendarPickerView.SelectionMode.SINGLE) //
                .withSelectedDate(new Date());
        calendar_e.setCustomDayView(new DefaultDayViewAdapter());
        Date today =new Date();
        calendar_e.setDecorators(Collections.<CalendarCellDecorator>emptyList());
        calendar_e.init(today,nextYear.getTime())
                .withSelectedDate(today).inMode(CalendarPickerView.SelectionMode.SINGLE);
        btn_e.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayList<Date> mydates = (ArrayList<Date>)calendar_e.getSelectedDates();
                for (int i = 0; i <mydates.size() ; i++) {
                    Date tempdate = mydates.get(i);
                    String testdate = DATE_FORMAT.format(tempdate);
                    str_edate=testdate;
                    et_enddate.setText(testdate);
                    dialog1.dismiss();

                }

            }
        });
        dialog1.show();
    }
    private boolean validateVitalsign(){
        if(str_edate.compareTo("")==0)
        {
            Toast.makeText(getApplicationContext(),"Choose Start Date",Toast.LENGTH_LONG).show();
            return false;
        }
        else if (str_sdate.compareTo("")==0)
        {
            Toast.makeText(getApplicationContext(),"Chosse End Date",Toast.LENGTH_LONG).show();
            return false;
        }
        if(str_title.compareTo("")==0)
        {
            Toast.makeText(getApplicationContext()," Plan name field empty",Toast.LENGTH_LONG).show();
            return false;
        }
          else {
            return true;
        }
    }
    private Request AddDietplan()
    {
        JSONObject postdata = new JSONObject();
        try {
            postdata.put("patient_id",Integer.parseInt(Prefhelper.getInstance(context).getUserid()));
            postdata.put("end_date",str_edate);
            postdata.put("start_date",str_sdate);
            postdata.put("plan_name",str_title);
            postdata.put("PlanDetails",jsonArray);
                   } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(JSON, postdata.toString());
        return new Request.Builder()
                .addHeader("X-Api-Key", "AB5433GMDF657VBB")
                .addHeader("Content-Type", "application/json")
                .url(ApiUtils.BASE_URL+"addDietPlan")
                .post(body)
                .build();
    }
    private void fetchdata()
    {
        OkHttpClient client = new OkHttpClient();
        Request request = AddDietplan();
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

                        try {
                            JSONObject jsonObject = new JSONObject(body);
                            if (jsonObject.getString("message").equals("success"))
                            {
                                AlertDialog.Builder builder;
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                    builder = new AlertDialog.Builder(context, android.R.style.Theme_Material_Dialog_Alert);
                                } else {
                                    builder = new AlertDialog.Builder(context);
                                }
                                builder.setTitle("Message")
                                        .setMessage(jsonObject.getString("data"))
                                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {
                                                // continue with delete
                                                finish();
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
                            progressDialog.dismiss();
                        } catch (Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(context, "" + e, Toast.LENGTH_SHORT).show();
                        }
                        progressDialog.dismiss();
                    }
                });
            }

        });

    }

    private Request UpdateDietplan()
    {
        JSONObject postdata = new JSONObject();
        try {
            postdata.put("patient_id",Integer.parseInt(Prefhelper.getInstance(context).getUserid()));
            postdata.put("end_date",str_edate);
            postdata.put("start_date",str_sdate);
            postdata.put("diet_id",diet_id);
            postdata.put("plan_name",str_title);
            postdata.put("PlanDetails",jsonArray);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(JSON, postdata.toString());
        return new Request.Builder()
                .addHeader("X-Api-Key", "AB5433GMDF657VBB")
                .addHeader("Content-Type", "application/json")
                .url(ApiUtils.BASE_URL+"UpdateDietPlan")
                .post(body)
                .build();
    }
    private void updatedata()
    {
        OkHttpClient client = new OkHttpClient();
        Request request = UpdateDietplan();
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
                        try {
                            JSONObject jsonObject = new JSONObject(body);
                            if (jsonObject.getString("message").equals("success"))
                            {
                                AlertDialog.Builder builder;
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                    builder = new AlertDialog.Builder(context, android.R.style.Theme_Material_Dialog_Alert);
                                } else {
                                    builder = new AlertDialog.Builder(context);
                                }
                                builder.setTitle("Message")
                                        .setMessage(jsonObject.getString("data"))
                                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {
                                                // continue with delete
                                                finish();
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
                            progressDialog.dismiss();
                        } catch (Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(context, "" + e, Toast.LENGTH_SHORT).show();
                        }
                        progressDialog.dismiss();
                    }
                });
            }

        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 5&& resultCode == RESULT_OK) {
           String name=data.getStringExtra("name");
           String id=data.getStringExtra("foodid");
          String  calories=data.getStringExtra("calories");
          int pos=data.getIntExtra("position",1);
            searchdoctorpojos.get(pos).setFoodcalories(calories);
            searchdoctorpojos.get(pos).setFoodname(name);
            searchdoctorpojos.get(pos).setFoodid(id);
            customAdapter_dietplan.notifyDataSetChanged();
        }
        else {
        }
    }

    class CustomAdapter_createdietplan extends RecyclerView.Adapter<CustomAdapter_createdietplan.MyViewHolder> {

        private List<dietlistpojo> dataSet;
        Context context;
        String foodlist[]={"Breakfast","Lunch","Snacks","Dinner"};
         String time;

        public  class MyViewHolder extends RecyclerView.ViewHolder {


            ImageView imageView;
            TextView textView_foodname;
          Spinner spinner_weekday,spinner_spinner_foodeattype;
          EditText editText_time,editText_desc;
            public MyViewHolder(View itemView) {
                super(itemView);
                imageView=itemView.findViewById(R.id.removetimetable);
                textView_foodname=itemView.findViewById(R.id.foodname);
                spinner_weekday=itemView.findViewById(R.id.spinner_weekday);
                spinner_spinner_foodeattype=itemView.findViewById(R.id.spinner_foodeattype);
                editText_desc=itemView.findViewById(R.id.et_desc);
                editText_time=itemView.findViewById(R.id.et_time);

            }
        }

        public CustomAdapter_createdietplan(List<dietlistpojo> data, Context context) {
            this.dataSet = data;
            this.context=context;
        }

        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent,
                                               int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.addlisttimetable, parent, false);

            MyViewHolder myViewHolder = new MyViewHolder(view);
            return myViewHolder;
        }

        @Override
        public void onBindViewHolder(final MyViewHolder holder, final int listPosition) {
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, R.layout.spinner_itemwhite,Arrays.asList(getResources().getStringArray(R.array.day)));

            holder.spinner_weekday.setAdapter(adapter);
            if(!searchdoctorpojos.get(listPosition).getWeekday().equals("")&& !searchdoctorpojos.get(listPosition).getWeekday().equals("7"))
            holder.spinner_weekday.setSelection(Integer.parseInt(searchdoctorpojos.get(listPosition).getWeekday()));
            if(!searchdoctorpojos.get(listPosition).getDesc().equals(""))
            holder.editText_desc.setText(searchdoctorpojos.get(listPosition).getDesc());
            if(!searchdoctorpojos.get(listPosition).getTime().equals(""))
            holder.editText_time.setText(searchdoctorpojos.get(listPosition).getTime());

            if(!searchdoctorpojos.get(listPosition).getFoodname().equals("0"))
            {
                holder.textView_foodname.setText(searchdoctorpojos.get(listPosition).getFoodname());
                switch (searchdoctorpojos.get(listPosition).getMealtime())
                {
                    case "Breakfast":
                        holder.spinner_spinner_foodeattype.setSelection(0);
                        break;
                        case "Lunch":
                            holder.spinner_spinner_foodeattype.setSelection(1);
                        break;
                        case "Snacks":
                            holder.spinner_spinner_foodeattype.setSelection(2);
                        break;
                        case "Dinner":
                            holder.spinner_spinner_foodeattype.setSelection(3);
                        break;
                        default:
                            holder.spinner_spinner_foodeattype.setSelection(0);
                            break;
                }

            }




         if(searchdoctorpojos.get(listPosition).getFoodname().equals(""))
             holder.textView_foodname.setText("Add Recipe");
         else
            holder.textView_foodname.setText(searchdoctorpojos.get(listPosition).getFoodname()+" ("+searchdoctorpojos.get(listPosition).getFoodcalories()+" Calories )");
            holder.imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    searchdoctorpojos.remove(holder.getAdapterPosition());
                    notifyDataSetChanged();
                }
            });
            holder.textView_foodname.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(context,FindRecipe.class);
                    intent.putExtra("position",listPosition);
                    startActivityForResult(intent,5);
                }
            });
            if(holder.getAdapterPosition()==0)
                holder.imageView.setVisibility(View.GONE);

            holder.spinner_weekday.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    searchdoctorpojos.get(listPosition).setWeekday(String.valueOf(position+1));
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
            holder.spinner_spinner_foodeattype.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    searchdoctorpojos.get(listPosition).setMealtime(foodlist[position]);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
            holder.editText_time.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    pickdate_time(holder.editText_time);
                }
            });
            holder.editText_time.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    searchdoctorpojos.get(listPosition).setTime(s.toString());
                 //   searchdoctorpojos.get(listPosition).setWeekday(holder.spinner_weekday.getSelectedItem().toString());

                }
            });
            holder.editText_desc.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    searchdoctorpojos.get(listPosition).setDesc(s.toString());
               //     searchdoctorpojos.get(listPosition).setWeekday(holder.spinner_weekday.getSelectedItem().toString());

                }
            });



        }

        @Override
        public int getItemCount() {
            return searchdoctorpojos.size();
        }
        void pickdate_time(final TextView textView)
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
                    if (timePicker.getHour() <= 9) {
                        if(timePicker.getMinute()<=9)
                            time="0"+timePicker.getHour()+":0"+timePicker.getMinute()+":00";
                        else
                            time="0"+timePicker.getHour()+":"+timePicker.getMinute()+":00";
                    } else
                    {
                        if(timePicker.getMinute()<=9)
                            time=timePicker.getHour()+":0"+timePicker.getMinute()+":00";
                        else
                            time=timePicker.getHour()+":"+timePicker.getMinute()+":00";
                    }

                     textView.setText(time);
                    dialog1.dismiss();
                }
            });
            dialog1.show();

        }
    }

}
