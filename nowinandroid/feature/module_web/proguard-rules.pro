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
-keep class com.just.agentweb.** {
    *;
}
-dontwarn com.just.agentweb.**

-dontwarn com.core.library_base.R$color
-dontwarn com.core.library_base.R$drawable
-dontwarn com.core.library_base.R$string
-dontwarn com.knight.kotlin.library_widget.R$layout

-dontwarn com.core.library_base.event.MessageEvent$MessageType
-dontwarn com.core.library_base.event.MessageEvent
-dontwarn com.core.library_base.util.EventBusUtils
-dontwarn com.core.library_base.vm.BaseViewModel
-dontwarn com.knight.kotlin.library_base.activity.BaseActivity
-dontwarn com.knight.kotlin.library_base.entity.WebDataEntity
-dontwarn com.knight.kotlin.library_base.fragment.BaseDialogFragment
-dontwarn com.knight.kotlin.library_network.model.ExceptionHandlerKt
-dontwarn com.knight.kotlin.library_util.UtilsKt
-dontwarn com.knight.kotlin.library_util.toast.ToastUtils
-dontwarn com.knight.kotlin.library_widget.LoveAnimatorRelativeLayout$onCollectListener
-dontwarn com.knight.kotlin.library_widget.LoveAnimatorRelativeLayout
-dontwarn com.knight.kotlin.library_widget.TransitWebCoorLayout
-dontwarn com.knight.kotlin.library_widget.WebContainer
-dontwarn com.knight.kotlin.library_widget.databinding.BaseIncludeToolbarBinding