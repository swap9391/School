package com.exa.mydemoapp.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.exa.mydemoapp.Common.CommonUtils;
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
    @ViewById(R.id.txt_second_installment)
    private TextView txtSecondInstallment;
    @ViewById(R.id.txt_third_installment)
    private TextView txtThirdInstallment;
    @ViewById(R.id.txt_balance)
    private TextView txtBalance;

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
        setData();

        return view;
    }

    private void setData() {
        UserModel userModel = getMyActivity().getUserModel();

        if (userModel != null) {

            if (userModel.getUserType().equals("STUDENT")) {
                String totalFees = userModel.getStudentFeesModel().getTotalFees() + "";
                double paid = 0.0;
                for (FeesInstallmentsModel feesInstallmentsModel : userModel.getStudentFeesModel().getFeesInstallmentsModels()) {
                    paid = paid + feesInstallmentsModel.getInstallmentAmount();
                    switch (feesInstallmentsModel.getInstallmentNo()){
                        case "1":
                            txtFirstInstallment.setText(getStringById(R.string.Rs) + " " + feesInstallmentsModel.getInstallmentAmount());
                            break;
                        case "2":
                            txtSecondInstallment.setText(getStringById(R.string.Rs) + " " + feesInstallmentsModel.getInstallmentAmount());
                            break;
                        case "3":
                            txtThirdInstallment.setText(getStringById(R.string.Rs) + " " + feesInstallmentsModel.getInstallmentAmount());
                            break;
                    }

                }
                String paidFees = "Paid :" + paid;
                double dues = userModel.getStudentFeesModel().getTotalFees() - paid;

                /*String dueDate = "";
                if (userModel.getDateInsvestment2() != null) {
                    dueDate = "\nNext payment date: " + CommonUtils.formatDateForDisplay(CommonUtils.toDate(userModel.getDateInsvestment2(), "dd-MM-yyyy hh:mm"), "dd/MM/yyy");
                } else if (userModel.getDateInsvestment3() != null) {
                    dueDate = "\nNext payment date: " + CommonUtils.formatDateForDisplay(CommonUtils.toDate(userModel.getDateInsvestment3(), "dd-MM-yyyy hh:mm"), "dd/MM/yyy");
                }*/

               // String duesPayable = "Total Dues :" + dues + " " + dueDate;

                txtTotalFees.setText(totalFees != null ? getStringById(R.string.Rs) + " " + totalFees : getStringById(R.string.Rs) + "0");

                txtBalance.setText(dues > 0 ? getStringById(R.string.Rs) + dues : getStringById(R.string.Rs) + "0");
            }
        }
    }


    private HomeActivity getMyActivity() {
        return (HomeActivity) getActivity();
    }
}
