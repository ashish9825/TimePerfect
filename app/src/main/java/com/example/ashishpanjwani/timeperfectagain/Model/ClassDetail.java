
package com.example.ashishpanjwani.timeperfectagain.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ClassDetail {

    @SerializedName("college")
    @Expose
    private String college;
    @SerializedName("branch")
    @Expose
    private String branch;
    @SerializedName("sem")
    @Expose
    private Integer sem;
    @SerializedName("class_rep")
    @Expose
    private String classRep;
    @SerializedName("mentor")
    @Expose
    private String mentor;
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

    public Integer getSem() {
        return sem;
    }

    public void setSem(Integer sem) {
        this.sem = sem;
    }

    public String getClassRep() {
        return classRep;
    }

    public void setClassRep(String classRep) {
        this.classRep = classRep;
    }

    public String getMentor() {
        return mentor;
    }

    public void setMentor(String mentor) {
        this.mentor = mentor;
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

}
