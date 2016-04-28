package com.lanxiao.doapp.adapter;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.CalendarContract;
import android.support.v4.app.FragmentActivity;
import android.text.ClipboardManager;
import android.text.Spannable;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
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
import com.easemob.chat.EMGroup;
import com.easemob.chat.EMGroupManager;
import com.easemob.easeui.Conversion;
import com.easemob.easeui.ui.EaseBaiduMapActivity;
import com.easemob.easeui.utils.EaseSmileUtils;
import com.easemob.exceptions.EaseMobException;
import com.example.doapp.R;
import com.lanxiao.doapp.activity.ChatActivity;
import com.lanxiao.doapp.activity.ImagePagerActivity;
import com.lanxiao.doapp.activity.LocatContentActivity;
import com.lanxiao.doapp.activity.MainActivity;
import com.lanxiao.doapp.activity.MeetingWebActivity;
import com.lanxiao.doapp.activity.VoiceCallActivity;
import com.lanxiao.doapp.activity.WebActivity;
import com.lanxiao.doapp.chatui.applib.chatuimain.utils.Constant;
import com.lanxiao.doapp.chatui.applib.controller.DemoHelper;
import com.lanxiao.doapp.entity.FriendStateItem;
import com.lanxiao.doapp.entity.LikeBean;
import com.lanxiao.doapp.entity.LinkContent;
import com.lanxiao.doapp.entity.ReplyBean;
import com.lanxiao.doapp.http.Api;
import com.lanxiao.doapp.http.Json;
import com.lanxiao.doapp.myView.ActionItem;
import com.lanxiao.doapp.myView.NewTextView;
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
import com.zhy.autolayout.widget.AutoCardView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * 评论适配类
 */
public class CommentAdapter extends MBaseAdapter implements View.OnTouchListener {
    private int resourceId;
    private FragmentActivity context;
    private LayoutInflater inflater;
    private List<ReplyBean> listReply;
    private static int VIEW_TYPE1 = 0;
    private static int VIEW_TYPE2 = 1;
    private FriendStateItem fs;
    Map<Integer, ArrayList<String>> map = new HashMap<>();
    int t = 0;

    public CommentAdapter(FragmentActivity context, List<ReplyBean> list
            , int resourceId, FriendStateItem friendStateItems) {
        this.listReply = list;
        this.context = context;
        this.resourceId = resourceId;
        inflater = LayoutInflater.from(context);
        this.fs = friendStateItems;
    }

    public void DataChange(List<ReplyBean> list, FriendStateItem friendStateItems) {
        this.listReply = list;
        this.fs = friendStateItems;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        if (fs == null) {
            return listReply.size();
        }
        return listReply.size() + 1;
    }

    @Override
    public Object getItem(int position) {
        if (listReply.size() == 0) {
            return null;
        }
        return listReply.get(position - 1);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == VIEW_TYPE1) {
            return VIEW_TYPE1;
        } else {
            return VIEW_TYPE2;
        }
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        int viewType = getItemViewType(position);
        ViewHolder holder = null;
        ViewHolder1 holder1 = null;
        parent.setDescendantFocusability(ViewGroup.FOCUS_BEFORE_DESCENDANTS);
        if (viewType == 0) {
            int mPosition = position;
            if (convertView == null) {
                convertView = LayoutInflater.from(context).inflate(R.layout.listitemforattentionforall, parent, false);
                holder = new ViewHolder(convertView);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            String layOutType = fs.getType();
            List<LinkContent> list_link = fs.getLinkContent();
            List<LikeBean> likeBeanList = fs.getLikeBean();
            List<ReplyBean> replyBeanList = fs.getReplyBean();
            String html = fs.getContent();
            holder.btnSearchBtnGo.setVisibility(View.GONE);
            holder.rlLayoutFile.setVisibility(View.GONE);
            holder.llLayoutVoice.setVisibility(View.GONE);
            holder.tvEndtime.setVisibility(View.GONE);
            holder.infoname.setVisibility(View.GONE);
            holder.tvLookAlldeaita.setVisibility(View.GONE);
            holder.llAttentioniconBackLoationContent.setVisibility(View.GONE);
            holder.rlBmobHeMain.setVisibility(View.GONE);
            holder.rlSearchHeMain.setVisibility(View.GONE);
            //大体布局的几种类型
            //会议类型
            if (layOutType.equals(FriendStateItem.MEETING)) {
                holder.ivAttentionitemRightbtn.setImageResource(R.drawable.righttop3);
                holder.ivAttentionitemRightbtn.setOnClickListener(new MonClickListener(mPosition));

            } else if
                //任务类型
                    (layOutType.equals(FriendStateItem.TASK)) {
                holder.ivAttentionitemRightbtn.setImageResource(R.drawable.righttop1);
                holder.ivAttentionitemRightbtn.setOnClickListener(new MonClickListener(mPosition));


                //复杂审批类型
            } else if (layOutType.equals(FriendStateItem.ADAPPROVE)) {
                holder.ivAttentionitemRightbtn.setImageResource(R.drawable.righttop2);
                holder.ivAttentionitemRightbtn.setOnClickListener(new MonClickListener(mPosition));

            }
            //审批类型
            else if (layOutType.equals(FriendStateItem.APPROVE)) {
                holder.ivAttentionitemRightbtn.setImageResource(R.drawable.righttop2);
                holder.ivAttentionitemRightbtn.setOnClickListener(new MonClickListener(mPosition));


                //微博类型
            } else if (layOutType.equals(FriendStateItem.BLOG)) {
                holder.ivAttentionitemRightbtn.setImageResource(R.drawable.righttop4);
                holder.ivAttentionitemRightbtn.setOnClickListener(new MonClickListener(mPosition));

            }
            //有链接内容
            if (list_link != null && list_link.size() != 0) {
                LinkContent linkContent = list_link.get(0);
                if (linkContent != null) {
                    holder.rlBmobHeMain.setVisibility(View.VISIBLE);
                    holder.tvBmobDeaita.setOnClickListener(new MonClickListener(position));
                    holder.rlBmobHeMain.setOnClickListener(new MonClickListener(position));
                    String content = linkContent.getMaincontent();
                    String name = linkContent.getUser();
                    content = "转发自" + name + ": " + content;
                    //设置带表情的内容
                    // Spannable span = EaseSmileUtils.getSmiledText(context, content);
                    // 设置内容
                    // holder.tvBmobContent.setText(span, TextView.BufferType.SPANNABLE);
                    holder.tvBmobContent.setMovementMethod(LinkMovementMethod.getInstance());
                    holder.tvBmobContent.setText(addClickablePart(content, linkContent.getUserId(), name.length(), context), TextView.BufferType.SPANNABLE);
                    //转发里面的图片
                    if (!linkContent.getImages()[0].equals("http://www.dosns.net/doimg/")) {
                        holder.llBmobImages.setVisibility(View.VISIBLE);
                        final String[] images = linkContent.getImages();
                        LogUtils.i(images.length + "");
                        final ArrayList<String> datas = new ArrayList<String>();
                        for (String imageurl : images) {
                            datas.add(imageurl);
                        }
                        holder.bind2(datas);
                    }


                    if (replyBeanList != null) {
                        holder.rlSearchHeMain.setVisibility(View.VISIBLE);
                        Utils.setAver(replyBeanList.get(0).getTouxiang(), holder.ivSearchIcon);
                        showListPopulist(holder.ivSearchIcon,2);
                        Spannable span = EaseSmileUtils.getSmiledText(context, replyBeanList.get(0).getReplyContent());
                        // 设置内容
                        holder.tvSearchInfo.setText(span, TextView.BufferType.SPANNABLE);
                        holder.tvSearchData.setText(replyBeanList.get(0).getTime());

                    } else {
                        holder.rlSearchHeMain.setVisibility(View.GONE);
                    }
                    if (likeBeanList != null) {

                        holder.rlSearchHeMain.setVisibility(View.VISIBLE);
                        holder.rlSearchMeArgeeinfo.setVisibility(View.VISIBLE);
                        StringBuffer sb = new StringBuffer();
                        for (int i = 0; i < likeBeanList.size(); i++) {
                            sb.append(likeBeanList.get(i).getUserName() + " ");
                        }
                        holder.tvSearchMeArgeeinfo.setText("点赞：" + sb.toString());
                    } else {
                        holder.rlSearchMeArgeeinfo.setVisibility(View.GONE);
                    }
                }
                //无链接只有回复
            } else if (replyBeanList != null) {
                holder.rlSearchHeMain.setVisibility(View.VISIBLE);
                holder.rlSearchMeArgeeinfo.setVisibility(View.GONE);
                Utils.setAver(replyBeanList.get(0).getTouxiang(), holder.ivSearchIcon);
                showListPopulist(holder.ivSearchIcon, 2);
                Spannable span = EaseSmileUtils.getSmiledText(context, replyBeanList.get(0).getReplyContent());
                // 设置内容
                holder.tvSearchInfo.setText(span, TextView.BufferType.SPANNABLE);
                holder.tvSearchData.setText(replyBeanList.get(0).getTime());
                if (likeBeanList != null) {
                    holder.rlSearchMeArgeeinfo.setVisibility(View.VISIBLE);
                    StringBuffer sb = new StringBuffer();
                    for (int i = 0; i < likeBeanList.size(); i++) {
                        sb.append(likeBeanList.get(i).getUserName() + " ");
                    }
                    holder.tvSearchMeArgeeinfo.setText("点赞：" + sb.toString());
                } else {
                    holder.rlSearchMeArgeeinfo.setVisibility(View.GONE);
                }

            }


            //有附件时
            if (!fs.getAttachments()[0].equals("http://www.dosns.net/doattachment/")) {
                holder.rlLayoutFile.setVisibility(View.VISIBLE);
                holder.tvFileWho.setText(fs.getName() + "提交了附件，请点击查看");
                holder.ivPostImg.setOnClickListener(new MonClickListener(mPosition));
            }
            //有语音时
            if (!fs.getVideoUri().equals("")) {
                holder.llLayoutVoice.setVisibility(View.VISIBLE);
                holder.tvVoiceTime.setText("＂");
                holder.llLayoutVoice.setOnClickListener(new MonClickListener(mPosition, holder));
            }
            if (!fs.getEndTime().equals("")) {
                holder.tvEndtime.setVisibility(View.VISIBLE);
                holder.tvEndtime.setText(fs.getEndTime());
                holder.tvEndtime.setOnClickListener(new MonClickListener(mPosition));
            }

            //有图片
            if (!fs.getImages()[0].equals("http://www.dosns.net/doimg/")) {
                final String[] images = fs.getImages();
                LogUtils.i(images.length + "");
                final ArrayList<String> datas = new ArrayList<String>();
                for (String imageurl : images) {
                    datas.add(imageurl);
                }
                holder.llPostImages.setVisibility(View.VISIBLE);
                holder.bind(datas);

            }
            //有定位
            if (!TextUtils.isEmpty(fs.getLocationName())) {
                holder.llAttentioniconBackLoationContent.setVisibility(View.VISIBLE);
                holder.newsfeedpublishPoiPlace.setText(fs.getLocationName());
                holder.llAttentioniconBackLoationContent.setOnClickListener(new MonClickListener(mPosition));
            }
            holder.rlBackforapply.setVisibility(View.GONE);
            holder.rlReplyforapply.setVisibility(View.GONE);
            holder.rlAttentionitemSmile.setVisibility(View.GONE);
            //处理的是底部栏
            if (fs.getShowlike().equals("1")) {
                holder.rlBackforapply.setVisibility(View.VISIBLE);
                holder.ivBackforapplySum.setText(fs.getLikenumber());
            }
            if (fs.getShowreply().equals("1")) {
                holder.rlReplyforapply.setVisibility(View.VISIBLE);
                holder.ivReplyforapplySum.setText(fs.getReplynumber());
                holder.rlReplyforapply.setOnClickListener(new MonClickListener(mPosition));
            }
            if (fs.getShowshare().equals("1")) {
                holder.rlAttentionitemSmile.setVisibility(View.VISIBLE);
                holder.ivAttentionitemSmileSum.setText(fs.getSharenumber());
                holder.rlAttentionitemSmile.setOnClickListener(new MonClickListener(mPosition));
            }
            //处理底部操作按钮

            if (fs.getOperatebutton().equals("1")) {
                holder.btnSearchBtnGo.setVisibility(View.GONE);
                holder.llTaskHeButton.setVisibility(View.VISIBLE);
                holder.tvTaskBtninfo.setText("接受：");
            } else if (fs.getOperatebutton().equals("2")) {
                holder.btnSearchBtnGo.setVisibility(View.GONE);
                holder.llTaskHeButton.setVisibility(View.VISIBLE);
                holder.tvTaskBtninfo.setText("处理完毕：");
            } else if (fs.getOperatebutton().equals("3")) {
                holder.btnSearchBtnGo.setVisibility(View.GONE);
                holder.llTaskHeButton.setVisibility(View.VISIBLE);
                holder.tvTaskBtninfo.setText("结束：");
            } else if (fs.getOperatebutton().equals("4")) {
                holder.btnSearchBtnGo.setVisibility(View.GONE);
                holder.llTaskHeButton.setVisibility(View.VISIBLE);
                holder.tvTaskBtninfo.setText("同意：");
            } else if (fs.getOperatebutton().equals("5")) {
                holder.llTaskHeButton.setVisibility(View.GONE);
                holder.btnSearchBtnGo.setVisibility(View.VISIBLE);
                holder.btnSearchBtnGo.setOnClickListener(new MonClickListener(mPosition));
            } else if (fs.getOperatebutton().equals("6")) {
                holder.btnSearchBtnGo.setVisibility(View.GONE);
                holder.llTaskHeButton.setVisibility(View.VISIBLE);
                holder.tvTaskBtninfo.setText("参加：");
            } else {
                holder.tvTaskBtninfo.setText("");
                holder.btnSearchBtnGo.setVisibility(View.GONE);
                holder.llTaskHeButton.setVisibility(View.GONE);
            }
            holder.btnTaskBtnYes.setOnClickListener(new MonClickListener(mPosition));
            holder.btnTaskBtnNo.setOnClickListener(new MonClickListener(mPosition));
            holder.rlBackforapply.setOnClickListener(new MonClickListener(mPosition));

            if (!TextUtils.isEmpty(html)) {
                holder.infoname.setVisibility(View.VISIBLE);
                //设置带表情的内容
                Spannable span = EaseSmileUtils.getSmiledText(context, html);
                // 设置内容
                holder.infoname.setFocusable(true);
                holder.infoname.setText(span, TextView.BufferType.SPANNABLE);
                holder.root_comment.setOnLongClickListener(new MyOnLongClickListener( -1, -1));
                holder.root_comment.setOnTouchListener(this);
                //holder.infoname.setText(html);
            }
            Utils.setAver(fs.getUserIcon(), holder.ivIconforattention);
            showListPopulist(holder.ivIconforattention,1);
            holder.tvAppfordate.setText(fs.getDate());
            //holder.tv_answer.setText("对方已接收");
            holder.tvAttentionforname.setText(fs.getName());
            return convertView;
        }
        if (viewType == 1) {
            final ReplyBean bean = listReply.get(position - 1);
            if (convertView == null) {
                convertView = inflater.inflate(resourceId, parent, false);
                holder1 = new ViewHolder1();
                holder1.rl_feed_buddle = (RelativeLayout) convertView.findViewById(R.id.rl_feed_buddle);
                holder1.commentItemImg = (RoundedCornersImage)
                        convertView.findViewById(R.id.comment_profile_photo);
                holder1.commentNickname = (TextView) convertView.findViewById(R.id.feed_nickName);
                holder1.commentItemTime = (TextView)
                        convertView.findViewById(R.id.tv_comment_time);
                holder1.iv_feed_smallcircle = (ImageView) convertView.findViewById(R.id.iv_feed_smallcircle);
                holder1.ll_rely_layout_text = (TextView) convertView.findViewById(R.id.ll_rely_layout_text);
                holder1.ll_rely_layout_chat= (TextView) convertView.findViewById(R.id.ll_rely_layout_chat);

                holder1.ll_rely_layout_Image = (ImageView) convertView.findViewById(R.id.ll_rely_layout_Image);
                holder1.ll_reply_voice = (RelativeLayout) convertView.findViewById(R.id.ll_reply_voice);
                holder1.iv_reply_voice = (ImageView) convertView.findViewById(R.id.iv_reply_voice);
                holder1.pb_iv_voice = (ProgressBar) convertView.findViewById(R.id.pb_iv_voice);
                holder1.tv_location = (TextView) convertView.findViewById(R.id.tv_location);
                holder1.rl_comment_file = (RelativeLayout) convertView.findViewById(R.id.rl_comment_file);
                holder1.moment_smalldot = (ImageView) convertView.findViewById(R.id.moment_smalldot);
                holder1.fedd_clear = (LinearLayout) convertView.findViewById(R.id.fedd_clear);
                convertView.setTag(holder1);
                AutoUtils.autoSize(convertView);
            } else {
                holder1 = (ViewHolder1) convertView.getTag();

            }
            final String type = bean.getType();
            holder1.rl_feed_buddle.setOnLongClickListener(new MyOnLongClickListener(position-1,Integer.parseInt(type)));
            holder1.rl_feed_buddle.setOnTouchListener(this);
            holder1.ll_rely_layout_chat.setVisibility(View.GONE);
            holder1.ll_rely_layout_Image.setVisibility(View.GONE);
            holder1.ll_rely_layout_text.setVisibility(View.GONE);
            holder1.ll_reply_voice.setVisibility(View.GONE);
            holder1.tv_location.setVisibility(View.GONE);
            holder1.rl_comment_file.setVisibility(View.GONE);
            holder1.iv_feed_smallcircle.setVisibility(View.GONE);
            holder1.fedd_clear.setVisibility(View.GONE);
            if (position == 1) {
                holder1.iv_feed_smallcircle.setVisibility(View.VISIBLE);
                holder1.fedd_clear.setVisibility(View.VISIBLE);
            }
            //0点赞
            if (type.equals("0")) {
                holder1.moment_smalldot.setBackgroundResource(R.drawable.iv_friends_remetn);
                holder1.ll_rely_layout_text.setVisibility(View.VISIBLE);
                holder1.ll_rely_layout_text.setText(bean.getReplyContent());

            }
            //1文字
            else if (type.equals("1")) {
                holder1.ll_rely_layout_text.setVisibility(View.VISIBLE);
                //设置带表情的内容
                Spannable span = EaseSmileUtils.getSmiledText(context, bean.getReplyContent());
                // 设置内容
                holder1.ll_rely_layout_text.setText(span, TextView.BufferType.SPANNABLE);
                holder1.moment_smalldot.setBackgroundResource(R.drawable.iv_work_approve);

                //2图片
            } else if (type.equals("2")) {
                holder1.ll_rely_layout_Image.setVisibility(View.VISIBLE);
                holder1.ll_rely_layout_Image.setTag(bean.getImages()[0]);
                Utils.loadImage(bean.getImages()[0], holder1.ll_rely_layout_Image);
                holder1.moment_smalldot.setBackgroundResource(R.drawable.iv_image);
                ArrayList<String> datas = new ArrayList<>();
                datas.add(bean.getImages()[0]);
                holder1.ll_rely_layout_Image.setOnClickListener(new CommentonClickListener(datas, holder1.ll_rely_layout_Image, 0));

                //3语音
            } else if (type.equals("3")) {
                holder1.ll_reply_voice.setVisibility(View.VISIBLE);
                holder1.ll_reply_voice.setOnClickListener(new CommentonClickListener(position - 1, holder1));

                holder1.moment_smalldot.setBackgroundResource(R.drawable.reply_yuying);
                //4地图
            } else if (type.equals("4")) {
                holder1.tv_location.setVisibility(View.VISIBLE);
                holder1.tv_location.setText(bean.getGeopos());
                holder1.moment_smalldot.setBackgroundResource(R.drawable.iv_userinfo_button3);
                holder1.tv_location.setOnClickListener(new CommentonClickListener(position - 1));
                //5附件
            } else if (type.equals("5")) {
                holder1.rl_comment_file.setVisibility(View.VISIBLE);
                holder1.moment_smalldot.setBackgroundResource(R.drawable.reply_fujian);
                holder1.rl_comment_file.setOnClickListener(new CommentonClickListener(position - 1));
                //6群聊
            } else if (type.equals("6")) {
                holder1.moment_smalldot.setBackgroundResource(R.drawable.iv_maincont_friend);
                holder1.ll_rely_layout_chat.setVisibility(View.VISIBLE);
                holder1.ll_rely_layout_chat.setOnClickListener(new CommentonClickListener(position - 1));

            }
            Utils.setAver(bean.getTouxiang(), holder1.commentItemImg);
            holder1.commentItemImg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Utils.intoInfo(context, bean.getReplyAccount());
                }
            });
            holder1.commentNickname.setText(bean.getReplyNickname());
            // 用户名
            String time = bean.getTime();
           /* String [] arr=time.split(" |,");
            String data = arr[0];
            String hour = arr[1];*/
            holder1.commentItemTime.setText(time);
        }
        return convertView;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        rawX = event.getRawX();
        rawY = event.getRawY();
        return false;
    }
    float rawX;
    float rawY;
   /* public void dateChange(List<Comment> messages) {
        list = messages;
        notifyDataSetChanged();
    }*/

    private class ViewHolder1 {
        public RoundedCornersImage commentItemImg;            //评论人图片
        public TextView commentNickname;            //评论人昵称
        public TextView tv_location, tv_voice_time;                    //回复
        public TextView commentItemTime;            //评论时间
        public TextView ll_rely_layout_text,ll_rely_layout_chat;            //评论内容
        public RelativeLayout ll_reply_voice, rl_comment_file, rl_feed_buddle;
        public LinearLayout fedd_clear;
        public ImageView iv_reply_voice, moment_smalldot, iv_feed_smallcircle,ll_rely_layout_Image;
        public ProgressBar pb_iv_voice;
        public View v;
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

    private class CommentonClickListener implements View.OnClickListener {
        private int p;
        private ViewHolder1 holder1;
        private ImageView imageView;
        private ImageView singleImageView;
        private int t;
        ArrayList<String> datas;

        public CommentonClickListener(int p, ViewHolder1 holder1) {
            this.p = p;
            this.holder1 = holder1;
        }

        public CommentonClickListener(ArrayList<String> datas, ImageView imageView, int t) {
            this.datas = datas;
            this.singleImageView = imageView;
            this.t = t;
        }

        public CommentonClickListener(int p) {
            this.p = p;
        }

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                //点击查看地图
                case R.id.ll_attentionicon_backLoationContent:
                    Intent intent;
                    intent = new Intent(context, EaseBaiduMapActivity.class);
                    intent.putExtra("latitude", Double.parseDouble(fs.getLatitude()));
                    intent.putExtra("longitude", Double.parseDouble(fs.getLongitude()));
                    intent.putExtra("address", fs.getLocationName());
                    intent.putExtra("status", 1);
                    LogUtils.i("longtitude:" + fs.getLongitude() + "latitude:" + fs.getLatitude());
                    context.startActivity(intent);
                    break;
                case R.id.tv_location:
                    Intent intent1;
                    intent1 = new Intent(context, EaseBaiduMapActivity.class);
                    String geopara = listReply.get(p).getGeopara();
                    String[] ar = geopara.split(",");
                    intent1.putExtra("latitude", Double.parseDouble(ar[1]));
                    intent1.putExtra("longitude", Double.parseDouble(ar[0]));
                    intent1.putExtra("address", listReply.get(p).getGeopos());
                    intent1.putExtra("status", 1);
                    LogUtils.i("longtitude:" + fs.getLongitude() + "latitude:" + fs.getLatitude());
                    context.startActivity(intent1);
                    break;

                case R.id.iv_attentionitem_rightbtn:
                    TitlePopup popup = getButtonShos(fs.getMenu_delete(), fs.getMenu_takeback(), fs.getMenu_top());
                    popup.show(view);
                    break;
                case R.id.rl_search_he_main:

                    break;

                //看大图
                case R.id.ll_rely_layout_Image:
                    intoSpaceImageDetailActivity(datas, t);
                    break;
                //点击播放声音
                case R.id.ll_reply_voice:
                    String repvoiceUri = listReply.get(p).getSound();
                    new VoicePlayClickListener(repvoiceUri, holder1.iv_reply_voice, context.getApplication(), holder1.pb_iv_voice, p + "").onClick(view);
                    break;
                //打开文件
                case R.id.rl_comment_file:
                    //TODO 下载文件并打开
                    String fileurl = listReply.get(p).getAttachments()[0];
                    LogUtils.i("附件被点击了" + fileurl);
                    downFile(fileurl);
                    break;
                case R.id.ll_rely_layout_chat:
                    final ProgressDialog pd = new ProgressDialog(context);
                    pd.setTitle("正在加载...");
                    pd.show();
                    //进入聊天室
                    // 进入群聊
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                EMGroup group = EMGroupManager.getInstance().getGroupFromServer(listReply.get(p).getChatroomid());
                                Thread.sleep(1000);
                                List<String> stringList = group.getMembers();
                                Boolean is = false;
                                for (String s :
                                        stringList) {
                                    if (s.equals(DemoHelper.getInstance().getCurrentUsernName())) {
                                        is = true;
                                        break;
                                    }
                                }
                                if (is) {
                                    Intent intent3 = new Intent(context, ChatActivity.class);
                                    // it is group chat
                                    intent3.putExtra("chatType", Constant.CHATTYPE_GROUP);
                                    intent3.putExtra("userId", listReply.get(p).getChatroomid());
                                    context.startActivity(intent3);
                                } else {
                                    Utils.showToast("您不是该群成员,无权进入", context);
                                }
                                pd.dismiss();
                            } catch (EaseMobException e) {
                                e.printStackTrace();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }

                        }
                    }).start();
                    break;
            }
        }
    }

    /**
     * 图片查看器
     */
    private void intoSpaceImageDetailActivity(ArrayList<String> datas, int position, View iv_post_img) {
        Log.i("weitie", "点击查看大图");
        Intent intent = new Intent(context, ImagePagerActivity.class);
        intent.putExtra("image_urls", datas);//非必须
        intent.putExtra("image_index", position);
        context.startActivity(intent);
    }

    /**
     * 图片查看器
     */
    private void intoSpaceImageDetailActivity(ArrayList<String> datas, int position) {
        Log.i("weitie", "点击查看大图");
        Intent intent = new Intent(context, ImagePagerActivity.class);
        intent.putExtra("image_urls", datas);//非必须
        intent.putExtra("image_index", position);
        context.startActivity(intent);
    }

    private void downFile(String fileUrl) {
        File f = new File(fileUrl);
        final ProgressDialog pb = new ProgressDialog(context);
        pb.setTitle("正在打开,请等待...");
        pb.show();
        HttpUtils hp = new HttpUtils();
        HttpHandler httpHandler = hp.download(fileUrl, Utils.getExternalCacheDir(context) + f.getName(), false, true, new RequestCallBack<File>() {
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

    /**
     * 获取+号点击后的项目
     */
    private TitlePopup getButtonShos(String menu_delete, String menu_takeback, String menu_top) {
        //实例化标题栏弹窗
        TitlePopup titlePopup = new TitlePopup(context, getScreenWidth(context) / 2, ViewGroup.LayoutParams.WRAP_CONTENT);
        if (menu_delete.equals("1")) {
            titlePopup.addAction(new ActionItem(context, "删除", R.drawable.iconfont_delete));
        }
        if (menu_takeback.equals("1")) {
            titlePopup.addAction(new ActionItem(context, "收回", R.drawable.iconfont_filetop));
        }
        if (menu_top.equals("1")) {
            titlePopup.addAction(new ActionItem(context, "置顶", R.drawable.iconfont_transmit));
        }

        TitlePopup.OnItemOnClickListener itemOnClickListener = new TitlePopup.OnItemOnClickListener() {
            @Override
            public void onItemClick(ActionItem item, int position) {
                if (item.getmTitle().equals("删除")) {
                    if (fs.getUserId().equals("11870")) {
                        Utils.showToast("您不能删除管理员信息！", context);
                        return;
                    }
                    sendOperate(0, "2", "1");
                    return;
                }
                if (item.getmTitle().equals("收回")) {
                    sendOperate(0, "2", "2");
                    return;
                }
                if (item.getmTitle().equals("置顶")) {
                    sendOperate(0, "2", "3");
                    return;
                }

            }
        };
        titlePopup.setItemOnClickListener(itemOnClickListener);
        return titlePopup;
    }

    /**
     * 用户点是操作
     *
     * @param p
     */
    private void sendOperate(final int p, final String oprtype, final String operatebuttonvalue) {
        LogUtils.i("oprtype:" + oprtype + ",operatebuttonvalue:" + operatebuttonvalue + ",Operatebutton:" + fs.getOperatebutton());
        RequestParams params = new RequestParams("UTF-8");
        if (oprtype.equals("0")) {
            params.addBodyParameter("operatebutton", "0");
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
                fs.setDocid(friendStateItems.get(0).getDocid());
                fs.setLikeBean(friendStateItems.get(0).getLikeBean());
                fs.setLikenumber(friendStateItems.get(0).getLikenumber());
                fs.setMenu_delete(friendStateItems.get(0).getMenu_delete());
                fs.setMenu_takeback(friendStateItems.get(0).getMenu_takeback());
                fs.setMenu_top(friendStateItems.get(0).getMenu_top());
                fs.setOperatebutton(friendStateItems.get(0).getOperatebutton());
                fs.setReplyBean(friendStateItems.get(0).getReplyBean());
                fs.setReplynumber(friendStateItems.get(0).getReplynumber());
                fs.setSharenumber(friendStateItems.get(0).getSharenumber());
                fs.setShowlike(friendStateItems.get(0).getShowlike());
                fs.setShowreply(friendStateItems.get(0).getShowreply());
                fs.setShowshare(friendStateItems.get(0).getShowshare());
                if (oprtype.equals("2") && operatebuttonvalue.equals("1")) {
                    Intent intent = new Intent(context, MainActivity.class);
                    intent.putExtra("type", 2);
                    context.startActivity(intent);
                    context.finish();
                }

                resh();
                getReplyData();
            }

            @Override
            public void onFailure(HttpException e, String s) {
                LogUtils.i(s);
            }
        });
    }
    /**
     * 获取回复列表数据
     */
    private void getReplyData(){
        listReply.clear();
        RequestParams params = new RequestParams("UTF-8");
        params.addBodyParameter("mainid", fs.getDocid());
        params.addBodyParameter("userid", fs.getUserId());
        final HttpUtils httpUtils=new HttpUtils();
        httpUtils.send(HttpRequest.HttpMethod.POST, Api.Post_REPLY, params, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                LogUtils.i(responseInfo.result);
                try {
                    //解析评论的内容
                    JSONObject jsonObject = new JSONObject(responseInfo.result);
                    JSONArray ja = jsonObject.optJSONArray("replylist");
                    if (ja != null) {
                        for (int j = 0; j < ja.length(); j++) {
                            JSONObject jo_re = ja.optJSONObject(j);
                            ReplyBean rb = new ReplyBean();
                            rb.setReplyNickname(jo_re.optString("user"));
                            rb.setReply_id(jo_re.optString("replydocid"));
                            rb.setReplyAccount(jo_re.optString("userid"));
                            rb.setTouxiang(jo_re.optString("touxiang"));
                            rb.setTime(jo_re.optString("time"));
                            rb.setReplyContent(jo_re.optString("maincontent"));
                            rb.setSound(jo_re.optString("sound"));
                            rb.setGeopara(jo_re.optString("geopara"));
                            rb.setGeopos(jo_re.optString("geopos"));
                            rb.setChatroomid(jo_re.optString("chatroomid"));
                            rb.setChatroomname(jo_re.optString("chatroomname"));
                            rb.setType(jo_re.optString("type"));
                            JSONArray Images = jo_re.optJSONArray("images");
                            if (Images != null) {
                                String[] images = new String[Images.length()];
                                for (int k = 0; k < Images.length(); k++) {
                                    images[k] = Images.optString(k);
                                }
                                rb.setImages(images);
                            }
                            JSONArray jsattachments2 = jo_re.optJSONArray("attachments");
                            if (jsattachments2 != null) {
                                String[] attachments = new String[jsattachments2.length()];
                                for (int k = 0; k < jsattachments2.length(); k++) {
                                    attachments[k] = jsattachments2.optString(k);
                                }
                                rb.setAttachments(attachments);
                            }
                            listReply.add(rb);
                        }
                        resh();
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(HttpException e, String s) {

            }
        });
    }
    private void resh() {
        notifyDataSetChanged();
    }

    private class MonClickListener implements View.OnClickListener {
        private int p;
        private ViewHolder holder;
        private ImageView imageView;
        private int t;
        private String userid;
        ArrayList<String> datas;

        public MonClickListener(int p, ViewHolder holder) {
            this.p = p;
            this.holder = holder;
        }



        public MonClickListener(int p) {
            this.p = p;
        }


        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                //微啵转发链接内容
                case R.id.tv_bmob_deaita:
                    LinkContent linkContent = fs.getLinkContent().get(0);
                    if (linkContent.getLinktype().equals("web")) {
                        intoWebActivity(linkContent.getLinkaddress(), "4");
                    }
                    break;
                case R.id.rl_bmob_he_main:
                    LinkContent linkContent1 = fs.getLinkContent().get(0);
                    if (linkContent1.getLinktype().equals("web")) {
                        intoWebActivity(linkContent1.getLinkaddress(), "4");
                    }
                    break;
                //点击是否加入日程提醒
                case R.id.tv_endtime:
                    if (DateUntils.isUpNowtime(fs.getEndTime())) {
                        new AlertDialog.Builder(context).setMessage("是否加入日程提醒?").setNegativeButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                inputCalalderEVENT(p);
                            }
                        }).setNeutralButton("取消", null).show();
                    } else {
                        Utils.showToast("日程已经过期！", context);
                    }
                    break;
                //点击了转发按钮
                case R.id.rl_attentionitem_smile:
                    //进入转发界面
                    intoReaper(p);
                    break;
                case R.id.btn_task_btn_yes:
                    //发送操作请求
                    Log.i("weitie:", "yes");
                    sendOperate(p, "1", "1");
                    break;
                //点击查看全文
                case R.id.tv_look_alldeaita:
                    holder.infoname.setMaxLines(10);
                    holder.tvLookAlldeaita.setVisibility(View.GONE);
                    break;
                case R.id.btn_task_btn_no:
                    //发送操作请求
                    sendOperate(p, "1", "0");
                    break;
                //点赞
                case R.id.rl_backforapply:
                    sendOperate(p, "0", "0");
                    break;
                case R.id.iv_post_img:
                    //点击打开附件
                    String fileurl = fs.getAttachments()[0];
                    LogUtils.i("附件被点击了" + fileurl);
                    downFile(fileurl);
                    break;
                //点击播放声音
                case R.id.ll_layout_voice:
                    String voiceUri = fs.getVideoUri();
                    new VoicePlayClickListener(voiceUri, holder.ivShowAttentionMessageVoiceanmion, context, holder.pbPostVoice, p + "").onClick(view);
                    // Utils.playMusic(voiceUri,holder.iv_showAttentionMessage_voiceanmion,);
                    break;
                //点击查看地图
                case R.id.ll_attentionicon_backLoationContent:
                    Intent intent;
                    intent = new Intent(context, EaseBaiduMapActivity.class);
                    intent.putExtra("latitude", Double.parseDouble(fs.getLatitude()));
                    intent.putExtra("longitude", Double.parseDouble(fs.getLongitude()));
                    intent.putExtra("address", fs.getLocationName());
                    intent.putExtra("status", 1);
                    LogUtils.i("longtitude:" + fs.getLongitude() + "latitude:" + fs.getLatitude());

                    context.startActivity(intent);
                    break;

                case R.id.iv_attentionitem_rightbtn:
                    TitlePopup popup = getButtonShos(fs.getMenu_delete(), fs.getMenu_takeback(), fs.getMenu_top());
                    popup.show(view);
                    break;
                case R.id.btn_search_btn_go:
                    intoWebActivity(fs.getLinkContent().get(0).getLinkaddress(), "4");
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
        event.put("title", "DoAPP任务");
        event.put("description", fs.getContent());
        //插入hoohbood@gmail.com这个账户
        event.put("calendar_id", calId);
        Calendar mCalendar = Calendar.getInstance(Locale.CHINA);
        mCalendar.setTime(new Date());
        long start = mCalendar.getTime().getTime();
        String eralttiem = fs.getEndTime();
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
        String type = fs.getType();
        if (type.equals(FriendStateItem.TASK) || type.equals(FriendStateItem.MEETING) || type.equals(FriendStateItem.BLOG)) {
            Intent intent = new Intent(context, LocatContentActivity.class);
            intent.putExtra("type", type);
            LogUtils.i(fs.getDocid());
            intent.putExtra("mainid", fs.getDocid());
            context.startActivityForResult(intent, 0x22);
        } else if (type.equals(FriendStateItem.APPROVE) || type.equals(FriendStateItem.ADAPPROVE)) {
            Intent intent = new Intent(context, WebActivity.class);
            intent.putExtra("result", Api.POST_Replter_H5_test);
            context.startActivity(intent);
        }
    }
    private List<String> ShowListPropuplist(final int p,String type) {
        List<String> popupMenuItemList = new ArrayList<String>();
        switch (type) {
            case "-1":
                popupMenuItemList.add("复制");
                return popupMenuItemList;
            case "0":
                //文字消息
            case "1":
                popupMenuItemList.add("复制");
                if (canDelete(p)) {
                    popupMenuItemList.add("删除");
                }


                break;
            //图片
            case "2":
                if (canDelete(p)) {
                    popupMenuItemList.add("删除");
                } else {
                    return popupMenuItemList;
                }

                break;
            //语音
            case "3":
                final SharedPreferences sp = context.getSharedPreferences("config", Context.MODE_PRIVATE);
                final int voiceMode = sp.getInt("voicemode", 1);
                if (voiceMode == 1) {
                    popupMenuItemList.add("听筒");
                } else {
                    popupMenuItemList.add("扬声");
                }
                if (canDelete(p)) {
                    popupMenuItemList.add("删除");
                }


                break;
            //地图
            case "4":
                if (canDelete(p)) {
                    popupMenuItemList.add("删除");
                } else {
                    return popupMenuItemList;
                }

                break;
            //附件
            case "5":
                if (canDelete(p)) {
                    popupMenuItemList.add("删除");
                } else {
                    return popupMenuItemList;
                }

                break;
            //聊天室
            case "6":
                if (canDelete(p)) {
                    popupMenuItemList.add("删除");
                } else {
                    return popupMenuItemList;
                }
                break;
        }
        return popupMenuItemList;
    }
       /* new PopupList().initViewPopupList(context,p, layout, popupMenuItemList,Integer.parseInt(type), new PopupListAdapter.OnPopupListClickListener() {
            @Override
            public void onPopupListItemClick(View contextView, int contextPosition, View view, int position,int type) {
                switch (type){
                    case 0:
                    case 1:
                        switch (position) {
                            //复制消息
                            case 0:
                                ClipboardManager clipboard = (ClipboardManager)
                                        context.getSystemService(Context.CLIPBOARD_SERVICE);
                                clipboard.setText(listReply.get(contextPosition).getReplyContent());
                                break;
                            //删除消息
                            case 1:
                                DeleteMessage(contextPosition);
                                break;
                        }
                        break;
                    case 3:
                        switch (position) {
                            //复制消息
                            case 0:
                                SharedPreferences sp = context.getSharedPreferences("config", Context.MODE_PRIVATE);
                                int voiceMode = sp.getInt("voicemode", 1);
                                SharedPreferences.Editor edit = sp.edit();
                                if (voiceMode == 1) {
                                    edit.putInt("voicemode", 2);
                                    Utils.showToast("已切换到听筒播放模式", context);
                                } else {
                                    edit.putInt("voicemode", 1);
                                    Utils.showToast("已切换到扬声器播放模式", context);
                                }
                                edit.apply();
                                break;
                            //删除消息
                            case 1:
                                DeleteMessage(contextPosition);
                                break;
                        }
                        break;
                    case 2:
                    case 4:
                    case 5:
                        switch (position) {

                            //删除消息
                            case 0:
                                DeleteMessage(contextPosition);
                                break;
                        }
                        break;
                }

            }
        });*/

    public class MyOnLongClickListener implements View.OnLongClickListener {
        int p,type;
        List<String> popupMenuItemList;
        public MyOnLongClickListener(int p,int type){
            this.p=p;
            this.popupMenuItemList=ShowListPropuplist(p,type+"");
            this.type=type;
            new PopupList().setOnPopupListClickListener(new PopupListAdapter.OnPopupListClickListener() {
                @Override
                public void onPopupListItemClick(View contextView, int contextPosition, View view, int position, int type) {
                    switch (type) {
                        case -1:
                            ClipboardManager clipboard1 = (ClipboardManager)
                                    context.getSystemService(Context.CLIPBOARD_SERVICE);
                            clipboard1.setText(fs.getContent());
                            break;
                        case 0:
                        case 1:
                            switch (position) {
                                //复制消息
                                case 0:
                                    ClipboardManager clipboard = (ClipboardManager)
                                            context.getSystemService(Context.CLIPBOARD_SERVICE);
                                    clipboard.setText(listReply.get(contextPosition).getReplyContent());
                                    break;
                                //删除消息
                                case 1:
                                    DeleteMessage(contextPosition);
                                    break;
                            }
                            break;
                        case 3:
                            switch (position) {
                                //复制消息
                                case 0:
                                    SharedPreferences sp = context.getSharedPreferences("config", Context.MODE_PRIVATE);
                                    int voiceMode = sp.getInt("voicemode", 1);
                                    SharedPreferences.Editor edit = sp.edit();
                                    if (voiceMode == 1) {
                                        edit.putInt("voicemode", 2);
                                        Utils.showToast("已切换到听筒播放模式", context);
                                    } else {
                                        edit.putInt("voicemode", 1);
                                        Utils.showToast("已切换到扬声器播放模式", context);
                                    }
                                    edit.apply();
                                    break;
                                //删除消息
                                case 1:
                                    DeleteMessage(contextPosition);
                                    break;
                            }
                            break;
                        case 2:
                        case 4:
                        case 5:
                            switch (position) {

                                //删除消息
                                case 0:
                                    DeleteMessage(contextPosition);
                                    break;
                            }
                            break;
                    }

                }
            });
        }
        @Override
        public boolean onLongClick(View v) {
            if(type==3){
                SharedPreferences sp = context.getSharedPreferences("config", Context.MODE_PRIVATE);
                int voiceMode = sp.getInt("voicemode", 1);
                popupMenuItemList.remove(0);
                if (voiceMode == 1) {
                    popupMenuItemList.add(0,"听筒");
                }else {
                    popupMenuItemList.add(0,"扬声");
                }
            }
            new PopupList().showPopupWindow(context, v, p, popupMenuItemList, rawX, rawY, type);
            return true;
        }


    }
    /**
     * 气泡长按事件的对话框
     *
     * @param p
     * @param type
     */
   /* private void showDioalg(final int p, final String type) {
        final SharedPreferences sp = context.getSharedPreferences("config", Context.MODE_PRIVATE);
        final int voiceMode = sp.getInt("voicemode", 1);
        String[] items;
        if (type.equals("3")) {
            if (voiceMode == 1) {
                if (canDelete(p)) {
                    items = new String[]{"使用听筒模式", "删除消息"};
                } else {
                    items = new String[]{"使用听筒模式"};
                }
            } else {
                if (canDelete(p)) {
                    items = new String[]{"使用扬声器模式", "删除消息"};
                } else {
                    items = new String[]{"使用扬声器模式"};
                }
            }
        } else {
            if (canDelete(p)) {
                items = new String[]{"复制消息", "删除消息"};
            } else {
                items = new String[]{"复制消息"};
            }
        }
        new AlertDialog.Builder(context).setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        //复制文字信息
                        if (type.equals("0") || type.equals("1")) {
                            ClipboardManager clipboard = (ClipboardManager)
                                    context.getSystemService(Context.CLIPBOARD_SERVICE);
                            clipboard.setText(listReply.get(p).getReplyContent());
                            //切换播放语音模式
                        } else if (type.equals("3")) {
                            SharedPreferences.Editor edit = sp.edit();
                            if (voiceMode == 1) {
                                edit.putInt("voicemode", 2);
                                Utils.showToast("已切换到听筒播放模式", context);
                            } else {
                                edit.putInt("voicemode", 1);
                                Utils.showToast("已切换到杨声器播放模式", context);
                            }
                            edit.commit();
                        }

                        break;
                    case 1:
                        //删除消息
                        DeleteMessage(p);
                        break;
                }
            }
        }).show();
    }
*/
    /**
     * 是否有权限删除评论
     *
     * @param p
     * @return
     */
    private Boolean canDelete(int p) {
        Boolean canDelete = false;
        if (listReply.get(p).getReplyAccount().equals(DemoHelper.getInstance().getCurrentUsernName())
                || fs.getUserId().equals(DemoHelper.getInstance().getCurrentUsernName())) {
            canDelete = true;
        }
        return canDelete;
    }

    private void DeleteMessage(final int p) {
        LogUtils.i("listReply.get(p).getReply_id():" + listReply.get(p).getReply_id());
        RequestParams params = new RequestParams("UTF-8");
        params.addBodyParameter("replydocid", listReply.get(p).getReply_id());
        params.addBodyParameter("userid", DemoHelper.getInstance().getCurrentUsernName());
        params.addBodyParameter("mainid", fs.getDocid());
        final HttpUtils httpUtils = new HttpUtils();
        httpUtils.send(HttpRequest.HttpMethod.POST, Api.DELETEREPLY, params, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                LogUtils.i(responseInfo.result);
                try {
                    //解析评论的内容
                    JSONObject jsonObject = new JSONObject(responseInfo.result);
                    if (jsonObject.optString("result").equals("0")) {
                        Utils.showToast("删除成功", context);
                        listReply.remove(p);
                        resh();
                    } else {
                        Utils.showToast("删除失败,请重试", context);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(HttpException e, String s) {
                Utils.showToast("删除失败,请重试", context);
            }
        });
    }

    static class ViewHolder {
        @InjectView(R.id.iv_iconforattention)
        RoundedCornersImage ivIconforattention;
        @InjectView(R.id.tv_attentionforname)
        TextView tvAttentionforname;
        @InjectView(R.id.tv_appfordate)
        TextView tvAppfordate;
        @InjectView(R.id.iv_attentionitem_funion)
        ImageView ivAttentionitemFunion;
        @InjectView(R.id.iv_attentionitem_rightbtn)
        ImageView ivAttentionitemRightbtn;
        @InjectView(R.id.ll_attentionicon)
        RelativeLayout llAttentionicon;
        @InjectView(R.id.infoname)
        NewTextView infoname;
        @InjectView(R.id.tv_look_alldeaita)
        TextView tvLookAlldeaita;
        @InjectView(R.id.iv_post_img)
        ImageView ivPostImg;
        @InjectView(R.id.tv_file_who)
        TextView tvFileWho;
        @InjectView(R.id.rl_layout_file)
        RelativeLayout rlLayoutFile;
        @InjectView(R.id.ll_post_images)
        NineGridImageView llPostImages;
        @InjectView(R.id.tv_attentionitme_jieshou)
        TextView tvAttentionitmeJieshou;
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
        @InjectView(R.id.tv_bmob_content)
        TextView tvBmobContent;
        @InjectView(R.id.ll_bmob_images)
        NineGridImageView llBmobImages;
        @InjectView(R.id.tv_bmob_deaita)
        TextView tvBmobDeaita;
        @InjectView(R.id.rl_bmob_he_main)
        LinearLayout rlBmobHeMain;
        @InjectView(R.id.iv_search_icon)
        RoundedCornersImage ivSearchIcon;
        @InjectView(R.id.tv_search_info)
        TextView tvSearchInfo;
        @InjectView(R.id.tv_search_data)
        TextView tvSearchData;
        @InjectView(R.id.tv_search_deaita)
        TextView tvSearchDeaita;
        @InjectView(R.id.tv_search_me_replyinfo)
        TextView tvSearchMeReplyinfo;
        @InjectView(R.id.rl_search_me_replyinfo)
        RelativeLayout rlSearchMeReplyinfo;
        @InjectView(R.id.tv_search_me_argeeinfo)
        TextView tvSearchMeArgeeinfo;
        @InjectView(R.id.rl_search_me_argeeinfo)
        RelativeLayout rlSearchMeArgeeinfo;
        @InjectView(R.id.rl_search_he_main)
        RelativeLayout rlSearchHeMain;
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
        @InjectView(R.id.iv_backforapply)
        ImageView ivBackforapply;
        @InjectView(R.id.iv_backforapply_sum)
        TextView ivBackforapplySum;
        @InjectView(R.id.rl_backforapply)
        RelativeLayout rlBackforapply;
        @InjectView(R.id.iv_replyforapply)
        ImageView ivReplyforapply;
        @InjectView(R.id.iv_replyforapply_sum)
        TextView ivReplyforapplySum;
        @InjectView(R.id.rl_replyforapply)
        RelativeLayout rlReplyforapply;
        @InjectView(R.id.iv_attentionitem_smile)
        ImageView ivAttentionitemSmile;
        @InjectView(R.id.iv_attentionitem_smile_sum)
        TextView ivAttentionitemSmileSum;
        @InjectView(R.id.rl_attentionitem_smile)
        RelativeLayout rlAttentionitemSmile;
        @InjectView(R.id.tv_attentionitem_replysum)
        TextView tvAttentionitemReplysum;
        @InjectView(R.id.ll_attentlayout_button)
        LinearLayout llAttentlayoutButton;
        @InjectView(R.id.root_comment)
        AutoCardView root_comment;
        private NineGridImageViewAdapter<String> mAdapter = new NineGridImageViewAdapter<String>() {
            @Override
            protected void onDisplayImage(Context context, ImageView imageView, String s) {
                Picasso.with(context)
                        .load(s)
                        .resize(AutoUtils.getPercentWidthSize(200), AutoUtils.getPercentWidthSize(200))
                        .centerCrop().config(Bitmap.Config.RGB_565)
                        .into(imageView);
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


        public void bind(ArrayList<String> datas) {
            llPostImages.setImagesData(datas);
        }
        public void bind2(ArrayList<String> datas) {
            llBmobImages.setImagesData(datas);
        }
        ViewHolder(View view) {
            ButterKnife.inject(this, view);
            AutoUtils.autoSize(view);
            llPostImages.setAdapter(mAdapter);
            llBmobImages.setAdapter(mAdapter);
        }
    }
    /**
     * 点击头像弹出对话框
     * @param layout
     * @param type
     */
    private void showListPopulist(ImageView layout,int type) {
        if(type==1){
            if(fs.getUserId().equals(DemoHelper.getInstance().getCurrentUsernName())){
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
            if (fs.getReplyBean().get(0).getReply_id().equals(DemoHelper.getInstance().getCurrentUsernName())){
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
        new PopupList().initViewPopupList(context, 0, layout, popupMenuItemList, type, new PopupListAdapter.OnPopupListClickListener() {
            @Override
            public void onPopupListItemClick(View contextView, int contextPosition, View view, int position, int type) {
                LogUtils.i("Main");
                String userid = null;
                String phone = null;
                if (type == 1) {
                    userid = fs.getUserId();
                    phone = fs.getCellphone();
                } else if (type == 2) {
                    userid = fs.getReplyBean().get(0).getReply_id();
                    phone = fs.getReplyBean().get(0).getReplyCellphone();
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
                    Utils.showToast("不是正确的手机号码格式", context.getApplication());
                    return;
                }
                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" +
                        phone));
                context.startActivity(intent);

            } else {
                Utils.showToast("暂无号码", context.getApplication());
            }
        } else {
            Utils.showToast("你还未开启电话权限", context.getApplication());
        }
    }
}
