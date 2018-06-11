package com.exa.mydemoapp.fragment;

import android.app.DatePickerDialog;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.Request;
import com.exa.mydemoapp.Common.CommonUtils;
import com.exa.mydemoapp.Common.Constants;
import com.exa.mydemoapp.HomeActivity;
import com.exa.mydemoapp.R;
import com.exa.mydemoapp.adapter.HomeWorkAdapter;
import com.exa.mydemoapp.adapter.StudentAttendaceAdapter;
import com.exa.mydemoapp.annotation.ViewById;
import com.exa.mydemoapp.model.DailyHomeworkModel;
import com.exa.mydemoapp.model.DropdownMasterModel;
import com.exa.mydemoapp.model.StudentAttendanceDetailsModel;
import com.exa.mydemoapp.model.StudentModel;
import com.exa.mydemoapp.model.UserModel;
import com.exa.mydemoapp.webservice.CallWebService;
import com.exa.mydemoapp.webservice.IUrls;
import com.exa.mydemoapp.webservice.VolleyResponseListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class HomeWorkListFragment extends CommonFragment {
    View view;
    @ViewById(R.id.recyclerView)
    private RecyclerView recyclerView;
    @ViewById(R.id.date_picker_event)
    private Button datePicker;
    private List<DailyHomeworkModel> listDailyHomeworkModels;
    HomeWorkAdapter homeWorkAdapter;
    DailyHomeworkModel dailyHomeworkModel;
    boolean apiFlag = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.layout_homework_list, container, false);
        getMyActivity().toolbar.setTitle(getString(R.string.dashboard_upload));
        getMyActivity().init();
        initViewBinding(view);

        dailyHomeworkModel= new DailyHomeworkModel();

        datePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePicker();
            }
        });
        datePicker.setText(CommonUtils.formatDateForDisplay(Calendar.getInstance().getTime(), "dd MMM yyyy"));
        // attendanceMasterModel.setDateStamp(CommonUtils.formatDateForDisplay(Calendar.getInstance().getTime(), Constants.ONLY_DATE_FORMAT));
        long timestamp = System.currentTimeMillis() / 1000;
        dailyHomeworkModel.setHomeworkDate(timestamp);

        getHomeWork();

        return view;
    }

    private void showDatePicker() {
        DatePickerFragment date = new DatePickerFragment();
        /**
         * Set Up Current Date Into dialog
         */
        Calendar calender = Calendar.getInstance();
        Bundle args = new Bundle();
        args.putInt("year", calender.get(Calendar.YEAR));
        args.putInt("month", calender.get(Calendar.MONTH));
        args.putInt("day", calender.get(Calendar.DAY_OF_MONTH));
        date.setArguments(args);
        /**
         * Set Call back to capture selected date
         */
        date.setCallBack(ondate);
        date.show(getFragmentManager(), "Date Picker");
    }

    DatePickerDialog.OnDateSetListener ondate = new DatePickerDialog.OnDateSetListener() {

        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            monthOfYear += 1;
            String month = "" + monthOfYear;
            String day = "" + dayOfMonth;
            if (monthOfYear < 10) {
                month = "0" + monthOfYear;
            }
            if (dayOfMonth < 10) {
                day = "0" + dayOfMonth;
            }
            Date date = CommonUtils.toDate(year + "" + month + "" + day, "yyyyMMdd");
            String formatedDate = CommonUtils.formatDateForDisplay(date, Constants.ONLY_DATE_FORMAT);
            long timestamp = date.getTime();
            dailyHomeworkModel.setHomeworkDate(timestamp);
            datePicker.setText(CommonUtils.formatDateForDisplay(date, "dd MMM yyyy"));
            getHomeWork();
        }
    };

    public void getHomeWork() {
        if (!apiFlag) {
            apiFlag = true;
            HashMap<String, Object> hashMap = new HashMap<>();
            String url = String.format(IUrls.URL_GET_HOMEWORK, dailyHomeworkModel.getHomeworkDate());
            Log.d("url", url);
            CallWebService.getWebservice(getMyActivity(), Request.Method.GET, url, hashMap, new VolleyResponseListener<DailyHomeworkModel>() {
                @Override
                public void onResponse(DailyHomeworkModel[] object) {
                    recyclerView.setVisibility(View.VISIBLE);
                    listDailyHomeworkModels = new ArrayList<>();
                    listDailyHomeworkModels.addAll(Arrays.asList(object));
                    apiFlag = false;
                    initAdapter(listDailyHomeworkModels);
                }

                @Override
                public void onResponse() {
                }

                @Override
                public void onResponse(DailyHomeworkModel object) {

                }

                @Override
                public void onError(String message) {
                    recyclerView.setVisibility(View.INVISIBLE);
                    if (message != null && !message.isEmpty()) {
                        getMyActivity().showToast(message);
                    }

                    apiFlag = false;
                }
            }, DailyHomeworkModel[].class);

        }
    }

    private void initAdapter(List<DailyHomeworkModel> listStudent) {
        homeWorkAdapter = new HomeWorkAdapter(listDailyHomeworkModels, getMyActivity());
        LinearLayoutManager mLayoutManager =
                new LinearLayoutManager(getMyActivity());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(homeWorkAdapter);
        homeWorkAdapter.notifyDataSetChanged();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_save:
                getMyActivity().showFragment(new HomeWorkFragment(), null);
                break;
        }
        return true;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_save_info, menu);
        menu.findItem(R.id.action_gallery).setVisible(false);
        menu.findItem(R.id.action_save).setIcon(R.drawable.ic_plus);
        if (getMyActivity().getUserModel().getUserType().equals(Constants.USER_TYPE_STUDENT)) {
            menu.findItem(R.id.action_save).setVisible(false);
        }
        super.onCreateOptionsMenu(menu, inflater);

    }

    public HomeActivity getMyActivity() {
        return (HomeActivity) getActivity();
    }
}
