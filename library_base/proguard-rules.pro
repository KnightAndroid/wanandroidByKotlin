# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile
-keepattributes *Annotation*
-keepclassmembers class * {
    @org.greenrobot.eventbus.Subscribe <methods>;
}
-keep enum org.greenrobot.eventbus.ThreadMode { *; }

# And if you use AsyncExecutor:
-keepclassmembers class * extends org.greenrobot.eventbus.util.ThrowableFailureEvent {
    <init>(java.lang.Throwable);
}

#-keep public class com.alibaba.android.arouter.routes.**{*;}
#-keep public class com.alibaba.android.arouter.facade.**{*;}
#-keep class * implements com.alibaba.android.arouter.facade.template.ISyringe{*;}
-keepclassmembers class * {
    @com.wyjson.router.annotation.Param <fields>;
}

# 避免 ViewBinding 类被混淆导致反射初始化失败
-keep public interface androidx.viewbinding.ViewBinding
-keepclassmembers class * implements androidx.viewbinding.ViewBinding{
    *;
}
# 加载视图混淆
-dontwarn com.kingja.loadsir.**
-keep class com.kingja.loadsir.** {*;}

-keepclassmembers class * {
    @com.knight.kotlin.library_base.network.interfaces.NetworkMonitor <methods>;
}

# 错误日志收集
-dontwarn com.tencent.bugly.**
-keep public class com.tencent.bugly.**{*;}

# 百度地图
-keep class com.baidu.** {*;}
-keep class vi.com.** {*;}
-keep class com.baidu.vi.** {*;}
-dontwarn com.baidu.**

# 全局作用于各library 单独各自写在库的混淆文件不起作用
## https://stackoverflow.com/questions/77143071/r8-and-proguard-missing-class-error-in-data-class-that-is-using-keep-annotatio
-dontwarn java.lang.invoke.StringConcatFactory
-dontwarn com.knight.kotlin.library_base.R$anim
-dontwarn com.knight.kotlin.library_base.R$color
-dontwarn com.google.android.material.R$id


