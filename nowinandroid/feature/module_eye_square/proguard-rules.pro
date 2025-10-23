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
-dontwarn com.knight.kotlin.library_base.entity.EyeApiRequest
-dontwarn com.knight.kotlin.library_base.entity.EyeCardDataEntity
-dontwarn com.knight.kotlin.library_base.entity.EyeCardEntity
-dontwarn com.knight.kotlin.library_base.entity.EyeCardListEntity
-dontwarn com.knight.kotlin.library_base.entity.EyeCommonAuthor
-dontwarn com.knight.kotlin.library_base.entity.EyeCommonAvatar
-dontwarn com.knight.kotlin.library_base.entity.EyeCommonConsumption
-dontwarn com.knight.kotlin.library_base.entity.EyeCommonCover
-dontwarn com.knight.kotlin.library_base.entity.EyeCommonImageInfo
-dontwarn com.knight.kotlin.library_base.entity.EyeCommonTopic
-dontwarn com.knight.kotlin.library_base.entity.EyeMetroCard
-dontwarn com.knight.kotlin.library_base.entity.EyeMetroList
-dontwarn com.knight.kotlin.library_base.entity.eye_type.EyeBannerImageList$Companion
-dontwarn com.knight.kotlin.library_base.entity.eye_type.EyeBannerImageList
-dontwarn com.knight.kotlin.library_base.entity.eye_type.EyeBannerSquare
-dontwarn com.knight.kotlin.library_base.entity.eye_type.EyeFeedItemDetail$Companion
-dontwarn com.knight.kotlin.library_base.entity.eye_type.EyeFeedItemDetail
-dontwarn com.knight.kotlin.library_base.entity.eye_type.EyeVideo
-dontwarn com.knight.kotlin.library_base.entity.eye_type.EyeWaterFallCoverVideoImage$Companion
-dontwarn com.knight.kotlin.library_base.entity.eye_type.EyeWaterFallCoverVideoImage
-dontwarn com.knight.kotlin.library_base.ktx.CommonBaseExtKt
-dontwarn com.knight.kotlin.library_network.model.ExceptionHandlerKt
-dontwarn com.knight.kotlin.library_util.image.ImageExtKt
-dontwarn com.knight.kotlin.library_util.image.ImageLoader$Companion
-dontwarn com.knight.kotlin.library_util.image.ImageLoader
-dontwarn com.knight.kotlin.library_widget.databinding.BaseIncludeToolbarBinding
-dontwarn com.knight.kotlin.library_widget.ktx.ViewExtKt