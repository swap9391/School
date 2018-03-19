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
import com.exa.mydemoapp.model.ImageRequest;
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

    final List<ImageRequest> questionList = new ArrayList<>();
    public NewsFeedsAdapter mAdapter;
    @ViewById(R.id.recyclerView)
    private RecyclerView recyclerView;
    List<String> albumNames;
    private ArrayList<ImageRequest> AllImages;
    private ArrayList<ImageRequest> CoverImages;
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
        feed = bundle.getString("FEED");
        albumNames = new ArrayList<>();
        getMyActivity().init();
        if (Connectivity.isConnected(getMyActivity())) {
            getImageList();
        } else {
            getMyActivity().showToast("Please Connect to internet !!");
        }
        getMyActivity().toolbar.setTitle(feed);


        return view;
    }


    private void getImageList() {
        Map<Integer, ImageRequest> td = new HashMap<>();
        Map<String, ImageRequest> mapAlbum = new HashMap<>();
        String studentId = "0";
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put(IJson.studentId, studentId);
        hashMap.put(IJson.imageType, getStringById(R.string.img_type_news));
        // hashMap.put(IJson.password, "" + studentId);

        CallWebService.getWebservice(getMyActivity(), Request.Method.POST, IUrls.URL_IMAGE_LIST, hashMap, new VolleyResponseListener<ImageRequest>() {
            @Override
            public void onResponse(ImageRequest[] object) {
                if (object[0] instanceof ImageRequest) {
                    int count = 0;
                    for (ImageRequest bean : object) {
                        count++;
                        mapAlbum.put(bean.getPlaceName(), bean);
                        td.put(count, bean);
                    }
                    AllImages = new ArrayList<>(td.values());
                    List<String> albumNames = new ArrayList<String>(mapAlbum.keySet());
                    CoverImages = new ArrayList<ImageRequest>();
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
            public void onResponse(ImageRequest object) {

            }

            @Override
            public void onError(String message) {
                getMyActivity().showToast(message);
            }
        }, ImageRequest[].class);

    }


    public void getImageData() {
        String userId = getMyActivity().databaseReference.push().getKey();
        DatabaseReference ref1 = getMyActivity().databaseReference.child(Constants.MAIN_TABLE);
        DatabaseReference ref2 = ref1.child(Constants.IMAGE_TABLE);
        Query query = ref2.orderByChild("imageType").equalTo(feed);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Map<String, ImageRequest> td = new HashMap<String, ImageRequest>();
                Map<String, ImageRequest> mapAlbum = new HashMap<String, ImageRequest>();
                for (DataSnapshot Snapshot : dataSnapshot.getChildren()) {
                    ImageRequest imageRequest = Snapshot.getValue(ImageRequest.class);
                    if (imageRequest.getVisiblity().equalsIgnoreCase("TRUE")) {
                        mapAlbum.put(imageRequest.getPlaceName(), imageRequest);
                        td.put(Snapshot.getKey(), imageRequest);
                    }
                }

                AllImages = new ArrayList<>(td.values());
                List<String> albumNames = new ArrayList<String>(mapAlbum.keySet());
                CoverImages = new ArrayList<ImageRequest>();
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
