package com.exa.mydemoapp.fragment;

import android.app.Activity;
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

import com.exa.mydemoapp.Common.CommonUtils;
import com.exa.mydemoapp.Common.Connectivity;
import com.exa.mydemoapp.Common.Constants;
import com.exa.mydemoapp.HomeActivity;
import com.exa.mydemoapp.R;
import com.exa.mydemoapp.annotation.ViewById;
import com.exa.mydemoapp.model.ImageRequest;
import com.exa.mydemoapp.model.RewardModel;
import com.exa.mydemoapp.model.StudentModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
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

    List<String> listClass;
    List<String> listStudentName;
    List<String> listRewardType;
    List<StudentModel> listStudent;
    ProgressDialog progressDialog;

    RewardModel rewardModel;

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

        rewardModel = new RewardModel();

        listClass = Arrays.asList(getResources().getStringArray(R.array.class_type));
        //listClass.remove(new String("All"));
        ArrayAdapter<String> classAdapter = new ArrayAdapter<String>(getMyActivity(), android.R.layout.simple_spinner_item, listClass);
        classAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnClass.setAdapter(classAdapter);
        classAdapter.notifyDataSetChanged();

        listRewardType = Arrays.asList(getResources().getStringArray(R.array.reward_type));
        ArrayAdapter<String> rewardAdapter = new ArrayAdapter<String>(getMyActivity(), android.R.layout.simple_spinner_item, listRewardType);
        rewardAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerRewardType.setAdapter(rewardAdapter);
        rewardAdapter.notifyDataSetChanged();


        spnClass.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                if (!listClass.get(position).equals("All")) {

                    if (Connectivity.isConnected(getMyActivity())) {
                        progressDialog = new ProgressDialog(getMyActivity());
                        progressDialog.setTitle("Loading Student List...");
                        progressDialog.show();
                        getStudents(listClass.get(position));
                    } else {
                        getMyActivity().showToast("Please Connect to internet !!");
                    }
                } else {
                    spinnerStudentName.setVisibility(View.GONE);
                    txtStudentSpinnerTitle.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }
        });

        return view;
    }


    public void getStudents(String className) {
        listStudent = new ArrayList<>();
        listStudentName = new ArrayList<>();
        DatabaseReference ref1 = getMyActivity().databaseReference.child(Constants.MAIN_TABLE);
        DatabaseReference ref2 = ref1.child(Constants.STUDENT);
        Query query = ref2.orderByChild("className").equalTo(className);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                progressDialog.dismiss();
                for (DataSnapshot Snapshot : dataSnapshot.getChildren()) {
                    StudentModel studentData = Snapshot.getValue(StudentModel.class);
                    listStudent.add(studentData);
                    listStudentName.add(studentData.getStudentName() + " " + studentData.getRollNumber());
                }
                if (listStudent != null && listStudent.size() > 0) {
                    spinnerStudentName.setVisibility(View.VISIBLE);
                    txtStudentSpinnerTitle.setVisibility(View.VISIBLE);
                    ArrayAdapter<String> classAdapter = new ArrayAdapter<String>(getMyActivity(), android.R.layout.simple_spinner_item, listStudentName);
                    classAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinnerStudentName.setAdapter(classAdapter);
                    classAdapter.notifyDataSetChanged();
                } else {
                    spinnerStudentName.setVisibility(View.GONE);
                    txtStudentSpinnerTitle.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("Exception", "onCancelled", databaseError.toException());
                progressDialog.dismiss();
            }
        });

    }

    private void bindModel() {
        rewardModel.setClassName(spnClass.getSelectedItem().toString());
        if (listStudentName != null && listStudentName.size() > 0) {
            String studentId = listStudent.get(spinnerStudentName.getSelectedItemPosition()).getUniqKey();
            if (studentId != null) {
                rewardModel.setStudentId(studentId);
            }
        }
        rewardModel.setDescription(edt_description.getText().toString().trim());
        rewardModel.setRewardType(spinnerRewardType.getSelectedItem().toString());
        rewardModel.setDateStamp(CommonUtils.formatDateForDisplay(Calendar.getInstance().getTime(), Constants.DATE_FORMAT));
        rewardModel.setPoints(CommonUtils.asInt(edt_points.getText().toString(), 0));
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
            getMyActivity().showToast("Please Select Class Name");
            return false;
        }
        if (rewardModel.getStudentId() == null || rewardModel.getStudentId().equals("")) {
            getMyActivity().showToast("Please Select Student Name");
            return false;
        }
        if (rewardModel.getRewardType() == null || rewardModel.getRewardType().equals("")) {
            getMyActivity().showToast("Please Select Reward Type");
            return false;
        }
        if (rewardModel.getPoints() <= 0) {
            getMyActivity().showToast("Please Enter Points");
            return false;
        }

        if (rewardModel.getDescription() == null || rewardModel.getDescription().equals("")) {
            getMyActivity().showToast("Please Enter Reward Description");
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
                    builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            bindModel();
                            if (check()) {
                                if (Connectivity.isConnected(getMyActivity())) {
                                    saveUserInformation();
                                } else {
                                    getMyActivity().showToast("Please Connect to internet !!");
                                }
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

                break;
            case R.id.action_gallery:
                getMyActivity().showFragment(new NewsFeedFragment(), null);
                break;

        }
        return true;
    }

    private void saveUserInformation() {
        final String userId = getMyActivity().databaseReference.push().getKey();
        bindModel();
        rewardModel.setUniqKey(userId);
        getMyActivity().databaseReference.child(Constants.MAIN_TABLE).child(Constants.REWARD_TABLE).child(userId).setValue(rewardModel);
        Toast.makeText(getMyActivity(), "Information Saved...", Toast.LENGTH_LONG).show();
        getMyActivity().showFragment(getMyActivity().dashboardFragment, null);
    }


    private HomeActivity getMyActivity() {
        return (HomeActivity) getActivity();
    }
}
