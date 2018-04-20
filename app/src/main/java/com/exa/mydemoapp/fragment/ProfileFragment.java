package com.exa.mydemoapp.fragment;

import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.exa.mydemoapp.Common.AppController;
import com.exa.mydemoapp.Common.CommonUtils;
import com.exa.mydemoapp.Common.Constants;
import com.exa.mydemoapp.HomeActivity;
import com.exa.mydemoapp.R;
import com.exa.mydemoapp.annotation.ViewById;
import com.exa.mydemoapp.model.StudentRewardsModel;
import com.exa.mydemoapp.model.UserModel;
import com.exa.mydemoapp.webservice.CallWebService;
import com.exa.mydemoapp.webservice.IJson;
import com.exa.mydemoapp.webservice.IUrls;
import com.exa.mydemoapp.webservice.VolleyResponseListener;

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
    @ViewById(R.id.circularImageView1)
    CircleImageView circleImageView;
    @ViewById(R.id.txt_class_name)
    TextView txtClassName;
    @ViewById(R.id.txt_division)
    TextView txtDivision;
    @ViewById(R.id.txt_blood_group)
    TextView txtBloodGroup;
    @ViewById(R.id.view_reward)
    View viewReward;
    @ViewById(R.id.lay_reward)
    LinearLayout layReward;
    @ViewById(R.id.layCard)
    CardView cardView;
    List<StudentRewardsModel> rewardModelList;

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
        getMyActivity().toolbar.setTitle(getString(R.string.title_profile));
        setData();
        rewardModelList = new ArrayList<>();

        layLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getMyActivity().logOut();
            }
        });
        if (getMyActivity().getUserModel().getUserType().equals(Constants.USER_TYPE_STUDENT)) {
            getRewards();

            layReward.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (rewardModelList.size() > 0) {
                        Bundle bundle = new Bundle();
                        bundle.putSerializable(Constants.LIST_TYPE, (Serializable) rewardModelList);
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
        UserModel userModel = getMyActivity().getUserModel();
        String userType = CommonUtils.getSharedPref(Constants.USER_TYPE, getMyActivity());
        if (userType.equals(getStringById(R.string.user_type_driver))) {
            cardView.setVisibility(View.GONE);
        }
        if (userType.equals(getStringById(R.string.user_type_admin))) {
            cardView.setVisibility(View.GONE);
        }
        if (userModel != null) {
            String name = userModel.getFirstName() + " " + userModel.getLastName();
            String address = userModel.getUserInfoModel().getAddress();
            // String bloodGrp = "Blood Group " + userModel.getUserInfoModel().getBloodGroup();

          /*  if (userModel.getUserInfoModel().getGender().equalsIgnoreCase("Boy")) {
                circleImageView.setImageDrawable(getMyActivity().getResources().getDrawable(R.drawable.icon_boy));
            } else {
                circleImageView.setImageDrawable(getMyActivity().getResources().getDrawable(R.drawable.icon_girl));
            }*/
            circleImageView.setImageDrawable(getMyActivity().getResources().getDrawable(R.drawable.icon_boy));
            txtName.setText(name != null ? name : "");
            txtAddress.setText(address != null ? address : "");
            String className = getString(R.string.lbl_email) + userModel.getEmail();
            String division = getString(R.string.lbl_contact_number)+ userModel.getContactNumber();
            txtClassName.setText(className != null ? className : "");
            txtDivision.setText(division != null ? division : "");
            // txtBloodGroup.setText(bloodGrp != null ? bloodGrp : "");
        }
    }

    private void getRewards() {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put(IJson.studentId, "" + CommonUtils.getSharedPref(Constants.STUDENT_ID, getMyActivity()));
        CallWebService.getWebservice(getMyActivity(), Request.Method.POST, IUrls.URL_GET_REWARDS, hashMap, new VolleyResponseListener<StudentRewardsModel>() {
            @Override
            public void onResponse(StudentRewardsModel[] object) {

                for (StudentRewardsModel rewardModel : object) {
                    rewardModelList.add(rewardModel);
                }
                /*if (object[0] instanceof UserModel) {
                 for (S)
                }*/

            }

            @Override
            public void onResponse() {
            }

            @Override
            public void onResponse(StudentRewardsModel object) {

            }

            @Override
            public void onError(String message) {
            }
        }, StudentRewardsModel[].class);

    }

    public HomeActivity getMyActivity() {
        return (HomeActivity) getActivity();
    }
}
