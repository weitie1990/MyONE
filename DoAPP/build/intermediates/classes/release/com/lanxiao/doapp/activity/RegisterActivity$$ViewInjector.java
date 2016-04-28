// Generated code from Butter Knife. Do not modify!
package com.lanxiao.doapp.activity;

import android.view.View;
import butterknife.ButterKnife.Finder;

public class RegisterActivity$$ViewInjector {
  public static void inject(Finder finder, final com.lanxiao.doapp.activity.RegisterActivity target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131624210, "field 'etRegisterPhone'");
    target.etRegisterPhone = (android.widget.EditText) view;
    view = finder.findRequiredView(source, 2131624211, "field 'etRegisterYanzhengma'");
    target.etRegisterYanzhengma = (android.widget.EditText) view;
    view = finder.findRequiredView(source, 2131624212, "field 'etRegisterGet' and method 'onClick'");
    target.etRegisterGet = (android.widget.ImageView) view;
    view.setOnClickListener(
      new android.view.View.OnClickListener() {
        @Override public void onClick(
          android.view.View p0
        ) {
          target.onClick();
        }
      });
    view = finder.findRequiredView(source, 2131624135, "field 'password'");
    target.password = (android.widget.EditText) view;
    view = finder.findRequiredView(source, 2131624204, "field 'btnRegisterNext'");
    target.btnRegisterNext = (android.widget.Button) view;
  }

  public static void reset(com.lanxiao.doapp.activity.RegisterActivity target) {
    target.etRegisterPhone = null;
    target.etRegisterYanzhengma = null;
    target.etRegisterGet = null;
    target.password = null;
    target.btnRegisterNext = null;
  }
}
