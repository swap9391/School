package com.exa.mydemoapp.fragment;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
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
import com.exa.mydemoapp.Common.AppController;
import com.exa.mydemoapp.Common.CommonUtils;
import com.exa.mydemoapp.Common.Connectivity;
import com.exa.mydemoapp.Common.Constants;
import com.exa.mydemoapp.HomeActivity;
import com.exa.mydemoapp.R;
import com.exa.mydemoapp.model.AlbumMasterModel;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by Swapnil on 4/8/17.
 */

public class SlideshowDialogFragment extends DialogFragment {
    private String TAG = SlideshowDialogFragment.class.getSimpleName();
    private ArrayList<AlbumMasterModel> images;
    private ViewPager viewPager;
    private MyViewPagerAdapter myViewPagerAdapter;
    private TextView lblCount, lblTitle;
    private int selectedPosition = 0;
    private String fragmentFrom;
    private boolean isGuest;

    private LayoutInflater layoutInflater;
    private ImageButton btnShare;
    private ImageButton btnDownload;
    private ImageButton btnDelete;

    private ImageView currentImage;

    static SlideshowDialogFragment newInstance() {
        SlideshowDialogFragment f = new SlideshowDialogFragment();
        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_image_slider, container, false);

        selectedPosition = getArguments().getInt(Constants.INTENT_TYPE_POSITION);
        fragmentFrom = getArguments().getString(Constants.INTENT_TYPE_FRAGMENT);
        isGuest = getArguments().getBoolean(Constants.INTENT_TYPE_GUEST);
        images = (ArrayList<AlbumMasterModel>) getArguments().getSerializable(Constants.INTENT_TYPE_IMAGES);


        viewPager = (ViewPager) v.findViewById(R.id.viewpager);
        lblCount = (TextView) v.findViewById(R.id.lblCount);
        lblTitle = (TextView) v.findViewById(R.id.lblTitle);

        btnShare = (ImageButton) v.findViewById(R.id.btn_share);
        btnDownload = (ImageButton) v.findViewById(R.id.btn_download);
        btnDelete = (ImageButton) v.findViewById(R.id.btn_delete);

        if (getMyActivity().isAdmin) {
            btnDelete.setVisibility(View.VISIBLE);
        } else {
            btnDelete.setVisibility(View.GONE);
        }
        if (isGuest) {
            btnDownload.setVisibility(View.GONE);
            btnShare.setVisibility(View.GONE);
        }

        btnShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlbumMasterModel image = images.get(selectedPosition);
                shareImage(currentImage, image.getAlbumTitle());
            }
        });
        btnDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Connectivity.isConnected(getMyActivity())) {
                    AlbumMasterModel image = images.get(selectedPosition);
                    downloadImage(image.getImg());
                } else {
                    getMyActivity().showToast(getString(R.string.no_internet));
                }
            }
        });
        if (fragmentFrom.equals(Constants.INTENT_VALUE_CHAT)) {
            btnDelete.setVisibility(View.GONE);
        } else if (getMyActivity().isAdmin && !isGuest && !fragmentFrom.equals(Constants.INTENT_VALUE_CHAT)) {
            btnDelete.setVisibility(View.VISIBLE);
            btnDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AlbumMasterModel image = images.get(selectedPosition);
                    //  deleteImage(image);
                }
            });
        }
        List<AlbumMasterModel> requestList = new ArrayList<>();
        requestList.add(images.get(0));
        getMyActivity().setListAlbumChild(requestList);

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
        if (position >= 0) {
            lblCount.setText((position + 1) + " of " + images.size());
        }
        AlbumMasterModel image = images.get(position);
        lblTitle.setText(image.getAlbumTitle() == null ? "" : image.getAlbumTitle());
        //  lblDate.setText(image.getDescription());
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
    }

    //  adapter
    public class MyViewPagerAdapter extends PagerAdapter {


        public MyViewPagerAdapter() {
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {

            layoutInflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = layoutInflater.inflate(R.layout.image_fullscreen_preview, container, false);

            ImageView imageViewPreview = (ImageView) view.findViewById(R.id.image_preview);
            ImageButton btnRotate;
            btnRotate = (ImageButton) view.findViewById(R.id.btn_rotate);
            btnRotate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    imageViewPreview.setRotation(90);
                }
            });
            currentImage = imageViewPreview;
            final AlbumMasterModel image = images.get(position);


            Glide.with(getActivity()).load(image.getImg())
                    .thumbnail(0.5f)
                    .crossFade()
                    .override(800, 500)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(imageViewPreview);

            container.addView(view);

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

    private HomeActivity getMyActivity() {
        return (HomeActivity) getActivity();
    }

    private void downloadImage(String url) {
        File file;
        String dirPath, fileName;
        final ProgressDialog pd = new ProgressDialog(getMyActivity());

        AndroidNetworking.initialize(getMyActivity());

        //Folder Creating Into Phone Storage
        dirPath = Environment.getExternalStorageDirectory() + getString(R.string.download_folder);

        fileName = "image" + Calendar.getInstance().getTime() + ".jpeg";

        //file Creating With Folder & Fle Name
        file = new File(dirPath, fileName);

        pd.setMessage(getString(R.string.msg_progress_download));
        pd.show();
        AndroidNetworking.download(url, dirPath, fileName)
                .build()
                .startDownload(new DownloadListener() {

                    @Override
                    public void onDownloadComplete() {
                        pd.dismiss();
                        Toast.makeText(getMyActivity(), getString(R.string.msg_download_folder), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(ANError anError) {
                        Toast.makeText(getMyActivity(), anError.getMessage(), Toast.LENGTH_SHORT).show();
                        pd.dismiss();
                    }
                });

    }

    public Bitmap capture(View view) {
        view.setDrawingCacheEnabled(true);
        Bitmap bitmap = Bitmap.createBitmap(view.getDrawingCache());
        view.setDrawingCacheEnabled(false);
        return bitmap;
    }

    public void shareImage(View view, String filename) {
        Context context = view.getContext();
        Bitmap bitmap = capture(view);
        try {
            File file = new File(context.getExternalCacheDir(), filename + ".png");
            FileOutputStream fos = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.flush();
            fos.close();
            Intent i = new Intent(Intent.ACTION_SEND);
            i.setType("image/png");
            Uri uri = Uri.fromFile(file);
            i.putExtra(Intent.EXTRA_STREAM, uri);
            context.startActivity(Intent.createChooser(i, "Share"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

   /* private void deleteImage(AlbumMasterModel albumImagesModel) {

        try {
            AlertDialog.Builder builder = getMyActivity().showAlertDialog(getMyActivity(), getString(R.string.app_name), getString(R.string.delete_msg));
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    getMyActivity().databaseReference.child(Constants.MAIN_TABLE).child(Constants.IMAGE_TABLE).child(albumImagesModel.getUniqKey()).setValue(albumImagesModel);
                    CommonUtils.showToast(getMyActivity(), "Photo deleted successfully!");
                    images.remove(albumImagesModel);
                    List<AlbumMasterModel> requestList = new ArrayList<>();
                    requestList.add(images.get(0));
                    getMyActivity().setListAlbumChild(requestList);
                    getMyActivity().performBackForDesign();
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
    }*/

}
