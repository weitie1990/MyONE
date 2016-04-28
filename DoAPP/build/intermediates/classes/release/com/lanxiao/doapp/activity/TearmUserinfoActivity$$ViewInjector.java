// Generated code from Butter Knife. Do not modify!
package com.lanxiao.doapp.activity;

import android.view.View;
import butterknife.ButterKnife.Finder;

public class TearmUserinfoActivity$$ViewInjector {
  public static void inject(Finder finder, final com.lanxiao.doapp.activity.TearmUserinfoActivity target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131624341, "field 'llTeamSettingAddteam' and method 'onClick'");
    target.llTeamSettingAddteam = (com.lanxiao.doapp.myView.MainUnittemView) view;
    view.setOnClickListener(
      new android.view.View.OnClickListener() {
        @Override public void onClick(
          android.view.View p0
        ) {
          target.onClick(p0);
        }
      });
    view = finder.findRequiredView(source, 2131624340, "field 'll_team_setting_commpay'");
    target.ll_team_setting_commpay = (com.lanxiao.doapp.myView.MainUnittemView) view;
  }

  public static void reset(com.lanxiao.doapp.activity.TearmUserinfoActivity target) {
    target.llTeamSettingAddteam = null;
    target.ll_team_setting_commpay = null;
  }
}
