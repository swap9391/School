package com.exa.mydemoapp.fragment;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.exa.mydemoapp.Common.CommonUtils;
import com.exa.mydemoapp.Common.Constants;
import com.exa.mydemoapp.HomeActivity;
import com.exa.mydemoapp.R;
import com.exa.mydemoapp.annotation.ViewById;
import com.exa.mydemoapp.model.FeesInstallmentsModel;
import com.exa.mydemoapp.model.UserModel;

/**
 * Created by midt-078 on 12/2/18.
 */

public class FeeStructureFragment extends CommonFragment {
    View view;
    @ViewById(R.id.txt_total_fees)
    private TextView txtTotalFees;
    @ViewById(R.id.txt_first_installment)
    private TextView txtFirstInstallment;
    @ViewById(R.id.recyclerView)
    private RecyclerView recyclerView;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.layout_fee_structure, container, false);
        getMyActivity().toolbar.setTitle(getStringById(R.string.dashboard_fees));
        getMyActivity().init();
        initViewBinding(view);

        return view;
    }




    private HomeActivity getMyActivity() {
        return (HomeActivity) getActivity();
    }
}
