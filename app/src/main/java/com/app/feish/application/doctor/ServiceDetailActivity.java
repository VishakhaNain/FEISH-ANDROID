package com.app.feish.application.doctor;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.app.feish.application.Connectiondetector;
import com.app.feish.application.R;
import com.app.feish.application.Remote.ApiUtils;
import com.app.feish.application.ShowImage;
import com.app.feish.application.model.serviceworkinghours;
import com.app.feish.application.modelclassforapi.ContactService;
import com.app.feish.application.modelclassforapi.ServiceData;
import com.app.feish.application.sessiondata.Prefhelper;
import com.darsh.multipleimageselect.activities.AlbumSelectActivity;
import com.darsh.multipleimageselect.helpers.Constants;
import com.darsh.multipleimageselect.models.Image;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.MultipartBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import static com.app.feish.application.doctor.AddWorkingHours.JSON;

public class ServiceDetailActivity extends AppCompatActivity {
    FloatingActionButton floatingActionButton;
    LinearLayout linearLayout_pic;
    Context context=this;
    int code=0;
 ProgressDialog  progressDialog;
    private ArrayList<Image> mImages = new ArrayList<>();
    private static final int REQUEST_WRITE_PERMISSION=102;
    GridView gridView;
    TextView textView,textView_title,textView_add,textView_mob,textView_desc;
    ServiceData serviceData;
    ArrayList<serviceworkinghours> serviceworkinghourslist= new ArrayList<>();
    ArrayList<Integer> dayofweek= new ArrayList<>();
    Connectiondetector connectiondetector;
    ListView listView;
    CustomAdapter_seworkhrs  customAdapter_seworkhrs;
    private static final int PERMISSION_REQUEST_WRITE_EXTERNAL_STORAGE = 218;
    //ImageView uploadImage;
    Bitmap selectimage;
    ProgressDialog pd;
    ArrayList<String> photolist= new ArrayList<>();
    ImageView imageUpload;
    String src="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_detail);
        imageUpload=findViewById(R.id.servicelogo);
        progressDialog= new ProgressDialog(context);
        progressDialog.setTitle("Uploading.....");

        imageUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploaodImage();
            }
        });
        pd= new ProgressDialog(context);
        pd.setTitle("Uploadting");
        pd.setCanceledOnTouchOutside(false);
        floatingActionButton = findViewById(R.id.fab);
        gridView = findViewById(R.id.grid);
        textView = findViewById(R.id.workingh);
        connectiondetector= new Connectiondetector(getApplicationContext());
        listView=findViewById(R.id.list);
        customAdapter_seworkhrs= new CustomAdapter_seworkhrs(context);
        initView();
        serviceData= (ServiceData) getIntent().getSerializableExtra("MyClass");
        if(!serviceData.getLogo().equals("not_available.gif"))
        {
            Picasso.with(context)
                    .load("http://feish.online/img/services/"+serviceData.getLogo())
                    .into(imageUpload);
        }
        else
        {
            imageUpload.setImageResource(R.drawable.doctor_images);
        }
       if(connectiondetector.isConnectingToInternet())
        {
            fetchworkinghr();
            fetchservicepic();
        }
        else
        {
            Toast.makeText(context, "No Internet", Toast.LENGTH_SHORT).show();
        }
           textView_mob.setText(serviceData.getPhone());
          textView_title.setText(serviceData.getTitle());
          textView_add.setText(serviceData.getAddress()+" "+serviceData.getLocality()+" "+serviceData.getCity()+" "+serviceData.getPinCode());
          textView_desc.setText(serviceData.getDescription());



         gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if(photolist.size()>0)
        code=2;
        else
            code=0;

        Intent intent = new Intent(ServiceDetailActivity.this, ShowImage.class);
        intent.putExtra("code",code);
        Bundle args = new Bundle();
        if(code==2)
            args.putSerializable("imagelist",photolist);
        intent.putExtra("BUNDLE",args);
        startActivity(intent);
    }
});

        linearLayout_pic = findViewById(R.id.ll_pic);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(ServiceDetailActivity.this, AddWorkingHours.class);
                intent.putExtra("service_id",serviceData.getId());
                startActivity(intent);
            }
        });
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        linearLayout_pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, AlbumSelectActivity.class);
                intent.putExtra(Constants.INTENT_EXTRA_LIMIT, 6);
                startActivityForResult(intent, Constants.REQUEST_CODE);
            }
        });
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
        {
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED)
            {
                ActivityCompat.requestPermissions(ServiceDetailActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_WRITE_PERMISSION);
            }
        }

    }

    private void initView()
    {
        textView_title=findViewById(R.id.sertitle);
        textView_add=findViewById(R.id.sercadd);
        textView_mob=findViewById(R.id.sermob);
        textView_desc=findViewById(R.id.desc);
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

                        try {
                        JSONArray jsonArray= new JSONArray(body);
                            for (int i = 0; i <jsonArray.length() ; i++) {
                                JSONObject jsonObject=jsonArray.getJSONObject(i);
                                photolist.add(jsonObject.optString("url"));

                            }
                            CustomAdapter customAdapter= new CustomAdapter(1);
                            gridView.setAdapter(customAdapter);

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
            postdata.put("service_id",serviceData.getId());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(JSON, postdata.toString());
        return new Request.Builder()
                .addHeader("X-Api-Key", "AB5433GMDF657VBB")
                .addHeader("Content-Type", "application/json")
                .url(ApiUtils.BASE_URL+"fetchservicephoto")
                .post(body)
                .build();
    }
    public void fetchworkinghr()
    {
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
                final String body=response.body().string();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try{
                            JSONObject jsonObject= new JSONObject(body);
                            if(jsonObject.getInt("Success")==1)
                            {
                                JSONArray jsonArray= jsonObject.getJSONArray("ServicesWorkingTiming");
                                for (int i = 0; i <jsonArray.length() ; i++) {
                                    JSONObject jsonObject1=jsonArray.getJSONObject(i);
                                    serviceworkinghourslist.add(new serviceworkinghours(jsonObject1.getString("open_time"),jsonObject1.getString("close_time"),jsonObject1.getInt("id"),jsonObject1.getInt("day_of_week")));
                                    dayofweek.add(jsonObject1.getInt("day_of_week"));
                                }
                                listView.setAdapter(customAdapter_seworkhrs);
                            }
                            else
                            {
                                Toast.makeText(context, "Error!!", Toast.LENGTH_SHORT).show();
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

    private Request listworkinghr() {
        JSONObject postdata = new JSONObject();
        try {
            postdata.put("service_id",Integer.parseInt(serviceData.getId()));
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
    @SuppressLint("StaticFieldLeak")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, final Intent data) {
        if (resultCode != RESULT_OK) {
            return;
        }
        if (requestCode == Constants.REQUEST_CODE && data != null) {
            new AsyncTask<Void, Void, Void>() {
                @Override
                protected Void doInBackground(Void... params) {
                    mImages = data.getParcelableArrayListExtra(Constants.INTENT_EXTRA_IMAGES);
                    return null;
                }

                @Override
                protected void onPostExecute(Void nothing) {
                    //gridView.setImageURI(Uri.parse(mImages.get(0).path));
                    CustomAdapter customAdapter= new CustomAdapter(0);
                    gridView.setAdapter(customAdapter);
                    if(connectiondetector.isConnectingToInternet()) {
                        progressDialog.show();
                        for (int i = 0; i < mImages.size(); i++) {
                            uploadphoto(mImages.get(i));
                        }
                        progressDialog.dismiss();
                    }
                    else
                    {
                        Toast.makeText(context, "No Internet!!", Toast.LENGTH_SHORT).show();
                    }


                }
            }.execute();
        }
        else
            {
            if (requestCode == 1) {
                selectimage = (Bitmap) data.getExtras().get("data");
                Uri selectedImageURI = getImageUri(getApplicationContext(), selectimage);
                InputStream imageStream = null;
                try {
                    imageStream = ServiceDetailActivity.this.getContentResolver().openInputStream(selectedImageURI);
                    selectimage = BitmapFactory.decodeStream(imageStream);
                    src=getPath(selectedImageURI);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                if (!(selectimage == null)) {
                    imageUpload.setImageBitmap(selectimage);
                    apiCall(selectimage, selectedImageURI);
                }
            } else if (requestCode == 2) {
                Uri selectedImageURI = data.getData();
                InputStream imageStream ;
                try {
                    imageStream = ServiceDetailActivity.this.getContentResolver().openInputStream(selectedImageURI);
                    selectimage = BitmapFactory.decodeStream(imageStream);
                    src=getPath(selectedImageURI);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                if (selectimage != null) {
                    imageUpload.setImageBitmap(selectimage);
                    apiCall(selectimage, selectedImageURI);

                }
            }
        }
    }
    
    /////for logo upate//////////////
    private void uploaodImage() {

        checkPermission();
        askForPermission(Manifest.permission.CAMERA, 1);
        addLogo();
    }

    AlertDialog coupandialog;

    private void addLogo() {
        final CharSequence[] options = {/*"Take Photo",*/
                "Choose From Gallery", "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(ServiceDetailActivity.this);
        builder.setTitle("Add Photo");

        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
             /*   if (options[item].equals("Take Photo")) {
                    Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(cameraIntent, 1);
                } else */if (options[item].equals("Choose From Gallery")) {
                    Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                    photoPickerIntent.setType("image/*");
                    startActivityForResult(photoPickerIntent, 2);
                } else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                }

            }
        });
        coupandialog = builder.create();
        coupandialog.show();
    }

    public String getPath(Uri uri) {

        String path;
        String[] projection = { MediaStore.Files.FileColumns.DATA };
        Cursor cursor = ServiceDetailActivity.this.getContentResolver().query(uri, projection, null, null, null);

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
    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }
    private void uploadphoto(Image image)
    {
        File sourceFile = new File(image.path);
        Log.d("", "File...::::" + sourceFile + " : " + sourceFile.exists());


        RequestBody requestBody = new MultipartBuilder()
                .type(MultipartBuilder.FORM)
                .addFormDataPart("service_id",serviceData.getId())
                .addFormDataPart("doctor_id", Prefhelper.getInstance(context).getUserid())
                .addFormDataPart("profile_pic", "profile.png", RequestBody.create(MediaType.parse("image/*"), sourceFile))
                .build();

        Request request = new Request.Builder()
                .addHeader("X-Api-Key", "AB5433GMDF657VBB")
                .addHeader("Content-Type", "application/json")
                .url("http://feish.online/apis/uplodeservicePic")
                .post(requestBody)
                .build();

        OkHttpClient client = new OkHttpClient();
        client.newCall(request).enqueue(new Callback() {

            @Override
            public void onFailure(Request request, final  IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(ServiceDetailActivity.this, "Failure"+e.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                });
            }

            @Override
            public void onResponse(final Response response) throws IOException {
                final String body=response.body().string();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                       // Toast.makeText(context, "", Toast.LENGTH_SHORT).show();


                    }
                });
            }
        });


    }

    private void apiCall(Bitmap selectimage, Uri entity) {
      //  pd.show();
        OkHttpClient client = new OkHttpClient();
        Request validation_request = profilePicrequest(entity);
        client.newCall(validation_request).enqueue(new Callback() {

            @Override
            public void onFailure(Request request, IOException e) {
                Toast.makeText(ServiceDetailActivity.this, "Failure", Toast.LENGTH_SHORT).show();
                pd.dismiss();
            }

            @Override
            public void onResponse(final Response response) throws IOException {
                final String body=response.body().string();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                       //     Toast.makeText(context, ""+body, Toast.LENGTH_SHORT).show();
                        try
                        {
                            JSONObject jsonObject= new JSONObject(body);

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
                }
            })

            .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            })

            // .setIcon(android.R.drawable.m)

            .show();
    builder.setCancelable(false);
}

                        }
                        catch (JSONException e)
                        {

                        }

                    }
                });
            }
        });
    }
    private void askForPermission(String permission, Integer requestCode) {
        if (ContextCompat.checkSelfPermission(ServiceDetailActivity.this, permission) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(ServiceDetailActivity.this, permission)) {
                ActivityCompat.requestPermissions(ServiceDetailActivity.this, new String[]{permission}, requestCode);
            } else {
                ActivityCompat.requestPermissions(ServiceDetailActivity.this, new String[]{permission}, requestCode);
            }
        }
    }
    public boolean profileresponse(String response) {
        Gson gson = new GsonBuilder().create();
        ContactService profileResponse = gson.fromJson(response,ContactService.class);
        return profileResponse.getStatus();
    }
    private Request profilePicrequest(Uri entity) {
        File sourceFile = new File(src);
        Log.d("", "File...::::" + sourceFile + " : " + sourceFile.exists());

        final MediaType MEDIA_TYPE = getRealPathFromUri(ServiceDetailActivity.this, entity).endsWith("png") ?
                MediaType.parse("image/png") : MediaType.parse("image/jpeg");


        RequestBody requestBody = new MultipartBuilder()
                .type(MultipartBuilder.FORM)
                .addFormDataPart("service_id",serviceData.getId())
                .addFormDataPart("profile_pic", "profile.png", RequestBody.create(MEDIA_TYPE, sourceFile))
                .build();

        Request request = new Request.Builder()
                .addHeader("X-Api-Key", "AB5433GMDF657VBB")
                .addHeader("Content-Type", "application/json")
                .url("http://feish.online/apis/uploadServicelogo")
                .post(requestBody)
                .build();

        return request;
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

    private void checkPermission() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (ContextCompat.checkSelfPermission(ServiceDetailActivity.this,
                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(ServiceDetailActivity.this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        PERMISSION_REQUEST_WRITE_EXTERNAL_STORAGE);
            }
        }
    }
    
    
    
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_WRITE_PERMISSION: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                }
            }
        }
    }
    public class CustomAdapter extends BaseAdapter {
        LayoutInflater inflter;
        /*public CustomAdapter(Context applicationContext, int[] logos) {
          *//*  this.context = applicationContext;
            this.logos = logos;*//*

        }*/
        int code;
        public CustomAdapter(int code)
        {
               inflter = (LayoutInflater.from(context));
               this.code=code;
        }
        @Override
        public int getCount() {
            if(code==0)
            return mImages.size();
            else
             return    photolist.size();
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
          /*  ImageView imageView= new ImageView(context);
            imageView.setImageURI(Uri.parse(mImages.get(i).path));
            imageView.setMaxWidth(50);
            imageView.setMaxHeight(50);
           // imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            imageView.setPadding(2,2,2,2);*/
            view = inflter.inflate(R.layout.imageviewlayout, null); // inflate the layout
            ImageView icon = (ImageView) view.findViewById(R.id.img); // get the reference of ImageView
            if(code==0)
            icon.setImageURI(Uri.parse(mImages.get(i).path)); // set logo images
            else
            {
                Picasso.with(context)
                        .load("http://feish.online/img/services/"+photolist.get(i))
                        .into(icon);
            }

              return view;
        }
    }


    class CustomAdapter_seworkhrs extends BaseAdapter{

        Context context;
        private  LayoutInflater inflater=null;
        String day[]={"Monday","Tuesday","Wednesday","Thusday","Friday","Saturday","Sunday"};
        private CustomAdapter_seworkhrs(Context context) {
            // TODO Auto-generated constructor stub
           this.context=context;
            inflater = ( LayoutInflater )context.
                    getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }
        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return serviceworkinghourslist.size();
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
            TextView tv_dayofweek,tv_time;
        }
        @SuppressLint("ViewHolder")
        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            // TODO Auto-generated method stub
            Holder holder=new Holder();
            View rowView;
            rowView = inflater.inflate(R.layout.listworkhrservice, null);
            holder.tv_dayofweek=rowView.findViewById(R.id.dayofweek);
            holder.tv_time= rowView.findViewById(R.id.time);
            holder.tv_dayofweek.setText(day[serviceworkinghourslist.get(position).getDay_of_week()]);
            holder.tv_time.setText(String.format("%s - %s", serviceworkinghourslist.get(position).getOpen_time(), serviceworkinghourslist.get(position).getClose_time()));
            return rowView;
        }

    }

}
