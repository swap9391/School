package com.exa.mydemoapp.fragment;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.Request;
import com.exa.mydemoapp.HomeActivity;
import com.exa.mydemoapp.R;
import com.exa.mydemoapp.annotation.ViewById;
import com.exa.mydemoapp.model.DailyHomeworkModel;
import com.exa.mydemoapp.model.DropdownMasterModel;
import com.exa.mydemoapp.model.StudentAttendanceDetailsModel;
import com.exa.mydemoapp.model.StudentModel;
import com.exa.mydemoapp.model.UserModel;
import com.exa.mydemoapp.webservice.CallWebService;
import com.exa.mydemoapp.webservice.IUrls;
import com.exa.mydemoapp.webservice.VolleyResponseListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class HomeWorkListFragment extends CommonFragment {
    View view;
    @ViewById(R.id.spinner_class_name)
    private Spinner spnClass;
    @ViewById(R.id.spinner_division)
    private Spinner spnDivision;
    @ViewById(R.id.spinner_student_name)
    private Spinner spinnerStudentName;
    @ViewById(R.id.txt_student_spinner)
    private TextView txtStudentSpinnerTitle;
    @ViewById(R.id.lbl_division)
    private TextView txtDivisionSpinnerTitle;
    @ViewById(R.id.recyclerView)
    private RecyclerView recyclerView;
    private List<DropdownMasterModel> listClass;
    private List<DropdownMasterModel> listDivision;
    private List<StudentModel> listStudent;
    private List<UserModel> listStudents;
    private List<String> listStudentName;
    private List<DailyHomeworkModel> listDailyHomeworkModels;

    DailyHomeworkModel dailyHomeworkModel;
    boolean apiFlag = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.upload_info_layout, container, false);
        getMyActivity().toolbar.setTitle(getString(R.string.dashboard_upload));
        getMyActivity().init();
        initViewBinding(view);


        listClass = getMyActivity().getDbInvoker().getDropDownByType("CLASSTYPE");
        ArrayAdapter<DropdownMasterModel> classAdapter = new ArrayAdapter<DropdownMasterModel>(getMyActivity(), android.R.layout.simple_spinner_item, listClass);
        classAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnClass.setAdapter(classAdapter);
        classAdapter.notifyDataSetChanged();

        DropdownMasterModel allDropdownMasterModel = new DropdownMasterModel();
        allDropdownMasterModel.setDropdownValue("All");
        allDropdownMasterModel.setServerValue(null);
        listDivision = new ArrayList<>();
        listDivision.add(allDropdownMasterModel);
        listDivision = getMyActivity().getDbInvoker().getDropDownByType("DEVISION");
        ArrayAdapter<DropdownMasterModel> divisionAdapter = new ArrayAdapter<>(getMyActivity(), android.R.layout.simple_spinner_item, listDivision);
        divisionAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnDivision.setAdapter(divisionAdapter);
        divisionAdapter.notifyDataSetChanged();


        spnDivision.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                getStudent(listClass.get(spnClass.getSelectedItemPosition()).getServerValue(), listDivision.get(position).getServerValue());

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spnClass.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (listClass.get(spnClass.getSelectedItemPosition()).getDropdownValue().equals("All")) {
                    spnDivision.setVisibility(View.GONE);
                    spinnerStudentName.setVisibility(View.GONE);
                    txtDivisionSpinnerTitle.setVisibility(View.GONE);
                    txtStudentSpinnerTitle.setVisibility(View.GONE);
                    dailyHomeworkModel.setStudentId(null);
                    dailyHomeworkModel.setDivisionName(null);
                } else {
                    spnDivision.setVisibility(View.VISIBLE);
                    spinnerStudentName.setVisibility(View.VISIBLE);
                    txtDivisionSpinnerTitle.setVisibility(View.VISIBLE);
                    txtStudentSpinnerTitle.setVisibility(View.VISIBLE);
                    getStudent(listClass.get(position).getServerValue(), listDivision.get(spnDivision.getSelectedItemPosition()).getServerValue());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        return view;
    }


    public void getStudent(String classId, String divisionId) {
        if (!apiFlag) {
            apiFlag = true;
            HashMap<String, Object> hashMap = new HashMap<>();
      /*  hashMap.put(IJson.userId, userModel.getPkeyId());
        hashMap.put(IJson.otp, txtOtp.getText().toString());*/
            String url = String.format(IUrls.URL_CLASS_WISE_STUDENT, classId, divisionId);
            Log.d("url", url);
            CallWebService.getWebservice(getMyActivity(), Request.Method.GET, url, hashMap, new VolleyResponseListener<StudentModel>() {
                @Override
                public void onResponse(StudentModel[] object) {
                    listStudent = new ArrayList<>();
                    StudentModel allStudent = new StudentModel();
                    allStudent.setFullName("All");
                    allStudent.setPkeyId(null);
                    listStudent.add(allStudent);
                    for (StudentModel studentModel : object) {
                        listStudent.add(studentModel);
                    }
                    ArrayAdapter<StudentModel> studentAdapter = new ArrayAdapter(getMyActivity(), android.R.layout.simple_spinner_item, listStudent);
                    studentAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinnerStudentName.setAdapter(studentAdapter);
                    studentAdapter.notifyDataSetChanged();


                    apiFlag = false;
                }

                @Override
                public void onResponse() {
                }

                @Override
                public void onResponse(StudentModel object) {

                }

                @Override
                public void onError(String message) {
                    spinnerStudentName.setVisibility(View.GONE);
                    txtStudentSpinnerTitle.setVisibility(View.GONE);
                    if (message != null && !message.isEmpty()) {
                        getMyActivity().showToast(message);
                    }
                    apiFlag = false;
                }
            }, StudentModel[].class);

        }
    }


   /* public void getInStudent(String classId, String divisionId, String studentId) {
        if (!apiFlag) {
            apiFlag = true;
            HashMap<String, Object> hashMap = new HashMap<>();
            String url = String.format(IUrls.URL_GET_ATTENDANCE_LIST, classId, divisionId, attendanceMasterModel.getAttendanceDate());
            Log.d("url", url);
            CallWebService.getWebservice(getMyActivity(), Request.Method.GET, url, hashMap, new VolleyResponseListener<DailyHomeworkModel>() {
                @Override
                public void onResponse(DailyHomeworkModel[] object) {
                    recyclerView.setVisibility(View.VISIBLE);
                    listDailyHomeworkModels = new ArrayList<>();
                    listDailyHomeworkModels.addAll(Arrays.asList(object));
                    apiFlag = false;
                    initAdapter(listDailyHomeworkModels);
                }

                @Override
                public void onResponse() {
                }

                @Override
                public void onResponse(DailyHomeworkModel object) {

                }

                @Override
                public void onError(String message) {
                    recyclerView.setVisibility(View.INVISIBLE);
                    if (message != null && !message.isEmpty()) {
                        getMyActivity().showToast(message);
                    }

                    apiFlag = false;
                }
            }, DailyHomeworkModel[].class);

        }
    }*/

    public HomeActivity getMyActivity() {
        return (HomeActivity) getActivity();
    }
}
