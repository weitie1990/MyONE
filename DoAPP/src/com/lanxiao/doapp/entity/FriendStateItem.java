package com.lanxiao.doapp.entity;

import android.graphics.Bitmap;

import com.easemob.chat.LocationMessageBody;

import java.io.Serializable;
import java.util.List;

public class FriendStateItem implements Serializable{
	public static final String MEETING="meeting";//会议类型
	public static final String TASK="task";//任务类型
	public static final String APPROVE="approve";//审批类型
	public static final String ADAPPROVE="advapprove";//复杂审批类型
	public static final String BLOG="blog";//个人微博类型
	private String userIcon;
	private String name;
	private String date;
	private String content;
	private String [] images;
	private String fileUrl;
	private String type;
	private String docid;
	private String cellphone;

	public String getCellphone() {
		return cellphone;
	}

	public void setCellphone(String cellphone) {
		this.cellphone = cellphone;
	}

	public String getDocid() {
		return docid;
	}

	public void setDocid(String docid) {
		this.docid = docid;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String [] getAttachments() {
		return attachments;
	}

	public void setAttachments(String [] attachments) {
		this.attachments = attachments;
	}

	public String getShowreply() {
		return showreply;
	}

	public void setShowreply(String showreply) {
		this.showreply = showreply;
	}

	public String getShowlike() {
		return showlike;
	}

	public void setShowlike(String showlike) {
		this.showlike = showlike;
	}

	public String getShowshare() {
		return showshare;
	}

	public void setShowshare(String showshare) {
		this.showshare = showshare;
	}

	public String getLikenumber() {
		return likenumber;
	}

	public void setLikenumber(String likenumber) {
		this.likenumber = likenumber;
	}

	public String getReplynumber() {
		return replynumber;
	}

	public void setReplynumber(String replynumber) {
		this.replynumber = replynumber;
	}

	public String getSharenumber() {
		return sharenumber;
	}

	public void setSharenumber(String sharenumber) {
		this.sharenumber = sharenumber;
	}

	public String getOperatebutton() {
		return operatebutton;
	}

	public void setOperatebutton(String operatebutton) {
		this.operatebutton = operatebutton;
	}

	public String getMenu_delete() {
		return menu_delete;
	}

	public void setMenu_delete(String menu_delete) {
		this.menu_delete = menu_delete;
	}

	public String getMenu_takeback() {
		return menu_takeback;
	}

	public void setMenu_takeback(String menu_takeback) {
		this.menu_takeback = menu_takeback;
	}

	public String getMenu_top() {
		return menu_top;
	}

	public void setMenu_top(String menu_top) {
		this.menu_top = menu_top;
	}

	public List<ReplyBean> getReplyBean() {
		return replyBean;
	}

	public void setReplyBean(List<ReplyBean> replyBean) {
		this.replyBean = replyBean;
	}

	public List<LikeBean> getLikeBean() {
		return likeBean;
	}

	public void setLikeBean(List<LikeBean> likeBean) {
		this.likeBean = likeBean;
	}

	public List<LinkContent> getLinkContent() {
		return linkContent;
	}

	public void setLinkContent(List<LinkContent> linkContent) {
		this.linkContent = linkContent;
	}

	private String latitude;
	private String longitude;
	private String userId;
	private String [] attachments;//附件
	private String showlike;//是否显示点赞按钮
	private String showreply;//是否显示聊天按钮
	private String showshare;//是否显示转发
	private String likenumber;//点赞数
	private String replynumber;//聊天数
	private String sharenumber;//分享数
	private String operatebutton;//是否显示操作按钮
	private String menu_delete;//是否显示子菜单中的删除按钮
	private String menu_takeback;//是否显示子菜单中的回收按钮
	private String menu_top;//是否显示子菜单中的置顶按钮
	private List<ReplyBean> replyBean;//回复的内容
	private List<LikeBean> likeBean;//点赞的人
	private List<LinkContent> linkContent;//链接的内容


















	public String getUserIcon() {
		return userIcon;
	}
	public void setUserIcon(String userIcon) {
		this.userIcon = userIcon;
	}
	public String getLatitude() {
		return latitude;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

	public String getLongitude() {
		return longitude;
	}

	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}

	public String getFileUrl() {
		return fileUrl;
	}

	public void setFileUrl(String fileUrl) {
		this.fileUrl = fileUrl;
	}

	public String[] getImages() {
		return images;
	}

	public void setImages(String[] images) {
		this.images = images;
	}

	private String endTime;
	private int filetye;
	private boolean hasVoice;
	private String videoUri;
	private Bitmap bitmap;
	private int VoiceTime;
	private String locationName;
	private String imageUrl;
	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public LocationMessageBody getLocBody() {
		return locBody;
	}

	public void setLocBody(LocationMessageBody locBody) {
		this.locBody = locBody;
	}

	private LocationMessageBody locBody;

	public int getVoiceTime() {
		return VoiceTime;
	}

	public void setVoiceTime(int voiceTime) {
		VoiceTime = voiceTime;
	}

	public void setLocationName(String locationName) {

		this.locationName = locationName;
	}

	public boolean isHasVoice() {
		return hasVoice;
	}
	public FriendStateItem(){

	}

	public Bitmap getBitmap() {
		return bitmap;
	}

	public void setBitmap(Bitmap bitmap) {
		this.bitmap = bitmap;
	}



	public String getVideoUri() {
		return videoUri;
	}

	public void setVideoUri(String videoUri) {
		this.videoUri = videoUri;
	}

	public void setHasVoice(boolean hasVoice) {
		this.hasVoice = hasVoice;
	}

	public int getFiletye() {
		return filetye;
	}

	public void setFiletye(int filetye) {
		this.filetye = filetye;
	}




	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getEndTime() {
		return endTime;
	}
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public void setVideoTime(int time) {
		this.VoiceTime=time;
	}public int getVideoTime() {
		return VoiceTime;
	}

	public void setLocationNmae(String name) {
		this.locationName=name;
	}
	public String getLocationName(){
		return locationName;
	}
}

