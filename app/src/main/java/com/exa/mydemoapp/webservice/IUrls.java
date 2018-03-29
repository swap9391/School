package com.exa.mydemoapp.webservice;

/**
 * Created by midt-078 on 20/2/18.
 */

public interface IUrls {
    //String BASE_URL = "http://192.168.0.104:8080/SchoolServices/";
    String BASE_URL = "http://kalpatarukids.com:8080/klpschoolservices/";
    String S3_BASE_URL = "https://s3.ap-south-1.amazonaws.com/kk2018photos/";

    String SIGN_UP = BASE_URL + "signup";
    String URL_LOGIN = BASE_URL + "login";
    String URL_IMAGE_UPLOAD = BASE_URL + "uploadimages";
    String URL_IMAGE_LIST = BASE_URL + "getimages";
    String URL_USER_LIST = BASE_URL + "getuserlist";
    String URL_ADD_REWARD = BASE_URL + "savereward";
    String URL_GET_REWARDS = BASE_URL + "getrewards";
    String URL_ADD_EVENTS = BASE_URL + "saveevents";
    String URL_GET_EVENTS = BASE_URL + "getevents";
    String URL_ADD_ATTENDANCE = BASE_URL + "markattendance";
    String URL_GET_ATTENDANCE = BASE_URL + "getattendance";
    String URL_FIREBASE_REG = BASE_URL + "inserttoken";
}
