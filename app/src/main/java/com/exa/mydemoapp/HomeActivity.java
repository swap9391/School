package com.exa.mydemoapp;

import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.exa.mydemoapp.Common.AppController;
import com.exa.mydemoapp.Common.CommonActivity;
import com.exa.mydemoapp.Common.CommonUtils;
import com.exa.mydemoapp.Common.Constants;
import com.exa.mydemoapp.Common.StudentInfoSingleton;
import com.exa.mydemoapp.adapter.UserAdapter;
import com.exa.mydemoapp.database.Database;
import com.exa.mydemoapp.database.DbInvoker;
import com.exa.mydemoapp.fragment.AboutSchoolFragment;
import com.exa.mydemoapp.fragment.AlbumViewFragment;
import com.exa.mydemoapp.fragment.AnnualEventFragment;
import com.exa.mydemoapp.fragment.CalenderViewFragment;
import com.exa.mydemoapp.fragment.CommunityFragment;
import com.exa.mydemoapp.fragment.DashboardFragment;
import com.exa.mydemoapp.fragment.GalleryViewFragment;
import com.exa.mydemoapp.fragment.NewsFeedFragment;
import com.exa.mydemoapp.fragment.ProfileFragment;
import com.exa.mydemoapp.fragment.RewardGraphFragment;
import com.exa.mydemoapp.fragment.RewardsPointsFragment;
import com.exa.mydemoapp.fragment.SlideshowDialogFragment;
import com.exa.mydemoapp.fragment.StaffInfoFragment;
import com.exa.mydemoapp.fragment.UploadPhotoFragment;
import com.exa.mydemoapp.fragment.UsersListFragment;
import com.exa.mydemoapp.model.ImageRequest;
import com.exa.mydemoapp.model.StudentModel;
import com.exa.mydemoapp.tracker.TrackerService;
import com.exa.mydemoapp.webservice.CallWebService;
import com.exa.mydemoapp.webservice.IJson;
import com.exa.mydemoapp.webservice.IUrls;
import com.exa.mydemoapp.webservice.VolleyResponseListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by midt-006 on 12/10/17.
 */
public class HomeActivity extends CommonActivity {
    public Toolbar toolbar;
    public AlbumViewFragment albumViewFragment;
    public AnnualEventFragment annualEventFragment;
    public CalenderViewFragment calenderViewFragment;
    public DashboardFragment dashboardFragment;
    public GalleryViewFragment galleryViewFragment;
    public NewsFeedFragment newsFeedFragment;
    public SlideshowDialogFragment slideshowDialogFragment;
    public UploadPhotoFragment uploadPhotoFragment;
    public CommunityFragment communityFragment;
    public AboutSchoolFragment aboutSchoolFragment;
    public ProfileFragment profileFragment;
    public SignUpFragment signUpFragment;
    public StaffInfoFragment staffInfoFragment;
    public UsersListFragment usersListFragment;
    public RewardsPointsFragment rewardsPointsFragment;
    public RewardGraphFragment rewardGraphFragment;
    public List<ImageRequest> listAlbumChild = new ArrayList<ImageRequest>();
    public boolean isGallery = true;
    public boolean isGuest = false;
    private Fragment fromFragment;
    private String newsFeedType;
    private StudentInfoSingleton studentInfoSingleton;
    Database db;
    DbInvoker dbInvoker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        db = new Database(HomeActivity.this);
        dbInvoker = new DbInvoker(this);
        if (intent != null && intent.getStringExtra(Constants.USER_TYPE) != null && intent.getStringExtra(Constants.USER_TYPE).equals(Constants.USER_TYPE_GUEST)) {
            isGuest = true;
        } else {
            isGuest = false;
        }
        if (isGuest) {
            setContentView(R.layout.layout_home_guest);
        } else {
            setContentView(R.layout.layout_home);
            //   startNotificationService();
            studentInfoSingleton = StudentInfoSingleton.getInstance(this);
            FirebaseMessaging.getInstance().subscribeToTopic("test123");
            FirebaseInstanceId.getInstance().getToken();
            String token = FirebaseInstanceId.getInstance().getToken();
            /*String fb_reg = CommonUtils.getSharedPref(Constants.FIREBASE, this);
            if (fb_reg == null) {
                registerToken(token);
            }*/
        }

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(Color.WHITE);


        setSupportActionBar(toolbar);
        BottomNavigationView bottomNavigationView = (BottomNavigationView)
                findViewById(R.id.navigation);
        fragmentInit();

        bottomNavigationView.setOnNavigationItemSelectedListener
                (new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        Bundle bundle = new Bundle();
                        switch (item.getItemId()) {
                            case R.id.action_item1:
                                showToolbar();
                                showFragment(dashboardFragment, null);
                                break;
                            case R.id.action_item2:
                                showToolbar();
                                bundle.putString("FEED", "News Feed");
                                showFragment(newsFeedFragment, bundle);
                                break;
                            case R.id.action_item3:
                                    showToolbar();
                                    showFragment(usersListFragment, null);
                                break;
                        }
                        return true;
                    }
                });

        showFragment(dashboardFragment, null);
        // updateAlbumInfo();
        // updateStudentInfo();

    }
/*
    private void startNotificationService() {
        // Before we start the service, confirm that we have extra power usage privileges.
        Intent intent = new Intent();
        intent.setAction(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS);
        intent.setData(Uri.parse("package:" + getPackageName()));
        startActivity(intent);
        startService(new Intent(this, NotifyService.class));
    }*/

    @Override
    public void onBackPressed() {
        // super.onBackPressed();
        performBackForDesign();
    }

    public void performBackForDesign() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment currentFragment = fragmentManager.findFragmentById(R.id.frame_layout);
        if (dashboardFragment != null && dashboardFragment == currentFragment) {
            exitDialog();
        } else if (newsFeedFragment != null && newsFeedFragment.getClass() == currentFragment.getClass()) {
            showToolbar();
            showFragment(dashboardFragment, null);
        } else if (uploadPhotoFragment != null && uploadPhotoFragment.getClass() == currentFragment.getClass()) {
            showToolbar();
            showFragment(dashboardFragment, null);
        } else if (albumViewFragment != null && albumViewFragment.getClass() == currentFragment.getClass()) {
            showToolbar();
            showFragment(dashboardFragment, null);
        } else if (calenderViewFragment != null && calenderViewFragment.getClass() == currentFragment.getClass()) {
            showToolbar();
            showFragment(dashboardFragment, null);
        } else if (annualEventFragment != null && annualEventFragment.getClass() == currentFragment.getClass()) {
            showToolbar();
            showFragment(dashboardFragment, null);
        } else if (galleryViewFragment != null && galleryViewFragment.getClass() == currentFragment.getClass()) {
            showFragment(albumViewFragment, null);
        } else if (aboutSchoolFragment != null && aboutSchoolFragment.getClass() == currentFragment.getClass()) {
            showToolbar();
            showFragment(dashboardFragment, null);
        } else if (profileFragment != null && profileFragment.getClass() == currentFragment.getClass()) {
            showToolbar();
            showFragment(dashboardFragment, null);
        } else if (slideshowDialogFragment != null && slideshowDialogFragment.getClass() == currentFragment.getClass()) {
            Bundle bundle = new Bundle();
            bundle.putSerializable("mylist", (Serializable) getListAlbumChild());
            if (isGallery) {
                showFragment(galleryViewFragment, bundle);
            } else if (fromFragment == newsFeedFragment) {
                bundle.putString("FEED", getNewsFeedType());
                showFragment(newsFeedFragment, bundle);
            } else {
                if (studentInfoSingleton.getStudentModel() != null) {
                    showFragment(communityFragment, null);
                } else {
                    studentInfoSingleton.checkLogin();
                }
            }
        } else if (communityFragment != null && communityFragment.getClass() == currentFragment.getClass()) {
            showToolbar();
            showFragment(dashboardFragment, null);
        } else if (staffInfoFragment != null && staffInfoFragment.getClass() == currentFragment.getClass()) {
            showToolbar();
            showFragment(dashboardFragment, null);
        } else if (signUpFragment != null && signUpFragment.getClass() == currentFragment.getClass()) {
            showToolbar();
            showFragment(usersListFragment, null);
        } else if (usersListFragment != null && usersListFragment.getClass() == currentFragment.getClass()) {
            showToolbar();
            showFragment(profileFragment, null);
        } else if (rewardsPointsFragment != null && rewardsPointsFragment.getClass() == currentFragment.getClass()) {
            showToolbar();
            showFragment(dashboardFragment, null);
        } else if (rewardGraphFragment != null && rewardGraphFragment.getClass() == currentFragment.getClass()) {
            showToolbar();
            showFragment(profileFragment, null);
        } else {
            exitDialog();
        }

    }

    private void showToolbar() {
        getToolbar().setVisibility(View.VISIBLE);
    }

    public void showFragment(Fragment fragmentClass, Bundle bundle) {
        getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        fragmentClass.setArguments(bundle);
        transaction.replace(R.id.frame_layout, fragmentClass);
        transaction.commit();
    }

    private void fragmentInit() {
        albumViewFragment = new AlbumViewFragment();
        annualEventFragment = new AnnualEventFragment();
        calenderViewFragment = new CalenderViewFragment();
        dashboardFragment = new DashboardFragment();
        galleryViewFragment = new GalleryViewFragment();
        newsFeedFragment = new NewsFeedFragment();
        slideshowDialogFragment = new SlideshowDialogFragment();
        uploadPhotoFragment = new UploadPhotoFragment();
        communityFragment = new CommunityFragment();
        aboutSchoolFragment = new AboutSchoolFragment();
        profileFragment = new ProfileFragment();
        signUpFragment = new SignUpFragment();
        staffInfoFragment = new StaffInfoFragment();
        usersListFragment = new UsersListFragment();
        rewardsPointsFragment = new RewardsPointsFragment();
        rewardGraphFragment = new RewardGraphFragment();
    }

    public List<ImageRequest> getListAlbumChild() {
        return listAlbumChild;
    }

    public void setListAlbumChild(List<ImageRequest> listAlbumChild) {
        this.listAlbumChild = listAlbumChild;
    }

    public boolean isGallery() {
        return isGallery;
    }

    public void setGallery(boolean gallery) {
        isGallery = gallery;
    }

   /* @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_logout, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_logout:
                CommonUtils.removeSharePref(Constants.USER_NAME, HomeActivity.this);
                Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
                startActivity(intent);
                break;
        }
        return true;

    }*/

    private void exitFromApp() {
        super.onBackPressed();
    }

    private void exitDialog() {
        try {
            AlertDialog.Builder builder = showAlertDialog(this, getString(R.string.app_name), getString(R.string.exit_msg));
            builder.setPositiveButton(getStringById(R.string.yes), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    exitFromApp();
                }
            }).setNegativeButton(getStringById(R.string.no), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            }).show();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }

    }

    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

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

    private void stopLocationService() {
        if (isServiceRunning(TrackerService.class)) {
            stopService(new Intent(this, TrackerService.class));
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

    public void logoOut() {
        stopLocationService();
        try {
            AlertDialog.Builder builder = showAlertDialog(this, getString(R.string.logout_title), getString(R.string.logout_msg));
            builder.setPositiveButton(getStringById(R.string.yes), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    CommonUtils.removeAllPref(HomeActivity.this);
                    Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                }
            }).setNegativeButton(getStringById(R.string.no), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();

                }
            }).show();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }

    }

    public static AlertDialog.Builder showAlertDialog(Context context, String title, String msg) {
        AlertDialog.Builder alertDialog = null;
        alertDialog = new AlertDialog.Builder(context);
        alertDialog.setTitle(title);
        alertDialog.setMessage(msg);
        return alertDialog;
    }

    public void setFromFragment(Fragment fromFragment) {
        this.fromFragment = fromFragment;
    }

    public String getNewsFeedType() {
        return newsFeedType;
    }

    public void setNewsFeedType(String newsFeedType) {
        this.newsFeedType = newsFeedType;
    }

    public Toolbar getToolbar() {
        return toolbar;
    }

    public StudentInfoSingleton getStudentInfoSingleton() {
        return studentInfoSingleton;
    }


    private void registerToken(final String token) {
        String url = null;
        final ProgressDialog pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");
        pDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url + "?Token=" + token,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        CommonUtils.insertSharedPref(HomeActivity.this, Constants.FIREBASE, "FIREBASE");
                        pDialog.hide();
                        Toast.makeText(getApplicationContext(), response, Toast.LENGTH_SHORT).show();

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        pDialog.hide();
                        Toast.makeText(getApplication(), error.toString(), Toast.LENGTH_LONG).show();
                    }
                });

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                5000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }





}

