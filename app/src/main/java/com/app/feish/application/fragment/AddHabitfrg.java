package com.app.feish.application.fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;
import com.app.feish.application.R;
import com.app.feish.application.Remote.ApiUtils;
import com.app.feish.application.model.addhabitpojo;
import com.app.feish.application.modelclassforapi.HabitRecordpojo;
import com.app.feish.application.sessiondata.Prefhelper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.List;


public class AddHabitfrg extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    RecyclerView recyclerView;
    CustomAdapter_addhabit customAdapter_addhabit;
    List<addhabitpojo> addhabitpojos=new ArrayList<>();
    LinearLayoutManager layoutManager;
    HabitRecordpojo habitRecordpojo;
    ImageView imageView;
    String userid="";
    Button button_habit;
    Hashtable<Integer,String> integerStringHashMap= new Hashtable<>();
    ArrayList<Integer> integers = new ArrayList<>();
    private static final String SELECTED_ITEM_POSITION = "ItemPosition";
    private int mPosition;
    View  view;
    public static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");
    JSONArray jsonArray,jsonArray_delete;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public AddHabitfrg() {
        // Required empty public constructor
    }
    public static AddHabitfrg newInstance(int param1, String param2) {
        AddHabitfrg fragment = new AddHabitfrg();
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            userid = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view= inflater.inflate(R.layout.fragment_add_habitfrg, container, false);

        recyclerView= view.findViewById(R.id.recycler_view);
        imageView= view.findViewById(R.id.img_back);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });
        recyclerView.setHasFixedSize(true);
        button_habit=view.findViewById(R.id.habitupdate);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setNestedScrollingEnabled(false);
        fetchingdata();


        button_habit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Updatingdata();
            }
        });
        return view;
    }
    private void Updatingdata()
    {
        try {
            jsonArray = new JSONArray();
            jsonArray_delete= new JSONArray();
            for (int i = 0; i < addhabitpojos.size(); i++)
            {
                if (addhabitpojos.get(i).getUseredit() == 1)
                {
                    JSONObject obj = new JSONObject();
                    obj.put("id", addhabitpojos.get(i).getId())
                            .put("habit_id", integers.get(i))
                            .put("frequency", addhabitpojos.get(i).getFrequency())
                            .put("unit", addhabitpojos.get(i).getUnit())
                            .put("time_period", addhabitpojos.get(i).gettimeperiod())
                            .put("habit_since", addhabitpojos.get(i).getHabitsince())
                            .put("is_stopped", addhabitpojos.get(i).getIsstop());

                    jsonArray.put(obj);

                }

                if(addhabitpojos.get(i).getUseredit() == 0 && addhabitpojos.get(i).getHabit())
                {

                    JSONObject jsonObject= new JSONObject();
                    jsonObject.put("id",addhabitpojos.get(i).getId());
                    jsonArray_delete.put(jsonObject);
                }

            }
            if(jsonArray.length()>0) {
                fetchdata();
                if(jsonArray_delete.length()>0)
                    deteledata();
            }
            else
            {
                Toast.makeText(getActivity(), "Nothing To Update", Toast.LENGTH_SHORT).show();
            }
        }
        catch (JSONException e)
        {

        }
    }
    private Request UpdateHabitDetail()
    {
        JSONObject postdata = new JSONObject();
        try {
            postdata.put("patient_id",Integer.parseInt(Prefhelper.getInstance(getActivity()).getUserid()));
            postdata.put("PatientHabitDetail",jsonArray);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(JSON, postdata.toString());
        return new Request.Builder()
                .addHeader("X-Api-Key", "AB5433GMDF657VBB")
                .addHeader("Content-Type", "application/json")
                .url(ApiUtils.BASE_URL+"UpdateHabitDetail")
                .post(body)
                .build();
    }
    private void fetchdata()
    {
        OkHttpClient client = new OkHttpClient();
        Request request = UpdateHabitDetail();
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

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        try {
                            JSONObject jsonObject = new JSONObject(body);
                            if (jsonObject.getString("message").equals("success"))
                            {
                                AlertDialog.Builder builder;
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                    builder = new AlertDialog.Builder(getActivity(), android.R.style.Theme_Material_Dialog_Alert);
                                } else {
                                    builder = new AlertDialog.Builder(getActivity());
                                }
                                builder.setTitle("Message")
                                        .setMessage(jsonObject.getString("data"))
                                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {
                                                // continue with delete
                                                getActivity().finish();
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
                                Toast.makeText(getActivity(), "Error", Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception e) {

                            Toast.makeText(getActivity(), "" + e, Toast.LENGTH_SHORT).show();
                        }

                    }
                });
            }

        });

    }
    private Request DeleteHabitDetail()
    {
        JSONObject postdata = new JSONObject();
        try {
            postdata.put("patient_id",Integer.parseInt(Prefhelper.getInstance(getActivity()).getUserid()));
            postdata.put("PatientHabitDetaildelete",jsonArray_delete);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(JSON, postdata.toString());
        return new Request.Builder()
                .addHeader("X-Api-Key", "AB5433GMDF657VBB")
                .addHeader("Content-Type", "application/json")
                .url(ApiUtils.BASE_URL+"deletePatientHealthRecord")
                .post(body)
                .build();
    }
    private void deteledata()
    {
        OkHttpClient client = new OkHttpClient();
        Request request = DeleteHabitDetail();
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

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        Toast.makeText(getActivity(), ""+body, Toast.LENGTH_SHORT).show();

                    }
                });
            }

        });

    }
    private Request Patient_detail()
    {
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
                .url(ApiUtils.BASE_URL+"fetchhabits")
                .post(body)
                .build();
    }
    public void pdetailJSON(String response) {
        Gson gson = new GsonBuilder().create();
        habitRecordpojo = gson.fromJson(response, HabitRecordpojo.class);
    }
    private void fetchingdata()
    {
        OkHttpClient client = new OkHttpClient();
        Request request = Patient_detail();
        Log.i("", "onClick: "+request);
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                Log.i("Activity", "onFailure: Fail");
            }
            @Override
            public void onResponse(final Response response) throws IOException {

                final String body=response.body().string();
                pdetailJSON(body);
                Log.i("1234check", "onResponse: "+body);
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {

                            if (habitRecordpojo.getSuccess() == 1) {
                                for (int l = 0; l < habitRecordpojo.getData().size(); l++) {
                                    integers.add(habitRecordpojo.getData().get(l).getId());
                                    integerStringHashMap.put(habitRecordpojo.getData().get(l).getId(), habitRecordpojo.getData().get(l).getHabitName());
                                    addhabitpojos.add(new addhabitpojo(0, false, habitRecordpojo.getData().get(l).getHabitName(), 0, "", 0, "", 0,0));

                                }

                                for (int i = 0; i < habitRecordpojo.getRecord().size(); i++) {
                                    int pos = integers.indexOf(habitRecordpojo.getRecord().get(i).getHabitId());
                                    addhabitpojos.set(pos, new addhabitpojo(habitRecordpojo.getRecord().get(i).getId(), true, integerStringHashMap.get(habitRecordpojo.getRecord().get(i).getHabitId()), habitRecordpojo.getRecord().get(i).getFrequency(), habitRecordpojo.getRecord().get(i).getUnit(), habitRecordpojo.getRecord().get(i).getHabitSince(), habitRecordpojo.getRecord().get(i).getTimePeriod(), habitRecordpojo.getRecord().get(i).getIsStopped(),1));

                                }
                            }


                            customAdapter_addhabit = new CustomAdapter_addhabit(addhabitpojos, getActivity());
                            recyclerView.setAdapter(customAdapter_addhabit);
                        }
                        catch (Exception e)
                        {
                            Toast.makeText(getActivity(), ""+e, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }

        });

    }
    class CustomAdapter_addhabit extends RecyclerView.Adapter<CustomAdapter_addhabit.MyViewHolder> {

        private List<addhabitpojo> dataSet;
        Context context;

        public  class MyViewHolder extends RecyclerView.ViewHolder {

            CheckBox checkBox_habbit,checkBox_isstop;
            Spinner spinner_unit,spinner_habitsince,spinner_stopwhen;
            EditText editText_frequency;
            // CardView cardView;
            private MyViewHolder(View itemView) {
                super(itemView);
                checkBox_habbit=itemView.findViewById(R.id.checkbox_habit);
                checkBox_isstop=itemView.findViewById(R.id.checkbox_isstopped);
                spinner_unit=itemView.findViewById(R.id.spinner_unit);
                spinner_habitsince=itemView.findViewById(R.id.spinner_habitsince);
                spinner_stopwhen=itemView.findViewById(R.id.spinner_stopwhen);
                editText_frequency=itemView.findViewById(R.id.frequency);
            }
        }

        CustomAdapter_addhabit(List<addhabitpojo> data, Context context) {
            this.dataSet = data;
            this.context=context;
        }

        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.addlhabitlayout, parent, false);

            return new MyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull final MyViewHolder holder, final int listPosition) {
            final addhabitpojo addhabitpojo=dataSet.get(listPosition);
            holder.editText_frequency.setText(""+addhabitpojo.getFrequency());
            final List<String> Lines_unit = Arrays.asList(context.getResources().getStringArray(R.array.Unit));
            int i=  Lines_unit.indexOf(addhabitpojo.getUnit());
            holder.spinner_unit.setSelection(i);
            final List<String> Lines_timeperiod = Arrays.asList(context.getResources().getStringArray(R.array.TimePeroid));
            int i1=  Lines_timeperiod.indexOf(addhabitpojo.gettimeperiod());
            holder.spinner_habitsince.setSelection(i1);

            final List<String> Lines_years = Arrays.asList(context.getResources().getStringArray(R.array.Years));
            int i2=  Lines_years.indexOf(""+addhabitpojo.getHabitsince());
            holder.spinner_stopwhen.setSelection(i2);


            holder.checkBox_habbit.setChecked(addhabitpojo.getHabit());
            if((addhabitpojo.getIsstop()==0))
                holder.checkBox_isstop.setChecked(false);
            else
                holder.checkBox_isstop.setChecked(true);

            holder.checkBox_habbit.setText(addhabitpojo.getHabittitle());
            if(addhabitpojo.getHabit())
            {
                holder.editText_frequency.setVisibility(View.VISIBLE);
                holder.spinner_habitsince.setVisibility(View.VISIBLE);
                holder.spinner_stopwhen.setVisibility(View.VISIBLE);
                holder.spinner_unit.setVisibility(View.VISIBLE);
                holder.checkBox_isstop.setVisibility(View.VISIBLE);
            }

            holder.checkBox_habbit.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if(isChecked)
                    {

                        holder.editText_frequency.setVisibility(View.VISIBLE);
                        holder.spinner_habitsince.setVisibility(View.VISIBLE);
                        holder.spinner_stopwhen.setVisibility(View.VISIBLE);
                        holder.spinner_unit.setVisibility(View.VISIBLE);
                        holder.checkBox_isstop.setVisibility(View.VISIBLE);
                        dataSet.get(holder.getAdapterPosition()).setUseredit(1);
                    }
                    else
                    {
                        holder.editText_frequency.setVisibility(View.INVISIBLE);
                        holder.spinner_habitsince.setVisibility(View.INVISIBLE);
                        holder.spinner_stopwhen.setVisibility(View.INVISIBLE);
                        holder.spinner_unit.setVisibility(View.INVISIBLE);
                        holder.checkBox_isstop.setVisibility(View.INVISIBLE);
                        dataSet.get(holder.getAdapterPosition()).setUseredit(0);
                    }

                }
            });
            holder.checkBox_isstop.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if(isChecked)
                    {
                        dataSet.get(holder.getAdapterPosition()).setIsstop(1);
                    }
                    else
                    {
                        dataSet.get(holder.getAdapterPosition()).setIsstop(0);
                    }

                }
            });

            holder.editText_frequency.addTextChangedListener(new TextWatcher()
            {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) { }
                @Override
                public void afterTextChanged(Editable s)
                {
                    if(!s.toString().equals(""))
                        dataSet.get(holder.getAdapterPosition()).setFrequency(Integer.parseInt(s.toString()));
                }
            });
            holder.spinner_unit.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    dataSet.get(holder.getAdapterPosition()).setUnit(Lines_unit.get(position));
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
            holder.spinner_habitsince.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    dataSet.get(holder.getAdapterPosition()).settimeperiod(Lines_timeperiod.get(position));
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
            holder.spinner_stopwhen.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    if(!Lines_years.get(position).equals("Years"))
                        dataSet.get(holder.getAdapterPosition()).setHabitsince(Integer.parseInt(Lines_years.get(position)));
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }

        @Override
        public int getItemCount() {
            return dataSet.size();
        }
    }


}
