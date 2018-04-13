package com.exa.mydemoapp.fragment;

import android.content.res.TypedArray;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;

import com.exa.mydemoapp.Common.AppController;
import com.exa.mydemoapp.Common.CommonUtils;
import com.exa.mydemoapp.Common.Constants;
import com.exa.mydemoapp.HomeActivity;
import com.exa.mydemoapp.R;
import com.exa.mydemoapp.adapter.HomeGridAdapter;
import com.exa.mydemoapp.annotation.ViewById;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by midt-006 on 4/10/17.
 */

public class DashboardFragment extends CommonFragment {
    View view;
    @ViewById(R.id.grid_view)
    GridView gridview;
    @ViewById(R.id.image)
    ImageView imageView;

    public String[] nameForAdmin;
    public TypedArray imagesForAdmin;

    public String[] nameForStudent;

    public TypedArray imagesForStudent;

    public String[] nameForGuest;
    public TypedArray imagesForGuest;

    public String[] nameForDriver;
    public TypedArray imagesForDriver;


    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.layout_dashboard, container, false);
        getMyActivity().toolbar.setTitle("Home");

        nameForAdmin = getResources().getStringArray(R.array.nameForAdmin);
        imagesForAdmin = getResources().obtainTypedArray(R.array.imagesForAdmin);

        nameForStudent = getMyActivity().getResources().getStringArray(R.array.nameForSudent);
        imagesForStudent = getResources().obtainTypedArray(R.array.imagesForStudent);

        nameForDriver = getMyActivity().getResources().getStringArray(R.array.nameForDriver);
        imagesForDriver = getResources().obtainTypedArray(R.array.imagesForDriver);


        nameForGuest = getMyActivity().getResources().getStringArray(R.array.nameForGuest);
        imagesForGuest = getResources().obtainTypedArray(R.array.imagesForGuest);


        initViewBinding(view);
        if (getMyActivity().isGuest) {
            gridview.setAdapter(new HomeGridAdapter(getMyActivity(), nameForGuest, imagesForGuest));
        } else {
            String userType = CommonUtils.getSharedPref(Constants.USER_TYPE, getMyActivity());
            if (userType.equals(Constants.USER_TYPE_ADMIN)) {
                gridview.setAdapter(new HomeGridAdapter(getMyActivity(), nameForAdmin, imagesForAdmin));
            } else if (userType.equals(Constants.USER_TYPE_STUDENT)) {
                gridview.setAdapter(new HomeGridAdapter(getMyActivity(), nameForStudent, imagesForStudent));
            } else if (userType.equals(Constants.USER_TYPE_DRIVER)) {
                gridview.setAdapter(new HomeGridAdapter(getMyActivity(), nameForDriver, imagesForDriver));
            } else {
                gridview.setAdapter(new HomeGridAdapter(getMyActivity(), nameForGuest, imagesForGuest));
            }
        }
        return view;
    }


    public static String[] removeElements(String[] input, String deleteMe) {
        List result = new LinkedList();

        for (String item : input)
            if (!deleteMe.equals(item))
                result.add(item);

        return (String[]) result.toArray(input);
    }

    public HomeActivity getMyActivity() {
        return (HomeActivity) getActivity();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_logout, menu);
        if (getMyActivity().isGuest) {
            menu.findItem(R.id.action_logout).setVisible(false);
        }
        //menu.findItem(R.id.action_gallery).setVisible(false);
        super.onCreateOptionsMenu(menu, inflater);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_logout:
                getMyActivity().logoOut();
                break;
        }
        return true;

    }
}
