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
import com.exa.mydemoapp.fragment.AnnualEventFragment;
import com.exa.mydemoapp.fragment.SignUpFragment;
import com.exa.mydemoapp.model.AnnualCalenderMasterModel;
import com.exa.mydemoapp.model.FeesInstallmentsModel;
import com.exa.mydemoapp.model.UserModel;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

/*
  Created by Swapnil Jadhav on 22/6/17.
 */

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.MyViewHolder> {

    private List<AnnualCalenderMasterModel> listEvent;
    private HomeActivity context;

    public EventAdapter(List<AnnualCalenderMasterModel> listEvent, HomeActivity context) {
        this.listEvent = listEvent;
        this.context = context;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener

    {
        private TextView txtEventName;
        private TextView txtEventType;
        private TextView txtEventDate;
        private TextView txtClassName;
        private TextView txtDivision;
        private ImageButton imgEdit;

        public MyViewHolder(View view) {
            super(view);

            txtEventName = (TextView) view.findViewById(R.id.txt_event_name);
            txtEventType = (TextView) view.findViewById(R.id.txt_event_type);
            txtEventDate = (TextView) view.findViewById(R.id.txt_event_date);
            txtClassName = (TextView) view.findViewById(R.id.txt_class_name);
            txtDivision = (TextView) view.findViewById(R.id.txt_division);
            imgEdit = (ImageButton) view.findViewById(R.id.img_edit);

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
                .inflate(R.layout.item_event_list, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        AnnualCalenderMasterModel selectedItem = listEvent.get(position);

        holder.txtEventName.setText(selectedItem.getEventName());
        holder.txtEventType.setText(selectedItem.getEventType());
        holder.txtClassName.setText("Class Name:"+selectedItem.getClassName());
        holder.txtDivision.setText(selectedItem.getDivisionName() != null ?"Division:"+ selectedItem.getDivisionName() : "");
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(selectedItem.getEventDate());
        Date date = calendar.getTime();
        holder.txtEventDate.setText(CommonUtils.formatDateForDisplay(date, Constants.DATE_FORMAT));
        holder.imgEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //  AlbumMasterModel bean = listAlbumChild.get(holder.currentPage);
                Bundle bundle = new Bundle();
                bundle.putSerializable(Constants.INTENT_TYPE_EVENT_DATA, selectedItem);
                context.showFragment(new AnnualEventFragment(), bundle);
            }
        });


    }


    @Override
    public int getItemCount() {
        return listEvent.size();
    }

}
