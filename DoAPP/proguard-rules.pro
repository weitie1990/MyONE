

-optimizationpasses 5          # 指定代码的压缩级别
-dontusemixedcaseclassnames   # 是否使用大小写混合
-dontpreverify           # 混淆时是否做预校验
-verbose                # 混淆时是否记录日志
-optimizations !code/simplification/arithmetic,!field/*,!class/merging/*  # 混淆时所采用的算法
-keep public class * extends android.app.Application   # 保持哪些类不被混淆
-keep public class * extends android.app.Service       # 保持哪些类不被混淆
-keep public class * extends android.content.BroadcastReceiver  # 保持哪些类不被混淆
-keep public class * extends android.content.ContentProvider    # 保持哪些类不被混淆
-keep public class * extends android.app.backup.BackupAgentHelper # 保持哪些类不被混淆
-keep public class * extends android.preference.Preference        # 保持哪些类不被混淆
-keep public class com.android.vending.licensing.ILicensingService    # 保持哪些类不被混淆
# 如果有引用v4包可以添加下面这行
-keep public class * extends android.support.v4.app.Fragment
    -dontwarn android.support.**
    #显示行号和源文件错误信息
    -keepattributes SourceFile,LineNumberTable
# 忽略警告
-ignorewarning
-keepclasseswithmembernames class * {  # 保持 native 方法不被混淆
    native <methods>;
}
-keepclasseswithmembers class * {   # 保持自定义控件类不被混淆
    public <init>(android.content.Context, android.util.AttributeSet);
}
-keepclasseswithmembers class * {# 保持自定义控件类不被混淆
    public <init>(android.content.Context, android.util.AttributeSet, int);
}
-keepclassmembers class * extends android.app.Activity {  # 保持自定义控件类不被混淆
    public void *(android.view.View);
}
-keepclassmembers enum * {     # 保持枚举 enum 类不被混淆
 public static **[] values();
 public static ** valueOf(java.lang.String);
}
-keep class * implements android.os.Parcelable { # 保持 Parcelable 不被混淆
 public static final android.os.Parcelable$Creator *;
}

-keep class com.easemob.** {*;}
-keep class org.jivesoftware.** {*;}
-keep class org.apache.** {*;}
-dontwarn  com.easemob.**

#-keep class com.example.utils.SmileUtils {*;}
#如果使用easeui库，需要这么写
-keep class com.easemob.easeui.utils.EaseSmileUtils {*;}

#2.0.9后加入语音通话功能，如需使用此功能的api，加入以下keep
-dontwarn ch.imvs.**
-dontwarn org.slf4j.**
-keep class org.ice4j.** {*;}
-keep class net.java.sip.** {*;}
-keep class org.webrtc.voiceengine.** {*;}
-keep class org.bitlet.** {*;}
-keep class org.slf4j.** {*;}
-keep class ch.imvs.** {*;}
#百度地图的混淆
-keep class com.baidu.** { *; }
-keep class vi.com.gdi.bgl.android.** {*;}
-keep class com.baidu.location.** {*;}
#google service的混淆
-keep class com.google.android.gms.** {*;}
#apache.ttp的混xiao
-keep class com.android.internal.http.multipart.** {*;}
#pinyin
-keep class com.hp.hpl.sparta.** {*;}
-keep class net.sourceforge.pinyin4j.** {*;}
#image-loader
-keep class com.nostra13.universalimageloader.** {*;}
#xutils
-keep class com.lidroid.xutils.** {*;}
-keep class com.lidroid.xutils.db.table.** {*;}
-keep class com.lidroid.xutils.util.** {*;}
#zxing
-keep class com.google.zxing.** {*;}
#Parse
-keep class com.parse.** {*;}
#Parse
-keep class com.squareup.picasso.** {*;}
#weixinsdk
-keep class com.tencent.** {*;}
#自己项目包的实体类
-keep class com.lanxiao.doapp.entity.** {*;}
-keep class com.zhy.autolayout.** {*;}
#butterknife
-keep class butterknife.** { *; }
-dontwarn butterknife.internal.**
-keep class **$$ViewInjector { *; }

-keepclasseswithmembernames class * {
    @butterknife.* <fields>;
}
-keepclasseswithmembernames class * {
    @butterknife.* <methods>;
}
#umeng
-keepclassmembers class * {
   public <init> (org.json.JSONObject);
}
-keep public class [com.example.doapp].R$*{
public static final int *;
}
-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}
# MPermissions
-dontwarn com.zhy.m.**
-keep class com.zhy.m.** {*;}
-keep interface com.zhy.m.** { *; }
-keep class **$$PermissionProxy { *; }