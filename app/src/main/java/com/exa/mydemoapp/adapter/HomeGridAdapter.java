package com.exa.mydemoapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.exa.mydemoapp.viewer.HomeActivity;
import com.exa.mydemoapp.MapsActivity;
import com.exa.mydemoapp.R;
import com.exa.mydemoapp.fragment.AboutSchoolFragment;
import com.exa.mydemoapp.fragment.AlbumViewFragment;
import com.exa.mydemoapp.fragment.AnnualEventFragment;
import com.exa.mydemoapp.fragment.CalenderViewFragment;
import com.exa.mydemoapp.fragment.CommunityFragment;
import com.exa.mydemoapp.fragment.NewsFeedFragment;
import com.exa.mydemoapp.fragment.UploadPhotoFragment;

/**
 * Created by midt-006 on 13/11/17.
 */
public class HomeGridAdapter extends BaseAdapter {

    String[] result;
    HomeActivity context;
    int[] imageId;
    private static LayoutInflater inflater = null;

    public HomeGridAdapter(HomeActivity mainActivity, String[] osNameList, int[] osImages) {
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
        holder.os_img.setImageResource(imageId[position]);

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
                        context.setNewsFeedType("School Facilities");
                        bundle.putString("FEED", context.getNewsFeedType());
                        context.showFragment(new NewsFeedFragment(), bundle);
                        break;
                    case "Calender":
                        context.showFragment(new CalenderViewFragment(), null);
                        break;
                    case "Image Gallery":
                        context.showFragment(new AlbumViewFragment(), null);
                        break;
                    case "Achievements":
                        context.setNewsFeedType("Achievement");
                        bundle.putString("FEED", context.getNewsFeedType());
                        context.showFragment(new NewsFeedFragment(), bundle);
                        break;
                    case "Blog":
                        context.setNewsFeedType("Blog");
                        bundle.putString("FEED", context.getNewsFeedType());
                        context.showFragment(new NewsFeedFragment(), bundle);
                        break;
                    case "News Feed":
                        context.setNewsFeedType("News Feed");
                        bundle.putString("FEED", context.getNewsFeedType());
                        context.showFragment(new NewsFeedFragment(), bundle);
                        break;
                    case "Community":
                        context.showFragment(new CommunityFragment(), null);
                        break;
                    case "Staff Information":
                        context.showToast("You Clicked " + result[position]);
                        break;
                    case "Upload Image":
                        context.showFragment(new UploadPhotoFragment(), null);
                        break;
                    case "Annual Event":
                        context.showFragment(new AnnualEventFragment(), null);
                        break;
                    case "Bus Location":
                        Intent intent1 = new Intent(context, MapsActivity.class);
                        context.startActivity(intent1);
                        context.finish();
                        break;
                }
            }
        });

        return rowView;
    }

}
