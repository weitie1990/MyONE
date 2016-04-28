package com.easemob.easeui.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.widget.ImageView;
import android.widget.TextView;

import com.easemob.easeui.R;
import com.easemob.easeui.controller.EaseUI;
import com.easemob.easeui.controller.EaseUI.EaseUserProfileProvider;
import com.easemob.easeui.domain.EaseUser;
import com.squareup.picasso.Picasso;
import com.zhy.autolayout.utils.AutoUtils;

public class EaseUserUtils {
    
    static EaseUserProfileProvider userProvider;
    
    static {
        userProvider = EaseUI.getInstance().getUserProfileProvider();
    }
    
    /**
     * 根据username获取相应user
     * @param username
     * @return
     */
    public static EaseUser getUserInfo(String username){
        if(userProvider != null)
            return userProvider.getUser(username);
        
        return null;
    }
    

    
    /**
     * 设置用户昵称
     */
    public static void setUserNick(String username,TextView textView){
        if(textView != null){
        	EaseUser user = getUserInfo(username);
        	if(user != null && user.getNick() != null){
        		textView.setText(user.getNick());
        	}else{
        		textView.setText(username);
        	}
        }
    }
    public static void setAver(final String url, final ImageView iv) {
        Picasso.with(iv.getContext())
                .load(url)
                .resize(AutoUtils.getPercentWidthSize(100), AutoUtils.getPercentWidthSize(100))
                .centerCrop().config(Bitmap.Config.RGB_565).placeholder(R.drawable.ease_default_avatar)
                .into(iv);
    }
    public static void setImage(final String url, final ImageView iv) {
        Picasso.with(iv.getContext())
                .load(url)
                .config(Bitmap.Config.RGB_565)
                .into(iv);
    }
}
