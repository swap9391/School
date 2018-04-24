package com.exa.mydemoapp.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Binder;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import com.android.volley.Request;
import com.exa.mydemoapp.Common.CommonUtils;
import com.exa.mydemoapp.Common.Connectivity;
import com.exa.mydemoapp.Common.Constants;
import com.exa.mydemoapp.model.BusLocationsModel;
import com.exa.mydemoapp.model.StudentRewardsModel;
import com.exa.mydemoapp.tracker.TrackerService;
import com.exa.mydemoapp.tracker.TrackerTaskService;
import com.exa.mydemoapp.webservice.CallWebService;
import com.exa.mydemoapp.webservice.IJson;
import com.exa.mydemoapp.webservice.IUrls;
import com.exa.mydemoapp.webservice.VolleyResponseListener;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.gcm.GcmNetworkManager;
import com.google.android.gms.gcm.OneoffTask;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

//not used
public class LocationUpdateService extends Service {

    private DatabaseReference databaseReference;
    private StorageReference mStorageRef;
    // Binder given to clients
    private final IBinder binder = new LocalBinder();
    // Registered callbacks
    private ServiceCallbacks serviceCallbacks;


    private final String TAG = "LocationActivity";
    private final long INTERVAL = 1000 * 10;
    private final long FASTEST_INTERVAL = 1000 * 5;
    private boolean startUpload = false;
    Handler handler;
    private String rootType;
    private String tripType;


    @Override
    public void onCreate() {

        Log.d(TAG, "onCreate ...............................");
        //show error dialog if GoolglePlayServices not available
    }


    public Runnable runnable = new Runnable() {
        @Override
        public void run() {
            handler = new Handler();
            handler.postDelayed(new Runnable() {
                public void run() {
                    handler.postDelayed(this, 30000);
                    updateUI();
                }
            }, 30000);
        }
    };

    /*@Override
    public void onStop() {
        super.onStop();
        Log.d(TAG, "onStop fired ..............");
        mGoogleApiClient.disconnect();
        Log.d(TAG, "isConnected ...............: " + mGoogleApiClient.isConnected());
    }*/


    protected void startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Log.d(TAG, "Location update started ..............: ");
    }


    private void updateUI() {
        Log.d(TAG, "UI update initiated .............");
        HashMap<String, Object> hashMap = new HashMap<>();
        long date = System.currentTimeMillis() / 1000;

        String url = String.format(IUrls.URL_GET_BUS_LOCATION, date, "Moshi", "Morning");

        CallWebService.getWebserviceObject(LocationUpdateService.this, false, false, Request.Method.POST, url, hashMap, new VolleyResponseListener<BusLocationsModel>() {
            @Override
            public void onResponse(BusLocationsModel[] object) {
            }

            @Override
            public void onResponse(BusLocationsModel busLocationsModel) {
                serviceCallbacks.doSomething(busLocationsModel);
            }

            @Override
            public void onResponse() {
            }

            @Override
            public void onError(String message) {
                // stopSelf();
            }
        }, BusLocationsModel.class);
    }

   /* @Override
    protected void onPause() {
        super.onPause();
        stopLocationUpdates();
    }*/


    /*@Override
    public void onResume() {
        super.onResume();
        if (mGoogleApiClient.isConnected()) {
            startLocationUpdates();
            Log.d(TAG, "Location update resumed .....................");
        }
    }*/

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent.getStringExtra(Constants.ROUTE_TYPE) != null && intent.getStringExtra(Constants.TRIP_TYPE) != null) {
            rootType = intent.getStringExtra(Constants.ROUTE_TYPE);
            tripType = intent.getStringExtra(Constants.TRIP_TYPE);
            runnable.run();
        }
       /* if (intent.getStringExtra("VANTYPE") != null) {
            vanType = intent.getStringExtra("VANTYPE");
            if (mGoogleApiClient != null) {
                mGoogleApiClient.connect();
                stopUpload = false;
            }
            if (mGoogleApiClient.isConnected()) {
                startLocationUpdates();
                Log.d(TAG, "Location update resumed .....................");
            }
        }*/
        return START_REDELIVER_INTENT;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    public class LocalBinder extends Binder {
        public LocationUpdateService getService() {
            // Return this instance of MyService so clients can call public methods
            return LocationUpdateService.this;
        }
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }

    public void setCallbacks(ServiceCallbacks callbacks) {
        serviceCallbacks = callbacks;
    }


}
