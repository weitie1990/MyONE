package com.lanxiao.doapp.myView;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Interpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

import com.example.doapp.R;
import com.lanxiao.doapp.MultiImageSelector.MultiImageSelectorActivity;
import com.lanxiao.doapp.activity.CurrentLocationActivity;
import com.lanxiao.doapp.activity.InputActivity;
import com.lanxiao.doapp.activity.MeetingDoActivity;
import com.lanxiao.doapp.activity.MeetingWebActivity;
import com.lanxiao.doapp.activity.PostUserinfoActivity;
import com.lanxiao.doapp.activity.WebActivity;
import com.lanxiao.doapp.chatui.applib.controller.DemoHelper;
import com.lanxiao.doapp.framment.Frament_workappion;
import com.lanxiao.doapp.http.Api;
import com.lanxiao.doapp.untils.CinemaUtil;
import com.lidroid.xutils.util.LogUtils;
import com.zhy.autolayout.AutoRelativeLayout;
import com.zhy.autolayout.utils.AutoUtils;
import com.zhy.autolayout.utils.ScreenUtils;

import java.io.File;

/**
 * 底部弹出对话框
 */
public class MoreWindow extends PopupWindow implements OnClickListener {
    private String TAG = MoreWindow.class.getSimpleName();
    FragmentActivity mContext;
    private int mWidth;
    private int mHeight;
    private int statusBarHeight;
    private Bitmap mBitmap = null;
    private Bitmap overlay = null;
    private RelativeLayout iv_window_jiandao, iv_window_answer, iv_window_meeting;
    private ImageView iv_sum_file, iv_sum_chiema, iv_sum_do, iv_sum_work, iv_sum_faviate;
    File file;
    Uri uri;
    private Handler mHandler = new Handler();


    public MoreWindow(FragmentActivity context) {
        mContext = context;
    }

    public void init(int heigtht) {
        Rect frame = new Rect();
        mContext.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
        statusBarHeight = frame.top;
        DisplayMetrics metrics = new DisplayMetrics();
        mContext.getWindowManager().getDefaultDisplay()
                .getMetrics(metrics);
        mWidth = metrics.widthPixels;
        mHeight = metrics.heightPixels;
        LogUtils.i("width:" + mWidth + ",height:" + mHeight + ",tabHeitht:" + heigtht);

        setWidth(mWidth);
        setHeight(mHeight - heigtht - statusBarHeight);
    }
    //获取底部 navigation bar 高度

    public int getNavigationBarHeight() {
        Resources resources = mContext.getResources();
        int resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android");
        int height = resources.getDimensionPixelSize(resourceId);
        Log.v("dbw", "Navi height:" + height);
        return height;
    }

    private Bitmap blur() {
        if (null != overlay) {
            return overlay;
        }
        long startMs = System.currentTimeMillis();

        View view = mContext.getWindow().getDecorView();
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache(true);
        mBitmap = view.getDrawingCache();

        float scaleFactor = 8;//图片缩放比例；
        float radius = 1;//模糊程度
        int width = mBitmap.getWidth();
        int height = mBitmap.getHeight();

        overlay = Bitmap.createBitmap((int) (width / scaleFactor), (int) (height / scaleFactor), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(overlay);
        canvas.scale(1 / scaleFactor, 1 / scaleFactor);
        Paint paint = new Paint();
        paint.setFlags(Paint.FILTER_BITMAP_FLAG);
        canvas.drawBitmap(mBitmap, 0, 0, paint);

        overlay = FastBlur.doBlur(overlay, (int) radius, true);
        Log.i(TAG, "blur time is:" + (System.currentTimeMillis() - startMs));
        return overlay;
    }

    AutoRelativeLayout layout;
    public void showMoreWindow(View line) {
        layout = (AutoRelativeLayout) LayoutInflater.from(mContext).inflate(R.layout.frame_function,null);
        setContentView(layout);
        iv_window_jiandao = (RelativeLayout) layout.findViewById(R.id.iv_window_jiandao);
        iv_window_answer = (RelativeLayout) layout.findViewById(R.id.iv_window_answer);
        iv_window_meeting = (RelativeLayout) layout.findViewById(R.id.iv_window_meeting);
        iv_sum_file = (ImageView) layout.findViewById(R.id.iv_sum_file);
        iv_sum_chiema = (ImageView) layout.findViewById(R.id.iv_sum_chiema);
        iv_sum_do = (ImageView) layout.findViewById(R.id.iv_sum_do);
        iv_sum_work = (ImageView) layout.findViewById(R.id.iv_sum_work);
        iv_sum_faviate = (ImageView) layout.findViewById(R.id.iv_sum_faviate);
/*
        int centerY = ScreenUtils.getScreenSize(mContext,false)[1];
        iv_sum_file.setY(centerY);
        iv_sum_chiema.setY(centerY);
        iv_sum_do.setY(centerY);
        iv_sum_work.setY(centerY);
        iv_sum_faviate.setY(centerY);
*/
        layout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        iv_window_jiandao.setOnClickListener(this);
        iv_window_answer.setOnClickListener(this);
        iv_window_meeting.setOnClickListener(this);
        iv_sum_file.setOnClickListener(this);
        iv_sum_chiema.setOnClickListener(this);
        iv_sum_work.setOnClickListener(this);
        iv_sum_do.setOnClickListener(this);
        iv_sum_faviate.setOnClickListener(this);
        setBackgroundDrawable(new BitmapDrawable(mContext.getResources(), blur()));
        showAsDropDown(line, 0, 0);
        showAnimation(layout);
    }

    int j=0;
    private void showAnimation(ViewGroup layout) {
        j=0;
        for (int i = 0; i < layout.getChildCount(); i++) {
            final View child = layout.getChildAt(i);
            mHandler.postDelayed(new Runnable() {

                @Override
                public void run() {
                    ValueAnimator fadeAnim;
                    if (child.getId() == iv_window_jiandao.getId() || child.getId() == iv_window_answer.getId() || child.getId() == iv_window_meeting.getId()) {
                        fadeAnim = ObjectAnimator.ofFloat(child, "translationY", AutoUtils.getPercentHeightSize(-1200), 0);
                        fadeAnim.setDuration(450);
                        fadeAnim.start();
                    } else {
                        bindChildAnimation(child,j,450);
                        j++;
                    }

                }
            }, 0);
        }

    }
    public void close() {
        j=0;
        for (int i = 0; i < layout.getChildCount(); i++) {
            final View child = layout.getChildAt(i);
            mHandler.postDelayed(new Runnable() {

                @Override
                public void run() {
                    ValueAnimator fadeAnim;
                    if (child.getId() == iv_window_jiandao.getId() || child.getId() == iv_window_answer.getId() || child.getId() == iv_window_meeting.getId()) {
                        fadeAnim = ObjectAnimator.ofFloat(child, "translationY", 0, AutoUtils.getPercentHeightSize(-1200));
                        fadeAnim.setDuration(450);
                        fadeAnim.start();
                    } else {
                        closeChildAnimation(child,j,450);
                        j++;
                    }

                }
            }, 0);
        }
    }
    TakePhotoPopWin takePhotoPopWin;

    public void showPopFormBottom(View view) {
        takePhotoPopWin = new TakePhotoPopWin(mContext, onClickListener);
        //showAtLocation(View parent, int gravity, int x, int y)
        takePhotoPopWin.showAtLocation(mContext.findViewById(R.id.tool), Gravity.CENTER, 0, 0);
    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            switch (v.getId()) {
                case R.id.btn_take_photo:
                    //进入照相机
                    intoCimera();
                    break;
                case R.id.btn_pick_photo:
                    Intent intent = new Intent(mContext, InputActivity.class);
                    intent.putExtra("type", 1);
                    mContext.startActivity(intent);
                    break;
            }
            if (takePhotoPopWin != null) {
                takePhotoPopWin.dismiss();
            }
        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_window_jiandao:
                Intent intent = new Intent(mContext, CurrentLocationActivity.class);
                intent.putExtra("type", "jiandao");
                mContext.startActivity(intent);

                break;
            case R.id.iv_window_meeting:
                mContext.startActivity(new Intent(mContext, MeetingDoActivity.class));
                break;
            case R.id.iv_window_answer:
                intoWeb(Api.WANGSHANGTOUPIAO + DemoHelper.getInstance().getCurrentUsernName(), "9");
                break;
            case R.id.iv_sum_file:
                Intent intent1 = new Intent(mContext, WebActivity.class);
                intent1.putExtra("result", "http://www.dosns.net/help.html");
                mContext.startActivity(intent1);
                break;
            case R.id.iv_sum_chiema:
                showPopFormBottom(v);
                break;
            case R.id.iv_sum_do:
                intoInputActivity();

                break;
            case R.id.iv_sum_work:
                mContext.startActivity(new Intent(mContext, Frament_workappion.class));

                break;
            case R.id.iv_sum_faviate:
                Intent in = new Intent(mContext, PostUserinfoActivity.class);
                in.putExtra("type", 1);
                mContext.startActivity(in);
                break;
            default:
                break;
        }
    }

    private void intoWeb(String url, String type) {
        Intent intent1 = new Intent(mContext, MeetingWebActivity.class);
        intent1.putExtra("result", url);
        intent1.putExtra("type", type);
        mContext.startActivity(intent1);
    }

    public void destroy() {
        if (null != overlay) {
            overlay.recycle();
            overlay = null;
            System.gc();
        }
        if (null != mBitmap) {
            mBitmap.recycle();
            mBitmap = null;
            System.gc();
        }
    }

    /**
     * 调用系统拍照
     */
    private void intoCimera() {
        Intent intent = new Intent(mContext, MultiImageSelectorActivity.class);
        // 是否显示拍摄图片
        intent.putExtra(MultiImageSelectorActivity.EXTRA_SHOW_CAMERA, true);
        // 最大可选择图片数量
        intent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_COUNT, 9);
        // 选择模式
        intent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_MODE, MultiImageSelectorActivity.MODE_MULTI);
        //Intent intent= CinemaUtil.IntoSystemPic(mContext);
        //mContext.startActivityForResult(intent, 0x00);
        mContext.startActivityForResult(intent, 0x00);

    }

    /**
     * 裁剪图片
     *
     * @param uri
     */
    public void startPhotoZoom(Uri uri, int size) {
        Intent intent = CinemaUtil.startPhotoZoom(uri, size);
        mContext.startActivityForResult(intent, 0x01);
    }

    private void intoInputActivity() {
        Intent intent = new Intent("android.intent.inputActivity");
        mContext.startActivity(intent);
        mContext.overridePendingTransition(R.anim.in_from_blew, R.anim.out_to_up);
    }
    public static final float DEFAULT_FROM_DEGREES = 210.0f;

    public static final float DEFAULT_TO_DEGREES = 330.0f;
    private void bindChildAnimation(final View child, final int index, final long duration) {
        final int centerX = getWidth() / 2;
        final int centerY = ScreenUtils.getScreenSize(mContext,false)[1];
        final int radius = AutoUtils.getPercentWidthSize(500);
        Log.i("weitie:","radius:"+radius);
        final int childCount = 5;
        final float perDegrees = (DEFAULT_TO_DEGREES - DEFAULT_FROM_DEGREES) / (childCount - 1);
        Rect frame = computeChildFrame(centerX, centerY, radius, DEFAULT_FROM_DEGREES + index * perDegrees, child.getHeight());

        final int toXDelta = frame.left - child.getLeft();
        final int toYDelta = frame.top - child.getTop();

        Interpolator interpolator =  new OvershootInterpolator(1.5f);
        ObjectAnimator.ofFloat(child, "translationX", 0F, toXDelta).setDuration(duration).start();//X轴平移旋转
        ObjectAnimator.ofFloat(child, "translationY", 0F, toYDelta).setDuration(duration).start();//Y轴平移旋转

    }
    private void closeChildAnimation(final View child, final int index, final long duration) {
        final int centerX = getWidth() / 2;
        final int centerY = ScreenUtils.getScreenSize(mContext,false)[1];
        final int radius = AutoUtils.getPercentWidthSize(500);
        Log.i("weitie:","radius:"+radius);
        final int childCount = 5;
        final float perDegrees = (DEFAULT_TO_DEGREES - DEFAULT_FROM_DEGREES) / (childCount - 1);
        Rect frame = computeChildFrame(centerX, centerY, radius, DEFAULT_FROM_DEGREES + index * perDegrees, child.getHeight());

        final int toXDelta = frame.left - child.getLeft();
        final int toYDelta = frame.top - child.getTop();
        ObjectAnimator.ofFloat(child, "translationX", toXDelta,0F ).setDuration(duration).start();//X轴平移旋转
        ObjectAnimator translationY = ObjectAnimator.ofFloat(child, "translationY", toYDelta, 0F).setDuration(duration);//Y轴平移旋转
        translationY.start();
        translationY.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                if(isShowing()) {
                    dismiss();
                }
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
      /*  Animation animation =
                createExpandAnimation(0, toXDelta, 0, toYDelta, 0, duration, interpolator);

        child.setAnimation(animation);*/
    }
    public static Rect computeChildFrame(final int centerX, final int centerY, final int radius, final float degrees,
                                         final int size) {

        final double childCenterX = centerX + radius * Math.cos(Math.toRadians(degrees));
        final double childCenterY = centerY + radius * Math.sin(Math.toRadians(degrees));

        return new Rect((int) (childCenterX - size / 2), (int) (childCenterY - size / 2),
                (int) (childCenterX + size / 2), (int) (childCenterY + size / 2));
    }
}
