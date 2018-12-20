package com.app.feish.application.PaymentDetail;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.webkit.SslErrorHandler;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.app.feish.application.Assistant.PatientEntry;
import com.app.feish.application.Patient.MedicalHitoryp;
import com.app.feish.application.R;
import com.app.feish.application.Remote.ApiUtils;
import com.app.feish.application.modelclassforapi.DoctorEncounters;
import com.app.feish.application.sessiondata.Prefhelper;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class PayUMoneyActivity extends AppCompatActivity {

    /**
     * Adding WebView as setContentView
     */
    WebView webView;
    DoctorEncounters appointmentdatum;

    /**
     * Context for Activity
     */
    Context activity;
    /**
     * Order Id
     * To Request for Updating Payment Status if Payment Successfully Done
     */
    int mId=105; //Getting from Previous Activity
    /**
     * Required Fields
     */
    // Test Variables
    private String mMerchantKey = AppEnvironment.PRODUCTION.merchant_Key();
    private String mSalt = AppEnvironment.PRODUCTION.salt();
    String fee="";
    private String mBaseURL = "https://secure.payu.in";
    private String dBaseURL = "https://sandboxsecure.payu.in";
/*
    // Final Variables
    private String mMerchantKey = "Your Merchant Key";
    private String mSalt = "Salt";
    private String mBaseURL = "https://secure.payu.in";*/


    private String mAction = ""; // For Final URL
    private String mTXNId; // This will create below randomly
    private String mHash; // This will create below randomly
    private String mProductInfo = "Food Items"; //Passing String only
    private String mFirstName=""; // From Previous Activity
    private String mEmailId=""; // From Previous Activity
    private double mAmount=1; // From Previous Activity
    private String mPhone=""; // From Previous Activity
    private String mServiceProvider = "payu_paisa";
    private String mSuccessUrl = "https://www.payumoney.com/mobileapp/payumoney/success.php";
    private String mFailedUrl = "https://www.payumoney.com/mobileapp/payumoney/failure.php";


    boolean isFromOrder;
    /**
     * Handler
     */
    Handler mHandler = new Handler();

    /**
     * @param savedInstanceState
     */
    @SuppressLint({"AddJavascriptInterface", "SetJavaScriptEnabled", "WrongConstant", "JavascriptInterface"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().requestFeature(Window.FEATURE_PROGRESS);
        super.onCreate(savedInstanceState);

        /**
         * Setting WebView to Screen
         */
        setContentView(R.layout.activity_webview_for_payumoney);

        /**
         * Creating WebView
         */
        webView = (WebView) findViewById(R.id.payumoney_webview);

        /**
         * Context Variable
         */
        activity = getApplicationContext();

        /**
         * Actionbar Settings
         /*   *//*
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
        // enabling action bar app icon and behaving it as toggle button
        ab.setHomeButtonEnabled(true);
        ab.setTitle("Online Payment");
*/
        /**
         * Getting Intent Variables...
         */
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            appointmentdatum=(DoctorEncounters) bundle.getSerializable("data");
            fee=bundle.getString("fee");
            mFirstName = appointmentdatum.getAppointmentdata().get(0).getUser().getFirstName()+" "+appointmentdatum.getAppointmentdata().get(0).getUser().getLastName();;
            mEmailId = appointmentdatum.getAppointmentdata().get(0).getUser().getEmail();

            //fee="1";
            mAmount = Double.parseDouble(fee);
            mPhone = appointmentdatum.getAppointmentdata().get(0).getUser().getMobile();
            mId = 105;
            isFromOrder = true;


        Log.i("info", "" + mFirstName + " : " + mEmailId + " : " + mAmount + " : " + mPhone);

        /**
         * Creating Transaction Id
         */
        Random rand = new Random();
        String randomString = Integer.toString(rand.nextInt()) + (System.currentTimeMillis() / 1000L);
        mTXNId = hashCal("SHA-256", randomString).substring(0, 20);

        mAmount = new BigDecimal(mAmount).setScale(0, RoundingMode.UP).intValue();

        /**
         * Creating Hash Key
         */
        mHash = hashCal("SHA-512", mMerchantKey + "|" +
                mTXNId + "|" +
                mAmount + "|" +
                mProductInfo + "|" +
                mFirstName + "|" +
                mEmailId + "|||||||||||" +
                mSalt);

        /**
         * Final Action URL...
         */
        mAction = dBaseURL.concat("/_payment");

        /**
         * WebView Client
         */
        webView.setWebViewClient(new WebViewClient() {

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);
                Toast.makeText(activity, "Oh no! " + error, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onReceivedSslError(WebView view,
                                           SslErrorHandler handler, SslError error) {
                Toast.makeText(activity, "SSL Error! " + error, Toast.LENGTH_SHORT).show();
                handler.proceed();
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return super.shouldOverrideUrlLoading(view, url);
            }

            @Override
            public void onPageFinished(WebView view, String url) {

                  /*  if (url.equals(mSuccessUrl)) {
                        Intent intent = new Intent(PayUMoneyActivity.this, PaymentStatusActivity.class);
                        intent.putExtra("status", true);
                        intent.putExtra("transaction_id", mTXNId);
                        intent.putExtra("id", mId);
                        intent.putExtra("isFromOrder", isFromOrder);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    } else if (url.equals(mFailedUrl)) {
                        Intent intent = new Intent(PayUMoneyActivity.this, PaymentStatusActivity.class);
                        intent.putExtra("status", false);
                        intent.putExtra("transaction_id", mTXNId);
                        intent.putExtra("id", mId);
                        intent.putExtra("isFromOrder", isFromOrder);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    }*/
                super.onPageFinished(view, url);
            }
        });

        webView.setVisibility(View.VISIBLE);
        webView.getSettings().setBuiltInZoomControls(true);
        webView.getSettings().setCacheMode(2);
        webView.getSettings().setDomStorageEnabled(true);
        webView.clearHistory();
        webView.clearCache(true);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setSupportZoom(true);
        webView.getSettings().setUseWideViewPort(false);
        webView.getSettings().setLoadWithOverviewMode(false);
        webView.addJavascriptInterface(new PayUJavaScriptInterface(PayUMoneyActivity.this), "PayUMoney");

        /**
         * Mapping Compulsory Key Value Pairs
         */
        Map<String, String> mapParams = new HashMap<>();

        mapParams.put("key", mMerchantKey);
        mapParams.put("txnid", mTXNId);
        mapParams.put("amount", String.valueOf(mAmount));
        mapParams.put("productinfo", mProductInfo);
        mapParams.put("firstname", mFirstName);
        mapParams.put("email", mEmailId);
        mapParams.put("phone", mPhone);
        mapParams.put("surl", mSuccessUrl);
        mapParams.put("furl", mFailedUrl);
        mapParams.put("hash", mHash);
        mapParams.put("service_provider", mServiceProvider);

        webViewClientPost(webView, mAction, mapParams.entrySet());
        } else {
            Toast.makeText(activity, "Something went wrong, Try again.", Toast.LENGTH_LONG).show();
        }
    }

    /**
     * Posting Data on PayUMoney Site with Form
     *
     * @param webView
     * @param url
     * @param postData
     */
    public void webViewClientPost(WebView webView, String url,
                                  Collection<Map.Entry<String, String>> postData) {
        StringBuilder sb = new StringBuilder();

        sb.append("<html><head></head>");
        sb.append("<body onload='form1.submit()'>");
        sb.append(String.format("<form id='form1' action='%s' method='%s'>", url, "post"));

        for (Map.Entry<String, String> item : postData) {
            sb.append(String.format("<input name='%s' type='hidden' value='%s' />", item.getKey(), item.getValue()));
        }
        sb.append("</form></body></html>");

        Log.d("TAG", "webViewClientPost called: " + sb.toString());
        webView.loadData(sb.toString(), "text/html", "utf-8");
    }

    /**
     * Hash Key Calculation
     *
     * @param type
     * @param str
     * @return
     */
    public String hashCal(String type, String str) {
        byte[] hashSequence = str.getBytes();
        StringBuffer hexString = new StringBuffer();
        try {
            MessageDigest algorithm = MessageDigest.getInstance(type);
            algorithm.reset();
            algorithm.update(hashSequence);
            byte messageDigest[] = algorithm.digest();

            for (int i = 0; i < messageDigest.length; i++) {
                String hex = Integer.toHexString(0xFF & messageDigest[i]);
                if (hex.length() == 1)
                    hexString.append("0");
                hexString.append(hex);
            }
        } catch (NoSuchAlgorithmException NSAE) {
        }
        return hexString.toString();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home) {
            onPressingBack();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        onPressingBack();
    }

    /**
     * On Pressing Back
     * Giving Alert...
     */
    private void onPressingBack() {

        final Intent intent;

        if(isFromOrder)
            intent = new Intent(PayUMoneyActivity.this, PatientEntry.class);
        else
            intent = new Intent(PayUMoneyActivity.this, PatientEntry.class);

        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(PayUMoneyActivity.this);

        // Setting Dialog Title
        alertDialog.setTitle("Warning");

        // Setting Dialog Message
        alertDialog.setMessage("Do you cancel this transaction?");

        // On pressing Settings button
        alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                finish();
                startActivity(intent);
            }
        });

        // on pressing cancel button
        alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        // Showing Alert Message
        alertDialog.show();
    }

    public class PayUJavaScriptInterface {
        Context mContext;

        /**
         * Instantiate the interface and set the context
         */
        PayUJavaScriptInterface(Context c) {
            mContext = c;
        }

        public void success(long id, final String paymentId) {
            mHandler.post(new Runnable() {

                public void run() {
                    mHandler = null;
                    Toast.makeText(PayUMoneyActivity.this, "Payment Successfully.", Toast.LENGTH_SHORT).show();
                    update("Card",0);
                }
            });
        }
    }
    private Request updateappodetail(String paymentmpde) {
        JSONObject postdata = new JSONObject();
        try{
            postdata.put("id",appointmentdatum.getAppointmentdata().get(0).getAppointment().getId());
            postdata.put("appointed_timing",appointmentdatum.getAppointmentdata().get(0).getAppointment().getAppointedTiming());
            postdata.put("user_id",appointmentdatum.getAppointmentdata().get(0).getUser().getId());
            postdata.put("doctor_id",appointmentdatum.getAppointmentdata().get(0).getDoctor().getId());
            postdata.put("service_id",appointmentdatum.getAppointmentdata().get(0).getService().getId());
            postdata.put("scheduled_date",appointmentdatum.getAppointmentdata().get(0).getAppointment().getScheduledDate());
            postdata.put("patient_arrival_time",appointmentdatum.getAppointmentdata().get(0).getAppointment().getPatient_arrival_time());
            postdata.put("patient_in_time",appointmentdatum.getAppointmentdata().get(0).getAppointment().getPatient_in_time());
            postdata.put("patient_out_time",appointmentdatum.getAppointmentdata().get(0).getAppointment().getPatient_out_time());
            postdata.put("patient_exit_time",appointmentdatum.getAppointmentdata().get(0).getAppointment().getPatient_exit_time());
            postdata.put("token_id",appointmentdatum.getAppointmentdata().get(0).getAppointment().getToken_id());
            postdata.put("paymentmode",paymentmpde);
            postdata.put("amount",Integer.parseInt(String.valueOf(mAmount)));
            postdata.put("tranid",mTXNId);
            postdata.put("collected_by", Prefhelper.getInstance(activity).getUserid());
        }
        catch (JSONException e)
        {
            Toast.makeText(activity, ""+e, Toast.LENGTH_SHORT).show();
        }
        RequestBody body = RequestBody.create(MedicalHitoryp.JSON, postdata.toString());
        return new Request.Builder()
                .addHeader("X-Api-Key", "AB5433GMDF657VBB")
                .addHeader("Content-Type", "application/json")
                .url(ApiUtils.BASE_URL+"updateappointmentdetail")
                .post(body)
                .build();

    }
    private void update(String paymentmode, final int code)
    {
        OkHttpClient client = new OkHttpClient();
        Request request = updateappodetail(paymentmode);

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
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                       // Toast.makeText(activity, ""+body, Toast.LENGTH_SHORT).show();
                        try {
                            JSONObject  jsonObject= new JSONObject(body);

                            if(jsonObject.getBoolean("status"))
                            {
                                AlertDialog.Builder builder;
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                    builder = new AlertDialog.Builder(activity, android.R.style.Theme_Material_Dialog_Alert);
                                } else {
                                    builder = new AlertDialog.Builder(activity);
                                }
                                builder.setTitle("Message")
                                        .setMessage("Payment Successfull with "+mTXNId)
                                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {
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
                            }

                        } catch (JSONException e) {
                            Toast.makeText(activity, ""+e, Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                        }
                    }
                });
            }

        });

    }
}
