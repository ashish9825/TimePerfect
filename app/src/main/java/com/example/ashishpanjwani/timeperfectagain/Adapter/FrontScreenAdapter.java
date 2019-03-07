package com.example.ashishpanjwani.timeperfectagain.Adapter;


import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.ashishpanjwani.timeperfectagain.Interfaces.CustomLectureClickListener;
import com.example.ashishpanjwani.timeperfectagain.Model.CurrentTimeList;
import com.example.ashishpanjwani.timeperfectagain.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class FrontScreenAdapter extends RecyclerView.Adapter<FrontScreenAdapter.FrontViewHolder> {

    private Context context;
    List<CurrentTimeList> currentTimeList;

    public FrontScreenAdapter(Context context, List<CurrentTimeList> currentTimeList) {
        this.context=context;
        this.currentTimeList=currentTimeList;
    }

    @Override
    public FrontViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.lecture_cards,parent,false);
        return new FrontViewHolder(view);
    }

    @Override
    public void onBindViewHolder(FrontViewHolder holder, int position) {
        final CurrentTimeList timeList = currentTimeList.get(position);
        Date date = new Date();
        String strDateFormat1 = "kk:mm";
        DateFormat dateFormat1 = new SimpleDateFormat(strDateFormat1);
        final int timeCurrent = Integer.parseInt(dateFormat1.format(date).replace(":", ""));

        if (position == 0) {
            StringBuilder startTime = new StringBuilder(timeList.getStartTime().toString());
            if (startTime.length()==3) {
                startTime.insert(1, ":");
            } else {
                startTime.insert(2, ":");
            }
            StringBuilder endTime = new StringBuilder(timeList.getEndTime().toString());
            endTime.insert(2, " : ");
            holder.roomNumber.setSelected(true);
            holder.startTime.setText(startTime);
            holder.endTime.setText(endTime);
            holder.subjectName.setText(timeList.getSubject());
            holder.roomNumber.setText(timeList.getRoom());
            if (timeCurrent >= timeList.getStartTime() && timeCurrent < timeList.getEndTime()) {
                holder.current.setVisibility(View.VISIBLE);
            }
        }

        if (position > 0) {
            StringBuilder startTime = new StringBuilder(timeList.getStartTime().toString());
            startTime.insert(2, ":");
            StringBuilder endTime = new StringBuilder(timeList.getEndTime().toString());
            endTime.insert(2, " : ");
            holder.roomNumber.setSelected(true);
            holder.startTime.setText(startTime);
            holder.endTime.setText(endTime);
            holder.subjectName.setText(timeList.getSubject());
            holder.roomNumber.setText(timeList.getRoom());
            if (timeCurrent >= timeList.getStartTime() && timeCurrent < timeList.getEndTime()) {
                holder.current.setVisibility(View.VISIBLE);
            }
        }

        /*if (holder.current.getVisibility() == View.GONE || holder.current.getVisibility() == View.INVISIBLE) {
            if (position == 0) {
                holder.roomNumber.setText("Current Lecture");
                holder.subjectName.setText("Break !");
                holder.startTime.setText("");
                holder.endTime.setText("");
            }
            if (position > 0) {
                holder.cardView.setVisibility(View.GONE);
            }
        }*/
    }

    @Override
    public int getItemCount() {
        return currentTimeList.size();
    }

    public class FrontViewHolder extends RecyclerView.ViewHolder {

        TextView roomNumber;
        TextView subjectName;
        TextView startTime;
        TextView endTime;
        ImageView current;
        CardView cardView;
        RelativeLayout background;

        public FrontViewHolder(View itemView) {
            super(itemView);

            roomNumber = itemView.findViewById(R.id.roomno);
            subjectName = itemView.findViewById(R.id.lecture_name);
            startTime = itemView.findViewById(R.id.starttime);
            endTime = itemView.findViewById(R.id.endtime);
            current = itemView.findViewById(R.id.correct_icon);
            cardView = itemView.findViewById(R.id.card_view);
            background = itemView.findViewById(R.id.background_layout);

            Typeface typeface = Typeface.createFromAsset(itemView.getContext().getAssets(),"fonts/Comfortaa-Regular.ttf");
            Typeface typeface1 = Typeface.createFromAsset(itemView.getContext().getAssets(),"fonts/GOTHICB.TTF");
            Typeface typeface2 = Typeface.createFromAsset(itemView.getContext().getAssets(),"fonts/GOTHIC.TTF");

            roomNumber.setTypeface(typeface1);
            subjectName.setTypeface(typeface);
            startTime.setTypeface(typeface2);
            endTime.setTypeface(typeface2);
        }
    }
}
