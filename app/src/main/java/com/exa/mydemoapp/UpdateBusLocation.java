package com.exa.mydemoapp;

/**
 * Created by midt-006 on 16/1/18.
 */

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.exa.mydemoapp.Common.CommonActivity;
import com.exa.mydemoapp.Common.CommonUtils;
import com.exa.mydemoapp.Common.Connectivity;
import com.exa.mydemoapp.annotation.ViewById;
import com.exa.mydemoapp.service.LocationUpdateService;
import com.exa.mydemoapp.service.ServiceCallbacks;

import static com.exa.mydemoapp.Common.Constants.LAST_VAN_TYPE;
//not used
public class UpdateBusLocation extends CommonActivity implements
        ServiceCallbacks {

    @ViewById(R.id.txt_van_type)
    EditText edtVanType;
    @ViewById(R.id.btn_start_drive)
    Button btnStart;
    @ViewById(R.id.btn_stop_drive)
    Button btnStop;
    View view;
    LocationUpdateService myservice;
    boolean bound = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();

        setContentView(R.layout.activity_main);
        view = getWindow().getDecorView();
        initViewBinding(view);
        if (CommonUtils.getSharedPref(LAST_VAN_TYPE, this) != null) {
            edtVanType.setText(CommonUtils.getSharedPref(LAST_VAN_TYPE, this));
        }

        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Connectivity.isConnected(UpdateBusLocation.this) && edtVanType.getText().toString() != null && !edtVanType.getText().toString().isEmpty()) {
                    CommonUtils.insertSharedPref(UpdateBusLocation.this, LAST_VAN_TYPE, edtVanType.getText().toString());
                    Intent serviceIntent = new Intent(UpdateBusLocation.this, LocationUpdateService.class);
                    serviceIntent.putExtra("VANTYPE", edtVanType.getText().toString());
                    bindService(serviceIntent, serviceConnection, Context.BIND_AUTO_CREATE);
                    startService(serviceIntent);

                } else {
                    showToast("Please Enter Van type");
                }
            }
        });

        btnStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean isbound = false;
                if (myservice != null) {
                    isbound = getApplicationContext().bindService(new Intent(getApplicationContext(), LocationUpdateService.class), serviceConnection, Context.BIND_AUTO_CREATE);
                    if (isbound) {
                        myservice.stopLocationUpdates();
                        getApplicationContext().unbindService(serviceConnection);
                        stopService(new Intent(UpdateBusLocation.this, LocationUpdateService.class));
                    }
                }
            }
        });

    }

    public ServiceConnection serviceConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className, IBinder service) {
            // cast the IBinder and get MyService instance
            LocationUpdateService.LocalBinder binder = (LocationUpdateService.LocalBinder) service;
            myservice = binder.getService();
            bound = true;
            myservice.setCallbacks(UpdateBusLocation.this);
          /*  myService.setCallbacks(DashboardFragment.this); // register
            myService.setCount(DashboardFragment.this);*/
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            bound = false;
        }
    };

    @Override
    public void doSomething() {
        showToast("Location Updated");
    }
}