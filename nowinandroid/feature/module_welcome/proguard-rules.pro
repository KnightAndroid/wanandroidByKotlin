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
-dontwarn com.core.library_base.util.ActivityManagerUtils$Companion
-dontwarn com.core.library_base.util.ActivityManagerUtils
-dontwarn com.core.library_base.vm.BaseViewModel
-dontwarn com.knight.kotlin.library_base.BaseApp$Companion
-dontwarn com.knight.kotlin.library_base.BaseApp
-dontwarn com.knight.kotlin.library_base.activity.BaseActivity
-dontwarn com.knight.kotlin.library_base.fragment.BaseDialogFragment
-dontwarn com.knight.kotlin.library_network.model.ExceptionHandlerKt
-dontwarn com.knight.kotlin.library_util.TextClickUtils$OnClickToWebListener
-dontwarn com.knight.kotlin.library_util.UtilsKt
-dontwarn com.knight.kotlin.library_util.baidu.OnceLocationListener
-dontwarn com.knight.kotlin.library_widget.AnimLogoView
-dontwarn com.knight.kotlin.library_widget.SimpleProgressBar