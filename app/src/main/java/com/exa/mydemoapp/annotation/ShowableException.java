package com.exa.mydemoapp.annotation;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by midt-006 on 27/11/17.
 */

public class ShowableException extends Exception {

    public void notifyUserWithToast(Context context) {
        Toast.makeText(context, toString(), Toast.LENGTH_SHORT).show();
    }
}