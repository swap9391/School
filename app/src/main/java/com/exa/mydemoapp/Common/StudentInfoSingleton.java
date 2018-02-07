package com.exa.mydemoapp.Common;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.exa.mydemoapp.HomeActivity;
import com.exa.mydemoapp.LoginActivity;
import com.exa.mydemoapp.model.StudentModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by midt-078 on 7/2/18.
 */

public class StudentInfoSingleton {
    private StudentModel studentModel;
    private static StudentInfoSingleton studentInfoSingleton;
    private Activity activity;
    ProgressDialog progressDialog;

    public StudentInfoSingleton(Activity activity) {
        this.activity = activity;
    }

    public static StudentInfoSingleton getInstance(Activity activity) {
        if (studentInfoSingleton == null) {
            studentInfoSingleton = new StudentInfoSingleton(activity);
        }
        return studentInfoSingleton;
    }

    public StudentModel getStudentModel() {
        return studentModel;
    }

    public void setStudentModel(StudentModel studentModel) {
        this.studentModel = studentModel;
    }

    public void checkLogin() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        String userId = databaseReference.push().getKey();
        DatabaseReference ref1 = databaseReference.child(Constants.MAIN_TABLE);
        DatabaseReference ref2 = ref1.child(Constants.STUDENT);

        Query query = ref2.orderByChild("studentUserName").equalTo(CommonUtils.getSharedPref(Constants.USER_NAME, activity));
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot Snapshot : dataSnapshot.getChildren()) {
                    StudentModel studentData = Snapshot.getValue(StudentModel.class);
                    studentInfoSingleton.setStudentModel(studentData);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("Exception", "onCancelled", databaseError.toException());
            }
        });

    }
}
