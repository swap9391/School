package com.exa.mydemoapp.fragment;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
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
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.exa.mydemoapp.Common.CommonUtils;
import com.exa.mydemoapp.Common.Constants;
import com.exa.mydemoapp.HomeActivity;
import com.exa.mydemoapp.R;
import com.exa.mydemoapp.annotation.ViewById;
import com.exa.mydemoapp.model.DropdownMasterModel;
import com.exa.mydemoapp.model.StudentModel;
import com.exa.mydemoapp.model.StudentRewardsModel;
import com.exa.mydemoapp.model.UserModel;
import com.exa.mydemoapp.webservice.CallWebService;
import com.exa.mydemoapp.webservice.IJson;
import com.exa.mydemoapp.webservice.IUrls;
import com.exa.mydemoapp.webservice.VolleyResponseListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

/**
 * Created by midt-078 on 12/2/18.
 */

public class RewardsPointsFragment extends CommonFragment {
    View view;
    @ViewById(R.id.spinner_class_name)
    private Spinner spnClass;
    @ViewById(R.id.spinner_student_name)
    private Spinner spinnerStudentName;
    @ViewById(R.id.spinner_reward_type)
    private Spinner spinnerRewardType;
    @ViewById(R.id.txt_student_spinner)
    private TextView txtStudentSpinnerTitle;
    @ViewById(R.id.edt_description)
    private EditText edt_description;
    @ViewById(R.id.edt_point)
    private EditText edt_points;
    @ViewById(R.id.spinner_division)
    private Spinner spnDivision;
    @ViewById(R.id.lbl_division)
    private TextView txtDivisionSpinnerTitle;

    private List<DropdownMasterModel> listClass;
    private List<DropdownMasterModel> listDivision;
    private List<StudentModel> listStudent;
    List<DropdownMasterModel> listRewardType;
    StudentRewardsModel rewardModel;
    boolean apiFlag = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.layout_rewards_points, container, false);
        getMyActivity().toolbar.setTitle(getStringById(R.string.dashboard_reward));
        getMyActivity().init();
        initViewBinding(view);

        rewardModel = new StudentRewardsModel();

        listClass = getMyActivity().getDbInvoker().getDropDownByType("CLASSTYPE");
        ArrayAdapter<DropdownMasterModel> classAdapter = new ArrayAdapter<DropdownMasterModel>(getMyActivity(), android.R.layout.simple_spinner_item, listClass);
        classAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnClass.setAdapter(classAdapter);
        classAdapter.notifyDataSetChanged();

        listRewardType = getMyActivity().getDbInvoker().getDropDownByType("REWARDTYPE");
        ArrayAdapter<DropdownMasterModel> rewardAdapter = new ArrayAdapter<>(getMyActivity(), android.R.layout.simple_spinner_item, listRewardType);
        rewardAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerRewardType.setAdapter(rewardAdapter);
        rewardAdapter.notifyDataSetChanged();


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
                    rewardModel.setStudentId(null);
                    rewardModel.setDivisionName(null);
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

    private void bindModel() {
        rewardModel.setClassName(spnClass.getSelectedItem().toString());
        rewardModel.setDescription(edt_description.getText().toString().trim());
        rewardModel.setRewardType(spinnerRewardType.getSelectedItem().toString());
        rewardModel.setPoints(CommonUtils.asInt(edt_points.getText().toString(), 0));
        if (rewardModel.getClassName().equals("All")) {
            rewardModel.setStudentId(null);
            rewardModel.setDivisionName(null);
        } else {
            rewardModel.setStudentId(listStudent.get(spinnerStudentName.getSelectedItemPosition()).getPkeyId());
            rewardModel.setDivisionName(listDivision.get(spnDivision.getSelectedItemPosition()).getServerValue());
        }
    }

    private void setPoints(String rewardType) {
        final String sport = getStringById(R.string.reward_sports);
        final String cultural = getStringById(R.string.reward_cultural);
        String interschool = getStringById(R.string.reward_inter_school);
        String academics = getStringById(R.string.reward_academics);
        String other = getStringById(R.string.reward_other);
        if (rewardType != null) {
            if (rewardType.equals(sport)) {
                rewardModel.setPoints(Constants.REWARD_SPORT);
            } else if (rewardType.equals(cultural)) {
                rewardModel.setPoints(Constants.REWARD_CULTURAL);
            } else if (rewardType.equals(interschool)) {
                rewardModel.setPoints(Constants.REWARD_INTERSCHOOL);
            } else if (rewardType.equals(academics)) {
                rewardModel.setPoints(Constants.REWARD_ACADEMICS);
            } else if (rewardType.equals(other)) {
                rewardModel.setPoints(Constants.REWARD_OTHER);
            }
        }
    }

    private boolean check() {
        if (rewardModel.getClassName() == null || rewardModel.getClassName().equals("")) {
            getMyActivity().showToast(getString(R.string.valid_class_name));
            return false;
        }
        if (rewardModel.getStudentId() == null || rewardModel.getStudentId().equals("")) {
            getMyActivity().showToast(getString(R.string.valid_student_name));
            return false;
        }
        if (rewardModel.getRewardType() == null || rewardModel.getRewardType().equals("")) {
            getMyActivity().showToast(getString(R.string.valid_reward_type));
            return false;
        }
        if (rewardModel.getPoints() <= 0) {
            getMyActivity().showToast(getString(R.string.valid_points));
            return false;
        }

        if (rewardModel.getDescription() == null || rewardModel.getDescription().equals("")) {
            getMyActivity().showToast(getString(R.string.valid_description));
            return false;
        }

        return true;
    }


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
                    AlertDialog.Builder builder = getMyActivity().showAlertDialog(getMyActivity(), getString(R.string.app_name), getString(R.string.save_msg));
                    builder.setPositiveButton(getString(R.string.dialog_button_save), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            bindModel();
                            if (check()) {
                                save();
                            }
                        }
                    }).setNegativeButton(getString(R.string.dialog_button_cancel), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();

                        }
                    }).show();
                } catch (Throwable throwable) {
                    throwable.printStackTrace();
                }

                break;
            case R.id.action_gallery:
                getMyActivity().showFragment(new NewsFeedFragment(), null);
                break;

        }
        return true;
    }


    private void save() {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put(IJson.className, rewardModel.getClassName());
        hashMap.put(IJson.division, rewardModel.getDivisionName());
        hashMap.put(IJson.studentId, rewardModel.getStudentId());
        hashMap.put(IJson.rewardType, rewardModel.getRewardType());
        hashMap.put(IJson.points, rewardModel.getPoints());
        hashMap.put(IJson.description, rewardModel.getDescription());
        if (rewardModel.getPkeyId() != null) {
            hashMap.put(IJson.id, rewardModel.getPkeyId().toString());
        }

        CallWebService.getWebserviceObject(getMyActivity(), true, true, Request.Method.POST, IUrls.URL_ADD_REWARD, hashMap, new VolleyResponseListener<StudentRewardsModel>() {
            @Override
            public void onResponse(StudentRewardsModel[] object) {
            }

            @Override
            public void onResponse(StudentRewardsModel studentData) {
                getMyActivity().showFragment(new DashboardFragment(), null);
            }

            @Override
            public void onResponse() {
            }

            @Override
            public void onError(String message) {
                Toast.makeText(getMyActivity(), message, Toast.LENGTH_SHORT).show();
            }
        }, StudentRewardsModel.class);

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


    private HomeActivity getMyActivity() {
        return (HomeActivity) getActivity();
    }
}
