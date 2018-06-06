package com.exa.mydemoapp.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.exa.mydemoapp.Common.Constants;
import com.exa.mydemoapp.HomeActivity;
import com.exa.mydemoapp.R;
import com.exa.mydemoapp.annotation.ViewById;

public class ChooseAttendance extends CommonFragment implements View.OnClickListener {
    View view;
    @ViewById(R.id.btn_in)
    Button btnIn;
    @ViewById(R.id.btn_out)
    Button btnOut;
    @ViewById(R.id.btn_update)
    Button btnUpdate;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.layout_choose_attendance, container, false);
        getMyActivity().toolbar.setTitle(getString(R.string.dashboard_attendance));
        setHasOptionsMenu(true);
        initViewBinding(view);
        btnIn.setOnClickListener(this);
        btnOut.setOnClickListener(this);
        btnUpdate.setOnClickListener(this);
        return view;
    }


    private HomeActivity getMyActivity() {
        return (HomeActivity) getActivity();
    }

    @Override
    public void onClick(View view) {
        Bundle bundle = new Bundle();
        switch (view.getId()) {
            case R.id.btn_in:
                bundle.putString(Constants.ATTENDANCE_TYPE, Constants.ATTENDANCE_IN);
                getMyActivity().showFragment(new AttendanceFragment(), bundle);
                break;
            case R.id.btn_update:
                bundle.putString(Constants.ATTENDANCE_TYPE, Constants.ATTENDANCE_UPDATE);
                getMyActivity().showFragment(new AttendanceFragment(), bundle);
                break;
            case R.id.btn_out:
                bundle.putString(Constants.ATTENDANCE_TYPE, Constants.ATTENDANCE_OUT);
                getMyActivity().showFragment(new AttendanceFragment(), bundle);
                break;
        }
    }
}
