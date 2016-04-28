package com.lanxiao.doapp.entity;

import java.io.Serializable;

/**
 * Created by Thinkpad on 2016/2/18.
 * 链接的内容
 */
public class LinkContent implements Serializable{
    private int id;
    private String user;
    private String userId;
    private String touxiang;
    private String time;
    private String maincontent;
    private String []images;
    private String []attachments;
    private String sound;
    private String geopara;
    private String geopos;
    private String linktype;
    private String linkaddress;

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getTouxiang() {
        return touxiang;
    }

    public void setTouxiang(String touxiang) {
        this.touxiang = touxiang;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getMaincontent() {
        return maincontent;
    }

    public void setMaincontent(String maincontent) {
        this.maincontent = maincontent;
    }

    public String[] getImages() {
        return images;
    }

    public void setImages(String []images) {
        this.images = images;
    }

    public String []getAttachments() {
        return attachments;
    }

    public void setAttachments(String []attachments) {
        this.attachments = attachments;
    }

    public String getSound() {
        return sound;
    }

    public void setSound(String sound) {
        this.sound = sound;
    }

    public String getGeopara() {
        return geopara;
    }

    public void setGeopara(String geopara) {
        this.geopara = geopara;
    }

    public String getGeopos() {
        return geopos;
    }

    public void setGeopos(String geopos) {
        this.geopos = geopos;
    }

    public String getLinktype() {
        return linktype;
    }

    public void setLinktype(String linktype) {
        this.linktype = linktype;
    }

    public String getLinkaddress() {
        return linkaddress;
    }

    public void setLinkaddress(String linkaddress) {
        this.linkaddress = linkaddress;
    }
}
