package com.exa.mydemoapp.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;

import com.exa.mydemoapp.Common.AppController;
import com.exa.mydemoapp.viewer.HomeActivity;
import com.exa.mydemoapp.R;
import com.exa.mydemoapp.adapter.HomeGridAdapter;
import com.exa.mydemoapp.annotation.ViewById;

/**
 * Created by midt-006 on 4/10/17.
 */

public class DashboardFragment extends CommonFragment {
    View view;
    @ViewById(R.id.grid_view)
    GridView gridview;
    @ViewById(R.id.image)
    ImageView imageView;

    public static String[] nameForAdmin = {
            "About School",
            "School Facilities",
            "Calender",
            "Image Gallery",
            "Achievements",
            "Blog",
            "News Feed",
            "Community",
            "Staff Information",
            "Upload Image",
            "Annual Event",
            "Bus Location"
    };
    public static int[] imagesForAdmin = {
            R.drawable.ic_about_school,
            R.drawable.ic_facility,
            R.drawable.ic_annual_calender,
            R.drawable.ic_gallery,
            R.drawable.ic_archivement,
            R.drawable.ic_blog,
            R.drawable.ic_news_feed,
            R.drawable.ic_community,
            R.drawable.ic_teacher,
            R.drawable.ic_paren_student,
            R.drawable.ic_paren_student,
            R.drawable.icon_bus_location
    };

    public static String[] nameForStudent = {
            "About School",
            "School Facilities",
            "Calender",
            "Image Gallery",
            "Achievements",
            "Blog",
            "News Feed",
            "Community",
            "Staff Information",
            "Bus Location"
    };
    public static int[] imagesForStudent = {
            R.drawable.ic_about_school,
            R.drawable.ic_facility,
            R.drawable.ic_annual_calender,
            R.drawable.ic_gallery,
            R.drawable.ic_archivement,
            R.drawable.ic_blog,
            R.drawable.ic_news_feed,
            R.drawable.ic_community,
            R.drawable.ic_teacher,
            R.drawable.icon_bus_location
    };


    public static String[] nameForGuest = {
            "About School",
            "School Facilities",
            "Calender",
            "Image Gallery",
            "Achievements",
            "Blog",
            "Staff Information"
    };
    public static int[] imagesForGuest = {
            R.drawable.ic_about_school,
            R.drawable.ic_facility,
            R.drawable.ic_annual_calender,
            R.drawable.ic_gallery,
            R.drawable.ic_archivement,
            R.drawable.ic_blog,
            R.drawable.ic_teacher
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.layout_dashboard, container, false);
        getMyActivity().toolbar.setTitle("Home");

        initViewBinding(view);
        if (getMyActivity().isGuest) {
            gridview.setAdapter(new HomeGridAdapter(getMyActivity(), nameForGuest, imagesForGuest));
        } else {
            if (AppController.isAdmin(getMyActivity())) {
                gridview.setAdapter(new HomeGridAdapter(getMyActivity(), nameForAdmin, imagesForAdmin));
            } else {
                gridview.setAdapter(new HomeGridAdapter(getMyActivity(), nameForStudent, imagesForStudent));
            }
        }


     /*   ((Button) view.findViewById(R.id.btnUpload)).setOnClickListener(this);
        ((Button) view.findViewById(R.id.btnAlbum)).setOnClickListener(this);
        ((Button) view.findViewById(R.id.btnNewsFeed)).setOnClickListener(this);
        ((Button) view.findViewById(R.id.btnCalender)).setOnClickListener(this);
        ((Button) view.findViewById(R.id.btnBlog)).setOnClickListener(this);
        ((Button) view.findViewById(R.id.btnArchivement)).setOnClickListener(this);
        ((Button) view.findViewById(R.id.btnAboutSchool)).setOnClickListener(this);
        ((Button) view.findViewById(R.id.btnFacilities)).setOnClickListener(this);
        ((Button) view.findViewById(R.id.btnCommunity)).setOnClickListener(this);
        ((Button) view.findViewById(R.id.btnStaff)).setOnClickListener(this);
        ((Button) view.findViewById(R.id.btnAnnualEvent)).setOnClickListener(this);*/
        return view;
    }


    /* @Override
     public void onClick(View view) {
         Bundle bundle = new Bundle();
         switch (view.getId()) {
             case R.id.btnUpload:
                 getMyActivity().showFragment(new UploadPhotoFragment(), null);
                 break;
             case R.id.btnAlbum:
                 getMyActivity().showFragment(new AlbumViewFragment(), null);
                 break;
             case R.id.btnNewsFeed:
                 bundle.putString("FEED", "News Feed");
                 getMyActivity().showFragment(new NewsFeedFragment(), bundle);
                 break;
             case R.id.btnArchivement:
                 bundle.putString("FEED", "Achievement");
                 getMyActivity().showFragment(new NewsFeedFragment(), bundle);
                 break;
             case R.id.btnBlog:
                 bundle.putString("FEED", "Blog");
                 getMyActivity().showFragment(new NewsFeedFragment(), bundle);
                 break;
             case R.id.btnFacilities:
                 bundle.putString("FEED", "School Facilities");
                 getMyActivity().showFragment(new NewsFeedFragment(), bundle);
                 break;
             case R.id.btnCalender:
                 getMyActivity().showFragment(new CalenderViewFragment(), null);
                 break;
             case R.id.btnAnnualEvent:
                 getMyActivity().showFragment(new AnnualEventFragment(), null);
                 break;


         }

     }*/

    public HomeActivity getMyActivity() {
        return (HomeActivity) getActivity();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_logout, menu);
        if (getMyActivity().isGuest) {
            menu.findItem(R.id.action_logout).setVisible(false);
        }
        //menu.findItem(R.id.action_gallery).setVisible(false);
        super.onCreateOptionsMenu(menu, inflater);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_logout:
                getMyActivity().logoOut();
                break;
        }
        return true;

    }
}
