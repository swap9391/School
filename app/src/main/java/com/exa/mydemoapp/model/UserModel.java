package com.exa.mydemoapp.model;

import com.exa.mydemoapp.annotation.Required;
import com.exa.mydemoapp.database.BasicBean;
import com.exa.mydemoapp.database.ContentHolder;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by midt-006 on 11/12/17.
 */

public class UserModel extends BasicBean implements Serializable {

    private String username;
    private String password;
    private String userType;
    private String firstName;
    private String middleName;
    private String lastName;
    private String profilePicUrl;
    private String email;
    private int contactNumber;
    private boolean contactNumberVerified;
    private long lastLoginAt;
    private String busRoute;
    private boolean credentialsExpired;
    private boolean accountEnabled;
    private boolean accountExpired;
    private boolean accountLocked;
    private boolean isActive;
    private StudentFeesModel studentFeesModel= new StudentFeesModel();
    private UserInfoModel userInfoModel= new UserInfoModel();
    private UserDevice userDevice= new UserDevice();


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getProfilePicUrl() {
        return profilePicUrl;
    }

    public void setProfilePicUrl(String profilePicUrl) {
        this.profilePicUrl = profilePicUrl;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(int contactNumber) {
        this.contactNumber = contactNumber;
    }

    public boolean isContactNumberVerified() {
        return contactNumberVerified;
    }

    public void setContactNumberVerified(boolean contactNumberVerified) {
        this.contactNumberVerified = contactNumberVerified;
    }

    public long getLastLoginAt() {
        return lastLoginAt;
    }

    public void setLastLoginAt(long lastLoginAt) {
        this.lastLoginAt = lastLoginAt;
    }

    public String getBusRoute() {
        return busRoute;
    }

    public void setBusRoute(String busRoute) {
        this.busRoute = busRoute;
    }

    public boolean isCredentialsExpired() {
        return credentialsExpired;
    }

    public void setCredentialsExpired(boolean credentialsExpired) {
        this.credentialsExpired = credentialsExpired;
    }

    public boolean isAccountEnabled() {
        return accountEnabled;
    }

    public void setAccountEnabled(boolean accountEnabled) {
        this.accountEnabled = accountEnabled;
    }

    public boolean isAccountExpired() {
        return accountExpired;
    }

    public void setAccountExpired(boolean accountExpired) {
        this.accountExpired = accountExpired;
    }

    public boolean isAccountLocked() {
        return accountLocked;
    }

    public void setAccountLocked(boolean accountLocked) {
        this.accountLocked = accountLocked;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public StudentFeesModel getStudentFeesModel() {
        return studentFeesModel;
    }

    public void setStudentFeesModel(StudentFeesModel studentFeesModel) {
        this.studentFeesModel = studentFeesModel;
    }

    public UserInfoModel getUserInfoModel() {
        return userInfoModel;
    }

    public void setUserInfoModel(UserInfoModel userInfoModel) {
        this.userInfoModel = userInfoModel;
    }

    public UserDevice getUserDevice() {
        return userDevice;
    }

    public void setUserDevice(UserDevice userDevice) {
        this.userDevice = userDevice;
    }

    /*@Override
    public void dbBinding(ContentHolder holder) {
        super.dbBinding(holder);
        holder.setTable("student_data");
        holder.getValues().put("_id", getPkeyId());
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
    }*/
}
