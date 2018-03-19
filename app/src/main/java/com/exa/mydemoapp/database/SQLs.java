package com.exa.mydemoapp.database;

/**
 * Created by Swapnil on 31/01/2016.
 */
public class SQLs {
    private SQLs() {
    }

    public static final String sel_student_data = "Select localId, _id,registrationDate,schoolName,className,divisionId," +
            "registrationId,studentName,studentAddress,studentUserName,studentPassword,userType,studentBloodGrp," +
            "gender,totalFees,installmentType,installment1,installment2,installment3,rollNumber,contactNumber," +
            "dateInsvestment2,dateInsvestment3,subscribed,visiblity" +
            " From student_data Where 1=1";
    public static final String del_class_name = "DELETE from class_data";
    public static final String sel_division_name = "Select _id,class_id,division_name From division_data Where 1=1";
    public static final String del_division_name = "DELETE from division_data";
    public static final String sel_user_data = "Select _id,class_id,division_id,name,mobile,education,blood_group,dob,user_type,user_name,password,profile_local_path,profile_server_path user From user Where 1=1";
    public static final String del_user_data = "DELETE from user";
    public static final String sel_attendance="Select _id,class_id,division_id,user_id,attendance_date,status,user_type from attendance_data Where 1=1";
    public static final String del_attendance = "DELETE from attendance_data";
    public static final String sel_image_file="Select _id,class_id,division_id,user_id,img_date,local_path,server_url,image_name,event_name,user_like from image_file Where 1=1";
    public static final String del_image_file = "DELETE from image_file";
}
