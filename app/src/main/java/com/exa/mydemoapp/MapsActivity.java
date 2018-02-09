package com.exa.mydemoapp;

import android.animation.ValueAnimator;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.exa.mydemoapp.Common.AppController;
import com.exa.mydemoapp.Common.CommonActivity;
import com.exa.mydemoapp.Common.Connectivity;
import com.exa.mydemoapp.Common.Constants;
import com.exa.mydemoapp.model.LocationModel;
import com.exa.mydemoapp.tracker.TrackerActivity;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.maps.model.SquareCap;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.google.android.gms.maps.model.JointType.ROUND;

public class MapsActivity extends CommonActivity implements OnMapReadyCallback {

    private static final String TAG = MapsActivity.class.getSimpleName();
    SupportMapFragment mapFragment;
    private GoogleMap mMap;
    private List<LatLng> polyLineList;
    private Marker marker;
    private float v;
    private double lat, lng;
    private Handler handler;
    private LatLng startPosition, endPosition;
    private int index, next;
    private LatLng sydney;
    private String destination;
    private PolylineOptions polylineOptions, blackPolylineOptions;
    private Polyline blackPolyline, greyPolyLine;
    private boolean driveStarted = false;
    private String vanType;

    private Toolbar toolbar;
    private Spinner spnVehicle;
    private Button btnSearch;
    private List<String> vehicleList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_bus_locator);
        init();
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        spnVehicle = (Spinner) findViewById(R.id.spinner_vehicle);
        btnSearch = (Button) findViewById(R.id.btn_search);
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setTitle("Bus Location");
        setSupportActionBar(toolbar);

        destination = "Alandi,Pune";
        destination = destination.replace(" ", "+");
        Log.d(TAG, destination);
        mapFragment.getMapAsync(MapsActivity.this);

        if (Connectivity.isConnected(MapsActivity.this)) {
            //   getVanType();
            getVehicleList();
        } else {
            showToast("Please Connect to internet !!");
        }
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (spnVehicle.getSelectedItem().toString() != null) {
                    String vehicleNumber = spnVehicle.getSelectedItem().toString();
                    if (Connectivity.isConnected(MapsActivity.this)) {
                        //   getVanType();
                        loadPreviousStatuses(vehicleNumber);
                    } else {
                        showToast("Please Connect to internet !!");
                    }

                } else {
                    showToast("No vehicle found");
                }
            }
        });
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mMap.setTrafficEnabled(false);
        mMap.setIndoorEnabled(false);
        mMap.setBuildingsEnabled(false);
        mMap.getUiSettings().setZoomControlsEnabled(true);
        // Add a marker in Home and move the camera
        sydney = new LatLng(18.572897, 73.880653);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Home"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        mMap.moveCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition.Builder()
                .target(googleMap.getCameraPosition().target)
                .zoom(17)
                .bearing(30)
                .tilt(10)
                .build()));
    }


    private void startDrive() {

       /* polyLineList.add(new LatLng(18.588250174643207, 73.73746633529663));
        polyLineList.add(new LatLng(18.591178865663135, 73.73895764350891));
        polyLineList.add(new LatLng(18.59106700686151, 73.74303460121155));
        polyLineList.add(new LatLng(18.59199238201055, 73.75710010528564));
        polyLineList.add(new LatLng(18.592460152150963, 73.7615418434143));
        polyLineList.add(new LatLng(18.590161964765173, 73.77295732498169));
        polyLineList.add(new LatLng(18.588494234151252, 73.7811005115509));
        polyLineList.add(new LatLng(18.587517994021304, 73.78862142562866));
        polyLineList.add(new LatLng(18.583796027210724, 73.79573464393616));*/


       /* ValueAnimator polylineAnimator = ValueAnimator.ofInt(0, 100);
        polylineAnimator.setDuration(2000);
        polylineAnimator.setInterpolator(new LinearInterpolator());
        polylineAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                List<LatLng> points = greyPolyLine.getPoints();
                int percentValue = (int) valueAnimator.getAnimatedValue();
                int size = polyLineList.size();
                int newPoints = (int) (size * (percentValue / 100.0f));
                List<LatLng> p = polyLineList.subList(0, newPoints);
                blackPolyline.setPoints(p);
            }
        });
        polylineAnimator.start();*/

        int height = 140;
        int width = 70;
        BitmapDrawable bitmapdraw = (BitmapDrawable) getResources().getDrawable(R.drawable.icon_bus);
        Bitmap b = bitmapdraw.getBitmap();
        Bitmap smallMarker = Bitmap.createScaledBitmap(b, width, height, false);

        marker = mMap.addMarker(new MarkerOptions().position(polyLineList.get(0))
                .flat(true)
                .icon(BitmapDescriptorFactory.fromBitmap((smallMarker))));
        handler = new Handler();
        index = -1;
        next = 1;

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                if (index < polyLineList.size() - 1) {
                    index++;
                    next = index + 1;
                }
                if (index < polyLineList.size() - 1) {
                    startPosition = polyLineList.get(index);
                    endPosition = polyLineList.get(next);
                } else {
                    handler.removeCallbacks(this::run);
                    driveStarted = false;
                    setFixMarker();
                    return;
                }
                ValueAnimator valueAnimator = ValueAnimator.ofFloat(0, 1);
                valueAnimator.setDuration(30000);
                valueAnimator.setInterpolator(new LinearInterpolator());
                valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator valueAnimator) {
                        v = valueAnimator.getAnimatedFraction();
                        lng = v * endPosition.longitude + (1 - v)
                                * startPosition.longitude;
                        lat = v * endPosition.latitude + (1 - v)
                                * startPosition.latitude;
                        LatLng newPos = new LatLng(lat, lng);
                        marker.setPosition(newPos);
                        marker.setAnchor(0.5f, 0.5f);
                        marker.setRotation(getBearing(startPosition, newPos));
                        mMap.moveCamera(CameraUpdateFactory
                                .newCameraPosition
                                        (new CameraPosition.Builder()
                                                .target(newPos)
                                                .zoom(15.5f)
                                                .build()));
                    }
                });
                valueAnimator.start();
                handler.postDelayed(this, 3000);
            }
        }, 3000);


    }

    private List<LatLng> decodePoly(String encoded) {
        List<LatLng> poly = new ArrayList<>();
        int index = 0, len = encoded.length();
        int lat = 0, lng = 0;

        while (index < len) {
            int b, shift = 0, result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lat += dlat;

            shift = 0;
            result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lng += dlng;

            LatLng p = new LatLng((((double) lat / 1E5)),
                    (((double) lng / 1E5)));
            poly.add(p);
        }

        return poly;
    }

    private float getBearing(LatLng begin, LatLng end) {
        double lat = Math.abs(begin.latitude - end.latitude);
        double lng = Math.abs(begin.longitude - end.longitude);

        if (begin.latitude < end.latitude && begin.longitude < end.longitude)
            return (float) (Math.toDegrees(Math.atan(lng / lat)));
        else if (begin.latitude >= end.latitude && begin.longitude < end.longitude)
            return (float) ((90 - Math.toDegrees(Math.atan(lng / lat))) + 90);
        else if (begin.latitude >= end.latitude && begin.longitude >= end.longitude)
            return (float) (Math.toDegrees(Math.atan(lng / lat)) + 180);
        else if (begin.latitude < end.latitude && begin.longitude >= end.longitude)
            return (float) ((90 - Math.toDegrees(Math.atan(lng / lat))) + 270);
        return -1;
    }

    private void setFixMarker() {
        LatLng newPos = new LatLng(lat, lng);
        marker.setPosition(newPos);
        marker.setAnchor(0.5f, 0.5f);
        mMap.moveCamera(CameraUpdateFactory
                .newCameraPosition
                        (new CameraPosition.Builder()
                                .target(newPos)
                                .zoom(15.5f)
                                .build()));
    }

    public void getVanType() {
        DatabaseReference ref1 = databaseReference.child(Constants.MAIN_TABLE);
        Query ref2 = ref1.child(Constants.LOCATION_TABLE).orderByKey().limitToLast(1);
        ref2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(final DataSnapshot dataSnapshot) {
                for (DataSnapshot Snapshot : dataSnapshot.getChildren()) {
                    LocationModel locationModel = Snapshot.getValue(LocationModel.class);
                    vanType = locationModel.getVanType();
                }

                if (vanType != null && !vanType.isEmpty()) {
                    if (Connectivity.isConnected(MapsActivity.this)) {
                        getDriveData();
                    } else {
                        showToast("Please Connect to internet !!");
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("Exception", "onCancelled", databaseError.toException());
            }
        });
    }

    private void loadPreviousStatuses(String vehicleNumber) {
        LinkedList<Map<String, Object>> mTransportStatuses = new LinkedList<>();
        polyLineList = new ArrayList<>();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.child(Constants.MAIN_TABLE).child(Constants.LOCATION_TABLE).child(vehicleNumber).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot != null) {
                    for (DataSnapshot transportStatus : snapshot.getChildren()) {
                        try {
                            mTransportStatuses.add(0,
                                    (Map<String, Object>) transportStatus.getValue());

                            Map<String, Object> status = (Map<String, Object>) transportStatus.getValue();
                            //LocationModel locationModel = transportStatus.getValue(LocationModel.class);
                            Location locationForStatus = new Location("");
                            locationForStatus.setLatitude((double) status.get("lattitude"));
                            locationForStatus.setLongitude((double) status.get("longitude"));
                            polyLineList.add(new LatLng(locationForStatus.getLatitude(), locationForStatus.getLongitude()));
                            // if (driveStarted == false && polyLineList.size() > 10) {
                            if (driveStarted == false) {
                                startDrive();
                                driveStarted = true;
                            }
                        } catch (Exception e) {
                            Log.e("Firebase Error", e.getMessage());
                        }


                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // TODO: Handle gracefully
            }
        });
    }


    public void getVehicleList() {
        LinkedList<Map<String, Object>> linkedList = new LinkedList<>();
        DatabaseReference ref1 = databaseReference.child(Constants.MAIN_TABLE);
        //Query ref2 = ref1.child(Constants.LOCATION_TABLE).orderByKey().limitToLast(20);
        DatabaseReference ref2 = ref1.child(Constants.LOCATION_TABLE);
        vehicleList = new ArrayList<>();
        ref2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(final DataSnapshot dataSnapshot) {
                for (DataSnapshot Snapshot : dataSnapshot.getChildren()) {
                    String key = Snapshot.getKey();
                    vehicleList.add(key);
                }
                Set<String> set = new HashSet<String>(vehicleList);
                List<String> sortedList = new ArrayList<>();
                sortedList.addAll(set);
                ArrayAdapter<String> vehicleAdapter = new ArrayAdapter<String>(MapsActivity.this, android.R.layout.simple_spinner_item, sortedList);
                vehicleAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spnVehicle.setAdapter(vehicleAdapter);
                vehicleAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("Exception", "onCancelled", databaseError.toException());
            }
        });

    }

    public void getDriveData() {
        DatabaseReference ref1 = databaseReference.child(Constants.MAIN_TABLE);
        //Query ref2 = ref1.child(Constants.LOCATION_TABLE).orderByKey().limitToLast(20);
        DatabaseReference ref2 = ref1.child(Constants.LOCATION_TABLE);
        Query query = ref2.orderByChild("vanType").equalTo(vanType);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(final DataSnapshot dataSnapshot) {
                for (DataSnapshot Snapshot : dataSnapshot.getChildren()) {
                    LocationModel locationModel = Snapshot.getValue(LocationModel.class);
                    Location location = new Location("");
                    location.setLatitude(locationModel.getLattitude());
                    location.setLongitude(locationModel.getLongitude());
                    polyLineList.add(new LatLng(locationModel.getLattitude(), locationModel.getLongitude()));
                    if (driveStarted == false && polyLineList.size() > 10) {
                        startDrive();
                        driveStarted = true;
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("Exception", "onCancelled", databaseError.toException());
            }
        });

    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent1 = new Intent(this, HomeActivity.class);
        startActivity(intent1);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_map, menu);
        if (!AppController.isAdmin(MapsActivity.this)) {
            menu.findItem(R.id.menu_tracker).setVisible(false);
            menu.findItem(R.id.menu_map_demo).setVisible(false);
        }
        // return true so that the menu pop up is opened
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_tracker:
                Intent intent1 = new Intent(this, TrackerActivity.class);
                startActivity(intent1);
                finish();
                break;
            case R.id.menu_map_demo:
                DemoMap();
                break;
        }
        return true;
    }

    private void DemoMap() {
        String requestUrl = null;
        try {
            final double latitude = 18.572605;
            double longitude = 73.878208;
            requestUrl = "https://maps.googleapis.com/maps/api/directions/json?" +
                    "mode=driving&"
                    + "transit_routing_preference=less_driving&"
                    + "origin=" + latitude + "," + longitude + "&"
                    + "destination=" + destination + "&"
                    + "key=" + BuildConfig.google_directions_key + "+&sensor=true";
            Log.d(TAG, requestUrl);

           /* if (Connectivity.isConnected(MapsActivity.this)) {
                //   getVanType();
                loadPreviousStatuses();
            } else {
                showToast("Please Connect to internet !!");
            }*/

            HashMap<String, String> params = new HashMap<String, String>();
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(requestUrl, new JSONObject(params),
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Log.d(TAG, response + "");
                            try {
                                JSONArray jsonArray = response.getJSONArray("routes");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject route = jsonArray.getJSONObject(i);
                                    JSONObject poly = route.getJSONObject("overview_polyline");
                                    String polyline = poly.getString("points");
                                    polyLineList = decodePoly(polyline);
                                    Log.d(TAG, polyLineList + "");
                                }
                                //Adjusting bounds
                                LatLngBounds.Builder builder = new LatLngBounds.Builder();
                                for (LatLng latLng : polyLineList) {
                                    builder.include(latLng);
                                }
                                LatLngBounds bounds = builder.build();
                                CameraUpdate mCameraUpdate = CameraUpdateFactory.newLatLngBounds(bounds, 2);
                                mMap.animateCamera(mCameraUpdate);

                                polylineOptions = new PolylineOptions();
                                polylineOptions.color(Color.GRAY);
                                polylineOptions.width(5);
                                polylineOptions.startCap(new SquareCap());
                                polylineOptions.endCap(new SquareCap());
                                polylineOptions.jointType(ROUND);
                                polylineOptions.addAll(polyLineList);
                                greyPolyLine = mMap.addPolyline(polylineOptions);

                                blackPolylineOptions = new PolylineOptions();
                                blackPolylineOptions.width(5);
                                blackPolylineOptions.color(Color.BLACK);
                                blackPolylineOptions.startCap(new SquareCap());
                                blackPolylineOptions.endCap(new SquareCap());
                                blackPolylineOptions.jointType(ROUND);
                                blackPolyline = mMap.addPolyline(blackPolylineOptions);

                                mMap.addMarker(new MarkerOptions()
                                        .position(polyLineList.get(polyLineList.size() - 1)));

                                ValueAnimator polylineAnimator = ValueAnimator.ofInt(0, 100);
                                polylineAnimator.setDuration(2000);
                                polylineAnimator.setInterpolator(new LinearInterpolator());
                                polylineAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                                    @Override
                                    public void onAnimationUpdate(ValueAnimator valueAnimator) {
                                        List<LatLng> points = greyPolyLine.getPoints();
                                        int percentValue = (int) valueAnimator.getAnimatedValue();
                                        int size = points.size();
                                        int newPoints = (int) (size * (percentValue / 100.0f));
                                        List<LatLng> p = points.subList(0, newPoints);
                                        blackPolyline.setPoints(p);
                                    }
                                });
                                polylineAnimator.start();

                                int height = 140;
                                int width = 70;
                                BitmapDrawable bitmapdraw = (BitmapDrawable) getResources().getDrawable(R.drawable.icon_bus);
                                Bitmap b = bitmapdraw.getBitmap();
                                Bitmap smallMarker = Bitmap.createScaledBitmap(b, width, height, false);

                                marker = mMap.addMarker(new MarkerOptions().position(sydney)
                                        .flat(true)
                                        .icon(BitmapDescriptorFactory.fromBitmap((smallMarker))));
                                handler = new Handler();
                                index = -1;
                                next = 1;
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        if (index < polyLineList.size() - 1) {
                                            index++;
                                            next = index + 1;
                                        }
                                        if (index < polyLineList.size() - 1) {
                                            startPosition = polyLineList.get(index);
                                            endPosition = polyLineList.get(next);
                                        }
                                        ValueAnimator valueAnimator = ValueAnimator.ofFloat(0, 1);
                                        valueAnimator.setDuration(3000);
                                        valueAnimator.setInterpolator(new LinearInterpolator());
                                        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                                            @Override
                                            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                                                v = valueAnimator.getAnimatedFraction();
                                                lng = v * endPosition.longitude + (1 - v)
                                                        * startPosition.longitude;
                                                lat = v * endPosition.latitude + (1 - v)
                                                        * startPosition.latitude;
                                                LatLng newPos = new LatLng(lat, lng);
                                                marker.setPosition(newPos);
                                                marker.setAnchor(0.5f, 0.5f);
                                                marker.setRotation(getBearing(startPosition, newPos));
                                                mMap.moveCamera(CameraUpdateFactory
                                                        .newCameraPosition
                                                                (new CameraPosition.Builder()
                                                                        .target(newPos)
                                                                        .zoom(15.5f)
                                                                        .build()));
                                            }
                                        });
                                        valueAnimator.start();
                                        handler.postDelayed(this, 3000);
                                    }
                                }, 3000);


                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.d(TAG, error + "");
                }
            });
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(jsonObjectRequest);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
