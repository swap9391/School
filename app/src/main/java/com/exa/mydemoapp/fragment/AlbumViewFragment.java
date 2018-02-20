package com.exa.mydemoapp.fragment;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Request;
import com.exa.mydemoapp.Common.CommonUtils;
import com.exa.mydemoapp.Common.Connectivity;
import com.exa.mydemoapp.Common.Constants;
import com.exa.mydemoapp.HomeActivity;
import com.exa.mydemoapp.R;
import com.exa.mydemoapp.adapter.GalleryAdapter;
import com.exa.mydemoapp.annotation.ViewById;
import com.exa.mydemoapp.model.ImageRequest;
import com.exa.mydemoapp.model.StudentModel;
import com.exa.mydemoapp.webservice.CallWebService;
import com.exa.mydemoapp.webservice.IJson;
import com.exa.mydemoapp.webservice.IUrls;
import com.exa.mydemoapp.webservice.VolleyResponseListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by midt-006 on 21/8/17.
 */

public class AlbumViewFragment extends CommonFragment {
    private ArrayList<ImageRequest> images;
    private GalleryAdapter mAdapter;
    @ViewById(R.id.recycler_view)
    private RecyclerView recyclerView;
    View view;
    ProgressDialog progressDialog;
    List<ImageRequest> listAlbumChild;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.layout_gallery, container, false);
        getMyActivity().toolbar.setTitle("Image Album");
        getMyActivity().init();
        initViewBinding(view);
        images = new ArrayList<>();

        if (Connectivity.isConnected(getMyActivity())) {
            progressDialog = new ProgressDialog(getMyActivity());
            progressDialog.setTitle("Loading...");
            progressDialog.show();
            getImageData();
        } else {
            getMyActivity().showToast("Please Connect to internet !!");
        }
        return view;
    }

    private void getImageData() {
        String userId = getMyActivity().databaseReference.push().getKey();
        String studentId = getMyActivity().getStudentInfoSingleton().getStudentModel().getUniqKey();
        DatabaseReference ref1 = getMyActivity().databaseReference.child(Constants.MAIN_TABLE);
        DatabaseReference ref2 = ref1.child(Constants.IMAGE_TABLE);
        Query query = ref2.orderByChild("imageType").equalTo("Gallery");
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Map<String, ImageRequest> td = new HashMap<String, ImageRequest>();
                Map<String, ImageRequest> mapAlbum = new HashMap<String, ImageRequest>();
                for (DataSnapshot Snapshot : dataSnapshot.getChildren()) {
                    ImageRequest imageRequest = Snapshot.getValue(ImageRequest.class);
                    if (imageRequest.getVisiblity().equalsIgnoreCase("TRUE")) {
                        if (!imageRequest.getStudentId().equals("NA") && !imageRequest.getStudentId().equals(studentId)) {

                        } else {
                            mapAlbum.put(imageRequest.getPlaceName(), imageRequest);
                            td.put(Snapshot.getKey(), imageRequest);
                        }
                    }
                }

                images = new ArrayList<>(td.values());
                List<String> albumNames = new ArrayList<String>(mapAlbum.keySet());
                List<ImageRequest> listAlbum = new ArrayList<ImageRequest>();
                for (String string : albumNames) {
                    listAlbum.add(mapAlbum.get(string));
                }
                ShowList(listAlbum);
                progressDialog.dismiss();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("Exception", "onCancelled", databaseError.toException());
                progressDialog.dismiss();
            }
        });

    }


    private void ShowList(final List<ImageRequest> imageRequestList) {
        mAdapter = new GalleryAdapter(getMyActivity(), imageRequestList, imageRequestList.size(), null);

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getMyActivity(), 2);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);


        recyclerView.addOnItemTouchListener(new GalleryAdapter.RecyclerTouchListener(getMyActivity(), recyclerView, new GalleryAdapter.ClickListener() {
            @Override
            public void onClick(View view, int position) {

                ImageRequest imageRequest = imageRequestList.get(position);
                List<ImageRequest> listAlbumChild = new ArrayList<ImageRequest>();
                for (ImageRequest img : images) {
                    if (imageRequest.getPlaceName().equals(img.getPlaceName())) {
                        listAlbumChild.add(img);
                    }
                }

                Bundle bundle = new Bundle();
                bundle.putSerializable("mylist", (Serializable) listAlbumChild);
                getMyActivity().showFragment(new GalleryViewFragment(), bundle);

            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));
    }

    private void getImageList() {
        String studentId = getMyActivity().getStudentInfoSingleton().getStudentModel().getUniqKey();
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put(IJson.mobile_no, "" + getStringById(R.string.img_type_gallery));
        hashMap.put(IJson.password, "" + studentId);

        CallWebService.getWebservice(getMyActivity(), Request.Method.POST, IUrls.URL_LOGIN, hashMap, new VolleyResponseListener<StudentModel>() {
            @Override
            public void onResponse(StudentModel[] object) {
                if (object[0] instanceof StudentModel) {
                    for (StudentModel bean : object) {

                    }
                }
            }

            @Override
            public void onError(String message) {
            }
        }, StudentModel[].class);

    }

    public HomeActivity getMyActivity() {
        return (HomeActivity) getActivity();
    }
}