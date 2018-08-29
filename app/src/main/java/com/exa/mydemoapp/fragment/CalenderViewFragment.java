package com.exa.mydemoapp.fragment;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.exa.mydemoapp.Common.CommonUtils;
import com.exa.mydemoapp.Common.Connectivity;
import com.exa.mydemoapp.Common.Constants;
import com.exa.mydemoapp.HomeActivity;
import com.exa.mydemoapp.R;
import com.exa.mydemoapp.adapter.CalenderEventAdapter;
import com.exa.mydemoapp.adapter.UserAdapter;
import com.exa.mydemoapp.model.AnnualCalenderMasterModel;
import com.exa.mydemoapp.model.DateModel;
import com.exa.mydemoapp.webservice.CallWebService;
import com.exa.mydemoapp.webservice.IJson;
import com.exa.mydemoapp.webservice.IUrls;
import com.exa.mydemoapp.webservice.VolleyResponseListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.roomorama.caldroid.CaldroidFragment;
import com.roomorama.caldroid.CaldroidListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by midt-006 on 4/10/17.
 */
@SuppressLint("SimpleDateFormat")
public class CalenderViewFragment extends Fragment {
    private boolean undo = false;
    private CaldroidFragment caldroidFragment;
    private CaldroidFragment dialogCaldroidFragment;
    private RecyclerView recyclerView;
    List<AnnualCalenderMasterModel> listEvent = new ArrayList<>();
    List<DateModel> listDate;
    ProgressDialog progressDialog;
    View view;
    Map<String, String> datemap;
    CalenderEventAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.layout_calender, container, false);
        progressDialog = new ProgressDialog(getMyActivity());

        getMyActivity().toolbar.setTitle(getString(R.string.dashboard_calender));
        getMyActivity().init();

        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);

        // Setup caldroid fragment
        // **** If you want normal CaldroidFragment, use below line ****

        caldroidFragment = new CaldroidFragment();

        // //////////////////////////////////////////////////////////////////////
        // **** This is to show customized fragment. If you want customized
        // version, uncomment below line ****
//		 caldroidFragment = new CaldroidSampleCustomFragment();

        // Setup arguments

        // If Activity is created after rotation
        if (savedInstanceState != null) {
            caldroidFragment.restoreStatesFromKey(savedInstanceState,
                    "CALDROID_SAVED_STATE");
        }
        // If activity is created from fresh
        else {
            Bundle args = new Bundle();
            Calendar cal = Calendar.getInstance();
            args.putInt(CaldroidFragment.MONTH, cal.get(Calendar.MONTH) + 1);
            args.putInt(CaldroidFragment.YEAR, cal.get(Calendar.YEAR));
            args.putBoolean(CaldroidFragment.ENABLE_SWIPE, true);
            args.putBoolean(CaldroidFragment.SIX_WEEKS_IN_CALENDAR, true);

            // Uncomment this to customize startDayOfWeek
            // args.putInt(CaldroidFragment.START_DAY_OF_WEEK,
            // CaldroidFragment.TUESDAY); // Tuesday

            // Uncomment this line to use Caldroid in compact mode
            // args.putBoolean(CaldroidFragment.SQUARE_TEXT_VIEW_CELL, false);

            // Uncomment this line to use dark theme
//            args.putInt(CaldroidFragment.THEME_RESOURCE, com.caldroid.R.style.CaldroidDefaultDark);

            caldroidFragment.setArguments(args);
        }

        // setCustomResourceForDates();

        // Attach to the activity
        if (listEvent.size() == 0) {
            getCalenderEvents();
        } else {
            initCalender();
        }
        // Setup listener
        final CaldroidListener listener = new CaldroidListener() {

            @Override
            public void onSelectDate(Date date, View view) {

                List<String> eventList = new ArrayList<>();

                for (DateModel bean : listDate) {
                    if (date.compareTo(bean.getDate()) == 0) {
                        eventList.add(listEvent.get(bean.getPosition()).getEventName());
                    }
                }

                adapter = new CalenderEventAdapter(eventList, getMyActivity());
                LinearLayoutManager mLayoutManager =
                        new LinearLayoutManager(getMyActivity());
                recyclerView.setLayoutManager(mLayoutManager);
                recyclerView.setItemAnimator(new DefaultItemAnimator());
                recyclerView.setAdapter(adapter);

            }

            @Override
            public void onChangeMonth(int month, int year) {
                countWeekendDays(year, month);
                if (listEvent.size() > 0) {
                    initCalender();
                } else {
                    getCalenderEvents();
                }
            }

            @Override
            public void onLongClickDate(Date date, View view) {
           /*     Toast.makeText(getApplicationContext(),
                        "Long click " + formatter.format(date),
                        Toast.LENGTH_SHORT).show();*/
            }

            @Override
            public void onCaldroidViewCreated() {
                if (caldroidFragment.getLeftArrowButton() != null) {
                    //calender created
                }
            }

        };

        // Setup Caldroid
        caldroidFragment.setCaldroidListener(listener);

        return view;
    }

    /**
     * Save current states of the Caldroid here
     */
/*    @Override
    protected void onSaveInstanceState(Bundle outState) {
        // TODO Auto-generated method stub
        super.onSaveInstanceState(outState);

        if (caldroidFragment != null) {
            caldroidFragment.saveStatesToKey(outState, "CALDROID_SAVED_STATE");
        }

        if (dialogCaldroidFragment != null) {
            dialogCaldroidFragment.saveStatesToKey(outState,
                    "DIALOG_CALDROID_SAVED_STATE");
        }
    }*/
    public int countWeekendDays(int year, int month) {
        Calendar calendar = Calendar.getInstance();
        // Note that month is 0-based in calendar, bizarrely.
        calendar.set(year, month - 1, 1);
        int daysInMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        ColorDrawable red = new ColorDrawable(getResources().getColor(R.color.caldroid_light_red));
        int count = 0;
        for (int day = 1; day <= daysInMonth; day++) {
            calendar.set(year, month - 1, day);
            int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
            if (dayOfWeek == Calendar.SUNDAY || dayOfWeek == Calendar.SATURDAY) {
                caldroidFragment.setBackgroundDrawableForDate(red, calendar.getTime());
                count++;
                // Or do whatever you need to with the result.
            }
        }
        return count;
    }


    private void getData() {
        listDate = new ArrayList<>();
        progressDialog.setTitle("Loading...");
        progressDialog.show();
        String userId = getMyActivity().databaseReference.push().getKey();
        DatabaseReference ref1 = getMyActivity().databaseReference.child(Constants.MAIN_TABLE);
        DatabaseReference ref2 = ref1.child(Constants.EVENT_TABLE);
        ref2.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int count = 0;
                for (DataSnapshot Snapshot : dataSnapshot.getChildren()) {
                    AnnualCalenderMasterModel bean = Snapshot.getValue(AnnualCalenderMasterModel.class);
                    listEvent.add(bean);
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTimeInMillis(bean.getEventDate());
                    Date date = calendar.getTime();
                    DateModel dateModel = new DateModel();
                    dateModel.setPosition(count);
                    dateModel.setDate(date);
                    dateModel.setEventType(bean.getEventType());
                    listDate.add(dateModel);
                    count++;
                }
                ColorDrawable skyblue = new ColorDrawable(getResources().getColor(R.color.caldroid_sky_blue));
                ColorDrawable yellow = new ColorDrawable(getResources().getColor(R.color.disable_btn_color));
                ColorDrawable green = new ColorDrawable(getResources().getColor(R.color.green));
                ColorDrawable red = new ColorDrawable(getResources().getColor(R.color.caldroid_light_red));

                for (DateModel dateModel : listDate) {

                    switch (dateModel.getEventType()) {
                        case "Holiday":
                            caldroidFragment.setBackgroundDrawableForDate(skyblue, dateModel.getDate());
                            break;
                        case "Exam":
                            caldroidFragment.setBackgroundDrawableForDate(yellow, dateModel.getDate());
                            break;
                        case "Curricular":
                            caldroidFragment.setBackgroundDrawableForDate(green, dateModel.getDate());
                            break;
                        default:
                            caldroidFragment.setBackgroundDrawableForDate(red, dateModel.getDate());
                            break;
                    }

                }

                FragmentTransaction t = getMyActivity().getSupportFragmentManager().beginTransaction();
                t.replace(R.id.calendar1, caldroidFragment);
                t.commit();
                progressDialog.dismiss();


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("Exception", "onCancelled", databaseError.toException());
                progressDialog.dismiss();
            }
        });

    }

    private void getCalenderEvents() {
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
                for (AnnualCalenderMasterModel rewardModel : object) {
                    listEvent.add(rewardModel);
                }
                if (listEvent.size() > 0) {
                    initCalender();
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
                getMyActivity().showToast(message);
            }
        }, AnnualCalenderMasterModel[].class);

    }


    private void initCalender() {
        int count = 0;
        listDate = new ArrayList<>();
        datemap = new HashMap<>();
        for (AnnualCalenderMasterModel bean : listEvent) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(bean.getEventDate());
            Date date = calendar.getTime();
            DateModel dateModel = new DateModel();
            dateModel.setPosition(count);
            dateModel.setDate(date);
            dateModel.setEventType(bean.getEventType());
            listDate.add(dateModel);
            datemap.put(CommonUtils.formatDateForDisplay(date, Constants.ONLY_DATE_FORMAT), bean.getEventName());
            count++;
        }
        ColorDrawable skyblue = new ColorDrawable(getResources().getColor(R.color.caldroid_sky_blue));
        ColorDrawable yellow = new ColorDrawable(getResources().getColor(R.color.disable_btn_color));
        ColorDrawable green = new ColorDrawable(getResources().getColor(R.color.green));
        ColorDrawable red = new ColorDrawable(getResources().getColor(R.color.caldroid_light_red));

        for (DateModel dateModel : listDate) {

            switch (dateModel.getEventType()) {
                case "HL":
                    caldroidFragment.setBackgroundDrawableForDate(skyblue, dateModel.getDate());
                    break;
                case "EX":
                    caldroidFragment.setBackgroundDrawableForDate(yellow, dateModel.getDate());
                    break;
                case "CR":
                    caldroidFragment.setBackgroundDrawableForDate(green, dateModel.getDate());
                    break;
                default:
                    caldroidFragment.setBackgroundDrawableForDate(red, dateModel.getDate());
                    break;
            }

        }

        FragmentTransaction t = getMyActivity().getSupportFragmentManager().beginTransaction();
        t.replace(R.id.calendar1, caldroidFragment);
        t.commit();

    }

    public HomeActivity getMyActivity() {
        return (HomeActivity) getActivity();
    }
}