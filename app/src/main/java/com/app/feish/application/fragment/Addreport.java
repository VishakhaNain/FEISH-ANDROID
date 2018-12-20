package com.app.feish.application.fragment;

import android.Manifest;
import android.app.Dialog;
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
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.app.feish.application.Connectiondetector;
import com.app.feish.application.DialogActivity;
import com.app.feish.application.R;
import com.app.feish.application.Remote.ApiUtils;
import com.app.feish.application.Remote.EncryptionDecryption;
import com.app.feish.application.model.doctormsglist;
import com.app.feish.application.sessiondata.Prefhelper;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.MultipartBuilder;
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

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import static android.app.Activity.RESULT_OK;
import static com.app.feish.application.Patient.MedicalHitoryp.JSON;

/**
 * Created by lenovo on 6/4/2016.
 */
public class Addreport extends Fragment {
    // Store instance variables
    AlertDialog coupandialog;
    View view1;
    EditText et_desc,et_ov;
  Connectiondetector connectiondetector;
    String src="";
    String userid="";
    private static final int PERMISSION_REQUEST_WRITE_EXTERNAL_STORAGE = 218;
  Spinner spinner_testlist;
  RelativeLayout relativeLayout;
  EditText editText,et_testdate;
    CalendarPickerView calendar1;
    SimpleDateFormat DATE_FORMAT;
    ImageView imageView,imageView_remove;
    Button btn;
    String str_testid="",str_date="",str_observedvalue="",str_desc="",str_reportpic="";
    Uri uri;
    Button btn_savereport;
    ArrayList<doctormsglist> doctormsglists= new ArrayList<>();
   
    public static Addreport newInstance(int page, String title) {
        Addreport fragmentFirst = new Addreport();
        Bundle args = new Bundle();
        args.putInt("someInt", page);
        args.putString("Stringlist", title);
        fragmentFirst.setArguments(args);
        return fragmentFirst;
    }

    // Store instance variables based on arguments passed
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
              userid=getArguments().getString("Stringlist");
      }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        checkPermission();
        fecthingtestlist();
        connectiondetector= new Connectiondetector(getActivity());
        imageView=view1.findViewById(R.id.lab_reportfile);
        et_desc=view1.findViewById(R.id.lab_desc);
        et_ov=view1.findViewById(R.id.lab_ov);
        btn_savereport=view1.findViewById(R.id.btn_savereport);
        spinner_testlist=view1.findViewById(R.id.testlist_spinner);
        imageView_remove=view1.findViewById(R.id.img_remove);
        relativeLayout=view1.findViewById(R.id.rl_report);
        editText=view1.findViewById(R.id.uploadreport);
        et_testdate=view1.findViewById(R.id.testdate);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(), DialogActivity.class);
                intent.putExtra("image",uri.toString());
                startActivity(intent);
            }
        });


        DATE_FORMAT = new SimpleDateFormat("yyyy/MM/dd");
        editText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final CharSequence[] options = {"Take Photo","Choose From Gallery", "Cancel"};
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
        imageView_remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageView.setImageBitmap(null);
                relativeLayout.setVisibility(View.GONE);
                editText.setText("");
            }
        });
        et_testdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              pickdate();
            }
        });
        btn_savereport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                str_desc=et_desc.getText().toString();
                str_observedvalue=et_ov.getText().toString();
                if(connectiondetector.isConnectingToInternet()) {
                    if (validatereport())
                    {
                        if (src.equals("")) {
                            finalsaveme();
                         //   finalsavemeMDB();
                        } else {
                            OkHttpClient client = new OkHttpClient();
                            Request validation_request = profilePicrequest();
                            client.newCall(validation_request).enqueue(new Callback() {

                                @Override
                                public void onFailure(Request request, final IOException e) {
                                    getActivity().runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(getActivity(), "Failure" + e, Toast.LENGTH_SHORT).show();
                                        }
                                    });

                                }

                                @Override
                                public void onResponse(final Response response) throws IOException {

                                    //    final boolean isSuccessful = profilePicresponse(response.body().string());
                                    final String body = response.body().string();
                                    getActivity().runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            try {
                                                JSONObject jsonObject = new JSONObject(body);
                                                if (jsonObject.getInt("Success") == 1) {
                                                    str_reportpic = jsonObject.getString("message");
                                                    finalsaveme();
                                             //       finalsavemeMDB();
                                                } else {
                                                    Toast.makeText(getActivity(), "not Success" + jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                                                }
                                            } catch (JSONException e) {
                                                Toast.makeText(getActivity(), "" + e, Toast.LENGTH_SHORT).show();
                                                e.printStackTrace();
                                            }

                                        }
                                    });
                                }
                            });
                        }
                    }
                }
                else
                {
                    Toast.makeText(getActivity(), "No Internet!!", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub

        switch (requestCode) {

            case 7:

                if (resultCode == RESULT_OK) {
                     relativeLayout.setVisibility(View.VISIBLE);
                     uri = data.getData();
                    imageView.setImageURI(uri);
                     src = getPath(uri);
                    editText.setText(src);
                    imageView_remove.setVisibility(View.VISIBLE);

                }
                else
                {
                    Toast.makeText(getActivity(), "No Picture Taken", Toast.LENGTH_SHORT).show();
                }
                break;
            case 1:
                if (resultCode == RESULT_OK) {
              Bitmap  selectimage = (Bitmap) data.getExtras().get("data");
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                  selectimage.compress(Bitmap.CompressFormat.JPEG, 80, bytes);
               uri=getImageUri(getActivity(),selectimage);
                imageView.setImageURI(uri);
                 src = getPath(uri);
                editText.setText(src);
                relativeLayout.setVisibility(View.VISIBLE);
                imageView_remove.setVisibility(View.VISIBLE);
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


    // Inflate the view for the fragment based on layout XML
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
         view1 = inflater.inflate(R.layout.fragment_report, container, false);

   //     getActivity().setTitle("CollegeName");


        return view1;
    }
    public void hideSoftKeyboard() {
        if (getActivity().getCurrentFocus() != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getContext().getSystemService(getContext().INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
        }
    }
    void pickdate()
    {
        final Dialog dialog1 = new Dialog(getActivity());
        dialog1.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog1.setContentView(R.layout.camgal);
        dialog1.setCanceledOnTouchOutside(false);
        final Calendar nextYear=Calendar.getInstance();
        nextYear.add(Calendar.YEAR,1);
        final  Calendar lastYear=Calendar.getInstance();
        lastYear.add(Calendar.YEAR,-1);
        btn=(Button)dialog1.findViewById(R.id.ok);
        calendar1 = (CalendarPickerView) dialog1.findViewById(R.id.calendar_view);
        calendar1.init(lastYear.getTime(), nextYear.getTime()) //
                .inMode(CalendarPickerView.SelectionMode.SINGLE) //
                .withSelectedDate(new Date());
        calendar1.setCustomDayView(new DefaultDayViewAdapter());
        Date today =new Date();
        calendar1.setDecorators(Collections.<CalendarCellDecorator>emptyList());
        calendar1.init(today,nextYear.getTime())
                .withSelectedDate(today).inMode(CalendarPickerView.SelectionMode.SINGLE);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayList<Date> mydates = (ArrayList<Date>)calendar1.getSelectedDates();
                for (int i = 0; i <mydates.size() ; i++) {
                    Date tempdate = mydates.get(i);
                    String testdate = DATE_FORMAT.format(tempdate);
                    str_date=testdate;
                    et_testdate.setText(testdate);
                    dialog1.dismiss();

                }

            }
        });
        dialog1.show();

    }
    private void fecthingtestlist()
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
                //Log.i("1234add", "onResponse: "+body);

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONObject jsonObject= new JSONObject(body);
                            JSONArray jsonArray=jsonObject.getJSONArray("data");
                            for (int i = 0; i <jsonArray.length() ; i++)
                            {
                                JSONObject  jsonObject1= jsonArray.getJSONObject(i);
                                doctormsglists.add(new doctormsglist(jsonObject1.getInt("id"),jsonObject1.getString("test_name")));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        CustomAdapter_report customAdapter=new CustomAdapter_report(getActivity(),doctormsglists);
                        spinner_testlist.setAdapter(customAdapter);
                        spinner_testlist.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                str_testid= String.valueOf(doctormsglists.get(position).getId());

                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {

                            }
                        });
                    }
                });


            }

        });

    }

    private Request fetchingmedicalcondition() {
        JSONObject postdata = new JSONObject();
        RequestBody body = RequestBody.create(JSON, postdata.toString());
        return new Request.Builder()
                .addHeader("X-Api-Key", "AB5433GMDF657VBB")
                .addHeader("Content-Type", "application/json")
                .url(ApiUtils.BASE_URL+"listTest")
                .post(body)
                .build();

    }
    private Request profilePicrequest() {
        File sourceFile = new File(src.toString());
        /*if(sourceFile.exists())
        {
            Toast.makeText(getActivity(), "exist", Toast.LENGTH_SHORT).show();
        }
        else
        {
            Toast.makeText(getActivity(), " not exist", Toast.LENGTH_SHORT).show();
        }*/

        Log.d("", "File...::::" + sourceFile + " : " + sourceFile.exists());

        final MediaType MEDIA_TYPE = src.toString().endsWith("png") ?
                MediaType.parse("image/png") : MediaType.parse("image/jpeg");
        RequestBody requestBody = new MultipartBuilder()
                .type(MultipartBuilder.FORM)
                .addFormDataPart("report", "report.png", RequestBody.create(MEDIA_TYPE, sourceFile))
                .build();

        Request request = new Request.Builder()
                .addHeader("X-Api-Key", "AB5433GMDF657VBB")
                .addHeader("Content-Type", "application/json")
                .url("http://feish.online/apis/addLabResult")
                .post(requestBody)
                .build();

        return request;
    }
    private Request Finalsave() {
        JSONObject postdata = new JSONObject();

        try {
            postdata.put("user_id", userid);
            postdata.put("test_id",str_testid);
            postdata.put("test_date",str_date);
            postdata.put("description",str_desc);
            postdata.put("observed_value",str_observedvalue);
            postdata.put("report",str_reportpic);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestBody body = RequestBody.create(JSON,postdata.toString());

        final Request request = new Request.Builder()
                .addHeader("X-Api-Key","AB5433GMDF657VBB")
                .addHeader("Content-Type", "application/json")
                .url("http://feish.online/apis/updateLabResult")
                .post(body)
                .build();
        return request;
    }
    public void finalsaveme()
    {
        OkHttpClient client = new OkHttpClient();
        Request validation_request = Finalsave();
        client.newCall(validation_request).enqueue(new Callback() {

            @Override
            public void onFailure(Request request,final IOException e) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getActivity(), "Failure"+e, Toast.LENGTH_SHORT).show();
                    }
                });

            }

            @Override
            public void onResponse(final Response response) throws IOException {

                //    final boolean isSuccessful = profilePicresponse(response.body().string());
                final String body=response.body().string();
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
                            else
                            {
                                Toast.makeText(getActivity(), jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                });
            }
        });
    }

    /*private Request FinalsaveMDB() {
                JSONObject postdatauser = new JSONObject();

        String encrypttestdate= EncryptionDecryption.encode(str_date);

        try {
         *//*   JSONObject postdatauser = new JSONObject();
            JSONObject postdatavalue = new JSONObject();
            JSONArray jsonArrayrecord = new JSONArray();
            JSONObject postdata = new JSONObject();*//*
            postdatauser.put("user_id", userid);
            postdatauser.put("test_id",str_testid);
            postdatauser.put("test_date",str_date);
            postdatauser.put("description",str_desc);
            postdatauser.put("observed_value",str_observedvalue);
            postdatauser.put("report",str_reportpic);

            postdatauser.put("modified_by", Prefhelper.getInstance(getActivity()).getUserid());
            postdatauser.put("modified_at",new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(new java.util.Date()));
            postdatauser.put("source_type","mobile");
            postdatauser.put("deleted_flag","0");*/


        /*    postdata.put("LabReport", postdatauser);

            postdatavalue.put("value", postdata);

            jsonArrayrecord.put(postdatavalue);

            postdatamain.put("record", jsonArrayrecord);
            Log.d("register data", postdatamain.toString());*/

        /*} catch (JSONException e) {
            e.printStackTrace();
        }
        //Log.d("medicaldata",postdatamain.toString());

        RequestBody body = RequestBody.create(JSON,postdatauser.toString());

        final Request request = new Request.Builder()
             *//*   .addHeader("X-Api-Key","AB5433GMDF657VBB")
                .addHeader("Content-Type", "application/json")*//*
                .url(ApiUtils.BASE_URLMAngoDB+"add/labresult")
                .post(body)
                .build();
        return request;
    }
    public void finalsavemeMDB()
    {
        OkHttpClient client = new OkHttpClient();
        Request validation_request = FinalsaveMDB();
        client.newCall(validation_request).enqueue(new Callback() {

            @Override
            public void onFailure(Request request,final IOException e) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getActivity(), "Failure"+e, Toast.LENGTH_SHORT).show();
                    }
                });

            }

            @Override
            public void onResponse(final Response response) throws IOException {

                //    final boolean isSuccessful = profilePicresponse(response.body().string());
                final String body=response.body().string();
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getActivity(), ""+body, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }*/

    private void checkPermission() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (ContextCompat.checkSelfPermission(getActivity(),
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        PERMISSION_REQUEST_WRITE_EXTERNAL_STORAGE);
            }
        }
    }
    private boolean validatereport(){
        if(str_date.compareTo("")==0)
        {
            Toast.makeText(getActivity(),"Date field empty",Toast.LENGTH_LONG).show();
            return false;
        }
        else if (str_desc.compareTo("")==0)
        {
            Toast.makeText(getActivity(),"Description field empty",Toast.LENGTH_LONG).show();
            return false;
        }
        if(str_observedvalue.compareTo("")==0)
        {
            Toast.makeText(getActivity(),"Observed Value field empty",Toast.LENGTH_LONG).show();
            return false;
        }
       if(str_testid.compareTo("")==0)
        {
            Toast.makeText(getActivity(),"test field empty",Toast.LENGTH_LONG).show();
            return false;
        }

        else {
            return true;
        }
    }
}
class CustomAdapter_report extends BaseAdapter {
    Context context;
    LayoutInflater inflter;
    List<doctormsglist> medicalconditionlists;
    public CustomAdapter_report(Context applicationContext, List<doctormsglist> medicalconditionlists) {
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