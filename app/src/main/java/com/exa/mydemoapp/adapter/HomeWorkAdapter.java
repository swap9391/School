package com.exa.mydemoapp.adapter;

import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.exa.mydemoapp.Common.CommonUtils;
import com.exa.mydemoapp.Common.Constants;
import com.exa.mydemoapp.HomeActivity;
import com.exa.mydemoapp.R;
import com.exa.mydemoapp.fragment.ViewFullImageFullViewFragment;
import com.exa.mydemoapp.model.DailyHomeworkModel;
import com.exa.mydemoapp.model.HomeWorkImageModel;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

/*
  Created by Swapnil Jadhav on 22/6/17.
 */

public class HomeWorkAdapter extends RecyclerView.Adapter<HomeWorkAdapter.MyViewHolder> {
    private List<DailyHomeworkModel> listHomeWork;
    private HomeActivity context;

    public HomeWorkAdapter(List<DailyHomeworkModel> listHomeWork, HomeActivity context) {
        this.listHomeWork = listHomeWork;
        this.context = context;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView txtStudentName;
        private TextView txtSubject;
        private TextView txtDiscription;
        private TextView txtTimeStamp;
        private ImageButton imgEdit;
        private LinearLayout linearLayout;
        private HorizontalScrollView horizontalScrollView;

        public MyViewHolder(View view) {
            super(view);

            txtStudentName = (TextView) view.findViewById(R.id.txt_student_name);
            txtSubject = (TextView) view.findViewById(R.id.txt_subject_name);
            txtDiscription = (TextView) view.findViewById(R.id.txt_description);
            txtTimeStamp = (TextView) view.findViewById(R.id.txt_time_stamp);
            linearLayout = (LinearLayout) view.findViewById(R.id.lay1);
            imgEdit = (ImageButton) view.findViewById(R.id.img_edit);
            horizontalScrollView = (HorizontalScrollView) view.findViewById((R.id.horizontalLayout));
        }

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
            }

        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_homework, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        DailyHomeworkModel selectedItem = listHomeWork.get(position);

        holder.txtSubject.setText(selectedItem.getSubjectName());
        holder.txtDiscription.setText(selectedItem.getDescription());
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(selectedItem.getHomeworkDate());
        Date date = calendar.getTime();
        holder.txtTimeStamp.setText(CommonUtils.formatDateForDisplay(date, Constants.ONLY_DATE_FORMAT));

       /* holder.imgEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putSerializable(Constants.INTENT_TYPE_USER_DATA, selectedItem);
                context.showFragment(new SignUpFragment(), bundle);
            }
        });*/
        /*if (context.getUserModel().getUserType().equals(Constants.USER_TYPE_ADMIN)) {
            holder.imgEdit.setVisibility(View.VISIBLE);
        } else {
            holder.imgEdit.setVisibility(View.GONE);
        }*/

        if (selectedItem.getAlbumImagesModel() != null && selectedItem.getAlbumImagesModel().size() > 0) {
            holder.horizontalScrollView.setVisibility(View.VISIBLE);
            holder.linearLayout.setVisibility(View.VISIBLE);
            bindView(selectedItem.getAlbumImagesModel(), holder.linearLayout);

        } else {
            holder.horizontalScrollView.setVisibility(View.GONE);
            holder.linearLayout.setVisibility(View.GONE);
        }

    }

    private void bindView(List<HomeWorkImageModel> images, LinearLayout layout1) {

        for (HomeWorkImageModel image : images) {
            if (image.getImageUrl() != null) {
                ImageView imageView = new ImageView(context);
                CardView cardView = new CardView(context);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                );
                params.setMargins(2, 2, 2, 2);
                imageView.setLayoutParams(params);
                cardView.setLayoutParams(params);

                Glide.with(context)
                        .load(image.getImageUrl())
                        .asBitmap()
                        .placeholder(R.drawable.defualt_album_icon)
                        .error(R.drawable.defualt_album_icon)
                        .override(300, 300)
                        .fitCenter()
                        .into(imageView);
            /*if (flag) {
                imageView.setRotation(90);
            }*/
                cardView.addView(imageView);
                layout1.addView(cardView);
                imageView.setOnClickListener(new ImageClick(image.getImageUrl()));

            }
        }


    }

    private class ImageClick implements View.OnClickListener {
        final String path;

        public ImageClick(String path) {
            this.path = path;
        }

        @Override
        public void onClick(View v) {
            Bundle bundle = new Bundle();
            bundle.putString(Constants.IMAGE_PATH, path);
            context.showFragment(new ViewFullImageFullViewFragment(), bundle);
        }
    }

    @Override
    public int getItemCount() {
        return listHomeWork.size();
    }

}
