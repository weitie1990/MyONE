// Generated code from Butter Knife. Do not modify!
package com.lanxiao.doapp.adapter;

import android.view.View;
import butterknife.ButterKnife.Finder;

public class PersonSignInLogAdapter$ViewHolder$$ViewInjector {
  public static void inject(Finder finder, final com.lanxiao.doapp.adapter.PersonSignInLogAdapter.ViewHolder target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131624685, "field 'signinLogPersonUserpic'");
    target.signinLogPersonUserpic = (com.lanxiao.doapp.myView.CircularImage) view;
    view = finder.findRequiredView(source, 2131624686, "field 'signinLogPersonUsername'");
    target.signinLogPersonUsername = (android.widget.TextView) view;
    view = finder.findRequiredView(source, 2131624687, "field 'signinLogPersonItemTime'");
    target.signinLogPersonItemTime = (android.widget.TextView) view;
    view = finder.findRequiredView(source, 2131624688, "field 'signinLogPersonBeiju'");
    target.signinLogPersonBeiju = (android.widget.TextView) view;
    view = finder.findRequiredView(source, 2131624689, "field 'signinLogPersonImg'");
    target.signinLogPersonImg = (android.widget.ImageView) view;
    view = finder.findRequiredView(source, 2131624690, "field 'signinLogPersonAddress'");
    target.signinLogPersonAddress = (android.widget.TextView) view;
  }

  public static void reset(com.lanxiao.doapp.adapter.PersonSignInLogAdapter.ViewHolder target) {
    target.signinLogPersonUserpic = null;
    target.signinLogPersonUsername = null;
    target.signinLogPersonItemTime = null;
    target.signinLogPersonBeiju = null;
    target.signinLogPersonImg = null;
    target.signinLogPersonAddress = null;
  }
}
