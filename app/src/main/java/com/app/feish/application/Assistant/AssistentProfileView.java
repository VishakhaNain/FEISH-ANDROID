package com.app.feish.application.Assistant;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.app.feish.application.Connectiondetector;
import com.app.feish.application.R;
import com.app.feish.application.Remote.ApiUtils;
import com.app.feish.application.modelclassforapi.ContactService;
import com.app.feish.application.modelclassforapi.ContactService_getDetails;
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

import static com.app.feish.application.fragment.Addfamilyreport.JSON;

public class AssistentProfileView extends AppCompatActivity {
    AlertDialog coupandialog;
    private Button btnSelect;
    private ImageView ivImage;
    Button done;
    Bitmap selectimage;
    Connectiondetector connectiondetector;
    String src;
    ContactService_getDetails contactService_getDetails;
    TextView emailprofile, passwordprofile, fnameprofile, lnameprofile;
    Context context=this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assistent_profile_view);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        connectiondetector= new Connectiondetector(getApplicationContext());
        if(connectiondetector.isConnectingToInternet())
        {
            fetchingdata();
        }
        emailprofile = findViewById(R.id.emailprofile);
        passwordprofile = findViewById(R.id.passwordprofile);
        fnameprofile = findViewById(R.id.fnameprofile);
        lnameprofile = findViewById(R.id.lnameprofile);
        btnSelect = findViewById(R.id.select);
        done = findViewById(R.id.done);
        retrofit();
        btnSelect.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
              addLogo();
            }
        });
        ivImage =  findViewById(R.id.image);
    }
    private void addLogo() {
        final CharSequence[] options = {/*"Take Photo"*/
                "Choose From Gallery", "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(AssistentProfileView.this);
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
        Cursor cursor = AssistentProfileView.this.getContentResolver().query(uri, projection, null, null, null);

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
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == 1) {

                selectimage = (Bitmap) data.getExtras().get("data");

                Uri selectedImageURI = getImageUri(getApplicationContext(), selectimage);
                src=getPath(selectedImageURI);
                InputStream imageStream;
                try {
                    imageStream = AssistentProfileView.this.getContentResolver().openInputStream(selectedImageURI);
                    selectimage = BitmapFactory.decodeStream(imageStream);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                if (!(selectimage == null)) {
                    ivImage.setImageBitmap(selectimage);
                    // byte[] bytedata = null;
                    apiCall(selectedImageURI);

                }
            } else if (requestCode == 2) {
                Uri selectedImageURI = data.getData();
                src=getPath(selectedImageURI);
                InputStream imageStream;
                try {
                    assert selectedImageURI != null;
                    imageStream = AssistentProfileView.this.getContentResolver().openInputStream(selectedImageURI);
                    selectimage = BitmapFactory.decodeStream(imageStream);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                if (selectimage != null) {
                    ivImage.setImageBitmap(selectimage);
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
        OkHttpClient client = new OkHttpClient();
        Request validation_request = profilePicrequest(entity);
        client.newCall(validation_request).enqueue(new Callback() {

            @Override
            public void onFailure(Request request, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(AssistentProfileView.this, "Failure", Toast.LENGTH_SHORT).show();
                       }
                });

            }

            @Override
            public void onResponse(final Response response) throws IOException {

                final boolean isSuccessful = profilePicresponse(response.body().string());
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        if (isSuccessful) {
//                            double wgt=Double.parseDouble(weight.getText().toString());
//                            double hgt=Double.parseDouble(height.getText().toString());
//                            double hgtsq=Math.pow(hgt,2);
//                            bmi.setText(Double.toString(wgt/hgtsq));
                            Toast.makeText(getApplicationContext(), "Update successful", Toast.LENGTH_LONG).show();

//                            Intent intent=new Intent(AssistentProfileView.this,ViewProfilePatient.class);
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

    private Request profilePicrequest(Uri entity) {
          File sourceFile = new File(src);
        Log.d("", "File...::::" + sourceFile + " : " + sourceFile.exists());

        final MediaType MEDIA_TYPE = getRealPathFromUri(AssistentProfileView.this, entity).endsWith("png") ?
                MediaType.parse("image/png") : MediaType.parse("image/jpeg");


        RequestBody requestBody = new MultipartBuilder()
                .type(MultipartBuilder.FORM)
                .addFormDataPart("user_id",Prefhelper.getInstance(AssistentProfileView.this).getUserid())
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
    public boolean profilePicresponse(String response) {
        Gson gson = new GsonBuilder().create();
        ContactService profileResponse = gson.fromJson(response, ContactService.class);

        return profileResponse.getStatus();
    }

    private void retrofit() {
       OkHttpClient client = new OkHttpClient();
        Request validation_request = listservices();
        client.newCall(validation_request).enqueue(new Callback() {

            @Override
            public void onFailure(Request request, IOException e) {

                Toast.makeText(context,"Fail",Toast.LENGTH_LONG).show();
                Log.i("Activity", "onFailure: Fail");
            }

            @Override
            public void onResponse(final Response response) throws IOException {
                final String body = response.body().string();

                runOnUiThread(new Runnable() {
                                  @Override
                                  public void run()
                                  {
                                     // Toast.makeText(context, "" + body, Toast.LENGTH_SHORT).show();
                                      try {
                                          JSONArray jsonArray = new JSONArray(body);

                                          for (int i = 0; i < jsonArray.length(); i++) {


                                              JSONObject jsonobject = jsonArray.getJSONObject(i);

                                              String mail = jsonobject.getString("email");
                                             // String pass = jsonobject.getString("password");
                                              String fnamee = jsonobject.getString("first_name");
                                              String lnamee = jsonobject.getString("last_name");
                                              emailprofile.setText(mail);
                                            //  passwordprofile.setText(pass);
                                              fnameprofile.setText(fnamee);
                                              lnameprofile.setText(lnamee);
                                          }



                                      } catch (JSONException e) {
                                          Toast.makeText(AssistentProfileView.this, "" + e, Toast.LENGTH_SHORT).show();
                                      }

                                    }

                              }
                );
            }
        });
    }

    private Request listservices() {
        JSONObject postdata = new JSONObject();
        try{
            postdata.put("id",Prefhelper.getInstance(context).getUserid());
        }
        catch (JSONException e)
        {
            Toast.makeText(context, ""+e, Toast.LENGTH_SHORT).show();
        }
        RequestBody body = RequestBody.create(JSON, postdata.toString());
        return new Request.Builder()
                .addHeader("X-Api-Key", "AB5433GMDF657VBB")
                 .addHeader("Content-Type", "application/json")
                .url(ApiUtils.BASE_URL+"fetchassistantprofileview")
                .post(body)
                .build();
    }
    private Request Patient_detail() {
        JSONObject postdata = new JSONObject();
        try {
            postdata.put("user_id", Prefhelper.getInstance(AssistentProfileView.this).getUserid());
            //postdata.put("password",lpassword);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(JSON, postdata.toString());
        final Request request = new Request.Builder()
                .addHeader("X-Api-Key", "AB5433GMDF657VBB")
                .addHeader("Content-Type", "application/json")
                .url("http://feish.online/apis/getPatientdetails")
                .post(body)
                .build();
        return request;
    }
    public void pdetailJSON(String response) {
        Gson gson = new GsonBuilder().create();
        contactService_getDetails = gson.fromJson(response, ContactService_getDetails.class);
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

                String body=response.body().string();
                Log.i("", "onResponse: "+body);
                pdetailJSON(body);
                final String message = contactService_getDetails.getMessage();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //pb.setVisibility(View.GONE);
                        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

                        if (message.compareTo("success") == 0) {
                            if(contactService_getDetails.getData().getAvatar()!=null)
                            {
                                Picasso.with(AssistentProfileView.this)
                                        .load(contactService_getDetails.getData().getAvatar().toString())
                                        .into(ivImage);
                            }
                            else
                            {
                                ivImage.setImageResource(R.drawable.patienr);
                            }
                        }

                        else {
                            Toast.makeText(getApplicationContext(), "Fail", Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }

        });

    }


}
