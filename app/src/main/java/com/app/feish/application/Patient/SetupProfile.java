package com.app.feish.application.Patient;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.app.feish.application.Connectiondetector;
import com.app.feish.application.R;
import com.app.feish.application.Remote.ApiUtils;
import com.app.feish.application.Remote.EncryptionDecryption;
import com.app.feish.application.modelclassforapi.ContactService;
import com.app.feish.application.sessiondata.Prefhelper;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by This Pc on 3/8/2018.
 */

public class SetupProfile extends AppCompatActivity {
    ProgressDialog pd;
    List<String> occupationlists= new ArrayList<>();
    List<String> Ethnicitylists= new ArrayList<>();
    List<String> Identitytypelists= new ArrayList<>();
    ImageView img_back;
    CircleImageView  imageUpload;
    private EditText identityno,mobile, firstname, lastname, email, address, height, weight;
    private int gender = 1, marrital = 1, occupation_val, ethinicity_val, bloodgroup_val, identitytype_val;
    private TextView bmi;
    private RadioButton radioButtonMale, radioButtonFemale, radioButtonMarried, radioButtonUnmarried;
    private Spinner ethinicity, bloodgroup, occupation, identitytype;
    private Button submit;
    Bundle bundle;
    String src,str_address="";
    Connectiondetector connectiondetector;
    public static final MediaType JSON = MediaType.parse("application/json:charset=utf-8");
    private static final int PERMISSION_REQUEST_WRITE_EXTERNAL_STORAGE = 218;
    AlertDialog coupandialog;
    Context context=this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.newsetupprofileactivity);
        initViews();
        ClickViews();
        receiveSetvalues();
        fetchoccupation();

    }
    private void initViews()
    {

        pd = new ProgressDialog(SetupProfile.this);
        pd.setMessage("Loading");
        pd.setCancelable(false);
        connectiondetector=new Connectiondetector(getApplicationContext());
        firstname = findViewById(R.id.setupfirstname);
        lastname =findViewById(R.id.setuplastname);
        email =findViewById(R.id.setupemail);
        mobile =findViewById(R.id.setupmobile);

        imageUpload =findViewById(R.id.uploadpImageID);
        submit =  findViewById(R.id.submitprofile);
        address =findViewById(R.id.setupaddress);
        identityno = findViewById(R.id.identityno);
        height =findViewById(R.id.setupheight);
        weight =findViewById(R.id.setupweight);
        bmi =  findViewById(R.id.setupbmi);
        img_back=findViewById(R.id.img_back);
        bloodgroup =  findViewById(R.id.setupbloodgroup);

        radioButtonMale = findViewById(R.id.radioMale);
        radioButtonFemale =findViewById(R.id.radioFemale);

        radioButtonMarried =findViewById(R.id.marriedid);
        radioButtonUnmarried =findViewById(R.id.unmarriedid);

        occupation =findViewById(R.id.setupoccupation);
        ethinicity =findViewById(R.id.setupethnicity);
        identitytype = findViewById(R.id.setupidentityproof);

    }
    private void ClickViews()
    {

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (radioButtonMale.isChecked()) {
                    gender = 1;
                } else if (radioButtonFemale.isChecked()) {
                    gender = 2;
                }

                if (radioButtonMarried.isChecked()) {
                    marrital = 1;
                } else if (radioButtonUnmarried.isChecked()) {
                    marrital = 2;
                }
                if(connectiondetector.isConnectingToInternet())
                {


                     fetchrecord();
                   // fetchrecordMDB();
                }
                else
                {
                    Toast.makeText(SetupProfile.this, "No Internet!!", Toast.LENGTH_SHORT).show();
                }
            }
        });
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        occupation.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                        occupation_val = pos + 1;
                    }
                    public void onNothingSelected(AdapterView<?> parent) {
                        occupation_val = 1;
                    }
                });

        bloodgroup.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                        bloodgroup_val = pos + 1;
                    }
                    public void onNothingSelected(AdapterView<?> parent) {
                        bloodgroup_val = 1;
                    }
                });


        ethinicity.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                        ethinicity_val = pos + 1;
                    }
                    public void onNothingSelected(AdapterView<?> parent) {
                        ethinicity_val = 1;
                    }
                });



        identitytype.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                        identitytype_val = pos + 1;
                        //Log.i(TAG, "onItemSelected: "+pos);
                        //Log.i(TAG, "onItemSelected: "+id);
                    }
                    public void onNothingSelected(AdapterView<?> parent) {
                        identitytype_val = 1;
                    }
                });

        imageUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                uploaodImage();
            }
        });

    }
   private void receiveSetvalues()
   {
        bundle = getIntent().getExtras();
       if(bundle!=null)
       {
       firstname.setText(bundle.getString("firstname"));
       lastname.setText(bundle.getString("lastname"));
       email.setText(bundle.getString("email"));
       mobile.setText(bundle.getString("mobile"));
       if(bundle.getString("pic").equals(""))
       {
           imageUpload.setImageResource(R.drawable.patienr);
       }
       else {
           Picasso.with(SetupProfile.this)
                   .load(bundle.getString("pic"))
                   .into(imageUpload);
       }

       bloodgroup.setSelection((Integer.parseInt(bundle.getString("bloodgroup")))-1);

       gender=Integer.parseInt(bundle.getString("gen"));
       marrital=Integer.parseInt(bundle.getString("m_status"));

       if(gender==1)
           radioButtonMale.setChecked(true);
       else
           radioButtonFemale.setChecked(true);

       if(marrital==1)
           radioButtonMarried.setChecked(true);
       else
           radioButtonUnmarried.setChecked(true);

       str_address=bundle.getString("address");
       if(str_address!=null&&!str_address.equals(""))
           address.setText(bundle.getString("address"));
       }

       if(!bundle.getString("IdentityId").equals("")) {
           identityno.setText(bundle.getString("IdentityId"));
           String decryptidentity= EncryptionDecryption.decode(bundle.getString("IdentityId"));
           Toast.makeText(SetupProfile.this, ""+decryptidentity, Toast.LENGTH_SHORT).show();
           identityno.setText(decryptidentity);
       }

       if(!bundle.getString("Height").equals(""))
           height.setText(bundle.getString("Height"));
       else
           height.setText("");
       if(!bundle.getString("Weight").equals(""))
           weight.setText(bundle.getString("Weight"));
       else
           weight.setText("");

       if (!weight.getText().toString().equals("") && !height.getText().toString().equals(""))
       {
           double wgt = Double.parseDouble(weight.getText().toString());
           double hgt = Double.parseDouble(height.getText().toString());
           double hgtsq = Math.pow(hgt, 2);
           bmi.setText(String.valueOf(wgt / hgtsq));
       }

   }

    private void uploaodImage() {

        checkPermission();
      //  askForPermission(Manifest.permission.CAMERA, CAMERA);
        addLogo();
    }




    private void addLogo() {
        final CharSequence[] options = {/*"Take Photo"*/
                "Choose From Gallery", "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(SetupProfile.this);
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

    Bitmap selectimage;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == 1) {

                selectimage = (Bitmap) data.getExtras().get("data");

                Uri selectedImageURI = getImageUri(getApplicationContext(), selectimage);
                src=getPath(selectedImageURI);
                InputStream imageStream;
                try {
                    imageStream = SetupProfile.this.getContentResolver().openInputStream(selectedImageURI);
                    selectimage = BitmapFactory.decodeStream(imageStream);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                if (!(selectimage == null)) {
                    imageUpload.setImageBitmap(selectimage);
                   // byte[] bytedata = null;
                    apiCall(selectedImageURI);

                }
            } else if (requestCode == 2) {
                Uri selectedImageURI = data.getData();
                src=getPath(selectedImageURI);
                InputStream imageStream;
                try {
                    assert selectedImageURI != null;
                    imageStream = SetupProfile.this.getContentResolver().openInputStream(selectedImageURI);
                    selectimage = BitmapFactory.decodeStream(imageStream);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                if (selectimage != null) {
                    imageUpload.setImageBitmap(selectimage);
                    apiCall(selectedImageURI);

                }
            }
        }
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }
    private void apiCall(Uri entity) {
        pd.show();
        OkHttpClient client = new OkHttpClient();
        Request validation_request = profilePicrequest(entity);
        client.newCall(validation_request).enqueue(new Callback() {

            @Override
            public void onFailure(Request request, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(SetupProfile.this, "Failure", Toast.LENGTH_SHORT).show();
                        pd.dismiss();
                    }
                });

            }

            @Override
            public void onResponse(final Response response) throws IOException {

                final boolean isSuccessful = profilePicresponse(response.body().string());
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        pd.dismiss();

                        if (isSuccessful) {
//                            double wgt=Double.parseDouble(weight.getText().toString());
//                            double hgt=Double.parseDouble(height.getText().toString());
//                            double hgtsq=Math.pow(hgt,2);
//                            bmi.setText(Double.toString(wgt/hgtsq));
                            Toast.makeText(getApplicationContext(), "Update successful", Toast.LENGTH_LONG).show();

//                            Intent intent=new Intent(SetupProfile.this,ViewProfilePatient.class);
//                            //intent.putExtra("userid",userid);
//                            startActivity(intent);
//                            finish();
                        } else {
                            Toast.makeText(getApplicationContext(), "OOPS!!", Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });
    }

   /* private void askForPermission(String permission, Integer requestCode) {
        if (ContextCompat.checkSelfPermission(SetupProfile.this, permission) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(SetupProfile.this, permission)) {
                ActivityCompat.requestPermissions(SetupProfile.this, new String[]{permission}, requestCode);
            } else {
                ActivityCompat.requestPermissions(SetupProfile.this, new String[]{permission}, requestCode);
            }
        }
    }
*/
    private Request profile_request() {
        JSONObject postdata = new JSONObject();
        String encryptphone= EncryptionDecryption.encode(mobile.getText().toString());
        String encryptidentity= EncryptionDecryption.encode(identityno.getText().toString());

        try {
            postdata.put("user_id", Prefhelper.getInstance(SetupProfile.this).getUserid());
            postdata.put("weight", weight.getText().toString());
            postdata.put("height", height.getText().toString());
            postdata.put("gender", Integer.toString(gender));
            postdata.put("marital_status", Integer.toString(marrital));
            postdata.put("address", address.getText().toString());
            postdata.put("first_name", firstname.getText().toString());
            postdata.put("last_name", lastname.getText().toString());
            postdata.put("email", email.getText().toString());
            postdata.put("mobile", encryptphone);
            postdata.put("occupation_id", Integer.toString(occupation_val));
            postdata.put("ethnicity_id", Integer.toString(ethinicity_val));
            postdata.put("blood_group", Integer.toString(bloodgroup_val));
            postdata.put("identity_type", Integer.toString(identitytype_val));
            postdata.put("identity_id", encryptidentity);
            //postdata.put("height",height.getText().toString());
            // postdata.put("address",address.getText().toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d("profile",postdata.toString());
        RequestBody body = RequestBody.create(JSON, postdata.toString());
        return new Request.Builder()
                .addHeader("X-Api-Key", "AB5433GMDF657VBB")
                .addHeader("Content-Type", "application/json")
                .url("http://feish.online/apis/set_up_profile")
                .post(body)
                .build();
    }
    private void fetchrecord()
    {

        OkHttpClient client = new OkHttpClient();
        Request validation_request = profile_request();
        client.newCall(validation_request).enqueue(new Callback() {

            @Override
            public void onFailure(Request request, IOException e) {

                // Toast.makeText(getApplicationContext(),"Fail",Toast.LENGTH_LONG).show();
                Log.i("Activity", "onFailure: Fail");
            }

            @Override
            public void onResponse(final Response response) throws IOException {

                final boolean isSuccessful = profileresponse(response.body().string());
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (isSuccessful) {
                            if (!weight.getText().toString().equals("") && !height.getText().toString().equals(""))
                            {
                                double wgt = Double.parseDouble(weight.getText().toString());
                                double hgt = Double.parseDouble(height.getText().toString());
                                double hgtsq = Math.pow(hgt, 2);
                                bmi.setText(String.valueOf(wgt / hgtsq));
                            }
                            Toast.makeText(getApplicationContext(), "Update successful", Toast.LENGTH_LONG).show();

                            Intent intent = new Intent(SetupProfile.this, HealthParameter.class);
                            //intent.putExtra("userid",userid);
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(getApplicationContext(), "OOPS!!", Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });
    }

    private Request profile_requestMDB() {

        JSONObject postdatauser = new JSONObject();
       /* JSONObject postdatavalue = new JSONObject();
        JSONArray  jsonArrayrecord= new JSONArray();
      */  JSONObject postdata = new JSONObject();
      //  JSONObject postdatamain = new JSONObject();
        String encryptmobile=EncryptionDecryption.encode(mobile.getText().toString());
        String encryptemail=EncryptionDecryption.encode(email.getText().toString());
        String encryptidentity=EncryptionDecryption.encode(identityno.getText().toString());


        try {
            postdatauser.put("user_id", Prefhelper.getInstance(SetupProfile.this).getUserid());
            postdatauser.put("weight", weight.getText().toString());
            postdatauser.put("height", height.getText().toString());
            postdatauser.put("gender", Integer.toString(gender));
            postdatauser.put("marital_status", Integer.toString(marrital));
            postdatauser.put("address", address.getText().toString());
            postdatauser.put("first_name", firstname.getText().toString());
            postdatauser.put("last_name", lastname.getText().toString());
            postdatauser.put("email", encryptemail);
            postdatauser.put("mobile", encryptmobile);
            postdatauser.put("occupation_id", Integer.toString(occupation_val));
            postdatauser.put("ethnicity_id", Integer.toString(ethinicity_val));
            postdatauser.put("blood_group", Integer.toString(bloodgroup_val));
            postdatauser.put("identity_type", Integer.toString(identitytype_val));
            postdatauser.put("identity_id", encryptidentity);

            postdatauser.put("modified_by",Prefhelper.getInstance(context).getUserid());
            postdatauser.put("modified_at",new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(new java.util.Date()));
            postdatauser.put("source_type","mobile");
            postdatauser.put("deleted_flag","0");



            postdata.put("Patient",postdatauser);

          //  postdatavalue.put("value",postdata);

           // jsonArrayrecord.put(postdatavalue);

            //postdatamain.put("record",jsonArrayrecord);
        } catch(JSONException e){
            e.printStackTrace();
        }
        Log.d("register data",postdata.toString());

        RequestBody body = RequestBody.create(JSON, postdata.toString());
        return new Request.Builder()
                /*.addHeader("X-Api-Key", "AB5433GMDF657VBB")
                .addHeader("Content-Type", "application/json")*/
                .url(ApiUtils.BASE_URLMAngoDB+"update/newpatient")
                .post(body)
                .build();
    }
    private void fetchrecordMDB()
    {

        OkHttpClient client = new OkHttpClient();
        Request validation_request = profile_requestMDB();
        client.newCall(validation_request).enqueue(new Callback() {

            @Override
            public void onFailure(Request request, IOException e) {

                // Toast.makeText(getApplicationContext(),"Fail",Toast.LENGTH_LONG).show();
                Log.i("Activity", "onFailure: Fail");
            }

            @Override
            public void onResponse(final Response response) throws IOException {
                final  String body=response.body().string();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(context, ""+body, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    private void fetchoccupation()
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
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        try {
                          JSONObject  jsonObject = new JSONObject(body);
                            JSONArray jsonArray= jsonObject.getJSONArray("Occupation");
                            JSONArray jsonArray1= jsonObject.getJSONArray("Ethnicity");
                            JSONArray jsonArray_IdentityType= jsonObject.getJSONArray("IdentityType");
                            for (int i = 0; i <jsonArray.length() ; i++)
                            {
                                JSONObject jsonObject1= jsonArray.getJSONObject(i);
                                occupationlists.add(jsonObject1.getString("name"));
                            }
                            CustomAdapter_setupprofile customAdapter=new CustomAdapter_setupprofile(context,occupationlists);
                            occupation.setAdapter(customAdapter);
                            if(!bundle.getString("occupation").equals(""))
                            {
                                if((Integer.parseInt(bundle.getString("occupation"))>occupationlists.size()))
                                    occupation.setSelection(0);
                                    else
                                occupation.setSelection((Integer.parseInt(bundle.getString("occupation"))) - 1);
                            }


                            for (int j = 0; j <jsonArray1.length() ; j++)
                            {
                                JSONObject jsonObject2= jsonArray1.getJSONObject(j);
                                Ethnicitylists.add(jsonObject2.getString("name"));
                            }
                            CustomAdapter_setupprofile customAdapter1=new CustomAdapter_setupprofile(context,Ethnicitylists);
                            ethinicity.setAdapter(customAdapter1);
                            if(!bundle.getString("ethn").equals("")) {
                                if((Integer.parseInt(bundle.getString("occupation"))>Ethnicitylists.size()))
                                    ethinicity.setSelection(0);
                                else
                                ethinicity.setSelection((Integer.parseInt(bundle.getString("ethn"))) - 1);
                            }


                            for (int j = 0; j <jsonArray_IdentityType.length() ; j++)
                            {
                                JSONObject jsonObject2= jsonArray_IdentityType.getJSONObject(j);
                                Identitytypelists.add(jsonObject2.getString("name"));
                            }
                            CustomAdapter_setupprofile customAdapter2=new CustomAdapter_setupprofile(context,Identitytypelists);
                            identitytype.setAdapter(customAdapter2);
                            if(!bundle.getString("identitytype").equals("")) {
                                if((Integer.parseInt(bundle.getString("occupation"))>Identitytypelists.size()))
                                    identitytype.setSelection(0);
                                else
                                identitytype.setSelection((Integer.parseInt(bundle.getString("identitytype"))) - 1);
                            }
                        }


                        catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(context, ""+e, Toast.LENGTH_SHORT).show();
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
                .url("http://feish.online/apis/listoccupation")
                .post(body)
                .build();
    }

    public boolean profileresponse(String response) {
        Gson gson = new GsonBuilder().create();
        ContactService profileResponse = gson.fromJson(response, ContactService.class);

        return profileResponse.getStatus();
    }

    public boolean profilePicresponse(String response) {
        Gson gson = new GsonBuilder().create();
        ContactService profileResponse = gson.fromJson(response, ContactService.class);

        return profileResponse.getStatus();
    }

    public String getPath(Uri uri) {

        String path;
        String[] projection = { MediaStore.Files.FileColumns.DATA };
        Cursor cursor = SetupProfile.this.getContentResolver().query(uri, projection, null, null, null);

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

    private Request profilePicrequest(Uri entity) {
        JSONObject postdata = new JSONObject();

        try {
            postdata.put("user_id", Prefhelper.getInstance(SetupProfile.this).getUserid());
            postdata.put("weight", weight.getText().toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    //    File sourceFile = new File(getRealPathFromUri(SetupProfile.this, entity));
        File sourceFile = new File(src);
        Log.d("", "File...::::" + sourceFile + " : " + sourceFile.exists());

        final MediaType MEDIA_TYPE = getRealPathFromUri(SetupProfile.this, entity).endsWith("png") ?
                MediaType.parse("image/png") : MediaType.parse("image/jpeg");


        RequestBody requestBody = new MultipartBuilder()
                .type(MultipartBuilder.FORM)
                .addFormDataPart("user_id",Prefhelper.getInstance(SetupProfile.this).getUserid())
                .addFormDataPart("profile_pic", "profile.png", RequestBody.create(MEDIA_TYPE, sourceFile))
                .build();

        return new Request.Builder()
                .addHeader("X-Api-Key", "AB5433GMDF657VBB")
                .addHeader("Content-Type", "application/json")
                .url("http://feish.online/apis/uploadProfilePic")
                .post(requestBody)
                .build();
    }

    public static String getRealPathFromUri(Context context, Uri contentUri) {
        Cursor cursor = null;
        try {
            String[] proj = {MediaStore.Images.Media.DATA};
            cursor = context.getContentResolver().query(contentUri, proj, null, null, null);
            assert cursor != null;
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
            if (ContextCompat.checkSelfPermission(SetupProfile.this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(SetupProfile.this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        PERMISSION_REQUEST_WRITE_EXTERNAL_STORAGE);
            }
        }
    }


}
class CustomAdapter_setupprofile extends BaseAdapter {
    Context context;
    LayoutInflater inflter;
    List<String> medicalconditionlists;
    public CustomAdapter_setupprofile(Context applicationContext, List<String> medicalconditionlists) {
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
        names.setText(medicalconditionlists.get(i));
        return view;
    }
}
