package com.exa.mydemoapp.model;

import com.exa.mydemoapp.database.BasicBean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by midt-078 on 11/4/18.
 */

public class StudentFeesModel extends BasicBean implements Serializable {
    private String studentId;
    private String studentName;
    private double totalFees;
    private double pendingFees;
    private String noOfInstallments;

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public double getTotalFees() {
        return totalFees;
    }

    public void setTotalFees(double totalFees) {
        this.totalFees = totalFees;
    }

    public double getPendingFees() {
        return pendingFees;
    }

    public void setPendingFees(double pendingFees) {
        this.pendingFees = pendingFees;
    }

    public String getNoOfInstallments() {
        return noOfInstallments;
    }

    public void setNoOfInstallments(String noOfInstallments) {
        this.noOfInstallments = noOfInstallments;
    }
}
