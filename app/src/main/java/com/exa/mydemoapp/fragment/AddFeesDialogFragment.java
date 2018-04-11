package com.exa.mydemoapp.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DialogFragment;
import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.exa.mydemoapp.Common.CommonUtils;
import com.exa.mydemoapp.HomeActivity;
import com.exa.mydemoapp.R;
import com.exa.mydemoapp.annotation.ViewById;
import com.exa.mydemoapp.listner.DialogResultListner;
import com.exa.mydemoapp.model.FeesInstallmentsModel;

import java.io.ByteArrayOutputStream;
import java.util.Arrays;
import java.util.List;

/**
 * Created by midt-078 on 11/4/18.
 */

public class AddFeesDialogFragment extends DialogFragment {

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
    private Button edtBankName;
    @ViewById(R.id.edt_cheque_number)
    private Button edtChequeNumber;
    @ViewById(R.id.chk_paid)
    private ImageView imgCheque;
    @ViewById(R.id.img_cheque)

    private Button chkPaid;

    private List<String> listFees;
    private List<String> listPaymentMode;
    HomeActivity activity;
    DialogResultListner dialogResultListner;
    FeesInstallmentsModel feesInstallmentsModel;
    Uri imageUri;
    Uri fileView;
    private int REQUEST_CAMERA = 101;

    public AddFeesDialogFragment() {
    }

    @SuppressLint("ValidFragment")
    public AddFeesDialogFragment(HomeActivity activity, DialogResultListner dialogResultListner) {
        this.activity = activity;
        this.dialogResultListner = dialogResultListner;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.layout_add_fees, container, false);
        activity.initViewBinding(v);
        feesInstallmentsModel = new FeesInstallmentsModel();

        listFees = Arrays.asList(getResources().getStringArray(R.array.fees_type));
        ArrayAdapter<String> feesAdapter = new ArrayAdapter<String>(activity, android.R.layout.simple_spinner_item, listFees);
        feesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnFeesType.setAdapter(feesAdapter);
        feesAdapter.notifyDataSetChanged();
        spnFeesType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                if (listFees.get(position).equals("Cheque")) {
                    edtBankName.setVisibility(View.VISIBLE);
                    edtChequeNumber.setVisibility(View.VISIBLE);
                    txtChequeImage.setVisibility(View.VISIBLE);
                    imgCheque.setVisibility(View.VISIBLE);
                } else {
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


        listPaymentMode = Arrays.asList(getResources().getStringArray(R.array.payment_mode));
        ArrayAdapter<String> paymentModeAdapter = new ArrayAdapter<String>(activity, android.R.layout.simple_spinner_item, listPaymentMode);
        paymentModeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnPaymentMode.setAdapter(paymentModeAdapter);
        paymentModeAdapter.notifyDataSetChanged();
        spnPaymentMode.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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


        imgCheque.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        return v;
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

}
