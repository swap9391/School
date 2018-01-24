package com.exa.mydemoapp.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.exa.mydemoapp.Common.CommonUtils;
import com.exa.mydemoapp.Common.Constants;
import com.exa.mydemoapp.HomeActivity;
import com.exa.mydemoapp.R;
import com.exa.mydemoapp.annotation.ViewById;

/**
 * Created by midt-006 on 16/11/17.
 */

public class ProfileFragment extends CommonFragment {
    private View view;
    @ViewById(R.id.txt_student_name)
    TextView txtName;
    @ViewById(R.id.txt_student_address)
    TextView txtAddress;
    @ViewById(R.id.txt_class_name)
    TextView txtClassName;


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
        return view;
    }

    private void setData() {
        String name = CommonUtils.getSharedPref(Constants.STUDENT_NAME, getMyActivity());
        String address = CommonUtils.getSharedPref(Constants.STUDENT_ADDRESS, getMyActivity());
        String className = CommonUtils.getSharedPref(Constants.CLASS_NAME, getMyActivity());

        txtName.setText(name != null ? name : "");
        txtAddress.setText(address != null ? address : "");
        txtClassName.setText(className != null ? className : "");
    }

    public HomeActivity getMyActivity() {
        return (HomeActivity) getActivity();
    }
}
