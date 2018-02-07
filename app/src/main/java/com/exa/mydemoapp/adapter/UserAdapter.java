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
import com.exa.mydemoapp.SignUpFragment;
import com.exa.mydemoapp.fragment.UploadPhotoFragment;
import com.exa.mydemoapp.model.ImageRequest;
import com.exa.mydemoapp.model.StudentModel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/*
  Created by Swapnil Jadhav on 22/6/17.
 */

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.MyViewHolder> {

    private List<StudentModel> listUser;
    private HomeActivity context;

    public UserAdapter(List<StudentModel> listUser, HomeActivity context) {
        this.listUser = listUser;
        this.context = context;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener

    {
        private TextView txtName;
        private TextView txtUserType;
        private TextView txtRegNo;
        private TextView txtTimeStamp;
        private RelativeLayout rltLayoutDesign;
        private ImageButton imgEdit;

        public MyViewHolder(View view) {
            super(view);
            txtName = (TextView) view.findViewById(R.id.txt_user_name);
            txtUserType = (TextView) view.findViewById(R.id.txt_user_type);
            txtRegNo = (TextView) view.findViewById(R.id.txt_registration_no);
            txtTimeStamp = (TextView) view.findViewById(R.id.txt_time_stamp);
            rltLayoutDesign = (RelativeLayout) view.findViewById(R.id.rl_layout_design);
            imgEdit = (ImageButton) view.findViewById(R.id.img_edit);
            rltLayoutDesign.setOnClickListener(this);
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
                .inflate(R.layout.item_user_list, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        StudentModel selectedItem = listUser.get(position);

        holder.txtName.setText(selectedItem.getStudentName());
        holder.txtUserType.setText("User Type :" + selectedItem.getUserType());
        holder.txtRegNo.setText("Reg.No :" + selectedItem.getRegistrationId());
        Date date = CommonUtils.toDate(selectedItem.getDateStamp(), Constants.DATE_FORMAT);
        holder.txtTimeStamp.setText(CommonUtils.formatDateForDisplay(date, Constants.DATE_FORMAT));
        holder.imgEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //  ImageRequest bean = listAlbumChild.get(holder.currentPage);
                Bundle bundle = new Bundle();
                bundle.putSerializable("studentData", selectedItem);
                context.showFragment(new SignUpFragment(), bundle);
            }
        });

    }

    @Override
    public int getItemCount() {
        return listUser.size();
    }

}
