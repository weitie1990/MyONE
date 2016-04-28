package com.lanxiao.doapp.entity;

/**
 * Created by Thinkpad on 2016/2/7.
 */
public class DoWorkItem {
    private int id;

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String userid;
    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
    private int isAdd;
    private int image;

    public int getIsAdd() {
        return isAdd;
    }

    public void setIsAdd(int isAdd) {
        this.isAdd = isAdd;
    }

    private String text;

    public DoWorkItem(int image, String text) {
        this.image = image;
        this.text = text;
    }
    public DoWorkItem() {

    }
}
