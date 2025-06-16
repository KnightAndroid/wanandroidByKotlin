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
# aop注解 解决混淆会把注解去掉
-keep @com.knight.kotlin.library_aop.clickintercept.* class * {*;}
-keep @com.knight.kotlin.library_aop.loginintercept.* class * {*;}
-keep class * {
    @com.knight.kotlin.library_aop.clickintercept.* <fields>;
    @com.knight.kotlin.library_aop.loginintercept.* <fields>;
}
-keepclassmembers class * {
    @com.knight.kotlin.library_aop.clickintercept.* <methods>;
    @com.knight.kotlin.library_aop.loginintercept.* <methods>;
}

-keep class * {
    @androidx.annotation.Keep <fields>;
}

#-keepnames class * implements com.flyjingfish.android_aop_annotation.base.BasePointCut
#-keepnames class * implements com.flyjingfish.android_aop_annotation.base.MatchClassMethod
#-keep class * implements com.flyjingfish.android_aop_annotation.base.BasePointCut{
#    public <init>();
#}
#-keep class * implements com.flyjingfish.android_aop_annotation.base.MatchClassMethod{
#    public <init>();
#}
