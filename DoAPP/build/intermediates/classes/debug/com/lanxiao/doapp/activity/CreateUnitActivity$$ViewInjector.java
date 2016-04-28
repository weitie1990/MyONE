// Generated code from Butter Knife. Do not modify!
package com.lanxiao.doapp.activity;

import android.view.View;
import butterknife.ButterKnife.Finder;

public class CreateUnitActivity$$ViewInjector {
  public static void inject(Finder finder, final com.lanxiao.doapp.activity.CreateUnitActivity target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131624213, "field 'etRegisterShort'");
    target.etRegisterShort = (android.widget.EditText) view;
    view = finder.findRequiredView(source, 2131624214, "field 'etRegisterFull'");
    target.etRegisterFull = (android.widget.EditText) view;
  }

  public static void reset(com.lanxiao.doapp.activity.CreateUnitActivity target) {
    target.etRegisterShort = null;
    target.etRegisterFull = null;
  }
}
