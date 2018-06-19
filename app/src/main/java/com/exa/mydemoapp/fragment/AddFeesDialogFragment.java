package com.exa.mydemoapp.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
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
import com.exa.mydemoapp.HomeActivity;
import com.exa.mydemoapp.R;
import com.exa.mydemoapp.annotation.ViewById;
import com.exa.mydemoapp.listner.DialogResultListner;
import com.exa.mydemoapp.model.AlbumImagesModel;
import com.exa.mydemoapp.model.AlbumMasterModel;
import com.exa.mydemoapp.model.DailyHomeworkModel;
import com.exa.mydemoapp.model.DropdownMasterModel;
import com.exa.mydemoapp.model.FeesInstallmentsModel;
import com.exa.mydemoapp.model.StudentFeesModel;
import com.exa.mydemoapp.s3Upload.S3FileTransferDelegate;
import com.exa.mydemoapp.s3Upload.S3UploadActivity;
import com.exa.mydemoapp.webservice.CallWebService;
import com.exa.mydemoapp.webservice.IJson;
import com.exa.mydemoapp.webservice.IUrls;
import com.exa.mydemoapp.webservice.VolleyResponseListener;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;

/**
 * Created by midt-078 on 11/4/18.
 */

public class AddFeesDialogFragment extends CommonFragment implements View.OnClickListener {

    @ViewById(R.id.lblInstallmentType1)
    TextView txtInstallmentType1;
    @ViewById(R.id.lblPaymentMode1)
    TextView txtPaymentMode1;
    @ViewById(R.id.lblChequeImg1)
    TextView txtChequeImage1;
    @ViewById(R.id.spinner_payment_mode1)
    private Spinner spnPaymentMode1;
    @ViewById(R.id.edt_installment_amount1)
    private EditText edtInstallmentAmount1;
    @ViewById(R.id.date_picker_installment1)
    private Button datePickerInvest1;
    @ViewById(R.id.edt_cheque_bank1)
    private EditText edtBankName1;
    @ViewById(R.id.edt_cheque_number1)
    private EditText edtChequeNumber1;
    @ViewById(R.id.img_cheque1)
    private ImageView imgCheque1;
    @ViewById(R.id.chk_paid1)
    private CheckBox chkPaid1;


    @ViewById(R.id.lblInstallmentType2)
    TextView txtInstallmentType2;
    @ViewById(R.id.lblPaymentMode2)
    TextView txtPaymentMode2;
    @ViewById(R.id.lblChequeImg2)
    TextView txtChequeImage2;
    @ViewById(R.id.spinner_payment_mode2)
    private Spinner spnPaymentMode2;
    @ViewById(R.id.edt_installment_amount2)
    private EditText edtInstallmentAmount2;
    @ViewById(R.id.date_picker_installment2)
    private Button datePickerInvest2;
    @ViewById(R.id.edt_cheque_bank2)
    private EditText edtBankName2;
    @ViewById(R.id.edt_cheque_number2)
    private EditText edtChequeNumber2;
    @ViewById(R.id.img_cheque2)
    private ImageView imgCheque2;
    @ViewById(R.id.chk_paid2)
    private CheckBox chkPaid2;


    @ViewById(R.id.lblInstallmentType3)
    TextView txtInstallmentType3;
    @ViewById(R.id.lblPaymentMode3)
    TextView txtPaymentMode3;
    @ViewById(R.id.lblChequeImg3)
    TextView txtChequeImage3;
    @ViewById(R.id.spinner_payment_mode3)
    private Spinner spnPaymentMode3;
    @ViewById(R.id.edt_installment_amount3)
    private EditText edtInstallmentAmount3;
    @ViewById(R.id.date_picker_installment3)
    private Button datePickerInvest3;
    @ViewById(R.id.edt_cheque_bank3)
    private EditText edtBankName3;
    @ViewById(R.id.edt_cheque_number3)
    private EditText edtChequeNumber3;
    @ViewById(R.id.img_cheque3)
    private ImageView imgCheque3;
    @ViewById(R.id.chk_paid3)
    private CheckBox chkPaid3;

    private List<DropdownMasterModel> listFees;
    private List<DropdownMasterModel> listPaymentMode;
    HomeActivity activity;
    FeesInstallmentsModel feesInstallmentsModel1;
    FeesInstallmentsModel feesInstallmentsModel2;
    FeesInstallmentsModel feesInstallmentsModel3;
    StudentFeesModel studentFeesModel;
    Uri imageUri1, imageUri2, imageUri3;
    Uri fileView;
    File chequeFile1, chequeFile2, chequeFile3;
    private int REQUEST_CAMERA1 = 101, REQUEST_CAMERA2 = 102, REQUEST_CAMERA3 = 103;
    List<FeesInstallmentsModel> list;
    boolean isEdit = false;

    View view;

    public AddFeesDialogFragment() {
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.layout_add_fees, container, false);
        getMyActivity().toolbar.setTitle(getStringById(R.string.title_add_fees));
        getMyActivity().init();
        initViewBinding(view);
        list = new ArrayList<>();
        feesInstallmentsModel1 = new FeesInstallmentsModel();
        feesInstallmentsModel2 = new FeesInstallmentsModel();
        feesInstallmentsModel3 = new FeesInstallmentsModel();
        studentFeesModel = new StudentFeesModel();
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            studentFeesModel = (StudentFeesModel) bundle.getSerializable("FEESMODEL");
        }

        listPaymentMode = getMyActivity().getDbInvoker().getDropDownByType("PAYMENTMODE");
        ArrayAdapter<DropdownMasterModel> paymentModeAdapter = new ArrayAdapter<>(getMyActivity(), android.R.layout.simple_spinner_item, listPaymentMode);
        paymentModeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnPaymentMode1.setAdapter(paymentModeAdapter);
        paymentModeAdapter.notifyDataSetChanged();
        spnPaymentMode1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                if (listPaymentMode.get(position).getDropdownValue().equals("CHEQUE")) {
                    edtBankName1.setVisibility(View.VISIBLE);
                    edtChequeNumber1.setVisibility(View.VISIBLE);
                    txtChequeImage1.setVisibility(View.VISIBLE);
                    imgCheque1.setVisibility(View.VISIBLE);
                } else {
                    edtBankName1.setVisibility(View.GONE);
                    edtChequeNumber1.setVisibility(View.GONE);
                    txtChequeImage1.setVisibility(View.GONE);
                    imgCheque1.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }
        });
        imgCheque1.setOnClickListener(this);
        datePickerInvest1.setOnClickListener(this);

        //installment 2

        datePickerInvest2.setVisibility(View.VISIBLE);

        ArrayAdapter<DropdownMasterModel> paymentModeAdapter2 = new ArrayAdapter<>(getMyActivity(), android.R.layout.simple_spinner_item, listPaymentMode);
        paymentModeAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnPaymentMode2.setAdapter(paymentModeAdapter2);
        paymentModeAdapter2.notifyDataSetChanged();
        spnPaymentMode2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                if (listPaymentMode.get(position).getDropdownValue().equals("CHEQUE")) {
                    edtBankName2.setVisibility(View.VISIBLE);
                    edtChequeNumber2.setVisibility(View.VISIBLE);
                    txtChequeImage2.setVisibility(View.VISIBLE);
                    imgCheque2.setVisibility(View.VISIBLE);
                } else {
                    edtBankName2.setVisibility(View.GONE);
                    edtChequeNumber2.setVisibility(View.GONE);
                    txtChequeImage2.setVisibility(View.GONE);
                    imgCheque2.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }
        });
        imgCheque2.setOnClickListener(this);
        datePickerInvest2.setOnClickListener(this);

        //Installment 3
        datePickerInvest3.setVisibility(View.VISIBLE);

        ArrayAdapter<DropdownMasterModel> paymentModeAdapter3 = new ArrayAdapter<>(getMyActivity(), android.R.layout.simple_spinner_item, listPaymentMode);
        paymentModeAdapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnPaymentMode3.setAdapter(paymentModeAdapter3);
        paymentModeAdapter3.notifyDataSetChanged();
        spnPaymentMode3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                if (listPaymentMode.get(position).getDropdownValue().equals("CHEQUE")) {
                    edtBankName3.setVisibility(View.VISIBLE);
                    edtChequeNumber3.setVisibility(View.VISIBLE);
                    txtChequeImage3.setVisibility(View.VISIBLE);
                    imgCheque3.setVisibility(View.VISIBLE);
                } else {
                    edtBankName3.setVisibility(View.GONE);
                    edtChequeNumber3.setVisibility(View.GONE);
                    txtChequeImage3.setVisibility(View.GONE);
                    imgCheque3.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }
        });
        imgCheque3.setOnClickListener(this);
        datePickerInvest3.setOnClickListener(this);


        if (studentFeesModel.getNoOfInstallments().equals("1")) {
            hideInstallment2();
            hideInstallment3();
        } else if (studentFeesModel.getNoOfInstallments().equals("2")) {
            hideInstallment3();
        }

        return view;
    }



    /*private void bindView(FeesInstallmentsModel feesInstallmentsModel) {
        txtPaymentMode.setText("Payment Mode :" + feesInstallmentsModel.getPaymentMode());
        if (feesInstallmentsModel.getInstallmentNo().equals("Cheque")) {
            edtBankName.setText(feesInstallmentsModel.getChequeBankName());
            edtChequeNumber.setText(feesInstallmentsModel.getChequeNo());
            txtChequeImage.setVisibility(View.VISIBLE);
            Glide.with(this)
                    .load(feesInstallmentsModel.getChequeImage())
                    .asBitmap()
                    .placeholder(R.drawable.defualt_album_icon)
                    .error(R.drawable.defualt_album_icon)
                    .override(300, 300)
                    .fitCenter()
                    .into(imgCheque);
        }
        edtInstallmentAmount.setText(feesInstallmentsModel.getInstallmentAmount() + "");
        if (feesInstallmentsModel.getPaymentStatus().equals("PAID")) {
            chkPaid.setChecked(true);
        } else {
            chkPaid.setChecked(false);
        }

    }*/

    private void startCamera(int req, Uri uri) {
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "LEFT");
        values.put(MediaStore.Images.Media.DESCRIPTION, "From your Camera");
        uri = getMyActivity().getContentResolver().insert(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        imageUri1 = uri;
        imageUri2 = uri;
        imageUri3 = uri;
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        startActivityForResult(intent, req);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CAMERA1) {
            if (resultCode == Activity.RESULT_OK) {
                try {
                    String imageurl = getMyActivity().getRealPathFromURI(imageUri1);
                    // setVehicleImage(imageurl, requestCode);
                    Log.e("", imageurl);
                    chequeFile1 = new File(getMyActivity().getRealPathFromURI(imageUri1));
                    setImage(imageurl, imgCheque1);
                } catch (Exception e) {
                    e.printStackTrace();
                    getMyActivity().showToast("Try Again");
                }

            } else {
                getMyActivity().showToast("Capture Cancelled");
            }
        }


        if (requestCode == REQUEST_CAMERA2) {
            if (resultCode == Activity.RESULT_OK) {
                try {
                    String imageurl = getMyActivity().getRealPathFromURI(imageUri2);
                    // setVehicleImage(imageurl, requestCode);
                    Log.e("", imageurl);
                    chequeFile1 = new File(getMyActivity().getRealPathFromURI(imageUri2));
                    setImage(imageurl, imgCheque2);
                } catch (Exception e) {
                    e.printStackTrace();
                    getMyActivity().showToast("Try Again");
                }

            } else {
                getMyActivity().showToast("Capture Cancelled");
            }
        }

        if (requestCode == REQUEST_CAMERA3) {
            if (resultCode == Activity.RESULT_OK) {
                try {
                    String imageurl = getMyActivity().getRealPathFromURI(imageUri3);
                    // setVehicleImage(imageurl, requestCode);
                    Log.e("", imageurl);
                    chequeFile2 = new File(getMyActivity().getRealPathFromURI(imageUri3));
                    setImage(imageurl, imgCheque3);
                } catch (Exception e) {
                    e.printStackTrace();
                    getMyActivity().showToast("Try Again");
                }

            } else {
                getMyActivity().showToast("Capture Cancelled");
            }
        }

    }

    //for camera image
    private void setImage(String uristr, ImageView imgage) {
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
                bindView(fileView, true, imgage);

            }
        } catch (Exception e) {

        }
    }

    private void bindView(Uri uri, boolean flag, ImageView imageView) {
        Glide.with(this)
                .load(uri)
                .asBitmap()
                .override(300, 300)
                .fitCenter()
                .into(imageView);
        if (flag) {
            imageView.setRotation(90);
        }

    }


    private void bindModel() {

        //Installment 1
        feesInstallmentsModel1.setPaymentMode(spnPaymentMode1.getSelectedItem().toString());
        feesInstallmentsModel1.setInstallmentAmount(CommonUtils.asDouble(edtInstallmentAmount1.getText().toString()));
        feesInstallmentsModel1.setChequeNo(edtChequeNumber1.getText().toString());
        feesInstallmentsModel1.setChequeBankName(edtBankName1.getText().toString());
        feesInstallmentsModel1.setInstallmentNo("1");
        if (feesInstallmentsModel1.getInstallmentDate() <= 0) {
            feesInstallmentsModel1.setInstallmentDate(Calendar.getInstance().getTimeInMillis());
        }
        if (chkPaid1.isChecked()) {
            feesInstallmentsModel1.setPaymentStatus("PAID");
        } else {
            feesInstallmentsModel1.setPaymentStatus("UNPAID");
        }

        //Installment 2
        if (studentFeesModel.getNoOfInstallments().equals("2") || studentFeesModel.getNoOfInstallments().equals("3")) {
            feesInstallmentsModel2.setPaymentMode(spnPaymentMode2.getSelectedItem().toString());
            feesInstallmentsModel2.setInstallmentAmount(CommonUtils.asDouble(edtInstallmentAmount2.getText().toString()));
            feesInstallmentsModel2.setChequeNo(edtChequeNumber2.getText().toString());
            feesInstallmentsModel2.setChequeBankName(edtBankName2.getText().toString());
            feesInstallmentsModel2.setInstallmentNo("2");
            if (feesInstallmentsModel2.getInstallmentDate() <= 0) {
                feesInstallmentsModel2.setInstallmentDate(Calendar.getInstance().getTimeInMillis());
            }
            if (chkPaid2.isChecked()) {
                feesInstallmentsModel2.setPaymentStatus("PAID");
            } else {
                feesInstallmentsModel2.setPaymentStatus("UNPAID");
            }
        }
        if (studentFeesModel.getNoOfInstallments().equals("3")) {
            //Installment 3
            feesInstallmentsModel3.setPaymentMode(spnPaymentMode3.getSelectedItem().toString());
            feesInstallmentsModel3.setInstallmentAmount(CommonUtils.asDouble(edtInstallmentAmount3.getText().toString()));
            feesInstallmentsModel3.setChequeNo(edtChequeNumber3.getText().toString());
            feesInstallmentsModel3.setChequeBankName(edtBankName3.getText().toString());
            feesInstallmentsModel3.setInstallmentNo("3");
            if (feesInstallmentsModel3.getInstallmentDate() <= 0) {
                feesInstallmentsModel3.setInstallmentDate(Calendar.getInstance().getTimeInMillis());
            }
            if (chkPaid3.isChecked()) {
                feesInstallmentsModel3.setPaymentStatus("PAID");
            } else {
                feesInstallmentsModel3.setPaymentStatus("UNPAID");
            }
        }
    }

    private boolean check() {
        if (feesInstallmentsModel1.getInstallmentNo() == null && feesInstallmentsModel1.getInstallmentNo().isEmpty()) {
            getMyActivity().showToast("Please select installment type of installment 1");
            return false;
        }
        if (feesInstallmentsModel1.getPaymentMode() == null && feesInstallmentsModel1.getPaymentMode().isEmpty()) {
            getMyActivity().showToast("Please select payment mode of installment 1");
            return false;
        }
        if (feesInstallmentsModel1.getInstallmentAmount() <= 0) {
            getMyActivity().showToast("Please enter installment amount of installment 1");
            return false;
        }
        if (feesInstallmentsModel1.getInstallmentNo() != null
                && feesInstallmentsModel1.getInstallmentNo().equals("Cheque")
                && feesInstallmentsModel1.getChequeBankName() == null
                && feesInstallmentsModel1.getChequeBankName().isEmpty()
                ) {
            getMyActivity().showToast("Please enter bank name of installment 1");
            return false;
        }
        if (feesInstallmentsModel1.getInstallmentNo() != null
                && feesInstallmentsModel1.getInstallmentNo().equals("Cheque")
                && feesInstallmentsModel1.getChequeNo() == null
                && feesInstallmentsModel1.getChequeNo().isEmpty()
                ) {
            getMyActivity().showToast("Please enter cheque number of installment 1");
            return false;
        }

        //installment 2
        if (studentFeesModel.getNoOfInstallments().equals("2") || studentFeesModel.getNoOfInstallments().equals("3")) {
            if (feesInstallmentsModel2.getInstallmentNo() == null && feesInstallmentsModel2.getInstallmentNo().isEmpty()) {
                getMyActivity().showToast("Please select installment type of installment 2");
                return false;
            }
            if (feesInstallmentsModel2.getPaymentMode() == null && feesInstallmentsModel2.getPaymentMode().isEmpty()) {
                getMyActivity().showToast("Please select payment mode of installment 2");
                return false;
            }
            if (feesInstallmentsModel2.getInstallmentAmount() <= 0) {
                getMyActivity().showToast("Please enter installment amount of installment 2");
                return false;
            }
            if (feesInstallmentsModel2.getInstallmentNo() != null
                    && feesInstallmentsModel2.getInstallmentNo().equals("Cheque")
                    && feesInstallmentsModel2.getChequeBankName() == null
                    && feesInstallmentsModel2.getChequeBankName().isEmpty()
                    ) {
                getMyActivity().showToast("Please enter bank name of installment 2");
                return false;
            }
            if (feesInstallmentsModel2.getInstallmentNo() != null
                    && feesInstallmentsModel2.getInstallmentNo().equals("Cheque")
                    && feesInstallmentsModel2.getChequeNo() == null
                    && feesInstallmentsModel2.getChequeNo().isEmpty()
                    ) {
                getMyActivity().showToast("Please enter cheque number of installment 2");
                return false;
            }
        }
        //installment 3

        if (studentFeesModel.getNoOfInstallments().equals("3")) {
            if (feesInstallmentsModel3.getInstallmentNo() == null && feesInstallmentsModel3.getInstallmentNo().isEmpty()) {
                getMyActivity().showToast("Please select installment type of installment 3");
                return false;
            }
            if (feesInstallmentsModel3.getPaymentMode() == null && feesInstallmentsModel3.getPaymentMode().isEmpty()) {
                getMyActivity().showToast("Please select payment mode of installment 3");
                return false;
            }
            if (feesInstallmentsModel3.getInstallmentAmount() <= 0) {
                getMyActivity().showToast("Please enter installment amount of installment 3");
                return false;
            }
            if (feesInstallmentsModel3.getInstallmentNo() != null
                    && feesInstallmentsModel3.getInstallmentNo().equals("Cheque")
                    && feesInstallmentsModel3.getChequeBankName() == null
                    && feesInstallmentsModel3.getChequeBankName().isEmpty()
                    ) {
                getMyActivity().showToast("Please enter bank name of installment 3");
                return false;
            }
            if (feesInstallmentsModel3.getInstallmentNo() != null
                    && feesInstallmentsModel3.getInstallmentNo().equals("Cheque")
                    && feesInstallmentsModel3.getChequeNo() == null
                    && feesInstallmentsModel3.getChequeNo().isEmpty()
                    ) {
                getMyActivity().showToast("Please enter cheque number of installment 3");
                return false;
            }
        }
        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_cheque1:
                startCamera(REQUEST_CAMERA1, imageUri1);
                break;
            case R.id.img_cheque2:
                startCamera(REQUEST_CAMERA2, imageUri2);
                break;

            case R.id.img_cheque3:
                startCamera(REQUEST_CAMERA3, imageUri1);
                break;

            case R.id.date_picker_installment1:
                showDatePicker();
                break;
            case R.id.date_picker_installment2:
                showDatePicker2();
                break;
            case R.id.date_picker_installment3:
                showDatePicker3();
                break;
        }
    }

    private void uploadImages1() {
        final ProgressDialog progressDialog = new ProgressDialog(getMyActivity());
        progressDialog.setTitle("Uploading ... ");
        progressDialog.show();
        progressDialog.setCancelable(false);
        S3UploadActivity.uploadData(getMyActivity(), new S3FileTransferDelegate() {
            @Override
            public void onS3FileTransferStateChanged(int id, TransferState state, String url, Object object) {
                File file = (File) object;
                progressDialog.dismiss();
                feesInstallmentsModel1.setChequeImage(url);
                if (chequeFile2 != null) {
                    uploadImages2();
                } else {
                    if (check()) {
                        save();
                    }
                }
            }

            @Override
            public void onS3FileTransferProgressChanged(int id, String fileName, int percentage) {
                progressDialog.setTitle("Uploading.. " + percentage + "%    ");
            }

            @Override
            public void onS3FileTransferError(int id, String fileName, Exception ex) {
                progressDialog.dismiss();
            }
        }, "schoolImage" + CommonUtils.formatDateForDisplay(Calendar.getInstance().getTime(), "ddMMyyyyhhmmss") + edtChequeNumber1.getText().toString(), chequeFile1);
    }

    private void uploadImages2() {
        final ProgressDialog progressDialog = new ProgressDialog(getMyActivity());
        progressDialog.setTitle("Uploading ... ");
        progressDialog.show();
        progressDialog.setCancelable(false);
        S3UploadActivity.uploadData(getMyActivity(), new S3FileTransferDelegate() {
            @Override
            public void onS3FileTransferStateChanged(int id, TransferState state, String url, Object object) {
                File file = (File) object;
                progressDialog.dismiss();
                feesInstallmentsModel2.setChequeImage(url);
                if (chequeFile3 != null) {
                    uploadImages3();
                } else {
                    if (check()) {
                        save();
                    }
                }
            }

            @Override
            public void onS3FileTransferProgressChanged(int id, String fileName, int percentage) {
                progressDialog.setTitle("Uploading.. " + percentage + "%    ");
            }

            @Override
            public void onS3FileTransferError(int id, String fileName, Exception ex) {
                progressDialog.dismiss();
            }
        }, "schoolImage" + CommonUtils.formatDateForDisplay(Calendar.getInstance().getTime(), "ddMMyyyyhhmmss") + edtChequeNumber2.getText().toString(), chequeFile2);
    }


    private void uploadImages3() {
        final ProgressDialog progressDialog = new ProgressDialog(getMyActivity());
        progressDialog.setTitle("Uploading ... ");
        progressDialog.show();
        progressDialog.setCancelable(false);
        S3UploadActivity.uploadData(getMyActivity(), new S3FileTransferDelegate() {
            @Override
            public void onS3FileTransferStateChanged(int id, TransferState state, String url, Object object) {
                File file = (File) object;
                progressDialog.dismiss();
                feesInstallmentsModel3.setChequeImage(url);

                if (check()) {
                    save();
                }
            }

            @Override
            public void onS3FileTransferProgressChanged(int id, String fileName, int percentage) {
                progressDialog.setTitle("Uploading.. " + percentage + "%    ");
            }

            @Override
            public void onS3FileTransferError(int id, String fileName, Exception ex) {
                progressDialog.dismiss();
            }
        }, "schoolImage" + CommonUtils.formatDateForDisplay(Calendar.getInstance().getTime(), "ddMMyyyyhhmmss") + edtChequeNumber3.getText().toString(), chequeFile3);
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
        date.setCallBack(ondate1);
        date.show(getMyActivity().getSupportFragmentManager(), "Date Picker");
    }

    DatePickerDialog.OnDateSetListener ondate1 = new DatePickerDialog.OnDateSetListener() {

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
            String formatedDate = CommonUtils.formatDateForDisplay(date, Constants.DATE_FORMAT);
            //annualCalenderMasterModel.setEventDate(formatedDate);
            datePickerInvest1.setText(CommonUtils.formatDateForDisplay(date, "dd MMM yyyy"));
            feesInstallmentsModel1.setInstallmentDate(date.getTime());
        }
    };


    private void showDatePicker2() {
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
        date.setCallBack(ondate2);
        date.show(getMyActivity().getSupportFragmentManager(), "Date Picker");
    }

    DatePickerDialog.OnDateSetListener ondate2 = new DatePickerDialog.OnDateSetListener() {

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
            String formatedDate = CommonUtils.formatDateForDisplay(date, Constants.DATE_FORMAT);
            //annualCalenderMasterModel.setEventDate(formatedDate);
            datePickerInvest2.setText(CommonUtils.formatDateForDisplay(date, "dd MMM yyyy"));
            feesInstallmentsModel2.setInstallmentDate(date.getTime());
        }
    };


    private void showDatePicker3() {
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
        date.setCallBack(ondate3);
        date.show(getMyActivity().getSupportFragmentManager(), "Date Picker");
    }

    DatePickerDialog.OnDateSetListener ondate3 = new DatePickerDialog.OnDateSetListener() {

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
            String formatedDate = CommonUtils.formatDateForDisplay(date, Constants.DATE_FORMAT);
            //annualCalenderMasterModel.setEventDate(formatedDate);
            datePickerInvest3.setText(CommonUtils.formatDateForDisplay(date, "dd MMM yyyy"));
            feesInstallmentsModel3.setInstallmentDate(date.getTime());
        }
    };

    private void save() {
        if (studentFeesModel.getNoOfInstallments().equals("1")) {
            feesInstallmentsModel2 = new FeesInstallmentsModel();
            feesInstallmentsModel3 = new FeesInstallmentsModel();
            list.add(feesInstallmentsModel1);
        } else if (studentFeesModel.getNoOfInstallments().equals("2")) {
            list.add(feesInstallmentsModel1);
            list.add(feesInstallmentsModel2);
        } else {
            list.add(feesInstallmentsModel1);
            list.add(feesInstallmentsModel2);
            list.add(feesInstallmentsModel3);
        }

        studentFeesModel.getListInstallments().addAll(list);

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put(IJson.studentId, studentFeesModel.getStudentId());
        hashMap.put(IJson.totalFees, studentFeesModel.getTotalFees());
        hashMap.put(IJson.noOfInstallments, studentFeesModel.getNoOfInstallments());
        hashMap.put(IJson.installments, studentFeesModel.getListInstallments());

        String url = String.format(IUrls.URL_ADD_FEES, studentFeesModel.getStudentId());

        CallWebService.getWebserviceObject(getMyActivity(), true, true, Request.Method.POST, url, hashMap, new VolleyResponseListener<StudentFeesModel>() {
            @Override
            public void onResponse(StudentFeesModel[] object) {
            }

            @Override
            public void onResponse(StudentFeesModel studentData) {
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
        }, StudentFeesModel.class);


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
                            if (chequeFile1 != null) {
                                uploadImages1();
                            } else if (chequeFile2 != null) {
                                uploadImages2();
                            } else if (chequeFile3 != null) {
                                uploadImages3();
                            } else {
                                save();
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
        }
        return true;
    }

    private void hideInstallment2() {
        txtInstallmentType2.setVisibility(View.GONE);
        txtPaymentMode2.setVisibility(View.GONE);
        txtChequeImage2.setVisibility(View.GONE);
        spnPaymentMode2.setVisibility(View.GONE);
        edtInstallmentAmount2.setVisibility(View.GONE);
        datePickerInvest2.setVisibility(View.GONE);
        edtBankName2.setVisibility(View.GONE);
        edtChequeNumber2.setVisibility(View.GONE);
        imgCheque2.setVisibility(View.GONE);
        chkPaid2.setVisibility(View.GONE);
    }

    private void hideInstallment3() {
        txtInstallmentType3.setVisibility(View.GONE);
        txtPaymentMode3.setVisibility(View.GONE);
        txtChequeImage3.setVisibility(View.GONE);
        spnPaymentMode3.setVisibility(View.GONE);
        edtInstallmentAmount3.setVisibility(View.GONE);
        datePickerInvest3.setVisibility(View.GONE);
        edtBankName3.setVisibility(View.GONE);
        edtChequeNumber3.setVisibility(View.GONE);
        imgCheque3.setVisibility(View.GONE);
        chkPaid3.setVisibility(View.GONE);
    }


    private HomeActivity getMyActivity() {
        return (HomeActivity) getActivity();
    }
}
