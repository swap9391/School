package com.exa.mydemoapp.model;

import com.exa.mydemoapp.database.BasicBean;

import java.io.Serializable;

/**
 * Created by midt-078 on 11/4/18.
 */

public class OtpMasterModel extends BasicBean implements Serializable {
    private int contactNumber;
    private String otp;

    public int getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(int contactNumber) {
        this.contactNumber = contactNumber;
    }

    public String getOtp() {
        return otp;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }
}
