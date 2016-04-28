package com.easemob.easeui.widget;

import android.content.Context;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.easemob.chat.EMChatManager;
import com.easemob.easeui.R;
import com.easemob.easeui.domain.EaseUser;
import com.easemob.easeui.utils.EaseUserUtils;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.exception.DbException;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.util.LogUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by Thinkpad on 2016/1/22.
 */
public class MyUtils {
    //查找朋友的URL
    public static final String SEARCH_FRIEND="http://www.dosns.net/modules/friend/search.aspx";
    //查找所有朋友
    public static final String SEARCH_ALL_FRIEND="http://www.dosns.net/modules/friend/userinfo.aspx";
    public static void setNickNameandAver(String from, final ImageView iv, final TextView tv, final Context context){
       /* LogUtils.i(from+"setNickNameandAver");
        HttpUtils hu=new HttpUtils();
        RequestParams rp=new RequestParams("UTF-8");
        rp.addBodyParameter("useridlist",from);
        hu.send(com.lidroid.xutils.http.client.HttpRequest.HttpMethod.POST, SEARCH_ALL_FRIEND, rp, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                try {
                    JSONObject js = new JSONObject(responseInfo.result);
                    JSONArray ja=js.getJSONArray("userlist");
                    if(ja.length()==0){
                        LogUtils.i("未查找到");
                        return;
                    }
                    for (int i=0;i<ja.length();i++) {
                        JSONObject js2 = ja.getJSONObject(i);
                        String ToNickName=js2.optString("nickname");
                        String heanUrl=js2.optString("touxiang");
                        //ToNickName=js.optString("username");
                        //tv.setVisibility(View.VISIBLE);
                        tv.setText(ToNickName);
                        if(!TextUtils.isEmpty(heanUrl)){
                            BitmapUtils bu=new BitmapUtils(context);
                            bu.display(iv,heanUrl);
                        }else{
                            iv.setImageResource(R.drawable.ease_default_avatar);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onFailure(HttpException e, String s) {
                LogUtils.i(e.getMessage() + ":" + s);
            }
        });*/
        DbUtils db=DbUtils.create(context, "doapp");
        try {
            EaseUser Parent = db.findFirst(Selector.from(EaseUser.class).where("userid", "=", from).and("whosfriend","=", EMChatManager.getInstance().getCurrentUser()));
            LogUtils.i((Parent==null)+"Parent");
            if(Parent!=null){
                EaseUserUtils.setAver(Parent.getAvatar(),iv);
                tv.setText(Parent.getNickName());
            }
        } catch (DbException e) {
            e.printStackTrace();
        }
    }
    public static void setNickNameandAverFromSever(String from, final ImageView iv, final TextView tv, final Context context){
        LogUtils.i(from+"setNickNameandAver");
        HttpUtils hu=new HttpUtils();
        RequestParams rp=new RequestParams("UTF-8");
        rp.addBodyParameter("useridlist",from);
        hu.send(com.lidroid.xutils.http.client.HttpRequest.HttpMethod.POST, SEARCH_ALL_FRIEND, rp, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                try {
                    JSONObject js = new JSONObject(responseInfo.result);
                    JSONArray ja=js.getJSONArray("userlist");
                    if(ja.length()==0){
                        LogUtils.i("未查找到");
                        return;
                    }
                    for (int i=0;i<ja.length();i++) {
                        JSONObject js2 = ja.getJSONObject(i);
                        String ToNickName=js2.optString("nickname");
                        String heanUrl=js2.optString("touxiang");
                        //ToNickName=js.optString("username");
                        //tv.setVisibility(View.VISIBLE);
                        tv.setText(ToNickName);
                        if(!TextUtils.isEmpty(heanUrl)){
                            BitmapUtils bu=new BitmapUtils(context);
                            bu.display(iv,heanUrl);
                        }else{
                            iv.setImageResource(R.drawable.ease_default_avatar);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onFailure(HttpException e, String s) {
                LogUtils.i(e.getMessage() + ":" + s);
            }
        });

    }
    public static void setNickName(String from, final TextView tv, final Context context){
        LogUtils.i(from+"setNickNameandAver");
        HttpUtils hu=new HttpUtils();
        RequestParams rp=new RequestParams("UTF-8");
        rp.addBodyParameter("useridlist",from);
        hu.send(com.lidroid.xutils.http.client.HttpRequest.HttpMethod.POST, SEARCH_ALL_FRIEND, rp, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                try {
                    JSONObject js = new JSONObject(responseInfo.result);
                    JSONArray ja=js.getJSONArray("userlist");
                    if(ja.length()==0){
                        LogUtils.i("未查找到");
                        return;
                    }
                    for (int i=0;i<ja.length();i++) {
                        JSONObject js2 = ja.getJSONObject(i);
                        String ToNickName=js2.optString("nickname");
                        String heanUrl=js2.optString("touxiang");
                        //ToNickName=js.optString("username");
                        //tv.setVisibility(View.VISIBLE);
                        tv.setText(ToNickName);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onFailure(HttpException e, String s) {
                LogUtils.i(e.getMessage() + ":" + s);
            }
        });
    }
    public static void setNickNameandAver(String from, final ImageView iv, final TextView tv, final Context context,final TextView nickname){
        LogUtils.i(from+"setNickNameandAver");
        HttpUtils hu=new HttpUtils();
        RequestParams rp=new RequestParams("UTF-8");
        rp.addBodyParameter("useridlist",from);
        hu.send(com.lidroid.xutils.http.client.HttpRequest.HttpMethod.POST, SEARCH_ALL_FRIEND, rp, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                LogUtils.i(responseInfo.result);
                try {
                    JSONObject js = new JSONObject(responseInfo.result);
                    JSONArray ja=js.getJSONArray("userlist");
                    if(ja.length()==0){
                        LogUtils.i("未查找到");
                        return;
                    }
                    for (int i=0;i<ja.length();i++) {
                        JSONObject js2 = ja.getJSONObject(i);
                        String ToNickName=js2.optString("nickname");
                        String heanUrl=js2.optString("touxiang");
                        //ToNickName=js.optString("username");
                        tv.setText(ToNickName);
                        nickname.setText(ToNickName);
                        if(!TextUtils.isEmpty(heanUrl)){
                            BitmapUtils bu=new BitmapUtils(context);
                            bu.display(iv,heanUrl);
                        }else{
                            iv.setImageResource(R.drawable.ease_default_avatar);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onFailure(HttpException e, String s) {
                LogUtils.i(e.getMessage() + ":" + s);
            }
        });
    }
    /**
     * 只设置标题
     * @param from
     * @param titleBar
     */
    public static void setNickNameandAver(String from,final EaseTitleBar titleBar,Context context){
       /* LogUtils.i(from + "setNickNameandAver");
        HttpUtils hu=new HttpUtils();
        RequestParams rp=new RequestParams("UTF-8");
        rp.addBodyParameter("useridlist",from);
        hu.send(com.lidroid.xutils.http.client.HttpRequest.HttpMethod.POST, SEARCH_ALL_FRIEND, rp, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                LogUtils.i(responseInfo.result);
                try {
                    JSONObject js = new JSONObject(responseInfo.result);
                    JSONArray ja=js.getJSONArray("userlist");
                    if(ja.length()==0){
                        LogUtils.i("未查找到");
                        return;
                    }
                    for (int i=0;i<ja.length();i++) {
                        JSONObject js2 = ja.getJSONObject(i);
                        String ToNickName=js2.optString("nickname");
                        String heanUrl=js2.optString("touxiang");
                        //ToNickName=js.optString("username");
                        titleBar.setTitle(ToNickName);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onFailure(HttpException e, String s) {
                LogUtils.i(e.getMessage() + ":" + s);
            }
        });*/
        DbUtils db=DbUtils.create(context, "doapp");
        try {
            EaseUser Parent = db.findFirst(Selector.from(EaseUser.class).where("userid", "=", from).and("whosfriend","=", EMChatManager.getInstance().getCurrentUser()));
            LogUtils.i((Parent==null)+"Parent");
            if(Parent!=null){
                titleBar.setTitle(Parent.getNickName());
            }
        } catch (DbException e) {
            e.printStackTrace();
        }
    }

    /**
     * 群组中添加朋友
     * @param sb
     * @param contactList
     */
    public static void getMachFriend(String sb, final List<EaseUser> contactList){
        RequestParams params = new RequestParams("UTF-8");
        params.addBodyParameter("useridlist", sb);
        HttpUtils httpUtils = new HttpUtils();
        httpUtils.send(com.lidroid.xutils.http.client.HttpRequest.HttpMethod.POST, SEARCH_ALL_FRIEND, params, new RequestCallBack<String>() {
            @Override
            public void onFailure(HttpException arg0, String arg1) {
                com.lidroid.xutils.util.LogUtils.i(arg0.getMessage() + ":" + arg1);
                return;
            }

            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                com.lidroid.xutils.util.LogUtils.i(responseInfo.result);
                JSONObject js= null;
                try {
                    js = new JSONObject(responseInfo.result);
                    JSONArray ja=js.getJSONArray("userlist");
                    for (int i=0;i<ja.length();i++){
                        JSONObject js2=ja.getJSONObject(i);
                        EaseUser user = new EaseUser();
                        user.setUserId(js2.optString("userid"));
                        user.setAvatar(js2.optString("touxiang"));
                        user.setNickName(js2.optString("nickname"));
                        contactList.add(user);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
    }

}
