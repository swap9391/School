package com.exa.mydemoapp.model;

import com.exa.mydemoapp.database.BasicBean;

import java.io.Serializable;

/**
 * Created by midt-078 on 11/4/18.
 */

public class FeesInstallmentsModel extends BasicBean implements Serializable {
    private String studentFeeId;
    private String installmentNo;
    private String installmentLocalValue;
    private double installmentAmount;
    private long installmentDate;
    private String paymentMode;
    private boolean isPaid;
    private String chequeBankName;
    private String chequeNo;
    private String chequeImage;

    public String getStudentFeeId() {
        return studentFeeId;
    }

    public void setStudentFeeId(String studentFeeId) {
        this.studentFeeId = studentFeeId;
    }

    public String getInstallmentNo() {
        return installmentNo;
    }

    public void setInstallmentNo(String installmentNo) {
        this.installmentNo = installmentNo;
    }

    public double getInstallmentAmount() {
        return installmentAmount;
    }

    public void setInstallmentAmount(double installmentAmount) {
        this.installmentAmount = installmentAmount;
    }

    public long getInstallmentDate() {
        return installmentDate;
    }

    public void setInstallmentDate(long installmentDate) {
        this.installmentDate = installmentDate;
    }

    public String getPaymentMode() {
        return paymentMode;
    }

    public void setPaymentMode(String paymentMode) {
        this.paymentMode = paymentMode;
    }

    public boolean isPaid() {
        return isPaid;
    }

    public void setPaid(boolean paid) {
        isPaid = paid;
    }

    public String getChequeBankName() {
        return chequeBankName;
    }

    public void setChequeBankName(String chequeBankName) {
        this.chequeBankName = chequeBankName;
    }

    public String getChequeNo() {
        return chequeNo;
    }

    public void setChequeNo(String chequeNo) {
        this.chequeNo = chequeNo;
    }

    public String getChequeImage() {
        return chequeImage;
    }

    public void setChequeImage(String chequeImage) {
        this.chequeImage = chequeImage;
    }

    public String getInstallmentLocalValue() {
        return installmentLocalValue;
    }

    public void setInstallmentLocalValue(String installmentLocalValue) {
        this.installmentLocalValue = installmentLocalValue;
    }
}
