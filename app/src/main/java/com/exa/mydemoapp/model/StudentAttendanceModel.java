package com.exa.mydemoapp.model;

/**
 * Created by midt-078 on 14/2/18.
 */

public class StudentAttendanceModel {
    private int studentId;
    private boolean present;

    public int getStudentId() {
        return studentId;
    }

    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }

    public boolean isPresent() {
        return present;
    }

    public void setPresent(boolean present) {
        this.present = present;
    }
}
