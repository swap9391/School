package com.exa.mydemoapp.model;

import com.exa.mydemoapp.database.BasicBean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by midt-078 on 11/4/18.
 */

public class StudentFeesModel extends BasicBean implements Serializable {
    private String userId;
    private double totalFees;
    private int noOfInstallments;
    private List<FeesInstallmentsModel> feesInstallmentsModels = new ArrayList<>();

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public double getTotalFees() {
        return totalFees;
    }

    public void setTotalFees(double totalFees) {
        this.totalFees = totalFees;
    }

    public int getNoOfInstallments() {
        return noOfInstallments;
    }

    public void setNoOfInstallments(int noOfInstallments) {
        this.noOfInstallments = noOfInstallments;
    }

    public List<FeesInstallmentsModel> getFeesInstallmentsModels() {
        return feesInstallmentsModels;
    }

    public void setFeesInstallmentsModels(List<FeesInstallmentsModel> feesInstallmentsModels) {
        this.feesInstallmentsModels = feesInstallmentsModels;
    }
}
