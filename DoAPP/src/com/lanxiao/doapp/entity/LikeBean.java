package com.lanxiao.doapp.entity;

import java.io.Serializable;

/**
 * Created by Thinkpad on 2016/2/18.
 * 首页的回复类
 */
public class LikeBean implements Serializable{
    private int id;
    private String userId;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    private String userName;
}
