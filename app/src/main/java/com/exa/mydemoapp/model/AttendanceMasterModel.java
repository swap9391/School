package com.exa.mydemoapp.model;

import com.exa.mydemoapp.database.BasicBean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by midt-078 on 14/2/18.
 */

public class AttendanceMasterModel extends BasicBean implements Serializable {

    private long attendanceDate;
    private String className;
    private String divisionName;
    private List<StudentAttendanceDetailsModel> studentList= new ArrayList<>();

    public long getAttendanceDate() {
        return attendanceDate;
    }

    public void setAttendanceDate(long attendanceDate) {
        this.attendanceDate = attendanceDate;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getDivisionName() {
        return divisionName;
    }

    public void setDivisionName(String divisionName) {
        this.divisionName = divisionName;
    }

    public List<StudentAttendanceDetailsModel> getStudentList() {
        return studentList;
    }

    public void setStudentList(List<StudentAttendanceDetailsModel> studentList) {
        this.studentList = studentList;
    }
}
