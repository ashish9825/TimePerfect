package com.example.ashishpanjwani.timeperfectagain.Adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.ashishpanjwani.timeperfectagain.Model.CurrentTimeList;
import com.example.ashishpanjwani.timeperfectagain.R;

import java.util.List;

public class TimeAdapter extends RecyclerView.Adapter<TimeAdapter.ViewHolder> {

    private Context context;
    List<CurrentTimeList> currentTimeList;
    Typeface typeface1,typeface2,typeface3,typeface4;

    public TimeAdapter(Context context,List<CurrentTimeList> currentTimeList) {
        this.context=context;
        this.currentTimeList=currentTimeList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.lecture_schedule,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder( ViewHolder holder, int position) {
        final CurrentTimeList timeList = currentTimeList.get(position);
        if (position == 0) {
            StringBuilder startTime = new StringBuilder(timeList.getStartTime().toString());
            startTime.insert(1, ":");
            StringBuilder endTime = new StringBuilder(timeList.getEndTime().toString());
            endTime.insert(2, " : ");
            holder.lectureStart.setText(startTime);
            holder.lectureEnd.setText(endTime);
            int pos=position+1;
            holder.lectureNumber.setText(""+pos+"st Lecture");
            holder.lectureName.setSelected(true);
            holder.lectureName.setText(timeList.getSubject());
            holder.lectureRoom.setSelected(true);
            holder.lectureRoom.setText(timeList.getRoom());
        }

        if (position == 1) {
            StringBuilder startTime = new StringBuilder(timeList.getStartTime().toString());
            startTime.insert(2, ":");
            StringBuilder endTime = new StringBuilder(timeList.getEndTime().toString());
            endTime.insert(2, " : ");
            holder.lectureStart.setText(startTime);
            holder.lectureEnd.setText(endTime);
            int pos = position + 1;
            holder.lectureNumber.setText("" + pos + "nd Lecture");
            holder.lectureName.setSelected(true);
            holder.lectureName.setText(timeList.getSubject());
            holder.lectureRoom.setSelected(true);
            holder.lectureRoom.setText(timeList.getRoom());
        }
        if (position == 2) {
            StringBuilder startTime = new StringBuilder(timeList.getStartTime().toString());
            startTime.insert(2, ":");
            StringBuilder endTime = new StringBuilder(timeList.getEndTime().toString());
            endTime.insert(2, " : ");
            holder.lectureStart.setText(startTime);
            holder.lectureEnd.setText(endTime);
            int pos = position + 1;
            holder.lectureNumber.setText("" + pos + "rd Lecture");
            holder.lectureName.setSelected(true);
            holder.lectureName.setText(timeList.getSubject());
            holder.lectureRoom.setSelected(true);
            holder.lectureRoom.setText(timeList.getRoom());
        }

        if (position > 2) {
            StringBuilder startTime = new StringBuilder(timeList.getStartTime().toString());
            startTime.insert(2, ":");
            StringBuilder endTime = new StringBuilder(timeList.getEndTime().toString());
            endTime.insert(2, " : ");
            holder.lectureStart.setText(startTime);
            holder.lectureEnd.setText(endTime);
            int pos = position + 1;
            holder.lectureNumber.setText("" + pos + "th Lecture");
            holder.lectureName.setSelected(true);
            holder.lectureName.setText(timeList.getSubject());
            holder.lectureRoom.setSelected(true);
            holder.lectureRoom.setText(timeList.getRoom());
        }
    }

    @Override
    public int getItemCount() {
        return currentTimeList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView lectureNumber;
        TextView lectureName;
        TextView lectureRoom;
        TextView lectureStart;
        TextView lectureEnd;

        public ViewHolder(View itemView) {
            super(itemView);
            lectureNumber = itemView.findViewById(R.id.lecture_number);
            lectureName = itemView.findViewById(R.id.subject_name);
            lectureRoom = itemView.findViewById(R.id.room_no);
            lectureStart = itemView.findViewById(R.id.start_time);
            lectureEnd = itemView.findViewById(R.id.end_time);

            typeface1=Typeface.createFromAsset(itemView.getContext().getAssets(),"fonts/GOTHIC.TTF");
            typeface2=Typeface.createFromAsset(itemView.getContext().getAssets(),"fonts/Comfortaa-Regular.ttf");
            typeface3=Typeface.createFromAsset(itemView.getContext().getAssets(),"fonts/CaviarDreams.ttf");
            typeface4=Typeface.createFromAsset(itemView.getContext().getAssets(),"fonts/GOTHICB.TTF");

            this.lectureNumber.setTypeface(typeface4);
            this.lectureName.setTypeface(typeface1);
            this.lectureRoom.setTypeface(typeface2);
            this.lectureStart.setTypeface(typeface1);
            this.lectureEnd.setTypeface(typeface1);
        }
    }
}
