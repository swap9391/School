package com.exa.mydemoapp.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.Request;
import com.exa.mydemoapp.Common.CommonUtils;
import com.exa.mydemoapp.Common.Constants;
import com.exa.mydemoapp.HomeActivity;
import com.exa.mydemoapp.R;
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
 * Created by Swapnil on 15/06/2018.
 */

public class AddFeesDetailFragment extends CommonFragment {
    View view;
    @ViewById(R.id.lbl_class_name)
    private TextView txtClassName;
    @ViewById(R.id.spinner_class)
    private Spinner spnClass;
    @ViewById(R.id.lbl_division)
    private TextView txtDivisionSpinnerTitle;
    @ViewById(R.id.spinner_division)
    private Spinner spnDivision;
    @ViewById(R.id.lbl_student)
    private TextView txtStudent;
    @ViewById(R.id.spinner_student_name)
    private Spinner spnStudent;
    @ViewById(R.id.edt_total_amount)
    private EditText etTotalAmount;
    @ViewById(R.id.lblInstallmentType)
    TextView txtInstallmentType;
    @ViewById(R.id.spinner_fees_type)
    private Spinner spnFeesType;
    private List<DropdownMasterModel> listFees;
    StudentFeesModel studentFeesModel;
    private List<DropdownMasterModel> listClass;
    private List<DropdownMasterModel> listDivision;
    private List<StudentModel> listStudent;
    boolean apiFlag = false;
    @ViewById(R.id.btn_next)
    private Button btnNext;
    List<FeesInstallmentsModel> feesInstallmentsModels;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.layout_add_fees_detail, container, false);
        getMyActivity().toolbar.setTitle(getStringById(R.string.title_add_fees));
        getMyActivity().init();
        initViewBinding(view);
        feesInstallmentsModels = new ArrayList<>();
        studentFeesModel = new StudentFeesModel();
        btnNext.setOnClickListener(new OnNextClick(feesInstallmentsModels));
        if (getMyActivity().getUserModel().getUserType().equals(Constants.USER_TYPE_ADMIN)) {
            txtClassName.setVisibility(View.VISIBLE);
            spnClass.setVisibility(View.VISIBLE);
            txtDivisionSpinnerTitle.setVisibility(View.VISIBLE);
            spnDivision.setVisibility(View.VISIBLE);
        } else {
            getStudent(getMyActivity().getUserModel().getUserInfoModel().getClassName(), getMyActivity().getUserModel().getUserInfoModel().getDivisionName());
        }

        listClass = getMyActivity().getDbInvoker().getDropDownByType("CLASSTYPE");
        listClass.remove(0);
        ArrayAdapter<DropdownMasterModel> classAdapter = new ArrayAdapter<DropdownMasterModel>(getMyActivity(), android.R.layout.simple_spinner_item, listClass);
        classAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnClass.setAdapter(classAdapter);
        classAdapter.notifyDataSetChanged();

        DropdownMasterModel allDropdownMasterModel = new DropdownMasterModel();
        allDropdownMasterModel.setDropdownValue("All");
        allDropdownMasterModel.setServerValue(null);
        listDivision = new ArrayList<>();
        listDivision.add(allDropdownMasterModel);
        listDivision = getMyActivity().getDbInvoker().getDropDownByType("DEVISION");
        ArrayAdapter<DropdownMasterModel> divisionAdapter = new ArrayAdapter<>(getMyActivity(), android.R.layout.simple_spinner_item, listDivision);
        divisionAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnDivision.setAdapter(divisionAdapter);
        divisionAdapter.notifyDataSetChanged();


        spnDivision.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (!listClass.get(spnClass.getSelectedItemPosition()).getDropdownValue().equals("All")) {
                    getStudent(listClass.get(spnClass.getSelectedItemPosition()).getServerValue(), listDivision.get(position).getServerValue());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spnClass.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (listClass.get(spnClass.getSelectedItemPosition()).getDropdownValue().equals("All")) {
                    spnDivision.setVisibility(View.GONE);
                    spnStudent.setVisibility(View.GONE);
                    txtDivisionSpinnerTitle.setVisibility(View.GONE);
                    txtStudent.setVisibility(View.GONE);
                } else {
                    spnDivision.setVisibility(View.VISIBLE);
                    spnStudent.setVisibility(View.VISIBLE);
                    txtDivisionSpinnerTitle.setVisibility(View.VISIBLE);
                    txtStudent.setVisibility(View.VISIBLE);
                    getStudent(listClass.get(position).getServerValue(), listDivision.get(spnDivision.getSelectedItemPosition()).getServerValue());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        listFees = getMyActivity().getDbInvoker().getDropDownByType("FEESTYPE");
        ArrayAdapter<DropdownMasterModel> feesAdapter = new ArrayAdapter<>(getMyActivity(), android.R.layout.simple_spinner_item, listFees);
        feesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnFeesType.setAdapter(feesAdapter);
        feesAdapter.notifyDataSetChanged();

        return view;
    }


    private void bindModel() {
        if (spnStudent.getVisibility()==View.VISIBLE){
            studentFeesModel.setStudentId(listStudent.get(spnStudent.getSelectedItemPosition()).getPkeyId());
        }
        studentFeesModel.setTotalFees(CommonUtils.asDouble(etTotalAmount.getText().toString().trim(),0.0) );
        studentFeesModel.setNoOfInstallments(listFees.get(spnFeesType.getSelectedItemPosition()).getServerValue());
    }


    private boolean check() {
        if (studentFeesModel.getStudentId() == null || studentFeesModel.getStudentId().isEmpty()) {
            getMyActivity().showToast(getStringById(R.string.valid_student_name));
            return false;
        }
        if (studentFeesModel.getTotalFees() <= 0.0) {
            getMyActivity().showToast(getStringById(R.string.valid_total_amount));
            return false;
        }
        return true;
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
                    txtStudent.setVisibility(View.VISIBLE);
                    spnStudent.setVisibility(View.VISIBLE);
                    listStudent = new ArrayList<>();
                    listStudent.addAll(Arrays.asList(object));
                    if (listStudent.size() > 0) {
                        ArrayAdapter<StudentModel> studentAdapter = new ArrayAdapter(getMyActivity(), android.R.layout.simple_spinner_item, listStudent);
                        studentAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spnStudent.setAdapter(studentAdapter);
                        studentAdapter.notifyDataSetChanged();
                    }
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
                    apiFlag = false;
                    spnStudent.setVisibility(View.GONE);
                    txtStudent.setVisibility(View.GONE);
                    if (message != null && !message.isEmpty()) {
                        getMyActivity().showToast(message);
                    }

                }
            }, StudentModel[].class);

        }
    }

    private class OnNextClick implements View.OnClickListener {
        List<FeesInstallmentsModel> feesInstallmentsModels;

        public OnNextClick(List<FeesInstallmentsModel> feesInstallmentsModels) {
            this.feesInstallmentsModels = feesInstallmentsModels;
        }

        @Override
        public void onClick(View v) {
            bindModel();
            if (check()) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("FEESMODEL", studentFeesModel);
                getMyActivity().showFragment(new AddFeesDialogFragment(), bundle);
            }
        }
    }

    private HomeActivity getMyActivity() {
        return (HomeActivity) getActivity();
    }
}
