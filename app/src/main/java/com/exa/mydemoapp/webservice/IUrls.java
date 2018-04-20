package com.exa.mydemoapp.webservice;

/**
 * Created by midt-078 on 20/2/18.
 */

public interface IUrls {
    //String BASE_URL = "http://192.168.0.104:8080/SchoolServices/";
   // String BASE_URL = "http://kalpatarukids.com:8080/klpschoolservices/";
    String S3_BASE_URL = "https://s3.ap-south-1.amazonaws.com/kk2018photos/";
    String BASE_URL = "http://app.kalpatarukids.com:8080/kalpatarukidsdev/api/";

    String SIGN_UP = BASE_URL + "signup";
 //   String URL_LOGIN = BASE_URL + "login";
    String URL_LOGIN = BASE_URL + "signin";
    String URL_IMAGE_UPLOAD = BASE_URL + "album/add";
    String URL_IMAGE_LIST = BASE_URL + "getimages";
    String URL_USER_LIST = BASE_URL + "users/list";
    String URL_ADD_REWARD = BASE_URL + "savereward";
    String URL_GET_REWARDS = BASE_URL + "getrewards";
    String URL_ADD_EVENTS = BASE_URL + "saveevents";
    String URL_GET_EVENTS = BASE_URL + "getevents";
    String URL_ADD_ATTENDANCE = BASE_URL + "markattendance";
    String URL_GET_ATTENDANCE = BASE_URL + "getattendance";
    String URL_FIREBASE_REG = BASE_URL + "inserttoken";
    String URL_VERIFY_OTP= BASE_URL + "user/%1$s/verify/otp/%2$s";
    String URL_DROPDOWN_LIST = BASE_URL + "dropdown/list";
    String URL_LOG_OUT = BASE_URL + "signout";
    String URL_CLASS_WISE_STUDENT= BASE_URL + "users/classwise/%1$s/%2$s";

}
