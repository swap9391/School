package com.exa.mydemoapp.s3Upload;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.amazonaws.mobile.client.AWSMobileClient;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferListener;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferObserver;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferState;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility;
import com.amazonaws.services.s3.AmazonS3Client;
import com.exa.mydemoapp.R;
import com.exa.mydemoapp.webservice.IUrls;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

/**
 * Created by midt-078 on 7/3/18.
 */

public class S3UploadActivity {


    public static void uploadData(Activity activity, S3FileTransferDelegate s3FileTransferDelegate, String filename, File file) {
        // Initialize AWSMobileClient if not initialized upon the app startup.
        TransferUtility transferUtility =
                TransferUtility.builder()
                        .context(activity.getApplicationContext())
                        .awsConfiguration(AWSMobileClient.getInstance().getConfiguration())
                        .s3Client(new AmazonS3Client(AWSMobileClient.getInstance().getCredentialsProvider()))
                        .build();


        Bitmap bitmap = BitmapFactory.decodeResource(activity.getResources(), R.drawable.icon_girl);
        File filesDir = activity.getFilesDir();
        File imageFile = new File(filesDir, filename + ".JPG");

        OutputStream os;
        try {
            os = new FileOutputStream(imageFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, os);
            os.flush();
            os.close();
        } catch (Exception e) {
            Log.e(activity.getClass().getSimpleName(), "Error writing bitmap", e);
        }
        TransferObserver uploadObserver =
                transferUtility.upload(
                        filename + ".JPG",
                        file);

        uploadObserver.setTransferListener(new TransferListener() {

            @Override
            public void onStateChanged(int id, TransferState state) {
                if (TransferState.COMPLETED == state) {
                    // Handle a completed upload.
                    s3FileTransferDelegate.onS3FileTransferStateChanged(id, state, IUrls.S3_BASE_URL + filename + ".JPG",file);
                }
            }

            @Override
            public void onProgressChanged(int id, long bytesCurrent, long bytesTotal) {
                float percentDonef = ((float) bytesCurrent / (float) bytesTotal) * 100;
                int percentDone = (int) percentDonef;
                s3FileTransferDelegate.onS3FileTransferProgressChanged(id, filename, percentDone);
                Log.d("MainActivity", "   ID:" + id + "   bytesCurrent: " + bytesCurrent + "   bytesTotal: " + bytesTotal + " " + percentDone + "%");
            }

            @Override
            public void onError(int id, Exception ex) {
                // Handle errors
                Log.e("Error", ex.getMessage());
                s3FileTransferDelegate.onS3FileTransferError(id, filename, ex);
            }

        });

        // If your upload does not trigger the onStateChanged method inside your
        // TransferListener, you can directly check the transfer state as shown here.
        if (TransferState.COMPLETED == uploadObserver.getState()) {
            // Handle a completed upload.
        }
    }
}