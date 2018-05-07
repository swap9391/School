package com.exa.mydemoapp.fragment;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Request;
import com.exa.mydemoapp.Common.Connectivity;
import com.exa.mydemoapp.Common.Constants;
import com.exa.mydemoapp.HomeActivity;
import com.exa.mydemoapp.R;
import com.exa.mydemoapp.adapter.NewsFeedsAdapter;
import com.exa.mydemoapp.annotation.ViewById;
import com.exa.mydemoapp.model.AlbumMasterModel;
import com.exa.mydemoapp.model.UserModel;
import com.exa.mydemoapp.webservice.CallWebService;
import com.exa.mydemoapp.webservice.IJson;
import com.exa.mydemoapp.webservice.IUrls;
import com.exa.mydemoapp.webservice.VolleyResponseListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by midt-006 on 6/9/17.
 */

public class NewsFeedFragment extends CommonFragment {

    final List<AlbumMasterModel> questionList = new ArrayList<>();
    public NewsFeedsAdapter mAdapter;
    @ViewById(R.id.recyclerView)
    private RecyclerView recyclerView;
    List<String> albumNames;
    private ArrayList<AlbumMasterModel> AllImages;
    private ArrayList<AlbumMasterModel> CoverImages;
    private View view;
    ProgressDialog progressDialog;
    String feed;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.layout_news_feed, container, false);
        initViewBinding(view);
        Bundle bundle = getArguments();
        feed = bundle.getString(Constants.FEED);
        albumNames = new ArrayList<>();
        getMyActivity().init();
        if (Connectivity.isConnected(getMyActivity())) {
            getImageList();
        } else {
            getMyActivity().showToast(getString(R.string.no_internet));
        }
        getMyActivity().toolbar.setTitle(feed);


        return view;
    }


    private void getImageList() {
        Map<Integer, AlbumMasterModel> td = new HashMap<>();
        Map<String, AlbumMasterModel> mapAlbum = new HashMap<>();

        HashMap<String, Object> hashMap = new HashMap<>();


        UserModel userModel = getMyActivity().getUserModel();
        String imageType = feed;
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
                        mapAlbum.put(bean.getAlbumTitle(), bean);
                        td.put(count, bean);
                    }
                    AllImages = new ArrayList<>(td.values());
                    List<String> albumNames = new ArrayList<String>(mapAlbum.keySet());
                    CoverImages = new ArrayList<AlbumMasterModel>();
                    for (String string : albumNames) {
                        CoverImages.add(mapAlbum.get(string));
                    }
                    mAdapter = new NewsFeedsAdapter(AllImages, CoverImages, getMyActivity(), feed);
                    LinearLayoutManager mLayoutManager =
                            new LinearLayoutManager(getMyActivity());
                    recyclerView.setLayoutManager(mLayoutManager);
                    recyclerView.setItemAnimator(new DefaultItemAnimator());
                    recyclerView.setAdapter(mAdapter);

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
                getMyActivity().showToast(message);
            }
        }, AlbumMasterModel[].class);

    }


    public void getImageData() {
        String userId = getMyActivity().databaseReference.push().getKey();
        DatabaseReference ref1 = getMyActivity().databaseReference.child(Constants.MAIN_TABLE);
        DatabaseReference ref2 = ref1.child(Constants.IMAGE_TABLE);
        Query query = ref2.orderByChild("imageType").equalTo(feed);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Map<String, AlbumMasterModel> td = new HashMap<String, AlbumMasterModel>();
                Map<String, AlbumMasterModel> mapAlbum = new HashMap<String, AlbumMasterModel>();
                for (DataSnapshot Snapshot : dataSnapshot.getChildren()) {
                    AlbumMasterModel albumImagesModel = Snapshot.getValue(AlbumMasterModel.class);
                   /* if (albumImagesModel.getVisiblity().equalsIgnoreCase("TRUE")) {
                        mapAlbum.put(albumImagesModel.getPlaceName(), albumImagesModel);
                        td.put(Snapshot.getKey(), albumImagesModel);
                    }*/
                }

                AllImages = new ArrayList<>(td.values());
                List<String> albumNames = new ArrayList<String>(mapAlbum.keySet());
                CoverImages = new ArrayList<AlbumMasterModel>();
                for (String string : albumNames) {
                    CoverImages.add(mapAlbum.get(string));
                }
                mAdapter = new NewsFeedsAdapter(AllImages, CoverImages, getMyActivity(), feed);
                LinearLayoutManager mLayoutManager =
                        new LinearLayoutManager(getMyActivity());
                recyclerView.setLayoutManager(mLayoutManager);
                recyclerView.setItemAnimator(new DefaultItemAnimator());
                recyclerView.setAdapter(mAdapter);
                progressDialog.dismiss();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("Exception", "onCancelled", databaseError.toException());
                progressDialog.dismiss();
            }
        });

    }

    public HomeActivity getMyActivity() {
        return (HomeActivity) getActivity();
    }


}
