package com.exa.mydemoapp.listner;

import com.exa.mydemoapp.model.StudentAttendanceModel;
import com.exa.mydemoapp.model.StudentModel;

/**
 * Created by midt-078 on 20/3/18.
 */

public interface AttendanceListner {
    public void present(StudentAttendanceModel bean);

    public void absent(StudentAttendanceModel bean);
}
