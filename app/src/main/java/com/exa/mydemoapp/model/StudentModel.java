package com.exa.mydemoapp.model;

import com.exa.mydemoapp.annotation.Required;

import java.io.Serializable;

/**
 * Created by midt-006 on 11/12/17.
 */

public class StudentModel implements Serializable {
    private String visiblity;
    private String uniqKey;
    private String dateStamp;
    @Required(message = "Please Select School Name")
    private String schoolName;
    @Required(message = "Please Select Class Name")
    private String className;
    @Required(message = "Please Select Division Name")
    private String division;
    @Required(message = "Please Enter Student Name")
    private String studentName;
    @Required(message = "Please Enter Student Address")
    private String studentAddress;
    @Required(message = "Please Enter User Name")
    private String studentUserName;
    @Required(message = "Please Enter Password")
    private String studentPassword;
    private String userType;
    private String studentBloodGrp;

    public String getVisiblity() {
        return visiblity;
    }

    public void setVisiblity(String visiblity) {
        this.visiblity = visiblity;
    }

    public String getUniqKey() {
        return uniqKey;
    }

    public void setUniqKey(String uniqKey) {
        this.uniqKey = uniqKey;
    }

    public String getSchoolName() {
        return schoolName;
    }

    public void setSchoolName(String schoolName) {
        this.schoolName = schoolName;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getDivision() {
        return division;
    }

    public void setDivision(String division) {
        this.division = division;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getStudentAddress() {
        return studentAddress;
    }

    public void setStudentAddress(String studentAddress) {
        this.studentAddress = studentAddress;
    }

    public String getStudentBloodGrp() {
        return studentBloodGrp;
    }

    public void setStudentBloodGrp(String studentBloodGrp) {
        this.studentBloodGrp = studentBloodGrp;
    }

    public String getDateStamp() {
        return dateStamp;
    }

    public void setDateStamp(String dateStamp) {
        this.dateStamp = dateStamp;
    }

    public String getStudentPassword() {
        return studentPassword;
    }

    public void setStudentPassword(String studentPassword) {
        this.studentPassword = studentPassword;
    }

    public String getStudentUserName() {
        return studentUserName;
    }

    public void setStudentUserName(String studentUserName) {
        this.studentUserName = studentUserName;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }
}
