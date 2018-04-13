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
    private String contactNumber;
    private boolean contactNumberVerified;
    private long lastLoginAt;
    private String busRoute;
    private boolean credentialsExpired;
    private boolean accountEnabled;
    private boolean accountExpired;
    private boolean accountLocked;
    private boolean isActive;
    private String loginFrom = "APP";
    private StudentFeesModel studentFeesModel = new StudentFeesModel();
    private UserInfoModel userInfoModel = new UserInfoModel();
    private UserDevice userDevice = new UserDevice();


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

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
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

    public String getLoginFrom() {
        return loginFrom;
    }

    public void setLoginFrom(String loginFrom) {
        this.loginFrom = loginFrom;
    }

    public UserDevice getUserDevice() {
        return userDevice;
    }

    public void setUserDevice(UserDevice userDevice) {
        this.userDevice = userDevice;
    }


    @Override
    public void dbBinding(ContentHolder holder) {
        super.dbBinding(holder);
        holder.setTable("user_data");
        holder.getValues().put("pkeyId", getPkeyId());
        holder.getValues().put("username", getUsername());
        holder.getValues().put("password", getPassword());
        holder.getValues().put("userType", getUserType());
        holder.getValues().put("firstName", getFirstName());
        holder.getValues().put("middleName", getMiddleName());
        holder.getValues().put("lastName", getLastName());
        holder.getValues().put("profilePicUrl", getProfilePicUrl());
        holder.getValues().put("email", getEmail());
        holder.getValues().put("contactNumber",  getContactNumber());
        holder.getValues().put("busRoute", getBusRoute());
        holder.getValues().put("sessionKey", getSessionKey());

    }
}
