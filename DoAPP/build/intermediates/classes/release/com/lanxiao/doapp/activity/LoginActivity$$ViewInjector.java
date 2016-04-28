// Generated code from Butter Knife. Do not modify!
package com.lanxiao.doapp.activity;

import android.view.View;
import butterknife.ButterKnife.Finder;

public class LoginActivity$$ViewInjector {
  public static void inject(Finder finder, final com.lanxiao.doapp.activity.LoginActivity target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131624137, "field 'tvRegister' and method 'onClick'");
    target.tvRegister = (android.widget.TextView) view;
    view.setOnClickListener(
      new android.view.View.OnClickListener() {
        @Override public void onClick(
          android.view.View p0
        ) {
          target.onClick();
        }
      });
  }

  public static void reset(com.lanxiao.doapp.activity.LoginActivity target) {
    target.tvRegister = null;
  }
}
