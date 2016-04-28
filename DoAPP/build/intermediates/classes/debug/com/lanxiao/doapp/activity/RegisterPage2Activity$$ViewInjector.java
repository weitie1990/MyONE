// Generated code from Butter Knife. Do not modify!
package com.lanxiao.doapp.activity;

import android.view.View;
import butterknife.ButterKnife.Finder;

public class RegisterPage2Activity$$ViewInjector {
  public static void inject(Finder finder, final com.lanxiao.doapp.activity.RegisterPage2Activity target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131624215, "field 'etRegisterNickname'");
    target.etRegisterNickname = (android.widget.EditText) view;
    view = finder.findRequiredView(source, 2131624216, "field 'etRegisterUtil'");
    target.etRegisterUtil = (android.widget.EditText) view;
  }

  public static void reset(com.lanxiao.doapp.activity.RegisterPage2Activity target) {
    target.etRegisterNickname = null;
    target.etRegisterUtil = null;
  }
}
