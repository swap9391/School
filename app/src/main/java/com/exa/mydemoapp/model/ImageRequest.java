package com.exa.mydemoapp.model;

import com.exa.mydemoapp.annotation.Required;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Swapnil on 03/8/17.
 */

public class ImageRequest implements Serializable {
    @Required(message = "Place Name")
    private String placeName;
    private String description;
    private String rating;
    private String visiblity;
    private String userName;
    private String img;
    private String uniqKey;
    private Integer image_like;
    private String imageType;
    private String dateStamp;
    private String className;
    private String studentId;
    private List<String> imageUrl = new ArrayList<>();

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getPlaceName() {
        return placeName;
    }

    public void setPlaceName(String placeName) {
        this.placeName = placeName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getVisiblity() {
        return visiblity;
    }

    public void setVisiblity(String visiblity) {
        this.visiblity = visiblity;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUniqKey() {
        return uniqKey;
    }

    public void setUniqKey(String uniqKey) {
        this.uniqKey = uniqKey;
    }

    public List<String> getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(List<String> imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Integer getImage_like() {
        return image_like;
    }

    public void setImage_like(Integer image_like) {
        this.image_like = image_like;
    }

    public String getImageType() {
        return imageType;
    }

    public void setImageType(String imageType) {
        this.imageType = imageType;
    }

    public String getDateStamp() {
        return dateStamp;
    }

    public void setDateStamp(String dateStamp) {
        this.dateStamp = dateStamp;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }
}
