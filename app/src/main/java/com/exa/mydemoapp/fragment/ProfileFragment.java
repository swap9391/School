package com.exa.mydemoapp.fragment;

import android.animation.ObjectAnimator;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.exa.mydemoapp.Common.AppController;
import com.exa.mydemoapp.Common.CommonUtils;
import com.exa.mydemoapp.Common.Connectivity;
import com.exa.mydemoapp.Common.Constants;
import com.exa.mydemoapp.Common.StudentInfoSingleton;
import com.exa.mydemoapp.HomeActivity;
import com.exa.mydemoapp.R;
import com.exa.mydemoapp.adapter.UserAdapter;
import com.exa.mydemoapp.annotation.ViewById;
import com.exa.mydemoapp.model.ImageRequest;
import com.exa.mydemoapp.model.RewardModel;
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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by midt-006 on 16/11/17.
 */

public class ProfileFragment extends CommonFragment {
    private View view;
    @ViewById(R.id.txt_student_name)
    TextView txtName;
    @ViewById(R.id.txt_student_address)
    TextView txtAddress;
    @ViewById(R.id.lay_logout)
    LinearLayout layLogout;
    @ViewById(R.id.lay_manage_user)
    LinearLayout layManageUser;
    @ViewById(R.id.circularImageView1)
    CircleImageView circleImageView;
    @ViewById(R.id.txt_class_name)
    TextView txtClassName;
    @ViewById(R.id.txt_division)
    TextView txtDivision;
    @ViewById(R.id.txt_blood_group)
    TextView txtBloodGroup;
    @ViewById(R.id.txt_total_fees)
    TextView txtTotalFees;
    @ViewById(R.id.txt_paid_fees)
    TextView txtPaidFees;
    @ViewById(R.id.txt_total_dues)
    TextView txtTotalDues;
    @ViewById(R.id.txt_rewards)
    TextView txtRewards;
    StudentInfoSingleton studentInfoSingleton;
    @ViewById(R.id.view_reward)
    View viewReward;
    @ViewById(R.id.lay_reward)
    LinearLayout layReward;
    List<RewardModel> rewardModelList;
    ProgressDialog progressDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.layout_profile, container, false);
        initViewBinding(view);
        getMyActivity().init();
        getMyActivity().toolbar.setTitle("My Profile");
        setData();
        if (!AppController.isAdmin(getMyActivity())) {
            layManageUser.setVisibility(View.GONE);
        }
        layManageUser.setOnClickListener(new onManageUserClick());
        layLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getMyActivity().logoOut();
            }
        });
        if (!AppController.isAdmin(getMyActivity())) {
            getRewards();

            layReward.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (rewardModelList.size() > 0) {
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("mylist", (Serializable) rewardModelList);
                        getMyActivity().showFragment(getMyActivity().rewardGraphFragment, bundle);
                    }

                }
            });
        } else {
            viewReward.setVisibility(View.GONE);
            layReward.setVisibility(View.GONE);
        }


        return view;
    }

    private void setData() {
        studentInfoSingleton = StudentInfoSingleton.getInstance(getMyActivity());
        String name = studentInfoSingleton.getStudentModel().getStudentName();
        String address = studentInfoSingleton.getStudentModel().getStudentAddress();
        String className = "Class " + studentInfoSingleton.getStudentModel().getClassName();
        String division = "Division " + studentInfoSingleton.getStudentModel().getDivision();
        String bloodGrp = "Blood Group " + studentInfoSingleton.getStudentModel().getStudentBloodGrp();
        String totalFees = "Total Fees :" + studentInfoSingleton.getStudentModel().getTotalFees();
        int paid = CommonUtils.asInt(studentInfoSingleton.getStudentModel().getInstallment1(), 0)
                + CommonUtils.asInt(studentInfoSingleton.getStudentModel().getInstallment2(), 0)
                + CommonUtils.asInt(studentInfoSingleton.getStudentModel().getInstallment3(), 0);
        String paidFees = "Paid :" + paid;
        int dues = studentInfoSingleton.getStudentModel().getTotalFees() - paid;

        String dueDate = "";
        if (studentInfoSingleton.getStudentModel().getDateInsvestment2() != null) {
            dueDate = "\nNext payment date: " + CommonUtils.formatDateForDisplay(CommonUtils.toDate(studentInfoSingleton.getStudentModel().getDateInsvestment2(), "dd-MM-yyyy hh:mm"), "dd/MM/yyy");
        } else if (studentInfoSingleton.getStudentModel().getDateInsvestment3() != null) {
            dueDate = "\nNext payment date: " + CommonUtils.formatDateForDisplay(CommonUtils.toDate(studentInfoSingleton.getStudentModel().getDateInsvestment3(), "dd-MM-yyyy hh:mm"), "dd/MM/yyy");
        }

        String duesPayable = "Total Dues :" + dues + " " + dueDate;

        if (studentInfoSingleton.getStudentModel().getGender().equalsIgnoreCase("Boy")) {
            circleImageView.setImageDrawable(getMyActivity().getResources().getDrawable(R.drawable.icon_boy));
        } else {
            circleImageView.setImageDrawable(getMyActivity().getResources().getDrawable(R.drawable.icon_girl));
        }
        txtName.setText(name != null ? name : "");
        txtAddress.setText(address != null ? address : "");
        txtClassName.setText(className != null ? className : "");
        txtDivision.setText(division != null ? division : "");
        txtBloodGroup.setText(bloodGrp != null ? bloodGrp : "");
        txtTotalFees.setText(totalFees != null ? totalFees : "");
        txtPaidFees.setText(paid > 0 ? paidFees : "");
        txtTotalDues.setText(dues > 0 ? duesPayable : "");
    }

    private class onManageUserClick implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            getMyActivity().showFragment(getMyActivity().usersListFragment, null);
        }
    }

    private void getRewards() {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put(IJson.studentId, "" + CommonUtils.getSharedPref(Constants.STUDENT_ID, getMyActivity()));
        CallWebService.getWebservice(getMyActivity(), Request.Method.POST, IUrls.URL_USER_LIST, hashMap, new VolleyResponseListener<RewardModel>() {
            @Override
            public void onResponse(RewardModel[] object) {

                for (RewardModel rewardModel : object) {
                    rewardModelList.add(rewardModel);
                }
                /*if (object[0] instanceof StudentModel) {
                 for (S)
                }*/

            }

            @Override
            public void onResponse(RewardModel object) {

            }

            @Override
            public void onError(String message) {
            }
        }, RewardModel[].class);

    }

    public HomeActivity getMyActivity() {
        return (HomeActivity) getActivity();
    }
}
