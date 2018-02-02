package com.exa.mydemoapp.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.exa.mydemoapp.HomeActivity;
import com.exa.mydemoapp.R;
import com.exa.mydemoapp.annotation.ViewById;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by midt-006 on 16/1/18.
 */

public class AboutSchoolFragment extends CommonFragment {
    View view;
    @ViewById(R.id.txt_school_title)
    TextView txtSchoolTitle;
    @ViewById(R.id.txt_school_address)
    TextView txtSchoolAddress;
    @ViewById(R.id.txt_school_content)
    TextView txtSchoolContent;
    @ViewById(R.id.img_school_logo)
    CircleImageView circleImageView;
    // private String path = "https://firebasestorage.googleapis.com/v0/b/prawas-9af67.appspot.com/o/images%2FimageMon+Oct+30+20%3A49%3A17+GMT%2B05%3A30+2017.jpg?alt=media&token=e9243a74-ef83-42dc-b3e6-2147f3e647ee";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_about_school, container, false);
        getMyActivity().getToolbar().setVisibility(View.GONE);
        getMyActivity().init();
        initViewBinding(view);

        txtSchoolTitle.setText("Kalpatru Kids");
        txtSchoolAddress.setText("Pune");
        txtSchoolContent.setText(getMyActivity().getResources().getString(R.string.school_content));

      /*  Glide.with(getMyActivity()).load(R.mipmap.ic_launcher)
                .thumbnail(0.5f)
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.drawable.defualt_album_icon)
                .into(circleImageView);*/

        return view;
    }


    private HomeActivity getMyActivity() {
        return (HomeActivity) getActivity();
    }
}

