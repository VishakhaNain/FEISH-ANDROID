package com.app.feish.application.fragment;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
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
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.app.feish.application.Adpter.Ingrementadt;
import com.app.feish.application.Connectiondetector;
import com.app.feish.application.R;
import com.app.feish.application.Remote.ApiUtils;
import com.app.feish.application.model.fooditemlistmodel;
import com.app.feish.application.model.ingredientitempojo;
import com.app.feish.application.sessiondata.Prefhelper;
import com.squareup.okhttp.Callback;
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
import java.util.List;
import static android.app.Activity.RESULT_OK;
import static com.app.feish.application.fragment.Addfamilyreport.JSON;


public class Fooditems extends Fragment
{
    RecyclerView recyclerView;
    ArrayList<fooditemlistmodel>  fooditemlistmodels= new ArrayList<>();
    Fooditemsadt fooditemsadt;
    ListView listView;
    EditText editText;
    ArrayList<String> searchlist= new ArrayList<>();
    ProgressDialog progressDialog;
    private static final String ARG_SECTION_NUMBER = "section_number";
    private static final String ARG_USER_ID = "user_id";
    ArrayAdapter stringArrayAdapter;
    Connectiondetector connectiondetector;
    Spinner spinner_calories_spinner;
    CardView cardView,cardView_goback;
    TextView textView;
    LinearLayout linearLayout_allrecipe;
    View view1;
    public static Fooditems newInstance(int sectionNumber,int userid)
    {
        Fooditems fragment = new Fooditems();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        args.putInt(ARG_USER_ID, userid);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) { super.onCreate(savedInstanceState); }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
        recyclerView=view1.findViewById(R.id.recycler_view);
        editText=view1.findViewById(R.id.et_searchrecipe);
        cardView=view1.findViewById(R.id.cardmyrecipe);
        cardView_goback=view1.findViewById(R.id.cardgoback);
        linearLayout_allrecipe=view1.findViewById(R.id.allrecipe);
        textView=view1.findViewById(R.id.txtor);
        spinner_calories_spinner=view1.findViewById(R.id.calories_spinner);
        progressDialog= new ProgressDialog(getActivity());
        progressDialog.setTitle("Fetching........");
        progressDialog.setCancelable(false);
        connectiondetector= new Connectiondetector(getActivity());
        fooditemsadt= new Fooditemsadt(fooditemlistmodels,getActivity());
        if(connectiondetector.isConnectingToInternet())
        fetch();
        else {
            Toast.makeText(getActivity(), "No Internet!!", Toast.LENGTH_SHORT).show();
        }
        listView=view1.findViewById(R.id.list);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                editText.setText(stringArrayAdapter.getItem(position).toString());
                listView.setVisibility(View.GONE);
            }
        });


        final LinearLayoutManager linearLayoutManager= new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setNestedScrollingEnabled(false);
        RelativeLayout relativeLayout=view1.findViewById(R.id.search_go);

        relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              //  recyclerView.setAdapter(fooditemsadt);
                fooditemsadt.setFilter(fooditemlistmodels);
                final List<fooditemlistmodel> filteredModelList = filter(fooditemlistmodels, editText.getText().toString());
                fooditemsadt.setFilter(filteredModelList);
            }
        });
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }
            @Override
            public void afterTextChanged(Editable s) {
                listView.setVisibility(View.VISIBLE);
                stringArrayAdapter.getFilter().filter(s.toString());
                final List<fooditemlistmodel> filteredModelList = filter(fooditemlistmodels, editText.getText().toString());
                fooditemsadt.setFilter(filteredModelList);
            }
        });
cardView.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v)
    {
        linearLayout_allrecipe.setVisibility(View.GONE);
        cardView_goback.setVisibility(View.VISIBLE);
        final List<fooditemlistmodel> filteredModelList = filterforuser(fooditemlistmodels);
        Toast.makeText(getActivity(), ""+filteredModelList.size()+" recipe found!!", Toast.LENGTH_SHORT).show();
        fooditemsadt.setFilter(filteredModelList);
        }
});
        cardView_goback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                linearLayout_allrecipe.setVisibility(View.VISIBLE);
                cardView_goback.setVisibility(View.GONE);
                fooditemsadt.setFilter(fooditemlistmodels);
            }
        });
       spinner_calories_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
       @Override
       public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
      {
        final List<fooditemlistmodel> filteredModelListspinner = new ArrayList<>();
        final List<fooditemlistmodel> filteredModelListspinnero = new ArrayList<>();
        filteredModelListspinnero.addAll(fooditemlistmodels);
        for (fooditemlistmodel model : filteredModelListspinnero)
        {
            final String calories = model.getCalories();
            switch (position)
            {
                    case 1:
                    if(Integer.parseInt(calories)<100)
                    break;
                    case 2:
                        if(Integer.parseInt(calories)<200 && Integer.parseInt(calories)>100)
                            filteredModelListspinner.add(model);
                    break;
                    case 3:
                        if(Integer.parseInt(calories)<300 && Integer.parseInt(calories)>300)
                            filteredModelListspinner.add(model);
                        break;
                    case 4:
                        if(Integer.parseInt(calories)<400 && Integer.parseInt(calories)>300)
                            filteredModelListspinner.add(model);
                            break;
                    case 5:
                        if(Integer.parseInt(calories)<500 && Integer.parseInt(calories)>400)
                            filteredModelListspinner.add(model);
                            break;
                    case 6:
                        if(Integer.parseInt(calories)<600 && Integer.parseInt(calories)>500)
                            filteredModelListspinner.add(model);
                        break;
                    case 7:
                        if(Integer.parseInt(calories)>600)
                            filteredModelListspinner.add(model);
                            break;
                    default:
                                break;
            }
            if(position!=0&& filteredModelListspinner.size()>0) {
                fooditemsadt.setFilter(filteredModelListspinner);
             /*   fooditemsadt= new Fooditemsadt(filteredModelListspinner,getActivity());
                recyclerView.setAdapter(fooditemsadt);*/
            }
        }


    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
});

    }
    private List<fooditemlistmodel> filter(List<fooditemlistmodel> models, String query) {
        query = query.toLowerCase();

        final List<fooditemlistmodel> filteredModelList = new ArrayList<>();
        for (fooditemlistmodel model : models)
        {
            final String text = model.getName().toLowerCase();
            if (text.contains(query))
            {
                filteredModelList.add(model);
            }
        }
        return filteredModelList;
    }
    private List<fooditemlistmodel> filterforuser(List<fooditemlistmodel> models) {

        final List<fooditemlistmodel> filteredModelList = new ArrayList<>();
        for (fooditemlistmodel model : models)
        {
            final int text = model.getUser_id();
            if (Integer.parseInt(Prefhelper.getInstance(getActivity()).getUserid())==text)
            {
                filteredModelList.add(model);
            }
        }
        return filteredModelList;
    }

    // Inflate the view for the fragment based on layout XML
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view1 = inflater.inflate(R.layout.content_fooditems, container, false);
        return view1;
    }

    private Request family_his() {
        JSONObject postdata = new JSONObject();
        RequestBody body = RequestBody.create(JSON, postdata.toString());
        return new Request.Builder()
                .addHeader("X-Api-Key", "AB5433GMDF657VBB")
                .addHeader("Content-Type", "application/json")
                .url(ApiUtils.BASE_URL+"listrecipe")
                .post(body)
                .build();

    }

    private void fetch()
    {
        progressDialog.show();
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
                Log.i("1234", "onResponse: "+body);
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        try
                        {
                            JSONObject jsonObject=new JSONObject(body);
                            if(jsonObject.getInt("Success")==1)
                            {
                                JSONArray jsonArray= jsonObject.getJSONArray("RecipeList");

                                for (int i = 0; i <jsonArray.length() ; i++)
                                {
                                    JSONObject jsonObject1=jsonArray.getJSONObject(i);
                                    searchlist.add(jsonObject1.getString("recipe_name"));
                                    stringArrayAdapter= new ArrayAdapter(getActivity(),android.R.layout.simple_list_item_1,searchlist);
                                    listView.setAdapter(stringArrayAdapter);

                                    if(jsonObject1.getInt("usercreated")==1 && Integer.parseInt(Prefhelper.getInstance(getActivity()).getUserid())==jsonObject1.getInt("user_id")) {
                                        fooditemlistmodels.add(new fooditemlistmodel(jsonObject1.getInt("id"), jsonObject1.getString("recipe_name"), jsonObject1.getString("recipe_calories"), jsonObject1.getString("used_ingre"), jsonObject1.getString("ingre_calories"), jsonObject1.getInt("user_id")));
                                            cardView.setVisibility(View.VISIBLE);
                                            textView.setVisibility(View.VISIBLE);
                                    }
                                   else
                                    fooditemlistmodels.add(new fooditemlistmodel(jsonObject1.getInt("id"),jsonObject1.getString("recipe_name"),jsonObject1.getString("recipe_calories"),jsonObject1.getString("used_ingre"),jsonObject1.getString("ingre_calories"),0));

                                    fooditemsadt= new Fooditemsadt(fooditemlistmodels,getActivity());
                                    recyclerView.setAdapter(fooditemsadt);
                                }
                                    progressDialog.dismiss();
                            }
                            else
                            {
                                progressDialog.dismiss();
                                Toast.makeText(getActivity(), jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                            }

                        }
                        catch (Exception e)
                        {
                            progressDialog.dismiss();
                            Toast.makeText(getActivity(), ""+e, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }

        });

    }

    public class Fooditemsadt extends RecyclerView.Adapter<Fooditemsadt.MyViewHolder> {

        private List<fooditemlistmodel> dataSet;
        Context context;
        ArrayList<ingredientitempojo> ingredientitempojos= new ArrayList<>();
         class MyViewHolder extends RecyclerView.ViewHolder {

            TextView textView_foodname,textView_foodcalories,textView_addfood,textView_recipemoredetail;
            public MyViewHolder(View itemView) {
                super(itemView);
                this.textView_foodname=  itemView.findViewById(R.id.foodname);
                this.textView_foodcalories =  itemView.findViewById(R.id.foodcalories);
                this.textView_addfood =  itemView.findViewById(R.id.addfood);
                this.textView_recipemoredetail =  itemView.findViewById(R.id.recipemoredetail);
            }
        }

         Fooditemsadt(List<fooditemlistmodel> data, Context context) {
            this.dataSet = data;
            this.context=context;
        }

        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent,
                                               int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.fooditemlist, parent, false);
            return new MyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull final MyViewHolder holder, final int listPosition) {

            holder.textView_foodname.setText(dataSet.get(listPosition).getName());
            holder.textView_foodcalories.setText(dataSet.get(listPosition).getCalories()+" Calories");

            holder.textView_addfood.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent data = new Intent();
                    data.putExtra("name", dataSet.get(holder.getAdapterPosition()).getName());
                    data.putExtra("foodid", String.valueOf(dataSet.get(holder.getAdapterPosition()).getId()));
                    data.putExtra("calories", dataSet.get(holder.getAdapterPosition()).getCalories());
                    data.putExtra("position", getArguments().getInt(ARG_SECTION_NUMBER));
                    getActivity().setResult(RESULT_OK, data);
                    getActivity().finish();
                }
            });
            holder.textView_recipemoredetail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    fullrecipedetail(dataSet.get(listPosition).getUsed_ingre());

                }
            });
        }

        @Override
        public int getItemCount() {
            return dataSet.size();
        }
        void setFilter(List<fooditemlistmodel> countryModels) {
            dataSet = new ArrayList<>();
            dataSet.addAll(countryModels);
            notifyDataSetChanged();
        }
        private void fullrecipedetail(String useridingr)
        {
            final Dialog dialog= new Dialog(getActivity(),android.R.style.Theme_Light);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.fullrecipedetail);
            String ingrecode[]=useridingr.split("\\,");
            JSONArray stringList = new JSONArray(Arrays.asList(ingrecode));
           RecyclerView recyclerView=dialog.findViewById(R.id.recycler_view);
           TextView textView=dialog.findViewById(R.id.submitcreaterecipe);
           // editText_recipename=dialog.findViewById(R.id.recipename);
            LinearLayoutManager linearLayoutManager= new LinearLayoutManager(getActivity());
            recyclerView.setLayoutManager(linearLayoutManager);
            recyclerView.setNestedScrollingEnabled(false);
            Ingrementadt     ingrementadt= new Ingrementadt(ingredientitempojos,getActivity());
            recyclerView.setAdapter(ingrementadt);
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
            fullrecipedetailfetch(stringList,ingrementadt,dialog);

        }
        private Request fullrecipedetailcall(JSONArray ingrecode) {
            JSONObject postdata = new JSONObject();
            try {
                postdata.put("ingrecode",ingrecode);
            }
            catch (Exception e)
            {
                Toast.makeText(context, ""+e, Toast.LENGTH_SHORT).show();
            }
            RequestBody body = RequestBody.create(JSON, postdata.toString());
            return new Request.Builder()
                    .addHeader("X-Api-Key", "AB5433GMDF657VBB")
                    .addHeader("Content-Type", "application/json")
                    .url(ApiUtils.BASE_URL+"fullingredetail")
                    .post(body)
                    .build();

        }

        private void fullrecipedetailfetch(JSONArray ingrecode, final Ingrementadt recyclerView, final Dialog dialog)
        {
            progressDialog.show();
            OkHttpClient client = new OkHttpClient();
            Request request = fullrecipedetailcall(ingrecode);
            Log.i("", "onClick: "+request);
            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Request request, IOException e) {
                    Log.i("Activity", "onFailure: Fail");
                }
                @Override
                public void onResponse(final Response response) throws IOException {

                    final String body=response.body().string();
                    Log.i("1234", "onResponse: "+body);
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            progressDialog.dismiss();
                            try {
                                JSONObject jsonObject= new JSONObject(body);
                                JSONArray jsonArray= jsonObject.getJSONArray("result");
                                if(ingredientitempojos.size()>0)
                                    ingredientitempojos.clear();
                                for (int i = 0; i <jsonArray.length() ; i++)
                                {
                                    JSONArray jsonObject1=jsonArray.getJSONArray(i);
                                    for (int j = 0; j <jsonObject1.length() ; j++) {
                                        JSONObject jsonObject2=jsonObject1.getJSONObject(j);
                                        ingredientitempojos.add(new ingredientitempojo(jsonObject2.getString("Name"), jsonObject2.getString("Serving_Size"), jsonObject2.getString("Calcium"),jsonObject2.getString("Protein"),jsonObject2.getString("Carbohydrate"), jsonObject2.getString("Energy")));


                                    }

                                }
                                recyclerView.notifyDataSetChanged();
dialog.show();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    });
                }

            });

        }
    }




}
