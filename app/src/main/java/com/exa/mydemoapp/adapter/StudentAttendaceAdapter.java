package com.exa.mydemoapp.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.exa.mydemoapp.R;
import com.exa.mydemoapp.database.DbInvoker;
import com.exa.mydemoapp.listner.AttendanceListner;
import com.exa.mydemoapp.model.StudentAttendanceDetailsModel;

import java.util.List;

/*
  Created by Swapnil Jadhav on 22/6/17.
 */

public class StudentAttendaceAdapter extends RecyclerView.Adapter<StudentAttendaceAdapter.MyViewHolder> {

    private List<StudentAttendanceDetailsModel> listUser;
    private Activity context;
    private int lastCheckedPosition = -1;
    private AttendanceListner attendanceListner;

    public StudentAttendaceAdapter(List<StudentAttendanceDetailsModel> listUser, Activity context, AttendanceListner attendanceListner) {
        this.listUser = listUser;
        this.context = context;
        this.attendanceListner = attendanceListner;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener

    {
        private TextView txtName;
        private TextView txtAttendanceStatus;
        private TextView txtInOutStatus;
        private LinearLayout linearLayout;
        private CheckBox chkAttendance;


        public MyViewHolder(View view) {
            super(view);
            txtName = (TextView) view.findViewById(R.id.txt_student_name);
            txtAttendanceStatus = (TextView) view.findViewById(R.id.txt_attendance_status);
            txtInOutStatus = (TextView) view.findViewById(R.id.txt_inout_status);
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
        StudentAttendanceDetailsModel selectedItem = listUser.get(position);
        DbInvoker dbInvoker = new DbInvoker(context);

        holder.txtName.setText(selectedItem.getStudentName());
        // StudentAttendanceModel bean = listUser.get(lastCheckedPosition);

        if (selectedItem.isStudentIn()) {
            holder.txtInOutStatus.setText("IN");
            holder.chkAttendance.setChecked(true);
        }
        if (selectedItem.isStudentOut()) {
            holder.txtInOutStatus.setText("OUT");
        }


        if (selectedItem.isPresent()) {
            holder.chkAttendance.setChecked(true);
        } else if (!selectedItem.isPresent()) {
            holder.chkAttendance.setChecked(false);
        } else {
            holder.chkAttendance.setChecked(true);
        }
        if (holder.chkAttendance.isChecked()) {
            lastCheckedPosition = position;
            holder.txtAttendanceStatus.setText("Present");
            attendanceListner.present(selectedItem, position);
        } else {
            lastCheckedPosition = position;
            holder.txtAttendanceStatus.setText("Absent");
            attendanceListner.absent(selectedItem, position);
        }

        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lastCheckedPosition = position;
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
                if (isChecked) {
                    holder.txtAttendanceStatus.setText("Present");
                    attendanceListner.present(selectedItem, position);
                } else {
                    holder.txtAttendanceStatus.setText("Absent");
                    attendanceListner.absent(selectedItem, position);
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
