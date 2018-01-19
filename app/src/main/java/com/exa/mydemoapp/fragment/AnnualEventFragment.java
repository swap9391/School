package com.exa.mydemoapp.fragment;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
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
import android.widget.Toast;

import com.exa.mydemoapp.Common.CommonUtils;
import com.exa.mydemoapp.Common.Connectivity;
import com.exa.mydemoapp.Common.Constants;
import com.exa.mydemoapp.HomeActivity;
import com.exa.mydemoapp.R;
import com.exa.mydemoapp.model.EventModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by midt-006 on 11/10/17.
 */

public class AnnualEventFragment extends Fragment implements View.OnClickListener {
    private Button datePicker;
    private Spinner spinnerType;
    private Spinner spinnerClass;
    private EditText edtEventName;
    private EventModel eventModel;
    List<EventModel> listEvent = new ArrayList<>();
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

        eventModel = new EventModel();
        datePicker = (Button) view.findViewById(R.id.date_picker_event);
        spinnerType = (Spinner) view.findViewById(R.id.spinner_event_type);
        spinnerClass = (Spinner) view.findViewById(R.id.spinner_class);
        edtEventName = (EditText) view.findViewById(R.id.edt_event_name);
        datePicker.setOnClickListener(this);

        List<String> listEventType = Arrays.asList(getResources().getStringArray(R.array.event_type));
        ArrayAdapter<String> eventAdapter = new ArrayAdapter<String>(getMyActivity(), android.R.layout.simple_spinner_item, listEventType);
        eventAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerType.setAdapter(eventAdapter);
        eventAdapter.notifyDataSetChanged();
        List<String> listClass = Arrays.asList(getResources().getStringArray(R.array.class_type));
        ArrayAdapter<String> classAdapter = new ArrayAdapter<String>(getMyActivity(), android.R.layout.simple_spinner_item, listClass);
        classAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerClass.setAdapter(classAdapter);
        classAdapter.notifyDataSetChanged();

        getData();
        return view;
    }


    private void bindModel() {
        eventModel.setEventType(spinnerType.getSelectedItem().toString());
        eventModel.setEventClass(spinnerClass.getSelectedItem().toString());
        eventModel.setEventName(edtEventName.getText().toString());
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.date_picker_event:
                showDatePicker();
                break;
        }
    }

    private void saveUserInformation() {
        String userId = getMyActivity().databaseReference.push().getKey();
        eventModel.setUniqKey(userId);
        getMyActivity().databaseReference.child(Constants.MAIN_TABLE).child(Constants.EVENT_TABLE).child(userId).setValue(eventModel);
        Toast.makeText(getMyActivity(), "Information Saved...", Toast.LENGTH_LONG).show();
        getData();
    }


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
                        saveUserInformation();
                    } else {
                        getMyActivity().showToast("Please Connect to internet !!");
                    }
                }
                break;

        }
        return true;
    }

    private boolean check() {
        if (eventModel.getEventName() == null || eventModel.getEventName().trim().length() <= 0) {
            CommonUtils.showToast(getMyActivity(), "Please Enter Event Name");
            return false;
        }
        if (eventModel.getEventDate() == null || eventModel.getEventDate().trim().length() <= 0) {
            CommonUtils.showToast(getMyActivity(), "Please Select Event Date");
            return false;
        }
        if (eventModel.getEventType() == null || eventModel.getEventType().trim().length() <= 0) {
            CommonUtils.showToast(getMyActivity(), "Please Select Event Type");
            return false;
        }
        if (eventModel.getEventClass() == null || eventModel.getEventClass().trim().length() <= 0) {
            CommonUtils.showToast(getMyActivity(), "Please Select Event Class");
            return false;
        }

        for (EventModel bean : listEvent) {
            if (eventModel.getEventDate().equalsIgnoreCase(bean.getEventDate())) {
                CommonUtils.showToast(getMyActivity(), "Event already present to this date.");
                return false;
            }
        }

        return true;
    }

    private void getData() {

        String userId = getMyActivity().databaseReference.push().getKey();
        DatabaseReference ref1 = getMyActivity().databaseReference.child(Constants.MAIN_TABLE);
        DatabaseReference ref2 = ref1.child(Constants.EVENT_TABLE);
        ref2.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot Snapshot : dataSnapshot.getChildren()) {
                    EventModel bean = Snapshot.getValue(EventModel.class);
                    listEvent.add(bean);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("Exception", "onCancelled", databaseError.toException());
            }
        });
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
            eventModel.setEventDate(formatedDate);

        }
    };


    public HomeActivity getMyActivity() {
        return (HomeActivity) getActivity();
    }

}
