package com.lanxiao.doapp.untils.util;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.AnimationDrawable;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.hardware.Camera.Size;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Build.VERSION_CODES;
import android.os.Environment;
import android.os.Looper;
import android.os.StrictMode;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import com.easemob.easeui.Conversion;
import com.easemob.util.HanziToPinyin;
import com.example.doapp.R;
import com.lanxiao.doapp.MultiImageSelector.MultiImageSelectorActivity;
import com.lanxiao.doapp.activity.CaptureActivity;
import com.lanxiao.doapp.activity.CurrentLocationActivity;
import com.lanxiao.doapp.activity.ImageGridActivity;
import com.lanxiao.doapp.activity.MeetingDoActivity;
import com.lanxiao.doapp.activity.PostUserinfoActivity;
import com.lanxiao.doapp.activity.SetActivity_set;
import com.lanxiao.doapp.activity.UserProfileActivity;
import com.lanxiao.doapp.chatui.applib.chatuimain.utils.DemoApplication;
import com.lanxiao.doapp.chatui.applib.controller.DemoHelper;
import com.lanxiao.doapp.entity.PersonInfo;
import com.lanxiao.doapp.http.Api;
import com.lanxiao.doapp.myView.CircularImage;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.exception.DbException;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lidroid.xutils.util.LogUtils;
import com.squareup.picasso.Picasso;
import com.zhy.autolayout.utils.AutoUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utils {

    private Utils() {
    }

    ;

    @SuppressLint("NewApi")
    public static void enableStrictMode() {
        if (Utils.hasGingerbread()) {
            StrictMode.ThreadPolicy.Builder threadPolicyBuilder =
                    new StrictMode.ThreadPolicy.Builder()
                            .detectAll()
                            .penaltyLog();
            StrictMode.VmPolicy.Builder vmPolicyBuilder =
                    new StrictMode.VmPolicy.Builder()
                            .detectAll()
                            .penaltyLog();

            if (Utils.hasHoneycomb()) {
                threadPolicyBuilder.penaltyFlashScreen();
                vmPolicyBuilder
                        .setClassInstanceLimit(ImageGridActivity.class, 1);
            }
            StrictMode.setThreadPolicy(threadPolicyBuilder.build());
            StrictMode.setVmPolicy(vmPolicyBuilder.build());
        }


    }

    public static boolean hasFroyo() {
        return Build.VERSION.SDK_INT >= VERSION_CODES.FROYO;

    }

    public static boolean hasGingerbread() {
        return Build.VERSION.SDK_INT >= VERSION_CODES.GINGERBREAD;
    }

    public static boolean hasHoneycomb() {
        return Build.VERSION.SDK_INT >= VERSION_CODES.HONEYCOMB;
    }

    public static boolean hasHoneycombMR1() {
        return Build.VERSION.SDK_INT >= VERSION_CODES.HONEYCOMB_MR1;
    }

    public static boolean hasJellyBean() {
        return Build.VERSION.SDK_INT >= VERSION_CODES.JELLY_BEAN;
    }

    public static boolean hasKitKat() {
        return Build.VERSION.SDK_INT >= 19;
    }

    public static List<Size> getResolutionList(Camera camera) {
        Parameters parameters = camera.getParameters();
        List<Size> previewSizes = parameters.getSupportedPreviewSizes();
        return previewSizes;
    }

    public static int getKeybackSum(String html) {
        StringBuffer sb = new StringBuffer(html);
        int num = 0;
        for (int i = 0; i < sb.length(); i++) {
            if (sb.charAt(i) == '\n') {
                num++;
            }
        }
        return num;
    }

    public static class ResolutionComparator implements Comparator<Size> {

        @Override
        public int compare(Size lhs, Size rhs) {
            if (lhs.height != rhs.height)
                return lhs.height - rhs.height;
            else
                return lhs.width - rhs.width;
        }
    }

    static MediaPlayer mMediaPlayer = new MediaPlayer();

    /**
     * @param name
     * @播放本地音频文件
     */
    public static void playMusic(String name, final ImageView iv_inputMessage_voiceanmion, Context context) {
        Log.i("weitie", "playvoice");
        try {

            if (mMediaPlayer.isPlaying()) {
                mMediaPlayer.stop();
                mMediaPlayer.reset();
                mMediaPlayer.release();
                return;
            }
            mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            //mMediaPlayer.reset();
            //final AnimationDrawable voiceAnimation = (AnimationDrawable) iv_inputMessage_voiceanmion.getDrawable();
            mMediaPlayer.setDataSource(name);
            mMediaPlayer.prepareAsync();
            mMediaPlayer.prepare();
            iv_inputMessage_voiceanmion.setImageResource(R.anim.voice_from_icon);
            ((AnimationDrawable) iv_inputMessage_voiceanmion.getDrawable()).start();
            mMediaPlayer.start();

            mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                public void onCompletion(MediaPlayer mp) {
                    mMediaPlayer.release();
                    ((AnimationDrawable) iv_inputMessage_voiceanmion.getDrawable()).stop();
                    iv_inputMessage_voiceanmion.setImageResource(R.drawable.ease_chatfrom_voice_playing);
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 检查当前网络是否可用
     *
     * @return
     */

    public static boolean isNetworkAvailable(Activity activity) {
        Context context = activity.getApplicationContext();
        // 获取手机所有连接管理对象（包括对wi-fi,net等连接的管理）
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        if (connectivityManager == null) {
            return false;
        } else {
            // 获取NetworkInfo对象
            NetworkInfo[] networkInfo = connectivityManager.getAllNetworkInfo();

            if (networkInfo != null && networkInfo.length > 0) {
                for (int i = 0; i < networkInfo.length; i++) {
                    System.out.println(i + "===状态===" + networkInfo[i].getState());
                    System.out.println(i + "===类型===" + networkInfo[i].getTypeName());
                    // 判断当前网络状态是否为连接状态
                    if (networkInfo[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * 获取手机名称的最后两个字符
     *
     * @param src
     * @return
     */
    public static String getSubString(String src) {
        String s = null;
        if (src.length() > 2) {
            s = src.substring(src.length() - 2);
        } else {
            s = src;
        }
        return s;
    }

    // 校验Tag Alias 只能是数字,英文字母和中文
    public static boolean isValidTagAndAlias(String s) {
        Pattern p = Pattern.compile("^[\u4E00-\u9FA50-9a-zA-Z_-]{0,}$");
        Matcher m = p.matcher(s);
        return m.matches();
    }

    public static boolean isConnected(Context context) {
        ConnectivityManager conn = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = conn.getActiveNetworkInfo();
        return (info != null && info.isConnected());
    }

    public static void showToast(final String toast, final Context context) {
        new Thread(new Runnable() {

            @Override
            public void run() {
                Looper.prepare();
                Toast.makeText(context, toast, Toast.LENGTH_SHORT).show();
                Looper.loop();
            }
        }).start();
    }

    /**
     * 获取sort key的首个字符，如果是英文字母就直接返回，否则返回#。
     *
     * @param sortKeyString 数据库中读取出的sort key
     * @return 英文字母或者#
     */
    public static String getSortKey(String sortKeyString) {
        String key = sortKeyString.substring(0, 1).toUpperCase();
        if (key.matches("[A-Z]")) {
            return key;
        }
        return "#";
    }

    /**
     * 将拼音转换成英文
     *
     * @param inputString
     * @return
     *//*
    public static String getPingYin(String inputString) {
		HanyuPinyinOutputFormat format = new HanyuPinyinOutputFormat();
		format.setCaseType(HanyuPinyinCaseType.LOWERCASE);
		format.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
		format.setVCharType(HanyuPinyinVCharType.WITH_V);
		char[] input = inputString.trim().toCharArray();
		String output = "";
		try {
			for (char curchar : input) {
				if (java.lang.Character.toString(curchar).matches("[\\u4E00-\\u9FA5]+")) {
					String[] temp = PinyinHelper.toHanyuPinyinStringArray(curchar, format);
					output += temp[0];
				} else
					output += java.lang.Character.toString(curchar);
			}
		} catch (BadHanyuPinyinOutputFormatCombination e) {
			e.printStackTrace();
		}
		return output;
	}*/
    public static String getPingYin(String inputString) {
        LogUtils.i(inputString);
        String headerName = null;
        String sokekey = null;
        if (!TextUtils.isEmpty(inputString)) {
            headerName = inputString.trim();
        } else {
            headerName = DemoHelper.getInstance().getCurrentUsernName();
        }
        if (Character.isDigit(headerName.charAt(0))) {
            return "#";
        } else {
            sokekey = HanziToPinyin.getInstance().get(headerName.substring(0, 1)).get(0).target.substring(0, 1)
                    .toUpperCase();
            LogUtils.i(sokekey);
            if (sokekey.toLowerCase().charAt(0) < 'a' || sokekey.toLowerCase().charAt(0) > 'z') {
                return "#";
            }
        }
        return sokekey;
    }

    /**
     * 是否为手机号码
     *
     * @param mobiles
     * @return
     */
    public static boolean isMobileNO(String mobiles) {
        Pattern p = Pattern.compile("^((13[0-9])|(15[^4,\\D])|(18[0-9]))\\d{8}$");
        Matcher m = p.matcher(mobiles);
        return m.matches();
    }
    /**
     * 是否为纯数字
     *
     * @param mobiles
     * @return
     */
    public static boolean isNO(String mobiles) {
        Pattern p = Pattern.compile("[0-9]*");
        Matcher m = p.matcher(mobiles);
        return m.matches();
    }
    /**
     * 针对TextView显示中文中出现的排版错乱问题，通过调用此方法得以解决
     *
     * @return 返回全部为全角字符的字符串
     */
    public static String ToSBC(String input) {
        char c[] = input.toCharArray();
        for (int i = 0; i < c.length; i++) {
            if (c[i] == ' ') {
                c[i] = '\u3000';
            } else if (c[i] < '\177') {
                c[i] = (char) (c[i] + 65248);
            }
        }
        return new String(c);
    }

    /**
     * 设置用户头像
     *
     * @return 返回全部为全角字符的字符串
     */
    public static void setAver(final String url, final ImageView iv) {
        Picasso.with(iv.getContext())
                .load(url)
                .resize(AutoUtils.getPercentWidthSize(100), AutoUtils.getPercentWidthSize(100))
                .centerCrop().config(Bitmap.Config.RGB_565)
                .into(iv);
       /* iv.setTag(url);
        // 使用DisplayImageOptions.Builder()创建DisplayImageOptions
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.ease_default_avatar) // 设置图片下载期间显示的图片
                .showImageForEmptyUri(R.drawable.ease_default_avatar) // 设置图片Uri为空或是错误的时候显示的图片
                .showImageOnFail(R.drawable.ease_default_avatar) // 设置图片加载或解码过程中发生错误显示的图片
                .imageScaleType(ImageScaleType.IN_SAMPLE_INT)
                .cacheInMemory(true) // 设置下载的图片是否缓存在内存中
                .cacheOnDisk(true) // 设置下载的图片是否缓存在SD卡中
                .bitmapConfig(Bitmap.Config.RGB_565)
                .build(); // 构建完成
        if (!TextUtils.isEmpty(url)) {
            ImageLoader.getInstance().displayImage(url, iv, options, new ImageLoadingListener() {
                @Override
                public void onLoadingStarted(String s, View view) {

                }

                @Override
                public void onLoadingFailed(String s, View view, FailReason failReason) {

                }

                @Override
                public void onLoadingComplete(String s, View view, Bitmap bitmap) {
                    if (iv.getTag().equals(url)) {
                        iv.setImageBitmap(bitmap);
                    }
                }

                @Override
                public void onLoadingCancelled(String s, View view) {

                }
            });
        }*/
    }

    public static void setAver(final String url, final CircularImage iv) {
        Picasso.with(iv.getContext())
                .load(url)
                .resize(AutoUtils.getPercentWidthSize(100), AutoUtils.getPercentWidthSize(100))
                .centerCrop().config(Bitmap.Config.RGB_565)
                .into(iv);
       /* iv.setTag(url);
        // 使用DisplayImageOptions.Builder()创建DisplayImageOptions
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.ease_default_avatar) // 设置图片下载期间显示的图片
                .showImageForEmptyUri(R.drawable.ease_default_avatar) // 设置图片Uri为空或是错误的时候显示的图片
                .showImageOnFail(R.drawable.ease_default_avatar) // 设置图片加载或解码过程中发生错误显示的图片
                .imageScaleType(ImageScaleType.IN_SAMPLE_INT)
                .cacheInMemory(true) // 设置下载的图片是否缓存在内存中
                .cacheOnDisk(true) // 设置下载的图片是否缓存在SD卡中
                .bitmapConfig(Bitmap.Config.RGB_565)
                .build(); // 构建完成
        if (!TextUtils.isEmpty(url)) {
            ImageLoader.getInstance().displayImage(url, iv,options, new ImageLoadingListener() {
                @Override
                public void onLoadingStarted(String s, View view) {

                }

                @Override
                public void onLoadingFailed(String s, View view, FailReason failReason) {

                }

                @Override
                public void onLoadingComplete(String s, View view, Bitmap bitmap) {
                    if (iv.getTag().equals(url)) {
                        iv.setImageBitmap(bitmap);
                    }
                }

                @Override
                public void onLoadingCancelled(String s, View view) {

                }
            });
        }*/
    }

    /**
     * 加载图片
     * @param url
     * @param iv
     */
    public static void loadImage(final String url, final ImageView iv) {
        Picasso.with(iv.getContext())
                .load(url)
                .resize(AutoUtils.getPercentWidthSize(200), AutoUtils.getPercentWidthSize(200))
                .centerCrop().config(Bitmap.Config.RGB_565).placeholder(R.drawable.ease_default_image)
                .into(iv);
    }
    public static void loadImage(final String url, final ImageView iv,Object tag) {
        Picasso.with(iv.getContext())
                .load(url)
                .resize(AutoUtils.getPercentWidthSize(200), AutoUtils.getPercentWidthSize(200))
                .centerCrop().config(Bitmap.Config.RGB_565).tag(tag).placeholder(R.drawable.ease_default_image)
                .into(iv);
        // 使用DisplayImageOptions.Builder()创建DisplayImageOptions
       /* DisplayImageOptions options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.loading) // 设置图片下载期间显示的图片
                .showImageForEmptyUri(R.drawable.loading) // 设置图片Uri为空或是错误的时候显示的图片
                .showImageOnFail(R.drawable.loading) // 设置图片加载或解码过程中发生错误显示的图片
                .imageScaleType(ImageScaleType.IN_SAMPLE_INT)
                .cacheInMemory(true) // 设置下载的图片是否缓存在内存中
                .cacheOnDisk(true) // 设置下载的图片是否缓存在SD卡中
                .bitmapConfig(Bitmap.Config.RGB_565)
                .build(); // 构建完成
        ImageSize size=new ImageSize(AutoUtils.getPercentWidthSize(200),AutoUtils.getPercentWidthSize(200));
        ImageLoader.getInstance().loadImage(url, size, options, new ImageLoadingListener() {
            @Override
            public void onLoadingStarted(String s, View view) {

            }

            @Override
            public void onLoadingFailed(String s, View view, FailReason failReason) {

            }

            @Override
            public void onLoadingComplete(String s, View view, Bitmap bitmap) {
                if(iv.getTag().equals(url)) {
                    iv.setImageBitmap(bitmap);
                }
            }

            @Override
            public void onLoadingCancelled(String s, View view) {

            }
        });*/


    }
    public static void loadLocatalImage(final String url, final ImageView iv) {
        Picasso.with(iv.getContext())
                .load(new File(url))
                .resize(AutoUtils.getPercentWidthSize(200), AutoUtils.getPercentWidthSize(200))
                .centerCrop().config(Bitmap.Config.RGB_565).placeholder(R.drawable.ease_default_image)
                .into(iv);
    }
    /**
     * 获取拓展存储Cache的绝对路径
     *
     * @param context
     */
    public static String getExternalCacheDir(Context context) {

        StringBuilder sb = new StringBuilder();
        File file = context.getExternalCacheDir();
        // In some case, even the sd card is mounted,
        // getExternalCacheDir will return null
        // may be it is nearly full.
        if (file == null) {
            sb.append(Environment.getExternalStorageDirectory().getPath()).append("/Android/data/").append(context.getPackageName())
                    .append("/cache/").append(File.separator).toString();
        } else {
            sb.append(file.getAbsolutePath()).append(File.separator);
        }
        return sb.toString();
    }
    /**
     * 调用系统拍照
     */
    public static void intoCimera(Activity mContext,int type){
        Intent intent = new Intent(mContext, MultiImageSelectorActivity.class);
        // 是否显示拍摄图片
        intent.putExtra(MultiImageSelectorActivity.EXTRA_SHOW_CAMERA, true);
        // 最大可选择图片数量
        intent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_COUNT, 9);
        // 选择模式
        intent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_MODE, type);
        //Intent intent= CinemaUtil.IntoSystemPic(mContext);
        //mContext.startActivityForResult(intent, 0x00);
        mContext.startActivityForResult(intent,0x00);

    }
    public static void updateAver(final Activity mContext,String imagepath, final ProgressDialog pd, final ImageView iv){
        pd.setCanceledOnTouchOutside(false);
        pd.setTitle("正在更新,请等待...");
        pd.show();
        LogUtils.i(imagepath);
        HttpUtils httpUtils = new HttpUtils(5000);
        RequestParams params = new RequestParams("UTF-8");
        params.addBodyParameter("userid", DemoHelper.getInstance().getCurrentUsernName());
        params.addBodyParameter("file1", new File(imagepath));
        httpUtils.send(HttpRequest.HttpMethod.POST, Api.UPDATEUSERHEADLOGO, params, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                LogUtils.i(responseInfo.result);
                pd.dismiss();
                try {
                    JSONObject jb = new JSONObject(responseInfo.result);
                    if (jb.optString("result").equals("0")) {
                        Utils.showToast("头像更换成功", mContext);
                        String path = jb.optString("bodyvalue");
                        Utils.setAver(path, iv);
                        Conversion.getInstance().setHeadUrl(path);
                        PersonInfo Parent = DemoApplication.getInstance().getDb().findFirst(Selector.from(PersonInfo.class).where("userid", "=", DemoHelper.getInstance().getCurrentUsernName()));
                        if (Parent != null) {
                            Parent.setHeadUrl(path);
                            DemoApplication.getInstance().getDb().update(Parent);
                        }

                    } else {
                        Utils.showToast("头像更换失败", mContext);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (DbException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(HttpException e, String s) {
                pd.dismiss();
                e.printStackTrace();
                Utils.showToast("头像更新失败", mContext);
                LogUtils.i(s);
            }
        });
    }

    /**
     * 点击头像进入详细信息界面
     * @param context
     * @param userID
     */
    public static void intoInfo(Context context,String userID){
        LogUtils.i(userID);
        if(userID.equals(DemoHelper.getInstance().getCurrentUsernName())){
            Intent intent=new Intent(context, SetActivity_set.class);
            context.startActivity(intent);
        }else {
            Intent intent=new Intent(context, UserProfileActivity.class);
            intent.putExtra("username",userID);
            context.startActivity(intent);
        }
    }
    /**
     * 获取当前应用程序的版本号
     */
    public static String getVersion(Context context) {
        String version=null;
        PackageManager pm = context.getPackageManager();
        try {
            PackageInfo packinfo = pm.getPackageInfo(context.getPackageName(), 0);
            version=packinfo.versionName;
            return version;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 工作和应用界面通过对应的tag进入相关的界面操作
     * @param tag
     * @param context
     */
    public static void intoApp(String tag,Context context){
        switch (tag){
            case "app0001":
                Intent intent1=new Intent(context, CurrentLocationActivity.class);
                intent1.putExtra("type","jiandao");
                context.startActivity(intent1);
                break;
            case "app0002":
                context.startActivity(new Intent(context, MeetingDoActivity.class));
                break;
            case "app0003":

                break;
            case "app0004":
                Intent intent=new Intent("android.intent.inputActivity");
                context.startActivity(intent);
                break;
            case "app0005":
                Intent intent3=new Intent(context, PostUserinfoActivity.class);
                intent3.putExtra("type",1);
                context.startActivity(intent3);
                break;
            case "app0006":
                Intent intent2=new Intent(context, CaptureActivity.class);
                context.startActivity(intent2);
                break;
        }
    }

    /**
     * 工作和应用界面通过对应的logo返回对应的图标id
     * @param image
     */
    public static int forImage(String image){
        int resId=R.drawable.iv_work_approve;
        switch (image){
            //二维码
            case "barcode.png":
                resId=R.drawable.iv_work_approve;
                break;

            //会议
            case "meeting.png":
                resId=R.drawable.iv_apply_meeting;
                break;
            //签到
            case "sign.png":
                resId=R.drawable.iv_apply_hr;
                break;

            //投票
            case "poll.png":
                resId=R.drawable.iv_apply_hr;
                break;
            //任务
            case "task.png":
                resId=R.drawable.iv_work_task;
                break;
            //心情
            case "mood.png":
                resId=R.drawable.iv_work_project;
                break;
            //审批
            case "folder.png":
                resId=R.drawable.iv_work_approve;
                break;
            //报销
            case "baoxiao.png":
                resId=R.drawable.iv_work_account;
                break;
            //出差
            case "chuchai.png":
                resId=R.drawable.iv_work_log;
                break;
            //物品
            case "wupin.png":
                resId=R.drawable.iv_work_account;
                break;
            //请假
            case "qingjia.png":
                resId=R.drawable.iv_work_leave;
                break;

        }
        return resId;
    }
}
