package com.app.feish.application.doctor;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.app.feish.application.DoctorCompleteProfile;
import com.app.feish.application.R;
import com.app.feish.application.Remote.ApiUtils;
import com.app.feish.application.model.pojorating;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import me.zhanghai.android.materialratingbar.MaterialRatingBar;

import static com.app.feish.application.doctor.SetupProfileForDoctor.JSON;

/**
 * Created by lenovo on 10/27/2017.
 */

public class Ratingadt extends RecyclerView.Adapter<Ratingadt.MyViewHolder> {

    private List<pojorating> moviesList;
    Context context;
int code;
    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tv_name,tv_date,tv_review,tv_givenby;
        me.zhanghai.android.materialratingbar.MaterialRatingBar materialRatingBar;

        public MyViewHolder(View view) {
            super(view);
            tv_name=(TextView)view.findViewById(R.id.name);
            tv_date=(TextView)view.findViewById(R.id.date);
            tv_review=(TextView)view.findViewById(R.id.review);
            tv_givenby=(TextView)view.findViewById(R.id.givenby);
            materialRatingBar=(MaterialRatingBar)view.findViewById(R.id.ratingbar);
        }
    }


    public Ratingadt(ArrayList<pojorating> moviesList, Context context,int code) {
        this.moviesList = moviesList;
        this.context=context;
        this.code=code;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.ratinglist, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
    final     pojorating pojorating = moviesList.get(position);
        holder.tv_name.setText(pojorating.getName());
        holder.tv_date.setText(pojorating.getDate());
        holder.tv_review.setText(pojorating.getReview());
        if(code!=0)
        holder.tv_givenby.setText(pojorating.getgivenby());
        else
        {
            if(pojorating.getgivenby().equals("Published"))
                holder.tv_givenby.setTextColor(Color.GREEN);
                else
                holder.tv_givenby.setTextColor(Color.RED);

            holder.tv_givenby.setText(pojorating.getgivenby());

        }
        holder.tv_givenby.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int userid=Drreviewrating.id;

                Toast.makeText(context, "Clicked", Toast.LENGTH_SHORT).show();
                if(pojorating.getgivenby().equals("Published"))
                {
                    publishreview("Unpublished",pojorating.getId(),0,holder.tv_givenby,pojorating.getRating(),pojorating.getReview(),userid);
                }
                else if(pojorating.getgivenby().equals("Unpublished"))
                {
                    publishreview("Published",pojorating.getId(),1,holder.tv_givenby,pojorating.getRating(),pojorating.getReview(),userid);
                }

            }
        });
        holder.materialRatingBar.setRating(Float.parseFloat(pojorating.getRating()));

    }

    public void publishreview(final String Publish, final int id, final int value, final TextView  textView, final String rating, final String review, final int doocid)
    {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        alertDialogBuilder.setMessage("Are you sure, You wanted to"+ Publish +"decision");
        alertDialogBuilder.setPositiveButton("yes",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        Toast.makeText(context,"You clicked yes button",Toast.LENGTH_LONG).show();
                        fetchinggdrreview(id,value,textView,Publish,rating,review,doocid);

                    }
                });

        alertDialogBuilder.setNegativeButton("No",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    @Override
    public int getItemCount() {
        return moviesList.size();
    }
    private Request FetchReview(int id, int is_published,String rating,String review,int doctor_id)
    {
        JSONObject postdata = new JSONObject();
        try {
            postdata.put("id", id);
            postdata.put("is_published", is_published);
            postdata.put("rating", Float.parseFloat(rating));
            postdata.put("review", review);
            postdata.put("doctor_id", doctor_id);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(JSON, postdata.toString());
        return new Request.Builder()
                .addHeader("X-Api-Key", "AB5433GMDF657VBB")
                .addHeader("Content-Type", "application/json")
                .url(ApiUtils.BASE_URL+"makeratingpublished")
                .post(body)
                .build();
    }
    private void fetchinggdrreview(int id, int is_published, final TextView textView, final String Publish,String rating,String review,int doctor_id)
    {
        OkHttpClient client = new OkHttpClient();
        Request request = FetchReview(id,is_published,rating,review,doctor_id);
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
                ((Activity)context).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                                       textView.setText(Publish);
                    }
                });
            }

        });

    }
}
