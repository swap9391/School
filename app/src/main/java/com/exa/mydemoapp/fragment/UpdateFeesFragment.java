package com.exa.mydemoapp.fragment;

import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.Request;
import com.exa.mydemoapp.Common.Constants;
import com.exa.mydemoapp.HomeActivity;
import com.exa.mydemoapp.R;
import com.exa.mydemoapp.adapter.FeesAdapter;
import com.exa.mydemoapp.annotation.ViewById;
import com.exa.mydemoapp.model.DropdownMasterModel;
import com.exa.mydemoapp.model.FeesInstallmentsModel;
import com.exa.mydemoapp.model.StudentFeesModel;
import com.exa.mydemoapp.model.StudentModel;
import com.exa.mydemoapp.webservice.CallWebService;
import com.exa.mydemoapp.webservice.IUrls;
import com.exa.mydemoapp.webservice.VolleyResponseListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;


/**
 * Created by midt-006 on 6/9/17.
 */

public class UpdateFeesFragment extends CommonFragment {

    public FeesAdapter mAdapter;
    @ViewById(R.id.recyclerView)
    private RecyclerView recyclerView;
    private List<DropdownMasterModel> listClass;
    private List<DropdownMasterModel> listDivision;
    private List<StudentModel> listStudent;
    @ViewById(R.id.spinner_class_name)
    private Spinner spnClass;
    @ViewById(R.id.spinner_division)
    private Spinner spnDivision;
    @ViewById(R.id.spinner_student_name)
    private Spinner spnStudent;
    @ViewById(R.id.layCard)
    private CardView cardView;
    @ViewById(R.id.txt_total_fees)
    private TextView edtTotalFees;
    @ViewById(R.id.txt_no_isntallment)
    private TextView edtNoOfFees;
    private View view;
    private List<FeesInstallmentsModel> listFees;
    boolean apiFlag = false;
    StudentFeesModel studentFeesModel;
    @ViewById(R.id.lay_admin)
    LinearLayout layAdmin;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.update_fees_structure, container, false);
        initViewBinding(view);
        setHasOptionsMenu(true);
        getMyActivity().getToolbar().setTitle("Fee Structure");
        listFees = new ArrayList<>();
        studentFeesModel = new StudentFeesModel();
        cardView.setVisibility(View.GONE);
        if (getMyActivity().getUserModel().getUserType().equalsIgnoreCase(Constants.USER_TYPE_STUDENT)) {
            getFeesForStudent();
        }


        listClass = getMyActivity().getDbInvoker().getDropDownExceptValue("CLASSTYPE", "All");
        ArrayAdapter<DropdownMasterModel> classAdapter = new ArrayAdapter<>(getMyActivity(), android.R.layout.simple_spinner_item, listClass);
        classAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnClass.setAdapter(classAdapter);
        classAdapter.notifyDataSetChanged();

        listDivision = getMyActivity().getDbInvoker().getDropDownByType("DEVISION");

        ArrayAdapter<DropdownMasterModel> divisionAdapter = new ArrayAdapter<>(getMyActivity(), android.R.layout.simple_spinner_item, listDivision);
        divisionAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnDivision.setAdapter(divisionAdapter);
        divisionAdapter.notifyDataSetChanged();

        listStudent = new ArrayList<>();
        spnDivision.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                getStudent(listClass.get(spnClass.getSelectedItemPosition()).getServerValue(), listDivision.get(position).getServerValue());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spnClass.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                getStudent(listClass.get(position).getServerValue(), listDivision.get(spnDivision.getSelectedItemPosition()).getServerValue());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        spnStudent.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                getFees(listStudent.get(spnStudent.getSelectedItemPosition()).getPkeyId());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        return view;
    }

    private void getFeesForStudent() {
        layAdmin.setVisibility(View.GONE);
        getFees(getMyActivity().getUserModel().getPkeyId());
    }

    public void getStudent(String classId, String divisionId) {
        if (!apiFlag) {
            apiFlag = true;
            HashMap<String, Object> hashMap = new HashMap<>();

            String url = String.format(IUrls.URL_CLASS_WISE_STUDENT, classId, divisionId);
            Log.d("url", url);
            CallWebService.getWebservice(getMyActivity(), Request.Method.GET, url, hashMap, new VolleyResponseListener<StudentModel>() {
                @Override
                public void onResponse(StudentModel[] object) {
                    /*for (StudentModel studentModel : object) {
                        listStudent.add(studentModel);
                    }*/
                    listStudent.addAll(Arrays.asList(object));
                    cardView.setVisibility(View.VISIBLE);
                    ArrayAdapter<StudentModel> studentAdapter = new ArrayAdapter(getMyActivity(), android.R.layout.simple_spinner_item, listStudent);
                    studentAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spnStudent.setAdapter(studentAdapter);
                    studentAdapter.notifyDataSetChanged();
                    apiFlag = false;
                }

                @Override
                public void onResponse() {
                }

                @Override
                public void onResponse(StudentModel object) {

                }

                @Override
                public void onError(String message) {
                    if (message != null && !message.isEmpty()) {
                        getMyActivity().showToast(message);
                    }
                    apiFlag = false;
                }
            }, StudentModel[].class);

        }
    }


    public void getFees(String studentId) {
        if (!apiFlag) {
            apiFlag = true;
            HashMap<String, Object> hashMap = new HashMap<>();

            String url = String.format(IUrls.URL_GET_FEES, studentId);
            Log.d("url", url);
            CallWebService.getWebserviceObject(getMyActivity(), true, true, Request.Method.GET, url, hashMap, new VolleyResponseListener<StudentFeesModel>() {
                @Override
                public void onResponse(StudentFeesModel[] object) {
                }

                @Override
                public void onResponse() {
                }

                @Override
                public void onResponse(StudentFeesModel object) {
                    apiFlag = false;
                    studentFeesModel = object;
                    cardView.setVisibility(View.VISIBLE);
                    edtTotalFees.setText("" + object.getTotalFees());
                    edtNoOfFees.setText("" + object.getNoOfInstallments());
                    initAdapter();
                }

                @Override
                public void onError(String message) {
                    if (message != null && !message.isEmpty()) {
                        getMyActivity().showToast(message);
                    }
                    apiFlag = false;
                }
            }, StudentFeesModel.class);

        }
    }


    private void initAdapter() {
        mAdapter = new FeesAdapter(studentFeesModel.getListInstallments(), getMyActivity(), studentFeesModel);
        LinearLayoutManager mLayoutManager =
                new LinearLayoutManager(getMyActivity());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_save:
                getMyActivity().showFragment(new AddFeesDetailFragment(), null);
                break;
        }
        return true;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_save_info, menu);
        menu.findItem(R.id.action_gallery).setVisible(false);
        menu.findItem(R.id.action_save).setIcon(R.drawable.ic_plus);
        if (getMyActivity().getUserModel().getUserType().equals(Constants.USER_TYPE_STUDENT)) {
            menu.findItem(R.id.action_save).setVisible(false);
        }
        super.onCreateOptionsMenu(menu, inflater);

    }

    public HomeActivity getMyActivity() {
        return (HomeActivity) getActivity();
    }

}
