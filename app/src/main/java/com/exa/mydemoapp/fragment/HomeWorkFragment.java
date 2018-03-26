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
import com.exa.mydemoapp.model.HomeWorkModel;
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

public class HomeWorkFragment extends CommonFragment {
    View view;
    @ViewById(R.id.spinner_class_name)
    private Spinner spnClass;
    @ViewById(R.id.edt_subject)
    private TextView edtSubject;
    @ViewById(R.id.edt_description)
    private EditText edt_description;

    List<String> listClass;
    List<String> listRewardType;
    HomeWorkModel homeWorkModel;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.layout_homework, container, false);
        getMyActivity().toolbar.setTitle(getStringById(R.string.dashboard_reward));
        getMyActivity().init();
        initViewBinding(view);

        homeWorkModel = new HomeWorkModel();


        listClass = Arrays.asList(getResources().getStringArray(R.array.class_type));
        //listClass.remove(new String("All"));
        ArrayAdapter<String> classAdapter = new ArrayAdapter<String>(getMyActivity(), android.R.layout.simple_spinner_item, listClass);
        classAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnClass.setAdapter(classAdapter);
        classAdapter.notifyDataSetChanged();


        /*spnClass.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                if (!listClass.get(position).equals("All")) {
                    listStudentClassWise = getMyActivity().getDbInvoker().getStudentListByClass(listClass.get(position));
                    if (listStudentClassWise != null && listStudentClassWise.size() > 0) {
                        listStudentClassWise = new ArrayList<>();
                        for (StudentModel bean : listStudentClassWise) {
                            listStudentName.add(bean.getStudentName() + " " + bean.getRegistrationId());
                        }
                        spinnerStudentName.setVisibility(View.VISIBLE);
                        txtStudentSpinnerTitle.setVisibility(View.VISIBLE);
                        ArrayAdapter<String> classAdapter = new ArrayAdapter<String>(getMyActivity(), android.R.layout.simple_spinner_item, listStudentName);
                        classAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinnerStudentName.setAdapter(classAdapter);
                        classAdapter.notifyDataSetChanged();
                    }
                } else {
                    spinnerStudentName.setVisibility(View.GONE);
                    txtStudentSpinnerTitle.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }
        });
*/
        return view;
    }

    private void bindModel() {
        homeWorkModel.setClassId(spnClass.getSelectedItem().toString());
        homeWorkModel.setSubject(edtSubject.getText().toString().trim());
        homeWorkModel.setDescription(edt_description.getText().toString().trim());
        homeWorkModel.setDataStamp(CommonUtils.formatDateForDisplay(Calendar.getInstance().getTime(), Constants.DATE_FORMAT));
    }


    private boolean check() {
        if (homeWorkModel.getClassId() == null || homeWorkModel.getClassId().equals("")) {
            getMyActivity().showToast("Please Select Class Name");
            return false;
        }
        if (homeWorkModel.getSubject() == null || homeWorkModel.getSubject().equals("")) {
            getMyActivity().showToast("Please Select Subject Name");
            return false;
        }

        if (homeWorkModel.getDescription() == null || homeWorkModel.getDescription().equals("")) {
            getMyActivity().showToast("Please Enter Homework Description");
            return false;
        }

        return true;
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // TODO Add your menu entries here
        inflater.inflate(R.menu.menu_save_info, menu);
        menu.findItem(R.id.action_gallery).setVisible(false);
        super.onCreateOptionsMenu(menu, inflater);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_save:
                try {
                    AlertDialog.Builder builder = getMyActivity().showAlertDialog(getMyActivity(), getString(R.string.app_name), getString(R.string.save_msg));
                    builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            bindModel();
                            if (check()) {
                                save();
                            }
                        }
                    }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();

                        }
                    }).show();
                } catch (Throwable throwable) {
                    throwable.printStackTrace();
                }

                break;
            case R.id.action_gallery:
                getMyActivity().showFragment(new NewsFeedFragment(), null);
                break;

        }
        return true;
    }


    private void save() {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put(IJson.classId, "" + homeWorkModel.getClassId());
        hashMap.put(IJson.subject, "" + homeWorkModel.getSubject());
        hashMap.put(IJson.dateStamp, "" + homeWorkModel.getDataStamp());
        hashMap.put(IJson.description, "" + homeWorkModel.getDescription());

        CallWebService.getWebserviceObject(getMyActivity(), Request.Method.POST, IUrls.URL_ADD_REWARD, hashMap, new VolleyResponseListener<RewardModel>() {
            @Override
            public void onResponse(RewardModel[] object) {
            }

            @Override
            public void onResponse(RewardModel studentData) {

            }

            @Override
            public void onError(String message) {
                Toast.makeText(getMyActivity(), message, Toast.LENGTH_SHORT).show();
            }
        }, RewardModel.class);

    }



    private HomeActivity getMyActivity() {
        return (HomeActivity) getActivity();
    }
}
