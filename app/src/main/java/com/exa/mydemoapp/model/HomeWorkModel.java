package com.exa.mydemoapp.model;

import com.exa.mydemoapp.database.BasicBean;

import java.io.Serializable;

/**
 * Created by midt-078 on 26/3/18.
 */

public class HomeWorkModel extends BasicBean implements Serializable {
    private String classId;
    private String subject;
    private String description;
    private String dataStamp;

    public String getClassId() {
        return classId;
    }

    public void setClassId(String classId) {
        this.classId = classId;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDataStamp() {
        return dataStamp;
    }

    public void setDataStamp(String dataStamp) {
        this.dataStamp = dataStamp;
    }
}
