package com.exa.mydemoapp.model;

import com.exa.mydemoapp.database.BasicBean;

import java.io.Serializable;

/**
 * Created by midt-078 on 11/4/18.
 */

public class AlbumImagesModel extends BasicBean implements Serializable {
    private String albumId;
    private String imageUrl;
    private boolean isImageVisible;

    public String getAlbumId() {
        return albumId;
    }

    public void setAlbumId(String albumId) {
        this.albumId = albumId;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public boolean isImageVisible() {
        return isImageVisible;
    }

    public void setImageVisible(boolean imageVisible) {
        isImageVisible = imageVisible;
    }
}
