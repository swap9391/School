package com.exa.mydemoapp.model;

/**
 * Created by midt-078 on 14/2/18.
 */

public class StudentAttendanceModel {
    private String studentId;
    private boolean present;

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public boolean isPresent() {
        return present;
    }

    public void setPresent(boolean present) {
        this.present = present;
    }
}
