package com.exa.mydemoapp.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.DialogFragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.DownloadListener;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.exa.mydemoapp.Common.CommonUtils;
import com.exa.mydemoapp.Common.Constants;
import com.exa.mydemoapp.HomeActivity;
import com.exa.mydemoapp.R;
import com.exa.mydemoapp.model.ImageRequest;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by Swapnil on 4/8/17.
 */

public class SlideshowDialogFragment extends DialogFragment {
    private String TAG = SlideshowDialogFragment.class.getSimpleName();
    private ArrayList<ImageRequest> images;
    private ViewPager viewPager;
    private MyViewPagerAdapter myViewPagerAdapter;
    private TextView lblCount, lblTitle;
    private int selectedPosition = 0;
    private String fragmentFrom;

    static SlideshowDialogFragment newInstance() {
        SlideshowDialogFragment f = new SlideshowDialogFragment();
        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_image_slider, container, false);
        viewPager = (ViewPager) v.findViewById(R.id.viewpager);
        lblCount = (TextView) v.findViewById(R.id.lblCount);
        lblTitle = (TextView) v.findViewById(R.id.lblTitle);
        selectedPosition = getArguments().getInt("position");
        fragmentFrom = getArguments().getString("frag");
        images = (ArrayList<ImageRequest>) getArguments().getSerializable("images");

        getMyActivity().setListAlbumChild(images);

        myViewPagerAdapter = new MyViewPagerAdapter();
        viewPager.setAdapter(myViewPagerAdapter);
        viewPager.addOnPageChangeListener(viewPagerPageChangeListener);

        setCurrentItem(selectedPosition);

        return v;
    }

    private void setCurrentItem(int position) {
        viewPager.setCurrentItem(position, false);
        displayMetaInfo(selectedPosition);
    }

    //  page change listener
    ViewPager.OnPageChangeListener viewPagerPageChangeListener = new ViewPager.OnPageChangeListener() {

        @Override
        public void onPageSelected(int position) {
            displayMetaInfo(position);
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {

        }

        @Override
        public void onPageScrollStateChanged(int arg0) {

        }
    };

    private void displayMetaInfo(int position) {
        if (position > 0) {
            lblCount.setText((position + 1) + " of " + images.size());
        }
        ImageRequest image = images.get(position);
        lblTitle.setText(image.getPlaceName() == null ? "" : image.getPlaceName());
        //  lblDate.setText(image.getDescription());
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
    }

    //  adapter
    public class MyViewPagerAdapter extends PagerAdapter {

        private LayoutInflater layoutInflater;
        private ImageButton btnShare;
        private ImageButton btnDownload;
        private ImageButton btnDelete;

        public MyViewPagerAdapter() {
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {

            layoutInflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = layoutInflater.inflate(R.layout.image_fullscreen_preview, container, false);

            ImageView imageViewPreview = (ImageView) view.findViewById(R.id.image_preview);
            btnShare = (ImageButton) view.findViewById(R.id.btn_share);
            btnDownload = (ImageButton) view.findViewById(R.id.btn_download);
            btnDelete = (ImageButton) view.findViewById(R.id.btn_delete);

            final ImageRequest image = images.get(position);

            Glide.with(getActivity()).load(image.getImg())
                    .thumbnail(0.5f)
                    .crossFade()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(imageViewPreview);

            container.addView(view);


            btnShare.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    shareImage(image);
                }
            });
            btnDownload.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    downloadImage(image.getImg());
                }
            });
            if (fragmentFrom.equals("community")) {
                btnDelete.setVisibility(View.GONE);
            } else {
                btnDelete.setVisibility(View.VISIBLE);
                btnDelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        deleteImage(image);
                    }
                });
            }


            return view;
        }

        @Override
        public int getCount() {
            return images.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object obj) {
            return view == ((View) obj);
        }


        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }

    private void shareImage(ImageRequest imageRequest) {

        Intent share = new Intent(Intent.ACTION_SEND);
        share.setType("image/*");
        share.putExtra(Intent.EXTRA_STREAM, Uri.parse(imageRequest.getImg()));
        share.putExtra(Intent.EXTRA_TEXT, imageRequest.getPlaceName());
        startActivity(Intent.createChooser(share, "Share via"));

       /* Uri imageUri = Uri.parse(imageRequest.getImg());
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        //Target whatsapp:
        // shareIntent.setPackage("com.whatsapp");
        //Add text and then Image URI
        shareIntent.putExtra(Intent.EXTRA_TEXT, imageRequest.getPlaceName());
        shareIntent.putExtra(Intent.EXTRA_STREAM, imageUri);
        shareIntent.setType("image/jpeg");
        shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

        try {
            startActivity(shareIntent);
        } catch (Exception ex) {
            Toast.makeText(getActivity(), "Whatsapp have not been installed.", Toast.LENGTH_SHORT).show();
        }*/
    }


    private HomeActivity getMyActivity() {
        return (HomeActivity) getActivity();
    }

    private void downloadImage(String url) {
        File file;
        String dirPath, fileName;
        final ProgressDialog pd = new ProgressDialog(getMyActivity());

        AndroidNetworking.initialize(getMyActivity());

        //Folder Creating Into Phone Storage
        dirPath = Environment.getExternalStorageDirectory() + "/School Downloads";

        fileName = "image" + Calendar.getInstance().getTime() + ".jpeg";

        //file Creating With Folder & Fle Name
        file = new File(dirPath, fileName);

        pd.setMessage("Downloading image...");
        pd.show();
        AndroidNetworking.download(url, dirPath, fileName)
                .build()
                .startDownload(new DownloadListener() {

                    @Override
                    public void onDownloadComplete() {
                        pd.dismiss();
                        Toast.makeText(getMyActivity(), "Image Downloaded to sd card folder School Downloads", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(ANError anError) {
                        Toast.makeText(getMyActivity(), anError.getMessage(), Toast.LENGTH_SHORT).show();
                        pd.dismiss();
                    }
                });

    }

    private void deleteImage(ImageRequest imageRequest) {
        imageRequest.setVisiblity("FALSE");
        getMyActivity().databaseReference.child(Constants.MAIN_TABLE).child(Constants.IMAGE_TABLE).child(imageRequest.getUniqKey()).setValue(imageRequest);
        CommonUtils.showToast(getMyActivity(), "Photo deleted successfully!");
        images.remove(imageRequest);
        getMyActivity().setListAlbumChild(images);
        getMyActivity().performBackForDesign();
    }

}
