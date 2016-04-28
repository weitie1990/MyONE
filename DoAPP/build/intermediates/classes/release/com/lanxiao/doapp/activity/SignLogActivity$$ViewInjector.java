// Generated code from Butter Knife. Do not modify!
package com.lanxiao.doapp.activity;

import android.view.View;
import butterknife.ButterKnife.Finder;

public class SignLogActivity$$ViewInjector {
  public static void inject(Finder finder, final com.lanxiao.doapp.activity.SignLogActivity target, Object source) {
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
    view = finder.findRequiredView(source, 2131624236, "field 'llSigninLogTeam' and method 'onClick'");
    target.llSigninLogTeam = (android.widget.LinearLayout) view;
    view.setOnClickListener(
      new android.view.View.OnClickListener() {
        @Override public void onClick(
          android.view.View p0
        ) {
          target.onClick(p0);
        }
      });
    view = finder.findRequiredView(source, 2131624237, "field 'llSigninLogMy' and method 'onClick'");
    target.llSigninLogMy = (android.widget.LinearLayout) view;
    view.setOnClickListener(
      new android.view.View.OnClickListener() {
        @Override public void onClick(
          android.view.View p0
        ) {
          target.onClick(p0);
        }
      });
    view = finder.findRequiredView(source, 2131624238, "field 'tvSigninLogTv1'");
    target.tvSigninLogTv1 = (android.widget.TextView) view;
    view = finder.findRequiredView(source, 2131624239, "field 'tvSigninLogTv2'");
    target.tvSigninLogTv2 = (android.widget.TextView) view;
    view = finder.findRequiredView(source, 2131624240, "field 'tvSigninLogTv3'");
    target.tvSigninLogTv3 = (android.widget.TextView) view;
  }

  public static void reset(com.lanxiao.doapp.activity.SignLogActivity target) {
    target.signinLogBack = null;
    target.llSigninLogTeam = null;
    target.llSigninLogMy = null;
    target.tvSigninLogTv1 = null;
    target.tvSigninLogTv2 = null;
    target.tvSigninLogTv3 = null;
  }
}
