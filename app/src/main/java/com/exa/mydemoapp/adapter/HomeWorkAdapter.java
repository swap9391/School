package com.exa.mydemoapp.adapter;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.exa.mydemoapp.Common.CommonUtils;
import com.exa.mydemoapp.Common.Constants;
import com.exa.mydemoapp.HomeActivity;
import com.exa.mydemoapp.R;
import com.exa.mydemoapp.fragment.SignUpFragment;
import com.exa.mydemoapp.model.DailyHomeworkModel;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/*
  Created by Swapnil Jadhav on 22/6/17.
 */

public class HomeWorkAdapter extends RecyclerView.Adapter<HomeWorkAdapter.MyViewHolder> {

    private List<DailyHomeworkModel> listUser;
    private HomeActivity context;

    public HomeWorkAdapter(List<DailyHomeworkModel> listUser, HomeActivity context) {
        this.listUser = listUser;
        this.context = context;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener

    {
        private TextView txtStudentName;
        private TextView txtSubject ;
        private TextView txtDiscription;
        private TextView txtTimeStamp;
        private ImageButton imgEdit;

        public MyViewHolder(View view) {
            super(view);

            txtStudentName = (TextView) view.findViewById(R.id.txt_student_name);
            txtSubject = (TextView) view.findViewById(R.id.txt_subject_name);
            txtDiscription = (TextView) view.findViewById(R.id.txt_description);
            txtTimeStamp = (TextView) view.findViewById(R.id.txt_time_stamp);

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
        DailyHomeworkModel selectedItem = listUser.get(position);

        holder.txtStudentName.setText(selectedItem.getSubjectName() );
        holder.txtSubject.setText(selectedItem.getSubjectName());
        holder.txtDiscription.setText(selectedItem.getDescription());

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(selectedItem.getHomeworkDate());
        Date date = calendar.getTime();
        holder.txtTimeStamp.setText(CommonUtils.formatDateForDisplay(date, Constants.DATE_FORMAT));
       /* holder.imgEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putSerializable(Constants.INTENT_TYPE_USER_DATA, selectedItem);
                context.showFragment(new SignUpFragment(), bundle);
            }
        });*/
        if (context.getUserModel().getUserType().equals(Constants.USER_TYPE_ADMIN)) {
            holder.imgEdit.setVisibility(View.VISIBLE);
        } else {
            holder.imgEdit.setVisibility(View.GONE);
        }
       /* Glide.with(context)
                .load(selectedItem.getProfilePicUrl())
                .asBitmap()
                .override(300, 300)
                .fitCenter()
                .placeholder(R.drawable.defualt_album_icon)
                .error(R.drawable.defualt_album_icon)
                .into(holder.circleImageView);
*/
    }

    @Override
    public int getItemCount() {
        return listUser.size();
    }

}
