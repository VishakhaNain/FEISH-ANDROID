package com.app.feish.application;

import android.app.Activity;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;


public class DialogActivity extends Activity {
    ImageView imageView,imageView_back;
    Uri uri;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        uri=Uri.parse(getIntent().getStringExtra("image"));
        AlertDialog.Builder alertadd = new AlertDialog.Builder(DialogActivity.this);
        LayoutInflater factory = LayoutInflater.from(DialogActivity.this);
        final View view = factory.inflate(R.layout.sample, null);
        imageView=view.findViewById(R.id.dialog_imageview);
        imageView_back=view.findViewById(R.id.img_back);
        imageView_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        imageView.setImageURI(uri);

        alertadd.setView(view);
        alertadd.setNeutralButton("Close", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dlg, int sumthin) {
finish();
            }
        });

        alertadd.show();
    }
}
