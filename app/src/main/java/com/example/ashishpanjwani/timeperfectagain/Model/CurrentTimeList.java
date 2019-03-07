
package com.example.ashishpanjwani.timeperfectagain.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CurrentTimeList {

    @SerializedName("start_time")
    @Expose
    private Integer startTime;
    @SerializedName("subject")
    @Expose
    private String subject;
    @SerializedName("teacher_incharge")
    @Expose
    private String teacherIncharge;
    @SerializedName("room")
    @Expose
    private String room;
    @SerializedName("end_time")
    @Expose
    private Integer endTime;

    //State of the item
    private boolean expanded;

    public Integer getStartTime() {
        return startTime;
    }

    public void setStartTime(Integer startTime) {
        this.startTime = startTime;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getTeacherIncharge() {
        return teacherIncharge;
    }

    public void setTeacherIncharge(String teacherIncharge) {
        this.teacherIncharge = teacherIncharge;
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public Integer getEndTime() {
        return endTime;
    }

    public void setEndTime(Integer endTime) {
        this.endTime = endTime;
    }

    public void setExpanded(boolean expanded) {
        this.expanded = expanded;
    }

    public boolean isExpanded() {
        return expanded;
    }
}
