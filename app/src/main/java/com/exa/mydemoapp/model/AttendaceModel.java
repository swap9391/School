package com.exa.mydemoapp.model;

import com.exa.mydemoapp.database.BasicBean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by midt-078 on 14/2/18.
 */

public class AttendaceModel extends BasicBean implements Serializable {

    private String dateStamp;
    private String classId;
    private String divisionId;
    private List<StudentAttendanceModel> studentList= new ArrayList<>();

    public String getDateStamp() {
        return dateStamp;
    }

    public void setDateStamp(String dateStamp) {
        this.dateStamp = dateStamp;
    }

    public String getClassName() {
        return classId;
    }

    public void setClassName(String className) {
        this.classId = className;
    }

    public String getDivisionId() {
        return divisionId;
    }

    public void setDivisionId(String divisionId) {
        this.divisionId = divisionId;
    }

    public List<StudentAttendanceModel> getStudentList() {
        return studentList;
    }

    public void setStudentList(List<StudentAttendanceModel> studentList) {
        this.studentList = studentList;
    }
}
