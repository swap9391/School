package com.exa.mydemoapp.webservice;

/**
 * Created by midt-078 on 20/2/18.
 */

public interface IUrls {
    //String BASE_URL = "http://192.168.0.104:8080/SchoolServices/";
   // String BASE_URL = "http://kalpatarukids.com:8080/klpschoolservices/";
    String S3_BASE_URL = "https://s3.ap-south-1.amazonaws.com/kk2018photos/";
    String BASE_URL = "http://app.kalpatarukids.com:8080/kalpatarukidsdev/api/secure/";
    String BASE_URL1 = "http://app.kalpatarukids.com:8080/kalpatarukidsdev/api/";
    String SIGN_UP = BASE_URL1 + "signup";
    String UPDATE_USER = BASE_URL + "users/%1$s";
 //   String URL_LOGIN = BASE_URL + "login";
    String URL_LOGIN = BASE_URL1 + "signin";
    String URL_IMAGE_UPLOAD = BASE_URL + "album/add";
    String URL_IMAGE_LIST = BASE_URL + "album/images?albumType=%1$s&className=%2$s&divisionName=%3$s&studentId=%4$s";
    String URL_IMAGE_LIST_NEWS_COMMON = BASE_URL + "album/images?albumType=%1$s";
    String URL_USER_LIST = BASE_URL + "users/list";
    String URL_ADD_REWARD = BASE_URL + "student/rewards";
    String URL_GET_REWARDS = BASE_URL + "student/%1$s/rewards";
    String URL_ADD_EVENTS = BASE_URL + "annual-calender/event";
    String URL_GET_EVENTS = BASE_URL + "annual-calender/event/%1$s/%2$s";
    String URL_ADD_ATTENDANCE = BASE_URL + "attendance";
    String URL_GET_ATTENDANCE_LIST = BASE_URL + "attendance/%1$s/%2$s/%3$s";
    String URL_FIREBASE_REG = BASE_URL + "inserttoken";
    String URL_VERIFY_OTP= BASE_URL + "user/%1$s/verify/otp/%2$s";
    String URL_DROPDOWN_LIST = BASE_URL + "dropdown/list";
    String URL_LOG_OUT = BASE_URL1 + "signout";
    String URL_CLASS_WISE_STUDENT= BASE_URL + "users/classwise/%1$s/%2$s";
    String URL_ADD_BUS_LOCATION= BASE_URL + "bus-location/";
    String URL_GET_BUS_LOCATION_LIST= BASE_URL + "bus-location/list/%1$s/%2$s/%3$s";
    String URL_GET_BUS_LATEST_LOCATION= BASE_URL + "bus-location/%1$s/%2$s/%3$s/latest/";
    String URL_GET_FEES= BASE_URL + "fees/%1$s";
    String URL_ADD_HOMEWORK = BASE_URL + "homework";
}
