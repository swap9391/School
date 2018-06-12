package com.exa.mydemoapp.model;

import com.exa.mydemoapp.database.BasicBean;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by midt-078 on 11/4/18.
 */

public class StudentAttendanceDetailsModel extends BasicBean implements Serializable {
    private String attendanceId;
    private String studentId;
    private String isPresent;
    private String isOut;
    private String studentName;

    public String getAttendanceId() {
        return attendanceId;
    }

    public void setAttendanceId(String attendanceId) {
        this.attendanceId = attendanceId;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getIsPresent() {
        return isPresent;
    }

    public void setIsPresent(String isPresent) {
        this.isPresent = isPresent;
    }

    public String getIsOut() {
        return isOut;
    }

    public void setIsOut(String isOut) {
        this.isOut = isOut;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }
}
