package com.exa.mydemoapp.adapter;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.exa.mydemoapp.Common.CommonUtils;
import com.exa.mydemoapp.Common.Constants;
import com.exa.mydemoapp.HomeActivity;
import com.exa.mydemoapp.R;
import com.exa.mydemoapp.SignUpFragment;
import com.exa.mydemoapp.model.StudentModel;

import java.util.Date;
import java.util.List;

/*
  Created by Swapnil Jadhav on 22/6/17.
 */

public class StudentAttendaceAdapter extends RecyclerView.Adapter<StudentAttendaceAdapter.MyViewHolder> {

    private List<StudentModel> listUser;
    private HomeActivity context;

    public StudentAttendaceAdapter(List<StudentModel> listUser, HomeActivity context) {
        this.listUser = listUser;
        this.context = context;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener

    {
        private TextView txtName;
        private CheckBox chkAttend;
        private RelativeLayout relativeLayout;

        public MyViewHolder(View view) {
            super(view);
            txtName = (TextView) view.findViewById(R.id.txt_user_name);
            chkAttend = (CheckBox) view.findViewById(R.id.chk_attendance);
            relativeLayout = (RelativeLayout) view.findViewById(R.id.relativeLayout);
            relativeLayout.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.relativeLayout:
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
                .inflate(R.layout.item_attendance, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        StudentModel selectedItem = listUser.get(position);

        holder.txtName.setText(selectedItem.getStudentName());
        holder.chkAttend.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

            }
        });

    }

    @Override
    public int getItemCount() {
        return listUser.size();
    }

}
