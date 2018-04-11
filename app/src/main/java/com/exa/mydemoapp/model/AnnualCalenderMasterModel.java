package com.exa.mydemoapp.model;

import com.exa.mydemoapp.database.BasicBean;

import java.io.Serializable;

/**
 * Created by midt-006 on 11/10/17.
 */

public class AnnualCalenderMasterModel extends BasicBean implements Serializable {

    private String eventName;
    private long eventDate;
    private String eventType;
    private String className;
    private String divisionName;
    private boolean isVisible;

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public long getEventDate() {
        return eventDate;
    }

    public void setEventDate(long eventDate) {
        this.eventDate = eventDate;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
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

    public boolean isVisible() {
        return isVisible;
    }

    public void setVisible(boolean visible) {
        isVisible = visible;
    }
}
