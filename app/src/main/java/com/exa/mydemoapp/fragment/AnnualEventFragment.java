package com.exa.mydemoapp.fragment;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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
import com.exa.mydemoapp.annotation.ViewById;
import com.exa.mydemoapp.model.AnnualCalenderMasterModel;
import com.exa.mydemoapp.model.DropdownMasterModel;
import com.exa.mydemoapp.model.UserModel;
import com.exa.mydemoapp.webservice.CallWebService;
import com.exa.mydemoapp.webservice.IJson;
import com.exa.mydemoapp.webservice.IUrls;
import com.exa.mydemoapp.webservice.VolleyResponseListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Created by midt-006 on 11/10/17.
 */

public class AnnualEventFragment extends CommonFragment implements View.OnClickListener {
    @ViewById(R.id.date_picker_event)
    private Button datePicker;
    @ViewById(R.id.txt_selected_date)
    private TextView txtSelectedDate;
    @ViewById(R.id.spinner_event_type)
    private Spinner spinnerType;
    @ViewById(R.id.spinner_class)
    private Spinner spinnerClass;
    @ViewById(R.id.edt_event_name)
    private EditText edtEventName;
    @ViewById(R.id.lbl_division)
    private TextView txtDivisionSpinnerTitle;
    @ViewById(R.id.spinner_division)
    private Spinner spnDivision;

    private AnnualCalenderMasterModel annualCalenderMasterModel;
    boolean flag = false;

    private List<DropdownMasterModel> listClass;
    private List<DropdownMasterModel> listDivision;
    private List<DropdownMasterModel> listEventType;
    boolean isEdit = false;
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
        initViewBinding(view);


        datePicker.setOnClickListener(this);

        listEventType = getMyActivity().getDbInvoker().getDropDownByType("EVENTTYPE");
        ArrayAdapter<DropdownMasterModel> eventAdapter = new ArrayAdapter<>(getMyActivity(), android.R.layout.simple_spinner_item, listEventType);
        eventAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerType.setAdapter(eventAdapter);
        eventAdapter.notifyDataSetChanged();

        listClass = getMyActivity().getDbInvoker().getDropDownByType("CLASSTYPE");
        ArrayAdapter<DropdownMasterModel> classAdapter = new ArrayAdapter<DropdownMasterModel>(getMyActivity(), android.R.layout.simple_spinner_item, listClass);
        classAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerClass.setAdapter(classAdapter);
        classAdapter.notifyDataSetChanged();


        DropdownMasterModel allDropdownMasterModel = new DropdownMasterModel();
        allDropdownMasterModel.setDropdownValue("All");
        allDropdownMasterModel.setServerValue(null);
        listDivision = new ArrayList<>();
        listDivision.add(allDropdownMasterModel);
        listDivision.addAll(getMyActivity().getDbInvoker().getDropDownByType("DEVISION"));
        ArrayAdapter<DropdownMasterModel> divisionAdapter = new ArrayAdapter<>(getMyActivity(), android.R.layout.simple_spinner_item, listDivision);
        divisionAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnDivision.setAdapter(divisionAdapter);
        divisionAdapter.notifyDataSetChanged();

        spinnerClass.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (listClass.get(spinnerClass.getSelectedItemPosition()).getDropdownValue().equals("All")) {
                    spnDivision.setVisibility(View.GONE);
                    txtDivisionSpinnerTitle.setVisibility(View.GONE);
                } else {
                    spnDivision.setVisibility(View.VISIBLE);
                    txtDivisionSpinnerTitle.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        Bundle bundle = getArguments();
        if (bundle != null) {
            annualCalenderMasterModel = (AnnualCalenderMasterModel) bundle.getSerializable(Constants.INTENT_TYPE_EVENT_DATA);
            if (annualCalenderMasterModel != null) {
                isEdit = true;
                bindView();
            }
        } else {
            annualCalenderMasterModel= new AnnualCalenderMasterModel();
            isEdit = false;
        }

        //getCalenderEvents();
        return view;
    }

    private void bindView() {
        int classPostion = 0;
        int divisionPosition = 0;
        int eventPosition = 0;
        for (int i = 0; i < listClass.size(); i++) {
            if (listClass.get(i).getDropdownValue().equals(annualCalenderMasterModel.getClassName())) {
                classPostion = i;
                break;
            }
        }
        for (int i = 0; i < listDivision.size(); i++) {
            if (listDivision.get(i).getDropdownValue().equals(annualCalenderMasterModel.getDivisionName())) {
                divisionPosition = i;
                break;
            }
        }
        for (int i = 0; i < listEventType.size(); i++) {
            if (listEventType.get(i).getDropdownValue().equals(annualCalenderMasterModel.getEventType())) {
                eventPosition = i;
                break;
            }
        }

        spinnerClass.setSelection(classPostion);
        spnDivision.setSelection(divisionPosition);
        spinnerType.setSelection(eventPosition);
        edtEventName.setText(annualCalenderMasterModel.getEventName());
        long date = annualCalenderMasterModel.getEventDate();
        datePicker.setText(CommonUtils.formatDateForDisplay(new Date(date), Constants.ONLY_DATE_FORMAT));
}

    private void bindModel() {
        annualCalenderMasterModel.setEventType(listEventType.get(spinnerType.getSelectedItemPosition()).getServerValue());
        annualCalenderMasterModel.setClassName(listClass.get(spinnerClass.getSelectedItemPosition()).getServerValue());
        annualCalenderMasterModel.setEventName(edtEventName.getText().toString());
        if (!spinnerClass.getSelectedItem().toString().equals("All")) {
            annualCalenderMasterModel.setDivisionName(listDivision.get(spnDivision.getSelectedItemPosition()).getServerValue());
        }

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
                try {
                    AlertDialog.Builder builder = getMyActivity().showAlertDialog(getMyActivity(), getString(R.string.app_name), getString(R.string.save_msg));
                    builder.setPositiveButton(getString(R.string.dialog_button_save), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            bindModel();
                            if (check()) {
                                save();
                            }
                        }
                    }).setNegativeButton(getString(R.string.dialog_button_cancel), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();

                        }
                    }).show();
                } catch (Throwable throwable) {
                    throwable.printStackTrace();
                }
                break;

        }
        return true;
    }

    private boolean check() {
        if (annualCalenderMasterModel.getEventName() == null || annualCalenderMasterModel.getEventName().trim().length() <= 0) {
            CommonUtils.showToast(getMyActivity(), getString(R.string.valid_event_name));
            return false;
        }
        if (annualCalenderMasterModel.getEventDate() <= 0) {
            CommonUtils.showToast(getMyActivity(), getString(R.string.valid_event_date));
            return false;
        }
        if (annualCalenderMasterModel.getEventType() == null || annualCalenderMasterModel.getEventType().trim().length() <= 0) {
            CommonUtils.showToast(getMyActivity(), getString(R.string.valid_event_type));
            return false;
        }
        if (annualCalenderMasterModel.getClassName() == null || annualCalenderMasterModel.getClassName().trim().length() <= 0) {
            CommonUtils.showToast(getMyActivity(), getString(R.string.valid_class_name));
            return false;
        }


        return true;
    }

 /*   private void getCalenderEvents() {
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

    }*/


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
            annualCalenderMasterModel.setEventDate(date.getTime());
            txtSelectedDate.setText(CommonUtils.formatDateForDisplay(date, Constants.ONLY_DATE_FORMAT));
        }
    };


    public HomeActivity getMyActivity() {
        return (HomeActivity) getActivity();
    }

    private void save() {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put(IJson.eventName, annualCalenderMasterModel.getEventName());
        hashMap.put(IJson.eventDate, annualCalenderMasterModel.getEventDate());
        hashMap.put(IJson.eventType, annualCalenderMasterModel.getEventType());
        hashMap.put(IJson.className, annualCalenderMasterModel.getClassName());
        hashMap.put(IJson.division, annualCalenderMasterModel.getDivisionName());
        int method;
        if (annualCalenderMasterModel.getPkeyId() != null) {
            hashMap.put(IJson.id, annualCalenderMasterModel.getPkeyId().toString());
            method= Request.Method.PUT;
        } else {
            method= Request.Method.POST;
        }

        CallWebService.getWebserviceObject(getMyActivity(), true, true, method, IUrls.URL_ADD_EVENTS, hashMap, new VolleyResponseListener<AnnualCalenderMasterModel>() {
            @Override
            public void onResponse(AnnualCalenderMasterModel[] object) {
            }

            @Override
            public void onResponse(AnnualCalenderMasterModel studentData) {
                getMyActivity().showFragment(new EventListFragment(), null);
            }

            @Override
            public void onResponse() {
                getMyActivity().showFragment(new EventListFragment(), null);
            }

            @Override
            public void onError(String message) {
                Toast.makeText(getMyActivity(), message, Toast.LENGTH_SHORT).show();
            }
        }, AnnualCalenderMasterModel.class);

    }


}
