package com.example.ashishpanjwani.timeperfectagain.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.example.ashishpanjwani.timeperfectagain.Model.CurrentTimeList;
import com.example.ashishpanjwani.timeperfectagain.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class CurrentTimeAdapter extends RecyclerView.Adapter<CurrentTimeAdapter.TimeViewHolder> {

    private Context context;
    private List<CurrentTimeList> currentTimes;

    public CurrentTimeAdapter(Context context, List<CurrentTimeList> currentTimes) {
        this.context=context;
        this.currentTimes=currentTimes;
    }

    @Override
    public TimeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater inflater=LayoutInflater.from(context);
        View view=inflater.inflate(R.layout.activity_main,parent,false);
        return new TimeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TimeViewHolder holder, int position) {

        CurrentTimeList currentTime=currentTimes.get(position);
        Date date=new Date();
        String strDateFormat1="hh:mm";
        DateFormat dateFormat1=new SimpleDateFormat(strDateFormat1);
        int timeCurrent=Integer.parseInt(dateFormat1.format(date).replace(":",""));

        if (timeCurrent >= currentTime.getStartTime() && timeCurrent <= currentTime.getEndTime()) {
            holder.firstLEcture.setText(currentTime.getSubject());
            currentTime=currentTimes.get(position+1);
            holder.secondLecture.setText(currentTime.getSubject());
        }

    }

    @Override
    public int getItemCount() {
        return currentTimes.size();
    }

    public class TimeViewHolder extends RecyclerView.ViewHolder {

        TextView firstLEcture;
        TextView secondLecture;
        TextView thirdLecture;
        TextView fourthLecture;
        TextView fifthLecture;
        TextView sixthLecture;
        TextView seventhLecture;

        public TimeViewHolder(View itemView) {
            super(itemView);

            firstLEcture=itemView.findViewById(R.id.first_lecture);
            secondLecture=itemView.findViewById(R.id.sec_lecture);
            thirdLecture=itemView.findViewById(R.id.thi_lecture);
            fourthLecture=itemView.findViewById(R.id.fo_lecture);
            fifthLecture=itemView.findViewById(R.id.fif_lecture);
            sixthLecture=itemView.findViewById(R.id.six_lecture);
            seventhLecture=itemView.findViewById(R.id.sev_lecture);
        }
    }
}
