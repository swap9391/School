package com.exa.mydemoapp.Common;

import com.exa.mydemoapp.BuildConfig;

/**
 * Created by midt-006 on 10/10/17.
 */

public interface Constants {

    String RESPONSE_MESSAGE = "message";
    String RESPONSE_KEY = "messageType";
    String RESPONSE_INFO = "data";
    String RESPONSE_SUCCESS = "SUCCESS";
    String RESPONSE_ERROR = "FAILURE";

    public static String MAIN_TABLE = BuildConfig.MAIN_TABLE_NAME;
    public static String IMAGE_TABLE = "IMAGETABLE";
    public static String EVENT_TABLE = "EVENTTABLE";
    public static String STUDENT = "STUDENT";
    public static String LOCATION_TABLE = "LOCATION_TABLE";
    public static String COMMUNITY_TABLE = "COMMUNITY_TABLE";
    public static String REWARD_TABLE = "REWARDTABLE";
    public static String DATE_FORMAT = "dd-MM-yyyy hh:mm";
    public static String ONLY_DATE_FORMAT = "dd-MM-yyyy";
    public static String USER_NAME = "USER_NAME";
    public static String STUDENT_NAME = "STUDENT_NAME";
    public static String STUDENT_ID = "STUDENT_ID";
    public static String LAST_VAN_TYPE = "LAST_VAN_TYPE";
    public static int UPDATE_INTERVAL = 3000; // 3 sec
    public static int FATEST_INTERVAL = 3000; // 5 sec
    public static int DISPLACEMENT = 10; // 10 meters
    public static String USER_TYPE = "USER_TYPE";
    public static String USER_TYPE_GUEST = "GUEST";
    public static String USER_TYPE_ADMIN= "MA";
    public static String USER_TYPE_STUDENT = "MS";
    public static String USER_TYPE_DRIVER = "MD";
    public static String USER_TYPE_TEACHER = "MT";
    public static String FB_TITLE = "title";
    public static String FB_MESSAGE = "message";
    public static String FIREBASE = "firebase";
    public static String FIREBASE_REGISTER = "FIREBASE_REGISTER";


    public static int REWARD_SPORT = 10;
    public static int REWARD_CULTURAL = 10;
    public static int REWARD_INTERSCHOOL = 20;
    public static int REWARD_ACADEMICS = 20;
    public static int REWARD_OTHER = 10;
    public static String FEED = "FEED";
    public static String FEED_TYPE_NAME = "Feed Type";
    public static String LIST_TYPE = "mylist";
    public static String INTENT_TYPE_IMAGES = "images";
    public static String INTENT_TYPE_POSITION = "position";
    public static String INTENT_TYPE_FRAGMENT = "frag";
    public static String INTENT_TYPE_GUEST = "Guest";
    public static String INTENT_TYPE_OTP = "Otp";
    public static String INTENT_TYPE_MSG = "Message";
    public static String INTENT_VALUE_GALLERY= "gallery";
    public static String INTENT_VALUE_CHAT= "community";
    public static String INTENT_IS_DROPDOWN= "dropdown";
    public static String INTENT_TYPE_USER_DATA = "userdata";
    public static String INTENT_TYPE_EVENT_DATA= "eventdata";
    public static String TRIP_TYPE = "tripType";
    public static String ROUTE_TYPE = "routType";
    public static String ATTENDANCE_TYPE = "attendanceType";
    public static String ATTENDANCE_IN = "in";
    public static String ATTENDANCE_OUT = "out";
    public static String ATTENDANCE_UPDATE = "update";
    public static String GET_TRIP_TYPE = "gettripType";
    public static String GET_ROUTE_TYPE = "getroutType";

}
