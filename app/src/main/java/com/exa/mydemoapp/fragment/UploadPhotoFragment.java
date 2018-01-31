package com.exa.mydemoapp.fragment;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
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

import com.bumptech.glide.Glide;
import com.exa.mydemoapp.Common.CommonUtils;
import com.exa.mydemoapp.Common.Connectivity;
import com.exa.mydemoapp.Common.Constants;
import com.exa.mydemoapp.Common.FloatingActionButton;
import com.exa.mydemoapp.HomeActivity;
import com.exa.mydemoapp.R;
import com.exa.mydemoapp.annotation.ViewById;
import com.exa.mydemoapp.model.ImageRequest;
import com.exa.mydemoapp.model.LocationModel;
import com.exa.mydemoapp.model.StudentModel;
import com.exa.mydemoapp.service.LocationUpdateService;
import com.exa.mydemoapp.service.ServiceCallbacks;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Swapnil Jadhav on 3/8/17.
 */

public class UploadPhotoFragment extends CommonFragment implements View.OnClickListener, ServiceCallbacks {

    Uri fileView;
    List<Uri> imglist = new ArrayList<>();
    private int REQUEST_CAMERA = 101, PICK_IMAGE = 102;
    Uri imageUri;
    int count = 0;
    ImageRequest imageRequest;
    LocationUpdateService myservice;
    private boolean bound = false;
    View view;
    boolean isEdit = false;
    List<String> listEventType;
    ArrayList<ImageRequest> imageRequestArrayList;
    @ViewById(R.id.lattitude)
    TextView lat;
    @ViewById(R.id.longitude)
    TextView longit;
    @ViewById(R.id.edt_title)
    private EditText edt_title;
    @ViewById(R.id.edt_description)
    private EditText edt_description;
    @ViewById(R.id.edt_user_name)
    private EditText edt_user;
    @ViewById(R.id.spinner_image_type)
    private Spinner spinnerType;
    @ViewById(R.id.spinner_class_name)
    private Spinner spnClass;
    @ViewById(R.id.spinner_student_name)
    private Spinner spinnerStudentName;
    @ViewById(R.id.txt_student_spinner)
    private TextView txtStudentSpinnerTitle;


    private List<String> listClass;
    private List<StudentModel> listStudents;
    private List<String> listStudentName;
    ProgressDialog progressDialog;
    int totalImages;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.upload_info_layout, container, false);
        getMyActivity().toolbar.setTitle("Upload Photo");
        getMyActivity().init();
        initViewBinding(view);

        listEventType = Arrays.asList(getResources().getStringArray(R.array.image_type));
        ArrayAdapter<String> eventAdapter = new ArrayAdapter<String>(getMyActivity(), android.R.layout.simple_spinner_item, listEventType);
        eventAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerType.setAdapter(eventAdapter);
        eventAdapter.notifyDataSetChanged();


        listClass = Arrays.asList(getResources().getStringArray(R.array.class_type));
        ArrayAdapter<String> classAdapter = new ArrayAdapter<String>(getMyActivity(), android.R.layout.simple_spinner_item, listClass);
        classAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnClass.setAdapter(classAdapter);
        classAdapter.notifyDataSetChanged();

        spnClass.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                if (!listClass.get(position).equals("All")) {

                    if (Connectivity.isConnected(getMyActivity())) {
                        progressDialog = new ProgressDialog(getMyActivity());
                        progressDialog.setTitle("Loading Student List...");
                        progressDialog.show();
                        getStudents(listClass.get(position));
                    } else {
                        getMyActivity().showToast("Please Connect to internet !!");
                    }
                } else {
                    spinnerStudentName.setVisibility(View.GONE);
                    txtStudentSpinnerTitle.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }
        });


        Bundle bundle = getArguments();
        imageRequestArrayList = new ArrayList<>();

        if (bundle != null) {
            imageRequestArrayList = (ArrayList<ImageRequest>) bundle.getSerializable("imageData");
        }
        if (imageRequestArrayList != null && imageRequestArrayList.size() > 0) {
            imageRequest = imageRequestArrayList.get(0);
            bindView();
            isEdit = true;
        } else {
            imageRequest = new ImageRequest();
            isEdit = false;
        }

        LinearLayout yourframelayout = (LinearLayout) view.findViewById(R.id.btnFloat);
        FloatingActionButton fabButton = new FloatingActionButton.Builder(getMyActivity(), yourframelayout)
                .withDrawable(getResources().getDrawable(R.drawable.ic_plus))
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
                    if (Validator.validateForNulls(imageRequest, getMyActivity())) {
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
        imageRequest.setPlaceName(edt_title.getText().toString().trim());
        imageRequest.setDescription(edt_description.getText().toString().trim());
        imageRequest.setUserName(edt_user.getText().toString().trim());
        imageRequest.setImageType(spinnerType.getSelectedItem().toString());
        imageRequest.setClassName(spnClass.getSelectedItem().toString());
        if (listStudentName != null && listStudentName.size() > 0) {
            String studentId = listStudents.get(spinnerStudentName.getSelectedItemPosition()).getUniqKey();
            if (studentId != null) {
                imageRequest.setStudentId(studentId);
            }
        } else {
            imageRequest.setStudentId("NA");
        }

        imageRequest.setDateStamp(CommonUtils.formatDateForDisplay(Calendar.getInstance().getTime(), Constants.DATE_FORMAT));
        imageRequest.setVisiblity("TRUE");
    }

    private void bindView() {
        edt_title.setText(imageRequest.getPlaceName());
        edt_description.setText(imageRequest.getDescription());
        edt_user.setText(imageRequest.getUserName());
        int eventPosition = listEventType.indexOf(imageRequest.getImageType());
        spinnerType.setSelection(eventPosition);
        if (isEdit) {
            edt_title.setEnabled(false);
        }
    }

    private void saveUserInformation() {
        final String userId = getMyActivity().databaseReference.push().getKey();
        if (!isEdit) {
            bindModel();
            imageRequest.setUniqKey(userId);
            getMyActivity().databaseReference.child(Constants.MAIN_TABLE).child(Constants.IMAGE_TABLE).child(userId).setValue(imageRequest);
            Toast.makeText(getMyActivity(), "Information Saved...", Toast.LENGTH_LONG).show();
        } else {
            bindModel();
            for (ImageRequest imageRequest1 : imageRequestArrayList) {
                imageRequest.setUniqKey(imageRequest1.getUniqKey());
                getMyActivity().databaseReference.child(Constants.MAIN_TABLE).child(Constants.IMAGE_TABLE).child(imageRequest.getUniqKey()).setValue(imageRequest);
                Toast.makeText(getMyActivity(), "Information Saved...", Toast.LENGTH_LONG).show();
            }
            isEdit = false;
            if (imglist.size() > 0) {
                uploadImage();
            }
        }
    }

    @SuppressWarnings("VisibleForTests")
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
                        imageRequest.setImg(downloadUrl.toString());
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

    }


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
                    getMyActivity().showToast("Try Again");
                }

            } else {
                getMyActivity().showToast("Capture Cancelled");
            }


        }

        if (requestCode == PICK_IMAGE && resultCode == getMyActivity().RESULT_OK
                && null != data) {

            try {

             /*   Uri selectedImage = data.getData();
                String[] filePathColumn = {MediaStore.Images.Media.DATA};

                Cursor cursor = getContentResolver().query(selectedImage,
                        filePathColumn, null, null, null);
                cursor.moveToFirst();

                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                String picturePath = cursor.getString(columnIndex);
                cursor.close();

                Bitmap bitmap = BitmapFactory.decodeFile(picturePath);

                setImage(bitmap);*/


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
                imglist.add(fileView);
                bindView(fileView, true);

            }
        } catch (Exception e) {

        }
    }

    private void setImage(List<Uri> listOfUri) {
        totalImages = listOfUri.size();
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
                bindView(fileView, false);


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
        imageRequest = new ImageRequest();
        LinearLayout layout1 = (LinearLayout) view.findViewById(R.id.lay1);
        layout1.removeAllViews();
        edt_title.getText().clear();
        edt_description.getText().clear();
        edt_user.getText().clear();
    }


    @Override
    public void onClick(View view) {

    }

    private boolean check() {
        if (imageRequest.getClassName() == null || imageRequest.getClassName().equals("")) {
            getMyActivity().showToast("Please Select Class Name");
            return false;
        }
        if (!imageRequest.getClassName().equalsIgnoreCase("All")) {
            if (imageRequest.getStudentId() == null || imageRequest.getStudentId().equals("")) {
                getMyActivity().showToast("Please Select Student Name");
                return false;
            }
        }
        if (imageRequest.getUserName() == null || imageRequest.getUserName().equals("")) {
            getMyActivity().showToast("Please Enter User Name");
            return false;
        }
        if (imageRequest.getPlaceName() == null || imageRequest.getPlaceName().equals("")) {
            getMyActivity().showToast("Please Enter Place Name");
            return false;
        }
        if (imageRequest.getDescription() == null || imageRequest.getDescription().equals("")) {
            getMyActivity().showToast("Please Enter Description");
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
                    builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (check()) {
                                if (Connectivity.isConnected(getMyActivity())) {
                                    if (!isEdit && imglist.size() > 0) {
                                        uploadImage();
                                    } else {
                                        saveUserInformation();
                                    }
                                } else {
                                    getMyActivity().showToast("Please Connect to internet !!");
                                }
                            }
                        }
                    }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
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
                Map<String, ImageRequest> td = new HashMap<String, ImageRequest>();
                for (DataSnapshot Snapshot : dataSnapshot.getChildren()) {
                    ImageRequest imageRequest = Snapshot.getValue(ImageRequest.class);

                    td.put(Snapshot.getKey(), imageRequest);
                }

                ArrayList<ImageRequest> values = new ArrayList<>(td.values());
                List<String> keys = new ArrayList<String>(td.keySet());

                for (ImageRequest job : values) {
                    Log.d("firebase", job.getImg().toString());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("Exception", "onCancelled", databaseError.toException());
            }
        });

    }

    private void getData() {
        //Query phoneQuery =databaseReference.orderByChild("lattitude");
        getMyActivity().databaseReference.child("location_db").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Map<String, LocationModel> td = new HashMap<String, LocationModel>();
                for (DataSnapshot Snapshot : dataSnapshot.getChildren()) {
                    LocationModel locationModel = Snapshot.getValue(LocationModel.class);
                    lat.setText(locationModel.getLattitude().toString());
                    longit.setText(locationModel.getLongitude().toString());
                    td.put(Snapshot.getKey(), locationModel);
                }

                ArrayList<LocationModel> values = new ArrayList<>(td.values());
                List<String> keys = new ArrayList<String>(td.keySet());

                for (LocationModel job : values) {
                    Log.d("firebase", job.getLattitude().toString());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("Exception", "onCancelled", databaseError.toException());
            }
        });
    }


    public ServiceConnection serviceConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className, IBinder service) {
            // cast the IBinder and get MyService instance
            LocationUpdateService.LocalBinder binder = (LocationUpdateService.LocalBinder) service;
            myservice = binder.getService();
            bound = true;
            myservice.setCallbacks(UploadPhotoFragment.this);
          /*  myService.setCallbacks(DashboardFragment.this); // register
            myService.setCount(DashboardFragment.this);*/
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            bound = false;
        }
    };

    @Override
    public void doSomething() {
        Log.e("Update", "Updated");
        getData();
    }

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
                    StudentModel studentData = Snapshot.getValue(StudentModel.class);
                    listStudents.add(studentData);
                    listStudentName.add(studentData.getStudentName());
                }
                if (listStudents != null && listStudents.size() > 0) {
                    spinnerStudentName.setVisibility(View.VISIBLE);
                    txtStudentSpinnerTitle.setVisibility(View.VISIBLE);
                    ArrayAdapter<String> classAdapter = new ArrayAdapter<String>(getMyActivity(), android.R.layout.simple_spinner_item, listStudentName);
                    classAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinnerStudentName.setAdapter(classAdapter);
                    classAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("Exception", "onCancelled", databaseError.toException());
                progressDialog.dismiss();
            }
        });

    }
    /* public void getMyValues() {
         HashMap<String, String> hashMap = new HashMap<>();
         // hashMap.put(IJson.userId, "" + CommonUtils.getSharedPref(getMyActivity(),IConstants.USER_ID));
         http://reportcard.ae/rest/api/v1/login?password=Qwerty12#$&username=soundvvn_trial&st=R5s3vGRuCG7DPPM25pckKaeWY8
         hashMap.put("password",  "Qwerty12#$");
         hashMap.put("username",  "soundvvn_trial");
         hashMap.put("st",  "R5s3vGRuCG7DPPM25pckKaeWY8");

         String url = "http://reportcard.ae/rest/api/v1/login";
         CallWebService.getWebservice(url, hashMap);
     }*/


    public HomeActivity getMyActivity() {
        return (HomeActivity) getActivity();
    }


}