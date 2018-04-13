package com.exa.mydemoapp.fragment;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
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
import com.exa.mydemoapp.model.StudentAttendanceDetailsModel;
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

/**
 * Created by midt-078 on 14/2/18.
 */

public class AttendanceFragment extends CommonFragment implements AttendanceListner {
    View view;
    @ViewById(R.id.recyclerView)
    private RecyclerView recyclerView;
    @ViewById(R.id.spinner_class_name)
    private Spinner spinnerClass;
    @ViewById(R.id.btn_in)
    private Button btnIn;
    @ViewById(R.id.btn_out)
    private Button btnOut;
    @ViewById(R.id.date_picker_event)
    private Button datePicker;

    private List<UserModel> listStudent;
    private List<StudentAttendanceDetailsModel> selectedStudents;
    HashMap<String, Object> selectedStudentMap = new HashMap<>();
    private DbInvoker dbInvoker;
    StudentAttendaceAdapter mAdapter;
    AttendanceMasterModel attendanceMasterModel;
    int lastClassName;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.layout_attendance, container, false);
        dbInvoker = new DbInvoker(getMyActivity());
        attendanceMasterModel = new AttendanceMasterModel();
        listStudent = new ArrayList<>();
        getMyActivity().toolbar.setTitle("Add Attendance");
        setHasOptionsMenu(true);
        initViewBinding(view);
        List<String> listClass = new ArrayList(Arrays.asList(getResources().getStringArray(R.array.class_type)));
        listClass.remove(0);
        ArrayAdapter<String> classAdapter = new ArrayAdapter<String>(getMyActivity(), android.R.layout.simple_spinner_item, listClass);
        classAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerClass.setAdapter(classAdapter);
        classAdapter.notifyDataSetChanged();

        spinnerClass.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                selectedStudents = new ArrayList<>();
                lastClassName = spinnerClass.getLastVisiblePosition();
                getAttendance(listClass.get(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }
        });


        btnIn.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View v) {
                bindModel();
                if (check()) {
                    save("STATUS_IN");
                }
            }
        });

        btnOut.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View v) {
                bindModel();
                if (check()) {
                    save("STATUS_OUT");
                }
            }
        });
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
        return view;
    }


    private void initAdapter(List<StudentAttendanceDetailsModel> listStudent) {
        //if (listStudent != null && listStudent.size() > 0) {
        mAdapter = new StudentAttendaceAdapter(listStudent, getMyActivity(), this);
        LinearLayoutManager mLayoutManager =
                new LinearLayoutManager(getMyActivity());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);
        /*} else {s
           mAdapter.
        }*/
    }

    private void bindModel() {
        attendanceMasterModel.setClassName(spinnerClass.getSelectedItem().toString());
        attendanceMasterModel.setStudentList(getSelectedStudents());
    }

    public List<StudentAttendanceDetailsModel> getSelectedStudents() {
        return selectedStudents;
    }

    public void setSelectedStudents(List<StudentAttendanceDetailsModel> selectedStudents) {
        this.selectedStudents = selectedStudents;
    }


    @Override
    public void present(StudentAttendanceDetailsModel bean, int position) {
        getSelectedStudents().get(position).setPresent(true);
    }

    @Override
    public void absent(StudentAttendanceDetailsModel bean, int position) {
        getSelectedStudents().get(position).setPresent(false);
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
        if (attendanceMasterModel.getStudentList() == null || attendanceMasterModel.getStudentList().size() <= 0) {
            getMyActivity().showToast("Please Select Atleast One Student");
            return false;
        }

        return true;
    }

    private void save(String inStatus) {

        AlertDialog.Builder builder = getMyActivity().showAlertDialog(getMyActivity(), getString(R.string.app_name), getString(R.string.save_msg));
        builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                List<StudentAttendanceDetailsModel> studentAttendanceModels = new ArrayList<>();
                for (StudentAttendanceDetailsModel bean : getSelectedStudents()) {
                    if (inStatus.equals("STATUS_IN")) {
                        bean.setStudentIn(true);
                    } else {
                        bean.setStudentIn(true);
                    }
                    studentAttendanceModels.add(bean);

                }


                HashMap<String, Object> hashMap = new HashMap<>();
                hashMap.put(IJson.classId, "" + attendanceMasterModel.getClassName());
                hashMap.put(IJson.dateStamp, "" + attendanceMasterModel.getAttendanceDate());
                hashMap.put(IJson.studentList, studentAttendanceModels);
                if (attendanceMasterModel.getPkeyId() != null && !attendanceMasterModel.getPkeyId().isEmpty()) {
                    hashMap.put(IJson.id, attendanceMasterModel.getPkeyId());
                }

                CallWebService.getWebserviceObject(getMyActivity(), Request.Method.POST, IUrls.URL_ADD_ATTENDANCE, hashMap, new VolleyResponseListener<AttendanceMasterModel>() {
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

    private void getAttendance(String className) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put(IJson.dateStamp, CommonUtils.formatDateForDisplay(Calendar.getInstance().getTime(), Constants.ONLY_DATE_FORMAT));
        hashMap.put(IJson.classId, className);
        CallWebService.getWebserviceObject(getMyActivity(), Request.Method.POST, IUrls.URL_GET_ATTENDANCE, hashMap, new VolleyResponseListener<AttendanceMasterModel>() {

            @Override
            public void onResponse(AttendanceMasterModel[] object) {

            }
            @Override
            public void onResponse() {
            }
            @Override
            public void onResponse(AttendanceMasterModel object) {

                attendanceMasterModel = object;
                for (StudentAttendanceDetailsModel attendanceModel : object.getStudentList()) {
                    UserModel userModel = dbInvoker.getStudentById(attendanceModel.getStudentId());
                    attendanceModel.setStudentName(userModel.getFirstName()+" "+userModel.getLastName());
                    attendanceModel.setStudentId(userModel.getPkeyId());
                    selectedStudents.add(attendanceModel);
                }

                initAdapter(selectedStudents);

            }

            @Override
            public void onError(String message) {
                if (message != null && message.isEmpty()) {
                    Toast.makeText(getMyActivity(), message, Toast.LENGTH_SHORT).show();
                }
             /*   UserModel studentModel0 = new UserModel();
                studentModel0.setId(1);
                studentModel0.setStudentName("Swapnil");
                UserModel studentModel1 = new UserModel();
                studentModel1.setId(2);
                studentModel1.setStudentName("Vaibhav");
                UserModel studentModel2 = new UserModel();
                studentModel2.setId(3);
                studentModel2.setStudentName("Manohar");
                UserModel studentModel3 = new UserModel();
                studentModel3.setId(4);
                studentModel3.setStudentName("Uday");
                listStudent.add(studentModel0);
                listStudent.add(studentModel1);
                listStudent.add(studentModel2);
                listStudent.add(studentModel3);
*/
                listStudent = dbInvoker.getUserListByStudent(className);
                for (UserModel userModel : listStudent) {
                    StudentAttendanceDetailsModel attendanceModel = new StudentAttendanceDetailsModel();
                    attendanceModel.setStudentName(userModel.getFirstName()+" "+userModel.getLastName());
                    attendanceModel.setStudentId(userModel.getPkeyId());
                    selectedStudents.add(attendanceModel);
                }

                initAdapter(selectedStudents);
            }
        }, AttendanceMasterModel.class);


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

    private HomeActivity getMyActivity() {
        return (HomeActivity) getActivity();
    }
}
