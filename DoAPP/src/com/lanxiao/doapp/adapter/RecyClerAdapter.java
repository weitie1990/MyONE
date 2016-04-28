package com.lanxiao.doapp.adapter;

import android.app.Activity;
import android.app.ActivityOptions;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.easemob.chat.EMChatManager;
import com.easemob.easeui.Conversion;
import com.easemob.easeui.utils.EaseSmileUtils;
import com.example.doapp.R;
import com.lanxiao.doapp.activity.ChatActivity;
import com.lanxiao.doapp.activity.ImagePagerActivity;
import com.lanxiao.doapp.activity.LocatContentActivity;
import com.lanxiao.doapp.activity.MeetingWebActivity;
import com.lanxiao.doapp.activity.MyBaiduMapActivity;
import com.lanxiao.doapp.activity.VoiceCallActivity;
import com.lanxiao.doapp.activity.WebActivity;
import com.lanxiao.doapp.chatui.applib.controller.DemoHelper;
import com.lanxiao.doapp.entity.FriendStateItem;
import com.lanxiao.doapp.entity.LikeBean;
import com.lanxiao.doapp.entity.LinkContent;
import com.lanxiao.doapp.entity.ReplyBean;
import com.lanxiao.doapp.http.Api;
import com.lanxiao.doapp.http.Json;
import com.lanxiao.doapp.myView.ActionItem;
import com.lanxiao.doapp.myView.NineGridImageView;
import com.lanxiao.doapp.myView.NineGridImageViewAdapter;
import com.lanxiao.doapp.myView.PopupList;
import com.lanxiao.doapp.myView.RoundedCornersImage;
import com.lanxiao.doapp.myView.TitlePopup;
import com.lanxiao.doapp.untils.DateUntils;
import com.lanxiao.doapp.untils.util.FileUtils;
import com.lanxiao.doapp.untils.util.Utils;
import com.lanxiao.doapp.widget.VoicePlayClickListener;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.HttpHandler;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lidroid.xutils.util.LogUtils;
import com.squareup.picasso.Picasso;
import com.zhy.autolayout.utils.AutoUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by Thinkpad on 2016/4/13.
 */
public class RecyClerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements View.OnClickListener {
    Object tag;
    ArrayList<FriendStateItem> list;
    private Context context;
    private MonClickListener monClickListener;

    public RecyClerAdapter(Context context, ArrayList<FriendStateItem> list,Object tag) {
        this.context = context;
        this.list = list;
        monClickListener = new MonClickListener();
        this.tag=tag;

    }

    public void setData(ArrayList<FriendStateItem> list) {
        if (list != null) {
            LogUtils.i(list.size() + "");
            this.list = list;
            notifyDataSetChanged();
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        // 给ViewHolder设置布局文件
        if (i == NOIML) {
            View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.listitemforattention, viewGroup, false);
            v.setOnClickListener(this);
            return new MyViewHolderForDeaufult(v);
        }
        // 给ViewHolder设置布局文件
        else if (i == LINK) {
            View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.listitemforattention_forbmob, viewGroup, false);
            v.setOnClickListener(this);
            return new MyViewHolderForBmob(v);
        } else if (i == REPLY) {
            View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.listitemforattention_forsearch, viewGroup, false);
            v.setOnClickListener(this);
            return new MyViewHolderforBean(v);
        } else {
            View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.listitemforattentionforall, viewGroup, false);
            v.setOnClickListener(this);
            return new MyViewHolder(v);
        }
    }

    public static final int NOIML = 0;
    public static final int LINK = 1;
    public static final int REPLY = 2;
    public static final int ALL = 3;

    @Override
    public int getItemViewType(int position) {
        FriendStateItem fs = list.get(position);
        List<LinkContent> linkContents = fs.getLinkContent();
        List<ReplyBean> replyBeans = fs.getReplyBean();
        if (linkContents == null && replyBeans == null) {
            return NOIML;
        }
        if (linkContents != null && replyBeans == null) {
            return LINK;
        }
        if (linkContents == null && replyBeans != null) {
            return REPLY;
        }
        return ALL;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int mPosition) {
        FriendStateItem fs = list.get(mPosition);
        String layOutType = fs.getType();
        List<LinkContent> list_link = fs.getLinkContent();
        List<LikeBean> likeBeanList = fs.getLikeBean();
        List<ReplyBean> replyBeanList = fs.getReplyBean();
        String html = fs.getContent();
        //将数据保存在itemView的Tag中，以便点击时进行获取
        //包含所有布局的
        if (holder instanceof MyViewHolder) {

            ((MyViewHolder) holder).root_comment.setTag(mPosition);
            ((MyViewHolder) holder).btnSearchBtnGo.setVisibility(View.GONE);
            ((MyViewHolder) holder).rl_layout_file.setVisibility(View.GONE);
            ((MyViewHolder) holder).llLayoutVoice.setVisibility(View.GONE);
            ((MyViewHolder) holder).tvEndtime.setVisibility(View.GONE);
            ((MyViewHolder) holder).infoname.setVisibility(View.GONE);
            ((MyViewHolder) holder).tvLookAlldeaita.setVisibility(View.GONE);
            ((MyViewHolder) holder).llAttentioniconBackLoationContent.setVisibility(View.GONE);
            ((MyViewHolder) holder).rlBackforapply.setVisibility(View.GONE);
            ((MyViewHolder) holder).rlReplyforapply.setVisibility(View.GONE);
            ((MyViewHolder) holder).rlAttentionitemSmile.setVisibility(View.GONE);
            ((MyViewHolder) holder).btnSearchBtnGo.setVisibility(View.GONE);
            ((MyViewHolder) holder).llTaskHeButton.setVisibility(View.GONE);
            ((MyViewHolder) holder).llBmobImages.setVisibility(View.GONE);
            ((MyViewHolder) holder).llPostImages.setVisibility(View.GONE);

            //大体布局的几种类型
            //会议类型
            if (layOutType.equals(FriendStateItem.MEETING)) {
                ((MyViewHolder) holder).iv_attentionitem_rightbtn.setImageResource(R.drawable.righttop3);

            } else if
                //任务类型
                    (layOutType.equals(FriendStateItem.TASK)) {
                ((MyViewHolder) holder).iv_attentionitem_rightbtn.setImageResource(R.drawable.righttop1);

                //复杂审批类型
            } else if (layOutType.equals(FriendStateItem.ADAPPROVE)) {
                ((MyViewHolder) holder).iv_attentionitem_rightbtn.setImageResource(R.drawable.righttop2);

            }
            //审批类型
            else if (layOutType.equals(FriendStateItem.APPROVE)) {
                ((MyViewHolder) holder).iv_attentionitem_rightbtn.setImageResource(R.drawable.righttop2);
                //微博类型
            } else if (layOutType.equals(FriendStateItem.BLOG)) {
                ((MyViewHolder) holder).iv_attentionitem_rightbtn.setImageResource(R.drawable.righttop4);
            }
            ((MyViewHolder) holder).iv_attentionitem_rightbtn.setTag(R.id.tag_rightbtn, mPosition);
            ((MyViewHolder) holder).iv_attentionitem_rightbtn.setOnClickListener(monClickListener);
            //有链接内容
            LinkContent linkContent = list_link.get(0);
            ((MyViewHolder) holder).rlBmobHeMain.setVisibility(View.VISIBLE);
            ((MyViewHolder) holder).tvBmobDeaita.setTag(R.id.tag_tvBmobDeaita, mPosition);
            ((MyViewHolder) holder).tvBmobDeaita.setOnClickListener(monClickListener);
            ((MyViewHolder) holder).rlBmobHeMain.setTag(R.id.tag_rlBmobHeMain, mPosition);
            ((MyViewHolder) holder).rlBmobHeMain.setOnClickListener(monClickListener);
            String content = linkContent.getMaincontent();
            String name = linkContent.getUser();
            content = "转发自" + name + ": " + content;
            //设置带表情的内容
            // 设置内容
            ((MyViewHolder) holder).tvBmobContent.setMovementMethod(LinkMovementMethod.getInstance());
            ((MyViewHolder) holder).tvBmobContent.setText(addClickablePart(content, linkContent.getUserId(), name.length(), context), TextView.BufferType.SPANNABLE);
            //转发里面的图片
            if (!linkContent.getImages()[0].equals("http://www.dosns.net/doimg/")) {
                ((MyViewHolder) holder).llBmobImages.setVisibility(View.VISIBLE);
                final String[] images = linkContent.getImages();
                LogUtils.i(images.length + "");
                final ArrayList<String> datas = new ArrayList<String>();
                for (String imageurl : images) {
                    datas.add(imageurl);
                }
                LogUtils.i("bind2");
                        ((MyViewHolder) holder).bind2(datas);

            }
            showListPopulist(((MyViewHolder) holder).ivSearchIcon, mPosition, 2);
            Utils.setAver(replyBeanList.get(0).getTouxiang(), ((MyViewHolder) holder).ivSearchIcon);
            Spannable span = EaseSmileUtils.getSmiledText(context, replyBeanList.get(0).getReplyContent());
            // 设置内容
            ((MyViewHolder) holder).tvSearchInfo.setText(span, TextView.BufferType.SPANNABLE);
            ((MyViewHolder) holder).tvSearchData.setText(replyBeanList.get(0).getTime());


            ((MyViewHolder) holder).rlSearchMeArgeeinfo.setVisibility(View.GONE);
            Utils.setAver(replyBeanList.get(0).getTouxiang(), ((MyViewHolder) holder).ivSearchIcon);
            ((MyViewHolder) holder).ivSearchIcon.setTag(R.id.tag_ivSearchIcon, replyBeanList.get(0).getReply_id());
            ((MyViewHolder) holder).ivSearchIcon.setOnClickListener(monClickListener);
            Spannable span2 = EaseSmileUtils.getSmiledText(context, replyBeanList.get(0).getReplyContent());
            // 设置内容
            ((MyViewHolder) holder).tvSearchInfo.setText(span2, TextView.BufferType.SPANNABLE);
            ((MyViewHolder) holder).tvSearchData.setText(replyBeanList.get(0).getTime());
            if (likeBeanList != null) {
                ((MyViewHolder) holder).rlSearchMeArgeeinfo.setVisibility(View.VISIBLE);
                StringBuffer sb1 = new StringBuffer();
                for (int i = 0; i < likeBeanList.size(); i++) {
                    sb1.append(likeBeanList.get(i).getUserName() + " ");
                }
                LogUtils.i("bind1");
                ((MyViewHolder) holder).tvSearchMeArgeeinfo.setText("点赞：" + sb1);
            }


            //有附件时
            if (!list.get(mPosition).getAttachments()[0].equals("http://www.dosns.net/doattachment/")) {
                ((MyViewHolder) holder).rl_layout_file.setVisibility(View.VISIBLE);
                ((MyViewHolder) holder).tv_file_who.setText(fs.getName() + "提交了附件，请点击查看");
            }
            //有语音时
            if (!list.get(mPosition).getVideoUri().equals("")) {
                final int pos = mPosition;
                ((MyViewHolder) holder).llLayoutVoice.setVisibility(View.VISIBLE);
                ((MyViewHolder) holder).tvVoiceTime.setText("＂");
                ((MyViewHolder) holder).llLayoutVoice.setTag(R.id.tag_llLayoutVoiceforid, mPosition);
                ((MyViewHolder) holder).llLayoutVoice.setTag(R.id.tag_llLayoutVoice, (MyViewHolder) holder);
                ((MyViewHolder) holder).llLayoutVoice.setOnClickListener(monClickListener);
                ((MyViewHolder) holder).llLayoutVoice.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        showDioalg(pos);
                        return false;
                    }
                });
            }
            if (!list.get(mPosition).getEndTime().equals("")) {
                ((MyViewHolder) holder).tvEndtime.setVisibility(View.VISIBLE);
                ((MyViewHolder) holder).tvEndtime.setText(list.get(mPosition).getEndTime());
                ((MyViewHolder) holder).tvEndtime.setTag(R.id.tag_tvEndtime, mPosition);
                ((MyViewHolder) holder).tvEndtime.setOnClickListener(monClickListener);
            }

            //有图片
            if (!list.get(mPosition).getImages()[0].equals("http://www.dosns.net/doimg/")) {
                ((MyViewHolder) holder).llPostImages.setVisibility(View.VISIBLE);
                final String[] images = list.get(mPosition).getImages();
                LogUtils.i(images.length + "");
                final ArrayList<String> datas = new ArrayList<String>();
                for (String imageurl : images) {
                    datas.add(imageurl);
                }
                ((MyViewHolder) holder).bind(datas);


            }
            //有定位
            if (!TextUtils.isEmpty(fs.getLocationName())) {
                ((MyViewHolder) holder).llAttentioniconBackLoationContent.setVisibility(View.VISIBLE);
                ((MyViewHolder) holder).newsfeedpublishPoiPlace.setText(fs.getLocationName());
                ((MyViewHolder) holder).llAttentioniconBackLoationContent.setTag(R.id.tag_LoationContent, mPosition);
                ((MyViewHolder) holder).llAttentioniconBackLoationContent.setOnClickListener(monClickListener);
            }

            //处理的是底部栏
            if (fs.getShowlike().equals("1")) {
                ((MyViewHolder) holder).rlBackforapply.setVisibility(View.VISIBLE);
                ((MyViewHolder) holder).ivBackforapplySum.setText(fs.getLikenumber());
            }
            if (fs.getShowreply().equals("1")) {
                ((MyViewHolder) holder).rlReplyforapply.setVisibility(View.VISIBLE);
                ((MyViewHolder) holder).ivReplyforapplySum.setText(fs.getReplynumber());
                ((MyViewHolder) holder).rlReplyforapply.setTag(R.id.tag_rlReplyforapply, mPosition);
                ((MyViewHolder) holder).rlReplyforapply.setOnClickListener(monClickListener);
            }
            if (fs.getShowshare().equals("1")) {
                ((MyViewHolder) holder).rlAttentionitemSmile.setVisibility(View.VISIBLE);
                ((MyViewHolder) holder).ivAttentionitemSmileSum.setText(fs.getSharenumber());
                ((MyViewHolder) holder).rlAttentionitemSmile.setTag(R.id.tag_rlAttentionitemSmile, mPosition);
                ((MyViewHolder) holder).rlAttentionitemSmile.setOnClickListener(monClickListener);
            }
            //处理底部操作按钮

            if (fs.getOperatebutton().equals("1")) {
                ((MyViewHolder) holder).llTaskHeButton.setVisibility(View.VISIBLE);
                ((MyViewHolder) holder).tvTaskBtninfo.setText("接受：");
            } else if (fs.getOperatebutton().equals("2")) {
                ((MyViewHolder) holder).llTaskHeButton.setVisibility(View.VISIBLE);
                ((MyViewHolder) holder).tvTaskBtninfo.setText("处理完毕：");
            } else if (fs.getOperatebutton().equals("3")) {
                ((MyViewHolder) holder).llTaskHeButton.setVisibility(View.VISIBLE);
                ((MyViewHolder) holder).tvTaskBtninfo.setText("结束：");
            } else if (fs.getOperatebutton().equals("4")) {
                ((MyViewHolder) holder).llTaskHeButton.setVisibility(View.VISIBLE);
                ((MyViewHolder) holder).tvTaskBtninfo.setText("同意：");
            } else if (fs.getOperatebutton().equals("5")) {
                ((MyViewHolder) holder).btnSearchBtnGo.setVisibility(View.VISIBLE);
                ((MyViewHolder) holder).btnSearchBtnGo.setTag(R.id.tag_btnSearchBtnGo, mPosition);
                ((MyViewHolder) holder).btnSearchBtnGo.setOnClickListener(monClickListener);
            } else if (fs.getOperatebutton().equals("6")) {
                ((MyViewHolder) holder).llTaskHeButton.setVisibility(View.VISIBLE);
                ((MyViewHolder) holder).tvTaskBtninfo.setText("参加：");
            } else {
                ((MyViewHolder) holder).tvTaskBtninfo.setText("");
            }
            ((MyViewHolder) holder).btnTaskBtnYes.setTag(R.id.tag_btnTaskBtnYes, mPosition);
            ((MyViewHolder) holder).btnTaskBtnNo.setTag(R.id.tag_btnTaskBtnNo, mPosition);
            ((MyViewHolder) holder).rlBackforapply.setTag(R.id.tag_rlBackforapply, mPosition);
            ((MyViewHolder) holder).btnTaskBtnYes.setOnClickListener(monClickListener);
            ((MyViewHolder) holder).btnTaskBtnNo.setOnClickListener(monClickListener);
            ((MyViewHolder) holder).rlBackforapply.setOnClickListener(monClickListener);

            if (!TextUtils.isEmpty(html)) {
                ((MyViewHolder) holder).infoname.setVisibility(View.VISIBLE);
                ((MyViewHolder) holder).infoname.setMaxLines(3);
                if (html.length() > 180 || Utils.getKeybackSum(html) > 3) {
                    ((MyViewHolder) holder).tvLookAlldeaita.setVisibility(View.VISIBLE);
                    ((MyViewHolder) holder).tvLookAlldeaita.setTag(R.id.tag_tvLookAlldeaita, (MyViewHolder) holder);
                    ((MyViewHolder) holder).tvLookAlldeaita.setOnClickListener(monClickListener);
                }
                //设置带表情的内容
                Spannable span1 = EaseSmileUtils.getSmiledText(context, html);
                // 设置内容
                ((MyViewHolder) holder).infoname.setText(span1, TextView.BufferType.SPANNABLE);
                //holder.infoname.setText(html);
            }
            showListPopulist(((MyViewHolder) holder).ivIconforattention, mPosition, 1);
            Utils.setAver(list.get(mPosition).getUserIcon(), ((MyViewHolder) holder).ivIconforattention);
            ((MyViewHolder) holder).tvAppfordate.setText(list.get(mPosition).getDate());
            ((MyViewHolder) holder).tvAttentionforname.setText(list.get(mPosition).getName());
        }


        //只有回复时
        else if (holder instanceof MyViewHolderforBean) {
            ((MyViewHolderforBean) holder).root_comment.setTag(mPosition);
            ((MyViewHolderforBean) holder).btnSearchBtnGo.setVisibility(View.GONE);
            ((MyViewHolderforBean) holder).rl_layout_file.setVisibility(View.GONE);
            ((MyViewHolderforBean) holder).llLayoutVoice.setVisibility(View.GONE);
            ((MyViewHolderforBean) holder).tvEndtime.setVisibility(View.GONE);
            ((MyViewHolderforBean) holder).infoname.setVisibility(View.GONE);
            ((MyViewHolderforBean) holder).tvLookAlldeaita.setVisibility(View.GONE);
            ((MyViewHolderforBean) holder).llAttentioniconBackLoationContent.setVisibility(View.GONE);
            ((MyViewHolderforBean) holder).rlBackforapply.setVisibility(View.GONE);
            ((MyViewHolderforBean) holder).rlReplyforapply.setVisibility(View.GONE);
            ((MyViewHolderforBean) holder).rlAttentionitemSmile.setVisibility(View.GONE);
            ((MyViewHolderforBean) holder).btnSearchBtnGo.setVisibility(View.GONE);
            ((MyViewHolderforBean) holder).llTaskHeButton.setVisibility(View.GONE);
            ((MyViewHolderforBean) holder).llPostImages.setVisibility(View.GONE);
            //大体布局的几种类型
            //会议类型
            if (layOutType.equals(FriendStateItem.MEETING)) {
                ((MyViewHolderforBean) holder).iv_attentionitem_rightbtn.setImageResource(R.drawable.righttop3);

            } else if
                //任务类型
                    (layOutType.equals(FriendStateItem.TASK)) {
                ((MyViewHolderforBean) holder).iv_attentionitem_rightbtn.setImageResource(R.drawable.righttop1);

                //复杂审批类型
            } else if (layOutType.equals(FriendStateItem.ADAPPROVE)) {
                ((MyViewHolderforBean) holder).iv_attentionitem_rightbtn.setImageResource(R.drawable.righttop2);

            }
            //审批类型
            else if (layOutType.equals(FriendStateItem.APPROVE)) {
                ((MyViewHolderforBean) holder).iv_attentionitem_rightbtn.setImageResource(R.drawable.righttop2);
                //微博类型
            } else if (layOutType.equals(FriendStateItem.BLOG)) {
                ((MyViewHolderforBean) holder).iv_attentionitem_rightbtn.setImageResource(R.drawable.righttop4);
            }
            ((MyViewHolderforBean) holder).iv_attentionitem_rightbtn.setTag(R.id.tag_rightbtn, mPosition);
            ((MyViewHolderforBean) holder).iv_attentionitem_rightbtn.setOnClickListener(monClickListener);
            Utils.setAver(replyBeanList.get(0).getTouxiang(), ((MyViewHolderforBean) holder).ivSearchIcon);
            ((MyViewHolderforBean) holder).ivSearchIcon.setTag(R.id.tag_ivSearchIcon, replyBeanList.get(0).getReplyAccount());
            ((MyViewHolderforBean) holder).ivSearchIcon.setOnClickListener(monClickListener);
            Spannable span = EaseSmileUtils.getSmiledText(context, replyBeanList.get(0).getReplyContent());
            // 设置内容
            ((MyViewHolderforBean) holder).tvSearchInfo.setText(span, TextView.BufferType.SPANNABLE);
            ((MyViewHolderforBean) holder).tvSearchData.setText(replyBeanList.get(0).getTime());


            //无链接只有回复
            ((MyViewHolderforBean) holder).rlSearchMeArgeeinfo.setVisibility(View.GONE);
            Utils.setAver(replyBeanList.get(0).getTouxiang(), ((MyViewHolderforBean) holder).ivSearchIcon);
            showListPopulist(((MyViewHolderforBean) holder).ivSearchIcon, mPosition, 2);
            Spannable span2 = EaseSmileUtils.getSmiledText(context, replyBeanList.get(0).getReplyContent());
            // 设置内容
            ((MyViewHolderforBean) holder).tvSearchInfo.setText(span2, TextView.BufferType.SPANNABLE);
            ((MyViewHolderforBean) holder).tvSearchData.setText(replyBeanList.get(0).getTime());
            if (likeBeanList != null) {
                ((MyViewHolderforBean) holder).rlSearchMeArgeeinfo.setVisibility(View.VISIBLE);
                StringBuffer sb1 = new StringBuffer();
                for (int i = 0; i < likeBeanList.size(); i++) {
                    sb1.append(likeBeanList.get(i).getUserName() + " ");
                }
                ((MyViewHolderforBean) holder).tvSearchMeArgeeinfo.setText("点赞：" + sb1.toString());
            } else {
                ((MyViewHolderforBean) holder).rlSearchMeArgeeinfo.setVisibility(View.GONE);
            }

            //有附件时
            if (!list.get(mPosition).getAttachments()[0].equals("http://www.dosns.net/doattachment/")) {
                ((MyViewHolderforBean) holder).rl_layout_file.setVisibility(View.VISIBLE);
                ((MyViewHolderforBean) holder).tv_file_who.setText(fs.getName() + "提交了附件，请点击查看");
            }
            //有语音时
            if (!list.get(mPosition).getVideoUri().equals("")) {
                final int pos = mPosition;
                ((MyViewHolderforBean) holder).llLayoutVoice.setVisibility(View.VISIBLE);
                ((MyViewHolderforBean) holder).tvVoiceTime.setText("＂");
                ((MyViewHolderforBean) holder).llLayoutVoice.setTag(R.id.tag_llLayoutVoiceforid, mPosition);
                ((MyViewHolderforBean) holder).llLayoutVoice.setTag(R.id.tag_llLayoutVoice, (MyViewHolderforBean) holder);
                ((MyViewHolderforBean) holder).llLayoutVoice.setOnClickListener(monClickListener);
                ((MyViewHolderforBean) holder).llLayoutVoice.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        showDioalg(pos);
                        return false;
                    }
                });
            }
            if (!list.get(mPosition).getEndTime().equals("")) {
                ((MyViewHolderforBean) holder).tvEndtime.setVisibility(View.VISIBLE);
                ((MyViewHolderforBean) holder).tvEndtime.setText(list.get(mPosition).getEndTime());
                ((MyViewHolderforBean) holder).tvEndtime.setTag(R.id.tag_tvEndtime, mPosition);
                ((MyViewHolderforBean) holder).tvEndtime.setOnClickListener(monClickListener);
            }

            //有图片
            if (!list.get(mPosition).getImages()[0].equals("http://www.dosns.net/doimg/")) {
                ((MyViewHolderforBean) holder).llPostImages.setVisibility(View.VISIBLE);
                final String[] images = list.get(mPosition).getImages();
                final ArrayList<String> datas = new ArrayList<String>();
                for (String imageurl : images) {
                    datas.add(imageurl);
                    LogUtils.i("imageurl:" + imageurl);
                }
                ((MyViewHolderforBean) holder).bind(datas);


            }
            //有定位
            if (!TextUtils.isEmpty(fs.getLocationName())) {
                ((MyViewHolderforBean) holder).llAttentioniconBackLoationContent.setVisibility(View.VISIBLE);
                ((MyViewHolderforBean) holder).newsfeedpublishPoiPlace.setText(fs.getLocationName());
                ((MyViewHolderforBean) holder).llAttentioniconBackLoationContent.setTag(R.id.tag_LoationContent, mPosition);
                ((MyViewHolderforBean) holder).llAttentioniconBackLoationContent.setOnClickListener(monClickListener);
            }
            //处理的是底部栏
            if (fs.getShowlike().equals("1")) {
                ((MyViewHolderforBean) holder).rlBackforapply.setVisibility(View.VISIBLE);
                ((MyViewHolderforBean) holder).ivBackforapplySum.setText(fs.getLikenumber());
            }
            if (fs.getShowreply().equals("1")) {
                ((MyViewHolderforBean) holder).rlReplyforapply.setVisibility(View.VISIBLE);
                ((MyViewHolderforBean) holder).ivReplyforapplySum.setText(fs.getReplynumber());
                ((MyViewHolderforBean) holder).rlReplyforapply.setTag(R.id.tag_rlReplyforapply, mPosition);
                ((MyViewHolderforBean) holder).rlReplyforapply.setOnClickListener(monClickListener);
            }
            if (fs.getShowshare().equals("1")) {
                ((MyViewHolderforBean) holder).rlAttentionitemSmile.setVisibility(View.VISIBLE);
                ((MyViewHolderforBean) holder).ivAttentionitemSmileSum.setText(fs.getSharenumber());
                ((MyViewHolderforBean) holder).rlAttentionitemSmile.setTag(R.id.tag_rlAttentionitemSmile, mPosition);
                ((MyViewHolderforBean) holder).rlAttentionitemSmile.setOnClickListener(monClickListener);
            }
            //处理底部操作按钮

            if (fs.getOperatebutton().equals("1")) {
                ((MyViewHolderforBean) holder).btnSearchBtnGo.setVisibility(View.GONE);
                ((MyViewHolderforBean) holder).llTaskHeButton.setVisibility(View.VISIBLE);
                ((MyViewHolderforBean) holder).tvTaskBtninfo.setText("接受：");
            } else if (fs.getOperatebutton().equals("2")) {
                ((MyViewHolderforBean) holder).btnSearchBtnGo.setVisibility(View.GONE);
                ((MyViewHolderforBean) holder).llTaskHeButton.setVisibility(View.VISIBLE);
                ((MyViewHolderforBean) holder).tvTaskBtninfo.setText("处理完毕：");
            } else if (fs.getOperatebutton().equals("3")) {
                ((MyViewHolderforBean) holder).btnSearchBtnGo.setVisibility(View.GONE);
                ((MyViewHolderforBean) holder).llTaskHeButton.setVisibility(View.VISIBLE);
                ((MyViewHolderforBean) holder).tvTaskBtninfo.setText("结束：");
            } else if (fs.getOperatebutton().equals("4")) {
                ((MyViewHolderforBean) holder).btnSearchBtnGo.setVisibility(View.GONE);
                ((MyViewHolderforBean) holder).llTaskHeButton.setVisibility(View.VISIBLE);
                ((MyViewHolderforBean) holder).tvTaskBtninfo.setText("同意：");
            } else if (fs.getOperatebutton().equals("5")) {
                ((MyViewHolderforBean) holder).llTaskHeButton.setVisibility(View.GONE);
                ((MyViewHolderforBean) holder).btnSearchBtnGo.setVisibility(View.VISIBLE);
                ((MyViewHolderforBean) holder).btnSearchBtnGo.setTag(R.id.tag_btnSearchBtnGo, mPosition);
                ((MyViewHolderforBean) holder).btnSearchBtnGo.setOnClickListener(monClickListener);
            } else if (fs.getOperatebutton().equals("6")) {
                ((MyViewHolderforBean) holder).btnSearchBtnGo.setVisibility(View.GONE);
                ((MyViewHolderforBean) holder).llTaskHeButton.setVisibility(View.VISIBLE);
                ((MyViewHolderforBean) holder).tvTaskBtninfo.setText("参加：");
            } else {
                ((MyViewHolderforBean) holder).tvTaskBtninfo.setText("");
                ((MyViewHolderforBean) holder).btnSearchBtnGo.setVisibility(View.GONE);
                ((MyViewHolderforBean) holder).llTaskHeButton.setVisibility(View.GONE);
            }
            ((MyViewHolderforBean) holder).btnTaskBtnYes.setTag(R.id.tag_btnTaskBtnYes, mPosition);
            ((MyViewHolderforBean) holder).btnTaskBtnNo.setTag(R.id.tag_btnTaskBtnNo, mPosition);
            ((MyViewHolderforBean) holder).rlBackforapply.setTag(R.id.tag_rlBackforapply, mPosition);
            ((MyViewHolderforBean) holder).btnTaskBtnYes.setOnClickListener(monClickListener);
            ((MyViewHolderforBean) holder).btnTaskBtnNo.setOnClickListener(monClickListener);
            ((MyViewHolderforBean) holder).rlBackforapply.setOnClickListener(monClickListener);

            if (!TextUtils.isEmpty(html)) {
                ((MyViewHolderforBean) holder).infoname.setVisibility(View.VISIBLE);
                ((MyViewHolderforBean) holder).infoname.setMaxLines(3);
                if (html.length() > 180) {
                    ((MyViewHolderforBean) holder).tvLookAlldeaita.setVisibility(View.VISIBLE);
                    ((MyViewHolderforBean) holder).tvLookAlldeaita.setTag(R.id.tag_tvLookAlldeaita, (MyViewHolderforBean) holder);
                    ((MyViewHolderforBean) holder).tvLookAlldeaita.setOnClickListener(monClickListener);
                }
                //设置带表情的内容
                Spannable span1 = EaseSmileUtils.getSmiledText(context, html);
                // 设置内容
                ((MyViewHolderforBean) holder).infoname.setText(span1, TextView.BufferType.SPANNABLE);
            }
            showListPopulist(((MyViewHolderforBean) holder).ivIconforattention, mPosition, 1);
            Utils.setAver(list.get(mPosition).getUserIcon(), ((MyViewHolderforBean) holder).ivIconforattention);
            ((MyViewHolderforBean) holder).tvAppfordate.setText(list.get(mPosition).getDate());
            ((MyViewHolderforBean) holder).tvAttentionforname.setText(list.get(mPosition).getName());
        }


        //有转发的
        else if (holder instanceof MyViewHolderForBmob) {
            ((MyViewHolderForBmob) holder).root_comment.setTag(mPosition);
            ((MyViewHolderForBmob) holder).btnSearchBtnGo.setVisibility(View.GONE);
            ((MyViewHolderForBmob) holder).rl_layout_file.setVisibility(View.GONE);
            ((MyViewHolderForBmob) holder).llLayoutVoice.setVisibility(View.GONE);
            ((MyViewHolderForBmob) holder).tvEndtime.setVisibility(View.GONE);
            ((MyViewHolderForBmob) holder).infoname.setVisibility(View.GONE);
            ((MyViewHolderForBmob) holder).tvLookAlldeaita.setVisibility(View.GONE);
            ((MyViewHolderForBmob) holder).llAttentioniconBackLoationContent.setVisibility(View.GONE);
            ((MyViewHolderForBmob) holder).rlBackforapply.setVisibility(View.GONE);
            ((MyViewHolderForBmob) holder).rlReplyforapply.setVisibility(View.GONE);
            ((MyViewHolderForBmob) holder).rlAttentionitemSmile.setVisibility(View.GONE);
            ((MyViewHolderForBmob) holder).btnSearchBtnGo.setVisibility(View.GONE);
            ((MyViewHolderForBmob) holder).llTaskHeButton.setVisibility(View.GONE);
            ((MyViewHolderForBmob) holder).llPostImages.setVisibility(View.GONE);
            ((MyViewHolderForBmob) holder).llBmobImages.setVisibility(View.GONE);
            //大体布局的几种类型
            //会议类型
            if (layOutType.equals(FriendStateItem.MEETING)) {
                ((MyViewHolderForBmob) holder).iv_attentionitem_rightbtn.setImageResource(R.drawable.righttop3);

            } else if
                //任务类型
                    (layOutType.equals(FriendStateItem.TASK)) {
                ((MyViewHolderForBmob) holder).iv_attentionitem_rightbtn.setImageResource(R.drawable.righttop1);

                //复杂审批类型
            } else if (layOutType.equals(FriendStateItem.ADAPPROVE)) {
                ((MyViewHolderForBmob) holder).iv_attentionitem_rightbtn.setImageResource(R.drawable.righttop2);

            }
            //审批类型
            else if (layOutType.equals(FriendStateItem.APPROVE)) {
                ((MyViewHolderForBmob) holder).iv_attentionitem_rightbtn.setImageResource(R.drawable.righttop2);
                //微博类型
            } else if (layOutType.equals(FriendStateItem.BLOG)) {
                ((MyViewHolderForBmob) holder).iv_attentionitem_rightbtn.setImageResource(R.drawable.righttop4);
            }
            ((MyViewHolderForBmob) holder).iv_attentionitem_rightbtn.setTag(R.id.tag_rightbtn, mPosition);
            ((MyViewHolderForBmob) holder).iv_attentionitem_rightbtn.setOnClickListener(monClickListener);
            //有链接内容
            LinkContent linkContent = list_link.get(0);
            ((MyViewHolderForBmob) holder).tvBmobDeaita.setTag(R.id.tag_tvBmobDeaita, mPosition);
            ((MyViewHolderForBmob) holder).tvBmobDeaita.setOnClickListener(monClickListener);
            ((MyViewHolderForBmob) holder).rlBmobHeMain.setTag(R.id.tag_rlBmobHeMain, mPosition);
            ((MyViewHolderForBmob) holder).rlBmobHeMain.setOnClickListener(monClickListener);
            String content = linkContent.getMaincontent();
            String name = linkContent.getUser();
            content = "转发自" + name + ": " + content;
            //设置带表情的内容
            // 设置内容
            ((MyViewHolderForBmob) holder).tvBmobContent.setMovementMethod(LinkMovementMethod.getInstance());
            ((MyViewHolderForBmob) holder).tvBmobContent.setText(addClickablePart(content, linkContent.getUserId(), name.length(), context), TextView.BufferType.SPANNABLE);
            //转发里面的图片
            if (!linkContent.getImages()[0].equals("http://www.dosns.net/doimg/")) {
                ((MyViewHolderForBmob) holder).llBmobImages.setVisibility(View.VISIBLE);
                final String[] images = linkContent.getImages();
                final ArrayList<String> datas = new ArrayList<String>();
                for (String imageurl : images) {
                    datas.add(imageurl);
                }
                ((MyViewHolderForBmob) holder).bind2(datas);

            }


            //有附件时
            if (!list.get(mPosition).getAttachments()[0].equals("http://www.dosns.net/doattachment/")) {
                ((MyViewHolderForBmob) holder).rl_layout_file.setVisibility(View.VISIBLE);
                ((MyViewHolderForBmob) holder).tv_file_who.setText(fs.getName() + "提交了附件，请点击查看");
            }
            //有语音时
            if (!list.get(mPosition).getVideoUri().equals("")) {
                final int pos = mPosition;
                ((MyViewHolderForBmob) holder).llLayoutVoice.setVisibility(View.VISIBLE);
                ((MyViewHolderForBmob) holder).tvVoiceTime.setText("＂");
                ((MyViewHolderForBmob) holder).llLayoutVoice.setTag(R.id.tag_llLayoutVoiceforid, mPosition);
                ((MyViewHolderForBmob) holder).llLayoutVoice.setTag(R.id.tag_llLayoutVoice, (MyViewHolderForBmob) holder);
                ((MyViewHolderForBmob) holder).llLayoutVoice.setOnClickListener(monClickListener);
                ((MyViewHolderForBmob) holder).llLayoutVoice.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        showDioalg(pos);
                        return false;
                    }
                });
            }
            if (!list.get(mPosition).getEndTime().equals("")) {
                ((MyViewHolderForBmob) holder).tvEndtime.setVisibility(View.VISIBLE);
                ((MyViewHolderForBmob) holder).tvEndtime.setText(list.get(mPosition).getEndTime());
                ((MyViewHolderForBmob) holder).tvEndtime.setTag(R.id.tag_tvEndtime, mPosition);
                ((MyViewHolderForBmob) holder).tvEndtime.setOnClickListener(monClickListener);
            }

            //有图片
            if (!list.get(mPosition).getImages()[0].equals("http://www.dosns.net/doimg/")) {
                ((MyViewHolderForBmob) holder).llPostImages.setVisibility(View.VISIBLE);
                final String[] images = list.get(mPosition).getImages();
                LogUtils.i(images.length + "");
                final ArrayList<String> datas = new ArrayList<String>();
                for (String imageurl : images) {
                    datas.add(imageurl);
                }
                ((MyViewHolderForBmob) holder).bind(datas);


            }
            //有定位
            if (!TextUtils.isEmpty(fs.getLocationName())) {
                ((MyViewHolderForBmob) holder).llAttentioniconBackLoationContent.setVisibility(View.VISIBLE);
                ((MyViewHolderForBmob) holder).newsfeedpublishPoiPlace.setText(fs.getLocationName());
                ((MyViewHolderForBmob) holder).llAttentioniconBackLoationContent.setTag(R.id.tag_LoationContent, mPosition);
                ((MyViewHolderForBmob) holder).llAttentioniconBackLoationContent.setOnClickListener(monClickListener);
            }
            ((MyViewHolderForBmob) holder).rlBackforapply.setVisibility(View.GONE);
            ((MyViewHolderForBmob) holder).rlReplyforapply.setVisibility(View.GONE);
            ((MyViewHolderForBmob) holder).rlAttentionitemSmile.setVisibility(View.GONE);
            //处理的是底部栏
            if (fs.getShowlike().equals("1")) {
                ((MyViewHolderForBmob) holder).rlBackforapply.setVisibility(View.VISIBLE);
                ((MyViewHolderForBmob) holder).ivBackforapplySum.setText(fs.getLikenumber());
            }
            if (fs.getShowreply().equals("1")) {
                ((MyViewHolderForBmob) holder).rlReplyforapply.setVisibility(View.VISIBLE);
                ((MyViewHolderForBmob) holder).ivReplyforapplySum.setText(fs.getReplynumber());
                ((MyViewHolderForBmob) holder).rlReplyforapply.setTag(R.id.tag_rlReplyforapply, mPosition);
                ((MyViewHolderForBmob) holder).rlReplyforapply.setOnClickListener(monClickListener);

            }
            if (fs.getShowshare().equals("1")) {
                ((MyViewHolderForBmob) holder).rlAttentionitemSmile.setVisibility(View.VISIBLE);
                ((MyViewHolderForBmob) holder).ivAttentionitemSmileSum.setText(fs.getSharenumber());
                ((MyViewHolderForBmob) holder).rlAttentionitemSmile.setTag(R.id.tag_rlAttentionitemSmile, mPosition);
                ((MyViewHolderForBmob) holder).rlAttentionitemSmile.setOnClickListener(monClickListener);
            }
            //处理底部操作按钮

            if (fs.getOperatebutton().equals("1")) {
                ((MyViewHolderForBmob) holder).btnSearchBtnGo.setVisibility(View.GONE);
                ((MyViewHolderForBmob) holder).llTaskHeButton.setVisibility(View.VISIBLE);
                ((MyViewHolderForBmob) holder).tvTaskBtninfo.setText("接受：");
            } else if (fs.getOperatebutton().equals("2")) {
                ((MyViewHolderForBmob) holder).btnSearchBtnGo.setVisibility(View.GONE);
                ((MyViewHolderForBmob) holder).llTaskHeButton.setVisibility(View.VISIBLE);
                ((MyViewHolderForBmob) holder).tvTaskBtninfo.setText("处理完毕：");
            } else if (fs.getOperatebutton().equals("3")) {
                ((MyViewHolderForBmob) holder).btnSearchBtnGo.setVisibility(View.GONE);
                ((MyViewHolderForBmob) holder).llTaskHeButton.setVisibility(View.VISIBLE);
                ((MyViewHolderForBmob) holder).tvTaskBtninfo.setText("结束：");
            } else if (fs.getOperatebutton().equals("4")) {
                ((MyViewHolderForBmob) holder).btnSearchBtnGo.setVisibility(View.GONE);
                ((MyViewHolderForBmob) holder).llTaskHeButton.setVisibility(View.VISIBLE);
                ((MyViewHolderForBmob) holder).tvTaskBtninfo.setText("同意：");
            } else if (fs.getOperatebutton().equals("5")) {
                ((MyViewHolderForBmob) holder).llTaskHeButton.setVisibility(View.GONE);
                ((MyViewHolderForBmob) holder).btnSearchBtnGo.setVisibility(View.VISIBLE);
                ((MyViewHolderForBmob) holder).btnSearchBtnGo.setTag(R.id.tag_btnSearchBtnGo, mPosition);
                ((MyViewHolderForBmob) holder).btnSearchBtnGo.setOnClickListener(monClickListener);
            } else if (fs.getOperatebutton().equals("6")) {
                ((MyViewHolderForBmob) holder).btnSearchBtnGo.setVisibility(View.GONE);
                ((MyViewHolderForBmob) holder).llTaskHeButton.setVisibility(View.VISIBLE);
                ((MyViewHolderForBmob) holder).tvTaskBtninfo.setText("参加：");
            } else {
                ((MyViewHolderForBmob) holder).tvTaskBtninfo.setText("");
                ((MyViewHolderForBmob) holder).btnSearchBtnGo.setVisibility(View.GONE);
                ((MyViewHolderForBmob) holder).llTaskHeButton.setVisibility(View.GONE);
            }
            ((MyViewHolderForBmob) holder).btnTaskBtnYes.setTag(R.id.tag_btnTaskBtnYes, mPosition);
            ((MyViewHolderForBmob) holder).btnTaskBtnNo.setTag(R.id.tag_btnTaskBtnNo, mPosition);
            ((MyViewHolderForBmob) holder).rlBackforapply.setTag(R.id.tag_rlBackforapply, mPosition);
            ((MyViewHolderForBmob) holder).btnTaskBtnYes.setOnClickListener(monClickListener);
            ((MyViewHolderForBmob) holder).btnTaskBtnNo.setOnClickListener(monClickListener);
            ((MyViewHolderForBmob) holder).rlBackforapply.setOnClickListener(monClickListener);

            if (!TextUtils.isEmpty(html)) {
                ((MyViewHolderForBmob) holder).infoname.setVisibility(View.VISIBLE);
                ((MyViewHolderForBmob) holder).infoname.setMaxLines(3);
                if (html.length() > 180 || Utils.getKeybackSum(html) > 3) {
                    ((MyViewHolderForBmob) holder).tvLookAlldeaita.setVisibility(View.VISIBLE);
                    ((MyViewHolderForBmob) holder).tvLookAlldeaita.setTag(R.id.tag_tvLookAlldeaita, (MyViewHolderForBmob) holder);
                    ((MyViewHolderForBmob) holder).tvLookAlldeaita.setOnClickListener(monClickListener);
                }
                //设置带表情的内容
                Spannable span1 = EaseSmileUtils.getSmiledText(context, html);
                // 设置内容
                ((MyViewHolderForBmob) holder).infoname.setText(span1, TextView.BufferType.SPANNABLE);
                //holder.infoname.setText(html);
            }
            showListPopulist(((MyViewHolderForBmob) holder).ivIconforattention, mPosition, 1);
            Utils.setAver(list.get(mPosition).getUserIcon(), ((MyViewHolderForBmob) holder).ivIconforattention);
            ((MyViewHolderForBmob) holder).tvAppfordate.setText(list.get(mPosition).getDate());
            ((MyViewHolderForBmob) holder).tvAttentionforname.setText(list.get(mPosition).getName());
        }

        //默认的
        else if (holder instanceof MyViewHolderForDeaufult) {
            ((MyViewHolderForDeaufult) holder).root_comment.setTag(mPosition);
            ((MyViewHolderForDeaufult) holder).btnSearchBtnGo.setVisibility(View.GONE);
            ((MyViewHolderForDeaufult) holder).rl_layout_file.setVisibility(View.GONE);
            ((MyViewHolderForDeaufult) holder).llLayoutVoice.setVisibility(View.GONE);
            ((MyViewHolderForDeaufult) holder).tvEndtime.setVisibility(View.GONE);
            ((MyViewHolderForDeaufult) holder).infoname.setVisibility(View.GONE);
            ((MyViewHolderForDeaufult) holder).tvLookAlldeaita.setVisibility(View.GONE);
            ((MyViewHolderForDeaufult) holder).llAttentioniconBackLoationContent.setVisibility(View.GONE);
            ((MyViewHolderForDeaufult) holder).rlBackforapply.setVisibility(View.GONE);
            ((MyViewHolderForDeaufult) holder).rlReplyforapply.setVisibility(View.GONE);
            ((MyViewHolderForDeaufult) holder).rlAttentionitemSmile.setVisibility(View.GONE);
            ((MyViewHolderForDeaufult) holder).btnSearchBtnGo.setVisibility(View.GONE);
            ((MyViewHolderForDeaufult) holder).llTaskHeButton.setVisibility(View.GONE);
            ((MyViewHolderForDeaufult) holder).llPostImages.setVisibility(View.GONE);
            //大体布局的几种类型
            //会议类型
            if (layOutType.equals(FriendStateItem.MEETING)) {
                ((MyViewHolderForDeaufult) holder).iv_attentionitem_rightbtn.setImageResource(R.drawable.righttop3);

            } else if
                //任务类型
                    (layOutType.equals(FriendStateItem.TASK)) {
                ((MyViewHolderForDeaufult) holder).iv_attentionitem_rightbtn.setImageResource(R.drawable.righttop1);

                //复杂审批类型
            } else if (layOutType.equals(FriendStateItem.ADAPPROVE)) {
                ((MyViewHolderForDeaufult) holder).iv_attentionitem_rightbtn.setImageResource(R.drawable.righttop2);

            }
            //审批类型
            else if (layOutType.equals(FriendStateItem.APPROVE)) {
                ((MyViewHolderForDeaufult) holder).iv_attentionitem_rightbtn.setImageResource(R.drawable.righttop2);
                //微博类型
            } else if (layOutType.equals(FriendStateItem.BLOG)) {
                ((MyViewHolderForDeaufult) holder).iv_attentionitem_rightbtn.setImageResource(R.drawable.righttop4);
            }
            ((MyViewHolderForDeaufult) holder).iv_attentionitem_rightbtn.setTag(R.id.tag_rightbtn, mPosition);
            ((MyViewHolderForDeaufult) holder).iv_attentionitem_rightbtn.setOnClickListener(monClickListener);

            if (!list.get(mPosition).getAttachments()[0].equals("http://www.dosns.net/doattachment/")) {
                ((MyViewHolderForDeaufult) holder).rl_layout_file.setVisibility(View.VISIBLE);
                ((MyViewHolderForDeaufult) holder).tv_file_who.setText(fs.getName() + "提交了附件，请点击查看");
            }
            //有语音时
            if (!list.get(mPosition).getVideoUri().equals("")) {
                final int pos = mPosition;
                ((MyViewHolderForDeaufult) holder).llLayoutVoice.setVisibility(View.VISIBLE);
                ((MyViewHolderForDeaufult) holder).tvVoiceTime.setText("＂");
                ((MyViewHolderForDeaufult) holder).llLayoutVoice.setTag(R.id.tag_llLayoutVoice, (MyViewHolderForDeaufult) holder);
                ((MyViewHolderForDeaufult) holder).llLayoutVoice.setTag(R.id.tag_llLayoutVoiceforid, mPosition);
                ((MyViewHolderForDeaufult) holder).llLayoutVoice.setOnClickListener(monClickListener);
                ((MyViewHolderForDeaufult) holder).llLayoutVoice.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        showDioalg(pos);
                        return false;
                    }
                });
            }
            if (!list.get(mPosition).getEndTime().equals("")) {
                ((MyViewHolderForDeaufult) holder).tvEndtime.setVisibility(View.VISIBLE);
                ((MyViewHolderForDeaufult) holder).tvEndtime.setText(list.get(mPosition).getEndTime());
                ((MyViewHolderForDeaufult) holder).tvEndtime.setTag(R.id.tag_tvEndtime, mPosition);
                ((MyViewHolderForDeaufult) holder).tvEndtime.setOnClickListener(monClickListener);
            }

            //有图片
            if (!list.get(mPosition).getImages()[0].equals("http://www.dosns.net/doimg/")) {
                ((MyViewHolderForDeaufult) holder).llPostImages.setVisibility(View.VISIBLE);
                final String[] images = list.get(mPosition).getImages();
                LogUtils.i(images.length + "");
                final ArrayList<String> datas = new ArrayList<String>();
                for (String imageurl : images) {
                    datas.add(imageurl);
                }
                ((MyViewHolderForDeaufult) holder).bind(datas);


            }
            //有定位
            if (!TextUtils.isEmpty(fs.getLocationName())) {
                ((MyViewHolderForDeaufult) holder).llAttentioniconBackLoationContent.setVisibility(View.VISIBLE);
                ((MyViewHolderForDeaufult) holder).newsfeedpublishPoiPlace.setText(fs.getLocationName());
                ((MyViewHolderForDeaufult) holder).llAttentioniconBackLoationContent.setTag(R.id.tag_LoationContent, mPosition);
                ((MyViewHolderForDeaufult) holder).llAttentioniconBackLoationContent.setOnClickListener(monClickListener);
            }
            ((MyViewHolderForDeaufult) holder).rlBackforapply.setVisibility(View.GONE);
            ((MyViewHolderForDeaufult) holder).rlReplyforapply.setVisibility(View.GONE);
            ((MyViewHolderForDeaufult) holder).rlAttentionitemSmile.setVisibility(View.GONE);
            //处理的是底部栏
            if (fs.getShowlike().equals("1")) {
                ((MyViewHolderForDeaufult) holder).rlBackforapply.setVisibility(View.VISIBLE);
                ((MyViewHolderForDeaufult) holder).ivBackforapplySum.setText(fs.getLikenumber());
            }
            if (fs.getShowreply().equals("1")) {
                ((MyViewHolderForDeaufult) holder).rlReplyforapply.setVisibility(View.VISIBLE);
                ((MyViewHolderForDeaufult) holder).ivReplyforapplySum.setText(fs.getReplynumber());
                ((MyViewHolderForDeaufult) holder).rlReplyforapply.setTag(R.id.tag_rlReplyforapply, mPosition);
                ((MyViewHolderForDeaufult) holder).rlReplyforapply.setOnClickListener(monClickListener);

            }
            if (fs.getShowshare().equals("1")) {
                ((MyViewHolderForDeaufult) holder).rlAttentionitemSmile.setVisibility(View.VISIBLE);
                ((MyViewHolderForDeaufult) holder).ivAttentionitemSmileSum.setText(fs.getSharenumber());
                ((MyViewHolderForDeaufult) holder).rlAttentionitemSmile.setTag(R.id.tag_rlAttentionitemSmile, mPosition);
                ((MyViewHolderForDeaufult) holder).rlAttentionitemSmile.setOnClickListener(monClickListener);
            }
            //处理底部操作按钮

            if (fs.getOperatebutton().equals("1")) {
                ((MyViewHolderForDeaufult) holder).btnSearchBtnGo.setVisibility(View.GONE);
                ((MyViewHolderForDeaufult) holder).llTaskHeButton.setVisibility(View.VISIBLE);
                ((MyViewHolderForDeaufult) holder).tvTaskBtninfo.setText("接受：");
            } else if (fs.getOperatebutton().equals("2")) {
                ((MyViewHolderForDeaufult) holder).btnSearchBtnGo.setVisibility(View.GONE);
                ((MyViewHolderForDeaufult) holder).llTaskHeButton.setVisibility(View.VISIBLE);
                ((MyViewHolderForDeaufult) holder).tvTaskBtninfo.setText("处理完毕：");
            } else if (fs.getOperatebutton().equals("3")) {
                ((MyViewHolderForDeaufult) holder).btnSearchBtnGo.setVisibility(View.GONE);
                ((MyViewHolderForDeaufult) holder).llTaskHeButton.setVisibility(View.VISIBLE);
                ((MyViewHolderForDeaufult) holder).tvTaskBtninfo.setText("结束：");
            } else if (fs.getOperatebutton().equals("4")) {
                ((MyViewHolderForDeaufult) holder).btnSearchBtnGo.setVisibility(View.GONE);
                ((MyViewHolderForDeaufult) holder).llTaskHeButton.setVisibility(View.VISIBLE);
                ((MyViewHolderForDeaufult) holder).tvTaskBtninfo.setText("同意：");
            } else if (fs.getOperatebutton().equals("5")) {
                ((MyViewHolderForDeaufult) holder).llTaskHeButton.setVisibility(View.GONE);
                ((MyViewHolderForDeaufult) holder).btnSearchBtnGo.setVisibility(View.VISIBLE);
                ((MyViewHolderForDeaufult) holder).btnSearchBtnGo.setTag(R.id.tag_btnSearchBtnGo, mPosition);
                ((MyViewHolderForDeaufult) holder).btnSearchBtnGo.setOnClickListener(monClickListener);
            } else if (fs.getOperatebutton().equals("6")) {
                ((MyViewHolderForDeaufult) holder).btnSearchBtnGo.setVisibility(View.GONE);
                ((MyViewHolderForDeaufult) holder).llTaskHeButton.setVisibility(View.VISIBLE);
                ((MyViewHolderForDeaufult) holder).tvTaskBtninfo.setText("参加：");
            } else {
                ((MyViewHolderForDeaufult) holder).tvTaskBtninfo.setText("");
                ((MyViewHolderForDeaufult) holder).btnSearchBtnGo.setVisibility(View.GONE);
                ((MyViewHolderForDeaufult) holder).llTaskHeButton.setVisibility(View.GONE);
            }
            ((MyViewHolderForDeaufult) holder).btnTaskBtnYes.setTag(R.id.tag_btnTaskBtnYes, mPosition);
            ((MyViewHolderForDeaufult) holder).btnTaskBtnNo.setTag(R.id.tag_btnTaskBtnNo, mPosition);
            ((MyViewHolderForDeaufult) holder).rlBackforapply.setTag(R.id.tag_rlBackforapply, mPosition);
            ((MyViewHolderForDeaufult) holder).btnTaskBtnYes.setOnClickListener(monClickListener);
            ((MyViewHolderForDeaufult) holder).btnTaskBtnNo.setOnClickListener(monClickListener);
            ((MyViewHolderForDeaufult) holder).rlBackforapply.setOnClickListener(monClickListener);

            if (!TextUtils.isEmpty(html)) {
                ((MyViewHolderForDeaufult) holder).infoname.setVisibility(View.VISIBLE);
                ((MyViewHolderForDeaufult) holder).infoname.setMaxLines(3);
                if (html.length() > 180) {
                    ((MyViewHolderForDeaufult) holder).tvLookAlldeaita.setVisibility(View.VISIBLE);
                    ((MyViewHolderForDeaufult) holder).tvLookAlldeaita.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                        }
                    });
                }
                //设置带表情的内容
                Spannable span1 = EaseSmileUtils.getSmiledText(context, html);
                // 设置内容
                ((MyViewHolderForDeaufult) holder).infoname.setText(span1, TextView.BufferType.SPANNABLE);
            }
            showListPopulist(((MyViewHolderForDeaufult) holder).ivIconforattention, mPosition, 1);
            Utils.setAver(list.get(mPosition).getUserIcon(), ((MyViewHolderForDeaufult) holder).ivIconforattention);
            ((MyViewHolderForDeaufult) holder).tvAppfordate.setText(list.get(mPosition).getDate());
            ((MyViewHolderForDeaufult) holder).tvAttentionforname.setText(list.get(mPosition).getName());
        }
    }


    @Override
    public int getItemCount() {
        // 返回数据总数
        return list == null ? 0 : list.size();
    }

    @Override
    public void onClick(View v) {
        if (mOnItemClickListener != null) {
            //注意这里使用getTag方法获取数据
            mOnItemClickListener.onItemClick(v,(int)v.getTag());
        }
    }

    // 重写的自定义ViewHolder
    public class MyViewHolder
            extends RecyclerView.ViewHolder {
        @InjectView(R.id.iv_post_img)
        ImageView iv_post_img;
        @InjectView(R.id.rl_layout_file)
        RelativeLayout rl_layout_file;
        @InjectView(R.id.tv_file_who)
        TextView tv_file_who;
        @InjectView(R.id.iv_attentionitem_rightbtn)
        ImageView iv_attentionitem_rightbtn;
        @InjectView(R.id.iv_iconforattention)
        RoundedCornersImage ivIconforattention;
        @InjectView(R.id.tv_attentionforname)
        TextView tvAttentionforname;
        @InjectView(R.id.tv_appfordate)
        TextView tvAppfordate;
        @InjectView(R.id.infoname)
        TextView infoname;
        @InjectView(R.id.tv_look_alldeaita)
        TextView tvLookAlldeaita;
        @InjectView(R.id.iv_search_icon)
        RoundedCornersImage ivSearchIcon;
        @InjectView(R.id.tv_search_info)
        TextView tvSearchInfo;
        @InjectView(R.id.tv_search_data)
        TextView tvSearchData;
        @InjectView(R.id.tv_search_me_argeeinfo)
        TextView tvSearchMeArgeeinfo;
        @InjectView(R.id.rl_search_me_argeeinfo)
        RelativeLayout rlSearchMeArgeeinfo;
        @InjectView(R.id.ll_post_images)
        NineGridImageView llPostImages;
        @InjectView(R.id.tv_voice_time)
        TextView tvVoiceTime;
        @InjectView(R.id.iv_showAttentionMessage_voiceanmion)
        ImageView ivShowAttentionMessageVoiceanmion;
        @InjectView(R.id.pb_post_voice)
        ProgressBar pbPostVoice;
        @InjectView(R.id.ll_layout_voice)
        RelativeLayout llLayoutVoice;
        @InjectView(R.id.newsfeedpublish_poi_place)
        TextView newsfeedpublishPoiPlace;
        @InjectView(R.id.ll_attentionicon_backLoationContent)
        LinearLayout llAttentioniconBackLoationContent;
        @InjectView(R.id.tv_task_btninfo)
        TextView tvTaskBtninfo;
        @InjectView(R.id.btn_task_btn_yes)
        Button btnTaskBtnYes;
        @InjectView(R.id.btn_task_btn_no)
        Button btnTaskBtnNo;
        @InjectView(R.id.ll_task_he_button)
        RelativeLayout llTaskHeButton;
        @InjectView(R.id.btn_search_btn_go)
        Button btnSearchBtnGo;
        @InjectView(R.id.tv_endtime)
        TextView tvEndtime;
        @InjectView(R.id.iv_backforapply_sum)
        TextView ivBackforapplySum;
        @InjectView(R.id.rl_backforapply)
        RelativeLayout rlBackforapply;
        @InjectView(R.id.iv_replyforapply_sum)
        TextView ivReplyforapplySum;
        @InjectView(R.id.rl_replyforapply)
        RelativeLayout rlReplyforapply;
        @InjectView(R.id.iv_attentionitem_smile_sum)
        TextView ivAttentionitemSmileSum;
        @InjectView(R.id.rl_attentionitem_smile)
        RelativeLayout rlAttentionitemSmile;
        @InjectView(R.id.tv_bmob_content)
        TextView tvBmobContent;
        @InjectView(R.id.tv_bmob_deaita)
        TextView tvBmobDeaita;
        @InjectView(R.id.ll_bmob_images)
        NineGridImageView llBmobImages;
        @InjectView(R.id.rl_bmob_he_main)
        LinearLayout rlBmobHeMain;
        @InjectView(R.id.root_comment)
        CardView root_comment;



        public void bind(ArrayList<String> datas) {
            LogUtils.i("bind1:"+datas.size());
            llPostImages.setImagesData(datas);

        }
        public void bind2(ArrayList<String> datas) {
            LogUtils.i("bind2:"+datas.size());
            llBmobImages.setImagesData(datas);

        }
        public MyViewHolder(View v) {
            super(v);
            AutoUtils.autoSize(v);
            ButterKnife.inject(this, v);
            llPostImages.setAdapter(mAdapter);
            llBmobImages.setAdapter(mAdapter);
        }
    }
    private NineGridImageViewAdapter<String> mAdapter = new NineGridImageViewAdapter<String>() {
        @Override
        protected void onDisplayImage(Context context, ImageView imageView, String s) {
            Utils.loadImage(s,imageView,tag);
        }

        @Override
        protected ImageView generateImageView(Context context) {
            return super.generateImageView(context);
        }

        @Override
        protected void onItemImageClick(Context context, int index, List<String> list) {
            Intent intent = new Intent(context, ImagePagerActivity.class);
            intent.putExtra("image_urls", (ArrayList)list);//非必须
            intent.putExtra("image_index", index);
            context.startActivity(intent);
        }
    };
    // 重写的自定义ViewHolder
    public class MyViewHolderForBmob
            extends RecyclerView.ViewHolder {
        @InjectView(R.id.iv_post_img)
        ImageView iv_post_img;
        @InjectView(R.id.rl_layout_file)
        RelativeLayout rl_layout_file;
        @InjectView(R.id.tv_file_who)
        TextView tv_file_who;
        @InjectView(R.id.iv_attentionitem_rightbtn)
        ImageView iv_attentionitem_rightbtn;
        @InjectView(R.id.iv_iconforattention)
        RoundedCornersImage ivIconforattention;
        @InjectView(R.id.tv_attentionforname)
        TextView tvAttentionforname;
        @InjectView(R.id.tv_appfordate)
        TextView tvAppfordate;
        @InjectView(R.id.infoname)
        TextView infoname;
        @InjectView(R.id.tv_look_alldeaita)
        TextView tvLookAlldeaita;

        @InjectView(R.id.ll_post_images)
        NineGridImageView llPostImages;
        @InjectView(R.id.tv_voice_time)
        TextView tvVoiceTime;
        @InjectView(R.id.iv_showAttentionMessage_voiceanmion)
        ImageView ivShowAttentionMessageVoiceanmion;
        @InjectView(R.id.pb_post_voice)
        ProgressBar pbPostVoice;
        @InjectView(R.id.ll_layout_voice)
        RelativeLayout llLayoutVoice;
        @InjectView(R.id.newsfeedpublish_poi_place)
        TextView newsfeedpublishPoiPlace;
        @InjectView(R.id.ll_attentionicon_backLoationContent)
        LinearLayout llAttentioniconBackLoationContent;
        @InjectView(R.id.tv_task_btninfo)
        TextView tvTaskBtninfo;
        @InjectView(R.id.btn_task_btn_yes)
        Button btnTaskBtnYes;
        @InjectView(R.id.btn_task_btn_no)
        Button btnTaskBtnNo;
        @InjectView(R.id.ll_task_he_button)
        RelativeLayout llTaskHeButton;
        @InjectView(R.id.btn_search_btn_go)
        Button btnSearchBtnGo;
        @InjectView(R.id.tv_endtime)
        TextView tvEndtime;
        @InjectView(R.id.iv_backforapply_sum)
        TextView ivBackforapplySum;
        @InjectView(R.id.rl_backforapply)
        RelativeLayout rlBackforapply;
        @InjectView(R.id.iv_replyforapply_sum)
        TextView ivReplyforapplySum;
        @InjectView(R.id.rl_replyforapply)
        RelativeLayout rlReplyforapply;
        @InjectView(R.id.iv_attentionitem_smile_sum)
        TextView ivAttentionitemSmileSum;
        @InjectView(R.id.rl_attentionitem_smile)
        RelativeLayout rlAttentionitemSmile;
        @InjectView(R.id.tv_bmob_content)
        TextView tvBmobContent;
        @InjectView(R.id.tv_bmob_deaita)
        TextView tvBmobDeaita;
        @InjectView(R.id.ll_bmob_images)
        NineGridImageView llBmobImages;
        @InjectView(R.id.rl_bmob_he_main)
        LinearLayout rlBmobHeMain;
        @InjectView(R.id.root_comment)
        CardView root_comment;



        public void bind(ArrayList<String> datas) {
            llPostImages.setImagesData(datas);
        }
        public void bind2(ArrayList<String> datas) {
            llBmobImages.setImagesData(datas);
        }
        public MyViewHolderForBmob(View v) {
            super(v);
            AutoUtils.autoSize(v);
            ButterKnife.inject(this, v);
            llPostImages.setAdapter(mAdapter);
            llBmobImages.setAdapter(mAdapter);
        }

    }

    // 重写的自定义ViewHolder
    public class MyViewHolderforBean
            extends RecyclerView.ViewHolder {
        @InjectView(R.id.iv_post_img)
        ImageView iv_post_img;
        @InjectView(R.id.rl_layout_file)
        RelativeLayout rl_layout_file;
        @InjectView(R.id.tv_file_who)
        TextView tv_file_who;
        @InjectView(R.id.iv_attentionitem_rightbtn)
        ImageView iv_attentionitem_rightbtn;
        @InjectView(R.id.iv_iconforattention)
        RoundedCornersImage ivIconforattention;
        @InjectView(R.id.tv_attentionforname)
        TextView tvAttentionforname;
        @InjectView(R.id.tv_appfordate)
        TextView tvAppfordate;
        @InjectView(R.id.infoname)
        TextView infoname;
        @InjectView(R.id.tv_look_alldeaita)
        TextView tvLookAlldeaita;
        @InjectView(R.id.iv_search_icon)
        RoundedCornersImage ivSearchIcon;
        @InjectView(R.id.tv_search_info)
        TextView tvSearchInfo;
        @InjectView(R.id.tv_search_data)
        TextView tvSearchData;
        @InjectView(R.id.tv_search_me_argeeinfo)
        TextView tvSearchMeArgeeinfo;
        @InjectView(R.id.rl_search_me_argeeinfo)
        RelativeLayout rlSearchMeArgeeinfo;
        @InjectView(R.id.ll_post_images)
        NineGridImageView llPostImages;
        @InjectView(R.id.tv_voice_time)
        TextView tvVoiceTime;
        @InjectView(R.id.iv_showAttentionMessage_voiceanmion)
        ImageView ivShowAttentionMessageVoiceanmion;
        @InjectView(R.id.pb_post_voice)
        ProgressBar pbPostVoice;
        @InjectView(R.id.ll_layout_voice)
        RelativeLayout llLayoutVoice;
        @InjectView(R.id.newsfeedpublish_poi_place)
        TextView newsfeedpublishPoiPlace;
        @InjectView(R.id.ll_attentionicon_backLoationContent)
        LinearLayout llAttentioniconBackLoationContent;
        @InjectView(R.id.tv_task_btninfo)
        TextView tvTaskBtninfo;
        @InjectView(R.id.btn_task_btn_yes)
        Button btnTaskBtnYes;
        @InjectView(R.id.btn_task_btn_no)
        Button btnTaskBtnNo;
        @InjectView(R.id.ll_task_he_button)
        RelativeLayout llTaskHeButton;
        @InjectView(R.id.btn_search_btn_go)
        Button btnSearchBtnGo;
        @InjectView(R.id.tv_endtime)
        TextView tvEndtime;
        @InjectView(R.id.iv_backforapply_sum)
        TextView ivBackforapplySum;
        @InjectView(R.id.rl_backforapply)
        RelativeLayout rlBackforapply;
        @InjectView(R.id.iv_replyforapply_sum)
        TextView ivReplyforapplySum;
        @InjectView(R.id.rl_replyforapply)
        RelativeLayout rlReplyforapply;
        @InjectView(R.id.iv_attentionitem_smile_sum)
        TextView ivAttentionitemSmileSum;
        @InjectView(R.id.rl_attentionitem_smile)
        RelativeLayout rlAttentionitemSmile;
        @InjectView(R.id.root_comment)
        CardView root_comment;


        public void bind(ArrayList<String> datas) {
            llPostImages.setImagesData(datas);
        }
        public MyViewHolderforBean(View v) {
            super(v);
            AutoUtils.autoSize(v);
            ButterKnife.inject(this, v);
            llPostImages.setAdapter(mAdapter);
        }
    }

    // 重写的自定义ViewHolder
    public class MyViewHolderForDeaufult
            extends RecyclerView.ViewHolder {
        @InjectView(R.id.iv_post_img)
        ImageView iv_post_img;
        @InjectView(R.id.rl_layout_file)
        RelativeLayout rl_layout_file;
        @InjectView(R.id.tv_file_who)
        TextView tv_file_who;
        @InjectView(R.id.iv_attentionitem_rightbtn)
        ImageView iv_attentionitem_rightbtn;
        @InjectView(R.id.iv_iconforattention)
        RoundedCornersImage ivIconforattention;
        @InjectView(R.id.tv_attentionforname)
        TextView tvAttentionforname;
        @InjectView(R.id.tv_appfordate)
        TextView tvAppfordate;
        @InjectView(R.id.infoname)
        TextView infoname;
        @InjectView(R.id.tv_look_alldeaita)
        TextView tvLookAlldeaita;
        @InjectView(R.id.ll_post_images)
        NineGridImageView llPostImages;
        @InjectView(R.id.tv_voice_time)
        TextView tvVoiceTime;
        @InjectView(R.id.iv_showAttentionMessage_voiceanmion)
        ImageView ivShowAttentionMessageVoiceanmion;
        @InjectView(R.id.pb_post_voice)
        ProgressBar pbPostVoice;
        @InjectView(R.id.ll_layout_voice)
        RelativeLayout llLayoutVoice;
        @InjectView(R.id.newsfeedpublish_poi_place)
        TextView newsfeedpublishPoiPlace;
        @InjectView(R.id.ll_attentionicon_backLoationContent)
        LinearLayout llAttentioniconBackLoationContent;
        @InjectView(R.id.tv_task_btninfo)
        TextView tvTaskBtninfo;
        @InjectView(R.id.btn_task_btn_yes)
        Button btnTaskBtnYes;
        @InjectView(R.id.btn_task_btn_no)
        Button btnTaskBtnNo;
        @InjectView(R.id.ll_task_he_button)
        RelativeLayout llTaskHeButton;
        @InjectView(R.id.btn_search_btn_go)
        Button btnSearchBtnGo;
        @InjectView(R.id.tv_endtime)
        TextView tvEndtime;
        @InjectView(R.id.iv_backforapply_sum)
        TextView ivBackforapplySum;
        @InjectView(R.id.rl_backforapply)
        RelativeLayout rlBackforapply;
        @InjectView(R.id.iv_replyforapply_sum)
        TextView ivReplyforapplySum;
        @InjectView(R.id.rl_replyforapply)
        RelativeLayout rlReplyforapply;
        @InjectView(R.id.iv_attentionitem_smile_sum)
        TextView ivAttentionitemSmileSum;
        @InjectView(R.id.rl_attentionitem_smile)
        RelativeLayout rlAttentionitemSmile;
        @InjectView(R.id.root_comment)
        CardView root_comment;



        public void bind(ArrayList<String> datas) {
            llPostImages.setImagesData(datas);
        }
        public MyViewHolderForDeaufult(View v) {
            super(v);
            AutoUtils.autoSize(v);
            ButterKnife.inject(this, v);
            llPostImages.setAdapter(mAdapter);
        }
    }

    /**
     * 进入WEB界面
     *
     * @param linkaddress
     */
    private void intoWebActivity(String linkaddress, String type) {
        Intent intent = new Intent(context, MeetingWebActivity.class);
        intent.putExtra("result", linkaddress + "?userid=" + DemoHelper.getInstance().getCurrentUsernName());
        intent.putExtra("type", type);
        context.startActivity(intent);
    }



    public void intoDescatte(FriendStateItem fs) {
        Intent intent = new Intent("android.intent.publicActivity");
        Bundle bundle = new Bundle();
        bundle.putSerializable("fs", fs);
        bundle.putInt("type", 2);
        bundle.putString("docId", fs.getDocid());
        intent.putExtras(bundle);
        context.startActivity(intent);
    }

    private class MonClickListener implements View.OnClickListener {


        public MonClickListener() {

        }


        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                //微啵转发链接内容
                case R.id.tv_bmob_deaita:
                    int p = (int) view.getTag(R.id.tag_tvBmobDeaita);
                    LinkContent linkContent = list.get(p).getLinkContent().get(0);
                    if (linkContent.getLinktype().equals("app")) {
                        Intent intent = new Intent("android.intent.publicActivity");
                        Bundle bundle = new Bundle();
                        bundle.putString("docId", linkContent.getLinkaddress());
                        bundle.putInt("type", 1);
                        intent.putExtras(bundle);
                        context.startActivity(intent);
                    } else {
                        intoWebActivity(linkContent.getLinkaddress(), "4");
                    }
                    break;
                case R.id.rl_bmob_he_main:
                    int p1 = (int) view.getTag(R.id.tag_rlBmobHeMain);
                    LinkContent linkContent1 = list.get(p1).getLinkContent().get(0);
                    if (linkContent1.getLinktype().equals("app")) {
                        Intent intent = new Intent("android.intent.publicActivity");
                        Bundle bundle = new Bundle();
                        bundle.putString("docId", linkContent1.getLinkaddress());
                        bundle.putInt("type", 1);
                        intent.putExtras(bundle);
                        context.startActivity(intent);
                        //requstLinken(linkContent1.getLinkaddress());
                    } else {
                        intoWebActivity(linkContent1.getLinkaddress(), "4");
                        LogUtils.i("link:" + linkContent1.getLinkaddress());
                    }
                    break;
                //进入回复界面
                case R.id.rl_replyforapply:
                    int p2 = (int) view.getTag(R.id.tag_rlReplyforapply);
                    intoDescatte(list.get(p2));
                    break;
                //点击是否加入日程提醒
                case R.id.tv_endtime:
                    final int p3 = (int) view.getTag(R.id.tag_tvEndtime);
                    if (DateUntils.isUpNowtime(list.get(p3).getEndTime())) {
                        new AlertDialog.Builder(context).setMessage("是否加入日程提醒?").setNegativeButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                inputCalalderEVENT(p3);
                            }
                        }).setNeutralButton("取消", null).show();
                    } else {
                        Utils.showToast("日程已经过期！", context);
                    }
                    break;
                //点击了转发按钮
                case R.id.rl_attentionitem_smile:
                    int p4 = (int) view.getTag(R.id.tag_rlAttentionitemSmile);
                    //进入转发界面
                    intoReaper(p4);
                    break;
                case R.id.btn_task_btn_yes:
                    int p5 = (int) view.getTag(R.id.tag_btnTaskBtnYes);
                    //发送操作请求
                    Log.i("weitie:", "yes");
                    sendOperate(p5, "1", "1");
                    break;
                //点击查看全文
                case R.id.tv_look_alldeaita:
                    RecyclerView.ViewHolder viewHolder = (RecyclerView.ViewHolder) view.getTag(R.id.tag_tvLookAlldeaita);
                    if (viewHolder instanceof MyViewHolder) {
                        ((MyViewHolder) viewHolder).infoname.setMaxLines(10);
                        ((MyViewHolder) viewHolder).tvLookAlldeaita.setVisibility(View.GONE);
                    }
                    if (viewHolder instanceof MyViewHolderforBean) {
                        ((MyViewHolderforBean) viewHolder).infoname.setMaxLines(10);
                        ((MyViewHolderforBean) viewHolder).tvLookAlldeaita.setVisibility(View.GONE);
                    }
                    if (viewHolder instanceof MyViewHolderForBmob) {
                        ((MyViewHolderForBmob) viewHolder).infoname.setMaxLines(10);
                        ((MyViewHolderForBmob) viewHolder).tvLookAlldeaita.setVisibility(View.GONE);
                    }
                    if (viewHolder instanceof MyViewHolderForDeaufult) {
                        ((MyViewHolderForDeaufult) viewHolder).infoname.setMaxLines(10);
                        ((MyViewHolderForDeaufult) viewHolder).tvLookAlldeaita.setVisibility(View.GONE);
                    }
                    break;
                case R.id.btn_task_btn_no:
                    int p6 = (int) view.getTag(R.id.tag_btnTaskBtnNo);
                    //发送操作请求
                    sendOperate(p6, "1", "0");
                    break;
                //点赞
                case R.id.rl_backforapply:
                    int p7 = (int) view.getTag(R.id.tag_rlBackforapply);
                    sendOperate(p7, "0", "0");
                    break;
               /* case R.id.iv_post_img:
                    //点击打开附件
                    String fileurl = list.get(p).getAttachments()[0];
                    LogUtils.i("附件被点击了" + fileurl);
                    downFile(fileurl);
                    break;*/
                //点击播放声音
                case R.id.ll_layout_voice:
                    int p8 = (int) view.getTag(R.id.tag_llLayoutVoiceforid);
                    String voiceUri = list.get(p8).getVideoUri();
                    RecyclerView.ViewHolder viewHolder1 = (RecyclerView.ViewHolder) view.getTag(R.id.tag_llLayoutVoice);
                    if (viewHolder1 instanceof MyViewHolder) {
                        new VoicePlayClickListener(voiceUri, ((MyViewHolder) viewHolder1).ivShowAttentionMessageVoiceanmion, context, ((MyViewHolder) viewHolder1).pbPostVoice, p8 + "").onClick(view);
                    }
                    if (viewHolder1 instanceof MyViewHolderforBean) {
                        new VoicePlayClickListener(voiceUri, ((MyViewHolderforBean) viewHolder1).ivShowAttentionMessageVoiceanmion, context, ((MyViewHolderforBean) viewHolder1).pbPostVoice, p8 + "").onClick(view);

                    }
                    if (viewHolder1 instanceof MyViewHolderForBmob) {
                        new VoicePlayClickListener(voiceUri, ((MyViewHolderForBmob) viewHolder1).ivShowAttentionMessageVoiceanmion, context, ((MyViewHolderForBmob) viewHolder1).pbPostVoice, p8 + "").onClick(view);

                    }
                    if (viewHolder1 instanceof MyViewHolderForDeaufult) {
                        new VoicePlayClickListener(voiceUri, ((MyViewHolderForDeaufult) viewHolder1).ivShowAttentionMessageVoiceanmion, context, ((MyViewHolderForDeaufult) viewHolder1).pbPostVoice, p8 + "").onClick(view);
                    }

                    break;
                //点击查看地图
                case R.id.ll_attentionicon_backLoationContent:
                    int p9 = (int) view.getTag(R.id.tag_LoationContent);
                    Intent intent;
                    intent = new Intent(context, MyBaiduMapActivity.class);
                    intent.putExtra("latitude", Double.parseDouble(list.get(p9).getLatitude()));
                    intent.putExtra("longitude", Double.parseDouble(list.get(p9).getLongitude()));
                    intent.putExtra("address", list.get(p9).getLocationName());
                    intent.putExtra("status", 1);
                    context.startActivity(intent);
                    break;

                case R.id.iv_attentionitem_rightbtn:
                    int p10 = (int) view.getTag(R.id.tag_rightbtn);
                    TitlePopup popup = getButtonShos(list.get(p10).getMenu_delete(), list.get(p10).getMenu_takeback(), list.get(p10).getMenu_top(), p10);
                    popup.show(view);
                    break;
               /* //头像点击事件
                case R.id.iv_iconforattention:
                    String userid0 = (String) view.getTag(R.id.tag_ivIconforattention);
                    Utils.intoInfo(context, userid0);
                    break;
                case R.id.iv_search_icon:
                    String userid = (String) view.getTag(R.id.tag_ivSearchIcon);
                    Utils.intoInfo(context, userid);
                    break;*/
                case R.id.btn_search_btn_go:
                    int p11 = (int) view.getTag(R.id.tag_btnSearchBtnGo);
                    intoWebActivity(list.get(p11).getLinkContent().get(0).getLinkaddress(), "4");
                    break;


            }
        }
    }

    /**
     * 向系统日历添加事件
     *
     * @param p
     */
    private void inputCalalderEVENT(int p) {
        String calanderURL = "content://com.android.calendar/calendars";
        String calanderEventURL = "content://com.android.calendar/events";
        String calanderRemiderURL = "content://com.android.calendar/reminders";
        //获取要出入的gmail账户的id
        String calId = "";
        Cursor userCursor = context.getContentResolver().query(Uri.parse(calanderURL), null,
                null, null, null);
        if (userCursor.getCount() > 0) {
            userCursor.moveToFirst();
            calId = userCursor.getString(userCursor.getColumnIndex("_id"));

        }
        ContentValues event = new ContentValues();
        event.put("title", "麻绳任务");
        event.put("description", list.get(p).getContent());
        //插入hoohbood@gmail.com这个账户
        event.put("calendar_id", calId);
        Calendar mCalendar = Calendar.getInstance(Locale.CHINA);
        mCalendar.setTime(new Date());
        long start = mCalendar.getTime().getTime();
        String eralttiem = list.get(p).getEndTime();
        mCalendar.setTime(DateUntils.getEndDate(eralttiem));
        long end = mCalendar.getTime().getTime();
        LogUtils.i("start:" + start + ",end:" + end);
        event.put("dtstart", start);
        event.put("dtend", end);
        event.put("hasAlarm", 1);
        event.put(CalendarContract.Events.EVENT_TIMEZONE, "America/Los_Angeles");
        Uri newEvent = context.getContentResolver().insert(Uri.parse(calanderEventURL), event);
        long id = Long.parseLong(newEvent.getLastPathSegment());
        ContentValues values = new ContentValues();
        values.put("event_id", id);
        //提前10分钟有提醒
        values.put("minutes", 0);
        context.getContentResolver().insert(Uri.parse(calanderRemiderURL), values);
        Toast.makeText(context, "插入事件成功,已加入系统日历提醒", Toast.LENGTH_LONG).show();
    }

    //进入转发界面
    private void intoReaper(int p) {
        String type = list.get(p).getType();
        if (type.equals(FriendStateItem.TASK) || type.equals(FriendStateItem.MEETING) || type.equals(FriendStateItem.BLOG)) {
            Intent intent = new Intent(context, LocatContentActivity.class);
            intent.putExtra("type", type);
            LogUtils.i(list.get(p).getDocid());
            intent.putExtra("mainid", list.get(p).getDocid());
            ((Activity) context).startActivityForResult(intent, 0x22);
        } else if (type.equals(FriendStateItem.APPROVE) || type.equals(FriendStateItem.ADAPPROVE)) {
            Intent intent = new Intent(context, WebActivity.class);
            intent.putExtra("result", Api.POST_Replter_H5_test);
            context.startActivity(intent);
        }
    }




    /**
     * 用户点是操作
     *
     * @param p
     */
    private void sendOperate(final int p, final String oprtype, final String operatebuttonvalue) {
        FriendStateItem fs = list.get(p);
        LogUtils.i("oprtype:" + oprtype + ",operatebuttonvalue:" + operatebuttonvalue + ",Operatebutton:" + fs.getOperatebutton());
        RequestParams params = new RequestParams("UTF-8");
        if (oprtype.equals("0")) {
            params.addBodyParameter("operatebutton", "0");
            params.addBodyParameter("operatebutton", fs.getOperatebutton());
        } else if (oprtype.equals("1")) {
            params.addBodyParameter("operatebutton", fs.getOperatebutton());
            params.addBodyParameter("operatebuttonvalue", operatebuttonvalue);
        } else if (oprtype.equals("2")) {
            params.addBodyParameter("operatebutton", "0");
            params.addBodyParameter("menuopr", operatebuttonvalue);
        }
        // params.addBodyParameter("mainid",fs.getDocid());
        params.addBodyParameter("mainid", fs.getDocid());
        params.addBodyParameter("replieruserid", DemoHelper.getInstance().getCurrentUsernName());
        params.addBodyParameter("repliername", Conversion.getInstance().getNickName());
        params.addBodyParameter("oprtype", oprtype);
        LogUtils.i("mainid:" + fs.getDocid() + ",repliername:" + Conversion.getInstance().getNickName());
        HttpUtils httpUtils = new HttpUtils();
        httpUtils.send(HttpRequest.HttpMethod.POST, Api.POST_OPERATE, params, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                LogUtils.i(responseInfo.result);
                ArrayList<FriendStateItem> friendStateItems = Json.parseOpaterdData(responseInfo.result);
                LogUtils.i(friendStateItems.size() + "");
                FriendStateItem fi = list.get(p);
                fi.setDocid(friendStateItems.get(0).getDocid());
                fi.setLikeBean(friendStateItems.get(0).getLikeBean());
                fi.setLikenumber(friendStateItems.get(0).getLikenumber());
                fi.setMenu_delete(friendStateItems.get(0).getMenu_delete());
                fi.setMenu_takeback(friendStateItems.get(0).getMenu_takeback());
                fi.setMenu_top(friendStateItems.get(0).getMenu_top());
                fi.setOperatebutton(friendStateItems.get(0).getOperatebutton());
                fi.setReplyBean(friendStateItems.get(0).getReplyBean());
                fi.setReplynumber(friendStateItems.get(0).getReplynumber());
                fi.setSharenumber(friendStateItems.get(0).getSharenumber());
                fi.setShowlike(friendStateItems.get(0).getShowlike());
                fi.setShowreply(friendStateItems.get(0).getShowreply());
                fi.setShowshare(friendStateItems.get(0).getShowshare());
                list.remove(p);
                if (oprtype.equals("2") && operatebuttonvalue.equals("1") || operatebuttonvalue.equals("2")) {
                   // resh(p);
                    notifyItemRemoved(p);
                } else if (oprtype.equals("2") && operatebuttonvalue.equals("3")) {
                    list.add(0, fi);
                    //resh(0);
                    notifyItemInserted(0);
                } else {
                    list.add(p, fi);
                   // resh(p);
                    notifyItemChanged(p);
                }
            }

            @Override
            public void onFailure(HttpException e, String s) {
                LogUtils.i(s);
            }
        });
    }

    private void downFile(String fileUrl) {
        File f = new File(fileUrl);
        final ProgressDialog pb = new ProgressDialog(context);
        pb.setTitle("正在打开,请等待...");
        pb.show();
        HttpUtils hp = new HttpUtils();
        HttpHandler httpHandler = hp.download(fileUrl, "/sdcard/" + f.getName(), false, true, new RequestCallBack<File>() {
            @Override
            public void onSuccess(ResponseInfo<File> responseInfo) {
                LogUtils.i("downloaded:" + responseInfo.result.getPath());
                FileUtils.openFile(new File(responseInfo.result.getPath()), context);
                Log.i("weitie1:", responseInfo.result.getPath());
                pb.dismiss();

            }

            @Override
            public void onFailure(HttpException e, String s) {
                LogUtils.i(s);
                Log.i("weitie2:", s);
                pb.dismiss();
            }
        });
    }

    private void resh(int p) {

        notifyDataSetChanged();
        LogUtils.i(p + "");
        //lv.sendMessage(lv.obtainMessage(1, p));
    }

    /**
     * 获取+号点击后的项目
     */
    private TitlePopup getButtonShos(String menu_delete, String menu_takeback, String menu_top, final int p) {
        //实例化标题栏弹窗
        TitlePopup titlePopup = new TitlePopup(context, getScreenWidth((Activity) context) / 2, ViewGroup.LayoutParams.WRAP_CONTENT);
        if (menu_delete.equals("1")) {
            titlePopup.addAction(new ActionItem(context, "删除", R.drawable.iconfont_delete));
        }
        if (menu_takeback.equals("1")) {
            titlePopup.addAction(new ActionItem(context, "收回", R.drawable.iconfont_filetop));
        }
        if (menu_top.equals("1")) {
            titlePopup.addAction(new ActionItem(context, "置顶", R.drawable.iconfont_transmit));
        }
        titlePopup.addAction(new ActionItem(context, "详情", R.drawable.iconfont_transmit));


        TitlePopup.OnItemOnClickListener itemOnClickListener = new TitlePopup.OnItemOnClickListener() {
            @Override
            public void onItemClick(ActionItem item, int position) {
                if (item.getmTitle().equals("删除")) {
                    if (list.get(p).getUserId().equals("11870")) {
                        Utils.showToast("您不能删除管理员信息！", context);
                        return;
                    }
                    sendOperate(p, "2", "1");
                    return;
                }
                if (item.getmTitle().equals("收回")) {
                    sendOperate(p, "2", "2");
                    return;
                }
                if (item.getmTitle().equals("置顶")) {
                    sendOperate(p, "2", "3");
                    return;
                }
                if (item.getmTitle().equals("详情")) {
                    intoDescatte(list.get(p));
                }
            }
        };
        titlePopup.setItemOnClickListener(itemOnClickListener);
        return titlePopup;
    }

    /**
     * 语音长按事件的对话框
     *
     * @param p
     */
    private void showDioalg(int p) {
        final SharedPreferences sp = context.getSharedPreferences("config", Context.MODE_PRIVATE);
        final int voiceMode = sp.getInt("voicemode", 1);
        String[] items;
        if (voiceMode == 1) {
            items = new String[]{"使用听筒模式"};
        } else {
            items = new String[]{"使用扬声器模式"};
        }
        new AlertDialog.Builder(context).setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        //切换播放语音模式
                        SharedPreferences.Editor edit = sp.edit();
                        if (voiceMode == 1) {
                            edit.putInt("voicemode", 2);
                            Utils.showToast("已切换到听筒播放模式", context);
                        } else {
                            edit.putInt("voicemode", 1);
                            Utils.showToast("已切换到杨声器播放模式", context);
                        }
                        edit.commit();
                        break;
                }
            }
        }).show();
    }

    public SpannableStringBuilder addClickablePart(String str, final String userid, int lenth, final Context context) {
        SpannableStringBuilder ssb = new SpannableStringBuilder(str);
        if (str.length() > 0) {
            // 最后一个
            ssb.setSpan(new ClickableSpan() {

                @Override
                public void onClick(View widget) {
                    LogUtils.i("点击了人名");
                    Utils.intoInfo(context, userid);

                }

                @Override
                public void updateDrawState(TextPaint ds) {
                    super.updateDrawState(ds);
                    // ds.setColor(Color.RED); // 设置文本颜色
                    ds.setColor(Color.BLUE);
                    // 去掉下划线
                    ds.setUnderlineText(false);
                }

            }, 3, 3 + lenth, 0);
        }
        return ssb;
    }

    public int getScreenWidth(Activity mContext) {
        DisplayMetrics metrics = new DisplayMetrics();
        mContext.getWindowManager().getDefaultDisplay()
                .getMetrics(metrics);
        return metrics.widthPixels;
    }

    /**
     * 点击头像弹出对话框
     * @param layout
     * @param contextPosition
     * @param type
     */
    private void showListPopulist(ImageView layout, int contextPosition,int type) {
        if(type==1){
            if(list.get(contextPosition).getUserId().equals(DemoHelper.getInstance().getCurrentUsernName())){
                layout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Utils.intoInfo(context,DemoHelper.getInstance().getCurrentUsernName());
                    }
                });
                return;
            }
        }
        if(type==2){
            if(list.get(contextPosition).getReplyBean().get(0).getReply_id().equals(DemoHelper.getInstance().getCurrentUsernName())){
                layout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Utils.intoInfo(context, DemoHelper.getInstance().getCurrentUsernName());
                    }
                });
                return;
            }
        }
        List<String> popupMenuItemList = new ArrayList<String>();
        popupMenuItemList.add("聊天");
        popupMenuItemList.add("语音");
        popupMenuItemList.add("电话");
        popupMenuItemList.add("详情");
        LogUtils.i("PopupList");
        new PopupList().initViewPopupList(context, contextPosition, layout, popupMenuItemList,type, new PopupListAdapter.OnPopupListClickListener() {
            @Override
            public void onPopupListItemClick(View contextView, int contextPosition, View view, int position,int type) {
                LogUtils.i("Main");
                String userid=null;
                String phone=null;
                if(type==1){
                    userid=list.get(contextPosition).getUserId();
                    phone=list.get(contextPosition).getCellphone();
                }else if(type==2){
                    userid=list.get(contextPosition).getReplyBean().get(0).getReply_id();
                    phone=list.get(contextPosition).getReplyBean().get(0).getReplyCellphone();
                }
                switch (position) {
                    //聊天
                    case 0:
                        context.startActivity(new Intent(context, ChatActivity.class).putExtra("userId", userid));
                        break;
                    //语音电话
                    case 1:
                        startVoiceCall(userid);
                        break;
                    //打电话
                    case 2:
                        playPhone(phone);
                        break;
                    //详情页
                    case 3:
                        Utils.intoInfo(context, userid);
                        break;
                }
            }
        });
    }

    /**
     * 拨打语音电话
     */
    protected void startVoiceCall(String username) {
        if (!EMChatManager.getInstance().isConnected()) {
            Toast.makeText(context, R.string.not_connect_to_server, Toast.LENGTH_SHORT).show();
        } else {
            context.startActivity(new Intent(context, VoiceCallActivity.class).putExtra("username", username)
                    .putExtra("isComingCall", false));
        }
    }
    private void playPhone(String phone) {
        PackageManager pm = context.getPackageManager();
        boolean permission = (PackageManager.PERMISSION_GRANTED ==
                pm.checkPermission("android.permission.CALL_PHONE", "com.example.doapp"));
        if (permission) {
            if (!TextUtils.isEmpty(phone)) {
                if (!Utils.isMobileNO(phone)) {
                    Utils.showToast("不是正确的手机号码格式", context);
                    return;
                }
                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" +
                        phone));
                context.startActivity(intent);

            } else {
                Utils.showToast("暂无号码", context);
            }
        } else {
            Utils.showToast("你还未开启电话权限", context);
        }
    }
    private OnRecyclerViewItemClickListener mOnItemClickListener = null;

    //define interface
    public static interface OnRecyclerViewItemClickListener {
        void onItemClick(View view , int data);
    }
    public void setOnItemClickListener(OnRecyclerViewItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }
}
