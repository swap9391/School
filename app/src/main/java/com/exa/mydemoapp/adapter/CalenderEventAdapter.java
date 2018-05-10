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

public class CalenderEventAdapter extends RecyclerView.Adapter<CalenderEventAdapter.MyViewHolder> {

    private List<String> list;
    private HomeActivity context;

    public CalenderEventAdapter(List<String> list, HomeActivity context) {
        this.list = list;
        this.context = context;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener

    {
        private TextView text1;

        public MyViewHolder(View view) {
            super(view);
            text1 = (TextView) view.findViewById(R.id.txt_event);

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
                .inflate(R.layout.item_calender_event, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        String selectedItem = list.get(position);
        holder.text1.setText(selectedItem);

    }


    @Override
    public int getItemCount() {
        return list.size();
    }

}
