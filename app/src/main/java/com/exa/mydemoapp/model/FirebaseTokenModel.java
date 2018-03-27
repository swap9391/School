package com.exa.mydemoapp.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by midt-078 on 23/3/18.
 */

public class FirebaseTokenModel extends BaseModel implements Serializable{
    @SerializedName("tokenNo")
    private String token;
    @SerializedName("userId")
    private String studentId;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }
}
