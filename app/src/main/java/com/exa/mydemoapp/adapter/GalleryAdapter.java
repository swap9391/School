package com.exa.mydemoapp.adapter;

/**
 * Created by midt-006 on 4/8/17.
 */

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.exa.mydemoapp.R;
import com.exa.mydemoapp.model.ImageRequest;

import java.util.List;


/**
 * Created by Swapnil on 31/03/16.
 */

public class GalleryAdapter extends RecyclerView.Adapter<GalleryAdapter.MyViewHolder> {

    private List<ImageRequest> images;
    private Context mContext;
    private int count;
    private String fragment;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private ImageView thumbnail;
        private TextView txtImageFooter;
        private CardView cardView;
        private TextView txtName;
        private TextView txtDescription;

        public MyViewHolder(View view) {
            super(view);
            thumbnail = (ImageView) view.findViewById(R.id.thumbnail);
            txtImageFooter = (TextView) view.findViewById(R.id.txt_album_name);
            if (fragment != null && fragment.equalsIgnoreCase("Staff")) {
                cardView = (CardView) view.findViewById(R.id.layCard);
                txtName = (TextView) view.findViewById(R.id.txt_staff_name);
                txtDescription = (TextView) view.findViewById(R.id.txt_description);
            }

        }
    }


    public GalleryAdapter(Context context, List<ImageRequest> images, int count, String fragment) {
        mContext = context;
        this.images = images;
        this.count = count;
        this.fragment = fragment;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView;
        if (fragment != null && fragment.equalsIgnoreCase("Staff")) {
            itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.staff_gallery_thumbnail, parent, false);
        } else {
            itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.gallery_thumbnail, parent, false);
        }
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        ImageRequest image = images.get(position);

        if (count <= 0) {
            // holder.relativeLayoutCount.setVisibility(View.GONE);
            holder.txtImageFooter.setVisibility(View.GONE);
        } else {
            holder.txtImageFooter.setVisibility(View.VISIBLE);
            holder.txtImageFooter.setText(image.getPlaceName());
        }
        if (image.getImages().size()>0&&image.getImages().get(0).getImgUrl()!=null) {
            Glide.with(mContext).load(image.getImages().get(0).getImgUrl())
                    .thumbnail(0.5f)
                    .crossFade()
                    .override(200, 200)
                    .placeholder(R.drawable.defualt_album_icon)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(holder.thumbnail);
        }
        if (fragment != null && fragment.equalsIgnoreCase("Staff")) {
            holder.cardView.setVisibility(View.VISIBLE);
            holder.txtImageFooter.setVisibility(View.GONE);
            holder.txtName.setText(image.getPlaceName());
            holder.txtDescription.setText(image.getDescription());
        }
    }

    @Override
    public int getItemCount() {
        return images.size();
    }

    public interface ClickListener {
        void onClick(View view, int position);

        void onLongClick(View view, int position);
    }

    public static class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {

        private GestureDetector gestureDetector;
        private GalleryAdapter.ClickListener clickListener;

        public RecyclerTouchListener(Context context, final RecyclerView recyclerView, final GalleryAdapter.ClickListener clickListener) {
            this.clickListener = clickListener;
            gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }

                @Override
                public void onLongPress(MotionEvent e) {
                    View child = recyclerView.findChildViewUnder(e.getX(), e.getY());
                    if (child != null && clickListener != null) {
                        clickListener.onLongClick(child, recyclerView.getChildPosition(child));
                    }
                }
            });
        }

        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {

            View child = rv.findChildViewUnder(e.getX(), e.getY());
            if (child != null && clickListener != null && gestureDetector.onTouchEvent(e)) {
                clickListener.onClick(child, rv.getChildPosition(child));
            }
            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {
        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

        }
    }

}