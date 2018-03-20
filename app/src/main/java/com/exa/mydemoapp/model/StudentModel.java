package com.exa.mydemoapp.model;

import com.exa.mydemoapp.annotation.Required;
import com.exa.mydemoapp.database.BasicBean;
import com.exa.mydemoapp.database.ContentHolder;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by midt-006 on 11/12/17.
 */

public class StudentModel extends BasicBean implements Serializable {

    @SerializedName("visiblity")
    private String visiblity;
    private String uniqKey;
    @SerializedName("registrationDate")
    private String dateStamp;
    @SerializedName("schoolName")
    @Required(message = "Please Select School Name")
    private String schoolName;
    @SerializedName("classId")
    @Required(message = "Please Select Class Name")
    private String className;
    @SerializedName("divisionId")
    @Required(message = "Please Select Division Name")
    private String division;
    @SerializedName("registrationId")
    @Required(message = "Please Registration Id")
    private String registrationId;
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
    private String gender;
    private int totalFees;
    private String installmentType;
    private String installment1;
    private String installment2;
    private String installment3;
    private String rollNumber;
    private String contactNumber;
    private String dateInsvestment2;
    private String dateInsvestment3;
    private String subscribed;

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

    public String getRegistrationId() {
        return registrationId;
    }

    public void setRegistrationId(String registrationId) {
        this.registrationId = registrationId;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getInstallmentType() {
        return installmentType;
    }

    public void setInstallmentType(String installmentType) {
        this.installmentType = installmentType;
    }

    public String getInstallment1() {
        return installment1;
    }

    public void setInstallment1(String installment1) {
        this.installment1 = installment1;
    }

    public String getInstallment2() {
        return installment2;
    }

    public void setInstallment2(String installment2) {
        this.installment2 = installment2;
    }

    public String getInstallment3() {
        return installment3;
    }

    public void setInstallment3(String installment3) {
        this.installment3 = installment3;
    }

    public int getTotalFees() {
        return totalFees;
    }

    public void setTotalFees(int totalFees) {
        this.totalFees = totalFees;
    }

    public String getRollNumber() {
        return rollNumber;
    }

    public void setRollNumber(String rollNumber) {
        this.rollNumber = rollNumber;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public String getDateInsvestment2() {
        return dateInsvestment2;
    }

    public void setDateInsvestment2(String dateInsvestment2) {
        this.dateInsvestment2 = dateInsvestment2;
    }

    public String getDateInsvestment3() {
        return dateInsvestment3;
    }

    public void setDateInsvestment3(String dateInsvestment3) {
        this.dateInsvestment3 = dateInsvestment3;
    }

    public String getSubscribed() {
        return subscribed;
    }

    public void setSubscribed(String subscribed) {
        this.subscribed = subscribed;
    }

    @Override
    public void dbBinding(ContentHolder holder) {
        super.dbBinding(holder);
        holder.setTable("student_data");
        holder.getValues().put("_id", getId());
        holder.getValues().put("registrationDate", getDateStamp());
        holder.getValues().put("schoolName", getSchoolName());
        holder.getValues().put("className", getClassName());
        holder.getValues().put("divisionId", getDivision());
        holder.getValues().put("registrationId", getRegistrationId());
        holder.getValues().put("studentName", getStudentName());
        holder.getValues().put("studentAddress", getStudentAddress());
        holder.getValues().put("studentUserName", getStudentUserName());
        holder.getValues().put("studentPassword", getStudentPassword());
        holder.getValues().put("studentBloodGrp", getStudentBloodGrp());
        holder.getValues().put("gender", getGender());
        holder.getValues().put("totalFees", "" + getTotalFees());
        holder.getValues().put("installmentType", getInstallmentType());
        holder.getValues().put("installment1", getInstallment1());
        holder.getValues().put("installment2", getInstallment2());
        holder.getValues().put("installment3", getInstallment3());
        holder.getValues().put("rollNumber", getRollNumber());
        holder.getValues().put("contactNumber", getContactNumber());
        holder.getValues().put("dateInsvestment2", getDateInsvestment2());
        holder.getValues().put("dateInsvestment3", getDateInsvestment3());
        holder.getValues().put("subscribed", getSubscribed());
        holder.getValues().put("userType", getUserType());
        holder.getValues().put("visiblity", getVisiblity());
    }
}
