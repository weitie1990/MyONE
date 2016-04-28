// Generated code from Butter Knife. Do not modify!
package com.lanxiao.doapp.activity;

import android.view.View;
import butterknife.ButterKnife.Finder;

public class UserProfileActivity$$ViewInjector {
  public static void inject(Finder finder, final com.lanxiao.doapp.activity.UserProfileActivity target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131624180, "field 'ivUserPic'");
    target.ivUserPic = (com.lanxiao.doapp.myView.CircularImage) view;
    view = finder.findRequiredView(source, 2131624181, "field 'ivUserinfoName'");
    target.ivUserinfoName = (android.widget.TextView) view;
    view = finder.findRequiredView(source, 2131624182, "field 'ivUserinfoId'");
    target.ivUserinfoId = (android.widget.TextView) view;
    view = finder.findRequiredView(source, 2131624183, "field 'ivSettingTwocode' and method 'onClick'");
    target.ivSettingTwocode = (android.widget.ImageView) view;
    view.setOnClickListener(
      new android.view.View.OnClickListener() {
        @Override public void onClick(
          android.view.View p0
        ) {
          target.onClick(p0);
        }
      });
    view = finder.findRequiredView(source, 2131624270, "field 'tvUsersetDosum'");
    target.tvUsersetDosum = (android.widget.TextView) view;
    view = finder.findRequiredView(source, 2131624271, "field 'tvUsersetFriendsum'");
    target.tvUsersetFriendsum = (android.widget.TextView) view;
    view = finder.findRequiredView(source, 2131624272, "field 'tvUsersetAttentionsum'");
    target.tvUsersetAttentionsum = (android.widget.TextView) view;
    view = finder.findRequiredView(source, 2131624273, "field 'tvUsersetFuns'");
    target.tvUsersetFuns = (android.widget.TextView) view;
    view = finder.findRequiredView(source, 2131624274, "field 'llUsrinfoName'");
    target.llUsrinfoName = (com.lanxiao.doapp.myView.UserInfoItemView) view;
    view = finder.findRequiredView(source, 2131624275, "field 'llUsrinfoUnit'");
    target.llUsrinfoUnit = (com.lanxiao.doapp.myView.UserInfoItemView) view;
    view = finder.findRequiredView(source, 2131624276, "field 'llUsrinfoTeam'");
    target.llUsrinfoTeam = (com.lanxiao.doapp.myView.UserInfoItemView) view;
    view = finder.findRequiredView(source, 2131624280, "field 'll_usrinfo_address'");
    target.ll_usrinfo_address = (com.lanxiao.doapp.myView.UserInfoItemView) view;
    view = finder.findRequiredView(source, 2131624269, "field 'llUsrinfoSign'");
    target.llUsrinfoSign = (android.widget.TextView) view;
    view = finder.findRequiredView(source, 2131624278, "field 'llUsrinfoPhone'");
    target.llUsrinfoPhone = (android.widget.TextView) view;
    view = finder.findRequiredView(source, 2131624034, "field 'title'");
    target.title = (android.widget.RelativeLayout) view;
    view = finder.findRequiredView(source, 2131624268, "field 'iv_userinfo_logout' and method 'onClick'");
    target.iv_userinfo_logout = (android.widget.Button) view;
    view.setOnClickListener(
      new android.view.View.OnClickListener() {
        @Override public void onClick(
          android.view.View p0
        ) {
          target.onClick(p0);
        }
      });
    view = finder.findRequiredView(source, 2131624279, "method 'onClick'");
    view.setOnClickListener(
      new android.view.View.OnClickListener() {
        @Override public void onClick(
          android.view.View p0
        ) {
          target.onClick(p0);
        }
      });
  }

  public static void reset(com.lanxiao.doapp.activity.UserProfileActivity target) {
    target.ivUserPic = null;
    target.ivUserinfoName = null;
    target.ivUserinfoId = null;
    target.ivSettingTwocode = null;
    target.tvUsersetDosum = null;
    target.tvUsersetFriendsum = null;
    target.tvUsersetAttentionsum = null;
    target.tvUsersetFuns = null;
    target.llUsrinfoName = null;
    target.llUsrinfoUnit = null;
    target.llUsrinfoTeam = null;
    target.ll_usrinfo_address = null;
    target.llUsrinfoSign = null;
    target.llUsrinfoPhone = null;
    target.title = null;
    target.iv_userinfo_logout = null;
  }
}
