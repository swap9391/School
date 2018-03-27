package com.exa.mydemoapp.fragment;

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
import com.exa.mydemoapp.Common.Connectivity;
import com.exa.mydemoapp.Common.Constants;
import com.exa.mydemoapp.Common.StudentInfoSingleton;
import com.exa.mydemoapp.HomeActivity;
import com.exa.mydemoapp.R;
import com.exa.mydemoapp.adapter.UserAdapter;
import com.exa.mydemoapp.annotation.ViewById;
import com.exa.mydemoapp.database.DbInvoker;
import com.exa.mydemoapp.model.StudentModel;
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
    private List<StudentModel> listStudent;
    private View view;
    ProgressDialog progressDialog;
    DbInvoker dbInvoker;

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
        getMyActivity().init();
        setHasOptionsMenu(true);
        listStudent = new ArrayList<>();
        if (getMyActivity().flagCallUserList) {
            getUserList();
        } else {
            initAdapter();
        }


        /*if (Connectivity.isConnected(getMyActivity())) {
            progressDialog = new ProgressDialog(getMyActivity());
            progressDialog.setTitle("Loading...");
            progressDialog.show();
            getImageData();
        } else {
            getMyActivity().showToast("Please Connect to internet !!");
        }*/

        getMyActivity().toolbar.setTitle("Users");
        return view;
    }

    private void initAdapter() {
        listStudent = getMyActivity().getDbInvoker().getUserList();
        if (listStudent != null && listStudent.size() > 0) {
            mAdapter = new UserAdapter(listStudent, getMyActivity());
            LinearLayoutManager mLayoutManager =
                    new LinearLayoutManager(getMyActivity());
            recyclerView.setLayoutManager(mLayoutManager);
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.setAdapter(mAdapter);
        }
    }


    public void getImageData() {
        StudentInfoSingleton studentInfoSingleton = StudentInfoSingleton.getInstance(getMyActivity());
        String userId = getMyActivity().databaseReference.push().getKey();
        DatabaseReference ref1 = getMyActivity().databaseReference.child(Constants.MAIN_TABLE);
        DatabaseReference ref2 = ref1.child(Constants.STUDENT);
        Query query = ref2.orderByChild("schoolName").equalTo(studentInfoSingleton.getStudentModel().getSchoolName());
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot Snapshot : dataSnapshot.getChildren()) {
                    StudentModel imageRequest = Snapshot.getValue(StudentModel.class);
                    if (imageRequest.getVisiblity().equalsIgnoreCase("TRUE")) {
                        listStudent.add(imageRequest);
                    }
                }

                mAdapter = new UserAdapter(listStudent, getMyActivity());
                LinearLayoutManager mLayoutManager =
                        new LinearLayoutManager(getMyActivity());
                recyclerView.setLayoutManager(mLayoutManager);
                recyclerView.setItemAnimator(new DefaultItemAnimator());
                recyclerView.setAdapter(mAdapter);
                progressDialog.dismiss();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("Exception", "onCancelled", databaseError.toException());
                progressDialog.dismiss();
            }
        });

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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_save:
                getMyActivity().showFragment(getMyActivity().signUpFragment, null);
                break;
        }
        return true;
    }

    public void getUserList() {
        HashMap<String, String> hashMap = new HashMap<>();
        // hashMap.put(IJson.password, "" + studentId);
        CallWebService.getWebservice(getMyActivity(), Request.Method.POST, IUrls.URL_USER_LIST, hashMap, new VolleyResponseListener<StudentModel>() {
            @Override
            public void onResponse(StudentModel[] object) {
                getMyActivity().getDbInvoker().deleteStudents();
                for (StudentModel studentModel : object) {
                    getMyActivity().getDbInvoker().insertUpdateUser(studentModel);
                }
                /*if (object[0] instanceof StudentModel) {
                 for (S)
                }*/
                initAdapter();

            }

            @Override
            public void onResponse(StudentModel object) {

            }

            @Override
            public void onError(String message) {
            }
        }, StudentModel[].class);
    }

}
