package com.exa.mydemoapp.s3Upload;

import com.amazonaws.mobileconnectors.s3.transferutility.TransferState;

public interface S3FileTransferDelegate {

    void onS3FileTransferStateChanged(int id, TransferState state, String url);

    void onS3FileTransferProgressChanged(int id, String fileName, int percentage);

    void onS3FileTransferError(int id, String fileName, Exception ex);

}