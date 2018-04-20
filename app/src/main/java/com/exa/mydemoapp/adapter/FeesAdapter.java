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
import com.exa.mydemoapp.fragment.SignUpFragment;
import com.exa.mydemoapp.model.FeesInstallmentsModel;
import com.exa.mydemoapp.model.UserModel;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

/*
  Created by Swapnil Jadhav on 22/6/17.
 */

public class FeesAdapter extends RecyclerView.Adapter<FeesAdapter.MyViewHolder> {

    private List<FeesInstallmentsModel> listFees;
    private HomeActivity context;

    public FeesAdapter(List<FeesInstallmentsModel> listFees, HomeActivity context) {
        this.listFees = listFees;
        this.context = context;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener

    {
        private TextView txtInstallmentType;
        private TextView txtInstallmentDate;
        private TextView txtInstallmentAmount;
        private TextView txtPaymentStatus;
        private TextView txtPaymentMode;
        private Button btnPay;

        public MyViewHolder(View view) {
            super(view);

            txtInstallmentType = (TextView) view.findViewById(R.id.txt_isntallment_type);
            txtInstallmentDate = (TextView) view.findViewById(R.id.txt_installment_date);
            txtInstallmentAmount = (TextView) view.findViewById(R.id.txt_installment_amount);
            txtPaymentStatus = (TextView) view.findViewById(R.id.txt_payment_status);
            txtPaymentMode = (TextView) view.findViewById(R.id.txt_payment_mode);
            btnPay = (Button) view.findViewById(R.id.btn_pay);
            btnPay.setOnClickListener(this);
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
                .inflate(R.layout.item_update_fees, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        FeesInstallmentsModel selectedItem = listFees.get(position);

        holder.txtInstallmentType.setText(selectedItem.getInstallmentNo());
        holder.txtInstallmentDate.setText(CommonUtils.formatDateForDisplay(new Date(selectedItem.getInstallmentDate()), Constants.ONLY_DATE_FORMAT));
        holder.txtInstallmentAmount.setText(context.getString(R.string.Rs) + " " + selectedItem.getInstallmentAmount());
        holder.txtPaymentMode.setText(selectedItem.getPaymentMode());
        holder.txtPaymentStatus.setText(selectedItem.isPaid() ? "Paid" : "Unpaid");
        if (!selectedItem.isPaid()) {
            holder.btnPay.setVisibility(View.VISIBLE);
            holder.btnPay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        } else {
            holder.btnPay.setVisibility(View.GONE);
        }
    }


    @Override
    public int getItemCount() {
        return listFees.size();
    }

}
