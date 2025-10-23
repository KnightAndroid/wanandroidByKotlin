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
-dontwarn com.core.library_base.R$string

-dontwarn com.core.library_base.ktx.CommonExtKt
-dontwarn com.core.library_base.vm.BaseViewModel
-dontwarn com.knight.kotlin.library_base.activity.BaseActivity
-dontwarn com.knight.kotlin.library_base.entity.EyeAuthorEntity
-dontwarn com.knight.kotlin.library_base.entity.EyeCover
-dontwarn com.knight.kotlin.library_base.entity.EyeData
-dontwarn com.knight.kotlin.library_base.entity.EyeItemEntity
-dontwarn com.knight.kotlin.library_base.fragment.BaseFragment
-dontwarn com.knight.kotlin.library_network.model.ExceptionHandlerKt
-dontwarn com.knight.kotlin.library_util.DateUtils
-dontwarn com.knight.kotlin.library_util.UtilsKt
-dontwarn com.knight.kotlin.library_util.image.ImageExtKt
-dontwarn com.knight.kotlin.library_video.play.OkPlayer$NormalBackListener
-dontwarn com.knight.kotlin.library_video.play.OkPlayerStd
-dontwarn com.knight.kotlin.library_widget.AppendEndTextView
-dontwarn com.knight.kotlin.library_widget.ExpandableTextView
-dontwarn com.knight.kotlin.library_widget.SlowShowTextView
