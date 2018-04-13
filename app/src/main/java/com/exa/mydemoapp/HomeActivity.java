package com.exa.mydemoapp;

import android.app.ActivityManager;
import android.app.AlertDialog;
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
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.amazonaws.mobile.client.AWSMobileClient;
import com.android.volley.Request;
import com.exa.mydemoapp.Common.AppController;
import com.exa.mydemoapp.Common.CommonActivity;
import com.exa.mydemoapp.Common.CommonUtils;
import com.exa.mydemoapp.Common.Constants;
import com.exa.mydemoapp.database.Database;
import com.exa.mydemoapp.database.DbInvoker;
import com.exa.mydemoapp.fragment.AboutSchoolFragment;
import com.exa.mydemoapp.fragment.AlbumViewFragment;
import com.exa.mydemoapp.fragment.AnnualEventFragment;
import com.exa.mydemoapp.fragment.AttendanceFragment;
import com.exa.mydemoapp.fragment.CalenderViewFragment;
import com.exa.mydemoapp.fragment.CommunityFragment;
import com.exa.mydemoapp.fragment.ContactUsFragment;
import com.exa.mydemoapp.fragment.DashboardFragment;
import com.exa.mydemoapp.fragment.GalleryViewFragment;
import com.exa.mydemoapp.fragment.NewsFeedFragment;
import com.exa.mydemoapp.fragment.ProfileFragment;
import com.exa.mydemoapp.fragment.RewardGraphFragment;
import com.exa.mydemoapp.fragment.RewardsPointsFragment;
import com.exa.mydemoapp.fragment.SignUpFragment;
import com.exa.mydemoapp.fragment.SlideshowDialogFragment;
import com.exa.mydemoapp.fragment.StaffInfoFragment;
import com.exa.mydemoapp.fragment.UploadPhotoFragment;
import com.exa.mydemoapp.fragment.UsersListFragment;
import com.exa.mydemoapp.model.AlbumMasterModel;
import com.exa.mydemoapp.model.FirebaseRegistrationModel;
import com.exa.mydemoapp.model.UserModel;
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
    public AttendanceFragment attendanceFragment;
    public CommunityFragment communityFragment;
    public AboutSchoolFragment aboutSchoolFragment;
    public ProfileFragment profileFragment;
    public SignUpFragment signUpFragment;
    public StaffInfoFragment staffInfoFragment;
    public UsersListFragment usersListFragment;
    public RewardsPointsFragment rewardsPointsFragment;
    public RewardGraphFragment rewardGraphFragment;
    public ContactUsFragment contactUsFragment;
    public List<AlbumMasterModel> listAlbumChild = new ArrayList<AlbumMasterModel>();
    public boolean isGallery = true;
    public boolean isGuest = false;
    public boolean isAdmin=false;
    private Fragment fromFragment;
    private String newsFeedType;
    Database db;
    DbInvoker dbInvoker;
    public boolean flagCallUserList = false;
    UserModel userModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        db = new Database(HomeActivity.this);
        dbInvoker = new DbInvoker(this);
        AWSMobileClient.getInstance().initialize(this).execute();
        String studentId = CommonUtils.getSharedPref(Constants.STUDENT_ID, this);
        if (studentId != null) {
            userModel = dbInvoker.getStudentById(studentId);
        }

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
         /*   String subscriberId = CommonUtils.getSharedPref(Constants.USER_NAME, this);
            FirebaseMessaging.getInstance().subscribeToTopic(subscriberId);
            FirebaseInstanceId.getInstance().getToken();*/
            String token = FirebaseInstanceId.getInstance().getToken();
//            CommonUtils.insertSharedPref(HomeActivity.this, Constants.FIREBASE_REGISTER, "TRUE");
            String fb_reg = CommonUtils.getSharedPref(Constants.FIREBASE_REGISTER, this);
            if (fb_reg == null || !fb_reg.equalsIgnoreCase("TRUE")) {
              //  registerToken(token);
            } else {
                if (CommonUtils.getSharedPref(Constants.USER_TYPE, this).equals(Constants.USER_TYPE_ADMIN)) {
                 //   getUserList();
                    isAdmin=true;
                }
            }
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
                                showFragment(profileFragment, null);
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
                showFragment(communityFragment, null);
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
            showFragment(dashboardFragment, null);
        } else if (rewardsPointsFragment != null && rewardsPointsFragment.getClass() == currentFragment.getClass()) {
            showToolbar();
            showFragment(dashboardFragment, null);
        } else if (attendanceFragment != null && attendanceFragment.getClass() == currentFragment.getClass()) {
            showToolbar();
            showFragment(dashboardFragment, null);
        } else if (rewardGraphFragment != null && rewardGraphFragment.getClass() == currentFragment.getClass()) {
            showToolbar();
            showFragment(profileFragment, null);
        } else if (contactUsFragment != null && contactUsFragment.getClass() == currentFragment.getClass()) {
            showToolbar();
            showFragment(dashboardFragment, null);
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
        attendanceFragment = new AttendanceFragment();
        contactUsFragment = new ContactUsFragment();
    }

    public List<AlbumMasterModel> getListAlbumChild() {
        return listAlbumChild;
    }

    public void setListAlbumChild(List<AlbumMasterModel> listAlbumChild) {
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

    private void registerToken(final String token) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put(IJson.token, token);
        hashMap.put(IJson.userId, "" + CommonUtils.getSharedPref(Constants.STUDENT_ID, HomeActivity.this));
        CallWebService.getWebserviceObject(HomeActivity.this, Request.Method.POST, IUrls.URL_FIREBASE_REG, hashMap, new VolleyResponseListener<FirebaseRegistrationModel>() {
            @Override
            public void onResponse(FirebaseRegistrationModel[] object) {

            }

            @Override
            public void onResponse(FirebaseRegistrationModel studentData) {
                CommonUtils.insertSharedPref(HomeActivity.this, Constants.FIREBASE_REGISTER, "TRUE");
                if (isAdmin) {
                    getUserList();
                }
            }

            @Override
            public void onError(String message) {
                Toast.makeText(HomeActivity.this, message, Toast.LENGTH_SHORT).show();
            }
        }, FirebaseRegistrationModel.class);
    }


    public void getUserList() {
        HashMap<String, String> hashMap = new HashMap<>();
        // hashMap.put(IJson.password, "" + studentId);
        CallWebService.getWebservice(HomeActivity.this, Request.Method.POST, IUrls.URL_USER_LIST, hashMap, new VolleyResponseListener<UserModel>() {
            @Override
            public void onResponse(UserModel[] object) {
                dbInvoker.deleteStudents();
                for (UserModel userModel : object) {
                    dbInvoker.insertUpdateUser(userModel);
                }
                /*if (object[0] instanceof UserModel) {
                 for (S)
                }*/

            }

            @Override
            public void onResponse(UserModel object) {

            }

            @Override
            public void onError(String message) {
            }
        }, UserModel[].class);
    }

    public UserModel getUserModel() {
        return userModel;
    }

    public void setUserModel(UserModel userModel) {
        this.userModel = userModel;
    }

    public DbInvoker getDbInvoker() {
        return dbInvoker;
    }

    public void setDbInvoker(DbInvoker dbInvoker) {
        this.dbInvoker = dbInvoker;
    }
}

