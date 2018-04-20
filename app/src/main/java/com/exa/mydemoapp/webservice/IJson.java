package com.exa.mydemoapp.webservice;

import com.exa.mydemoapp.annotation.Required;

/**
 * Created by midt-078 on 20/2/18.
 */

public interface IJson {

    //student
    String id = "pkeyId";
    String schoolName = "schoolName";
    String className = "className";
    String division = "divisionName";
    String registrationId = "registrationId";
    String firstName = "firstName";
    String middleName = "middleName";
    String lastName = "lastName";
    String profilePicUrl = "profilePicUrl";
    String username = "username";
    String password = "password";
    String userType = "userType";
    String email = "email";
    String busRoute = "busRoute";
    String contactNumberVerified = "contactNumberVerified";
    String contactNumber = "contactNumber";
    String studentFeesModel = "studentFeesModel";
    String userInfoModel = "userInfoModel";
    String userDevice = "userDevice";
    String loginFrom = "loginFrom";


    //image data
    String imgTitle = "albumTitle";
    String description = "albumDescription";
    String rating = "rating";
    String userName = "userName";
    String imageType = "albumType";
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

    //Calender
    String eventName = "eventName";
    String eventDate = "eventDate";
    String eventType = "eventType";


    //firebase token
    String token = "tokenNo";
    String userId = "userId";

    //Homework
    String subject = "subject";
    String deviceDetails = "deviceDetails";

    //otp
    String otp="otp";

}
