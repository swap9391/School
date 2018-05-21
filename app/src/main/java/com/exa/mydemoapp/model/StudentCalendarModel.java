package com.exa.mydemoapp.model;

import com.exa.mydemoapp.database.BasicBean;

import java.io.Serializable;

/**
 * Created by Swapnil on 19/05/2018.
 */

public class StudentCalendarModel extends BasicBean implements Serializable {
    private long attendanceDate;
    private boolean isPresent;

    public long getAttendanceDate() {
        return attendanceDate;
    }

    public void setAttendanceDate(long attendanceDate) {
        this.attendanceDate = attendanceDate;
    }

    public boolean isPresent() {
        return isPresent;
    }

    public void setPresent(boolean present) {
        isPresent = present;
    }
}
