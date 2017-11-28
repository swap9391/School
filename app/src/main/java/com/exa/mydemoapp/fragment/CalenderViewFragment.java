package com.exa.mydemoapp.fragment;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.exa.mydemoapp.Common.CommonUtils;
import com.exa.mydemoapp.Common.Connectivity;
import com.exa.mydemoapp.Common.Constants;
import com.exa.mydemoapp.HomeActivity;
import com.exa.mydemoapp.R;
import com.exa.mydemoapp.model.DateModel;
import com.exa.mydemoapp.model.EventModel;
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
import java.util.List;

/**
 * Created by midt-006 on 4/10/17.
 */
@SuppressLint("SimpleDateFormat")
public class CalenderViewFragment extends Fragment {
    private boolean undo = false;
    private CaldroidFragment caldroidFragment;
    private CaldroidFragment dialogCaldroidFragment;
    private TextView textview;
    List<EventModel> listEvent = new ArrayList<>();
    List<DateModel> listDate ;
    ProgressDialog progressDialog;

    View view;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.layout_calender, container, false);
        progressDialog= new ProgressDialog(getMyActivity());;
        getMyActivity().toolbar.setTitle("Calender View");
        getMyActivity().init();

        textview = (TextView) view.findViewById(R.id.textview);
        final SimpleDateFormat formatter = new SimpleDateFormat("dd MMM yyyy");

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
        if (Connectivity.isConnected(getMyActivity())) {
            getData();
        }
        // Setup listener
        final CaldroidListener listener = new CaldroidListener() {

            @Override
            public void onSelectDate(Date date, View view) {
                for (DateModel bean : listDate) {
                    if (date.compareTo(bean.getDate()) == 0) {
                        textview.setText(listEvent.get(bean.getPosition()).getEventName());
                        return;
                    } else {
                        textview.setText("");
                    }
                }
            }

            @Override
            public void onChangeMonth(int month, int year) {
                String text = "month: " + month + " year: " + year;
                countWeekendDays(year, month);
                if (Connectivity.isConnected(getMyActivity())) {
                    getData();
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
        listDate= new ArrayList<>();
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
                    EventModel bean = Snapshot.getValue(EventModel.class);
                    listEvent.add(bean);
                    Date date = CommonUtils.toDate(bean.getEventDate(), Constants.DATE_FORMAT);
                    DateModel dateModel = new DateModel();
                    dateModel.setPosition(count);
                    dateModel.setDate(date);
                    listDate.add(dateModel);
                    count++;
                }
                ColorDrawable skyblue = new ColorDrawable(getResources().getColor(R.color.caldroid_sky_blue));
                for (DateModel bean : listDate) {
                    caldroidFragment.setBackgroundDrawableForDate(skyblue, bean.getDate());
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

    public HomeActivity getMyActivity() {
        return (HomeActivity) getActivity();
    }
}