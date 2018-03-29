package com.exa.mydemoapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
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
import com.exa.mydemoapp.model.StudentModel;
import com.exa.mydemoapp.webservice.CallWebService;
import com.exa.mydemoapp.webservice.IJson;
import com.exa.mydemoapp.webservice.IUrls;
import com.exa.mydemoapp.webservice.VolleyResponseListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

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
    StudentModel studentModel;
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
        studentModel = new StudentModel();
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
        studentModel.setStudentUserName(edtUserName.getText().toString().trim());
        studentModel.setStudentPassword(edtPassword.getText().toString().trim());
    }

    public void checkLogin() {
        String encryptedText = null;
        try {
            encryptedText = CommonUtils.encrypt(edtPassword.getText().toString().trim());
        } catch (Exception e) {
            Log.e("Error", e.getMessage());
        }
        DatabaseReference ref1 = databaseReference.child(Constants.MAIN_TABLE);
        DatabaseReference ref2 = ref1.child(Constants.STUDENT);
        Query query = ref2.orderByChild("studentUserName").equalTo(studentModel.getStudentUserName());
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
                    StudentModel studentData = Snapshot.getValue(StudentModel.class);
                    String password = studentData.getStudentPassword().substring(0, 10);
                    if (password.equals(finalEncryptedText)) {
                        CommonUtils.insertSharedPref(LoginActivity.this, Constants.USER_NAME, studentData.getStudentUserName());
                        CommonUtils.insertSharedPref(LoginActivity.this, Constants.USER_TYPE, studentData.getUserType());
                        studentInfoSingleton = StudentInfoSingleton.getInstance(LoginActivity.this);
                        studentInfoSingleton.setStudentModel(studentData);
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

    }

  /*  private void saveLogin() {

       *//* studentModel.setId(102);
        studentModel.setStudentUserName("vaibhav");
        studentModel.setStudentPassword("123456");
        studentModel.setDateInsvestment2("12/02/2018");
        studentModel.setDateInsvestment3("12/04/2018");
        studentModel.setContactNumber("123456789");
        studentModel.setTotalFees(1123);
        studentModel.setRollNumber("12");
        studentModel.setGender("male");
        studentModel.setInstallmentType("1st");
        studentModel.setRegistrationId("NREf434");
        studentModel.setVisiblity("TRUE");
        studentModel.setStudentAddress("Sangvi");
        studentModel.setStudentName("Vaibhav J");
        studentModel.setClassName("9th");
        studentModel.setDivision("A");
        studentModel.setSchoolName("NKVS");
        studentModel.setInstallment1("12000");
        studentModel.setUserType("STUDENT");
        studentModel.setStudentBloodGrp("B+ve");
        studentModel.setSubscribed("TRUE");
        studentModel.setDateStamp("12/03/2018");

        dbInvoker.insertUpdateUser(studentModel);*//*
        List<StudentModel> studentModelList = new ArrayList<>();
        studentModelList = dbInvoker.getUserList();
        studentModelList= dbInvoker.getStudentListByClass("9th");
        StudentModel studentModel = new StudentModel();
        studentModel= dbInvoker.getStudentById(102);
    }*/

    private void Login() {
        String encryptedText = null;
        String finalEncryptedText = null;
        try {
            encryptedText = CommonUtils.encrypt(edtPassword.getText().toString().trim());
            // finalEncryptedText = encryptedText.substring(0, 10);
        } catch (Exception e) {
            Log.e("Error", e.getMessage());
        }

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put(IJson.studentUserName, "" + studentModel.getStudentUserName());
        hashMap.put(IJson.studentPassword, "" + encryptedText.trim());
        CallWebService.getWebserviceObject(this, Request.Method.POST, IUrls.URL_LOGIN, hashMap, new VolleyResponseListener<StudentModel>() {
            @Override
            public void onResponse(StudentModel[] object) {
            }

            @Override
            public void onResponse(StudentModel studentData) {
                Log.e("Tag", studentData.getStudentName());
                /*String password = studentData.getStudentPassword().substring(0, 10);
                if (password.equals(finalEncryptedText1)) {*/
                CommonUtils.insertSharedPref(LoginActivity.this, Constants.USER_NAME, studentData.getStudentUserName());
                CommonUtils.insertSharedPref(LoginActivity.this, Constants.USER_TYPE, studentData.getUserType());
                CommonUtils.insertSharedPref(LoginActivity.this, Constants.STUDENT_ID, studentData.getId().toString());
                CommonUtils.insertSharedPref(LoginActivity.this, Constants.STUDENT_NAME, studentData.getStudentName());
                AppController.isAdmin(LoginActivity.this);
                Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                startActivity(intent);
                finish();
                // Toast.makeText(getApplicationContext(), getStringById(R.string.login_success), Toast.LENGTH_SHORT).show();

                /*} else {
                    Toast.makeText(getApplicationContext(), getStringById(R.string.valid_password), Toast.LENGTH_SHORT).show();
                }*/
            }

            @Override
            public void onError(String message) {
                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
            }
        }, StudentModel.class);

    }

}
