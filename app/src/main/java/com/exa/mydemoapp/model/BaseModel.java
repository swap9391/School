package com.exa.mydemoapp.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Swapnil on 15/03/2018.
 */

public class BaseModel implements Serializable {
    @SerializedName("_id")
    private int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
