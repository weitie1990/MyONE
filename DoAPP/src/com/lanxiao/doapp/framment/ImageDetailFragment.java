package com.lanxiao.doapp.framment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.easemob.easeui.widget.photoview.PhotoViewAttacher;
import com.example.doapp.R;
import com.squareup.picasso.Callback;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;

import java.io.File;

/**
 * Created by Thinkpad on 2016/1/6.
 */
public class ImageDetailFragment extends BaseFrament {
    private String mImageUrl;
    private ImageView mImageView;
    private ProgressBar progressBar;
    private PhotoViewAttacher mAttacher;

    public static ImageDetailFragment newInstance(String imageUrl) {
        final ImageDetailFragment f = new ImageDetailFragment();

        final Bundle args = new Bundle();
        args.putString("url", imageUrl);
        f.setArguments(args);

        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mImageUrl = getArguments() != null ? getArguments().getString("url")
                : null;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.image_detail_fragment,
                container, false);
        mImageView = (ImageView) v.findViewById(R.id.image);
        mAttacher = new PhotoViewAttacher(mImageView);

        mAttacher.setOnPhotoTapListener(new PhotoViewAttacher.OnPhotoTapListener() {

            @Override
            public void onPhotoTap(View arg0, float arg1, float arg2) {
                getActivity().finish();
            }
        });

        progressBar = (ProgressBar) v.findViewById(R.id.loading);
        return v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if(mImageUrl.startsWith("http://")) {
            Picasso.with(getContext())
                    .load(mImageUrl)
                    .memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE)
                    .into(mImageView, new Callback() {
                        @Override
                        public void onSuccess() {
                            mAttacher.update();
                        }

                        @Override
                        public void onError() {

                        }
                    });
            /*ImageLoader.getInstance().loadImage(mImageUrl, new ImageLoadingListener() {
                @Override
                public void onLoadingStarted(String s, View view) {
                    progressBar.setVisibility(View.VISIBLE);
                }

                @Override
                public void onLoadingFailed(String s, View view, FailReason failReason) {
                    String message = null;
                    switch (failReason.getType()) {
                        case IO_ERROR:
                            message = "下载错误";
                            break;
                        case DECODING_ERROR:
                            message = "图片无法显示";
                            break;
                        case NETWORK_DENIED:
                            message = "网络有问题，无法下载";
                            break;
                        case OUT_OF_MEMORY:
                            message = "图片太大无法显示";
                            break;
                        case UNKNOWN:
                            message = "未知的错误";
                            break;
                    }
                    Toast.makeText(getActivity(), message,
                            Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                }

                @Override
                public void onLoadingComplete(String s, View view, Bitmap bitmap) {
                        mImageView.setImageBitmap(bitmap);
                        progressBar.setVisibility(View.GONE);
                        mAttacher.update();
                }

                @Override
                public void onLoadingCancelled(String s, View view) {

                }
            });*/
           /* ImageLoader.getInstance().displayImage(mImageUrl, mImageView,
                    new SimpleImageLoadingListener() {
                        @Override
                        public void onLoadingStarted(String imageUri, View view) {
                            progressBar.setVisibility(View.VISIBLE);
                        }

                        @Override
                        public void onLoadingFailed(String imageUri, View view,
                                                    FailReason failReason) {
                            String message = null;
                            switch (failReason.getType()) {
                                case IO_ERROR:
                                    message = "下载错误";
                                    break;
                                case DECODING_ERROR:
                                    message = "图片无法显示";
                                    break;
                                case NETWORK_DENIED:
                                    message = "网络有问题，无法下载";
                                    break;
                                case OUT_OF_MEMORY:
                                    message = "图片太大无法显示";
                                    break;
                                case UNKNOWN:
                                    message = "未知的错误";
                                    break;
                            }
                            Toast.makeText(getActivity(), message,
                                    Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.GONE);
                        }

                        @Override
                        public void onLoadingComplete(String imageUri, View view,
                                                      Bitmap loadedImage) {
                            progressBar.setVisibility(View.GONE);
                            mAttacher.update();
                        }
                    });*/
        }else {
            Picasso.with(getContext()).load(new File(mImageUrl)).placeholder(R.drawable.ease_default_image).into(mImageView, new Callback() {
                @Override
                public void onSuccess() {
                    mAttacher.update();
                }

                @Override
                public void onError() {

                }
            });
            /*ImageLoader.getInstance().displayImage(new File(mImageUrl), mImageView, new ImageLoadingListener() {
                @Override
                public void onLoadingStarted(String s, View view) {

                }

                @Override
                public void onLoadingFailed(String s, View view, FailReason failReason) {

                }

                @Override
                public void onLoadingComplete(String s, View view, Bitmap bitmap) {
                    mAttacher.update();
                }

                @Override
                public void onLoadingCancelled(String s, View view) {

                }
            });*/
        }
    }
}
