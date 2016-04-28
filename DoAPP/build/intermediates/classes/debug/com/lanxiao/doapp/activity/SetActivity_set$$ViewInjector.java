// Generated code from Butter Knife. Do not modify!
package com.lanxiao.doapp.activity;

import android.view.View;
import butterknife.ButterKnife.Finder;

public class SetActivity_set$$ViewInjector {
  public static void inject(Finder finder, final com.lanxiao.doapp.activity.SetActivity_set target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131624131, "field 'messageTitle'");
    target.messageTitle = (android.widget.TextView) view;
    view = finder.findRequiredView(source, 2131624180, "field 'ivUserPic' and method 'onClick'");
    target.ivUserPic = (com.lanxiao.doapp.myView.CircularImage) view;
    view.setOnClickListener(
      new android.view.View.OnClickListener() {
        @Override public void onClick(
          android.view.View p0
        ) {
          target.onClick(p0);
        }
      });
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
    view = finder.findRequiredView(source, 2131624530, "field 'llSettingPersonuserinfo' and method 'onClick'");
    target.llSettingPersonuserinfo = (com.lanxiao.doapp.myView.MainSetItemView) view;
    view.setOnClickListener(
      new android.view.View.OnClickListener() {
        @Override public void onClick(
          android.view.View p0
        ) {
          target.onClick(p0);
        }
      });
    view = finder.findRequiredView(source, 2131624531, "field 'llSettingUnit' and method 'onClick'");
    target.llSettingUnit = (com.lanxiao.doapp.myView.MainSetItemView) view;
    view.setOnClickListener(
      new android.view.View.OnClickListener() {
        @Override public void onClick(
          android.view.View p0
        ) {
          target.onClick(p0);
        }
      });
    view = finder.findRequiredView(source, 2131624532, "field 'llSettingTeam' and method 'onClick'");
    target.llSettingTeam = (com.lanxiao.doapp.myView.MainSetItemView) view;
    view.setOnClickListener(
      new android.view.View.OnClickListener() {
        @Override public void onClick(
          android.view.View p0
        ) {
          target.onClick(p0);
        }
      });
    view = finder.findRequiredView(source, 2131624533, "field 'llSettingPost' and method 'onClick'");
    target.llSettingPost = (com.lanxiao.doapp.myView.MainSetItemView) view;
    view.setOnClickListener(
      new android.view.View.OnClickListener() {
        @Override public void onClick(
          android.view.View p0
        ) {
          target.onClick(p0);
        }
      });
    view = finder.findRequiredView(source, 2131624534, "field 'llSettingGeneralSetting' and method 'onClick'");
    target.llSettingGeneralSetting = (com.lanxiao.doapp.myView.MainSetItemView) view;
    view.setOnClickListener(
      new android.view.View.OnClickListener() {
        @Override public void onClick(
          android.view.View p0
        ) {
          target.onClick(p0);
        }
      });
    view = finder.findRequiredView(source, 2131624535, "field 'llSettingSafeSetting' and method 'onClick'");
    target.llSettingSafeSetting = (com.lanxiao.doapp.myView.MainSetItemView) view;
    view.setOnClickListener(
      new android.view.View.OnClickListener() {
        @Override public void onClick(
          android.view.View p0
        ) {
          target.onClick(p0);
        }
      });
    view = finder.findRequiredView(source, 2131624536, "field 'llSettingHelp' and method 'onClick'");
    target.llSettingHelp = (com.lanxiao.doapp.myView.MainSetItemView) view;
    view.setOnClickListener(
      new android.view.View.OnClickListener() {
        @Override public void onClick(
          android.view.View p0
        ) {
          target.onClick(p0);
        }
      });
  }

  public static void reset(com.lanxiao.doapp.activity.SetActivity_set target) {
    target.messageTitle = null;
    target.ivUserPic = null;
    target.ivUserinfoName = null;
    target.ivUserinfoId = null;
    target.ivSettingTwocode = null;
    target.tvUsersetDosum = null;
    target.tvUsersetFriendsum = null;
    target.tvUsersetAttentionsum = null;
    target.tvUsersetFuns = null;
    target.llSettingPersonuserinfo = null;
    target.llSettingUnit = null;
    target.llSettingTeam = null;
    target.llSettingPost = null;
    target.llSettingGeneralSetting = null;
    target.llSettingSafeSetting = null;
    target.llSettingHelp = null;
  }
}
