package com.exa.mydemoapp;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.exa.mydemoapp.Common.CommonUtils;
import com.exa.mydemoapp.Common.Constants;
import com.exa.mydemoapp.annotation.RequiredFieldException;
import com.exa.mydemoapp.annotation.Validator;
import com.exa.mydemoapp.annotation.ViewById;
import com.exa.mydemoapp.fragment.CommonFragment;
import com.exa.mydemoapp.fragment.DatePickerFragment;
import com.exa.mydemoapp.fragment.UsersListFragment;
import com.exa.mydemoapp.model.StudentModel;
import com.exa.mydemoapp.webservice.CallWebService;
import com.exa.mydemoapp.webservice.IJson;
import com.exa.mydemoapp.webservice.IUrls;
import com.exa.mydemoapp.webservice.VolleyResponseListener;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
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
    private List<String> listUserType;
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
    @ViewById(R.id.edt_roll_number)
    private EditText edtRollNumber;
    @ViewById(R.id.edt_contact_number)
    private EditText edtContactNumber;
    @ViewById(R.id.rd_girl)
    RadioButton rdGirl;
    @ViewById(R.id.rd_boy)
    RadioButton rdBoy;
    @ViewById(R.id.date_picker_invest1)
    private Button datePickerInvest1;
    @ViewById(R.id.date_picker_invest2)
    private Button datePickerInvest2;
    @ViewById(R.id.txtBalance)
    TextView txtBalance;
    @ViewById(R.id.spinner_user_type)
    private Spinner spnUserType;
    @ViewById(R.id.txt_class_name)
    TextView txtClassName;
    @ViewById(R.id.txt_division)
    TextView txtDivision;
    @ViewById(R.id.lblInstallment1)
    TextView txtInstallmentType;
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


        listUserType = Arrays.asList(getResources().getStringArray(R.array.userType));
        ArrayAdapter<String> userTypeadapter = new ArrayAdapter<String>(getMyActivity(), android.R.layout.simple_spinner_item, listUserType);
        userTypeadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnUserType.setAdapter(userTypeadapter);
        userTypeadapter.notifyDataSetChanged();


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
                        datePickerInvest2.setVisibility(View.GONE);
                        break;
                    case "Second Installment":
                        edtInstallment1.setVisibility(View.VISIBLE);
                        edtInstallment2.setVisibility(View.VISIBLE);
                        datePickerInvest2.setVisibility(View.VISIBLE);
                        datePickerInvest1.setVisibility(View.GONE);
                        edtInstallment3.setVisibility(View.GONE);
                        break;
                    case "Third Installment":
                        edtInstallment1.setVisibility(View.VISIBLE);
                        edtInstallment2.setVisibility(View.VISIBLE);
                        edtInstallment3.setVisibility(View.VISIBLE);
                        datePickerInvest2.setVisibility(View.GONE);
                        datePickerInvest1.setVisibility(View.GONE);
                        break;
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }
        });

        Bundle bundle = getArguments();
        if (bundle != null) {
            studentModel = (StudentModel) bundle.getSerializable("studentData");
            if (studentModel != null) {
                isEdit = true;
                bindView();
            }
        } else {
            studentModel = new StudentModel();
            isEdit = false;
        }

        datePickerInvest1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePickerInvest1.setTag("invest2");
                showDatePicker("install2");
            }
        });

        datePickerInvest2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePickerInvest2.setTag("invest3");
                showDatePicker("install3");
            }
        });


        //usertype UI binding
        spnUserType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                switch (listUserType.get(position)) {
                    case "ADMIN":
                        txtClassName.setVisibility(View.GONE);
                        txtDivision.setVisibility(View.GONE);
                        spnClass.setVisibility(View.GONE);
                        spnDivision.setVisibility(View.GONE);
                        edtRollNumber.setVisibility(View.GONE);
                        edtTotalFees.setVisibility(View.GONE);
                        spnFeesType.setVisibility(View.GONE);
                        datePickerInvest1.setVisibility(View.GONE);
                        datePickerInvest2.setVisibility(View.GONE);
                        txtInstallmentType.setVisibility(View.GONE);
                        edtInstallment1.setVisibility(View.GONE);
                        edtInstallment2.setVisibility(View.GONE);
                        edtInstallment3.setVisibility(View.GONE);
                        break;
                    case "TEACHER":
                        txtClassName.setVisibility(View.VISIBLE);
                        txtDivision.setVisibility(View.VISIBLE);
                        spnClass.setVisibility(View.VISIBLE);
                        spnDivision.setVisibility(View.VISIBLE);
                        edtRollNumber.setVisibility(View.GONE);
                        edtTotalFees.setVisibility(View.GONE);
                        spnFeesType.setVisibility(View.GONE);
                        datePickerInvest1.setVisibility(View.GONE);
                        datePickerInvest2.setVisibility(View.GONE);
                        txtInstallmentType.setVisibility(View.GONE);
                        edtInstallment1.setVisibility(View.GONE);
                        edtInstallment2.setVisibility(View.GONE);
                        edtInstallment3.setVisibility(View.GONE);
                        break;
                    case "DRIVER":
                        txtClassName.setVisibility(View.GONE);
                        txtDivision.setVisibility(View.GONE);
                        spnClass.setVisibility(View.GONE);
                        spnDivision.setVisibility(View.GONE);
                        edtRollNumber.setVisibility(View.GONE);
                        edtTotalFees.setVisibility(View.GONE);
                        spnFeesType.setVisibility(View.GONE);
                        datePickerInvest1.setVisibility(View.GONE);
                        datePickerInvest2.setVisibility(View.GONE);
                        txtInstallmentType.setVisibility(View.GONE);
                        edtInstallment1.setVisibility(View.GONE);
                        edtInstallment2.setVisibility(View.GONE);
                        edtInstallment3.setVisibility(View.GONE);
                        break;
                    case "STUDENT":
                        txtClassName.setVisibility(View.VISIBLE);
                        txtDivision.setVisibility(View.VISIBLE);
                        spnClass.setVisibility(View.VISIBLE);
                        spnDivision.setVisibility(View.VISIBLE);
                        edtRollNumber.setVisibility(View.VISIBLE);
                        edtTotalFees.setVisibility(View.VISIBLE);
                        spnFeesType.setVisibility(View.VISIBLE);
                        datePickerInvest1.setVisibility(View.VISIBLE);
                        txtInstallmentType.setVisibility(View.VISIBLE);
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
        edtContactNumber.setText(studentModel.getContactNumber());
        edtAddress.setText(studentModel.getStudentAddress());
        edtBloodGrp.setText(studentModel.getStudentBloodGrp());
        edtUsername.setText(studentModel.getStudentUserName());
        edtPassword.setText(decryptPassword);
        edtTotalFees.setText("" + studentModel.getTotalFees());
        edtRollNumber.setText(studentModel.getRollNumber());
        edtInstallment1.setText(studentModel.getInstallment1());
        edtInstallment2.setText(studentModel.getInstallment2());
        edtInstallment3.setText(studentModel.getInstallment3());
        if (studentModel.getGender().equals("Boy")) {
            rdBoy.setChecked(true);
        } else {
            rdGirl.setChecked(true);
        }
        long totalInstallment = CommonUtils.asInt(studentModel.getInstallment1(), 0) + CommonUtils.asInt(studentModel.getInstallment2(), 0) + CommonUtils.asInt(studentModel.getInstallment3(), 0);
        long banalce = studentModel.getTotalFees() - totalInstallment;
        if (isEdit) {
            txtBalance.setText("* Patment Due " + banalce);
        }
    }

    private void bindModel() {
        studentModel.setStudentName(edtStudentName.getText().toString().trim());
        studentModel.setStudentAddress(edtAddress.getText().toString().trim());
        studentModel.setStudentBloodGrp(edtBloodGrp.getText().toString().trim());
        studentModel.setStudentUserName(edtUsername.getText().toString().trim());
        studentModel.setContactNumber(edtContactNumber.getText().toString().trim());
        studentModel.setRollNumber(edtRollNumber.getText().toString().trim());
        studentModel.setUserType(spnUserType.getSelectedItem().toString().trim());
        try {
            studentModel.setStudentPassword(CommonUtils.encrypt(edtPassword.getText().toString().trim()).trim());
        } catch (Exception e) {
            Log.e("Error", e.getMessage());
        }
        studentModel.setSchoolName(spnSchoolName.getSelectedItem().toString());
        studentModel.setClassName(spnClass.getSelectedItem().toString().trim());
        studentModel.setDivision(spnDivision.getSelectedItem().toString().trim());
        studentModel.setRegistrationId(edtRegistrationId.getText().toString().trim());

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
                        try {
                            AlertDialog.Builder builder = getMyActivity().showAlertDialog(getMyActivity(), getString(R.string.app_name), getString(R.string.save_msg));
                            builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    bindModel();
                                    if (check()) {
                                        save();
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
            AlertDialog.Builder builder = showAlertDialog(getMyActivity(), getString(R.string.user_reg_title), getString(R.string.save_msg));
            builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if (!isEdit) {
                        final String userId = getMyActivity().databaseReference.push().getKey();
                        studentModel.setUniqKey(userId);
                        getMyActivity().databaseReference.child(Constants.MAIN_TABLE).child(Constants.STUDENT).child(studentModel.getUniqKey()).setValue(studentModel);
                        Toast.makeText(getMyActivity(), "Information Saved...", Toast.LENGTH_LONG).show();
                        //  getMyActivity().showFragment(getMyActivity().profileFragment, null);
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
        if (studentModel.getSchoolName() == null || studentModel.getSchoolName().isEmpty()) {
            getMyActivity().showToast("Please Select School Name");
            return false;
        }
        if (studentModel.getUserType() == null || studentModel.getUserType().isEmpty()) {
            getMyActivity().showToast("Please Select User Type");
            return false;
        }
        if ((studentModel.getUserType().equals("STUDENT") || studentModel.getUserType().equals("TEACHER")) && (studentModel.getClassName() == null || studentModel.getClassName().isEmpty())) {
            getMyActivity().showToast("Please Select Class");
            return false;
        }
        if ((studentModel.getUserType().equals("STUDENT") || studentModel.getUserType().equals("TEACHER")) && (studentModel.getDivision() == null || studentModel.getDivision().isEmpty())) {
            getMyActivity().showToast("Please Select Division");
            return false;
        }
        if (studentModel.getRegistrationId() == null || studentModel.getRegistrationId().isEmpty()) {
            getMyActivity().showToast("Please Enter Registration Id");
            return false;
        }
        if ((studentModel.getUserType().equals("STUDENT")) && (studentModel.getRollNumber() == null || studentModel.getRollNumber().isEmpty())) {
            getMyActivity().showToast("Please Enter Roll Number");
            return false;
        }
        if (studentModel.getStudentName() == null || studentModel.getStudentName().isEmpty()) {
            getMyActivity().showToast("Please Enter Full Name");
            return false;
        }
        if (studentModel.getContactNumber() == null || studentModel.getContactNumber().isEmpty()) {
            getMyActivity().showToast("Please Enter Contact Number");
            return false;
        }
        if (studentModel.getContactNumber() == null || studentModel.getContactNumber().length() < 10) {
            getMyActivity().showToast("Please Enter Valid Contact Number");
            return false;
        }
        if (studentModel.getGender() == null || studentModel.getGender().isEmpty()) {
            getMyActivity().showToast("Please Select Gender");
            return false;
        }
        if (studentModel.getStudentAddress() == null || studentModel.getStudentAddress().isEmpty()) {
            getMyActivity().showToast("Please Enter Address");
            return false;
        }

        if (studentModel.getStudentUserName() == null || studentModel.getStudentUserName().isEmpty()) {
            getMyActivity().showToast("Please Enter User Name");
            return false;
        }
        if (studentModel.getStudentPassword() == null || studentModel.getStudentPassword().isEmpty()) {
            getMyActivity().showToast("Please Enter Password");
            return false;
        }

        if (studentModel.getUserType().equals("STUDENT")) {
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

            long totalPay = CommonUtils.asInt(studentModel.getInstallment1(), 0) + CommonUtils.asInt(studentModel.getInstallment2(), 0) + CommonUtils.asInt(studentModel.getInstallment3(), 0);
            if (totalPay > studentModel.getTotalFees()) {
                getMyActivity().showToast("Please Enter Valid Amount");
                return false;
            }
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

    private void showDatePicker(String flag) {
        DatePickerFragment date = new DatePickerFragment();
        /**
         * Set Up Current Date Into dialog
         */
        Calendar calender = Calendar.getInstance();
        Bundle args = new Bundle();
        args.putInt("year", calender.get(Calendar.YEAR));
        args.putInt("month", calender.get(Calendar.MONTH));
        args.putInt("day", calender.get(Calendar.DAY_OF_MONTH));
        date.setArguments(args);
        /**
         * Set Call back to capture selected date
         */
        if (flag.equals("install2")) {
            date.setCallBack(ondate);
        } else if (flag.equals("install3")) {
            date.setCallBack(ondate2);
        }
        date.show(getFragmentManager(), "Date Picker");
    }

    DatePickerDialog.OnDateSetListener ondate = new DatePickerDialog.OnDateSetListener() {

        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            monthOfYear += 1;
            String month = "" + monthOfYear;
            String day = "" + dayOfMonth;
            if (monthOfYear < 10) {
                month = "0" + monthOfYear;
            }
            if (dayOfMonth < 10) {
                day = "0" + dayOfMonth;
            }
            Date date = CommonUtils.toDate(year + "" + month + "" + day, "yyyyMMdd");
            String formatedDate = CommonUtils.formatDateForDisplay(date, Constants.DATE_FORMAT);

            studentModel.setDateInsvestment2(formatedDate);
            datePickerInvest1.setText(CommonUtils.formatDateForDisplay(date, "dd-MM-yyyy"));


        }
    };

    DatePickerDialog.OnDateSetListener ondate2 = new DatePickerDialog.OnDateSetListener() {

        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            monthOfYear += 1;
            String month = "" + monthOfYear;
            String day = "" + dayOfMonth;
            if (monthOfYear < 10) {
                month = "0" + monthOfYear;
            }
            if (dayOfMonth < 10) {
                day = "0" + dayOfMonth;
            }
            Date date = CommonUtils.toDate(year + "" + month + "" + day, "yyyyMMdd");
            String formatedDate = CommonUtils.formatDateForDisplay(date, Constants.DATE_FORMAT);
            studentModel.setDateInsvestment3(formatedDate);
            datePickerInvest2.setText(CommonUtils.formatDateForDisplay(date, "dd-MM-yyyy"));
        }
    };

    private void save() {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put(IJson.schoolName, studentModel.getSchoolName());
        if (studentModel.getUserType().equals("STUDENT") || studentModel.getUserType().equals("TEACHER")) {
            hashMap.put(IJson.className, studentModel.getClassName());
            hashMap.put(IJson.division, studentModel.getDivision());
        }
        hashMap.put(IJson.registrationId, studentModel.getRegistrationId());
        hashMap.put(IJson.studentName, studentModel.getStudentName());
        hashMap.put(IJson.studentAddress, studentModel.getStudentAddress());
        hashMap.put(IJson.studentUserName, studentModel.getStudentUserName());
        hashMap.put(IJson.studentPassword, studentModel.getStudentPassword());
        hashMap.put(IJson.userType, studentModel.getUserType());
        hashMap.put(IJson.studentBloodGrp, studentModel.getStudentBloodGrp());
        hashMap.put(IJson.gender, studentModel.getGender());
        hashMap.put(IJson.contactNumber, studentModel.getContactNumber());
        if (studentModel.getUserType().equals("STUDENT")) {
            hashMap.put(IJson.totalFees, "" + studentModel.getTotalFees());
            hashMap.put(IJson.installmentType, studentModel.getInstallmentType());
            hashMap.put(IJson.installment1, studentModel.getInstallment1());
            hashMap.put(IJson.installment2, studentModel.getInstallment2());
            hashMap.put(IJson.installment3, studentModel.getInstallment3());
            hashMap.put(IJson.rollNumber, studentModel.getRollNumber());
            hashMap.put(IJson.dateInsvestment2, studentModel.getDateInsvestment2());
            hashMap.put(IJson.dateInsvestment3, studentModel.getDateInsvestment3());
        }

        CallWebService.getWebserviceObject(getMyActivity(), Request.Method.POST, IUrls.SIGN_UP, hashMap, new VolleyResponseListener<StudentModel>() {
            @Override
            public void onResponse(StudentModel[] object) {
                if (object[0] instanceof StudentModel) {
                    for (StudentModel bean : object) {

                    }
                }
            }

            @Override
            public void onResponse(StudentModel object) {
                if (!isEdit) {
                    Toast.makeText(getMyActivity(), "Information Saved...", Toast.LENGTH_LONG).show();
                    // getMyActivity().showFragment(getMyActivity().profileFragment, null);
                } else {
                    Toast.makeText(getMyActivity(), "Information Saved...", Toast.LENGTH_LONG).show();
                    getMyActivity().showFragment(new UsersListFragment(), null);
                }
            }

            @Override
            public void onError(String message) {
                if (message != null && !message.isEmpty()) {
                    getMyActivity().showToast(message);
                }
            }
        }, StudentModel.class);
    }


    public AdminActivity getMyActivity() {
        return (AdminActivity) getActivity();
    }
}
