package com.exa.mydemoapp.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.CardView;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.amazonaws.mobileconnectors.s3.transferutility.TransferState;
import com.android.volley.Request;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
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
import com.exa.mydemoapp.listner.OtpListner;
import com.exa.mydemoapp.model.AlbumImagesModel;
import com.exa.mydemoapp.model.DropdownMasterModel;
import com.exa.mydemoapp.model.FeesInstallmentsModel;
import com.exa.mydemoapp.model.UserModel;
import com.exa.mydemoapp.s3Upload.S3FileTransferDelegate;
import com.exa.mydemoapp.s3Upload.S3UploadActivity;
import com.exa.mydemoapp.webservice.CallWebService;
import com.exa.mydemoapp.webservice.IJson;
import com.exa.mydemoapp.webservice.IUrls;
import com.exa.mydemoapp.webservice.VolleyResponseListener;

import java.io.File;
import java.util.ArrayList;
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
    private List<DropdownMasterModel> listClass;
    private List<DropdownMasterModel> listDivision;
    private List<DropdownMasterModel> listUserType;
    private List<DropdownMasterModel> listBloodGrp;
    @ViewById(R.id.toolbar)
    public Toolbar toolbar;
    @ViewById(R.id.spinner_school_name)
    private Spinner spnSchoolName;
    @ViewById(R.id.spinner_blood_group)
    private Spinner spnBloodGroup;
    @ViewById(R.id.spinner_class_name)
    private Spinner spnClass;
    @ViewById(R.id.spinner_division)
    private Spinner spnDivision;
    @ViewById(R.id.edt_first_name)
    private EditText edtFirstName;
    @ViewById(R.id.edt_middle_name)
    private EditText edtMiddleName;
    @ViewById(R.id.edt_last_name)
    private EditText edtLastName;
    @ViewById(R.id.edt_student_address)
    private EditText edtAddress;
    @ViewById(R.id.edt_student_username)
    private EditText edtUsername;
    @ViewById(R.id.edt_contact_number)
    private EditText edtContactNumber;
    @ViewById(R.id.edt_email)
    private EditText edtEmail;
    @ViewById(R.id.edt_qualification)
    private EditText edtQualification;
    @ViewById(R.id.edt_speciality)
    private EditText edtSpeciality;
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
    @ViewById(R.id.img_profile)
    ImageView imgProfile;
    @ViewById(R.id.date_picker_dob)
    Button btnDob;
    @ViewById(R.id.progress_bar)
    ProgressBar progressBar;
    File fileProfile;
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

        listClass = getMyActivity().getDbInvoker().getDropDownByType("CLASSTYPE");
        ArrayAdapter<DropdownMasterModel> classAdapter = new ArrayAdapter<>(getMyActivity(), android.R.layout.simple_spinner_item, listClass);
        classAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnClass.setAdapter(classAdapter);
        classAdapter.notifyDataSetChanged();

        listDivision = getMyActivity().getDbInvoker().getDropDownByType("DEVISION");
        ArrayAdapter<DropdownMasterModel> divisionAdapter = new ArrayAdapter<>(getMyActivity(), android.R.layout.simple_spinner_item, listDivision);
        divisionAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnDivision.setAdapter(divisionAdapter);
        divisionAdapter.notifyDataSetChanged();


        listUserType = getMyActivity().getDbInvoker().getDropDownByType("USERTYPE");
        ArrayAdapter<DropdownMasterModel> userTypeadapter = new ArrayAdapter<>(getMyActivity(), android.R.layout.simple_spinner_item, listUserType);
        userTypeadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnUserType.setAdapter(userTypeadapter);
        userTypeadapter.notifyDataSetChanged();

        listBloodGrp = getMyActivity().getDbInvoker().getDropDownByType("BLOODGROUP");
        ArrayAdapter<DropdownMasterModel> bloodGrpAdapter = new ArrayAdapter<>(getMyActivity(), android.R.layout.simple_spinner_item, listBloodGrp);
        bloodGrpAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnBloodGroup.setAdapter(bloodGrpAdapter);
        bloodGrpAdapter.notifyDataSetChanged();

        txtClassName.setVisibility(View.GONE);
        txtDivision.setVisibility(View.GONE);
        spnClass.setVisibility(View.GONE);
        spnDivision.setVisibility(View.GONE);
        edtQualification.setVisibility(View.GONE);
        edtSpeciality.setVisibility(View.GONE);
        Bundle bundle = getArguments();
        if (bundle != null) {
            userModel = (UserModel) bundle.getSerializable(Constants.INTENT_TYPE_USER_DATA);
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
                userModel.setUserType(listUserType.get(position).getServerValue());
                switch (listUserType.get(position).getDropdownValue()) {
                    case "ADMIN":
                        txtClassName.setVisibility(View.GONE);
                        txtDivision.setVisibility(View.GONE);
                        spnClass.setVisibility(View.GONE);
                        spnDivision.setVisibility(View.GONE);

                        edtQualification.setVisibility(View.GONE);
                        edtSpeciality.setVisibility(View.GONE);
                        break;
                    case "TEACHER":
                        txtClassName.setVisibility(View.VISIBLE);
                        txtDivision.setVisibility(View.VISIBLE);
                        spnClass.setVisibility(View.VISIBLE);
                        spnDivision.setVisibility(View.VISIBLE);
                        edtQualification.setVisibility(View.VISIBLE);
                        edtSpeciality.setVisibility(View.VISIBLE);

                        break;
                    case "DRIVER":
                        txtClassName.setVisibility(View.GONE);
                        txtDivision.setVisibility(View.GONE);
                        spnClass.setVisibility(View.GONE);
                        spnDivision.setVisibility(View.GONE);
                        edtQualification.setVisibility(View.GONE);
                        edtSpeciality.setVisibility(View.GONE);
                        break;
                    case "STUDENT":
                        txtClassName.setVisibility(View.VISIBLE);
                        txtDivision.setVisibility(View.VISIBLE);
                        spnClass.setVisibility(View.VISIBLE);
                        spnDivision.setVisibility(View.VISIBLE);
                        edtQualification.setVisibility(View.GONE);
                        edtSpeciality.setVisibility(View.GONE);
                        break;
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }
        });

        imgProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(pickPhoto, PICK_IMAGE);//one can be replaced with any action code
            }
        });

        btnDob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePicker();
            }
        });

        return view;
    }

    private void bindView() {
        int schoolPostion = 0;
        int classPostion = 0;
        int divisionPosition = 0;
        int userTypePosition = 0;
        int bloodGrpPosition = 0;

        for (int i = 0; i < listSchool.size(); i++) {
            if (listSchool.get(i).equals(userModel.getUserInfoModel().getSchoolName())) {
                schoolPostion = i;
                break;
            }
        }
        for (int i = 0; i < listBloodGrp.size(); i++) {
            if (listBloodGrp.get(i).equals(userModel.getUserInfoModel().getBloodGroup())) {
                bloodGrpPosition = i;
                break;
            }
        }
        for (int i = 0; i < listUserType.size(); i++) {
            if (listUserType.get(i).getServerValue().equals(userModel.getUserType())) {
                userTypePosition = i;
                break;
            }
        }
        for (int i = 0; i < listClass.size(); i++) {
            if (listClass.get(i).getServerValue().equals(userModel.getUserInfoModel().getClassName())) {
                classPostion = i;
                break;
            }
        }
        for (int i = 0; i < listDivision.size(); i++) {
            if (listDivision.get(i).getServerValue().equals(userModel.getUserInfoModel().getDivisionName())) {
                divisionPosition = i;
                break;
            }
        }
        edtUsername.setEnabled(false);
        spnUserType.setEnabled(false);
        spnClass.setSelection(classPostion);
        spnSchoolName.setSelection(schoolPostion);
        spnDivision.setSelection(divisionPosition);
        spnUserType.setSelection(userTypePosition);
        spnBloodGroup.setSelection(bloodGrpPosition);
        edtFirstName.setText(userModel.getFirstName());
        edtMiddleName.setText(userModel.getMiddleName());
        edtLastName.setText(userModel.getLastName());
        edtEmail.setText(userModel.getEmail());
        edtContactNumber.setText(userModel.getContactNumber());
        edtAddress.setText(userModel.getUserInfoModel().getAddress());
        edtUsername.setText(userModel.getUsername());
        edtSpeciality.setText(userModel.getUserInfoModel().getSpeciality());
        edtQualification.setText(userModel.getUserInfoModel().getQualification());
        long dob = CommonUtils.toLong(userModel.getUserInfoModel().getDateOfBirth());
        btnDob.setText(CommonUtils.formatDateForDisplay(new Date(dob), Constants.ONLY_DATE_FORMAT));
        if (userModel.getUserInfoModel().getGender().equals("Boy")) {
            rdBoy.setChecked(true);
        } else {
            rdGirl.setChecked(true);
        }
        progressBar.setVisibility(View.VISIBLE);

        Glide.with(this)
                .load(userModel.getProfilePicUrl())
                .asBitmap()
                .override(300, 300)
                .fitCenter()
                .placeholder(R.drawable.defualt_album_icon)
                .error(R.drawable.defualt_album_icon)
                .listener(new RequestListener<String, Bitmap>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<Bitmap> target, boolean isFirstResource) {
                        progressBar.setVisibility(View.GONE);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Bitmap resource, String model, Target<Bitmap> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        progressBar.setVisibility(View.GONE);
                        return false;
                    }
                })
                .into(imgProfile);


    }

    private void bindModel() {
        userModel.setFirstName(edtFirstName.getText().toString().trim());
        userModel.setMiddleName(edtMiddleName.getText().toString().trim());
        userModel.setLastName(edtLastName.getText().toString().trim());
        userModel.getUserInfoModel().setAddress(edtAddress.getText().toString().trim());
        userModel.setUsername(edtUsername.getText().toString().trim());
        userModel.setContactNumber(edtContactNumber.getText().toString().trim());
        userModel.setEmail(edtEmail.getText().toString().trim());
        userModel.getUserInfoModel().setSchoolName(listSchool.get(spnSchoolName.getSelectedItemPosition()));
        userModel.getUserInfoModel().setBloodGroup(listBloodGrp.get(spnBloodGroup.getSelectedItemPosition()).getServerValue());
        if (userModel.getUserType().equals(Constants.USER_TYPE_STUDENT) || userModel.getUserType().equals(Constants.USER_TYPE_TEACHER)) {
            userModel.getUserInfoModel().setClassName(listClass.get(spnClass.getSelectedItemPosition()).getServerValue());
            userModel.getUserInfoModel().setDivisionName(listDivision.get(spnDivision.getSelectedItemPosition()).getServerValue());
        }
        if (userModel.getUserType().equals(Constants.USER_TYPE_TEACHER)) {
            userModel.getUserInfoModel().setQualification(edtQualification.getText().toString().trim());
            userModel.getUserInfoModel().setSpeciality(edtSpeciality.getText().toString().trim());
        }

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
                                        uploadImages();
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
        if ((userModel.getUserType().equals(Constants.USER_TYPE_STUDENT) || userModel.getUserType().equals(Constants.USER_TYPE_TEACHER)) && (userModel.getUserInfoModel().getClassName() == null || userModel.getUserInfoModel().getClassName().isEmpty())) {
            getMyActivity().showToast("Please Select Class");
            return false;
        }
        if ((userModel.getUserType().equals(Constants.USER_TYPE_STUDENT) || userModel.getUserType().equals(Constants.USER_TYPE_TEACHER)) && (userModel.getUserInfoModel().getDivisionName() == null || userModel.getUserInfoModel().getDivisionName().isEmpty())) {
            getMyActivity().showToast("Please Select Division");
            return false;
        }
        if (userModel.getFirstName() == null || userModel.getFirstName().isEmpty()) {
            getMyActivity().showToast("Please Enter First Name");
            return false;
        }
        if (userModel.getMiddleName() == null || userModel.getMiddleName().isEmpty()) {
            getMyActivity().showToast("Please Enter Middle Name");
            return false;
        }
        if (userModel.getLastName() == null || userModel.getLastName().isEmpty()) {
            getMyActivity().showToast("Please Enter Last Name");
            return false;
        }
        if (userModel.getContactNumber() != null && userModel.getContactNumber().isEmpty()) {
            getMyActivity().showToast("Please Enter Contact Number");
            return false;
        }
        if (userModel.getContactNumber() != null && userModel.getContactNumber().length() < 10) {
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
        if (userModel.getUserType().equals(Constants.USER_TYPE_TEACHER) && edtQualification.getText().toString().isEmpty()) {
            getMyActivity().showToast("Please Enter Qualification");
            return false;
        }
        if (userModel.getUserType().equals(Constants.USER_TYPE_TEACHER) && edtSpeciality.getText().toString().isEmpty()) {
            getMyActivity().showToast("Please Enter Speciality");
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
        hashMap.put(IJson.username, userModel.getUsername());
        hashMap.put(IJson.userType, userModel.getUserType());
        hashMap.put(IJson.firstName, userModel.getFirstName());
        hashMap.put(IJson.middleName, userModel.getMiddleName());
        hashMap.put(IJson.lastName, userModel.getLastName());
        hashMap.put(IJson.profilePicUrl, userModel.getProfilePicUrl());
        hashMap.put(IJson.email, userModel.getEmail());
        hashMap.put(IJson.contactNumber, userModel.getContactNumber());
        hashMap.put(IJson.busRoute, userModel.getBusRoute());
        hashMap.put(IJson.userInfoModel, userModel.getUserInfoModel());
        hashMap.put(IJson.busRoute, "Moshi");
        int method;
        String url;
        if (userModel.getPkeyId() != null) {
            hashMap.put(IJson.id, userModel.getPkeyId().toString());
            method = Request.Method.PUT;
            url = String.format(IUrls.UPDATE_USER, userModel.getPkeyId().toString());
        } else {
            method = Request.Method.POST;
            url = IUrls.SIGN_UP;
        }
        CallWebService.getWebserviceObject(getMyActivity(), true, true, method, url, hashMap, new VolleyResponseListener<UserModel>() {
            @Override
            public void onResponse(UserModel[] object) {
                if (object[0] instanceof UserModel) {
                    for (UserModel bean : object) {

                    }
                }
                if (isEdit) {
                    getMyActivity().showFragment(getMyActivity().dashboardFragment, null);
                }
            }

            @Override
            public void onResponse() {
                if (isEdit) {
                    getMyActivity().showFragment(getMyActivity().dashboardFragment, null);
                }
            }

            @Override
            public void onResponse(UserModel object) {
                //if (isEdit) {
                    getMyActivity().showFragment(getMyActivity().dashboardFragment, null);
                //} else {
                //    showOtpDialog(object);
                //}
            }

            @Override
            public void onError(String message) {
                if (message != null && !message.isEmpty()) {
                    getMyActivity().showToast(message);
                }
            }
        }, UserModel.class);
    }

    /*private void showFeesDialog() {
        FeesInstallmentsModel feesInstallmentsModel = new FeesInstallmentsModel();
        if (userModel.getStudentFeesModel().getFeesInstallmentsModels().size() > 0) {

        }
        AddFeesDialogFragment dialog = new AddFeesDialogFragment(getMyActivity(), feesInstallmentsModel, userModel.getStudentFeesModel().getFeesInstallmentsModels(),false, new DialogResultListner() {
            @Override
            public void getResult(FeesInstallmentsModel feesInstallmentsModel) {
                int position = -1;
                for (FeesInstallmentsModel bean : userModel.getStudentFeesModel().getFeesInstallmentsModels()) {
                    position++;
                    if (bean.getInstallmentNo().equals(feesInstallmentsModel.getInstallmentNo())) {
                        userModel.getStudentFeesModel().getFeesInstallmentsModels().remove(position);
                    }
                }
                userModel.getStudentFeesModel().getFeesInstallmentsModels().add(feesInstallmentsModel);
                if (feesInstallmentsModel != null) {
                    userModel.getStudentFeesModel().setTotalFees(CommonUtils.asDouble(edtTotalFees.getText().toString().trim(), 0.0));
                    userModel.getStudentFeesModel().setNoOfInstallments(userModel.getStudentFeesModel().getFeesInstallmentsModels().size());
                }
                bindFeesView();
            }
        });
        dialog.show(getMyActivity().getFragmentManager(), "FeesDialog");
    }*/

    private void showOtpDialog(UserModel userModel) {
        OtpDialogFrag dialog = new OtpDialogFrag(new OtpListner() {
            @Override
            public void onResult(boolean flag) {
                if (flag) {
                    getMyActivity().flagCallUserList = true;
                    getMyActivity().performBackForDesign();
                }
            }
        }, userModel);
        dialog.show(getMyActivity().getFragmentManager(), "FeesDialog");
    }

    /*private void bindFeesView() {
        layout1.removeAllViews();
        for (FeesInstallmentsModel feesInstallmentsModel : userModel.getStudentFeesModel().getFeesInstallmentsModels()) {

            LinearLayout.LayoutParams layParam = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT

            );
            layParam.setMargins(5, 5, 5, 5);
            LinearLayout.LayoutParams textParam = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    1.0f
            );
            textParam.setMargins(5, 5, 5, 5);
            LinearLayout linearLayout = new LinearLayout(getMyActivity());
            linearLayout.setOrientation(LinearLayout.HORIZONTAL);
            linearLayout.setLayoutParams(textParam);

            TextView txtInstallmentType = new TextView(getMyActivity());
            TextView txtInstallmentAmount = new TextView(getMyActivity());
            TextView txtPayStatus = new TextView(getMyActivity());

            txtInstallmentType.setLayoutParams(textParam);
            txtInstallmentAmount.setLayoutParams(textParam);
            txtPayStatus.setLayoutParams(textParam);
            txtInstallmentType.setText(feesInstallmentsModel.getInstallmentNo());
            txtInstallmentAmount.setText(getStringById(R.string.Rs) + " " + feesInstallmentsModel.getInstallmentAmount());
            txtPayStatus.setText(feesInstallmentsModel.getIsPaid().endsWith("true") ? "Paid" : "Unpaid");
            linearLayout.addView(txtInstallmentType);
            linearLayout.addView(txtInstallmentAmount);
            linearLayout.addView(txtPayStatus);
            layout1.addView(linearLayout);

        }
    }
*/

    private void uploadImages() {
        if (fileProfile != null) {

            final ProgressDialog progressDialog = new ProgressDialog(getMyActivity());
            progressDialog.setTitle("Uploading... ");
            progressDialog.show();
            progressDialog.setCancelable(false);
            S3UploadActivity.uploadData(getMyActivity(), new S3FileTransferDelegate() {
                @Override
                public void onS3FileTransferStateChanged(int id, TransferState state, String url, Object object) {
                    progressDialog.dismiss();
                    File file = (File) object;
                    userModel.setProfilePicUrl(url);
                    save();
                }

                @Override
                public void onS3FileTransferProgressChanged(int id, String fileName, int percentage) {
                    progressDialog.setTitle("Uploading..." + percentage + "%    ");
                }

                @Override
                public void onS3FileTransferError(int id, String fileName, Exception ex) {
                    progressDialog.dismiss();
                }
            }, "schoolImage" + CommonUtils.formatDateForDisplay(Calendar.getInstance().getTime(), "ddMMyyyyhhmmss") + userModel.getUsername(), fileProfile);
        } else {
            save();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE && resultCode == getMyActivity().RESULT_OK
                && null != data) {
            Uri selectedImage = data.getData();
            Glide.with(this)
                    .load(selectedImage)
                    .asBitmap()
                    .override(300, 300)
                    .fitCenter()
                    .placeholder(R.drawable.defualt_album_icon)
                    .error(R.drawable.defualt_album_icon)
                    .into(imgProfile);
            fileProfile = new File(getMyActivity().getRealPathFromURI(selectedImage));
        } else {

            Log.i("SonaSys", "resultCode: " + resultCode);
            switch (resultCode) {
                case 0:
                    Log.i("SonaSys", "User cancelled");
                    break;
            }

        }
    }


    private void showDatePicker() {
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
        date.setCallBack(ondate);
        date.show(getMyActivity().getSupportFragmentManager(), "Date Picker");
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
            //annualCalenderMasterModel.setEventDate(formatedDate);
            btnDob.setText(CommonUtils.formatDateForDisplay(date, "dd MMM yyyy"));
            userModel.getUserInfoModel().setDateOfBirth("" + date.getTime());
        }
    };


    public HomeActivity getMyActivity() {
        return (HomeActivity) getActivity();
    }
}
