
package com.example.ashishpanjwani.timeperfectagain.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DayWiseList {

    @SerializedName("college")
    @Expose
    private String college;
    @SerializedName("branch")
    @Expose
    private String branch;
    @SerializedName("semester")
    @Expose
    private Integer semester;
    @SerializedName("subject")
    @Expose
    private String subject;
    @SerializedName("teacher_incharge")
    @Expose
    private String teacherIncharge;
    @SerializedName("start_time")
    @Expose
    private Integer startTime;
    @SerializedName("end_time")
    @Expose
    private Integer endTime;
    @SerializedName("day")
    @Expose
    private String day;
    @SerializedName("room")
    @Expose
    private String room;

    public String getCollege() {
        return college;
    }

    public void setCollege(String college) {
        this.college = college;
    }

    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

    public Integer getSemester() {
        return semester;
    }

    public void setSemester(Integer semester) {
        this.semester = semester;
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

    public Integer getStartTime() {
        return startTime;
    }

    public void setStartTime(Integer startTime) {
        this.startTime = startTime;
    }

    public Integer getEndTime() {
        return endTime;
    }

    public void setEndTime(Integer endTime) {
        this.endTime = endTime;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

}
