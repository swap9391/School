package com.exa.mydemoapp.adapter;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.exa.mydemoapp.Common.CommonUtils;
import com.exa.mydemoapp.Common.Constants;
import com.exa.mydemoapp.HomeActivity;
import com.exa.mydemoapp.R;
import com.exa.mydemoapp.fragment.AddFeesDetailFragment;
import com.exa.mydemoapp.fragment.AddFeesDialogFragment;
import com.exa.mydemoapp.fragment.SignUpFragment;
import com.exa.mydemoapp.listner.DialogResultListner;
import com.exa.mydemoapp.model.FeesInstallmentsModel;
import com.exa.mydemoapp.model.StudentFeesModel;
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
    private StudentFeesModel studentFeesModel;

    public FeesAdapter(List<FeesInstallmentsModel> listFees, HomeActivity context, StudentFeesModel studentFeesModel) {
        this.listFees = listFees;
        this.context = context;
        this.studentFeesModel = studentFeesModel;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener

    {
        private TextView txtInstallmentType;
        private TextView txtInstallmentDate;
        private TextView txtInstallmentAmount;
        private TextView txtPaymentStatus;
        private TextView txtPaymentMode;
        private Button btnPay;
        private ImageView imgCheck;
        private ProgressBar progressBar;

        public MyViewHolder(View view) {
            super(view);

            txtInstallmentType = (TextView) view.findViewById(R.id.txt_isntallment_type);
            txtInstallmentDate = (TextView) view.findViewById(R.id.txt_installment_date);
            txtInstallmentAmount = (TextView) view.findViewById(R.id.txt_installment_amount);
            txtPaymentStatus = (TextView) view.findViewById(R.id.txt_payment_status);
            txtPaymentMode = (TextView) view.findViewById(R.id.txt_payment_mode);
            imgCheck = (ImageView) view.findViewById(R.id.img_check);
            btnPay = (Button) view.findViewById(R.id.btn_pay);
            progressBar = (ProgressBar) view.findViewById(R.id.progress_bar);
        }

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.btn_pay:
                    break;
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
        holder.txtPaymentStatus.setText(selectedItem.getPaymentStatus().equalsIgnoreCase("PAID") ? "Paid" : "Unpaid");
        if (selectedItem.getPaymentStatus().equalsIgnoreCase("PAID")) {
            holder.txtPaymentStatus.setTextColor(Color.GREEN);
        } else {
            holder.txtPaymentStatus.setTextColor(Color.RED);
        }

        if (context.getUserModel().getUserType().equals(Constants.USER_TYPE_ADMIN) && !selectedItem.getPaymentStatus().equalsIgnoreCase("PAID")) {
            holder.btnPay.setVisibility(View.VISIBLE);
            holder.btnPay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("FEESMODEL", studentFeesModel);
                    context.showFragment(new AddFeesDialogFragment(), bundle);
                }
            });
        } else {
            holder.btnPay.setVisibility(View.GONE);
        }

        if (selectedItem.getPaymentMode().equalsIgnoreCase("CHEQUE") && selectedItem.getChequeImage() != null) {
            //holder.progressBar.setVisibility(View.VISIBLE);
            Glide.with(context).load(selectedItem.getChequeImage())
                    .thumbnail(0.5f)
                    .crossFade()
                    .override(100, 100)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .placeholder(R.drawable.defualt_album_icon)
              /*      .listener(new RequestListener<String, GlideDrawable>() {
                        @Override
                        public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                            holder.progressBar.setVisibility(View.GONE);
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                            holder.progressBar.setVisibility(View.GONE);
                            return false;
                        }
                    })*/
                    .into(holder.imgCheck);
        } else {
            holder.imgCheck.setVisibility(View.GONE);
        }

    }


    @Override
    public int getItemCount() {
        return listFees.size();
    }

}
