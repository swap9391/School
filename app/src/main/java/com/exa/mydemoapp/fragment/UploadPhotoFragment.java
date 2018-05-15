package com.exa.mydemoapp.fragment;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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
import com.exa.mydemoapp.Common.Connectivity;
import com.exa.mydemoapp.Common.Constants;
import com.exa.mydemoapp.Common.FloatingActionButton;
import com.exa.mydemoapp.HomeActivity;
import com.exa.mydemoapp.R;
import com.exa.mydemoapp.annotation.ViewById;
import com.exa.mydemoapp.model.AlbumImagesModel;
import com.exa.mydemoapp.model.AlbumMasterModel;
import com.exa.mydemoapp.model.DropdownMasterModel;
import com.exa.mydemoapp.model.StudentModel;
import com.exa.mydemoapp.model.UserModel;
import com.exa.mydemoapp.s3Upload.S3FileTransferDelegate;
import com.exa.mydemoapp.s3Upload.S3UploadActivity;
import com.exa.mydemoapp.service.LocationUpdateService;
import com.exa.mydemoapp.webservice.CallWebService;
import com.exa.mydemoapp.webservice.IJson;
import com.exa.mydemoapp.webservice.IUrls;
import com.exa.mydemoapp.webservice.VolleyResponseListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Swapnil Jadhav on 3/8/17.
 */

public class UploadPhotoFragment extends CommonFragment implements View.OnClickListener {

    Uri fileView;
    //List<Uri> imglist = new ArrayList<>();
    List<File> imageFiles = new ArrayList<>();
    int count = 0;
    AlbumMasterModel albumImagesModel;
    LocationUpdateService myservice;
    private boolean bound = false;
    View view;
    boolean isEdit = false;
    List<DropdownMasterModel> listEventType;
    ArrayList<AlbumMasterModel> albumImagesModelArrayList;
    @ViewById(R.id.lattitude)
    TextView lat;
    @ViewById(R.id.longitude)
    TextView longit;
    @ViewById(R.id.edt_title)
    private EditText edt_title;
    @ViewById(R.id.edt_description)
    private EditText edt_description;
    @ViewById(R.id.spinner_image_type)
    private Spinner spinnerType;
    @ViewById(R.id.spinner_class_name)
    private Spinner spnClass;
    @ViewById(R.id.spinner_division)
    private Spinner spnDivision;
    @ViewById(R.id.spinner_student_name)
    private Spinner spinnerStudentName;
    @ViewById(R.id.txt_student_spinner)
    private TextView txtStudentSpinnerTitle;
    @ViewById(R.id.lbl_division)
    private TextView txtDivisionSpinnerTitle;


    private List<DropdownMasterModel> listClass;
    private List<DropdownMasterModel> listDivision;
    private List<StudentModel> listStudent;
    private List<UserModel> listStudents;
    private List<String> listStudentName;
    ProgressDialog progressDialog;
    int totalImages;
    boolean apiFlag = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.upload_info_layout, container, false);
        getMyActivity().toolbar.setTitle(getString(R.string.dashboard_upload));
        getMyActivity().init();
        initViewBinding(view);
        imageFiles = new ArrayList<>();

        listEventType = getMyActivity().getDbInvoker().getDropDownByType("IMAGETYPE");
        ArrayAdapter<DropdownMasterModel> eventAdapter = new ArrayAdapter<>(getMyActivity(), android.R.layout.simple_spinner_item, listEventType);
        eventAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerType.setAdapter(eventAdapter);
        eventAdapter.notifyDataSetChanged();


        listClass = getMyActivity().getDbInvoker().getDropDownByType("CLASSTYPE");
        ArrayAdapter<DropdownMasterModel> classAdapter = new ArrayAdapter<DropdownMasterModel>(getMyActivity(), android.R.layout.simple_spinner_item, listClass);
        classAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnClass.setAdapter(classAdapter);
        classAdapter.notifyDataSetChanged();

        DropdownMasterModel allDropdownMasterModel = new DropdownMasterModel();
        allDropdownMasterModel.setDropdownValue("All");
        allDropdownMasterModel.setServerValue(null);
        listDivision = new ArrayList<>();
        listDivision.add(allDropdownMasterModel);
        listDivision = getMyActivity().getDbInvoker().getDropDownByType("DEVISION");
        ArrayAdapter<DropdownMasterModel> divisionAdapter = new ArrayAdapter<>(getMyActivity(), android.R.layout.simple_spinner_item, listDivision);
        divisionAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnDivision.setAdapter(divisionAdapter);
        divisionAdapter.notifyDataSetChanged();


        spnDivision.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                getStudent(listClass.get(spnClass.getSelectedItemPosition()).getServerValue(), listDivision.get(position).getServerValue());

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spnClass.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (listClass.get(spnClass.getSelectedItemPosition()).getDropdownValue().equals("All")) {
                    spnDivision.setVisibility(View.GONE);
                    spinnerStudentName.setVisibility(View.GONE);
                    txtDivisionSpinnerTitle.setVisibility(View.GONE);
                    txtStudentSpinnerTitle.setVisibility(View.GONE);
                    albumImagesModel.setStudentId(null);
                    albumImagesModel.setDivisionName(null);
                } else {
                    spnDivision.setVisibility(View.VISIBLE);
                    spinnerStudentName.setVisibility(View.VISIBLE);
                    txtDivisionSpinnerTitle.setVisibility(View.VISIBLE);
                    txtStudentSpinnerTitle.setVisibility(View.VISIBLE);
                    getStudent(listClass.get(position).getServerValue(), listDivision.get(spnDivision.getSelectedItemPosition()).getServerValue());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        Bundle bundle = getArguments();
        albumImagesModelArrayList = new ArrayList<>();

        if (bundle != null) {
            albumImagesModelArrayList = (ArrayList<AlbumMasterModel>) bundle.getSerializable("imageData");
        }
        if (albumImagesModelArrayList != null && albumImagesModelArrayList.size() > 0) {
            albumImagesModel = albumImagesModelArrayList.get(0);
            bindView();
            isEdit = true;
        } else {
            albumImagesModel = new AlbumMasterModel();
            isEdit = false;
        }

        LinearLayout yourframelayout = (LinearLayout) view.findViewById(R.id.btnFloat);
        FloatingActionButton fabButton = new FloatingActionButton.Builder(getMyActivity(), yourframelayout)
                .withDrawable(getResources().getDrawable(R.drawable.ic_add_white))
                .withButtonColor(Color.parseColor("#F43F68"))
                .withGravity(Gravity.BOTTOM | Gravity.RIGHT)
                .withMargins(0, 0, 2, 2)
                .create();


        fabButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //perform click
                bindModel();

              /*  try {
                    if (Validator.validateForNulls(albumImagesModel, getMyActivity())) {
                        // Do something that you want to
                        Log.d(TAG, "Validations Successful");
                    }
                } catch (RequiredFieldException | ClassNotFoundException | NoSuchFieldException | IllegalAccessException e) {
                    // Inform user about his SINS
                    e.printStackTrace();
                }*/

                if (check()) {
                    //startCamera();
                    picfromGallery();
                }

            }
        });


        return view;
    }

    private void bindModel() {
        albumImagesModel.setAlbumTitle(edt_title.getText().toString().trim());
        albumImagesModel.setAlbumDescription(edt_description.getText().toString().trim());
        albumImagesModel.setAlbumType(listEventType.get(spinnerType.getSelectedItemPosition()).getServerValue());
        albumImagesModel.setClassName(spnClass.getSelectedItem().toString());
        if (albumImagesModel.getClassName().equals("All")) {
            albumImagesModel.setStudentId(null);
            albumImagesModel.setDivisionName(null);
        } else {
            albumImagesModel.setStudentId(listStudent.get(spinnerStudentName.getSelectedItemPosition()).getPkeyId());
            albumImagesModel.setDivisionName(listDivision.get(spnDivision.getSelectedItemPosition()).getServerValue());
        }

    }

    private void bindView() {
        edt_title.setText(albumImagesModel.getAlbumTitle());
        edt_description.setText(albumImagesModel.getAlbumDescription());
        int eventPosition = listEventType.indexOf(albumImagesModel.getAlbumType());
        spinnerType.setSelection(eventPosition);
        if (isEdit) {
            edt_title.setEnabled(false);
        }
    }

   /* private void saveUserInformation() {
        final String userId = getMyActivity().databaseReference.push().getKey();
        if (!isEdit) {
            bindModel();
            albumImagesModel.setUniqKey(userId);
            getMyActivity().databaseReference.child(Constants.MAIN_TABLE).child(Constants.IMAGE_TABLE).child(userId).setValue(albumImagesModel);
            Toast.makeText(getMyActivity(), "Information Saved...", Toast.LENGTH_LONG).show();
        } else {
            bindModel();
            for (AlbumMasterModel imageRequest1 : albumImagesModelArrayList) {
                albumImagesModel.setUniqKey(imageRequest1.getUniqKey());
                getMyActivity().databaseReference.child(Constants.MAIN_TABLE).child(Constants.IMAGE_TABLE).child(albumImagesModel.getUniqKey()).setValue(albumImagesModel);
                Toast.makeText(getMyActivity(), "Information Saved...", Toast.LENGTH_LONG).show();
            }
            isEdit = false;
            *//*if (imglist.size() > 0) {
                uploadImage();
            }*//*
            if (imageFiles.size() > 0) {
                uploadImages();
            }
        }
    }*/

    public void uploadImages() {
        final ProgressDialog progressDialog = new ProgressDialog(getMyActivity());
        progressDialog.setTitle(getString(R.string.msg_progress_uploading));
        progressDialog.show();
        progressDialog.setCancelable(false);
        S3UploadActivity.uploadData(getMyActivity(), new S3FileTransferDelegate() {
            @Override
            public void onS3FileTransferStateChanged(int id, TransferState state, String url, Object object) {
                File file = (File) object;
                imageFiles.remove(file);
                AlbumImagesModel imageModel = new AlbumImagesModel();
                imageModel.setImageUrl(url);
                albumImagesModel.getAlbumImagesModel().add(imageModel);
                progressDialog.dismiss();
                count = imageFiles.size();
                if (count > 0) {
                    uploadImages();
                } else {
                    //saveUserInformation();
                    save();
                    /* ClearView();
                    getMyActivity().performBackForDesign();*/
                }
            }

            @Override
            public void onS3FileTransferProgressChanged(int id, String fileName, int percentage) {
                progressDialog.setTitle(getString(R.string.msg_progress_uploading) + percentage + "%    " + totalImages + "/" + imageFiles.size());
            }

            @Override
            public void onS3FileTransferError(int id, String fileName, Exception ex) {
                progressDialog.dismiss();
            }
        }, "schoolImage" + CommonUtils.formatDateForDisplay(Calendar.getInstance().getTime(), getString(R.string.date_format_joins)) + count, imageFiles.get(0));
    }


   /* @SuppressWarnings("VisibleForTests")
    public void uploadImage() {
        // Uri file = Uri.fromFile(new File("path/to/images/rivers.jpg"));
        final ProgressDialog progressDialog = new ProgressDialog(getMyActivity());
        progressDialog.setTitle("Uploading " + imglist.size() + "/" + totalImages);
        progressDialog.show();
        StorageReference riversRef = getMyActivity().mStorageRef.child("images/image" + Calendar.getInstance().getTime() + ".jpg");
        riversRef.putFile(imglist.get(0))
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        // Get a URL to the uploaded content
                        Uri downloadUrl = taskSnapshot.getMetadata().getDownloadUrl();
                        albumImagesModel.setImg(downloadUrl.toString());
                        saveUserInformation();
                        progressDialog.dismiss();
                        Log.e("Result", "");
                        imglist.remove(0);
                        count = imglist.size();
                        if (count > 0) {
                            uploadImage();
                        } else {
                            ClearView();
                            getMyActivity().performBackForDesign();
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

    }*/


    private void startCamera() {
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "LEFT");
        values.put(MediaStore.Images.Media.DESCRIPTION, "From your Camera");
        imageUri = getMyActivity().getContentResolver().insert(
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
                    String imageurl = getMyActivity().getRealPathFromURI(imageUri);
                    // setVehicleImage(imageurl, requestCode);
                    Log.e("", imageurl);
                    setImage(imageurl);
                } catch (Exception e) {
                    e.printStackTrace();
                    getMyActivity().showToast(getString(R.string.msg_try_agian));
                }

            } else {
                getMyActivity().showToast(getString(R.string.msg_capture_cacel));
            }


        }

        if (requestCode == PICK_IMAGE && resultCode == getMyActivity().RESULT_OK
                && null != data) {
          /*  System.out.println("++data" + data.getClipData().getItemCount());// Get count of image here.
            System.out.println("++count" + data.getClipData().getItemCount());*/
          try {
              List<Uri> listOfUri = new ArrayList<>();

              for (int i = 0; i < data.getClipData().getItemCount(); i++) {
                  listOfUri.add(data.getClipData().getItemAt(i).getUri());
              }
              setImage(listOfUri);
          }catch (Exception e){
              getMyActivity().showToast("Please Select Photos Option");
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

    //for camera image
    private void setImage(String uristr) {

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
                bindView(fileView, true);

            }
        } catch (Exception e) {

        }
    }

    //for gallery images
    private void setImage(List<Uri> listOfUri) {
        totalImages = listOfUri.size();
        //        Uri selectedImage1 = data.getData();
        int countOfImage = 0;
        for (Uri selectedImage : listOfUri) {
            countOfImage++;
            try {
                Bitmap bitmap = null;
                bitmap = MediaStore.Images.Media.getBitmap(getMyActivity().getContentResolver(), selectedImage);
                // ByteArrayOutputStream stream = new ByteArrayOutputStream();
                // Bitmap bt=Bitmap.createScaledBitmap(bitmap, 720, 1100, false);
                //  Bitmap bt = getMyActivity().BITMAP_RESIZER(bitmap, 300, 350);
                //  bt.compress(Bitmap.CompressFormat.PNG, 100, stream);
                // byte[] vehicleImage = stream.toByteArray();
                //fileView = getMyActivity().getImageUri(getMyActivity(), bitmap);


                File filesDir = getMyActivity().getFilesDir();
                File imageFile = new File(filesDir, "image" + CommonUtils.formatDateForDisplay(Calendar.getInstance().getTime(), getString(R.string.date_format_joins)) + countOfImage + ".JPG");

                OutputStream os;
                try {
                    os = new FileOutputStream(imageFile);
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 50, os);
                    os.flush();
                    os.close();
                } catch (Exception e) {
                    Log.e(getMyActivity().getClass().getSimpleName(), "Error writing bitmap", e);
                }

                imageFiles.add(imageFile);

                //imglist.add(fileView);
                bindView(selectedImage, false);


            } catch (Exception e) {

            }
        }
    }

    private void bindView(Uri uri, boolean flag) {
        LinearLayout layout1 = (LinearLayout) view.findViewById(R.id.lay1);

        ImageView imageView = new ImageView(getMyActivity());
        CardView cardView = new CardView(getMyActivity());

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        params.setMargins(5, 5, 5, 5);
        imageView.setLayoutParams(params);
        cardView.setLayoutParams(params);

        Glide.with(this)
                .load(uri)
                .asBitmap()
                .override(300, 300)
                .fitCenter()
                .into(imageView);
        if (flag) {
            imageView.setRotation(90);
        }
        cardView.addView(imageView);
        layout1.addView(cardView);


    }

    private void ClearView() {
        albumImagesModel = new AlbumMasterModel();
        LinearLayout layout1 = (LinearLayout) view.findViewById(R.id.lay1);
        layout1.removeAllViews();
        edt_title.getText().clear();
        edt_description.getText().clear();
    }


    @Override
    public void onClick(View view) {

    }

    private boolean check() {
        if (albumImagesModel.getClassName() == null || albumImagesModel.getClassName().equals("")) {
            getMyActivity().showToast(getString(R.string.valid_class_name));
            return false;
        }
        if (!albumImagesModel.getClassName().equalsIgnoreCase("All")) {
            if (albumImagesModel.getStudentId() == null || albumImagesModel.getStudentId().equals("")) {
                getMyActivity().showToast(getString(R.string.valid_student_name));
                return false;
            }
        }

        if (albumImagesModel.getAlbumTitle() == null || albumImagesModel.getAlbumTitle().equals("")) {
            getMyActivity().showToast(getString(R.string.valid_album_title));
            return false;
        }
        if (albumImagesModel.getAlbumDescription() == null || albumImagesModel.getAlbumDescription().equals("")) {
            getMyActivity().showToast(getString(R.string.valid_description));
            return false;
        }
        return true;
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
                            if (check()) {
                                if (Connectivity.isConnected(getMyActivity())) {
                                    if (!isEdit && imageFiles.size() > 0) {
                                        // uploadImage();
                                        uploadImages();
                                    } else {
                                        save();
                                    }
                                } else {
                                    getMyActivity().showToast(getString(R.string.no_internet));
                                }
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
            case R.id.action_gallery:
                getMyActivity().showFragment(new NewsFeedFragment(), null);
                break;
        }
        return true;
    }

    private void getImageData() {
        getMyActivity().databaseReference.child("place_data").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Map<String, AlbumMasterModel> td = new HashMap<String, AlbumMasterModel>();
                for (DataSnapshot Snapshot : dataSnapshot.getChildren()) {
                    AlbumMasterModel albumImagesModel = Snapshot.getValue(AlbumMasterModel.class);
                    td.put(Snapshot.getKey(), albumImagesModel);
                }
                ArrayList<AlbumMasterModel> values = new ArrayList<>(td.values());
                List<String> keys = new ArrayList<String>(td.keySet());
                for (AlbumMasterModel job : values) {
                    Log.d("firebase", job.getImg().toString());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("Exception", "onCancelled", databaseError.toException());
            }
        });

    }




   /* public ServiceConnection serviceConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className, IBinder service) {
            // cast the IBinder and get MyService instance
            LocationUpdateService.LocalBinder binder = (LocationUpdateService.LocalBinder) service;
            myservice = binder.getService();
            bound = true;
            myservice.setCallbacks(UploadPhotoFragment.this);
          *//*  myService.setCallbacks(DashboardFragment.this); // register
            myService.setCount(DashboardFragment.this);*//*
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            bound = false;
        }
    };*/


    public void getStudents(String className) {
        listStudents = new ArrayList<>();
        listStudentName = new ArrayList<>();
        DatabaseReference ref1 = getMyActivity().databaseReference.child(Constants.MAIN_TABLE);
        DatabaseReference ref2 = ref1.child(Constants.STUDENT);
        Query query = ref2.orderByChild("className").equalTo(className);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                progressDialog.dismiss();
                for (DataSnapshot Snapshot : dataSnapshot.getChildren()) {
                    UserModel studentData = Snapshot.getValue(UserModel.class);
                    listStudents.add(studentData);
                    listStudentName.add(studentData.getFirstName() + " " + studentData.getLastName() + " " + studentData.getUserInfoModel().getRegistrationId());
                }
                if (listStudents != null && listStudents.size() > 0) {
                    spinnerStudentName.setVisibility(View.VISIBLE);
                    txtStudentSpinnerTitle.setVisibility(View.VISIBLE);
                    ArrayAdapter<String> classAdapter = new ArrayAdapter<String>(getMyActivity(), android.R.layout.simple_spinner_item, listStudentName);
                    classAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinnerStudentName.setAdapter(classAdapter);
                    classAdapter.notifyDataSetChanged();
                } else {
                    spinnerStudentName.setVisibility(View.GONE);
                    txtStudentSpinnerTitle.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("Exception", "onCancelled", databaseError.toException());
                progressDialog.dismiss();
            }
        });

    }

    private void save() {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put(IJson.imgTitle, albumImagesModel.getAlbumTitle());
        hashMap.put(IJson.description, albumImagesModel.getAlbumDescription());
        // hashMap.put(IJson.userName, albumImagesModel.getCreatedBy());
        hashMap.put(IJson.imageType, albumImagesModel.getAlbumType());
        hashMap.put(IJson.className, albumImagesModel.getClassName());
        hashMap.put(IJson.division, albumImagesModel.getDivisionName() == null ? "All" : albumImagesModel.getDivisionName());
        hashMap.put(IJson.studentId, albumImagesModel.getStudentId() == null ? "All" : albumImagesModel.getStudentId());

        hashMap.put(IJson.images, albumImagesModel.getAlbumImagesModel());

        CallWebService.getWebserviceObject(getMyActivity(), true, true, Request.Method.POST, IUrls.URL_IMAGE_UPLOAD, hashMap, new VolleyResponseListener<AlbumMasterModel>() {
            @Override
            public void onResponse(AlbumMasterModel[] object) {

            }

            @Override
            public void onResponse(AlbumMasterModel studentData) {
                getMyActivity().showFragment(new DashboardFragment(), null);
            }

            @Override
            public void onResponse() {
            }

            @Override
            public void onError(String message) {
                if (message != null && !message.isEmpty()) {
                    Toast.makeText(getMyActivity(), message, Toast.LENGTH_SHORT).show();
                }
            }
        }, AlbumMasterModel.class);

    }


    public void getStudent(String classId, String divisionId) {
        if (!apiFlag) {
            apiFlag = true;
            HashMap<String, Object> hashMap = new HashMap<>();
      /*  hashMap.put(IJson.userId, userModel.getPkeyId());
        hashMap.put(IJson.otp, txtOtp.getText().toString());*/
            String url = String.format(IUrls.URL_CLASS_WISE_STUDENT, classId, divisionId);
            Log.d("url", url);
            CallWebService.getWebservice(getMyActivity(), Request.Method.GET, url, hashMap, new VolleyResponseListener<StudentModel>() {
                @Override
                public void onResponse(StudentModel[] object) {
                    listStudent = new ArrayList<>();
                    StudentModel allStudent = new StudentModel();
                    allStudent.setFullName("All");
                    allStudent.setPkeyId(null);
                    listStudent.add(allStudent);
                    for (StudentModel studentModel : object) {
                        listStudent.add(studentModel);
                    }
                    ArrayAdapter<StudentModel> studentAdapter = new ArrayAdapter(getMyActivity(), android.R.layout.simple_spinner_item, listStudent);
                    studentAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinnerStudentName.setAdapter(studentAdapter);
                    studentAdapter.notifyDataSetChanged();
                    apiFlag = false;
                }

                @Override
                public void onResponse() {
                }

                @Override
                public void onResponse(StudentModel object) {

                }

                @Override
                public void onError(String message) {
                    spinnerStudentName.setVisibility(View.GONE);
                    txtStudentSpinnerTitle.setVisibility(View.GONE);
                    if (message != null && !message.isEmpty()) {
                        getMyActivity().showToast(message);
                    }
                    apiFlag = false;
                }
            }, StudentModel[].class);

        }
    }


    public HomeActivity getMyActivity() {
        return (HomeActivity) getActivity();
    }


}