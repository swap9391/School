package com.exa.mydemoapp.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.amazonaws.mobileconnectors.s3.transferutility.TransferState;
import com.bumptech.glide.Glide;
import com.exa.mydemoapp.Common.CommonUtils;
import com.exa.mydemoapp.HomeActivity;
import com.exa.mydemoapp.R;
import com.exa.mydemoapp.annotation.ViewById;
import com.exa.mydemoapp.listner.DialogResultListner;
import com.exa.mydemoapp.model.AlbumImagesModel;
import com.exa.mydemoapp.model.FeesInstallmentsModel;
import com.exa.mydemoapp.s3Upload.S3FileTransferDelegate;
import com.exa.mydemoapp.s3Upload.S3UploadActivity;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

/**
 * Created by midt-078 on 11/4/18.
 */

public class AddFeesDialogFragment extends DialogFragment implements View.OnClickListener {

    @ViewById(R.id.lblInstallmentType)
    TextView txtInstallmentType;
    @ViewById(R.id.lblPaymentMode)
    TextView txtPaymentMode;
    @ViewById(R.id.lblChequeImg)
    TextView txtChequeImage;
    @ViewById(R.id.spinner_fees_type)
    private Spinner spnFeesType;
    @ViewById(R.id.spinner_payment_mode)
    private Spinner spnPaymentMode;
    @ViewById(R.id.edt_installment_amount)
    private EditText edtInstallmentAmount;
    @ViewById(R.id.date_picker_installment)
    private Button datePickerInvest;
    @ViewById(R.id.btn_add)
    private Button btnAdd;
    @ViewById(R.id.edt_cheque_bank)
    private EditText edtBankName;
    @ViewById(R.id.edt_cheque_number)
    private EditText edtChequeNumber;
    @ViewById(R.id.img_cheque)
    private ImageView imgCheque;
    @ViewById(R.id.chk_paid)
    private Button chkPaid;

    private List<String> listFees;
    private List<String> listPaymentMode;
    HomeActivity activity;
    DialogResultListner dialogResultListner;
    FeesInstallmentsModel feesInstallmentsModel;
    Uri imageUri;
    Uri fileView;
    File chequeFile;
    private int REQUEST_CAMERA = 101;
    List<FeesInstallmentsModel> list;

    public AddFeesDialogFragment() {
    }

    @SuppressLint("ValidFragment")
    public AddFeesDialogFragment(HomeActivity activity, FeesInstallmentsModel feesInstallmentsModel, List<FeesInstallmentsModel> list, DialogResultListner dialogResultListner) {
        this.activity = activity;
        this.dialogResultListner = dialogResultListner;
        this.feesInstallmentsModel = feesInstallmentsModel;
        this.list = list;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.layout_add_fees, container, false);
        initView(v);
        getDialog().getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        listFees = Arrays.asList(getResources().getStringArray(R.array.fees_type));
        for (FeesInstallmentsModel feesInstallmentsModel : list) {
            for (String string : listFees) {
                if (feesInstallmentsModel.getInstallmentNo().equals(string)) {
                    listFees.remove(string);
                }
            }
        }

        ArrayAdapter<String> feesAdapter = new ArrayAdapter<String>(activity, android.R.layout.simple_spinner_item, listFees);
        feesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnFeesType.setAdapter(feesAdapter);
        feesAdapter.notifyDataSetChanged();
        spnFeesType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                if (listFees.get(position).equals("First Installment")) {
                    datePickerInvest.setVisibility(View.GONE);
                } else {
                    datePickerInvest.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }
        });


        listPaymentMode = Arrays.asList(getResources().getStringArray(R.array.payment_mode));
        ArrayAdapter<String> paymentModeAdapter = new ArrayAdapter<String>(activity, android.R.layout.simple_spinner_item, listPaymentMode);
        paymentModeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnPaymentMode.setAdapter(paymentModeAdapter);
        paymentModeAdapter.notifyDataSetChanged();
        spnPaymentMode.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {


                if (listPaymentMode.get(position).equals("Cheque")) {
                    getDialog().getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
                    edtBankName.setVisibility(View.VISIBLE);
                    edtChequeNumber.setVisibility(View.VISIBLE);
                    txtChequeImage.setVisibility(View.VISIBLE);
                    imgCheque.setVisibility(View.VISIBLE);
                } else {
                    getDialog().getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
                    edtBankName.setVisibility(View.GONE);
                    edtChequeNumber.setVisibility(View.GONE);
                    txtChequeImage.setVisibility(View.GONE);
                    imgCheque.setVisibility(View.GONE);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }
        });


        imgCheque.setOnClickListener(this);
        btnAdd.setOnClickListener(this);

        return v;
    }

    private void initView(View view) {
        spnFeesType = (Spinner) view.findViewById(R.id.spinner_fees_type);
        txtInstallmentType = (TextView) view.findViewById(R.id.lblInstallmentType);
        txtPaymentMode = (TextView) view.findViewById(R.id.lblPaymentMode);
        txtChequeImage = (TextView) view.findViewById(R.id.lblChequeImg);
        spnPaymentMode = (Spinner) view.findViewById(R.id.spinner_payment_mode);
        edtInstallmentAmount = (EditText) view.findViewById(R.id.edt_installment_amount);
        datePickerInvest = (Button) view.findViewById(R.id.date_picker_installment);
        btnAdd = (Button) view.findViewById(R.id.btn_add);
        edtBankName = (EditText) view.findViewById(R.id.edt_cheque_bank);
        edtChequeNumber = (EditText) view.findViewById(R.id.edt_cheque_number);
        chkPaid = (CheckBox) view.findViewById(R.id.chk_paid);
        imgCheque = (ImageView) view.findViewById(R.id.img_cheque);
    }

    private void startCamera() {
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "LEFT");
        values.put(MediaStore.Images.Media.DESCRIPTION, "From your Camera");
        imageUri = activity.getContentResolver().insert(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(intent, REQUEST_CAMERA);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CAMERA) {
            if (resultCode == Activity.RESULT_OK) {
                try {
                    String imageurl = activity.getRealPathFromURI(imageUri);
                    // setVehicleImage(imageurl, requestCode);
                    Log.e("", imageurl);
                    chequeFile = new File(activity.getRealPathFromURI(imageUri));
                    setImage(imageurl);
                } catch (Exception e) {
                    e.printStackTrace();
                    activity.showToast("Try Again");
                }

            } else {
                activity.showToast("Capture Cancelled");
            }


        }
    }

    //for camera image
    private void setImage(String uristr) {

        Bitmap bitmap = null;
        try {
            if (uristr != null) {
                bitmap = activity.getBitmap(uristr);
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                // Bitmap bt=Bitmap.createScaledBitmap(bitmap, 720, 1100, false);
                Bitmap bt = activity.BITMAP_RESIZER(bitmap, 300, 350);
                bt.compress(Bitmap.CompressFormat.PNG, 50, stream);
                byte[] vehicleImage = stream.toByteArray();
                fileView = activity.getImageUri(activity, bt);


                //imglist.add(fileView);
                bindView(fileView, true);

            }
        } catch (Exception e) {

        }
    }

    private void bindView(Uri uri, boolean flag) {
        Glide.with(this)
                .load(uri)
                .asBitmap()
                .override(300, 300)
                .fitCenter()
                .into(imgCheque);
        if (flag) {
            imgCheque.setRotation(90);
        }

    }


    private void bindModel() {
        feesInstallmentsModel.setInstallmentNo(spnFeesType.getSelectedItem().toString());
        feesInstallmentsModel.setPaymentMode(spnPaymentMode.getSelectedItem().toString());
        feesInstallmentsModel.setInstallmentAmount(CommonUtils.asDouble(edtInstallmentAmount.getText().toString()));
        feesInstallmentsModel.setChequeNo(edtChequeNumber.getText().toString());
        feesInstallmentsModel.setChequeBankName(edtBankName.getText().toString());
        if (chkPaid.isSelected()) {
            feesInstallmentsModel.setPaid(true);
        } else {
            feesInstallmentsModel.setPaid(false);
        }

    }

    private boolean check() {
        if (feesInstallmentsModel.getInstallmentNo() == null && feesInstallmentsModel.getInstallmentNo().isEmpty()) {
            activity.showToast("Please select installment type");
            return false;
        }
        if (feesInstallmentsModel.getPaymentMode() == null && feesInstallmentsModel.getPaymentMode().isEmpty()) {
            activity.showToast("Please select payment mode");
            return false;
        }
        if (feesInstallmentsModel.getInstallmentAmount() <= 0) {
            activity.showToast("Please enter installment amount");
            return false;
        }
        if (feesInstallmentsModel.getInstallmentNo() != null
                && feesInstallmentsModel.getInstallmentNo().equals("Cheque")
                && feesInstallmentsModel.getChequeBankName() == null
                && feesInstallmentsModel.getChequeBankName().isEmpty()
                ) {
            activity.showToast("Please enter bank name");
            return false;
        }
        if (feesInstallmentsModel.getInstallmentNo() != null
                && feesInstallmentsModel.getInstallmentNo().equals("Cheque")
                && feesInstallmentsModel.getChequeNo() == null
                && feesInstallmentsModel.getChequeNo().isEmpty()
                ) {
            activity.showToast("Please enter cheque number");
            return false;
        }

        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_add:
                bindModel();
                if (check()) {
                    if (feesInstallmentsModel.getPaymentMode().equals("Cheque")) {
                        uploadImages();
                    } else {
                        dialogResultListner.getResult(feesInstallmentsModel);
                        getDialog().dismiss();
                    }
                }


                break;
            case R.id.img_cheque:
                startCamera();
                break;
        }
    }

    private void uploadImages() {
        final ProgressDialog progressDialog = new ProgressDialog(activity);
        progressDialog.setTitle("Uploading ... ");
        progressDialog.show();
        progressDialog.setCancelable(false);
        S3UploadActivity.uploadData(activity, new S3FileTransferDelegate() {
            @Override
            public void onS3FileTransferStateChanged(int id, TransferState state, String url, Object object) {
                File file = (File) object;
                progressDialog.dismiss();
                feesInstallmentsModel.setChequeImage(url);
                dialogResultListner.getResult(feesInstallmentsModel);
                getDialog().dismiss();
            }

            @Override
            public void onS3FileTransferProgressChanged(int id, String fileName, int percentage) {
                progressDialog.setTitle("Uploading.. " + percentage + "%    ");
            }

            @Override
            public void onS3FileTransferError(int id, String fileName, Exception ex) {
                progressDialog.dismiss();
            }
        }, "schoolImage" + CommonUtils.formatDateForDisplay(Calendar.getInstance().getTime(), "ddMMyyyyhhmmss") + edtChequeNumber.getText().toString(), chequeFile);
    }


}
