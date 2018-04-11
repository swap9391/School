package com.exa.mydemoapp.model;

import com.exa.mydemoapp.annotation.Required;
import com.exa.mydemoapp.database.BasicBean;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Swapnil on 03/8/17.
 */

public class AlbumMasterModel extends BasicBean implements Serializable {
    private String albumTitle;
    private String albumDescription;
    private boolean isAlbumVisible;
    private String albumType;
    private String className;
    private String divisionName;
    private String studentId;
    private String img;

    private List<AlbumImagesModel> albumImagesModels= new ArrayList<>();

    public String getAlbumTitle() {
        return albumTitle;
    }

    public void setAlbumTitle(String albumTitle) {
        this.albumTitle = albumTitle;
    }

    public String getAlbumDescription() {
        return albumDescription;
    }

    public void setAlbumDescription(String albumDescription) {
        this.albumDescription = albumDescription;
    }

    public boolean isAlbumVisible() {
        return isAlbumVisible;
    }

    public void setAlbumVisible(boolean albumVisible) {
        isAlbumVisible = albumVisible;
    }

    public String getAlbumType() {
        return albumType;
    }

    public void setAlbumType(String albumType) {
        this.albumType = albumType;
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

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public List<AlbumImagesModel> getAlbumImagesModels() {
        return albumImagesModels;
    }

    public void setAlbumImagesModels(List<AlbumImagesModel> albumImagesModels) {
        this.albumImagesModels = albumImagesModels;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }
}
