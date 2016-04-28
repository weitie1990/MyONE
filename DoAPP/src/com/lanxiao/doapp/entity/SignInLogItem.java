package com.lanxiao.doapp.entity;

import com.lidroid.xutils.db.annotation.Id;

/**
 * Created by Thinkpad on 2016/2/28.
 */
public class SignInLogItem {
    @Id
    private int id;
    private String userId;
    private String firstTime;
    private String firstAddress;
    private String twoTime;
    private String twoAddress;
    private String twoSatuas;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getFirstTime() {
        return firstTime;
    }

    public void setFirstTime(String firstTime) {
        this.firstTime = firstTime;
    }

    public String getFirstAddress() {
        return firstAddress;
    }

    public void setFirstAddress(String firstAddress) {
        this.firstAddress = firstAddress;
    }

    public String getTwoTime() {
        return twoTime;
    }

    public void setTwoTime(String twoTime) {
        this.twoTime = twoTime;
    }

    public String getTwoAddress() {
        return twoAddress;
    }

    public void setTwoAddress(String twoAddress) {
        this.twoAddress = twoAddress;
    }

    public String getTwoSatuas() {
        return twoSatuas;
    }

    public void setTwoSatuas(String twoSatuas) {
        this.twoSatuas = twoSatuas;
    }
}
