package com.exa.mydemoapp.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Request;
import com.exa.mydemoapp.Common.Constants;
import com.exa.mydemoapp.HomeActivity;
import com.exa.mydemoapp.R;
import com.exa.mydemoapp.adapter.EventAdapter;
import com.exa.mydemoapp.adapter.UserAdapter;
import com.exa.mydemoapp.annotation.ViewById;
import com.exa.mydemoapp.model.AnnualCalenderMasterModel;
import com.exa.mydemoapp.model.UserModel;
import com.exa.mydemoapp.webservice.CallWebService;
import com.exa.mydemoapp.webservice.IUrls;
import com.exa.mydemoapp.webservice.VolleyResponseListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;


/**
 * Created by midt-006 on 6/9/17.
 */

public class EventListFragment extends CommonFragment {

    public EventAdapter mAdapter;
    @ViewById(R.id.recyclerView)
    private RecyclerView recyclerView;
    private View view;
    List<AnnualCalenderMasterModel> listEvent;

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
        listEvent = new ArrayList<>();
        getList();
        return view;
    }

    private void initAdapter() {

        mAdapter = new EventAdapter(listEvent, getMyActivity());
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
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_save:
                getMyActivity().showFragment(new AnnualEventFragment(), null);
                break;
        }
        return true;
    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_save_info, menu);
        menu.findItem(R.id.action_gallery).setVisible(false);
        menu.findItem(R.id.action_save).setIcon(R.drawable.ic_plus);
        super.onCreateOptionsMenu(menu, inflater);

    }

    public void getList() {
        HashMap<String, Object> hashMap = new HashMap<>();

        Date startDate = new Date();
        Date endDate = new Date();
        Calendar calendar = new GregorianCalendar();
        int year = calendar.get(Calendar.YEAR);
        calendar.set(Calendar.MONTH, 06);
        calendar.set(Calendar.DATE, 01);
        calendar.set(Calendar.YEAR, year);
        startDate = calendar.getTime();

        Calendar calendar1 = new GregorianCalendar();
        calendar1.set(Calendar.MONTH, 05);
        calendar1.set(Calendar.DATE, 31);
        calendar1.set(Calendar.YEAR, year + 1);
        endDate = calendar1.getTime();


        String url = String.format(IUrls.URL_GET_EVENTS, startDate.getTime(), endDate.getTime());


        CallWebService.getWebservice(getMyActivity(), Request.Method.GET, url, hashMap, new VolleyResponseListener<AnnualCalenderMasterModel>() {
            @Override
            public void onResponse(AnnualCalenderMasterModel[] object) {
                listEvent.addAll(Arrays.asList(object));
                if (listEvent.size() > 0) {
                    initAdapter();
                }
            }

            @Override
            public void onResponse() {
            }

            @Override
            public void onResponse(AnnualCalenderMasterModel object) {
            }

            @Override
            public void onError(String message) {
            }
        }, AnnualCalenderMasterModel[].class);
    }


}
