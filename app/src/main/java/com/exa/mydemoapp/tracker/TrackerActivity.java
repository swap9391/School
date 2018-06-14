/**
 * Copyright 2017 Google Inc. All Rights Reserved.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.exa.mydemoapp.tracker;

import android.Manifest;
import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.PowerManager;
import android.provider.Settings;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.exa.mydemoapp.Common.CommonActivity;
import com.exa.mydemoapp.Common.CommonUtils;
import com.exa.mydemoapp.Common.Constants;
import com.exa.mydemoapp.MapsActivity;
import com.exa.mydemoapp.R;
import com.exa.mydemoapp.annotation.ViewById;
import com.exa.mydemoapp.database.DbInvoker;
import com.exa.mydemoapp.model.DropdownMasterModel;

import java.util.List;

public class TrackerActivity extends CommonActivity {

    private static final int PERMISSIONS_REQUEST = 1;
    private static String[] PERMISSIONS_REQUIRED = new String[]{
            Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.WRITE_EXTERNAL_STORAGE};

    private Snackbar mSnackbarPermissions;
    private Snackbar mSnackbarGps;
    Button mStartButton, mStopButton;
    Toolbar toolbar;
    private List<DropdownMasterModel> listTripType;
    private List<DropdownMasterModel> listRouteType;
    @ViewById(R.id.spinner_trip_type)
    private Spinner spnTripType;
    @ViewById(R.id.spinner_route_type)
    private Spinner spnRoute;
    DbInvoker dbInvoker;
    View view;

    /**
     * Configures UI elements, and starts validation if inputs have previously been entered.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_tracker);
        view = getWindow().getDecorView();
        initViewBinding(view);
        dbInvoker = new DbInvoker(this);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setTitle("Start Tracking");
        setSupportActionBar(toolbar);
        String transportID = CommonUtils.getSharedPref(getString(R.string.transport_id), this);
        mStartButton = (Button) findViewById(R.id.button_start);
        mStopButton = (Button) findViewById(R.id.button_stop);

        listRouteType = dbInvoker.getDropDownByType("BUSROUTE");
        ArrayAdapter<DropdownMasterModel> routeAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, listRouteType);
        routeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnRoute.setAdapter(routeAdapter);
        routeAdapter.notifyDataSetChanged();


        listTripType = dbInvoker.getDropDownByType("TRIPTYPE");
        ArrayAdapter<DropdownMasterModel> tripTypeAdapter = new ArrayAdapter<DropdownMasterModel>(this, android.R.layout.simple_spinner_item, listTripType);
        tripTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnTripType.setAdapter(tripTypeAdapter);
        tripTypeAdapter.notifyDataSetChanged();

        mStartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkLocationPermission();
                checkInputFields();
            }
        });

        mStopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                confirmStop();
            }
        });

        if (isServiceRunning(TrackerService.class)) {
            // If service already running, simply update UI.
            setTrackingStatus(R.string.tracking);
        } else {
            mStartButton.setVisibility(View.VISIBLE);
            mStopButton.setVisibility(View.GONE);
            // Inputs have previously been stored, start validation.
            //checkLocationPermission();
        }
    }

    private boolean isServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    private void setTrackingStatus(int status) {
        boolean tracking = status == R.string.tracking;
        spnRoute.setEnabled(!tracking);
        spnTripType.setEnabled(!tracking);
        mStartButton.setVisibility(tracking ? View.INVISIBLE : View.VISIBLE);
        mStopButton.setVisibility(tracking ? View.VISIBLE : View.INVISIBLE);
        ((TextView) findViewById(R.id.title)).setText(getString(status));
    }

    /**
     * Receives status messages from the tracking service.
     */
    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            setTrackingStatus(intent.getIntExtra(getString(R.string.status), 0));
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver,
                new IntentFilter(TrackerService.STATUS_INTENT));
    }

    @Override
    protected void onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mMessageReceiver);
        super.onPause();
    }

    /**
     * First validation check - ensures that required inputs have been
     * entered, and if so, store them and runs the next check.
     */
    private void checkInputFields() {
        // Validate permissions.
        if (spnRoute.getSelectedItem().toString().length() == 0 && spnTripType.getSelectedItem().toString().length() == 0) {
            Toast.makeText(TrackerActivity.this, R.string.missing_inputs, Toast.LENGTH_SHORT).show();
        } else {
            // Store values.
            CommonUtils.insertSharedPref(TrackerActivity.this, Constants.ROUTE_TYPE, listRouteType.get(spnRoute.getSelectedItemPosition()).getServerValue());
            CommonUtils.insertSharedPref(TrackerActivity.this, Constants.TRIP_TYPE, listTripType.get(spnTripType.getSelectedItemPosition()).getServerValue());
            // Validate permissions.
            // checkLocationPermission();
            checkGpsEnabled();
        }

    }

    /**
     * Second validation check - ensures the app has location permissions, and
     * if not, requests them, otherwise runs the next check.
     */
    private void checkLocationPermission() {
        int locationPermission = ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION);
        int storagePermission = ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (locationPermission != PackageManager.PERMISSION_GRANTED
                || storagePermission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, PERMISSIONS_REQUIRED, PERMISSIONS_REQUEST);
        } else {
            checkGpsEnabled();
        }
    }

    /**
     * Third and final validation check - ensures GPS is enabled, and if not, prompts to
     * enable it, otherwise all checks pass so start the location tracking service.
     */
    private void checkGpsEnabled() {
        LocationManager lm = (LocationManager) getSystemService(LOCATION_SERVICE);
        if (!lm.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            reportGpsError();
        } else {
            resolveGpsError();
            startLocationService();
        }
    }

    /**
     * Callback for location permission request - if successful, run the GPS check.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[]
            grantResults) {
        if (requestCode == PERMISSIONS_REQUEST) {
            // We request storage perms as well as location perms, but don't care
            // about the storage perms - it's just for debugging.
            for (int i = 0; i < permissions.length; i++) {
                if (permissions[i].equals(Manifest.permission.ACCESS_FINE_LOCATION)) {
                    if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                        reportPermissionsError();
                    } else {
                        resolvePermissionsError();
                        checkGpsEnabled();
                    }
                }
            }
        }
    }

    private void startLocationService() {
        // Before we start the service, confirm that we have extra power usage privileges.
        PowerManager pm = (PowerManager) getSystemService(POWER_SERVICE);
        Intent intent = new Intent();
        //    if (!pm.isIgnoringBatteryOptimizations(getPackageName())) {
        intent.setAction(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS);
        intent.setData(Uri.parse("package:" + getPackageName()));
        startActivity(intent);
        //}
        startService(new Intent(this, TrackerService.class));
    }

    private void stopLocationService() {
        stopService(new Intent(this, TrackerService.class));
    }


    private void confirmStop() {
        new AlertDialog.Builder(this)
                .setMessage(getString(R.string.confirm_stop))
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        spnRoute.setEnabled(true);
                        spnTripType.setEnabled(true);
                        mStartButton.setVisibility(View.VISIBLE);
                        stopLocationService();
                    }
                })
                .setNegativeButton(android.R.string.no, null).show();
    }

    private void reportPermissionsError() {
        Snackbar snackbar = Snackbar
                .make(
                        findViewById(R.id.rootView),
                        getString(R.string.location_permission_required),
                        Snackbar.LENGTH_INDEFINITE)
                .setAction(R.string.enable, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(Settings
                                .ACTION_APPLICATION_DETAILS_SETTINGS);
                        intent.setData(Uri.parse("package:" + getPackageName()));
                        startActivity(intent);
                    }
                });

        // Changing message text color
        snackbar.setActionTextColor(Color.RED);

        // Changing action button text color
        View sbView = snackbar.getView();
        TextView textView = (TextView) sbView.findViewById(
                android.support.design.R.id.snackbar_text);
        textView.setTextColor(Color.YELLOW);
        snackbar.show();
    }

    private void resolvePermissionsError() {
        if (mSnackbarPermissions != null) {
            mSnackbarPermissions.dismiss();
            mSnackbarPermissions = null;
        }
    }

    private void reportGpsError() {
        Snackbar snackbar = Snackbar
                .make(findViewById(R.id.rootView), getString(R.string
                        .gps_required), Snackbar.LENGTH_INDEFINITE)
                .setAction(R.string.enable, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                });

        // Changing message text color
        snackbar.setActionTextColor(Color.RED);

        // Changing action button text color
        View sbView = snackbar.getView();
        TextView textView = (TextView) sbView.findViewById(android.support.design.R.id
                .snackbar_text);
        textView.setTextColor(Color.YELLOW);
        snackbar.show();

    }

    private void resolveGpsError() {
        if (mSnackbarGps != null) {
            mSnackbarGps.dismiss();
            mSnackbarGps = null;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent1 = new Intent(this, MapsActivity.class);
        startActivity(intent1);
        finish();
    }
}
