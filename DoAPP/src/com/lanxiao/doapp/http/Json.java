package com.lanxiao.doapp.http;

import android.text.TextUtils;

import com.lanxiao.doapp.entity.FriendStateItem;
import com.lanxiao.doapp.entity.LikeBean;
import com.lanxiao.doapp.entity.LinkContent;
import com.lanxiao.doapp.entity.Result;
import com.lanxiao.doapp.entity.ReplyBean;
import com.lidroid.xutils.util.LogUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Thinkpad on 2015/12/23.
 */
public class Json {
    public static final String TOUXIANG="touxiang";
    public static final String USER="user";
    public static final String TIME="time";
    public static final String MAINCONTENT="maincontent";
    public static final String LASTCONTENT="lastcontent";
    public static final String IMAG_URL="img_url";
    public static final String TYPE="type";
    public static final String IMAGES="images";
    public static final String SOUND="sound";
    public static final String ALERTTIME="alerttime";
    /**
     * 获取发表信息
     * @param josn
     * @return
     */
    public static ArrayList<FriendStateItem> parseRecommendData(String josn){
        ArrayList<FriendStateItem> arrRecom = new ArrayList<FriendStateItem>();
        try {
            JSONObject jsonObjectSJ=new JSONObject(josn);
            JSONArray jsonArrays=jsonObjectSJ.getJSONArray("doinglist");
            LogUtils.i(jsonArrays.length()+"");
            for(int i=0;i<jsonArrays.length();i++){
                JSONObject jsonObject = jsonArrays.optJSONObject(i);
                FriendStateItem fs = new FriendStateItem();
                fs.setCellphone(jsonObject.optString("cellphone"));
                fs.setUserIcon(jsonObject.optString("touxiang"));
                fs.setUserId(jsonObject.optString("userid"));
                fs.setDocid(jsonObject.optString("docid"));
                fs.setName(jsonObject.optString("user"));
                fs.setDate(jsonObject.optString("time"));
                fs.setType(jsonObject.optString("type"));
                fs.setContent(jsonObject.optString("maincontent"));
                fs.setLocationNmae(jsonObject.optString("geopos"));
                String lq=jsonObject.optString("geopara");
                if(!TextUtils.isEmpty(lq)){
                    String [] ar=lq.split(",");
                    fs.setLongitude(ar[0]);
                    fs.setLatitude(ar[1]);
                }
                JSONArray jsImages=(jsonObject.optJSONArray("images"));
                if(jsImages!=null&&jsImages.length()!=0) {
                    String[] images = new String[jsImages.length()];
                    for (int j = 0; j < jsImages.length(); j++) {
                        images[j] = jsImages.optString(j);
                    }
                    fs.setImages(images);
                }
                JSONArray jsattachments=jsonObject.optJSONArray("attachments");
                if(jsattachments!=null&&jsattachments.length()!=0) {
                    String[] attachments = new String[jsattachments.length()];
                    for (int k = 0; k < jsattachments.length(); k++) {
                        attachments[k] = jsattachments.optString(k);
                    }
                    fs.setAttachments(attachments);
                }
                fs.setVideoUri(jsonObject.optString("sound"));
                fs.setEndTime(jsonObject.optString("alerttime"));
                fs.setShowlike(jsonObject.optString("showlike"));
                fs.setShowreply(jsonObject.optString("showreply"));
                fs.setShowshare(jsonObject.optString("showshare"));
                fs.setLikenumber(jsonObject.optString("likenumber"));
                fs.setReplynumber(jsonObject.optString("replynumber"));
                fs.setSharenumber(jsonObject.optString("sharenumber"));
                fs.setOperatebutton(jsonObject.optString("operatebutton"));
                fs.setMenu_delete(jsonObject.optString("menu_delete"));
                fs.setMenu_takeback(jsonObject.optString("menu_takeback"));
                fs.setMenu_top(jsonObject.optString("menu_top"));
                //解析操作的内容
                JSONArray ja=jsonObject.optJSONArray("reply");
                if(ja!=null&&ja.length()!=0) {
                    List<ReplyBean> list_reply=new ArrayList<>();
                    for (int j = 0; j < ja.length(); j++) {
                        JSONObject jo_re = ja.optJSONObject(j);
                        ReplyBean rb = new ReplyBean();
                        rb.setReplyNickname(jo_re.optString("user"));
                        rb.setReply_id(jo_re.optString("userid"));
                        rb.setTouxiang(jo_re.optString("touxiang"));
                        rb.setTime(jo_re.optString("time"));
                        rb.setReplyContent(jo_re.optString("lastcontent"));
                        rb.setSound(jo_re.optString("sound"));
                        rb.setGeopara(jo_re.optString("geopara"));
                        rb.setGeopos(jo_re.optString("geopos"));
                        rb.setReplyCellphone(jo_re.optString("cellphone"));
                        JSONArray Images=jo_re.optJSONArray("images");
                        if(Images!=null&&Images.length()!=0) {
                            String[] images = new String[Images.length()];
                            for (int k = 0; k < Images.length(); k++) {
                                images[k] = Images.optString(k);
                            }
                            rb.setImages(images);
                        }
                        JSONArray jsattachments2=jo_re.optJSONArray("attachments");
                        if(jsattachments2!=null&&jsattachments2.length()!=0) {
                            String[] attachments = new String[jsattachments2.length()];
                            for (int k = 0; k < jsattachments2.length(); k++) {
                                attachments[k] = jsattachments2.optString(k);
                            }
                            rb.setAttachments(attachments);
                        }
                        list_reply.add(rb);
                    }
                    fs.setReplyBean(list_reply);
                }
                //解析点赞的人
                JSONArray ja_like=jsonObject.optJSONArray("like");
                if(ja_like!=null&&ja_like.length()!=0){
                    List<LikeBean> list_like=new ArrayList<>();
                    for (int k = 0; k < ja_like.length(); k++) {
                        JSONObject jb_like=ja_like.getJSONObject(k);
                        LikeBean likeBean=new LikeBean();
                        LogUtils.i(jb_like.optString("userid"));
                        likeBean.setUserId(jb_like.optString("userid"));
                        likeBean.setUserName(jb_like.optString("user"));
                        list_like.add(likeBean);
                    }
                    fs.setLikeBean(list_like);
                }
                //解析链接的内容
                JSONArray ja_link=jsonObject.optJSONArray("linkcontent");
                if(ja_link!=null&&ja_link.length()!=0){
                    List<LinkContent> list_link=new ArrayList<>();
                    for (int k = 0; k < ja_link.length(); k++) {
                        JSONObject jb_link=ja_link.getJSONObject(k);
                        LinkContent linkContent=new LinkContent();
                        linkContent.setUserId(jb_link.optString("userid"));
                        linkContent.setUser(jb_link.optString("user"));
                        linkContent.setTime(jb_link.optString("time"));
                        linkContent.setMaincontent(jb_link.optString("maincontent"));
                        linkContent.setLinktype(jb_link.optString("linktype"));
                        linkContent.setLinkaddress(jb_link.optString("linkaddress"));
                        JSONArray js_link_images=jb_link.optJSONArray("images");
                        if(js_link_images!=null&&js_link_images.length()!=0) {
                            String[] images = new String[js_link_images.length()];
                            for (int j = 0; j < js_link_images.length(); j++) {
                                images[j] = js_link_images.optString(j);
                            }
                            linkContent.setImages(images);
                        }
                        list_link.add(linkContent) ;
                    }
                    fs.setLinkContent(list_link);
                }
                arrRecom.add(fs);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return arrRecom;
    }
    /**
     * 获取操作首页后的信息
     * @param josn
     * @return
     */
    public static ArrayList<FriendStateItem> parseOpaterdData(String josn){
        ArrayList<FriendStateItem> arrRecom = new ArrayList<FriendStateItem>();
        try {
            JSONObject jsonObjectSJ=new JSONObject(josn);
            JSONArray jsonArrays=jsonObjectSJ.getJSONArray("replylist");
            LogUtils.i(jsonArrays.length()+"");
            for(int i=0;i<jsonArrays.length();i++){
                JSONObject jsonObject = jsonArrays.optJSONObject(i);
                FriendStateItem fs = new FriendStateItem();
                fs.setDocid(jsonObject.optString("docid"));
                fs.setShowlike(jsonObject.optString("showlike"));
                fs.setShowreply(jsonObject.optString("showreply"));
                fs.setShowshare(jsonObject.optString("showshare"));
                fs.setLikenumber(jsonObject.optString("likenumber"));
                fs.setReplynumber(jsonObject.optString("replynumber"));
                fs.setSharenumber(jsonObject.optString("sharenumber"));
                fs.setOperatebutton(jsonObject.optString("operatebutton"));
                fs.setMenu_delete(jsonObject.optString("menu_delete"));
                fs.setMenu_takeback(jsonObject.optString("menu_takeback"));
                fs.setMenu_top(jsonObject.optString("menu_top"));
                //解析评论的内容
                JSONArray ja=jsonObject.optJSONArray("reply");
                if(ja!=null&&ja.length()!=0) {
                    List<ReplyBean> list_reply=new ArrayList<>();
                    for (int j = 0; j < ja.length(); j++) {
                        JSONObject jo_re = ja.optJSONObject(j);
                        ReplyBean rb = new ReplyBean();
                        rb.setReplyNickname(jo_re.optString("user"));
                        rb.setReply_id(jo_re.optString("userid"));
                        rb.setTouxiang(jo_re.optString("touxiang"));
                        rb.setTime(jo_re.optString("time"));
                        rb.setReplyContent(jo_re.optString("lastcontent"));
                        rb.setSound(jo_re.optString("sound"));
                        rb.setGeopara(jo_re.optString("geopara"));
                        rb.setGeopos(jo_re.optString("geopos"));
                        JSONArray Images=jo_re.optJSONArray("images");
                        if(Images.length()!=0) {
                            String[] images = new String[Images.length()];
                            for (int k = 0; k < Images.length(); k++) {
                                images[k] = Images.optString(k);
                            }
                            rb.setImages(images);
                        }
                        JSONArray jsattachments2=jo_re.optJSONArray("attachments");
                        if(jsattachments2!=null&&jsattachments2.length()!=0) {
                            String[] attachments = new String[jsattachments2.length()];
                            for (int k = 0; k < jsattachments2.length(); k++) {
                                attachments[k] = jsattachments2.optString(k);
                            }
                            rb.setAttachments(attachments);
                        }
                        list_reply.add(rb);
                    }
                    fs.setReplyBean(list_reply);
                }
                //解析点赞的人
                JSONArray ja_like=jsonObject.optJSONArray("like");
                if(ja_like!=null&&ja_like.length()!=0){
                    List<LikeBean> list_like=new ArrayList<>();
                    for (int k = 0; k < ja_like.length(); k++) {
                        JSONObject jb_like=ja_like.getJSONObject(k);
                        LikeBean likeBean=new LikeBean();
                        LogUtils.i(jb_like.optString("userid"));
                        likeBean.setUserId(jb_like.optString("userid"));
                        likeBean.setUserName(jb_like.optString("user"));
                        list_like.add(likeBean);
                    }
                    fs.setLikeBean(list_like);
                }
                arrRecom.add(fs);
            }
            LogUtils.i("arrRecom:"+arrRecom.size());

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return arrRecom;
    }
    public static Result getResult(String josn){
        Result result=new Result();
        try {
            JSONObject jsonObjectSJ=new JSONObject(josn);
            result.setMessage(jsonObjectSJ.optString("message"));
            result.setResult(jsonObjectSJ.optString("result"));
            result.setBodyvalue(jsonObjectSJ.optString("bodyvalue"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return result;
    }
    public static String ACCESS_TOKEN="access_token";
    public static String REFRESH_TOKEN="refresh_toke";
    public static String OPENID="openid";

    /**
     * 获取微信的接口凭证
     * @param josn
     * @return
     */
    public static Map<String,String> getAccectToken(String josn){
        Map<String,String> map=new HashMap<>();
        try {
            JSONObject jsonObject=new JSONObject(josn);
            String accectToken=jsonObject.optString("access_token");
            String openId=jsonObject.optString("openid");
            String refresh_token=jsonObject.optString("refresh_token");
            map.put(ACCESS_TOKEN,accectToken);
            map.put(OPENID,openId);
            map.put(REFRESH_TOKEN,refresh_token);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return map;
    }
}
