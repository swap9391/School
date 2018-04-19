package com.exa.mydemoapp.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import com.exa.mydemoapp.Common.Constants;
import com.exa.mydemoapp.HomeActivity;
import com.exa.mydemoapp.R;
import com.exa.mydemoapp.adapter.UserAdapter;
import com.exa.mydemoapp.annotation.ViewById;
import com.exa.mydemoapp.model.UserModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * Created by midt-006 on 6/9/17.
 */

public class StudentListFragment extends CommonFragment {

    public UserAdapter mAdapter;
    @ViewById(R.id.recyclerView)
    private RecyclerView recyclerView;
    private View view;
    private List<UserModel> studentList = new ArrayList<>();

    public StudentListFragment() {
    }

    @SuppressLint("ValidFragment")
    public StudentListFragment(List<UserModel> studentList) {
        this.studentList = studentList;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.layout_student_list, container, false);
        initViewBinding(view);
        Bundle bundle = getArguments();
        setHasOptionsMenu(true);

        initAdapter();


        return view;
    }

    private void initAdapter() {

        mAdapter = new UserAdapter(studentList, getMyActivity());
        LinearLayoutManager mLayoutManager =
                new LinearLayoutManager(getMyActivity());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);


    }

    public HomeActivity getMyActivity() {
        return (HomeActivity) getActivity();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_save_info, menu);
        menu.findItem(R.id.action_gallery).setVisible(false);
        menu.findItem(R.id.action_save).setIcon(R.drawable.ic_plus);
        super.onCreateOptionsMenu(menu, inflater);

    }


}
