// Generated code from Butter Knife. Do not modify!
package com.lanxiao.doapp.activity;

import android.view.View;
import butterknife.ButterKnife.Finder;

public class MeetingDoActivity$$ViewInjector {
  public static void inject(Finder finder, final com.lanxiao.doapp.activity.MeetingDoActivity target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131624153, "field 'messageBack' and method 'onClick'");
    target.messageBack = (android.widget.ImageView) view;
    view.setOnClickListener(
      new android.view.View.OnClickListener() {
        @Override public void onClick(
          android.view.View p0
        ) {
          target.onClick(p0);
        }
      });
    view = finder.findRequiredView(source, 2131624131, "field 'messageTitle'");
    target.messageTitle = (android.widget.TextView) view;
    view = finder.findRequiredView(source, 2131624155, "field 'etMeetingTitle'");
    target.etMeetingTitle = (android.widget.EditText) view;
    view = finder.findRequiredView(source, 2131624157, "field 'ivMeetingTime'");
    target.ivMeetingTime = (android.widget.TextView) view;
    view = finder.findRequiredView(source, 2131624156, "field 'llMeetingTime' and method 'onClick'");
    target.llMeetingTime = (android.widget.LinearLayout) view;
    view.setOnClickListener(
      new android.view.View.OnClickListener() {
        @Override public void onClick(
          android.view.View p0
        ) {
          target.onClick(p0);
        }
      });
    view = finder.findRequiredView(source, 2131624159, "field 'ivMeetingLocation'");
    target.ivMeetingLocation = (android.widget.TextView) view;
    view = finder.findRequiredView(source, 2131624158, "field 'llMeetingLocation' and method 'onClick'");
    target.llMeetingLocation = (android.widget.LinearLayout) view;
    view.setOnClickListener(
      new android.view.View.OnClickListener() {
        @Override public void onClick(
          android.view.View p0
        ) {
          target.onClick(p0);
        }
      });
    view = finder.findRequiredView(source, 2131624161, "field 'ivMeetingJion'");
    target.ivMeetingJion = (android.widget.TextView) view;
    view = finder.findRequiredView(source, 2131624160, "field 'llMeetingJion' and method 'onClick'");
    target.llMeetingJion = (android.widget.LinearLayout) view;
    view.setOnClickListener(
      new android.view.View.OnClickListener() {
        @Override public void onClick(
          android.view.View p0
        ) {
          target.onClick(p0);
        }
      });
    view = finder.findRequiredView(source, 2131624163, "field 'ivMeetingClock'");
    target.ivMeetingClock = (android.widget.TextView) view;
    view = finder.findRequiredView(source, 2131624162, "field 'llMeetingClock' and method 'onClick'");
    target.llMeetingClock = (android.widget.LinearLayout) view;
    view.setOnClickListener(
      new android.view.View.OnClickListener() {
        @Override public void onClick(
          android.view.View p0
        ) {
          target.onClick(p0);
        }
      });
    view = finder.findRequiredView(source, 2131624164, "field 'etMeetingBeiju'");
    target.etMeetingBeiju = (android.widget.EditText) view;
    view = finder.findRequiredView(source, 2131624165, "field 'btnMeetingDo' and method 'onClick'");
    target.btnMeetingDo = (android.widget.Button) view;
    view.setOnClickListener(
      new android.view.View.OnClickListener() {
        @Override public void onClick(
          android.view.View p0
        ) {
          target.onClick(p0);
        }
      });
    view = finder.findRequiredView(source, 2131624154, "method 'onClick'");
    view.setOnClickListener(
      new android.view.View.OnClickListener() {
        @Override public void onClick(
          android.view.View p0
        ) {
          target.onClick(p0);
        }
      });
  }

  public static void reset(com.lanxiao.doapp.activity.MeetingDoActivity target) {
    target.messageBack = null;
    target.messageTitle = null;
    target.etMeetingTitle = null;
    target.ivMeetingTime = null;
    target.llMeetingTime = null;
    target.ivMeetingLocation = null;
    target.llMeetingLocation = null;
    target.ivMeetingJion = null;
    target.llMeetingJion = null;
    target.ivMeetingClock = null;
    target.llMeetingClock = null;
    target.etMeetingBeiju = null;
    target.btnMeetingDo = null;
  }
}
