package com.exa.mydemoapp.listner;

import com.exa.mydemoapp.model.StudentAttendanceDetailsModel;

/**
 * Created by midt-078 on 20/3/18.
 */

public interface AttendanceListner {
    public void present(StudentAttendanceDetailsModel bean, int position);

    public void absent(StudentAttendanceDetailsModel bean,int position);
}
