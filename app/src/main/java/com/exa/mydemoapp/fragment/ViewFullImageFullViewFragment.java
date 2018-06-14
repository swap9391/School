package com.exa.mydemoapp.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.exa.mydemoapp.Common.Constants;
import com.exa.mydemoapp.HomeActivity;
import com.exa.mydemoapp.R;
import com.exa.mydemoapp.annotation.ViewById;

/**
 * Created by Swapnil on 14/06/2018.
 */

public class ViewFullImageFullViewFragment extends CommonFragment {


    View view;
    @ViewById(R.id.img_full_image)
    ImageView imgFullView;
    @ViewById(R.id.progress_bar)
    ProgressBar progressBar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.layout_full_image_view_frag, container, false);
        initViewBinding(view);
        Bundle bundle = getArguments();
        setHasOptionsMenu(true);

        String path = bundle.getString(Constants.IMAGE_PATH);
        progressBar.setVisibility(View.VISIBLE);
        Glide.with(getMyActivity()).load(path)
                .thumbnail(0.5f)
                .crossFade()
                .fitCenter()
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
                .into(imgFullView);


        return view;
    }

    public HomeActivity getMyActivity() {
        return (HomeActivity) getActivity();
    }

}
