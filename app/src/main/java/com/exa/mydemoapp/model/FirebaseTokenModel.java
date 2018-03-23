package com.exa.mydemoapp.model;

import java.io.Serializable;

/**
 * Created by midt-078 on 23/3/18.
 */

public class FirebaseTokenModel implements Serializable{
    private String token;
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
