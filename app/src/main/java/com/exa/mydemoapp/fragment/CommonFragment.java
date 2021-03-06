package com.exa.mydemoapp.fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.view.View;

import com.amazonaws.mobileconnectors.s3.transferutility.TransferState;
import com.exa.mydemoapp.Common.CommonUtils;
import com.exa.mydemoapp.HomeActivity;
import com.exa.mydemoapp.annotation.ViewById;
import com.exa.mydemoapp.model.AlbumImagesModel;
import com.exa.mydemoapp.s3Upload.S3FileTransferDelegate;
import com.exa.mydemoapp.s3Upload.S3UploadActivity;

import java.io.File;
import java.lang.reflect.Field;
import java.util.Calendar;

/**
 * Created by midt-006 on 27/11/17.
 */

public class CommonFragment extends Fragment {
    protected Uri imageUri;
    protected int REQUEST_CAMERA = 101, PICK_IMAGE = 102;

    protected void initViewBinding(View fragmentView) {
        Field[] fields = this.getClass().getDeclaredFields();
        if (fields != null && fields.length > 0) {
            for (Field field : fields) {
                ViewById viewById = field.getAnnotation(ViewById.class);
                if (viewById != null) {
                    View view = fragmentView.findViewById(viewById.value());
                    field.setAccessible(true);
                    try {
                       /* if (view instanceof MaterialSpinner) {
                            MaterialSpinner materialSpinner = (MaterialSpinner) view;
                            materialSpinner.setBackground(getActivity().getResources().getDrawable(R.drawable.textview_border));
                        }*/
                        field.set(this, view);
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }

            }
        }
    }

    protected String getStringById(int id) {
        return getResources().getString(id);
    }

    /*   protected <T> boolean readDataAndValidate(T model) {
           if (model != null) {
               boolean isValid = true;
               Field[] fields = model.getClass().getDeclaredFields();
               if (fields != null && fields.length > 0) {
                   for (Field field : fields) {
                       field.setAccessible(true);
                       Value valueAnnotation = field.getAnnotation(Value.class);
                       if (valueAnnotation != null) {
                           View view = fragmentView.findViewById(valueAnnotation.viewId());
                           if (view instanceof EditText) {
                               EditText editText = (EditText) view;
                               String value = editText.getText().toString();
                               NotNull notNull = field.getAnnotation(NotNull.class);
                               Pattern pattern = field.getAnnotation(Pattern.class);
                               NonZero nonZero = field.getAnnotation(NonZero.class);
                               if (notNull != null) {
                                   if (TextUtils.isEmpty(value)) {
                                       editText.setError(getString(notNull.messageCode()));
                                       isValid = false;
                                   }
                               }
                               if (pattern != null && !TextUtils.isEmpty(value)) {
                                   boolean isValidPattern = RegexUtils.validate(pattern.value(), value);
                                   if (!isValidPattern) {
                                       isValid = false;
                                       editText.setError(getString(pattern.messageCode()));
                                   }
                               }
                               if (nonZero != null && !TextUtils.isEmpty(value)) {
                                   if (TextUtils.isDigitsOnly(value)) {
                                       if (Long.parseLong(value) <= 0) {
                                           editText.setError(getString(nonZero.messageCode()));
                                           isValid = false;
                                       }
                                   }
                               }

                               if (!TextUtils.isEmpty(value)) {
                                   if (field.getType().isAssignableFrom(String.class)) {
                                       try {
                                           field.set(model, value);
                                       } catch (IllegalAccessException e) {
                                           e.printStackTrace();
                                           isValid = false;
                                       }
                                   } else if (field.getType() == Long.class) {
                                       if (TextUtils.isDigitsOnly(value)) {
                                           try {
                                               field.set(model, Long.parseLong(value));
                                           } catch (IllegalAccessException e) {
                                               e.printStackTrace();
                                               isValid = false;
                                           }
                                       } else {
                                           editText.setError("Invalid Number");
                                           isValid = false;
                                       }
                                   } else if (field.getType() == BigDecimal.class) {
                                       try {
                                           field.set(model, BigDecimal.valueOf(Double.parseDouble(value)));
                                       } catch (IllegalAccessException e) {
                                           e.printStackTrace();
                                           isValid = false;
                                       }
                                   }
                               }
                           }
                       }


                   }
               }
               return isValid;
           } else {
               throw new IllegalArgumentException("Model object can not be null");
           }

   }*/
    protected void picfromGallery() {
        /*Intent intent = new Intent();
        intent.setType("image*//*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);*/

        Intent intent = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
       // intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE);

       /* Intent intent = new Intent();
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);*/
    }

  


}
