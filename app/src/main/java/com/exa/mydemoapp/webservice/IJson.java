package com.exa.mydemoapp.webservice;

import com.exa.mydemoapp.annotation.Required;

/**
 * Created by midt-078 on 20/2/18.
 */

public interface IJson {

    //student
    String schoolName = "schoolName";
    String className = "classId";
    String division = "divisionId";
    String registrationId = "registrationId";
    String studentName = "studentName";
    String studentAddress = "studentAddress";
    String studentUserName = "studentUserName";
    String studentPassword = "studentPassword";
    String userType = "userType";
    String studentBloodGrp = "studentBloodGrp";
    String gender = "gender";
    String totalFees = "totalFees";
    String installmentType = "installmentType";
    String installment1 = "installment1";
    String installment2 = "installment2";
    String installment3 = "installment3";
    String dateInsvestment2 = "dateInsvestment2";
    String dateInsvestment3 = "dateInsvestment3";
    String rollNumber = "rollNumber";
    String contactNumber = "contactNumber";

    //image data
    String imgTitle = "imgTitle";
    String description = "description";
    String rating = "rating";
    String userName = "userName";
    String imageType = "imageType";
    String dateStamp = "dateStamp";
    String classId = "classId";
    String divisionId = "divisionId";
    String studentId = "studentId";
    String images = "imageList";

    //Reward data
    String rewardType = "rewardType";
    String points = "points";

    //Attendance
    String studentList = "studentList";
    String inStatus = "inStatus";

    //firebase token
    String token = "token";
}
