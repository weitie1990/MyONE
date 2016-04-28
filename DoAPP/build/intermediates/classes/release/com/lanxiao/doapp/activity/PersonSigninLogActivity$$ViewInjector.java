// Generated code from Butter Knife. Do not modify!
package com.lanxiao.doapp.activity;

import android.view.View;
import butterknife.ButterKnife.Finder;

public class PersonSigninLogActivity$$ViewInjector {
  public static void inject(Finder finder, final com.lanxiao.doapp.activity.PersonSigninLogActivity target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131624175, "field 'signinLogPersonBack' and method 'onClick'");
    target.signinLogPersonBack = (android.widget.ImageView) view;
    view.setOnClickListener(
      new android.view.View.OnClickListener() {
        @Override public void onClick(
          android.view.View p0
        ) {
          target.onClick(p0);
        }
      });
    view = finder.findRequiredView(source, 2131624176, "field 'signinLogPersonStatistic' and method 'onClick'");
    target.signinLogPersonStatistic = (android.widget.TextView) view;
    view.setOnClickListener(
      new android.view.View.OnClickListener() {
        @Override public void onClick(
          android.view.View p0
        ) {
          target.onClick(p0);
        }
      });
    view = finder.findRequiredView(source, 2131624177, "field 'signinLogPersonTime'");
    target.signinLogPersonTime = (android.widget.TextView) view;
    view = finder.findRequiredView(source, 2131624178, "field 'signinLogPersonSelecttime' and method 'onClick'");
    target.signinLogPersonSelecttime = (android.widget.TextView) view;
    view.setOnClickListener(
      new android.view.View.OnClickListener() {
        @Override public void onClick(
          android.view.View p0
        ) {
          target.onClick(p0);
        }
      });
    view = finder.findRequiredView(source, 2131624179, "field 'lvSigninLogPerson'");
    target.lvSigninLogPerson = (android.widget.ListView) view;
  }

  public static void reset(com.lanxiao.doapp.activity.PersonSigninLogActivity target) {
    target.signinLogPersonBack = null;
    target.signinLogPersonStatistic = null;
    target.signinLogPersonTime = null;
    target.signinLogPersonSelecttime = null;
    target.lvSigninLogPerson = null;
  }
}
