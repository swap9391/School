package com.exa.mydemoapp.fragment;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.exa.mydemoapp.Common.CommonUtils;
import com.exa.mydemoapp.Common.Connectivity;
import com.exa.mydemoapp.Common.Constants;
import com.exa.mydemoapp.HomeActivity;
import com.exa.mydemoapp.R;
import com.exa.mydemoapp.model.AnnualCalenderMasterModel;
import com.exa.mydemoapp.model.DropdownMasterModel;
import com.exa.mydemoapp.webservice.CallWebService;
import com.exa.mydemoapp.webservice.IJson;
import com.exa.mydemoapp.webservice.IUrls;
import com.exa.mydemoapp.webservice.VolleyResponseListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Created by midt-006 on 11/10/17.
 */

public class AnnualEventFragment extends Fragment implements View.OnClickListener {
    private Button datePicker;
    private TextView txtSelectedDate;
    private Spinner spinnerType;
    private Spinner spinnerClass;
    private EditText edtEventName;
    private AnnualCalenderMasterModel annualCalenderMasterModel;
    List<AnnualCalenderMasterModel> listEvent = new ArrayList<>();
    boolean flag = false;

    View view;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.layout_annual_event, container, false);
        getMyActivity().toolbar.setTitle("Add Event");
        getMyActivity().init();

        annualCalenderMasterModel = new AnnualCalenderMasterModel();
        datePicker = (Button) view.findViewById(R.id.date_picker_event);
        txtSelectedDate = (TextView) view.findViewById(R.id.txt_selected_date);
        spinnerType = (Spinner) view.findViewById(R.id.spinner_event_type);
        spinnerClass = (Spinner) view.findViewById(R.id.spinner_class);
        edtEventName = (EditText) view.findViewById(R.id.edt_event_name);
        datePicker.setOnClickListener(this);

        List<DropdownMasterModel> listEventType = getMyActivity().getDbInvoker().getDropDownByType("EVENTTYPE");
        ArrayAdapter<DropdownMasterModel> eventAdapter = new ArrayAdapter<>(getMyActivity(), android.R.layout.simple_spinner_item, listEventType);
        eventAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerType.setAdapter(eventAdapter);
        eventAdapter.notifyDataSetChanged();

        List<DropdownMasterModel> listClass = getMyActivity().getDbInvoker().getDropDownByType("CLASSTYPE");
        ArrayAdapter<DropdownMasterModel> classAdapter = new ArrayAdapter<DropdownMasterModel>(getMyActivity(), android.R.layout.simple_spinner_item, listClass);
        classAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerClass.setAdapter(classAdapter);
        classAdapter.notifyDataSetChanged();

        getCalenderEvents();
        return view;
    }


    private void bindModel() {
        annualCalenderMasterModel.setEventType(spinnerType.getSelectedItem().toString());
        annualCalenderMasterModel.setClassName(spinnerClass.getSelectedItem().toString());
        annualCalenderMasterModel.setEventName(edtEventName.getText().toString());
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.date_picker_event:
                showDatePicker();
                break;
        }
    }

   /* private void saveUserInformation() {
        String userId = getMyActivity().databaseReference.push().getKey();
        annualCalenderMasterModel.setUniqKey(userId);
        getMyActivity().databaseReference.child(Constants.MAIN_TABLE).child(Constants.EVENT_TABLE).child(userId).setValue(annualCalenderMasterModel);
        Toast.makeText(getMyActivity(), "Information Saved...", Toast.LENGTH_LONG).show();
    }*/

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // TODO Add your menu entries here
        inflater.inflate(R.menu.menu_save_info, menu);
        menu.findItem(R.id.action_gallery).setVisible(false);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_save:
                bindModel();
                if (check()) {
                    if (Connectivity.isConnected(getMyActivity())) {
                        //saveUserInformation();
                        save();
                    } else {
                        getMyActivity().showToast("Please Connect to internet !!");
                    }
                }
                break;

        }
        return true;
    }

    private boolean check() {
        if (annualCalenderMasterModel.getEventName() == null || annualCalenderMasterModel.getEventName().trim().length() <= 0) {
            CommonUtils.showToast(getMyActivity(), "Please Enter Event Name");
            return false;
        }
        if (annualCalenderMasterModel.getEventDate() <= 0) {
            CommonUtils.showToast(getMyActivity(), "Please Select Event Date");
            return false;
        }
        if (annualCalenderMasterModel.getEventType() == null || annualCalenderMasterModel.getEventType().trim().length() <= 0) {
            CommonUtils.showToast(getMyActivity(), "Please Select Event Type");
            return false;
        }
        if (annualCalenderMasterModel.getClassName() == null || annualCalenderMasterModel.getClassName().trim().length() <= 0) {
            CommonUtils.showToast(getMyActivity(), "Please Select Event Class");
            return false;
        }


        return true;
    }

    private void getCalenderEvents() {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put(IJson.classId, "All");
        hashMap.put(IJson.divisionId, "");
        CallWebService.getWebservice(getMyActivity(), Request.Method.POST, IUrls.URL_GET_EVENTS, hashMap, new VolleyResponseListener<AnnualCalenderMasterModel>() {
            @Override
            public void onResponse(AnnualCalenderMasterModel[] object) {
                for (AnnualCalenderMasterModel rewardModel : object) {
                    listEvent.add(rewardModel);
                }
            }

            @Override
            public void onResponse(AnnualCalenderMasterModel object) {
            }

            @Override
            public void onResponse() {
            }

            @Override
            public void onError(String message) {
            }
        }, AnnualCalenderMasterModel[].class);

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
            String formatedDate = CommonUtils.formatDateForDisplay(date, Constants.DATE_FORMAT);
            //annualCalenderMasterModel.setEventDate(formatedDate);
            txtSelectedDate.setText(CommonUtils.formatDateForDisplay(date, "dd MMM yyyy"));
        }
    };


    public HomeActivity getMyActivity() {
        return (HomeActivity) getActivity();
    }

    private void save() {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put(IJson.eventName, "" + annualCalenderMasterModel.getEventName());
        hashMap.put(IJson.eventDate, "" + annualCalenderMasterModel.getEventDate());
        hashMap.put(IJson.eventType, "" + annualCalenderMasterModel.getEventType());
        hashMap.put(IJson.classId, "" + annualCalenderMasterModel.getClassName());
        //hashMap.put(IJson.divisionId, "" + annualCalenderMasterModel.getdi());

        CallWebService.getWebserviceObject(getMyActivity(), Request.Method.POST, IUrls.URL_ADD_EVENTS, hashMap, new VolleyResponseListener<AnnualCalenderMasterModel>() {
            @Override
            public void onResponse(AnnualCalenderMasterModel[] object) {
            }

            @Override
            public void onResponse(AnnualCalenderMasterModel studentData) {
                getMyActivity().showFragment(new DashboardFragment(), null);
            }

            @Override
            public void onResponse() {
            }

            @Override
            public void onError(String message) {
                Toast.makeText(getMyActivity(), message, Toast.LENGTH_SHORT).show();
            }
        }, AnnualCalenderMasterModel.class);

    }


}
