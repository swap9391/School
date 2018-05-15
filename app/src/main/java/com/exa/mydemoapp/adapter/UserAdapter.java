package com.exa.mydemoapp.adapter;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.exa.mydemoapp.Common.CommonUtils;
import com.exa.mydemoapp.Common.Constants;
import com.exa.mydemoapp.HomeActivity;
import com.exa.mydemoapp.R;
import com.exa.mydemoapp.annotation.ViewById;
import com.exa.mydemoapp.fragment.SignUpFragment;
import com.exa.mydemoapp.model.FeesInstallmentsModel;
import com.exa.mydemoapp.model.UserModel;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/*
  Created by Swapnil Jadhav on 22/6/17.
 */

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.MyViewHolder> {

    private List<UserModel> listUser;
    private HomeActivity context;

    public UserAdapter(List<UserModel> listUser, HomeActivity context) {
        this.listUser = listUser;
        this.context = context;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener

    {
        private TextView txtName;
        private TextView txtUserType;
        private TextView txtRegNo;
        private TextView txtQualification;
        private TextView txtSpeceiality;
        private TextView txtTimeStamp;
        private RelativeLayout rltLayoutDesign;
        private ImageButton imgEdit;
        CircleImageView circleImageView;

        public MyViewHolder(View view) {
            super(view);

            txtName = (TextView) view.findViewById(R.id.txt_user_name);
            txtUserType = (TextView) view.findViewById(R.id.txt_user_type);
            txtRegNo = (TextView) view.findViewById(R.id.txt_registration_no);
            txtTimeStamp = (TextView) view.findViewById(R.id.txt_time_stamp);
            txtQualification = (TextView) view.findViewById(R.id.txt_qualification);
            txtSpeceiality = (TextView) view.findViewById(R.id.txt_speciality);
            rltLayoutDesign = (RelativeLayout) view.findViewById(R.id.rl_layout_design);
            imgEdit = (ImageButton) view.findViewById(R.id.img_edit);
            circleImageView = (CircleImageView) view.findViewById(R.id.img_profile);
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
                .inflate(R.layout.item_user_list, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        UserModel selectedItem = listUser.get(position);

        holder.txtName.setText(selectedItem.getFirstName() + " " + selectedItem.getLastName());
        holder.txtUserType.setText("User Type :" + selectedItem.getUserType());
        holder.txtRegNo.setText("Reg.No :" + selectedItem.getUserInfoModel().getRegistrationId());
        if (selectedItem.getUserType().equals(Constants.USER_TYPE_TEACHER)) {
            holder.txtQualification.setVisibility(View.VISIBLE);
            holder.txtSpeceiality.setVisibility(View.VISIBLE);
            holder.txtQualification.setText("Qualification :" + selectedItem.getUserInfoModel().getQualification());
            holder.txtSpeceiality.setText("Speciality :" + selectedItem.getUserInfoModel().getSpeciality());
        }

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(selectedItem.getUserInfoModel().getRegistrationDate());
        Date date = calendar.getTime();
        holder.txtTimeStamp.setText(CommonUtils.formatDateForDisplay(date, Constants.DATE_FORMAT));
        holder.imgEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putSerializable(Constants.INTENT_TYPE_USER_DATA, selectedItem);
                context.showFragment(new SignUpFragment(), bundle);
            }
        });
        if (context.getUserModel().getUserType().equals(Constants.USER_TYPE_ADMIN)) {
            holder.imgEdit.setVisibility(View.VISIBLE);
        } else {
            holder.imgEdit.setVisibility(View.GONE);
        }
        Glide.with(context)
                .load(selectedItem.getProfilePicUrl())
                .asBitmap()
                .override(300, 300)
                .fitCenter()
                .placeholder(R.drawable.defualt_album_icon)
                .error(R.drawable.defualt_album_icon)
                .into(holder.circleImageView);

    }

    @Override
    public int getItemCount() {
        return listUser.size();
    }

}
