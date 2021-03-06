package com.exa.mydemoapp.fragment;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Request;
import com.exa.mydemoapp.Common.Constants;
import com.exa.mydemoapp.Common.StudentInfoSingleton;
import com.exa.mydemoapp.HomeActivity;
import com.exa.mydemoapp.R;
import com.exa.mydemoapp.adapter.UserAdapter;
import com.exa.mydemoapp.annotation.ViewById;
import com.exa.mydemoapp.database.DbInvoker;
import com.exa.mydemoapp.model.UserModel;
import com.exa.mydemoapp.webservice.CallWebService;
import com.exa.mydemoapp.webservice.IUrls;
import com.exa.mydemoapp.webservice.VolleyResponseListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * Created by midt-006 on 6/9/17.
 */

public class UsersListFragment extends CommonFragment {

    public UserAdapter mAdapter;
    @ViewById(R.id.recyclerView)
    private RecyclerView recyclerView;
    private View view;
    HashMap<String, ArrayList<UserModel>> hashMap;
    private List<UserModel> adminList = new ArrayList<>();
    private List<UserModel> studentList = new ArrayList<>();
    private List<UserModel> teacherList = new ArrayList<>();
    private List<UserModel> driverList = new ArrayList<>();


    public UsersListFragment() {
    }

    @SuppressLint("ValidFragment")
    public UsersListFragment(HashMap<String, ArrayList<UserModel>> hashMap) {
        this.hashMap = hashMap;

        adminList = hashMap.get(Constants.USER_TYPE_ADMIN);
        studentList = hashMap.get(Constants.USER_TYPE_STUDENT);
        teacherList = hashMap.get(Constants.USER_TYPE_TEACHER);
        driverList = hashMap.get(Constants.USER_TYPE_DRIVER);
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


        /*if (Connectivity.isConnected(getMyActivity())) {
            progressDialog = new ProgressDialog(getMyActivity());
            progressDialog.setTitle("Loading...");
            progressDialog.show();
            getImageData();
        } else {
            getMyActivity().showToast("Please Connect to internet !!");
        }*/

        return view;
    }

    private void initAdapter() {

        mAdapter = new UserAdapter(getMyActivity().getListUsers(), getMyActivity());
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
        //menu.findItem(R.id.action_gallery).setVisible(false);
        super.onCreateOptionsMenu(menu, inflater);

    }


}
