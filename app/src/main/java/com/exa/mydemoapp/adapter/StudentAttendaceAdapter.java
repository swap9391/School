package com.exa.mydemoapp.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
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
    boolean flagCheckDisable;

    public StudentAttendaceAdapter(List<StudentAttendanceDetailsModel> listUser, Activity context, AttendanceListner attendanceListner, boolean flagCheckDisable) {
        this.listUser = listUser;
        this.context = context;
        this.attendanceListner = attendanceListner;
        this.flagCheckDisable = flagCheckDisable;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener

    {
        private TextView txtName;
        private TextView txtAttendanceStatus;
        private TextView txtInOutStatus;
        private LinearLayout linearLayout;
        private ImageView chkAttendance;


        public MyViewHolder(View view) {
            super(view);
            txtName = (TextView) view.findViewById(R.id.txt_student_name);
            txtAttendanceStatus = (TextView) view.findViewById(R.id.txt_attendance_status);
            txtInOutStatus = (TextView) view.findViewById(R.id.txt_inout_status);
            linearLayout = (LinearLayout) view.findViewById(R.id.relativeLayout);
            chkAttendance = (ImageView) view.findViewById(R.id.btn_checkbox);
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

        holder.txtName.setText(selectedItem.getStudentName());

        if (!flagCheckDisable) {
            holder.chkAttendance.setVisibility(View.GONE);
        } else {
            holder.chkAttendance.setVisibility(View.VISIBLE);
        }

        if (selectedItem.getIsPresent() != null && selectedItem.getIsPresent().equals("true")) {
            holder.txtInOutStatus.setText("IN");
            holder.chkAttendance.setImageResource(R.drawable.ic_check_box);
        }
        if (selectedItem.getIsOut() != null && selectedItem.getIsOut().equals("true")) {
            holder.txtInOutStatus.setText("OUT");
        }


        if (selectedItem.isChecked() && selectedItem.getIsPresent() != null && selectedItem.getIsPresent().equals("true")) {
            holder.chkAttendance.setImageResource(R.drawable.ic_check_box);
        } else if (selectedItem.getIsPresent() != null && selectedItem.getIsPresent().equals("false")) {
            holder.chkAttendance.setImageResource(R.drawable.ic_uncheck);
        }
        if (selectedItem.isChecked() && holder.chkAttendance.getDrawable().getConstantState() == context.getResources().getDrawable(R.drawable.ic_check_box).getConstantState()) {
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
                if (holder.chkAttendance.getDrawable().getConstantState() == context.getResources().getDrawable(R.drawable.ic_uncheck).getConstantState()) {
                    holder.chkAttendance.setImageResource(R.drawable.ic_check_box);
                    holder.txtAttendanceStatus.setText("Present");
                    attendanceListner.present(selectedItem, position);
                } else {
                    holder.chkAttendance.setImageResource(R.drawable.ic_uncheck);
                    holder.txtAttendanceStatus.setText("Absent");
                    attendanceListner.absent(selectedItem, position);
                }
            }
        });

        /*holder.chkAttendance.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                lastCheckedPosition = position;
                if (isChecked) {
                    holder.txtAttendanceStatus.setText("Present");
                    attendanceListner.present(selectedItem, position);
                    listUser.get(position).setChecked(true);
                } else {
                    holder.txtAttendanceStatus.setText("Absent");
                    attendanceListner.absent(selectedItem, position);
                    listUser.get(position).setChecked(false);
                }
                // notifyDataSetChanged();
            }
        });
*/

    }


    public int getPosition() {
        return lastCheckedPosition;
    }

    @Override
    public int getItemCount() {
        return listUser.size();
    }

}
