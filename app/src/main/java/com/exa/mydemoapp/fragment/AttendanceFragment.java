package com.exa.mydemoapp.fragment;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.exa.mydemoapp.Common.CommonUtils;
import com.exa.mydemoapp.Common.Constants;
import com.exa.mydemoapp.HomeActivity;
import com.exa.mydemoapp.R;
import com.exa.mydemoapp.adapter.StudentAttendaceAdapter;
import com.exa.mydemoapp.annotation.ViewById;
import com.exa.mydemoapp.database.DbInvoker;
import com.exa.mydemoapp.listner.AttendanceListner;
import com.exa.mydemoapp.model.AttendanceMasterModel;
import com.exa.mydemoapp.model.DropdownMasterModel;
import com.exa.mydemoapp.model.StudentAttendanceDetailsModel;
import com.exa.mydemoapp.model.StudentModel;
import com.exa.mydemoapp.model.UserModel;
import com.exa.mydemoapp.webservice.CallWebService;
import com.exa.mydemoapp.webservice.IJson;
import com.exa.mydemoapp.webservice.IUrls;
import com.exa.mydemoapp.webservice.VolleyResponseListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by midt-078 on 14/2/18.
 */

public class AttendanceFragment extends CommonFragment implements AttendanceListner {
    View view;
    @ViewById(R.id.recyclerView)
    private RecyclerView recyclerView;
    @ViewById(R.id.spinner_class_name)
    private Spinner spinnerClass;
    @ViewById(R.id.spinner_division)
    private Spinner spinnerDivision;
    @ViewById(R.id.date_picker_event)
    private Button datePicker;

    private Map<String, StudentAttendanceDetailsModel> selectedStudents = new HashMap<>();
    StudentAttendaceAdapter mAdapter;
    AttendanceMasterModel attendanceMasterModel;
    int lastClassName;
    private List<DropdownMasterModel> listDivision;
    List<DropdownMasterModel> listClass;
    private List<StudentAttendanceDetailsModel> listStudentAttendanceDetailsModels;
    boolean apiFlag = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.layout_attendance, container, false);
        attendanceMasterModel = new AttendanceMasterModel();
        getMyActivity().toolbar.setTitle(getString(R.string.dashboard_attendance));
        setHasOptionsMenu(true);
        initViewBinding(view);

        datePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePicker();
            }
        });
        datePicker.setText(CommonUtils.formatDateForDisplay(Calendar.getInstance().getTime(), "dd MMM yyyy"));
        // attendanceMasterModel.setDateStamp(CommonUtils.formatDateForDisplay(Calendar.getInstance().getTime(), Constants.ONLY_DATE_FORMAT));
        long timestamp = System.currentTimeMillis() / 1000;
        attendanceMasterModel.setAttendanceDate(timestamp);

        listClass = getMyActivity().getDbInvoker().getDropDownByType("CLASSTYPE");
        listClass.remove(0);
        ArrayAdapter<DropdownMasterModel> classAdapter = new ArrayAdapter<DropdownMasterModel>(getMyActivity(), android.R.layout.simple_spinner_item, listClass);
        classAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerClass.setAdapter(classAdapter);
        classAdapter.notifyDataSetChanged();

        spinnerClass.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                lastClassName = spinnerClass.getLastVisiblePosition();
                getInStudent(listClass.get(position).getServerValue(), listDivision.get(spinnerDivision.getSelectedItemPosition()).getServerValue());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }
        });


        spinnerDivision.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                getInStudent(listClass.get(spinnerClass.getSelectedItemPosition()).getServerValue(), listDivision.get(position).getServerValue());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        DropdownMasterModel allDropdownMasterModel = new DropdownMasterModel();
        allDropdownMasterModel.setDropdownValue("All");
        allDropdownMasterModel.setServerValue(null);
        listDivision = new ArrayList<>();
        listDivision.add(allDropdownMasterModel);
        listDivision = getMyActivity().getDbInvoker().getDropDownByType("DEVISION");
        ArrayAdapter<DropdownMasterModel> divisionAdapter = new ArrayAdapter<>(getMyActivity(), android.R.layout.simple_spinner_item, listDivision);
        divisionAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerDivision.setAdapter(divisionAdapter);
        divisionAdapter.notifyDataSetChanged();


        return view;
    }


    public void getStudent(String classId, String divisionId) {
        if (!apiFlag) {
            apiFlag = true;
            HashMap<String, Object> hashMap = new HashMap<>();
            String url = String.format(IUrls.URL_CLASS_WISE_STUDENT, classId, divisionId);
            Log.d("url", url);
            CallWebService.getWebservice(getMyActivity(), Request.Method.GET, url, hashMap, new VolleyResponseListener<StudentModel>() {
                @Override
                public void onResponse(StudentModel[] object) {
                    recyclerView.setVisibility(View.VISIBLE);
                    listStudentAttendanceDetailsModels = new ArrayList<>();
                    for (StudentModel studentModel : object) {
                        StudentAttendanceDetailsModel model = new StudentAttendanceDetailsModel();
                        model.setStudentId(studentModel.getPkeyId());
                        model.setStudentName(studentModel.getFullName());
                        listStudentAttendanceDetailsModels.add(model);
                    }
                    apiFlag = false;
                    initAdapter(listStudentAttendanceDetailsModels);
                }

                @Override
                public void onResponse() {
                }

                @Override
                public void onResponse(StudentModel object) {

                }

                @Override
                public void onError(String message) {
                    recyclerView.setVisibility(View.INVISIBLE);
                    if (message != null && !message.isEmpty()) {
                        getMyActivity().showToast(message);
                    }
                    apiFlag = false;
                }
            }, StudentModel[].class);

        }
    }

    public void getInStudent(String classId, String divisionId) {
        if (!apiFlag) {
            apiFlag = true;
            HashMap<String, Object> hashMap = new HashMap<>();
            String url = String.format(IUrls.URL_GET_ATTENDANCE_LIST, classId, divisionId,attendanceMasterModel.getAttendanceDate());
            Log.d("url", url);
            CallWebService.getWebservice(getMyActivity(), Request.Method.GET, url, hashMap, new VolleyResponseListener<StudentAttendanceDetailsModel>() {
                @Override
                public void onResponse(StudentAttendanceDetailsModel[] object) {
                    recyclerView.setVisibility(View.VISIBLE);
                    listStudentAttendanceDetailsModels = new ArrayList<>();
                    listStudentAttendanceDetailsModels.addAll(Arrays.asList(object));
                    apiFlag = false;
                    initAdapter(listStudentAttendanceDetailsModels);
                }

                @Override
                public void onResponse() {
                }

                @Override
                public void onResponse(StudentAttendanceDetailsModel object) {

                }

                @Override
                public void onError(String message) {
                    recyclerView.setVisibility(View.INVISIBLE);
                    if (message != null && !message.isEmpty()) {
                        getMyActivity().showToast(message);
                    }
                    apiFlag = false;
                }
            }, StudentAttendanceDetailsModel[].class);

        }
    }


    private void initAdapter(List<StudentAttendanceDetailsModel> listStudent) {
        //if (listStudent != null && listStudent.size() > 0) {
        mAdapter = new StudentAttendaceAdapter(listStudent, getMyActivity(), this);
        LinearLayoutManager mLayoutManager =
                new LinearLayoutManager(getMyActivity());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
        /*} else {s
           mAdapter.
        }*/
    }

    private void bindModel() {
        attendanceMasterModel.setClassName(listClass.get(spinnerClass.getSelectedItemPosition()).getServerValue());
        attendanceMasterModel.setDivisionName(listDivision.get(spinnerDivision.getSelectedItemPosition()).getServerValue());
        //  attendanceMasterModel.setStudentList(getSelectedStudents());
    }


    @Override
    public void present(StudentAttendanceDetailsModel bean, int position) {
        bean.setPresent(true);
        selectedStudents.put(bean.getStudentId(), bean);
    }

    @Override
    public void absent(StudentAttendanceDetailsModel bean, int position) {
        // getSelectedStudents().get(position).setPresent(false);
        bean.setPresent(false);
        selectedStudents.put(bean.getStudentId(), bean);
    }

    private boolean check() {
        if (attendanceMasterModel.getAttendanceDate() <= 0) {
            getMyActivity().showToast("Please Select Date");
            return false;
        }
        if (attendanceMasterModel.getClassName() == null || attendanceMasterModel.getClassName().isEmpty()) {
            getMyActivity().showToast("Please Select Class");
            return false;
        }
        if (selectedStudents.isEmpty()) {
            getMyActivity().showToast("Please Select Atleast One Student");
            return false;
        }

        return true;
    }

    private void inSave() {

        AlertDialog.Builder builder = getMyActivity().showAlertDialog(getMyActivity(), getString(R.string.app_name), getString(R.string.save_msg));
        builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                List<StudentAttendanceDetailsModel> studentAttendanceModels = new ArrayList<StudentAttendanceDetailsModel>(selectedStudents.values());
              /*  for (StudentAttendanceDetailsModel bean : getSelectedStudents()) {
                    if (inStatus.equals("STATUS_IN")) {
                        bean.setStudentIn(true);
                    } else {
                        bean.setStudentIn(true);
                    }
                    studentAttendanceModels.add(bean);

                }*/


                HashMap<String, Object> hashMap = new HashMap<>();
                hashMap.put(IJson.className, "" + attendanceMasterModel.getClassName());
                hashMap.put(IJson.division, "" + attendanceMasterModel.getDivisionName());
                hashMap.put(IJson.attendanceDate, "" + attendanceMasterModel.getAttendanceDate());
                hashMap.put(IJson.studentList, studentAttendanceModels);
                if (attendanceMasterModel.getPkeyId() != null && !attendanceMasterModel.getPkeyId().isEmpty()) {
                    hashMap.put(IJson.id, attendanceMasterModel.getPkeyId());
                }

                CallWebService.getWebserviceObject(getMyActivity(), true, true, Request.Method.POST, IUrls.URL_ADD_ATTENDANCE, hashMap, new VolleyResponseListener<AttendanceMasterModel>() {
                    @Override
                    public void onResponse(AttendanceMasterModel[] object) {
                    }

                    @Override
                    public void onResponse(AttendanceMasterModel studentData) {
                        getMyActivity().showFragment(new DashboardFragment(), null);
                    }

                    @Override
                    public void onResponse() {
                    }

                    @Override
                    public void onError(String message) {
                        Toast.makeText(getMyActivity(), message, Toast.LENGTH_SHORT).show();
                    }
                }, AttendanceMasterModel.class);


            }
        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        }).show();


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
            String formatedDate = CommonUtils.formatDateForDisplay(date, Constants.ONLY_DATE_FORMAT);
            //attendanceMasterModel.setDateStamp(formatedDate);
            datePicker.setText(CommonUtils.formatDateForDisplay(date, "dd MMM yyyy"));
        }
    };

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // TODO Add your menu entries here
        inflater.inflate(R.menu.menu_save_info, menu);
        menu.findItem(R.id.action_gallery).setVisible(false);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_save:
                try {
                    bindModel();
                    if (check()) {
                        inSave();
                    }
                } catch (Exception e) {

                }
        }
        return true;
    }

    private HomeActivity getMyActivity() {
        return (HomeActivity) getActivity();
    }
}
