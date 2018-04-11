package com.exa.mydemoapp.fragment;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
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
import com.exa.mydemoapp.HomeActivity;
import com.exa.mydemoapp.R;
import com.exa.mydemoapp.annotation.RequiredFieldException;
import com.exa.mydemoapp.annotation.Validator;
import com.exa.mydemoapp.annotation.ViewById;
import com.exa.mydemoapp.fragment.CommonFragment;
import com.exa.mydemoapp.fragment.DatePickerFragment;
import com.exa.mydemoapp.fragment.UsersListFragment;
import com.exa.mydemoapp.listner.DialogResultListner;
import com.exa.mydemoapp.model.FeesInstallmentsModel;
import com.exa.mydemoapp.model.UserModel;
import com.exa.mydemoapp.webservice.CallWebService;
import com.exa.mydemoapp.webservice.IJson;
import com.exa.mydemoapp.webservice.IUrls;
import com.exa.mydemoapp.webservice.VolleyResponseListener;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Created by midt-006 on 11/12/17.
 */

public class SignUpFragment extends CommonFragment {

    private UserModel userModel;
    private List<String> listSchool;
    private List<String> listClass;
    private List<String> listDivision;

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
    @ViewById(R.id.edt_student_password)
    private EditText edtPassword;
    @ViewById(R.id.edt_contact_number)
    private EditText edtContactNumber;
    @ViewById(R.id.edt_total_fees)
    private EditText edtTotalFees;
    @ViewById(R.id.rd_girl)
    RadioButton rdGirl;
    @ViewById(R.id.rd_boy)
    RadioButton rdBoy;
    @ViewById(R.id.spinner_user_type)
    private Spinner spnUserType;
    @ViewById(R.id.txt_class_name)
    TextView txtClassName;
    @ViewById(R.id.txt_division)
    TextView txtDivision;

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


        Bundle bundle = getArguments();
        if (bundle != null) {
            userModel = (UserModel) bundle.getSerializable("studentData");
            if (userModel != null) {
                isEdit = true;
                bindView();
            }
        } else {
            userModel = new UserModel();
            isEdit = false;
        }

       /* datePickerInvest1.setOnClickListener(new View.OnClickListener() {
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
        });*/


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
                        break;
                    case "TEACHER":
                        txtClassName.setVisibility(View.VISIBLE);
                        txtDivision.setVisibility(View.VISIBLE);
                        spnClass.setVisibility(View.VISIBLE);
                        spnDivision.setVisibility(View.VISIBLE);
                        break;
                    case "DRIVER":
                        txtClassName.setVisibility(View.GONE);
                        txtDivision.setVisibility(View.GONE);
                        spnClass.setVisibility(View.GONE);
                        spnDivision.setVisibility(View.GONE);
                        break;
                    case "STUDENT":
                        txtClassName.setVisibility(View.VISIBLE);
                        txtDivision.setVisibility(View.VISIBLE);
                        spnClass.setVisibility(View.VISIBLE);
                        spnDivision.setVisibility(View.VISIBLE);
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
            if (listSchool.get(i).equals(userModel.getUserInfoModel().getSchoolName())) {
                schoolPostion = i;
                break;
            }
        }
        for (int i = 0; i < listClass.size(); i++) {
            if (listClass.get(i).equals(userModel.getUserInfoModel().getClassName())) {
                classPostion = i;
                break;
            }
        }
        for (int i = 0; i < listDivision.size(); i++) {
            if (listDivision.get(i).equals(userModel.getUserInfoModel().getDivisionName())) {
                divisionPosition = i;
                break;
            }
        }
        spnClass.setSelection(classPostion);
        spnSchoolName.setSelection(schoolPostion);
        spnDivision.setSelection(divisionPosition);
        edtStudentName.setText(userModel.getFirstName());
        edtContactNumber.setText(userModel.getContactNumber());
        edtAddress.setText(userModel.getUserInfoModel().getAddress());
        edtBloodGrp.setText(userModel.getUserInfoModel().getBloodGroup());
        edtUsername.setText(userModel.getUsername());
        edtPassword.setText(userModel.getPassword());
        if (userModel.getUserInfoModel().getGender().equals("Boy")) {
            rdBoy.setChecked(true);
        } else {
            rdGirl.setChecked(true);
        }
    }

    private void bindModel() {
        userModel.setFirstName(edtStudentName.getText().toString().trim());
        userModel.getUserInfoModel().setAddress(edtAddress.getText().toString().trim());
        userModel.getUserInfoModel().setBloodGroup(edtBloodGrp.getText().toString().trim());
        userModel.setUsername(edtUsername.getText().toString().trim());
        userModel.setContactNumber(CommonUtils.asInt(edtContactNumber.getText().toString().trim(), 0));
        userModel.setUserType(spnUserType.getSelectedItem().toString().trim());
        userModel.setPassword(edtPassword.getText().toString().trim());
        userModel.getUserInfoModel().setSchoolName(spnSchoolName.getSelectedItem().toString());
        userModel.getUserInfoModel().setClassName(spnClass.getSelectedItem().toString().trim());
        userModel.getUserInfoModel().setDivisionName(spnDivision.getSelectedItem().toString().trim());
        if (rdGirl.isChecked()) {
            userModel.getUserInfoModel().setGender("Girl");
        } else if (rdBoy.isChecked()) {
            userModel.getUserInfoModel().setGender("Boy");
        }

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
                    if (Validator.validateForNulls(userModel, getMyActivity())) {
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

  /*  private void saveUserInformation() {

        try {
            AlertDialog.Builder builder = showAlertDialog(getMyActivity(), getString(R.string.user_reg_title), getString(R.string.save_msg));
            builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if (!isEdit) {
                        final String userId = getMyActivity().databaseReference.push().getKey();
                        userModel.setUniqKey(userId);
                        getMyActivity().databaseReference.child(Constants.MAIN_TABLE).child(Constants.STUDENT).child(userModel.getUniqKey()).setValue(userModel);
                        Toast.makeText(getMyActivity(), "Information Saved...", Toast.LENGTH_LONG).show();
                        //  getMyActivity().showFragment(getMyActivity().profileFragment, null);
                    } else {
                        getMyActivity().databaseReference.child(Constants.MAIN_TABLE).child(Constants.STUDENT).child(userModel.getUniqKey()).setValue(userModel);
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

    }*/

    private boolean check() {
        if (userModel.getUserInfoModel().getSchoolName() == null || userModel.getUserInfoModel().getSchoolName().isEmpty()) {
            getMyActivity().showToast("Please Select School Name");
            return false;
        }
        if (userModel.getUserType() == null || userModel.getUserType().isEmpty()) {
            getMyActivity().showToast("Please Select User Type");
            return false;
        }
        if ((userModel.getUserType().equals("STUDENT") || userModel.getUserType().equals("TEACHER")) && (userModel.getUserInfoModel().getClassName() == null || userModel.getUserInfoModel().getClassName().isEmpty())) {
            getMyActivity().showToast("Please Select Class");
            return false;
        }
        if ((userModel.getUserType().equals("STUDENT") || userModel.getUserType().equals("TEACHER")) && (userModel.getUserInfoModel().getDivisionName() == null || userModel.getUserInfoModel().getDivisionName().isEmpty())) {
            getMyActivity().showToast("Please Select Division");
            return false;
        }
        if (userModel.getFirstName() == null || userModel.getFirstName().isEmpty()) {
            getMyActivity().showToast("Please Enter Full Name");
            return false;
        }
        if (userModel.getContactNumber() <= 0) {
            getMyActivity().showToast("Please Enter Contact Number");
            return false;
        }
        if (userModel.getContactNumber() > 0 && userModel.getContactNumber() < 10) {
            getMyActivity().showToast("Please Enter Valid Contact Number");
            return false;
        }
        if (userModel.getUserInfoModel().getGender() == null || userModel.getUserInfoModel().getGender().isEmpty()) {
            getMyActivity().showToast("Please Select Gender");
            return false;
        }
        if (userModel.getUserInfoModel().getAddress() == null || userModel.getUserInfoModel().getAddress().isEmpty()) {
            getMyActivity().showToast("Please Enter Address");
            return false;
        }

        if (userModel.getUsername() == null || userModel.getUsername().isEmpty()) {
            getMyActivity().showToast("Please Enter User Name");
            return false;
        }
        if (userModel.getPassword() == null || userModel.getPassword().isEmpty()) {
            getMyActivity().showToast("Please Enter Password");
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

   /* private void showDatePicker(String flag) {
        DatePickerFragment date = new DatePickerFragment();
        *//**
     * Set Up Current Date Into dialog
     *//*
        Calendar calender = Calendar.getInstance();
        Bundle args = new Bundle();
        args.putInt("year", calender.get(Calendar.YEAR));
        args.putInt("month", calender.get(Calendar.MONTH));
        args.putInt("day", calender.get(Calendar.DAY_OF_MONTH));
        date.setArguments(args);
        */

    /**
     * Set Call back to capture selected date
     *//*
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

            userModel.setDateInsvestment2(formatedDate);
            datePickerInvest1.setText(CommonUtils.formatDateForDisplay(date, "dd-MM-yyyy"));


        }
    };*/

   /* DatePickerDialog.OnDateSetListener ondate2 = new DatePickerDialog.OnDateSetListener() {

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
            userModel.setDateInsvestment3(formatedDate);
            datePickerInvest2.setText(CommonUtils.formatDateForDisplay(date, "dd-MM-yyyy"));
        }
    };*/
    private void save() {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put(IJson.schoolName, userModel.getUserInfoModel().getSchoolName());
        if (userModel.getUserType().equals("STUDENT") || userModel.getUserType().equals("TEACHER")) {
            hashMap.put(IJson.className, userModel.getUserInfoModel().getClassName());
            hashMap.put(IJson.division, userModel.getUserInfoModel().getDivisionName());
        }
        hashMap.put(IJson.studentName, userModel.getFirstName());
        hashMap.put(IJson.studentAddress, userModel.getUserInfoModel().getAddress());
        hashMap.put(IJson.studentUserName, userModel.getUsername());
        hashMap.put(IJson.studentPassword, userModel.getPassword());
        hashMap.put(IJson.userType, userModel.getUserType());
        hashMap.put(IJson.studentBloodGrp, userModel.getUserInfoModel().getBloodGroup());
        hashMap.put(IJson.gender, userModel.getUserInfoModel().getGender());
        hashMap.put(IJson.contactNumber, userModel.getContactNumber());

        if (userModel.getPkeyId() != null) {
            hashMap.put(IJson.id, userModel.getPkeyId().toString());
        }
        CallWebService.getWebserviceObject(getMyActivity(), Request.Method.POST, IUrls.SIGN_UP, hashMap, new VolleyResponseListener<UserModel>() {
            @Override
            public void onResponse(UserModel[] object) {
                if (object[0] instanceof UserModel) {
                    for (UserModel bean : object) {

                    }
                }
            }

            @Override
            public void onResponse(UserModel object) {
                /*if (!isEdit) {
                    Toast.makeText(getMyActivity(), "Information Saved...", Toast.LENGTH_LONG).show();
                    // getMyActivity().showFragment(getMyActivity().profileFragment, null);
                } else {
                    Toast.makeText(getMyActivity(), "Information Saved...", Toast.LENGTH_LONG).show();
                    getMyActivity().showFragment(new UsersListFragment(), null);
                }*/
                getMyActivity().flagCallUserList = true;
                getMyActivity().performBackForDesign();

            }

            @Override
            public void onError(String message) {
                if (message != null && !message.isEmpty()) {
                    getMyActivity().showToast(message);
                }
            }
        }, UserModel.class);
    }

    private void showFeesDialog() {
        AddFeesDialogFragment dialog = new AddFeesDialogFragment(getMyActivity(), new DialogResultListner() {
            @Override
            public void getResult(FeesInstallmentsModel feesInstallmentsModel) {
                userModel.getStudentFeesModel().getFeesInstallmentsModels().add(feesInstallmentsModel);
            }
        });
        dialog.show(getMyActivity().getFragmentManager(), "FeesDialog");
    }


    public HomeActivity getMyActivity() {
        return (HomeActivity) getActivity();
    }
}