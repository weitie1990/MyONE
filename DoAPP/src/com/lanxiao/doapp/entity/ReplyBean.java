package com.lanxiao.doapp.entity;

import java.io.Serializable;

/**
 * Created by Thinkpad on 2015/11/30.
 */
public class ReplyBean implements Serializable {
    private String reply_id;			//内容ID
    private String replyAccount;	//回复人账号
    private String replyNickname;	//回复人昵称
    private String touxiang;	//回复人头像
    private String time;	//回复人时间
    private String  [] images;	//回复人回复的图片
    private String [] attachments;	//回复人回复的附件
    private String sound;	//回复的声音
    private String geopara;	//回复的经纬度
    private String geopos;	//回复人的所在位置名
    private String replyContent;	//回复的内容
    private String Type;
    private String chatroomid;
    private String chatroomname;
    private String replyCellphone;

    public String getReplyCellphone() {
        return replyCellphone;
    }

    public void setReplyCellphone(String replyCellphone) {
        this.replyCellphone = replyCellphone;
    }

    public String getType() {
        return Type;
    }

    public void setType(String type) {
        Type = type;
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

    public String [] getImages() {
        return images;
    }

    public void setImages(String [] images) {
        this.images = images;
    }

    public String[] getAttachments() {
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
    private String commentNickname;	//被回复人昵称




    public String getReply_id() {
        return reply_id;
    }

    public void setReply_id(String reply_id) {
        this.reply_id = reply_id;
    }

    public String getReplyAccount() {
        return replyAccount;
    }

    public void setReplyAccount(String replyAccount) {
        this.replyAccount = replyAccount;
    }

    public String getReplyNickname() {
        return replyNickname;
    }

    public void setReplyNickname(String replyNickname) {
        this.replyNickname = replyNickname;
    }

    public String getChatroomid() {
        return chatroomid;
    }

    public void setChatroomid(String chatroomid) {
        this.chatroomid = chatroomid;
    }

    public String getChatroomname() {
        return chatroomname;
    }

    public void setChatroomname(String chatroomname) {
        this.chatroomname = chatroomname;
    }

    public String getCommentNickname() {
        return commentNickname;
    }

    public void setCommentNickname(String commentNickname) {
        this.commentNickname = commentNickname;
    }

    public String getReplyContent() {
        return replyContent;
    }

    public void setReplyContent(String replyContent) {
        this.replyContent = replyContent;
    }
}
