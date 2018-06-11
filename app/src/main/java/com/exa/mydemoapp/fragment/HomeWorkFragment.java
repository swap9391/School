package com.exa.mydemoapp.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.amazonaws.mobileconnectors.s3.transferutility.TransferState;
import com.android.volley.Request;
import com.bumptech.glide.Glide;
import com.exa.mydemoapp.Common.CommonUtils;
import com.exa.mydemoapp.Common.Constants;
import com.exa.mydemoapp.Common.FloatingActionButton;
import com.exa.mydemoapp.HomeActivity;
import com.exa.mydemoapp.R;
import com.exa.mydemoapp.annotation.ViewById;
import com.exa.mydemoapp.model.AlbumImagesModel;
import com.exa.mydemoapp.model.AlbumMasterModel;
import com.exa.mydemoapp.model.DailyHomeworkModel;
import com.exa.mydemoapp.model.DropdownMasterModel;
import com.exa.mydemoapp.model.HomeWorkImageModel;
import com.exa.mydemoapp.model.StudentModel;
import com.exa.mydemoapp.model.UserModel;
import com.exa.mydemoapp.s3Upload.S3FileTransferDelegate;
import com.exa.mydemoapp.s3Upload.S3UploadActivity;
import com.exa.mydemoapp.webservice.CallWebService;
import com.exa.mydemoapp.webservice.IJson;
import com.exa.mydemoapp.webservice.IUrls;
import com.exa.mydemoapp.webservice.VolleyResponseListener;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Created by midt-078 on 12/2/18.
 */

public class HomeWorkFragment extends CommonFragment {
    View view;
    @ViewById(R.id.date_picker_event)
    private Button datePicker;
    @ViewById(R.id.txt_selected_date)
    private TextView txtSelectedDate;
    @ViewById(R.id.edt_description)
    private EditText edtDescription;

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
    @ViewById(R.id.edt_subject_name)
    private EditText txtSubject;

    DailyHomeworkModel dailyHomeworkModel;

    private List<DropdownMasterModel> listClass;
    private List<DropdownMasterModel> listDivision;
    private List<StudentModel> listStudent;

    boolean isEdit = false;
    boolean apiFlag = false;

    Uri fileView;
    List<File> imageFiles = new ArrayList<>();
    int count = 0;
    int totalImages;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.layout_add_homework, container, false);
        getMyActivity().toolbar.setTitle(getStringById(R.string.dashboard_add_homework));
        getMyActivity().init();
        initViewBinding(view);

        dailyHomeworkModel = new DailyHomeworkModel();

        if (getMyActivity().getUserModel().getUserType().equals(Constants.USER_TYPE_ADMIN)) {
            txtClassName.setVisibility(View.VISIBLE);
            spnClass.setVisibility(View.VISIBLE);
            txtDivisionSpinnerTitle.setVisibility(View.VISIBLE);
            spnDivision.setVisibility(View.VISIBLE);
        } else {
            getStudent(getMyActivity().getUserModel().getUserInfoModel().getClassName(), getMyActivity().getUserModel().getUserInfoModel().getDivisionName());
        }

        listClass = getMyActivity().getDbInvoker().getDropDownByType("CLASSTYPE");
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
                dailyHomeworkModel.setDivisionName(listDivision.get(position).getServerValue());
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
                    dailyHomeworkModel.setStudentId("All");
                    dailyHomeworkModel.setDivisionName("All");
                } else {
                    spnDivision.setVisibility(View.VISIBLE);
                    spnStudent.setVisibility(View.VISIBLE);
                    txtDivisionSpinnerTitle.setVisibility(View.VISIBLE);
                    txtStudent.setVisibility(View.VISIBLE);
                    getStudent(listClass.get(position).getServerValue(), listDivision.get(spnDivision.getSelectedItemPosition()).getServerValue());
                }
                dailyHomeworkModel.setClassName(listClass.get(position).getServerValue());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });


        datePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePicker();
            }
        });
        datePicker.setText(CommonUtils.formatDateForDisplay(Calendar.getInstance().getTime(), "dd MMM yyyy"));
        long timestamp = System.currentTimeMillis() / 1000;
        dailyHomeworkModel.setHomeworkDate(timestamp);


        LinearLayout yourframelayout = (LinearLayout) view.findViewById(R.id.btnFloat);
        FloatingActionButton fabButton = new FloatingActionButton.Builder(getMyActivity(), yourframelayout)
                .withDrawable(getResources().getDrawable(R.drawable.ic_add_white))
                .withButtonColor(Color.parseColor("#F43F68"))
                .withGravity(Gravity.BOTTOM | Gravity.RIGHT)
                .withMargins(0, 0, 2, 2)
                .create();

        fabButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bindModel();
                if (check()) {
                    picfromGallery();
                }

            }
        });
        imageFiles = new ArrayList<>();
        return view;
    }


    private void bindModel() {
        if (dailyHomeworkModel.getClassName().equals("All")) {
            dailyHomeworkModel.setStudentId("All");
            dailyHomeworkModel.setDivisionName("All");
        } else {
            if (spnStudent.getVisibility() == View.VISIBLE) {
                if (listStudent.get(spnStudent.getSelectedItemPosition()).equals("All")) {
                    dailyHomeworkModel.setStudentId("All");
                } else {
                    dailyHomeworkModel.setStudentId(listStudent.get(spnStudent.getSelectedItemPosition()).getPkeyId());
                }

            } else if (spnDivision.getVisibility() == View.VISIBLE) {
                dailyHomeworkModel.setDivisionName(listDivision.get(spnDivision.getSelectedItemPosition()).getServerValue());
            }


        }
        dailyHomeworkModel.setSubjectName(txtSubject.getText().toString().trim());
        //dailyHomeworkModel.setSubjectName(spnSubject.getText().toString().trim());
        dailyHomeworkModel.setDescription(edtDescription.getText().toString().trim());

        //homeWorkModel.setHomeworkDate(CommonUtils.formatDateForDisplay(Calendar.getInstance().getTime(), Constants.DATE_FORMAT));
    }


    private boolean check() {
        if (dailyHomeworkModel.getClassName() == null || dailyHomeworkModel.getClassName().equals("")) {
            getMyActivity().showToast(getStringById(R.string.valid_class_name));
            return false;
        }
        if (dailyHomeworkModel.getSubjectName() == null || dailyHomeworkModel.getSubjectName().equals("")) {
            getMyActivity().showToast(getStringById(R.string.valid_subject));
            return false;
        }

        if (dailyHomeworkModel.getDescription() == null || dailyHomeworkModel.getDescription().equals("")) {
            getMyActivity().showToast(getStringById(R.string.valid_description));
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
                    builder.setPositiveButton(getString(R.string.dialog_button_save), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            bindModel();
                            if (check()) {
                                if (!isEdit && imageFiles.size() > 0) {
                                    // uploadImage();
                                    uploadImages();
                                } else {
                                    save();
                                }
                            }
                        }
                    }).setNegativeButton(getString(R.string.dialog_button_cancel), new DialogInterface.OnClickListener() {
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
                    StudentModel allStudent = new StudentModel();
                    allStudent.setFullName("All");
                    allStudent.setPkeyId(null);
                    listStudent.add(allStudent);
                    for (StudentModel studentModel : object) {
                        listStudent.add(studentModel);
                    }
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

    private void save() {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put(IJson.className, dailyHomeworkModel.getClassName());
        hashMap.put(IJson.division, dailyHomeworkModel.getDivisionName());
        hashMap.put(IJson.studentId, dailyHomeworkModel.getStudentId());
        hashMap.put(IJson.subject, dailyHomeworkModel.getSubjectName());
        hashMap.put(IJson.homeworkDate, dailyHomeworkModel.getHomeworkDate());
        hashMap.put(IJson.homeworkDescription, dailyHomeworkModel.getDescription());
        List<String> listImages = new ArrayList<>();
        for (HomeWorkImageModel homeWorkImageModel : dailyHomeworkModel.getAlbumImagesModel()) {
            listImages.add(homeWorkImageModel.getImageUrl());
        }
        hashMap.put(IJson.homeworkImage, listImages);
        CallWebService.getWebserviceObject(getMyActivity(), true, true, Request.Method.POST, IUrls.URL_ADD_HOMEWORK, hashMap, new VolleyResponseListener<DailyHomeworkModel>() {
            @Override
            public void onResponse(DailyHomeworkModel[] object) {
            }

            @Override
            public void onResponse(DailyHomeworkModel studentData) {
                getMyActivity().showFragment(new DashboardFragment(), null);
            }

            @Override
            public void onResponse() {
                getMyActivity().showFragment(new DashboardFragment(), null);
            }

            @Override
            public void onError(String message) {
                Toast.makeText(getMyActivity(), message, Toast.LENGTH_SHORT).show();
            }
        }, DailyHomeworkModel.class);

    }


    private void showDatePicker() {
        DatePickerFragment date = new DatePickerFragment();
        /**
         * Set Up Current Date Into dialog
         */
        Calendar calender = Calendar.getInstance();
        Bundle args = new Bundle();
        args.putInt("year", calender.get(Calendar.YEAR));
        args.putInt("month", calender.get(Calendar.MONTH));
        args.putInt("day", calender.get(Calendar.DAY_OF_MONTH));
        date.setArguments(args);
        /**
         * Set Call back to capture selected date
         */
        date.setCallBack(ondate);
        date.show(getFragmentManager(), "Date Picker");
    }

    DatePickerDialog.OnDateSetListener ondate = new DatePickerDialog.OnDateSetListener() {

        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            monthOfYear += 1;
            String month = "" + monthOfYear;
            String day = "" + dayOfMonth;
            if (monthOfYear < 10) {
                month = "0" + monthOfYear;
            }
            if (dayOfMonth < 10) {
                day = "0" + dayOfMonth;
            }
            Date date = CommonUtils.toDate(year + "" + month + "" + day, "yyyyMMdd");
            String formatedDate = CommonUtils.formatDateForDisplay(date, Constants.ONLY_DATE_FORMAT);
            //attendanceMasterModel.setDateStamp(formatedDate);
            datePicker.setText(CommonUtils.formatDateForDisplay(date, "dd MMM yyyy"));
        }
    };

    private void startCamera() {
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "LEFT");
        values.put(MediaStore.Images.Media.DESCRIPTION, "From your Camera");
        imageUri = getMyActivity().getContentResolver().insert(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(intent, REQUEST_CAMERA);
    }

    public void uploadImages() {
        final ProgressDialog progressDialog = new ProgressDialog(getMyActivity());
        progressDialog.setTitle(getString(R.string.msg_progress_uploading));
        progressDialog.show();
        progressDialog.setCancelable(false);
        S3UploadActivity.uploadData(getMyActivity(), new S3FileTransferDelegate() {
            @Override
            public void onS3FileTransferStateChanged(int id, TransferState state, String url, Object object) {
                File file = (File) object;
                imageFiles.remove(file);
                HomeWorkImageModel homeWorkImageModel = new HomeWorkImageModel();
                homeWorkImageModel.setImageUrl(url);
                dailyHomeworkModel.getAlbumImagesModel().add(homeWorkImageModel);
                progressDialog.dismiss();
                count = imageFiles.size();
                if (count > 0) {
                    uploadImages();
                } else {
                    //saveUserInformation();
                    save();
                    /* ClearView();
                    getMyActivity().performBackForDesign();*/
                }
            }

            @Override
            public void onS3FileTransferProgressChanged(int id, String fileName, int percentage) {
                progressDialog.setTitle(getString(R.string.msg_progress_uploading) + percentage + "%    " + totalImages + "/" + imageFiles.size());
            }

            @Override
            public void onS3FileTransferError(int id, String fileName, Exception ex) {
                progressDialog.dismiss();
            }
        }, "schoolImage" + CommonUtils.formatDateForDisplay(Calendar.getInstance().getTime(), getString(R.string.date_format_joins)) + count, imageFiles.get(0));
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CAMERA) {
            if (resultCode == Activity.RESULT_OK) {
                try {
                    String imageurl = getMyActivity().getRealPathFromURI(imageUri);
                    // setVehicleImage(imageurl, requestCode);
                    Log.e("", imageurl);
                    setImage(imageurl);
                } catch (Exception e) {
                    e.printStackTrace();
                    getMyActivity().showToast(getString(R.string.msg_try_agian));
                }

            } else {
                getMyActivity().showToast(getString(R.string.msg_capture_cacel));
            }


        }

        if (requestCode == PICK_IMAGE && resultCode == getMyActivity().RESULT_OK
                && null != data) {
          /*  System.out.println("++data" + data.getClipData().getItemCount());// Get count of image here.
            System.out.println("++count" + data.getClipData().getItemCount());*/
            try {
                List<Uri> listOfUri = new ArrayList<>();

                for (int i = 0; i < data.getClipData().getItemCount(); i++) {
                    listOfUri.add(data.getClipData().getItemAt(i).getUri());
                }
                setImage(listOfUri);
            } catch (Exception e) {
                getMyActivity().showToast("Please Select Photos Option");
            }


        } else {

            Log.i("SonaSys", "resultCode: " + resultCode);
            switch (resultCode) {
                case 0:
                    Log.i("SonaSys", "User cancelled");
                    break;


            }

        }
    }

    //for camera image
    private void setImage(String uristr) {

        Bitmap bitmap = null;
        try {
            if (uristr != null) {
                bitmap = getMyActivity().getBitmap(uristr);
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                // Bitmap bt=Bitmap.createScaledBitmap(bitmap, 720, 1100, false);
                Bitmap bt = getMyActivity().BITMAP_RESIZER(bitmap, 300, 350);
                bt.compress(Bitmap.CompressFormat.PNG, 50, stream);
                byte[] vehicleImage = stream.toByteArray();
                fileView = getMyActivity().getImageUri(getMyActivity(), bt);


                //imglist.add(fileView);
                bindView(fileView, true);

            }
        } catch (Exception e) {

        }
    }

    //for gallery images
    private void setImage(List<Uri> listOfUri) {
        totalImages = listOfUri.size();
        //        Uri selectedImage1 = data.getData();
        int countOfImage = 0;
        for (Uri selectedImage : listOfUri) {
            countOfImage++;
            try {
                Bitmap bitmap = null;
                bitmap = MediaStore.Images.Media.getBitmap(getMyActivity().getContentResolver(), selectedImage);
                // ByteArrayOutputStream stream = new ByteArrayOutputStream();
                // Bitmap bt=Bitmap.createScaledBitmap(bitmap, 720, 1100, false);
                //  Bitmap bt = getMyActivity().BITMAP_RESIZER(bitmap, 300, 350);
                //  bt.compress(Bitmap.CompressFormat.PNG, 100, stream);
                // byte[] vehicleImage = stream.toByteArray();
                //fileView = getMyActivity().getImageUri(getMyActivity(), bitmap);


                File filesDir = getMyActivity().getFilesDir();
                File imageFile = new File(filesDir, "image" + CommonUtils.formatDateForDisplay(Calendar.getInstance().getTime(), getString(R.string.date_format_joins)) + countOfImage + ".JPG");

                OutputStream os;
                try {
                    os = new FileOutputStream(imageFile);
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 50, os);
                    os.flush();
                    os.close();
                } catch (Exception e) {
                    Log.e(getMyActivity().getClass().getSimpleName(), "Error writing bitmap", e);
                }

                imageFiles.add(imageFile);

                //imglist.add(fileView);
                bindView(selectedImage, false);


            } catch (Exception e) {

            }
        }
    }

    private void bindView(Uri uri, boolean flag) {
        LinearLayout layout1 = (LinearLayout) view.findViewById(R.id.lay1);

        ImageView imageView = new ImageView(getMyActivity());
        CardView cardView = new CardView(getMyActivity());

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        params.setMargins(5, 5, 5, 5);
        imageView.setLayoutParams(params);
        cardView.setLayoutParams(params);

        Glide.with(this)
                .load(uri)
                .asBitmap()
                .override(300, 300)
                .fitCenter()
                .into(imageView);
        if (flag) {
            imageView.setRotation(90);
        }
        cardView.addView(imageView);
        layout1.addView(cardView);


    }


    private HomeActivity getMyActivity() {
        return (HomeActivity) getActivity();
    }
}
