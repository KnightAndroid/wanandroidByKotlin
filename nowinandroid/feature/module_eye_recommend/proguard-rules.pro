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
-dontwarn com.knight.kotlin.library_widget.R$layout
-dontwarn com.core.library_base.R$string

-dontwarn com.core.library_base.ktx.CommonExtKt
-dontwarn com.core.library_base.ktx.NumberOrStringSerializer
-dontwarn com.core.library_base.vm.BaseViewModel
-dontwarn com.knight.kotlin.library_base.BaseApp
-dontwarn com.knight.kotlin.library_base.activity.BaseActivity
-dontwarn com.knight.kotlin.library_base.entity.EyeApiRequest
-dontwarn com.knight.kotlin.library_base.entity.EyeCardDataEntity
-dontwarn com.knight.kotlin.library_base.entity.EyeCardEntity
-dontwarn com.knight.kotlin.library_base.entity.EyeCardListEntity
-dontwarn com.knight.kotlin.library_base.entity.EyeCommonAuthor$$serializer
-dontwarn com.knight.kotlin.library_base.entity.EyeCommonAuthor
-dontwarn com.knight.kotlin.library_base.entity.EyeCommonAvatar
-dontwarn com.knight.kotlin.library_base.entity.EyeCommonCover$$serializer
-dontwarn com.knight.kotlin.library_base.entity.EyeCommonCover
-dontwarn com.knight.kotlin.library_base.entity.EyeCommonDuration$$serializer
-dontwarn com.knight.kotlin.library_base.entity.EyeCommonDuration
-dontwarn com.knight.kotlin.library_base.entity.EyeCommonTag$$serializer
-dontwarn com.knight.kotlin.library_base.entity.EyeCommonTag
-dontwarn com.knight.kotlin.library_base.entity.EyeInteraction
-dontwarn com.knight.kotlin.library_base.entity.EyeLayout
-dontwarn com.knight.kotlin.library_base.entity.EyeMetroCard$Companion
-dontwarn com.knight.kotlin.library_base.entity.EyeMetroCard
-dontwarn com.knight.kotlin.library_base.entity.EyeMetroList
-dontwarn com.knight.kotlin.library_base.entity.EyeStyle
-dontwarn com.knight.kotlin.library_base.entity.eye_type.CropArea$$serializer
-dontwarn com.knight.kotlin.library_base.entity.eye_type.CropArea
-dontwarn com.knight.kotlin.library_base.entity.eye_type.EyeVideoCtrl$$serializer
-dontwarn com.knight.kotlin.library_base.entity.eye_type.EyeVideoCtrl
-dontwarn com.knight.kotlin.library_network.model.ExceptionHandlerKt
-dontwarn com.knight.kotlin.library_share.ShareDialog$Companion
-dontwarn com.knight.kotlin.library_share.ShareDialog
-dontwarn com.knight.kotlin.library_util.image.ImageExtKt
-dontwarn com.knight.kotlin.library_widget.databinding.BaseIncludeToolbarBinding
-dontwarn com.knight.kotlin.library_widget.databinding.BaseLayoutRecycleviewBinding
-dontwarn com.knight.kotlin.library_widget.ktx.ViewExtKt
