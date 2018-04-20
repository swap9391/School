package com.exa.mydemoapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.exa.mydemoapp.Common.CommonUtils;
import com.exa.mydemoapp.Common.Constants;
import com.exa.mydemoapp.HomeActivity;
import com.exa.mydemoapp.MapsActivity;
import com.exa.mydemoapp.R;
import com.exa.mydemoapp.fragment.AboutSchoolFragment;
import com.exa.mydemoapp.fragment.AlbumViewFragment;
import com.exa.mydemoapp.fragment.AnnualEventFragment;
import com.exa.mydemoapp.fragment.AttendanceFragment;
import com.exa.mydemoapp.fragment.CalenderViewFragment;
import com.exa.mydemoapp.fragment.CommunityFragment;
import com.exa.mydemoapp.fragment.ContactUsFragment;
import com.exa.mydemoapp.fragment.FeeStructureFragment;
import com.exa.mydemoapp.fragment.HomeWorkFragment;
import com.exa.mydemoapp.fragment.NewsFeedFragment;
import com.exa.mydemoapp.fragment.PagerFragment;
import com.exa.mydemoapp.fragment.RewardsPointsFragment;
import com.exa.mydemoapp.fragment.StaffInfoFragment;
import com.exa.mydemoapp.fragment.UpdateFeesFragment;
import com.exa.mydemoapp.fragment.UploadPhotoFragment;
import com.exa.mydemoapp.fragment.UsersListFragment;

/**
 * Created by midt-006 on 13/11/17.
 */
public class HomeGridAdapter extends BaseAdapter {

    String[] result;
    HomeActivity context;
    TypedArray imageId;
    private static LayoutInflater inflater = null;

    public HomeGridAdapter(HomeActivity mainActivity, String[] osNameList, TypedArray osImages) {
        // TODO Auto-generated constructor stub
        result = osNameList;
        context = mainActivity;
        imageId = osImages;
        inflater = (LayoutInflater) context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return result.length;
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    public class Holder {
        TextView os_text;
        ImageView os_img;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        Holder holder = new Holder();
        View rowView;

        rowView = inflater.inflate(R.layout.item_home_grid, null);
        holder.os_text = (TextView) rowView.findViewById(R.id.os_texts);
        holder.os_img = (ImageView) rowView.findViewById(R.id.os_images);

        holder.os_text.setText(result[position]);
        holder.os_img.setImageResource(imageId.getResourceId(position, -1));

        rowView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub


                Bundle bundle = new Bundle();
                switch (result[position]) {
                    case "About School":
                        context.showFragment(new AboutSchoolFragment(), null);
                        break;
                    case "School Facilities":
                        context.setNewsFeedType(context.getStringById(R.string.img_type_facilities));
                        bundle.putString(Constants.FEED, context.getNewsFeedType());
                        context.showFragment(new NewsFeedFragment(), bundle);
                        break;
                    case "Calendar":
                        context.showFragment(new CalenderViewFragment(), null);
                        break;
                    case "Photo Gallery":
                        context.showFragment(new AlbumViewFragment(), null);
                        break;
                    case "Achievements":
                        context.setNewsFeedType(context.getStringById(R.string.img_type_archive));
                        bundle.putString(Constants.FEED, context.getNewsFeedType());
                        context.showFragment(new NewsFeedFragment(), bundle);
                        break;
                    case "Blog":
                        context.setNewsFeedType(context.getStringById(R.string.img_type_blog));
                        bundle.putString(Constants.FEED, context.getNewsFeedType());
                        context.showFragment(new NewsFeedFragment(), bundle);
                        break;
                    case "News":
                        context.setNewsFeedType(context.getStringById(R.string.img_type_news));
                        bundle.putString(Constants.FEED, context.getNewsFeedType());
                        context.showFragment(new NewsFeedFragment(), bundle);
                        break;
                    case "Parent Chatting":
                        context.showFragment(new CommunityFragment(), null);
                        break;
                    case "Staff Information":
                        context.showFragment(new StaffInfoFragment(), null);
                        break;
                    case "Upload Photo":
                        context.showFragment(new UploadPhotoFragment(), null);
                        break;
                    case "Student Attendance":
                        context.showFragment(new AttendanceFragment(), null);
                        break;
                    case "Annual Event":
                        context.showFragment(new AnnualEventFragment(), null);
                        break;
                    case "Add Reward Points":
                        context.showFragment(new RewardsPointsFragment(), null);
                        break;
                    case "Manage Users":
                        context.showFragment(new PagerFragment(), null);
                        break;
                    case "Fees Structure":
                        String userType = CommonUtils.getSharedPref(Constants.USER_TYPE, context);
                        if (userType.equals(Constants.USER_TYPE_ADMIN)) {
                            context.showFragment(new UpdateFeesFragment(), null);
                        }else {
                            context.showFragment(new FeeStructureFragment(), null);
                        }
                        break;
                    case "Bus Location":
                        Intent intent1 = new Intent(context, MapsActivity.class);
                        context.startActivity(intent1);
                        break;
                    case "Home Work":
                        context.showFragment(new HomeWorkFragment(), null);
                        break;
                    case "Contact Us":
                        context.showFragment(new ContactUsFragment(), null);
                        break;
                }
            }
        });

        return rowView;
    }

}
