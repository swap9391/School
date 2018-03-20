package com.exa.mydemoapp.fragment;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.exa.mydemoapp.Common.CommonUtils;
import com.exa.mydemoapp.Common.Constants;
import com.exa.mydemoapp.HomeActivity;
import com.exa.mydemoapp.LoginActivity;
import com.exa.mydemoapp.R;
import com.exa.mydemoapp.adapter.StudentAttendaceAdapter;
import com.exa.mydemoapp.adapter.UserAdapter;
import com.exa.mydemoapp.annotation.ViewById;
import com.exa.mydemoapp.database.DbInvoker;
import com.exa.mydemoapp.listner.AttendanceListner;
import com.exa.mydemoapp.model.AttendaceModel;
import com.exa.mydemoapp.model.RewardModel;
import com.exa.mydemoapp.model.StudentAttendanceModel;
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
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

    private List<StudentModel> listStudent;
    private List<StudentAttendanceModel> selectedStudents;
    private DbInvoker dbInvoker;
    StudentAttendaceAdapter mAdapter;
    AttendaceModel attendaceModel;

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
        attendaceModel = new AttendaceModel();
        getMyActivity().toolbar.setTitle("Add Attendance");
        initViewBinding(view);
        List<String> listClass = Arrays.asList(getResources().getStringArray(R.array.class_type));
        ArrayAdapter<String> classAdapter = new ArrayAdapter<String>(getMyActivity(), android.R.layout.simple_spinner_item, listClass);
        classAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerClass.setAdapter(classAdapter);
        classAdapter.notifyDataSetChanged();
        selectedStudents = new ArrayList<>();
        initAdapter();

        btnIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bindModel();
                if (check()) {
                    save("STATUS_IN");
                }
            }
        });

        btnOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bindModel();
                if (check()) {
                    save("STATUS_OUT");
                }
            }
        });

        return view;
    }


    private void initAdapter() {
        listStudent = dbInvoker.getUserList();
        if (listStudent != null && listStudent.size() > 0) {
            mAdapter = new StudentAttendaceAdapter(listStudent, getMyActivity(), this);
            LinearLayoutManager mLayoutManager =
                    new LinearLayoutManager(getMyActivity());
            recyclerView.setLayoutManager(mLayoutManager);
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.setAdapter(mAdapter);
        }
    }

    private void bindModel() {
        attendaceModel.setDateStamp(CommonUtils.formatDateForDisplay(Calendar.getInstance().getTime(), Constants.DATE_FORMAT));
        attendaceModel.setClassName(spinnerClass.getSelectedItem().toString());
        attendaceModel.setStudentAttendanceModels(getSelectedStudents());
    }

    public List<StudentAttendanceModel> getSelectedStudents() {
        return selectedStudents;
    }

    public void setSelectedStudents(List<StudentAttendanceModel> selectedStudents) {
        this.selectedStudents = selectedStudents;
    }


    @Override
    public void present(StudentModel bean) {
        StudentAttendanceModel studentAttendanceModel = new StudentAttendanceModel();
        studentAttendanceModel.setStudentId(bean.getId());
        studentAttendanceModel.setPresent(true);
        getSelectedStudents().add(studentAttendanceModel);

    }

    @Override
    public void absent(StudentModel bean) {
        for (StudentAttendanceModel studentModel : getSelectedStudents()) {
            if (studentModel.getStudentId() == bean.getId()) {
                getSelectedStudents().remove(bean);
                return;
            }
        }
    }

    private boolean check() {
        if (attendaceModel.getDateStamp() == null || attendaceModel.getDateStamp().isEmpty()) {
            getMyActivity().showToast("Please Select Data");
            return false;
        }
        if (attendaceModel.getClassName() == null || attendaceModel.getClassName().isEmpty()) {
            getMyActivity().showToast("Please Select Class");
            return false;
        }
        if (attendaceModel.getStudentAttendanceModels() == null || attendaceModel.getStudentAttendanceModels().size() <= 0) {
            getMyActivity().showToast("Please Select Atleast One Student");
            return false;
        }

        return true;
    }

    private void save(String inStatus) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put(IJson.classId, "" + attendaceModel.getClassName());
        hashMap.put(IJson.dateStamp, "" + attendaceModel.getDateStamp());
        hashMap.put(IJson.inStatus, inStatus);

        CallWebService.getWebserviceObject(getMyActivity(), Request.Method.POST, IUrls.URL_ADD_REWARD, hashMap, new VolleyResponseListener<AttendaceModel>() {
            @Override
            public void onResponse(AttendaceModel[] object) {
            }

            @Override
            public void onResponse(AttendaceModel studentData) {

            }

            @Override
            public void onError(String message) {
                Toast.makeText(getMyActivity(), message, Toast.LENGTH_SHORT).show();
            }
        }, AttendaceModel.class);

    }

    private HomeActivity getMyActivity() {
        return (HomeActivity) getActivity();
    }
}
