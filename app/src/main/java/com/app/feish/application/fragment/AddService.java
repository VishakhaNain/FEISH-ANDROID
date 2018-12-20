package com.app.feish.application.fragment;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.app.feish.application.Connectiondetector;
import com.app.feish.application.Patient.MedicalHitoryp;
import com.app.feish.application.R;
import com.app.feish.application.Remote.ApiUtils;
import com.app.feish.application.doctor.ServiceDoc;
import com.app.feish.application.model.doctormsglist;
import com.app.feish.application.sessiondata.Prefhelper;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;

/**
 * Created by lenovo on 6/4/2016.
 */
public class AddService extends Fragment {
  View view1;
    public static final MediaType JSON = MediaType.parse("application/json:charset=utf-8");
    private EditText facilityname, description, address, locality, pincode, phone;
    String src="",specilizationtosend="";
    ProgressDialog progressDialog;
    Uri uri;
    String str_stateid,str_cityid;
    int stateid=0;
    Connectiondetector connectiondetector;
    ArrayList<String> Specialtylist= new ArrayList<>();
    ArrayList<doctormsglist> statelist= new ArrayList<>();
    ArrayList<doctormsglist> citylist= new ArrayList<>();
    ImageView imageView;
    EditText editText;
    private static final int PERMISSION_REQUEST_Camera_EXTERNAL_STORAGE = 218;
    private EditText chooseSpecialist;
    AlertDialog coupandialog;
    protected ArrayList<CharSequence> selectedspecilization = new ArrayList<>();
    TextView save;
    Spinner spinner_state_spinner,spinner_city_spinner;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
connectiondetector= new Connectiondetector(getActivity());
if(connectiondetector.isConnectingToInternet()) {
    fetchSpecialtylist();
    addingdata();
}
else
    Toast.makeText(getActivity(), "No Internet!", Toast.LENGTH_SHORT).show();


    }

    // Inflate the view for the fragment based on layout XML
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
         view1 = inflater.inflate(R.layout.fragment_service, container, false);
        checkPermission();
        facilityname =  view1.findViewById(R.id.doctorfacilityNameID);
        spinner_state_spinner=view1.findViewById(R.id.state_spinner);
        spinner_city_spinner=view1.findViewById(R.id.city_spinner);
        description = view1.findViewById(R.id.doctordescriptionID);
        address = view1.findViewById(R.id.fulladdressID);
       // placename =  view1.findViewById(R.id.placenameID);
        //submit =view1.findViewById(R.id.submitprofile);
        locality = view1.findViewById(R.id.localityID);
        pincode =  view1.findViewById(R.id.pincodeID);
        phone =view1.findViewById(R.id.phonenumID);
        chooseSpecialist = view1.findViewById(R.id.spinner_spe);
        chooseSpecialist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSelectColoursDialog();
            }
        });
        save = view1.findViewById(R.id.saveservicebuttonid);
        LinearLayout linearLayout=view1.findViewById(R.id.ll_logo);
         imageView=view1.findViewById(R.id.img);
         editText=view1.findViewById(R.id.uploadreport);

        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final CharSequence[] options = {"Choose From Gallery", "Cancel"};
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Add Report");

                builder.setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int item) {
                        if (options[item].equals("Take Photo")) {
                            Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            startActivityForResult(cameraIntent, 1);
                        } else if (options[item].equals("Choose From Gallery")) {
                            Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                            photoPickerIntent.setType("image/*");
                            startActivityForResult(photoPickerIntent, 7);
                        } else if (options[item].equals("Cancel")) {
                            dialog.dismiss();
                        }

                    }
                });
                coupandialog = builder.create();
                coupandialog.show();
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                OkHttpClient client = new OkHttpClient();
                Request validation_request = add_service_request();
                client.newCall(validation_request).enqueue(new Callback() {

                    @Override
                    public void onFailure(Request request, IOException e) {

                        // Toast.makeText(getApplicationContext(),"Fail",Toast.LENGTH_LONG).show();
                        Log.i("Activity", "onFailure: Fail");
                    }

                    @Override
                    public void onResponse(final Response response) throws IOException {
                                final String body=response.body().string();
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getActivity(), ""+body, Toast.LENGTH_SHORT).show();
                                try {
                                    JSONObject jsonObject = new JSONObject(body);
                                    AlertDialog.Builder builder = null;
                                    if (jsonObject.getBoolean("status")) {
                                        Toast.makeText(getContext(), "Succesfully Added ", Toast.LENGTH_LONG).show();
                                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                            builder = new AlertDialog.Builder(getActivity(), android.R.style.Theme_Material_Dialog_Alert);
                                        } else {
                                            builder = new AlertDialog.Builder(getActivity());
                                        }
                                        builder.setTitle("Service")
                                                .setMessage("Service Added Succesfully")
                                                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        ((ServiceDoc) getActivity()).switchviewpager();
                                                        // continue with delete
                                                        //finish();
                                                    }
                                                })
                                                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        // do nothing
                                                    }
                                                })
                                                // .setIcon(android.R.drawable.m)
                                                .show();
                                        //Intent intent=new Intent(VerificationChoiceActivity.this,SetPasswordActivity.class);
                                        //intent.putExtra("userid",userid);
                                        //startActivity(intent);
                                    } else {
                                        Toast.makeText(getContext(), "OOPS!!", Toast.LENGTH_LONG).show();
                                    }
                                }
                                catch (JSONException e)
                                {
                                    Toast.makeText(getActivity(), ""+e, Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                });
            }
        });


        return view1;
    }
    private Request add_service_request() {
          JSONObject postdata = new JSONObject();
        try {
            postdata.put("title", facilityname.getText().toString());
            postdata.put("description", description.getText().toString());
            postdata.put("address", address.getText().toString());
            postdata.put("city",str_cityid);
            postdata.put("locality", locality.getText().toString());
            postdata.put("pin_code", pincode.getText().toString());
            postdata.put("user_id", Prefhelper.getInstance(getActivity()).getUserid());
            postdata.put("specialty_id",specilizationtosend);
            postdata.put("phone", phone.getText().toString());
            // postdata.put("specialty_id", specialitypos);
            //postdata.put("height",height.getText().toString());
            // postdata.put("address",address.getText().toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(JSON, postdata.toString());
        final Request request = new Request.Builder()
                .addHeader("X-Api-Key", "AB5433GMDF657VBB")
                .addHeader("Content-Type", "application/json")
                .url("http://feish.online/apis/addService")
                .post(body)
                .build();
        return request;
        /*}
        else {

            File sourceFile = new File(src);
            Log.d("", "File...::::" + sourceFile + " : " + sourceFile.exists());

            final MediaType MEDIA_TYPE = getRealPathFromUri(getActivity(), uri).endsWith("png") ?
                    MediaType.parse("image/png") : MediaType.parse("image/jpeg");


            RequestBody requestBody = new MultipartBuilder()
                    .type(MultipartBuilder.FORM)
                    .addFormDataPart("user_id", Prefhelper.getInstance(getActivity()).getUserid())
                    .addFormDataPart("title", facilityname.getText().toString())
                    .addFormDataPart("description", description.getText().toString())
                    .addFormDataPart("address", address.getText().toString())
                    .addFormDataPart("city", placename.getText().toString())
                    .addFormDataPart("locality", locality.getText().toString())
                    .addFormDataPart("pin_code", pincode.getText().toString())
                    .addFormDataPart("specialty_id", specilizationtosend)
                    .addFormDataPart("phone", phone.getText().toString())
                    .addFormDataPart("profile_pic", "profile.png", RequestBody.create(MEDIA_TYPE, sourceFile))
                    .build();

            Request request = new Request.Builder()
                    .addHeader("X-Api-Key", "AB5433GMDF657VBB")
                    .addHeader("Content-Type", "application/json")
                    .url("http://dev.feish.online/apis/addService")
                    .post(requestBody)
                    .build();

            return request;
        }*/
    }
    public static String getRealPathFromUri(Context context, Uri contentUri) {
        Cursor cursor = null;
        try {
            String[] proj = {MediaStore.Images.Media.DATA};
            cursor = context.getContentResolver().query(contentUri, proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub

        switch (requestCode) {

            case 7:

                if (resultCode == RESULT_OK) {
                    uri = data.getData();
                    imageView.setImageURI(uri);
                    src = getPath(uri);
                    editText.setText(src);
                }
                else
                {
                    Toast.makeText(getActivity(), "No Picture Taken", Toast.LENGTH_SHORT).show();
                }
                break;
            case 1:
                if (resultCode == RESULT_OK) {
                    Bitmap selectimage = (Bitmap) data.getExtras().get("data");
                    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                    selectimage.compress(Bitmap.CompressFormat.JPEG, 80, bytes);
                    uri=getImageUri(getActivity(),selectimage);
                    imageView.setImageURI(uri);
                    src = getPath(uri);
                    editText.setText(src);
                }
                else
                {
                    Toast.makeText(getActivity(), "No Picture Taken", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }
    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }
    public String getPath(Uri uri) {

        String path;
        String[] projection = { MediaStore.Files.FileColumns.DATA };
        Cursor cursor = getActivity().getContentResolver().query(uri, projection, null, null, null);

        if(cursor == null){
            path = uri.getPath();
        }
        else{
            cursor.moveToFirst();
            int column_index = cursor.getColumnIndexOrThrow(projection[0]);
            path = cursor.getString(column_index);
            cursor.close();
        }

        return ((path == null || path.isEmpty()) ? (uri.getPath()) : path);
    }


    private void checkPermission() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (ContextCompat.checkSelfPermission(getActivity(),
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.CAMERA},
                        PERMISSION_REQUEST_Camera_EXTERNAL_STORAGE);
            }
        }
    }
    public static AddService newInstance(int page, String title) {
        AddService fragmentFirst = new AddService();
        Bundle args = new Bundle();
        args.putInt("someInt", page);
        args.putString("Stringlist", title);
        fragmentFirst.setArguments(args);
        return fragmentFirst;
    }
    private void fetchSpecialtylist()
    {
        OkHttpClient client = new OkHttpClient();
        Request request = occupationrequest();

        Log.i("", "onClick: "+request);
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                Log.i("Activity", "onFailure: Fail");
            }
            @Override
            public void onResponse(final Response response) throws IOException {

                final String body=response.body().string();
                // medicalconditionJSON(body);
                Log.i("1234add", "onResponse: "+body);
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONObject jsonObject = new JSONObject(body);
                            JSONArray jsonArray= jsonObject.getJSONArray("Specialty");
                            for (int i = 0; i <jsonArray.length() ; i++)
                            {
                                JSONObject jsonObject1= jsonArray.getJSONObject(i);
                                Specialtylist.add(jsonObject1.getString("specialty_name"));
                            }
                        }
                        catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getActivity(), ""+e, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }






        });

    }

    private Request occupationrequest()
    {
        JSONObject postdata = new JSONObject();
        RequestBody body = RequestBody.create(JSON, postdata.toString());
        return new Request.Builder()
                .addHeader("X-Api-Key", "AB5433GMDF657VBB")
                .addHeader("Content-Type", "application/json")
                .url("http://feish.online/apis/listspeciality")
                .post(body)
                .build();
    }
    // Store instance variables based on arguments passed
    protected void onChangeSelectedColours() {

        StringBuilder stringBuilder = new StringBuilder();
        StringBuilder stringBuilder1 = new StringBuilder();

        //  for(CharSequence colour : selectedspecilization)
        for (int i = 0; i <selectedspecilization.size() ; i++) {
            stringBuilder.append(selectedspecilization.get(i) + ",");
            stringBuilder1.append(String.valueOf(i+1) + ",");
        }
        specilizationtosend=stringBuilder1.toString();
        int a=specilizationtosend.lastIndexOf(",");
        specilizationtosend=specilizationtosend.substring(0,a);

        chooseSpecialist.setText(stringBuilder.toString());
        //   Toast.makeText(getActivity(), ""+specilizationtosend, Toast.LENGTH_SHORT).show();
        //chooseSpecialist.setText(specilizationtosend);

    }
    protected void showSelectColoursDialog() {

        boolean[] checkedColours = new boolean[Specialtylist.size()];

        int count = Specialtylist.size();

        for(int i = 0; i < count; i++)

            checkedColours[i] = selectedspecilization.contains(Specialtylist.get(i));

        DialogInterface.OnMultiChoiceClickListener coloursDialogListener = new DialogInterface.OnMultiChoiceClickListener() {

            @Override

            public void onClick(DialogInterface dialog, int which, boolean isChecked) {

                if(isChecked)

                    selectedspecilization.add(Specialtylist.get(which));

                else

                    selectedspecilization.remove(Specialtylist.get(which));



            }

        };

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setTitle("Select Speciality");

        builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                onChangeSelectedColours();
                dialog.dismiss();
            }
        });

        builder.setMultiChoiceItems(Specialtylist.toArray(new String[Specialtylist.size()]), checkedColours, coloursDialogListener);
        AlertDialog dialog = builder.create();
        dialog.show();

    }

    private Request fetchingmedicalcondition() {
        JSONObject postdata = new JSONObject();
        RequestBody body = RequestBody.create(MedicalHitoryp.JSON, postdata.toString());
        return new Request.Builder()
                .addHeader("X-Api-Key", "AB5433GMDF657VBB")
                .addHeader("Content-Type", "application/json")
                .url(ApiUtils.BASE_URL+"listcityandstate")
                .post(body)
                .build();



    }
    private void addingdata()
    {
        OkHttpClient client = new OkHttpClient();
        Request request = fetchingmedicalcondition();

        Log.i("", "onClick: "+request);
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                Log.i("Activity", "onFailure: Fail");
            }
            @Override
            public void onResponse(final Response response) throws IOException {

                final String body=response.body().string();
                // medicalconditionJSON(body);
                Log.i("1234add", "onResponse: "+body);
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONObject jsonObject= new JSONObject(body);
                            JSONArray jsonArray=jsonObject.getJSONArray("State");
                            if(statelist.size()>0)
                                statelist.clear();
                            for (int i = 0; i <jsonArray.length() ; i++) {
                                JSONObject jsonObject1=jsonArray.getJSONObject(i);
                                statelist.add(new doctormsglist(jsonObject1.getInt("id"),jsonObject1.getString("name")));
                            }
                            CustomAdapter_bd customAdapter=new CustomAdapter_bd(getActivity(),statelist);
                            spinner_state_spinner.setAdapter(customAdapter);
                            spinner_state_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                    str_stateid=statelist.get(position).getName();
                                    stateid=statelist.get(position).getId();
                                    if(connectiondetector.isConnectingToInternet())
                                        citylist();
                                    else
                                        Toast.makeText(getActivity(), "No internet!!", Toast.LENGTH_SHORT).show();
                                }

                                @Override
                                public void onNothingSelected(AdapterView<?> parent) {

                                }
                            });
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }

        });

    }
    private Request fetchcity() {
        JSONObject postdata = new JSONObject();
        try {
            postdata.put("state_id",stateid);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(MedicalHitoryp.JSON, postdata.toString());
        return new Request.Builder()
                .addHeader("X-Api-Key", "AB5433GMDF657VBB")
                .addHeader("Content-Type", "application/json")
                .url(ApiUtils.BASE_URL+"listcity")
                .post(body)
                .build();

    }
    private void citylist()
    {
        OkHttpClient client = new OkHttpClient();
        Request request = fetchcity();

        Log.i("", "onClick: "+request);
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                Log.i("Activity", "onFailure: Fail");
            }
            @Override
            public void onResponse(final Response response) throws IOException {

                final String body=response.body().string();
                // medicalconditionJSON(body);
                Log.i("1234add", "onResponse: "+body);
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONObject jsonObject= new JSONObject(body);
                            JSONArray jsonArray=jsonObject.getJSONArray("City");
                            if(citylist.size()>0)
                                citylist.clear();
                            for (int i = 0; i <jsonArray.length() ; i++) {
                                JSONObject jsonObject1=jsonArray.getJSONObject(i);
                                citylist.add(new doctormsglist(jsonObject1.getInt("id"),jsonObject1.getString("name")));
                            }
                            CustomAdapter_bd customAdapter=new CustomAdapter_bd(getActivity(),citylist);
                            spinner_city_spinner.setAdapter(customAdapter);
                            spinner_city_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                    str_cityid=citylist.get(position).getName();
                                }

                                @Override
                                public void onNothingSelected(AdapterView<?> parent) {

                                }
                            });
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }

        });

    }
    class CustomAdapter_bd extends BaseAdapter {
        Context context;
        LayoutInflater inflter;
        List<doctormsglist> medicalconditionlists;
        public CustomAdapter_bd(Context applicationContext, List<doctormsglist> medicalconditionlists) {
            this.context = applicationContext;
            this.medicalconditionlists=medicalconditionlists;
            inflter = (LayoutInflater.from(applicationContext));
        }

        @Override
        public int getCount() {
            return medicalconditionlists.size();
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
            view = inflter.inflate(R.layout.spinner_item, null);
            TextView names = (TextView) view.findViewById(R.id.txt);
            names.setText(medicalconditionlists.get(i).getName());
            return view;
        }


    }



}