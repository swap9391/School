package com.exa.mydemoapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.Request;
import com.exa.mydemoapp.Common.AppController;
import com.exa.mydemoapp.Common.CommonActivity;
import com.exa.mydemoapp.Common.CommonUtils;
import com.exa.mydemoapp.Common.Connectivity;
import com.exa.mydemoapp.Common.Constants;
import com.exa.mydemoapp.Common.FloatingActionButton;
import com.exa.mydemoapp.Common.StudentInfoSingleton;
import com.exa.mydemoapp.annotation.ViewById;
import com.exa.mydemoapp.database.Database;
import com.exa.mydemoapp.database.DbInvoker;
import com.exa.mydemoapp.model.UserModel;
import com.exa.mydemoapp.webservice.CallWebService;
import com.exa.mydemoapp.webservice.IJson;
import com.exa.mydemoapp.webservice.IUrls;
import com.exa.mydemoapp.webservice.VolleyResponseListener;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.HashMap;

/**
 * Created by midt-006 on 12/12/17.
 */

public class LoginActivity extends CommonActivity {
    View view;
    @ViewById(R.id.btn_guest_login)
    Button btnGuest;
    @ViewById(R.id.edt_user_name)
    EditText edtUserName;
    @ViewById(R.id.edt_password)
    EditText edtPassword;
    @ViewById(R.id.floating_login)
    LinearLayout yourframelayout;
    UserModel userModel;
    ProgressDialog progressDialog;
    StudentInfoSingleton studentInfoSingleton;
    String appVersion;
    Database database;
    DbInvoker dbInvoker;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_login);
        view = getWindow().getDecorView();
        appVersion = CommonUtils.getAppVersion(this);
        database = new Database(this);
        dbInvoker = new DbInvoker(this);
        if (CommonUtils.getSharedPref(Constants.USER_NAME, this) != null && !CommonUtils.getSharedPref(Constants.USER_NAME, this).isEmpty()) {
            //studentInfoSingleton = StudentInfoSingleton.getInstance(LoginActivity.this);
            Intent intent = new Intent(this, HomeActivity.class);
            startActivity(intent);
            finish();
        }

        initViewBinding(view);
        init();
        userModel = new UserModel();
        btnGuest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                intent.putExtra(Constants.USER_TYPE, Constants.USER_TYPE_GUEST);
                startActivity(intent);

            }
        });

        FloatingActionButton fabButton = new FloatingActionButton.Builder(this, yourframelayout)
                .withDrawable(getResources().getDrawable(R.drawable.ic_login_check))
                .withButtonColor(Color.parseColor("#4c4c4c"))
                .withGravity(Gravity.BOTTOM | Gravity.RIGHT)
                .withMargins(0, 0, 2, 2)
                .create();


        fabButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Connectivity.isConnected(LoginActivity.this)) {
                    bindModel();
                    // checkLogin();
                    Login();
                    //saveLogin();
                } else {
                    showToast(getStringById(R.string.no_internet));
                }
            }
        });

    }

    private void bindModel() {
        userModel.setUsername(edtUserName.getText().toString().trim());
        userModel.setPassword(edtPassword.getText().toString().trim());
        String token = FirebaseInstanceId.getInstance().getToken();
        TelephonyManager telephonyManager = (TelephonyManager) getSystemService(this.TELEPHONY_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        String imei = telephonyManager.getDeviceId();

        userModel.getUserDevice().setDeviceType("Android");
        userModel.getUserDevice().setDeviceName(CommonUtils.getDeviceName());
        userModel.getUserDevice().setDeviceUid(imei);
        userModel.getUserDevice().setDeviceToken(token);
    }

   /* public void checkLogin() {
        String encryptedText = null;
        try {
            encryptedText = CommonUtils.encrypt(edtPassword.getText().toString().trim());
        } catch (Exception e) {
            Log.e("Error", e.getMessage());
        }
        DatabaseReference ref1 = databaseReference.child(Constants.MAIN_TABLE);
        DatabaseReference ref2 = ref1.child(Constants.STUDENT);
        Query query = ref2.orderByChild("studentUserName").equalTo(userModel.getStudentUserName());
        String finalEncryptedText = encryptedText.substring(0, 10);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                progressDialog.dismiss();
                if (dataSnapshot.getValue() == null) {
                    Toast.makeText(getApplicationContext(), getStringById(R.string.valid_user_name), Toast.LENGTH_SHORT).show();
                    return;
                }
                for (DataSnapshot Snapshot : dataSnapshot.getChildren()) {
                    UserModel studentData = Snapshot.getValue(UserModel.class);
                    String password = studentData.getStudentPassword().substring(0, 10);
                    if (password.equals(finalEncryptedText)) {
                        CommonUtils.insertSharedPref(LoginActivity.this, Constants.USER_NAME, studentData.getStudentUserName());
                        CommonUtils.insertSharedPref(LoginActivity.this, Constants.USER_TYPE, studentData.getUserType());
                        studentInfoSingleton = StudentInfoSingleton.getInstance(LoginActivity.this);
                        studentInfoSingleton.setUserModel(studentData);
                        AppController.isAdmin(LoginActivity.this);
                        Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                        startActivity(intent);
                        finish();
                        Toast.makeText(getApplicationContext(), getStringById(R.string.login_success), Toast.LENGTH_SHORT).show();

                    } else {
                        Toast.makeText(getApplicationContext(), getStringById(R.string.valid_password), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("Exception", "onCancelled", databaseError.toException());

                progressDialog.dismiss();
            }
        });

    }*/

    /*  private void saveLogin() {

     *//* userModel.setId(102);
        userModel.setStudentUserName("vaibhav");
        userModel.setStudentPassword("123456");
        userModel.setDateInsvestment2("12/02/2018");
        userModel.setDateInsvestment3("12/04/2018");
        userModel.setContactNumber("123456789");
        userModel.setTotalFees(1123);
        userModel.setRollNumber("12");
        userModel.setGender("male");
        userModel.setInstallmentType("1st");
        userModel.setRegistrationId("NREf434");
        userModel.setVisiblity("TRUE");
        userModel.setStudentAddress("Sangvi");
        userModel.setStudentName("Vaibhav J");
        userModel.setClassName("9th");
        userModel.setDivision("A");
        userModel.setSchoolName("NKVS");
        userModel.setInstallment1("12000");
        userModel.setUserType("STUDENT");
        userModel.setStudentBloodGrp("B+ve");
        userModel.setSubscribed("TRUE");
        userModel.setDateStamp("12/03/2018");

        dbInvoker.insertUpdateUser(userModel);*//*
        List<UserModel> studentModelList = new ArrayList<>();
        studentModelList = dbInvoker.getUserList();
        studentModelList= dbInvoker.getStudentListByClass("9th");
        UserModel userModel = new UserModel();
        userModel= dbInvoker.getStudentById(102);
    }*/

    private void Login() {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put(IJson.username, userModel.getUsername());
        hashMap.put(IJson.password, userModel.getPassword());
        hashMap.put(IJson.loginFrom, userModel.getLoginFrom());
        hashMap.put(IJson.deviceDetails, userModel.getUserDevice());

        CallWebService.getWebserviceObject(this, Request.Method.POST, IUrls.URL_LOGIN, hashMap, new VolleyResponseListener<UserModel>() {
            @Override
            public void onResponse(UserModel[] object) {
            }
            @Override
            public void onResponse() {
            }
            @Override
            public void onResponse(UserModel studentData) {
                    dbInvoker.insertUpdateUser(studentData);
                CommonUtils.insertSharedPref(LoginActivity.this, Constants.USER_NAME, studentData.getUsername());
                CommonUtils.insertSharedPref(LoginActivity.this, Constants.USER_TYPE, studentData.getUserType());
                CommonUtils.insertSharedPref(LoginActivity.this, Constants.STUDENT_ID, studentData.getPkeyId().toString());
                Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                startActivity(intent);
                finish();
            }

            @Override
            public void onError(String message) {
                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
            }
        }, UserModel.class);

    }

}
