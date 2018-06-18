package com.exa.mydemoapp.model;

import com.exa.mydemoapp.database.BasicBean;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by midt-078 on 11/4/18.
 */

public class BusLocationsModel extends BasicBean implements Serializable, Comparable<BusLocationsModel> {
    private String latitude;
    private String longitude;
    private String busRoute;
    private String busTripType;
    private long tripDate;
    Date dateTime;

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getBusRoute() {
        return busRoute;
    }

    public void setBusRoute(String busRoute) {
        this.busRoute = busRoute;
    }

    public String getBusTripType() {
        return busTripType;
    }

    public void setBusTripType(String busTripType) {
        this.busTripType = busTripType;
    }

    public long getTripDate() {
        return tripDate;
    }

    public void setTripDate(long tripDate) {
        this.tripDate = tripDate;
    }

    public Date getDateTime() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(getTripDate());
        Date date = calendar.getTime();
        return date;
    }

    public void setDateTime(Date dateTime) {
        this.dateTime = dateTime;
    }

    @Override
    public int compareTo(BusLocationsModel o) {
        return getDateTime().compareTo(o.getDateTime());
    }

}
