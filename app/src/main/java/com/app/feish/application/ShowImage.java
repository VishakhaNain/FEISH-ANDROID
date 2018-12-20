package com.app.feish.application;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.darsh.multipleimageselect.models.Image;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;


public class ShowImage extends AppCompatActivity {
    TextView textView;
    LinearLayout ll;
    CustomPagerAdapter mCustomPagerAdapter;
    ViewPager mViewPager;
    ArrayList<Image> object;
    Context context=this;
    ArrayList<Integer> second;
    ArrayList<String> imagelist;
    int code=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_image);
              ll= (LinearLayout)findViewById(R.id.msgimg);
        Intent intent = getIntent();
        code=intent.getIntExtra("code",0);
        if(code==1)
        {
            Bundle args = intent.getBundleExtra("BUNDLE");
            second = (ArrayList<Integer>) args.getSerializable("ARRAYLIST");
            mCustomPagerAdapter = new CustomPagerAdapter(this);

        }
        else if(code==0)
        {
            Bundle args = intent.getBundleExtra("BUNDLE");
            object = (ArrayList<Image>) args.getSerializable("ARRAYLIST");
            mCustomPagerAdapter = new CustomPagerAdapter(this);

        }
        else
        {
            Bundle args = intent.getBundleExtra("BUNDLE");
            imagelist = (ArrayList<String>) args.getSerializable("imagelist");
            mCustomPagerAdapter = new CustomPagerAdapter(this);
        }

        mViewPager = (ViewPager) findViewById(R.id.pager);
        textView =  findViewById(R.id.close);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mViewPager.setAdapter(mCustomPagerAdapter);

    }
   class CustomPagerAdapter extends PagerAdapter {

       Context mContext;
       LayoutInflater mLayoutInflater;

       public CustomPagerAdapter(Context context) {
           mContext = context;
           mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
       }

       @Override
       public int getCount() {
           if(code==0)
           return object.size();
           else if(code==1)
               return second.size();
           else
              return imagelist.size();
       }

       @Override
       public boolean isViewFromObject(View view, Object object) {
           return view == ((RelativeLayout) object);
       }

       @Override
       public Object instantiateItem(ViewGroup container, int position) {

           View itemView = mLayoutInflater.inflate(R.layout.pager_item, container, false);
           final Myimageview imageView = (Myimageview) itemView.findViewById(R.id.imageView);
           if(code==0)
           imageView.setImageURI(Uri.parse(object.get(position).path));
           else if(code==1)
               imageView.setImageResource(second.get(position));
           else {
               Picasso.with(context)
                       .load("http://feish.online/img/services/"+imagelist.get(position))
                       .into(imageView);
           }



           imageView.setMaxZoom(4f);




           //  setContentView(img);
    //       imageView.setImageResource(mResources[position]);

           container.addView(itemView);

           return itemView;
       }

       @Override
       public void destroyItem(ViewGroup container, int position, Object object) {
           container.removeView((RelativeLayout) object);
       }

   }

}
