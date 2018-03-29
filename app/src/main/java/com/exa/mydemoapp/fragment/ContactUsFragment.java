package com.exa.mydemoapp.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.exa.mydemoapp.HomeActivity;
import com.exa.mydemoapp.MapsActivity;
import com.exa.mydemoapp.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * Created by midt-078 on 29/3/18.
 */

public class ContactUsFragment extends CommonFragment implements OnMapReadyCallback {
    View view;
    SupportMapFragment mapFragment;
    private GoogleMap mMap;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.layout_contact_us, container, false);
        getMyActivity().init();
        initViewBinding(view);
        getMyActivity().getToolbar().setTitle("Contact Us");
        /*mapFragment = (SupportMapFragment) getMyActivity().getSupportFragmentManager()
                .findFragmentById(R.id.map);*/
        mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        return view;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mMap.setTrafficEnabled(false);
        mMap.setIndoorEnabled(false);
        mMap.setBuildingsEnabled(false);
        mMap.getUiSettings().setZoomControlsEnabled(true);
        // Add a marker in Home and move the camera
        LatLng myLocation = new LatLng(18.675890, 73.880187);
        mMap.addMarker(new MarkerOptions().position(myLocation).title(getStringById(R.string.app_name)));
      //  mMap.moveCamera(CameraUpdateFactory.newLatLng(myLocation));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(myLocation, 18));
       /* mMap.moveCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition.Builder()
                .target(googleMap.getCameraPosition().target)
                .zoom(10)
                .bearing(30)
                .tilt(10)
                .build()));*/
    }

    private HomeActivity getMyActivity() {
        return (HomeActivity) getActivity();
    }
}
