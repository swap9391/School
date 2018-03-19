package com.exa.mydemoapp.webservice;

/**
 * Created by midt-078 on 20/2/18.
 */

public interface IUrls {
    String BASE_URL = "http://192.168.0.104:8080/techSingapore/";
    String S3_BASE_URL = "https://s3.amazonaws.com/kalpataru-deployments-mobilehub-1026292921/";
    String SIGN_UP = BASE_URL + "signup";
    String URL_LOGIN = BASE_URL + "login";
    String URL_IMAGE_UPLOAD = BASE_URL + "uploadimages";
    String URL_IMAGE_LIST = BASE_URL + "getimages";
    String URL_USER_LIST = BASE_URL + "getuserlist";
    String URL_ADD_REWARD = BASE_URL + "savereward";
    String URL_GET_REWARDS = BASE_URL + "getrewards";
}
