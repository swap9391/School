package com.exa.mydemoapp.adapter;

/*
 * Created by Swapnil Jadhav on 27/6/17.
 */

import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.exa.mydemoapp.Common.AppController;
import com.exa.mydemoapp.Common.CommonUtils;
import com.exa.mydemoapp.Common.Constants;
import com.exa.mydemoapp.HomeActivity;
import com.exa.mydemoapp.R;
import com.exa.mydemoapp.fragment.SlideshowDialogFragment;
import com.exa.mydemoapp.model.ImageModel;
import com.exa.mydemoapp.model.ImageRequest;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


public class SlidingImageAdapter extends PagerAdapter {


    private List<ImageModel> listImages;
    private LayoutInflater inflater;
    private HomeActivity context;
    private ProgressBar progressBar;
    private ImageButton imgDelete;
    private String feed;

    public SlidingImageAdapter(HomeActivity context, List<ImageModel> listImages, String feed) {
        this.context = context;
        this.listImages = listImages;
        inflater = LayoutInflater.from(context);
        this.feed = feed;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getCount() {
        return listImages.size();
    }

    @Override
    public Object instantiateItem(ViewGroup view, final int position) {
        View imageLayout = inflater.inflate(R.layout.sliding_images_layout, view, false);

        assert imageLayout != null;
        final ImageView imageView = (ImageView) imageLayout
                .findViewById(R.id.image);

        progressBar = (ProgressBar) imageLayout.findViewById(R.id.progress_slider);
        imgDelete = (ImageButton) imageLayout.findViewById(R.id.img_delete);
        if (!context.isGuest && AppController.isAdmin(context)) {
            imgDelete.setVisibility(View.GONE);
        } else {
            imgDelete.setVisibility(View.GONE);
        }
        imageView.setTag(R.id.image, position);
        String path = listImages.get(position).getImgUrl();
        if (position > 0) {
            progressBar.setVisibility(View.VISIBLE);
        }
        Glide.with(context).load(path)
                .thumbnail(0.5f)
                .crossFade()
                .override(400, 400)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.drawable.defualt_album_icon)
                .listener(new RequestListener<String, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                        progressBar.setVisibility(View.GONE);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        progressBar.setVisibility(View.GONE);
                        return false;
                    }
                })
                .into(imageView);
        view.addView(imageLayout, 0);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ImageRequest imageRequest = new ImageRequest();
                List<ImageRequest> imageRequestList = new ArrayList<>();
                imageRequest.setImg(listImages.get(position).getImgUrl());
                imageRequestList.add(imageRequest);
                Bundle bundle = new Bundle();
                bundle.putSerializable("images", (Serializable) imageRequestList);
                bundle.putInt("position", 0);
                bundle.putString("frag", "community");
                context.setGallery(false);
                context.setFromFragment(context.newsFeedFragment);
                context.showFragment(new SlideshowDialogFragment(), bundle);
            }
        });

        imgDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


            }
        });

        return imageLayout;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view.equals(object);
    }

    @Override
    public void restoreState(Parcelable state, ClassLoader loader) {
    }

    @Override
    public Parcelable saveState() {
        return null;
    }


}
