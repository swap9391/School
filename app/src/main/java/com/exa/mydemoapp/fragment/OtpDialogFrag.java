package com.exa.mydemoapp.fragment;

import android.annotation.SuppressLint;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.Request;
import com.exa.mydemoapp.HomeActivity;
import com.exa.mydemoapp.R;
import com.exa.mydemoapp.listner.OtpListner;
import com.exa.mydemoapp.model.OtpMasterModel;
import com.exa.mydemoapp.model.UserModel;
import com.exa.mydemoapp.webservice.CallWebService;
import com.exa.mydemoapp.webservice.IJson;
import com.exa.mydemoapp.webservice.IUrls;
import com.exa.mydemoapp.webservice.VolleyResponseListener;

import java.util.HashMap;

@SuppressLint("ValidFragment")
public class OtpDialogFrag extends DialogFragment implements View.OnClickListener {

    Button btn_resend, btn_save;
    EditText txtOtp;
    OtpListner dialogResult;
    UserModel userModel;
    String serverOtp;

    public OtpDialogFrag(OtpListner dialogResult, UserModel userModel) {
        this.dialogResult = dialogResult;
        this.userModel = userModel;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.otp_layout, container);
        btn_resend = (Button) view.findViewById(R.id.btn_resend_otp);
        btn_save = (Button) view.findViewById(R.id.btn_save);
        txtOtp = (EditText) view.findViewById(R.id.otp);

        btn_resend.setOnClickListener(this);
        btn_save.setOnClickListener(this);
        return view;
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_resend_otp:
                if (!txtOtp.getText().toString().isEmpty()) {
                    getOtp();
                } else {
                    getMyActivity().showToast("Please Enter Otp");
                }
                break;
            case R.id.btn_save:
                getOtp();
                break;
        }

    }

    @Override
    public void onResume() {
        LocalBroadcastManager.getInstance(getMyActivity()).registerReceiver(receiver, new IntentFilter("otp"));
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(getMyActivity()).unregisterReceiver(receiver);
    }

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equalsIgnoreCase("otp")) {
                final String message = intent.getStringExtra("message");
                txtOtp.setText(getOnlyNumerics(message));
                //Do whatever you want with the code here
            }
        }
    };


    public HomeActivity getMyActivity() {
        return (HomeActivity) getActivity();
    }

    public static String getOnlyNumerics(String str) {

        if (str == null) {
            return null;
        }

        StringBuffer strBuff = new StringBuffer();
        char c;

        for (int i = 0; i < str.length(); i++) {
            c = str.charAt(i);

            if (Character.isDigit(c)) {
                strBuff.append(c);
            }
        }
        return strBuff.toString();
    }


    public void getOtp() {
        HashMap<String, Object> hashMap = new HashMap<>();
      /*  hashMap.put(IJson.userId, userModel.getPkeyId());
        hashMap.put(IJson.otp, txtOtp.getText().toString());*/
        String url = String.format(IUrls.URL_VERIFY_OTP, userModel.getPkeyId(), txtOtp.getText().toString());
        Log.d("otpUrl", url);
        CallWebService.getWebserviceObject(getMyActivity(), Request.Method.GET, url, hashMap, new VolleyResponseListener<OtpMasterModel>() {
            @Override
            public void onResponse(OtpMasterModel[] object) {

            }

            @Override
            public void onResponse(OtpMasterModel object) {

            }

            @Override
            public void onResponse() {
                dialogResult.onResult(true);
                getDialog().dismiss();
            }

            @Override
            public void onError(String message) {
                if (message != null && !message.isEmpty()) {
                    getMyActivity().showToast(message);
                }

            }
        }, OtpMasterModel.class);
    }


}
