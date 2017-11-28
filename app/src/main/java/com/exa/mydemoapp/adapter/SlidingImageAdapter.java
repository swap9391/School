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
import com.exa.mydemoapp.Common.CommonUtils;
import com.exa.mydemoapp.Common.Constants;
import com.exa.mydemoapp.HomeActivity;
import com.exa.mydemoapp.R;
import com.exa.mydemoapp.model.ImageRequest;

import java.util.List;


public class SlidingImageAdapter extends PagerAdapter {


    private List<ImageRequest> listImages;
    private LayoutInflater inflater;
    private HomeActivity context;
    private ProgressBar progressBar;
    private ImageButton imgDelete;
    private String feed;

    public SlidingImageAdapter(HomeActivity context, List<ImageRequest> listImages, String feed) {
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
        imageView.setTag(R.id.image, position);
        String path = listImages.get(position).getImg();
        if (position > 0) {
            progressBar.setVisibility(View.VISIBLE);
        }
        Glide.with(context).load(path)
                .thumbnail(0.5f)
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
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
        imgDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final ImageRequest imageRequest = listImages.get(position);
                imageRequest.setVisiblity("FALSE");
                context.databaseReference.child(Constants.MAIN_TABLE).child(Constants.IMAGE_TABLE).child(imageRequest.getUniqKey()).setValue(imageRequest);
                CommonUtils.showToast(context, "Photo deleted successfully!");
                Bundle bundle = new Bundle();
                bundle.putString("FEED", feed);
                context.showFragment(context.newsFeedFragment, bundle);
                /* Query query = context.databaseReference.child(Constants.MAIN_TABLE).child(Constants.IMAGE_TABLE).child(imageRequest.getUniqKey());
                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                            CommonUtils.showToast(context, "Photo deleted successfully!");
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });*/
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
