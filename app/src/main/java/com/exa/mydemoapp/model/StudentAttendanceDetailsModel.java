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
    private boolean isPresent;
    private boolean isStudentIn;
    private boolean isStudentOut;
    private boolean isInSmsSent;
    private boolean isOutSmsSent;
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

    public boolean isPresent() {
        return isPresent;
    }

    public void setPresent(boolean present) {
        isPresent = present;
    }

    public boolean isStudentIn() {
        return isStudentIn;
    }

    public void setStudentIn(boolean studentIn) {
        isStudentIn = studentIn;
    }

    public boolean isStudentOut() {
        return isStudentOut;
    }

    public void setStudentOut(boolean studentOut) {
        isStudentOut = studentOut;
    }

    public boolean isInSmsSent() {
        return isInSmsSent;
    }

    public void setInSmsSent(boolean inSmsSent) {
        isInSmsSent = inSmsSent;
    }

    public boolean isOutSmsSent() {
        return isOutSmsSent;
    }

    public void setOutSmsSent(boolean outSmsSent) {
        isOutSmsSent = outSmsSent;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }
}
