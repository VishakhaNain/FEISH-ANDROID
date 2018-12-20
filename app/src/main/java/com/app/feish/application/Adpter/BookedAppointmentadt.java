package com.app.feish.application.Adpter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.app.feish.application.Patient.BookedAppointmetDetail;
import com.app.feish.application.R;
import com.app.feish.application.modelclassforapi.Appointmentdatum;
import com.app.feish.application.modelclassforapi.PatientBookedappointment;
import java.util.Date;
import java.util.List;

public class BookedAppointmentadt extends RecyclerView.Adapter<BookedAppointmentadt.MyViewHolder> {

    int code;
    Context context;
    PatientBookedappointment patientBookedappointment;
    List<Appointmentdatum>  dataSet;

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView textView_drname,textView_date;
        TextView textView_drloc,textView_time;
        CardView cardView;
        public MyViewHolder(View itemView) {
            super(itemView);
            this.textView_drname = itemView.findViewById(R.id.drname);
            this.textView_date =itemView.findViewById(R.id.drdate);
            this.textView_drloc = itemView.findViewById(R.id.drloc);
            this.textView_time =itemView.findViewById(R.id.drtime);
            this.cardView = itemView.findViewById(R.id.card_view);
        }
    }

    public BookedAppointmentadt(int code, PatientBookedappointment patientBookedappointment, Context context) {
        this.code = code;
        this.context=context;
        this.patientBookedappointment=patientBookedappointment;
        this.dataSet=patientBookedappointment.getAppointmentdata();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent,
                                           int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.bookedappointmentlayout, parent, false);

        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int listPosition)
    {
                String datatime[] = patientBookedappointment.getAppointmentdata().get(listPosition).getAppointment().getAppointedTiming().split(" ");
                holder.textView_drname.setText(patientBookedappointment.getAppointmentdata().get(listPosition).getDoctor().getFirstName() + " " + patientBookedappointment.getAppointmentdata().get(listPosition).getDoctor().getLastName() + " ( " + patientBookedappointment.getAppointmentdata().get(listPosition).getDoctor().getQualification() + " )");
                String address = patientBookedappointment.getAppointmentdata().get(listPosition).getService().getAddress() + " " + patientBookedappointment.getAppointmentdata().get(listPosition).getService().getLocality() + " " + patientBookedappointment.getAppointmentdata().get(listPosition).getService().getCity();
                holder.textView_drloc.setText(address);
                holder.textView_date.setText(datatime[0]);
                holder.textView_time.setText(datatime[1]);

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(new Intent(context, BookedAppointmetDetail.class));
                i.putExtra("code",code);
                i.putExtra("appointmentlist", patientBookedappointment.getAppointmentdata().get(listPosition));
                i.putExtra("userdetaillist",patientBookedappointment.getAppodata().get(listPosition));
                context.startActivity(i);
            }
        });

    }

  public   void setFilter(PatientBookedappointment countryModels) {
         patientBookedappointment= new PatientBookedappointment();
         patientBookedappointment=countryModels;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return patientBookedappointment.getAppointmentdata().size();
    }
}
