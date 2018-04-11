package com.exa.mydemoapp.fragment;

import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.exa.mydemoapp.HomeActivity;
import com.exa.mydemoapp.R;
import com.exa.mydemoapp.adapter.GalleryAdapter;
import com.exa.mydemoapp.annotation.ViewById;
import com.exa.mydemoapp.model.AlbumMasterModel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by midt-006 on 4/8/17.
 */

public class GalleryViewFragment extends CommonFragment {
    private ArrayList<AlbumMasterModel> images;
    private GalleryAdapter mAdapter;
    @ViewById(R.id.recycler_view)
    private RecyclerView recyclerView;
    View view;

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
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            images = (ArrayList<AlbumMasterModel>) bundle.getSerializable("mylist");
        }
        ShowList(images);
        return view;
    }

    private void ShowList(List<AlbumMasterModel> albumImagesModelList) {
        List<AlbumMasterModel> newImagereqList = new ArrayList<>();
        for (AlbumMasterModel albumImagesModel : albumImagesModelList) {
            for (int i = 0; i < albumImagesModel.getAlbumImagesModels().size(); i++) {
                AlbumMasterModel iRequest = new AlbumMasterModel();
                iRequest.setAlbumTitle(albumImagesModel.getAlbumTitle());
                iRequest.setAlbumImagesModels(albumImagesModel.getAlbumImagesModels());
                iRequest.setImg(albumImagesModel.getAlbumImagesModels().get(i).getImageUrl());
                newImagereqList.add(i, iRequest);
            }
        }


        mAdapter = new GalleryAdapter(getMyActivity(), newImagereqList, 0, null);

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getMyActivity(), 2);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);


        recyclerView.addOnItemTouchListener(new GalleryAdapter.RecyclerTouchListener(getMyActivity(), recyclerView, new GalleryAdapter.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("images", (Serializable) newImagereqList);
                bundle.putInt("position", position);
                bundle.putString("frag", "gallery");
                bundle.putBoolean("Guest", getMyActivity().isGuest);
           /*     FragmentTransaction ft = getMyActivity().getSupportFragmentManager().beginTransaction();
                SlideshowDialogFragment newFragment = new SlideshowDialogFragment();
                newFragment.setArguments(bundle);
                newFragment.show(ft, "slideshow");*/
                getMyActivity().setGallery(true);
                getMyActivity().showFragment(new SlideshowDialogFragment(), bundle);

            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));
    }

    public HomeActivity getMyActivity() {
        return (HomeActivity) getActivity();
    }
}
