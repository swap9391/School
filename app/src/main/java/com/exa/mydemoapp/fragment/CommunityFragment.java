package com.exa.mydemoapp.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.exa.mydemoapp.Common.CommonUtils;
import com.exa.mydemoapp.Common.Constants;
import com.exa.mydemoapp.HomeActivity;
import com.exa.mydemoapp.R;
import com.exa.mydemoapp.model.CommunityModel;
import com.exa.mydemoapp.model.ImageRequest;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.Query;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by midt-006 on 24/11/17.
 */

public class CommunityFragment extends Fragment implements View.OnClickListener, TextWatcher {
    View view;
    EditText txtMsg;
    ImageButton btnAttach;
    ImageButton btnSend;
    CommunityModel communityModel;
    RecyclerView recyclerView;
    private LinearLayoutManager layoutManager;
    FirebaseRecyclerAdapter<CommunityModel, ChatMessageViewHolder> adapter;
    TelephonyManager telephonyManager;
    Uri fileView;
    List<Uri> imglist = new ArrayList<>();
    int PICK_IMAGE = 102;
    int count = 0;
    String studentName;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.layout_community, container, false);
        getMyActivity().toolbar.setTitle("Parent Community");
        getMyActivity().init();
        communityModel = new CommunityModel();
        txtMsg = (EditText) view.findViewById(R.id.edit_msg);
        btnSend = (ImageButton) view.findViewById(R.id.btn_send);
        btnAttach = (ImageButton) view.findViewById(R.id.btn_attach);

        btnAttach.setOnClickListener(this);
        btnSend.setOnClickListener(this);
        txtMsg.addTextChangedListener(this);
        btnSend.setVisibility(View.GONE);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(false);
        layoutManager = new LinearLayoutManager(getMyActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        studentName = CommonUtils.getSharedPref(Constants.STUDENT_NAME, getMyActivity());
        telephonyManager = (TelephonyManager) getMyActivity().getSystemService(Context.TELEPHONY_SERVICE);
        showList();
        return view;
    }

    public HomeActivity getMyActivity() {
        return (HomeActivity) getActivity();
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void afterTextChanged(Editable editable) {
        int length = editable.length();
        if (length > 0) {
            btnSend.setVisibility(View.VISIBLE);
        } else {
            btnSend.setVisibility(View.GONE);
        }
    }

    private boolean check() {
        if (communityModel.getMessageText() == null || communityModel.getMessageText().isEmpty()) {
            return false;
        }
        return true;
    }

    private void bindModel() {
        communityModel.setMessageText(txtMsg.getText().toString().isEmpty() ? "NA" : txtMsg.getText().toString().trim());
        communityModel.setMessageTime(CommonUtils.formatTime(Calendar.getInstance().getTime(), Constants.DATE_FORMAT));
        communityModel.setMessageUser("" + studentName);
        communityModel.setVisible("TRUE");
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_send:
                bindModel();
                if (check()) {
                    communityModel.setImg("NA");
                    saveData();
                }
                break;
            case R.id.btn_attach:
                picfromGallery();
                break;
        }

    }

    private void saveData() {
        String userId = getMyActivity().databaseReference.push().getKey();
        communityModel.setUniqueKey(userId);
        getMyActivity().databaseReference.child(Constants.MAIN_TABLE).child(Constants.COMMUNITY_TABLE).child(userId).setValue(communityModel);
        txtMsg.setText("");
    }


    private void showList() {
        Query chatRef;
        chatRef = getMyActivity().databaseReference.child(Constants.MAIN_TABLE).child(Constants.COMMUNITY_TABLE).limitToLast(50);
        adapter = new FirebaseRecyclerAdapter<CommunityModel, ChatMessageViewHolder>(CommunityModel.class, R.layout.item_community, ChatMessageViewHolder.class, chatRef) {
            @Override
            protected void populateViewHolder(final ChatMessageViewHolder viewHolder, final CommunityModel model, int position) {
                if (model.getVisible().equals("TRUE")) {
                    viewHolder.messageUser.setText(model.getMessageUser());
                    viewHolder.messageText.setText(model.getMessageText());
                    viewHolder.messageTime.setText(model.getMessageTime());

                    if (studentName.equals(model.getMessageUser())) {
                        viewHolder.messageUser.setTextColor(ContextCompat.getColor(getMyActivity(), R.color.colorPrimary));
                    } else {
                        viewHolder.messageUser.setTextColor(ContextCompat.getColor(getMyActivity(), R.color.blue));
                    }


                    if (!model.getImg().equals("NA")) {
                        viewHolder.frameLayout.setVisibility(View.VISIBLE);
                        viewHolder.imageView.setVisibility(View.VISIBLE);
                        viewHolder.progressBar.setVisibility(View.VISIBLE);
                        viewHolder.messageText.setVisibility(View.GONE);
                        Glide.with(getMyActivity()).load(model.getImg())
                                .thumbnail(0.5f)
                                .crossFade()
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .listener(new RequestListener<String, GlideDrawable>() {
                                    @Override
                                    public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                                        viewHolder.progressBar.setVisibility(View.GONE);
                                        return false;
                                    }

                                    @Override
                                    public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                                        viewHolder.progressBar.setVisibility(View.GONE);
                                        return false;
                                    }
                                })
                                .into(viewHolder.imageView);
                        viewHolder.frameLayout.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                List<ImageRequest> imageRequestList = new ArrayList<>();
                                ImageRequest imageRequest = new ImageRequest();
                                imageRequest.setImg(model.getImg());
                                imageRequestList.add(imageRequest);
                                Bundle bundle = new Bundle();
                                bundle.putSerializable("images", (Serializable) imageRequestList);
                                bundle.putInt("position", 0);
                                bundle.putString("frag", "community");
                                getMyActivity().setGallery(false);
                                getMyActivity().showFragment(new SlideshowDialogFragment(), bundle);
                            }
                        });

                    } else {
                        viewHolder.frameLayout.setVisibility(View.GONE);
                        viewHolder.imageView.setVisibility(View.GONE);
                        viewHolder.progressBar.setVisibility(View.GONE);
                        viewHolder.messageText.setVisibility(View.VISIBLE);
                    }

                } else {
                    adapter.notifyItemRemoved(position);
                }
            }

        };
        // Scroll to bottom on new messages
        adapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                layoutManager.smoothScrollToPosition(recyclerView, null, adapter.getItemCount());
            }
        });

        recyclerView.setAdapter(adapter);

    }

    public static class ChatMessageViewHolder extends RecyclerView.ViewHolder {
        TextView messageText;
        TextView messageUser;
        TextView messageTime;
        ProgressBar progressBar;
        ImageView imageView;
        FrameLayout frameLayout;

        public ChatMessageViewHolder(View itemView) {
            super(itemView);
            messageUser = (TextView) itemView.findViewById(R.id.message_user);
            messageText = (TextView) itemView.findViewById(R.id.message_text);
            messageTime = (TextView) itemView.findViewById(R.id.message_time);
            progressBar = (ProgressBar) itemView.findViewById(R.id.progress_slider);
            imageView = (ImageView) itemView.findViewById(R.id.img_chat);
            frameLayout = (FrameLayout) itemView.findViewById(R.id.frame_image);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

     /*   if (requestCode == REQUEST_CAMERA) {
            if (resultCode == Activity.RESULT_OK) {
                try {
                    String imageurl = getMyActivity().getRealPathFromURI(imageUri);
                    // setVehicleImage(imageurl, requestCode);
                    Log.e("", imageurl);
                    // setImage(imageurl);
                } catch (Exception e) {
                    e.printStackTrace();
                    getMyActivity().showToast("Try Again");
                }

            } else {
                getMyActivity().showToast("Capture Cancelled");
            }


        }
*/
        if (requestCode == PICK_IMAGE && resultCode == getMyActivity().RESULT_OK
                && null != data) {

            try {

                System.out.println("++data" + data.getClipData().getItemCount());// Get count of image here.
                System.out.println("++count" + data.getClipData().getItemCount());
                List<Uri> listOfUri = new ArrayList<>();
                for (int i = 0; i < data.getClipData().getItemCount(); i++) {
                    listOfUri.add(data.getClipData().getItemAt(i).getUri());
                }
                setImage(listOfUri);


            } catch (Exception e) {
                getMyActivity().showToast(e.getMessage());
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

    private void setImage(List<Uri> listOfUri) {

        //        Uri selectedImage1 = data.getData();
        for (Uri selectedImage : listOfUri) {
            try {
                Bitmap bitmap = null;
                bitmap = MediaStore.Images.Media.getBitmap(getMyActivity().getContentResolver(), selectedImage);
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                // Bitmap bt=Bitmap.createScaledBitmap(bitmap, 720, 1100, false);
                Bitmap bt = getMyActivity().BITMAP_RESIZER(bitmap, 300, 350);
                //  bt.compress(Bitmap.CompressFormat.PNG, 100, stream);
                byte[] vehicleImage = stream.toByteArray();
                fileView = getMyActivity().getImageUri(getMyActivity(), bitmap);
                imglist.add(fileView);

            } catch (Exception e) {

            }
        }

        if (imglist != null && imglist.size() > 0) {
            uploadImage();
        }

    }

    public void uploadImage() {
        // Uri file = Uri.fromFile(new File("path/to/images/rivers.jpg"));
        final ProgressDialog progressDialog = new ProgressDialog(getMyActivity());
        progressDialog.setTitle("Uploading");
        progressDialog.show();
        StorageReference riversRef = getMyActivity().mStorageRef.child("images/image" + Calendar.getInstance().getTime() + ".jpg");
        riversRef.putFile(imglist.get(0))
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        // Get a URL to the uploaded content
                        Uri downloadUrl = taskSnapshot.getMetadata().getDownloadUrl();
                        bindModel();
                        communityModel.setImg(downloadUrl.toString());
                        saveData();
                        progressDialog.dismiss();
                        Log.e("Result", "");
                        imglist.remove(0);
                        count = imglist.size();
                        if (count > 0) {
                            uploadImage();
                        } else {
                            return;
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle unsuccessful uploads
                        // ...
                        progressDialog.dismiss();
                        CommonUtils.showToast(getMyActivity(), exception.getMessage());
                        Log.e("Result", "");
                    }
                })
                .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                        //calculating progress percentage
                        double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();

                        //displaying percentage in progress dialog
                        progressDialog.setMessage("Uploaded " + ((int) progress) + "%...");
                    }
                });

    }

    private void picfromGallery() {
        /*Intent intent = new Intent();
        intent.setType("image*//*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);*/

        Intent intent = new Intent();
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);
    }

}
