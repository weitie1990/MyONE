// Generated code from Butter Knife. Do not modify!
package com.lanxiao.doapp.adapter;

import android.view.View;
import butterknife.ButterKnife.Finder;

public class PersonSignInLogAdapter$ViewHolder$$ViewInjector {
  public static void inject(Finder finder, final com.lanxiao.doapp.adapter.PersonSignInLogAdapter.ViewHolder target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131624678, "field 'signinLogPersonUserpic'");
    target.signinLogPersonUserpic = (com.lanxiao.doapp.myView.CircularImage) view;
    view = finder.findRequiredView(source, 2131624679, "field 'signinLogPersonUsername'");
    target.signinLogPersonUsername = (android.widget.TextView) view;
    view = finder.findRequiredView(source, 2131624680, "field 'signinLogPersonItemTime'");
    target.signinLogPersonItemTime = (android.widget.TextView) view;
    view = finder.findRequiredView(source, 2131624681, "field 'signinLogPersonBeiju'");
    target.signinLogPersonBeiju = (android.widget.TextView) view;
    view = finder.findRequiredView(source, 2131624682, "field 'signinLogPersonImg'");
    target.signinLogPersonImg = (android.widget.ImageView) view;
    view = finder.findRequiredView(source, 2131624683, "field 'signinLogPersonAddress'");
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
