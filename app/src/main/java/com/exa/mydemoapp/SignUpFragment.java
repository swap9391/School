package com.exa.mydemoapp;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.exa.mydemoapp.Common.CommonActivity;
import com.exa.mydemoapp.Common.CommonUtils;
import com.exa.mydemoapp.Common.Constants;
import com.exa.mydemoapp.annotation.RequiredFieldException;
import com.exa.mydemoapp.annotation.Validator;
import com.exa.mydemoapp.annotation.ViewById;
import com.exa.mydemoapp.fragment.CommonFragment;
import com.exa.mydemoapp.model.StudentModel;
import com.exa.mydemoapp.viewer.*;

import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import static android.content.ContentValues.TAG;

/**
 * Created by midt-006 on 11/12/17.
 */

public class SignUpFragment extends CommonFragment {

    private StudentModel studentModel;
    private List<String> listSchool;
    private List<String> listClass;
    private List<String> listDivision;
    @ViewById(R.id.toolbar)
    public Toolbar toolbar;
    @ViewById(R.id.spinner_school_name)
    private Spinner spnSchoolName;
    @ViewById(R.id.spinner_class_name)
    private Spinner spnClass;
    @ViewById(R.id.spinner_division)
    private Spinner spnDivision;
    @ViewById(R.id.edt_student_name)
    private EditText edtStudentName;
    @ViewById(R.id.edt_student_address)
    private EditText edtAddress;
    @ViewById(R.id.edt_student_blood_group)
    private EditText edtBloodGrp;
    @ViewById(R.id.edt_student_username)
    private EditText edtUsername;
    @ViewById(R.id.edt_student_password)
    private EditText edtRegistrationId;
    @ViewById(R.id.edt_registration_id)
    private EditText edtPassword;
    private View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.layout_sign_up, container, false);
        initViewBinding(view);
        getMyActivity().init();
        setHasOptionsMenu(true);
        getMyActivity().toolbar.setTitle("Register Student");
        listSchool = Arrays.asList(getResources().getStringArray(R.array.school_name));
        ArrayAdapter<String> schoolAdapter = new ArrayAdapter<String>(getMyActivity(), android.R.layout.simple_spinner_item, listSchool);
        schoolAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnSchoolName.setAdapter(schoolAdapter);
        schoolAdapter.notifyDataSetChanged();

        listClass = Arrays.asList(getResources().getStringArray(R.array.class_type));
        ArrayAdapter<String> classAdapter = new ArrayAdapter<String>(getMyActivity(), android.R.layout.simple_spinner_item, listClass);
        classAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnClass.setAdapter(classAdapter);
        classAdapter.notifyDataSetChanged();


        listDivision = Arrays.asList(getResources().getStringArray(R.array.division_name));
        ArrayAdapter<String> divisionAdapter = new ArrayAdapter<String>(getMyActivity(), android.R.layout.simple_spinner_item, listDivision);
        divisionAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnDivision.setAdapter(divisionAdapter);
        divisionAdapter.notifyDataSetChanged();

        studentModel = new StudentModel();

        return view;
    }

    private void bindModel() {
        studentModel.setStudentName(edtStudentName.getText().toString().trim());
        studentModel.setStudentAddress(edtAddress.getText().toString().trim());
        studentModel.setStudentBloodGrp(edtBloodGrp.getText().toString().trim());
        studentModel.setStudentUserName(edtUsername.getText().toString().trim());
        studentModel.setStudentPassword(edtPassword.getText().toString().trim());
        studentModel.setSchoolName(spnSchoolName.getSelectedItem().toString());
        studentModel.setClassName(spnClass.getSelectedItem().toString());
        studentModel.setDivision(spnDivision.getSelectedItem().toString());
        studentModel.setUserType("STUDENT");
        studentModel.setDateStamp(CommonUtils.formatDateForDisplay(Calendar.getInstance().getTime(), Constants.DATE_FORMAT));
        studentModel.setVisiblity("TRUE");
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_save_info, menu);
        menu.findItem(R.id.action_gallery).setVisible(false);
        //menu.findItem(R.id.action_gallery).setVisible(false);
        super.onCreateOptionsMenu(menu, inflater);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_save:
                bindModel();
                try {
                    if (Validator.validateForNulls(studentModel, getMyActivity())) {
                        saveUserInformation();
                        Log.d(TAG, "Validations Successful");
                    }
                } catch (RequiredFieldException | ClassNotFoundException | NoSuchFieldException | IllegalAccessException e) {
                    // Inform user about his SINS
                    e.printStackTrace();
                }
                break;
        }
        return true;
    }

    private void saveUserInformation() {

        try {
            AlertDialog.Builder builder = showAlertDialog(getMyActivity(), getString(R.string.logout_title), getString(R.string.save_msg));
            builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    final String userId = getMyActivity().databaseReference.push().getKey();
                    studentModel.setUniqKey(userId);
                    getMyActivity().databaseReference.child(Constants.MAIN_TABLE).child(Constants.STUDENT).child(studentModel.getUniqKey()).setValue(studentModel);
                    Toast.makeText(getMyActivity(), "Information Saved...", Toast.LENGTH_LONG).show();
                    getMyActivity().finish();
                }
            }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();

                }
            }).show();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }

    }

    public static AlertDialog.Builder showAlertDialog(Context context, String title, String msg) {
        AlertDialog.Builder alertDialog = null;
        alertDialog = new AlertDialog.Builder(context);
        alertDialog.setTitle(title);
        alertDialog.setMessage(msg);
        return alertDialog;
    }

    public com.exa.mydemoapp.viewer.HomeActivity getMyActivity() {
        return (com.exa.mydemoapp.viewer.HomeActivity) getActivity();
    }
}
