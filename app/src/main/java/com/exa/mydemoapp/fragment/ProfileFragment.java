package com.exa.mydemoapp.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.exa.mydemoapp.Common.AppController;
import com.exa.mydemoapp.Common.CommonUtils;
import com.exa.mydemoapp.Common.Constants;
import com.exa.mydemoapp.Common.StudentInfoSingleton;
import com.exa.mydemoapp.HomeActivity;
import com.exa.mydemoapp.SignUpFragment;
import com.exa.mydemoapp.R;
import com.exa.mydemoapp.annotation.ViewById;

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
    StudentInfoSingleton studentInfoSingleton;

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
        String duesPayable = "Total Dues :" + dues;

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

    public HomeActivity getMyActivity() {
        return (HomeActivity) getActivity();
    }
}
