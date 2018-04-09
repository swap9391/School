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

import com.exa.mydemoapp.Common.CommonUtils;
import com.exa.mydemoapp.Common.Constants;
import com.exa.mydemoapp.HomeActivity;
import com.exa.mydemoapp.R;
import com.exa.mydemoapp.SignUpFragment;
import com.exa.mydemoapp.annotation.ViewById;
import com.exa.mydemoapp.model.StudentModel;

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

        private TextView txtTotalFees;
        private TextView txtFirstInstallment;
        private TextView txtSecondInstallment;
        private TextView txtThirdInstallment;
        private TextView txtBalance;

        private RelativeLayout relativeLayoutFees;
        private Button btnFees;

        public MyViewHolder(View view) {
            super(view);

            txtName = (TextView) view.findViewById(R.id.txt_user_name);
            txtUserType = (TextView) view.findViewById(R.id.txt_user_type);
            txtRegNo = (TextView) view.findViewById(R.id.txt_registration_no);
            txtTimeStamp = (TextView) view.findViewById(R.id.txt_time_stamp);
            rltLayoutDesign = (RelativeLayout) view.findViewById(R.id.rl_layout_design);
            imgEdit = (ImageButton) view.findViewById(R.id.img_edit);

            txtTotalFees = (TextView) view.findViewById(R.id.txt_total_fees);
            txtFirstInstallment = (TextView) view.findViewById(R.id.txt_first_installment);
            txtSecondInstallment = (TextView) view.findViewById(R.id.txt_second_installment);
            txtThirdInstallment = (TextView) view.findViewById(R.id.txt_third_installment);
            txtBalance = (TextView) view.findViewById(R.id.txt_balance);
            relativeLayoutFees = (RelativeLayout) view.findViewById(R.id.relativeLayout_fees);
            btnFees = (Button) view.findViewById(R.id.img_fees_arrow);
            rltLayoutDesign.setOnClickListener(this);
            btnFees.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.relativeLayout_fees:

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
        Drawable downArrow = context.getResources().getDrawable(R.drawable.ic_arrow_down);
        Drawable upArrow = context.getResources().getDrawable(R.drawable.ic_arrow_up);
        setData(holder, selectedItem);
        if(selectedItem.getUserType().equals(context.getStringById(R.string.user_type_student))){
            holder.btnFees.setVisibility(View.VISIBLE);
        }else {
            holder.btnFees.setVisibility(View.GONE);
        }


        holder.btnFees.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.relativeLayoutFees.getVisibility() == View.GONE) {
                    holder.relativeLayoutFees.setVisibility(View.VISIBLE);
                    holder.btnFees.setCompoundDrawablesWithIntrinsicBounds(null, null, upArrow, null);
                } else {
                    holder.relativeLayoutFees.setVisibility(View.GONE);
                    holder.btnFees.setCompoundDrawablesWithIntrinsicBounds(null, null, downArrow, null);
                }

            }
        });

    }


    private void setData(MyViewHolder holder, StudentModel studentModel) {

        if (studentModel != null) {

            if (studentModel.getUserType().equals("STUDENT")) {
                String totalFees = studentModel.getTotalFees() + "";
                int paid = CommonUtils.asInt(studentModel.getInstallment1(), 0)
                        + CommonUtils.asInt(studentModel.getInstallment2(), 0)
                        + CommonUtils.asInt(studentModel.getInstallment3(), 0);
                String paidFees = "Paid :" + paid;
                int dues = studentModel.getTotalFees() - paid;

                String dueDate = "";
                if (studentModel.getDateInsvestment2() != null) {
                    dueDate = "\nNext payment date: " + CommonUtils.formatDateForDisplay(CommonUtils.toDate(studentModel.getDateInsvestment2(), "dd-MM-yyyy hh:mm"), "dd/MM/yyy");
                } else if (studentModel.getDateInsvestment3() != null) {
                    dueDate = "\nNext payment date: " + CommonUtils.formatDateForDisplay(CommonUtils.toDate(studentModel.getDateInsvestment3(), "dd-MM-yyyy hh:mm"), "dd/MM/yyy");
                }

                String duesPayable = "Total Dues :" + dues + " " + dueDate;

                holder.txtTotalFees.setText(totalFees != null ? context.getStringById(R.string.Rs) + " " + totalFees : context.getStringById(R.string.Rs) + "0");
                holder.txtFirstInstallment.setText(studentModel.getInstallment1() != null ? context.getStringById(R.string.Rs) + " " + studentModel.getInstallment1() : context.getStringById(R.string.Rs) + "0");
                holder.txtSecondInstallment.setText(studentModel.getInstallment2() != null ? context.getStringById(R.string.Rs) + " " + studentModel.getInstallment2() : context.getStringById(R.string.Rs) + "0");
                holder.txtThirdInstallment.setText(studentModel.getInstallment3() != null ? context.getStringById(R.string.Rs) + " " + studentModel.getInstallment3() : context.getStringById(R.string.Rs) + "0");
                holder.txtBalance.setText(dues > 0 ? context.getStringById(R.string.Rs) + dues : context.getStringById(R.string.Rs) + "0");
            }
        }
    }

    @Override
    public int getItemCount() {
        return listUser.size();
    }

}
