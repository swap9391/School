package com.exa.mydemoapp.model;

import com.exa.mydemoapp.database.BasicBean;

public class HomeWorkImageModel extends BasicBean {

    String dailyHomeworkId;
    String imageUrl;

    public String getDailyHomeworkId() {
        return dailyHomeworkId;
    }

    public void setDailyHomeworkId(String dailyHomeworkId) {
        this.dailyHomeworkId = dailyHomeworkId;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
