package com.exa.mydemoapp.adapter;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.exa.mydemoapp.Common.CommonUtils;
import com.exa.mydemoapp.Common.Constants;
import com.exa.mydemoapp.HomeActivity;
import com.exa.mydemoapp.R;
import com.exa.mydemoapp.SignUpFragment;
import com.exa.mydemoapp.fragment.AttendanceFragment;
import com.exa.mydemoapp.listner.AttendanceListner;
import com.exa.mydemoapp.model.StudentModel;

import java.util.Date;
import java.util.List;

/*
  Created by Swapnil Jadhav on 22/6/17.
 */

public class StudentAttendaceAdapter extends RecyclerView.Adapter<StudentAttendaceAdapter.MyViewHolder> {

    private List<StudentModel> listUser;
    private Activity context;
    private int lastCheckedPosition = -1;
    private AttendanceListner attendanceListner;

    public StudentAttendaceAdapter(List<StudentModel> listUser, Activity context, AttendanceListner attendanceListner) {
        this.listUser = listUser;
        this.context = context;
        this.attendanceListner = attendanceListner;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener

    {
        private TextView txtName;
        private TextView txtAttendanceStatus;
        private LinearLayout linearLayout;
        private CheckBox chkAttendance;

        public MyViewHolder(View view) {
            super(view);
            txtName = (TextView) view.findViewById(R.id.txt_student_name);
            txtAttendanceStatus = (TextView) view.findViewById(R.id.txt_attendance_status);
            linearLayout = (LinearLayout) view.findViewById(R.id.relativeLayout);
            chkAttendance = (CheckBox) view.findViewById(R.id.btn_checkbox);
            linearLayout.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.relativeLayout:

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


        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lastCheckedPosition = position;
                StudentModel bean = listUser.get(lastCheckedPosition);
                if (!holder.chkAttendance.isChecked()) {
                    holder.chkAttendance.setChecked(true);
                } else {
                    holder.chkAttendance.setChecked(false);
                }
                // notifyDataSetChanged();

            }
        });

        holder.chkAttendance.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                lastCheckedPosition = position;
                StudentModel bean = listUser.get(lastCheckedPosition);

                if (isChecked) {
                    holder.txtAttendanceStatus.setText("Present");
                    attendanceListner.present(bean);
                } else {
                    holder.txtAttendanceStatus.setText("Absent");
                    attendanceListner.absent(bean);
                }
                // notifyDataSetChanged();
            }
        });


    }


    public int getPosition() {
        return lastCheckedPosition;
    }

    @Override
    public int getItemCount() {
        return listUser.size();
    }

}
