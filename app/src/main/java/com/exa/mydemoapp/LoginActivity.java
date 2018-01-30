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

import com.exa.mydemoapp.Common.AppController;
import com.exa.mydemoapp.Common.CommonActivity;
import com.exa.mydemoapp.Common.CommonUtils;
import com.exa.mydemoapp.Common.Connectivity;
import com.exa.mydemoapp.Common.Constants;
import com.exa.mydemoapp.Common.FloatingActionButton;
import com.exa.mydemoapp.annotation.ViewById;
import com.exa.mydemoapp.model.StudentModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by midt-006 on 12/12/17.
 */

public class LoginActivity extends CommonActivity {
    View view;
    @ViewById(R.id.btn_sign_up)
    Button btnSignUp;
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

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_login);
        view = getWindow().getDecorView();
        if (CommonUtils.getSharedPref(Constants.USER_NAME, this) != null && !CommonUtils.getSharedPref(Constants.USER_NAME, this).isEmpty()) {
            Intent intent = new Intent(this, HomeActivity.class);
            startActivity(intent);
            finish();
        }

        initViewBinding(view);
        init();
        studentModel = new StudentModel();
        btnSignUp.setOnClickListener(new onBtnSignUp());
        btnGuest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                intent.putExtra(Constants.USER_TYPE, "GUEST");
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
                    progressDialog = new ProgressDialog(LoginActivity.this);
                    progressDialog.setTitle("Loading...");
                    progressDialog.show();
                    bindModel();
                    checkLogin();
                } else {
                    showToast("Please Connect to internet !!");
                }
            }
        });

    }

    private void bindModel() {
        studentModel.setStudentUserName(edtUserName.getText().toString().trim());
        studentModel.setStudentPassword(edtPassword.getText().toString().trim());
    }

    private class onBtnSignUp implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
            startActivity(intent);
        }
    }

    public void checkLogin() {
        String userId = databaseReference.push().getKey();
        DatabaseReference ref1 = databaseReference.child(Constants.MAIN_TABLE);
        DatabaseReference ref2 = ref1.child(Constants.STUDENT);
        Query query = ref2.orderByChild("studentUserName").equalTo(studentModel.getStudentUserName());
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                progressDialog.dismiss();
                if (dataSnapshot.getValue() == null) {
                    Toast.makeText(getApplicationContext(), "Please Check Your User Name", Toast.LENGTH_SHORT).show();
                    return;
                }
                for (DataSnapshot Snapshot : dataSnapshot.getChildren()) {
                    StudentModel studentData = Snapshot.getValue(StudentModel.class);
                    if (studentData.getStudentPassword().equalsIgnoreCase(studentModel.getStudentPassword())) {
                        CommonUtils.insertSharedPref(LoginActivity.this, Constants.USER_NAME, studentData.getStudentUserName());
                        CommonUtils.insertSharedPref(LoginActivity.this, Constants.SCHOOL_NAME, studentData.getSchoolName());
                        CommonUtils.insertSharedPref(LoginActivity.this, Constants.STUDENT_ADDRESS, studentData.getStudentAddress());
                        CommonUtils.insertSharedPref(LoginActivity.this, Constants.STUDENT_BLOOD, studentData.getStudentBloodGrp());
                        CommonUtils.insertSharedPref(LoginActivity.this, Constants.STUDENT_DIVISION, studentData.getDivision());
                        CommonUtils.insertSharedPref(LoginActivity.this, Constants.CLASS_NAME, studentData.getClassName());
                        CommonUtils.insertSharedPref(LoginActivity.this, Constants.STUDENT_NAME, studentData.getStudentName());
                        CommonUtils.insertSharedPref(LoginActivity.this, Constants.USER_TYPE, studentData.getUserType());
                        CommonUtils.insertSharedPref(LoginActivity.this, Constants.STUDENT_ID, studentData.getUniqKey());
                        AppController.isAdmin(LoginActivity.this);
                        Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                        startActivity(intent);
                        Toast.makeText(getApplicationContext(), "Login Successfully!!", Toast.LENGTH_SHORT).show();

                    } else {
                        Toast.makeText(getApplicationContext(), "Please Check Your Password", Toast.LENGTH_SHORT).show();
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

}
