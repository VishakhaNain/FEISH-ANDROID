package com.app.feish.application.Patient;

import android.content.DialogInterface;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.app.feish.application.Adpter.Ingrementadt;
import com.app.feish.application.R;
import com.app.feish.application.Remote.ApiUtils;
import com.app.feish.application.fragment.Fooditems;
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
import static com.app.feish.application.Patient.MedicalHitoryp.JSON;

public class FindRecipe extends AppCompatActivity {

    int mainposition;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_recipe);

        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Food List");
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mainposition=getIntent().getIntExtra("position",0);
        SectionsPagerAdapter mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        ViewPager mViewPager = findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        TabLayout tabLayout =findViewById(R.id.tabs);

        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));

    }
    public static class PlaceholderFragment extends Fragment
    {
        private static final String ARG_SECTION_NUMBER = "section_number";
        LinearLayout linearLayout_fullingre,linearLayout_findingre, linearLayout_addingrell;
        RecyclerView recyclerView;
        String  string_en="",string_serve="",string_enid="";
        TextView textView_submitcreaterecipe;
        CardView cardView_fulldeatil;
        TextView textView_addingre, textView_createrecipe,textView_txtsearch,tv_ingrename,tv_servingsize;
        Spinner  spinner_foodtype,spinner_foodname;
        ArrayList<String> foodtypelist= new ArrayList<>();
        ArrayList<String> foodnamelist= new ArrayList<>();
        ArrayList<String> usedingre= new ArrayList<>();
        ArrayList<String> usedingrcalories= new ArrayList<>();
        String ingretoinsert="",ingrecaloritoinsert="";
        String foodname="",foodtype="";
        View rootView;
        TextView tv_calciumvalue,tv_weightvalue,tv_percentwatervalue,tv_proteinvalue,tv_carbovalue,tv_fibervalue,tv_Cholesterolvalue,tv_totalfatvalue;
        TextView tv_Saturatefatvalue,tv_MonosatFatvalue,tv_PolyunsatFatvalue,tv_ironvalue,tv_Magnesiumvalue,tv_Sodiumvalue,tv_Phosphorousvalue,tv_CaPratiovalue;
        TextView tv_Potassiumvalue,tv_Niacinvalue,tv_Thiaminvalue,tv_Riboflavinvalue,tv_vitavalue,tv_vitb6value,tv_vitcvalue,tv_Zincvalue;
        EditText editText_recipename;
        String recipename="";
        ArrayList<ingredientitempojo> ingredientitempojos= new ArrayList<>();
        Ingrementadt ingrementadt;

        private void initviews()
        {
            tv_ingrename=rootView.findViewById(R.id.ingrename);
            tv_servingsize=rootView.findViewById(R.id.servingsize);
            textView_submitcreaterecipe=rootView.findViewById(R.id.submitcreaterecipe);
            tv_calciumvalue=rootView.findViewById(R.id.calciumvalue);
            tv_weightvalue=rootView.findViewById(R.id.weightvalue);
            tv_percentwatervalue=rootView.findViewById(R.id.percentwatervalue);
            tv_proteinvalue=rootView.findViewById(R.id.proteinvalue);
            tv_carbovalue=rootView.findViewById(R.id.carbovalue);
            tv_fibervalue=rootView.findViewById(R.id.fibervalue);
            tv_Cholesterolvalue=rootView.findViewById(R.id.Cholesterolvalue);
            tv_totalfatvalue=rootView.findViewById(R.id.totalfatvalue);
            tv_Saturatefatvalue=rootView.findViewById(R.id.Saturatefatvalue);
            tv_MonosatFatvalue=rootView.findViewById(R.id.MonosatFatvalue);
            tv_PolyunsatFatvalue=rootView.findViewById(R.id.PolyunsatFatvalue);
            tv_ironvalue=rootView.findViewById(R.id.ironvalue);
            tv_Magnesiumvalue=rootView.findViewById(R.id.Magnesiumvalue);
            tv_Sodiumvalue=rootView.findViewById(R.id.Sodiumvalue);
            tv_Phosphorousvalue=rootView.findViewById(R.id.Phosphorousvalue);
            tv_CaPratiovalue=rootView.findViewById(R.id.CaPratiovalue);
            tv_Potassiumvalue=rootView.findViewById(R.id.Potassiumvalue);
            tv_Niacinvalue=rootView.findViewById(R.id.Niacinvalue);
            tv_Thiaminvalue=rootView.findViewById(R.id.Thiaminvalue);
            tv_Riboflavinvalue=rootView.findViewById(R.id.Riboflavinvalue);
            tv_vitavalue=rootView.findViewById(R.id.vitavalue);
            tv_vitb6value=rootView.findViewById(R.id.vitb6value);
            tv_vitcvalue=rootView.findViewById(R.id.vitcvalue);
            tv_Zincvalue=rootView.findViewById(R.id.Zincvalue);
            recyclerView=rootView.findViewById(R.id.recycler_view);
            editText_recipename=rootView.findViewById(R.id.recipename);
            LinearLayoutManager linearLayoutManager= new LinearLayoutManager(getActivity());
            recyclerView.setLayoutManager(linearLayoutManager);
            recyclerView.setNestedScrollingEnabled(false);
            ingrementadt= new Ingrementadt(ingredientitempojos,getActivity());
            recyclerView.setAdapter(ingrementadt);

        }

        public PlaceholderFragment() { }

        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
             rootView = inflater.inflate(R.layout.fragment_find_recipe, container, false);
             initviews();
             linearLayout_fullingre=rootView.findViewById(R.id.fullingre);
             linearLayout_findingre=rootView.findViewById(R.id.ll_findingre);
             linearLayout_addingrell=rootView.findViewById(R.id.addingrell);
             cardView_fulldeatil=rootView.findViewById(R.id.ingr_detail);
             textView_addingre=rootView.findViewById(R.id.addingre);
             spinner_foodtype=rootView.findViewById(R.id.foodtype_spinner);
             spinner_foodname=rootView.findViewById(R.id.foodname_spinner);
             textView_createrecipe=rootView.findViewById(R.id.createrecipe);
             textView_txtsearch=rootView.findViewById(R.id.txtsearch);
             textView_createrecipe.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    linearLayout_fullingre.setVisibility(View.VISIBLE);
                    textView_createrecipe.setVisibility(View.GONE);

                 }
            });
            linearLayout_addingrell.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    linearLayout_findingre.setVisibility(View.VISIBLE);
                }
            });
            textView_submitcreaterecipe.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v)
                {
                    recipename=editText_recipename.getText().toString() ;
                    if(recipename.equals(""))
                    {
                        Toast.makeText(getActivity(), "Enter Recipe Name", Toast.LENGTH_SHORT).show();
                    }
                    else if(usedingrcalories.size()==0)
                    {
                        Toast.makeText(getActivity(), "Add Some ingredient", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        int temp, temp1;
                        ingretoinsert = "";
                        ingrecaloritoinsert = "";
                        int totalcal = 0;
                        for (int i = 0; i < usedingre.size(); i++) {
                            for (int j = i + 1; j < usedingre.size(); j++) {
                                if (Integer.parseInt(usedingre.get(i)) > Integer.parseInt(usedingre.get(j))) {
                                    temp = Integer.parseInt(usedingre.get(i));
                                    temp1 = Integer.parseInt(usedingrcalories.get(i));
                                    usedingre.set(i, usedingre.get(j));
                                    usedingrcalories.set(i, usedingrcalories.get(j));
                                    usedingre.set(j, String.valueOf(temp));
                                    usedingrcalories.set(j, String.valueOf(temp1));

                                }
                            }
                            totalcal = totalcal + Integer.parseInt(usedingrcalories.get(i));
                            if (ingretoinsert.equals("")) {
                                ingretoinsert = usedingre.get(i);
                                ingrecaloritoinsert = usedingrcalories.get(i);
                            } else {
                                ingretoinsert = ingretoinsert + "," + usedingre.get(i);
                                ingrecaloritoinsert = ingrecaloritoinsert + "," + usedingrcalories.get(i);
                            }

                        }

                        fetchingdata(editText_recipename.getText().toString(), ingretoinsert, ingrecaloritoinsert, String.valueOf(totalcal));
                    }
                    }
            });
            textView_addingre.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v)
                {
                    if(!usedingre.contains(string_enid)) {
                        ingredientitempojos.add(new ingredientitempojo(tv_ingrename.getText().toString(), string_serve, tv_calciumvalue.getText().toString(), tv_proteinvalue.getText().toString(), tv_carbovalue.getText().toString(), string_en));
                        usedingre.add(string_enid);
                        usedingrcalories.add(string_en);
                        cardView_fulldeatil.setVisibility(View.GONE);
                    }
                    else
                    {
                        Toast.makeText(getActivity(), "This ingredient is already added!!", Toast.LENGTH_SHORT).show();
                    }
                    ingrementadt.notifyDataSetChanged();

                }
            });
                   addingdata();
            return rootView;
        }


        private void fetchingdata(String recipe_name,String used_ingre,String ingre_cal,String recipe_cal)
        {
            OkHttpClient client = new OkHttpClient();
            Request request = createrecipe(recipe_name,used_ingre,ingre_cal,recipe_cal);
            Log.i("", "onClick: "+request);
            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Request request, IOException e) {
                    Log.i("Activity", "onFailure: Fail");
                }
                @Override
                public void onResponse(final Response response) throws IOException {

                    final String body=response.body().string();
                    Log.i("1234check", "onResponse: "+body);
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                JSONObject jsonObject= new JSONObject(body);
                                if(jsonObject.getInt("Success")==1)
                                {

                                    AlertDialog.Builder builder;
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                        builder = new AlertDialog.Builder(getActivity(), android.R.style.Theme_Material_Dialog_Alert);
                                    } else {
                                        builder = new AlertDialog.Builder(getActivity());
                                    }
                                    builder.setTitle("Message")
                                            .setMessage(jsonObject.getString("message"))
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

                                }
                                else if(jsonObject.getInt("Success")==2)
                                {
                                    JSONArray jsonArray=jsonObject.getJSONArray("result");
                                    for (int i = 0; i <jsonArray.length() ; i++)
                                    {
                                        JSONObject jsonObject1= jsonArray.getJSONObject(i);
                                        AlertDialog.Builder builder;
                                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                            builder = new AlertDialog.Builder(getActivity(), android.R.style.Theme_Material_Dialog_Alert);
                                        } else {
                                            builder = new AlertDialog.Builder(getActivity());
                                        }
                                        builder.setTitle("Message")
                                                .setMessage(jsonObject.getString("message")+" "+jsonObject1.getString("recipe_name"))
                                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        getActivity().finish();
                                                    }
                                                })

                                                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int which) {
                                                    }
                                                })
                                                .show();
                                        builder.setCancelable(false);

                                    }

                                }
                                else
                                {
                                    Toast.makeText(getActivity(), "Error!!", Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }


                        }
                    });
                }

            });

        }


        private Request createrecipe(String recipe_name,String used_ingre,String ingre_cal,String recipe_cal)
        {
            JSONObject postdata = new JSONObject();
            try {
                postdata.put("user_id", Integer.parseInt(Prefhelper.getInstance(getActivity()).getUserid()));
                postdata.put("recipe_name", recipe_name);
                postdata.put("used_ingre",used_ingre);
                postdata.put("ingre_calories", ingre_cal);
                postdata.put("recipe_calories",Integer.parseInt(recipe_cal));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            RequestBody body = RequestBody.create(JSON, postdata.toString());
            return new Request.Builder()
                    .addHeader("X-Api-Key", "AB5433GMDF657VBB")
                    .addHeader("Content-Type", "application/json")
                    .url(ApiUtils.BASE_URL+"createrecipe")
                    .post(body)
                    .build();
        }

        private Request listFoodType() {
            JSONObject postdata = new JSONObject();
            RequestBody body = RequestBody.create(JSON, postdata.toString());
            return new Request.Builder()
                    .addHeader("X-Api-Key", "AB5433GMDF657VBB")
                    .addHeader("Content-Type", "application/json")
                    .url(ApiUtils.BASE_URL+"listFoodType")
                    .post(body)
                    .build();

        }
        private void addingdata()
        {
            OkHttpClient client = new OkHttpClient();
            Request request = listFoodType();

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
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                JSONObject jsonObject=new JSONObject(body);
                                if(jsonObject.getString("message").equals("Success"))
                                {
                                    JSONArray jsonArray= jsonObject.getJSONArray("data");
                                    for (int i = 0; i <jsonArray.length() ; i++)
                                    {
                                        JSONObject jsonObject1= jsonArray.getJSONObject(i);
                                        foodtypelist.add(jsonObject1.getString("Food_type"));
                                    }
                                    ArrayAdapter  arrayAdapter= new ArrayAdapter(getActivity(),R.layout.spinner_itemwhite,foodtypelist);
                                    spinner_foodtype.setAdapter(arrayAdapter);
                                    spinner_foodtype.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                        @Override
                                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                                   foodtype=foodtypelist.get(position);
                                                  listfood(foodtypelist.get(position));
                                        }

                                        @Override
                                        public void onNothingSelected(AdapterView<?> parent) {

                                        }
                                    });
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    });
                }

            });

        }
        private Request listmainfood(String food) {
            JSONObject postdata = new JSONObject();
            try {
                 postdata.put("Food_type",food);
            }
            catch (Exception e)
            {
                Toast.makeText(getActivity(), ""+e, Toast.LENGTH_SHORT).show();
            }
            RequestBody body = RequestBody.create(JSON, postdata.toString());
            return new Request.Builder()
                    .addHeader("X-Api-Key", "AB5433GMDF657VBB")
                    .addHeader("Content-Type", "application/json")
                    .url(ApiUtils.BASE_URL+"listmainfood")
                    .post(body)
                    .build();

        }
        private void listfood(String food)
        {
            OkHttpClient client = new OkHttpClient();
            Request request = listmainfood(food);

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
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                JSONObject jsonObject=new JSONObject(body);
                                if(jsonObject.getString("message").equals("Success"))
                                {
                                    JSONArray jsonArray= jsonObject.getJSONArray("data");
                                    if(foodnamelist.size()>0)
                                        foodnamelist.clear();
                                    foodnamelist.add("---Select---");
                                    for (int i = 0; i <jsonArray.length() ; i++)
                                    {
                                        JSONObject jsonObject1= jsonArray.getJSONObject(i);
                                        foodnamelist.add(jsonObject1.getString("Name"));
                                    }
                                    ArrayAdapter  arrayAdapter= new ArrayAdapter(getActivity(),R.layout.spinner_itemwhite,foodnamelist);
                                    spinner_foodname.setAdapter(arrayAdapter);
                                    spinner_foodname.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                        @Override
                                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                                   foodname=foodnamelist.get(position);
                                                   if(!foodnamelist.get(position).equals("---Select---"))
                                                   {
                                                      completefooddetailfetch(foodtype,foodname);
                                                       cardView_fulldeatil.setVisibility(View.VISIBLE);
                                                   }
                                        }

                                        @Override
                                        public void onNothingSelected(AdapterView<?> parent) {

                                        }
                                    });
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    });
                }

            });

        }
        private Request completefoodcall(String foodtype,String foodname)
        {
            JSONObject postdata = new JSONObject();
            try {
                 postdata.put("Food_type",foodtype);
                 postdata.put("Name",foodname);
            }
            catch (Exception e)
            { Toast.makeText(getActivity(), ""+e, Toast.LENGTH_SHORT).show(); }

            RequestBody body = RequestBody.create(JSON, postdata.toString());
            return new Request.Builder()
                    .addHeader("X-Api-Key", "AB5433GMDF657VBB")
                    .addHeader("Content-Type", "application/json")
                    .url(ApiUtils.BASE_URL+"completefooddeatil")
                    .post(body)
                    .build();

        }
        private void completefooddetailfetch(String foodtype,String foodname)
        {
            OkHttpClient client = new OkHttpClient();
            Request request = completefoodcall(foodtype,foodname);

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
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run()
                        {
                            try {
                                JSONObject jsonObject=new JSONObject(body);
                                if(jsonObject.getString("message").equals("Success"))
                                {
                                    JSONArray jsonArray= jsonObject.getJSONArray("data");
                                    for (int i = 0; i <jsonArray.length() ; i++)
                                    {
                                        JSONObject jsonObject1= jsonArray.getJSONObject(i);

                                        tv_calciumvalue.setText(jsonObject1.getString("Calcium"));
                                        tv_weightvalue.setText(jsonObject1.getString("Weight"));
                                        tv_percentwatervalue.setText(jsonObject1.getString("percent_water"));
                                        tv_proteinvalue.setText(jsonObject1.getString("Protein"));
                                        tv_carbovalue.setText(jsonObject1.getString("Carbohydrate"));
                                        tv_fibervalue.setText(jsonObject1.getString("Fiber"));
                                        tv_Cholesterolvalue.setText(jsonObject1.getString("Cholesterol"));
                                        tv_totalfatvalue.setText(jsonObject1.getString("Total_Fat"));
                                        tv_Saturatefatvalue.setText(jsonObject1.getString("Saturate_Fat"));
                                        tv_MonosatFatvalue.setText(jsonObject1.getString("Monosat_Fat"));
                                        tv_PolyunsatFatvalue.setText(jsonObject1.getString("Polyunsat_Fat"));
                                        tv_ironvalue.setText(jsonObject1.getString("Iron"));
                                        tv_Magnesiumvalue.setText(jsonObject1.getString("Magnesium"));
                                        tv_Sodiumvalue.setText(jsonObject1.getString("Sodium"));
                                        tv_Phosphorousvalue.setText(jsonObject1.getString("Phosphorous"));
                                        tv_CaPratiovalue.setText(jsonObject1.getString("CaP_ratio"));
                                        tv_Potassiumvalue.setText(jsonObject1.getString("Potassium"));
                                        tv_Zincvalue.setText(jsonObject1.getString("Zinc"));
                                        tv_Niacinvalue.setText(jsonObject1.getString("Niacin"));
                                        tv_Thiaminvalue.setText(jsonObject1.getString("Thiamin"));
                                        tv_Riboflavinvalue.setText(jsonObject1.getString("Riboflavin"));
                                        tv_vitavalue.setText(jsonObject1.getString("Vit_A"));
                                        tv_vitb6value.setText(jsonObject1.getString("Vit_B6"));
                                        tv_vitcvalue.setText(jsonObject1.getString("Vit_C"));
                                        tv_ingrename.setText(jsonObject1.getString("Name"));
                                        tv_servingsize.setText(jsonObject1.getString("Serving_Size")+" ("+jsonObject1.getString("Energy")+" Calorie )");
                                        string_en=jsonObject1.getString("Energy");
                                        string_enid=jsonObject1.getString("id");
                                        string_serve=jsonObject1.getString("Serving_Size");
                                    }

                                }
                            } catch (JSONException e) {
                                Toast.makeText(getActivity(), ""+e, Toast.LENGTH_SHORT).show();
                                e.printStackTrace();
                            }

                        }
                    });
                }

            });

        }
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter
    {

        private SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {

            switch (position)
            {
                case  0:
                    return Fooditems.newInstance(mainposition,Integer.parseInt(Prefhelper.getInstance(FindRecipe.this).getUserid()));

                case 1 :
                    return PlaceholderFragment.newInstance(Integer.parseInt(Prefhelper.getInstance(FindRecipe.this).getUserid()));

                    default:
                        return null;

            }

        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 2;
        }
    }
}
