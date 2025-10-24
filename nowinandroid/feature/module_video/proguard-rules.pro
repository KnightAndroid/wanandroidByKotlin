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
-dontwarn com.core.library_base.R$string
-dontwarn com.knight.kotlin.library_widget.R$drawable
-dontwarn com.knight.kotlin.library_widget.R$layout

-dontwarn com.core.library_base.ktx.CommonExtKt
-dontwarn com.core.library_base.vm.BaseViewModel
-dontwarn com.knight.kotlin.library_base.activity.BaseActivity
-dontwarn com.knight.kotlin.library_base.adapter.BaseAdapter
-dontwarn com.knight.kotlin.library_base.entity.EyeAuthorEntity
-dontwarn com.knight.kotlin.library_base.entity.EyeConsumption
-dontwarn com.knight.kotlin.library_base.entity.EyeCover
-dontwarn com.knight.kotlin.library_base.entity.EyeDailyItemData
-dontwarn com.knight.kotlin.library_base.entity.EyeDailyItemEntity
-dontwarn com.knight.kotlin.library_base.entity.EyeDailyListEntity
-dontwarn com.knight.kotlin.library_base.entity.EyeData
-dontwarn com.knight.kotlin.library_base.entity.EyeItemEntity
-dontwarn com.knight.kotlin.library_base.fragment.BaseFragment
-dontwarn com.knight.kotlin.library_network.model.ExceptionHandlerKt
-dontwarn com.knight.kotlin.library_util.AnimUtils
-dontwarn com.knight.kotlin.library_util.DateUtils
-dontwarn com.knight.kotlin.library_util.TimeAgoUtils
-dontwarn com.knight.kotlin.library_util.UtilsKt
-dontwarn com.knight.kotlin.library_util.image.ImageLoader$Companion
-dontwarn com.knight.kotlin.library_util.image.ImageLoader
-dontwarn com.knight.kotlin.library_widget.BaseBottomSheetDialog$IBehaviorChanged
-dontwarn com.knight.kotlin.library_widget.BaseBottomSheetDialog
-dontwarn com.knight.kotlin.library_widget.CircleImageView
-dontwarn com.knight.kotlin.library_widget.IconFontTextView
-dontwarn com.knight.kotlin.library_widget.databinding.BaseIncludeToolbarBinding
-dontwarn com.knight.kotlin.library_widget.databinding.BaseLayoutRecycleviewBinding
-dontwarn com.knight.kotlin.library_widget.linkview.LinkMode
-dontwarn com.knight.kotlin.library_widget.linkview.LinkOnClickListener
-dontwarn com.knight.kotlin.library_widget.linkview.LinkTextView


