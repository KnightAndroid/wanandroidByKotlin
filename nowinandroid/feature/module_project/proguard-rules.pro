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
-dontwarn com.core.library_base.R$drawable
-dontwarn com.core.library_base.R$id
-dontwarn com.core.library_base.R$layout

-dontwarn com.core.library_base.annotation.EventBusRegister
-dontwarn com.core.library_base.databinding.BaseArticleItemBinding
-dontwarn com.core.library_base.event.MessageEvent$MessageType
-dontwarn com.core.library_base.event.MessageEvent
-dontwarn com.core.library_base.ktx.CommonExtKt
-dontwarn com.core.library_base.vm.BaseViewModel
-dontwarn com.knight.kotlin.library_base.fragment.BaseFragment
-dontwarn com.knight.kotlin.library_network.model.ExceptionHandlerKt
-dontwarn com.knight.kotlin.library_util.UtilsKt
-dontwarn com.knight.kotlin.library_util.image.ImageLoader$Companion
-dontwarn com.knight.kotlin.library_util.image.ImageLoader
-dontwarn com.knight.kotlin.library_widget.databinding.BaseLayoutRecycleviewBinding




