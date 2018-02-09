package com.exa.mydemoapp;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.exa.mydemoapp.Common.CommonUtils;
import com.exa.mydemoapp.Common.Constants;
import com.exa.mydemoapp.annotation.RequiredFieldException;
import com.exa.mydemoapp.annotation.Validator;
import com.exa.mydemoapp.annotation.ViewById;
import com.exa.mydemoapp.fragment.CommonFragment;
import com.exa.mydemoapp.fragment.UsersListFragment;
import com.exa.mydemoapp.model.StudentModel;

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
    private List<String> listFees;
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
    @ViewById(R.id.edt_registration_id)
    private EditText edtRegistrationId;
    @ViewById(R.id.edt_student_password)
    private EditText edtPassword;
    @ViewById(R.id.spinner_fees_type)
    private Spinner spnFeesType;
    @ViewById(R.id.edt_installment_1)
    private EditText edtInstallment1;
    @ViewById(R.id.edt_installment_2)
    private EditText edtInstallment2;
    @ViewById(R.id.edt_installment_3)
    private EditText edtInstallment3;
    @ViewById(R.id.edt_total_fees)
    private EditText edtTotalFees;
    @ViewById(R.id.rd_girl)
    RadioButton rdGirl;
    @ViewById(R.id.rd_boy)
    RadioButton rdBoy;
    boolean isEdit = false;

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

        edtInstallment2.setVisibility(View.GONE);
        edtInstallment3.setVisibility(View.GONE);

        listFees = Arrays.asList(getResources().getStringArray(R.array.fees_type));
        ArrayAdapter<String> feesAdapter = new ArrayAdapter<String>(getMyActivity(), android.R.layout.simple_spinner_item, listFees);
        feesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnFeesType.setAdapter(feesAdapter);
        feesAdapter.notifyDataSetChanged();
        spnFeesType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                switch (listFees.get(position)) {
                    case "First Installment":
                        edtInstallment1.setVisibility(View.VISIBLE);
                        edtInstallment2.setVisibility(View.GONE);
                        edtInstallment3.setVisibility(View.GONE);
                        break;
                    case "Second Installment":
                        edtInstallment1.setVisibility(View.VISIBLE);
                        edtInstallment2.setVisibility(View.VISIBLE);
                        edtInstallment3.setVisibility(View.GONE);
                        break;
                    case "Third Installment":
                        edtInstallment1.setVisibility(View.VISIBLE);
                        edtInstallment2.setVisibility(View.VISIBLE);
                        edtInstallment3.setVisibility(View.VISIBLE);
                        break;
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }
        });

        Bundle bundle = getArguments();
        studentModel = (StudentModel) bundle.getSerializable("studentData");
        if (bundle != null && studentModel != null) {
            bindView();
            isEdit = true;
        } else {
            studentModel = new StudentModel();
            isEdit = false;
        }

        return view;
    }

    private void bindView() {
        int schoolPostion = 0;
        int classPostion = 0;
        int divisionPosition = 0;
        int installmentPosition = 0;
        for (int i = 0; i < listSchool.size(); i++) {
            if (listSchool.get(i).equals(studentModel.getSchoolName())) {
                schoolPostion = i;
                break;
            }
        }
        for (int i = 0; i < listClass.size(); i++) {
            if (listClass.get(i).equals(studentModel.getClassName())) {
                classPostion = i;
                break;
            }
        }
        for (int i = 0; i < listDivision.size(); i++) {
            if (listDivision.get(i).equals(studentModel.getDivision())) {
                divisionPosition = i;
                break;
            }
        }
        for (int i = 0; i < listFees.size(); i++) {
            if (listFees.get(i).equals(studentModel.getInstallmentType())) {
                installmentPosition = i;
                break;
            }
        }
        String decryptPassword = "";
        try {
            decryptPassword = CommonUtils.decrypt(studentModel.getStudentPassword());
        } catch (Exception e) {
            Log.e("Error", e.getMessage());
        }
        spnClass.setSelection(classPostion);
        spnSchoolName.setSelection(schoolPostion);
        spnDivision.setSelection(divisionPosition);
        spnFeesType.setSelection(installmentPosition);
        edtRegistrationId.setText(studentModel.getRegistrationId());
        edtStudentName.setText(studentModel.getStudentName());
        edtAddress.setText(studentModel.getStudentAddress());
        edtBloodGrp.setText(studentModel.getStudentBloodGrp());
        edtUsername.setText(studentModel.getStudentUserName());
        edtPassword.setText(decryptPassword);
        edtTotalFees.setText("" + studentModel.getTotalFees());
        edtInstallment1.setText(studentModel.getInstallment1());
        edtInstallment2.setText(studentModel.getInstallment2());
        edtInstallment3.setText(studentModel.getInstallment3());
        if (studentModel.getGender().equals("Boy")) {
            rdBoy.setChecked(true);
        } else {
            rdGirl.setChecked(true);
        }

    }

    private void bindModel() {
        studentModel.setStudentName(edtStudentName.getText().toString().trim());
        studentModel.setStudentAddress(edtAddress.getText().toString().trim());
        studentModel.setStudentBloodGrp(edtBloodGrp.getText().toString().trim());
        studentModel.setStudentUserName(edtUsername.getText().toString().trim());
        try {
            studentModel.setStudentPassword(CommonUtils.encrypt(edtPassword.getText().toString().trim()));
        } catch (Exception e) {
            Log.e("Error", e.getMessage());
        }
        studentModel.setSchoolName(spnSchoolName.getSelectedItem().toString());
        studentModel.setClassName(spnClass.getSelectedItem().toString().trim());
        studentModel.setDivision(spnDivision.getSelectedItem().toString().trim());
        studentModel.setRegistrationId(edtRegistrationId.getText().toString().trim());
        if (!isEdit) {
            studentModel.setUserType("STUDENT");
        }
        studentModel.setDateStamp(CommonUtils.formatDateForDisplay(Calendar.getInstance().getTime(), Constants.DATE_FORMAT));
        studentModel.setVisiblity("TRUE");
        if (rdGirl.isChecked()) {
            studentModel.setGender("Girl");
        } else if (rdBoy.isChecked()) {
            studentModel.setGender("Boy");
        }
        studentModel.setInstallmentType(spnFeesType.getSelectedItem().toString());
        studentModel.setInstallment1(edtInstallment1.getText().toString().trim());
        studentModel.setInstallment2(edtInstallment2.getText().toString().trim());
        studentModel.setInstallment3(edtInstallment3.getText().toString().trim());
        studentModel.setTotalFees(CommonUtils.asInt(edtTotalFees.getText().toString().trim(), 0));
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
                        if (check()) {
                            saveUserInformation();
                            Log.d(TAG, "Validations Successful");
                        }
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
                    if (!isEdit) {
                        final String userId = getMyActivity().databaseReference.push().getKey();
                        studentModel.setUniqKey(userId);
                        getMyActivity().databaseReference.child(Constants.MAIN_TABLE).child(Constants.STUDENT).child(studentModel.getUniqKey()).setValue(studentModel);
                        Toast.makeText(getMyActivity(), "Information Saved...", Toast.LENGTH_LONG).show();
                        getMyActivity().showFragment(getMyActivity().profileFragment, null);
                    } else {
                        getMyActivity().databaseReference.child(Constants.MAIN_TABLE).child(Constants.STUDENT).child(studentModel.getUniqKey()).setValue(studentModel);
                        Toast.makeText(getMyActivity(), "Information Saved...", Toast.LENGTH_LONG).show();
                        getMyActivity().showFragment(new UsersListFragment(), null);
                    }

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

    private boolean check() {
        if (studentModel.getGender() == null || studentModel.getGender().isEmpty()) {
            getMyActivity().showToast("Please Select Gender");
            return false;
        }
        if (studentModel.getTotalFees() <= 0) {
            getMyActivity().showToast("Please Enter Total Fees");
            return false;
        }
        if (studentModel.getInstallmentType() == null || studentModel.getInstallmentType().isEmpty()) {
            getMyActivity().showToast("Please Select Fees Installment");
            return false;
        }
        if (studentModel.getInstallmentType().equalsIgnoreCase("First Installment") && (studentModel.getInstallment1() == null || studentModel.getInstallment1().isEmpty())) {
            getMyActivity().showToast("Please Enter First Installment");
            return false;
        }

        if (studentModel.getInstallmentType().equalsIgnoreCase("Second Installment") && (studentModel.getInstallment2() == null || studentModel.getInstallment2().isEmpty())) {
            getMyActivity().showToast("Please Enter Second Installment");
            return false;
        }

        if (studentModel.getInstallmentType().equalsIgnoreCase("Third Installment") && (studentModel.getInstallment3() == null || studentModel.getInstallment3().isEmpty())) {
            getMyActivity().showToast("Please Enter Third Installment");
            return false;
        }

        return true;
    }

    public static AlertDialog.Builder showAlertDialog(Context context, String title, String msg) {
        AlertDialog.Builder alertDialog = null;
        alertDialog = new AlertDialog.Builder(context);
        alertDialog.setTitle(title);
        alertDialog.setMessage(msg);
        return alertDialog;
    }

    public HomeActivity getMyActivity() {
        return (HomeActivity) getActivity();
    }
}