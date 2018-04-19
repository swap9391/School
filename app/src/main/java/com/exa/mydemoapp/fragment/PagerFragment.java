package com.exa.mydemoapp.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Request;
import com.exa.mydemoapp.Common.Constants;
import com.exa.mydemoapp.Common.CustomViewPager;
import com.exa.mydemoapp.HomeActivity;
import com.exa.mydemoapp.R;
import com.exa.mydemoapp.annotation.ViewById;
import com.exa.mydemoapp.listner.UserTabListner;
import com.exa.mydemoapp.model.UserModel;
import com.exa.mydemoapp.webservice.CallWebService;
import com.exa.mydemoapp.webservice.IUrls;
import com.exa.mydemoapp.webservice.VolleyResponseListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PagerFragment extends CommonFragment {

    private View view;
    @ViewById(R.id.pager)
    public CustomViewPager viewPager;
    @ViewById(R.id.tabLayout)
    private TabLayout tabLayout;
    List<UserModel> adminList = new ArrayList<>();
    List<UserModel> studentList = new ArrayList<>();
    List<UserModel> teacherList = new ArrayList<>();
    List<UserModel> driverList = new ArrayList<>();


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.layout_pager, container, false);
        getMyActivity().toolbar.setVisibility(View.VISIBLE);
        getMyActivity().toolbar.setTitle(getStringById(R.string.title_user_list));
        initViewBinding(view);
        getUserList();
        setHasOptionsMenu(true);
        final View touchView = viewPager;
        touchView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return false;
            }
        });


        return view;
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getChildFragmentManager());
        adapter.addFragment(new AdminListFragment(adminList), "Admin");
        adapter.addFragment(new StudentListFragment(studentList), "Student");
        adapter.addFragment(new TeacherListFragment(teacherList), "Teacher");
        adapter.addFragment(new DriverListFragment(driverList), "Driver");
        viewPager.setOffscreenPageLimit(4);
        viewPager.setAdapter(adapter);


        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {


                pageSelected(position);
                /*// changing the next button text 'NEXT' / 'GOT IT'
                if (position == layouts.length - 1) {
                    // last page. make button text to GOT IT
                    btnNext.setText(getString(R.string.start));
                    btnSkip.setVisibility(View.GONE);
                } else {
                    // still pages are left
                    btnNext.setText(getString(R.string.next));
                    btnSkip.setVisibility(View.VISIBLE);
                }*/
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }

        });

    }

    public void pageSelected(int position) {

    }

    public void next() {
        int current = getItem(+1);
        //  if (current < layouts.length) {
        // move to next screen
        viewPager.setCurrentItem(current);
        /*} else {
            launchHomeScreen();
        }*/
    }

    public void setPage(int position) {
        viewPager.setCurrentItem(position);
    }

    public int getItem(int i) {
        return viewPager.getCurrentItem() + i;
    }

    public class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<Fragment>();
        private final List<String> mFragmentTitleList = new ArrayList<String>();
        private Fragment mCurrentFragment;

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
            this.enabled = true;
        }

        private boolean enabled;

        public Fragment getCurrentFragment() {

            return mCurrentFragment;
        }


        @Override
        public Fragment getItem(int position) {

            return mFragmentList.get(position);

        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {

            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);

        }

        @Override
        public void setPrimaryItem(ViewGroup container, int position, Object object) {
            // addBottomDots(getMyActivity().getPosition());
            if (getCurrentFragment() != object) {
                mCurrentFragment = ((Fragment) object);
            }
            super.setPrimaryItem(container, position, object);
        }


        @Override
        public CharSequence getPageTitle(int position) {

            return mFragmentTitleList.get(position);
        }

        public void setPagingEnabled(boolean enabled) {
            this.enabled = enabled;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        FragmentPagerAdapter fragmentPagerAdapter = (FragmentPagerAdapter) viewPager.getAdapter();
        for (int i = 0; i < fragmentPagerAdapter.getCount(); i++) {
            Fragment viewPagerFragment = fragmentPagerAdapter.getItem(i);
            if (viewPagerFragment != null) {
                viewPagerFragment.onActivityResult(requestCode, resultCode, data);
                // Do something with your Fragment
                // Check viewPagerFragment.isResumed() if you intend on interacting with any views.
            }
        }


    }

    public HomeActivity getMyActivity() {
        return (HomeActivity) getActivity();
    }

    public void getUserList() {
        HashMap<String, String> hashMap = new HashMap<>();
        // hashMap.put(IJson.password, "" + studentId);
        CallWebService.getWebservice(getMyActivity(), Request.Method.GET, IUrls.URL_USER_LIST, hashMap, new VolleyResponseListener<UserModel>() {
            @Override
            public void onResponse(UserModel[] object) {
                for (UserModel userModel : object) {

                    switch (userModel.getUserType()) {
                        case Constants.USER_TYPE_ADMIN:
                            adminList.add(userModel);
                            break;
                        case Constants.USER_TYPE_STUDENT:
                            studentList.add(userModel);
                            break;
                        case Constants.USER_TYPE_TEACHER:
                            teacherList.add(userModel);
                            break;
                        case Constants.USER_TYPE_DRIVER:
                            driverList.add(userModel);
                            break;
                    }
                }

                setupViewPager(viewPager);
            }

            @Override
            public void onResponse() {
            }

            @Override
            public void onResponse(UserModel object) {

            }

            @Override
            public void onError(String message) {
                setupViewPager(viewPager);
            }
        }, UserModel[].class);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_save:
                getMyActivity().showFragment(getMyActivity().signUpFragment, null);
                break;
        }
        return true;
    }


}
