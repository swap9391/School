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
import com.exa.mydemoapp.model.ImageModel;
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
            getImageList();
        } else {
            getMyActivity().showToast("Please Connect to internet !!");
        }
        return view;
    }

    /*private void getImageData() {
        String userId = getMyActivity().databaseReference.push().getKey();
        String studentId = null;
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

    }*/


    private void ShowList(final List<ImageRequest> imageRequestList) {
      /*  List<ImageRequest> newImagereqList = new ArrayList<>();
        for (ImageRequest imageRequest : imageRequestList) {
            for (ImageModel imageModel : imageRequest.getImages()) {
                imageRequest.setImg(imageModel.getImgUrl());
                newImagereqList.add(imageRequest);
                break;
            }
        }*/
        List<ImageRequest> newImagereqList = new ArrayList<>();
        for (ImageRequest imageRequest : imageRequestList) {
            for (int i = 0; i < imageRequest.getImages().size(); i++) {
                ImageRequest iRequest = new ImageRequest();
                iRequest.setPlaceName(imageRequest.getPlaceName());
                iRequest.setImages(imageRequest.getImages());
                iRequest.setImg(imageRequest.getImages().get(i).getImgUrl());
                newImagereqList.add(i, iRequest);
                break;
            }
        }

        mAdapter = new GalleryAdapter(getMyActivity(), newImagereqList, imageRequestList.size(), null);

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getMyActivity(), 2);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);


        recyclerView.addOnItemTouchListener(new GalleryAdapter.RecyclerTouchListener(getMyActivity(), recyclerView, new GalleryAdapter.ClickListener() {
            @Override
            public void onClick(View view, int position) {

                ImageRequest imageRequest = newImagereqList.get(position);
                List<ImageRequest> listAlbumChild = new ArrayList<ImageRequest>();

                listAlbumChild.add(imageRequest);

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
        Map<Integer, ImageRequest> td = new HashMap<>();
        Map<String, ImageRequest> mapAlbum = new HashMap<>();
        String studentId = "0";
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put(IJson.studentId, studentId);
        hashMap.put(IJson.imageType, getStringById(R.string.img_type_gallery));
        // hashMap.put(IJson.password, "" + studentId);

        CallWebService.getWebservice(getMyActivity(), Request.Method.POST, IUrls.URL_IMAGE_LIST, hashMap, new VolleyResponseListener<ImageRequest>() {
            @Override
            public void onResponse(ImageRequest[] object) {
                if (object[0] instanceof ImageRequest) {
                    int count = 0;
                    for (ImageRequest bean : object) {
                        count++;
                        if (mapAlbum.get(bean.getPlaceName()) != null) {
                            ArrayList<ImageModel> tempList = new ArrayList<>();
                            ImageRequest tempImageRequest = new ImageRequest();
                            bean.getImages().addAll(mapAlbum.get(bean.getPlaceName()).getImages());
                            mapAlbum.put(bean.getPlaceName(), bean);
                        } else {
                            mapAlbum.put(bean.getPlaceName(), bean);
                        }

                        td.put(count, bean);
                    }
                    images = new ArrayList<>(td.values());
                    List<String> albumNames = new ArrayList<String>(mapAlbum.keySet());
                    List<ImageRequest> listAlbum = new ArrayList<ImageRequest>();
                    for (String string : albumNames) {
                        listAlbum.add(mapAlbum.get(string));
                    }
                    ShowList(listAlbum);

                }
            }

            @Override
            public void onResponse(ImageRequest object) {

            }

            @Override
            public void onError(String message) {
            }
        }, ImageRequest[].class);

    }

    public HomeActivity getMyActivity() {
        return (HomeActivity) getActivity();
    }
}