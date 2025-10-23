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
-dontwarn com.knight.kotlin.library_widget.R$layout

-dontwarn com.core.library_base.ktx.CommonExtKt
-dontwarn com.core.library_base.vm.BaseViewModel
-dontwarn com.core.library_common.ktx.KtxKt
-dontwarn com.knight.kotlin.library_base.activity.BaseActivity
-dontwarn com.knight.kotlin.library_base.entity.EyeAuthorEntity
-dontwarn com.knight.kotlin.library_base.entity.EyeCover
-dontwarn com.knight.kotlin.library_base.entity.EyeDailyItemData
-dontwarn com.knight.kotlin.library_base.entity.EyeDailyItemEntity
-dontwarn com.knight.kotlin.library_base.entity.EyeDailyListEntity
-dontwarn com.knight.kotlin.library_base.entity.EyeData
-dontwarn com.knight.kotlin.library_base.entity.EyeItemEntity
-dontwarn com.knight.kotlin.library_base.entity.EyeTag
-dontwarn com.knight.kotlin.library_network.model.ExceptionHandlerKt
-dontwarn com.knight.kotlin.library_util.DateUtils
-dontwarn com.knight.kotlin.library_util.UtilsKt
-dontwarn com.knight.kotlin.library_util.image.ImageExtKt
-dontwarn com.knight.kotlin.library_widget.databinding.BaseIncludeToolbarBinding