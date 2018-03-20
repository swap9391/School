package com.exa.mydemoapp.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by midt-078 on 14/2/18.
 */

public class AttendaceModel implements Serializable {

    private String dateStamp;
    private String className;
    private List<StudentAttendanceModel> studentAttendanceModels= new ArrayList<>();

    public String getDateStamp() {
        return dateStamp;
    }

    public void setDateStamp(String dateStamp) {
        this.dateStamp = dateStamp;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public List<StudentAttendanceModel> getStudentAttendanceModels() {
        return studentAttendanceModels;
    }

    public void setStudentAttendanceModels(List<StudentAttendanceModel> studentAttendanceModels) {
        this.studentAttendanceModels = studentAttendanceModels;
    }
}
