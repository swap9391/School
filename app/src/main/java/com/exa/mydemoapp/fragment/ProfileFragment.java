package com.exa.mydemoapp.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.exa.mydemoapp.HomeActivity;
import com.exa.mydemoapp.R;

/**
 * Created by midt-006 on 16/11/17.
 */

public class ProfileFragment extends Fragment {
    private View view;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.layout_profile, container, false);
        getMyActivity().init();
        getMyActivity().toolbar.setTitle("My Profile");

        return view;
    }

    public HomeActivity getMyActivity() {
        return (HomeActivity) getActivity();
    }
}
