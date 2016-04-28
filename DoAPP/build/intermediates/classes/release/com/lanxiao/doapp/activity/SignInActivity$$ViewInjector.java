// Generated code from Butter Knife. Do not modify!
package com.lanxiao.doapp.activity;

import android.view.View;
import butterknife.ButterKnife.Finder;

public class SignInActivity$$ViewInjector {
  public static void inject(Finder finder, final com.lanxiao.doapp.activity.SignInActivity target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131624225, "field 'signinBack' and method 'onClick'");
    target.signinBack = (android.widget.ImageView) view;
    view.setOnClickListener(
      new android.view.View.OnClickListener() {
        @Override public void onClick(
          android.view.View p0
        ) {
          target.onClick(p0);
        }
      });
    view = finder.findRequiredView(source, 2131624226, "field 'currentlocationItemIcon'");
    target.currentlocationItemIcon = (android.widget.ImageView) view;
    view = finder.findRequiredView(source, 2131624227, "field 'currentlocationItemName'");
    target.currentlocationItemName = (android.widget.TextView) view;
    view = finder.findRequiredView(source, 2131624228, "field 'currentlocationItemCount'");
    target.currentlocationItemCount = (android.widget.TextView) view;
    view = finder.findRequiredView(source, 2131624229, "field 'ivSigninCinema' and method 'onClick'");
    target.ivSigninCinema = (android.widget.ImageView) view;
    view.setOnClickListener(
      new android.view.View.OnClickListener() {
        @Override public void onClick(
          android.view.View p0
        ) {
          target.onClick(p0);
        }
      });
    view = finder.findRequiredView(source, 2131624230, "field 'iv_signin_userpic'");
    target.iv_signin_userpic = (android.widget.ImageView) view;
    view = finder.findRequiredView(source, 2131624231, "field 'etSigninBeiju'");
    target.etSigninBeiju = (android.widget.EditText) view;
    view = finder.findRequiredView(source, 2131624232, "field 'rbSigninClient' and method 'onClick'");
    target.rbSigninClient = (android.widget.RadioButton) view;
    view.setOnClickListener(
      new android.view.View.OnClickListener() {
        @Override public void onClick(
          android.view.View p0
        ) {
          target.onClick(p0);
        }
      });
    view = finder.findRequiredView(source, 2131624233, "field 'rbSigninWork' and method 'onClick'");
    target.rbSigninWork = (android.widget.RadioButton) view;
    view.setOnClickListener(
      new android.view.View.OnClickListener() {
        @Override public void onClick(
          android.view.View p0
        ) {
          target.onClick(p0);
        }
      });
    view = finder.findRequiredView(source, 2131624234, "field 'btnSiginOk' and method 'onClick'");
    target.btnSiginOk = (android.widget.Button) view;
    view.setOnClickListener(
      new android.view.View.OnClickListener() {
        @Override public void onClick(
          android.view.View p0
        ) {
          target.onClick(p0);
        }
      });
  }

  public static void reset(com.lanxiao.doapp.activity.SignInActivity target) {
    target.signinBack = null;
    target.currentlocationItemIcon = null;
    target.currentlocationItemName = null;
    target.currentlocationItemCount = null;
    target.ivSigninCinema = null;
    target.iv_signin_userpic = null;
    target.etSigninBeiju = null;
    target.rbSigninClient = null;
    target.rbSigninWork = null;
    target.btnSiginOk = null;
  }
}
