package com.exa.mydemoapp.annotation;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by midt-006 on 27/11/17.
 */

public class RequiredFieldException extends ShowableException {
    private String fieldName;
    private String localisedErrorMessage;
    private Context context;

    public RequiredFieldException(String fieldName, String localisedErrorMessage) {
        this.fieldName = fieldName;
        this.localisedErrorMessage = localisedErrorMessage;
    }

    public RequiredFieldException(Context context, String fieldName) {
        this.fieldName = fieldName;
        this.context = context;
        notifyUserWithToast(context);
    }

    public String toString() {
        String msg = fieldName;
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
        return msg;
    }
}
