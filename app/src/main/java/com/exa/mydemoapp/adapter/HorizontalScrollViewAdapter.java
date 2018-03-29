package com.exa.mydemoapp.adapter;

import android.net.Uri;
import android.support.v7.widget.RecyclerView;
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
import com.exa.mydemoapp.HomeActivity;
import com.exa.mydemoapp.R;

import java.util.List;

public class HorizontalScrollViewAdapter extends RecyclerView.Adapter<HorizontalScrollViewAdapter.MyViewHolder> {

    private List<Uri> listUser;
    private HomeActivity context;

    public HorizontalScrollViewAdapter(List<Uri> listUser, HomeActivity context) {
        this.listUser = listUser;
        this.context = context;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener

    {
        private ImageView image;
        private ProgressBar progressBar;


        public MyViewHolder(View view) {
            super(view);
            image = (ImageView) view.findViewById(R.id.image);
            progressBar = (ProgressBar) view.findViewById(R.id.pb_loading_drawer);
        }

        @Override
        public void onClick(View view) {
            switch (view.getId()) {

            }

        }
    }


    @Override
    public HorizontalScrollViewAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.drawer_item, parent, false);

        return new HorizontalScrollViewAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final HorizontalScrollViewAdapter.MyViewHolder holder, int position) {
        Uri selectedItem = listUser.get(position);

        holder.progressBar.setVisibility(View.VISIBLE);
        Glide.with(context).load(selectedItem)
                .thumbnail(0.5f)
                .crossFade()
                .override(400, 400)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.drawable.defualt_album_icon)
                .listener(new RequestListener<Uri, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e, Uri model, Target<GlideDrawable> target, boolean isFirstResource) {
                        holder.progressBar.setVisibility(View.GONE);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GlideDrawable resource, Uri model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        holder.progressBar.setVisibility(View.GONE);
                        return false;
                    }
                })
                .into(holder.image);

    }

    @Override
    public int getItemCount() {
        return listUser.size();
    }


}


