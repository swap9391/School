package com.exa.mydemoapp.fragment;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Request;
import com.exa.mydemoapp.Common.Connectivity;
import com.exa.mydemoapp.Common.Constants;
import com.exa.mydemoapp.HomeActivity;
import com.exa.mydemoapp.R;
import com.exa.mydemoapp.adapter.GalleryAdapter;
import com.exa.mydemoapp.annotation.ViewById;
import com.exa.mydemoapp.model.AlbumImagesModel;
import com.exa.mydemoapp.model.AlbumMasterModel;
import com.exa.mydemoapp.model.UserModel;
import com.exa.mydemoapp.webservice.CallWebService;
import com.exa.mydemoapp.webservice.IJson;
import com.exa.mydemoapp.webservice.IUrls;
import com.exa.mydemoapp.webservice.VolleyResponseListener;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by midt-006 on 21/8/17.
 */

public class AlbumViewFragment extends CommonFragment {
    private ArrayList<AlbumMasterModel> images;
    private GalleryAdapter mAdapter;
    @ViewById(R.id.recycler_view)
    private RecyclerView recyclerView;
    View view;
    ProgressDialog progressDialog;
    List<AlbumMasterModel> listAlbumChild;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.layout_gallery, container, false);
        getMyActivity().toolbar.setTitle(getStringById(R.string.dashboard_image_gallery));
        getMyActivity().init();
        initViewBinding(view);
        images = new ArrayList<>();

        if (Connectivity.isConnected(getMyActivity())) {
            getImageList();
        } else {
            getMyActivity().showToast(getStringById(R.string.no_internet));
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
                Map<String, AlbumMasterModel> td = new HashMap<String, AlbumMasterModel>();
                Map<String, AlbumMasterModel> mapAlbum = new HashMap<String, AlbumMasterModel>();
                for (DataSnapshot Snapshot : dataSnapshot.getChildren()) {
                    AlbumMasterModel albumImagesModel = Snapshot.getValue(AlbumMasterModel.class);
                    if (albumImagesModel.getVisiblity().equalsIgnoreCase("TRUE")) {
                        if (!albumImagesModel.getStudentId().equals("NA") && !albumImagesModel.getStudentId().equals(studentId)) {

                        } else {
                            mapAlbum.put(albumImagesModel.getPlaceName(), albumImagesModel);
                            td.put(Snapshot.getKey(), albumImagesModel);
                        }
                    }
                }

                images = new ArrayList<>(td.values());
                List<String> albumNames = new ArrayList<String>(mapAlbum.keySet());
                List<AlbumMasterModel> listAlbum = new ArrayList<AlbumMasterModel>();
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


    private void ShowList(final List<AlbumMasterModel> albumImagesModelList) {
      /*  List<AlbumMasterModel> newImagereqList = new ArrayList<>();
        for (AlbumMasterModel albumImagesModel : albumImagesModelList) {
            for (ImageModel imageModel : albumImagesModel.getImages()) {
                albumImagesModel.setImg(imageModel.getImgUrl());
                newImagereqList.add(albumImagesModel);
                break;
            }
        }*/
        List<AlbumMasterModel> newImagereqList = new ArrayList<>();
        for (AlbumMasterModel albumImagesModel : albumImagesModelList) {
            for (int i = 0; i < albumImagesModel.getAlbumImagesModel().size(); i++) {
                AlbumMasterModel iRequest = new AlbumMasterModel();
                iRequest.setAlbumTitle(albumImagesModel.getAlbumTitle());
                iRequest.setAlbumImagesModel(albumImagesModel.getAlbumImagesModel());
                iRequest.setImg(albumImagesModel.getAlbumImagesModel().get(i).getImageUrl());
                newImagereqList.add(i, iRequest);
                break;
            }
        }

        mAdapter = new GalleryAdapter(getMyActivity(), newImagereqList, albumImagesModelList.size(), null);

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getMyActivity(), 2);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);


        recyclerView.addOnItemTouchListener(new GalleryAdapter.RecyclerTouchListener(getMyActivity(), recyclerView, new GalleryAdapter.ClickListener() {
            @Override
            public void onClick(View view, int position) {

                AlbumMasterModel albumImagesModel = newImagereqList.get(position);
                List<AlbumMasterModel> listAlbumChild = new ArrayList<AlbumMasterModel>();

                listAlbumChild.add(albumImagesModel);

                Bundle bundle = new Bundle();
                bundle.putSerializable(Constants.LIST_TYPE, (Serializable) listAlbumChild);
                getMyActivity().showFragment(new GalleryViewFragment(), bundle);

            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));
    }

    private void getImageList() {
        Map<Integer, AlbumMasterModel> td = new HashMap<>();
        Map<String, AlbumMasterModel> mapAlbum = new HashMap<>();

        HashMap<String, Object> hashMap = new HashMap<>();


        UserModel userModel = getMyActivity().getUserModel();
        String imageType = getStringById(R.string.img_type_gallery);
        String className = userModel.getUserInfoModel().getClassName();
        String divisionName = userModel.getUserInfoModel().getDivisionName();
        String studentId = userModel.getPkeyId();

        String url;
        if (userModel.getUserType().equals(getStringById(R.string.user_type_student))) {
            url = String.format(IUrls.URL_IMAGE_LIST, imageType, className, divisionName, studentId);
        }
        else  {
            url = String.format(IUrls.URL_IMAGE_LIST, imageType, "All", "All", "All");
        }

        //http://localhost:8080/kalpataru/api/album/images?albumType=Gallery&className=All&divisionName=All&studentId=All
        CallWebService.getWebservice(getMyActivity(), Request.Method.GET, url, hashMap, new VolleyResponseListener<AlbumMasterModel>() {
            @Override
            public void onResponse(AlbumMasterModel[] object) {
                if (object[0] instanceof AlbumMasterModel) {
                    int count = 0;
                    for (AlbumMasterModel bean : object) {
                        count++;
                        if (mapAlbum.get(bean.getAlbumTitle()) != null) {
                            ArrayList<AlbumImagesModel> tempList = new ArrayList<>();
                            AlbumMasterModel tempAlbumImagesModel = new AlbumMasterModel();
                            bean.getAlbumImagesModel().addAll(mapAlbum.get(bean.getAlbumTitle()).getAlbumImagesModel());
                            mapAlbum.put(bean.getAlbumTitle(), bean);
                        } else {
                            mapAlbum.put(bean.getAlbumTitle(), bean);
                        }

                        td.put(count, bean);
                    }
                    images = new ArrayList<>(td.values());
                    List<String> albumNames = new ArrayList<String>(mapAlbum.keySet());
                    List<AlbumMasterModel> listAlbum = new ArrayList<AlbumMasterModel>();
                    for (String string : albumNames) {
                        listAlbum.add(mapAlbum.get(string));
                    }
                    ShowList(listAlbum);

                }
            }

            @Override
            public void onResponse() {
            }

            @Override
            public void onResponse(AlbumMasterModel object) {

            }

            @Override
            public void onError(String message) {
            }
        }, AlbumMasterModel[].class);

    }

    public HomeActivity getMyActivity() {
        return (HomeActivity) getActivity();
    }
}