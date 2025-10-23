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
-dontwarn com.core.library_base.R$dimen
-dontwarn com.core.library_base.R$string
-dontwarn com.knight.kotlin.library_widget.R$style

-dontwarn com.core.library_base.event.MessageEvent$MessageType
-dontwarn com.core.library_base.event.MessageEvent
-dontwarn com.core.library_base.util.EventBusUtils
-dontwarn com.core.library_base.vm.BaseViewModel
-dontwarn com.core.library_common.util.ColorUtils
-dontwarn com.knight.kotlin.library_base.activity.BaseActivity
-dontwarn com.knight.kotlin.library_common.util.LanguageFontSizeUtils
-dontwarn com.knight.kotlin.library_network.model.ExceptionHandlerKt
-dontwarn com.knight.kotlin.library_util.UtilsKt
-dontwarn com.knight.kotlin.library_widget.ChangeSizeView$OnChangeCallbackListener
-dontwarn com.knight.kotlin.library_widget.ChangeSizeView
-dontwarn com.knight.kotlin.library_widget.ColorPickerView$OnColorChangedListener
-dontwarn com.knight.kotlin.library_widget.ColorPickerView
-dontwarn com.knight.kotlin.library_widget.GestureLockView$OnSetPasswordListener
-dontwarn com.knight.kotlin.library_widget.GestureLockView
-dontwarn com.knight.kotlin.library_widget.RippleAnimation$Companion
-dontwarn com.knight.kotlin.library_widget.RippleAnimation
-dontwarn com.knight.kotlin.library_widget.databinding.BaseIncludeToolbarBinding
-dontwarn com.knight.kotlin.library_widget.databinding.BaseLayoutRecycleviewBinding
