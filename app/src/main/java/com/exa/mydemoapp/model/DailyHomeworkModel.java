package com.exa.mydemoapp.model;

import com.exa.mydemoapp.database.BasicBean;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by midt-078 on 11/4/18.
 */

public class DailyHomeworkModel extends BasicBean implements Serializable {


    private String studentId;
    private String className;
    private String divisionName;
    private String subjectName;
    private String description;
    private long homeworkDate;
    @SerializedName("images")
    private List<HomeWorkImageModel> albumImagesModel = new ArrayList<>();

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getDivisionName() {
        return divisionName;
    }

    public void setDivisionName(String divisionName) {
        this.divisionName = divisionName;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public long getHomeworkDate() {
        return homeworkDate;
    }

    public void setHomeworkDate(long homeworkDate) {
        this.homeworkDate = homeworkDate;
    }

    public List<HomeWorkImageModel> getAlbumImagesModel() {
        return albumImagesModel;
    }

    public void setAlbumImagesModel(List<HomeWorkImageModel> albumImagesModel) {
        this.albumImagesModel = albumImagesModel;
    }
}
