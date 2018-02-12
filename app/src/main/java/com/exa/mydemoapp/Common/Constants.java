package com.exa.mydemoapp.Common;

import com.exa.mydemoapp.BuildConfig;

/**
 * Created by midt-006 on 10/10/17.
 */

public interface Constants {

    public static String MAIN_TABLE = BuildConfig.MAIN_TABLE_NAME;
    public static String IMAGE_TABLE = "IMAGETABLE";
    public static String EVENT_TABLE = "EVENTTABLE";
    public static String STUDENT = "STUDENT";
    public static String LOCATION_TABLE = "LOCATION_TABLE";
    public static String COMMUNITY_TABLE = "COMMUNITY_TABLE";
    public static String REWARD_TABLE = "REWARDTABLE";
    public static String DATE_FORMAT = "dd-MM-yyyy hh:mm";
    public static String USER_NAME = "USER_NAME";
    public static String STUDENT_ID = "STUDENT_ID";
    public static String LAST_VAN_TYPE = "LAST_VAN_TYPE";
    public static int UPDATE_INTERVAL = 3000; // 3 sec
    public static int FATEST_INTERVAL = 3000; // 5 sec
    public static int DISPLACEMENT = 10; // 10 meters
    public static String USER_TYPE = "USER_TYPE";
    public static String USER_TYPE_GUEST = "GUEST";
    public static String USER_TYPE_STUDENT = "STUDENT";

    public static int REWARD_SPORT = 10;
    public static int REWARD_CULTURAL = 10;
    public static int REWARD_INTERSCHOOL = 20;
    public static int REWARD_ACADEMICS = 20;
    public static int REWARD_OTHER = 10;
}
