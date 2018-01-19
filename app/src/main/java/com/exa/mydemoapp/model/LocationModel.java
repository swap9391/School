package com.exa.mydemoapp.model;

import java.io.Serializable;

/**
 * Created by root on 1/6/17.
 */

public class LocationModel implements Serializable {
    private String uniqKey;
    Double lattitude;
    Double longitude;
    String dateStamp;
    String vanType;

    public Double getLattitude() {
        return lattitude;
    }

    public void setLattitude(Double lattitude) {
        this.lattitude = lattitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public String getUniqKey() {
        return uniqKey;
    }

    public void setUniqKey(String uniqKey) {
        this.uniqKey = uniqKey;
    }

    public String getDateStamp() {
        return dateStamp;
    }

    public void setDateStamp(String dateStamp) {
        this.dateStamp = dateStamp;
    }

    public String getVanType() {
        return vanType;
    }

    public void setVanType(String vanType) {
        this.vanType = vanType;
    }
}
