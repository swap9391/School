package com.exa.mydemoapp.adapter;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.exa.mydemoapp.Common.AppController;
import com.exa.mydemoapp.Common.CirclePageIndicator;
import com.exa.mydemoapp.Common.CommonUtils;
import com.exa.mydemoapp.Common.Constants;
import com.exa.mydemoapp.Common.PagerContainer;
import com.exa.mydemoapp.HomeActivity;
import com.exa.mydemoapp.R;
import com.exa.mydemoapp.fragment.UploadPhotoFragment;
import com.exa.mydemoapp.model.ImageRequest;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/*
  Created by Swapnil Jadhav on 22/6/17.
 */

public class NewsFeedsAdapter extends RecyclerView.Adapter<NewsFeedsAdapter.MyViewHolder> {

    private List<ImageRequest> allImages;
    private List<ImageRequest> coverImages;
    private int lastCheckedPosition = -1;
    private HomeActivity context;
    PagerContainer mContainer;
    SlidingImageAdapter slidingImageAdapter;
    ImageButton imgEdit;
    String feed;

    public NewsFeedsAdapter(List<ImageRequest> allImages, List<ImageRequest> coverImages, HomeActivity context, String feed) {
        this.allImages = allImages;
        this.coverImages = coverImages;
        this.context = context;
        this.feed = feed;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener

    {
        private TextView txtDesignTitle;
        private TextView txtDiscription;
        private TextView txtTimeStamp;
        private RelativeLayout rltLayoutDesign;
        private ImageButton imgEdit;
        private ImageButton imgDelete;
        private int currentPage;
        //slider
        private ViewPager mPager;
        CirclePageIndicator indicator;

        public MyViewHolder(View view) {
            super(view);
            txtDesignTitle = (TextView) view.findViewById(R.id.txt_title);
            txtDiscription = (TextView) view.findViewById(R.id.txt_description);
            txtTimeStamp = (TextView) view.findViewById(R.id.txt_time_stamp);
            rltLayoutDesign = (RelativeLayout) view.findViewById(R.id.rl_layout_design);
            imgEdit = (ImageButton) view.findViewById(R.id.img_edit);
            if (AppController.isAdmin(context)) {
                imgEdit.setVisibility(View.VISIBLE);
            } else {
                imgEdit.setVisibility(View.GONE);
            }
            rltLayoutDesign.setOnClickListener(this);
            mContainer = (PagerContainer) view.findViewById(R.id.pager_container);
            mPager = mContainer.getViewPager();
            mPager.setClipChildren(false);
            indicator = (CirclePageIndicator) view.
                    findViewById(R.id.indicator);


        }

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.rl_layout_design:
                   /* lastCheckedPosition = getAdapterPosition();
                    ImageRequest imageRequest = allImages.get(lastCheckedPosition);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("imageData", imageRequest);
                    context.showFragment(context.uploadPhotoFragment, bundle);*/
                    break;
            }

        }
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_news_feed, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        ImageRequest selectedItem = coverImages.get(position);


        holder.txtDesignTitle.setText(selectedItem.getPlaceName());
        holder.txtDiscription.setText(selectedItem.getDescription());
        Date date = CommonUtils.toDate(selectedItem.getDateStamp(), Constants.DATE_FORMAT);
        holder.txtTimeStamp.setText(CommonUtils.formatDateForDisplay(date, Constants.DATE_FORMAT));
        ImageRequest imageRequest = coverImages.get(position);
        final List<ImageRequest> listAlbumChild = new ArrayList<ImageRequest>();
        for (ImageRequest img : allImages) {
            if (imageRequest.getPlaceName().equals(img.getPlaceName())) {
                listAlbumChild.add(img);
            }
        }

        slidingImageAdapter = new SlidingImageAdapter(context, listAlbumChild, feed);
        holder.mPager.setAdapter(slidingImageAdapter);

        holder.indicator.setViewPager(holder.mPager);

        final float density = context.getResources().getDisplayMetrics().density;

//Set circle indicator radius
        holder.indicator.setRadius(5 * density);

        // Pager listener over indicator
        holder.indicator.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                holder.currentPage = position;

            }

            @Override
            public void onPageScrolled(int pos, float arg1, int arg2) {

            }

            @Override
            public void onPageScrollStateChanged(int pos) {

            }
        });

        holder.imgEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //  ImageRequest bean = listAlbumChild.get(holder.currentPage);
                Bundle bundle = new Bundle();
                bundle.putSerializable("imageData", (Serializable) listAlbumChild);
                context.showFragment(new UploadPhotoFragment(), bundle);
            }
        });

    }

    @Override
    public int getItemCount() {
        return coverImages.size();
    }


}
