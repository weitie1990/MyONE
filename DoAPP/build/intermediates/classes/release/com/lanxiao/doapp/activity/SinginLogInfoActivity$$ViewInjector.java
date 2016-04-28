// Generated code from Butter Knife. Do not modify!
package com.lanxiao.doapp.activity;

import android.view.View;
import butterknife.ButterKnife.Finder;

public class SinginLogInfoActivity$$ViewInjector {
  public static void inject(Finder finder, final com.lanxiao.doapp.activity.SinginLogInfoActivity target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131624235, "field 'signinLogBack' and method 'onClick'");
    target.signinLogBack = (android.widget.ImageView) view;
    view.setOnClickListener(
      new android.view.View.OnClickListener() {
        @Override public void onClick(
          android.view.View p0
        ) {
          target.onClick(p0);
        }
      });
    view = finder.findRequiredView(source, 2131624241, "field 'tvSigninLogInfoTime' and method 'onClick'");
    target.tvSigninLogInfoTime = (android.widget.TextView) view;
    view.setOnClickListener(
      new android.view.View.OnClickListener() {
        @Override public void onClick(
          android.view.View p0
        ) {
          target.onClick(p0);
        }
      });
    view = finder.findRequiredView(source, 2131624242, "field 'tvSigninLogInfoTimeBack' and method 'onClick'");
    target.tvSigninLogInfoTimeBack = (android.widget.ImageView) view;
    view.setOnClickListener(
      new android.view.View.OnClickListener() {
        @Override public void onClick(
          android.view.View p0
        ) {
          target.onClick(p0);
        }
      });
    view = finder.findRequiredView(source, 2131624243, "field 'tvSigninLogInfoTimeNext' and method 'onClick'");
    target.tvSigninLogInfoTimeNext = (android.widget.ImageView) view;
    view.setOnClickListener(
      new android.view.View.OnClickListener() {
        @Override public void onClick(
          android.view.View p0
        ) {
          target.onClick(p0);
        }
      });
    view = finder.findRequiredView(source, 2131624244, "field 'tvSigninLogInfoMap' and method 'onClick'");
    target.tvSigninLogInfoMap = (android.widget.Button) view;
    view.setOnClickListener(
      new android.view.View.OnClickListener() {
        @Override public void onClick(
          android.view.View p0
        ) {
          target.onClick(p0);
        }
      });
    view = finder.findRequiredView(source, 2131624245, "field 'lvSigninLog'");
    target.lvSigninLog = (android.widget.ListView) view;
  }

  public static void reset(com.lanxiao.doapp.activity.SinginLogInfoActivity target) {
    target.signinLogBack = null;
    target.tvSigninLogInfoTime = null;
    target.tvSigninLogInfoTimeBack = null;
    target.tvSigninLogInfoTimeNext = null;
    target.tvSigninLogInfoMap = null;
    target.lvSigninLog = null;
  }
}
