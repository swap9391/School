package com.exa.mydemoapp.fragment;

import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Request;
import com.exa.mydemoapp.Common.Connectivity;
import com.exa.mydemoapp.Common.Constants;
import com.exa.mydemoapp.HomeActivity;
import com.exa.mydemoapp.R;
import com.exa.mydemoapp.adapter.GalleryAdapter;
import com.exa.mydemoapp.annotation.ViewById;
import com.exa.mydemoapp.model.AlbumMasterModel;
import com.exa.mydemoapp.model.UserModel;
import com.exa.mydemoapp.webservice.CallWebService;
import com.exa.mydemoapp.webservice.IJson;
import com.exa.mydemoapp.webservice.IUrls;
import com.exa.mydemoapp.webservice.VolleyResponseListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by midt-006 on 21/8/17.
 */

public class StaffInfoFragment extends CommonFragment {
    private List<UserModel> teacherList = new ArrayList<>();
    @ViewById(R.id.recycler_view)
    private RecyclerView recyclerView;
    View view;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.layout_gallery, container, false);
        getMyActivity().toolbar.setTitle("Staff Information");
        setHasOptionsMenu(true);
        getMyActivity().init();
        initViewBinding(view);
        getTeacherList();

        return view;
    }

    public void getTeacherList() {
        HashMap<String, Object> hashMap = new HashMap<>();
        // hashMap.put(IJson.password, "" + studentId);
        CallWebService.getWebservice(getMyActivity(), Request.Method.GET, IUrls.URL_USER_LIST, hashMap, new VolleyResponseListener<UserModel>() {
            @Override
            public void onResponse(UserModel[] object) {
                for (UserModel userModel : object) {
                    if (userModel.getUserType().equals(Constants.USER_TYPE_TEACHER)) {
                        teacherList.add(userModel);
                    }
                }
                if (teacherList.size() > 0) {
                    getMyActivity().showFragment(new StudentListFragment(teacherList), null);
                }
            }

            @Override
            public void onResponse() {
            }

            @Override
            public void onResponse(UserModel object) {

            }

            @Override
            public void onError(String message) {
                getMyActivity().showToast(message);
            }
        }, UserModel[].class);
    }

    public HomeActivity getMyActivity() {
        return (HomeActivity) getActivity();
    }
}