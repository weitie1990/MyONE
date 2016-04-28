// Generated code from Butter Knife. Do not modify!
package com.lanxiao.doapp.activity;

import android.view.View;
import butterknife.ButterKnife.Finder;

public class TearmSumActivity$$ViewInjector {
  public static void inject(Finder finder, final com.lanxiao.doapp.activity.TearmSumActivity target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131624341, "field 'llTeamSettingAddteam' and method 'onClick'");
    target.llTeamSettingAddteam = (com.lanxiao.doapp.myView.MainUnittemView) view;
    view.setOnClickListener(
      new android.view.View.OnClickListener() {
        @Override public void onClick(
          android.view.View p0
        ) {
          target.onClick();
        }
      });
  }

  public static void reset(com.lanxiao.doapp.activity.TearmSumActivity target) {
    target.llTeamSettingAddteam = null;
  }
}
