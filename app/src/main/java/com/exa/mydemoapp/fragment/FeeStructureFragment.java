package com.exa.mydemoapp.fragment;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.exa.mydemoapp.Common.CommonUtils;
import com.exa.mydemoapp.Common.Constants;
import com.exa.mydemoapp.HomeActivity;
import com.exa.mydemoapp.R;
import com.exa.mydemoapp.annotation.ViewById;
import com.exa.mydemoapp.model.RewardModel;
import com.exa.mydemoapp.model.StudentModel;
import com.exa.mydemoapp.webservice.CallWebService;
import com.exa.mydemoapp.webservice.IJson;
import com.exa.mydemoapp.webservice.IUrls;
import com.exa.mydemoapp.webservice.VolleyResponseListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

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
        StudentModel studentModel = getMyActivity().getStudentModel();

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

                txtTotalFees.setText(totalFees != null ? getStringById(R.string.Rs) + " " + totalFees : getStringById(R.string.Rs) + "0");
                txtFirstInstallment.setText(studentModel.getInstallment1() != null ? getStringById(R.string.Rs) + " " + studentModel.getInstallment1() : getStringById(R.string.Rs) + "0");
                txtSecondInstallment.setText(studentModel.getInstallment2() != null ? getStringById(R.string.Rs) + " " + studentModel.getInstallment2() : getStringById(R.string.Rs) + "0");
                txtThirdInstallment.setText(studentModel.getInstallment3() != null ? getStringById(R.string.Rs) + " " + studentModel.getInstallment3() : getStringById(R.string.Rs) + "0");
                txtBalance.setText(dues > 0 ? getStringById(R.string.Rs) + dues : getStringById(R.string.Rs) + "0");
            }
        }
    }


    private HomeActivity getMyActivity() {
        return (HomeActivity) getActivity();
    }
}
