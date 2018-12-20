package com.app.feish.application;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.app.feish.application.Assistant.AssistantDashbord;
import com.app.feish.application.Patient.PatientDashboard;
import com.app.feish.application.clinic.ClinicDashboard;
import com.app.feish.application.doctor.DoctorDashboard;
import com.app.feish.application.sessiondata.Prefhelper;

public class SplashScreen extends Activity {
    Intent i;
    ImageView imageView;
    int SPLASH_TIME_OUT = 3000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        imageView =findViewById(R.id.img);
        Thread timer = new Thread() {
            public void run() {
                try {
                    Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade);
                    imageView.startAnimation(animation);
                    sleep(SPLASH_TIME_OUT);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {

                    if(Prefhelper.getInstance(SplashScreen.this).getLoggedIn())
                    {
                        if (Prefhelper.getInstance(SplashScreen.this).getUsertype().equals("4")) {
                            Intent intent = new Intent(SplashScreen.this, PatientDashboard.class);
                       //     intent.putExtra("userid", Prefhelper.getInstance(SplashScreen.this).getUserid());
                            startActivity(intent);
                            overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
                        //    finish();
                        }
                        else  if (Prefhelper.getInstance(SplashScreen.this).getUsertype().equals("2"))
                            {
                            Intent intent = new Intent(SplashScreen.this, DoctorDashboard.class);
                          //  intent.putExtra("userid", Prefhelper.getInstance(SplashScreen.this).getUserid());
                            startActivity(intent);
                            overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
                        //    finish();
                            }


                        else  if (Prefhelper.getInstance(SplashScreen.this).getUsertype().equals("5"))
                        {
                            Intent intent = new Intent(SplashScreen.this, ClinicDashboard.class);
                            //  intent.putExtra("userid", Prefhelper.getInstance(SplashScreen.this).getUserid());
                            startActivity(intent);
                            overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
                            //    finish();
                        }
                            else
                        {
                            Intent intent = new Intent(SplashScreen.this, AssistantDashbord.class);
                            //  intent.putExtra("userid", Prefhelper.getInstance(SplashScreen.this).getUserid());
                            startActivity(intent);
                            overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
                        }
                    }
                    else
                    {
                        Intent i = new Intent(SplashScreen.this, LoginActivity.class);
                        startActivity(i);
                        overridePendingTransition(R.anim.fade_in,R.anim.fade_out);

                    }
                }
            }
        };
        timer.start();

      /*  new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                    i = new Intent(SplashScreen.this, SplashScreen.class);
                    finish();
                    startActivity(i);


            }
        }, SPLASH_TIME_OUT);*/
    }
    @Override
    protected void onPause() {
        super.onPause();
        finish();

    }

}

