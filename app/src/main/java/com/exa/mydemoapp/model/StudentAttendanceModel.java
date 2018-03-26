package com.exa.mydemoapp.model;

import com.exa.mydemoapp.database.BasicBean;

import java.io.Serializable;

/**
 * Created by midt-078 on 14/2/18.
 */

public class StudentAttendanceModel extends BasicBean implements Serializable {
    private Integer studentId;
    private String present;
    private String in;
    private String out;
    private String inSms;
    private String outSms;
    private String studentName;

    public Integer getStudentId() {
        return studentId;
    }

    public void setStudentId(Integer studentId) {
        this.studentId = studentId;
    }

    public String getPresent() {
        return present;
    }

    public void setPresent(String present) {
        this.present = present;
    }

    public String getIn() {
        return in;
    }

    public void setIn(String in) {
        this.in = in;
    }

    public String getOut() {
        return out;
    }

    public void setOut(String out) {
        this.out = out;
    }

    public String getInSms() {
        return inSms;
    }

    public void setInSms(String inSms) {
        this.inSms = inSms;
    }

    public String getOutSms() {
        return outSms;
    }

    public void setOutSms(String outSms) {
        this.outSms = outSms;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }
}

