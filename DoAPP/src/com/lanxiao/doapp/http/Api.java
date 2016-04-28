package com.lanxiao.doapp.http;

/**
 * Created by Thinkpad on 2016/1/5.
 */
public class Api {
    //apk的下载地址
    public static final String DOWNLOADAPK="http://www.dosns.net/modules/doinglist/versionandroid.aspx";
    //请求doing界面每条信息的JSON
    public static final String POST_DOLIST="http://www.dosns.net/modules/doinglist/doinglist.aspx";
    //提交每条信息的URL
    public static final String POST_DO_SUBMIT="http://www.dosns.net/modules/doinglist/save.aspx";
    //访问h5界面的url
    public static final String INTO_H5="http://www.dosns.net/modules/apptest/save.aspx";
    //微信登陆的url
    public static final String UP_WEIXIN_INFO="http://www.dosns.net/modules/doinglist/verifyuser.aspx";
    //查找朋友的URL
    public static final String SEARCH_FRIEND="http://www.dosns.net/modules/friend/search.aspx";
    //添加朋友
    public static final String ADD_CONTENT="http://www.dosns.net/modules/friend/addfriend.aspx";
    //查找所有朋友
    public static final String SEARCH_ALL_FRIEND="http://www.dosns.net/modules/friend/userinfo.aspx";
    //查找所有同事
    public static final String SEARCH_ALL_TONGSHI="http://www.dosns.net/modules/doinglist/companyuserlist.aspx";
    //微信转发给朋友的状态
    public static final String WEIXIN_STATAUE_SUBMIT="http://www.dosns.net/modules/doinglist/webchatstatus.aspx";
    //用户注册
    public static final String DEAUFULT_REGISTER="http://www.dosns.net/modules/appuser/createuser.aspx";
    //用户登陆
    public static final String DEAUFULT_LOGIN="http://www.dosns.net/modules/appuser/applogin.aspx";
    //创建单位
    public static final String CREATE_COMPANY="http://www.dosns.net/modules/appuser/createcompany.aspx";
    //创建部门
    public static final String CREATE_PARTMENT="http://www.dosns.net/modules/appuser/createdepartment.aspx";
    //查看用户个人设置信息
    public static final String USER_INFO="http://www.dosns.net/modules/appuser/userinfo.aspx";
    //更新用户个人信息
    public static final String UPDATAUSER ="http://www.dosns.net/modules/appuser/updateuser.aspx";
    //查看用户个人设置信息
    public static final String USER_PERSON_INFO="http://www.dosns.net/modules/appuser/userdetail.aspx";
    //查看公司信息
    public static final String COMPANY_INFO="http://www.dosns.net/modules/appuser/companydetail.aspx";
    //修改公司设置信息
    public static final String UPDATA_COMPANY_INFO="http://www.dosns.net/modules/appuser/updatecompany.aspx";
    //获取部门信息
    public static final String Team_INFO="http://www.dosns.net/modules/appuser/deptlist.aspx";
    //接受每条信息的评论
    public static final String Post_REPLY="http://www.dosns.net/modules/doreply/replylist.aspx";
    //删除每条信息的评论
    public static final String DELETEREPLY="http://www.dosns.net/modules/doreply/deletereply.aspx";
    //发表每条信息的评论
    public static final String PUISH_REPLY="http://www.dosns.net/modules/doreply/save.aspx";
    //每条信息操作的提交
    public static final String POST_OPERATE="http://www.dosns.net/modules/doinglist/dooperate.aspx";
    //转发提交
    public static final String POST_Replter="http://www.dosns.net/modules/doinglist/doforward.aspx";
    //事项转发(H5)
    public static final String POST_Replter_H5="http://www.dosns.net/modules/doinglist/mainform.aspx?id=11&opr=forward";
    //事项转发(H5)测试地址
    public static final String POST_Replter_H5_test="http://www.dosns.net/modules/apptest/form.htm";
    //会议提交接口
    public static final String MEETING_SUMMIT="http://www.dosns.net/modules/meeting/save.aspx";
    //签到交接口
    public static final String SIGNIN_SUMMIT="http://www.dosns.net/modules/sign/save.aspx";
    //会议接口
    public static final String MEETING_SEARCH ="http://www.dosns.net/modules/meeting/ListViews.aspx?userid=";
    //会议接口
    public static final String LEAVE ="http://www.dosns.net/modules/leaveapp/listviews.aspx?userid=";
    //出差
    public static final String CHUCHAIDUANJ ="http://www.dosns.net/modules/chuchaiduanj/ListViews.aspx?userid=";
    //物品领用
    public static final String WUPINXUQIUSHENQING ="http://www.dosns.net/modules/wupinxuqiushenqing/ListViews.aspx?userid=";
    //网上投票
    public static final String WANGSHANGTOUPIAO ="http://www.dosns.net/modules/wangshangtoupiao/toupiao.html?pid=2?userid=";
    //报销
    public static final String BIAOXIAO ="http://www.dosns.net/modules/VGO_BXFXM/ListViews.aspx?userid=";
    //通用审批
    public static final String SHENGPI ="http://www.dosns.net/modules/shiwu/ListViews.aspx?userid=";

    public static final String SIGNIN_SEARCH ="http://www.dosns.net/modules/sign/ListViews.aspx?userid=";
    //搜索公司接口
    public static final String SEARCH_COMPANY ="http://www.dosns.net/modules/appuser/searchcompany.aspx";
    //推荐人的数据
    public static final String SEARCH_RECOMMENT ="http://www.dosns.net/modules/friend/recommend.aspx";
    //关注接口
    public static final String ATTENTION ="http://www.dosns.net/modules/friend/follow.aspx";
    //心情显示接口
    public static final String USERMOOD ="http://www.dosns.net/modules/appuser/usermood.aspx";
    //心情显示接口
    public static final String UPDATEUSERHEADLOGO ="http://www.dosns.net/modules/doinglist/saveheadlogo.aspx";
    //查看他人用户详细信息
    public static final String FRIENDUSERINFO ="http://www.dosns.net/modules/appuser/userinfoview.aspx";
    //微啵转发内容的url
    public static final String BMOBREPLER ="http://www.dosns.net/modules/doinglist/targetdoing.aspx";

    //万科接口
    public static final String WANKE ="http://www.dosns.net/modules/yuanqu/vanke.aspx";
    //御河
    public static final String YUHE ="http://www.dosns.net/modules/yuanqu/yuhe.aspx";
    //智能园区
    public static final String ZHINENG ="http://www.dosns.net/modules/yuanqu/zhineng.aspx";
    //联通
    public static final String LIANTONG ="http://www.dosns.net/modules/yuanqu/liantong.aspx";
    //关于园区
    public static final String ABOUTYQ ="http://www.dosns.net/modules/yuanqu/aboutyq.aspx";
    //工作界面每项数据的请求
    public static final String WORKITEM ="http://www.dosns.net/modules/doinglist/worklist.aspx";
    //应用界面每项数据的请求
    public static final String APPLYITEM ="http://www.dosns.net/modules/doinglist/applist.aspx";
}
