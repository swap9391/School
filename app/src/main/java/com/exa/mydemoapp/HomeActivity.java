package com.exa.mydemoapp;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.exa.mydemoapp.Common.CommonActivity;
import com.exa.mydemoapp.fragment.AboutSchoolFragment;
import com.exa.mydemoapp.fragment.AlbumViewFragment;
import com.exa.mydemoapp.fragment.AnnualEventFragment;
import com.exa.mydemoapp.fragment.CalenderViewFragment;
import com.exa.mydemoapp.fragment.CommunityFragment;
import com.exa.mydemoapp.fragment.DashboardFragment;
import com.exa.mydemoapp.fragment.GalleryViewFragment;
import com.exa.mydemoapp.fragment.NewsFeedFragment;
import com.exa.mydemoapp.fragment.ProfileFragment;
import com.exa.mydemoapp.fragment.SlideshowDialogFragment;
import com.exa.mydemoapp.fragment.UploadPhotoFragment;
import com.exa.mydemoapp.model.ImageRequest;

import java.io.Serializable;
import java.util.ArrayList;
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
    public CommunityFragment communityFragment;
    public AboutSchoolFragment aboutSchoolFragment;
    public ProfileFragment profileFragment;
    public List<ImageRequest> listAlbumChild = new ArrayList<ImageRequest>();
    public boolean isGallery = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_home);
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
                        Fragment selectedFragment = null;
                        Bundle bundle = new Bundle();
                        switch (item.getItemId()) {
                            case R.id.action_item1:
                                selectedFragment = new DashboardFragment();
                                break;
                            case R.id.action_item2:
                                selectedFragment = new NewsFeedFragment();
                                bundle.putString("FEED", "News Feed");
                                break;
                            case R.id.action_item3:
                                selectedFragment = new ProfileFragment();
                                break;
                        }
                        showFragment(selectedFragment, bundle);
                        return true;
                    }
                });

        showFragment(new DashboardFragment(), null);

    }

    @Override
    public void onBackPressed() {
        // super.onBackPressed();
        performBackForDesign();
    }

    public void performBackForDesign() {

        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment currentFragment = fragmentManager.findFragmentById(R.id.frame_layout);


        if (dashboardFragment != null && dashboardFragment == currentFragment) {
            super.onBackPressed();
        } else if (newsFeedFragment != null && newsFeedFragment.getClass() == currentFragment.getClass()) {
            showFragment(dashboardFragment, null);
        } else if (uploadPhotoFragment != null && uploadPhotoFragment.getClass() == currentFragment.getClass()) {
            showFragment(dashboardFragment, null);
        } else if (albumViewFragment != null && albumViewFragment.getClass() == currentFragment.getClass()) {
            showFragment(dashboardFragment, null);
        } else if (calenderViewFragment != null && calenderViewFragment.getClass() == currentFragment.getClass()) {
            showFragment(dashboardFragment, null);
        } else if (annualEventFragment != null && annualEventFragment.getClass() == currentFragment.getClass()) {
            showFragment(dashboardFragment, null);
        } else if (galleryViewFragment != null && galleryViewFragment.getClass() == currentFragment.getClass()) {
            showFragment(albumViewFragment, null);
        } else if (aboutSchoolFragment != null && aboutSchoolFragment.getClass() == currentFragment.getClass()) {
            showFragment(dashboardFragment, null);
        } else if (profileFragment != null && profileFragment.getClass() == currentFragment.getClass()) {
            showFragment(dashboardFragment, null);
        } else if (slideshowDialogFragment != null && slideshowDialogFragment.getClass() == currentFragment.getClass()) {
            Bundle bundle = new Bundle();
            bundle.putSerializable("mylist", (Serializable) getListAlbumChild());
            if (isGallery) {
                showFragment(galleryViewFragment, bundle);
            } else {
                showFragment(communityFragment, null);
            }
        } else if (communityFragment != null && communityFragment.getClass() == currentFragment.getClass()) {
            showFragment(dashboardFragment, null);
        } else {
            super.onBackPressed();
        }

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
}

