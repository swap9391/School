package com.exa.mydemoapp.model;

import java.util.Date;

/**
 * Created by midt-006 on 11/10/17.
 */

public class DateModel {
    Date date;
    int position;
    String eventType;
    String isPresent;

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public String isPresent() {
        return isPresent;
    }

    public void setPresent(String present) {
        isPresent = present;
    }
}
